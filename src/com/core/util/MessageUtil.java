package com.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.core.bean.Article;
import com.core.bean.Image;
import com.core.bean.Music;
import com.core.bean.MusicMessage;
import com.core.bean.NewsMessage;
import com.core.bean.ImageMessage;
import com.core.bean.TextMessage;
import com.core.bean.Video;
import com.core.bean.VideoMessage;
import com.thoughtworks.xstream.XStream;

public class MessageUtil {
	
	private static Logger log = Logger.getLogger(MessageUtil.class);
	
	public static final String MESSAGE_TEXT = "text";
	public static final String MESSAGE_NEWS = "news";
	public static final String MESSAGE_IMAGE = "image";
	public static final String MESSAGE_VOICE = "voice";
	public static final String MESSAGE_MUSIC = "music";
	public static final String MESSAGE_LINK = "link";
	public static final String MESSAGE_LOCATION = "location";
	public static final String MESSAGE_EVENT = "event";
	public static final String MESSAGE_SUSCRIBE = "subscribe";
	public static final String MESSAGE_UNSUBSCRIBE = "unsubscribe";
	public static final String MESSAGE_SCANCODEPUSH = "scancode_push";
	public static final String MESSAGE_SCANCODEWAITMSG = "scancode_waitmsg";
	public static final String MESSAGE_CLICK = "CLICK";
	public static final String MESSAGE_VIEW = "VIEW";
	
	/**
	 * xml转换成map集合
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,String> xmlToMap(HttpServletRequest req){
		Map<String,String> map = new HashMap<String,String>();
		SAXReader reader = new SAXReader();		
		try {
			InputStream in = req.getInputStream();
			Document document = reader.read(in);
			Element root = document.getRootElement();
			List<Element> list = root.elements();
			for(Element e : list){
				map.put(e.getName(), e.getText());
			}
			in.close();
		} catch (IOException e) { 
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 将文本、音乐消息对象转换成xml
	 * @return
	 */
	public static String messageToXml(Object messageObject){
		XStream xstream = new XStream();
		xstream.alias("xml", messageObject.getClass());
		return xstream.toXML(messageObject);
	}
	
	/**
	 * 生成文本消息
	 */
	public static String initText(String toUserName, String fromUserName, String content){
		TextMessage textMessage = new TextMessage();
		//接收消息发送方作为来源方
		textMessage.setFromUserName(toUserName);
		textMessage.setToUserName(fromUserName);
		textMessage.setMsgType(MessageUtil.MESSAGE_TEXT);
		textMessage.setCreateTime(new Date().getTime());
		textMessage.setContent(content);
		//将消息数据转换成xml
		return MessageUtil.messageToXml(textMessage);
	}
	
	/**
	 * 关注响应
	 */
	public static String menuText(){
		StringBuffer sb = new StringBuffer();
		sb.append("这么隐蔽都能被发现？那你厉害了，follow my tips：\n");
		sb.append("收图文:\t\"1\"\n");
		sb.append("看张图:\t\"2\"\n");
		sb.append("听首歌:\t\"3\"\n");
		sb.append("二维码:\t\"二维码：\"\n");
		sb.append("复读机:\t\"其它任意字符\"\n");
		sb.append("主菜单:\t\"?\"\n");
		sb.append("其它功能:\t自己探索吧/::D\n");
		return sb.toString();
	}
	
	/**
	 * 复读机功能
	 */
	public static String mirrorMenu(String content){
		StringBuffer sb = new StringBuffer();
		sb.append(content);
		return sb.toString();
	}
	
	/**
	 * 其他功能
	 */
	public static String otherType(){
		StringBuffer sb = new StringBuffer();
		sb.append("大爷还不会识别该类信息，再等两天吧。\n");
		return sb.toString();
	}
	
	/**
	 * 将文本消息对象转换成xml
	 * @return
	 * @throws ClassNotFoundException 
	 */
	public static String newsMessageToXml(NewsMessage newsMessage){
		XStream xstream = new XStream();
		xstream.alias("xml", newsMessage.getClass());
		try {
			xstream.alias("item", Class.forName("com.core.bean.Article"));		//new Article().getClass()
		} catch (ClassNotFoundException e) {
			log.error("NewsMessage To Xml:Class\"Article\" not found.");
		}
		return xstream.toXML(newsMessage);
	}
	
