package com.xmg.p2p.base.domain;

import com.xmg.p2p.base.util.BitStatesUtil;
import com.xmg.p2p.base.util.MaskUtil;

import lombok.Getter;
import lombok.Setter;
/**
 * 用户基本信息
 * @author Dxd
 *
 */
@Setter@Getter
public class UserInfo extends BaseDomain{
	private int version;//版本号
	private Long bitState=0L;//状态值
	private String realName;//真实名称
	private String idNumber;//身份证号
	private String phoneNumber="";//电话
	private String email;//邮箱
	private int creditBorrowScore = 0;//信用认证分数
	
	private Long realAuthId;//通过用户关联到所对应的是哪个实名认证信息
	
	private SystemDictionaryItem incomeGrade;
	private SystemDictionaryItem marriage;
	private SystemDictionaryItem kidCount;
	private SystemDictionaryItem educationBackground;
	private SystemDictionaryItem houseCondition;
	
	//用户是否绑定手机
	public boolean getHasBindPhone() {
		return BitStatesUtil.hasState(this.bitState,BitStatesUtil.OP_BIND_PHONE);
	}
	
	//用户是否绑定邮箱
	public boolean getHasBindEmail() {
		return BitStatesUtil.hasState(this.bitState, BitStatesUtil.OP_BIND_EMAIL);
	}
	
	//用户是否填写基本信息
	public boolean getHasSaveBasicInfo() {
		return BitStatesUtil.hasState(this.bitState,BitStatesUtil.OP_SAVE_BASIC_INFO);
	}
	
	//用户的身份是否已经认证
	public boolean getHasIdentityAuth() {
		return BitStatesUtil.hasState(this.bitState, BitStatesUtil.OP_IS_IDENTITY_AUTH);
	}
	
	//用户是否进行了视频认证
	public boolean getHasVedioAuth() {
		return BitStatesUtil.hasState(this.bitState, BitStatesUtil.OP_IS_VEDIO_AUTH);
	}
	
	//用户是否有一个借款正在处理当中
	public boolean getHasBidRequestInProcess() {
		return BitStatesUtil.hasState(this.bitState, BitStatesUtil.OP_BID_REQUEST_PROCESS);
	}
	
	//隐藏用户真实姓名
	public String getAnonymousRealName() {
		return MaskUtil.getAnonymousRealName(this.realName);
	}
	
	//隐藏用户身份证号码
	public String getAnonymousIdNumber() {
		return MaskUtil.getAnonymousIdNumber(this.idNumber);
	}
	
	//添加绑定验证码
	public void addBitState(Long otherBitState) {
		this.setBitState(BitStatesUtil.addState(this.bitState, otherBitState));
	}
	
	//移除绑定的状态码
	public Long removeBitState(Long opBidRequestProcess) {
		return BitStatesUtil.removeState(this.bitState, opBidRequestProcess);
	}
}
