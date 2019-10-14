package com.xmg.p2p.base.service;

public interface IVerifyCodeService {

	boolean checkVerifyCode();

	void sendVerifyCode(String phoneNumber);
	
	boolean checkPhoneNumberAndVerifyCode(String phoneNumber,String verifyCode);
}
