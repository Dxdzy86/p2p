package com.xmg.p2p.base.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
/**
 * 邮箱验证
 * @author Dxd
 *
 */
@Setter@Getter
public class EmailVerify extends BaseDomain{
	private Long userInfoId;//用户基本信息id
	private Date sendEmailDate;//发送邮件时间
	private String randomcode;//随机码，用于邮箱验证使用
	private String email;//邮箱
}
