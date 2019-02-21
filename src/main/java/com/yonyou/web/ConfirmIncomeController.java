package com.yonyou.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import com.yonyou.business.entity.TokenEntity;
import com.yonyou.business.entity.TokenUtil;
import com.yonyou.util.BussnissException;
import com.yonyou.util.ExcelTools;
import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.sql.SQLUtil.WhereEnum;
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.util.sql.SqlWhereEntity;

/**	元数据左右选择框控制类
 * @author XIANGJIAN
 * @date 创建时间：2017年4月6日
 * @version 1.0
 */
@RestController
@RequestMapping(value = "/confirmIncome")
public class ConfirmIncomeController extends BaseController {
	private static java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 初始化发生日期
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=initHappenDate", method = RequestMethod.POST) 
	@ResponseBody
	public void initHappenDate(HttpServletRequest request,HttpServletResponse response,@RequestBody String jsonData) throws Exception {
		
		JSONObject json = JSONObject.fromObject(jsonData);
		String jsonMessage=null;
		try {
			
			
			String sqlString="SELECT HAPPEN_DATE,CHARGE FROM FIN_AUDIT_CHARGE_DETAIL_HAPPEN_DATE WHERE parent_id = '"+json.getString("ID")+"'";	 
			List<Map<String, Object>> list = BaseDao.getBaseDao().query(sqlString.toLowerCase(),"");

			jsonMessage = "{\"status\":\"success\",\"body\":"+JSONArray.fromObject(list).toString()+"}";
			this.ajaxWrite(jsonMessage, request, response);
			return;
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			jsonMessage = "{\"status\":\"fail\",\"message\":\"系统出错\"}";
			this.ajaxWrite(jsonMessage, request, response);
			return;
		}
	}
	private void ajaxWrite(String ajaxMessage,HttpServletRequest request,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.print(ajaxMessage);
		out.flush();
		out.close();
	}
	private void appendWhereByIDs(JSONObject json,SqlWhereEntity whereEntity){
		
		String id = json.getString("ID");
		whereEntity.putWhere("ID", id, WhereEnum.EQUAL_INT);
	}
	
