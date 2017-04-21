package com.core.test;

import java.io.IOException;

import com.core.util.TokenUtil;
import com.core.util.UploadUtil;

public class MediaIdTest {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		String filePath = "C:\\Log\\myWeichat1.0\\upload\\timg.jpg";
		try {
			String mediaId = UploadUtil.uplaod(filePath, TokenUtil.getAccessToken().getToken(), "thumb");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
