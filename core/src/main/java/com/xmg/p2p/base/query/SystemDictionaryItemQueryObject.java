package com.xmg.p2p.base.query;

import org.springframework.util.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class SystemDictionaryItemQueryObject extends QueryObject{
	private String keyword;
	private String parentId;
	
	public String getKeyword() {
		return StringUtils.hasLength(this.keyword)?this.keyword:null;
	}
}
