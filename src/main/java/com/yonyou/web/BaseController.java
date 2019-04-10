package com.yonyou.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.WebUtils;

import com.yonyou.business.DataSourceUtil;
import com.yonyou.business.MetaDataUtil;
import com.yonyou.business.ProxyPageUtil;
import com.yonyou.business.RmDictReferenceUtil;
import com.yonyou.business.ValidatorUtil;
import com.yonyou.business.WorkflowNodeUtil;
import com.yonyou.business.entity.TokenEntity;
import com.yonyou.business.entity.TokenUtil;
import com.yonyou.iuap.cache.CacheManager;
import com.yonyou.util.BussnissException;
import com.yonyou.util.ConditionTypeUtil;
import com.yonyou.util.Import_Excel;
import com.yonyou.util.JsonUtils;
import com.yonyou.util.RmIdFactory;
import com.yonyou.util.busflow.BusFlowAbs;
import com.yonyou.util.importdata.ImportToolForExcel;
import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.mail.MailHelper;
import com.yonyou.util.page.PageUtil;
import com.yonyou.util.page.proxy.PageBulidHtmlAbs;
import com.yonyou.util.page.proxy.bulid.PageBulidHtmlBySel;
import com.yonyou.util.quartz.QuartzManager;
import com.yonyou.util.replace.WhereReplaceUtil;
import com.yonyou.util.sql.DataSourceCodeConstants;
import com.yonyou.util.sql.SQLUtil.WhereEnum;
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.util.sql.SqlWhereEntity;
import com.yonyou.util.wsystem.service.DocClient;

/**
 * 
* @ClassName: BaseController 
* @Description: 基础controller类,可处理基本CRUD操作，如存在其他业务操作可继承后重写方法
* @author 博超
* @date 2016年12月27日 
*
 */
@RestController
@RequestMapping(value = "/base")
public class BaseController extends BusFlowAbs implements DataSourceCodeConstants{

	@Autowired
	protected IBaseDao dcmsDAO;
	
