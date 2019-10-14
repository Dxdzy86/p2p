package com.xmg.p2p.mgrsite.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xmg.p2p.base.domain.BaseDomain;
import com.xmg.p2p.base.domain.LoginInfo;
import com.xmg.p2p.base.service.ILoginInfoService;
import com.xmg.p2p.base.util.JSONResult;
import com.xmg.p2p.base.util.MD5;

@Controller
public class LoginInfoController extends BaseDomain{
	@Autowired
	private ILoginInfoService loginInfoService;
	@RequestMapping("/login")
	@ResponseBody
	public JSONResult login(String username,String password,HttpServletRequest request) {
		JSONResult result = new JSONResult();
		LoginInfo loginInfo = loginInfoService.login(username, MD5.encode(password), request.getRemoteAddr(), LoginInfo.USERTYPE_MANAGER);
		if(loginInfo == null) {
			result.setSuccess(false);
			result.setMsg("登录失败");
		}
		return result;
	}
	/**
	 * 后台管理主页面
	 * @return
	 */
	@RequestMapping("/main")
	public String index(Model model) {
		return "main";
	}
}
