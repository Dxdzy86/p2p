package com.xmg.p2p.business.mapper;

import java.util.List;

import com.xmg.p2p.business.domain.BidRequestAuditHistory;

public interface BidRequestAuditHistoryMapper {
    int insert(BidRequestAuditHistory record);

    BidRequestAuditHistory selectByPrimaryKey(Long id);

	List<BidRequestAuditHistory> queryAuditHistoryByBidRequestId(Long id);

}