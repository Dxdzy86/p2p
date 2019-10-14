package com.xmg.p2p.base.util;

import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.xmg.p2p.base.domain.LoginInfo;
import com.xmg.p2p.base.vo.VerifyCodeVO;

public class UserContext {
	public static final String USER_IN_SESSION = "loginInfo";//登录时将登录用户放入session中
	public static final String VerifyCodeVO_IN_SESSION = "VERIFYCODEVO_IN_SESSION";//将验证码vo对象放入session中
	//该方法可以根据当前的请求获得session
	public static HttpSession getSession() {
		ServletRequestAttributes requestAttribute = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		return requestAttribute.getRequest().getSession();
	}
	//把登录用户放入session中的方法
	public static void putCurrentUser(LoginInfo user) {
		getSession().setAttribute(USER_IN_SESSION, user);
	}
	//从session中取出当前登录用户
	public static LoginInfo getCurrentUser() {
		return (LoginInfo) getSession().getAttribute(USER_IN_SESSION);
	}
	//将验证码VO对象放入session中
	public static void setVerifyCodeVO(VerifyCodeVO vo) {
		getSession().setAttribute(VerifyCodeVO_IN_SESSION, vo);
	}
	//从session中取出验证码vo对象
	public static VerifyCodeVO getVerifyCodeVo() {
		return (VerifyCodeVO) getSession().getAttribute(VerifyCodeVO_IN_SESSION);
	}
}
