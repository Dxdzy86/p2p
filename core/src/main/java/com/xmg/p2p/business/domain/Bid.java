package com.xmg.p2p.business.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.xmg.p2p.base.domain.BaseDomain;
import com.xmg.p2p.base.domain.LoginInfo;
import com.xmg.p2p.base.util.BidConst;

import lombok.Getter;
import lombok.Setter;
@Setter@Getter
@Alias("Bid")
public class Bid extends BaseDomain{
	  private BigDecimal actualRate;//年化利率(和借款的年化利率相同)
	  private BigDecimal availableAmount = BidConst.ZERO;//投标金额
	  private Long bidRequestId;//投的是哪一个标
	  private String bidRequestTitle;//投的那个标的主题
	  private LoginInfo bidUser;//投标人
	  private Date bidTime;//投标时间
	  private int bidRequestState;//标的状态
}
