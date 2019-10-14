package com.xmg.p2p.base.service;

import java.util.List;

import com.xmg.p2p.base.domain.SystemDictionaryItem;
import com.xmg.p2p.base.query.PageResult;
import com.xmg.p2p.base.query.SystemDictionaryItemQueryObject;

public interface ISystemDictionaryItemService {

	PageResult query(SystemDictionaryItemQueryObject qo);

	void update(SystemDictionaryItem item);

	void insert(SystemDictionaryItem item);

	SystemDictionaryItem get(Long id);

	List<SystemDictionaryItem> listItemByParentSn(String sn);

}
