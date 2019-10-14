package com.xmg.p2p.base.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmg.p2p.base.domain.SystemDictionaryItem;
import com.xmg.p2p.base.mapper.SystemDictionaryItemMapper;
import com.xmg.p2p.base.query.PageResult;
import com.xmg.p2p.base.query.SystemDictionaryItemQueryObject;
import com.xmg.p2p.base.service.ISystemDictionaryItemService;
/**
 * 数据字典明细业务层
 * @author Dxd
 *
 */
@Service
public class SystemDictionaryItemServiceImpl implements ISystemDictionaryItemService {
	@Autowired
	private SystemDictionaryItemMapper systemDictionaryItemMapper;	
	@Override
	public PageResult query(SystemDictionaryItemQueryObject qo) {
		int totalCount = systemDictionaryItemMapper.queryDicItemCount(qo);
		if(totalCount > 0) {
			List<SystemDictionaryItem> data = systemDictionaryItemMapper.query(qo);
			PageResult pageResult = new PageResult(data, totalCount);
			return pageResult;
		}
		return PageResult.empty();
	}
	@Override
	public void update(SystemDictionaryItem item) {
		systemDictionaryItemMapper.updateByPrimaryKey(item);
	}
	@Override
	public void insert(SystemDictionaryItem item) {
		systemDictionaryItemMapper.insert(item);
	}
	@Override
	public SystemDictionaryItem get(Long id) {
		return systemDictionaryItemMapper.selectByPrimaryKey(id);
	}
	
	@Override
	public List<SystemDictionaryItem> listItemByParentSn(String sn) {
		return systemDictionaryItemMapper.listItemByParentSn(sn);
	}
	
}