	/**
	 * 生成图文消息
	 */
	public static String initNewsMessage(String toUserName, String fromUserName){
		NewsMessage newsMessage = new NewsMessage();
		//接收消息发送方作为来源方
		newsMessage.setFromUserName(toUserName);
		newsMessage.setToUserName(fromUserName);
		newsMessage.setMsgType(MessageUtil.MESSAGE_NEWS);
		newsMessage.setCreateTime(new Date().getTime());
		newsMessage.setArticleCount(1);
		List<Article> articlesList = new ArrayList<Article>();
		
		StringBuilder sb = new StringBuilder();
		sb.append("微信公众平台是运营者通过公众号为微信用户提供资讯和服务的平台，而公众平台开发接口则是提供服务的基础，");
		sb.append("开发者在公众平台网站中创建公众号、获取接口权限后，可以通过阅读本接口文档来帮助开发。\n");
		sb.append("为了识别用户，每个用户针对每个公众号会产生一个安全的OpenID，如果需要在多公众号、移动应用之间做用户共通，");
		sb.append("则需前往微信开放平台，将这些公众号和应用绑定到一个开放平台账号下，绑定后，");
		sb.append("一个用户虽然对多个公众号和应用有多个不同的OpenID，但他对所有这些同一开放平台账号下的公众号和应用，");
		sb.append("只有一个UnionID，可以在用户管理-获取用户基本信息（UnionID机制）文档了解详情。");
		
		Article article = new Article();
		article.setTitle("你瞅啥");
		article.setDescription(sb.toString());
		article.setPicUrl("http://groping.ngrok.cc/myWeichat/resources/image/doge.jpg");	//http://groping.ngrok.cc/myWeichat/WebContent/resources/image/doge.jpg
		article.setUrl("www.nowcoder.com");
		articlesList.add(article);
		newsMessage.setArticles(articlesList);
		//将消息数据转换成xml
		return MessageUtil.newsMessageToXml(newsMessage);
	}
	
	/**
	 * 生成图片消息
	 */
	public static String initImageMessage(String toUserName, String fromUserName, String filePath){
		ImageMessage imageMessage = new ImageMessage();
		imageMessage.setFromUserName(toUserName);
		imageMessage.setToUserName(fromUserName);
		imageMessage.setCreateTime(new Date().getTime());
		imageMessage.setMsgType(MessageUtil.MESSAGE_IMAGE);
		String mediaId = null;
		try {
			mediaId = UploadUtil.uplaod(filePath, TokenUtil.getAccessToken().getToken(), "image");
		} catch (IOException e) {
			e.printStackTrace();
		}
		Image image = new Image();
		image.setMediaId(mediaId);
		
		imageMessage.setImage(image);
		
		return MessageUtil.messageToXml(imageMessage);
	}
	
	/**
	 * 生成音乐消息
	 */
	public static String initMusicMessage(String toUserName, String fromUserName){
		MusicMessage musicMessage = new MusicMessage();
		musicMessage.setFromUserName(toUserName);
		musicMessage.setToUserName(fromUserName);
		musicMessage.setCreateTime(new Date().getTime());
		musicMessage.setMsgType(MessageUtil.MESSAGE_MUSIC);
		
		Music music = new Music();
		music.setTitle("才华有限公司");
		music.setDescription("金岐玟");
		music.setMusicUrl("http://groping.ngrok.cc/myWeichat/resources/music/music1.mp3");
		music.setHQMusicUrl("http://groping.ngrok.cc/myWeichat/resources/music/music1.mp3");
		
		String mediaId = null;
		String filePath = "C:\\Log\\myWeichat1.0\\upload\\timg.jpg";
		try {
			mediaId = UploadUtil.uplaod(filePath, TokenUtil.getAccessToken().getToken(), "thumb");
		} catch (IOException e) {
			e.printStackTrace();
		}
		music.setThumbMediaId(mediaId);
		musicMessage.setMusic(music);
		return MessageUtil.messageToXml(musicMessage);
	}
	
	/**
	 * 生成视频消息
	 */
	public static String initVideoMessage(String toUserName, String fromUserName){
		VideoMessage videoMessage = new VideoMessage();
		
		Video video = new Video();
		String mediaId = null;
		String videoPath = "C:\\Log\\myWeichat1.0\\upload\\1.mp4";
		try {
			mediaId = UploadUtil.uplaod(videoPath, TokenUtil.getAccessToken().getToken(), "video");
		} catch (IOException e) {
			log.debug("get video mediaId error!");
		}
		
		video.setMediaId(mediaId);
		video.setTitle("心空空");
		video.setDescription("谢春花");
		
		videoMessage.setFromUserName(toUserName);
		videoMessage.setToUserName(fromUserName);
		videoMessage.setCreateTime(new Date().getTime());
		videoMessage.setMsgType("video");
		videoMessage.setVideo(video);
		
		//log.debug(MessageUtil.messageToXml(videoMessage));
		return MessageUtil.messageToXml(videoMessage);
	}
}
