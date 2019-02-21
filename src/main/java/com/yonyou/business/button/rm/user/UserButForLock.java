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
import org.util.tools.helper.RmStringHelper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import com.yonyou.business.button.demobutton.DemoButton;
import com.yonyou.util.BussnissException;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SqlWhereEntity;

public class UserButForLock extends DemoButton {

	@Override
	protected boolean befortOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) throws IOException, BussnissException {

		// TODO 自动生成的方法存根
		String jsonMessage = "";
		String dataSourceCode =request.getParameter("dataSourceCode");
		String tabName = findTableNameByDataSourceCode(dataSourceCode);
		JSONArray jsonArray = JSONArray.fromObject(request.getParameter("jsonData"));
		System.out.println("jsonArray:"+jsonArray);
		String[] ids = new String[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jb = (JSONObject) jsonArray.get(i);
			String id = jb.getString("ID");
			ids[i] = id;
		}
		if(ids.length>0){
			List<Map<String, Object>> entityList = new ArrayList<Map<String, Object>>();
			Map<String, Object> entity = new HashMap<String, Object>();
			Map<String,String> whereMap = new HashMap<String,String>();
			SqlWhereEntity sqlWhere = new SqlWhereEntity();
			
	 		//rm_user	锁定用户
	 		entity.put("LOCK_STATUS","0");
			entityList.add(entity);
			whereMap.put("ID", "ID in ("+RmStringHelper.parseToString(ids)+")" );
			sqlWhere.setWhereMap(whereMap);
	 		dcmsDAO.update(RmPartyConstants.rmUser, entityList, sqlWhere);
	 		entity.clear();
	 		entityList.clear();
	 		whereMap.clear();
	 		sqlWhere.clear();
	 		
	 		//更新密码历史锁定时间
	 		for(int i=0;i<ids.length;i++){
	 			Timestamp invalidDate = RmDateHelper.getSysTimestamp();
	 			entity.put("INVALID_DATE",invalidDate);
	 			entityList.add(entity);
	 			whereMap.put("ID", " ID = (SELECT * FROM (SELECT RM_PASSWORD_HISTORY.ID FROM RM_PASSWORD_HISTORY WHERE RM_PASSWORD_HISTORY.USER_ID = "+ids[i]+" ORDER BY RM_PASSWORD_HISTORY.MODIFY_DATE DESC ) WHERE ROWNUM <= 1)" );
	 			sqlWhere.setWhereMap(whereMap);
	 			dcmsDAO.update(RmPartyConstants.rmPasswordHistory, entityList, sqlWhere);
	 			entity.clear();
	 			entityList.clear();
	 			whereMap.clear();
	 			sqlWhere.clear();
	 		}
		}
 		jsonMessage = "{\"message\":\"锁定成功\"}";
		
		this.ajaxWrite(jsonMessage, request, response);
		return null;
	}

	@Override
	protected void afterOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO 自动生成的方法存根
		
	}

}
