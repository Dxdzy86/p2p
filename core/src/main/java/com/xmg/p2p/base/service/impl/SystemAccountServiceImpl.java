package com.xmg.p2p.base.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmg.p2p.base.domain.SystemAccount;
import com.xmg.p2p.base.domain.SystemAccountFlow;
import com.xmg.p2p.base.mapper.SystemAccountFlowMapper;
import com.xmg.p2p.base.mapper.SystemAccountMapper;
import com.xmg.p2p.base.service.ISystemAccountService;
import com.xmg.p2p.base.util.BidConst;
import com.xmg.p2p.business.domain.BidRequest;
@Service
public class SystemAccountServiceImpl implements ISystemAccountService{

	@Autowired
	private SystemAccountMapper systemAccountMapper;
	@Autowired
	private SystemAccountFlowMapper systemAccountFlowMapper; 
	
	@Override
	public void update(SystemAccount systemAccount) {
		int rows = this.systemAccountMapper.updateByPrimaryKey(systemAccount);
		if(rows == 0) {
			throw new RuntimeException("系统账户乐观锁失败");
		}
	}

	@Override
	public void initSystemAccount() {
		SystemAccount account = this.systemAccountMapper.getSystemAccount();
		if(account == null) {
			account = new SystemAccount();
			this.systemAccountMapper.insert(account);
		}
	}

	//系统平台收取用户借款管理费，生成账户流水
	@Override
	public void chargeManageFee(BigDecimal manageChargeFee,BidRequest currentBidRequest) {
		SystemAccount account = this.systemAccountMapper.getSystemAccount();
		account.setFreezedAmount(account.getFreezedAmount());
		account.setTotalBalance(account.getTotalBalance().add(manageChargeFee));
		this.systemAccountMapper.updateByPrimaryKey(account);
		
		//生成系统账户流水
		SystemAccountFlow flow = new SystemAccountFlow();
		flow.setAccountType(BidConst.SYSTEM_ACCOUNT_ACTIONTYPE_MANAGE_CHARGE);
		flow.setAmount(manageChargeFee);
		flow.setFreezedAmount(account.getFreezedAmount());
		flow.setNote(currentBidRequest.getTitle()+"向系统平台支付了"+manageChargeFee+"手续费");
		flow.setTradeTime(new Date());
		flow.setUsableAmount(account.getTotalBalance());
		this.systemAccountFlowMapper.insert(flow);
	}

}
