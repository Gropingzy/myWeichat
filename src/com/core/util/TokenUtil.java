package com.core.util;


import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.core.bean.AccessToken;

import net.sf.json.JSONObject;

public class TokenUtil {

	@SuppressWarnings("unused")
	private static final String APPID = "wxe3eb524fd74933aa";
	@SuppressWarnings("unused")
	private static final String APPSECRET = "1826e74abeae004b5c711da5f1bd3b7c";
	
	private static final String APPID_TEST = "wxe849b21508b26db4";
	private static final String APPSECRET_TEST = "b1e7ef29e35e62dca5439fce2737f5a2";
	private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	
	public static JSONObject doGetStr(String url){
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		JSONObject jsonObject = null;
		try {
			HttpResponse response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			if(entity != null){
				String result = EntityUtils.toString(entity,"UTF-8");
				jsonObject = JSONObject.fromObject(result);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	public static JSONObject doPostStr(String url, String outStr){
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		JSONObject jsonObject = null;
		try {
			httpPost.setEntity(new StringEntity(outStr,"UTF-8"));
			HttpResponse response = httpClient.execute(httpPost);
			String result = EntityUtils.toString(response.getEntity(),"UTF-8");
			jsonObject = JSONObject.fromObject(result);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	public static AccessToken getAccessToken(){
		AccessToken token = new AccessToken();
		String url = TokenUtil.ACCESS_TOKEN_URL.replace("APPID", APPID_TEST).replace("APPSECRET", APPSECRET_TEST);
		JSONObject jsonObject = doGetStr(url);
		if(jsonObject != null){
			token.setToken((String)jsonObject.get("access_token"));
			token.setExpiresIn((int)jsonObject.get("expires_in"));
		}
		return token;
	}
}
