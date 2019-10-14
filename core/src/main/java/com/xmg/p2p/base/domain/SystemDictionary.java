package com.xmg.p2p.base.domain;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class SystemDictionary extends BaseDomain{
	private String sn;//数据字典分类编码
	private String title;//数据字典分类名称
	//private String intro;//数据字典分类使用说明
}
