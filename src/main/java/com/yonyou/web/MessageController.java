package com.yonyou.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;
import org.util.RmPartyConstants;






import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yonyou.business.DataSourceUtil;
import com.yonyou.business.MetaDataUtil;
import com.yonyou.business.ProxyPageUtil;
import com.yonyou.business.RmDictReferenceUtil;
import com.yonyou.business.button.ButtonAbs;
import com.yonyou.util.BussnissException;
import com.yonyou.util.ConditionTypeUtil;
import com.yonyou.util.JsonUtils;
import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.mail.MailHelper;
import com.yonyou.util.page.PageUtil;
import com.yonyou.util.page.proxy.PageBulidHtmlAbs;
import com.yonyou.util.page.proxy.bulid.PageBulidHtmlBySel;
import com.yonyou.util.sql.SQLUtil.WhereEnum;
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.util.sql.SqlWhereEntity;

@RestController
@RequestMapping(value = "/message")
public class MessageController {
	
	
	@Autowired
	protected IBaseDao dcmsDAO;
	
	protected Map<String,ButtonAbs> findButtonMap(){
		return new HashMap<String,ButtonAbs>();
	}
	
	@RequestMapping(params = "cmd=sendMessage")
	public void sendMessage(HttpServletRequest request, HttpServletResponse response)  {

		String buttonToken =request.getParameter("buttonToken");
		
		String manager_id=request.getParameter("manager_id");
		String content=request.getParameter("content");
		SqlTableUtil sqlEntity;
		try {
			sqlEntity = DataSourceUtil.dataSourceToSQL("RM_USER_REF");
			sqlEntity.appendWhereAnd(" ID="+manager_id);
			List<Map<String, Object>> mapList = dcmsDAO.query(sqlEntity);
			
			
			MailHelper mailHelper = new MailHelper();
			mailHelper.init();
			mailHelper.sendMail("A","新邮件", content, (String)mapList.get(0).get("EMAIL"));
		} catch (BussnissException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		
		
	}
	
	
	
}
