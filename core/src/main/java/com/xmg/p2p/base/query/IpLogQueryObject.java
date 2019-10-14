package com.xmg.p2p.base.query;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.xmg.p2p.base.util.DateUtil;

import lombok.Getter;
import lombok.Setter;
@Setter@Getter
public class IpLogQueryObject extends QueryObject{
	private Date beginDate;
	private Date endDate;
	private int loginState = -1;
	private String username;
	private int userType = -1;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	public void setBeginDate(Date date) {
		this.beginDate = date;
	}
	@DateTimeFormat(pattern="yyyy-MM-dd")
	public void setEndDate(Date date) {
		this.endDate = date;
	}
	
	public Date getEndDate() {
		return endDate!=null?DateUtil.getEndDate(endDate):null;
	}
}
