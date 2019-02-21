package com.yonyou.util.validator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.json.JSONObject;

import com.yonyou.util.validator.bulid.IBulidValidFileds;
import com.yonyou.util.validator.bulid.IBulidValidHead;
import com.yonyou.util.validator.bulid.imp.BulidValidFileds;
import com.yonyou.util.validator.bulid.imp.BulidValidHead;
import com.yonyou.util.validator.mark.BulidValidMarkAbs;
import com.yonyou.util.validator.mark.imp.BulidValidMark;
import com.yonyou.util.validator.rale.IBulidFilesRule;
import com.yonyou.util.validator.rale.imp.BulidFilesRule;

public class BulidValidMain {
	
	
	private static IBulidValidHead bulidHead =new BulidValidHead();
	private static IBulidValidFileds bulidFiles =new BulidValidFileds();
	private static IBulidFilesRule bulidRale =new BulidFilesRule();
	private static BulidValidMarkAbs bulidMark =new BulidValidMark();
	
	static{
		//模板初始化
		bulidMark.init();
	}
	public static String findValidString(Map<String,ConcurrentHashMap<String, String>> filedMap){
		
		JSONObject obj =findValidJson(filedMap);
		if(obj!=null){
			return obj.toString();
		}else{
			return "";
		}
	}
	
	public static JSONObject findValidJson(Map<String,ConcurrentHashMap<String, String>> filedMap){
		JSONObject obj =new JSONObject();
		//添加头信息
		bulidHead.appendJson(obj, bulidMark);
		//添加字段信息
		bulidFiles.appendJson(obj, filedMap, bulidRale, bulidMark);
		return obj;
	}
	
}
