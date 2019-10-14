package com.xmg.p2p.base.service.impl;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.xmg.p2p.base.domain.EmailVerify;
import com.xmg.p2p.base.domain.LoginInfo;
import com.xmg.p2p.base.domain.UserInfo;
import com.xmg.p2p.base.mapper.UserInfoMapper;
import com.xmg.p2p.base.service.IEmailVerifyService;
import com.xmg.p2p.base.service.IUserInfoService;
import com.xmg.p2p.base.service.IVerifyCodeService;
import com.xmg.p2p.base.util.BidConst;
import com.xmg.p2p.base.util.BitStatesUtil;
import com.xmg.p2p.base.util.DateUtil;
import com.xmg.p2p.base.util.UserContext;
/**
 * 用户基本信息Service
 * @author Dxd
 */
@Service
public class UserInfoServiceImpl implements IUserInfoService{
	@Value("${email.url}")
	private String url;
	
	@Autowired
	private UserInfoMapper userInfoMapper;
	@Autowired
	private IVerifyCodeService verifyCodeService;
	@Autowired
	private IEmailVerifyService emailVerifyService;
	@Override
	public UserInfo get(Long id) {
		return userInfoMapper.selectByPrimaryKey(id);
	}
	
	@Override
	public void update(UserInfo userInfo) {
		userInfoMapper.updateByPrimaryKey(userInfo);
	}
	/**
	 * 绑定手机
	 */
	@Override
	public void bindPhone(String phoneNumber,String verifyCode) {
		//先进行判断，当前的手机号码是不是接收验证码的手机号码
		if(verifyCodeService.checkPhoneNumberAndVerifyCode(phoneNumber, verifyCode)) {
			UserInfo userInfo = this.get(UserContext.getCurrentUser().getId());//获得当前用户基本信息
			if(!userInfo.getHasBindPhone()) {//如果用户没有绑定手机号码
				userInfo.setPhoneNumber(phoneNumber);
				//更改用户状态
				userInfo.setBitState(BitStatesUtil.addState(userInfo.getBitState(), BitStatesUtil.OP_BIND_PHONE));
				this.update(userInfo);//更新数据库(为防止并发访问问题，使用乐观锁)
			}
		}else {
			throw new RuntimeException("手机号输入错误");
		}
	}
	//发送邮件
	@Override
	public void sendEmail(String email) {
		//得到当前登录用户基本信息
		UserInfo userInfo = this.get(UserContext.getCurrentUser().getId());
		if(userInfo != null && !userInfo.getHasBindEmail()) {
			//封装一些邮件验证信息,为了用户在去邮箱点击绑定链接时进行验证
			EmailVerify em = new EmailVerify();
			em.setUserInfoId(userInfo.getId());//登录用户基本信息
			em.setEmail(email);
			em.setSendEmailDate(new Date());//邮件发送时间
			em.setRandomcode(UUID.randomUUID().toString());//随机码是给用户在邮件中点击绑定邮箱使用
			
			//发送邮件(谁来发送邮件?),邮件包含了html内容,a标签的地址其实指向的是用户登录的网站进行邮箱绑定的地址
			StringBuilder sb = new StringBuilder(100).append("<a href='")
					.append(url).append("?randomcode=").append(em.getRandomcode())
					.append("'>点击绑定</a>");
			System.out.println(sb);
			
			this.emailVerifyService.insert(em);
		}
	}
	//绑定邮箱
	@Override
	public void bindEmail(String randomcode) {
		//根据随机码找到对应的邮箱验证对象，进而找到当前登录用户
		EmailVerify ev = emailVerifyService.selectByRandomCode(randomcode);
		if(ev != null) {
			//当前登录用户基本信息
			UserInfo currentUser = userInfoMapper.selectByPrimaryKey(ev.getUserInfoId());
			if(currentUser != null && !currentUser.getHasBindEmail()
					&& DateUtil.timeInterval(ev.getSendEmailDate(), new Date())
							< BidConst.VALIDATE_DATE*24*3600*1000) {
				//添加绑定邮箱状态码
				currentUser.setBitState(BitStatesUtil.addState(currentUser.getBitState(), BitStatesUtil.OP_BIND_EMAIL));
				currentUser.setEmail(ev.getEmail());
				this.update(currentUser);
			}
		}else {
			throw new RuntimeException("邮箱绑定失败");
		}
	}

	@Override
	public void updateBasicInfo(UserInfo userInfo) {
		//先根据登录用户id从数据库中取出原来的用户信息对象
		LoginInfo currentUser = UserContext.getCurrentUser();
		UserInfo oldUserInfo = this.userInfoMapper.selectByPrimaryKey(currentUser.getId());
		//将新的用户信息值保存在旧的用户信息对象中
		oldUserInfo.setEducationBackground(userInfo.getEducationBackground());
		oldUserInfo.setHouseCondition(userInfo.getHouseCondition());
		oldUserInfo.setIncomeGrade(userInfo.getIncomeGrade());
		oldUserInfo.setKidCount(userInfo.getKidCount());
		oldUserInfo.setMarriage(userInfo.getMarriage());
		//绑定身份验证码
		if(!oldUserInfo.getHasIdentityAuth()) {
			oldUserInfo.addBitState(BitStatesUtil.OP_SAVE_BASIC_INFO);
		}
		//更新数据库
		this.userInfoMapper.updateByPrimaryKey(oldUserInfo);
	}

}