	@Autowired
	protected CacheManager cacheManager;
	/**
	 * 
	* @Title: init 
	* @Description: 查询数据库记录 
	* @param request
	* @param response
	* @throws Exception
	 */
	@RequestMapping(params = "cmd=init")
	public void init(HttpServletRequest request, HttpServletResponse response) throws Exception {

		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		
		String dataSourceCode = request.getParameter("dataSourceCode");
		boolean boo = false;
		String  noAuthString[]={"SU_EVAL_PLAN_CT_EVALUATION","FIN_INVOICE_REAL_OPEN","FIN_RECEIVED_PAYMENT"};
		for(String string :noAuthString){
			if(dataSourceCode.equals(string)){
				boo = true;
			}
		}
		if(dataSourceCode.startsWith("VIEW")||dataSourceCode.endsWith("REF")){
			boo = true;
		}
		//暂时处理以VIEW开头的视图 需要数据权限的功能 
		String authString[] ={"VIEW_CUS_LEVEL_MESSAGE","FIN_VIEW_COLL_SUMMARY","VIEW_RECEIVED_CLAIM"};
		for(String string :authString){
			if(dataSourceCode.equals(string)){
				boo = false;
			}
		}
		SqlTableUtil sqlEntity = DataSourceUtil.dataSourceToSQL(dataSourceCode);
		String pageParam=request.getParameter("pageParam");
		if(pageParam!=null&&pageParam.length()>2){
			pageParam=pageParam.substring(1, pageParam.length()-1);
			sqlEntity.appendWhereAnd(pageParam);
		}
		sqlEntity.appendSqlWhere(getQueryCondition(request));
		//根据缓存角色id 获取角色数据权限和差异化并集查询数据 
		TokenEntity tokenEntity = TokenUtil.initTokenEntity(request);
		//System.out.println("###data_auth=" + tokenEntity.ROLE.getDataAuth());
		//判断数据源code 是否系统   非系统则进行数据权限校验
		String sqlString = "SELECT DISTINCT meta.*  from cd_datasource  sour INNER JOIN cd_metadata meta  on  meta.DATA_CODE = sour.METADATA_CODE where  sour.DATASOURCE_CODE ='"+dataSourceCode+"'";
		List<Map<String, Object>> list = BaseDao.getBaseDao().query(sqlString, "");
		String systemFlag = "";
		for(Map<String, Object> dataMap:list){
			systemFlag = String.valueOf(dataMap.get("SYSTEM_FLAG"));
		}
		if(null != tokenEntity.getToken()&&!"".equals(tokenEntity.getToken())){
			if(!systemFlag.equals("1")){
				if(boo==false){
					sqlEntity =	collectDataSQL(tokenEntity,sqlEntity,dataSourceCode);
				}
			}
		}/*else{
			//如果没有token 查询不到数据
			sqlEntity.appendWhereAnd(" CREATOR_ID = '' ");
		}*/
		
		String limitStr = request.getParameter("limit");
		String offsetStr = request.getParameter("offset");
		int limit = limitStr != null ? Integer.parseInt(limitStr) : 10;
		int offset = offsetStr != null ? Integer.parseInt(offsetStr) : 0;
		//添加替换数据
		WhereReplaceUtil.appendReplaceWhereSql(sqlEntity, dataSourceCode, request);
		List<Map<String, Object>> mapList = dcmsDAO.query(sqlEntity, offset, limit);
		mapList = RmDictReferenceUtil.transByDict(mapList, dataSourceCode);
		System.out.println("#######mapList\t" + mapList);
		int total = 10;
		total = dcmsDAO.queryLength(sqlEntity);
		sqlEntity.clearTableMap();
		System.out.println("#######length\t" + 10);
		// 需要返回的数据有总记录数和行数据
		String json = "{\"total\":" + total + ",\"rows\":" + JsonUtils.object2json(mapList) + "}";
		System.out.println("############" + json);
		
		out.print(json);
		out.flush();
		out.close();
	}
	public static SqlTableUtil collectDataSQL(TokenEntity tokenEntity,SqlTableUtil sqlEntity,String pageCode){
		//SqlWhereEntity  whereEntity =new SqlWhereEntity();
		String id = "";
		String userId = "";
		String companyId = "";
		if(null!=tokenEntity.ROLE){
			id = tokenEntity.ROLE.getRoleId();
		} 
		if(null!=tokenEntity.USER){
			userId =tokenEntity.USER.getId();
		}
		if(null!=tokenEntity.COMPANY){
			companyId = tokenEntity.COMPANY.getCompany_id();
		}
		
		//
		String sqlString = "select rmauth.*,rmuser.USER_PERSONAL_DATA ,rmuser.USER_COMPANY_DATA ,rmuser.rm_user_id ,rmuser.user_golbal from rm_role_auth   rmauth "+ 
		" left JOin (SELECT personal_data AS USER_PERSONAL_DATA,company_data AS USER_COMPANY_DATA,rm_role_id,rm_user_id,user_golbal from rm_role_auth_user where rm_user_id = '"+userId+"' ) rmuser on rmauth.rm_role_id = rmuser.rm_role_id"+
		" where rmauth.rm_role_id = '"+id+"'";
		List<Map<String, Object>> list = BaseDao.getBaseDao().query(sqlString, "");
		//1全局角色 不用拼写任何条件 2 角色有配置个人或者公司权限时且用户无差异化 拼接公司和者制单id,3如果有差异化取并集公司id和制单人id
		StringBuilder  userIds = new StringBuilder();
		StringBuilder  companyIds = new StringBuilder();
		String golbal = "";
		String user_golbal="";
		String uidString = "";
		String  idString ="''";
		if(null!= list && list.size()>0){
			for(Map<String, Object> dataMap:list){
				golbal = String.valueOf(dataMap.get("GOLBAL"));
				user_golbal = String.valueOf(dataMap.get("USER_GOLBAL"));
				uidString = String.valueOf(dataMap.get("RM_USER_ID"));
			}
			if(golbal.equals("0")||user_golbal.equals("0")){
				
			}else if(null!=uidString&&!uidString.equals("")){
				//如果角色非全局   用户有差异化的数据 拼接 
				String role_is_company = list.get(0).get("COMPANY").toString();
				String role_is_user = list.get(0).get("PERSONAL").toString();
				String userCompanyData="";
				String userPersonalData="";
				String roleCompanyData="";
				String rolePersonalData=""; 
				if(null!= list.get(0).get("USER_COMPANY_DATA")){
					userCompanyData = list.get(0).get("USER_COMPANY_DATA").toString();
				}
				if(null!= list.get(0).get("USER_PERSONAL_DATA")){
					userPersonalData = list.get(0).get("USER_PERSONAL_DATA").toString();
				}
				if(null!= list.get(0).get("COMPANY_DATA")){
					roleCompanyData = list.get(0).get("COMPANY_DATA").toString();
				}
				if(null!= list.get(0).get("PERSONAL_DATA")){
					rolePersonalData = list.get(0).get("PERSONAL_DATA").toString();
				}
				
				com.alibaba.fastjson.JSONArray userCompany = com.alibaba.fastjson.JSONArray.parseArray(userCompanyData);
				com.alibaba.fastjson.JSONArray userUser = com.alibaba.fastjson.JSONArray.parseArray(userPersonalData);
				if(role_is_company.equals("0")){
					companyIds.append(companyId);
				}
				com.alibaba.fastjson.JSONArray comJSONArray = com.alibaba.fastjson.JSONArray.parseArray(roleCompanyData);
				if(null!= comJSONArray){
					for(int i =0;i<comJSONArray.size();i++){
						if(i==0&&companyIds.toString().equals("")){
							companyIds.append(comJSONArray.getJSONObject(i).getString("id"));
						}else{
							companyIds.append(",");
							companyIds.append(comJSONArray.getJSONObject(i).getString("id"));
						}
					}
				}
				if(role_is_user.equals("0")){
					userIds.append(userId);
				}
				com.alibaba.fastjson.JSONArray personalJSONArray = com.alibaba.fastjson.JSONArray.parseArray(rolePersonalData);
				if(null!=personalJSONArray){
					for(int i =0;i<personalJSONArray.size();i++){
						if(i==personalJSONArray.size()-1){
							if(userIds.toString().endsWith(",")){
								userIds.append(personalJSONArray.getJSONObject(i).getString("ID"));
							}else if(!userIds.toString().equals("")){
								userIds.append(",").append(personalJSONArray.getJSONObject(i).getString("ID"));
							}else{
								userIds.append(personalJSONArray.getJSONObject(i).getString("ID"));
							}
							
						}else{
							if(userIds.toString().endsWith(",")){
								userIds.append(personalJSONArray.getJSONObject(i).getString("ID")).append(",");
							}else if(!userIds.toString().equals("")){
								userIds.append(",").append(personalJSONArray.getJSONObject(i).getString("ID"));
							}else{
								userIds.append(personalJSONArray.getJSONObject(i).getString("ID")).append(",");
							}
						}
					}
				}
				//必须放入子表的个人和公司数据集合
				if(null!= userCompany&&userCompany.size()>0){
					for(int i =0;i<userCompany.size();i++){
						if(i==userCompany.size()-1){
							if(companyIds.toString().endsWith(",")){
								companyIds.append(userCompany.getJSONObject(i).getString("id"));
							}else if(!companyIds.toString().equals("")){
								companyIds.append(",").append(userCompany.getJSONObject(i).getString("id"));
							}else{
								companyIds.append(userCompany.getJSONObject(i).getString("id"));
							}
						}else{
							if(companyIds.toString().endsWith(",")){
								companyIds.append(userCompany.getJSONObject(i).getString("id")).append(",");
							}else if(!companyIds.toString().equals("")){
								companyIds.append(",").append(userCompany.getJSONObject(i).getString("id"));
							}else{
								companyIds.append(userCompany.getJSONObject(i).getString("id")).append(",");
							}
							
						}
					}
				}
				
				if(null!=userUser){
					for(int i =0;i<userUser.size();i++){
						if(i==userUser.size()-1){
							if(userIds.toString().endsWith(",")){
								userIds.append(userUser.getJSONObject(i).getString("ID"));
							}else if(!userIds.toString().equals("")){
								userIds.append(",").append(userUser.getJSONObject(i).getString("ID"));
							}else{
								userIds.append(userUser.getJSONObject(i).getString("ID"));
							}
						}else{
							if(userIds.toString().endsWith(",")){
								userIds.append(userUser.getJSONObject(i).getString("ID")).append(",");
							}else if(!userIds.toString().equals("")){
								userIds.append(",").append(userUser.getJSONObject(i).getString("ID"));
							}else{
								userIds.append(userUser.getJSONObject(i).getString("ID")).append(",");
							}
						}
					}
				}
				//if(userIds.toString().length()>0){
				//whereEntity.putWhere("CREATOR_ID",userIds.toString() , WhereEnum.IN);
				//}
				
				if(!userIds.toString().equals("")){
					idString = userIds.toString();
				}
				if(companyIds.toString().equals("")){
					companyIds = companyIds.append("''");
				}
				sqlEntity.appendWhereAnd("  ((CREATOR_ID in ("+userId+") AND organization_id = "+tokenEntity.COMPANY.getCompany_id()+" ) or CREATOR_ID in("+idString+") or  organization_id in ("+companyIds.toString()+")    ");
				//sqlEntity.appendSqlWhere(whereEntity);
				//if(companyIds.toString().length()>0){
				//whereEntity.putWhere("organization_id",companyIds.toString(), WhereEnum.IN);
				//whereEntity.putWhere(key, value, whereEnum)
				//}
			}
		}else{
			//如果没有分配角色数据权限设置,无法查询到数据
			sqlEntity.appendWhereAnd("(CREATOR_ID in ('') ");
		}
		if(userIds.toString().length()>0){
			userId+=","+userIds.toString();
		}
		if(!golbal.equals("0")&&!user_golbal.equals("0")){
			//勾选部门后应该查询指定 责任人或项目经理 为部门下所有人员的数据 , 如果部门为多个集合多个部门的人员id作为查询参数 
			if(!companyIds.toString().equals("")&&!companyIds.toString().equals("''")){
				//勾选了部门 
				String cid= "";
				String [] ids = companyIds.toString().split(",");
				int length = ids.length;
				for(int i=0;i<length;i++){
					if(i==length-1){
						cid="'"+ids[i]+"'";
					}else{
						cid="'"+ids[i]+"',";
					}
				}
				String userList = "select id from rm_user where ORGANIZATION_ID in ("+cid+" )";
				List<Map<String, Object>> userlist = BaseDao.getBaseDao().query(userList, "");
				//合并用户id 到 userId中去
				String userListIdString="";
				if(null!= userlist &&userlist.size()>0){
					for(int j=0 ;j<userlist.size();j++){
						Map<String, Object> dataMap=userlist.get(j);
						if(j==userlist.size()-1){
							userListIdString = userListIdString +"'"+String.valueOf(dataMap.get("ID"))+"'";
						}else{
							userListIdString = userListIdString +"'"+String.valueOf(dataMap.get("ID"))+"'"+",";
						}
					}
					//
					userId= userId+","+userListIdString;
				}
			}
			if(pageCode.equals("PROJ_REQUIREMENT")){
				sqlEntity.appendWhereOr("  (proj_manager_id in("+userId+")) ");
				sqlEntity.appendWhereOr("  (duty_id in ("+userId+")) ");
			}
			if(pageCode.equals("PROJ_RELEASED")){
				sqlEntity.appendWhereOr("  (manager_id in("+userId+")) ");
				sqlEntity.appendWhereOr("  (duty_id in ("+userId+")) ");
			}
		}
		if(!golbal.equals("0")&&!user_golbal.equals("0")){
			sqlEntity.appendWhereAnd(" 1=1 )");
		}
		return sqlEntity;
	}
	
	@RequestMapping(params = "cmd=uploadCor") 
	public void uploadCor(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
	}
	
