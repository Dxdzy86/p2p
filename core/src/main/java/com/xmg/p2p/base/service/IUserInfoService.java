package com.xmg.p2p.base.service;

import com.xmg.p2p.base.domain.UserInfo;

public interface IUserInfoService {
	UserInfo get(Long id);

	void update(UserInfo userInfo);

	void bindPhone(String phoneNumber,String verifyCode);

	void sendEmail(String email);

	void bindEmail(String randomcode);

	void updateBasicInfo(UserInfo userInfo);
}
