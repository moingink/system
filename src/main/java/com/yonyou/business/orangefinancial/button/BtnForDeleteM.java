package com.yonyou.business.orangefinancial.button;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.util.tools.helper.RmStringHelper;

import com.yonyou.business.button.util.ButForPhysicalDelete;
import com.yonyou.util.BussnissException;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SqlWhereEntity;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class BtnForDeleteM extends ButForPhysicalDelete{
	@Override
	protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) throws IOException, BussnissException {
		// TODO 自动生成的方法存根
		String dataSourceCode =request.getParameter("dataSourceCode");
		String tabName = findTableNameByDataSourceCode(dataSourceCode);
		JSONArray jsonArray = JSONArray.fromObject(request.getParameter("jsonData"));
		String ids[] = new String[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jb = (JSONObject) jsonArray.get(i);
			String id = jb.getString("ID");
			ids[i]=id;
		}
		
		
		Map<String,String> mWhere = new HashMap<String,String>();
		mWhere.put("ID", "ID IN ("+RmStringHelper.parseToSQLStringApos(ids)+") " );
		SqlWhereEntity whereEntity = new SqlWhereEntity();
		whereEntity.setWhereMap(mWhere);
		dcmsDAO.delete(tabName, whereEntity);
//		子表删除——begin
		Map<String,String> bWhere = new HashMap<String,String>();
		bWhere.put("PID", "PID IN ("+RmStringHelper.parseToSQLStringApos(ids)+") " );
		SqlWhereEntity whereBEntity = new SqlWhereEntity();
		whereBEntity.setWhereMap(bWhere);
		dcmsDAO.delete("BS_MD_DEF_B", whereBEntity);	
		
//		子表结束——end
		String jsonMessage = "{\"message\":\"删除成功\"}";
		
		this.ajaxWrite(jsonMessage, request, response);
		return null;
	}
}
