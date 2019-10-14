package com.xmg.p2p.base.mapper;

import com.xmg.p2p.base.domain.SystemAccount;

public interface SystemAccountMapper {
    int insert(SystemAccount record);

    int updateByPrimaryKey(SystemAccount record);
    
    SystemAccount getSystemAccount();
}