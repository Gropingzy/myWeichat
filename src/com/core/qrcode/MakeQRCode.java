package com.core.qrcode;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class MakeQRCode {
	private static Logger log = Logger.getLogger(MakeQRCode.class);
	private static String filePath = "C:\\Log\\myWeichat1.0\\upload\\qrCode.png";
	
	public static void makeqr(String content){
		int width = 300;
		int height = 300;
		String format = "png";
		
		//定义二维码参数
		HashMap hints = new HashMap();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
		hints.put(EncodeHintType.MARGIN, 2);
		
		 try {
			BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
			Path file = new File(filePath).toPath();
			MatrixToImageWriter.writeToPath(bitMatrix, format, file); 
		} catch (Exception e) {
			log.error("MakeQRCode error!");
		}
	}
	
}
