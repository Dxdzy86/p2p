package com.xmg.p2p.base.service;

import com.xmg.p2p.base.domain.RechargeOffline;
import com.xmg.p2p.base.query.PageResult;
import com.xmg.p2p.base.query.RechargeOfflineQueryObject;

public interface IRechargeOfflineService {

	void apply(RechargeOffline rechargeOffline);
	
	PageResult query(RechargeOfflineQueryObject qo);

	void audit(Long id, int state, String remark);
}
