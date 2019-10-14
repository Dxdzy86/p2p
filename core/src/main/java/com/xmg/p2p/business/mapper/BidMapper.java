package com.xmg.p2p.business.mapper;

import java.util.List;

import com.xmg.p2p.business.domain.Bid;

public interface BidMapper {
    int insert(Bid record);

    Bid selectByPrimaryKey(Long id);

    List<Bid> selectAll();

    int updateByPrimaryKey(Bid record);
}