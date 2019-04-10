package com.yonyou.business.orangefinancial.button;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import net.sf.json.JSONObject;

import com.yonyou.business.button.ButtonAbs;
import com.yonyou.business.button.util.IPublicBusColumn;
import com.yonyou.util.BussnissException;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SQLUtil.WhereEnum;
import com.yonyou.util.sql.SqlWhereEntity;

/**
 * 
 * @author changjr
 *
 */
@Component
public class BtnForUpdateM extends ButtonAbs {
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request, HttpServletResponse response)
			throws IOException, BussnissException {

		String dataSourceCode = request.getParameter("dataSourceCode");
		String childDataSourceCode = request.getParameter("childDataSourceCode");
		String tabName = findTableNameByDataSourceCode(dataSourceCode);
		String childTabName = findTableNameByDataSourceCode(childDataSourceCode);
		JSONObject json = JSONObject.fromObject(request.getParameter("jsonData"));
		String[] childJsonArray = request.getParameterValues("childJsonData");
		String deleteIds = request.getParameter("deleteIds");

		this.appendData(json, request);
		SqlWhereEntity whereEntity = new SqlWhereEntity();
		try {
			this.appendWhereByIDs(json, whereEntity);
			dcmsDAO.updateByTransfrom(tabName, json, whereEntity);
		} catch (BussnissException e) {
			e.printStackTrace();
		}
		if (childJsonArray != null) {
			for (int i = 0; i < childJsonArray.length; i++) {
				JSONObject childJson = JSONObject.fromObject(childJsonArray[i]);
				try {
					if (childJson.getString("ID").indexOf("add") == -1) {
						SqlWhereEntity where = new SqlWhereEntity();
						where.putWhere("ID", childJson.getString("ID"), WhereEnum.EQUAL_INT);
						dcmsDAO.updateByTransfrom(childTabName, childJson, where);
					} else {
						childJson.put("ID", "");
						childJson.put("PID", json.get("ID"));
						if (!TOKEN_ENTITY.isEmpty()) {
							// 制单人
							childJson.put(IPublicBusColumn.CREATOR_ID, TOKEN_ENTITY.USER.getId());
							childJson.put(IPublicBusColumn.CREATOR_NAME, TOKEN_ENTITY.USER.getName());

							// 组织字段
							childJson.put(IPublicBusColumn.ORGANIZATION_ID, TOKEN_ENTITY.COMPANY.getCompany_id());
							childJson.put(IPublicBusColumn.ORGANIZATION_NAME, TOKEN_ENTITY.COMPANY.getCompany_name());
						}
						dcmsDAO.insertByTransfrom(childTabName, childJson);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (StringUtils.isNotBlank(deleteIds)) {
			deleteIds = deleteIds.substring(0, deleteIds.length() - 1);
			SqlWhereEntity whereEntity1 = new SqlWhereEntity();
			whereEntity1.putWhere("ID", deleteIds, WhereEnum.IN);
			dcmsDAO.delete(childTabName, whereEntity1);
		}
		String jsonMessage = "{\"message\":\"修改成功\"}";
		this.ajaxWrite(jsonMessage, request, response);
		return null;
	}

	@Override
	protected boolean befortOnClick(IBaseDao dcmsDAO, HttpServletRequest request, HttpServletResponse response) {
		return false;
	}

	@Override
	protected void afterOnClick(IBaseDao dcmsDAO, HttpServletRequest request, HttpServletResponse response) {
	}

	private void appendWhereByIDs(JSONObject json, SqlWhereEntity whereEntity) {
		String id = json.getString("ID");
		whereEntity.putWhere("ID", id, WhereEnum.EQUAL_INT);
	}

}
