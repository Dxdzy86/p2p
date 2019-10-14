package com.xmg.p2p.business.domain;

import java.util.Date;

import com.xmg.p2p.base.domain.BaseDomain;
import com.xmg.p2p.base.domain.LoginInfo;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class BidRequestAuditHistory extends BaseDomain{
	public static final int STATE_NORMAL = 0;// 正常
	public static final int STATE_AUDIT = 1;// 审核通过
	public static final int STATE_REJECT = 2;// 审核拒绝
	
	public static final int PUBLISH_AUDIT = 0;//发标审核
	public static final int FULL_AUDIT_1 = 1;//满标一审
	public static final int FULL_AUDIT_2 = 2;//满标二审
	
	private int state=STATE_NORMAL;//标的审核状态(审核通过or审核拒绝)
	private String remark;//审核意见
	private Date auditTime;//审核时间
	private Date applyTime;//申请时间
	private LoginInfo applier;//申请人
	private LoginInfo auditor;//审核人
	
	private Long bidRequestId;//审核的是哪一个标
	private int auditType;//审核标的类型(发标审核、满标一审、满标二审)
	
	public String gerStateDisplay() {
		switch(state) {
			case STATE_NORMAL:
				return "待审核";
			case STATE_AUDIT:
				return "审核通过";
			case STATE_REJECT:
				return "审核拒绝";
			default:
				return "";
		}
	}
	
	public String getAuditTypeDisplay() {
		switch(auditType) {
			case PUBLISH_AUDIT:
				return "发标审核";
			case FULL_AUDIT_1:
				return "满标一审";
			case FULL_AUDIT_2:
				return "满标二审";
			default:
				return "";
		}
	}
}
