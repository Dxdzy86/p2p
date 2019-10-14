package com.xmg.p2p.base.mapper;

import java.util.HashMap;
import java.util.List;

import com.xmg.p2p.base.domain.VedioAuth;
import com.xmg.p2p.base.query.VedioAuthQueryObject;

public interface VedioAuthMapper {
    int insert(VedioAuth record);

    VedioAuth selectByPrimaryKey(Long id);

    List<VedioAuth> selectAll();

	int queryVedioAuthCount(VedioAuthQueryObject qo);

	List<VedioAuth> query(VedioAuthQueryObject qo);

	List<HashMap<String, Object>> autoComplete(String keyword);
}