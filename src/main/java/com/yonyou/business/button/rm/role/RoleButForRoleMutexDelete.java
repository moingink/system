package com.yonyou.business.button.rm.role;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.util.RmPartyConstants;
import org.util.tools.helper.RmStringHelper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.yonyou.business.button.ButtonAbs;
import com.yonyou.util.BussnissException;
import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.util.sql.SqlWhereEntity;

public class RoleButForRoleMutexDelete extends ButtonAbs {

	@Override
	protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) throws IOException, BussnissException {
		// TODO 自动生成的方法存根
		//String dataSourceCode =request.getParameter("dataSourceCode");
		//String tabName = findTableNameByDataSourceCode(dataSourceCode);
		JSONArray jsonArray = JSONArray.fromObject(request.getParameter("jsonData"));
		String ids[] = new String[jsonArray.size()*2];
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jb = (JSONObject) jsonArray.get(i);
			String id = jb.getString("ID");
			ids[i]=id;
			//查询角色关系对应的另一条角色关系，同步删除。
			SqlTableUtil RoleSql = new SqlTableUtil(RmPartyConstants.rmRoleMutex,"");
			RoleSql.appendSelFiled("ROLE_ID");
			RoleSql.appendSelFiled("MUTEX_ROLE_ID");
			RoleSql.appendWhereAnd(" ID = '"+id+"' ");
			RoleSql.appendWhereAnd(" DR = '0' ");
 			Map<String, Object> Role = BaseDao.getBaseDao().find(RoleSql);
 			
 			SqlTableUtil mutexRoleSql = new SqlTableUtil(RmPartyConstants.rmRoleMutex,"");
			mutexRoleSql.appendSelFiled("ID");
			mutexRoleSql.appendWhereAnd(" ROLE_ID = '"+Role.get("MUTEX_ROLE_ID").toString()+"' ");
			mutexRoleSql.appendWhereAnd(" MUTEX_ROLE_ID = '"+Role.get("ROLE_ID").toString()+"' ");
			mutexRoleSql.appendWhereAnd(" DR = '0' ");
 			Map<String, Object> mutexRole = BaseDao.getBaseDao().find(mutexRoleSql);
			ids[jsonArray.size()+i] = mutexRole.get("ID")==null?"":mutexRole.get("ID").toString();
		}
		
		
		Map<String,String> mWhere = new HashMap<String,String>();
		mWhere.put("ID", "ID IN ("+RmStringHelper.parseToSQLStringApos(ids)+") " );
		SqlWhereEntity whereEntity = new SqlWhereEntity();
		whereEntity.setWhereMap(mWhere);
		dcmsDAO.delete(RmPartyConstants.rmRoleMutex, whereEntity);
		
		String jsonMessage = "{\"message\":\"删除成功\"}";
		
		this.ajaxWrite(jsonMessage, request, response);
		return null;
	}

	@Override
	protected boolean befortOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	protected void afterOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO 自动生成的方法存根
		
	}

}
