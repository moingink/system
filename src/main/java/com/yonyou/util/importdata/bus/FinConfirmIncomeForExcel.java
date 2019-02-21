package com.yonyou.util.importdata.bus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.yonyou.business.button.util.IPublicBusColumn;
import com.yonyou.business.entity.TokenEntity;
import com.yonyou.util.importdata.ExcelHandleData;
import com.yonyou.util.jdbc.BaseDao;

public class FinConfirmIncomeForExcel extends ExcelHandleData{

	@Override
	public List<Map<String, String>> HandleList(String dataSourceCode, List<Map<String, String>> dataList, TokenEntity tokenEntity) {
		List<Integer> errorDataIndexList = new ArrayList<Integer>();
		StringBuffer errorMessage = new StringBuffer();
		
		Map<String,Map<String,Object>> customerInfoCacheMap = new HashMap<String,Map<String, Object>>();
		Map<String,Map<String,Object>> userInfoCacheMap = new HashMap<String,Map<String, Object>>();
		Map<String,Map<String,Object>> companyInfoCacheMap = new HashMap<String,Map<String, Object>>();
		
		if (dataList.size() > 0) {
			for (int i = 0; i < dataList.size() - 1; i++) {
				Map<String, String> dataMap = dataList.get(i);
				
				String errorFields = "";
				String RMRN = dataMap.get("RMRN");
				String clientName = dataMap.get("CLIENT_NAME");// 客户名称
				String clientManager = dataMap.get("CLIENT_MANAGER");// 客户经理
				String dept = dataMap.get("DEPT");// 部门
				
				if (StringUtils.isNotEmpty(clientName)) {
					Map<String, Object> customerInfoMap = queryBusinessInfo("CUMA_ACCOUNT", "CUSTOMER_PERSON", clientName, customerInfoCacheMap);
					if (!customerInfoMap.isEmpty()) {
						String customerId = customerInfoMap.get("ID").toString();
						dataMap.put("CUS_ID", customerId);// 客户主键
					} else {
						errorFields += "客户名称、";
					}
				}
				
				if (StringUtils.isNotEmpty(clientManager)) {
					Map<String, Object> userInfoMap = queryBusinessInfo("RM_USER", "NAME", clientManager, userInfoCacheMap);
					if (!userInfoMap.isEmpty()) {
						String userId = userInfoMap.get("ID").toString();
						dataMap.put("CLIENT_MANAGER_ID", userId);// 客户经理主键
					} else {
						errorFields += "客户经理、";
					}
				}
				
				if (StringUtils.isNotEmpty(dept)) {
					Map<String, Object> companyInfoMap = queryBusinessInfo("TM_COMPANY", "NAME", dept, companyInfoCacheMap);
					if (!companyInfoMap.isEmpty()) {
						String companyId = companyInfoMap.get("ID").toString();
						dataMap.put("DEPT_ID", companyId);// 部门主键
					} else {
						errorFields += "部门、";
					}
				}
				
				if (!tokenEntity.isEmpty()) {
					// 制单人
					dataMap.put(IPublicBusColumn.CREATOR_ID,tokenEntity.USER.getId());
					dataMap.put(IPublicBusColumn.CREATOR_NAME,tokenEntity.USER.getName());
					// 组织字段
					dataMap.put(IPublicBusColumn.ORGANIZATION_ID,tokenEntity.COMPANY.getCompany_id());
					dataMap.put(IPublicBusColumn.ORGANIZATION_NAME,tokenEntity.COMPANY.getCompany_name());
				}
				
				if (StringUtils.isNotEmpty(errorFields)) {
					errorDataIndexList.add(i);
					errorFields = errorFields.substring(0,errorFields.length() - 1);
					errorMessage.append("Excel中第").append(RMRN).append("行数据").append(errorFields).append("字段在系统中不存在，请查证修改后重新导入！").append("<br>");
				}
			}
		}
		
		if (errorDataIndexList.size() > 0) {
			for (int i = errorDataIndexList.size() - 1; i >= 0; i--) {
				dataList.remove(errorDataIndexList.get(i).intValue());
			}
		}
		
		// 如果有错误消息，需put到当前对象的_BUS_ERROR_MESSAGE中
		if (StringUtils.isNotEmpty(errorMessage.toString())) {
			Map<String, String> map = dataList.get(dataList.size() - 1);
			map.put("_BUS_ERROR_MESSAGE", errorMessage.toString());
		}
		
		return dataList;
	}
	
	/**
	 * 查询业务信息
	 * @param tableName 表名
	 * @param fieldName 字段名
	 * @param fieldValue 字段值
	 * @param cacheMap 缓存
	 * @return
	 */
	public Map<String, Object> queryBusinessInfo(String tableName, String fieldName, String fieldValue, Map<String, Map<String, Object>> cacheMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (cacheMap.containsKey(fieldValue)) {
			map = cacheMap.get(fieldValue);
		} else {
			String sql = "SELECT * FROM " + tableName + " WHERE " + fieldName + " = '" + fieldValue + "' ";
			List<Map<String, Object>> businessInfoList = BaseDao.getBaseDao().query(sql, "");
			if (businessInfoList.size() > 0) {
				cacheMap.put(fieldValue, businessInfoList.get(0));
				map = businessInfoList.get(0);
			}
		}
		return map;
	}
}