package com.yonyou.business.button.util;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.yonyou.util.HttpRequest;
import com.yonyou.util.PropertyFileUtil;
import com.yonyou.util.jdbc.IBaseDao;

/**
 * 解决：业务功能中上传附件时，如果业务撤销应同步撤销上传的附件；
 * 单开线程验证request里参数达成条件则让document更改表中数据字段【业务是否保存【ISDOCSAVETRUE】】状态
 * 
 * @date 2018-11-05 18:09:37
 * @author yansu
 *
 */
public class IsDocSaveTrueTools implements Runnable {
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static Logger logger = LoggerFactory.getLogger(IsDocSaveTrueTools.class);
	private String param;

	@Autowired
	protected IBaseDao dcmsDAO;

	public IsDocSaveTrueTools(String param) {
		this.param = param;
	}

	/**
	 * 查找request参数json value中是否有DOC ID 有的话走线程去document更改文档单据保存状态
	 * @param request
	 */
	@Autowired
	public synchronized static void IsDocSaveTrue(HttpServletRequest request) {
		logger.info("进入com.yonyou.business.button.util.IsDocSaveTrueTools.IsDocSaveTrue(){}");
		JSONObject reqjson =null;
		try {
			 reqjson = JSONObject.fromObject(request.getParameter("jsonData"));
		} catch (JSONException e) {
			// TODO: handle exception
			logger.info("通知附件JSON数据格式有误:"+e.getMessage());
		}
		if (reqjson==null||reqjson.size() < 1) {
			return;
		}
		//{"EVALUATION_STANDARD_DESC":"2","EVALUATION_STANDARD_NAME":"2","ID":""}
		//{"PROJECT_NAME":"WBS测试 PZW","PURC_INQUIRY":"联通智网","PROCUREMENT_METHOD":"1","PROJECT_OVERVIEW":"1","OTHER":"","BUDGET":"1.000000","kuangname":"","kuang":"","anchor_ATTACHMENT":"C:\fakepath20180919_ICCsx_index.jsp","sub":"","ATTACHMENT":"DOC000000000015532","bs_code":"PURCHASE_INQUIRY","showfiles_ATTACHMENT":"","CC_PERSON":"","RECEIVE_MAILBOX":"1","CREATE_PERSON":"联通智网管理员","DATE_CREATED":"","ID":""}
		Iterator<?> it = reqjson.keys(); 
		while (it.hasNext()) {
			String key = it.next().toString();
			String value = reqjson.getString(key);
			//System.out.println(key+":"+value);
			if (value.indexOf("DOC")!=-1) {
				new Thread(new IsDocSaveTrueTools("{\"batch_no\":\""+value+"\"}")).start();
			}
		}
	}

	/**
	 * 重写的run()
	 */
	@Override
	public void run() {
		try {
			//System.out.println("运行run()");
			logger.info("线程 ThreadName: "+ Thread.currentThread().getName() + " ;参数:" + param	+ ", ");
//			// 让线程睡眠一会
			Thread.sleep(0);
			// 实际业务操作
			String url=PropertyFileUtil.getValue("doc.url")+"/base?cmd=isdocsavetrue";
			HttpRequest httpPost=new HttpRequest();
			if (param!=null) {
				httpPost.httpToStringPost(url, param.toString());
			}
			// 实际业务操作end
		} catch (InterruptedException e) {
			logger.info("Thread " + param + " interrupted.");
		}
		//System.out.println("退出run()");
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		for (int i = 0; i < 10; i++) {
			Object reqjson = "test" + i;
			new Thread(new IsDocSaveTrueTools(reqjson.toString())).start();
		}
	}
	
}
