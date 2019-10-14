package com.xmg.p2p.base.query;

import org.springframework.util.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class SystemDictionaryQueryObject extends QueryObject{
	private String keyword;
	
	public String getKeyword() {
		return StringUtils.hasLength(keyword) ? keyword : null;
	}
}
