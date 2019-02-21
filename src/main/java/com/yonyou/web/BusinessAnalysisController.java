package com.yonyou.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
@RequestMapping(value = "/businessAnalysis")
public class BusinessAnalysisController extends BaseController {
	private static java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private void ajaxWrite(String ajaxMessage,HttpServletRequest request,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.print(ajaxMessage);
		out.flush();
		out.close();
	}
	/**
	 * 获取饼图数据
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=piechart") 
	@ResponseBody
	public void piechart(HttpServletRequest request,HttpServletResponse response,@RequestBody String jsonData) throws Exception {
 
		String jsonMessage=null;
		try {
			String sql="select COUNT(c.DEPT) as 'value',if(c.DEPT='','总部',c.DEPT) as 'name' from fin_confirm_income c GROUP BY c.DEPT";
			List<Map<String, Object>> list = dcmsDAO.query(sql, "");
			JSONArray rowdata = new JSONArray();
			JSONArray rowname = new JSONArray();
			for (Map<String, Object> map : list) {
				rowname.add(map.get("NAME"));
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("name", map.get("NAME"));
				jsonObject.put("value", map.get("VALUE"));
				rowdata.add(jsonObject);
			}
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("rowdata", rowdata);
			jsonObject.put("rowname", rowname);
			jsonObject.put("dataname", "部门人数");
			jsonObject.put("title", "联通智网部门人数分布图");
			jsonObject.put("subtitle", "饼状图");
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
	 * 获取折线图数据
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=linechart") 
	@ResponseBody
	public void linechart(HttpServletRequest request,HttpServletResponse response,@RequestBody String jsonData) throws Exception {
		
		String jsonMessage=null;
		try {
			String sql="select t.type_name,t.id,m.business_name,(select count(1) from fin_confirm_income c where c.OPPORTY_CODE=m.business_id) num1, (select IFNULL(SUM(a.CONFIRM_INCOME),0) from fin_audit_charge_detail a LEFT JOIN  fin_confirm_income c on a.PARENT_ID=c.BILL_NO  where c.OPPORTY_CODE=m.business_id ) num2 from bus_business_message m LEFT JOIN buss_type t on  m.bus_main_id LIKE CONCAT('%',t.id,'%') where t.id is not null  ";
			List<Map<String, Object>> list = dcmsDAO.query(sql, "");
			JSONArray rowname = new JSONArray();
			JSONArray rowdata = new JSONArray();
			Map<String,JSONObject> hashMap = new HashMap<String,JSONObject>();
			for (Map<String, Object> map : list) {
				if(!hashMap.keySet().contains(map.get("ID").toString())){
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("name", map.get("TYPE_NAME"));
					jsonObject.put("type", "scatter");
					JSONArray jsonArray = new JSONArray();
					jsonArray.add("["+map.get("NUM1")+","+map.get("NUM2")+"]");
					jsonObject.put("data", jsonArray);
					hashMap.put(map.get("ID").toString(), jsonObject);
					
					rowname.add(map.get("TYPE_NAME"));
				}else{
					JSONObject jsonObject = hashMap.get(map.get("ID").toString());
					JSONArray jsonArray = jsonObject.getJSONArray("data");
					jsonArray.add("["+map.get("NUM1")+","+map.get("NUM2")+"]");
					jsonObject.put("data", jsonArray);
					hashMap.put(map.get("ID").toString(), jsonObject);
				}
			}
			rowdata.addAll(hashMap.values());
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("rowdata", rowdata);
			jsonObject.put("rowname", rowname);
			jsonObject.put("dataname", "部门人数");
			jsonObject.put("title", "");
			jsonObject.put("subtitle", "");
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
	 * 商机分析数量
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=businessAnalysisNumber") 
	@ResponseBody
	public void businessAnalysisNumber(HttpServletRequest request,HttpServletResponse response,@RequestBody String jsonData) throws Exception {
 
		String jsonMessage=null;
		try {
			JSONObject json = JSONObject.fromObject(jsonData);
			List<Map<String, Object>> arrayList = new ArrayList<Map<String,Object>>();
				String sql="SELECT t.type_name,CONCAT(t.id,'') id, (select count(1) from bus_business_message m where  m.bus_main_id LIKE CONCAT('%',t.id,'%') ";
				if(json.get("bus_state")!=null){
					if(!json.get("bus_state").equals("-1")){
						sql+=" and m.business_state='"+json.get("bus_state")+"' ";
						
					}else{
						
						sql+=" and m.business_state is null ";
					}
					
				}
				if(json.get("company")!=null){
					sql+=" and m.business_sub_company='"+json.get("company")+"' ";
				}
				if(json.get("datestart")!=null){
					sql+=" and m.apply_time >='"+json.get("datestart")+"' ";
				}
				if(json.get("dateend")!=null){
					sql+=" and m.apply_time <='"+json.get("dateend")+"' ";
				}
				
				sql+=") as num from buss_type t   ";
				List<Map<String, Object>> list = dcmsDAO.query(sql, "");
				
				arrayList.addAll(list);
				
			JSONArray rowdata = new JSONArray();
			JSONArray rowname = new JSONArray();
			for (Map<String, Object> map : arrayList) {
				rowname.add(map.get("TYPE_NAME"));
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("name", map.get("TYPE_NAME"));
				jsonObject.put("value", map.get("NUM"));
				jsonObject.put("id", map.get("ID"));
				rowdata.add(jsonObject);
			}
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("rowdata", rowdata);
			jsonObject.put("rowname", rowname);
			jsonObject.put("dataname", "业务类型        数量");
			jsonObject.put("title", "");
			jsonObject.put("subtitle", "");
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
	 * 商机分析收入
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=businessAnalysisIncome") 
	@ResponseBody
	public void businessAnalysisIncome(HttpServletRequest request,HttpServletResponse response,@RequestBody String jsonData) throws Exception {
 
		String jsonMessage=null;
		try {
			JSONObject json = JSONObject.fromObject(jsonData);
			List<Map<String, Object>> arrayList = new ArrayList<Map<String,Object>>();
				String sql="select t.type_name,CONCAT(t.id,'') id, (select IFNULL(SUM(a.ESTIMATED_INCOME),0) from fin_audit_charge_detail a LEFT JOIN  fin_confirm_income c on a.PARENT_ID=c.BILL_NO  LEFT JOIN  bus_business_message m on c.OPPORTY_CODE=m.business_id where  m.bus_main_id like concat('%',t.id,'%') ";
				if(json.get("bus_state")!=null){
					if(!json.get("bus_state").equals("-1")){
						sql+=" and m.business_state='"+json.get("bus_state")+"' ";
						
					}else{
						
						sql+=" and m.business_state is null ";
					}
					
				}
				if(json.get("company")!=null){
					sql+=" and m.business_sub_company='"+json.get("company")+"' ";
				}
				if(json.get("datestart")!=null){
					sql+=" and m.apply_time >='"+json.get("datestart")+"' ";
				}
				if(json.get("dateend")!=null){
					sql+=" and m.apply_time <='"+json.get("dateend")+"' ";
				}
				
				sql+=") as num from buss_type t   ";
				List<Map<String, Object>> list = dcmsDAO.query(sql, "");
				
				arrayList.addAll(list);
				
			JSONArray rowdata = new JSONArray();
			JSONArray rowname = new JSONArray();
			for (Map<String, Object> map : arrayList) {
				rowname.add(map.get("TYPE_NAME"));
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("name", map.get("TYPE_NAME"));
				jsonObject.put("value", map.get("NUM"));
				jsonObject.put("id", map.get("ID"));
				rowdata.add(jsonObject);
			}
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("rowdata", rowdata);
			jsonObject.put("rowname", rowname);
			jsonObject.put("dataname", "业务类型        收入");
			jsonObject.put("title", "");
			jsonObject.put("subtitle", "");
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
	 * 公司信息
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=companyInfo") 
	@ResponseBody
	public void companyInfo(HttpServletRequest request,HttpServletResponse response,@RequestBody String jsonData) throws Exception {
 
		String jsonMessage=null;
		try {
			String sql="select CONCAT(t.`ID`,'') as 'ID' ,t.`NAME`,t.OU from tm_company t where t.OU_TYPE='0'";
			List<Map<String, Object>> list = dcmsDAO.query(sql, "");
			JSONArray rowdata = JSONArray.fromObject(list);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("rowdata", rowdata);
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
	 * 商机状态
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=businessState") 
	@ResponseBody
	public void businessState(HttpServletRequest request,HttpServletResponse response,@RequestBody String jsonData) throws Exception {
 
		String jsonMessage=null;
		try {
			String sql="select d.DATA_KEY,d.DATA_VALUE from rm_code_data d LEFT JOIN rm_code_type t on d.CODE_TYPE_ID=t.ID where t.TYPE_KEYWORD='OPPORTUNITY_STATUS';";
			List<Map<String, Object>> list = dcmsDAO.query(sql, "");
			JSONArray rowdata = JSONArray.fromObject(list);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("rowdata", rowdata);
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
	 * 商机分析数量详情
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=businessAnalysisNumberDetail") 
	@ResponseBody
	public void businessAnalysisNumberDetail(HttpServletRequest request,HttpServletResponse response,@RequestBody String jsonData) throws Exception {
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
			String sql="select t.type_name,m.* from  bus_business_message m LEFT JOIN buss_type t   on m.bus_main_id like concat('%',t.id,'%') where 1=1 ";
			if(json.get("bus_state")!=null&&!json.get("bus_state").equals("")){
				if(!json.get("bus_state").equals("-1")){
					sql+=" and m.business_state='"+json.get("bus_state")+"' ";
					
				}else{
					
					sql+=" and m.business_state is null ";
				}
				
			}
			if(json.get("company")!=null&&!json.get("company").equals("")){
				sql+=" and m.business_sub_company='"+json.get("company")+"' ";
			}
			if(json.get("datestart")!=null&&!json.get("datestart").equals("")){
				sql+=" and m.apply_time >='"+json.get("datestart")+"' ";
			}
			if(json.get("dateend")!=null&&!json.get("dateend").equals("")){
				sql+=" and m.apply_time <='"+json.get("dateend")+"' ";
			}
			if(json.get("id")!=null&&!json.get("id").equals("")){
				sql+=" and t.id ='"+json.get("id")+"' ";
			}

			String sql1="select count(1) as num from ("+sql+") countnum";
			sql+=" limit "+pagestart+","+pagesize;
			List<Map<String, Object>> list = dcmsDAO.query(sql, "");
			
			List<Map<String, Object>> list1 = dcmsDAO.query(sql1, "");
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

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("rowdata", rowdata);
			jsonObject.put("total", list1.get(0).get("NUM"));
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
	 * 商机分析收入详情
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=businessAnalysisIncomeDetail") 
	@ResponseBody
	public void businessAnalysisIncomeDetail(HttpServletRequest request,HttpServletResponse response,@RequestBody String jsonData) throws Exception {
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
			String sql="select t.type_name,m.* ,( select IFNULL(SUM(a.ESTIMATED_INCOME),0) from fin_audit_charge_detail a LEFT JOIN  fin_confirm_income c on a.PARENT_ID=c.BILL_NO where c.OPPORTY_CODE=m.business_id ) as income from  bus_business_message m LEFT JOIN buss_type t   on m.bus_main_id like concat('%',t.id,'%') where 1=1 ";
			if(json.get("bus_state")!=null&&!json.get("bus_state").equals("")){
				if(!json.get("bus_state").equals("-1")){
					sql+=" and m.business_state='"+json.get("bus_state")+"' ";
					
				}else{
					
					sql+=" and m.business_state is null ";
				}
				
			}
			if(json.get("company")!=null&&!json.get("company").equals("")){
				sql+=" and m.business_sub_company='"+json.get("company")+"' ";
			}
			if(json.get("datestart")!=null&&!json.get("datestart").equals("")){
				sql+=" and m.apply_time >='"+json.get("datestart")+"' ";
			}
			if(json.get("dateend")!=null&&!json.get("dateend").equals("")){
				sql+=" and m.apply_time <='"+json.get("dateend")+"' ";
			}
			if(json.get("id")!=null&&!json.get("id").equals("")){
				sql+=" and t.id ='"+json.get("id")+"' ";
			}

			String sql1="select count(1) as num from ("+sql+") countnum";
			sql+=" limit "+pagestart+","+pagesize;
			List<Map<String, Object>> list = dcmsDAO.query(sql, "");
			List<Map<String, Object>> list1 = dcmsDAO.query(sql1, "");
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

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("rowdata", rowdata);
			jsonObject.put("total", list1.get(0).get("NUM"));
			
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
	 * 商机状态数量
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=businessStateNumber") 
	@ResponseBody
	public void businessStateNumber(HttpServletRequest request,HttpServletResponse response,@RequestBody String jsonData) throws Exception {
		
		String jsonMessage=null;
		try {
			JSONObject json = JSONObject.fromObject(jsonData);
			List<Map<String, Object>> arrayList = new ArrayList<Map<String,Object>>();
			String sql="select d.DATA_KEY,d.DATA_VALUE,(select count(1) from bus_business_message m where  m.business_state=d.DATA_KEY  ";
			if(json.get("company")!=null){
				sql+=" and m.business_sub_company='"+json.get("company")+"' ";
			}
			if(json.get("datestart")!=null){
				sql+=" and m.apply_time >='"+json.get("datestart")+"' ";
			}
			if(json.get("dateend")!=null){
				sql+=" and m.apply_time <='"+json.get("dateend")+"' ";
			}
			
			sql+=") as num from rm_code_data d LEFT JOIN rm_code_type t on d.CODE_TYPE_ID=t.ID where t.TYPE_KEYWORD='OPPORTUNITY_STATUS' ";
			List<Map<String, Object>> list = dcmsDAO.query(sql, "");
			
			arrayList.addAll(list);
			
			sql=" select count(1) as num ,'-1' as DATA_KEY,'无状态' as DATA_VALUE from bus_business_message m where  m.business_state is null ";
			if(json.get("company")!=null){
				sql+=" and m.business_sub_company='"+json.get("company")+"' ";
			}
			if(json.get("datestart")!=null){
				sql+=" and m.apply_time >='"+json.get("datestart")+"' ";
			}
			if(json.get("dateend")!=null){
				sql+=" and m.apply_time <='"+json.get("dateend")+"' ";
			}
			list = dcmsDAO.query(sql, "");
			arrayList.addAll(list);
				
			JSONArray rowdata = new JSONArray();
			JSONArray rowname = new JSONArray();
			for (Map<String, Object> map : arrayList) {
				rowname.add(map.get("DATA_VALUE"));
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("name", map.get("DATA_VALUE"));
				jsonObject.put("value", map.get("NUM"));
				jsonObject.put("bus_state", map.get("DATA_KEY"));
				rowdata.add(jsonObject);
			}
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("rowdata", rowdata);
			jsonObject.put("rowname", rowname);
			jsonObject.put("dataname", "商机状态        数量");
			jsonObject.put("title", "");
			jsonObject.put("subtitle", "");
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
	 * 商机状态数量详情
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=businessStateNumberDetail") 
	@ResponseBody
	public void businessStateNumberDetail(HttpServletRequest request,HttpServletResponse response,@RequestBody String jsonData) throws Exception {
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
			String sql="select d.DATA_VALUE,m.* from  bus_business_message m LEFT JOIN rm_code_data d on d.DATA_KEY=m.business_state LEFT JOIN rm_code_type t on d.CODE_TYPE_ID=t.ID where t.TYPE_KEYWORD='OPPORTUNITY_STATUS' ";
			if(json.get("bus_state")!=null&&!json.get("bus_state").equals("")){
				if(!json.get("bus_state").equals("-1")){
					sql+=" and m.business_state='"+json.get("bus_state")+"' ";
					
				}else{
					
					sql=" select  '无状态' as DATA_VALUE,m.* from bus_business_message m where  m.business_state is null ";

				}
				
			}
			if(json.get("company")!=null&&!json.get("company").equals("")){
				sql+=" and m.business_sub_company='"+json.get("company")+"' ";
			}
			if(json.get("datestart")!=null&&!json.get("datestart").equals("")){
				sql+=" and m.apply_time >='"+json.get("datestart")+"' ";
			}
			if(json.get("dateend")!=null&&!json.get("dateend").equals("")){
				sql+=" and m.apply_time <='"+json.get("dateend")+"' ";
			}

			String sql1="select count(1) as num from ("+sql+") countnum";
			sql+=" limit "+pagestart+","+pagesize;
			List<Map<String, Object>> list = dcmsDAO.query(sql, "");
			
			List<Map<String, Object>> list1 = dcmsDAO.query(sql1, "");
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

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("rowdata", rowdata);
			jsonObject.put("total", list1.get(0).get("NUM"));
			
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
	 * 营销单元数量
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=companyNumber") 
	@ResponseBody
	public void companyNumber(HttpServletRequest request,HttpServletResponse response,@RequestBody String jsonData) throws Exception {
		
		String jsonMessage=null;
		try {
			JSONObject json = JSONObject.fromObject(jsonData);
			List<Map<String, Object>> arrayList = new ArrayList<Map<String,Object>>();
			String sql="select CONCAT(t.`ID`,'') as 'ID' ,t.`NAME`, (select count(1) from bus_business_message m where  m.business_sub_company=CONCAT(t.`ID`,'')   ";
			if(json.get("bus_state")!=null&&!json.get("bus_state").equals("")){
				if(!json.get("bus_state").equals("-1")){
					sql+=" and m.business_state='"+json.get("bus_state")+"' ";
					
				}else{
					
					sql+=" and m.business_state is null ";
				}
				
			}
			if(json.get("datestart")!=null){
				sql+=" and m.apply_time >='"+json.get("datestart")+"' ";
			}
			if(json.get("dateend")!=null){
				sql+=" and m.apply_time <='"+json.get("dateend")+"' ";
			}
			
			sql+=") as num from tm_company t  where t.OU_TYPE='0'";
			List<Map<String, Object>> list = dcmsDAO.query(sql, "");
			
			arrayList.addAll(list);
			
				
			JSONArray rowdata = new JSONArray();
			JSONArray rowname = new JSONArray();
			for (Map<String, Object> map : arrayList) {
				rowname.add(map.get("NAME"));
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("name", map.get("NAME"));
				jsonObject.put("value", map.get("NUM"));
				jsonObject.put("company", map.get("ID"));
				rowdata.add(jsonObject);
			}
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("rowdata", rowdata);
			jsonObject.put("rowname", rowname);
			jsonObject.put("dataname", "营销单元        数量");
			jsonObject.put("title", "");
			jsonObject.put("subtitle", "");
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
	 * 营销单元数量详情
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=companyNumberDetail") 
	@ResponseBody
	public void companyNumberDetail(HttpServletRequest request,HttpServletResponse response,@RequestBody String jsonData) throws Exception {
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
			String sql="select t.`NAME`,m.* from  bus_business_message m LEFT JOIN  tm_company t on m.business_sub_company=CONCAT(t.`ID`,'') where 1=1  and t.OU_TYPE='0' ";
			if(json.get("bus_state")!=null&&!json.get("bus_state").equals("")){
				if(!json.get("bus_state").equals("-1")){
					sql+=" and m.business_state='"+json.get("bus_state")+"' ";
					
				}else{
					
					sql+=" and m.business_state is null ";
				}
				
			}
			if(json.get("company")!=null&&!json.get("company").equals("")){
				sql+=" and m.business_sub_company='"+json.get("company")+"' ";
			}
			if(json.get("datestart")!=null&&!json.get("datestart").equals("")){
				sql+=" and m.apply_time >='"+json.get("datestart")+"' ";
			}
			if(json.get("dateend")!=null&&!json.get("dateend").equals("")){
				sql+=" and m.apply_time <='"+json.get("dateend")+"' ";
			}

			String sql1="select count(1) as num from ("+sql+") countnum";
			sql+=" limit "+pagestart+","+pagesize;
			List<Map<String, Object>> list = dcmsDAO.query(sql, "");
			List<Map<String, Object>> list1 = dcmsDAO.query(sql1, "");
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

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("rowdata", rowdata);
			jsonObject.put("total", list1.get(0).get("NUM"));
			
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
}