	@RequestMapping(params = "cmd=jsload") 
	public void jsload(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String nodeCode = request.getParameter("nodeCode");
		if(null==nodeCode || "".equals(nodeCode)){
			
	}
		String roleCode = request.getParameter("roleCode");
		if(null==roleCode || "".equals(roleCode)){
			
			
		}

		 PrintWriter out;
			try {
				String jsstr =" function loadjsTest(){ "
						+ "alert('11111');\r\n"
						+ "alert('22222');\r\n"
						+ "alert('33333');\r\n"
						+ "}";
				out = response.getWriter();
				response.setContentType("application/json;charset=UTF-8");   
				out.write(jsstr.toString());
			    out.flush();	                  
			    out.close(); 
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}  
			
	}
	
	@RequestMapping(params = "cmd=buttonload") 
	public void buttonload(HttpServletRequest request, HttpServletResponse response) throws Exception {
		 PrintWriter out;
			try {
				String buttonstr =" <button id=\"button\" type=\"button\" class=\"btn btn-default\"	onclick=\"loadjsTest()\">动态测试</button>";
				out = response.getWriter();
				response.setContentType("application/json;charset=UTF-8");   
				out.write(buttonstr.toString());
			    out.flush();	                  
			    out.close(); 
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}  
			
	}
	
	/**
	 * 
	* @Title: uploadAffix 
	* @Description: 上传附件
	* @param request
	* @param response
	* @throws Exception
	 */
	@RequestMapping(params = "cmd=uploadAffix") 
	public void uploadAffix(HttpServletRequest request, HttpServletResponse response, @RequestParam(value="files") MultipartFile files) throws Exception {
		
		Map<String, String> result = new HashMap<>();
		String fieldCode = request.getParameter("fieldCode");
		String batchNo = request.getParameter("batchNo");
//		if(files != null && files.length >0){
			DocClient docClient = new DocClient();
//			for (int i = 0; i < files.length; i++) {
				HashMap<String, String> dataMap = new HashMap<>();
				dataMap.put("userid", "11");
				dataMap.put("companyid","11");
//				dataMap.put("fieldcode", fieldCode);
				dataMap.put("batchno", batchNo);
				dataMap.put("bscode","QPG");
				MultipartFile tempFile = files;//[i];
				result = docClient.uploadAffix(dataMap, tempFile);
//			}
//		}
				
		PrintWriter out = response.getWriter();
		response.setCharacterEncoding("utf-8");
		out.print(JsonUtils.object2json(result));
		out.flush();
		out.close();
	}
	
	
	@RequestMapping(params = "cmd=getAffixList") 
	public void getAffixList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String result = null;
		String batchNo = request.getParameter("batchNo");
		if(batchNo != null && batchNo.length() >0){
			DocClient docClient = new DocClient();
			result = docClient.getAffixListHtml(batchNo);
		}
		
