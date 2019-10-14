package com.xmg.p2p.base.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.type.Alias;
import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.JSONObject;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
@Alias("RechargeOffline")
public class RechargeOffline extends BaseDomain{
	public static final int STATE_APPLY = 0;//审核状态
	public static final int STATE_PASS = 1;//审核通过
	public static final int STATE_REJECT = 2;//审核拒绝
	
	public int state = STATE_APPLY;//账单的状态
	public PlatformBankInfo bankInfo;//平台账号
	public String remark;//账单备注信息
	public LoginInfo applier;//转账用户
	public LoginInfo auditor;//被转账用户
	public Date applyTime;//转账时间
	public Date auditTime;//审核时间
	public String tradeCode;//交易号
	public Date tradeTime;//交易时间
	public BigDecimal amount;//交易金额
	public String note;//转账备注
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	public void setTradeTime(Date tradeTime) {
		this.tradeTime = tradeTime;
	}
	
	public String getStateDisplay() {
		switch(state) {
			case STATE_APPLY:return "审核中";
			case STATE_PASS:return "审核通过";
			case STATE_REJECT:return "审核拒绝";
			default:
				return "";
		}
	}
	
	public String getJsonString() {
		Map<String,Object> map = new HashMap<>();
		map.put("id", super.getId());
		map.put("username", applier.getUsername());
		map.put("amount", amount);
		map.put("tradeTime", tradeTime);
		map.put("tradeCode", tradeCode);
		
		return JSONObject.toJSONString(map);
	}
}
