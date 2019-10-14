package com.xmg.p2p.base.domain;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class PlatformBankInfo extends BaseDomain{
	private String bankName;//银行名称
	private String bankNumber;//银行账号
	private String accountName;//帐户名
	private String bankForkName;//开户支行
	
	public String getJsonString() {
		Map<String,Object> map = new HashMap<>();
		
		map.put("id", super.getId());
		map.put("bankName", this.bankName);
		map.put("bankNumber", this.bankNumber);
		map.put("accountName", this.accountName);
		map.put("bankForkName", this.bankForkName);
		
		return JSONObject.toJSONString(map);
	}
}
