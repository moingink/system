package com.yonyou.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
//import com.sun.tools.classfile.StackMapTable_attribute.same_locals_1_stack_item_frame;
import com.yonyou.business.RmDictReferenceUtil;
import com.yonyou.business.button.util.ButForInsert;
import com.yonyou.util.BussnissException;
import com.yonyou.util.JsonUtils;
import com.yonyou.util.SerialNumberUtil;
import com.yonyou.util.SerialNumberUtil.SerialType;
import com.yonyou.util.busflow.util.IBusFlowOperationType;
import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.mail.MailHelper;
import com.yonyou.util.sql.SQLUtil.WhereEnum;
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.util.sql.SqlWhereEntity;

import net.sf.json.JSONObject;

@RestController
@RequestMapping(value = "/project")
public class ProjectController extends BaseController {
	
	@Autowired
	protected IBaseDao dcmsDAO;
	
	//查询产品类型  和类型下的产品
	@RequestMapping(value = "getBusinessType", method = RequestMethod.GET)
	public String getBusinessType(HttpServletRequest request, HttpServletResponse response) throws IOException  {
		//String  proj_source_id = request.getParameter("proj_source_id");
		String sqlString = "SELECT  * from  buss_type";
		List<Map<String, Object>> list = BaseDao.getBaseDao().query(sqlString, "");
		//JSONArray array = new JSONArray();
		com.alibaba.fastjson.JSONObject  jsonObjectALL  = new com.alibaba.fastjson.JSONObject();
		//com.alibaba.fastjson.JSONObject  jsonObject  = new com.alibaba.fastjson.JSONObject();
		com.alibaba.fastjson.JSONObject  jsonObj  = new com.alibaba.fastjson.JSONObject();
		//com.alibaba.fastjson.JSONObject  jsonObjectProductType  = new com.alibaba.fastjson.JSONObject();
		for(Map<String, Object> dataMap:list){
			String idString = String.valueOf(dataMap.get("ID"));
			String typeName = String.valueOf(dataMap.get("TYPE_NAME"));
			jsonObj.put(idString, typeName);
			
			//查询产品类型  
			String sqlStringProductType = "SELECT  * from product_type_message  where pro_type_id = '"+idString+"'";
			List<Map<String, Object>> listProductType = BaseDao.getBaseDao().query(sqlStringProductType, "");
			com.alibaba.fastjson.JSONObject  jsonObjectProduct  = new com.alibaba.fastjson.JSONObject();
			for(Map<String, Object> prodataMap:listProductType){
				String ptid = String.valueOf(prodataMap.get("ID"));
				String ptypeName = String.valueOf(prodataMap.get("PRO_TYPE_NAME"));
				jsonObjectProduct.put(ptid, ptypeName);
				//调用内部方法获取每个产品下类型
				jsonObjectALL.put(ptid, putProduct(ptid));
			}
			jsonObjectALL.put(idString, jsonObjectProduct);
		}
		jsonObjectALL.put("86", jsonObj);
		//jsonObject.put("86", jsonObj);
		//存放业务类型下的产品类型
		
		return JSON.toJSONString(jsonObjectALL);
	}
	//
	public com.alibaba.fastjson.JSONObject putProduct(String id){
		String sqlStringProduct = "SELECT  * from product_info where  pro_type_message_id= '"+id+"'";
		List<Map<String, Object>> listProduct = BaseDao.getBaseDao().query(sqlStringProduct, "");
		com.alibaba.fastjson.JSONObject productJsonObject = new com.alibaba.fastjson.JSONObject();
		for(Map<String, Object> prodataMap:listProduct){
			String pid = String.valueOf(prodataMap.get("ID"));
			String pName = String.valueOf(prodataMap.get("PRO_NAME"));
			productJsonObject.put(pid, pName);
		}
		//json.put(id, productJsonObject);
		return productJsonObject;
	}
	
	//保存建议书产品
	@RequestMapping(value = "saveProduct", method = RequestMethod.POST)
	public String saveProduct(HttpServletRequest request, HttpServletResponse response) throws IOException, BussnissException  {
		String  json = request.getParameter("json");
		String  id = request.getParameter("id");
		JSONArray jsonArray = JSONArray.parseArray(json);
		List<Map<String, String>> productData = new ArrayList<Map<String, String>>();
		for(int i=0; i<jsonArray.size();i++){
			Map map = JSON.parseObject(jsonArray.getJSONObject(i).toJSONString());
			productData.add(map);
		}
		SqlWhereEntity whereEntity =new SqlWhereEntity();
		whereEntity.putWhere("PROJ_SOURCE_ID", id, WhereEnum.EQUAL_INT);
		dcmsDAO.delete("PROJ_PROPOSAL_PRODUCT", whereEntity);
		dcmsDAO.insertByTransfrom("PROJ_PROPOSAL_PRODUCT", productData);
		com.alibaba.fastjson.JSONObject jsonObj = new com.alibaba.fastjson.JSONObject();
		jsonObj.put("success", true);
		return jsonObj.toJSONString();
	}
	@RequestMapping(value = "saveDuty", method = RequestMethod.POST)
	public String saveDuty(HttpServletRequest request, HttpServletResponse response) throws IOException, BussnissException  {
		String  id = request.getParameter("id");
		String duty_id = request.getParameter("duty_id");
		dcmsDAO.updateBySql("update proj_requirement set  duty_id = '"+duty_id+"' where id = '"+id+"' ");
		return JSON.toJSONString("success");
	}
	//保存能力项目指定项目经理
	@RequestMapping(value = "saveProjectManager", method = RequestMethod.GET)
	public String saveProjectManager(HttpServletRequest request, HttpServletResponse response) throws IOException, BussnissException  {
		String  id = request.getParameter("id");
		String  manager_name = request.getParameter("manager_name");
		String  manager_id = request.getParameter("manager_id");
		dcmsDAO.updateBySql("update proj_requirement set  proj_manager = '"+manager_name+"', proj_manager_id='"+manager_id+"' where id = '"+id+"' ");
		this.notityBusFlow("PROJ_REQUIREMENT_ZDJL", id, null, request);
		return JSON.toJSONString("success");
	}
	//更新立项类型
	@RequestMapping(value = "updateProjectOperationType", method = RequestMethod.GET)
	public String updateProjectOperationType(HttpServletRequest request, HttpServletResponse response) throws IOException, BussnissException  {
		String  id = request.getParameter("id");
		String  operation_type = request.getParameter("operation_type");
		dcmsDAO.updateBySql("update proj_released set  operation_type = '"+operation_type+"'  where id = '"+id+"' ");
		return JSON.toJSONString("success");
	}
	
	@RequestMapping(value = "checkUploadFile", method = RequestMethod.GET)
	public String checkUploadFile(HttpServletRequest request, HttpServletResponse response) throws IOException, BussnissException  {
		String  doc = request.getParameter("doc");
		String sqlString = "SELECT  * from doc_document where  BATCH_NO ='"+doc+"'";
		List<Map<String, Object>> list = BaseDao.getBaseDao().query(sqlString, "");
		return JSON.toJSONString(list);
	}
	@RequestMapping(value = "checkExitCodingRule", method = RequestMethod.GET)
	public String checkExitCodingRule(HttpServletRequest request, HttpServletResponse response) throws IOException, BussnissException  {
		String  code = request.getParameter("total_code");
		String sqlString = "SELECT  * from proj_coding_rule  where  TOTAL_CODE ='"+code+"'";
		List<Map<String, Object>> list = BaseDao.getBaseDao().query(sqlString, "");
		JSONArray array =new JSONArray();
		for(Map<String, Object> dataMap:list){
			String idString = String.valueOf(dataMap.get("ID"));
			dataMap.remove("ID");
			dataMap.put("ID", idString);
			array.add(dataMap);
		}
		return JSON.toJSONString(array);
	}
	//	获取项目code
	@RequestMapping(value = "getProjectCode", method = RequestMethod.GET)
	public String getProjectCode(HttpServletRequest request, HttpServletResponse response) throws IOException, BussnissException  {
		Calendar cal = Calendar.getInstance();
		String codeRule = request.getParameter("codeRule");
		String yearMonth = cal.get(Calendar.YEAR)+String.format("%02d", cal.get(Calendar.MONTH)+1);;
		//项目编号
		String serialCode = SerialNumberUtil.getSerialCode(SerialType.PROJECT_CODE, "" + yearMonth+codeRule);
		return JSON.toJSONString(serialCode);
	}
	
	@RequestMapping(value = "getProductList", method = RequestMethod.GET)
	public String getProductList(HttpServletRequest request, HttpServletResponse response) throws IOException, BussnissException  {
		String  id = request.getParameter("id");
		String sqlString = "SELECT  * from proj_proposal_product where  proj_source_id ='"+id+"'";
		List<Map<String, Object>> list = BaseDao.getBaseDao().query(sqlString, "");
		JSONArray array =new JSONArray();
		for(Map<String, Object> dataMap:list){
			String idString = String.valueOf(dataMap.get("BUSINESS_TYPE_ID"));
			dataMap.remove("BUSINESS_TYPE_ID");
			dataMap.put("BUSINESS_TYPE_ID", idString);
			
			String pro_idString = String.valueOf(dataMap.get("PRODUCT_TYPE_ID"));
			dataMap.remove("PRODUCT_TYPE_ID");
			dataMap.put("PRODUCT_TYPE_ID", pro_idString);
			
			String product_id = String.valueOf(dataMap.get("PRODUCT_ID"));
			dataMap.remove("PRODUCT_ID");
			dataMap.put("PRODUCT_ID", product_id);
			
			String duty_dept_id = String.valueOf(dataMap.get("DUTY_DEPT_ID"));
			dataMap.remove("DUTY_DEPT_ID");
			dataMap.put("DUTY_DEPT_ID", duty_dept_id);
			
			String danjuid = String.valueOf(dataMap.get("ID"));
			dataMap.remove("ID");
			dataMap.put("ID", danjuid);
			array.add(dataMap);
		}
		
		return JSON.toJSONString(array);
	}	
	@RequestMapping(value = "getProductListHistory", method = RequestMethod.GET)
	public String getProductListHistory(HttpServletRequest request, HttpServletResponse response) throws IOException, BussnissException  {
		String  vcode = request.getParameter("vcode");
		String sqlString = "SELECT  * from proj_proposal_product_history where  v_code ='"+vcode+"'";
		List<Map<String, Object>> list = BaseDao.getBaseDao().query(sqlString, "");
		JSONArray array =new JSONArray();
		for(Map<String, Object> dataMap:list){
			String idString = String.valueOf(dataMap.get("BUSINESS_TYPE_ID"));
			dataMap.remove("BUSINESS_TYPE_ID");
			dataMap.put("BUSINESS_TYPE_ID", idString);
			
			String pro_idString = String.valueOf(dataMap.get("PRODUCT_TYPE_ID"));
			dataMap.remove("PRODUCT_TYPE_ID");
			dataMap.put("PRODUCT_TYPE_ID", pro_idString);
			
			String product_id = String.valueOf(dataMap.get("PRODUCT_ID"));
			dataMap.remove("PRODUCT_ID");
			dataMap.put("PRODUCT_ID", product_id);
			
			String duty_dept_id = String.valueOf(dataMap.get("DUTY_DEPT_ID"));
			dataMap.remove("DUTY_DEPT_ID");
			dataMap.put("DUTY_DEPT_ID", duty_dept_id);
			
			String danjuid = String.valueOf(dataMap.get("ID"));
			dataMap.remove("ID");
			dataMap.put("ID", danjuid);
			array.add(dataMap);
		}
		
		return JSON.toJSONString(array);
	}	
	//获取业务类型产品
	@RequestMapping(value = "getProductDept", method = RequestMethod.GET)
	public String getProductDept(HttpServletRequest request, HttpServletResponse response) throws IOException  {
		String  pid = request.getParameter("pid");
		String sqlString = "SELECT  * from product_info where  id ='"+pid+"'";
		List<Map<String, Object>> list = BaseDao.getBaseDao().query(sqlString, "");
		com.alibaba.fastjson.JSONObject productJsonObject = new com.alibaba.fastjson.JSONObject();
		for(Map<String, Object> dataMap:list){
			String code = String.valueOf(dataMap.get("ORG_DEPT_CODE"));
			String name = String.valueOf(dataMap.get("ORG_DEPT_NAME"));
			productJsonObject.put("CODE", code);
			productJsonObject.put("NAME", name);
		}
		return JSON.toJSONString(productJsonObject);
	}	
	
	//查询组织架构
	@RequestMapping(value = "getOrag", method = RequestMethod.GET)
	public String getOrag(HttpServletRequest request, HttpServletResponse response) throws IOException  {
		String  proj_source_id = request.getParameter("proj_source_id");
		String sqlString = "SELECT CONCAT(ID) as ID,FUNCTIONAL_GROUP,PERSON_TYPE,`NAME`,ROLE,EMAIL,cust_name,cust_role,cust_email,TEL ,cust_tel,CONCAT(user_id) as user_id  from proj_organization "+
		"where PROJ_SOURCE_ID = '"+proj_source_id+"' and dr=0 and PROJ_TYPE =0";
		List<Map<String, Object>> list = BaseDao.getBaseDao().query(sqlString, "");
		return JSON.toJSONString(list);
	}
	
	//通用查询
	@RequestMapping(value = "getProposalChild", method = RequestMethod.GET)
	public String getProposalChild(HttpServletRequest request, HttpServletResponse response) throws IOException  {
		String  proj_source_id = request.getParameter("proj_source_id");
		String  code = request.getParameter("code");
		String  table = request.getParameter("table");
		String sqlString = "SELECT * from "+table + " where proj_source_id='"+proj_source_id+"' and v_code='"+code+"'";
		List<Map<String, Object>> list = BaseDao.getBaseDao().query(sqlString, "");
		return JSON.toJSONString(list);
	}
	//查询项目预算
	@RequestMapping(value = "budget", method = RequestMethod.GET)
	public String getBudget(HttpServletRequest request, HttpServletResponse response) throws IOException  {
		String  proj_source_id = request.getParameter("proj_source_id");
		String sqlString = " SELECT * from proj_budget  "+
		"where PROJ_SOURCE_ID = '"+proj_source_id+"' and dr=0 and PROJ_TYPE =0 order by cost_class,cost_type ";
		List<Map<String, Object>> list = BaseDao.getBaseDao().query(sqlString, "");
		JSONArray array =new JSONArray();
		for(Map<String, Object> dataMap:list){
			String idString = String.valueOf(dataMap.get("ID"));
			dataMap.remove("ID");
			dataMap.put("ID", idString);
			array.add(dataMap);
		}
		return JSON.toJSONString(array);
	}
	@RequestMapping(value = "proposal_history", method = RequestMethod.GET)
	public String getProposal_history(HttpServletRequest request, HttpServletResponse response) throws IOException  {
		String  id = request.getParameter("id");
		String sqlString = "SELECT ed.edition ,pph.PROJ_SOURCE_ID   from proj_edition   ed INNER JOIN proj_proposal_history pph on ed.p_id = pph.id where p_id ='"+id+"'";
		List<Map<String, Object>> list = BaseDao.getBaseDao().query(sqlString, "");
		JSONArray array =new JSONArray();
		for(Map<String, Object> dataMap:list){
			String idString = String.valueOf(dataMap.get("PROJ_SOURCE_ID"));
			dataMap.remove("PROJ_SOURCE_ID");
			dataMap.put("PROJ_SOURCE_ID", idString);
			array.add(dataMap);
		}
		return JSON.toJSONString(array);
	}
	
