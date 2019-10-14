package com.xmg.p2p.base.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
/**
 * 视频认证对象
 * @author Dxd
 *
 */
@Setter@Getter
public class VedioAuth extends BaseDomain{
	public static final int STATE_PASS = 1;
	public static final int STATE_REJECT = 2;
	
	private int state = -1;
	private String remark;
	private LoginInfo applier;
	private LoginInfo auditor;
	private Date applyTime;
	private Date auditTime;
	
	public String getStateDisplay() {
		switch(state) {
			case STATE_PASS:
				return "审核通过";
			case STATE_REJECT:
				return "审核拒绝";
			default:
				return "状态错误";
		}
	}
}
