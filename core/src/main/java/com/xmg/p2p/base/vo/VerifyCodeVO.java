package com.xmg.p2p.base.vo;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
@Setter@Getter
public class VerifyCodeVO {
	private Date lastSendVerifyCodeTime;//发送验证码的上一次时间
	private String verifyCode;//验证码
	private String phoneNumber;//手机号码
}
