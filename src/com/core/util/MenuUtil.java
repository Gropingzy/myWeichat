package com.core.util;

import net.sf.json.JSONObject;

import com.core.bean.menu.Button;
import com.core.bean.menu.ClickButton;
import com.core.bean.menu.Menu;
import com.core.bean.menu.ViewButton;

public class MenuUtil {
	//生成菜单URL
	private static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	//删除菜单URL
	private static final String DELETE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
	
	/**
	 * 创建一个二级菜单，一级：一个view，一个click
	 * 				    二级：click下有五个：扫码，微信图片，地理位置，生成二维码，百度翻译
	 * @return
	 */
	public static Menu initMenu(){
		Menu menu = new Menu();
		
		ClickButton clickBt1 = new ClickButton();
		clickBt1.setName("备份图片");
		clickBt1.setType("pic_weixin");
		clickBt1.setKey("cbt1");
		
		ClickButton clickBt2 = new ClickButton();
		clickBt2.setName("来定个位");
		clickBt2.setType("location_select");
		clickBt2.setKey("cbt2");
		
		ClickButton clickBt3 = new ClickButton();
		clickBt3.setName("扫码&看时间");
		clickBt3.setType("scancode_waitmsg");
		clickBt3.setKey("cbt3");
		
		ClickButton clickBt4 = new ClickButton();
		clickBt4.setName("diy二维码");
		clickBt4.setType("click");
		clickBt4.setKey("cbt4");
		
		ClickButton clickBt5 = new ClickButton();
		clickBt5.setName("百度翻译");
		clickBt5.setType("click");
		clickBt5.setKey("cbt5");
		
		Button cButton = new Button();
		cButton.setName("小功能1.0");
		cButton.setType("click");
		cButton.setSub_button(new Button[]{clickBt1,clickBt2,clickBt3,clickBt4,clickBt5});
		
		ViewButton vButton = new ViewButton();
		vButton.setName("学习更多姿势");
		vButton.setType("view");
		vButton.setUrl("http://www.icourse163.org/");
		
		menu.setButton(new Button[]{vButton,cButton});
		
		return menu;
	}
	
	/**
	 * post请求创建menu
	 */
	public static void createMenu(String menu){
		String url = MenuUtil.CREATE_MENU_URL.replace("ACCESS_TOKEN", TokenUtil.getAccessToken().getToken());
		JSONObject jsonObj = TokenUtil.doPostStr(url, menu);
		if("ok".equals(jsonObj.getString("errmsg"))){
			System.out.print("create menu success!");
		}
	}
	
	/**
	 * get请求删除menu
	 */
	public static void deleteMenu(){
		String url = MenuUtil.DELETE_MENU_URL.replace("ACCESS_TOKEN", TokenUtil.getAccessToken().getToken());
		JSONObject jsonObj = TokenUtil.doGetStr(url);
		if("ok".equals(jsonObj.getString("errmsg"))){
			System.out.print("delete menu success!");
		}
	}
	
}
