package com.xmg.p2p.base.util;
/**
 * 用户状态码工具类
 *	判断一个用户是否已经绑定身份证、手机、邮箱，是否是会员。这里采用位状态的方式。
 *	用户初始的状态码为0000 0000 0000 0000
 *	用户的手机状态码为0000 0000 0000 0010
 *	用户的邮箱状态码为0000 0000 0000 0100
 *	1、为当前用户绑定手机，初始状态码||手机状态码(用户状态码=0000 ... 0010)
 *	2、判断当前用户是否绑定邮箱，用户状态码&邮箱状态码(0010&0100=0000)。若>0则已经绑定邮箱，否者没有
 *	3、移除用户拥有的某个状态码，用户状态码^手机状态码(0010^0010=0000)
 * @author Dxd
 */
public class BitStatesUtil {
	//public static final Long OP_BASIC_INFO = 1L;//用户注册时给用户一个默认的状态值
	public static final Long OP_BIND_PHONE = (1L)<<1;//用户绑定手机的状态码(2)
	public static final Long OP_BIND_EMAIL = (1L)<<2;//用户绑定邮箱状态码(4)
	public static final Long OP_SAVE_BASIC_INFO = (1L)<<3;//用户填写基本信息状态码(8)
	public static final Long OP_IS_IDENTITY_AUTH = (1L)<<4;//用户身份认证(16)
	public static final Long OP_IS_VEDIO_AUTH = (1L)<<5;//用户视频认证(32)
	public static final Long OP_BID_REQUEST_PROCESS = (1L)<<6;//用户是否有一个借款正在处理中(64)
	
	public static final int CREDIT_BORROW_SCORES = 30;//信用标认证分数达到30以上才可以申请借款
	/**
	 * @param bitState	用户当前的状态码
	 * @param opBindPhone	需要添加的状态码
	 * @return	判断用户是否含有手机状态码
	 */
	public static boolean hasState(Long bitState, Long otherBitState) {
		return (bitState & otherBitState)!=0;
	}
	/**
	 * 
	 * @param bitState	用户当前状态码
	 * @param otherBitState	需要添加的状态码
	 * @return 添加之后的用户状态码
	 */
	public static Long addState(Long bitState,Long otherBitState) {
		if(hasState(bitState,otherBitState)) {
			return bitState;
		}
		return (bitState | otherBitState);
	}
	/**
	 * 
	 * @param states 用户当前的状态码
	 * @param otherBitState 需要移除的状态码
	 * @return
	 */
	public static long removeState(long states, long otherBitState) {
		if (!hasState(states, otherBitState)) {
			return states;
		}
		return states ^ otherBitState;
	}
}
