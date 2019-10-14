package com.xmg.p2p.base.domain;

import lombok.Getter;
import lombok.Setter;
/**
 * 平台所有用户d
 * @author Dxd
 *
 */
@Setter@Getter
public class LoginInfo extends BaseDomain{
	public static final int STATE_NORMAL = 0;//用户普通状态
	public static final int STATE_LOCK = 1;//用户锁定状态
	
	public static final int USERTYPE_USER = 1;//前台用户
	public static final int USERTYPE_MANAGER = 0;//后台管理员
	
	private String username;//用户名
	private String password;//密码
	private int state=STATE_NORMAL;//用户的状态
	private int userType;//用户类型(用以区分是前台用户还是后台用户)
}
