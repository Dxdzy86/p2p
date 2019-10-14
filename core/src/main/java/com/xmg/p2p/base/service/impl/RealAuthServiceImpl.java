package com.xmg.p2p.base.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmg.p2p.base.domain.RealAuth;
import com.xmg.p2p.base.domain.UserInfo;
import com.xmg.p2p.base.mapper.RealAuthMapper;
import com.xmg.p2p.base.mapper.UserInfoMapper;
import com.xmg.p2p.base.query.PageResult;
import com.xmg.p2p.base.query.RealAuthQueryObject;
import com.xmg.p2p.base.service.IRealAuthService;
import com.xmg.p2p.base.util.BitStatesUtil;
import com.xmg.p2p.base.util.UserContext;
@Service
public class RealAuthServiceImpl implements IRealAuthService{
	@Autowired
	private RealAuthMapper realAuthMapper;
	@Autowired
	private UserInfoMapper userInfoMapper;

	@Override
	public RealAuth getRealAuth(Long realAuthId) {
		return realAuthMapper.selectByPrimaryKey(realAuthId);
	}

	/**
	 * 身份认证申请
	 */
	/**
	 * realName: 代
		sex: 0
		idNumber: 34323577376472364
		bornDate: 1995-01-01
		address: 浙江
		image1: /upload/dafe9fd6-2881-47bf-ae65-32b639f2fd4c.png
		image2: /upload/1a71a279-ebc8-424d-9b8a-8c8c3d745ab2.png
	 */
	@Override
	public void realAuthApply(RealAuth realAuth) {
		realAuth.setState(RealAuth.STATE_APPLY);
		realAuth.setApplier(UserContext.getCurrentUser());
		realAuth.setApplyTime(new Date());
		this.realAuthMapper.insert(realAuth);
		
		//给当前用户添加实名认证对象
		UserInfo userInfo = userInfoMapper.selectByPrimaryKey(UserContext.getCurrentUser().getId());
		userInfo.setRealAuthId(realAuth.getId());
		this.userInfoMapper.updateByPrimaryKey(userInfo);
	}

	/**
	 * 高级查询
	 */
	@Override
	public PageResult query(RealAuthQueryObject qo) {
		int totalCount = this.realAuthMapper.queryRealAuthCount(qo);
		if(totalCount > 0) {
			List<RealAuth> data = this.realAuthMapper.query(qo);
			return new PageResult(data,totalCount,qo.getCurrentPage(),qo.getPageSize());
		}
		return PageResult.empty();
	}

	@Override
	public void audit(Long id,int state,String remark) {
		//先从数据库中获得当前需要审核的实名认证数据
		RealAuth oldRealAuth = this.realAuthMapper.selectByPrimaryKey(id);
		if(oldRealAuth != null) {
			//如果当前用户实名认证的状态是正在审核中
			if(oldRealAuth.getState() == RealAuth.STATE_APPLY) {
				oldRealAuth.setAuditor(UserContext.getCurrentUser());
				oldRealAuth.setAuditTime(new Date());
				oldRealAuth.setRemark(remark);
				oldRealAuth.setState(state);//将实名认证状态设置为前台传入的审核状态
				
				//根据申请实名认证的用户在数据库中找到相应的用户基本信息
				UserInfo userInfo = this.userInfoMapper.selectByPrimaryKey(oldRealAuth.getApplier().getId());
				//如果前台传入的审核状态是审核拒绝,用户实名认证审核不通过
				if(state == RealAuth.STATE_REJECT) {
					userInfo.setRealAuthId(null);//申请进行实名认证的用户的实名认证id为null，表示实名认证不通过
				}else {
					userInfo.setRealAuthId(oldRealAuth.getId());//给用户基本信息添加审核通过的实名认证对象id
					userInfo.setRealName(oldRealAuth.getRealName());//用户真实姓名
					userInfo.setIdNumber(oldRealAuth.getIdNumber());//用户的身份证号码
					userInfo.addBitState(BitStatesUtil.OP_IS_IDENTITY_AUTH);//绑定实名认证通过
				}
				//数据库更新
				this.userInfoMapper.updateByPrimaryKey(userInfo);
				this.realAuthMapper.updateByPrimaryKey(oldRealAuth);
			}
		}
	}
}
