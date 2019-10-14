package com.xmg.p2p.base.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.xmg.p2p.base.util.RequiredLogin;
import com.xmg.p2p.base.util.UserContext;
/**
 * 登录拦截器
 * @author Dxd
 * HandlerInterceptorAdaptor是一个抽象类，其实现了HandlerInterceptor接口里的所有方法
 * 我们只需要重写相关的方法-----典型的适配器模式的使用
 */

public class LoginCheckInterceptor extends HandlerInterceptorAdapter{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if(handler instanceof HandlerMethod) {
			HandlerMethod hm = (HandlerMethod)handler;//封装了与请求相关的方法
			RequiredLogin requiredLogin = hm.getMethodAnnotation(RequiredLogin.class);//检查请求的方法上是否标有RequiredLogin注解
			//从session中取出当前登录用户，判断用户是否存在
			if(requiredLogin != null) {
				if(UserContext.getCurrentUser() == null) {
					response.sendRedirect("/login.html");
					return false;
				}
			}
		}
		return true;
	}
}
