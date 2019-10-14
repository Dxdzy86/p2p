package com.xmg.p2p.business.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmg.p2p.base.domain.Account;
import com.xmg.p2p.base.domain.LoginInfo;
import com.xmg.p2p.base.domain.PaymentSchedule;
import com.xmg.p2p.base.domain.PaymentScheduleDetail;
import com.xmg.p2p.base.domain.UserInfo;
import com.xmg.p2p.base.mapper.LoginInfoMapper;
import com.xmg.p2p.base.mapper.PaymentScheduleDetailMapper;
import com.xmg.p2p.base.mapper.PaymentScheduleMapper;
import com.xmg.p2p.base.query.PageResult;
import com.xmg.p2p.base.service.IAccountFlowService;
import com.xmg.p2p.base.service.IAccountService;
import com.xmg.p2p.base.service.ISystemAccountService;
import com.xmg.p2p.base.service.IUserInfoService;
import com.xmg.p2p.base.util.BidConst;
import com.xmg.p2p.base.util.BitStatesUtil;
import com.xmg.p2p.base.util.UserContext;
import com.xmg.p2p.business.domain.Bid;
import com.xmg.p2p.business.domain.BidRequest;
import com.xmg.p2p.business.domain.BidRequestAuditHistory;
import com.xmg.p2p.business.mapper.BidMapper;
import com.xmg.p2p.business.mapper.BidRequestAuditHistoryMapper;
import com.xmg.p2p.business.mapper.BidRequestMapper;
import com.xmg.p2p.business.query.BidRequestQueryObject;
import com.xmg.p2p.business.service.IBidRequestService;
import com.xmg.p2p.business.util.CalculatetUtil;
/**
 * 申请借款服务类
 * @author Dxd
 *
 */
@Service
public class BidRequestServiceImpl implements IBidRequestService{
	@Autowired
	private BidRequestMapper bidRequestMapper;
	@Autowired
	private IUserInfoService userInfoService;
	@Autowired
	private LoginInfoMapper loginInfoMapper;
	@Autowired
	private IAccountService accountService;
	@Autowired
	private BidRequestAuditHistoryMapper bidRequestAuditHistoryMapper;
	@Autowired
	private BidMapper bidMapper;
	@Autowired
	private IAccountFlowService accountFlowService;
	@Autowired
	private ISystemAccountService systemAccountService;
	@Autowired
	private PaymentScheduleMapper paymentScheduleMapper;
	@Autowired
	private PaymentScheduleDetailMapper paymentScheduleDetailMapper; 
	
	@Override
	public void update(BidRequest bidRequest) {
		int count = this.bidRequestMapper.updateByPrimaryKey(bidRequest);
		if(count == 0) {
			throw new RuntimeException("乐观锁失败");
		}
	}
	//判断借款用户是否满足借款条件
	public boolean canApplyBidRequest(LoginInfo loginInfo) {
		UserInfo userInfo = this.userInfoService.get(loginInfo.getId());
		if(userInfo.getHasSaveBasicInfo()//用户已经填写基本资料
				&& userInfo.getHasIdentityAuth()//用户已经身份认证
				&& userInfo.getHasVedioAuth()//用户已经视频认证
				&& userInfo.getCreditBorrowScore() >= BitStatesUtil.CREDIT_BORROW_SCORES//风控材料认证分数达到30以上
				&& !userInfo.getHasBidRequestInProcess()//用户没有借款在整个平台中
				) {
			return true;
		}
		return false;
	}
	