	/**
	 * 查询登陆用户信息
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=queryLoginUserInfo",method=RequestMethod.POST) 
	@ResponseBody
	public void queryLoginUserInfo(HttpServletRequest request,HttpServletResponse response,@RequestBody String jsonData) throws Exception {
		
		String jsonMessage=null;
		try {
			//获取当前登陆用户的角色
			TokenEntity tokenEntity = TokenUtil.initTokenEntity(request);
			String roleId = tokenEntity.ROLE.getRoleId();

			SqlTableUtil sqlEntity = DataSourceUtil.dataSourceToSQL("RM_ROLE");
			SqlWhereEntity sqlWhereEntity = new SqlWhereEntity();
			sqlWhereEntity.putWhere("ID", roleId, WhereEnum.EQUAL_STRING);
			sqlEntity.appendSqlWhere(sqlWhereEntity);
			Map<String, Object> enty = dcmsDAO.find(sqlEntity);
			
			roleId=(String)enty.get("ROLE_CODE");
			String userId = tokenEntity.USER.getId();
			String dept = tokenEntity.COMPANY.getCompany_name()+"("+tokenEntity.COMPANY.getCompany_id()+")";
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("roleId", roleId);
			jsonObject.put("userId", userId);
			String format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			jsonObject.put("APPLY_DATE",format);
			jsonObject.put("DEPT", dept);
			jsonObject.put("CLIENT_MANAGER", tokenEntity.USER.getName());
			jsonMessage = "{\"status\":\"success\",\"body\":"+jsonObject.toString()+"}";
			this.ajaxWrite(jsonMessage, request, response);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			jsonMessage = "{\"status\":\"fail\",\"message\":\"系统出错\"}";
			this.ajaxWrite(jsonMessage, request, response);
			return;
		}
	}
	
	protected String findTableNameByDataSourceCode(String dataSourceCode) throws BussnissException{
		String tableName="";
		Map<String,String> dataMap=DataSourceUtil.getDataSource(dataSourceCode);
		if(dataMap!=null&&dataMap.containsKey(DataSourceUtil.DATASOURCE_METADATA_CODE)){
			tableName= dataMap.get(DataSourceUtil.DATASOURCE_METADATA_CODE);
		}
		
		return tableName;
		
	}

	/**
	 * 查询登陆用户信息
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=queryLoginUserOperateAuth",method=RequestMethod.POST) 
	@ResponseBody
	public void queryLoginUserOperateAuth(HttpServletRequest request,HttpServletResponse response,@RequestBody String jsonData) throws Exception {
		
		String jsonMessage=null;
		try {
			//获取当前登陆用户的角色
			TokenEntity tokenEntity = TokenUtil.initTokenEntity(request);
			String roleId = tokenEntity.ROLE.getRoleId();

			SqlTableUtil sqlEntity = DataSourceUtil.dataSourceToSQL("RM_ROLE");
			SqlWhereEntity sqlWhereEntity = new SqlWhereEntity();
			sqlWhereEntity.putWhere("ID", roleId, WhereEnum.EQUAL_STRING);
			sqlEntity.appendSqlWhere(sqlWhereEntity);
			Map<String, Object> enty = dcmsDAO.find(sqlEntity);
			
			roleId=(String)enty.get("ROLE_CODE");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("auth", false);
			if(!roleId.equals("ZHCWJSZG")){

				sqlEntity = DataSourceUtil.dataSourceToSQL("FIN_CONFIRM_BUSINESS_OPEN");
				sqlWhereEntity = new SqlWhereEntity();
				sqlWhereEntity.putWhere("CLIENT_MANAGER_ID", tokenEntity.USER.getId(), WhereEnum.EQUAL_STRING);
				sqlWhereEntity.putWhere("APPLY_STATE", "3", WhereEnum.EQUAL_STRING);
				Calendar instance = Calendar.getInstance();
				instance.setTime(new Date());
				instance.add(Calendar.DAY_OF_MONTH, -1);
				
				sqlWhereEntity.putWhere("OPEN_TERM", sdf.format(instance.getTime()), WhereEnum.TO_DATE_GREATER);
				sqlEntity.appendSqlWhere(sqlWhereEntity);
				sqlEntity.appendOrderBy("OPEN_TERM", "DESC");
				List<Map<String, Object>> entryList = dcmsDAO.query(sqlEntity);
				if(entryList.size()>0){
					jsonObject.put("auth", true);
				}
			}else{
				jsonObject.put("auth", true);
			}
			jsonMessage = "{\"status\":\"success\",\"body\":"+jsonObject.toString()+"}";
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
	 * 计收统计
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=incomeAnalysis") 
	@ResponseBody
	public void incomeAnalysis(HttpServletRequest request,HttpServletResponse response,@RequestBody String jsonData) throws Exception {
 
		String jsonMessage=null;
		try {
			JSONObject json = JSONObject.fromObject(jsonData);
			java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("yyyy");
			Calendar calendar = Calendar.getInstance();
			simpleDateFormat.format(calendar.getTime());
			if(json.get("year")!=null&&!json.get("year").equals("")){
				calendar.setTime(simpleDateFormat.parse(json.getString("year")));
			}
			String nowYear=simpleDateFormat.format(calendar.getTime());
			calendar.add(Calendar.YEAR, 1);
			String nextYear=simpleDateFormat.format(calendar.getTime());

			String sql="select IFNULL(d.CONTRACT_CODE,'') as CONTRACT_CODE,IFNULL(d.CONTRACT_NAME,'') as CONTRACT_NAME,IFNULL(IFNULL(e.PROJ_CODE,c.PROJ_CODE),'') as PROJ_CODE,IFNULL(IFNULL(e.PROJ_NAME,c.PROJ_NAME),'') as PROJ_NAME ";
			sql+=",IFNULL(sum((case when dh.happen_date <'"+nowYear+"01'  then dh.charge else 0 end )),0) as previous_year_confirm  ";
			sql+=",IFNULL(sum((case when dh.happen_date ='"+nowYear+"01'  then dh.charge else 0 end )),0) as month01_confirm  ";
			sql+=",IFNULL(sum((case when dh.happen_date ='"+nowYear+"02'  then dh.charge else 0 end )),0) as month02_confirm  ";
			sql+=",IFNULL(sum((case when dh.happen_date ='"+nowYear+"03'  then dh.charge else 0 end )),0) as month03_confirm  ";
			sql+=",IFNULL(sum((case when dh.happen_date ='"+nowYear+"04'  then dh.charge else 0 end )),0) as month04_confirm  ";
			sql+=",IFNULL(sum((case when dh.happen_date ='"+nowYear+"05'  then dh.charge else 0 end )),0) as month05_confirm  ";
			sql+=",IFNULL(sum((case when dh.happen_date ='"+nowYear+"06'  then dh.charge else 0 end )),0) as month06_confirm  ";
			sql+=",IFNULL(sum((case when dh.happen_date ='"+nowYear+"07'  then dh.charge else 0 end )),0) as month07_confirm  ";
			sql+=",IFNULL(sum((case when dh.happen_date ='"+nowYear+"08'  then dh.charge else 0 end )),0) as month08_confirm  ";
			sql+=",IFNULL(sum((case when dh.happen_date ='"+nowYear+"09'  then dh.charge else 0 end )),0) as month09_confirm  ";
			sql+=",IFNULL(sum((case when dh.happen_date ='"+nowYear+"10'  then dh.charge else 0 end )),0) as month10_confirm  ";
			sql+=",IFNULL(sum((case when dh.happen_date ='"+nowYear+"11'  then dh.charge else 0 end )),0) as month11_confirm  ";
			sql+=",IFNULL(sum((case when dh.happen_date ='"+nowYear+"12'  then dh.charge else 0 end )),0) as month12_confirm  ";
			sql+=",IFNULL(sum((case when dh.happen_date >='"+nowYear+"-01-01' and dh.happen_date <'"+nextYear+"-01-01' then dh.charge else 0 end )),0) as month_total_confirm ";
			sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-01-01' and d.CONTRACT_EFFECT_DATE <'"+nowYear+"-02-01'  then d.ESTIMATED_INCOME else 0 end )),0) as month01_estimate ";
			sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-02-01' and d.CONTRACT_EFFECT_DATE <'"+nowYear+"-03-01'  then d.ESTIMATED_INCOME else 0 end )),0) as month02_estimate ";
			sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-03-01' and d.CONTRACT_EFFECT_DATE <'"+nowYear+"-04-01'  then d.ESTIMATED_INCOME else 0 end )),0) as month03_estimate ";
			sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-04-01' and d.CONTRACT_EFFECT_DATE <'"+nowYear+"-05-01'  then d.ESTIMATED_INCOME else 0 end )),0) as month04_estimate ";
			sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-05-01' and d.CONTRACT_EFFECT_DATE <'"+nowYear+"-06-01'  then d.ESTIMATED_INCOME else 0 end )),0) as month05_estimate ";
			sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-06-01' and d.CONTRACT_EFFECT_DATE <'"+nowYear+"-07-01'  then d.ESTIMATED_INCOME else 0 end )),0) as month06_estimate ";
			sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-07-01' and d.CONTRACT_EFFECT_DATE <'"+nowYear+"-08-01'  then d.ESTIMATED_INCOME else 0 end )),0) as month07_estimate ";
			sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-08-01' and d.CONTRACT_EFFECT_DATE <'"+nowYear+"-09-01'  then d.ESTIMATED_INCOME else 0 end )),0) as month08_estimate ";
			sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-09-01' and d.CONTRACT_EFFECT_DATE <'"+nowYear+"-10-01'  then d.ESTIMATED_INCOME else 0 end )),0) as month09_estimate ";
			sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-10-01' and d.CONTRACT_EFFECT_DATE <'"+nowYear+"-11-01'  then d.ESTIMATED_INCOME else 0 end )),0) as month10_estimate ";
			sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-11-01' and d.CONTRACT_EFFECT_DATE <'"+nowYear+"-12-01'  then d.ESTIMATED_INCOME else 0 end )),0) as month11_estimate ";
			sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-12-01' and d.CONTRACT_EFFECT_DATE <'"+nextYear+"-01-01'  then d.ESTIMATED_INCOME else 0 end )),0) as month12_estimate  ";
			sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-01-01' and d.CONTRACT_EFFECT_DATE <'"+nextYear+"-01-01'  then d.ESTIMATED_INCOME else 0 end )),0) as month_total_estimate  ";
			sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-01-01' and d.CONTRACT_EFFECT_DATE <'"+nowYear+"-02-01'  then d.ESTIMATED_INCOME+d.ADJUST_INCOME else 0 end )),0) as month01_account ";
			sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-02-01' and d.CONTRACT_EFFECT_DATE <'"+nowYear+"-03-01'  then d.ESTIMATED_INCOME+d.ADJUST_INCOME else 0 end )),0) as month02_account ";
			sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-03-01' and d.CONTRACT_EFFECT_DATE <'"+nowYear+"-04-01'  then d.ESTIMATED_INCOME+d.ADJUST_INCOME else 0 end )),0) as month03_account ";
			sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-04-01' and d.CONTRACT_EFFECT_DATE <'"+nowYear+"-05-01'  then d.ESTIMATED_INCOME+d.ADJUST_INCOME else 0 end )),0) as month04_account ";
			sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-05-01' and d.CONTRACT_EFFECT_DATE <'"+nowYear+"-06-01'  then d.ESTIMATED_INCOME+d.ADJUST_INCOME else 0 end )),0) as month05_account ";
			sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-06-01' and d.CONTRACT_EFFECT_DATE <'"+nowYear+"-07-01'  then d.ESTIMATED_INCOME+d.ADJUST_INCOME else 0 end )),0) as month06_account ";
			sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-07-01' and d.CONTRACT_EFFECT_DATE <'"+nowYear+"-08-01'  then d.ESTIMATED_INCOME+d.ADJUST_INCOME else 0 end )),0) as month07_account ";
			sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-08-01' and d.CONTRACT_EFFECT_DATE <'"+nowYear+"-09-01'  then d.ESTIMATED_INCOME+d.ADJUST_INCOME else 0 end )),0) as month08_account ";
			sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-09-01' and d.CONTRACT_EFFECT_DATE <'"+nowYear+"-10-01'  then d.ESTIMATED_INCOME+d.ADJUST_INCOME else 0 end )),0) as month09_account ";
			sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-10-01' and d.CONTRACT_EFFECT_DATE <'"+nowYear+"-11-01'  then d.ESTIMATED_INCOME+d.ADJUST_INCOME else 0 end )),0) as month10_account ";
			sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-11-01' and d.CONTRACT_EFFECT_DATE <'"+nowYear+"-12-01'  then d.ESTIMATED_INCOME+d.ADJUST_INCOME else 0 end )),0) as month11_account ";
			sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-12-01' and d.CONTRACT_EFFECT_DATE <'"+nextYear+"-01-01'  then d.ESTIMATED_INCOME+d.ADJUST_INCOME else 0 end )),0) as month12_account  ";
			sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-01-01' and d.CONTRACT_EFFECT_DATE <'"+nextYear+"-01-01'  then d.ESTIMATED_INCOME+d.ADJUST_INCOME else 0 end )),0) as month_total_account  ";
			sql+=",d.organization_id,d.organization_name from  fin_audit_charge_detail d  LEFT JOIN  fin_audit_charge_detail_happen_date dh on dh.parent_id=d.ID  LEFT JOIN fin_estimated_income e ";
			sql+="on e.ID=d.estimated_id LEFT JOIN  fin_confirm_income c on c.BILL_NO=d.PARENT_ID LEFT JOIN tm_company t on d.INCOME_OF_DEPT_ID=t.ID  where 1=1   and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-01-01' and d.CONTRACT_EFFECT_DATE <'"+nextYear+"-01-01'  and t.ID is not null  and t.OU_TYPE='0'  ";

			if(json.get("company")!=null&&!json.get("company").equals("")){
				sql+=" and d.INCOME_OF_DEPT_ID='"+json.get("company")+"' ";
			}
			sql+=" GROUP BY d.CONTRACT_CODE  HAVING d.CONTRACT_CODE is not null ;";
			sql+="";
			sql+="";
			sql+="";
			List<Map<String, Object>> list = dcmsDAO.query(sql, "");
			JSONArray jsonArray = new JSONArray();
			for (Map<String, Object> map : list) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.putAll(map);
				jsonArray.add(jsonObject);
			}
			jsonMessage = "{\"status\":\"success\",\"body\":"+jsonArray+",\"year\":"+nowYear+"}";
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
	 * 计收统计导出
	 * @param request
	 * @param response
	 * @throws ParseException 
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=incomeAnalysisExport") 
	public Object incomeAnalysisExport(HttpServletRequest request,HttpServletResponse response) throws ParseException  {
 
		String jsonDataEncode=request.getParameter("json");
		String jsonData =null;
		try {
			jsonData = URLDecoder.decode(jsonDataEncode,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		JSONObject json = JSONObject.fromObject(jsonData);
		java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("yyyy");
		Calendar calendar = Calendar.getInstance();
		simpleDateFormat.format(calendar.getTime());
		if(json.get("year")!=null&&!json.get("year").equals("")){
			calendar.setTime(simpleDateFormat.parse(json.getString("year")));
		}
		String nowYear=simpleDateFormat.format(calendar.getTime());
		calendar.add(Calendar.YEAR, 1);
		String nextYear=simpleDateFormat.format(calendar.getTime());

		String sql="select IFNULL(d.CONTRACT_CODE,'') as CONTRACT_CODE,IFNULL(d.CONTRACT_NAME,'') as CONTRACT_NAME,IFNULL(e.PROJ_CODE,c.PROJ_CODE) as PROJ_CODE,IFNULL(e.PROJ_NAME,c.PROJ_NAME) as PROJ_NAME ";
		sql+=",IFNULL(sum((case when dh.happen_date <'"+nowYear+"01'  then dh.charge else 0 end )),0) as previous_year_confirm  ";
		sql+=",IFNULL(sum((case when dh.happen_date ='"+nowYear+"01'  then dh.charge else 0 end )),0) as month01_confirm  ";
		sql+=",IFNULL(sum((case when dh.happen_date ='"+nowYear+"02'  then dh.charge else 0 end )),0) as month02_confirm  ";
		sql+=",IFNULL(sum((case when dh.happen_date ='"+nowYear+"03'  then dh.charge else 0 end )),0) as month03_confirm  ";
		sql+=",IFNULL(sum((case when dh.happen_date ='"+nowYear+"04'  then dh.charge else 0 end )),0) as month04_confirm  ";
		sql+=",IFNULL(sum((case when dh.happen_date ='"+nowYear+"05'  then dh.charge else 0 end )),0) as month05_confirm  ";
		sql+=",IFNULL(sum((case when dh.happen_date ='"+nowYear+"06'  then dh.charge else 0 end )),0) as month06_confirm  ";
		sql+=",IFNULL(sum((case when dh.happen_date ='"+nowYear+"07'  then dh.charge else 0 end )),0) as month07_confirm  ";
		sql+=",IFNULL(sum((case when dh.happen_date ='"+nowYear+"08'  then dh.charge else 0 end )),0) as month08_confirm  ";
		sql+=",IFNULL(sum((case when dh.happen_date ='"+nowYear+"09'  then dh.charge else 0 end )),0) as month09_confirm  ";
		sql+=",IFNULL(sum((case when dh.happen_date ='"+nowYear+"10'  then dh.charge else 0 end )),0) as month10_confirm  ";
		sql+=",IFNULL(sum((case when dh.happen_date ='"+nowYear+"11'  then dh.charge else 0 end )),0) as month11_confirm  ";
		sql+=",IFNULL(sum((case when dh.happen_date ='"+nowYear+"12'  then dh.charge else 0 end )),0) as month12_confirm  ";
		sql+=",IFNULL(sum((case when dh.happen_date >='"+nowYear+"-01-01' and dh.happen_date <'"+nextYear+"-01-01' then dh.charge else 0 end )),0) as month_total_confirm ";
		sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-01-01' and d.CONTRACT_EFFECT_DATE <'"+nowYear+"-02-01'  then d.ESTIMATED_INCOME else 0 end )),0) as month01_estimate ";
		sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-02-01' and d.CONTRACT_EFFECT_DATE <'"+nowYear+"-03-01'  then d.ESTIMATED_INCOME else 0 end )),0) as month02_estimate ";
		sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-03-01' and d.CONTRACT_EFFECT_DATE <'"+nowYear+"-04-01'  then d.ESTIMATED_INCOME else 0 end )),0) as month03_estimate ";
		sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-04-01' and d.CONTRACT_EFFECT_DATE <'"+nowYear+"-05-01'  then d.ESTIMATED_INCOME else 0 end )),0) as month04_estimate ";
		sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-05-01' and d.CONTRACT_EFFECT_DATE <'"+nowYear+"-06-01'  then d.ESTIMATED_INCOME else 0 end )),0) as month05_estimate ";
		sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-06-01' and d.CONTRACT_EFFECT_DATE <'"+nowYear+"-07-01'  then d.ESTIMATED_INCOME else 0 end )),0) as month06_estimate ";
		sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-07-01' and d.CONTRACT_EFFECT_DATE <'"+nowYear+"-08-01'  then d.ESTIMATED_INCOME else 0 end )),0) as month07_estimate ";
		sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-08-01' and d.CONTRACT_EFFECT_DATE <'"+nowYear+"-09-01'  then d.ESTIMATED_INCOME else 0 end )),0) as month08_estimate ";
		sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-09-01' and d.CONTRACT_EFFECT_DATE <'"+nowYear+"-10-01'  then d.ESTIMATED_INCOME else 0 end )),0) as month09_estimate ";
		sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-10-01' and d.CONTRACT_EFFECT_DATE <'"+nowYear+"-11-01'  then d.ESTIMATED_INCOME else 0 end )),0) as month10_estimate ";
		sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-11-01' and d.CONTRACT_EFFECT_DATE <'"+nowYear+"-12-01'  then d.ESTIMATED_INCOME else 0 end )),0) as month11_estimate ";
		sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-12-01' and d.CONTRACT_EFFECT_DATE <'"+nextYear+"-01-01'  then d.ESTIMATED_INCOME else 0 end )),0) as month12_estimate  ";
		sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-01-01' and d.CONTRACT_EFFECT_DATE <'"+nextYear+"-01-01'  then d.ESTIMATED_INCOME else 0 end )),0) as month_total_estimate  ";
		sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-01-01' and d.CONTRACT_EFFECT_DATE <'"+nowYear+"-02-01'  then d.ESTIMATED_INCOME+d.ADJUST_INCOME else 0 end )),0) as month01_account ";
		sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-02-01' and d.CONTRACT_EFFECT_DATE <'"+nowYear+"-03-01'  then d.ESTIMATED_INCOME+d.ADJUST_INCOME else 0 end )),0) as month02_account ";
		sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-03-01' and d.CONTRACT_EFFECT_DATE <'"+nowYear+"-04-01'  then d.ESTIMATED_INCOME+d.ADJUST_INCOME else 0 end )),0) as month03_account ";
		sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-04-01' and d.CONTRACT_EFFECT_DATE <'"+nowYear+"-05-01'  then d.ESTIMATED_INCOME+d.ADJUST_INCOME else 0 end )),0) as month04_account ";
		sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-05-01' and d.CONTRACT_EFFECT_DATE <'"+nowYear+"-06-01'  then d.ESTIMATED_INCOME+d.ADJUST_INCOME else 0 end )),0) as month05_account ";
		sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-06-01' and d.CONTRACT_EFFECT_DATE <'"+nowYear+"-07-01'  then d.ESTIMATED_INCOME+d.ADJUST_INCOME else 0 end )),0) as month06_account ";
		sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-07-01' and d.CONTRACT_EFFECT_DATE <'"+nowYear+"-08-01'  then d.ESTIMATED_INCOME+d.ADJUST_INCOME else 0 end )),0) as month07_account ";
		sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-08-01' and d.CONTRACT_EFFECT_DATE <'"+nowYear+"-09-01'  then d.ESTIMATED_INCOME+d.ADJUST_INCOME else 0 end )),0) as month08_account ";
		sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-09-01' and d.CONTRACT_EFFECT_DATE <'"+nowYear+"-10-01'  then d.ESTIMATED_INCOME+d.ADJUST_INCOME else 0 end )),0) as month09_account ";
		sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-10-01' and d.CONTRACT_EFFECT_DATE <'"+nowYear+"-11-01'  then d.ESTIMATED_INCOME+d.ADJUST_INCOME else 0 end )),0) as month10_account ";
		sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-11-01' and d.CONTRACT_EFFECT_DATE <'"+nowYear+"-12-01'  then d.ESTIMATED_INCOME+d.ADJUST_INCOME else 0 end )),0) as month11_account ";
		sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-12-01' and d.CONTRACT_EFFECT_DATE <'"+nextYear+"-01-01'  then d.ESTIMATED_INCOME+d.ADJUST_INCOME else 0 end )),0) as month12_account  ";
		sql+=",IFNULL(sum((case when d.estimated_id is not null and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-01-01' and d.CONTRACT_EFFECT_DATE <'"+nextYear+"-01-01'  then d.ESTIMATED_INCOME+d.ADJUST_INCOME else 0 end )),0) as month_total_account  ";
		sql+=",d.organization_id,d.organization_name from  fin_audit_charge_detail d  LEFT JOIN  fin_audit_charge_detail_happen_date dh on dh.parent_id=d.ID  LEFT JOIN fin_estimated_income e ";
		sql+="on e.ID=d.estimated_id LEFT JOIN  fin_confirm_income c on c.BILL_NO=d.PARENT_ID LEFT JOIN tm_company t on d.INCOME_OF_DEPT_ID=t.ID where 1=1 and d.CONTRACT_EFFECT_DATE >='"+nowYear+"-01-01' and d.CONTRACT_EFFECT_DATE <'"+nextYear+"-01-01' and t.ID is not null  and t.OU_TYPE='0' ";

		if(json.get("company")!=null&&!json.get("company").equals("")){
			sql+=" and d.INCOME_OF_DEPT_ID='"+json.get("company")+"' ";
		}
		sql+=" GROUP BY d.CONTRACT_CODE  HAVING d.CONTRACT_CODE is not null ;";
		sql+="";
		sql+="";
		sql+="";
		List<Map<String, Object>> mapList = dcmsDAO.query(sql, "");
		Map<String, String> headMap2 = new LinkedHashMap<String, String>();  
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		headMap2.put("PROJ_CODE", "项目编码");
		headMap2.put("PROJ_NAME", "项目名称");
		headMap2.put("CONTRACT_CODE", "合同编码");
		headMap2.put("CONTRACT_NAME", "合同名称");
		headMap2.put("PREVIOUS_YEAR_CONFIRM", "计入以前年度");
		headMap2.put("MONTH01_CONFIRM", "1月份实际收入");
		headMap2.put("MONTH01_ACCOUNT", "1月份帐显收入");
		headMap2.put("MONTH02_CONFIRM", "2月份实际收入");
		headMap2.put("MONTH02_ACCOUNT", "2月份帐显收入");
		headMap2.put("MONTH03_CONFIRM", "3月份实际收入");
		headMap2.put("MONTH03_ACCOUNT", "3月份帐显收入");
		headMap2.put("MONTH04_CONFIRM", "4月份实际收入");
		headMap2.put("MONTH04_ACCOUNT", "4月份帐显收入");
		headMap2.put("MONTH05_CONFIRM", "5月份实际收入");
		headMap2.put("MONTH05_ACCOUNT", "5月份帐显收入");
		headMap2.put("MONTH06_CONFIRM", "6月份实际收入");
		headMap2.put("MONTH06_ACCOUNT", "6月份帐显收入");
		headMap2.put("MONTH07_CONFIRM", "7月份实际收入");
		headMap2.put("MONTH07_ACCOUNT", "7月份帐显收入");
		headMap2.put("MONTH08_CONFIRM", "8月份实际收入");
		headMap2.put("MONTH08_ACCOUNT", "8月份帐显收入");
		headMap2.put("MONTH09_CONFIRM", "9月份实际收入");
		headMap2.put("MONTH09_ACCOUNT", "9月份帐显收入");
		headMap2.put("MONTH10_CONFIRM", "10月份实际收入");
		headMap2.put("MONTH10_ACCOUNT", "10月份帐显收入");
		headMap2.put("MONTH11_CONFIRM", "11月份实际收入");
		headMap2.put("MONTH11_ACCOUNT", "11月份帐显收入");
		headMap2.put("MONTH12_CONFIRM", "12月份实际收入");
		headMap2.put("MONTH12_ACCOUNT", "12月份帐显收入");
		headMap2.put("MONTH_TOTAL_CONFIRM", "实际收入合计");
		headMap2.put("MONTH_TOTAL_ACCOUNT", "帐显收入合计");
		for(Map<String, Object> map : mapList){
			Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
			dataMap.putAll(map);
			list.add(dataMap);
		}
		if(list.size()==0){
			list.add(new LinkedHashMap<String, Object>());
		}
		ExcelTools.downloadExcelFile("计收财务报表", headMap2, list,response);
		return null;
	}
}
