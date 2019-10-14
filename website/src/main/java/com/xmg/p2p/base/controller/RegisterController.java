package com.xmg.p2p.base.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xmg.p2p.base.domain.LoginInfo;
import com.xmg.p2p.base.service.ILoginInfoService;
import com.xmg.p2p.base.util.JSONResult;
import com.xmg.p2p.base.util.MD5;
import com.xmg.p2p.base.util.UserContext;

@Controller
public class RegisterController {
	@Autowired
	private ILoginInfoService loginInfoService;
	/**
	 * 用户注册
	 * @param username
	 * @param password
	 */
	@RequestMapping("/register")
	@ResponseBody
	public JSONResult register(String username,String password) {
		JSONResult result = null;
		try {
			loginInfoService.register(username, password);
			result = new JSONResult(true,"注册成功");
		} catch (Exception e) {
			//以JSON的格式返回信息到前台页面
			result = new JSONResult(false,e.getMessage());//如果用户名存在，会抛出异常
		}
		return result;
	}
	
	@RequestMapping("/checkUsername")
	@ResponseBody
	public String checkUsername(String username) {
		//前台调用该方法远程检查数据库是否存有该username和userType的用户，区分普通用户和后台管理员
		if(loginInfoService.checkUsername(username,LoginInfo.USERTYPE_USER)) {
			return "false";
		}
		return "true";
	}
	/**
	 * 用户登录
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping("/login")
	@ResponseBody
	public JSONResult login(String username,String password,HttpServletRequest request) {
		JSONResult result = new JSONResult();//JSONResult里success属性默认值为true
		LoginInfo user = loginInfoService.login(username,MD5.encode(password),request.getRemoteAddr(),LoginInfo.USERTYPE_USER);
		if(user == null){
			//用户名或者密码错误
			result.setMsg("用户名或密码错误");
			result.setSuccess(false);
		}
		return result;
	}
	
	/**
	 * 用户注销
	 * @return
	 */
	@RequestMapping("/logout")
	public String logout() {
		UserContext.putCurrentUser(null);//将session中登录的用户注销
		return "redirect:/index.do";
	}
}
