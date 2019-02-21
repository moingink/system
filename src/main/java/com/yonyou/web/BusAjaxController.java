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
import com.yonyou.util.page.PageUtil;
import com.yonyou.util.page.proxy.PageBulidHtmlAbs;
import com.yonyou.util.page.proxy.bulid.PageBulidHtmlBySel;
import com.yonyou.util.sql.SQLUtil.WhereEnum;
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.util.sql.SqlWhereEntity;

@RestController
@RequestMapping(value = "/busAjax")
public class BusAjaxController {
	
	
	@Autowired
	protected IBaseDao dcmsDAO;
	
	protected Map<String,ButtonAbs> findButtonMap(){
		return new HashMap<String,ButtonAbs>();
	}
	
	/**
	 * 项目获取 前评估信息
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping(params = "cmd=findPreByProj")
	public void button(HttpServletRequest request, HttpServletResponse response) throws IOException  {
		
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		
		//商机编码
		String code =request.getParameter("OPPORTUNITY_CODE");
		StringBuffer sql =new StringBuffer();
		sql.append("select  TOTAL_INCOME  INCOME_ESTIMATE  ,TOTAL_COST,TOTAL_INCOME-TOTAL_COST  PROFIT_ESTIMATE,RATE_OF_RETURN  RETURN_RATE_ESTIMATE from pre_assessment  where BUSINESS_NUMBER ='").append(code).append("'");
		
		List<Map<String,Object>> dataList=this.dcmsDAO.query(sql.toString(),"");
		
		
		Map<String,String> columnMap =new HashMap<String,String>();
		
		columnMap.put("TOTAL_INCOME", "INCOME_ESTIMATE");
		columnMap.put("PROFIT_ESTIMATE", "PROFIT_ESTIMATE");
		columnMap.put("RATE_OF_RETURN", "RETURN_RATE_ESTIMATE");
		
		
		Map<String,Object> returnMap =new HashMap<String,Object>();
		
		
		if(dataList.size()==1){
			for(String key:dataList.get(0).keySet()){
				if(columnMap.containsKey(key)){
					returnMap.put(columnMap.get(key), dataList.get(0).get(key));
				}
			}
		}
		
		
		
		out.print(JsonUtils.map2json(returnMap));
		out.flush();
		out.close();
	}
	
	
	
}
