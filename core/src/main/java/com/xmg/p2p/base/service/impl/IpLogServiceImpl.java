package com.xmg.p2p.base.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmg.p2p.base.domain.IpLog;
import com.xmg.p2p.base.mapper.IpLogMapper;
import com.xmg.p2p.base.query.IpLogQueryObject;
import com.xmg.p2p.base.query.PageResult;
import com.xmg.p2p.base.service.IIpLogService;
@Service
public class IpLogServiceImpl implements IIpLogService {
	@Autowired
	private IpLogMapper ipLogMapper;
	@Override
	public PageResult query(IpLogQueryObject qo) {
		//从数据库中查询出日志记录总数
		Integer count = ipLogMapper.queryLogCount(qo);
		if(count>0) {
			List<IpLog> data = ipLogMapper.query(qo);
			return new PageResult(data,count,qo.getCurrentPage(),qo.getPageSize());
		}
		return PageResult.empty();
	}

}
