package com.xmg.p2p.base.domain;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class SystemDictionaryItem extends BaseDomain{
	private Long parentId;//字典明细对应的分类id
	private String title;//字典明细的名称
	//private String intro;//明细的使用说明
	//private String tvalue;//明细的可选值
	private Integer sequence;//字典明细在该分类中的排序
}
