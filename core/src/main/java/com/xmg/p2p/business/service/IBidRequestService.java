package com.xmg.p2p.business.service;

import java.math.BigDecimal;
import java.util.List;

import com.xmg.p2p.base.domain.LoginInfo;
import com.xmg.p2p.base.query.PageResult;
import com.xmg.p2p.business.domain.BidRequest;
import com.xmg.p2p.business.query.BidRequestQueryObject;

public interface IBidRequestService {
	void update(BidRequest bidRequest);
	
	BidRequest get(Long id);
	
	boolean canApplyBidRequest(LoginInfo loginInfo);

	void applyBidRequest(BidRequest bidRequest);
	
	//高级查询相关
	PageResult query(BidRequestQueryObject qo);

	void publishAudit(Long id, int state, String remark);
	//查询首页显示的标记录
	List<BidRequest> queryIndexBidRequests(int i);

	void bid(Long bidRequestId, BigDecimal amount);
	//满标一审
	void fullAudit1(Long id, int state, String remark);
	//满标二审
	void fullAudit2(Long id, int state, String remark);

}