	/**
	 * 对标的内容进行验证，看用户写的参数是否符合平台规范
	 *
	 */
	@Override
	public void applyBidRequest(BidRequest bidRequest) {
		//对前台传入的数据进行验证
		
		Account account = this.accountService.get(UserContext.getCurrentUser().getId());//当前用户的账户
		if(bidRequest.getBidRequestAmount().compareTo(BidConst.SMALLEST_BIDREQUEST_AMOUNT)>=0
				&& bidRequest.getBidRequestAmount().compareTo(account.getRemainBorrowLimit())<=0//系统最小借款金额<=最小借款金额<=剩余授信额度
				&& bidRequest.getCurrentRate().compareTo(BidConst.SMALLEST_CURRENT_RATE)>=0
				&& bidRequest.getCurrentRate().compareTo(BidConst.MAX_CURRENT_RATE)<=0//系统最小年化利率<=年化利率<=系统最大年化利率
				&& bidRequest.getMinBidAmount().compareTo(BidConst.SMALLEST_BID_AMOUNT)>=0//系统最小投标金额<=投标金额
				) {
			BidRequest br = new BidRequest();//创建一个新的借款对象
			//借款对象属性相关设置
			br.setBidRequestAmount(bidRequest.getBidRequestAmount());
			br.setCurrentRate(bidRequest.getCurrentRate());
			br.setMonthes2Return(bidRequest.getMonthes2Return());
			br.setReturnType(bidRequest.getReturnType());
			br.setMinBidAmount(bidRequest.getMinBidAmount());
			br.setDisableDays(bidRequest.getDisableDays());
			br.setTitle(bidRequest.getTitle());
			br.setDescription(bidRequest.getDescription());
			//额外的属性设置
			br.setApplyTime(new Date());//标的申请时间
			br.setCreateUser(UserContext.getCurrentUser());//标的申请人
			br.setTotalRewardAmount(CalculatetUtil.calTotalInterest(br.getReturnType(),br.getBidRequestAmount(),
					br.getCurrentRate(),br.getDisableDays()));//计算总的回报金额
			
			br.setBidRequestState(BidConst.BIDREQUEST_STATE_PUBLISH_PENDING);//设置标的状态(待发布状态)
			
			UserInfo userInfo = this.userInfoService.get(UserContext.getCurrentUser().getId());
			//当前用户信息对象绑定一个表示该用户有一个标在平台中的状态码(通过该验证码，我们就可以知道用户是否有申请标，防止用户重复申请借款)
			userInfo.setBitState(BitStatesUtil.addState(userInfo.getBitState(),BitStatesUtil.OP_BID_REQUEST_PROCESS));//该平台中当前用户有一个标
			
			this.bidRequestMapper.insert(br);
			this.userInfoService.update(userInfo);
		}
		
	}
	
	//高级查询相关
	@Override
	public PageResult query(BidRequestQueryObject qo) {
		if(qo.getBidRequestState() == -1) {
			qo.setBidRequestStatesArray(new int[] {
					BidConst.BIDREQUEST_STATE_BIDDING,//招标中
					BidConst.BIDREQUEST_STATE_COMPLETE_PAY_BACK//已还清
			});
		}
		int totalCount = this.bidRequestMapper.queryBidRequestCount(qo);
		
		if(totalCount > 0) {
			List<BidRequest> data = this.bidRequestMapper.query(qo);
			return new PageResult(data,totalCount,qo.getCurrentPage(),qo.getPageSize());
		}
		
		return PageResult.empty();
	}
	
