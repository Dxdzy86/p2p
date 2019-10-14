package com.xmg.p2p.base.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmg.p2p.base.domain.Account;
import com.xmg.p2p.base.domain.IpLog;
import com.xmg.p2p.base.domain.LoginInfo;
import com.xmg.p2p.base.domain.UserInfo;
import com.xmg.p2p.base.mapper.AccountMapper;
import com.xmg.p2p.base.mapper.IpLogMapper;
import com.xmg.p2p.base.mapper.LoginInfoMapper;
import com.xmg.p2p.base.mapper.UserInfoMapper;
import com.xmg.p2p.base.service.ILoginInfoService;
import com.xmg.p2p.base.util.BidConst;
import com.xmg.p2p.base.util.MD5;
import com.xmg.p2p.base.util.UserContext;
@Service
public class LoginInfoServiceImpl implements ILoginInfoService{
	@Autowired
	private LoginInfoMapper dao;
	@Autowired
	private AccountMapper accountMapper;
	@Autowired
	private UserInfoMapper userInfoMapper;
	@Autowired
	private IpLogMapper ipLogMapper;
	/**
	 * 前台注册
	 */
	public void register(String username,String password) {
		//注册用户时默认的用户类型就是普通用户
		int count = dao.getCountByUsername(username,LoginInfo.USERTYPE_USER);
		//若用户 存在，则抛出异常
		if(count>0) {
			throw new RuntimeException("用户名已存在");
		}else {
			LoginInfo loginInfo = new LoginInfo();
			//若用户不存在，则进行注册
			loginInfo.setUsername(username);
			loginInfo.setPassword(MD5.encode(password));
			loginInfo.setState(loginInfo.STATE_NORMAL);
			loginInfo.setUserType(loginInfo.USERTYPE_USER);//前台用户
			dao.insert(loginInfo);
			//注册成功后，创建该用户对应的账户信息
			Account account = new Account();
			account.setId(loginInfo.getId());//根据当前用户ID设置其账户id
			accountMapper.insert(account);
			//创建该用户的用户基本信息
			UserInfo userInfo = new UserInfo();
			userInfo.setId(loginInfo.getId());//根据当前用户ID设置其用户基本信息id
			//userInfo.setBitState(BitStatesUtil.OP_BASIC_INFO);//给用户基本信息设置默认的状态码
			userInfoMapper.insert(userInfo);
		}
	}
	/**
	 * 前台用户名输入栏失去焦点时调用的方法
	 */
	@Override
	public Boolean checkUsername(String username,int userType) {
		int count = dao.getCountByUsername(username,userType);//查询数据库是否含有该用户
		if(count>0) {
			return true;
		}
		return false;
	}
	/**
	 * 用户登录方法
	 */
	@Override
	public LoginInfo login(String username, String password,String ip,int userType) {
		LoginInfo user = dao.login(username,password,userType);
		//创建日志记录对象
		IpLog ipLog = new IpLog(username,ip,new Date(),IpLog.LOGIN_FAILED);
		if(user!=null) {
			//将用户存入session中
			UserContext.putCurrentUser(user);
			ipLog.setUsername(user.getUsername());
			ipLog.setIp(ip);
			ipLog.setLoginState(IpLog.LOGIN_SUCCESS);
			ipLog.setLoginTime(new Date());
			ipLog.setUserType(userType);//设置用户类型
		}
		ipLogMapper.insert(ipLog);//若用户存在，则记录登录成功
		return user;
	}
	/**
	 * spring容器启动后判断数据库是否含有后台超级管理员
	 */
	public boolean hasAdminUser() {
		int count = dao.getCountByUsername("admin", LoginInfo.USERTYPE_MANAGER);
		return count > 0 ? true : false;
	}
	
	/**
	 * 创建超级管理员
	 */
	public void createAdminUser() {
		LoginInfo admin = new LoginInfo();
		admin.setUsername(BidConst.DEFAULT_ADMIN);
		admin.setPassword(MD5.encode("1"));
		admin.setState(LoginInfo.STATE_NORMAL);
		admin.setUserType(LoginInfo.USERTYPE_MANAGER);
		dao.insert(admin);
	}
}
