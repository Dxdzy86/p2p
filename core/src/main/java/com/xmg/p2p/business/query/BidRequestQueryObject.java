package com.xmg.p2p.business.query;

import com.xmg.p2p.base.query.QueryObject;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class BidRequestQueryObject extends QueryObject{
	
	int bidRequestState = -1;//标的状态
	
	//投标中；还款中；已完成的标
	int[] bidRequestStatesArray;
	//按照什么方式排序
	String orderBy;
	//升序还是降序
	String orderByType;
}
