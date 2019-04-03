package com.yonyou.business.orangefinancial.button;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yonyou.business.button.util.ButForInsert;
import com.yonyou.util.BussnissException;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SqlWhereEntity;
import com.yonyou.util.sql.SQLUtil.WhereEnum;

import net.sf.json.JSONObject;

/**
 * 2019年4月2日09:32:09
 * @author changjr
 * 主数据关系映射节点 保存按钮
 */
public class BtnForInsertM extends ButForInsert{

	@SuppressWarnings("unchecked")
	@Override
	protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) throws IOException, BussnissException {
		
		
		
		String dataSourceCode = request.getParameter("dataSourceCode");
		String childDataSourceCode = request.getParameter("childDataSourceCode");
		String tabName = findTableNameByDataSourceCode(dataSourceCode);
		String childTabName = findTableNameByDataSourceCode(childDataSourceCode);
		JSONObject json = JSONObject.fromObject(request.getParameter("jsonData"));
		String[] childJsonArray = request.getParameterValues("childJsonData");
		String deleteIds = request.getParameter("deleteIds");
		String id= "";
		this.appendData(json,request);
		try {
			id= dcmsDAO.insertByTransfrom(tabName, json);
		} catch (BussnissException e) {
			e.printStackTrace();
		}
		if(childJsonArray != null){
			for(int i = 0;i < childJsonArray.length;i++){
				JSONObject childJson = JSONObject.fromObject(childJsonArray[i]);
				childJson.put("ID", "");
				childJson.put("PID", id);
				this.appendData(childJson,request);
				try {
					dcmsDAO.insertByTransfrom(childTabName, childJson);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		int i=1/0;
		if(StringUtils.isNotBlank(deleteIds)){
			deleteIds = deleteIds.substring(0, deleteIds.length()-1);
			SqlWhereEntity whereEntity = new SqlWhereEntity();
			whereEntity.putWhere("ID", deleteIds, WhereEnum.IN);
			return dcmsDAO.delete(childTabName, whereEntity);
		}
		String jsonMessage = "{\"message\":\"保存成功\"}";
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
		//this.handleTask(IBusFlowOperationType.HANDLE_TASK, request);
	}

}
