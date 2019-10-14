package com.xmg.p2p.base.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmg.p2p.base.domain.LoginInfo;
import com.xmg.p2p.base.domain.UserInfo;
import com.xmg.p2p.base.domain.VedioAuth;
import com.xmg.p2p.base.mapper.VedioAuthMapper;
import com.xmg.p2p.base.query.PageResult;
import com.xmg.p2p.base.query.VedioAuthQueryObject;
import com.xmg.p2p.base.service.IUserInfoService;
import com.xmg.p2p.base.service.IVedioAuthService;
import com.xmg.p2p.base.util.BitStatesUtil;
import com.xmg.p2p.base.util.UserContext;
/**
 * 视频认证审核业务相关
 * @author Dxd
 *
 */
@Service
public class VedioAuthServiceImpl implements IVedioAuthService {

	@Autowired
	private VedioAuthMapper vedioAuthMapper;
	@Autowired
	private IUserInfoService userInfoService;
	
	@Override
	public PageResult query(VedioAuthQueryObject qo) {
		int totalCount = this.vedioAuthMapper.queryVedioAuthCount(qo);
		if(totalCount>0) {
			List<VedioAuth> data = this.vedioAuthMapper.query(qo);
			return new PageResult(data,totalCount,qo.getCurrentPage(),qo.getPageSize());
		}
		return PageResult.empty();
	}
	
	/**
	 * 视频认证审核
	 */
	@Override
	public void audit(int state, Long loginInfoValue, String remark) {
		//从数据库中获取需要进行视频认证的用户基本信息对象
		UserInfo userInfo = this.userInfoService.get(loginInfoValue);
		if(userInfo != null) {
			//如果该用户没有进行视频认证绑定
			if(!userInfo.getHasVedioAuth()) {
				//视频认证审核操作
				VedioAuth vedioAuth = new VedioAuth();
				LoginInfo loginInfo = new LoginInfo();
				loginInfo.setId(loginInfoValue);
				vedioAuth.setApplier(loginInfo);//当前需要审核的用户
				vedioAuth.setApplyTime(new Date());//申请认证时间
				vedioAuth.setAuditor(UserContext.getCurrentUser());//审核人
				vedioAuth.setAuditTime(new Date());//审核时间
				vedioAuth.setRemark(remark);
				vedioAuth.setState(state);
				//如果前台不是"审核拒绝"操作
				if(state!=VedioAuth.STATE_REJECT) {
					userInfo.addBitState(BitStatesUtil.OP_IS_VEDIO_AUTH);
					this.userInfoService.update(userInfo);
				}
				this.vedioAuthMapper.insert(vedioAuth);
			}
		}
	}
	/**
	 * 自动补全
	 */
	@Override
	public List<HashMap<String, Object>> autoCompleteList(String keyword) {
		
		return this.vedioAuthMapper.autoComplete(keyword);
	}

}
