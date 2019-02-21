package com.yonyou.business.button.system.button;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.yonyou.business.button.system.SystemButtonUtil;
import com.yonyou.util.BussnissException;
import com.yonyou.util.IdRmrnUtil;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SQLUtil.WhereEnum;
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.util.sql.SqlWhereEntity;

public class SystemButtonButForUnbind extends SystemButtonUtil {

	@Override
	protected boolean befortOnClick(IBaseDao dcmsDAO,
			HttpServletRequest request, HttpServletResponse response) {
		
		return false;
	}

	@Override
	protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) throws IOException, BussnissException {
		JSONArray jsonArray = this.getJsonArray(request);
		String jsonMessage = "{\"message\":\"解除绑定成功\"}";
		//SqlTableUtil stu = new SqlTableUtil("RM_BUTTON_RELATION_MENU","");
		SqlWhereEntity whereEntity = new SqlWhereEntity();
		whereEntity.putWhere("ID", IdRmrnUtil.getIdRmrnMap(jsonArray).get("IDS").toString(), WhereEnum.IN);
		/*List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jb = (JSONObject) jsonArray.get(i);
			String id = jb.getString("ID");
			//将删除关联表的记录放入该relMap下
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ID", id);
			map.put("DR","1");
			list.add(map);
		}
		dcmsDAO.update("RM_BUTTON_RELATION_MENU", list, new SqlWhereEntity());
*/		dcmsDAO.deleteByTransfrom("RM_BUTTON_RELATION_MENU", whereEntity);
		this.ajaxWrite(jsonMessage, request, response);
		return null;
	}

	@Override
	protected void afterOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
	}

}
