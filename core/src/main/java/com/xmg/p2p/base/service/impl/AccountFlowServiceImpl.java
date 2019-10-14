package com.xmg.p2p.base.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmg.p2p.base.domain.Account;
import com.xmg.p2p.base.domain.AccountFlow;
import com.xmg.p2p.base.domain.RechargeOffline;
import com.xmg.p2p.base.mapper.AccountFlowMapper;
import com.xmg.p2p.base.service.IAccountFlowService;
import com.xmg.p2p.base.service.IAccountService;
import com.xmg.p2p.base.util.BidConst;
import com.xmg.p2p.business.domain.Bid;
import com.xmg.p2p.business.domain.BidRequest;
/**
 * 账户流水服务
 * @author Dxd
 *
 */
@Service
public class AccountFlowServiceImpl implements IAccountFlowService {

	@Autowired
	private AccountFlowMapper accountFlowMapper;
	@Autowired
	private IAccountService accountService;
	
	/**
	 * 创建一条账户流水
	 */
	@Override
	public void rechargeFlow(RechargeOffline rechargeOffline, AccountFlow af) {
		//当前被审核的充值单是哪一个账户的
		Account account = this.accountService.get(rechargeOffline.getApplier().getId());
		af.setAccountId(account.getId());
		af.setAccountType(BidConst.ACCOUNT_ACTIONTYPE_RECHARGE_OFFLINE);
		af.setAmount(rechargeOffline.getAmount());
		af.setNote("该次账户流水单金额为"+af.getAmount());
		af.setTradeTime(new Date());
		//当前这个充值单的账户可用金额增加
		account.setUsableAmount(account.getUsableAmount().add(rechargeOffline.getAmount()));
		af.setUsableAmount(account.getUsableAmount());
		af.setFreezedAmount(account.getFreezedAmount());
		
		this.accountService.update(account);
		this.accountFlowMapper.insert(af);
	}

	/**
	 * 投标流水
	 */
	@Override
	public void bidFlow(Bid bid, Account account) {
		//生成一条新的账户流水
		AccountFlow af = new AccountFlow();
		af.setAccountId(account.getId());//当前投标的账户
		af.setAmount(bid.getAvailableAmount());//投标金额
		af.setAccountType(BidConst.ACCOUNT_ACTIONTYPE_BID_FREEZED);//投标成功，可用余额增加，冻结金额减少
		af.setFreezedAmount(account.getFreezedAmount());
		af.setNote("所投的标是:"+bid.getBidRequestTitle()+",金额为："+bid.getAvailableAmount());
		af.setTradeTime(new Date());//交易时间
		af.setUsableAmount(account.getUsableAmount());//账户可用余额
		
		this.accountFlowMapper.insert(af);//新增账户流水
	}

	/**
	 * 记录钱返还给投标用户的账户流水
	 */
	@Override
	public void montyReturnAccountFlow(Bid bid, Account account) {
		AccountFlow af = new AccountFlow();
		af.setAccountId(account.getId());
		af.setAccountType(BidConst.ACCOUNT_ACTIONTYPE_BID_UNFREEZED);
		af.setAmount(bid.getAvailableAmount());
		af.setFreezedAmount(account.getFreezedAmount());
		af.setNote("所投的标"+bid.getBidRequestTitle()+"返还的金额为:"+bid.getAvailableAmount());
		af.setTradeTime(new Date());
		af.setUsableAmount(account.getUsableAmount());
		
		this.accountFlowMapper.insert(af);
	}

	//借款成功账户流水
	@Override
	public void borrowSuccess(BidRequest currentBidRequest, Account borrowAccount) {
		AccountFlow flow = new AccountFlow();
		flow.setAccountId(borrowAccount.getId());
		flow.setAccountType(BidConst.ACCOUNT_ACTIONTYPE_BIDREQUEST_SUCCESSFUL);
		flow.setAmount(currentBidRequest.getBidRequestAmount());
		flow.setFreezedAmount(borrowAccount.getFreezedAmount());
		flow.setNote(currentBidRequest.getTitle()+"标，成功借款："+currentBidRequest.getBidRequestAmount());
		flow.setTradeTime(new Date());
		flow.setUsableAmount(borrowAccount.getUsableAmount());
		this.accountFlowMapper.insert(flow);
	}

	//付给平台管理费的账户流水
	@Override
	public void borrowChargeFee(BidRequest currentBidRequest, Account borrowAccount,BigDecimal manageChargeFee) {
		AccountFlow flow = new AccountFlow();
		flow.setAccountId(borrowAccount.getId());
		flow.setAccountType(BidConst.ACCOUNT_ACTIONTYPE_CHARGE);
		flow.setAmount(manageChargeFee);
		flow.setFreezedAmount(borrowAccount.getFreezedAmount());
		flow.setNote("借款："+currentBidRequest.getTitle()+"，支付平台管理费："+manageChargeFee);
		flow.setTradeTime(new Date());
		flow.setUsableAmount(borrowAccount.getUsableAmount());
		this.accountFlowMapper.insert(flow);
	}

	//投标成功,冻结金额减少,生成流水
	@Override
	public void createReturnBidFlow(Bid bid, Account account) {
		AccountFlow flow = new AccountFlow();
		flow.setAccountId(account.getId());
		flow.setAccountType(BidConst.ACCOUNT_ACTIONTYPE_BID_SUCCESSFUL);
		flow.setAmount(bid.getAvailableAmount());
		flow.setFreezedAmount(account.getFreezedAmount());
		flow.setNote("投标"+bid.getBidRequestTitle()+"成功，冻结金额减少："+bid.getAvailableAmount());
		flow.setTradeTime(new Date());
		flow.setUsableAmount(account.getUsableAmount());
		this.accountFlowMapper.insert(flow);
	}

}
