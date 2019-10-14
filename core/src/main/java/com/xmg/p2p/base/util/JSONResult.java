package com.xmg.p2p.base.util;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class JSONResult{
	private boolean success = true;
	private String msg = null;
	public JSONResult() {}
	public JSONResult(Boolean success,String msg) {
		this.success = success;
		this.msg = msg;
	}
}
