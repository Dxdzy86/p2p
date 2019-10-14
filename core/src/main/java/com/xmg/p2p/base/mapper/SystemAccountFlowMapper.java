package com.xmg.p2p.base.mapper;

import com.xmg.p2p.base.domain.SystemAccountFlow;

public interface SystemAccountFlowMapper {
    int insert(SystemAccountFlow record);

    SystemAccountFlow selectByPrimaryKey(Long id);
}