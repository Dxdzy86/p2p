package com.xmg.p2p.base.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmg.p2p.base.domain.PlatformBankInfo;
import com.xmg.p2p.base.mapper.PlatformBankInfoMapper;
import com.xmg.p2p.base.query.PageResult;
import com.xmg.p2p.base.query.PlatformBankInfoQueryObject;
import com.xmg.p2p.base.service.IPlatformBankInfoService;
/**
 * 平台账号服务
 * @author Dxd
 *
 */
@Service
public class PlatformBankInfoServiceImpl implements IPlatformBankInfoService {
	
	@Autowired
	private PlatformBankInfoMapper platPlatformBankInfoMapper;
	
	/**
	 * 高级查询
	 */
	@Override
	public PageResult query(PlatformBankInfoQueryObject qo) {
		int totalCount = this.platPlatformBankInfoMapper.queryPlatformBankInfoCount(qo);
		if(totalCount > 0) {
			List<PlatformBankInfo> data = this.platPlatformBankInfoMapper.query(qo);
			return new PageResult(data,totalCount,qo.getCurrentPage(),qo.getPageSize());
		}
		
		return PageResult.empty();
	}

	/**
	 * 保存或者更新
	 */
	@Override
	public void saveOrUpdate(PlatformBankInfo info) {
		if(info.getId() != null) {
			this.platPlatformBankInfoMapper.updateByPrimaryKey(info);
		}else {
			this.platPlatformBankInfoMapper.insert(info);
		}
	}

	/**
	 * 查询平台所有的开户银行
	 */
	@Override
	public List<PlatformBankInfo> listBanksAll() {
		return this.platPlatformBankInfoMapper.selectAll();
	}

	@Override
	public PlatformBankInfo get(Long id) {
		return this.platPlatformBankInfoMapper.selectByPrimaryKey(id);
	}

}
