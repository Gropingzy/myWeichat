package com.core.util;

import java.util.Arrays;

import org.apache.commons.codec.digest.DigestUtils;

public class InitUtil {
	public static final String UTIL_TOKEN = "myWeichat";
	public static boolean SignatureCheck(String signature,String timestamp,String nonce){
		String[] params = new String[]{UTIL_TOKEN,timestamp,nonce};
		//1）将token、timestamp、nonce三个参数进行字典序排序
		Arrays.sort(params);
		//2）将三个参数字符串拼接成一个字符串进行sha1加密
		StringBuffer sortedParams = new StringBuffer();
		for(String param : params){
			sortedParams.append(param);
		}
		String sha1String = DigestUtils.sha1Hex(sortedParams.toString());
		//3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
		boolean checkFlag = signature.equals(sha1String);
		return checkFlag;
	}
}
