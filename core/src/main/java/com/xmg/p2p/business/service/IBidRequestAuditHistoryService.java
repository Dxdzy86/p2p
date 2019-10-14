package com.xmg.p2p.business.service;

import java.util.List;

import com.xmg.p2p.business.domain.BidRequestAuditHistory;

public interface IBidRequestAuditHistoryService {
	
	BidRequestAuditHistory get(Long id);

	List<BidRequestAuditHistory> queryAuditHistoryByBidRequestId(Long id);
}
