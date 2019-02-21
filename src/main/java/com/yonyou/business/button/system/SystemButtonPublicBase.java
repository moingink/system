package com.yonyou.business.button.system;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yonyou.business.DataSourceUtil;
import com.yonyou.util.BussnissException;
import com.yonyou.util.IdRmrnUtil;
import com.yonyou.util.ZipUtils;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SQLUtil.WhereEnum;
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.util.sql.SqlWhereEntity;

/**按钮管理
 * @author XIANGJIAN
 * @date 创建时间：2017年2月14日
 * @version 1.0
 */
@RestController
@RequestMapping(value = "/sysButPubBase")
public class SystemButtonPublicBase {
	
	@Autowired
	protected IBaseDao dcmsDAO;
	
	/**	公共验证
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws BussnissException 
	 */
	@RequestMapping(params = "cmd=verification")
	public void verification(HttpServletRequest request, HttpServletResponse response) throws IOException, BussnissException{
		String jsonMessage = "{\"message\":\"0\"}";
		String switchParma = request.getParameter("switchParma") == null ? "" : request.getParameter("switchParma");
		String result = "";
		try{
			switch(switchParma){
				case "public_update" 	://公共修改验证
					result = getResultOfMessage( request, "已提交单据不能修改", "BILL_STATUS", new String[]{"1","2","3","4","7"});
					break;
				default : 
					result = IdRmrnUtil.writeErrorInfo("原因：参数有误，请联系管理员！").toString();
					break;
			}
		}finally{
			jsonMessage = "{\"message\":\""+result+"\"}";
			this.ajaxWrite(jsonMessage, request, response);
		}
	}
	
	/**通过数据源获取表名
	 * @param dataSourceCode
	 * @return
	 * @throws BussnissException
	 */
	private String findTableNameByDataSourceCode(String dataSourceCode) throws BussnissException{
		String tableName="";
		Map<String,String> dataMap=DataSourceUtil.getDataSource(dataSourceCode);
		if(dataMap!=null&&dataMap.containsKey(DataSourceUtil.DATASOURCE_METADATA_CODE)){
			tableName= dataMap.get(DataSourceUtil.DATASOURCE_METADATA_CODE);
		}
		return tableName;
	}
	
