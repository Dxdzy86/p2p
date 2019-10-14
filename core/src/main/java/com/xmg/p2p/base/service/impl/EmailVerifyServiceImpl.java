package com.xmg.p2p.base.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmg.p2p.base.domain.EmailVerify;
import com.xmg.p2p.base.mapper.EmailVerifyMapper;
import com.xmg.p2p.base.service.IEmailVerifyService;
@Service
public class EmailVerifyServiceImpl implements IEmailVerifyService {
	@Autowired
	private EmailVerifyMapper emailVerifyMapper;
	
	
	@Override
	public void insert(EmailVerify em) {
		emailVerifyMapper.insert(em);
	}


	@Override
	public EmailVerify selectByRandomCode(String randomcode) {
		return emailVerifyMapper.selectByRandomCode(randomcode);
	}

}
