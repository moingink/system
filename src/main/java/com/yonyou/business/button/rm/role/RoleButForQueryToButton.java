package com.yonyou.business.button.rm.role;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yonyou.business.button.system.SystemButtonUtil;
import com.yonyou.util.BussnissException;
import com.yonyou.util.JsonUtils;
import com.yonyou.util.jdbc.IBaseDao;

/**
 * 角色按钮授权管理 查询
 * 
 * @author XIANGJIAN
 * @date 创建时间：2017年3月30日
 * @version 1.0
 */
public class RoleButForQueryToButton extends SystemButtonUtil {


	@Override
	protected boolean befortOnClick(IBaseDao dcmsDAO,
			HttpServletRequest request, HttpServletResponse response) {
		return false;
	}

	@Override
	protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) throws IOException, BussnissException {
		/*String dataSourceCode = request.getParameter("dataSourceCode");
		SqlTableUtil sqlEntity = DataSourceUtil.dataSourceToSQL(dataSourceCode);
		sqlEntity.appendSqlWhere(getQueryCondition(request));
		System.out.println("-------------------------"+roleId);
		sqlEntity.setJoinType(JoinEnum.LEFT);
		String pageParam=this.findPageParam(request);
		if(pageParam!=null&&pageParam.length()>0){
			sqlEntity.appendWhereAnd(pageParam);
		}
		String limitStr = request.getParameter("limit");
		String offsetStr = request.getParameter("offset");
		int limit = limitStr != null ? Integer.parseInt(limitStr) : 10;
		int offset = offsetStr != null ? Integer.parseInt(offsetStr) : 0;*/
		
		//2017-4-13  向建，修改sql
		String roleId = request.getParameter("roleId");
		String menuCode = request.getParameter("menuCode") ; 
		String menuName = request.getParameter("menuName") ; 
		StringBuffer sb = new StringBuffer() ; 
		sb.append("SELECT BM.ID,BM.MENU_ID,BM.MENU_CODE,BM.MENU_NAME,BM.BUTTON_ID,BM.BUTTON_CODE,BM.BUTTON_NAME,BM.UPDATE_AUTHORITY,BM.DR,BM.VERSION,");
		sb.append("(CASE (SELECT ARR1.UPDATE_AUTHORITY FROM RM_AUTHORIZE_RESOURCE R1 JOIN RM_AUTHORIZE_RESOURCE_RECORD ARR1 ON ARR1.AUTHORIZE_RESOURCE_ID = R1.ID WHERE BM.BUTTON_CODE = R1.TOTAL_CODE AND ARR1.PARTY_ID='"+roleId+"' AND ARR1.DR = '0' AND R1.DR = '0' limit 1) WHEN '000' THEN '已授权' WHEN '001' THEN '修改本人' WHEN '010' THEN '修改本机构' WHEN '100' THEN '修改所有' ELSE '未授权' END) AS 修改权限, ");
		sb.append(" CASE WHEN (SELECT ARR1.IS_BUTTON_AUTHORITY FROM RM_AUTHORIZE_RESOURCE R1  JOIN RM_AUTHORIZE_RESOURCE_RECORD ARR1 ON ARR1.AUTHORIZE_RESOURCE_ID = R1.ID WHERE BM.BUTTON_CODE = R1.TOTAL_CODE   AND ARR1.PARTY_ID='"+roleId+"' AND ARR1.DR = '0' AND R1.DR = '0' limit 1) = '1' THEN '是'  ELSE '否' END AS 是否授权,");
		sb.append("(select arr1.auth_level from rm_authorize_resource r1 join rm_authorize_resource_record arr1 on arr1.authorize_resource_id = r1.id where bm.button_code = r1.total_code and arr1.party_id='"+roleId+"' and arr1.dr = '0' and r1.dr = '0') as 授权级别");
		sb.append(" FROM RM_BUTTON_RELATION_MENU BM LEFT JOIN RM_AUTHORIZE_RESOURCE RAR ON BM.BUTTON_CODE = RAR.TOTAL_CODE WHERE BM.DR = 0 AND MENU_CODE = '"+menuCode+"' AND MENU_NAME = '"+menuName+"'  ORDER BY BM.BUTTON_CODE DESC");
		List<Map<String, Object>> mapList = dcmsDAO.query(sb.toString(), "");
		
		
		//List<Map<String, Object>> mapList = dcmsDAO.query(sqlEntity, offset, limit);
		/*mapList = RmDictReferenceUtil.transByDict(mapList, dataSourceCode);
		int total = 10;
		total = dcmsDAO.queryLength(sqlEntity);
		sqlEntity.clearTableMap();*/
		int total =mapList.size() ; 
		// 需要返回的数据有总记录数和行数据
		String json = "{\"total\":" + total + ",\"rows\":" + JsonUtils.object2json(mapList) + "}";
		this.ajaxWrite(json, request, response);
		return null;
	}

	@Override
	protected void afterOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
	}
}
