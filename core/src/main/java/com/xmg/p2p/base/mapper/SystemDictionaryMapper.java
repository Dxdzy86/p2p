package com.xmg.p2p.base.mapper;

import java.util.List;

import com.xmg.p2p.base.domain.SystemDictionary;
import com.xmg.p2p.base.query.SystemDictionaryQueryObject;

public interface SystemDictionaryMapper {

    int insert(SystemDictionary record);//新增数据字典

    SystemDictionary selectByPrimaryKey(Long id);//根据id查询数据字典

    List<SystemDictionary> selectAll();//查询全部

    int updateByPrimaryKey(SystemDictionary record);//更新数据字典

	int queryDicCount(SystemDictionaryQueryObject qo);//数据字典的数目

	List<SystemDictionary> query(SystemDictionaryQueryObject qo);//查询数据字典

	void delete(Long id);

	List<SystemDictionary> listAllDics();
}