package com.xmg.p2p.base.service;

import com.xmg.p2p.base.domain.RealAuth;
import com.xmg.p2p.base.query.PageResult;
import com.xmg.p2p.base.query.RealAuthQueryObject;

public interface IRealAuthService {

	RealAuth getRealAuth(Long realAuthId);

	void realAuthApply(RealAuth realAuth);

	PageResult query(RealAuthQueryObject qo);

	void audit(Long id,int state,String remark);
	
}
