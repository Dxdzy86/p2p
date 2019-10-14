package com.xmg.p2p.base.mapper;

import java.util.List;

import com.xmg.p2p.base.domain.IpLog;
import com.xmg.p2p.base.query.IpLogQueryObject;

public interface IpLogMapper {
    int insert(IpLog record);

	Integer queryLogCount(IpLogQueryObject qo);

	List<IpLog> query(IpLogQueryObject qo);
}