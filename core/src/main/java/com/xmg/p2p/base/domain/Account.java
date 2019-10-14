package com.xmg.p2p.base.domain;

import java.math.BigDecimal;

import com.xmg.p2p.base.util.BidConst;

import lombok.Getter;
import lombok.Setter;
@Setter@Getter
public class Account extends BaseDomain{
	private int version;
	private String tradePassword;//交易密码
	private BigDecimal usableAmount=BidConst.ZERO;//可用金额
	private BigDecimal freezedAmount=BidConst.ZERO;//冻结金额
	private BigDecimal unReceiveInterest=BidConst.ZERO;//待收利息
	private BigDecimal unReceivePrincipal=BidConst.ZERO;//待收本金
	private BigDecimal unReturnAmount=BidConst.ZERO;//待还金额
	private BigDecimal remainBorrowLimit=BidConst.INIT_BORROW_LIMIT;//剩余授信额度
	private BigDecimal borrowLimit=BidConst.ZERO;//授信额度
	
	//提供获取总账户总额的getTotalAmount方法
	public BigDecimal getTotalAmount() {
		return usableAmount.add(freezedAmount).add(unReceivePrincipal);
	}
}
