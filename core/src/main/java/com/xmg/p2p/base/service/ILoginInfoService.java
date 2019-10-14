package com.xmg.p2p.base.service;

import com.xmg.p2p.base.domain.LoginInfo;

public interface ILoginInfoService {
	void register(String username,String password);

	Boolean checkUsername(String username,int userType);

	LoginInfo login(String username, String password,String ip,int userType);

	boolean hasAdminUser();//判断是否含有超级管理员

	void createAdminUser();//创建超级管理员
}