	//查询wbs 列表
	@RequestMapping(value = "wbs", method = RequestMethod.GET)
	public String getWbsInfo(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException  {
		String  proj_source_id = request.getParameter("proj_source_id");
		String sqlString = " SELECT * from proj_wbsinfo  "+
		"where PROJ_SOURCE_ID = '"+proj_source_id+"' and dr=0 and PROJ_TYPE =0";
		List<Map<String, Object>> list = BaseDao.getBaseDao().query(sqlString, "");
		JSONArray array =new JSONArray();
		SimpleDateFormat  sf = new SimpleDateFormat("yyyy-MM-dd");
		for(Map<String, Object> dataMap:list){
			String idString = String.valueOf(dataMap.get("ID"));
			//取出日期  
			
			String startDate  =  String.valueOf(dataMap.get("WBS_START_DATE")).substring(0,10);
			String endDate  =   String.valueOf(dataMap.get("WBS_EXPECTED_END_DATE")).substring(0,10);;
			String completionDate=null;
			Date start = sf.parse(startDate);
			Date end = sf.parse(endDate);
			if(dataMap.get("WBS_COMPLETION_TIME")!= null&&!dataMap.get("WBS_COMPLETION_TIME").equals("null")){
				completionDate = String.valueOf(dataMap.get("WBS_COMPLETION_TIME")).substring(0,10);
				dataMap.remove("WBS_COMPLETION_TIME");
				dataMap.put("WBS_COMPLETION_TIME",completionDate);
			}
			dataMap.remove("ID");
			dataMap.remove("WBS_START_DATE");
			dataMap.remove("WBS_EXPECTED_END_DATE");
			dataMap.put("ID", idString);
			dataMap.put("WBS_START_DATE", startDate);
			dataMap.put("WBS_EXPECTED_END_DATE",endDate);
			if(null == dataMap.get("WBS_WORKING_HOURS")){
				double count = calLeaveDays(start,end);
				dataMap.put("WBS_WORKING_HOURS",count);
			}
			array.add(dataMap);
		}
		//计算工作时间日
		return JSON.toJSONString(array);
	}
	//查询问题风险进度跟踪
	@RequestMapping(value = "risk", method = RequestMethod.GET)
	public String getRiskManageMent(HttpServletRequest request, HttpServletResponse response) throws IOException  {
		String  proj_source_id = request.getParameter("proj_source_id");
		String sqlString = " SELECT * from proj_risk_management  "+
		"where PROJ_SOURCE_ID = '"+proj_source_id+"' and dr=0 and PROJ_TYPE =0";
		List<Map<String, Object>> list = BaseDao.getBaseDao().query(sqlString, "");
		JSONArray array =new JSONArray();
		for(Map<String, Object> dataMap:list){
			String idString = String.valueOf(dataMap.get("ID"));
			dataMap.remove("ID");
			dataMap.put("ID", idString);
			array.add(dataMap);
		}
		return JSON.toJSONString(array);
	}
	//查询进度计划
	@RequestMapping(value = "progress_plan", method = RequestMethod.GET)
	public String getProgressPlan(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException  {
		String  proj_source_id = request.getParameter("proj_source_id");
		String sqlString = " SELECT * from proj_progress_phase  "+"where PROJ_SOURCE_ID = '"+proj_source_id+"' and dr=0 and PROJ_TYPE =0";
		
		List<Map<String, Object>> list = BaseDao.getBaseDao().query(sqlString, "");
		JSONArray array =new JSONArray();
		SimpleDateFormat  sf = new SimpleDateFormat("yyyy-MM-dd");
		for(Map<String, Object> dataMap:list){
			String idString = String.valueOf(dataMap.get("ID"));
			String startDate  =  String.valueOf(dataMap.get("START_DATE")).substring(0,10);
			String endDate  =   String.valueOf(dataMap.get("EXPECTED_END_DATE")).substring(0,10);
			Date start = sf.parse(startDate);
			Date end = sf.parse(endDate);
			dataMap.remove("ID");
			dataMap.remove("START_DATE");
			dataMap.remove("EXPECTED_END_DATE");
			dataMap.put("ID", idString);
			dataMap.put("START_DATE", startDate);
			dataMap.put("EXPECTED_END_DATE",endDate);
			double count = calLeaveDays(start,end);
			dataMap.put("WBS_WORKING_HOURS",count);
			array.add(dataMap);
		}
		return JSON.toJSONString(array);
	}
	//查询进度计划历史
	@RequestMapping(value = "progress_plan_history", method = RequestMethod.GET)
	public String getProgressPlanHistory(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException  {
		String  proj_source_id = request.getParameter("proj_source_id");
		String  code = request.getParameter("code");
		String  table = request.getParameter("table");
		String sqlString = "SELECT * from "+table + " where proj_source_id='"+proj_source_id+"' and v_code='"+code+"'";;
		
		List<Map<String, Object>> list = BaseDao.getBaseDao().query(sqlString, "");
		JSONArray array =new JSONArray();
		SimpleDateFormat  sf = new SimpleDateFormat("yyyy-MM-dd");
		for(Map<String, Object> dataMap:list){
			String idString = String.valueOf(dataMap.get("ID"));
			String startDate  =  String.valueOf(dataMap.get("START_DATE")).substring(0,10);
			String endDate  =   String.valueOf(dataMap.get("EXPECTED_END_DATE")).substring(0,10);
			Date start = sf.parse(startDate);
			Date end = sf.parse(endDate);
			dataMap.remove("ID");
			dataMap.remove("START_DATE");
			dataMap.remove("EXPECTED_END_DATE");
			dataMap.put("ID", idString);
			dataMap.put("START_DATE", startDate);
			dataMap.put("EXPECTED_END_DATE",endDate);
			double count = calLeaveDays(start,end);
			dataMap.put("WBS_WORKING_HOURS",count);
			array.add(dataMap);
		}
		return JSON.toJSONString(array);
	}
	@RequestMapping(value = "projectinfo", method = RequestMethod.GET)
	public String getProjectInfo(HttpServletRequest request, HttpServletResponse response) throws IOException  {
		String  proj_source_id = request.getParameter("proj_source_id");
		String sqlString = " SELECT rel.PROJ_CODE ,rel.proj_name , ass.TOTAL_INCOME ,ass.RATE_OF_RETURN  ,rel.OPPORTUNITY_CODE   from proj_released  rel"+
		" INNER JOIN  pre_assessment ass  on  rel.OPPORTUNITY_CODE = ass.BUSINESS_NUMBER "+
		" where rel.id = '"+proj_source_id+"'";
		
		
		
		List<Map<String, Object>> list = BaseDao.getBaseDao().query(sqlString, "");
		return JSON.toJSONString(list);
	}
	//新建项目概况  从前评估读取部分字段到项目概况table,有则显示 无则不做修改
	@RequestMapping(value = "getamount", method = RequestMethod.GET)
	public String getAmount(HttpServletRequest request, HttpServletResponse response) throws IOException  {
		String  proj_source_id = request.getParameter("proj_source_id");
		String sqlString = "select pro.PROJ_NAME, pro.project_cost ,pro.set_build_investment_cost_t ,pro.set_build_investment_cost_f  from proj_requirement  req inner join  proj_proposal  pro  on req.id = pro.proj_source_id   where req.id ='"+proj_source_id+"'";
		List<Map<String, Object>> list = BaseDao.getBaseDao().query(sqlString, "");
		return JSON.toJSONString(list);
	}
	
	//获取需求指定的项目经理id和name  和建议书里的责任人id和name 回显到能力立项下达表单
	@RequestMapping(value = "getManagerInfo", method = RequestMethod.GET)
	public String getManagerInfo(HttpServletRequest request, HttpServletResponse response) throws IOException  {
		String  proj_source_id = request.getParameter("proj_source_id");
		String sqlString = " SELECT req.proj_manager_id,req.proj_manager , pro.duty_id,pro.duty_name,req.requirement_affix as req_affix,pro.AFFIX as pro_affix  from proj_requirement  req INNER JOIN  proj_proposal  pro  on req.id = pro.PROJ_SOURCE_ID "+
		" where req.REQUIREMENT_CODE ='"+proj_source_id+"'";
		List<Map<String, Object>> list = BaseDao.getBaseDao().query(sqlString, "");
		JSONArray array =new JSONArray();
		for(Map<String, Object> dataMap:list){
			String managerIdString = String.valueOf(dataMap.get("PROJ_MANAGER_ID"));
			String dutyIdString = String.valueOf(dataMap.get("DUTY_ID"));
			dataMap.remove("PROJ_MANAGER_ID");
			dataMap.remove("DUTY_ID");
			dataMap.put("PROJ_MANAGER_ID", managerIdString);
			dataMap.put("DUTY_ID", dutyIdString);
			array.add(dataMap);
		}
		return JSON.toJSONString(array);
	}
	
	
	//获取需求指定的项目经理id和name  和建议书里的责任人id和name 回显到能力立项下达表单
		@RequestMapping(value = "getManagerInfoByHis", method = RequestMethod.GET)
		public String getManagerInfoByHis(HttpServletRequest request, HttpServletResponse response) throws IOException  {
			String  proj_source_id = request.getParameter("proj_source_id");
			String sqlString = " SELECT req.proj_manager_id,req.proj_manager , pro.duty_id,pro.duty_name,req.requirement_affix as req_affix,pro.AFFIX as pro_affix  from proj_requirement  req INNER JOIN  proj_proposal  pro  on req.id = pro.PROJ_SOURCE_ID "+
			" where pro.proj_code in (select his.PROJ_CODE from proj_released_history his where his.id ="+proj_source_id+")";
			List<Map<String, Object>> list = BaseDao.getBaseDao().query(sqlString, "");
			JSONArray array =new JSONArray();
			for(Map<String, Object> dataMap:list){
				String managerIdString = String.valueOf(dataMap.get("PROJ_MANAGER_ID"));
				String dutyIdString = String.valueOf(dataMap.get("DUTY_ID"));
				dataMap.remove("PROJ_MANAGER_ID");
				dataMap.remove("DUTY_ID");
				dataMap.put("PROJ_MANAGER_ID", managerIdString);
				dataMap.put("DUTY_ID", dutyIdString);
				array.add(dataMap);
			}
			return JSON.toJSONString(array);
		}
	
	//获取单个角色下所有用户
		@RequestMapping(value = "roleUserList", method = RequestMethod.GET)
		public String getRoleUserList(HttpServletRequest request, HttpServletResponse response) throws IOException  {
			String roleIdString = request.getParameter("roleId");
			String roleName = request.getParameter("roleName");
			String sqlString = "select rmuser.ID,rmuser.`NAME` from rm_party_role rmrole join rm_user rmuser  on rmrole.OWNER_PARTY_ID = rmuser.ID where rmuser.DR =0 and   rmrole.ROLE_ID = '"+roleIdString+"'";
			List<Map<String, Object>> list = BaseDao.getBaseDao().query(sqlString, "");
			//拼装成tree 形式返回到前台直接展示 角色用户列表
			JSONArray jsonArray = new JSONArray();
			com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
			json.put("text", roleName);
			json.put("href", "#"+roleIdString);
			JSONArray childArray = new JSONArray();
			if(list!=null &&list.size()>0){
				for(Map<String, Object> dataMap:list){
					//获取用户id 和名称 
					com.alibaba.fastjson.JSONObject userJson = new com.alibaba.fastjson.JSONObject();
					userJson.put("text", String.valueOf(dataMap.get("NAME")));
					userJson.put("href", "#"+String.valueOf(dataMap.get("ID")));
					childArray.add(userJson);
				}
				json.put("nodes", childArray);
			}
			jsonArray.add(json);
			return JSON.toJSONString(jsonArray);
		}
	//查询角色是否设置了数据权限
	@RequestMapping(value = "roleDataAuth", method = RequestMethod.GET)
	public String getRoleDataAuth(HttpServletRequest request, HttpServletResponse response) throws IOException  {
		String  roleIdString = request.getParameter("roleId");
		String sqlString = " select * from rm_role_auth where dr=0 and rm_role_id =' "+roleIdString+"'";
		List<Map<String, Object>> list = BaseDao.getBaseDao().query(sqlString, "");
		return JSON.toJSONString(list.size());
	}
	//查询角色或用户数据权限json返回
		@RequestMapping(value = "dataAuth", method = RequestMethod.GET)
		public String getDataAuth(HttpServletRequest request, HttpServletResponse response) throws IOException  {
			String  roleIdString = request.getParameter("roleId");
			String  userIdString = request.getParameter("userId");
			String sqlString = "select rmauth.id AS ID,rmauth.rm_role_id as RM_ROLE_ID,rmauth.golbal AS GOLBAL,rmauth.personal AS PERSONAL,rmauth.company AS COMPANY,rmauth.personal_data AS PERSONAL_DATA,rmauth.company_data AS COMPANY_DATA,rmuser.USER_PERSONAL_DATA ,rmuser.USER_COMPANY_DATA ,rmuser.rm_user_id,rmuser.USER_GOLBAL ,rmuser.user_personal ,rmuser.user_company from rm_role_auth   rmauth "+ 
					" left JOin (SELECT personal_data AS USER_PERSONAL_DATA,company_data AS USER_COMPANY_DATA,rm_role_id,rm_user_id,user_golbal AS USER_GOLBAL,user_company ,user_personal   from rm_role_auth_user where rm_user_id = '"+userIdString+"' ) rmuser on rmauth.rm_role_id = rmuser.rm_role_id"+
					" where rmauth.rm_role_id = '"+roleIdString+"'";
			List<Map<String, Object>> list = BaseDao.getBaseDao().query(sqlString, "");
			Map<String, Object> map =  null;
			if(null!= list && list.size()>0){
				map = new HashMap<String, Object>();
				map = list.get(0);
			}
			//com.alibaba.fastjson.JSONObject jsonObject =  new com.alibaba.fastjson.JSONObject() ;
			//jsonObject.put("dataAuth", map);
			return JSONUtils.toJSONString(map);
		}
	//获取所有公司信息
	@RequestMapping(value = "companyList", method = RequestMethod.GET)
	public String getCompanyListInfo(HttpServletRequest request, HttpServletResponse response) throws IOException  {
		String sqlString = " select ID,name from tm_company where dr=0 ";
		List<Map<String, Object>> list = BaseDao.getBaseDao().query(sqlString, "");
		JSONArray array =new JSONArray();
		for(Map<String, Object> dataMap:list){
			String conpanyId = String.valueOf(dataMap.get("ID"));
			dataMap.remove("ID");
			dataMap.put("ID", conpanyId);
			array.add(dataMap);
		}
		return JSON.toJSONString(array);
	}	
	
	@SuppressWarnings("unchecked")
	public static String updateProgress(JSONObject jsonobj){
		//传递json数据 更新wbs信息表
		SqlWhereEntity whereEntity =new SqlWhereEntity();
		try {
			String idString = jsonobj.getString("ID");
			String milestoneString = jsonobj.getString("MILESTONE");//里程碑
			String startDateString =jsonobj.getString("START_DATE");//开始时间
			String endDateString =jsonobj.getString("EXPECTED_END_DATE");//计划完成时间
			String taskString =jsonobj.getString("PHASE_TASK");//阶段任务
			String dutyNameString =jsonobj.getString("PRINCIPAL_NAME");//负责人
			
			JSONObject wbsJsonObject = new JSONObject();
			wbsJsonObject.put("WBS_NAME", milestoneString);
			wbsJsonObject.put("WBS_START_DATE", startDateString);
			wbsJsonObject.put("WBS_EXPECTED_END_DATE", endDateString);
			try {
				Double calLeaveDays = calLeaveDays(new SimpleDateFormat("yyyy-MM-dd").parse(startDateString), new SimpleDateFormat("yyyy-MM-dd").parse(endDateString));
				wbsJsonObject.put("WBS_WORKING_HOURS", String.valueOf(calLeaveDays));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			wbsJsonObject.put("TASK_REMARK", taskString);
			wbsJsonObject.put("WBS_THOSE_RESPONSIBLE", dutyNameString);
			wbsJsonObject.put("PROJ_TYPE", "0");
			wbsJsonObject.put("DR", "0");
			if(StringUtils.isNoneBlank(idString) && !idString.equals("undefined")){
				whereEntity.putWhere("PROJ_PHASE_ID", idString, WhereEnum.EQUAL_INT);
				BaseDao.getBaseDao().updateByTransfrom("PROJ_WBSINFO", wbsJsonObject, whereEntity);
			}else{
				wbsJsonObject.put("PROJ_PHASE_ID", jsonobj.getString("insertId"));
				wbsJsonObject.put("PROJ_SOURCE_ID", jsonobj.getString("PROJ_SOURCE_ID"));
				BaseDao.getBaseDao().insertByTransfrom("PROJ_WBSINFO", wbsJsonObject);
			}
			whereEntity.clear();
		} catch (BussnissException e) {
			e.printStackTrace();
		}
		return "{\"message\":\"success\"}";
	}
	
	//获取建议书审批状态 返回到每个子页面来控制按钮
	@RequestMapping(value = "proposalStatue", method = RequestMethod.GET)
	public String getProposalStatue(HttpServletRequest request, HttpServletResponse response) throws IOException  {
		String  proj_source_id = request.getParameter("proj_source_id");
		String sqlString = " SELECT  propo.BILL_STATUS ,propo.PROJECT_COST from  proj_proposal propo "+
		//"INNER JOIN proj_released  rele  on rele.ID = propo.PROJ_SOURCE_ID "+
		"where propo.PROJ_SOURCE_ID = '"+proj_source_id+"'";
		List<Map<String, Object>> list = BaseDao.getBaseDao().query(sqlString, "");
		return JSON.toJSONString(list);
	}
	
	
	@RequestMapping(value = "updatereQuirement", method = RequestMethod.GET)
	public String update(HttpServletRequest request, HttpServletResponse response) throws IOException  {
		String  bind_buss_no = request.getParameter("bind_buss_no");
		String  projectCode = request.getParameter("proj_code");
		String  projectName = request.getParameter("proj_name");
		String  projectManager = request.getParameter("proj_manager");
		String sqlString = " update proj_requirement  set  proj_code = '"+projectCode+"', PROJ_NAME ='"+projectName+"' , proj_manager='"+projectManager+"'"+
		"where REQUIREMENT_CODE = '"+bind_buss_no+"'";
		int count = BaseDao.getBaseDao().update(sqlString);
		return JSON.toJSONString(count);
	}
	
	/**
	 * 查询变更表中是否存在json 不存在--查询本表   存在--返回json
	* @param request
	* @param response
	* @throws BussnissException
	* @throws IOException
	 */
	@RequestMapping(params = "cmd=find_selectTab", method = RequestMethod.GET)
	public void selectTab(HttpServletRequest request,HttpServletResponse response) throws BussnissException, IOException{
		PrintWriter out = response.getWriter();
		List<Map<String, Object>> ListMap = new ArrayList<Map<String, Object>>();
		String sql = "";
		String json = "";
		String modifyId = request.getParameter("modifyId");
		String modifyFieldName = request.getParameter("modifyFieldName");
		sql = "SELECT * FROM project_build_modify WHERE ID = '"+modifyId+"'";
		ListMap = BaseDao.getBaseDao().query(sql,"");
		if(ListMap.size()>0){
			if(StringUtils.isNotBlank(ListMap.get(0).get(modifyFieldName)!=null?ListMap.get(0).get(modifyFieldName).toString():null)){
				json = ListMap.get(0).get(modifyFieldName).toString().replace("'", "\"");
				Object jsonObj = com.alibaba.fastjson.JSONObject.parse(json);
				if(jsonObj instanceof com.alibaba.fastjson.JSONObject){
					json = ((com.alibaba.fastjson.JSONObject) jsonObj).toJSONString();
				}else if(jsonObj instanceof JSONArray){
					json = ((JSONArray) jsonObj).toJSONString();
				}
			}
		}
		out.print(json);
	}
	
	/**
	 * 获取流水号
	* @param request
	* @param response
	* @throws IOException
	 */
	@RequestMapping(params = "getSerialCode", method = RequestMethod.GET)
	public void aaa(HttpServletRequest request,HttpServletResponse response) throws IOException{
		PrintWriter out = response.getWriter();
		String modifyNumber = request.getParameter("modifyNumber");
		String serialCode = SerialNumberUtil.getSerialCode(SerialType.THREE_BIT_CODE, modifyNumber);
		System.out.println("变更编号加流水号:"+serialCode);
		out.print(serialCode);
	}
	
	/**
	 * 查询项目变更执行表
	* @param request
	* @param response
	* @throws BussnissException
	* @throws IOException
	 */
	@RequestMapping(params = "cmd=find_modifyExecute", method = RequestMethod.GET)
	public void modifyExecute(HttpServletRequest request,HttpServletResponse response) throws BussnissException, IOException{
		PrintWriter out = response.getWriter();
		List<Map<String, Object>> ListMap = new ArrayList<Map<String, Object>>();
		String dataSourceCode = request.getParameter("dataSourceCode");
		String ParentPKField = request.getParameter("ParentPKField");
		String ParentPKValue = request.getParameter("ParentPKValue");
		String sql = "SELECT * FROM "+dataSourceCode+" WHERE "+ParentPKField+" = '"+ParentPKValue+"'";
		ListMap = BaseDao.getBaseDao().query(sql,"");
		String json = JsonUtils.object2json(ListMap.size()>0?ListMap:null);
		out.print(json);
	}
	
	/**
	 * 更新建设项目变更申请表数据
	* @param request
	* @param response
	* @throws BussnissException
	* @throws IOException
	 */
	@RequestMapping(params = "cmd=updateBuildModify", method = RequestMethod.POST)
	public void updateBuildModify(HttpServletRequest request,HttpServletResponse response) throws BussnissException, IOException{
		PrintWriter out = response.getWriter();
		String updateFieldName = request.getParameter("updateFieldName");
		String updateFieldValue = request.getParameter("updateFieldValue");
		String zjID = request.getParameter("zjID");
		
		String setSql = "";
		String[] split = updateFieldName.split(":");
		String[] split2 = updateFieldValue.split(":");
		for(int i=0;i<split.length;i++){
			String string = split2[i].replaceAll("#", "");
			setSql += split[i]+"='"+string+"',";
		}
		String sql = "UPDATE project_build_modify SET "+setSql.substring(0,setSql.length()-1)+" WHERE MODIFY_STATE = 1 AND ID = "+zjID;
		BaseDao.getBaseDao().update(sql.toLowerCase());
		out.print("保存成功");
	}
	
	/**
	 * 更新字段为空
	* @param id
	* @param column
	* @param dataSourceCode
	 */
	@SuppressWarnings("unused")
	private void updateColumn(String id,String column,String dataSourceCode){
		Map<String,Object> parameMap = new HashMap<String, Object>();
		parameMap.put("ID",id);
		parameMap.put(column,"");
		try {
			BaseDao.getBaseDao().update(dataSourceCode, parameMap,new SqlWhereEntity());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 将项目建设变更申请表中字段存储的临时JSON回填到各个表中
	* @param request
	* @param response
	 * @throws BussnissException 
	 * @throws IOException 
	 */
	@RequestMapping(params = "cmd=updateModifyContent", method = RequestMethod.POST)
	public void updateModifyContent(HttpServletRequest request,HttpServletResponse response) throws BussnissException, IOException{
		String dataSourceCode = request.getParameter("dataSourceCode");
		String id = request.getParameter("ID");
		//new ProjectModifyUtil().modify(dataSourceCode, id, dcmsDAO);
	}
	
	/**
	 * 项目建设变更执行删除
	* @param request
	* @param response
	* @throws IOException
	 */
	@RequestMapping(params = "cmd=deleteModifyExecute", method = RequestMethod.POST)
	public void deleteModifyExecute(HttpServletRequest request,HttpServletResponse response) throws IOException{
		PrintWriter out = response.getWriter();
		String dataSourceCode = request.getParameter("dataSourceCode");
		String id = request.getParameter("ID");
		String sql = "DELETE FROM "+dataSourceCode+" WHERE ID = "+id;
		try {
			BaseDao.getBaseDao().execute(sql);
			out.print("删除成功");
		} catch (DataAccessException e) {
			out.print("删除成功");
			e.printStackTrace();
		}
	}
	public static double calLeaveDays(Date startTime,Date endTime){
		double leaveDays = 0;
		//从startTime开始循环，若该日期不是节假日或者不是周六日则请假天数+1
		Date flag = startTime;//设置循环开始日期
		Calendar cal = Calendar.getInstance();
		//循环遍历每个日期
		while(flag.compareTo(endTime)!=1){
		    cal.setTime(flag);
		    //判断是否为周六日
		    int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
		    if(week == 0 || week == 6){//0为周日，6为周六
		        //跳出循环进入下一个日期
		        cal.add(Calendar.DAY_OF_MONTH, +1);
		        flag = cal.getTime();
		        continue;
		    }
		    //不是节假日或者周末，天数+1
		    leaveDays = leaveDays + 1;
		    //日期往后加一天
		   	cal.add(Calendar.DAY_OF_MONTH, +1);
		   	flag = cal.getTime();
		}
		return leaveDays;
	}
	
	
	//能力立项
		@RequestMapping(params = "cmd=find_ability_id")
		public void find_ability_id(HttpServletRequest request,HttpServletResponse response,String id) throws Exception {
			
			String pro_id_ability=request.getParameter("pro_id_ability");
			
            
			
			
			Integer i_add=0;
			Integer i_add_count=0;
			String start_time="";
			Double now_all_time=0.00;
			Double end_all_time=0.00;
			
			String proj_all_com_inString="";
			
			String appln_time="";
			
			List<Map<String,Object>> lMaps=new ArrayList<Map<String,Object>>();
			List<Map<String,Object>> lMaps2=new ArrayList<Map<String,Object>>();
			List<Map<String,Object>> lMaps3=new ArrayList<Map<String,Object>>();
			JSONArray jArray=new JSONArray();
			String sqlString="select * from  " +"PROJ_RELEASED".toLowerCase()+" where PROJ_CODE="+"\""+id+"\"";
			lMaps=BaseDao.getBaseDao().query(sqlString,"");
			if(lMaps.size()<=0){
			String string="{CUS_LEVEL:0}";
			response.getWriter().println(JSONObject.fromObject(string));	
		    response.getWriter().close();	
			}else{
			//jArray.add(lMaps);
			String aString="";	
			for(Map<String,Object>map2:lMaps){
				if(map2.containsKey("RELEASED_DATE")){
					Date d2=new Date();
					SimpleDateFormat	simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                    
					d2=simpleDateFormat.parse(map2.get("RELEASED_DATE").toString());
					
					String string=simpleDateFormat.format(d2);
					
					map2.put("RELEASED_DATE",string);
					
				}
				
				
				aString=String.valueOf(map2.get("ID"));
			}	
	        		
			String sqlString1="select   COALESCE(WBS_START_DATE,'') WBS_START_DATE,COALESCE(WBS_EXPECTED_END_DATE,'') WBS_EXPECTED_END_DATE,COALESCE(WBS_COMPLETION_TIME,'') WBS_COMPLETION_TIME,WBS_WORKING_HOURS,WBS_THOSE_RESPONSIBLE,WBS_BILL_STATE,WBS_NAME,ID,PROJ_TYPE,TASK_REMARK,PROJ_SOURCE_ID from  proj_wbsinfo  where  proj_source_id="+pro_id_ability;           
			//String sqlString2="select count(*)  from  proj_wbsinfo  where  proj_source_id="+id;
			lMaps2=BaseDao.getBaseDao().query(sqlString1,"");
			//lMaps3=BaseDao.getBaseDao().query(sqlString2,"");
			if(lMaps2.size()<=0){
				proj_all_com_inString="";
			}
			else{
				String sqlString2="select count(*)  from  proj_wbsinfo  where  proj_source_id="+pro_id_ability;
				lMaps3=BaseDao.getBaseDao().query(sqlString2,"");
				  for(Map<String,Object> map_allMap:lMaps2){
	                 
					  i_add++;
					  
					  i_add_count++;
					  
					  if(map_allMap.containsKey("WBS_START_DATE")){	  
						  //String date = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date(Unix timestamp * 1000))
						 // Date epoch =SimpleDateFormat("dd/MM/yyyy").parse(map3.get("WBS_START_DATE").toString());
						 // long lTime= epoch.getTime();
						 // System.err.println(map3.get("WBS_START_DATE").toString());
						    Date d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(map_allMap.get("WBS_START_DATE").toString());
							SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
							start_time=sdf1.format(d1);
							
							Date d2=new SimpleDateFormat("MM/dd/yyyy").parse(sdf1.format(d1));
							Long aDate=d2.getTime();
							map_allMap.put("start",aDate);//map3.get("WBS_START_DATE").toString()); 
							map_allMap.remove("WBS_START_DATE");
						  }
					  if(map_allMap.containsKey("WBS_WORKING_HOURS")){
						  map_allMap.put("duration",map_allMap.get("WBS_WORKING_HOURS")); 
						  map_allMap.remove("WBS_WORKING_HOURS");
						  }
					  
					  if(map_allMap.containsKey("WBS_EXPECTED_END_DATE")){
						    Date d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(map_allMap.get("WBS_EXPECTED_END_DATE").toString());
							SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
							appln_time=sdf1.format(d1);
							
							Date d2=new SimpleDateFormat("MM/dd/yyyy").parse(sdf1.format(d1));
							Long aDate=d2.getTime();
							map_allMap.put("end",aDate);//map3.get("WBS_START_DATE").toString()); 
							map_allMap.remove("WBS_EXPECTED_END_DATE");
						  }
					  
					  
					  if("".equals(map_allMap.get("WBS_COMPLETION_TIME"))){
						 
						  
                          Date now_timeDate=new Date();//当前
						  
						  Date sDate=new SimpleDateFormat("MM/dd/yyyy").parse(start_time);//开始
						  
						  Date appln_Date=new SimpleDateFormat("MM/dd/yyyy").parse(appln_time);//计划
						  
						
						  
					  	 // now_all_time[i_add-1]=rdDouble;
						  
						 // end_all_time[i_add-1]=Double.valueOf(map_allMap.get("duration").toString());
						  
						  Double rdDouble=calLeaveDays(sDate,now_timeDate);
						  
						  if(map_allMap.get("duration")==null){
							  proj_all_com_inString="0.0";
						  }
						  else{
						  if(rdDouble>Double.valueOf(map_allMap.get("duration").toString())){
							  Double rdDoubles=calLeaveDays(sDate,appln_Date); 
							  now_all_time+=rdDoubles;
							  
						      end_all_time+=Double.valueOf(map_allMap.get("duration").toString());
						  }	
						  else{
					      now_all_time+=rdDouble;
							  
					      end_all_time+=Double.valueOf(map_allMap.get("duration").toString());
						  }
						  
						  
						  
						  
					  }
						  
						  
					  }else{
						  
                          Date now_timeDate=new Date();//当前
						  
						  Date sDate=new SimpleDateFormat("MM/dd/yyyy").parse(start_time);//开始
						  
						  Date appln_Date=new SimpleDateFormat("MM/dd/yyyy").parse(appln_time);//计划
						  
						  Double rdDouble=calLeaveDays(sDate,appln_Date);
						  
						  now_all_time+=rdDouble;
						  
					      end_all_time+=Double.valueOf(map_allMap.get("duration").toString());
					  }
					  if(i_add_count>=Integer.valueOf(lMaps3.get(0).get("COUNT(*)").toString())){i_add=0;i_add_count=0;}  
				  }
//				  Double now_sum_time=null,day_all_time=null;
		          
//		          for(int i=0;i<now_all_time.length;i++){
//		        	  
//		        	  now_sum_time+=now_all_time[i];
//		          }
//		          
//                  for(int i=0;i<end_all_time.length;i++){
//		        	  
//		        	  day_all_time+=end_all_time[i];
//	          }
                if(now_all_time==0.0 || end_all_time==0.0){
                	
                	proj_all_com_inString="100.0";
                }else{
				  
                  Double proj_all_com_inStringDouble=now_all_time/end_all_time;
                  
                  Double proj_all_com_inStringDoubles= (double) (Math.round(proj_all_com_inStringDouble*100)/100);
		          
                  BigDecimal bg = new BigDecimal(proj_all_com_inStringDouble*100);
                  double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                  
                  proj_all_com_inString=String.valueOf(f1);
                }
			}
			
			 List<Map<String,Object>> lMaps_Maps=new ArrayList<Map<String,Object>>();
			 String sql="select *  from proj_contrast_cus where ONLY_ONE_ID="+"\""+id+"\""; 
			 
			 lMaps_Maps=BaseDao.getBaseDao().query(sql,"");
			if(lMaps_Maps.size()<=0){
			
			 Map<String,Object>map_addMap=new HashMap<String, Object>();
			 String string[]={"PROJ_STAUTS","PROJ_IN_STAUTS","PROJ_NOW_PRICE","PROJ_TALK_MESSAGE","ONLY_ONE_ID"};
			 for(int i=0;i<string.length;i++){
				 if("PROJ_STAUTS".equals(string[i])){						 
					 map_addMap.put(string[i],"0");
				 }
				 if("PROJ_IN_STAUTS".equals(string[i])){						 
					 map_addMap.put(string[i],"0");
				 }
				 if("PROJ_OUT_STAUTS".equals(string[i])){						 
					 map_addMap.put(string[i],"0");
				 }
				 if("PROJ_NOW_PRICE".equals(string[i])){						 
					 map_addMap.put(string[i],"0");
				 }
				 if("ONLY_ONE_ID".equals(string[i])){						 
					 map_addMap.put(string[i],id);
				 }
				 if("PROJ_TALK_MESSAGE".equals(string[i])){						 
					 map_addMap.put(string[i],"");
				 }
			 }
			 
			 BaseDao.getBaseDao().insert("PROJ_CONTRAST_CUS", map_addMap);
			}
			
			
			          
                      
		    JSONObject jsonObject=new JSONObject();		
			jArray.add(lMaps);
			jsonObject.put("ID", aString);
			jsonObject.put("message",jArray.get(0));
			jsonObject.put("proj_speed_message", proj_all_com_inString);
			System.err.println("_________________________11111="+jsonObject);
		    response.getWriter().println(jsonObject);	
		    response.getWriter().close();
			}
		
			
		}
		
		
		//客户立项
				@RequestMapping(params = "cmd=find_Cus_id")
				public void find_Cus_id(HttpServletRequest request,HttpServletResponse response,String id) throws Exception {
					Integer i_add=0;
					Integer i_add_count=0;
					String start_time="";
					Double now_all_time=0.00;
					Double end_all_time=0.00;
					
					String proj_all_com_inString="";
					
					String appln_time="";
					
					List<Map<String,Object>> lMaps=new ArrayList<Map<String,Object>>();
					List<Map<String,Object>> lMaps2=new ArrayList<Map<String,Object>>();
					List<Map<String,Object>> lMaps3=new ArrayList<Map<String,Object>>();
					JSONArray jArray=new JSONArray();
					String sqlString="select * from  " +"PROJ_RELEASED".toLowerCase()+" where OPPORTUNITY_CODE="+"\""+id+"\"";
					lMaps=BaseDao.getBaseDao().query(sqlString,"");
					if(lMaps.size()<=0){
					String string="{CUS_LEVEL:0}";
					response.getWriter().println(JSONObject.fromObject(string));	
				    response.getWriter().close();	
					}else{
					//jArray.add(lMaps);
					String aString="";	
					for(Map<String,Object>map2:lMaps){
						if(map2.containsKey("RELEASED_DATE")){
							Date d2=new Date();
							SimpleDateFormat	simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                            
							d2=simpleDateFormat.parse(map2.get("RELEASED_DATE").toString());
							
							String string=simpleDateFormat.format(d2);
							
							map2.put("RELEASED_DATE",string);
							
						}
						
						
						aString=String.valueOf(map2.get("ID"));
					}	
			        		
					String sqlString1="select   COALESCE(WBS_START_DATE,'') WBS_START_DATE,COALESCE(WBS_EXPECTED_END_DATE,'') WBS_EXPECTED_END_DATE,COALESCE(WBS_COMPLETION_TIME,'') WBS_COMPLETION_TIME,WBS_WORKING_HOURS,WBS_THOSE_RESPONSIBLE,WBS_BILL_STATE,WBS_NAME,ID,PROJ_TYPE,TASK_REMARK,PROJ_SOURCE_ID from  proj_wbsinfo  where  proj_source_id="+aString;           
					//String sqlString2="select count(*)  from  proj_wbsinfo  where  proj_source_id="+id;
					lMaps2=BaseDao.getBaseDao().query(sqlString1,"");
					//lMaps3=BaseDao.getBaseDao().query(sqlString2,"");
					if(lMaps2.size()<=0){
						proj_all_com_inString="";
					}
					else{
						String sqlString2="select count(*)  from  proj_wbsinfo  where  proj_source_id="+aString;
						lMaps3=BaseDao.getBaseDao().query(sqlString2,"");
						  for(Map<String,Object> map_allMap:lMaps2){
			                 
							  i_add++;
							  
							  i_add_count++;
							  
							  if(map_allMap.containsKey("WBS_START_DATE")){	  
								  //String date = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date(Unix timestamp * 1000))
								 // Date epoch =SimpleDateFormat("dd/MM/yyyy").parse(map3.get("WBS_START_DATE").toString());
								 // long lTime= epoch.getTime();
								 // System.err.println(map3.get("WBS_START_DATE").toString());
								    Date d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(map_allMap.get("WBS_START_DATE").toString());
									SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
									start_time=sdf1.format(d1);
									
									Date d2=new SimpleDateFormat("MM/dd/yyyy").parse(sdf1.format(d1));
									Long aDate=d2.getTime();
									map_allMap.put("start",aDate);//map3.get("WBS_START_DATE").toString()); 
									map_allMap.remove("WBS_START_DATE");
								  }
							  if(map_allMap.containsKey("WBS_WORKING_HOURS")){
								  map_allMap.put("duration",map_allMap.get("WBS_WORKING_HOURS")); 
								  map_allMap.remove("WBS_WORKING_HOURS");
								  }
							  
							  if(map_allMap.containsKey("WBS_EXPECTED_END_DATE")){
								    Date d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(map_allMap.get("WBS_EXPECTED_END_DATE").toString());
									SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
									appln_time=sdf1.format(d1);
									
									Date d2=new SimpleDateFormat("MM/dd/yyyy").parse(sdf1.format(d1));
									Long aDate=d2.getTime();
									map_allMap.put("end",aDate);//map3.get("WBS_START_DATE").toString()); 
									map_allMap.remove("WBS_EXPECTED_END_DATE");
								  }
							  
							  
							  if("".equals(map_allMap.get("WBS_COMPLETION_TIME"))){
								 
								  
                                  Date now_timeDate=new Date();//当前
								  
								  Date sDate=new SimpleDateFormat("MM/dd/yyyy").parse(start_time);//开始
								  
								  Date appln_Date=new SimpleDateFormat("MM/dd/yyyy").parse(appln_time);//计划
								  
								
								  
							  	 // now_all_time[i_add-1]=rdDouble;
								  
								 // end_all_time[i_add-1]=Double.valueOf(map_allMap.get("duration").toString());
								  
								  Double rdDouble=calLeaveDays(sDate,now_timeDate);
								  
								  if(map_allMap.get("duration")==null){
									  proj_all_com_inString="0.0";
								  }
								  else{
								  if(rdDouble>Double.valueOf(map_allMap.get("duration").toString())){
									  Double rdDoubles=calLeaveDays(sDate,appln_Date); 
									  now_all_time+=rdDoubles;
									  
								      end_all_time+=Double.valueOf(map_allMap.get("duration").toString());
								  }	
								  else{
							      now_all_time+=rdDouble;
									  
							      end_all_time+=Double.valueOf(map_allMap.get("duration").toString());
								  }
								  
								  
								  
								  
							  }
								  
								  
							  }else{
								  
                                  Date now_timeDate=new Date();//当前
								  
								  Date sDate=new SimpleDateFormat("MM/dd/yyyy").parse(start_time);//开始
								  
								  Date appln_Date=new SimpleDateFormat("MM/dd/yyyy").parse(appln_time);//计划
								  
								  Double rdDouble=calLeaveDays(sDate,appln_Date);
								  
								  now_all_time+=rdDouble;
								  
							      end_all_time+=Double.valueOf(map_allMap.get("duration").toString());
							  }
							  if(i_add_count>=Integer.valueOf(lMaps3.get(0).get("COUNT(*)").toString())){i_add=0;i_add_count=0;}  
						  }
//						  Double now_sum_time=null,day_all_time=null;
				          
//				          for(int i=0;i<now_all_time.length;i++){
//				        	  
//				        	  now_sum_time+=now_all_time[i];
//				          }
//				          
//		                  for(int i=0;i<end_all_time.length;i++){
//				        	  
//				        	  day_all_time+=end_all_time[i];
//			          }
		                if(now_all_time==0.0 || end_all_time==0.0){
		                	
		                	proj_all_com_inString="100.0";
		                }else{
						  
		                  Double proj_all_com_inStringDouble=now_all_time/end_all_time;
		                  
		                  Double proj_all_com_inStringDoubles= (double) (Math.round(proj_all_com_inStringDouble*100)/100);
				          
		                  BigDecimal bg = new BigDecimal(proj_all_com_inStringDouble*100);
		                  double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		                  
		                  proj_all_com_inString=String.valueOf(f1);
		                }
					}
					
					 List<Map<String,Object>> lMaps_Maps=new ArrayList<Map<String,Object>>();
					 String sql="select *  from proj_contrast_cus where ONLY_ONE_ID="+"\""+id+"\""; 
					 
					 lMaps_Maps=BaseDao.getBaseDao().query(sql,"");
					if(lMaps_Maps.size()<=0){
					
					 Map<String,Object>map_addMap=new HashMap<String, Object>();
					 String string[]={"PROJ_STAUTS","PROJ_IN_STAUTS","PROJ_OUT_STAUTS","PROJ_NOW_PRICE","PROJ_TALK_MESSAGE","ONLY_ONE_ID"};
					 for(int i=0;i<string.length;i++){
						 if("PROJ_STAUTS".equals(string[i])){						 
							 map_addMap.put(string[i],"0");
						 }
						 if("PROJ_IN_STAUTS".equals(string[i])){						 
							 map_addMap.put(string[i],"0");
						 }
						 if("PROJ_OUT_STAUTS".equals(string[i])){						 
							 map_addMap.put(string[i],"0");
						 }
						 if("PROJ_NOW_PRICE".equals(string[i])){						 
							 map_addMap.put(string[i],"0");
						 }
						 if("ONLY_ONE_ID".equals(string[i])){						 
							 map_addMap.put(string[i],id);
						 }
						 if("PROJ_TALK_MESSAGE".equals(string[i])){						 
							 map_addMap.put(string[i],"");
						 }
					 }
					 
					 BaseDao.getBaseDao().insert("PROJ_CONTRAST_CUS", map_addMap);
					}
					
					
					          
		                      
				    JSONObject jsonObject=new JSONObject();		
					jArray.add(lMaps);
					jsonObject.put("ID", aString);
					jsonObject.put("message",jArray.get(0));
					jsonObject.put("proj_speed_message", proj_all_com_inString);
					//System.err.println("_________________________11111="+jsonObject);
				    response.getWriter().println(jsonObject);	
				    response.getWriter().close();
					}
				}
		
		
		
	
	
	
	
	//wbs信息95271
	@RequestMapping(params = "cmd=week_wbs_message")
	public void week_wbs_message(HttpServletRequest request,HttpServletResponse response,String id) throws Exception {
		
		Integer wbs_message_i=0;
		Integer wbs_message_j=0;
		String  start_time_all="";
		String  appln_time="";
		List<Map<String,Object>> lMaps=new ArrayList<Map<String,Object>>();
		//List<Map<String,String>> lMaps2=new ArrayList<Map<String,String>>();
		List<Map<String,Object>> lMaps3=new ArrayList<Map<String,Object>>();
		//JSONArray jArray=new JSONArray();
		//Map<String,Object> map=new HashMap<String, Object>();
		//Map<String,String> map2=new HashMap<String,String>();
		if(id==null){
			
            String sqlString="select  COALESCE(WBS_START_DATE,'') WBS_START_DATE,COALESCE(WBS_EXPECTED_END_DATE,'') WBS_EXPECTED_END_DATE,COALESCE(WBS_COMPLETION_TIME,'') WBS_COMPLETION_TIME,WBS_WORKING_HOURS,WBS_THOSE_RESPONSIBLE,COALESCE(WBS_BILL_STATE,'') WBS_BILL_STATE,WBS_NAME,ID,PROJ_TYPE,TASK_REMARK,PROJ_SOURCE_ID,COALESCE(LEVEL,'') LEVEL from  proj_wbsinfo_history ";
			
			String sqlString1="select count(*)  from  proj_wbsinfo_history";
			
			lMaps=BaseDao.getBaseDao().query(sqlString,"");
			lMaps3=BaseDao.getBaseDao().query(sqlString1,"");
		
		}else{
			String sqlString="select COALESCE(WBS_START_DATE,'') WBS_START_DATE,COALESCE(WBS_EXPECTED_END_DATE,'') WBS_EXPECTED_END_DATE,COALESCE(WBS_COMPLETION_TIME,'') WBS_COMPLETION_TIME,WBS_WORKING_HOURS,COALESCE(WBS_THOSE_RESPONSIBLE,0) WBS_THOSE_RESPONSIBLE,COALESCE(WBS_BILL_STATE,'') WBS_BILL_STATE,WBS_NAME,ID,PROJ_TYPE,TASK_REMARK,PROJ_SOURCE_ID,COALESCE(LEVEL,0) LEVEL ,COALESCE(PROJ_PHASE_ID,'') PROJ_PHASE_ID   from  proj_wbsinfo_history  where  version_history="+id;
			
			String sqlString1="select count(*)  from  proj_wbsinfo_history  where  version_history="+id;
			
			lMaps=BaseDao.getBaseDao().query(sqlString,"");
			lMaps3=BaseDao.getBaseDao().query(sqlString1,"");
		}
		
		if(lMaps.size()<=0){
			String string="{项目状态:0}";
			response.getWriter().println(JSONObject.fromObject(string));	
		    response.getWriter().close();	
		}else{
			//"startIsMilestone": false, "endIsMilestone": false, "collapsed": false, "assigs": [], "hasChild": true
			for(Map<String, Object>map3:lMaps){
				  wbs_message_i--;
				  wbs_message_j++;
				  if(map3.containsKey("WBS_NAME")){
//					  if("1".equals(map3.get("WBS_NAME").toString())) {map3.put("code","立项");}
//					  if("2".equals(map3.get("WBS_NAME").toString())) {map3.put("code","客户需求确认");}
//					  if("3".equals(map3.get("WBS_NAME").toString())) {map3.put("code","项目建议书评审完成");}
//					  if("4".equals(map3.get("WBS_NAME").toString())) {map3.put("code","采购完成");}
//					  if("5".equals(map3.get("WBS_NAME").toString())) {map3.put("code","开发设计");}
//					  if("6".equals(map3.get("WBS_NAME").toString())) {map3.put("code","实施");}
//					  if("7".equals(map3.get("WBS_NAME").toString())) {map3.put("code","项目初验");}
//					  if("8".equals(map3.get("WBS_NAME").toString())) {map3.put("code","客户验收上线");}
					  map3.put("code",map3.get("WBS_NAME").toString());
					  map3.remove("WBS_NAME");
					  }
				  if(map3.containsKey("TASK_REMARK")){
					  map3.put("name",map3.get("TASK_REMARK")); 
					  map3.remove("TASK_REMARK");
					  }
				  
				  if("".equals(map3.get("WBS_START_DATE"))){
					  map3.put("start","");
					  map3.remove("WBS_START_DATE");
				  }else{
				  if(map3.containsKey("WBS_START_DATE")){	  
					  //String date = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date(Unix timestamp * 1000))
					 // Date epoch =SimpleDateFormat("dd/MM/yyyy").parse(map3.get("WBS_START_DATE").toString());
					 // long lTime= epoch.getTime();
					 // System.err.println(map3.get("WBS_START_DATE").toString());
					    Date d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(map3.get("WBS_START_DATE").toString());
						SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
						start_time_all=sdf1.format(d1);
						Date d2=new SimpleDateFormat("MM/dd/yyyy").parse(sdf1.format(d1));
						Long aDate=d2.getTime();
					    map3.put("start",aDate);//map3.get("WBS_START_DATE").toString()); 
					    map3.remove("WBS_START_DATE");
					  }
				  }
				  if("".equals(map3.get("WBS_COMPLETION_TIME"))){
					  map3.put("appln_time","");
					  map3.remove("WBS_COMPLETION_TIME");
				  }else{
				  if(map3.containsKey("WBS_COMPLETION_TIME")){
					    Date d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(map3.get("WBS_COMPLETION_TIME").toString());
						SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
						//Date d2=new SimpleDateFormat("MM/dd/yyyy").parse(sdf1.format(d1));
					  map3.put("appln_time",sdf1.format(d1));//map3.get("WBS_EXPECTED_END_DATE")); 
					  map3.remove("WBS_COMPLETION_TIME");
					  }
				  }
				  
				  if("".equals(map3.get("WBS_EXPECTED_END_DATE"))){
					  map3.put("end","");
					  map3.remove("WBS_EXPECTED_END_DATE");
				  }else{
				  if(map3.containsKey("WBS_EXPECTED_END_DATE")){
					  
					    
					    Date d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(map3.get("WBS_EXPECTED_END_DATE").toString());
						SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
						Date d2=new SimpleDateFormat("MM/dd/yyyy").parse(sdf1.format(d1));
						
						appln_time=sdf1.format(d1);
						
						Long aDate=d2.getTime();
					    map3.put("end",aDate);//map3.get("WBS_COMPLETION_TIME")); 
					  map3.remove("WBS_EXPECTED_END_DATE");
					  }
				  }
				  if(map3.containsKey("WBS_WORKING_HOURS")){
					  map3.put("duration",map3.get("WBS_WORKING_HOURS")); 
					  map3.remove("WBS_WORKING_HOURS");
					  }
	
				  if(map3.containsKey("WBS_THOSE_RESPONSIBLE")){
					  if(map3.get("WBS_THOSE_RESPONSIBLE").toString().equals("0")){
						  map3.put("main_name",""); 
					  }
					  else{
					  map3.put("main_name",map3.get("WBS_THOSE_RESPONSIBLE")); 
					  }
					  map3.remove("WBS_THOSE_RESPONSIBLE");
					  }
				  if(map3.containsKey("WBS_BILL_STATE")){
					  if("".equals(map3.get("WBS_BILL_STATE").toString())){map3.put("wbs_status","");}else{
					  if("1".equals(map3.get("WBS_BILL_STATE").toString())){map3.put("wbs_status","正常");}
					  if("2".equals(map3.get("WBS_BILL_STATE").toString())){map3.put("wbs_status","预警");}
					  if("3".equals(map3.get("WBS_BILL_STATE").toString())){map3.put("wbs_status","延期");}
					  map3.remove("WBS_BILL_STATE");
					  }
				  }
				  if(map3.containsKey("ID")){
					  map3.put("wbs_ids",map3.get("ID").toString()); 
					  map3.put("id",wbs_message_i);
					 // System.err.println("***************="+wbs_message_i);
					
					  
					  //map3.put("proj_id",map3.get("PROJ_SOURCE_ID").toString());
					  //map3.put("proj_phase_id",map3.get("PROJ_PHASE_ID").toString());
					 
					  map3.remove("ID");
					  //map3.remove("PROJ_PHASE_ID");
					  
				  }
				  
				 
//				  if(map3.containsKey("LEVEL")){
//					  if("".equals(map3.get("LEVEL"))){map3.put("level","0");  map3.remove("LEVEL");   }
//					  else{map3.put("level","1"); map3.remove("LEVEL"); }					  
//				  }
			      
//				  for(Map.Entry<String, Object>mEntry:map3.entrySet()){					  
//					  map2.put(mEntry.getKey(),String.valueOf(mEntry.getValue()));
//				  }
//				  lMaps2.add(map2);				  
//				  JSONObject jsonObject=new JSONObject();
//				  jsonObject.putAll(map3);
//				  jsonObject.put("startIsMilestone",false);
//				  jsonObject.put("endIsMilestone",false);
//				  jsonObject.put("collapsed",false);
//				  jsonObject.put("assigs","[]");
//				  jsonObject.put("hasChild",true);
//				  jsonObject.put("level","0");			
//				  jsonObject.put("progressByWorklog",true);
//				  jsonObject.put("progress","0");
//				  jsonObject.put("relevance","0");
//				  jsonObject.put("typeId","");
//				  jsonObject.put("description","");
//				  jsonObject.put("status","STATUS_ACTIVE");
//				  jsonObject.put("canWrite",true);
			      map3.put("startIsMilestone",Boolean.valueOf("false"));
				  map3.put("endIsMilestone",Boolean.valueOf("false"));
				  map3.put("collapsed",Boolean.valueOf("false"));
				  map3.put("assigs","[]");
				  map3.put("hasChild",Boolean.valueOf("true"));
				  
				  //级别
				  map3.put("level",Integer.valueOf(map3.get("LEVEL").toString()));	
				 
//				  if(map3.get("LEVEL").toString().equals("0")){
//					  
//					  map3.put("canWrite",Boolean.valueOf("false"));  
//				  }
//				  else{
//					  map3.put("canWrite",Boolean.valueOf("true"));  
//				  }
				  
				  
				  
				  map3.put("progressByWorklog",Boolean.valueOf("false"));
				
				
				
				  if(map3.get("appln_time")!=""){
				     map3.put("progress","100");
				     map3.put("wbs_status","正常");
				  }else{
					  Date aDate=new Date();
					  SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
					  SimpleDateFormat sdf2 = new SimpleDateFormat("dd");
					  String end_time=sdf2.format(aDate);	
					  
					  String end_time1=sdf1.format(aDate);
					  
					  Date aDate2_end=new  SimpleDateFormat("MM/dd/yyyy").parse(end_time1);//当前

					  Date start_timeses=new SimpleDateFormat("MM/dd/yyyy").parse(start_time_all);//开始
					 
					  String start_times=new SimpleDateFormat("dd").format(start_timeses);
					  
					  Date appDate=new SimpleDateFormat("MM/dd/yyyy").parse(appln_time);//计划

					 // System.err.println(Integer.valueOf(start_times)+"  "+Integer.valueOf(end_time));
					  if(start_times.equals(end_time)){ map3.put("progress","0"); map3.put("wbs_status","正常");}
					  //else if(Integer.valueOf(start_times)>Integer.valueOf(end_time)){map3.put("progress","0");}
					  else{
						    Double end_pro_timeDouble=calLeaveDays(start_timeses,aDate2_end);
						    
						    if(map3.get("duration")==null){
						    	//map3.put("progress","0.0");
						    	//map3.put("wbs_status","预警");
						      
//						    	response.setContentType("text/html; charset=UTF-8");
//								PrintWriter out = response.getWriter();
//				      			out.println("<script language='javascript'>");
//				      			out.println("alert('工时，时间异常!')");
//							    //out.println("window.close();");
//								out.println("</script>");
//								out.flush();
//								out.close();
						    
						        continue ;
						    }  
						    else{
						    if(end_pro_timeDouble > Double.valueOf(map3.get("duration").toString())){
						    	
						    	map3.put("progress","100.0");
						    	
						    	map3.put("wbs_status","延期");
						    }
						    else{
						    	Double end_numbers =end_pro_timeDouble/Double.valueOf(map3.get("duration").toString());
						    	
						    	Double endDouble=(double)Math.round(end_numbers*100)/100;
								    
								map3.put("progress",endDouble*100);
								  map3.put("wbs_status","正常");
						    }
						    }
						    if(end_pro_timeDouble<0){		    	
						    	map3.put("progress","0.0");
						    	map3.put("wbs_status","正常");
						    }
						    
//						    else{
//						 						         
//						    //time_all[wbs_message_j]=end_pro_timeDouble; 
//						    //day_all[wbs_message_j]=(Double) map3.get("duration");
//						    
//						    
//						    
//						    
//						    Double end_numbers=(double) (end_pro_timeDouble/Double.valueOf(map3.get("duration").toString()));
//						    
//						   
//						   }
						 }				 
				  }
				  map3.put("relevance",0);
				  map3.put("typeId","");
				  map3.put("description","");
				  map3.put("status","STATUS_ACTIVE");
				  map3.put("canWrite",Boolean.valueOf("false"));  
				  
				  if(wbs_message_j>=Integer.valueOf(lMaps3.get(0).get("COUNT(*)").toString())){wbs_message_i=0;wbs_message_j=0;}
				  
				  //JacksonJsonUtil
				  //JsonUtils
				  //JsonUtils
//				  jsonObject.put("start", JsonUtils.getJSONString(Long.valueOf("1396994400000")));
//				  jsonObject.put("end",JsonUtils.getJSONString(Long.valueOf("1399586399999")));
//				  
//				  jArray.add(jsonObject);
//				  
//				 
//				  
//				  map2=new HashMap<String,String>();
//				  map2.clear();
				}	
	    //jArray.add(lMaps2);
	    //jArray =new JSONArray().fromObject(lMaps2);
	    JSONObject jsonObject=new JSONObject();
	    
	    
	    
	    jsonObject.put("tasks",JsonUtils.getJSONString(lMaps));
	    System.err.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++=="+jsonObject);
		response.getWriter().println(jsonObject);
	    response.getWriter().close();
		}
	}
    
	//项目文件wbs周报
	@RequestMapping(params = "cmd=find_wbs_message_pr")
	public void find_wbs_message_pr(HttpServletRequest request,HttpServletResponse response,String id) throws Exception {
		
		Integer wbs_message_i=0;
		Integer wbs_message_j=0;
		String  start_time_all="";
		String  appln_time="";
		List<Map<String,Object>> lMaps=new ArrayList<Map<String,Object>>();
		//List<Map<String,String>> lMaps2=new ArrayList<Map<String,String>>();
		List<Map<String,Object>> lMaps3=new ArrayList<Map<String,Object>>();
		//JSONArray jArray=new JSONArray();
		//Map<String,Object> map=new HashMap<String, Object>();
		//Map<String,String> map2=new HashMap<String,String>();
		if(id==null){
			
            String sqlString="select  COALESCE(WBS_START_DATE,'') WBS_START_DATE,COALESCE(WBS_EXPECTED_END_DATE,'') WBS_EXPECTED_END_DATE,COALESCE(WBS_COMPLETION_TIME,'') WBS_COMPLETION_TIME,WBS_WORKING_HOURS,WBS_THOSE_RESPONSIBLE,COALESCE(WBS_BILL_STATE,'') WBS_BILL_STATE,WBS_NAME,ID,PROJ_TYPE,TASK_REMARK,PROJ_SOURCE_ID,COALESCE(LEVEL,'') LEVEL from  proj_wbsinfo ";
			
			String sqlString1="select count(*)  from  proj_wbsinfo";
			
			lMaps=BaseDao.getBaseDao().query(sqlString,"");
			lMaps3=BaseDao.getBaseDao().query(sqlString1,"");
		
		}else{
			//String sqlString="select COALESCE(WBS_START_DATE,'') WBS_START_DATE,COALESCE(WBS_EXPECTED_END_DATE,'') WBS_EXPECTED_END_DATE,COALESCE(WBS_COMPLETION_TIME,'') WBS_COMPLETION_TIME,WBS_WORKING_HOURS,WBS_THOSE_RESPONSIBLE,COALESCE(WBS_BILL_STATE,'') WBS_BILL_STATE,WBS_NAME,ID,PROJ_TYPE,TASK_REMARK,PROJ_SOURCE_ID,COALESCE(LEVEL,0) LEVEL ,COALESCE(PROJ_PHASE_ID,'') PROJ_PHASE_ID   from  proj_wbsinfo  where  proj_source_id="+id;
			String sqlString="select COALESCE(WBS_START_DATE,'') WBS_START_DATE,COALESCE(WBS_EXPECTED_END_DATE,'') WBS_EXPECTED_END_DATE,COALESCE(WBS_COMPLETION_TIME,'') WBS_COMPLETION_TIME,WBS_WORKING_HOURS,COALESCE(WBS_THOSE_RESPONSIBLE,0) WBS_THOSE_RESPONSIBLE,COALESCE(WBS_BILL_STATE,'') WBS_BILL_STATE,WBS_NAME,ID,PROJ_TYPE,TASK_REMARK,PROJ_SOURCE_ID,COALESCE(LEVEL,0) LEVEL ,COALESCE(PROJ_PHASE_ID,'') PROJ_PHASE_ID   from  proj_wbsinfo_history  where  version_history="+id;
			String sqlString1="select count(*)  from  proj_wbsinfo_history  where  version_history="+id;
			
			lMaps=BaseDao.getBaseDao().query(sqlString,"");
			lMaps3=BaseDao.getBaseDao().query(sqlString1,"");
		}
		
		if(lMaps.size()<=0){
			String string="{项目状态:0}";
			response.getWriter().println(JSONObject.fromObject(string));	
		    response.getWriter().close();	
		}else{
			//"startIsMilestone": false, "endIsMilestone": false, "collapsed": false, "assigs": [], "hasChild": true
			for(Map<String, Object>map3:lMaps){
				  wbs_message_i--;
				  wbs_message_j++;
				  if(map3.containsKey("WBS_NAME")){
//					  if("1".equals(map3.get("WBS_NAME").toString())) {map3.put("code","立项");}
//					  if("2".equals(map3.get("WBS_NAME").toString())) {map3.put("code","客户需求确认");}
//					  if("3".equals(map3.get("WBS_NAME").toString())) {map3.put("code","项目建议书评审完成");}
//					  if("4".equals(map3.get("WBS_NAME").toString())) {map3.put("code","采购完成");}
//					  if("5".equals(map3.get("WBS_NAME").toString())) {map3.put("code","开发设计");}
//					  if("6".equals(map3.get("WBS_NAME").toString())) {map3.put("code","实施");}
//					  if("7".equals(map3.get("WBS_NAME").toString())) {map3.put("code","项目初验");}
//					  if("8".equals(map3.get("WBS_NAME").toString())) {map3.put("code","客户验收上线");}
					  map3.put("code",map3.get("WBS_NAME"));
					  map3.remove("WBS_NAME");
					  }
				  if(map3.containsKey("TASK_REMARK")){
					  map3.put("name",map3.get("TASK_REMARK")); 
					  map3.remove("TASK_REMARK");
					  }
				  
				  if("".equals(map3.get("WBS_START_DATE"))){
					  map3.put("start","");
					  map3.remove("WBS_START_DATE");
				  }else{
				  if(map3.containsKey("WBS_START_DATE")){	  
					  //String date = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date(Unix timestamp * 1000))
					 // Date epoch =SimpleDateFormat("dd/MM/yyyy").parse(map3.get("WBS_START_DATE").toString());
					 // long lTime= epoch.getTime();
					 // System.err.println(map3.get("WBS_START_DATE").toString());
					    Date d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(map3.get("WBS_START_DATE").toString());
						SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
						start_time_all=sdf1.format(d1);
						Date d2=new SimpleDateFormat("MM/dd/yyyy").parse(sdf1.format(d1));
						Long aDate=d2.getTime();
					    map3.put("start",aDate);//map3.get("WBS_START_DATE").toString()); 
					    map3.remove("WBS_START_DATE");
					  }
				  }
				  if("".equals(map3.get("WBS_COMPLETION_TIME"))){
					  map3.put("appln_time","");
					  map3.remove("WBS_COMPLETION_TIME");
				  }else{
				  if(map3.containsKey("WBS_COMPLETION_TIME")){
					    Date d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(map3.get("WBS_COMPLETION_TIME").toString());
						SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
						//Date d2=new SimpleDateFormat("MM/dd/yyyy").parse(sdf1.format(d1));
					  map3.put("appln_time",sdf1.format(d1));//map3.get("WBS_EXPECTED_END_DATE")); 
					  map3.remove("WBS_COMPLETION_TIME");
					  }
				  }
				  
				  if("".equals(map3.get("WBS_EXPECTED_END_DATE"))){
					  map3.put("end","");
					  map3.remove("WBS_EXPECTED_END_DATE");
				  }else{
				  if(map3.containsKey("WBS_EXPECTED_END_DATE")){
					  
					    
					    Date d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(map3.get("WBS_EXPECTED_END_DATE").toString());
						SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
						Date d2=new SimpleDateFormat("MM/dd/yyyy").parse(sdf1.format(d1));
						
						appln_time=sdf1.format(d1);
						
						Long aDate=d2.getTime();
					    map3.put("end",aDate);//map3.get("WBS_COMPLETION_TIME")); 
					  map3.remove("WBS_EXPECTED_END_DATE");
					  }
				  }
				  if(map3.containsKey("WBS_WORKING_HOURS")){
					  map3.put("duration",map3.get("WBS_WORKING_HOURS")); 
					  map3.remove("WBS_WORKING_HOURS");
					  }
	
				  if(map3.containsKey("WBS_THOSE_RESPONSIBLE")){
					  if(map3.get("WBS_THOSE_RESPONSIBLE").toString().equals("0") ){
						  map3.put("main_name","");
					  }
					  else{
					  map3.put("main_name",map3.get("WBS_THOSE_RESPONSIBLE")); 
					  }
					  map3.remove("WBS_THOSE_RESPONSIBLE");
					  }
				  if(map3.containsKey("WBS_BILL_STATE")){
					  if("".equals(map3.get("WBS_BILL_STATE").toString())){map3.put("wbs_status","");}else{
					  if("1".equals(map3.get("WBS_BILL_STATE").toString())){map3.put("wbs_status","正常");}
					  if("2".equals(map3.get("WBS_BILL_STATE").toString())){map3.put("wbs_status","预警");}
					  if("3".equals(map3.get("WBS_BILL_STATE").toString())){map3.put("wbs_status","延期");}
					  map3.remove("WBS_BILL_STATE");
					  }
				  }
				  if(map3.containsKey("ID")){
					  map3.put("wbs_ids",map3.get("ID").toString()); 
					  map3.put("id",wbs_message_i);
					  // System.err.println("***************="+wbs_message_i);
					  map3.remove("ID");
				  }
				  
			      map3.put("startIsMilestone",Boolean.valueOf("false"));
				  map3.put("endIsMilestone",Boolean.valueOf("false"));
				  map3.put("collapsed",Boolean.valueOf("false"));
				  map3.put("assigs","[]");
				  map3.put("hasChild",Boolean.valueOf("true"));
				  
				  //级别
				  map3.put("level",Integer.valueOf(map3.get("LEVEL").toString()));	
				  map3.put("progressByWorklog",Boolean.valueOf("false"));
				
				    if(map3.get("LEVEL").toString().equals("0")){
				    	 map3.put("canWrite",Boolean.valueOf("false"));
				    	 //map3.put("canWriteOnParent",Boolean.valueOf("false"));
				    }
				    else{
				    	 map3.put("canWrite",Boolean.valueOf("true"));
				    	 //map3.put("canWriteOnParent",Boolean.valueOf("true"));
				    }
				  
				
				
				  if(map3.get("appln_time")!=""){
				     map3.put("progress","100");
				     map3.put("wbs_status","正常");
				     map3.put("status","STATUS_ACTIVE");
				  }else{
					  Date aDate=new Date();
					  SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
					  SimpleDateFormat sdf2 = new SimpleDateFormat("dd");
					  String end_time=sdf2.format(aDate);	
					  
					  String end_time1=sdf1.format(aDate);
					  
					  Date aDate2_end=new  SimpleDateFormat("MM/dd/yyyy").parse(end_time1);//当前

					  Date start_timeses=new SimpleDateFormat("MM/dd/yyyy").parse(start_time_all);//开始
					 
					  String start_times=new SimpleDateFormat("dd").format(start_timeses);
					  
					  Date appDate=new SimpleDateFormat("MM/dd/yyyy").parse(appln_time);//计划

					 // System.err.println(Integer.valueOf(start_times)+"  "+Integer.valueOf(end_time));
					  if(start_times.equals(end_time)){ map3.put("progress","0"); map3.put("wbs_status","正常");map3.put("status","STATUS_ACTIVE");}
					  //else if(Integer.valueOf(start_times)>Integer.valueOf(end_time)){map3.put("progress","0");}
					  else{
						    Double end_pro_timeDouble=calLeaveDays(start_timeses,aDate2_end);
						    
						    if(map3.get("duration")==null){
						        continue ;
						    }  
						    else{
						    if(end_pro_timeDouble > Double.valueOf(map3.get("duration").toString())){
						    	
						    	map3.put("progress","100.0");
						    	
						    	map3.put("wbs_status","延期");
						    	map3.put("status","STATUS_FAILED");
						    }
						    else{
						    	Double end_numbers =end_pro_timeDouble/Double.valueOf(map3.get("duration").toString());
						    	
						    	Double endDouble=(double)Math.round(end_numbers*100)/100;
								    
								map3.put("progress",endDouble*100);
								  map3.put("wbs_status","正常");
								  map3.put("status","STATUS_ACTIVE");
						    }
						    }
						    if(end_pro_timeDouble<0){		    	
						    	map3.put("progress","0.0");
						    	map3.put("wbs_status","正常");
						    	map3.put("status","STATUS_ACTIVE");
						    }
						 }				 
				  }
				  map3.put("relevance",0);
				  map3.put("typeId","");
				  map3.put("description","");
				  
				 
				  
				  if(wbs_message_j>=Integer.valueOf(lMaps3.get(0).get("COUNT(*)").toString())){wbs_message_i=0;wbs_message_j=0;}
				  
				}	
	    JSONObject jsonObject=new JSONObject();
	    
	    
	    
	    jsonObject.put("tasks",JsonUtils.getJSONString(lMaps));
	    //System.err.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++=="+jsonObject);
		response.getWriter().println(jsonObject);
	    response.getWriter().close();
		}
	}
	
	
	
	//wbs周报
	@RequestMapping(params = "cmd=find_wbs_message")
	public void find_wbs_message(HttpServletRequest request,HttpServletResponse response,String id) throws Exception {
		
		Integer wbs_message_i=0;
		Integer wbs_message_j=0;
		String  start_time_all="";
		String  appln_time="";
		List<Map<String,Object>> lMaps=new ArrayList<Map<String,Object>>();
		//List<Map<String,String>> lMaps2=new ArrayList<Map<String,String>>();
		List<Map<String,Object>> lMaps3=new ArrayList<Map<String,Object>>();
		//JSONArray jArray=new JSONArray();
		//Map<String,Object> map=new HashMap<String, Object>();
		//Map<String,String> map2=new HashMap<String,String>();
		if(id==null){
			
            String sqlString="select  COALESCE(WBS_START_DATE,'') WBS_START_DATE,COALESCE(WBS_EXPECTED_END_DATE,'') WBS_EXPECTED_END_DATE,COALESCE(WBS_COMPLETION_TIME,'') WBS_COMPLETION_TIME,WBS_WORKING_HOURS,WBS_THOSE_RESPONSIBLE,COALESCE(WBS_BILL_STATE,'') WBS_BILL_STATE,WBS_NAME,ID,PROJ_TYPE,TASK_REMARK,PROJ_SOURCE_ID,COALESCE(LEVEL,'') LEVEL from  proj_wbsinfo ";
			
			String sqlString1="select count(*)  from  proj_wbsinfo";
			
			lMaps=BaseDao.getBaseDao().query(sqlString,"");
			lMaps3=BaseDao.getBaseDao().query(sqlString1,"");
		
		}else{
			//String sqlString="select COALESCE(WBS_START_DATE,'') WBS_START_DATE,COALESCE(WBS_EXPECTED_END_DATE,'') WBS_EXPECTED_END_DATE,COALESCE(WBS_COMPLETION_TIME,'') WBS_COMPLETION_TIME,WBS_WORKING_HOURS,WBS_THOSE_RESPONSIBLE,COALESCE(WBS_BILL_STATE,'') WBS_BILL_STATE,WBS_NAME,ID,PROJ_TYPE,TASK_REMARK,PROJ_SOURCE_ID,COALESCE(LEVEL,0) LEVEL ,COALESCE(PROJ_PHASE_ID,'') PROJ_PHASE_ID   from  proj_wbsinfo  where  proj_source_id="+id;
			String sqlString="select COALESCE(WBS_START_DATE,'') WBS_START_DATE,COALESCE(WBS_EXPECTED_END_DATE,'') WBS_EXPECTED_END_DATE,COALESCE(WBS_COMPLETION_TIME,'') WBS_COMPLETION_TIME,WBS_WORKING_HOURS,COALESCE(WBS_THOSE_RESPONSIBLE,0) WBS_THOSE_RESPONSIBLE,COALESCE(WBS_BILL_STATE,'') WBS_BILL_STATE,WBS_NAME,ID,PROJ_TYPE,TASK_REMARK,PROJ_SOURCE_ID,COALESCE(LEVEL,0) LEVEL ,COALESCE(PROJ_PHASE_ID,'') PROJ_PHASE_ID   from  proj_wbsinfo  where  proj_source_id="+id;
			String sqlString1="select count(*)  from  proj_wbsinfo  where  proj_source_id="+id;
			
			lMaps=BaseDao.getBaseDao().query(sqlString,"");
			lMaps3=BaseDao.getBaseDao().query(sqlString1,"");
		}
		
		if(lMaps.size()<=0){
			String string="{项目状态:0}";
			response.getWriter().println(JSONObject.fromObject(string));	
		    response.getWriter().close();	
		}else{
			//"startIsMilestone": false, "endIsMilestone": false, "collapsed": false, "assigs": [], "hasChild": true
			for(Map<String, Object>map3:lMaps){
				  wbs_message_i--;
				  wbs_message_j++;
				  if(map3.containsKey("WBS_NAME")){
//					  if("1".equals(map3.get("WBS_NAME").toString())) {map3.put("code","立项");}
//					  if("2".equals(map3.get("WBS_NAME").toString())) {map3.put("code","客户需求确认");}
//					  if("3".equals(map3.get("WBS_NAME").toString())) {map3.put("code","项目建议书评审完成");}
//					  if("4".equals(map3.get("WBS_NAME").toString())) {map3.put("code","采购完成");}
//					  if("5".equals(map3.get("WBS_NAME").toString())) {map3.put("code","开发设计");}
//					  if("6".equals(map3.get("WBS_NAME").toString())) {map3.put("code","实施");}
//					  if("7".equals(map3.get("WBS_NAME").toString())) {map3.put("code","项目初验");}
//					  if("8".equals(map3.get("WBS_NAME").toString())) {map3.put("code","客户验收上线");}
					  map3.put("code",map3.get("WBS_NAME"));
					  map3.remove("WBS_NAME");
					  }
				  if(map3.containsKey("TASK_REMARK")){
					  map3.put("name",map3.get("TASK_REMARK")); 
					  map3.remove("TASK_REMARK");
					  }
				  
				  if("".equals(map3.get("WBS_START_DATE"))){
					  map3.put("start","");
					  map3.remove("WBS_START_DATE");
				  }else{
				  if(map3.containsKey("WBS_START_DATE")){	  
					  //String date = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date(Unix timestamp * 1000))
					 // Date epoch =SimpleDateFormat("dd/MM/yyyy").parse(map3.get("WBS_START_DATE").toString());
					 // long lTime= epoch.getTime();
					 // System.err.println(map3.get("WBS_START_DATE").toString());
					    Date d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(map3.get("WBS_START_DATE").toString());
						SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
						start_time_all=sdf1.format(d1);
						Date d2=new SimpleDateFormat("MM/dd/yyyy").parse(sdf1.format(d1));
						Long aDate=d2.getTime();
					    map3.put("start",aDate);//map3.get("WBS_START_DATE").toString()); 
					    map3.remove("WBS_START_DATE");
					  }
				  }
				  if("".equals(map3.get("WBS_COMPLETION_TIME"))){
					  map3.put("appln_time","");
					  map3.remove("WBS_COMPLETION_TIME");
				  }else{
				  if(map3.containsKey("WBS_COMPLETION_TIME")){
					    Date d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(map3.get("WBS_COMPLETION_TIME").toString());
						SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
						//Date d2=new SimpleDateFormat("MM/dd/yyyy").parse(sdf1.format(d1));
					  map3.put("appln_time",sdf1.format(d1));//map3.get("WBS_EXPECTED_END_DATE")); 
					  map3.remove("WBS_COMPLETION_TIME");
					  }
				  }
				  
				  if("".equals(map3.get("WBS_EXPECTED_END_DATE"))){
					  map3.put("end","");
					  map3.remove("WBS_EXPECTED_END_DATE");
				  }else{
				  if(map3.containsKey("WBS_EXPECTED_END_DATE")){
					  
					    
					    Date d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(map3.get("WBS_EXPECTED_END_DATE").toString());
						SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
						Date d2=new SimpleDateFormat("MM/dd/yyyy").parse(sdf1.format(d1));
						
						appln_time=sdf1.format(d1);
						
						Long aDate=d2.getTime();
					    map3.put("end",aDate);//map3.get("WBS_COMPLETION_TIME")); 
					  map3.remove("WBS_EXPECTED_END_DATE");
					  }
				  }
				  if(map3.containsKey("WBS_WORKING_HOURS")){
					  map3.put("duration",map3.get("WBS_WORKING_HOURS")); 
					  map3.remove("WBS_WORKING_HOURS");
					  }
	
				  if(map3.containsKey("WBS_THOSE_RESPONSIBLE")){
					  if(map3.get("WBS_THOSE_RESPONSIBLE").toString().equals("0") ){
						  map3.put("main_name","");
					  }
					  else{
					  map3.put("main_name",map3.get("WBS_THOSE_RESPONSIBLE")); 
					  }
					  map3.remove("WBS_THOSE_RESPONSIBLE");
					  }
				  if(map3.containsKey("WBS_BILL_STATE")){
					  if("".equals(map3.get("WBS_BILL_STATE").toString())){map3.put("wbs_status","");}else{
					  if("1".equals(map3.get("WBS_BILL_STATE").toString())){map3.put("wbs_status","正常");}
					  if("2".equals(map3.get("WBS_BILL_STATE").toString())){map3.put("wbs_status","预警");}
					  if("3".equals(map3.get("WBS_BILL_STATE").toString())){map3.put("wbs_status","延期");}
					  map3.remove("WBS_BILL_STATE");
					  }
				  }
				  if(map3.containsKey("ID")){
					  map3.put("wbs_ids",map3.get("ID").toString()); 
					  map3.put("id",wbs_message_i);
					  // System.err.println("***************="+wbs_message_i);
					  map3.remove("ID");
				  }
				  
			      map3.put("startIsMilestone",Boolean.valueOf("false"));
				  map3.put("endIsMilestone",Boolean.valueOf("false"));
				  map3.put("collapsed",Boolean.valueOf("false"));
				  map3.put("assigs","[]");
				  map3.put("hasChild",Boolean.valueOf("true"));
				  
				  //级别
				  map3.put("level",Integer.valueOf(map3.get("LEVEL").toString()));	
				  map3.put("progressByWorklog",Boolean.valueOf("false"));
				
				    if(map3.get("LEVEL").toString().equals("0")){
				    	 map3.put("canWrite",Boolean.valueOf("false"));
				    	 //map3.put("canWriteOnParent",Boolean.valueOf("false"));
				    }
				    else{
				    	 map3.put("canWrite",Boolean.valueOf("true"));
				    	 //map3.put("canWriteOnParent",Boolean.valueOf("true"));
				    }
				  
				
				
				  if(map3.get("appln_time")!=""){
				     map3.put("progress","100");
				     map3.put("wbs_status","正常");
				     map3.put("status","STATUS_ACTIVE");
				  }else{
					  Date aDate=new Date();
					  SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
					  SimpleDateFormat sdf2 = new SimpleDateFormat("dd");
					  String end_time=sdf2.format(aDate);	
					  
					  String end_time1=sdf1.format(aDate);
					  
					  Date aDate2_end=new  SimpleDateFormat("MM/dd/yyyy").parse(end_time1);//当前

					  Date start_timeses=new SimpleDateFormat("MM/dd/yyyy").parse(start_time_all);//开始
					 
					  String start_times=new SimpleDateFormat("dd").format(start_timeses);
					  
					  Date appDate=new SimpleDateFormat("MM/dd/yyyy").parse(appln_time);//计划

					 // System.err.println(Integer.valueOf(start_times)+"  "+Integer.valueOf(end_time));
					  if(start_times.equals(end_time)){ map3.put("progress","0"); map3.put("wbs_status","正常"); map3.put("status","STATUS_ACTIVE");}
					  //else if(Integer.valueOf(start_times)>Integer.valueOf(end_time)){map3.put("progress","0");}
					  else{
						    Double end_pro_timeDouble=calLeaveDays(start_timeses,aDate2_end);
						    
						    if(map3.get("duration")==null){
						        continue ;
						    }  
						    else{
						    if(end_pro_timeDouble > Double.valueOf(map3.get("duration").toString())){
						    	
						    	map3.put("progress","100.0");
						    	
						    	map3.put("wbs_status","延期");
						    	map3.put("status","STATUS_FAILED");
						    }
						    else{
						    	Double end_numbers =end_pro_timeDouble/Double.valueOf(map3.get("duration").toString());
						    	
						    	Double endDouble=(double)Math.round(end_numbers*100)/100;
								    
								map3.put("progress",endDouble*100);
								  map3.put("wbs_status","正常");
								  map3.put("status","STATUS_ACTIVE");
						    }
						    }
						    if(end_pro_timeDouble<0){		    	
						    	map3.put("progress","0.0");
						    	map3.put("wbs_status","正常");
						    	 map3.put("status","STATUS_ACTIVE");
						    }
						 }				 
				  }
				  map3.put("relevance",0);
				  map3.put("typeId","");
				  map3.put("description","");
				 // map3.put("status","STATUS_ACTIVE");
				 
				  
				  if(wbs_message_j>=Integer.valueOf(lMaps3.get(0).get("COUNT(*)").toString())){wbs_message_i=0;wbs_message_j=0;}
				  
				}	
	    JSONObject jsonObject=new JSONObject();
	    
	    
	    
	    jsonObject.put("tasks",JsonUtils.getJSONString(lMaps));
	    //System.err.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++=="+jsonObject);
		response.getWriter().println(jsonObject);
	    response.getWriter().close();
		}
	}
	
	@RequestMapping(params = "cmd=find_wbs_message_history")
	public void find_wbs_message_history(HttpServletRequest request,HttpServletResponse response,String id) throws Exception {
		
		Integer wbs_message_i=0;
		Integer wbs_message_j=0;
		String  start_time_all="";
		String  appln_time="";
		String code = request.getParameter("VCODE");
		List<Map<String,Object>> lMaps=new ArrayList<Map<String,Object>>();
		//List<Map<String,String>> lMaps2=new ArrayList<Map<String,String>>();
		List<Map<String,Object>> lMaps3=new ArrayList<Map<String,Object>>();
		//JSONArray jArray=new JSONArray();
		//Map<String,Object> map=new HashMap<String, Object>();
		//Map<String,String> map2=new HashMap<String,String>();
		if(id==null){
			
            String sqlString="select  COALESCE(WBS_START_DATE,'') WBS_START_DATE,COALESCE(WBS_EXPECTED_END_DATE,'') WBS_EXPECTED_END_DATE,COALESCE(WBS_COMPLETION_TIME,'') WBS_COMPLETION_TIME,WBS_WORKING_HOURS,WBS_THOSE_RESPONSIBLE,COALESCE(WBS_BILL_STATE,'') WBS_BILL_STATE,WBS_NAME,ID,PROJ_TYPE,TASK_REMARK,PROJ_SOURCE_ID,COALESCE(LEVEL,'') LEVEL from  proj_wbsinfo_history ";
			
			String sqlString1="select count(*)  from  proj_wbsinfo_history";
			
			lMaps=BaseDao.getBaseDao().query(sqlString,"");
			lMaps3=BaseDao.getBaseDao().query(sqlString1,"");
		
		}else{
			String sqlString="select COALESCE(WBS_START_DATE,'') WBS_START_DATE,COALESCE(WBS_EXPECTED_END_DATE,'') WBS_EXPECTED_END_DATE,COALESCE(WBS_COMPLETION_TIME,'') WBS_COMPLETION_TIME,WBS_WORKING_HOURS,WBS_THOSE_RESPONSIBLE,COALESCE(WBS_BILL_STATE,'') WBS_BILL_STATE,WBS_NAME,ID,PROJ_TYPE,TASK_REMARK,PROJ_SOURCE_ID,COALESCE(LEVEL,0) LEVEL ,COALESCE(PROJ_PHASE_ID,'') PROJ_PHASE_ID ,v_code  from  proj_wbsinfo_history  where  proj_source_id="+id + " and v_code ='"+code+"'";;
			
			String sqlString1="select count(*)  from  proj_wbsinfo_history  where  proj_source_id="+id + " and v_code ='"+code+"'";
			
			lMaps=BaseDao.getBaseDao().query(sqlString,"");
			lMaps3=BaseDao.getBaseDao().query(sqlString1,"");
		}
		
		if(lMaps.size()<=0){
			String string="{项目状态:0}";
			response.getWriter().println(JSONObject.fromObject(string));	
		    response.getWriter().close();	
		}else{
			//"startIsMilestone": false, "endIsMilestone": false, "collapsed": false, "assigs": [], "hasChild": true
			for(Map<String, Object>map3:lMaps){
				  wbs_message_i--;
				  wbs_message_j++;
				  if(map3.containsKey("WBS_NAME")){
//					  if("1".equals(map3.get("WBS_NAME").toString())) {map3.put("code","立项");}
//					  if("2".equals(map3.get("WBS_NAME").toString())) {map3.put("code","客户需求确认");}
//					  if("3".equals(map3.get("WBS_NAME").toString())) {map3.put("code","项目建议书评审完成");}
//					  if("4".equals(map3.get("WBS_NAME").toString())) {map3.put("code","采购完成");}
//					  if("5".equals(map3.get("WBS_NAME").toString())) {map3.put("code","开发设计");}
//					  if("6".equals(map3.get("WBS_NAME").toString())) {map3.put("code","实施");}
//					  if("7".equals(map3.get("WBS_NAME").toString())) {map3.put("code","项目初验");}
//					  if("8".equals(map3.get("WBS_NAME").toString())) {map3.put("code","客户验收上线");}
					  map3.put("code",map3.get("WBS_NAME"));
					  map3.remove("WBS_NAME");
					  }
				  if(map3.containsKey("TASK_REMARK")){
					  map3.put("name",map3.get("TASK_REMARK")); 
					  map3.remove("TASK_REMARK");
					  }
				  
				  if("".equals(map3.get("WBS_START_DATE"))){
					  map3.put("start","");
					  map3.remove("WBS_START_DATE");
				  }else{
				  if(map3.containsKey("WBS_START_DATE")){	  
					  //String date = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date(Unix timestamp * 1000))
					 // Date epoch =SimpleDateFormat("dd/MM/yyyy").parse(map3.get("WBS_START_DATE").toString());
					 // long lTime= epoch.getTime();
					 // System.err.println(map3.get("WBS_START_DATE").toString());
					    Date d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(map3.get("WBS_START_DATE").toString());
						SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
						start_time_all=sdf1.format(d1);
						Date d2=new SimpleDateFormat("MM/dd/yyyy").parse(sdf1.format(d1));
						Long aDate=d2.getTime();
					    map3.put("start",aDate);//map3.get("WBS_START_DATE").toString()); 
					    map3.remove("WBS_START_DATE");
					  }
				  }
				  if("".equals(map3.get("WBS_COMPLETION_TIME"))){
					  map3.put("appln_time","");
					  map3.remove("WBS_COMPLETION_TIME");
				  }else{
				  if(map3.containsKey("WBS_COMPLETION_TIME")){
					    Date d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(map3.get("WBS_COMPLETION_TIME").toString());
						SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
						//Date d2=new SimpleDateFormat("MM/dd/yyyy").parse(sdf1.format(d1));
					  map3.put("appln_time",sdf1.format(d1));//map3.get("WBS_EXPECTED_END_DATE")); 
					  map3.remove("WBS_COMPLETION_TIME");
					  }
				  }
				  
				  if("".equals(map3.get("WBS_EXPECTED_END_DATE"))){
					  map3.put("end","");
					  map3.remove("WBS_EXPECTED_END_DATE");
				  }else{
				  if(map3.containsKey("WBS_EXPECTED_END_DATE")){
					  
					    
					    Date d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(map3.get("WBS_EXPECTED_END_DATE").toString());
						SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
						Date d2=new SimpleDateFormat("MM/dd/yyyy").parse(sdf1.format(d1));
						
						appln_time=sdf1.format(d1);
						
						Long aDate=d2.getTime();
					    map3.put("end",aDate);//map3.get("WBS_COMPLETION_TIME")); 
					  map3.remove("WBS_EXPECTED_END_DATE");
					  }
				  }
				  if(map3.containsKey("WBS_WORKING_HOURS")){
					  map3.put("duration",map3.get("WBS_WORKING_HOURS")); 
					  map3.remove("WBS_WORKING_HOURS");
					  }
	
				  if(map3.containsKey("WBS_THOSE_RESPONSIBLE")){
					  map3.put("main_name",map3.get("WBS_THOSE_RESPONSIBLE")); 
					  map3.remove("WBS_THOSE_RESPONSIBLE");
					  }
				  if(map3.containsKey("WBS_BILL_STATE")){
					  if("".equals(map3.get("WBS_BILL_STATE").toString())){map3.put("wbs_status","");}else{
					  if("1".equals(map3.get("WBS_BILL_STATE").toString())){map3.put("wbs_status","正常");}
					  if("2".equals(map3.get("WBS_BILL_STATE").toString())){map3.put("wbs_status","预警");}
					  if("3".equals(map3.get("WBS_BILL_STATE").toString())){map3.put("wbs_status","延期");}
					  map3.remove("WBS_BILL_STATE");
					  }
				  }
				  if(map3.containsKey("ID")){
					  map3.put("wbs_ids",map3.get("ID").toString()); 
					  map3.put("id",wbs_message_i);
					  // System.err.println("***************="+wbs_message_i);
					  map3.remove("ID");
				  }
				  
			      map3.put("startIsMilestone",Boolean.valueOf("false"));
				  map3.put("endIsMilestone",Boolean.valueOf("false"));
				  map3.put("collapsed",Boolean.valueOf("false"));
				  map3.put("assigs","[]");
				  map3.put("hasChild",Boolean.valueOf("true"));
				  
				  //级别
				  map3.put("level",Integer.valueOf(map3.get("LEVEL").toString()));	
				  map3.put("progressByWorklog",Boolean.valueOf("false"));
				
				    if(map3.get("LEVEL").toString().equals("0")){
				    	 map3.put("canWrite",Boolean.valueOf("false"));
				    	 //map3.put("canWriteOnParent",Boolean.valueOf("false"));
				    }
				    else{
				    	 map3.put("canWrite",Boolean.valueOf("true"));
				    	 //map3.put("canWriteOnParent",Boolean.valueOf("true"));
				    }
				  
				
				
				  if(map3.get("appln_time")!=""){
				     map3.put("progress","100");
				     map3.put("wbs_status","正常");
				  }else{
					  Date aDate=new Date();
					  SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
					  SimpleDateFormat sdf2 = new SimpleDateFormat("dd");
					  String end_time=sdf2.format(aDate);	
					  
					  String end_time1=sdf1.format(aDate);
					  
					  Date aDate2_end=new  SimpleDateFormat("MM/dd/yyyy").parse(end_time1);//当前

					  Date start_timeses=new SimpleDateFormat("MM/dd/yyyy").parse(start_time_all);//开始
					 
					  String start_times=new SimpleDateFormat("dd").format(start_timeses);
					  
					  Date appDate=new SimpleDateFormat("MM/dd/yyyy").parse(appln_time);//计划

					 // System.err.println(Integer.valueOf(start_times)+"  "+Integer.valueOf(end_time));
					  if(start_times.equals(end_time)){ map3.put("progress","0"); map3.put("wbs_status","正常");}
					  //else if(Integer.valueOf(start_times)>Integer.valueOf(end_time)){map3.put("progress","0");}
					  else{
						    Double end_pro_timeDouble=calLeaveDays(start_timeses,aDate2_end);
						    
						    if(map3.get("duration")==null){
						        continue ;
						    }  
						    else{
						    if(end_pro_timeDouble > Double.valueOf(map3.get("duration").toString())){
						    	
						    	map3.put("progress","100.0");
						    	
						    	map3.put("wbs_status","延期");
						    }
						    else{
						    	Double end_numbers =end_pro_timeDouble/Double.valueOf(map3.get("duration").toString());
						    	
						    	Double endDouble=(double)Math.round(end_numbers*100)/100;
								    
								map3.put("progress",endDouble*100);
								  map3.put("wbs_status","正常");
						    }
						    }
						    if(end_pro_timeDouble<0){		    	
						    	map3.put("progress","0.0");
						    	map3.put("wbs_status","正常");
						    }
						 }				 
				  }
				  map3.put("relevance",0);
				  map3.put("typeId","");
				  map3.put("description","");
				  map3.put("status","STATUS_ACTIVE");
				 
				  
				  if(wbs_message_j>=Integer.valueOf(lMaps3.get(0).get("COUNT(*)").toString())){wbs_message_i=0;wbs_message_j=0;}
				  
				}	
	    JSONObject jsonObject=new JSONObject();
	    
	    
	    
	    jsonObject.put("tasks",JsonUtils.getJSONString(lMaps));
	    //System.err.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++=="+jsonObject);
		response.getWriter().println(jsonObject);
	    response.getWriter().close();
		}
	}
	
	
	
	@RequestMapping(params = "cmd=add_new_price", method = RequestMethod.POST)
	public void add_new_price(HttpServletRequest request,HttpServletResponse response,String id) throws IOException{
			
		 //预算金额状态
		 String outString=request.getParameter("now_price");	
		 if(!outString.equals("null") && outString != null ){
			String sqlString3="update "+"PROJ_RELEASED".toLowerCase()+"  set  proj_nosure_price="+"\""+outString+"\""+"  where OPPORTUNITY_CODE="+"\""+id+"\"";
			BaseDao.getBaseDao().update(sqlString3);
			String string="{CUS_LEVEL:0}";
			response.getWriter().println(JSONObject.fromObject(string));	
		    response.getWriter().close();	
		 }
		
	}
	
	
	@RequestMapping(params = "cmd=add_ability_price", method = RequestMethod.POST)
	public void add_ability_price(HttpServletRequest request,HttpServletResponse response,String id) throws IOException{
			
		 //预算金额状态
		 String outString=request.getParameter("now_price");	
		 if(!outString.equals("null") && outString != null ){
			String sqlString3="update "+"PROJ_RELEASED".toLowerCase()+"  set  proj_nosure_price="+"\""+outString+"\""+"  where PROJ_CODE="+"\""+id+"\"";
			BaseDao.getBaseDao().update(sqlString3);
			String string="{CUS_LEVEL:0}";
			response.getWriter().println(JSONObject.fromObject(string));	
		    response.getWriter().close();	
		 }
		
	}
	
	@RequestMapping(params = "cmd=insert_Cus_ids_ability")
	public void insert_Cus_ids_ability(HttpServletRequest request,HttpServletResponse response,String id) throws Exception {
	 //项目内部状态
	 String conString=request.getParameter("con_in_status");	
	 if(!conString.equals("null") && conString != null ){
		String sqlString2="update "+"PROJ_RELEASED".toLowerCase()+" set project_stage="+"\""+conString+"\""+" where PROJ_CODE="+"\""+id+"\"";
		BaseDao.getBaseDao().update(sqlString2);
		String string="{CUS_LEVEL:0}";
		response.getWriter().println(JSONObject.fromObject(string));	
	    response.getWriter().close();	
	 }
	}
	
	
	
	@RequestMapping(params = "cmd=contrask_message", method = RequestMethod.POST)
	public void contrask_message(HttpServletRequest request,HttpServletResponse response,String id,String []map) throws IOException{
		//List<Map<String,Object>> lMaps=new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> lMapses=new ArrayList<Map<String,Object>>();
		JSONObject jsonObject=new JSONObject();
		String sqlString2="select *  from proj_contrast_cus where ONLY_ONE_ID="+"\""+id+"\"";
		    
		lMapses=BaseDao.getBaseDao().query(sqlString2,"");
		
		jsonObject.put("PROJ_STAUTS",lMapses);
	    response.getWriter().println(jsonObject);	
        response.getWriter().close();	
		
//	if("0".equals(map[0]) || "0".equals(map[2]) || "0".equals(map[3])){
//		jsonObject.put("PROJ_STAUTS",lMapses);
//	    response.getWriter().println(jsonObject);	
//        response.getWriter().close();	
//		
//	}else{
		if(lMapses.size()<=0){
			
			 Map<String,Object> map2=new HashMap<String, Object>();
			 String string[]={"PROJ_STAUTS","PROJ_SPEED","PROJ_IN_STAUTS","PROJ_OUT_STAUTS","PROJ_NOW_PRICE","PROJ_TALK_MESSAGE","ONLY_ONE_ID"};
			 
			 for(int i=0;i<string.length;i++){
				 map2.put(string[i],map[i]);
			 }
			 BaseDao.getBaseDao().insert("proj_contrast_cus".toUpperCase(),map2);
			 
//			    String string2="{CUS_LEVEL:0}";
//				response.getWriter().println(JSONObject.fromObject(string2));	
//			    response.getWriter().close();
			   
			 
		 }else{
			 Map<String,Object> map2=new HashMap<String, Object>();
			 String string[]={"PROJ_STAUTS","PROJ_SPEED","PROJ_IN_STAUTS","PROJ_OUT_STAUTS","PROJ_NOW_PRICE","PROJ_TALK_MESSAGE","ONLY_ONE_ID"};
			 for(int i=0;i<string.length;i++){
				 map2.put(string[i],map[i]);
			 }
			// String sql="update proj_contrast_cus set  PROJ_STAUTS="+"\""+cString+"\""+" where only_one_id="+"\""+id+"\"";
			 BaseDao.getBaseDao().update("proj_contrast_cus".toUpperCase(),map2, new SqlWhereEntity().putWhere("ONLY_ONE_ID","\""+id+"\"",WhereEnum.EQUAL_INT));			 
		 }
		 
		   
		   
	//}
	}
	
	
	
	@RequestMapping(params = "cmd=contrask_message_ability", method = RequestMethod.POST)
	public void contrask_message_ability(HttpServletRequest request,HttpServletResponse response,String id,String []map) throws IOException{
		//List<Map<String,Object>> lMaps=new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> lMapses=new ArrayList<Map<String,Object>>();
		JSONObject jsonObject=new JSONObject();
		String sqlString2="select *  from proj_contrast_cus where ONLY_ONE_ID="+"\""+id+"\"";
		    
		lMapses=BaseDao.getBaseDao().query(sqlString2,"");
		
		jsonObject.put("PROJ_STAUTS",lMapses);
	    response.getWriter().println(jsonObject);	
        response.getWriter().close();	
		
//	if("0".equals(map[0]) || "0".equals(map[2]) || "0".equals(map[3])){
//		jsonObject.put("PROJ_STAUTS",lMapses);
//	    response.getWriter().println(jsonObject);	
//        response.getWriter().close();	
//		
//	}else{
		if(lMapses.size()<=0){
			
			 Map<String,Object> map2=new HashMap<String, Object>();
			 String string[]={"PROJ_STAUTS","PROJ_SPEED","PROJ_IN_STAUTS","PROJ_NOW_PRICE","PROJ_TALK_MESSAGE","ONLY_ONE_ID"};
			 
			 for(int i=0;i<string.length;i++){
				 map2.put(string[i],map[i]);
			 }
			 BaseDao.getBaseDao().insert("proj_contrast_cus".toUpperCase(),map2);
			 
//			    String string2="{CUS_LEVEL:0}";
//				response.getWriter().println(JSONObject.fromObject(string2));	
//			    response.getWriter().close();
			   
			 
		 }else{
			 Map<String,Object> map2=new HashMap<String, Object>();
			 String string[]={"PROJ_STAUTS","PROJ_SPEED","PROJ_IN_STAUTS","PROJ_NOW_PRICE","PROJ_TALK_MESSAGE","ONLY_ONE_ID"};
			 for(int i=0;i<string.length;i++){
				 map2.put(string[i],map[i]);
			 }
			// String sql="update proj_contrast_cus set  PROJ_STAUTS="+"\""+cString+"\""+" where only_one_id="+"\""+id+"\"";
			 BaseDao.getBaseDao().update("proj_contrast_cus".toUpperCase(),map2, new SqlWhereEntity().putWhere("ONLY_ONE_ID","\""+id+"\"",WhereEnum.EQUAL_INT));			 
		 }
		 
		   
		   
	//}
	}
	
	
	
	
	
	@RequestMapping(params = "cmd=contrask_message_no", method = RequestMethod.POST)
	public void contrask_message_no(HttpServletRequest request,HttpServletResponse response,String id,String []map) throws IOException{
		//List<Map<String,Object>> lMaps=new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> lMapses=new ArrayList<Map<String,Object>>();
		JSONObject jsonObject=new JSONObject();
		String sqlString2="select *  from proj_contrast_cus where only_one_id="+"\""+id+"\"";
		    
		lMapses=BaseDao.getBaseDao().query(sqlString2,"");
		
		jsonObject.put("PROJ_STAUTS",lMapses);
	    response.getWriter().println(jsonObject);	
        response.getWriter().close();	
	}
	
	
	@RequestMapping(params = "cmd=find_book_id", method = RequestMethod.POST)
	public void find_book_id(HttpServletRequest request,HttpServletResponse response,String id) throws IOException{
		List<Map<String,Object>> lMaps=new ArrayList<Map<String,Object>>();	
		String sqlString="select * from  proj_proposal where PROJ_SOURCE_ID="+"\""+id+"\"";
		JSONObject jsonObject=new JSONObject();
		
		
		lMaps=BaseDao.getBaseDao().query(sqlString,"");
		
		if(lMaps.size()<=0){
			String string="{CUS_LEVEL:0}";
			response.getWriter().println(JSONObject.fromObject(string));	
		    response.getWriter().close();	
		}
		else{
			jsonObject.put("proj_proposal",lMaps);
			response.getWriter().println(jsonObject);	
		    response.getWriter().close();	
		}
		
	}
	
	@RequestMapping(params = "cmd=find_book_id_ability", method = RequestMethod.POST)
	public void find_book_id_ability(HttpServletRequest request,HttpServletResponse response,String id) throws IOException{
		List<Map<String,Object>> lMaps=new ArrayList<Map<String,Object>>();	
		String sqlString="select * from  proj_proposal where PROJ_SOURCE_ID="+"\""+id+"\"";
		JSONObject jsonObject=new JSONObject();
		
		
		lMaps=BaseDao.getBaseDao().query(sqlString,"");
		
		if(lMaps.size()<=0){
			String string="{CUS_LEVEL:0}";
			response.getWriter().println(JSONObject.fromObject(string));	
		    response.getWriter().close();	
		}
		else{
			jsonObject.put("proj_proposal",lMaps);
			response.getWriter().println(jsonObject);	
		    response.getWriter().close();	
		}
		
	}
	
	
	
	
	
	@RequestMapping(params = "cmd=find_proj_stauts_id", method = RequestMethod.POST)
	public void find_proj_stauts_id(HttpServletRequest request,HttpServletResponse response,String id) throws IOException{
		List<Map<String,Object>> lMaps=new ArrayList<Map<String,Object>>();		
		String sqlString="select BILL_STATUS from  proj_requirement where proj_code="+"\""+id+"\"";
		JSONObject jsonObject=new JSONObject();
		lMaps=BaseDao.getBaseDao().query(sqlString,"");		
		if(lMaps.size()<=0){
			String string="{CUS_LEVEL:0}";
			response.getWriter().println(JSONObject.fromObject(string));	
		    response.getWriter().close();	
		}
		else{
			jsonObject.put("proj_proposal",lMaps);
			response.getWriter().println(jsonObject);	
		    response.getWriter().close();	
		}
		
	}

	@RequestMapping(params = "cmd=find_proj_info")
	public void find_proj_info(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Integer i = 0;
		String parentStartDate = "";
		String childrenStartDate = "";
		Date startTime = null;
		Date endTime = null;
		double DifferDays = 0;
		double speed = 0;
		BigDecimal wbs_working_hours = new BigDecimal(0);
		BigDecimal number = new BigDecimal(0);
		List<Map<String,Object>> maps=new ArrayList<Map<String,Object>>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		String sql = "SELECT a.id,a.proj_name,a.MANAGER,a.project_statues,"
				+ "a.RELEASED_DATE,a.OPPORTUNITY_CODE,a.proj_construction_cost,a.BILL_STATUS "
				+ "FROM proj_released a ";
		List<Map<String, Object>> parentList = BaseDao.getBaseDao().query(sql,"");
		for(Map<String, Object> parentMap : parentList){
			i++;
			Map<String,Object> pmap = new HashMap<String, Object>();
			pmap.put("id", i);
			pmap.put("name", parentMap.get("PROJ_NAME"));
			pmap.put("code", parentMap.get("MANAGER"));
			if(parentMap.get("PROJECT_STATUES") != null){
				if(parentMap.get("PROJECT_STATUES").equals("1")){
					pmap.put("project_statues", "正常");
					pmap.put("color","rgba(0,0,0,.4)");
				}else if(parentMap.get("PROJECT_STATUES").equals("2")){
					pmap.put("project_statues", "预警");
					pmap.put("color","rgb(255,255,0)");
				}else if(parentMap.get("PROJECT_STATUES").equals("3")){
					pmap.put("project_statues", "延期");
					pmap.put("color","rgb(255,0,0)");
				}else if(parentMap.get("PROJECT_STATUES").equals("4")){
					pmap.put("project_statues", "挂起");
					pmap.put("color","rgb(190,190,190)");
				}else{
					pmap.put("project_statues", "完成");
					pmap.put("color","rgba(0,0,0,0)");
				}
			}else{
				pmap.put("project_statues", parentMap.get("PROJECT_STATUES"));
				pmap.put("color","rgba(0,0,0,.4)");
			}
			pmap.put("milestone", "");
			if(parentMap.get("RELEASED_DATE") != null){
				parentStartDate = parentMap.get("RELEASED_DATE").toString();
				pmap.put("start", sdf1.parse(sdf1.format(sdf.parse(parentStartDate))).getTime());
			}
			pmap.put("opportunity_code", parentMap.get("OPPORTUNITY_CODE"));
			pmap.put("proj_construction_cost", parentMap.get("PROJ_CONSTRUCTION_COST"));
			pmap.put("bill_status", parentMap.get("BILL_STATUS"));
			if(parentMap.get("RELEASED_DATE")!= null){
				pmap.put("released_date", sdf.format(parentMap.get("RELEASED_DATE")));
			}
			if(parentMap.get("ID")!= null){
				pmap.put("system_id", String.valueOf(parentMap.get("ID")));
			}
			pmap.put("level","0");
			pmap.put("status","STATUS_ACTIVE");
			pmap.put("collapsed",true);
			pmap.put("canAdd",false);
			pmap.put("canDelete",false);
			pmap.put("canAddIssue",false);
			pmap.put("hasChild",true);
			String sql1 = "SELECT a.MILESTONE,a.START_DATE,b.wbs_working_hours,b.wbs_bill_state,c.DATA_VALUE "
					+ " FROM proj_progress_phase a "
					+ " LEFT JOIN proj_wbsinfo b on a.id = b.proj_phase_id "
					+ " LEFT JOIN (SELECT DATA_KEY,DATA_VALUE FROM rm_code_data WHERE code_type_id = "
					+ " (SELECT id FROM rm_code_type WHERE type_keyword = 'milestone')) c ON c.DATA_KEY = a.MILESTONE"
					+ " WHERE a.PROJ_SOURCE_ID = "+parentMap.get("ID")+" ;";
			List<Map<String,Object>> childrenList = BaseDao.getBaseDao().query(sql1,"");
			if(childrenList.size() > 0){
				for(Map<String,Object> childrenMap : childrenList){
					if(childrenMap.get("WBS_WORKING_HOURS") !=null){
						wbs_working_hours = new BigDecimal(childrenMap.get("WBS_WORKING_HOURS").toString());
						number = wbs_working_hours.add(number);
					}
				}
					
			}
			pmap.put("duration", number);
			if(parentStartDate.equals("")){
				startTime = new Date();
			}else{
				startTime = sdf2.parse(parentStartDate);
			}
			endTime = sdf2.parse(sdf.format(new Date()));
			DifferDays = calLeaveDays(startTime, endTime);
			if(DifferDays == number.doubleValue() || DifferDays > number.doubleValue()){
				pmap.put("project_speed", "100%");
				pmap.put("progress", "100");
			}else{
				speed = Math.round(DifferDays/number.doubleValue()*100);
				pmap.put("project_speed", speed+"%");
				pmap.put("progress", speed);
			}
			number = new BigDecimal(0);
			maps.add(pmap);
			if(childrenList.size() > 0){
				for(Map<String,Object> childrenMap : childrenList){
					i++;
					Map<String,Object> cmap = new HashMap<String, Object>();
					cmap.put("id", i);
					cmap.put("name", parentMap.get("PROJ_NAME"));
					cmap.put("code", parentMap.get("MANAGER"));
					cmap.put("milestone", childrenMap.get("DATA_VALUE"));
					if(childrenMap.get("WBS_BILL_STATE")!= null){
						if(childrenMap.get("WBS_BILL_STATE").equals("1")){
							cmap.put("project_statues", "正常");	
							cmap.put("color","rgba(0,0,0,.4)");
						}else if(childrenMap.get("WBS_BILL_STATE").equals("2")){
							cmap.put("project_statues", "预警");
							cmap.put("color","rgb(255,255,0)");
						}else if(childrenMap.get("WBS_BILL_STATE").equals("3")){
							cmap.put("project_statues", "延期");
							cmap.put("color","rgb(255,0,0)");
						}else if(childrenMap.get("WBS_BILL_STATE").equals("4")){
							cmap.put("project_statues", "挂起");
							cmap.put("color","rgb(190,190,190)");
						}else{
							cmap.put("project_statues", "完成");
							cmap.put("color","rgba(0,0,0,0)");
						}
					}else{
						cmap.put("project_statues", childrenMap.get("WBS_BILL_STATE"));
						cmap.put("color","rgba(0,0,0,.4)");
					}
					if(childrenMap.get("WBS_WORKING_HOURS") != null){
						wbs_working_hours = new BigDecimal(childrenMap.get("WBS_WORKING_HOURS").toString());
					}
					cmap.put("duration", childrenMap.get("WBS_WORKING_HOURS"));
					if(childrenMap.get("START_DATE") != null){
						childrenStartDate = childrenMap.get("START_DATE").toString();
						cmap.put("start", sdf1.parse(sdf1.format(sdf.parse(childrenStartDate))).getTime());
					}
					if(childrenStartDate.equals("")){
						startTime = new Date();
					}else{
						startTime = sdf2.parse(childrenStartDate);
					}
					endTime = sdf2.parse(sdf.format(new Date()));
					DifferDays = calLeaveDays(startTime, endTime);
					if(DifferDays == wbs_working_hours.doubleValue() || DifferDays > wbs_working_hours.doubleValue()){
						cmap.put("project_speed", "100%");
						cmap.put("progress", "100");
					}else{
						speed = Math.round(DifferDays/wbs_working_hours.doubleValue()*100);
						cmap.put("project_speed", speed+"%");
						cmap.put("progress", speed);
					}
					cmap.put("opportunity_code", parentMap.get("OPPORTUNITY_CODE"));
					cmap.put("proj_construction_cost", parentMap.get("PROJ_CONSTRUCTION_COST"));
					cmap.put("bill_status", parentMap.get("BILL_STATUS"));
					if(parentMap.get("RELEASED_DATE")!= null){
						pmap.put("released_date", sdf.format(parentMap.get("RELEASED_DATE")));
					}
					if(parentMap.get("ID")!= null){
						pmap.put("system_id", String.valueOf(parentMap.get("ID")));
					}
					cmap.put("level","1");
					cmap.put("status","STATUS_ACTIVE");
					cmap.put("canAdd",false);
					cmap.put("canDelete",false);
					cmap.put("canAddIssue",false);
					cmap.put("collapsed",true);
					cmap.put("hasChild",false);
					maps.add(cmap);
				}
			}
		}
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("tasks", JsonUtils.getJSONString(maps));
		System.err.println(jsonObject);
		response.getWriter().println(jsonObject);	
		response.getWriter().close();
	}
	
	
	//能力立项
			@RequestMapping(params = "cmd=insert_ability_id")
			public void insert_ability_id(HttpServletRequest request,HttpServletResponse response,String id) throws Exception {
			 List<Map<String,Object>> lMaps=new ArrayList<Map<String,Object>>();
			 List<Map<String,Object>> lMapses=new ArrayList<Map<String,Object>>();
				
			 //项目状态
			 String cString=request.getParameter("con_status");
			 System.err.println(cString);
			  if(!cString.equals("null") && cString != null){
				String sqlString1="update "+"PROJ_RELEASED".toLowerCase()+" set project_statues="+"\""+cString+"\""+" where PROJ_CODE="+"\""+id+"\"";
				BaseDao.getBaseDao().update(sqlString1);
	            
				
				
				String stringss="{CUS_LEVEL:0}";
				response.getWriter().println(JSONObject.fromObject(stringss));	
			    response.getWriter().close();	
			    
//				if(lMaps.size()<=0){
//					Map<String,Object> map=new HashMap<String, Object>();
//					String string[]={"PROJ_STAUTS","PROJ_SPEED","PROJ_IN_STAUTS","PROJ_OUT_STAUTS","PROJ_NOW_PRICE","ONLY_ONE_ID"};
//					
//					for(int i=0;i<string.length;i++){
//						
//						if(i==0){
//						  map.put(string[i],cString);
//						}
//					     else if(i==5){
//						map.put(string[i],id);
//						}else{
//						map.put(string[i],"");
//					  }
//					}
//					BaseDao.getBaseDao().insert("proj_contrast_cus".toUpperCase(),map);	
//				}  
//				else{
//					String sql="update proj_contrast_cus set  PROJ_STAUTS="+"\""+cString+"\""+" where only_one_id="+"\""+id+"\"";
//					BaseDao.getBaseDao().update(sql.toLowerCase());
//				}
//				    String sqlString2="select  "+"PROJ_STAUTS".toLowerCase()+"  from proj_contrast_cus where only_one_id="+"\""+id+"\"";
//				    
//				    lMapses=BaseDao.getBaseDao().query(sqlString,"");
//				    
//				    JSONObject jsonObject=new JSONObject();
//				    jsonObject.put("PROJ_STAUTS",lMapses);
//				
//				String string="{CUS_LEVEL:0}";
//				response.getWriter().println(jsonObject);	
//			    response.getWriter().close();	
			 }
			
			}
	
			@RequestMapping(params = "cmd=insert_ability_ids")
			public void insert_ability_ids(HttpServletRequest request,HttpServletResponse response,String id) throws Exception {
			 //项目内部状态
			 String conString=request.getParameter("con_in_status");	
			 if(!conString.equals("null") && conString != null ){
				String sqlString2="update "+"PROJ_RELEASED".toLowerCase()+" set project_stage="+"\""+conString+"\""+" where PROJ_CODE="+"\""+id+"\"";
				BaseDao.getBaseDao().update(sqlString2);
				String string="{CUS_LEVEL:0}";
				response.getWriter().println(JSONObject.fromObject(string));	
			    response.getWriter().close();	
			 }
			}
			
			@RequestMapping(params = "cmd=find_buss_id_ability")
			public void find_buss_id_ability(HttpServletRequest request,HttpServletResponse response,String id) throws Exception {
				List<Map<String,Object>> lMaps=new ArrayList<Map<String,Object>>();
				JSONArray jArray=new JSONArray();
				Map<String,Object> map=new HashMap<String, Object>();
				String sqlString="select * from  PROJ_RELEASED , bus_contract_admin , bus_send_assessment  where  PROJ_CODE=pro_id  and business_id=bus_oppo_id and PROJ_CODE="+"\""+id+"\"";
				lMaps=BaseDao.getBaseDao().query(sqlString,"");
				if(lMaps.size()<=0){
				String string="{项目状态:0}";
				response.getWriter().println(JSONObject.fromObject(string));	
			    response.getWriter().close();	
				}else{
			    jArray.add(lMaps);
				//System.err.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^=="+jArray.get(0));
			    response.getWriter().println(jArray.get(0));
			    response.getWriter().close();
				}
			}

			//获取项目建议书状态
			@RequestMapping(value = "selectStatus",method = RequestMethod.GET)
			public String selectStatus(HttpServletRequest request,HttpServletResponse response) throws Exception {
				String id = request.getParameter("id");
				String sqlString = "select BILL_STATUS from proj_proposal where PROJ_SOURCE_ID="+id;
				List<Map<String, Object>> list = BaseDao.getBaseDao().query(sqlString, "");
				JSONArray array =new JSONArray();
				for(Map<String, Object> dataMap:list){
					array.add(dataMap);
				}
				return JSON.toJSONString(array);
			}

			//发送邮件
			@RequestMapping(params = "cmd=send_mail_message", method = RequestMethod.POST)
			public void send_mail_message(HttpServletRequest request,HttpServletResponse response,String []map) throws IOException{
				MailHelper mail=new MailHelper();
				mail.init();
				String title="【"+map[0]+"】_项目编号:"+map[1]+"_项目经理:"+map[2];
				String mailText="您好！项目编号"+map[1]+"已完成项目立项,指定项目经理:"+map[2]+",请登录业务管理系统查阅详情或处理业务代办。--【业务管理系统通知】 ";
				String to=map[3];			
				mail.sendMail("A",title, mailText, to);
                String string="{cg:0}";
                response.getWriter().print(JSONObject.fromObject(string));
                response.getWriter().flush();
                response.getWriter().close();
				
			}
			
			//项目概况 
			@RequestMapping(params = "cmd=pro_message")
			public void pro_message(HttpServletRequest request,HttpServletResponse response,String id) throws Exception {
			 //项目外部状态
			 String outString=request.getParameter("pro_talk");	
			 if(!outString.equals("null") && outString != null ){
				String sqlString3="update "+"PROJ_RELEASED".toLowerCase()+"  set  proj_talk_message="+"\""+outString+"\""+"  where OPPORTUNITY_CODE="+"\""+id+"\"";
				BaseDao.getBaseDao().update(sqlString3);
				String string="{CUS_LEVEL:0}";
				response.getWriter().println(JSONObject.fromObject(string));	
			    response.getWriter().close();	
			 }
			}
			
			@RequestMapping(params = "cmd=pro_messages")
			public void pro_messages(HttpServletRequest request,HttpServletResponse response,String id) throws Exception {

			 String conString=request.getParameter("pro_talk");	
			 if(!conString.equals("null") && conString != null ){
				String sqlString2="update "+"PROJ_RELEASED".toLowerCase()+" set proj_talk_message="+"\""+conString+"\""+" where PROJ_CODE="+"\""+id+"\"";
				BaseDao.getBaseDao().update(sqlString2);
				String string="{CUS_LEVEL:0}";
				response.getWriter().println(JSONObject.fromObject(string));	
			    response.getWriter().close();	
			 }
			}
			
			private void notityBusFlow(String code,String id,Map<String,Object> dataMap,HttpServletRequest request){
				Map<String,Object> notityMap =new HashMap<String,Object>();
				switch(code){
				case "PROJ_REQUIREMENT_ZDJL":
					notityMap =dcmsDAO.find(new SqlTableUtil("proj_requirement", "").appendSelFiled("*").appendWhereAnd(" id ='"+id+"'"));
					notityMap.put("BUS_ID", notityMap.get("ID"));
					break;
				default :
					notityMap=dataMap;
					break;
				}
				this.handleTask(IBusFlowOperationType.HANDLE_TASK, code, notityMap, request);
			}
			
			
			@RequestMapping(params = "cmd=add_all_message", method = RequestMethod.POST)
			public void add_all_message(HttpServletRequest request,HttpServletResponse response,String id) throws IOException{
				  IBaseDao iBaseDao=BaseDao.getBaseDao();
				
				
				  iBaseDao.delete("PROJ_WBSINFO", new SqlWhereEntity().putWhere("proj_source_id", id, WhereEnum.EQUAL_INT));
								
				  List<Map<String, Object>> lMaps=new ArrayList<Map<String,Object>>();
				  Map<String,Object> map=new HashMap<String, Object>();
				  String jsonString=request.getParameter("message");
				  JSONObject jsonObject=new JSONObject().fromObject(jsonString);
				  //JSONObject jsonObject1=new  JSONObject().fromObject(jsonObject.get("message").toString());
				  String jsString=jsonObject.get("tasks").toString();
				  net.sf.json.JSONArray jsonArray=new net.sf.json.JSONArray().fromObject(jsString);
				  
				  
				  for(int i=0;i<jsonArray.size();i++){
					  JSONObject jsons=jsonArray.getJSONObject(i);
					  Iterator iter = jsons.keys();
					  while(iter.hasNext()){
						  String  value="";
						  String  name=(String) iter.next();
						  
						  try {
							   if(name.equals("WBS_START_DATE") || name.equals("WBS_EXPECTED_END_DATE")){	  
								value=jsons.getString(name);
								
								if(value.indexOf("/")!= -1){
									 Date d1 = new SimpleDateFormat("MM/dd/yyyy").parse(value);
								      
									 SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");		
									
									 value=sdf1.format(d1);
								}
								else{
//								Long dateLong = Long.valueOf(value);
//							    String time = new SimpleDateFormat("MM/dd/yyyy").format(new Date(dateLong));
//								
//								Date d1 = new SimpleDateFormat("MM/dd/yyyy").parse(time);
//								SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");								
//								value=sdf1.format(d1);
								 Long dateLong = Long.valueOf(value);
								 String time = new SimpleDateFormat("MM/dd/yyyy").format(new Date(dateLong));
									
							     Date d1 = new SimpleDateFormat("MM/dd/yyyy").parse(time);
							     SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");								
								 value=sdf1.format(d1);
							   }
							  }
							   
							     else if(name.equals("WBS_COMPLETION_TIME")){
									  value=jsons.getString(name);
									  if(value.equals("") || value==null){
										  continue;
									  }
									  
									  //else{
										  
//										    Long dateLong = Long.valueOf(value);
//										    String time = new SimpleDateFormat("MM/dd/yyyy").format(new Date(dateLong));
//											
//											Date d1 = new SimpleDateFormat("MM/dd/yyyy").parse(time);
//											SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");								
//											value=sdf1.format(d1);	  
										  
										  
										  
									    Date d1 = new SimpleDateFormat("MM/dd/yyyy").parse(value);
								      
									    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");								
								        
									   
								      
									 // }
									  value=sdf1.format(d1);
								  }
							     else if(name.equals("PROJ_PHASE_ID")){
							    	 value=jsons.getString(name);
							    	 if(value.equals("")){
							    		 value="1";
							    	 }
							     }
							     else if(name.equals("WBS_THOSE_RESPONSIBLE")){
							    	 value=jsons.getString(name);
							    	 if(value.equals("") || value==null){
							    		 value="0";
							    	 }
							     }
							      
								  else{
									    value=jsons.getString(name);
								  }
								  
							   map.put(name, value);
							  
								
							  } catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}						 
  					  }
					  
					   //lMaps.add(map);
					   iBaseDao.insert("PROJ_WBSINFO",map);
					   map=new HashMap<String, Object>();
					   map.clear();
				  }
				  
				  
				    //System.err.println(lMaps);
				    //iBaseDao.insert("PROJ_WBSINFO",lMaps);
				  
				    String fString1="{项目状态:0}";
					response.getWriter().println(JSONObject.fromObject(fString1));
					response.getWriter().flush();
				    response.getWriter().close();
				  
			}
			

			@RequestMapping(params = "cmd=add_wbs_massage")
			public void add_wbs_massage(HttpServletRequest request,HttpServletResponse response,String []map,String id) throws Exception {
				String del=request.getParameter("is");
				if("0".equals(del)){
					if("undefined".equals(id)){
						return ;
					}
					BaseDao.getBaseDao().delete("proj_wbsinfo".toUpperCase(), new SqlWhereEntity().putWhere("ID",id,WhereEnum.EQUAL_INT));
					//BaseDao.getBaseDao().delete("proj_risk_management".toUpperCase(),);
					
					String string1="{项目状态:0}";
					response.getWriter().println(JSONObject.fromObject(string1));	
				    response.getWriter().close();
				}
				else{
				Map<String,Object>map2=new HashMap<String, Object>();
				String string[]={"WBS_NAME","TASK_REMARK","WBS_START_DATE","WBS_EXPECTED_END_DATE","WBS_COMPLETION_TIME","WBS_WORKING_HOURS","WBS_THOSE_RESPONSIBLE","WBS_BILL_STATE","proj_source_id"};
				//String string[]={"WBS_NAME","TASK_REMARK","WBS_START_DATE","WBS_EXPECTED_END_DATE","WBS_COMPLETION_TIME","WBS_WORKING_HOURS","WBS_THOSE_RESPONSIBLE","WBS_BILL_STATE","proj_source_id","LEVEL"};
				if("".equals(map[4])){
					Date d1 = new SimpleDateFormat("MM/dd/yyyy").parse(map[2]);
					//Date d2 = new SimpleDateFormat("MM/dd/yyyy").parse(map[3]);
					Date d3 = new SimpleDateFormat("MM/dd/yyyy").parse(map[3]);
					SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd");
					SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
					SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
					map[2]=sdf0.format(d1);
					map[4]=null;
					map[3]=sdf2.format(d3);
					
					if(id==""){
				    	for(int i=0;i<map.length;i++){
				    		
//				    		if("LEVEL".equals(string[i])){		    			
//				    			map2.put("LEVEL",1);
//				    		}
				    		
							map2.put(string[i],(Object)map[i]);
						}
						BaseDao.getBaseDao().insert("proj_wbsinfo".toUpperCase(),map2);
						String string1="{项目状态:0}";
						response.getWriter().println(JSONObject.fromObject(string1));	
					    response.getWriter().close();
				    }
				    else{
				    	String sqlString="update proj_wbsinfo set WBS_NAME="+"\""+map[0]+"\""+", TASK_REMARK="+"\""+map[1]+"\""+", WBS_START_DATE="+"\""+map[2]+"\""+", WBS_EXPECTED_END_DATE="+"\""+map[3]+"\""+", WBS_COMPLETION_TIME="+map[4]+", WBS_WORKING_HOURS="+Integer.parseInt(map[5])+", WBS_THOSE_RESPONSIBLE="+"\""+map[6]+"\""+", WBS_BILL_STATE="+map[7]+" where id="+id;
				    	BaseDao.getBaseDao().update(sqlString);
				    	
				    	String string1="{项目状态:0}";
						response.getWriter().println(JSONObject.fromObject(string1));	
					    response.getWriter().close();
				    }
				}else{
					Date d1 = new SimpleDateFormat("MM/dd/yyyy").parse(map[2]);
					Date d2 = new SimpleDateFormat("MM/dd/yyyy").parse(map[3]);
					Date d3 = new SimpleDateFormat("MM/dd/yyyy").parse(map[4]);
					SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd");
					SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
					SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
					map[2]=sdf0.format(d1);
					map[3]=sdf1.format(d2);
					map[4]=sdf2.format(d3);
					
					if(id==""){
				    	for(int i=0;i<map.length;i++){
							map2.put(string[i],(Object)map[i]);
						}
						BaseDao.getBaseDao().insert("proj_wbsinfo".toUpperCase(),map2);
						String string1="{项目状态:0}";
						response.getWriter().println(JSONObject.fromObject(string1));	
					    response.getWriter().close();
				    }
				    else{
				    	String sqlString="update proj_wbsinfo set WBS_NAME="+"\""+map[0]+"\""+", TASK_REMARK="+"\""+map[1]+"\""+", WBS_START_DATE="+"\""+map[2]+"\""+", WBS_EXPECTED_END_DATE="+"\""+map[3]+"\""+", WBS_COMPLETION_TIME="+"\""+map[4]+"\""+", WBS_WORKING_HOURS="+Integer.parseInt(map[5])+", WBS_THOSE_RESPONSIBLE="+"\""+map[6]+"\""+", WBS_BILL_STATE="+map[7]+" where id="+id;
				    	BaseDao.getBaseDao().update(sqlString);
				    	
				    	String string1="{项目状态:0}";
						response.getWriter().println(JSONObject.fromObject(string1));	
					    response.getWriter().close();
				    }
				   }
				    
				
				  }
			     }
	
			
			
			//看板历史版本
			@RequestMapping(params = "cmd=save_pro_history", method = RequestMethod.POST)
			public void save_pro_history(HttpServletRequest request,HttpServletResponse response,String map[]) throws IOException{
				 IBaseDao iBaseDao = BaseDao.getBaseDao();  
				 Date aDate=new Date();
		    	 SimpleDateFormat s1=new SimpleDateFormat("yyyyMMddHHmmss");
				 
		    	 String idString=s1.format(aDate);
		    	 
			     String sureid=request.getParameter("sureid");
			     
			     String[] weekString=new  String[map.length];
			     
			     String widString="";
			     
			     if(sureid.equals("2")){
			          String[] weekString2={"ID","BUSINESS_NUM","PRO_NAME","PRO_MANAGER","WANT_CONCENT_MESSAGE","PRO_BOOK_MESSAGE","PRO_TIME","PRO_STATUS","PRO_COM","PRO_IN_STATUS","WILL_COST_NOW","PRO_BUILD_COST","PRO_BUILD_COST_COM","PRO_BUILD_COST_PRI","PROJ_TALK_MESSAGE","PIDS"};
			          for(int i=0;i<weekString.length;i++){		           
			        	  weekString[i]=weekString2[i];
		              } 
			         
			          widString=map[15];
			          
			     }else{
			    	 
			    	  String[] weekString1={"ID","BUSINESS_NUM","PRO_NAME","PRO_MANAGER","WANT_CONCENT_MESSAGE","PRO_BOOK_MESSAGE","PRO_TIME","PRO_STATUS","PRO_COM","PRO_IN_STATUS","PRO_OUT_STATUS","WILL_COST_NOW","BEF_BUILD_COST","BEF_BUILD_COST_COM","BEF_BUILD_COST_PRI","PRO_BUILD_COST","PRO_BUILD_COST_COM","PRO_BUILD_COST_PRI","PROJ_TALK_MESSAGE","MTIME","OPEX","CAPEX"};   
		              for(int i=0;i<weekString.length;i++){
		            	  weekString[i]=weekString1[i];
		              }
		              
		              widString=map[0];
			    	 
			     }
			     
				       			    	   
				
				 String  tableEntity[]={"proj_wbsinfo","proj_risk_management"};
				 
				 String  sql="select * from %s where proj_source_id="+widString;
				 
				 List<Map<String,Object>>lMaList=new ArrayList<Map<String,Object>>();
				 Map<String,Object>lMap=new HashMap<String, Object>();
				 
				 ButForInsert bInsert=new ButForInsert();
				 
				 for(int i=0;i<map.length;i++){
					 if(i==0){continue;}
					 lMap.put(weekString[i],map[i]);
				 }
				 
				 lMap.put("ID",idString);
				 lMap.remove("PIDS");
				 
				 iBaseDao.insert("LOOK_BACK_MESSAGE",bInsert.No_Common(lMap, request));
				 
				 for(int j=0;j<tableEntity.length;j++){
					 lMaList=iBaseDao.query(String.format(sql,tableEntity[j]),"");
					 
					 if(lMaList.size()<=0){continue;}
					 
					 for(Map<String, Object>map2:lMaList){
						 if(map2.containsKey("PROJ_PHASE_ID")){
							 map2.put("PROJ_PHASE_ID",idString);
							 //map2.put("PROJ_RELEASED_ID",widString);
							 map2.put("VERSION_HISTORY",idString);
							 
							 
							 map2.remove("ID");
							 map2.remove("NOW_TIME");
							 map2.remove("PARENT_ID");
							 map2.remove("RMRN");
							 
							 //map2.remove("PARENT_ID");
						 }
						 else if(map2.containsKey("VERSION")){
							 map2.put("VERSION",idString);
							 //map2.put("PROJ_RELEASED_ID",widString);
							
							 map2.remove("ID");
							 map2.remove("RMRN");
						 }
						 
					 }
					 iBaseDao.insert(tableEntity[j].toUpperCase()+"_HISTORY",lMaList);
					 lMaList=new ArrayList<Map<String,Object>>();
					 lMaList.clear();
				 }
				     save_history("LOOK_BACK_MESSAGE",idString,widString);    
				 
				     String aString="{a:0}";
		    	     response.getWriter().print(JSONObject.fromObject(aString));
		    	     response.getWriter().flush();
		    	     response.getWriter().close();
				 			
			}
			
			//周报历史版本展示
			@RequestMapping(params = "cmd=showp_history", method = RequestMethod.POST)
			public void showp_history(HttpServletRequest request,HttpServletResponse response,String id) throws IOException{
				    IBaseDao iDao=BaseDao.getBaseDao();
				    String sql="select * from look_back_message where id="+id; 
				    List<Map<String,Object>> lMaps=new ArrayList<Map<String,Object>>();
				    lMaps=iDao.query(sql,"");
				    if(lMaps.size()<=0){
				    	 
				    	 String aString="{a:0}";
			    	     response.getWriter().print(JSONObject.fromObject(aString));
			    	     response.getWriter().flush();
			    	     response.getWriter().close();
				    }
				    else{
				    	net.sf.json.JSONArray jArray=new net.sf.json.JSONArray().fromObject(lMaps);
				    	 response.getWriter().print(jArray);
			    	     response.getWriter().flush();
			    	     response.getWriter().close();
				    }
				
			}
			
			
			public void save_history(String table,String id,String mainid) throws IOException{
			      ProjectController.proj_edition pEdition=new ProjectController().new proj_edition();
			      
			      pEdition.ProjEditionMessage(table, id,mainid);
		}
			
			//项目文件历史版本
			@RequestMapping(params = "cmd=save_history", method = RequestMethod.POST)
			public void save_history(String table,String id) throws IOException{
				      ProjectController.proj_edition pEdition=new ProjectController().new proj_edition();
				      pEdition.ProjEditionMessage(table, id,"");
			}
			@RequestMapping(params = "cmd=save_history_nl", method = RequestMethod.POST)
			public void save_history_nl(String table,String id,String mainid) throws IOException{
				      ProjectController.proj_edition pEdition=new ProjectController().new proj_edition();
				      pEdition.ProjEditionMessage(table, id,mainid);
			}
			//历史版本显示   yid 类似于商机编码  modename 模块儿类型  例：前评估    （仅限于直接显示最新版本信息）
			@RequestMapping(params = "cmd=all_show_history", method = RequestMethod.POST)
			public void all_show_history(HttpServletRequest request,HttpServletResponse response,String id,String yid,String modename) throws IOException{
				  IBaseDao iBaseDao = BaseDao.getBaseDao();   
				  String sqlString="";
				  String pd=request.getParameter("pdata");
				  
				  if(pd.equals("1")){
					sqlString="select * from proj_edition where id in (select MAX(id) from proj_edition where (main_id="+id+" or main_id=0) and y_id="+"'"+yid+"'"+ " and  mode_type="+"'"+modename+"'"+")"; 
				  }
				  else{
				    sqlString="select * from proj_edition where (main_id="+id +" or main_id=0) and (y_id='0' or y_id="+"'"+yid+"'"+") and (mode_type='0' or mode_type="+"'"+modename+"'"+")     order by s_id ,now_time asc";
				  }
				  //select * from proj_edition where id in (select MIN(id) from proj_edition GROUP BY edition_name) order by s_id 
				  String sqlStringM="select * from proj_edition where id in (select MIN(id) from proj_edition GROUP BY edition_name) order by s_id "; 
				  //查询类型的数量
				  String numberString="select  edition_name,COUNT(edition_name) as ems from proj_edition where (main_id="+id +" or main_id=0) and (y_id='0' or y_id="+"'"+yid+"'"+") and (mode_type='0' or mode_type="+"'"+modename+"'"+")   GROUP BY (edition_name)  order by s_id "; 
				  
				  List<Map<String, Object>>lMaps=new ArrayList<Map<String,Object>>();
				  List<Map<String, Object>>lMapses=new ArrayList<Map<String,Object>>();
				  List<Map<String, Object>>lMapsesM=new ArrayList<Map<String,Object>>();
				  lMaps=iBaseDao.query(sqlString,"");
				  lMapses=iBaseDao.query(numberString,""); 
				  lMapsesM=iBaseDao.query(sqlStringM,"");
			      if(lMaps.size()<=0){
			    	  JSONObject jObject=new JSONObject();
				    	
				    	 jObject.put("a","0");
			    	     response.getWriter().print(jObject);
			    	     response.getWriter().flush();
			    	     response.getWriter().close();
			      }
			      else{
			    	  
			    	  Map<String,List<Map<String,List<String>>>>mmpMap=new HashMap<String, List<Map<String,List<String>>>>();
			    	 
			    	  List<Map<String,List<String>>>list=new ArrayList<Map<String,List<String>>>();
			    	  
			    	  Map<String,List<String>> mapl=new HashMap<String, List<String>>();
			    	  
			    	  List<String>lStrings=new ArrayList<String>();
			    	 
			    		  
			    		  for(int j=0;j<lMapses.size();j++){
			    			  
			    			  for(int i=0;i<lMaps.size();i++){ 
			    			     
			    				  if(lMaps.get(i).get("EDITION_NAME").equals(lMapses.get(j).get("EDITION_NAME").toString())){
                                      
			    					  for(int k=0;k<lMapsesM.size();k++){
			    						  
			    						  if(lMapsesM.get(k).get("EDITION_NAME").equals(lMaps.get(i).get("EDITION_NAME").toString())){
			    							  lStrings.add(lMapsesM.get(k).get("SRC").toString());
					    			    	  lStrings.add(lMaps.get(i).get("P_ID").toString());
					    			    	  lStrings.add(lMaps.get(i).get("EDITION").toString());
					    			    	  lStrings.add(lMaps.get(i).get("Y_ID").toString());
					    			    	  lStrings.add(lMaps.get(i).get("MODE_TYPE").toString());
					    			    	  
			    						  }
			    					  }
			    					  
			    				      mapl.put(lMapses.get(j).get("EDITION_NAME").toString(),lStrings);
					    			  list.add(mapl);
					    			  lStrings=new ArrayList<String>();
					    			  lStrings.clear();
					    			  mapl=new HashMap<String, List<String>>();
					    			  mapl.clear();
					    			  
				    		  }
			    			    
			    		  }   
			    			 
			    			  mmpMap.put(lMapses.get(j).get("EDITION_NAME").toString(),list);
			    			  list=new ArrayList<Map<String,List<String>>>();
			    			  list.clear();
			    			  
			    	  }
			    	  
			    	  
			    	  
			    	  
			    	  JSONObject jsObject=new JSONObject();
			    	  net.sf.json.JSONArray jArray=new net.sf.json.JSONArray().fromObject(lMaps);
			          
			    	  jsObject.put("message",jArray);
			          jsObject.put("number",lMapses);
			          jsObject.put("endmessage",mmpMap);
			    	  System.err.println("*****************************************"+jsObject);
			          response.getWriter().print(jsObject);
			          response.getWriter().flush();
			          response.getWriter().close();
			      }
			}
			
			
			//项目文件历史版本显示
			@RequestMapping(params = "cmd=show_history", method = RequestMethod.POST)
			public void show_history(HttpServletRequest request,HttpServletResponse response,String id) throws IOException{
				  IBaseDao iBaseDao = BaseDao.getBaseDao();   
				  String sqlString="select * from proj_edition where main_id="+id +" order by s_id ,now_time asc";
				  
				  //select * from proj_edition where id in (select MIN(id) from proj_edition GROUP BY edition_name) order by s_id 
				  String sqlStringM="select * from proj_edition where id in (select MIN(id) from proj_edition GROUP BY edition_name) order by s_id "; 
				  //查询类型的数量
				  String numberString="select  edition_name,COUNT(edition_name) as ems from proj_edition where main_id="+id+"   GROUP BY (edition_name)  order by s_id "; 
				  
				  List<Map<String, Object>>lMaps=new ArrayList<Map<String,Object>>();
				  List<Map<String, Object>>lMapses=new ArrayList<Map<String,Object>>();
				  List<Map<String, Object>>lMapsesM=new ArrayList<Map<String,Object>>();
				  lMaps=iBaseDao.query(sqlString,"");
				  lMapses=iBaseDao.query(numberString,""); 
				  lMapsesM=iBaseDao.query(sqlStringM,"");
			      if(lMaps.size()<=0){
			    	  JSONObject jObject=new JSONObject();
				    	
				    	 jObject.put("a","0");
			    	     response.getWriter().print(jObject);
			    	     response.getWriter().flush();
			    	     response.getWriter().close();
			      }
			      else{
			    	  
			    	  Map<String,List<Map<String,List<String>>>>mmpMap=new HashMap<String, List<Map<String,List<String>>>>();
			    	 
			    	  List<Map<String,List<String>>>list=new ArrayList<Map<String,List<String>>>();
			    	  
			    	  Map<String,List<String>> mapl=new HashMap<String, List<String>>();
			    	  
			    	  List<String>lStrings=new ArrayList<String>();
			    	 
			    		  
			    		  for(int j=0;j<lMapses.size();j++){
			    			  
			    			  for(int i=0;i<lMaps.size();i++){ 
			    			     
			    				  if(lMaps.get(i).get("EDITION_NAME").equals(lMapses.get(j).get("EDITION_NAME").toString())){
                                      
			    					  for(int k=0;k<lMapsesM.size();k++){
			    						  
			    						  if(lMapsesM.get(k).get("EDITION_NAME").equals(lMaps.get(i).get("EDITION_NAME").toString())){
			    							  lStrings.add(lMapsesM.get(k).get("SRC").toString());
					    			    	  lStrings.add(lMaps.get(i).get("P_ID").toString());
					    			    	  lStrings.add(lMaps.get(i).get("EDITION").toString()); 
			    						  }
			    					  }
			    					  
			    				      mapl.put(lMapses.get(j).get("EDITION_NAME").toString(),lStrings);
					    			  list.add(mapl);
					    			  lStrings=new ArrayList<String>();
					    			  lStrings.clear();
					    			  mapl=new HashMap<String, List<String>>();
					    			  mapl.clear();
					    			  
				    		  }
			    			    
			    		  }   
			    			 
			    			  mmpMap.put(lMapses.get(j).get("EDITION_NAME").toString(),list);
			    			  list=new ArrayList<Map<String,List<String>>>();
			    			  list.clear();
			    			  
			    	  }
			    	  
			    	  
			    	  
			    	  
			    	  JSONObject jsObject=new JSONObject();
			    	  net.sf.json.JSONArray jArray=new net.sf.json.JSONArray().fromObject(lMaps);
			          
			    	  jsObject.put("message",jArray);
			          jsObject.put("number",lMapses);
			          jsObject.put("endmessage",mmpMap);
			    	  System.err.println("*****************************************"+jsObject);
			          response.getWriter().print(jsObject);
			          response.getWriter().flush();
			          response.getWriter().close();
			      }
			}
			
			
			
			
			
	//版本		
	public  class  proj_edition {
	  
	   private  IBaseDao iBaseDao = BaseDao.getBaseDao();	   
       //项目模块                                                  	   	
	   public  void  ProjEditionMessage(String table,String id,String mainid){		   
		   
		   String editionTable="PROJ_EDITION";	
		   String sql="";
		   
		   
		   		//		||table.equals("HAND_BEGINNING"	
		   if(table.equals("CAPACITY_DESIGN") || table.equals("PROJ_PROPOSAL")){			   
			   sql="select * from "+table+" where proj_source_id="+id;
		   }
		   else if(table.equals("PRE_ASSESSMENT")){
			   sql="select * from "+table+" where business_number="+"'"+id+"'";
		   }
		   else{
			   sql="select * from "+table+" where id="+id;
		   }
	      
	       
	       List<Map<String, Object>> lMaps = new ArrayList<Map<String,Object>>();
    	   Map<String,Object> map=new HashMap<String, Object>();
    	   lMaps=iBaseDao.query(sql,"");
    	   
    	   Date aDates=new Date();
   	       SimpleDateFormat st=new SimpleDateFormat("yyyyMMddHHmmss");
    	   
   	       
   	       
   	       String weeidString=lMaps.get(0).get("ID").toString();//周报
   	       
   	       String bnume=weeidString+"V1.";
    	   
    	   for(Map<String,Object>map2:lMaps){
    		   map2.remove("RMRN");
    		   map2.put("ID",st.format(aDates));
    	   }
    	   
	       switch (table) {
		      
	         case "PRE_ASSESSMENT":	//前评估信息
		   
	        	 lMaps.get(0).put("ID",weeidString);
	        	 lMaps.get(0).put("HISTORY_ID",st.format(aDates));
	        	 
	        	 
		    	   iBaseDao.insert(findNoteTables(table.toUpperCase()),lMaps);
		    	   
		    	   //单个版本编号非共享
			       String codeString=SerialNumberUtil.getSerialCode(SerialType.PROJ_BIT_CODE,bnume);
			       //codeString=codeString+".0";
			       //codeString=codeString.replaceAll("Q","");
			       codeString=revalueString(codeString);
			       
			       map.put("EDITION",codeString);
		    	   map.put("P_ID",lMaps.get(0).get("HISTORY_ID"));
		    	   map.put("EDITION_NAME",EditionName(table));
		    	   map.put("MAIN_ID",mainid);
		    	   map.put("Y_ID",lMaps.get(0).get("BUSINESS_NUMBER"));
		    	   map.put("S_ID","1");
		    	   
		    	   iBaseDao.insert(editionTable, map);   //存入版本表   
	           	   
		    	   break; 
	           	   
		      case "PROJ_RELEASED":		//立项下达通知书   			
		    	   
		    	   iBaseDao.insert(findNoteTables(table.toUpperCase()),lMaps);
		    	   
		    	   //bnume=lMaps.get(0).get("")
		    	   //单个版本编号非共享
			       String codeString1=SerialNumberUtil.getSerialCode(SerialType.PROJ_BIT_CODE1,bnume);
			       //codeString1=codeString1+".0";
			       //codeString1=codeString1.replaceAll("L","");
			       codeString1=revalueString(codeString1);
		    	   map.put("EDITION",codeString1);
		    	   map.put("P_ID",lMaps.get(0).get("ID"));
		    	   map.put("EDITION_NAME",EditionName(table));
		    	   map.put("MAIN_ID",mainid);
		    	   //map.put("o", );
		    	   map.put("S_ID","2");
		    	   iBaseDao.insert(editionTable, map);   //存入版本表   	    	  
		    	   break;
		      case "PROJ_PROPOSAL":		//项目建议书	
		    	     String pString[]={"PROJ_ORGANIZATION","PROJ_BUDGET","PROJ_PROGRESS_PHASE","PROJ_WBSINFO","PROJ_RISK_MANAGEMENT","PROJ_OTHERPLAN","PROJ_PROPOSAL_PRODUCT"};
		    	     String sqlString="select * from %s where proj_source_id="+id; 
		    	     List<Map<String, Object>>lMaps2=new ArrayList<Map<String,Object>>();
		    	     iBaseDao.insert(findNoteTables(table.toUpperCase()),lMaps);
		    	     
		    	     //单个版本编号非共享
		    	     String codeString6=SerialNumberUtil.getSerialCode(SerialType.PROJ_BIT_CODE2,bnume);
		    	     for(int i=0;i<pString.length;i++){
		    	    	 lMaps2=iBaseDao.query(String.format(sqlString, pString[i]),"");
		    	    	 if(lMaps2.size()<=0){continue;}	    	    	 
		    	    	 for(int n=0;n<lMaps2.size();n++){
		    	    	 	 lMaps2.get(n).put("V_CODE", st.format(aDates)+revalueString(codeString6));
		    	    	  }
		    	    	 for(Map<String,Object>map3:lMaps2){
		    	    		 map3.remove("RMRN");
		    	    		 map3.put("ID","");
		    	    	 }

		    	    	 iBaseDao.insert(findNoteTables(pString[i]),lMaps2);
		    	    	 lMaps2=new ArrayList<Map<String,Object>>();
		    	    	 lMaps2.clear();
		    	     }
		    	     

		    	   //单个版本编号非共享
				       //codeString6=codeString6+".0";
				       //codeString6=codeString6.replaceAll("X","");
		    	       codeString6=revalueString(codeString6);
				       map.put("EDITION",codeString6);
			    	   map.put("P_ID",lMaps.get(0).get("ID"));
			    	   map.put("EDITION_NAME",EditionName(table));
			    	   map.put("MAIN_ID",id);
			    	   map.put("S_ID","3");
			    	   iBaseDao.insert(editionTable, map);   //存入版本表   	
		    	     
		    	     
		           break;
		      case "PROJECT_BUILD_MODIFY":   //变更记录
		    	   //单个版本编号非共享
			       map.put("EDITION",lMaps.get(0).get("MODIFY_NUMBER"));
		    	   map.put("P_ID",weeidString);
		    	   map.put("EDITION_NAME",EditionName(table));
		    	   map.put("MAIN_ID",lMaps.get(0).get("PROJ_SOURCE_ID"));
		    	   map.put("S_ID","5");
		    	   iBaseDao.insert(editionTable, map);   //存入版本表   			
		           break;
		      case "HAND_BEGINNING":         //内部验收
                   
		    	   String sqlString2="select * from final_inspection where proj_source_id="+mainid;
		    	   List<Map<String,Object>>lMaps3=new ArrayList<Map<String,Object>>();
		    	   lMaps3=iBaseDao.query(sqlString2,"");
		    	   
		    	   if(lMaps3.size()<=0){
		    		   
		    	   }
		    	   else{
		    	     for(Map<String,Object>map4:lMaps3){
		    		    map4.remove("RMRN");
		    		    map4.put("ID",lMaps.get(0).get("ID"));
		    	     }
		    	        String tableString="FINAL_INSPECTION";
		    	        iBaseDao.insert(findNoteTables(tableString),lMaps3);
		    	   }
		    	   
		    	  
		    	   iBaseDao.insert(findNoteTables(table.toUpperCase()),lMaps);
		    	   
		    	   //单个版本编号非共享
			       String codeString3=SerialNumberUtil.getSerialCode(SerialType.PROJ_BIT_CODE4,bnume);
			       //codeString3=codeString3+".0";
			       //codeString3=codeString3.replaceAll("N","");
		    	   codeString3=revalueString(codeString3);
			       
			       map.put("EDITION",codeString3);
		    	   map.put("P_ID",lMaps.get(0).get("ID"));
		    	   map.put("EDITION_NAME",EditionName(table));
		    	   map.put("MAIN_ID",mainid);
		    	   map.put("S_ID","6");
		    	   iBaseDao.insert(editionTable, map);   //存入版本表   
		    	  
		    	  break;
		      case "FINAL_INSPECTION":         //内部验收
                   String numeString="V1.";
		    	   String meidString="";
		    	   String sqlString7="select * from hand_beginning where proj_source_id="+mainid;
		    	   List<Map<String,Object>>lMaps5=new ArrayList<Map<String,Object>>();
		    	   lMaps5=iBaseDao.query(sqlString7,"");
		    	    
		    	   if(lMaps5.size()<=0){
		    		   numeString=id+numeString;
		    	   }
		    	   else{
		    		   meidString=lMaps5.get(0).get("ID").toString();
		    		   numeString=meidString+numeString;
		    	     for(Map<String,Object>map4:lMaps5){
		    		    map4.remove("RMRN");
		    		    map4.put("ID",lMaps.get(0).get("ID"));
		    	     }
		    	        String tableString="HAND_BEGINNING";
		    	        iBaseDao.insert(findNoteTables(tableString),lMaps5);
		    	   }
		    	   
		    	   
		    	   iBaseDao.insert(findNoteTables(table.toUpperCase()),lMaps);
		    	   
		    	   //单个版本编号非共享
			       String codeString10=SerialNumberUtil.getSerialCode(SerialType.PROJ_BIT_CODE4,numeString);
			       //codeString3=codeString3+".0";
			       //codeString3=codeString3.replaceAll("N","");
			       codeString10=revalueString(codeString10);
			       
			       map.put("EDITION",codeString10);
		    	   map.put("P_ID",lMaps.get(0).get("ID"));
		    	   map.put("EDITION_NAME",EditionName("HAND_BEGINNING"));
		    	   map.put("MAIN_ID",mainid);
		    	   map.put("S_ID","6");
		    	   iBaseDao.insert(editionTable, map);   //存入版本表   
		    	  
		    	  break;
		      case "CLIENT_ACCEPTANCE":      //客户验收
		    	     
		    	   //CUSTOMER_SIDE
		    	   
		    	  String sqlString3="select * from customer_side where proj_source_id="+mainid;
		    	   
		    	   List<Map<String,Object>>lMaps4=new ArrayList<Map<String,Object>>();
		    	   lMaps4=iBaseDao.query(sqlString3,"");
		    	   if(lMaps4.size()<=0){
		    		   
		    	   }
		    	   else{
		    		   
		    	     for(Map<String,Object>map5:lMaps4){
		    		    map5.remove("RMRN");
		    		    map5.put("ID",lMaps.get(0).get("ID"));
		    	     }
		    	        String tableString1="CUSTOMER_SIDE";
		    	        iBaseDao.insert(findNoteTables(tableString1),lMaps4);
		    	   }
		    	  
		    	  
		    	  
		    	   iBaseDao.insert(findNoteTables(table.toUpperCase()),lMaps);
		    	   
		    	   //单个版本编号非共享
			       String codeString4=SerialNumberUtil.getSerialCode(SerialType.PROJ_BIT_CODE5,bnume);
			       //codeString4=codeString4+".0";
			       //codeString4=codeString4.replaceAll("K","");
			       codeString4=revalueString(codeString4);
			       
			       map.put("EDITION",codeString4);
		    	   map.put("P_ID",lMaps.get(0).get("ID"));
		    	   map.put("EDITION_NAME",EditionName(table));
		    	   map.put("MAIN_ID",mainid);
		    	   map.put("S_ID","7");
		    	   iBaseDao.insert(editionTable, map);   //存入版本表   
		    	  
		    	  break;
		    	  
		      case "CUSTOMER_SIDE":      //客户验收
		    	    String  cusnameString="V1.";
		    	    String  cusnameStringc="";
		    	    
		    	   //CUSTOMER_SIDE
		    	   
		    	  String sqlStringc="select * from client_acceptance where proj_source_id="+mainid;
		    	   
		    	   List<Map<String,Object>>lMapsc=new ArrayList<Map<String,Object>>();
		    	   lMapsc=iBaseDao.query(sqlStringc,"");
		    	   if(lMapsc.size()<=0){
		    		   cusnameString=id+cusnameString;
		    	   }
		    	   else{
		    		   cusnameStringc=lMapsc.get(0).get("ID").toString();
		    		   
		    		   cusnameString=cusnameStringc+cusnameString;
		    	     for(Map<String,Object>map5:lMapsc){
		    		    map5.remove("RMRN");
		    		    map5.put("ID",lMaps.get(0).get("ID"));
		    	     }
		    	        String tableString1="CLIENT_ACCEPTANCE";
		    	        lMapsc.get(0).put("ID", st.format(aDates));
		    	        iBaseDao.insert(findNoteTables(tableString1),lMapsc);
		    	   }
		    	  
		    	  
		    	  
		    	   iBaseDao.insert(findNoteTables(table.toUpperCase()),lMaps);
		    	   
		    	   //单个版本编号非共享
			       String codeString4c=SerialNumberUtil.getSerialCode(SerialType.PROJ_BIT_CODE5,cusnameString);
			       //codeString4=codeString4+".0";
			       //codeString4=codeString4.replaceAll("K","");
			       codeString4=revalueString(codeString4c);
			       
			       map.put("EDITION",codeString4);
		    	   map.put("P_ID",lMaps.get(0).get("ID"));
		    	   map.put("EDITION_NAME",EditionName("CLIENT_ACCEPTANCE"));
		    	   map.put("MAIN_ID",mainid);
		    	   map.put("S_ID","7");
		    	   iBaseDao.insert(editionTable, map);   //存入版本表   
		    	  
		    	  break;
		           
		       case "LOOK_BACK_MESSAGE":                //看板版本
		    	    Date aDate=new Date();
		    	    SimpleDateFormat s1=new SimpleDateFormat("yyyyMMddHHmmss");
		    	    
		    	    Calendar c = Calendar.getInstance();
					// 当前日期是本月第几周
					int weeks = c.get(Calendar.WEEK_OF_MONTH);
					//System.out.println(weeks);
					// 当前是星期几 java中一周第一天为星期天，所以1代表星期日，2代表星期一，以此类推，7代表星期6
					String num="";
					int week = c.get(Calendar.DAY_OF_WEEK);
					if(week==1){num="周日";}else if(week==2){num="周一";}else if(week==3){num="周二";}else if(week==4){num="周三";}else if(week==5){num="周四";}else if(week==6){num="周五";}else if(week==7){num="周六";}
				
					
		    	    String edString=s1.format(aDate)+"_"+"第"+week+"周"+"_"+num;
		    	   
		    	    map.put("EDITION",edString);
		    	    map.put("P_ID",weeidString);
		    	    map.put("EDITION_NAME",EditionName(table));
		    	    
		    	    map.put("MAIN_ID",mainid);
		    	    map.put("S_ID","4");
		    	    iBaseDao.insert(editionTable, map);   //存入版本表        
		    	   
		           break;
		           
		       case "CAPACITY_DESIGN":		//详细设计
		    	
		    	     iBaseDao.insert(findNoteTables(table.toUpperCase()),lMaps);

		    	   //单个版本编号非共享
				       String codeString9=SerialNumberUtil.getSerialCode(SerialType.PROJ_BIT_CODE2,bnume);
				       
				       //codeString6=codeString6+".0";
				       //codeString6=codeString6.replaceAll("X","");
				       codeString9=revalueString(codeString9);
				       map.put("EDITION",codeString9);
			    	   map.put("P_ID",lMaps.get(0).get("ID"));
			    	   map.put("EDITION_NAME",EditionName(table));
			    	   map.put("MAIN_ID",id);
			    	   map.put("S_ID","8");
			    	   iBaseDao.insert(editionTable, map);   //存入版本表   	
		    	     
		    	     
		           break;

		      default:
		    	  
		    	   iBaseDao.insert(findNoteTables(table.toUpperCase()),lMaps);
		    	   
		    	   String sqls="select * from"+ editionTable +" where id in (select MAX(id)as id from "+ editionTable +" where p_id="+lMaps.get(0).get("PROJ_SOURCE_ID").toString()+")";
		    	   
		    	   List<Map<String,Object>>list=new ArrayList<Map<String,Object>>();
		    	   
		    	   list=iBaseDao.query(sqls,"");
		    	   
		    	   if(list.size()<=0){
		    		   int s=0;
		    		   String codeString8="V1."+s;

		    		   map.put("EDITION",codeString8);
			    	   map.put("P_ID",lMaps.get(0).get("ID"));
			    	   map.put("EDITION_NAME",EditionName(table));
			    	  
			    	   iBaseDao.insert(editionTable, map);   //存入版本表   
		    	   }
		    	   else{
		    		   
		    		   int i=Integer.parseInt(list.get(0).get("EDITION").toString().replaceAll("V1.",""))+1;
		    		   
		    		   String nString="V1."+i;
		    		   
		    		   map.put("EDITION",nString);
		    		   
			    	   map.put("P_ID",lMaps.get(0).get("ID"));
			    	   map.put("EDITION_NAME",EditionName(table));
			    	  
			    	   iBaseDao.insert(editionTable, map);   //存入版本表   
		    	   }
		    	   
		    	  
			       break;
		      }
	   }  
	    
	   private String   revalueString(String value){
		   //QV1.1
		   int isa=value.indexOf("V");
		   
		   value=value.substring(isa,value.length());
		   //value=value.substring(1);
		   String number=value.substring(3,4);
		   if(number.equals("0")){
			  String mString=value.substring(4,5);
			  int i=Integer.parseInt(mString)-1;
			  value=value.substring(0,3)+i;
		   }
		   else{
		   value=value.substring(0,5);   // value.substring(0,value.indexOf("."))+"."+ (Integer.parseInt(value.substring(value.indexOf(".")+1)) - 1);
		   }
		   return value;	   
	   }
	   
	    private String  EditionName(String table){
			
	    	try {
				ConcurrentHashMap<String, String> selectMap=RmDictReferenceUtil.getDictReference("EDITION_NAME");
			    for(Map.Entry<String,String> entry:selectMap.entrySet()){
			    	if(table.equals(entry.getKey())){
			    	   table=entry.getValue();
			    	}
			    }
	    	
	    	} catch (BussnissException e) {		
				e.printStackTrace();				
			}
	    	return table;
	    }

	    private String  mark ="_HISTORY";
		
		private String findNoteTables(String table){
			return table+mark;
		}	
		
	}	
	
	//周报_风险
	@RequestMapping(params = "cmd=find_procj_id")
	public void find_procj_id(@RequestParam(value="page",required=false)String page,@RequestParam(value="rows",required=false)String rows,HttpServletRequest request,HttpServletResponse response,String id) throws Exception {
		SimpleDateFormat sDateFormat=new SimpleDateFormat("hh:mm:ss");
		PageBean pageBean=new ProjectController.PageBean(Integer.parseInt(page),Integer.parseInt(rows));
		List<Map<String,Object>> lMaps=new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> lMaps1=new ArrayList<Map<String,Object>>();
		List<Map<String,String>> lMaps2=new ArrayList<Map<String,String>>();
		net.sf.json.JSONArray jArray=new net.sf.json.JSONArray();
		JSONObject result=new JSONObject();
		Map<String,String> map=new HashMap<String, String>();
		String sqlString="select * from  proj_risk_management_history where  version="+id+"  limit "+pageBean.getStart()+" , "+pageBean.getPageSize();
		String sqlString1="select count(*)  from  proj_risk_management_history  where version="+id;
		lMaps=BaseDao.getBaseDao().query(sqlString,"");
		lMaps1=BaseDao.getBaseDao().query(sqlString1,"");
		if(lMaps.size()<=0){
		Date aDate=new Date();	Date bDate=new Date();	
		SimpleDateFormat s1=new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat s2=new SimpleDateFormat("yyyy-MM-dd");
		String dString=s1.format(aDate);
		String dString2=s2.format(bDate);
		
	    String string="[{INFORMATION:'请填写',INFLUENCE_LEVEL:'请填写',OCCURENCE_TIME:"+dString+",INTERNAL_SOLUTION:'请填写',SOLUTION_MEASURES:'请填写',DEADLINE:"+dString2+",RISK_STATUS:'请填写'}]";	
		
		JSONObject jsonObject=new JSONObject();
	    
		jsonObject.put("rows",string);
		jsonObject.put("total",1);
		response.getWriter().println(jsonObject);
		response.getWriter().close();	
			
			
//		String string="{项目状态:0}";
//		response.getWriter().println(JSONObject.fromObject(string));	
//	    response.getWriter().close();	
		}else{
		for(Map<String, Object>map2:lMaps){
		  for(Map.Entry<String, Object>mEntry:map2.entrySet()){
			  map.put(mEntry.getKey(),String.valueOf(mEntry.getValue()));
		  }
		  lMaps2.add(map);
		  map=new HashMap<String,String>();
		  map.clear();
		}
		//jArray.add(lMaps2);
		//System.err.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++="+jArray.getInt(0));
		for(Map<String,String>mEntry:lMaps2){
			if(mEntry.containsKey("INFLUENCE_LEVEL")){
				if("1".equals(mEntry.get("INFLUENCE_LEVEL"))){mEntry.put("INFLUENCE_LEVEL","高");}else if("2".equals(mEntry.get("INFLUENCE_LEVEL"))){mEntry.put("INFLUENCE_LEVEL","中");}else{mEntry.put("INFLUENCE_LEVEL","低");}
			}
			
			if(mEntry.containsKey("INTERNAL_SOLUTION")){
				if("1".equals(mEntry.get("INTERNAL_SOLUTION"))){mEntry.put("INTERNAL_SOLUTION","是");}else{mEntry.put("INTERNAL_SOLUTION","否");}
			}
			
			//if("1".equals(mEntry.get("INFLUENCE_LEVEL"))){mEntry.put("INFLUENCE_LEVEL","高");}else if(){}else{mEntry.put("INFLUENCE_LEVEL","低");}
			if(mEntry.containsKey("RISK_STATUS")){
				 if("1".equals(mEntry.get("RISK_STATUS"))){mEntry.put("RISK_STATUS","打开");}else if("2".equals(mEntry.get("RISK_STATUS"))){mEntry.put("RISK_STATUS","关闭");} else{mEntry.put("RISK_STATUS","风险转问题");}	
			}
			
		
		
//			if("1".equals(mEntry.get("NFLUENCE_LEVEL"))){mEntry.put("NFLUENCE_LEVEL","中");}else if("2".equals(mEntry.get("NFLUENCE_LEVEL"))){mEntry.put("NFLUENCE_LEVEL","低");}
//			else if("1".equals(mEntry.get("INTERNAL_SOLUTION"))){mEntry.put("INTERNAL_SOLUTION","是");}else if("0".equals(mEntry.get("INTERNAL_SOLUTION"))){mEntry.put("INTERNAL_SOLUTION","否");}
//			else if("1".equals(mEntry.get("RISK_STATUS"))){mEntry.put("RISK_STATUS","打开");}else if("0".equals(mEntry.get("RISK_STATUS"))){mEntry.put("NFLUENCE_LEVEL","关闭");}
		
		}
		
		
		jArray=new net.sf.json.JSONArray().fromObject(lMaps2);
		result.put("rows",jArray);
		result.put("total",lMaps1.get(0).get("COUNT(*)"));
		response.getWriter().println(result);
	    response.getWriter().close();
		}
	}	
	public class PageBean {
		private int page; // 第几页
		private int pageSize; // 每页记录数
		private int start;  // 起始页
		public PageBean(int page, int pageSize) {
			super();
			this.page = page;
			this.pageSize = pageSize;
		}	
		public int getPage() {
			return page;
		}
		public void setPage(int page) {
			this.page = page;
		}
		public int getPageSize() {
			return pageSize;
		}

		public void setPageSize(int pageSize) {
			this.pageSize = pageSize;
		}

		public int getStart() {
			return (page-1)*pageSize;
		}
	}
	
	//获取能力项目立项下达项目经理主键
	@RequestMapping(value = "getProjectManagerId")
	public String getProjectManagerId(String requirementId){
		String id = "";
		String sql = "SELECT manager_id FROM proj_released WHERE bid_buss_no = (SELECT requirement_code FROM proj_requirement WHERE id = '"+requirementId+"')";
		List<Map<String, Object>> listMap = dcmsDAO.query(sql, "");
		if(listMap.size()>0){
			id = listMap.get(0).get("MANAGER_ID")!=null?listMap.get(0).get("MANAGER_ID").toString():"";
		}
		return id;
	}
		
	//项目新增字段
	@RequestMapping(params = "cmd=opapmessage", method = RequestMethod.POST)
	public void opapmessage(HttpServletRequest request , HttpServletResponse response , String [] map) throws IOException{
		     IBaseDao isDao=BaseDao.getBaseDao();
		     String sql="select * from proj_new_data3 where min_time='"+map[0]+"'";
		     List<Map<String,Object>> lMaps=new ArrayList<Map<String,Object>>();
		     lMaps=isDao.query(sql,"");
		     if(lMaps.size()<=0){
		        Map<String,Object>mapmessageMap=new HashMap<String,Object>();
		        mapmessageMap.put("MIN_TIME",map[0]);
		        mapmessageMap.put("OPEX",map[1]);
		        mapmessageMap.put("CAPEX",map[2]);
		        
		        isDao.insert("PROJ_NEW_DATA3",mapmessageMap); 
		        
		        String aString="{a:0}";
	    	     response.getWriter().print(JSONObject.fromObject(aString));
	    	     response.getWriter().flush();
	    	     response.getWriter().close();
		        
		     }
		     else{
		    	 Map<String,Object>mapmessageMap=new HashMap<String,Object>();
			        mapmessageMap.put("MIN_TIME",map[0]);
			        mapmessageMap.put("OPEX",map[1]);
			        mapmessageMap.put("CAPEX",map[2]);	
			        isDao.update("PROJ_NEW_DATA3",mapmessageMap,new SqlWhereEntity().putWhere("MIN_TIME",map[0], WhereEnum.EQUAL_STRING));
		     
			        String aString="{a:0}";
		    	     response.getWriter().print(JSONObject.fromObject(aString));
		    	     response.getWriter().flush();
		    	     response.getWriter().close();
		     
		     }    
	}
	
	//show项目新增字段
		@RequestMapping(params = "cmd=showopapmessage", method = RequestMethod.POST)
		public void showopapmessage(HttpServletRequest request , HttpServletResponse response , String  id) throws IOException, ParseException{
			IBaseDao isDao=BaseDao.getBaseDao();
		     //String sql="select b.result z,p.opex o,p.capex c,b.fill_time f from bus_win_bid_result b , proj_new_data3 p  where p.min_time='"+id+"'  and  b.business_id=p.min_time";
		     String sql="select result z, fill_time f from bus_win_bid_result  where business_id='"+id+"'";
		     String sql1="select opex o,capex c  from proj_new_data3 where  min_time='"+id+"'";
		     
		     net.sf.json.JSONArray jArray=new net.sf.json.JSONArray();
		     
		     List<Map<String,Object>> lMaps=new ArrayList<Map<String,Object>>();
		     List<Map<String,Object>> lMaps1=new ArrayList<Map<String,Object>>();
		     
		     List<Map<String,String>> list=new ArrayList<Map<String,String>>();
	    	 Map<String,String> map=new HashMap<String,String>();
		     
		     lMaps=isDao.query(sql,"");
		     lMaps1=isDao.query(sql1,"");
		     
		     String aString="";
		     
		     JSONObject jObject=new JSONObject();
		     
		     if(lMaps.size()<=0){
		    	 aString="{a:0}";
		     }
		     else{
		    	 
		    	    
		    	  for(Map<String,Object> ma:lMaps){
		    		  
		    		  if(ma.containsKey("F")){
	    				  Date aDate=new Date();
	    				  SimpleDateFormat s1=new SimpleDateFormat("yyyy-MM-dd");
	    				  aDate=s1.parse(ma.get("F").toString());	
                         map.put("F",s1.format(aDate));
	    			     }
		    		  
		    		  for(Map.Entry<String,Object> entry:ma.entrySet()){			    			 
		    			  map.put(entry.getKey(),entry.getValue().toString());		    			  
		    		  }
		    	  }
		    	  list.add(map);
		    	  jObject.put("c",list.get(0));
		     }
		     
		     if(lMaps1.size()<=0){
		    	 aString="{a:0}";
		     }
		     else{
		    	 jObject.put("b",jArray.fromObject(lMaps1).get(0));
		     }
		     			     
		      jObject.put("a",aString);
	    	  response.getWriter().print(jObject);
	    	  response.getWriter().flush();
	    	  response.getWriter().close();
			     
			    
		}
		@RequestMapping(value = "storagePreAssessmentHistory")
		public void storagePreAssessmentHistory(String busCode, String id){
			ProjectController projectController = new ProjectController();
			try {
				projectController.save_history("PRE_ASSESSMENT", busCode, id);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
}
