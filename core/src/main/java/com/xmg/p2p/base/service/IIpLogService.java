package com.xmg.p2p.base.service;

import com.xmg.p2p.base.query.IpLogQueryObject;
import com.xmg.p2p.base.query.PageResult;

public interface IIpLogService {
	PageResult query(IpLogQueryObject qo);
}
