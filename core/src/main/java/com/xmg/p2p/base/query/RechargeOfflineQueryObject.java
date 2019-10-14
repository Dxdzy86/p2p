package com.xmg.p2p.base.query;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class RechargeOfflineQueryObject extends QueryObject{
	private Date beginDate;
	private Date endDate;
	private int state = -1;
	private Long applierId;//当前用户的id
	private String tradeCode=null;//交易号
	private Long bankInfoId=-1L;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public void setTradeCode(String tradeCode) {
		if(tradeCode=="") {
			this.tradeCode = null;
		}else {
			this.tradeCode = tradeCode;
		}
	}
}
