package com.core.test;

import net.sf.json.JSONObject;

import com.core.util.MenuUtil;

public class MenuTest {
	public static void main(String[] args){
		
		String menu = JSONObject.fromObject(MenuUtil.initMenu()).toString();
		//生成菜单
		MenuUtil.createMenu(menu);
		//删除菜单
		//MenuUtil.deleteMenu();
	}
}
