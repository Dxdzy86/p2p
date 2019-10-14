package com.xmg.p2p.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmg.p2p.business.domain.BidRequestAuditHistory;
import com.xmg.p2p.business.mapper.BidRequestAuditHistoryMapper;
import com.xmg.p2p.business.service.IBidRequestAuditHistoryService;
@Service
public class BidRequestAuditHistoryServiceImpl implements IBidRequestAuditHistoryService {
	@Autowired
	private BidRequestAuditHistoryMapper bidRequestAuditHistoryMapper;
	
	@Override
	public BidRequestAuditHistory get(Long id) {
		return bidRequestAuditHistoryMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<BidRequestAuditHistory> queryAuditHistoryByBidRequestId(Long id) {
		return bidRequestAuditHistoryMapper.queryAuditHistoryByBidRequestId(id);
	}

}
