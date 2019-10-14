package com.xmg.p2p.base.mapper;

import java.util.List;

import com.xmg.p2p.base.domain.RechargeOffline;
import com.xmg.p2p.base.query.RechargeOfflineQueryObject;

public interface RechargeOfflineMapper {

    int insert(RechargeOffline record);

    RechargeOffline selectByPrimaryKey(Long id);

    int updateByPrimaryKey(RechargeOffline record);
    
    int queryRechargeOfflineCount(RechargeOfflineQueryObject qo);
    
    List<RechargeOffline> query(RechargeOfflineQueryObject qo);
}