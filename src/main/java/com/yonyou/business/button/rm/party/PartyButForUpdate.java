package com.yonyou.business.button.rm.party;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.util.RmPartyConstants;

import net.sf.json.JSONObject;

import com.yonyou.business.button.ButtonAbs;
import com.yonyou.util.BussnissException;
import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.util.sql.SqlWhereEntity;

public class PartyButForUpdate extends ButtonAbs {

	@Override
	protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) throws IOException, BussnissException {
		// TODO 自动生成的方法存根
		String jsonMessage = "";
		String dataSourceCode =request.getParameter("dataSourceCode");
		String tabName = findTableNameByDataSourceCode(dataSourceCode);
		JSONObject json = JSONObject.fromObject(request.getParameter("jsonData"));
		//此处校验资金编码是否可用
		String tmCode = json.get("TM_CODE").toString();
		String id = json.getString("ID");
		boolean status = validatePartyRelation(tabName,tmCode,id);
		if(!status){
			jsonMessage = "{\"message\":\"修改失败，资金编码已被使用！\"}";
			this.ajaxWrite(jsonMessage, request, response);
			return null;
		}
		//根据团体ID更新相关的所有团体名称和团体关系名称
		List<Map<String, Object>> entityList = new ArrayList<Map<String, Object>>();
		Map<String, Object> entity = new HashMap<String, Object>();
		entity.put("NAME",json.get("DISPLAYNAME")==null?"":json.get("DISPLAYNAME").toString());
		entity.put("OLD_PARTY_ID",tmCode);
		entityList.add(entity);
		Map<String,String> whereMap = new HashMap<String,String>();
		whereMap.put("ID", "ID ='"+id+"'");
		SqlWhereEntity sqlWhere = new SqlWhereEntity();
		sqlWhere.setWhereMap(whereMap);
 		dcmsDAO.update(RmPartyConstants.rmParty, entityList, sqlWhere);
 		entity.clear();
 		entityList.clear();
 		whereMap.clear();
 		sqlWhere.clear();
 		
 		entity.put("CHILD_PARTY_NAME",json.get("DISPLAYNAME")==null?"":json.get("DISPLAYNAME").toString());
		entity.put("ORDER_CODE",json.get("CUORDER")==null?"":json.get("CUORDER").toString());
		entityList.add(entity);
		whereMap.put("CHILD_PARTY_ID", "CHILD_PARTY_ID ='"+id+"'");
		sqlWhere.setWhereMap(whereMap);
 		dcmsDAO.update(RmPartyConstants.rmPartyRelation, entityList, sqlWhere);
 		entity.clear();
 		entityList.clear();
 		whereMap.clear();
 		sqlWhere.clear();
		
		try {
			dcmsDAO.updateByTransfrom(tabName, json, new SqlWhereEntity());
		} catch (BussnissException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		jsonMessage = "{\"message\":\"修改成功\"}";
		this.ajaxWrite(jsonMessage, request, response);
		return null;
	}
	/**
	 * 验证资金编码TM_CODE是否被占用
	 * @param tabName
	 * @param tmCode
	 * @param id
	 * @return
	 */
	protected boolean validatePartyRelation(String tabName,String tmCode,String id){
		boolean status = false;
		if((!tmCode.isEmpty()) && (!id.isEmpty())){
			SqlTableUtil sql = new SqlTableUtil(tabName,"");
			sql.init("ID", "");
			sql.appendWhereAnd(" TM_CODE = '"+tmCode+"' ");
			sql.appendWhereAnd(" ID != '"+id+"' ");
			sql.appendWhereAnd(" DR = '0'");
			List<Map<String, Object>> mapList = BaseDao.getBaseDao().query(sql);
			if(mapList.isEmpty()){
				status = true;
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
