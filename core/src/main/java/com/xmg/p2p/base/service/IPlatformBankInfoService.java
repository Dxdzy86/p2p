package com.xmg.p2p.base.service;

import java.util.List;

import com.xmg.p2p.base.domain.PlatformBankInfo;
import com.xmg.p2p.base.query.PageResult;
import com.xmg.p2p.base.query.PlatformBankInfoQueryObject;

public interface IPlatformBankInfoService {
	
	PageResult query(PlatformBankInfoQueryObject qo);

	void saveOrUpdate(PlatformBankInfo info);
	
	List<PlatformBankInfo> listBanksAll();
	
	PlatformBankInfo get(Long id);
}
