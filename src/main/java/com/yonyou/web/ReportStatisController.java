package com.yonyou.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.yonyou.business.entity.TokenEntity;
import com.yonyou.business.entity.TokenUtil;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SqlWhereEntity;
import com.yonyou.util.sql.SQLUtil.WhereEnum;

@RestController
@RequestMapping(value = "/reportStatis")
public class ReportStatisController extends BaseController {
	
	@Autowired
	private IBaseDao dcmsDAO;
	
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
	 * 合作伙伴类别分析
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=partnerType") 
	@ResponseBody
	public void partnerType(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String jsonMessage=null;
		try {
			String sql="SELECT  concat(partner_type_ids,'') as partner_type_ids,ifnull(partner_type_message,'test') as partner_type_message,count(*) as count FROM partner_access where 1=1 ";
			sql+=this.getMarkUnit(request);
			sql+=" GROUP BY partner_type_ids";
			List<Map<String, Object>> list = dcmsDAO.query(sql, "");
			pubMethod(jsonMessage, request, response, list);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			jsonMessage = "{\"status\":\"fail\",\"message\":\"系统出错\"}";
			this.ajaxWrite(jsonMessage, request, response);
			return;
		}
	}
	
	/**
	 * 合作伙伴明细
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=partner") 
	@ResponseBody
	public void partner(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String jsonMessage=null;
		try {
			String partner_type_ids = request.getParameter("partner_type_ids");
			String sql="SELECT name_partner,legal_representative,registered_capital,company_type, ifnull(("+
					"SELECT `cusc_test`.`rm_code_data`.`DATA_VALUE` FROM `cusc_test`.`rm_code_data` "+
					"WHERE ((`cusc_test`.`rm_code_data`.`CODE_TYPE_ID` = ( SELECT `cusc_test`.`rm_code_type`.`ID` FROM `cusc_test`.`rm_code_type` WHERE ( `cusc_test`.`rm_code_type`.`TYPE_KEYWORD` = 'BILL_STATUS' ) )"+
					") AND ( `cusc_test`.`rm_code_data`.`DATA_KEY` = `partner_access`.`bill_status` ) ) ),'') as bill_status FROM partner_access WHERE partner_type_ids='"+partner_type_ids+"' ";
			sql+=this.getMarkUnit(request);
			List<Map<String, Object>> list = dcmsDAO.query(sql, "");
			pubMethod(jsonMessage, request, response, list);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			jsonMessage = "{\"status\":\"fail\",\"message\":\"系统出错\"}";
			this.ajaxWrite(jsonMessage, request, response);
			return;
		}
	}
	
	/**
	 * 关联合同项目
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=contractPro") 
	@ResponseBody
	public void contractPro(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String jsonMessage=null;
		try {
			String other_party_main = request.getParameter("other_party_main");
			String sql="SELECT con_id,con_name,pro_id,pro_name,bus_type,product FROM bus_contract_admin WHERE other_party_main='"+other_party_main+"' ";
			sql+=this.getMarkUnit(request);
			List<Map<String, Object>> list = dcmsDAO.query(sql, "");
			pubMethod(jsonMessage, request, response, list);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			jsonMessage = "{\"status\":\"fail\",\"message\":\"系统出错\"}";
			this.ajaxWrite(jsonMessage, request, response);
			return;
		}
	}
	
	/**
	 * 挣值分析
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=earnedValAnalysis") 
	@ResponseBody
	public void earnedValAnalysis(HttpServletRequest request,HttpServletResponse response,@RequestBody String jsonData) throws Exception {
		String jsonMessage=null;
		try {
			JSONObject json = JSONObject.fromObject(jsonData);
			String proj_code = request.getParameter("proj_code");
			String sql="SELECT calculation_date,sum(cpi) as cpi,sum(spi) as spi  FROM `earned_value_history` where proj_code='"+proj_code+"' ";
			if(json.get("datestart")!=null){
				sql+=" and calculation_date >='"+json.get("datestart")+"' ";
			}
			if(json.get("dateend")!=null){
				sql+=" and calculation_date <='"+json.get("dateend")+" 23:59:59' ";
			}			
			sql+="GROUP BY calculation_date ORDER BY calculation_date";
			List<Map<String, Object>> list = dcmsDAO.query(sql, "");
			pubMethod(jsonMessage, request, response, list);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			jsonMessage = "{\"status\":\"fail\",\"message\":\"系统出错\"}";
			this.ajaxWrite(jsonMessage, request, response);
			return;
		}
	}
	
	
	/**
	 * 应收余额及账龄分析
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=balanceAndAgeAnaly") 
	@ResponseBody
	public void balanceAndAgeAnaly(HttpServletRequest request,HttpServletResponse response,@RequestBody String jsonData) throws Exception {
		String jsonMessage=null;
		try {
			JSONObject json = JSONObject.fromObject(jsonData);
			String analysisDimension = (String)json.get("analysisDimension");
			
			String id = request.getParameter("id");
			String ageRangSql = "SELECT start_time,end_time FROM age_range_subtable WHERE parent_id="+id;
			List<Map<String, Object>> list1 = dcmsDAO.query(ageRangSql, "");
			
			String ageTime = (String)json.get("ageTime");
			String temp_str ="";
			if(ageTime!=null && ageTime!=""){
				temp_str=ageTime;
			}else{
				Date dt = new Date();   
			    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");   
			    temp_str=sdf.format(dt); 
			}
			if(analysisDimension.equals("1")){
				String sql ="";
				sql+=this.ageBalance(list1, temp_str, "CONFIRM_DATE");
				sql+=this.getMarkUnit(request);
				List<Map<String, Object>> list = dcmsDAO.query(sql, "");
				pubMethod(jsonMessage, request, response, list);
				return;
				
			}else{
				String sql ="";
				sql+=this.ageBalance(list1, temp_str, "term");
				sql+=this.getMarkUnit(request);
				List<Map<String, Object>> list = dcmsDAO.query(sql, "");
				pubMethod(jsonMessage, request, response, list);
				return;
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonMessage = "{\"status\":\"fail\",\"message\":\"系统出错\"}";
			this.ajaxWrite(jsonMessage, request, response);
			return;
		}
	}
	
	
	/**
	 * 查询账龄区间
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=selectAgeRange") 
	@ResponseBody
	public void selectAgeRange(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String jsonMessage=null;
		try {
			String sql = "SELECT id,ifnull(programme,'') as programme FROM age_range";
			List<Map<String, Object>> list = dcmsDAO.query(sql, "");
			pubMethod(jsonMessage, request, response, list);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			jsonMessage = "{\"status\":\"fail\",\"message\":\"系统出错\"}";
			this.ajaxWrite(jsonMessage, request, response);
			return;
		}
	}
	
	/**
	 * 查询账龄区间详细表
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=selectAgeRangeSub") 
	@ResponseBody
	public void selectAgeRangeSub(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String jsonMessage=null;
		try {
			String id = request.getParameter("id");
			String sql = "SELECT start_time,end_time FROM age_range_subtable WHERE parent_id="+id;
			List<Map<String, Object>> list = dcmsDAO.query(sql, "");
			pubMethod(jsonMessage, request, response, list);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			jsonMessage = "{\"status\":\"fail\",\"message\":\"系统出错\"}";
			this.ajaxWrite(jsonMessage, request, response);
			return;
		}
	}
	
	
	/**
	 * 新增账龄区间
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=saveAgeRang") 
	@ResponseBody
	public void saveAgeRang(HttpServletRequest request,HttpServletResponse response,@RequestBody String jsonData) throws Exception {
		String jsonMessage=null;
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");  
		try {
			JSONObject json = JSONObject.fromObject(jsonData);
			List<Map<String,Object>> entityList = new ArrayList<Map<String,Object>>();
			Map<String,Object> map_addMap = new HashMap<String, Object>();
			map_addMap.put("ID", sdf.format(date));
			map_addMap.put("PROGRAMME",null);
			entityList.add(map_addMap);
			
			
			System.out.println(request.getParameter("loop"));
			List<Map<String,Object>> entityList1 = new ArrayList<Map<String,Object>>();
			for(int i=0;i<Integer.parseInt(request.getParameter("loop"));i++){
				Map<String,Object> map_addMap1 = new HashMap<String, Object>();
				map_addMap1.put("ID", sdf.format(date)+i);
				map_addMap1.put("PARENT_ID",sdf.format(date));
				map_addMap1.put("START_TIME", ((JSONObject) ((JSONArray) json.get("aRang")).get(i)).get("start"));
				if( ((JSONObject) ((JSONArray) json.get("aRang")).get(i)).get("end")=="" || "".equals(((JSONObject) ((JSONArray) json.get("aRang")).get(i)).get("end"))){
					map_addMap1.put("END_TIME",null);
				}else{
					map_addMap1.put("END_TIME", ((JSONObject) ((JSONArray) json.get("aRang")).get(i)).get("end"));
				}
				entityList1.add(map_addMap1);
				
			}
		
			dcmsDAO.insert("age_range", entityList);
			dcmsDAO.insert("age_range_subtable", entityList1);
			
			jsonMessage = "{\"status\":\"success\",\"message\":\"保存成功\"}";
			this.ajaxWrite(jsonMessage, request, response);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			SqlWhereEntity sqlWhere=new SqlWhereEntity();
			sqlWhere.putWhere("ID", sdf.format(date), WhereEnum.IN);
	 		dcmsDAO.delete("age_range", sqlWhere);
	 		
			jsonMessage = "{\"status\":\"fail\",\"message\":\"系统出错\"}";
			this.ajaxWrite(jsonMessage, request, response);
			return;
		}
	}
	
	/**
	 * 删除账龄区间
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=delAgeRang") 
	public void delAgeRang(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String jsonMessage=null;
		try {
			SqlWhereEntity sqlWhere=new SqlWhereEntity();
			sqlWhere.putWhere("ID", request.getParameter("id"), WhereEnum.IN);
	 		dcmsDAO.delete("age_range", sqlWhere);
	 		
	 		SqlWhereEntity sqlWhere1=new SqlWhereEntity();
			sqlWhere1.putWhere("parent_id", request.getParameter("id"), WhereEnum.IN);
	 		dcmsDAO.delete("age_range_subtable", sqlWhere1);
	 		
			jsonMessage = "{\"status\":\"success\",\"message\":\"保存成功\"}";
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
	 * 查询销售漏斗各阶段数量
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=businessFunnel") 
	@ResponseBody
	public void businessFunnel(HttpServletRequest request,HttpServletResponse response,@RequestBody String jsonData) throws Exception {
		String jsonMessage=null;
		try {
			JSONObject json = JSONObject.fromObject(jsonData);
			//潜在商机
			String sql1 = "select count(1) as count from view_sales_funnel where 1=1 ";
			//有效商机
			String sql2 = "select count(1) as count from view_sales_funnel where is_approved='是' ";
			//有效前评估
			String sql3 = "select count(1) as count from view_sales_funnel where pre_assessment='是' ";
			//有效应标
			String sql4 = "select count(1) as count from view_sales_funnel where is_bid='是' ";
			//有效中标
			String sql5 = "select count(1) as count from view_sales_funnel where is_win_bid='是' ";
			//有效立项
			String sql6 = "select count(1) as count from view_sales_funnel where is_proj_approval='是' ";
			//有效合同
			String sql7 = "select count(1) as count from view_sales_funnel where is_sign_contract='是' ";
			if(json.get("mark_unit")!=null){
				sql1+=" and business_sub_company='"+json.get("mark_unit")+"' ";
				sql2+=" and business_sub_company='"+json.get("mark_unit")+"' ";
				sql3+=" and business_sub_company='"+json.get("mark_unit")+"' ";
				sql4+=" and business_sub_company='"+json.get("mark_unit")+"' ";
				sql5+=" and business_sub_company='"+json.get("mark_unit")+"' ";
				sql6+=" and business_sub_company='"+json.get("mark_unit")+"' ";
				sql7+=" and business_sub_company='"+json.get("mark_unit")+"' ";
			}
			if(json.get("contain_task")!=null){
				sql1+=" and contain_task like '%"+json.get("contain_task")+"%' ";
				sql2+=" and contain_task like '%"+json.get("contain_task")+"%' ";
				sql3+=" and contain_task like '%"+json.get("contain_task")+"%' ";
				sql4+=" and contain_task like '%"+json.get("contain_task")+"%' ";
				sql5+=" and contain_task like '%"+json.get("contain_task")+"%' ";
				sql6+=" and contain_task like '%"+json.get("contain_task")+"%' ";
				sql7+=" and contain_task like '%"+json.get("contain_task")+"%' ";
			}
			if(json.get("bus_type_code")!=null){
				sql1+=" and bus_type_code='"+json.get("bus_type_code")+"' ";
				sql2+=" and bus_type_code='"+json.get("bus_type_code")+"' ";
				sql3+=" and bus_type_code='"+json.get("bus_type_code")+"' ";
				sql4+=" and bus_type_code='"+json.get("bus_type_code")+"' ";
				sql5+=" and bus_type_code='"+json.get("bus_type_code")+"' ";
				sql6+=" and bus_type_code='"+json.get("bus_type_code")+"' ";
				sql7+=" and bus_type_code='"+json.get("bus_type_code")+"' ";
			}
			sql1+=this.getMarkUnit(request);
			sql2+=this.getMarkUnit(request);
			sql3+=this.getMarkUnit(request);
			sql4+=this.getMarkUnit(request);
			sql5+=this.getMarkUnit(request);
			sql6+=this.getMarkUnit(request);
			sql7+=this.getMarkUnit(request);
			
			
			//潜在商机
			JSONArray rowdata = new JSONArray();
			ergList(rowdata,dcmsDAO.query(sql1, ""));
			
			//有效商机
			JSONArray rowdata1 = new JSONArray();
			ergList(rowdata1,dcmsDAO.query(sql2, ""));
			
			//有效前评估
			JSONArray rowdata2 = new JSONArray();
			ergList(rowdata2,dcmsDAO.query(sql3, ""));
			
			//有效应标
			JSONArray rowdata3 = new JSONArray();
			ergList(rowdata3,dcmsDAO.query(sql4, ""));
			
			//有效中标
			JSONArray rowdata4 = new JSONArray();
			ergList(rowdata4,dcmsDAO.query(sql5, ""));
			
			//有效合同
			JSONArray rowdata5 = new JSONArray();
			ergList(rowdata5,dcmsDAO.query(sql7, ""));
			
			//有效立项
			JSONArray rowdata6 = new JSONArray();
			ergList(rowdata6,dcmsDAO.query(sql6, ""));
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("rowdata", rowdata);
			jsonObject.put("rowdata1", rowdata1);
			jsonObject.put("rowdata2", rowdata2);
			jsonObject.put("rowdata3", rowdata3);
			jsonObject.put("rowdata4", rowdata4);
			jsonObject.put("rowdata5", rowdata5);
			jsonObject.put("rowdata6", rowdata6);
			
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
	 * 查询销售漏斗各阶段详细信息  //改用视图view_sales_funnel
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=businessFunnelTab") 
	@ResponseBody
	public void businessFunnelTab(HttpServletRequest request,HttpServletResponse response,@RequestBody String jsonData) throws Exception {
		String jsonMessage=null;
		try {
			
			JSONObject json = JSONObject.fromObject(jsonData);
			
			String name = json.get("name").toString();
			String seriesName =json.get("seriesName").toString();
			//潜在商机
			String sql1 = "select * from view_sales_funnel where 1=1 ";
			//有效商机
			String sql2 = "select * from view_sales_funnel where is_approved='是' ";
			//有效前评估
			String sql3 = "select * from view_sales_funnel where pre_assessment='是' ";
			//有效应标
			String sql4 = "select * from view_sales_funnel where is_bid='是' ";
			//有效中标
			String sql5 = "select * from view_sales_funnel where is_win_bid='是' ";
			//有效立项
			String sql6 = "select * from view_sales_funnel where is_proj_approval='是' ";
			//有效合同
			String sql7 = "select * from view_sales_funnel where is_sign_contract='是' ";
			
			if(json.get("mark_unit")!=null){
				sql1+=" and business_sub_company='"+json.get("mark_unit")+"' ";
				sql2+=" and business_sub_company='"+json.get("mark_unit")+"' ";
				sql3+=" and business_sub_company='"+json.get("mark_unit")+"' ";
				sql4+=" and business_sub_company='"+json.get("mark_unit")+"' ";
				sql5+=" and business_sub_company='"+json.get("mark_unit")+"' ";
				sql6+=" and business_sub_company='"+json.get("mark_unit")+"' ";
				sql7+=" and business_sub_company='"+json.get("mark_unit")+"' ";
			}
			if(json.get("contain_task")!=null){
				sql1+=" and contain_task like '%"+json.get("contain_task")+"%' ";
				sql2+=" and contain_task like '%"+json.get("contain_task")+"%' ";
				sql3+=" and contain_task like '%"+json.get("contain_task")+"%' ";
				sql4+=" and contain_task like '%"+json.get("contain_task")+"%' ";
				sql5+=" and contain_task like '%"+json.get("contain_task")+"%' ";
				sql6+=" and contain_task like '%"+json.get("contain_task")+"%' ";
				sql7+=" and contain_task like '%"+json.get("contain_task")+"%' ";
			}
			if(json.get("bus_type_code")!=null){
				sql1+=" and bus_type_code='"+json.get("bus_type_code")+"' ";
				sql2+=" and bus_type_code='"+json.get("bus_type_code")+"' ";
				sql3+=" and bus_type_code='"+json.get("bus_type_code")+"' ";
				sql4+=" and bus_type_code='"+json.get("bus_type_code")+"' ";
				sql5+=" and bus_type_code='"+json.get("bus_type_code")+"' ";
				sql6+=" and bus_type_code='"+json.get("bus_type_code")+"' ";
				sql7+=" and bus_type_code='"+json.get("bus_type_code")+"' ";
			}
			sql1+=this.getMarkUnit(request);
			sql2+=this.getMarkUnit(request);
			sql3+=this.getMarkUnit(request);
			sql4+=this.getMarkUnit(request);
			sql5+=this.getMarkUnit(request);
			sql6+=this.getMarkUnit(request);
			sql7+=this.getMarkUnit(request);
			
			JSONArray rowdata = new JSONArray();
			if("商机".equals(name)){
				if("潜在".equals(seriesName)){
					rowdata = new JSONArray();
					ergList(rowdata,dcmsDAO.query(sql1, ""));
				}else{
					rowdata = new JSONArray();
					ergList(rowdata,dcmsDAO.query(sql2, ""));
				}
			}
			if("前评估".equals(name)){
				if("潜在".equals(seriesName)){
					rowdata = new JSONArray();
					ergList(rowdata,dcmsDAO.query(sql2, ""));
				}else{
					rowdata = new JSONArray();
					ergList(rowdata,dcmsDAO.query(sql3, ""));
				}
			}
			if("应标".equals(name)){
				if("潜在".equals(seriesName)){
					rowdata = new JSONArray();
					ergList(rowdata,dcmsDAO.query(sql3, ""));
				}else{
					rowdata = new JSONArray();
					ergList(rowdata,dcmsDAO.query(sql4, ""));
				}
			}
			if("中标".equals(name)){
				if("潜在".equals(seriesName)){
					rowdata = new JSONArray();
					ergList(rowdata,dcmsDAO.query(sql4, ""));
				}else{
					rowdata = new JSONArray();
					ergList(rowdata,dcmsDAO.query(sql5, ""));
				}
			}
			if("立项".equals(name)){
				if("潜在".equals(seriesName)){
					rowdata = new JSONArray();
					ergList(rowdata,dcmsDAO.query(sql5, ""));
				}else{
					rowdata = new JSONArray();
					ergList(rowdata,dcmsDAO.query(sql6, ""));
				}
			}
			if("合同".equals(name)){
				if("潜在".equals(seriesName)){
					rowdata = new JSONArray();
					ergList(rowdata,dcmsDAO.query(sql6, ""));
				}else{
					rowdata = new JSONArray();
					ergList(rowdata,dcmsDAO.query(sql7, ""));
				}
			}
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("rowdata", rowdata);
			
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
	 * 丢标打分
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=selectLost") 
	@ResponseBody
	public void selectLost(HttpServletRequest request,HttpServletResponse response,@RequestBody String jsonData) throws Exception {
		String jsonMessage=null;
		try {
			JSONObject json = JSONObject.fromObject(jsonData);
			String sql = "SELECT bs.grading_project as grading_project ,format(avg(bs.fraction),2) as fraction  FROM `bus_win_bid_result` br join bus_win_bid_result_score bs on br.id=bs.parent_id left join bus_business_message bm on br.bus_ids=bm.id WHERE result=0 ";
			if(json.get("mark_unit")!=null){
				sql+=" and bm.business_sub_company='"+json.get("mark_unit")+"' ";
			}
			if(json.get("contain_task")!=null){
				sql+=" and bm.contain_task like '%"+json.get("contain_task")+"%' ";
			}
			sql+=this.getMarkUnit(request);
			sql+=" GROUP BY bs.grading_project";
			List<Map<String, Object>> list = dcmsDAO.query(sql, "");
			pubMethod(jsonMessage, request, response, list);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			jsonMessage = "{\"status\":\"fail\",\"message\":\"系统出错\"}";
			this.ajaxWrite(jsonMessage, request, response);
			return;
		}
	}
	
	/**
	 * 根据计收单据号查询开票记录
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=selectInvoice") 
	@ResponseBody
	public void selectInvoice(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String jsonMessage=null;
		try {
			String sql = "select concat(ID,'') as ID,BILL_NO,APPLY_DATE,APPLY_AMOUNT  from fin_invoice_open where CONFIRM_NO='"+request.getParameter("bill_no")+"'";
			List<Map<String, Object>> list = dcmsDAO.query(sql, "");
			
			pubMethod(jsonMessage, request, response, list);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			jsonMessage = "{\"status\":\"fail\",\"message\":\"系统出错\"}";
			this.ajaxWrite(jsonMessage, request, response);
			return;
		}
	}
	
	/**
	 * 根据开票单id查询开票明细
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=selectInvoiceDetail") 
	@ResponseBody
	public void selectInvoiceDetail(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String jsonMessage=null;
		try {
			String sql = "select invoice_no,invoice_code,open_data,open_amount  from fin_invoice_real_open where fin_apply_id='"+request.getParameter("id")+"'";
			List<Map<String, Object>> list = dcmsDAO.query(sql, "");
			pubMethod(jsonMessage, request, response, list);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			jsonMessage = "{\"status\":\"fail\",\"message\":\"系统出错\"}";
			this.ajaxWrite(jsonMessage, request, response);
			return;
		}
	}
	
	/**
	 * 查看客户回款认领
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=selectReceived") 
	@ResponseBody
	public void selectReceived(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String jsonMessage=null;
		try {
			String sql = "select concat(ID,'') as ID,OPPOSITE_UNIT,OPPOSITE_ACCOUNT,ENTER_DATE,RECEIVE_AMOUNT  from fin_received_payment where ID='"+request.getParameter("received_payment_id")+"'";
			List<Map<String, Object>> list = dcmsDAO.query(sql, "");
			
			pubMethod(jsonMessage, request, response, list);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			jsonMessage = "{\"status\":\"fail\",\"message\":\"系统出错\"}";
			this.ajaxWrite(jsonMessage, request, response);
			return;
		}
	}
	
	/**
	 * 查看回款认领详情
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=selectReceivedDetail") 
	@ResponseBody
	public void selectReceivedDetail(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String jsonMessage=null;
		try {
			String sql = "select CUSTOMER_MANAGER,DEPARTMENT,BILL_NO,CONTRACT_NO,CLAIM_AMOUNT,(SELECT `cusc_test`.`rm_code_data`.`DATA_VALUE` FROM `cusc_test`.`rm_code_data` WHERE((`cusc_test`.`rm_code_data`.`CODE_TYPE_ID` = ( SELECT `cusc_test`.`rm_code_type`.`ID` FROM `cusc_test`.`rm_code_type` WHERE ( `cusc_test`.`rm_code_type`.`TYPE_KEYWORD` = 'CLAIM_TYPE' ) ) ) AND ( `cusc_test`.`rm_code_data`.`DATA_KEY` = `fin_claim_detail`.`CLAIM_TYPE` ) ) ) as CLAIM_TYPE,CLAIM_DATE  from fin_claim_detail where PARENT_ID='"+request.getParameter("id")+"'";
			List<Map<String, Object>> list = dcmsDAO.query(sql, "");
			pubMethod(jsonMessage, request, response, list);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			jsonMessage = "{\"status\":\"fail\",\"message\":\"系统出错\"}";
			this.ajaxWrite(jsonMessage, request, response);
			return;
		}
	}
	
	/**
	 * 查找业务类型
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=selectBusType") 
	@ResponseBody
	public void selectBusType(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String jsonMessage=null;
		try {
			String sql = "select type_name from buss_type";
			List<Map<String, Object>> list = dcmsDAO.query(sql, "");
			pubMethod(jsonMessage, request, response, list);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			jsonMessage = "{\"status\":\"fail\",\"message\":\"系统出错\"}";
			this.ajaxWrite(jsonMessage, request, response);
			return;
		}
	}
	
	/**
	 * 查找营销单元
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=selectMarkUnit") 
	@ResponseBody
	public void selectMarkUnit(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String jsonMessage=null;
		try {
			String sql = "select concat(id,'') as id,name from tm_company";
			List<Map<String, Object>> list = dcmsDAO.query(sql, "");
			pubMethod(jsonMessage, request, response, list);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			jsonMessage = "{\"status\":\"fail\",\"message\":\"系统出错\"}";
			this.ajaxWrite(jsonMessage, request, response);
			return;
		}
	}
	
	
	private void pubMethod(String jsonMessage,HttpServletRequest request,HttpServletResponse response,List<Map<String, Object>> list) throws Exception{
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
		
		System.out.println("------------------");
		System.out.println(jsonObject);
		
		jsonMessage = "{\"status\":\"success\",\"body\":"+jsonObject+"}";
		this.ajaxWrite(jsonMessage, request, response);
		
	}
	
	private String getMarkUnit(HttpServletRequest request){
		TokenEntity tokenEntity = TokenUtil.initTokenEntity(request);
		JSONObject json = JSONObject.fromObject(tokenEntity.ROLE.getDataAuth());
		System.out.println("json:"+json);
		if(json.get("company")!=null && !"".equals(json.get("company"))){
			return " and organization_id="+json.get("company");
		}
		return "";
	}
	
	private String ageBalance(List<Map<String, Object>> list,String temp_str,String cate){
		String sql = "select ifnull(v.CLIENT_NAME,'') as CLIENT_NAME,ifnull(v.PROJ_CODE,'') as PROJ_CODE,ifnull(v.PROJ_NAME,'') as PROJ_NAME,ifnull(v.CONTRACT_CODE,'') as CONTRACT_CODE,ifnull(v.CONTRACT_NAME,'') as CONTRACT_NAME,ifnull(v.CLIENT_MANAGER,'') as CLIENT_MANAGER,ifnull(v.DEPT,'') as DEPT,ifnull(v.PAYBACK_PERIOD,'') as PAYBACK_PERIOD";
		for(int i=0;i<list.size();i++){
			String vi = "v"+i;
			String cb = "cb"+i;
			if(list.get(i).get("END_TIME")!=null && i==list.size()-1){
				sql+=",ifnull("+vi+".claim_balance , 0) as "+cb;
				int j=i+1;
				String vj="v"+j;
				String cbj="cb"+j;
				sql+=",ifnull("+vj+".claim_balance , 0) as "+cbj;
			}else{
				sql+=",ifnull("+vi+".claim_balance , 0) as "+cb;
			}
		}
		sql+=" from view_balance_and_age v";
		for(int i=0;i<list.size();i++){
			String vi = "v"+i;
			int start=Integer.parseInt(((String)list.get(i).get("START_TIME")).substring(0, 1));
			int end=0;
			if(list.get(i).get("END_TIME")!=null){
				end=Integer.parseInt(((String)list.get(i).get("END_TIME")).substring(0, 1));
			}
			String moy=((String)list.get(i).get("START_TIME")).substring(1, 2);
			if(list.get(i).get("END_TIME")!=null){
				if(i==list.size()-1){
					if("月".equals(moy)){
						sql+=" left join ( SELECT id, claim_balance FROM view_balance_and_age WHERE datediff( '"+temp_str+"', "+cate+" ) BETWEEN "+start*30+" AND "+end*30+" ) "+vi+" ON v.id = "+vi+".id ";
						int j=i+1;
						String vj="v"+j;
						sql+=" left join ( SELECT id, claim_balance FROM view_balance_and_age WHERE datediff( '"+temp_str+"', "+cate+" )>"+end*30+" ) "+vj+" ON v.id = "+vj+".id";
					}else{
						sql+=" left join ( SELECT id, claim_balance FROM view_balance_and_age WHERE datediff( '"+temp_str+"', "+cate+" ) BETWEEN "+start*365+" AND "+end*365+" ) "+vi+" ON v.id = "+vi+".id ";
						int j=i+1;
						String vj="v"+j;
						sql+=" left join ( SELECT id, claim_balance FROM view_balance_and_age WHERE datediff( '"+temp_str+"', "+cate+" )>"+end*365+" ) "+vj+" ON v.id = "+vj+".id";
					}
				}else{
					if("月".equals(moy))
						sql+=" left join ( SELECT id, claim_balance FROM view_balance_and_age WHERE datediff( '"+temp_str+"', "+cate+" ) BETWEEN "+start*30+" AND "+end*30+" ) "+vi+" ON v.id = "+vi+".id";
					else
						sql+=" left join ( SELECT id, claim_balance FROM view_balance_and_age WHERE datediff( '"+temp_str+"', "+cate+" ) BETWEEN "+start*365+" AND "+end*365+" ) "+vi+" ON v.id = "+vi+".id";
				}
			}else{
				if("月".equals(moy))
					sql+=" left join ( SELECT id, claim_balance FROM view_balance_and_age WHERE datediff( '"+temp_str+"', "+cate+" )>"+start*30+" ) "+vi+" ON v.id = "+vi+".id";
				else
					sql+=" left join ( SELECT id, claim_balance FROM view_balance_and_age WHERE datediff( '"+temp_str+"', "+cate+" )>"+start*365+" ) "+vi+" ON v.id = "+vi+".id";
			}
		}
		sql+=" where confirm_date< '"+temp_str+"'";
		return sql;
	}
	
	//转json
	private void ergList(JSONArray rowdata,List<Map<String, Object>> list) throws Exception{
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
	}
}
