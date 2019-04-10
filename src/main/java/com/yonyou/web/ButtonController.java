package com.yonyou.web;

import java.io.IOException;

import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yonyou.business.DataSourceUtil;
import com.yonyou.business.MetaDataUtil;
import com.yonyou.business.ProxyPageUtil;
import com.yonyou.business.RmDictReferenceUtil;
import com.yonyou.business.button.ButtonAbs;
import com.yonyou.business.entity.TokenEntity;
import com.yonyou.business.entity.TokenUtil;
import com.yonyou.util.BussnissException;
import com.yonyou.util.ConditionTypeUtil;
import com.yonyou.util.JsonUtils;
import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.log.BusLogger;
import com.yonyou.util.page.PageUtil;
import com.yonyou.util.page.proxy.PageBulidHtmlAbs;
import com.yonyou.util.page.proxy.bulid.PageBulidHtmlBySel;
import com.yonyou.util.sql.SQLUtil.WhereEnum;
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.util.sql.SqlWhereEntity;

@RestController
@RequestMapping(value = "/button")
public class ButtonController {
	
	
	@Autowired
	protected IBaseDao dcmsDAO;
	
	protected Map<String,ButtonAbs> findButtonMap(){
		return new HashMap<String,ButtonAbs>();
	}
	
	
	@RequestMapping(params = "cmd=button")
	public void button(HttpServletRequest request, HttpServletResponse response)  {
		String buttonToken =request.getParameter("buttonToken");
		
		TokenEntity tokenEntity = TokenUtil.initTokenEntity(request);
		if("delete".equalsIgnoreCase(buttonToken) || "update".equalsIgnoreCase(buttonToken)){
			HashMap<String, String> map = new HashMap<>();		
			map.put(BusLogger.LOG_ACTION_TYPE, "no_public_button_log");
			map.put(BusLogger.LOG_USER_ID, tokenEntity.USER.getId());
			map.put(BusLogger.LOG_USER_ID_NAME, tokenEntity.USER.getName());
			map.put(BusLogger.LOG_OWNER_ORG_ID, tokenEntity.COMPANY.getCompany_id());
			map.put(BusLogger.LOG_CONTENT, "URL:"+request.getRequestURL());
			map.put(BusLogger.LOG_OPERATION_INFO, buttonToken);		
			map.put(BusLogger.LOG_ACTION_MODULE, request.getParameter("dataSourceCode"));	
					
	        BusLogger.record_log(map);
		}
		buttonToken = buttonToken.split("_operation")[0];
		if(findButtonMap().containsKey(buttonToken)){
			try {
				findButtonMap().get(buttonToken).onClick(dcmsDAO, request, response);
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (BussnissException e) {
				// TODO 自动生成的 catch 块
				this.ajaxWrite(e.getMessage(), request, response);
				e.printStackTrace();
			}
		}
	}
	@RequestMapping(value = "getMenuButtonS", method = RequestMethod.GET)
	public String getMenuButton(HttpServletRequest request, HttpServletResponse response) throws IOException  {
		
		//执行sql 
		String userId = request.getParameter("userId");
		String companyId = request.getParameter("companyId");
		String menuCode = request.getParameter("menuCode");
		String roleId = request.getParameter("roleId");
		String level = request.getParameter("level");
		
		String sql2 = "SELECT DISTINCT bm.MENU_CODE,rbtn.*,arr1.auth_level FROM rm_authorize_resource r1 "+
		"JOIN rm_authorize_resource_record arr1 ON arr1.authorize_resource_id = r1.id "+
		"JOIN rm_button_relation_menu bm on bm.button_code = r1.total_code INNER JOIN rm_button  rbtn on rbtn.ID = bm.BUTTON_ID WHERE "+
		"arr1.party_id in ('"+roleId+"')"+
		"AND arr1.dr = '0'AND r1.dr = '0' AND arr1.IS_BUTTON_AUTHORITY = 1 and bm.MENU_CODE ="+menuCode +" order by id, rbtn.BUTTON_ENTITY asc";
		
		List<Map<String, Object>> menuList = BaseDao.getBaseDao().query(sql2, "");
		JSONArray array = new JSONArray();
		for(int j=0;j<menuList.size();j++){
			JSONObject jsonObject = new JSONObject();
			Object authLevel = menuList.get(j).get("AUTH_LEVEL");
			if(authLevel != null){
				String[] als = authLevel.toString().split(",");
				Set<String> sets = new HashSet<>();
				for(String al : als){
					sets.add(al);
				}
				if(sets.contains(level)){
					jsonObject.put("name",  menuList.get(j).get("BUTTON_NAME"));
					jsonObject.put("fun",  menuList.get(j).get("HTML_CLICK_NAME"));
					jsonObject.put("buttonToken",  menuList.get(j).get("BUTTON_TOKEN"));
					array.add(jsonObject);
				}
			}else{
				jsonObject.put("name",  menuList.get(j).get("BUTTON_NAME"));
				jsonObject.put("fun",  menuList.get(j).get("HTML_CLICK_NAME"));
				jsonObject.put("buttonToken",  menuList.get(j).get("BUTTON_TOKEN"));
				array.add(jsonObject);
			}
		}
		
		JSONObject returnJsonObject = new JSONObject();
		if(menuCode == null || menuCode.equals("") || menuCode.equals("null")|| menuCode.equals("undefined") || array.size() == 0){
			returnJsonObject.put("openState", false);
		}else{
			returnJsonObject.put("openState", true);
		}
		returnJsonObject.put("buttonData", array);
		
		//拼接基础按钮返回的json数组格式返回
		return JSONObject.toJSONString(returnJsonObject);
	}
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
		SqlTableUtil sqlEntity = DataSourceUtil.dataSourceToSQL(dataSourceCode);
		sqlEntity.appendSqlWhere(getQueryCondition(request));
		String limitStr = request.getParameter("limit");
		String offsetStr = request.getParameter("offset");
		int limit = limitStr != null ? Integer.parseInt(limitStr) : 10;
		int offset = offsetStr != null ? Integer.parseInt(offsetStr) : 0;
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
	
	/**
	 * 
	* @Title: getQueryCondition 
	* @Description: 依据规范，由request获取所有查询条件，并拼接查询条件实例。
	* @param request
	* @return
	* @throws BussnissException
	 */
	public SqlWhereEntity getQueryCondition(HttpServletRequest request) throws BussnissException {
		
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
					}
				}
				
				sqlWhere.putWhere(columnName, value.toString(), whereEnum);
			}
		}
		return sqlWhere;
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
		SqlTableUtil sqlEntity = DataSourceUtil.dataSourceToSQL(dataSourceCode);
		String jsonMessage = IBootUtil.findColJosnStr(IBootUtil.findShowFiledNameMap(sqlEntity.getShowFiledMap(),sqlEntity.getFiledNameMap()));
		System.out.println("####   " + jsonMessage);
		
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
	
	protected void ajaxWrite(String ajaxMessage,HttpServletRequest request,HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		PrintWriter out =null;
		try {
			out = response.getWriter();
			out.print(ajaxMessage);
			
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} finally{
			
			out.flush();
			out.close();
		}
		
	}
	
	
}
