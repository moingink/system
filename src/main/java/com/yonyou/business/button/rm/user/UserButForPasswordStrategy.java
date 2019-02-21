package com.yonyou.business.button.rm.user;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.util.RmPartyConstants;
import org.util.tools.helper.RmDateHelper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import com.yonyou.business.button.ButtonAbs;
import com.yonyou.util.BussnissException;
import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.util.sql.SqlWhereEntity;

public class UserButForPasswordStrategy extends ButtonAbs {

	@Override
	protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) throws IOException, BussnissException {
		// TODO 自动生成的方法存根
		String jsonMessage = "";
		String dataSourceCode =request.getParameter("dataSourceCode");
		String tabName = findTableNameByDataSourceCode(dataSourceCode);
		JSONArray jsonArray = JSONArray.fromObject(request.getParameter("jsonData"));
		System.out.println("jsonArray:"+jsonArray);
		JSONObject user = (JSONObject) jsonArray.get(jsonArray.size()-1);
		
		String userId = user.getString("userId");
		String[] ids = new String[jsonArray.size()-1];
		for (int i = 0; i < jsonArray.size()-1; i++) {
			JSONObject jb = (JSONObject) jsonArray.get(i);
			String id = jb.getString("ID");
			ids[i] = id;
		}
		
		//先删除用户已配置的密码策略
		Map<String,String> mWhere = new HashMap<String,String>();
		mWhere.put("USER_ID", "USER_ID = '"+userId+"' " );
		SqlWhereEntity whereEntity = new SqlWhereEntity();
		whereEntity.setWhereMap(mWhere);
		dcmsDAO.delete(RmPartyConstants.rmPasswordStrategyUser, whereEntity);
		
		Timestamp sysTimestamp = RmDateHelper.getSysTimestamp();
		if(ids.length>0){
			//新增密码策略
			List<Map<String,Object>> entityList = new ArrayList<Map<String,Object>>();
			for(int i=0;i<ids.length;i++){
				Map<String,Object> entity = new HashMap<String,Object>();
				entity.put("USER_ID", userId);
				entity.put("PASSWORD_STRATEGY_ID", ids[i]);
				entity.put("MODIFY_DATE", sysTimestamp);
				entityList.add(entity);				
			}
			dcmsDAO.insert(RmPartyConstants.rmPasswordStrategyUser, entityList);
		}
		
 		jsonMessage = "{\"message\":\"设置成功\"}";
		
		this.ajaxWrite(jsonMessage, request, response);
		return null;
	}

	/**
	 * 验证角色是否被用户使用
	 * @param roleId
	 * @return
	 */
	protected boolean isUsing(String roleId){
		boolean status = true;
		if(!roleId.isEmpty()){
			SqlTableUtil sql = new SqlTableUtil(RmPartyConstants.rmPartyRole,"");
			sql.init("ID", "");
			sql.appendWhereAnd(" ROLE_ID = '"+roleId+"' ");
			sql.appendWhereAnd(" DR = '0' ");
			List<Map<String, Object>> mapList = BaseDao.getBaseDao().query(sql);
			if(mapList.isEmpty()){
				status = false;
			}
		}
		return status;
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
