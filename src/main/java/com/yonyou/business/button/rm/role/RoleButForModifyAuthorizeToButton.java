package com.yonyou.business.button.rm.role;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.util.RmPartyConstants;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.yonyou.business.button.system.SystemButtonUtil;
import com.yonyou.util.BussnissException;
import com.yonyou.util.IdRmrnUtil;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SQLUtil.WhereEnum;
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.util.sql.SqlWhereEntity;


/**角色按钮授权管理 修改权限
 * @author XIANGJIAN
 * @date 创建时间：2017年4月3日
 * @version 1.0
 */
public class RoleButForModifyAuthorizeToButton extends SystemButtonUtil {


	@Override
	protected boolean befortOnClick(IBaseDao dcmsDAO,
			HttpServletRequest request, HttpServletResponse response) {
		return false;
	}

	@Override
	protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) throws IOException, BussnissException {
		String buttonToken = request.getParameter("buttonToken");
		String roleId = request.getParameter("roleId");
		JSONArray jsonArray = JSONArray.fromObject(request.getParameter("jsonData"));	//获取json数组
		String result = "权限范围操作成功！" ; 
		String RM_AUTHORIZE_RESOURCE = "RM_AUTHORIZE_RESOURCE";
		String RM_AUTHORIZE_RESOURCE_RECORD = "RM_AUTHORIZE_RESOURCE_RECORD";
		String RM_BUTTON_RELATION_MENU = "RM_BUTTON_RELATION_MENU";
		System.out.println(jsonArray);
		Map<String,Object> map = new HashMap<String, Object>();
		switch(buttonToken){
		case "modify_myself_authority":
			map.put("UPDATE_AUTHORITY", "001");
			break ; 
		case "modify_company_authority":
			map.put("UPDATE_AUTHORITY", "010");
			break ; 
		case "modify_all_authority":
			map.put("UPDATE_AUTHORITY", "100");
			break ; 
		default : 
			result = IdRmrnUtil.writeErrorInfo("原因：参数有误，请联系管理员！").toString();
			break ; 
		}
		
		String menuCode = "" ; 
		for(int i = 0 ; i < jsonArray.size() ; i++){
			JSONObject json = jsonArray.getJSONObject(i);
			menuCode += json.get("BUTTON_CODE").toString() +",";
		}
		menuCode = menuCode.substring(0, menuCode.length()-1);
		String authorizeId =  RmPartyConstants.Authorize.FUNCTION_NODE.id() ;
		SqlTableUtil stu = new SqlTableUtil(RM_AUTHORIZE_RESOURCE, "");
		SqlWhereEntity swe = new SqlWhereEntity();
		swe.putWhere("DR", "0", WhereEnum.EQUAL_STRING).putWhere("OLD_RESOURCE_ID", menuCode, WhereEnum.IN).putWhere("AUTHORIZE_ID", authorizeId, WhereEnum.EQUAL_STRING);
		stu.appendSelFiled("ID").appendSqlWhere(swe);
		List<Map<String, Object>> resourceList = dcmsDAO.query(stu);
		String resourceId = "" ; 
		for(Map<String,Object> resourceMap : resourceList){
			resourceId += resourceMap.get("ID") +",";
		}
		resourceId = resourceId.substring(0, resourceId.length()-1);
		
		//dcmsDAO.update(RM_AUTHORIZE_RESOURCE, map , new SqlWhereEntity().putWhere("DR", "0", WhereEnum.EQUAL_STRING).putWhere("ID", resourceId, WhereEnum.IN));
		dcmsDAO.update(RM_AUTHORIZE_RESOURCE_RECORD, map, new SqlWhereEntity().putWhere("DR", "0", WhereEnum.EQUAL_STRING).putWhere("PARTY_ID", roleId, WhereEnum.EQUAL_STRING).putWhere("AUTHORIZE_RESOURCE_ID", resourceId, WhereEnum.IN));
		//dcmsDAO.update(RM_BUTTON_RELATION_MENU, map, new SqlWhereEntity().putWhere("DR", "0", WhereEnum.EQUAL_STRING).putWhere("ID", IdRmrnUtil.getIdRmrnMap(jsonArray).get("IDS").toString(), WhereEnum.IN));
		
		String jsonMessage = "{\"message\":\""+result+"\"}";
		
		this.ajaxWrite(jsonMessage, request, response);
		return null;
	}

	@Override
	protected void afterOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
	}

}
