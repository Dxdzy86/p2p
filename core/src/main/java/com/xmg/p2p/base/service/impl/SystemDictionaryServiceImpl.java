package com.xmg.p2p.base.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmg.p2p.base.domain.SystemDictionary;
import com.xmg.p2p.base.mapper.SystemDictionaryMapper;
import com.xmg.p2p.base.query.PageResult;
import com.xmg.p2p.base.query.SystemDictionaryQueryObject;
import com.xmg.p2p.base.service.ISystemDictionaryService;
@Service
public class SystemDictionaryServiceImpl implements ISystemDictionaryService{
	@Autowired
	private SystemDictionaryMapper systemDictionaryMapper;
	
	//新增数据字典
	@Override
	public void insert(SystemDictionary systemDictionary) {
		systemDictionaryMapper.insert(systemDictionary);
	}

	//更新数据字典
	@Override
	public void update(SystemDictionary systemDictionary) {
		systemDictionaryMapper.updateByPrimaryKey(systemDictionary);
	}
	//高级查询
	@Override
	public PageResult query(SystemDictionaryQueryObject qo) {
		int totalCount = systemDictionaryMapper.queryDicCount(qo);
		if(totalCount > 0) {
			List<SystemDictionary> data = systemDictionaryMapper.query(qo);
			PageResult pageResult = new PageResult(data, totalCount, qo.getCurrentPage(), qo.getPageSize());
			return pageResult;
		}
		return PageResult.empty();
	}

	@Override
	public SystemDictionary getDicById(Long id) {
		return systemDictionaryMapper.selectByPrimaryKey(id);
	}

	@Override
	public void delete(Long id) {
		systemDictionaryMapper.delete(id);
	}

	@Override
	public List<SystemDictionary> listAllDics() {
		return systemDictionaryMapper.listAllDics();
	}

}
