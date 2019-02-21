package com.yonyou.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.yonyou.business.DataSourceUtil;
import com.yonyou.business.MetaDataUtil;
import com.yonyou.business.RmDictReferenceUtil;
import com.yonyou.business.entity.TokenEntity;
import com.yonyou.business.entity.TokenUtil;
import com.yonyou.util.BussnissException;
import com.yonyou.util.JsonUtils;
import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.util.sql.SqlWhereEntity;
import com.yonyou.util.sql.SQLUtil.WhereEnum;

@RestController
@RequestMapping(value = "/actualAnalysis")
public class ActualAnalysisController extends BaseController {
	private static java.text.SimpleDateFormat sdff=new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("yyyy-MM-dd");
	private void ajaxWrite(String ajaxMessage,HttpServletRequest request,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.print(ajaxMessage);
		out.flush();
		out.close();
	}
	
	
	/**
	 * 查询条件信息
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=conditionInfo") 
	@ResponseBody
	public void companyInfo(HttpServletRequest request,HttpServletResponse response,@RequestBody String jsonData) throws Exception {
 
		String jsonMessage=null;
		try {
			//客户条件
			String sql="SELECT DISTINCT cus_name FROM view_coll_summary where 1=1 ";
			sql+=this.getMarkUnit(request);
			List<Map<String, Object>> list = dcmsDAO.query(sql, "");
			JSONArray rowdata = JSONArray.fromObject(list);
			//项目条件
			String sql1="SELECT DISTINCT pro_id,pro_name FROM view_coll_summary where 1=1 ";
			sql1+=this.getMarkUnit(request);
			List<Map<String, Object>> list1 = dcmsDAO.query(sql1, "");
			JSONArray rowdata1 = JSONArray.fromObject(list1);
			/*//合同条件
			String sql2="SELECT DISTINCT con_id,con_name FROM view_coll_summary";
			List<Map<String, Object>> list2 = dcmsDAO.query(sql2, "");
			JSONArray rowdata2 = JSONArray.fromObject(list2);
			//部门条件
			String sql3="SELECT DISTINCT dep FROM view_coll_summary";
			List<Map<String, Object>> list3 = dcmsDAO.query(sql3, "");
			JSONArray rowdata3 = JSONArray.fromObject(list3);*/
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("rowdata", rowdata);
			jsonObject.put("rowdata1", rowdata1);
			/*jsonObject.put("rowdata2", rowdata2);
			jsonObject.put("rowdata3", rowdata3);*/
			
			jsonMessage = "{\"status\":\"success\",\"body\":"+jsonObject+"}";
			this.ajaxWrite(jsonMessage, request, response);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			jsonMessage = "{\"status\":\"fail\",\"message\":\"系统出错\"}";
			this.ajaxWrite(jsonMessage, request, response);
			return;
		}
	}
	
	private String getMarkUnit(HttpServletRequest request){
		TokenEntity tokenEntity = TokenUtil.initTokenEntity(request);
		JSONObject json = JSONObject.fromObject(tokenEntity.ROLE.getDataAuth());
		if(json.get("company")!=null && !"".equals(json.get("company"))){
			return " and organization_id="+json.get("company");
		}
		return "";
	}

}
