package com.core.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.core.qrcode.MakeQRCode;
import com.core.util.InitUtil;
import com.core.util.MessageUtil;

@SuppressWarnings("serial")
public class MessageServlet extends HttpServlet{
	
	private Logger log = Logger.getLogger(MessageServlet.class);
	/**
	 * 接入微信后台
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String signature = req.getParameter("signature");
		String timestamp = req.getParameter("timestamp");
		String nonce = req.getParameter("nonce");
		String echostr = req.getParameter("echostr");
		
		PrintWriter out = resp.getWriter();
		if(InitUtil.SignatureCheck(signature, timestamp, nonce)){
			out.print(echostr);
		}
	}
	
	/**
	 * 消息接收与响应
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		
		PrintWriter out = resp.getWriter();
		//转换成Map类型
		Map<String,String> map = MessageUtil.xmlToMap(req);
		
		//从Map中取到消息属性
		String fromUserName = map.get("FromUserName");
		String toUserName = map.get("ToUserName");
		String msgType = map.get("MsgType");
		String content = map.get("Content");
		String createTime = map.get("CreateTime");
		SimpleDateFormat sdf = new SimpleDateFormat();
		String time = sdf.format(new Date(Long.valueOf(createTime) * 1000));
		//location类型取得属性
		String label = map.get("Label");
		String locationX = map.get("Location_X");
		String locationY = map.get("Location_Y");
		//图片url
		String picUrl = map.get("PicUrl");
		
		//响应消息
		String message = null;
		if(MessageUtil.MESSAGE_TEXT.equals(msgType)){
			//图文消息
			if("1".equals(content)){
				message = MessageUtil.initNewsMessage(toUserName, fromUserName);
			//图片消息
			}else if("2".equals(content)){
				String filePath = "C:\\Log\\myWeichat1.0\\upload\\selector.jpg";
				message = MessageUtil.initImageMessage(toUserName, fromUserName, filePath);
			//音乐消息
			}else if("3".equals(content)){
				message = MessageUtil.initMusicMessage(toUserName, fromUserName);
			//视频消息
			}else if("4".equals(content)){
				System.out.println("--------------video map--------------"+map);
				message = MessageUtil.initVideoMessage(toUserName, fromUserName);
			//小菊花
			}else if("你是一朵小菊花".equals(content)){
				message = MessageUtil.initText(toUserName, fromUserName, "么么么么哒");
			//临时生成二维码入口	
			}else if(content.startsWith("二维码：")){
				String filePath = "C:\\Log\\myWeichat1.0\\upload\\qrCode.png";
				String qrCode = content.replace("二维码：", "");
				if(!"".equals(qrCode)){
					MakeQRCode.makeqr(qrCode);
					message = MessageUtil.initImageMessage(toUserName, fromUserName, filePath);
				}else{
					message = MessageUtil.initText(toUserName, fromUserName, "请输入\"二维码：\"\'要生成二维码的内容\'");
				}
			//文本消息
			}else if("?".equals(content) || "？".equals(content)){
				message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());
			//文本消息
			}else{
				message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.mirrorMenu(content));
			}
		//事件消息
		}else if(MessageUtil.MESSAGE_EVENT.equals(msgType)){
			String eventType = map.get("Event");
			String eventKey = map.get("EventKey");
			//订阅消息
			if(MessageUtil.MESSAGE_SUSCRIBE.equals(eventType)){
				message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());
			}
			//扫码消息
			if(MessageUtil.MESSAGE_SCANCODEWAITMSG.equals(eventType)){
				message = MessageUtil.initText(toUserName, fromUserName, "现在是：" + time);
			}
			//点击事件
			if(MessageUtil.MESSAGE_CLICK.equals(eventType)){
				log.debug("--------click event map---------" + map);
				if(eventKey.equals("cbt4")){
					message = MessageUtil.initText(toUserName, fromUserName, "请输入内容生成相应的二维码，格式：\n\"二维码：\'要生成的内容\'\"");
				}else{
					message = MessageUtil.initText(toUserName, fromUserName, "translate function is developing...");
				}
			}
		//位置消息
		}else if(MessageUtil.MESSAGE_LOCATION.equals(msgType)){
			String locationStr = "位置：" + label + "\n" + "东经：" + locationX + "\n" + "北纬：" + locationY;
			message = MessageUtil.initText(toUserName, fromUserName, locationStr);
		//图片消息
		}else if(MessageUtil.MESSAGE_IMAGE.equals(msgType)){
			message = MessageUtil.initText(toUserName, fromUserName, picUrl);
		}
		//语音消息
		else if(MessageUtil.MESSAGE_VOICE.equals(msgType)){
				message = MessageUtil.initText(toUserName, fromUserName, "马什么梅？");
		//其它消息
		}else{
			message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.otherType());
		}
		//将消息响应输出
		log.debug("***********************************最终生成的message：  " + message);
		
		out.print(message);
		out.close();
	}
	
}
