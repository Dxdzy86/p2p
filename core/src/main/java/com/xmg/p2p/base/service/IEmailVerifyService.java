package com.xmg.p2p.base.service;

import com.xmg.p2p.base.domain.EmailVerify;

public interface IEmailVerifyService {

	void insert(EmailVerify em);

	EmailVerify selectByRandomCode(String randomcode);

}
