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
@RequestMapping(value = "/actualRevenue")
public class ActualRevenueController extends BaseController {
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
	 * 实际业务发生收入
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=feeTypeAnalysisDetail") 
	@ResponseBody
	public void feeTypeAnalysisDetail(HttpServletRequest request,HttpServletResponse response,@RequestBody String jsonData) throws Exception {
		String jsonMessage=null;
		try {
			JSONObject json = JSONObject.fromObject(jsonData);
			String pagestart="0";
			String pagesize="10";
			if(json.get("pagestart")!=null){
				pagestart=json.get("pagestart")+" ";
			}
			if(json.get("pagesize")!=null){
				pagesize=json.get("pagesize")+" ";
			}
			
			String sql="SELECT `cus_name`,`bus_id`,`pro_id`,`pro_name`,`coll_fill_time`,`dep`,`cus_manager`,`con_id`,`con_name`,`con_sign_of`,`con_eff_time`,`service_type`,`pro_type`,`con_rate`,`this_con_rate`,`con_time`,`con_return_time`,ifnull(actual_happen_mon,'') as actual_happen_mon,`charge_mode`,`pro_company`,`sett_rate`,`this_mon_inc`,ifnull(`sure_price`,0) as `sure_price`,ifnull(after_sure_time,'') as after_sure_time,`book_inc`,`mon_ner_num`,`sum_connect` FROM view_coll_summary where 1=1 ";
			
			if(json.get("customer")!=null){
				sql+=" and cus_name='"+json.get("customer")+"' ";
			}
			if(json.get("pro_name")!=null){
				sql+=" and pro_name='"+json.get("pro_name")+"' ";
			}
			
			sql+=this.getMarkUnit(request);
			
			String sql1="select count(1) as num from ("+sql+") countnum";
			String sql2=sql;
			sql+=" limit "+pagestart+","+pagesize;
			List<Map<String, Object>> list = dcmsDAO.query(sql, "");

			List<Map<String, Object>> list1 = dcmsDAO.query(sql1, "");
			
			List<Map<String, Object>> list2 = dcmsDAO.query(sql2, "");
			
			JSONArray rowdata = new JSONArray();
			for (Map<String, Object> map : list) {
				JSONObject jsonObject = new JSONObject();
				for (Map.Entry<String, Object> entry : map.entrySet()) {
					if(entry.getValue()!=null&&(entry.getValue().getClass().toString().equals("class java.sql.Date")||entry.getValue().getClass().toString().equals("class java.sql.Timestamp")))
						jsonObject.put(entry.getKey(), entry.getValue().toString());
					else
						jsonObject.put(entry.getKey(), entry.getValue());
				}
				rowdata.add(jsonObject);
			}
			
			JSONArray rowdata1 = new JSONArray();
			for (Map<String, Object> map : list2) {
				JSONObject jsonObject = new JSONObject();
				for (Map.Entry<String, Object> entry : map.entrySet()) {
					if(entry.getValue()!=null&&(entry.getValue().getClass().toString().equals("class java.sql.Date")||entry.getValue().getClass().toString().equals("class java.sql.Timestamp")))
						jsonObject.put(entry.getKey(), entry.getValue().toString());
					else
						jsonObject.put(entry.getKey(), entry.getValue());
				}
				rowdata1.add(jsonObject);
			}
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("rowdata", rowdata);
			jsonObject.put("rowdata1", rowdata1);
			jsonObject.put("total", list1.get(0).get("NUM"));
			
			System.out.println("------------------");
			System.out.println(jsonObject);
			
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
	
	/**
	 * 实际业务发生收入,分组
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=feeTypeAnalysisDetailGb") 
	@ResponseBody
	public void feeTypeAnalysisDetailGb(HttpServletRequest request,HttpServletResponse response,@RequestBody String jsonData) throws Exception {
		String jsonMessage=null;
		try {
			JSONObject json = JSONObject.fromObject(jsonData);
			String pagestart="0";
			String pagesize="10";
			if(json.get("pagestart")!=null){
				pagestart=json.get("pagestart")+" ";
			}
			if(json.get("pagesize")!=null){
				pagesize=json.get("pagesize")+" ";
			}
			
			String sql="SELECT ifnull(actual_happen_mon,'') as actual_happen_mon,sum(sure_price) AS sure_price FROM view_coll_summary where 1=1 ";
			
			
			if(json.get("customer")!=null){
				sql+=" and cus_name='"+json.get("customer")+"' ";
				if(json.get("pro_name")!=null){
					sql+=" and pro_name='"+json.get("pro_name")+"' ";
				}
				sql+=this.getMarkUnit(request);
				sql+=" group by actual_happen_mon,cus_name";
			}else{
				if(json.get("pro_name")!=null){
					sql+=" and pro_name='"+json.get("pro_name")+"' ";
				}
				sql+=this.getMarkUnit(request);
				sql+=" group by actual_happen_mon";
			}
			
			
			String sql1="select count(1) as num from ("+sql+") countnum";
			String sql2=sql;
			sql+=" limit "+pagestart+","+pagesize;
			List<Map<String, Object>> list = dcmsDAO.query(sql, "");

			List<Map<String, Object>> list1 = dcmsDAO.query(sql1, "");
			
			List<Map<String, Object>> list2 = dcmsDAO.query(sql2, "");
			
			JSONArray rowdata = new JSONArray();
			for (Map<String, Object> map : list) {
				JSONObject jsonObject = new JSONObject();
				for (Map.Entry<String, Object> entry : map.entrySet()) {
					if(entry.getValue()!=null&&(entry.getValue().getClass().toString().equals("class java.sql.Date")||entry.getValue().getClass().toString().equals("class java.sql.Timestamp")))
						jsonObject.put(entry.getKey(), entry.getValue().toString());
					else
						jsonObject.put(entry.getKey(), entry.getValue());
				}
				rowdata.add(jsonObject);
			}
			JSONArray rowdata1 = new JSONArray();
			for (Map<String, Object> map : list2) {
				JSONObject jsonObject = new JSONObject();
				for (Map.Entry<String, Object> entry : map.entrySet()) {
					if(entry.getValue()!=null&&(entry.getValue().getClass().toString().equals("class java.sql.Date")||entry.getValue().getClass().toString().equals("class java.sql.Timestamp")))
						jsonObject.put(entry.getKey(), entry.getValue().toString());
					else
						jsonObject.put(entry.getKey(), entry.getValue());
				}
				rowdata1.add(jsonObject);
			}
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("rowdata", rowdata);
			jsonObject.put("rowdata1", rowdata1);
			jsonObject.put("total", list1.get(0).get("NUM"));
			
			System.out.println("------------------");
			System.out.println(jsonObject);
			
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
