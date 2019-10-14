package com.xmg.p2p.base.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.type.Alias;

import com.alibaba.fastjson.JSONObject;
import com.xmg.p2p.base.util.MaskUtil;
import com.xmg.p2p.base.util.UserContext;

import lombok.Getter;
import lombok.Setter;
@Setter@Getter
@Alias("RealAuth")
public class RealAuth extends BaseDomain{
	public static final int SEX_MALE = 0;//男
	public static final int SEX_FEMALE = 1;//女
	
	public static final int STATE_APPLY = 0;//审核状态
	public static final int STATE_PASS = 1;//审核通过
	public static final int STATE_REJECT = 2;//审核拒绝
	
	private String realName;//真实姓名
	private int sex = RealAuth.SEX_MALE;//性别
	private String idNumber;//身份证号码
	private String birthDate="";//出生日期
	private String address="";//家庭住址
	private String image1;//身份证正面照
	private String image2;//身份证反面照
	
	private int state = RealAuth.STATE_APPLY;//审核状态
	private LoginInfo applier;//申请人
	private LoginInfo auditor;//审核人
	private Date applyTime;//申请时间
	private Date auditTime;//审核时间
	private String remark;//审核备注信息
	
	//前台性别显示
	public String getSexDisplay() {
		return this.sex == 0 ? "男":"女";
	}
	
	//前台审核状态显示
	public String getStateDisplay() {
		switch(this.state) {
			case STATE_APPLY:
				return "审核状态";
			case STATE_PASS:
				return "审核通过";
			case STATE_REJECT:
				return "审核失败";
			default:
				return "错误状态";
		}
	}
	
	//点击审核时，数据回显
	public String getJsonString() {
		Map<String,Object> map = new HashMap<>();
		map.put("id", super.getId());
		map.put("username",UserContext.getCurrentUser().getUsername());
		map.put("realName", this.realName);
		map.put("sex", this.getSexDisplay());
		map.put("idNumber", this.idNumber);
		map.put("address", this.address);
		map.put("birthDate", this.birthDate);
		map.put("image1",this.image1);
		map.put("image2",this.image2);
		
		return JSONObject.toJSONString(map);
	}
	
	//真实姓名打码
	public String getAnonymousRealName() {
		return MaskUtil.getAnonymousRealName(realName);
	}
	
	//身份证号打码
	public String getAnonymousIdNumber() {
		return MaskUtil.getAnonymousIdNumber(idNumber);
	}
	
	public String getAnonymousAddress() {
		return MaskUtil.getAnonymousAddress(address);
	}
}
