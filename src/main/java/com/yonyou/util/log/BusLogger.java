package com.yonyou.util.log;

/** 
 * @author zzh
 * @version 创建时间：2017年3月2日
 * 记录系统的日志
 */
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.yonyou.business.entity.TokenEntity;
import com.yonyou.business.entity.TokenUtil;
import com.yonyou.util.DateUtil;
import com.yonyou.util.SpringContextUtil;

public class BusLogger {

	// 表及表字段常量
	public static final String LOG_TABLE_NAME = "RM_LOG";
	public static final String LOG_ID = "ID";
	public static final String LOG_TYPE_ID = "LOG_TYPE_ID";
	public static final String LOG_ACTION_DATE = "ACTION_DATE";
	public static final String LOG_ACTION_IP = "ACTION_IP";
	public static final String LOG_ACTION_MODULE = "ACTION_MODULE";
	public static final String LOG_ACTION_TYPE = "ACTION_TYPE";
	public static final String LOG_USER_ID = "USER_ID";
	public static final String LOG_USER_ID_NAME = "USER_ID_NAME";
	// 保存请求日志的唯一标示UUID，业务流水号（BU:服务器ip:UUID:访问客户ip:其他标识信息）等
	public static final String LOG_ACTION_UUID = "ACTION_UUID";
	public static final String LOG_CONTENT = "CONTENT";
	public static final String LOG_USABLE_STATUS = "USABLE_STATUS";
	public static final String LOG_MODIFY_DATE = "MODIFY_DATE";
	public static final String LOG_MODIFY_IP = "MODIFY_IP";
	public static final String LOG_MODIFY_USER_ID = "MODIFY_USER_ID";
	public static final String LOG_TS = "TS";
	// 存放 菜单名或者按钮ButtonToken 信息
	public static final String LOG_OPERATION_INFO = "OPERATION_INFO";
	// 登陆组织]
	public static final String LOG_OWNER_ORG_ID = "OWNER_ORG_ID";

	/**
	 * 业务日志处理
	 */
	private static Action2DbLogBuffer dbLog = new Action2DbLogBuffer();

	// 记录按钮操作信息
	static String user_id;// 用户ID
	static String user_id_name;// 用户名称
	static String owner_org_id;// 组织ID
	static String content;// url
	static String operation_info;// 存放 菜单名或者按钮ButtonToken 信息

	/**
	 * 手动记录日志进入数据库，适用于记录重要的业务日志
	 *
	 * @param action_module
	 *            日志模块名
	 * @param content
	 *            日志内容
	 */
	public static void record_log(String action_module,String content, String operation_info,
			HttpServletRequest request, HttpServletResponse response) {

	
		long start = System.currentTimeMillis();
		
		HashMap<String, String> map = new HashMap();

		TokenEntity tEntity = TokenUtil.initTokenEntity(request);

		user_id = tEntity.USER.getId(); // 用户ID
		user_id_name = tEntity.USER.getName();// 用户名称
		owner_org_id = tEntity.COMPANY.getCompany_id(); // 组织ID

		if (action_module.equals("1")) {
			map.put(LOG_ACTION_MODULE, "按钮操作");
		}
		if (action_module.equals("0")) {
			map.put(LOG_ACTION_MODULE, "菜单操作");// 菜单状态另写
		} else {
			map.put(LOG_ACTION_MODULE, "操作状态异常");
		}
		map.put(LOG_ACTION_TYPE, "visit_log");
		map.put(LOG_USER_ID, user_id);
		map.put(LOG_USER_ID_NAME, user_id_name);
		map.put(LOG_OWNER_ORG_ID, owner_org_id);
		map.put(LOG_CONTENT, content);
		map.put(LOG_OPERATION_INFO, operation_info);
		
		
		
		map.put(LOG_ACTION_DATE, DateUtil.getNowTimestamp());
		map.put(LOG_ACTION_MODULE, action_module);

		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		String ip = addr.getHostAddress();

		map.put(LOG_ACTION_IP, ip);
		map.put(LOG_ACTION_UUID, Thread.currentThread().getName());
		map.put(LOG_USABLE_STATUS, "1");
		map.put(LOG_TS, DateUtil.getNowTimestamp());
		
		dbLog.add(map);
		System.err.println("busLogger======>>>time:"+(System.currentTimeMillis()-start) +";content:"+ map.toString());

	}

	public static void log(String action_module, String content) {

		
		HashMap map = new HashMap();
		
		map.put(LOG_TYPE_ID, "");
		map.put(LOG_ACTION_DATE, DateUtil.getNowTimestamp());
		map.put(LOG_ACTION_MODULE, action_module);
		map.put(LOG_ACTION_TYPE, "log");

		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		String ip = addr.getHostAddress();

		map.put(LOG_ACTION_IP, ip);
		map.put(LOG_USER_ID, "");
		map.put(LOG_USER_ID_NAME, "");
		map.put(LOG_ACTION_UUID, Thread.currentThread().getName());
		map.put(LOG_CONTENT, content);
		map.put(LOG_USABLE_STATUS, "1");
		map.put(LOG_TS, DateUtil.getNowTimestamp());

		dbLog.add(map);
		System.out.println("busLogger======" + map.toString());

	}
	/**
	 * 手动记录日志进入数据库，适用于记录重要的业务日志
	 *
	 * @param action_module
	 *            日志模块名
	 * @param content
	 *            日志内容
	 */
	public static void record_log(HashMap map) {
		
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		String ip = addr.getHostAddress();

		map.put(LOG_ACTION_IP, ip);
		
		map.put(LOG_USABLE_STATUS, "1");
		map.put(LOG_TS, DateUtil.getNowTimestamp());	
		map.put(LOG_ACTION_UUID, Thread.currentThread().getName());
		map.put(LOG_ACTION_DATE, DateUtil.getNowTimestamp());
		
		dbLog.add(map);

	}
	
//	public static void log(HashMap map) {
//		dbLog.add(map);
//
//	}


	public static Logger getLogger(String className) {
		return Logger.getLogger(className);
	}

	public static Logger getLogger(Class class_) {
		return getLogger(class_.getName());
	}

	public static void debug(Class class_, String str) {
		getLogger(class_).debug(str);
	}

	public static void info(Class class_, String str) {
		getLogger(class_).info(str);
	}

}
