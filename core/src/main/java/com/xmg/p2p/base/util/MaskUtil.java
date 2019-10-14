package com.xmg.p2p.base.util;

public class MaskUtil {
	
	public static String getAnonymousRealName(String name) {
		int length = name.length();
		if(length>=2) {
			String annonymousName = name.substring(0, length/2);
			for(int i=length/2;i<length;i++) {
				annonymousName += "*";
			}
			return annonymousName;
		}
		return name;
	}
	
	public static String getAnonymousIdNumber(String idNumber) {
		int length = idNumber.length();
		String annonymousIdNumber = idNumber.substring(0, length/2);
		for(int i=length/2;i<length;i++) {
			annonymousIdNumber += "*";
		}
		return annonymousIdNumber;
	}

	public static String getAnonymousAddress(String address) {
		int length = address.length();
		String annonymousAddress = address.substring(0, length/2);
		for(int i=length/2;i<length;i++) {
			annonymousAddress += "*";
		}
		return annonymousAddress;
	}
}
