package com.xmg.p2p.base.service;

import java.util.List;

import com.xmg.p2p.base.domain.SystemDictionary;
import com.xmg.p2p.base.query.PageResult;
import com.xmg.p2p.base.query.SystemDictionaryQueryObject;

public interface ISystemDictionaryService {

	void insert(SystemDictionary systemDictionary);

	void update(SystemDictionary systemDictionary);

	PageResult query(SystemDictionaryQueryObject qo);

	SystemDictionary getDicById(Long id);

	void delete(Long id);

	List<SystemDictionary> listAllDics();

}