		JSONObject retJson =new JSONObject();
		retJson.put("message",result);
		PrintWriter out = response.getWriter();
		response.setCharacterEncoding("utf-8");
		out.print(retJson);
		out.flush();
		out.close();
	}
	
	/**
	 * 下载附件
	* @param request
	* @param response
	* @return
	* @throws Exception
	 */
	@RequestMapping(params = "cmd=downloadAffix") 
	public ResponseEntity<byte[]> downloadAffix(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String fileId = request.getParameter("fid");
		DocClient docClient = new DocClient();
		Map<String, Object> result = docClient.downloadAffix(fileId);
		ResponseEntity<byte[]> responseEntity = null;
		if(result.containsKey("filename")){
			JSONArray fileJsonArray = JSONArray.fromObject(result.get("filecontent"));
			byte[] fileContent = (byte[]) JSONArray.toArray(fileJsonArray, byte.class);
			HttpHeaders headers = new HttpHeaders();
			List<Charset> charSet = new ArrayList<Charset>();
			charSet.add(Charset.forName("UTF-8"));
			headers.setAcceptCharset(charSet);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDispositionFormData("attachment", URLEncoder.encode((String)result.get("filename"), "UTF-8"));  
			responseEntity = new ResponseEntity<byte[]>(fileContent, headers, HttpStatus.CREATED);
		}

		return responseEntity;
	}
	
	/**
	 * 
	 * @Title: deleteAffix 
	 * @Description: 删除附件
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=deleteAffix") 
	public void deleteAffix(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String fileId = request.getParameter("fid");
		JSONObject result = new JSONObject();
		DocClient docClient = new DocClient();
		if(docClient.deleteAffix(fileId)){
			result.put("message", "删除成功");
		}else{
			result.put("message", "删除失败,请联系管理员");
		}
		
		PrintWriter out = response.getWriter();
		response.setCharacterEncoding("utf-8");
		out.print(result);
		out.flush();
		out.close();
	}

	/**
	 * 
	* @Title: queryColumns 
	* @Description: 初始化列布局
	* @param request
	* @param response
	* @throws IOException
	* @throws BussnissException
	 */
	@RequestMapping(params = "cmd=queryColumns")
	public void queryColumns(HttpServletRequest request,HttpServletResponse response) throws IOException, BussnissException {
		
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		
		String dataSourceCode = request.getParameter("dataSourceCode");
		@SuppressWarnings("unused")
		SqlTableUtil sqlEntity = DataSourceUtil.dataSourceToSQL(dataSourceCode);
		String jsonMessage = PageUtil.findPageHtml(dataSourceCode, PageUtil.PAGE_TYPE_FOR_TABLE);
		
		out.print(jsonMessage);
		out.flush();
		out.close();

	}
	
	/**
	 * 
	* @Title: queryParam 
	* @Description: 初始化页面查询条件
	* @param request
	* @param response
	* @throws Exception
	 */
	@RequestMapping(params = "cmd=queryParam")
	public void queryParam(HttpServletRequest request,HttpServletResponse response) throws Exception {
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		
		String dataSourceCode = request.getParameter("dataSourceCode");
		String paramHtml = PageUtil.findPageHtml(dataSourceCode, PageUtil.PAGE_TYPE_FOR_SELECT);
		
		out.print(paramHtml);
		out.flush();
		out.close();
	}
	
	/**
	 * 
	* @Title: querySingleRecord 
	* @Description: 查询单条记录
	* @param request
	* @param response
	* @throws Exception
	 */
	@RequestMapping(params = "cmd=querySingleRecord")
	public void querySingleRecord(HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		
		String dataSourceCode = request.getParameter("dataSourceCode");
		SqlTableUtil sqlEntity = DataSourceUtil.dataSourceToSQL(dataSourceCode);
		String pageParam=request.getParameter("pageParam");
		if(pageParam!=null&&pageParam.length()>2){
			pageParam=pageParam.substring(1, pageParam.length()-1);
			sqlEntity.appendWhereAnd(pageParam);
		}
		sqlEntity.appendSqlWhere(getQueryCondition(request));
		
		//添加替换数据
		WhereReplaceUtil.appendReplaceWhereSql(sqlEntity, dataSourceCode, request);
		Map<String, Object> mapList = dcmsDAO.find(sqlEntity);
		mapList = RmDictReferenceUtil.transByDict(mapList, dataSourceCode);
		// 需要返回的数据有总记录数和行数据
		String json = JsonUtils.object2json(mapList); 
		System.out.println("############" + json);
		
		out.print(json);
		out.flush();
		out.close();

	}
	
	/**
	 * 
	* @Title: queryMaintainCols 
	* @Description: 初始化维护页面
	* @param request
	* @param response
	* @throws Exception
	 */
	@RequestMapping(params = "cmd=queryMaintainCols")
	public void queryMaintainCols(HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String dataSourceCode = request.getParameter("dataSourceCode");
		String maintainHtml = PageUtil.findPageHtml(dataSourceCode, PageUtil.PAGE_TYPE_FOR_INSERT_UPDATE);
		out.print(maintainHtml);
		out.flush();
		out.close();

	}
	
	@RequestMapping(params = "cmd=queryValids")
	public void queryValids(HttpServletRequest request,HttpServletResponse response) throws Exception {
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String dataSourceCode = request.getParameter("dataSourceCode");


		//validJson = BulidValidMain.findValidJson(PageUtil.findFiledData(dataSourceCode, PageUtil.PAGE_TYPE_FOR_INSERT_UPDATE));
		JSONObject validJson = ValidatorUtil.findValidJson(dataSourceCode);			

		JSONObject returnJson =new JSONObject();
		System.out.println("------------------------------");
		System.out.println(validJson);
		returnJson.put("message", validJson);
		out.print(returnJson);
		out.flush();
		out.close();

	}
	
	/**
	 * 
	* @Title: queryIndUpdateCols 
	* @Description: 初始化修改页面
	* @param request
	* @param response
	* @throws Exception
	 */
	@RequestMapping(params = "cmd=queryIndUpdateCols")
	public void queryIndUpdateCols(HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		
		String dataSourceCode = request.getParameter("dataSourceCode");
		String indUpdateHtml;
		//如数据源没有配置独立修改页面字段则取维护页面配置字段（及其维护页面代理设置）-兼容原有数据
		String updateField = DataSourceUtil.getDataSource(dataSourceCode).get(DataSourceUtil.DATASOURCE_UPDATE_FIELD);
		if( updateField!=null && updateField.trim().length()>0 ){
			indUpdateHtml = PageUtil.findPageHtml(dataSourceCode, PageUtil.PAGE_TYPE_FOR_INDEPENDENT_UPDATE);
		}else{
			indUpdateHtml = PageUtil.findPageHtml(dataSourceCode, PageUtil.PAGE_TYPE_FOR_INSERT_UPDATE);
		}
		
		out.print(indUpdateHtml);
		out.flush();
		out.close();

	}
	
	/**
	 * 
	* @Title: queryMaintainCols 
	* @Description: 初始化维护页面
	* @param request
	* @param response
	* @throws Exception
	 */
	@RequestMapping(params = "cmd=findData")
	public void findData(HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String dataSourceCode = request.getParameter("dataSourceCode");
		String pageParam=request.getParameter("pageParam");
		String showType =request.getParameter("showType");
		SqlTableUtil sqlTableUtil =DataSourceUtil.dataSourceToSQL(dataSourceCode);
		if(pageParam!=null&&pageParam.length()>2){
			pageParam=pageParam.substring(1, pageParam.length()-1);
		}
		sqlTableUtil.appendWhereAnd(pageParam);
		Map<String,Object> dataMap =this.dcmsDAO.find(sqlTableUtil);
		String maintainHtml="";
		if(showType!=null&&showType.length()>0&&"TEXT".equals(showType.toUpperCase())){
			maintainHtml =PageUtil.findPageHtml(dataSourceCode, PageUtil.PAGE_TYPE_FOR_INSERT_UPDATE_TEXT,dataMap);
		}else{
			maintainHtml =PageUtil.findPageHtml(dataSourceCode, PageUtil.PAGE_TYPE_FOR_INDEPENDENT_UPDATE,dataMap);
		}
		
		out.print(maintainHtml);
		out.flush();
		out.close();

	}
	
	/**
	 * 
	* @Title: insRow 
	* @Description: 新增行 
	* @param request
	* @param response
	* @throws IOException
	 */
	@RequestMapping(params = "cmd=insRow")
	public void insRow(HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String tabName = request.getParameter("tabName");
		
		JSONObject json = JSONObject.fromObject(request.getParameter("jsonData"));
//		Map<String,Object> entity = json;
		try {
			tabName=this.findTableName(tabName, request);
			dcmsDAO.insertByTransfrom(tabName, json);
		} catch (BussnissException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		String jsonMessage = "{\"message\":\"保存成功\"}";
		out.print(jsonMessage);
		out.flush();
		out.close();
	}
	
	/**
	 * 
	* @Title: delRows 
	* @Description: 删除行-支持多行 
	* @param request
	* @param response
	* @throws IOException
	* @throws BussnissException
	 */
	@RequestMapping(params = "cmd=delRows")
	public void delRows(HttpServletRequest request,HttpServletResponse response) throws IOException, BussnissException {
		
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		
		String tabName = request.getParameter("tabName");
		JSONArray jsonArray = JSONArray.fromObject(request.getParameter("jsonData"));
		List<Map<String, Object>> entityList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jb = (JSONObject) jsonArray.get(i);
			String id = jb.getString("ID");
			Map<String, Object> entity = new HashMap<String, Object>();
			entity.put("ID", id);
			entity.put("DR","1");
			entityList.add(entity);
		}
		tabName=this.findTableName(tabName, request);
 		dcmsDAO.update(tabName, entityList, new SqlWhereEntity());
		
		String jsonMessage = "{\"message\":\"删除成功\"}";
		out.print(jsonMessage);
		out.flush();
		out.close();
	}
	
	/**
	 * 
	* @Title: editRow 
	* @Description: 修改行 
	* @param request
	* @param response
	* @throws IOException
	 */
	@RequestMapping(params = "cmd=editRow")
	public void editRow(HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
	
		String tabName = request.getParameter("tabName");
		JSONObject json = JSONObject.fromObject(request.getParameter("jsonData"));
		try {
			tabName=this.findTableName(tabName, request);
			dcmsDAO.updateByTransfrom(tabName, json, new SqlWhereEntity());
		} catch (BussnissException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		String jsonMessage = "{\"message\":\"修改成功\"}";
		out.print(jsonMessage);
		out.flush();
		out.close();
	}
	
	@RequestMapping(params = "cmd=addJob")
	public void addJob(HttpServletRequest request,HttpServletResponse response) throws IOException{
		QuartzManager.addJob("11", "com.yonyou.util.quartz.job.TestJob", "123","*/30 * * * * ?");
	}
	/**
	 * 
	* @Title: getQueryCondition 
	* @Description: 依据规范，由request获取所有查询条件，并拼接查询条件实例。
	* @param request
	* @return
	 * @throws Exception 
	 */
	public SqlWhereEntity getQueryCondition(HttpServletRequest request) throws Exception {
		
		String dataSourceCode = request.getParameter("dataSourceCode");
		Map<String, Object> searchParams = WebUtils.getParametersStartingWith(request, "SEARCH-");
		//获取字段信息map——包含元数据+代理信息
		PageBulidHtmlAbs justForSelFieldInfo = new PageBulidHtmlBySel();
		Map<String, ConcurrentHashMap<String, String>> selFieldMap = justForSelFieldInfo.findData(dataSourceCode);
		
		SqlWhereEntity sqlWhere = new SqlWhereEntity();
		for (Map.Entry<String, Object> entry : searchParams.entrySet()) {
				
			String columnName = entry.getKey();
			Object value = entry.getValue();
				
			// FIXME 参选回写带来的问题-回传了一个数组
			if (value instanceof String[]) {
				value = ((String[]) value)[0];
			}

			if (value != null && value.toString().length() > 0) {
				WhereEnum whereEnum = null;
				
				//配置为日期区间的columnName为字段名+后缀
				if (selFieldMap.containsKey(columnName)) {
					Map<String, String> fieldInfo = selFieldMap.get(columnName);
					// 拼接逻辑：以代理中配置的CONDITION_TYPE为主，若未配置则依据元数据的INPUT_TYPE
					if (fieldInfo.containsKey(ProxyPageUtil.PROXYSELECT_CONDITION_TYPE)
							&& null != fieldInfo.get(ProxyPageUtil.PROXYSELECT_CONDITION_TYPE)
							&& "" != fieldInfo.get(ProxyPageUtil.PROXYSELECT_CONDITION_TYPE).trim()) {
						String condTypeCode = fieldInfo.get(ProxyPageUtil.PROXYSELECT_CONDITION_TYPE);
						whereEnum = ConditionTypeUtil.ConditionMapBase.get(condTypeCode);
						if(whereEnum==null){
							whereEnum=WhereEnum.EQUAL_STRING;
						}
					} else {
						String inputType = fieldInfo.get(MetaDataUtil.FIELD_INPUT_TYPE);
						whereEnum = ConditionTypeUtil.ConditionMapForInputType.get(inputType);
					}
				} else {
					// 日期区间特殊处理
					if (columnName.indexOf("_FROM") != -1) {
						columnName = columnName.replaceAll("_FROM", "");
						whereEnum = WhereEnum.TO_DATE_GREATER;
					} else if (columnName.indexOf("_TO") != -1) {
						columnName = columnName.replaceAll("_TO", "");
						whereEnum = WhereEnum.TO_DATE_LESS;
					} else {
						throw new Exception("数据源"+dataSourceCode+"所关联元数据中不存在请求字段"+columnName+",无法获取相关字段信息");
					}
				}
				
				sqlWhere.putWhere(columnName, value.toString(), whereEnum);
			}
		}
		return sqlWhere;
	}
	
	/**
	 * 
	* @Title: cacheClear 
	* @Description: 服务器缓存清理 
	* @param request
	* @param response
	* @throws IOException
	 */
	@RequestMapping(params = "cmd=cacheClear")
	public void cacheClear(HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
	
		int cacheEnum = Integer.parseInt(request.getParameter("cacheEnum"));
		String cacheName = "";
		String messageTemp = "清理成功!";
		switch (cacheEnum) {
		case 0:
			cacheName = "所有缓存";
			DataSourceUtil.clear();
			MetaDataUtil.clear();
			RmDictReferenceUtil.clear();
			WorkflowNodeUtil.clear();
			ProxyPageUtil.clear();
			break;
		case 1:
			cacheName = "数据源缓存";
			DataSourceUtil.clear();
			break;
		case 2:
			cacheName = "元数据缓存";
			MetaDataUtil.clear();
			break;
		case 3:
			cacheName = "数据字典缓存";
			RmDictReferenceUtil.clear();
			break;
		case 4:
			cacheName = "代理页面缓存";
			ProxyPageUtil.clear();
			break;
		default:
			messageTemp = "ERROR：未知缓存";
			break;
		}
		
		String jsonMessage = "{\"message\":\""+cacheName+ messageTemp +"\"}";
		out.print(jsonMessage);
		out.flush();
		out.close();
	}
	
	private String findTableName(String tabName,HttpServletRequest request) throws BussnissException{
		String tableName=tabName;
		String dataSourceCode =request.getParameter("dataSourceCode");
		if(dataSourceCode!=null&&dataSourceCode.length()>0){
			Map<String,String> dataMap=DataSourceUtil.getDataSource(dataSourceCode);
			if(dataMap!=null){
				tableName=dataMap.get(DataSourceUtil.DATASOURCE_METADATA_CODE);
			}
		}
		return tableName;
	}
	
	@ResponseBody
	@RequestMapping(params = "cmd=allNoticeJSON") //排序后按照未查看状态在前其次时时间在前方式传5条数据
	public void allNoticeJSON(HttpServletRequest request, HttpServletResponse response)throws Exception{
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		//String dataSourceCode = request.getParameter("dataSourceCode");
		List<Map<String, Object>> mapList = dcmsDAO.query(DataSourceUtil.dataSourceToSQL("NF_NOTICE"));
		String json=JsonUtils.object2json(mapList);
		
		out.print(json); //对页面显示公告进行排序 1 未阅在前(IS_READ=0) 2 时间大在前(根据数据库记录问题可以取最后的数据)
						//SELECT * FROM NF_NOTICE ORDER BY IS_READ ASC, NOTICE_DATE DESC;
		out.flush();
 		out.close();
	}
	
	@ResponseBody
	@RequestMapping(params = "cmd=noticeJSON",method = RequestMethod.POST) //排序后按照未查看状态在前其次时时间在前方式传5条数据
	public void noticeJSON(HttpServletRequest request, HttpServletResponse response)throws Exception{
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String dataSourceCode = request.getParameter("dataSourceCode");
		SqlTableUtil sqlTableUtil =DataSourceUtil.dataSourceToSQL(dataSourceCode);
		//添加替换数据
		WhereReplaceUtil.appendReplaceWhereSql(sqlTableUtil, dataSourceCode, request);
		List<Map<String, Object>> mapList = dcmsDAO.query(sqlTableUtil,0,5);
		String jsonString=JsonUtils.object2json(mapList);
		JSONObject json =new JSONObject();
		json.put("message",jsonString);
		out.print(json); //对页面显示公告进行排序 1 未阅在前(IS_READ=0) 2 时间大在前(根据数据库记录问题可以取最后的数据)
						//SELECT * FROM NF_NOTICE ORDER BY IS_READ ASC, NOTICE_DATE DESC;
		out.flush();
 		out.close();
	}
	@ResponseBody	
	@RequestMapping(params = "cmd=updateState") 
	public void updateState(HttpServletRequest request, HttpServletResponse response)throws Exception{
		response.setCharacterEncoding("utf-8");
 		String id=request.getParameter("ID");
 		if (id!=null) {
			System.err.println("###########################获取ajaxID参数："+id);
			SqlTableUtil sqlEntity = new SqlTableUtil("NF_NOTICE", "").appendSelFiled("IS_READ").appendWhereAnd("ID = "+id);
			List<Map<String, Object>> aList = dcmsDAO.query(sqlEntity);
			Object mapp=aList.get(0).get("IS_READ");
			if (mapp.equals("0")) {
				//update NF_NOTICE set IS_READ='1' where ID = '1000999900000000024';
				dcmsDAO.updateBySql("update NF_NOTICE_SHOW set IS_READ='1' where ID = "+id);
			}
		}	
	}
	
	@RequestMapping(params = "cmd=uploadAffixs") 
	@Deprecated
	public void uploadAffixs(@RequestParam(value="files") MultipartFile files,String pageCode,HttpServletResponse response,HttpServletRequest request) throws Exception {
		String sign = Import_Excel.uploadAffixs(files, pageCode, request);
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<script language='javascript'>");
		if("FIN_TICKET_IMPORT".equals(pageCode)){
		   	if(sign.equals("true")){	
      			out.println("alert('导入成功!')");
			    out.println("window.close();");
			    out.println("opener.location.reload();");
		   	}
		} else if("FIN_ICBC_ACCOUNT".equals(pageCode)){
			if(sign.equals("true")){	
				out.println("alert('导入成功！')");
			    out.println("window.close();");
			    out.println("opener.location.reload();");
			}else if(sign.equals("false")){
				out.println("alert('文档为空，无法导入！')");
			    out.println("window.close();");
			}else{
				out.println("alert('EXCEL中第"+sign+"行数据为重复数据，请修正后导入！')");
			    out.println("window.close();");
			}
		} else if("FIN_COMPANY_ACCOUNT".equals(pageCode)){
			if(sign.equals("true")){	
      			out.println("alert('导入成功!')");
			    out.println("window.close();");
			    out.println("opener.location.reload();");
			}
		} else if("FIN_BUSINESS_IMPORT".equals(pageCode)){
			if(sign.equals("true")){	
      			out.println("alert('导入成功!')");
			    out.println("window.close();");
			    out.println("opener.location.reload();");
			}else if(sign.equals("false")){
				out.println("alert('文档为空，无法导入！')");
			    out.println("window.close();");
			}else{
				
			}
		} else if("BUS_CONTRACT_ADMIN".equals(pageCode)){
			if(sign.equals("true")){	
      			out.println("alert('导入成功,请及时填写补录信息，并提交审批!')");
			    out.println("window.close();");
			    out.println("opener.location.reload();");
			}
		}	
		out.println("</script>");
		out.flush();
		out.close();
	}
	
	@RequestMapping(params = "cmd=ajax_message") 
	public void cus_one(HttpServletResponse response,String id) throws Exception {
	List<Map<String,Object>> lMaps=new ArrayList<Map<String,Object>>();
	JSONObject jObject=new JSONObject();
	JSONArray jArray=new JSONArray();
	Map<String,Object> map=new HashMap<String, Object>();
	String sqlString[]={"select * from  CAR_NETWORKING_INFO      where cus_id="+id,		 
			            "select * from  net_project_info         where cus_id="+id,
			            "select * from  cuma_car_model_info      where cus_id="+id,
		                "select * from  cuma_pro_plans           where cus_id="+id,
			            "select * from  cuma_decidsion_reration  where cus_id="+id};
	for(int i=0;i<sqlString.length;i++){
	String sql1=sqlString[i];
	lMaps=BaseDao.getBaseDao().query(sql1.toLowerCase(),"");
	jArray.add(lMaps);
	}
	//System.err.println("_________________________="+jArray.get(0));
	//System.err.println("+++++++++++++++++++++++++="+jArray.toString());
	response.getWriter().println(jArray);
    response.getWriter().close();
	}
	
	@RequestMapping(params = "cmd=getIDByDataSourceCode")
	public void getIDByDataSourceCode(HttpServletRequest request,HttpServletResponse response,String pageCode) throws Exception {
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();	
		//String dataSourceCode = request.getParameter(pageCode);
		
		String dataMeString=DataSourceUtil.getDataSource(pageCode).get(DataSourceUtil.DATASOURCE_METADATA_CODE);
		String[] idStrings = RmIdFactory.requestId(dataMeString, 1);
		
		String  jString="{"+"id:"+"\""+idStrings[0]+"\""+"}";
		//System.err.println("111111111111="+idStrings[0]);
		out.print(JSONObject.fromObject(jString));
		out.flush();
		out.close();
	}
	
	@RequestMapping(params = "cmd=level_sure") 
	public void level_sure(HttpServletResponse response,String level) throws Exception {
		List<Map<String,Object>> lMaps=new ArrayList<Map<String,Object>>();
		JSONArray jArray=new JSONArray();
		Map<String,Object> map=new HashMap<String, Object>();
		String string="{'CUS_LEVEL:'+'0'}";
		
		String sqlString="select * from  VISIT_RULES  where cus_level="+level;	 
		lMaps=BaseDao.getBaseDao().query(sqlString.toLowerCase(),"");
		String str = "";
		if(lMaps.size()>0){
			str = "1";
			//jArray.add(lMaps.get(0));
		}else{
			str = "0";
		}
		
		//System.err.println("_________________________="+jArray.get(0));
		
		/*if(jArray.get(0)=="[]"){		
			response.getWriter().println(JSONObject.fromObject(string));
		}else{
				
		}*/
		response.getWriter().println(str);
	    response.getWriter().close();
	}
	
	@RequestMapping(params = "cmd=level_sures") 
	public void level_sures(HttpServletResponse response,String level,String id) throws Exception {
	List<Map<String,Object>> lMaps=new ArrayList<Map<String,Object>>();
	JSONArray jArray=new JSONArray();
	Map<String,Object> map=new HashMap<String, Object>();
	String string="{'CUS_LEVEL:'+'0'}";
	
	String sqlString="select * from  CUMA_GRADE_SECTION  where CUSTOMERLEVEL="+level;	 
	lMaps=BaseDao.getBaseDao().query(sqlString.toLowerCase(),"");
	jArray.add(lMaps);
	//System.err.println("_________________________="+jArray.get(0));
	
	if(id.equals("0")){
		if(jArray.get(0)=="[]"){		
			response.getWriter().println(JSONObject.fromObject(string));
		}else{
			response.getWriter().println(jArray.get(0));	
		}
	   
		response.getWriter().close();
	}
	else{
		if(id.equals(lMaps.get(0).get("ID").toString())){
			String asd="{cg:0}";
			response.getWriter().println(JSONObject.fromObject(asd));
			response.getWriter().close();
		}
		else{
			String asd="{cu:1}";
			response.getWriter().println(JSONObject.fromObject(asd));
			response.getWriter().close();
		}
	}
	
	}
	
	
	
	@RequestMapping(params = "cmd=find_Messages") 
	public void find_Message(HttpServletResponse response,String id) throws Exception {
	List<Map<String,Object>> lMaps=new ArrayList<Map<String,Object>>();
	JSONArray jArray=new JSONArray();
	Map<String,Object> map=new HashMap<String, Object>();
	String sqlString="select * from  cuma_account  where id="+id;	 
	lMaps=BaseDao.getBaseDao().query(sqlString.toLowerCase(),"");
	jArray.add(lMaps);
	System.err.println("_________________________="+jArray.get(0));

    response.getWriter().println(jArray.get(0));	

    response.getWriter().close();
	}	
	
	@RequestMapping(params = "cmd=find_Messageses") 
	public void find_Messageses(HttpServletResponse response,String sel) throws Exception {
	List<Map<String,Object>> lMaps=new ArrayList<Map<String,Object>>();
	JSONArray jArray=new JSONArray();
	Map<String,Object> map=new HashMap<String, Object>();	
	Integer id_level=null;
	Integer id_levels=null;
	int levels[]={0,1,2,3,4};
	String sel_messageString="";
    String string2="{CUS_LEVEL:0}";
	if(sel.equals("一级客户")){
		id_level=levels[0];
	}
	else if(sel.equals("二级客户")){
		id_level=levels[1];
	}
    else if(sel.equals("三级客户")){
    	id_level=levels[2];
	}
    else if(sel.equals("四级客户")){
    	id_level=levels[3];
    }
    else{
    	id_level=levels[4];
    }
	String sqlString="select * from  visit_rules  where cus_level="+id_level;
	lMaps=BaseDao.getBaseDao().query(sqlString,"");
	
	if(lMaps.size() == 0){
		
		    response.getWriter().println(JSONObject.fromObject(string2));	
		    response.getWriter().close(); 
		
		
	}else{
		map=lMaps.get(0);
		for(Map.Entry<String,Object> mEntry: map.entrySet()){
			if(mEntry.getKey().equals("FREQUENCY")){
		    mEntry.getValue();
		    System.err.println("________________________="+mEntry.getValue());
		    String string="{CUS_LEVEL:"+"\""+mEntry.getValue()+"\""+"}";
		    
		    System.err.println("((((((((((((((((((((((((((((((="+string);
		    response.getWriter().println(JSONObject.fromObject(string));	
		    response.getWriter().close();
		} 
		}	
	         
	}
}
	
	@RequestMapping(params = "cmd=_num")
	public void _num(HttpServletRequest request,HttpServletResponse response,String sel,String id) throws Exception {
		int i=0;
		String sqlString="update VIS_PLAN set VIS_LAST_NUM="+String.valueOf(sel)+" , PLAN_STATE=1"+" where id="+id;
		i=BaseDao.getBaseDao().update(sqlString.toLowerCase());
		if(i==1){
			 String string2="{CUS_LEVEL:0}";
			 response.getWriter().println(JSONObject.fromObject(string2));	
			 response.getWriter().close();
		}else{
		 	 String string2="{CUS_LEVEL:1}";
			 response.getWriter().println(JSONObject.fromObject(string2));	
			 response.getWriter().close();
		}
	    //System.err.println("))))))))))))))))))))))))))))))))))))))))))))))))))+="+i);
	}
	

	@RequestMapping(params = "cmd=find_cus_id_message")
	public void find_cus_id_message(HttpServletRequest request,HttpServletResponse response,String id) throws Exception {
		List<Map<String,Object>> lMaps=new ArrayList<Map<String,Object>>();
		JSONArray jArray=new JSONArray();
		Map<String,Object> map=new HashMap<String, Object>();
		String string2="{CUS_LEVEL:0}";
		if(id==""){
		  id="0";	
		}
		String sqlString="select cuma_account.id,cuma_account.customer_person,cuma_account.vcode,cuma_account.vshortname,view_cus_level.customerlevel customerlevel,cuma_account.customer_classify,cuma_account.net_phase,cuma_account.customer_strategic,cuma_account.brand_name,cuma_account.noinvestment,cuma_account.investment_rela,cuma_account.nvestment_rela_com,cuma_account.start_time,cuma_account.unicom_pro,cuma_account.customer_represent,cuma_account.provincial_cus_num,cuma_account.provincial_cus_mai,cuma_account.cusc_customer_represent,cuma_account.cusc_customer_code,cuma_account.cusc_cus_mail,cuma_account.cusc_cus_num,cuma_account.department,cuma_account.customer_stage,cuma_account.cusc_strategic,cuma_account.brand_vehicles,cuma_account.main_service,cuma_account.bill_statuses,cuma_account.bill_status from cuma_account cuma_account , view_cus_level where dr=0 and cuma_account.id=cuma_account_id and cuma_account.id="+id;
		lMaps=BaseDao.getBaseDao().query(sqlString.toLowerCase(),"");
		if(lMaps.size()==0){
			response.getWriter().println(JSONObject.fromObject(string2));	
		    response.getWriter().close(); 
		}
		else{
		jArray.add(lMaps);
		System.err.println("_________________________222="+jArray);
	    response.getWriter().println(jArray.get(0));	
	    response.getWriter().close();
		}
	}
	
	@RequestMapping(params = "cmd=find_visit_id_message")
	public void find_visit_id_message(HttpServletRequest request,HttpServletResponse response,String id) throws Exception {
		List<Map<String,Object>> lMaps=new ArrayList<Map<String,Object>>();
		JSONArray jArray=new JSONArray();
		Map<String,Object> map=new HashMap<String, Object>();
		String sqlString="select * from vis_plan where id="+id;
		lMaps=BaseDao.getBaseDao().query(sqlString.toLowerCase(),"");
		jArray.add(lMaps);
		System.err.println("_________________________="+jArray);
	    response.getWriter().println(jArray.get(0));	
	    response.getWriter().close();
		
	}
	
	@RequestMapping(params = "cmd=find_info_id")
	public void find_info_id(HttpServletRequest request,HttpServletResponse response,String id) throws Exception {
		List<Map<String,Object>> lMaps=new ArrayList<Map<String,Object>>();
		//JSONArray jArray=new JSONArray();
		String sqlString="select * from PRODUCT_INFO where bill_status = 3 AND bus_ids = "+id;
		lMaps=BaseDao.getBaseDao().query(sqlString.toLowerCase(),"");
		for(Map<String,Object> map2 :lMaps){
			String ID = map2.get("ID").toString();
			map2.remove("ID");
			map2.put("ID", "\""+ID+"\"");
		}
		String json = com.alibaba.dubbo.common.json.JSON.json(lMaps);
		//jArray.add(lMaps);
		//System.err.println("_________________________="+jArray);
	    response.getWriter().println(json);	
	    response.getWriter().close();
		
	}
	
	
	
	
	
	//客户立项
		@RequestMapping(params = "cmd=insert_Cus_id")
		public void insert_Cus_id(HttpServletRequest request,HttpServletResponse response,String id) throws Exception {
		 List<Map<String,Object>> lMaps=new ArrayList<Map<String,Object>>();
		 List<Map<String,Object>> lMapses=new ArrayList<Map<String,Object>>();
			
		 //项目状态
		 String cString=request.getParameter("con_status");
		 System.err.println(cString);
		  if(!cString.equals("null") && cString != null){
			String sqlString1="update "+"PROJ_RELEASED".toLowerCase()+" set project_statues="+"\""+cString+"\""+" where OPPORTUNITY_CODE="+"\""+id+"\"";
			BaseDao.getBaseDao().update(sqlString1);
            
			
			
			String stringss="{CUS_LEVEL:0}";
			response.getWriter().println(JSONObject.fromObject(stringss));	
		    response.getWriter().close();	
		    
//			if(lMaps.size()<=0){
//				Map<String,Object> map=new HashMap<String, Object>();
//				String string[]={"PROJ_STAUTS","PROJ_SPEED","PROJ_IN_STAUTS","PROJ_OUT_STAUTS","PROJ_NOW_PRICE","ONLY_ONE_ID"};
//				
//				for(int i=0;i<string.length;i++){
//					
//					if(i==0){
//					  map.put(string[i],cString);
//					}
//				     else if(i==5){
//					map.put(string[i],id);
//					}else{
//					map.put(string[i],"");
//				  }
//				}
//				BaseDao.getBaseDao().insert("proj_contrast_cus".toUpperCase(),map);	
//			}  
//			else{
//				String sql="update proj_contrast_cus set  PROJ_STAUTS="+"\""+cString+"\""+" where only_one_id="+"\""+id+"\"";
//				BaseDao.getBaseDao().update(sql.toLowerCase());
//			}
//			    String sqlString2="select  "+"PROJ_STAUTS".toLowerCase()+"  from proj_contrast_cus where only_one_id="+"\""+id+"\"";
//			    
//			    lMapses=BaseDao.getBaseDao().query(sqlString,"");
//			    
//			    JSONObject jsonObject=new JSONObject();
//			    jsonObject.put("PROJ_STAUTS",lMapses);
//			
//			String string="{CUS_LEVEL:0}";
//			response.getWriter().println(jsonObject);	
//		    response.getWriter().close();	
		 }
		
		}
		
		@RequestMapping(params = "cmd=insert_Cus_ids")
		public void insert_Cus_ids(HttpServletRequest request,HttpServletResponse response,String id) throws Exception {
		 //项目内部状态
		 String conString=request.getParameter("con_in_status");	
		 if(!conString.equals("null") && conString != null ){
			String sqlString2="update "+"PROJ_RELEASED".toLowerCase()+" set project_stage="+"\""+conString+"\""+" where OPPORTUNITY_CODE="+"\""+id+"\"";
			BaseDao.getBaseDao().update(sqlString2);
			String string="{CUS_LEVEL:0}";
			response.getWriter().println(JSONObject.fromObject(string));	
		    response.getWriter().close();	
		 }
		}
		
		@RequestMapping(params = "cmd=insert_out_Cus_ids")
		public void insert_out_Cus_ids(HttpServletRequest request,HttpServletResponse response,String id) throws Exception {
		 //项目外部状态
		 String outString=request.getParameter("con_out_status");	
		 if(!outString.equals("null") && outString != null ){
			String sqlString3="update "+"PROJ_RELEASED".toLowerCase()+"  set  proj_out_stauts="+outString+"  where OPPORTUNITY_CODE="+"\""+id+"\"";
			BaseDao.getBaseDao().update(sqlString3);
			String string="{CUS_LEVEL:0}";
			response.getWriter().println(JSONObject.fromObject(string));	
		    response.getWriter().close();	
		 }
		}
		
		
		//能力项目
		@RequestMapping(params = "cmd=find_Cus_abliity_id")
		public void find_Cus_abliity_id(HttpServletRequest request,HttpServletResponse response,String id) throws Exception {
			List<Map<String,Object>> lMaps=new ArrayList<Map<String,Object>>();
			JSONArray jArray=new JSONArray();
			Map<String,Object> map=new HashMap<String, Object>();
			String sqlString="select * from  " +"proj_requirement".toLowerCase()+", proj_released   where  PROJ_CODE=BID_BUSS_NO  and  PROJ_CODE="+"\""+id+"\"";
			lMaps=BaseDao.getBaseDao().query(sqlString,"");
			if(lMaps.size()<=0){
			String string="{CUS_LEVEL:0}";
			response.getWriter().println(JSONObject.fromObject(string));	
		    response.getWriter().close();	
			}else{
			//jArray.add(lMaps);
			String aString="";	
			for(Map<String,Object>map2:lMaps){
				aString=String.valueOf(map2.get("ID"));
			}	
		    JSONObject jsonObject=new JSONObject();		
			jArray.add(lMaps);
			jsonObject.put("ID", aString);
			jsonObject.put("message",jArray.get(0));
			System.err.println("_________________________222="+jsonObject);
		    response.getWriter().println(jsonObject);	
		    response.getWriter().close();
			}
		}
	
	
	
	
	@RequestMapping(params = "cmd=find_buss_id")
	public void find_buss_id(HttpServletRequest request,HttpServletResponse response,String id) throws Exception {
		List<Map<String,Object>> lMaps=new ArrayList<Map<String,Object>>();
		JSONArray jArray=new JSONArray();
		Map<String,Object> map=new HashMap<String, Object>();
		String sqlString="select ASSESSMENT_ING_PRICE,SIGN_SURE_TIME from  PROJ_RELEASED , bus_contract_admin , bus_send_assessment  where  OPPORTUNITY_CODE=business_id  and business_id=bus_oppo_id and OPPORTUNITY_CODE="+"\""+id+"\"";
		lMaps=BaseDao.getBaseDao().query(sqlString,"");
		if(lMaps.size()<=0){
		String string="{项目状态:0}";
		response.getWriter().println(JSONObject.fromObject(string));	
	    response.getWriter().close();	
		
		}else{
          if(lMaps.get(0).get("ASSESSMENT_ING_PRICE")==null){
        	  lMaps.get(0).put("ASSESSMENT_ING_PRICE", "0");
          }
          
          
          if(lMaps.get(0).get("SIGN_SURE_TIME")==null || "".equals(lMaps.get(0).get("SIGN_SURE_TIME"))){
        	  lMaps.get(0).put("SIGN_SURE_TIME","0");	  
        	  
          }
          else{        
          String  aString=lMaps.get(0).get("SIGN_SURE_TIME").toString().replaceAll("-","");
          lMaps.get(0).put("SIGN_SURE_TIME", aString);	
          }
          jArray.add(lMaps.get(0));
		//System.err.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^=="+jArray.get(0));
	    response.getWriter().println(jArray.get(0));
	    response.getWriter().close();
		}
	}
	
	@RequestMapping(params = "cmd=find_procj_id")
	public void find_procj_id(@RequestParam(value="page",required=false)String page,@RequestParam(value="rows",required=false)String rows,HttpServletRequest request,HttpServletResponse response,String id) throws Exception {
		SimpleDateFormat sDateFormat=new SimpleDateFormat("hh:mm:ss");
		PageBean pageBean=new BaseController.PageBean(Integer.parseInt(page),Integer.parseInt(rows));
		List<Map<String,Object>> lMaps=new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> lMaps1=new ArrayList<Map<String,Object>>();
		List<Map<String,String>> lMaps2=new ArrayList<Map<String,String>>();
		JSONArray jArray=new JSONArray();
		JSONObject result=new JSONObject();
		Map<String,String> map=new HashMap<String, String>();
		String sqlString="select * from  proj_risk_management where  PROJ_SOURCE_ID="+id+"  limit "+pageBean.getStart()+" , "+pageBean.getPageSize();
		String sqlString1="select count(*)  from  proj_risk_management  where PROJ_SOURCE_ID="+id;
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
		
		
		jArray=new JSONArray().fromObject(lMaps2);
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
	@RequestMapping(params = "cmd=save_procj_id")
	public void save_procj_id(HttpServletRequest request,HttpServletResponse response,String id,String map[]) throws Exception {
		String del=request.getParameter("del");
		if("0".equals(del)){
			if("undefined".equals(id)){
				return ;
			}
			BaseDao.getBaseDao().delete("proj_risk_management".toUpperCase(),new SqlWhereEntity().putWhere("ID",id,WhereEnum.EQUAL_INT));   
		
			String string1="{项目状态:0}";
			response.getWriter().println(JSONObject.fromObject(string1));	
		    response.getWriter().close();	
		}
		else{
		Map<String,Object>map2=new HashMap<String, Object>();
		String string[]={"INFORMATION","INFLUENCE_LEVEL","OCCURENCE_TIME","INTERNAL_SOLUTION","SOLUTION_MEASURES","DEADLINE","RISK_STATUS","PROJ_SOURCE_ID"};
		Date d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(map[2]);
		Date d2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(map[5]);
		SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		map[2]=sdf0.format(d1);
		map[5]=sdf1.format(d2);	
		if(id==""){
			for(int i=0;i<map.length;i++){
				map2.put(string[i],(Object)map[i]);
			}
			BaseDao.getBaseDao().insert("proj_risk_management".toUpperCase(),map2);
			
			String string1="{项目状态:0}";
			response.getWriter().println(JSONObject.fromObject(string1));	
		    response.getWriter().close();	
		}
		else{
			for(int i=0;i<map.length;i++){
				map2.put(string[i],(Object)map[i]);
			}
			//String sqlString="update proj_risk_management set "+string[0]+"="+"\""+map2.get("INFORMATION")+"\""+", "+string[1]+"="+map2.get("INFLUENCE_LEVEL")+", "+string[2]+"="+"\""+map2.get("OCCURENCE_TIME")+"\""+", "+string[3]+"="+map2.get("INTERNAL_SOLUTION")+", "+string[4]+"="+map2.get("SOLUTION_MEASURES")+", "+string[5]+"="+"\""+map2.get("DEADLINE")+"\""+", "+string[6]+"="+map2.get("RISK_STATUS")+" where ID="+id;
		    
			String sqlString="update proj_risk_management set "+string[0]+"="+"\""+map2.get(string[0])+"\""+","+string[1]+"="+map2.get(string[1])+","+string[2]+"="+"\""+map2.get(string[2])+"\""+","+string[3]+"="+map2.get(string[3])+","+string[4]+"="+"\""+map2.get(string[4])+"\""+","+string[5]+"="+"\""+map2.get(string[5])+"\""+","+string[6]+"="+map2.get(string[6])+"  where ID="+id;
			
			BaseDao.getBaseDao().update(sqlString);
			String string1="{项目状态:0}";
			response.getWriter().println(JSONObject.fromObject(string1));	
		    response.getWriter().close();	
		}
		}
	}
	
	
	
	
	
	
	private DateFormat SimpleDateFormat(String string) {
		// TODO Auto-generated method stub
		return null;
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
		    		
//		    		if("LEVEL".equals(string[i])){		    			
//		    			map2.put("LEVEL",1);
//		    		}
		    		
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
	

	@RequestMapping(params = "cmd=getBusDepByUserId") 
	public void getBusDepByUserId(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String userId = request.getParameter("userId");
		JSONArray jArray=new JSONArray();
		String sqlString="SELECT CAST(organization_id as char) AS ORGANIZATION_ID FROM rm_user WHERE ID = "+userId;	 
		List<Map<String, Object>> list = BaseDao.getBaseDao().query(sqlString.toLowerCase(),"");
		jArray.add(list);
		response.getWriter().println(jArray.get(0));	
		response.getWriter().close();
	}
	
	/**
	 * 获取功能节点标签
	 * @param dataSourceCode 数据源编码
	 * @return
	 */
	@RequestMapping(value = "getLable")
	public List<Map<String, Object>> getLable(String dataSourceCode){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if(StringUtils.isNoneBlank(dataSourceCode)){
			String sql = "SELECT a.* FROM cd_label_configure_detail a LEFT JOIN cd_label_configure b ON a.parent_id = b.id WHERE b.datasource_code = '"+dataSourceCode+"'";
			list = BaseDao.getBaseDao().query(sql,"");
		}
		return list;
	}

	/**
	 * Excel导入
	 * @param files
	 * @param request
	 * @param response
	 * @return
	 * @throws BussnissException
	 * @throws IOException
	 */
	@RequestMapping(params = "cmd=uploadFile")
	public String uploadFile(@RequestParam(value = "file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws BussnissException, IOException{
		TokenEntity tokenEntity = TokenUtil.initTokenEntity(request);
		String dataSourceCode = request.getParameter("dataSourceCode");
		StringBuffer errorMessage = ImportToolForExcel.importExcel(file, dataSourceCode, tokenEntity);
		return "{\"errorMessage\":\""+errorMessage+"\"}";
	}
	
	/**
	 * 根据Json初始化详情页面
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "initDetailPageByJson", method = RequestMethod.GET)
	public void initDetailPageByJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String showType = request.getParameter("showType");
		String dataSourceCode = request.getParameter("dataSourceCode");
		JSONObject json = JSONObject.fromObject(request.getParameter("json"));
		
		String detailPageHtml="";
		if (StringUtils.isNotEmpty(showType) && "TEXT".equals(showType.toUpperCase())) {
			detailPageHtml = PageUtil.findPageHtml(dataSourceCode, PageUtil.PAGE_TYPE_FOR_INSERT_UPDATE_TEXT, json);
		} else {
			detailPageHtml = PageUtil.findPageHtml(dataSourceCode, PageUtil.PAGE_TYPE_FOR_INDEPENDENT_UPDATE, json);
		}
		
		out.print(detailPageHtml);
		out.flush();
		out.close();
	}
	@RequestMapping(params = "cmd=send_mail_password", method = RequestMethod.POST)
	public void send_mail_password(HttpServletRequest request, HttpServletResponse response, String []map) throws IOException{
		String name = map[0];
		String password = map[1];
		String email = map[2];
		MailHelper mail=new MailHelper();
		mail.init();
		mail.sendMail("A", name, password, email);
        String string="{cg:0}";
        response.getWriter().print(JSONObject.fromObject(string));
        response.getWriter().flush();
        response.getWriter().close();
		
	}
}	
