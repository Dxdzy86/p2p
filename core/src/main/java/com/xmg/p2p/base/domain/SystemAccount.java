package com.xmg.p2p.base.domain;

import java.math.BigDecimal;

import org.apache.ibatis.type.Alias;

import com.xmg.p2p.base.util.BidConst;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
@Alias("SystemAccount")
public class SystemAccount extends BaseDomain{
	private int version;
	private BigDecimal totalBalance = BidConst.ZERO;
	private BigDecimal freezedAmount=BidConst.ZERO;//冻结金额
}
