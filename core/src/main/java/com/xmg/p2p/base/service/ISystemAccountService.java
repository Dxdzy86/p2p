package com.xmg.p2p.base.service;

import java.math.BigDecimal;

import com.xmg.p2p.base.domain.SystemAccount;
import com.xmg.p2p.business.domain.BidRequest;

public interface ISystemAccountService {
	void update(SystemAccount systemAccount);
	
	void initSystemAccount();
	
	//系统平台收取借款用户的管理费
	void chargeManageFee(BigDecimal manageChargeFee,BidRequest currentBidRequest);
}
