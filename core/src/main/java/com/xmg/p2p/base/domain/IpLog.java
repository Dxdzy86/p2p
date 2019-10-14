package com.xmg.p2p.base.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
@Setter@Getter
public class IpLog extends BaseDomain{
	public static final int LOGIN_SUCCESS = 1;
	public static final int LOGIN_FAILED = 0;
	private String username;//登录的用户名
	private String ip;//登录ip
	private Date loginTime;//登录时间
	private int loginState;//登录状态
	private int userType;
	public IpLog() {
		super();
	}
	public IpLog(String username, String ip, Date loginTime, int loginState) {
		super();
		this.username = username;
		this.ip = ip;
		this.loginTime = loginTime;
		this.loginState = loginState;
	}
	
	public String getLoginStateDisplay() {
		return loginState>0?"登录成功":"登录失败";
	}
	
	public String getUserTypeDisplay() {
		return userType>0?"前台用户":"后台管理员";
	}
}
