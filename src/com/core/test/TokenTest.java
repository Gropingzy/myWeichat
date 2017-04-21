package com.core.test;

import com.core.bean.AccessToken;
import com.core.util.TokenUtil;

public class TokenTest {

	public static void main(String[] args) {
		AccessToken token = TokenUtil.getAccessToken();
		System.out.println("------------------------get token start----------------------");
		System.out.println(token.getToken());
		System.out.println(token.getExpiresIn());
	}

}
