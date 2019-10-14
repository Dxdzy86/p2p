package com.xmg.p2p.base.query;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.xmg.p2p.base.util.DateUtil;

import lombok.Getter;
import lombok.Setter;
@Setter@Getter
public class RealAuthQueryObject extends QueryObject{
	private int state = 0;
	private Date beginDate;
	private Date endDate;
	
	//根据指定日期格式设置前台的日期
	@DateTimeFormat(pattern="yyyy-MM-dd")
	public void setBeginDate(Date date) {
		this.beginDate = date;
	}
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	public void setEndDate(Date date) {
		this.endDate = date;
	}
	
	//根据指定的日期格式获得数据库中的日期时间
	public Date getEndDate() {
		return this.endDate==null?null:DateUtil.getEndDate(this.endDate);
	}
}
