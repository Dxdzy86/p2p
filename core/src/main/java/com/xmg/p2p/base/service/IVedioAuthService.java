package com.xmg.p2p.base.service;

import java.util.HashMap;
import java.util.List;

import com.xmg.p2p.base.query.PageResult;
import com.xmg.p2p.base.query.VedioAuthQueryObject;

public interface IVedioAuthService {

	PageResult query(VedioAuthQueryObject qo);

	void audit(int state, Long loginInfoValue, String remark);

	List<HashMap<String, Object>> autoCompleteList(String keyword);

}
