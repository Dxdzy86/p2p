package com.xmg.p2p.base.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmg.p2p.base.domain.AccountFlow;
import com.xmg.p2p.base.domain.RechargeOffline;
import com.xmg.p2p.base.mapper.RechargeOfflineMapper;
import com.xmg.p2p.base.query.PageResult;
import com.xmg.p2p.base.query.RechargeOfflineQueryObject;
import com.xmg.p2p.base.service.IAccountFlowService;
import com.xmg.p2p.base.service.IPlatformBankInfoService;
import com.xmg.p2p.base.service.IRechargeOfflineService;
import com.xmg.p2p.base.util.UserContext;
/**
 * 线下充值服务相关
 * @author Dxd
 *
 */
@Service
public class RechargeOfflineServiceImpl implements IRechargeOfflineService {
	@Autowired
	private RechargeOfflineMapper rechargeOfflineMapper;
	@Autowired
	private IPlatformBankInfoService platformBankInfoService;
	@Autowired
	private IAccountFlowService accountFlowService;
	
	/**
	 * 前台传过来的线下充值详情
	 */
	@Override
	public void apply(RechargeOffline rechargeOffline) {
		RechargeOffline ro = new RechargeOffline();
		ro.setAmount(rechargeOffline.getAmount());
		ro.setApplier(UserContext.getCurrentUser());
		ro.setApplyTime(new Date());
		ro.setBankInfo(this.platformBankInfoService.get(rechargeOffline.getBankInfo().getId()));
		ro.setNote(rechargeOffline.getNote());
		ro.setTradeCode(rechargeOffline.getTradeCode());
		ro.setTradeTime(rechargeOffline.getTradeTime());
		this.rechargeOfflineMapper.insert(ro);
	}

	/**
	 * 高级查询
	 */
	@Override
	public PageResult query(RechargeOfflineQueryObject qo) {
		int totalCount = this.rechargeOfflineMapper.queryRechargeOfflineCount(qo);
		
		if(totalCount > 0) {
			List<RechargeOffline> data = this.rechargeOfflineMapper.query(qo);
			return new PageResult(data,totalCount,qo.getCurrentPage(),qo.getPageSize());
		}
		return PageResult.empty();
	}

	/**
	 * 后台线下充值审核
	 */
	@Override
	public void audit(Long id, int state, String remark) {
		//从数据库中查询出当前审核的充值单
		RechargeOffline rechargeOffline = this.rechargeOfflineMapper.selectByPrimaryKey(id);
		//如果当前的充值单是未审核情况
		if(rechargeOffline != null && rechargeOffline.getState() == RechargeOffline.STATE_APPLY) {
			//设置审核相关属性
			rechargeOffline.setAuditor(UserContext.getCurrentUser());
			rechargeOffline.setAuditTime(new Date());
			rechargeOffline.setRemark(remark);
			rechargeOffline.setState(state);
			//如果对该充值单审核通过
			if(rechargeOffline.getState() == RechargeOffline.STATE_PASS) {
				//创建一条账户流水
				AccountFlow af = new AccountFlow();
				this.accountFlowService.rechargeFlow(rechargeOffline,af);
			}
		}
		
		this.rechargeOfflineMapper.updateByPrimaryKey(rechargeOffline);
	}
}
