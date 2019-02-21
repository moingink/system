package com.yonyou.business.button.demobutton.button;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.yonyou.business.button.demobutton.DemoButton;
import com.yonyou.util.BussnissException;
import com.yonyou.util.IdRmrnUtil;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.util.sql.SqlWhereEntity;
import com.yonyou.util.sql.SQLUtil.WhereEnum;

public class DemoButForCheckBack extends DemoButton {

	@Override
	protected boolean befortOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		String jsonMessage = "{\"message\":\"撤销复核成功\"}";
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		SqlTableUtil sqlTable =new SqlTableUtil("RA_AD_DAILY_DATA","");
		String result = null ;
		String reason = null ;
		SqlWhereEntity whereEntity  = new SqlWhereEntity();
		JSONArray jsonArray = JSONArray.fromObject(request.getParameter("jsonData"));	//获取json数组
		Map<String, Object> rmrn = IdRmrnUtil.getIdRmrnMap(jsonArray);
		whereEntity.putWhere("ID", (String)rmrn.get("IDS"), WhereEnum.IN)
		.putWhere("DR", "null", WhereEnum.IS_NULL)
		.putWhere("OFFICE_CODE", "C00100S00C020003,C00100S00C020004", WhereEnum.IN);
		sqlTable.appendSelFiled("*").appendOrderBy("BUSINESS_DATE", "DESC").appendSqlWhere(whereEntity);
		List<Map<String, Object>> lists = dcmsDAO.query(sqlTable);
		try{
			result = IdRmrnUtil.getNoPassRmrn("CHECK_STATUS", "0", lists,jsonArray) ;
			if(!"".equals(result)){
				reason = "原因：未复核数据不能撤销复核" ;
				jsonMessage = "{\"message\":\""+IdRmrnUtil.writeErrorInfo(result,reason)+"\"}";
				return true;
			}
		}finally{
			out.print(jsonMessage);
			out.flush();
			out.close();
		}
		return false;
	}

	@Override
	protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) throws IOException, BussnissException {
		
		String tabName = request.getParameter("tabName");		//表名
		JSONArray jsonArray = JSONArray.fromObject(request.getParameter("jsonData"));	//获取json数组
		List<Map<String, Object>> entityList = new ArrayList<Map<String, Object>>();	
		int len =  jsonArray.size();			
		for (int i = 0; i < len; i++) {
			JSONObject jb = (JSONObject) jsonArray.get(i);
			String id = jb.getString("ID");
			Map<String, Object> entity = new HashMap<String, Object>();
			entity.put("ID", id);
			entity.put("CHECK_DATE",  "");
			entity.put("CHECK_STATUS",  "0");
			entity.put("CHECK_EMPLOYEE", "");
			entityList.add(entity);
		}
		dcmsDAO.update(tabName, entityList, new SqlWhereEntity());
		
		String jsonMessage = "{\"message\":\"撤销复核成功\"}";
		
		this.ajaxWrite(jsonMessage, request, response);
		return null;
	}

	@Override
	protected void afterOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO 自动生成的方法存根
		
	}

}
