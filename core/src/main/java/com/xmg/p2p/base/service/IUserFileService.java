package com.xmg.p2p.base.service;

import java.util.List;

import com.xmg.p2p.base.domain.UserFile;
import com.xmg.p2p.base.query.PageResult;
import com.xmg.p2p.base.query.UserFileQueryObject;

public interface IUserFileService {
	void applyUserFile(String relativePath);
	
	//是否按照风控资料类型进行查询(根据参数的boolean值来判断)
	List<UserFile> queryUserFileList(Long id, boolean b);

	void updateUserFileTypes(Long[] ids, Long[] fileTypeIds);

	PageResult query(UserFileQueryObject qo);

	void audit(Long id, int state, int score, String remark);
}
