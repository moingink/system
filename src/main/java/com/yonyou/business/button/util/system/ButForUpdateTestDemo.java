package com.yonyou.business.button.util.system;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.yonyou.business.button.ButtonAbs;
import com.yonyou.business.button.util.IPublicBusColumn;
import com.yonyou.util.BussnissException;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SQLUtil.WhereEnum;
import com.yonyou.util.sql.SqlWhereEntity;

import net.sf.json.JSONObject;
public class ButForUpdateTestDemo extends ButtonAbs {

	@SuppressWarnings("unchecked")
	@Override
	protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request,HttpServletResponse response) throws IOException, BussnissException {
		String dataSourceCode = request.getParameter("dataSourceCode");
		String childDataSourceCode = request.getParameter("childDataSourceCode");
		//String tabName = findTableNameByDataSourceCode(dataSourceCode);
		//String childTabName = findTableNameByDataSourceCode(childDataSourceCode);
		JSONObject json = JSONObject.fromObject(request.getParameter("jsonData"));
		String[] childJsonArray = request.getParameterValues("childJsonData");
		String deleteIds = request.getParameter("deleteIds");
		String parent_file = request.getParameter("parentFile");
		this.appendData(json,request);
		SqlWhereEntity whereEntity =new SqlWhereEntity();
		try {
			this.appendWhereByIDs(json, whereEntity);
			dcmsDAO.updateByTransfrom(dataSourceCode, json, whereEntity);
		} catch (BussnissException e) {
			e.printStackTrace();
		}
		if(childJsonArray!=null){
			for(int i = 0;i < childJsonArray.length;i++){
				JSONObject childJson = JSONObject.fromObject(childJsonArray[i]);
				try {
					if(childJson.getString("ID").indexOf("add") == -1){
						SqlWhereEntity where = new SqlWhereEntity();
						where.putWhere("ID", childJson.getString("ID"), WhereEnum.EQUAL_INT);
						dcmsDAO.updateByTransfrom(childDataSourceCode, childJson, where);
					}else{
						childJson.put("ID", "");
						childJson.put(parent_file, json.get("ID"));
						if(!TOKEN_ENTITY.isEmpty()){
							//?????????
							childJson.put(IPublicBusColumn.CREATOR_ID, TOKEN_ENTITY.USER.getId());
							childJson.put(IPublicBusColumn.CREATOR_NAME, TOKEN_ENTITY.USER.getName());
					        	
							//????????????
							childJson.put(IPublicBusColumn.ORGANIZATION_ID, TOKEN_ENTITY.COMPANY.getCompany_id());
							childJson.put(IPublicBusColumn.ORGANIZATION_NAME, TOKEN_ENTITY.COMPANY.getCompany_name());
						}
						dcmsDAO.insertByTransfrom(childDataSourceCode, childJson);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if(StringUtils.isNotBlank(deleteIds)){
			deleteIds = deleteIds.substring(0, deleteIds.length()-1);
			SqlWhereEntity whereEntity1 = new SqlWhereEntity();
			whereEntity1.putWhere("ID", deleteIds, WhereEnum.IN);
			dcmsDAO.delete(childDataSourceCode, whereEntity1);
		}
		String jsonMessage = "{\"message\":\"????????????\"}";
		this.ajaxWrite(jsonMessage, request, response);
		return null;
	}

	@Override
	protected boolean befortOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
		return false;
	}

	@Override
	protected void afterOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
	}
	
	private void appendWhereByIDs(JSONObject json,SqlWhereEntity whereEntity){
		String id = json.getString("ID");
		whereEntity.putWhere("ID", id, WhereEnum.EQUAL_INT);
	}
	
}
