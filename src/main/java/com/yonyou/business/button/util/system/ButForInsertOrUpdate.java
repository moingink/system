package com.yonyou.business.button.util.system;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.yonyou.business.button.ButtonAbs;
import com.yonyou.util.BussnissException;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SQLUtil.WhereEnum;
import com.yonyou.util.sql.SqlWhereEntity;

public class ButForInsertOrUpdate extends ButtonAbs {

	@Override
	protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) throws IOException, BussnissException {
		//  一主多子表 多页签保存
		//  获取多行数据,有id为修改,没有id为新增数据 调用来个方法执行数据保存
		String tabName = request.getParameter("table");
		JSONArray jsonArray = JSONArray.parseArray(request.getParameter("jsonData"));
		SqlWhereEntity sqlWhere=new SqlWhereEntity();
		for (int i = 0; i < jsonArray.size(); i++) {
			//JSONObject jb = (JSONObject) jsonArray.get(i);
			net.sf.json.JSONObject json =net.sf.json.JSONObject.fromObject(jsonArray.getJSONObject(i));
			String id = json.getString("ID");
			sqlWhere.putWhere("ID", json.getString("ID"), WhereEnum.EQUAL_INT);
			if(null == id ||id.equals("")){
				json.remove("ID");
				dcmsDAO.insertByTransfrom(tabName, json);
			}else {
				dcmsDAO.updateByTransfrom(tabName, json, sqlWhere);
			}
		}
		String jsonMessage = "{\"message\":\"操作成功\"}";
		
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
