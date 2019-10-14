package com.xmg.p2p.base.mapper;

import java.util.List;

import com.xmg.p2p.base.domain.RealAuth;
import com.xmg.p2p.base.query.RealAuthQueryObject;

public interface RealAuthMapper {
    int insert(RealAuth record);

    RealAuth selectByPrimaryKey(Long id);

    List<RealAuth> selectAll();

	int queryRealAuthCount(RealAuthQueryObject qo);

	List<RealAuth> query(RealAuthQueryObject qo);

	void updateByPrimaryKey(RealAuth oldRealAuth);
}