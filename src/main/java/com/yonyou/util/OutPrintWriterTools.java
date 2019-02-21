package com.yonyou.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * outPrintWriter回参
 * @author yansu
 *
 */
public class OutPrintWriterTools {

	 private static Logger logger = Logger.getLogger(OutPrintWriterTools.class);
	 
	/**
	 * outPrintWriter回参JSONObject
	 * @param response
	 * @param returnJsonStr
	 */
	public static void outPrW(HttpServletResponse response,
			JSONObject returnJson) {
		// TODO Auto-generated method stub
		response.setCharacterEncoding("utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("text/json;charset=UTF-8");
		// 回参
		PrintWriter out;
		try {
			logger.info("outPrW{}"+returnJson);
			out = response.getWriter();
			out.print(returnJson);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * outPrintWriter回参JSONArray
	 * @param response
	 * @param returnJsonStr
	 */
	public static void outPrWArr(HttpServletResponse response,
			JSONArray returnJsonArr) {
		// TODO Auto-generated method stub
		response.setCharacterEncoding("utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("text/json;charset=UTF-8");
		// 回参
		PrintWriter out;
		try {
			logger.info("outPrW{}"+returnJsonArr);
			out = response.getWriter();
			out.print(returnJsonArr.toString());
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
