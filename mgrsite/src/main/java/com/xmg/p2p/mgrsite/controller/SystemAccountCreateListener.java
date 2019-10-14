package com.xmg.p2p.mgrsite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.xmg.p2p.base.service.ISystemAccountService;

@Component
public class SystemAccountCreateListener implements ApplicationListener<ContextRefreshedEvent>{
	@Autowired
	private ISystemAccountService systemAccountService;
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		this.systemAccountService.initSystemAccount();
	}

}