	//发标审核通过或拒绝逻辑
	@Override
	public void publishAudit(Long id, int state, String remark) {
		//从数据库中查询出对应的借款对象记录
		BidRequest bidRequest = this.bidRequestMapper.selectByPrimaryKey(id);
		//检查该借款对象是否是待发布状态
		if(bidRequest != null && bidRequest.getBidRequestState() == BidConst.BIDREQUEST_STATE_PUBLISH_PENDING) {
			//BidRequestAuditHistory类型的对象负责每一个需要审核的标的相关操作
			BidRequestAuditHistory history = new BidRequestAuditHistory();
			history.setState(state);//保存标的审核状态
			history.setApplier(bidRequest.getCreateUser());
			history.setAuditor(UserContext.getCurrentUser());
			history.setApplyTime(bidRequest.getApplyTime());
			history.setAuditTime(new Date());
			history.setRemark(remark);
			history.setBidRequestId(id);//审核的是哪一个标
			history.setAuditType(BidRequestAuditHistory.PUBLISH_AUDIT);//发标审核状态
			this.bidRequestAuditHistoryMapper.insert(history);//插入审核记录
			
			//如果是审核通过
			if(history.getState() == BidRequestAuditHistory.STATE_AUDIT) {
				//设置前台传入的属性值
				bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_BIDDING);//招标中
				bidRequest.setNote(remark);//风控评审意见
				//招标截至日期
				bidRequest.setDisableDate(DateUtils.addDays(new Date(), bidRequest.getDisableDays()));
				bidRequest.setPublishTime(new Date());
			}else {
				//如果是审核拒绝
				bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_PUBLISH_REFUSE);//发标失败
				bidRequest.setNote(remark);
				//移除用户基本信息中关于标的状态码
				UserInfo userInfo = this.userInfoService.get(bidRequest.getCreateUser().getId());
				userInfo.setBitState(userInfo.removeBitState(BitStatesUtil.OP_BID_REQUEST_PROCESS));
				this.userInfoService.update(userInfo);
			}
			this.bidRequestMapper.updateByPrimaryKey(bidRequest);
		}
	}
	
	@Override
	public BidRequest get(Long id) {
		return this.bidRequestMapper.selectByPrimaryKey(id);
	}
	//查询首页显示的标
	@Override
	public List<BidRequest> queryIndexBidRequests(int pageSize) {
		//查询出(投标中；还款中；已完成的标) 这3种标，按照投标中>还款中>已完成的标排序，显示5条标的记录
		
		//这里我们将条件封装进BidRequestQueryObject对象中，使用起来更灵活
		BidRequestQueryObject qo = new BidRequestQueryObject();
		qo.setBidRequestStatesArray(new int[] {
				BidConst.BIDREQUEST_STATE_BIDDING,//招标中
				BidConst.BIDREQUEST_STATE_PAYING_BACK,//还款中
				BidConst.BIDREQUEST_STATE_COMPLETE_PAY_BACK//已还清
		});
		qo.setPageSize(pageSize);
		qo.setOrderBy("bidRequestState");
		qo.setOrderByType("ASC");
		
		return this.bidRequestMapper.query(qo);
	}
	
	/**
	 * 用户投标
	 */
	@Override
	public void bid(Long bidRequestId, BigDecimal amount) {
		//得到当前用户所投的是那个标
		BidRequest currentBidRequest = bidRequestMapper.selectByPrimaryKey(bidRequestId);
		//判断标是否存在以及当前标是否是招标中状态
		if(currentBidRequest != null && currentBidRequest.getBidRequestState()==BidConst.BIDREQUEST_STATE_BIDDING) {
			//当前用户的账户
			Account currentAccount = this.accountService.get(UserContext.getCurrentUser().getId());
			
			if(amount.compareTo(BidConst.SMALLEST_BID_AMOUNT)>=0//投标金额大于系统最小投标金额
					&& amount.compareTo(currentAccount.getUsableAmount())<=0//投标金额小于当前用户可用余额
					&& amount.compareTo(currentBidRequest.getBidRequestAmount())<=0//投标金额小于借款标金额
					&& !currentBidRequest.getCreateUser().getId().equals(UserContext.getCurrentUser().getId())//当前投标用户不是所投标的拥有者
					) {
				//创建一个投标对象,修改投标相关属性
				Bid bid = new Bid();
				bid.setActualRate(currentBidRequest.getCurrentRate());
				bid.setAvailableAmount(amount);
				bid.setBidRequestId(bidRequestId);//当前投的是哪一个标
				bid.setBidRequestTitle(currentBidRequest.getTitle());
				bid.setBidTime(new Date());
				bid.setBidUser(this.loginInfoMapper.selectByPrimaryKey(currentAccount.getId()));
				this.bidMapper.insert(bid);//插入到数据库中
				
				//所投的标发生变化
				currentBidRequest.setBidCount(currentBidRequest.getBidCount()+1);//当前借款的投标次数加一
				currentBidRequest.setCurrentSum(currentBidRequest.getCurrentSum().add(amount));//当前被投金额增加
				
				//当前用户的账户发生改变
				currentAccount.setUsableAmount(currentAccount.getUsableAmount().subtract(amount));//当前用户账户可用余额减少
				currentAccount.setFreezedAmount(currentAccount.getFreezedAmount().add(amount));//当前用户账户冻结金额增加
				this.accountService.update(currentAccount);//更新当前用户的账户信息
				
				//生成一条投标流水线
				this.accountFlowService.bidFlow(bid,currentAccount);
				
				//如果当前所有投的钱等于这个标需要的借款，更新这个标的状态为满标一审
				if(currentBidRequest.getCurrentSum().equals(currentBidRequest.getBidRequestAmount())) {
					currentBidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1);
				}
				//更新所投的标到数据库中
				this.bidRequestMapper.updateByPrimaryKey(currentBidRequest);
			}
		}
		
	}
	
	/**
	 * 后台进行满标一审
	 */
	@Override
	public void fullAudit1(Long bidRequestId, int state, String remark) {
		//得到当前需要进行审核的标
		BidRequest currentBidRequest = this.bidRequestMapper.selectByPrimaryKey(bidRequestId);
		//判这个标是否存在以及是否处于满标一审的状态
		if(currentBidRequest != null && currentBidRequest.getBidRequestState() == BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1) {
			//创建一个标审核历史对象
			BidRequestAuditHistory history = new BidRequestAuditHistory();
			//设置该审核历史对象的属性
			history.setApplier(currentBidRequest.getCreateUser());//标的申请人是谁
			history.setApplyTime(currentBidRequest.getApplyTime());//标的申请时间
			history.setAuditor(UserContext.getCurrentUser());//当前标的审核人
			history.setAuditTime(new Date());//审核时间
			history.setAuditType(currentBidRequest.getBidRequestState());//所审核的标的状态(当前的标是什么类型的标：满标一审or满标二审or发标审核)
			history.setBidRequestId(bidRequestId);//审核的是哪一个标
			history.setRemark(remark);//审核备注
			history.setState(state);//审核通过or审核拒绝
			//插入审核历史记录到数据库中
			this.bidRequestAuditHistoryMapper.insert(history);
			
			//如果当前标审核失败
			if(state == history.STATE_REJECT) {
				//将当前审核的标的状态(bidRequestState)设置满标审核被拒
				currentBidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_REJECTED);
				//这个标的发出者需要移除借款状态
				UserInfo currentUserInfo = this.userInfoService.get(currentBidRequest.getCreateUser().getId());
				currentUserInfo.removeBitState(BitStatesUtil.OP_BID_REQUEST_PROCESS);
				this.userInfoService.update(currentUserInfo);
				//投入到这个标的钱需要返还给各个投标用户(冻结金额减少，可用金额增加)
				returnMoney(currentBidRequest);
			}else {//审核成功
				//将当前审核的标的状态设置为满标二审
				currentBidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2);
			}
			//风控评审意见设为“满标一审拒绝”或者“满标一审通过”
			currentBidRequest.setNote(remark);
			this.bidRequestMapper.updateByPrimaryKey(currentBidRequest);
		}
		
	}
	
	/**
	 * 后台进行满标二审审核
	 */
	@Override
	public void fullAudit2(Long id, int state, String remark) {
		//当前审核的是哪一个标,判断是否是满标二审状态
		BidRequest currentBidRequest = this.get(id);
		if(currentBidRequest != null 
				&& currentBidRequest.getBidRequestState()==BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2) {
			//创建一个标的审核历史对象
			BidRequestAuditHistory history = new BidRequestAuditHistory();
			//设置审核历史对象的相关属性
			history.setApplier(currentBidRequest.getCreateUser());
			history.setApplyTime(currentBidRequest.getApplyTime());
			history.setAuditor(UserContext.getCurrentUser());
			history.setAuditTime(new Date());
			history.setAuditType(currentBidRequest.getBidRequestState());//所审核的标现在是什么状态
			history.setBidRequestId(id);
			history.setRemark(remark);
			history.setState(state);
			
			this.bidRequestAuditHistoryMapper.insert(history);
			//如果审核通过
			if(state == BidRequestAuditHistory.STATE_AUDIT) {
				//1:借款对象有什么变化
				//1.1:修改借款状态:进入还款中
				currentBidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_PAYING_BACK);
				//1.2:修改投标状态(先不写)
				
				//2:借款人(借款人的账户)有什么变化
				Account borrowAccount = this.accountService.get(currentBidRequest.getCreateUser().getId());
				//2.1:借款成功,可用金额增加,生成流水
				borrowAccount.setUsableAmount(borrowAccount.getUsableAmount().add(currentBidRequest.getBidRequestAmount()));
				this.accountFlowService.borrowSuccess(currentBidRequest,borrowAccount);
				//2.2:剩余授信额度减小
				borrowAccount.setRemainBorrowLimit(borrowAccount.getRemainBorrowLimit().subtract(currentBidRequest.getBidRequestAmount()));
				//2.3:待还金额增加(待还本金+待还利息)
				borrowAccount.setUnReturnAmount(borrowAccount.getUnReturnAmount()
						.add(currentBidRequest.getBidRequestAmount())
						.add(currentBidRequest.getTotalRewardAmount()));
				//2.4:移除借款用户处于借款中的状态
				UserInfo userInfo = this.userInfoService.get(currentBidRequest.getCreateUser().getId());
				userInfo.removeBitState(BitStatesUtil.OP_BID_REQUEST_PROCESS);
				this.userInfoService.update(userInfo);
				//2.5:支付平台借款管理费,生成流水
				BigDecimal manageChargeFee = CalculatetUtil.calAccountManagementCharge(currentBidRequest.getBidRequestAmount());
				borrowAccount.setUsableAmount(borrowAccount.getUsableAmount().subtract(manageChargeFee));
				this.accountFlowService.borrowChargeFee(currentBidRequest,borrowAccount,manageChargeFee);
				//更新借款人
				this.accountService.update(borrowAccount);
				//2.6:平台系统账户收取管理手续费,生成系统账户流水
				this.systemAccountService.chargeManageFee(manageChargeFee,currentBidRequest);
				//3:投资人有什么变化
				//3.1:遍历每一个投标
				Map<Long,Account> map = new HashMap<>();
				for(Bid bid:currentBidRequest.getBids()) {
					Account account = map.get(bid.getId());
					if(account == null) {
						account = accountService.get(bid.getId());
						map.put(bid.getId(), account);
					}
					//3.2:投标成功,冻结金额减少,生成流水
					account.setFreezedAmount(account.getFreezedAmount().subtract(bid.getAvailableAmount()));
					this.accountFlowService.createReturnBidFlow(bid,account);
				}
				
				//4:还款怎么做
				//4.1:生成对应的还款计划对象以及还款明细对象
				List<PaymentSchedule> list = createPaymentSchedules(currentBidRequest);
				//3.3:计算待收本金和待收利息(最后做)
				for(PaymentSchedule ps:list) {
					for(PaymentScheduleDetail psd:ps.getDetails()) {
						Account account = map.get(psd.getInvestorId());
						if(account == null) {
							map.put(psd.getInvestorId(), this.accountService.get(psd.getInvestorId()));
						}
						account.setUnReceivePrincipal(account.getUnReceivePrincipal().add(psd.getPrincipal()));
						account.setUnReceiveInterest(account.getUnReceiveInterest().add(psd.getInterest()));
					}
				}
				//将所有投标人的账户进行更新
				for(Account a:map.values()) {
					this.accountService.update(a);
				}
				
			}else {//审核拒绝
				//设置标的状态为满标审核被拒绝
				currentBidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_REJECTED);
				//移除当前标所属的用户有标正在流程中的状态码
				UserInfo userInfo = this.userInfoService.get(currentBidRequest.getCreateUser().getId());
				userInfo.removeBitState(BitStatesUtil.OP_BID_REQUEST_PROCESS);
				this.userInfoService.update(userInfo);
				//将投的钱退给投标用户
				returnMoney(currentBidRequest);
			}
		}
		//风控评审意见设为“满标一审拒绝”或者“满标一审通过”
		currentBidRequest.setNote(remark);
		this.bidRequestMapper.updateByPrimaryKey(currentBidRequest);
	}
	
	//针对某个用户借款生成对应的还款计划对象以及还款明细对象
	private List<PaymentSchedule> createPaymentSchedules(BidRequest currentBidRequest) {
		List<PaymentSchedule> list = new ArrayList<>();
		int monthes2Return = currentBidRequest.getMonthes2Return();//还款期限
		Date now = new Date();//满标二审通过后，才是真正的发标时间
		BigDecimal totalAmount = BidConst.ZERO;
		BigDecimal totalInterest = BidConst.ZERO;
		for(int i=0;i<monthes2Return;i++) {
			PaymentSchedule ps = new PaymentSchedule();
			list.add(ps);
			
		    ps.setDeadLine(DateUtils.addMonths(now,i+1));//本期还款截止日期
		    ps.setMonthIndex(i+1);//第几期还款(第几个月)
		    ps.setState(BidConst.PAYMENT_STATE_NORMAL);//本期还款状态
		    ps.setBidRequestId(currentBidRequest.getId());//借款对象
		    ps.setBorrowUser(currentBidRequest.getCreateUser());//借款人/还款人
		    ps.setBidRequestTitle(currentBidRequest.getTitle());//借款标题
		    ps.setBidRequestType(currentBidRequest.getBidRequestType());//借款类型
		    ps.setReturnType(currentBidRequest.getReturnType());//还款方式
		    
		    if(i < monthes2Return-1) {
		    	ps.setTotalAmount(CalculatetUtil.calMonthToReturnMoney(currentBidRequest.getReturnType(),
			    		currentBidRequest.getBidRequestAmount(),currentBidRequest.getCurrentRate(),
			    		i+1,monthes2Return));//本期还款金额
			    ps.setInterest(CalculatetUtil.calMonthlyInterest(currentBidRequest.getReturnType(), 
			    		currentBidRequest.getBidRequestAmount(),currentBidRequest.getCurrentRate(),
			    		i+1, monthes2Return));//本期还款利息
			    ps.setPrincipal(ps.getTotalAmount().subtract(ps.getInterest()));//本期还款本金
			    
			    totalAmount = totalAmount.add(ps.getTotalAmount());
			    totalInterest = totalInterest.add(ps.getInterest());
		    }else {
		    	ps.setTotalAmount(currentBidRequest.getBidRequestAmount().subtract(totalAmount));
		    	ps.setInterest(currentBidRequest.getTotalRewardAmount().subtract(totalInterest));
		    	ps.setPrincipal(ps.getTotalAmount().subtract(ps.getInterest()));
		    }
		    //插入还款计划到数据库中
		    paymentScheduleMapper.insert(ps);
		    //还款明细
		    createPaymentScheduleDetails(ps,currentBidRequest);
		}
		return list;
	}
	
	//针对每一个还款计划对象生成对应的还款明细
	private void createPaymentScheduleDetails(PaymentSchedule ps, BidRequest currentBidRequest) {
		BigDecimal principal = BidConst.ZERO;
		BigDecimal interest = BidConst.ZERO;
		for(int i=0;i<currentBidRequest.getBids().size();i++) {
			PaymentScheduleDetail psd = new PaymentScheduleDetail();
			Bid bid = currentBidRequest.getBids().get(i);
			psd.setBidAmount(psd.getBidAmount().add(bid.getAvailableAmount()));//该投标人总共投标金额,便于还款/垫付查询
		    psd.setBidId(bid.getId());//投标对象
		    psd.setPaymentScheduleId(ps.getId());//所属还款计划
		    psd.setBorrowUser(currentBidRequest.getCreateUser());//发标人/还款人
		    psd.setInvestorId(bid.getBidUser().getId());//投资人(收款人) investorId
		    psd.setBidRequestId(currentBidRequest.getId());//借款对象
		    psd.setReturnType(currentBidRequest.getReturnType());//还款方式
		    psd.setDeadLine(ps.getDeadLine());//本期还款截止日期
		    psd.setMonthIndex(ps.getMonthIndex());//第几期还款(第几个月)
		    
		    if(i<currentBidRequest.getBids().size()-1) {
		    	psd.setPrincipal(bid.getAvailableAmount().divide(currentBidRequest.getBidRequestAmount(),BidConst.CAL_SCALE, RoundingMode.HALF_UP).
			    		multiply(ps.getPrincipal()).setScale(BidConst.STORE_SCALE, RoundingMode.HALF_UP));//本期还款本金
			    psd.setInterest(bid.getAvailableAmount().divide(currentBidRequest.getBidRequestAmount(),BidConst.CAL_SCALE,RoundingMode.HALF_UP).
			    		multiply(ps.getInterest()).setScale(BidConst.STORE_SCALE,RoundingMode.HALF_UP));//本期还款利息
			    psd.setTotalAmount(psd.getTotalAmount().add((psd.getPrincipal().add(psd.getInterest()))));//本期还款金额(本金+利息)
			    principal = principal.add(psd.getPrincipal());
			    interest = interest.add(psd.getInterest());
		    }else {
		    	psd.setPrincipal(ps.getPrincipal().subtract(principal));
		    	psd.setInterest(ps.getInterest().subtract(interest));
		    	psd.setTotalAmount(psd.getPrincipal().add(psd.getInterest()));
		    }
		    paymentScheduleDetailMapper.insert(psd);
		    ps.getDetails().add(psd);
		}
	}
	
	/**
	 * 满标审核被拒返还投标用户的钱
	 * @param currentBidRequest
	 */
	private void returnMoney(BidRequest currentBidRequest) {
		Map<Long,Account> map = new HashMap<>();
		//得到当前标的所有投标用户集合
		List<Bid> bids = currentBidRequest.getBids();
		for(Bid bid : bids) {
			Account account = map.get(bid.getBidUser().getId());//得到当前投标用户的账户
			if(account == null) {//如果map里不存在投标用户投的标
				account = this.accountService.get(bid.getBidUser().getId());//得到投标用户的账户
				//将所投的标和投标用户的账户以键值对的形式放入map中
				map.put(account.getId(), account);
			}
			//投标用户的冻结金额减少，可用金额增加
			account.setFreezedAmount(account.getFreezedAmount().subtract(bid.getAvailableAmount()));
			account.setUsableAmount(account.getUsableAmount().add(bid.getAvailableAmount()));
			//生成一条账户流水
			this.accountFlowService.montyReturnAccountFlow(bid,account);
		}
		
		for(Account account:map.values()) {
			this.accountService.update(account);
		}
	}
	
	
}
