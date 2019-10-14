package com.xmg.p2p.base.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
@Alias("AccountFlow")
public class AccountFlow extends BaseDomain{
	private Long AccountId;//当前充值用户的账户id
	private int accountType;//账户流水类型(参考BidConst)
	private String note;//审核备注
	private Date tradeTime;//审核时间
	private BigDecimal amount;//审核用户充值的金额
	private BigDecimal usableAmount;//充值用户的账户可用余额
	private BigDecimal freezedAmount;//当前充值用户冻结金额
}
