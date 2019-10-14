package com.xmg.p2p.base.query;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.xmg.p2p.base.util.DateUtil;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class VedioAuthQueryObject extends QueryObject{
	private int state=0;
	private Date beginDate;
	private Date endDate;
	
	//设置前台输入日期的格式
	@DateTimeFormat(pattern="yyyy-MM-dd")
	public void setBeginDate(Date date) {
		this.beginDate = date;
	}
	
	//设置前台输入日期的格式
	@DateTimeFormat(pattern="yyyy-MM-dd")
	public void setEndDate(Date date) {
		this.endDate = date;
	}
	
	//后台返回到前台的日期格式
	public Date getEndDate() {
		return this.endDate == null? null:DateUtil.getEndDate(this.endDate);
	}
}
