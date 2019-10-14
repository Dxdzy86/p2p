package com.xmg.p2p.test;

import com.xmg.p2p.base.util.BitStatesUtil;

public class BitStatesUtilTest {
	public static final Long OP_BIND_PHONE = (1L)<<1;
	public static boolean hasState(Long bitState, Long otherBitState) {
		return (bitState & otherBitState)!=0;
	}
	
	public static boolean getHasBindPhone() {
		return BitStatesUtil.hasState(0L,BitStatesUtil.OP_BIND_PHONE);
	}
	
	public static void main(String[] args) {
		System.out.println(getHasBindPhone());
	}
}
