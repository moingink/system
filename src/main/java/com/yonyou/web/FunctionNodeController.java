package com.yonyou.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.util.RmPartyConstants;

import com.yonyou.business.DataSourceUtil;
import com.yonyou.business.RmDictReferenceUtil;
import com.yonyou.business.api.B001005001_001;
import com.yonyou.business.entity.TokenEntity;
import com.yonyou.business.entity.TokenUtil;
import com.yonyou.util.BussnissException;
import com.yonyou.util.JsonUtils;
import com.yonyou.util.TreeUtil;
import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.sql.SqlTableUtil;

/**
 * 
 * @ClassName: FunctionNodeController
 * @Description: 菜单结构控制类
 * @author xiaotao
 * @date 2017年2月15日
 *
 */
@RestController
@RequestMapping(value = "/functionNode")
public class FunctionNodeController extends BaseController {

	/**
	 * 获取菜单树，用于菜单管理
	 * 
	 * @param request
	 * @param response
	 * @throws BussnissException 
	 */
	@RequestMapping(value = "ajax", method = RequestMethod.GET)
	public void ajaxDatas(HttpServletRequest request,
			HttpServletResponse response) throws BussnissException {
		String dataSourceCode = "RM_FUNCTION_NODE";

		SqlTableUtil sqlEntity = DataSourceUtil.dataSourceToSQL(dataSourceCode);
		String totalCode= request.getParameter("TOTAL_CODE");
		
		String condition = " DR = '0' AND ENABLE_STATUS='1' ";
		if(null!=totalCode && !"".equals(totalCode.trim())){
			condition=condition+" AND TOTAL_CODE like '"+totalCode+"%'";
		}
		sqlEntity.appendWhereAnd(condition);
		List<Map<String, Object>> mapList = BaseDao.getBaseDao().query(
				sqlEntity);
		mapList = RmDictReferenceUtil.transByDict(mapList, dataSourceCode);
		int total = 10;
		total = BaseDao.getBaseDao().queryLength(sqlEntity);
		// sqlEntity.clearTableMap();
		// 需要返回的数据有总记录数和行数据
		String json1 = "{\"total\":" + total + ",\"rows\":"
				+ JsonUtils.object2json(mapList) + "}";
		// this.ajaxWrite(json, request, response);
		System.out.println("json1:" + json1);

		response.setCharacterEncoding("utf-8");
		try {
			PrintWriter out = response.getWriter();
			String json = TreeUtil.getOrgTree(mapList);// toJOSNString(mapList);
			System.out.println("json:" + json);
			out.print(json);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

	}

	@RequestMapping(value = "sub", method = RequestMethod.GET)
	public void getSubCode(HttpServletRequest request,
			HttpServletResponse response) {
		String tableName = request.getParameter("dataSourceCode");
		String totalCode = request.getParameter("totalCode");

		SqlTableUtil sql = new SqlTableUtil(tableName, "");

		sql.init("MAX(TOTAL_CODE)", "");
		sql.appendWhereAnd(" TOTAL_CODE LIKE '" + totalCode + "%'");
		sql.appendWhereAnd("LENGTH(TOTAL_CODE) = " + (totalCode.length() + 3));

		List<Map<String, Object>> mapList = BaseDao.getBaseDao().query(sql);

		int total = 10;
		total = BaseDao.getBaseDao().queryLength(sql);
		// 需要返回的数据有总记录数和行数据
		String json1 = "{\"total\":" + total + ",\"rows\":"
				+ JsonUtils.object2json(mapList) + "}";
		System.out.println("**************json1:" + json1);

		response.setCharacterEncoding("utf-8");
		try {
			PrintWriter out = response.getWriter();

			String json = "";
			if (!mapList.isEmpty()) {
				Map<String, Object> jsoMap = new HashMap<String, Object>();
				if (mapList.get(0).get("MAX(TOTAL_CODE)") == null) {
					jsoMap.put("subCode", totalCode + "100");
				} else {
					jsoMap.put("subCode", mapList.get(0).get("MAX(TOTAL_CODE)"));
				}
				json = JsonUtils.object2json(jsoMap);
			}

			System.out.println("*************json:" + json);
			out.print(json);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	/**
	 * 获取菜单树，用于角色授权
	 * 
	 * @param request
	 * @param response
	 * @throws BussnissException
	 */
	@RequestMapping(value = "authorizeTree", method = RequestMethod.GET)
	public void authorizeTree(HttpServletRequest request,
			HttpServletResponse response) throws BussnissException {
		String roleId = request.getParameter("roleId");
		String authorize_id = RmPartyConstants.Authorize.FUNCTION_NODE.id();
		String retJson = "";
		List<Map<String, Object>> menuList = null;
		TokenEntity tokenEntity = TokenUtil.initTokenEntity(request);
		if (!tokenEntity.isEmpty()) {
			if (!tokenEntity.USER.getLoginId().equals("admin")) {
				// 只能授权登录人已被授权的資源
				String userId = tokenEntity.USER.getId();
				String companyId = tokenEntity.COMPANY.getCompany_id();
				// 根据用户ID及所在公司ID，从RM_ROLE和RM_PARTY_ROLE表同时过滤后的 ROLE_ID列表
				// "select R.ID,R.ROLE_CODE,R.NAME from RM_ROLE R join RM_PARTY_ROLE PR on R.ID = PR.ROLE_ID where PR.OWNER_PARTY_ID = '"+userId+"' and ((R.IS_SYSTEM_LEVEL = '1' and (PR.OWNER_ORG_ID IS NULL or PR.OWNER_ORG_ID ='"+companyId+"' )) or (r.is_system_level = '0' and R.OWNER_ORG_ID ='"+companyId+"')) and r.Enable_Status='1'";
				// 全局角色挂组织
				String sql = "select R.ID,R.ROLE_CODE,R.NAME from RM_ROLE R join RM_PARTY_ROLE PR on R.ID = PR.ROLE_ID where PR.OWNER_PARTY_ID = '"
						+ userId
						+ "' and ((R.IS_SYSTEM_LEVEL = '1' and ((R.OWNER_ORG_ID IS NULL or r.owner_org_id ='')or((R.OWNER_ORG_ID IS NOT NULL or r.owner_org_id ='' )AND EXISTS (SELECT id FROM rm_party_relation rr WHERE rr.party_view_id = '"
						+ RmPartyConstants.viewId
						+ "' AND rr.parent_party_id = R.OWNER_ORG_ID "
						+ RmPartyConstants.recurPartySql(companyId)
						+ ")) )) or (r.is_system_level = '0' and R.OWNER_ORG_ID ='"
						+ companyId + "')) and r.Enable_Status='1'";
				List<Map<String, Object>> roleCommonList = BaseDao.getBaseDao()
						.query(sql, "");
				if (roleCommonList != null && roleCommonList.size() > 0) {
					String[] roleIds = new String[roleCommonList.size()];
					for (int i = 0; i < roleCommonList.size(); i++) {
						Map<String, Object> map = roleCommonList.get(i);
						roleIds[i] = map.get("ID").toString();
					}
					String menuSql = B001005001_001
							.sqlJoinAuthorize_resource(
									roleIds,
									"RM_FUNCTION_NODE",
									"RM_FUNCTION_NODE",
									"TOTAL_CODE",
									"SUBSTR(RM_FUNCTION_NODE.TOTAL_CODE,1,LENGTH(RM_FUNCTION_NODE.TOTAL_CODE)-3) AS PARENT_TOTAL_CODE,RM_FUNCTION_NODE.ICON,RM_FUNCTION_NODE.TOTAL_CODE,RM_FUNCTION_NODE.NAME,RM_FUNCTION_NODE.ID,RM_FUNCTION_NODE.IS_LEAF,RM_FUNCTION_NODE.NODE_TYPE,RM_FUNCTION_NODE.FUNCTION_PROPERTY,RM_FUNCTION_NODE.ORDER_CODE,RM_FUNCTION_NODE.DATA_VALUE,RM_FUNCTION_NODE.PATTERN_VALUE,RM_FUNCTION_NODE.ICON HELP_NAME",
									"and RM_FUNCTION_NODE.DR='0' and RM_FUNCTION_NODE.ENABLE_STATUS='1' and RM_FUNCTION_NODE.NODE_TYPE!='3' OR RM_FUNCTION_NODE.ID = '1000201400000000106' order by SUBSTR(RM_FUNCTION_NODE.TOTAL_CODE, 1, LENGTH(RM_FUNCTION_NODE.TOTAL_CODE)-3),RM_FUNCTION_NODE.ORDER_CODE");
					menuList = BaseDao.getBaseDao().query(menuSql, "");

					// 查询角色下已有的菜单授权
					// 查询出RM_AUTHORIZE_RESOURCE_RECORD已有的相关记录
					SqlTableUtil recordEntity = new SqlTableUtil(
							RmPartyConstants.rmAuthorizeResourceRecord, "arr");
					recordEntity.appendSelFiled("ar.TOTAL_CODE");
					recordEntity.appendJoinTable(
							RmPartyConstants.rmAuthorizeResource, "ar",
							"arr.AUTHORIZE_RESOURCE_ID=ar.ID AND ar.DR = '0' ");
					recordEntity.appendWhereAnd(" arr.PARTY_ID = '" + roleId
							+ "' ");
					recordEntity.appendWhereAnd(" ar.AUTHORIZE_ID = '"
							+ authorize_id + "' ");
					recordEntity.appendWhereAnd(" arr.DR = '0' ");
					recordEntity.appendOrderBy("id ");
					List<Map<String, Object>> resource = BaseDao.getBaseDao()
							.query(recordEntity);

					String authorizeIds[] = new String[resource.size()];
					for (int i = 0; i < resource.size(); i++) {
						authorizeIds[i] = resource.get(i).get("TOTAL_CODE")
								.toString();
					}
					retJson = TreeUtil.getMarkedOrgTree(menuList, authorizeIds);
				} else {
					throw new BussnissException("未获取到用户ID为" + userId
							+ "的相关角色信息！");
				}
			} else {
				//如果是admin用戶的话就显示全部资源用以给角色授权
				String dataSourceCode = "RM_FUNCTION_NODE";
				SqlTableUtil sqlEntity = DataSourceUtil
						.dataSourceToSQL(dataSourceCode);
				//String condition = "  enable_status = 1 ";
				//String condition1 = " STATUS =0 ";
				//sqlEntity.appendWhereAnd(condition);
				//sqlEntity.appendWhereAnd(condition1);
				menuList = BaseDao.getBaseDao().query(sqlEntity);
				// 查询出RM_AUTHORIZE_RESOURCE_RECORD已有的相关记录
				SqlTableUtil recordEntity = new SqlTableUtil(
						RmPartyConstants.rmAuthorizeResourceRecord, "arr");
				recordEntity.appendSelFiled("ar.TOTAL_CODE");
				recordEntity.appendJoinTable(
						RmPartyConstants.rmAuthorizeResource, "ar",
						"arr.AUTHORIZE_RESOURCE_ID=ar.ID AND ar.DR = '0' ");
				recordEntity
						.appendWhereAnd(" arr.PARTY_ID = '" + roleId + "' ");
				recordEntity.appendWhereAnd(" ar.AUTHORIZE_ID = '"
						+ authorize_id + "' ");
				recordEntity.appendWhereAnd(" arr.DR = '0' ");
				
				recordEntity.appendOrderBy("id ");
				List<Map<String, Object>> resource = BaseDao.getBaseDao()
						.query(recordEntity);

				String authorizeIds[] = new String[resource.size()];
				for (int i = 0; i < resource.size(); i++) {
					authorizeIds[i] = resource.get(i).get("TOTAL_CODE")
							.toString();
				}
				//retJson = TreeUtil.toTreeJOSN(menuList);
				 retJson =TreeUtil.getMarkedOrgTree(menuList,authorizeIds);
			}

		} else {
			throw new BussnissException("未获取到当前登录人的token信息！");
		}

		response.setCharacterEncoding("utf-8");
		try {
			PrintWriter out = response.getWriter();
			System.out.println("json:" + retJson);
			out.print(retJson);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

	}

}