	/**通过数据源获取表名
	 * @param request
	 * @return 数据库表名
	 */
	private String getTableName(HttpServletRequest request){
		String dataSourceCode =request.getParameter("dataSourceCode")==null ? "" : request.getParameter("dataSourceCode");
		if("".equals(dataSourceCode)){
			return "";
		}
		try {
			return findTableNameByDataSourceCode(dataSourceCode);
		} catch (BussnissException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**通过Id获取多条记录
	 * @param tableName
	 * @param request
	 * @return 
	 */
	private List<Map<String, Object>> getlist(HttpServletRequest request){
		return dcmsDAO.query(getSqlTable(request));
	}
	
	/**通过Id获取一条记录
	 * @param tableName
	 * @param request
	 * @return 
	 */
	private Map<String, Object> getMapById(HttpServletRequest request){
		return dcmsDAO.find(getSqlTable(request));
	}
	
	/**获取sql
	 * @param request
	 * @return
	 */
	private SqlTableUtil getSqlTable(HttpServletRequest request){
		SqlTableUtil sqlTable =new SqlTableUtil(this.getTableName(request),"");
		SqlWhereEntity whereEntity  = new SqlWhereEntity();
		whereEntity.putWhere("ID", IdRmrnUtil.getIdRmrnMap(this.getJsonArray(request)).get("IDS").toString(), WhereEnum.IN).putWhere("DR", "0", WhereEnum.IN);
		sqlTable.appendSelFiled("*").appendSqlWhere(whereEntity);
		return sqlTable;
	}
	
	/**回写信息
	 * @param ajaxMessage
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void ajaxWrite(String ajaxMessage,HttpServletRequest request,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.print(ajaxMessage);
		out.flush();
		out.close();
	}
	

	private String getResultOfMessage(HttpServletRequest request,String reason ,String key,String value[]){
		String result = IdRmrnUtil.getNoPassRmrn(key, value, getlist(request) , getJsonArray(request)) ;
		if(!"".equals(result)){
			return IdRmrnUtil.writeErrorInfo(result,"原因："+reason).toString();
		}else{
			return  "0";
		}
	}
	
	private String getResultOfMessage(HttpServletRequest request,String reason ,String key,String value){
		String result = IdRmrnUtil.getNoPassRmrn(key, value, getlist(request) , getJsonArray(request)) ;
		if(!"".equals(result)){
			return IdRmrnUtil.writeErrorInfo(result,"原因："+reason).toString();
		}else{
			return  "0";
		}
	}
	
	/**获取jsonarray
	 * @param request
	 * @return
	 */
	private JSONArray getJsonArray(HttpServletRequest request){
		JSONArray jsonArray = JSONArray.fromObject(request.getParameter("jsonData"));
		return jsonArray;
	}
	
	/**通过ID获取map
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws BussnissException 
	 */
	@RequestMapping(params = "cmd=findMapById")
	public void findMapById(HttpServletRequest request, HttpServletResponse response)throws IOException, BussnissException{
		String jsonMessage = "{\"message\":\"系统错误，请联系管理员\"}";
		String dataSourceCode =request.getParameter("dataSourceCode")==null ? "" : request.getParameter("dataSourceCode");
		if("".equals(dataSourceCode)){
			throw new BussnissException("未获取到数据源");
		}else{
			jsonMessage = "{\"message\":\""+this.getMapById(request)+"\"}";
		}
		this.ajaxWrite(jsonMessage, request, response);
	}
	
	/**通过关联表主键获取按钮Map
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws BussnissException
	 */
	@RequestMapping(params = "cmd=getMapByRelTableId")
	public void getMapByRelTableId(HttpServletRequest request, HttpServletResponse response)throws IOException, BussnissException{
		String jsonMessage = "";
		SqlTableUtil stu = new SqlTableUtil("RM_BUTTON_RELATION_MENU" , "A");
		String id = request.getParameter("id")==null ? "" : request.getParameter("id");
		SqlWhereEntity whereEntity  = new SqlWhereEntity();
		whereEntity.putWhere("A.ID", id, WhereEnum.EQUAL_STRING).putWhere("B.DR", "0", WhereEnum.EQUAL_STRING);
		stu.appendSelFiled("B.* ").appendJoinTable("RM_BUTTON", "B", "A.BUTTON_ID = B.ID").appendSqlWhere(whereEntity);
		Map<String,Object> map = dcmsDAO.find(stu);
		if(map.size()<=0){
			jsonMessage = "{\"message\":\""+IdRmrnUtil.writeErrorInfo("该菜单节点下未设置按钮！")+"\"}";
		}else{
			if(map.get("IS_PUBLIC_BUTTON") != null && "1".equals(map.get("IS_PUBLIC_BUTTON")) ){
				jsonMessage = "{\"message\":\""+IdRmrnUtil.writeErrorInfo("公共按钮不允许修改！")+"\"}";
			}else{
				if( map.get("JSCONTENT") == null ){
					map.put("JSCONTENT", "");
				}else{
					String js = map.get("JSCONTENT").toString() ;
					map.put("JSCONTENT", ZipUtils.gunzip(js));//对脚本内容进行解压缩
				}
				jsonMessage = "{\"message\":\""+map+"\"}";
			}
		}
		this.ajaxWrite(jsonMessage, request, response);
	}
	
	/**统一对JSCONTENT的 Base64编码格式进行压缩
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(params = "cmd=zipJsContent")
	public void zipJsContent(HttpServletRequest request, HttpServletResponse response)throws IOException{
		String jsContent = request.getParameter("jsContent")==null?"":request.getParameter("jsContent");
		String jsonMessage = "{\"message\":\""+ZipUtils.gzip(jsContent)+"\"}";
		//String jsonMessage = "{\"message\":\""+jsContent+"\"}";
		this.ajaxWrite(jsonMessage, request, response);
	}
	
	/**统一对JSCONTENT的 Base64编码压缩的字符串进行解压缩
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(params = "cmd=unZipJsContent")
	public void unZipJsContent(HttpServletRequest request, HttpServletResponse response) throws IOException{
		JSONArray jsonArray = JSONArray.fromObject(request.getParameter("jsonData"));
		int len =  jsonArray.size();		
		String jsContent = "";
		for (int i = 0; i < len; i++) {
			JSONObject jb = (JSONObject) jsonArray.get(i);
			jsContent= ZipUtils.gunzip(jb.get("JSCONTENT").toString()) ;
		}
		String jsonMessage = "{\"message\":\""+jsContent+"\"}";
		this.ajaxWrite(jsonMessage, request, response);
	}
	
	/**删除公共按钮时，验证菜单节点下是否在用此按钮
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(params = "cmd=verButtonDelete")
	public void verButtonDelete(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String id = request.getParameter("id")==null?"":request.getParameter("id");
		SqlTableUtil stu = new SqlTableUtil("RM_BUTTON_RELATION_MENU", "");
		SqlWhereEntity where = new SqlWhereEntity();
		where.putWhere("DR", "0", WhereEnum.EQUAL_STRING).putWhere("BUTTON_ID",id, WhereEnum.EQUAL_STRING);
		stu.appendSelFiled("*").appendSqlWhere(where);
		List<Map<String, Object>> list = dcmsDAO.query(stu);
		String jsonMessage = "{\"message\":\"0\"}";
		String menuName = "" ;
		if(list.size()>0){
			for(Map<String, Object> map : list){
				menuName += map.get("MENU_NAME")+",";
			}
			jsonMessage = "{\"message\":\""+IdRmrnUtil.writeErrorInfo("原因：此按钮已绑定在  '"+menuName.substring(0,menuName.length()-1)+"'  菜单下，删除前请先解除绑定！")+"\"}";
		}
		this.ajaxWrite(jsonMessage, request, response);
	}
	
	/**新增按钮生成按钮编码
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws BussnissException
	 */
	@RequestMapping(params = "cmd=getButtonCode")
	public void getButtonCode(HttpServletRequest request, HttpServletResponse response)throws IOException, BussnissException{
		String maxCode = "" ;
		String menuCode = request.getParameter("menuCode")==null ? "" : request.getParameter("menuCode");
		SqlTableUtil stu = new SqlTableUtil("RM_BUTTON_RELATION_MENU" , "");
		SqlWhereEntity whereEntity  = new SqlWhereEntity();
		whereEntity.putWhere("DR", "0", WhereEnum.EQUAL_STRING).putWhere("MENU_CODE", menuCode, WhereEnum.EQUAL_STRING);
		stu.appendSelFiled("MAX(BUTTON_CODE)").appendSqlWhere(whereEntity);
		Map<String, Object> funMap = dcmsDAO.find(stu);
		if(funMap.get("MAX(BUTTON_CODE)") == null){
			maxCode = menuCode + "301" ; 
		}else{
			maxCode = (Long.parseLong(funMap.get("MAX(BUTTON_CODE)").toString()) + 1) + "" ;
		}
		String jsonMessage = "{\"message\":\""+maxCode+"\"}";
		this.ajaxWrite(jsonMessage, request, response);
	}
	
}
