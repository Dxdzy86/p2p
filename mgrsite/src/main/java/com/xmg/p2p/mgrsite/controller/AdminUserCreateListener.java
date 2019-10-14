package com.xmg.p2p.mgrsite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.xmg.p2p.base.service.ILoginInfoService;
/**
 * 监听spring容器启动，在spring容器启动后执行相应的操作
 * @author Dxd
 *
 */
@Component
public class AdminUserCreateListener implements ApplicationListener<ContextRefreshedEvent>{
	@Autowired
	private ILoginInfoService loginInfoService;
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		//判断数据库中是否含有超级管理员
		if(!loginInfoService.hasAdminUser()) {
			loginInfoService.createAdminUser();
		}
	}
}
