package com.yonyou.business.button.rm.role;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.util.RmPartyConstants;

import com.yonyou.business.button.system.SystemButtonUtil;
import com.yonyou.util.BussnissException;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.util.sql.SqlWhereEntity;
import com.yonyou.util.sql.SQLUtil.WhereEnum;

public class RoleButtonAuthNext extends SystemButtonUtil {

	/** 授权资源表 */
	private static final String RM_AUTHORIZE_RESOURCE = "RM_AUTHORIZE_RESOURCE";
	/** 功能节点表 即菜单表 */
	private static final String RM_AUTHORIZE_RESOURCE_RECORD = "RM_AUTHORIZE_RESOURCE_RECORD";
	/** 按钮表 */
	private static final String RM_BUTTON_RELATION_MENU = "RM_BUTTON_RELATION_MENU";

	private static final String OLD_RESOURCE_ID = "OLD_RESOURCE_ID";

	@Override
	protected boolean befortOnClick(IBaseDao dcmsDAO,
			HttpServletRequest request, HttpServletResponse response) {
		return false;
	}

	@Override
	protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request, HttpServletResponse response) throws IOException, BussnissException {
		String jsonMessage = "{\"message\":\"授权成功\"}";
		
		JSONArray jsonArray = JSONArray.fromObject(request.getParameter("jsonData"));
		String roleId = request.getParameter("roleId") == null ? "" : request.getParameter("roleId");
		String menuCode = request.getParameter("menuCode") == null ? "" : request.getParameter("menuCode");
		String menuName = request.getParameter("menuName") == null ? "" : request.getParameter("menuName");
		String level = request.getParameter("level") == null ? "" : request.getParameter("level");
		
		int jsonLen = jsonArray.size();
		String authorizeId = RmPartyConstants.Authorize.FUNCTION_NODE.id();
		String buttonCodeJson = "";
		String buttonCodeJsonArr[] = jsonLen > 0 ? new String[jsonLen] : null;
		if (jsonLen > 0) {
			for (int i = 0; i < jsonLen; i++) {
				JSONObject json = jsonArray.getJSONObject(i);
				buttonCodeJson += json.get("BUTTON_CODE") + ",";
			}
			buttonCodeJson = buttonCodeJson.substring(0, buttonCodeJson.length() - 1);
		}
		if (buttonCodeJsonArr != null) {
			buttonCodeJsonArr = buttonCodeJson.split(",");
		}
		List<Map<String, Object>> buttonList = this.getMenuChildrenButton(dcmsDAO, menuCode);
		String buttonCodes = "";
		if (buttonList.size() == 0) {
			jsonMessage = "{\"message\":\"您所选菜单暂未关联按钮，请选前往“按钮管理”关联按钮！\"}";
		} else {
			SqlTableUtil verStu = new SqlTableUtil(RM_AUTHORIZE_RESOURCE, "");
			SqlWhereEntity verSwe = new SqlWhereEntity();
			verSwe.putWhere("DR", "0", WhereEnum.EQUAL_STRING)
					.putWhere(OLD_RESOURCE_ID, menuCode, WhereEnum.EQUAL_STRING)
					.putWhere("AUTHORIZE_ID", authorizeId,
							WhereEnum.EQUAL_STRING);
			verStu.appendSelFiled("*").appendSqlWhere(verSwe);
			Map<String, Object> verMap = dcmsDAO.find(verStu);
			if (verMap.size() == 0) {
				grantToMenu(dcmsDAO, authorizeId, menuCode, menuName, roleId, verMap);
			}
			for (Map<String, Object> map : buttonList) {
				buttonCodes += map.get("BUTTON_CODE").toString() + ",";
			}
			buttonCodes = buttonCodes.substring(0, buttonCodes.length() - 1);
			SqlTableUtil sourceStu = new SqlTableUtil(RM_AUTHORIZE_RESOURCE, "");
			SqlWhereEntity sourceSwe = new SqlWhereEntity();
			sourceSwe.putWhere(OLD_RESOURCE_ID, buttonCodes, WhereEnum.IN)
					.putWhere("DR", "0", WhereEnum.EQUAL_STRING)
					.putWhere("AUTHORIZE_ID", authorizeId, WhereEnum.EQUAL_STRING);
			sourceStu.appendSelFiled("*").appendSqlWhere(sourceSwe);
			List<Map<String, Object>> sourceList = dcmsDAO.query(sourceStu);
			String sourceId = "";
			if (sourceList.size() > 0) {
				for (Map<String, Object> map : sourceList) {
					sourceId += map.get("OLD_RESOURCE_ID").toString() + ",";
				}
				sourceId = sourceId.substring(0, sourceId.length() - 1);
			}
			int sourceLen = sourceList.size();
			List<Map<String, Object>> insertSourceList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> updateSourceList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> insertRecordList = new ArrayList<Map<String, Object>>();
			int menuButtonLen = buttonList.size();
			if (sourceLen < menuButtonLen) {
				for (Map<String, Object> map : buttonList) {
					Map<String, Object> sourceMap = new HashMap<String, Object>();
					if (!sourceId.contains(map.get("BUTTON_CODE").toString())) {
						sourceMap.put("AUTHORIZE_ID", authorizeId);
						sourceMap.put("OLD_RESOURCE_ID", map.get("BUTTON_CODE"));
						sourceMap.put("DEFAULT_ACCESS", RmPartyConstants.RM_NO);
						sourceMap.put("DEFAULT_IS_AFFIX_DATA", RmPartyConstants.RM_NO);
						sourceMap.put("DEFAULT_IS_RECURSIVE", RmPartyConstants.RM_NO);
						sourceMap.put("DEFAULT_ACCESS_TYPE", "1");
						sourceMap.put("TOTAL_CODE", map.get("BUTTON_CODE"));
						sourceMap.put("NAME", map.get("BUTTON_NAME"));
						insertSourceList.add(sourceMap);
					}
				}
				dcmsDAO.insert(RM_AUTHORIZE_RESOURCE, insertSourceList);
			}

			String sql = "SELECT RAR.ID,RAR.OLD_RESOURCE_ID FROM RM_AUTHORIZE_RESOURCE RAR JOIN RM_AUTHORIZE_RESOURCE_RECORD RARR ON RAR.ID = RARR.AUTHORIZE_RESOURCE_ID WHERE RARR.IS_BUTTON_AUTHORITY = '1' AND RARR.DR = 0 AND RAR.DR=0 AND RARR.PARTY_ID = "
					+ roleId
					+ " AND RAR.AUTHORIZE_ID = "
					+ authorizeId
					+ " AND RAR.TOTAL_CODE IN (" + buttonCodes + ")";
			List<Map<String, Object>> allRecord = dcmsDAO.query(sql, "");
			int au_sourceLen = allRecord.size();

			String deleteRecordIds = "";
			if (jsonLen == 0 && au_sourceLen == 0) {
				jsonMessage = "{\"message\":\"授权成功！\"}";
			} else if (jsonLen > 0 && au_sourceLen == 0) {
				String au_sql = "SELECT ID FROM RM_AUTHORIZE_RESOURCE WHERE OLD_RESOURCE_ID IN ("
						+ buttonCodeJson
						+ ") AND AUTHORIZE_ID = "
						+ authorizeId + "  AND DR = 0 ";
				List<Map<String, Object>> allSource = dcmsDAO.query(au_sql, "");
				for (Map<String, Object> map : allSource) {
					Map<String, Object> entity = new HashMap<String, Object>();
					entity.put("AUTHORIZE_RESOURCE_ID", map.get("ID").toString());
					entity.put("PARTY_ID", roleId);
					entity.put("AUTHORIZE_STATUS", RmPartyConstants.RM_YES);
					entity.put("IS_AFFIX_DATA", RmPartyConstants.RM_NO);
					entity.put("IS_RECURSIVE", RmPartyConstants.RM_NO);
					entity.put("ACCESS_TYPE", RmPartyConstants.RM_YES);
					entity.put("UPDATE_AUTHORITY", "000");
					entity.put("IS_BUTTON_AUTHORITY", "1");
					entity.put("AUTH_LEVEL", level);
					insertRecordList.add(entity);
				}
			} else if (jsonLen == 0) {
				String sql2 = "SELECT ID FROM RM_AUTHORIZE_RESOURCE WHERE OLD_RESOURCE_ID IN ("
						+ buttonCodes
						+ ") AND AUTHORIZE_ID = "
						+ authorizeId
						+ "  AND DR = 0 ";
				List<Map<String, Object>> allSource = dcmsDAO.query(sql2, "");
				for (Map<String, Object> map : allSource) {
					deleteRecordIds += map.get("ID") + ",";
				}
				deleteRecordIds = deleteRecordIds.substring(0, deleteRecordIds.length() - 1);
			} else {
				String functionCodes = "";
				for (Map<String, Object> map : allRecord) {
					functionCodes += map.get(OLD_RESOURCE_ID) + ",";
				}
				String[] functionCodeArr = null;
				if (functionCodes.length() > 0) {
					functionCodes = functionCodes.substring(0,
							functionCodes.length() - 1);
					functionCodeArr = functionCodes.split(",");
				}
				String insertStr = "";
				for (int i = 0; i < jsonLen; i++) {
					if (!functionCodes.contains(buttonCodeJsonArr[i])) {
						insertStr += buttonCodeJsonArr[i] + ",";
					}
				}
				if (insertStr.length() > 0) {
					String sqlInsertId = "SELECT ID FROM RM_AUTHORIZE_RESOURCE  WHERE OLD_RESOURCE_ID IN ("
							+ insertStr.subSequence(0, insertStr.length() - 1)
							+ ") AND AUTHORIZE_ID = "
							+ authorizeId
							+ " AND DR=0";
					List<Map<String, Object>> beforeInsertList = dcmsDAO.query(
							sqlInsertId, "");
					for (Map<String, Object> map : beforeInsertList) {
						Map<String, Object> recordMap = new HashMap<String, Object>();
						recordMap.put("AUTHORIZE_RESOURCE_ID", map.get("ID"));
						recordMap.put("PARTY_ID", roleId);
						recordMap.put("AUTHORIZE_STATUS", RmPartyConstants.RM_YES);
						recordMap.put("IS_AFFIX_DATA", RmPartyConstants.RM_NO);
						recordMap.put("IS_RECURSIVE", RmPartyConstants.RM_NO);
						recordMap.put("ACCESS_TYPE", RmPartyConstants.RM_YES);
						recordMap.put("UPDATE_AUTHORITY", "000");
						recordMap.put("IS_BUTTON_AUTHORITY", "1");
						recordMap.put("AUTH_LEVEL", level);
						insertRecordList.add(recordMap);
					}
				}
				String beforeDeleteId = "";
				String beforeDeleteIdArr[] = null;
				for (Map<String, Object> map : allRecord) {
					beforeDeleteId += map.get("ID") + ",";
				}
				;
				if (beforeDeleteId.length() > 0) {
					beforeDeleteId = beforeDeleteId.substring(0,
							beforeDeleteId.length() - 1);
					beforeDeleteIdArr = beforeDeleteId.split(",");
				}
				String deleteId = "";
				for (int i = 0; i < au_sourceLen; i++) {
					if (beforeDeleteIdArr != null) {
						if (!buttonCodeJson.contains(functionCodeArr[i])) {
							deleteId += functionCodeArr[i] + ",";
						}
					}
				}
				if (deleteId.length() > 0) {
					String sqlDeleteId = "SELECT RAR.ID FROM RM_AUTHORIZE_RESOURCE RAR JOIN RM_AUTHORIZE_RESOURCE_RECORD RARR ON RAR.ID = RARR.AUTHORIZE_RESOURCE_ID WHERE RAR.OLD_RESOURCE_ID IN ("
							+ deleteId.subSequence(0, deleteId.length() - 1)
							+ ") AND RARR.PARTY_ID="
							+ roleId
							+ " AND RAR.AUTHORIZE_ID = "
							+ authorizeId
							+ " AND RARR.DR=0 AND RAR.DR=0";
					List<Map<String, Object>> beforeInsertList = dcmsDAO.query(sqlDeleteId, "");
					for (Map<String, Object> map : beforeInsertList) {
						deleteRecordIds += map.get("ID") + ",";
					}
					deleteRecordIds = deleteRecordIds.substring(0, deleteRecordIds.length() - 1);
				}
			}
			if (updateSourceList.size() > 0) {
				dcmsDAO.update(RM_AUTHORIZE_RESOURCE, updateSourceList, new SqlWhereEntity());
			}
			if (insertRecordList.size() > 0) {
				dcmsDAO.insert(RM_AUTHORIZE_RESOURCE_RECORD, insertRecordList);
			}
			if (!"".equals(deleteRecordIds)) {
				dcmsDAO.delete(RM_AUTHORIZE_RESOURCE_RECORD, new SqlWhereEntity()
					.putWhere("AUTHORIZE_RESOURCE_ID", deleteRecordIds, WhereEnum.IN)
					.putWhere("PARTY_ID", roleId, WhereEnum.EQUAL_STRING));
			}
		}
		this.ajaxWrite(jsonMessage, request, response);
		return null;
	}

	@Override
	protected void afterOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO 自动生成的方法存根

	}

	/**
	 * 将菜单授权给角色
	 * 
	 * @param dcmsDAO
	 * @param authorizeId
	 * @param menuCode
	 * @param menuName
	 * @param roleId
	 * @param verMap
	 */
	private void grantToMenu(IBaseDao dcmsDAO, String authorizeId,
			String menuCode, String menuName, String roleId,
			Map<String, Object> verMap) {
		Map<String, Object> sourceMap = new HashMap<String, Object>();
		sourceMap.put("AUTHORIZE_ID", authorizeId);
		sourceMap.put("OLD_RESOURCE_ID", menuCode);
		sourceMap.put("DEFAULT_ACCESS", RmPartyConstants.RM_NO);
		sourceMap.put("DEFAULT_IS_AFFIX_DATA", RmPartyConstants.RM_NO);
		sourceMap.put("DEFAULT_IS_RECURSIVE", RmPartyConstants.RM_NO);
		sourceMap.put("DEFAULT_ACCESS_TYPE", "1");
		sourceMap.put("TOTAL_CODE", menuCode);
		sourceMap.put("NAME", menuName);
		String sourceId = dcmsDAO.insert(RM_AUTHORIZE_RESOURCE, sourceMap);

		Map<String, Object> recordMap = new HashMap<String, Object>();
		recordMap.put("AUTHORIZE_RESOURCE_ID", sourceId);
		recordMap.put("PARTY_ID", roleId);
		recordMap.put("AUTHORIZE_STATUS", RmPartyConstants.RM_YES);
		recordMap.put("IS_AFFIX_DATA", RmPartyConstants.RM_NO);
		recordMap.put("IS_RECURSIVE", RmPartyConstants.RM_NO);
		recordMap.put("ACCESS_TYPE", "1");
		dcmsDAO.insert(RM_AUTHORIZE_RESOURCE_RECORD, recordMap);
	}

	/**
	 * 获取该菜单下已关联的按钮
	 * 
	 * @param dcmsDAO
	 * @param menuCode
	 * @return
	 */
	private List<Map<String, Object>> getMenuChildrenButton(IBaseDao dcmsDAO,
			String menuCode) {
		SqlTableUtil buttonStu = new SqlTableUtil(RM_BUTTON_RELATION_MENU, "");
		SqlWhereEntity buttonSwe = new SqlWhereEntity();
		buttonSwe.putWhere("DR", "0", WhereEnum.EQUAL_STRING).putWhere("MENU_CODE", menuCode, WhereEnum.EQUAL_STRING);
		buttonStu.appendSelFiled("BUTTON_CODE,BUTTON_NAME").appendSqlWhere(buttonSwe);
		return dcmsDAO.query(buttonStu);
	}

}
