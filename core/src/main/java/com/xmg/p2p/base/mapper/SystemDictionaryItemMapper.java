package com.xmg.p2p.base.mapper;

import java.util.List;

import com.xmg.p2p.base.domain.SystemDictionary;
import com.xmg.p2p.base.domain.SystemDictionaryItem;
import com.xmg.p2p.base.query.SystemDictionaryItemQueryObject;

public interface SystemDictionaryItemMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SystemDictionaryItem record);

    SystemDictionaryItem selectByPrimaryKey(Long id);

    List<SystemDictionaryItem> selectAll();

    int updateByPrimaryKey(SystemDictionaryItem record);

	int queryDicItemCount(SystemDictionaryItemQueryObject qo);

	List<SystemDictionaryItem> query(SystemDictionaryItemQueryObject qo);

	List<SystemDictionary> listAllDics();

	List<SystemDictionaryItem> listItemByParentSn(String sn);
}