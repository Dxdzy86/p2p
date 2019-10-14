package com.xmg.p2p.base.service;

import java.math.BigDecimal;

import com.xmg.p2p.base.domain.Account;
import com.xmg.p2p.base.domain.AccountFlow;
import com.xmg.p2p.base.domain.RechargeOffline;
import com.xmg.p2p.business.domain.Bid;
import com.xmg.p2p.business.domain.BidRequest;

/**
 * 账户流水
 * @author Dxd
 *
 */
public interface IAccountFlowService {

	void rechargeFlow(RechargeOffline rechargeOffline, AccountFlow af);
	
	//用户投标流水
	void bidFlow(Bid bid, Account currentAccount);
	
	//用户借款失败，退钱给其他用户的账户流水
	void montyReturnAccountFlow(Bid bid, Account account);
	
	//借款成功账户流水
	void borrowSuccess(BidRequest currentBidRequest, Account borrowAccount);
	//生成付给平台管理费的账户流水
	void borrowChargeFee(BidRequest currentBidRequest, Account borrowAccount,BigDecimal manageChargeFee);
	//生成投标用户冻结资金减少的流水
	void createReturnBidFlow(Bid bid, Account account);

}
