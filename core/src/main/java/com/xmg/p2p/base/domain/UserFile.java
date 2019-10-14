package com.xmg.p2p.base.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.type.Alias;

import com.alibaba.fastjson.JSONObject;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
@Alias("UserFile")
public class UserFile extends BaseDomain{
	public static final int STATE_APPLY = 0;//审核状态
	public static final int STATE_PASS = 1;//审核通过
	public static final int STATE_REJECT = 2;//审核拒绝
	
	private int state = UserFile.STATE_APPLY;//默认风控资料审核的状态
	private String remark;//审核时的备注信息
	private Date applyTime;//申请时间
	private Date auditTime;//审核时间
	private LoginInfo applier;//申请人
	private LoginInfo auditor;//审核人
	private String file;//风控材料(图片地址)
	private SystemDictionaryItem fileType;//风控材料类型
	private int score;//风控分数
	
	/**
	 * 用于显示风控资料的审核状态
	 * @return
	 */
	public String getStateDisplay() {
		switch(this.state) {
			case 0:
				return "待审核";
			case 1:
				return "审核通过";
			case 2:
				return "审核拒绝";
		}
		return "发生未知的异常，请联系系统管理员";
	}
	
	public String getJsonString() {
		Map<String,Object> map = new HashMap<>();
		map.put("id", super.getId());
		map.put("applier", this.applier.getUsername());
		map.put("fileType", this.fileType.getTitle());
		map.put("file", this.file);
		return JSONObject.toJSONString(map);
	}
}
