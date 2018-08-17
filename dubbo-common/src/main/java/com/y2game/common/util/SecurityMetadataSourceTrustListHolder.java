package com.y2game.common.util;

import java.util.List;

public class SecurityMetadataSourceTrustListHolder {
	
	private static List<String> falsetList  = null;
	private static String success  = "swagger";
	private static String success1  = "login";
	private static String success2  = "api-docs";
	private static String success3  = "sdk";

	
	public static boolean isTrustSecurityMetadataSource(String url){

		if(url.contains(success)|| url.contains(success1)|| url.contains(success2)|| url.contains(success3)){
			return true;
		}
		return false;
	}
}
