package com.xmg.p2p.base.query;

import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
@Setter@Getter
public class PageResult {
	private List<?> data;
	private Integer prevPage;
	private Integer nextPage;
	private Integer totalPage;
	private Integer totalCount;
	
	public PageResult(List<?> data,Integer totalCount,Integer currentPage,Integer pageSize) {
		this.data = data;
		this.totalCount = totalCount;
		this.totalPage = (this.totalCount%pageSize)==0?this.totalCount/pageSize:this.totalCount/pageSize+1;
		this.prevPage = currentPage-1 > 0?currentPage-1:1;
		this.nextPage = currentPage+1 < this.totalPage?currentPage+1:this.totalPage;
	}
	
	//totalPage有可能为0，因此需要特别处理一下
	public Integer getTotalPage() {
		return this.totalPage == null ? 1:this.totalPage;
	}
	
	public PageResult(List<?> data,Integer totalCount) {
		this.data = data;
		this.totalCount = totalCount;
	}
	
	public static PageResult empty() {
		return new PageResult(Collections.EMPTY_LIST,0);
	}
}
