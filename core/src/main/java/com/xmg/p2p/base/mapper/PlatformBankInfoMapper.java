package com.xmg.p2p.base.mapper;

import java.util.List;

import com.xmg.p2p.base.domain.PlatformBankInfo;
import com.xmg.p2p.base.query.PlatformBankInfoQueryObject;

public interface PlatformBankInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PlatformBankInfo record);

    PlatformBankInfo selectByPrimaryKey(Long id);

    List<PlatformBankInfo> selectAll();

    int updateByPrimaryKey(PlatformBankInfo record);

	int queryPlatformBankInfoCount(PlatformBankInfoQueryObject qo);

	List<PlatformBankInfo> query(PlatformBankInfoQueryObject qo);
}