package com.xmg.p2p.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xmg.p2p.base.domain.LoginInfo;
import com.xmg.p2p.base.service.IAccountService;
import com.xmg.p2p.base.service.IUserInfoService;
import com.xmg.p2p.base.service.IVerifyCodeService;
import com.xmg.p2p.base.util.JSONResult;
import com.xmg.p2p.base.util.RequiredLogin;
import com.xmg.p2p.base.util.UserContext;
/**
 * 个人用户账户
 * @author Dxd
 *
 */
@Controller
public class PersonController {
	@Autowired
	private IAccountService accountService;
	@Autowired
	private IUserInfoService userInfoService;
	@Autowired
	private IVerifyCodeService verifyCodeService;
	
	@RequiredLogin//访问该方法必须要登录
	@RequestMapping("/personal")
	public String personCenter(Model model) {
		LoginInfo loginInfo = UserContext.getCurrentUser();
		//从Session中查询出当前登录用户
		//model.addAttribute("loginInfo", loginInfo);
		//将当前登录用户的Account(账户)和UserInfo(用户信息)添加到Model中用于前台页面数据显示
		model.addAttribute("account",accountService.get(loginInfo.getId()));
		model.addAttribute("userInfo",userInfoService.get(loginInfo.getId()));
		return "personal";
	}
	
	//发送验证码
	@RequestMapping("/sendVerifyCode")
	@ResponseBody
	public String sendVerifyCode(String phoneNumber) {
		verifyCodeService.sendVerifyCode(phoneNumber);
		return "true";
	}
	
	//绑定手机
	@RequestMapping("/bindPhone")
	@ResponseBody
	public JSONResult bindPhone(String phoneNumber,String verifyCode) {
		JSONResult result = new JSONResult();
		try {
			userInfoService.bindPhone(phoneNumber,verifyCode);//绑定手机(注意，这里使用的是userInfoService)
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMsg(e.getMessage());
		}
		return result;
	}
	
	//发送邮件
	@RequestMapping("/sendEmail")
	@ResponseBody
	public JSONResult sendEmail(String email) {
		JSONResult result = new JSONResult();
		try {
			userInfoService.sendEmail(email);
			result.setSuccess(true);
		}catch(Exception e) {
			result.setSuccess(false);
			result.setMsg("绑定邮箱失败");
		}
		return result;
	}
	
	//绑定邮箱
	@RequestMapping("/bindEmail")
	public String bindEmail(String randomcode,Model model) {
		try {
			userInfoService.bindEmail(randomcode);
			model.addAttribute("success",true);
		}catch(Exception e) {
			model.addAttribute("success",false);
			model.addAttribute("msg",e.getMessage());
		}
		return "checkmail_result";
	}
}
