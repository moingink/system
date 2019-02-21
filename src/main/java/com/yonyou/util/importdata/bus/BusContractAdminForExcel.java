package com.yonyou.util.importdata.bus;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.yonyou.business.button.util.IPublicBusColumn;
import com.yonyou.business.entity.TokenEntity;
import com.yonyou.util.importdata.ExcelHandleData;
import com.yonyou.util.jdbc.BaseDao;

public class BusContractAdminForExcel extends ExcelHandleData{

	@Override
	public List<Map<String, String>> HandleList(String dataSourceCode, List<Map<String, String>> dataList, TokenEntity tokenEntity) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<Integer> errorDataIndexList = new ArrayList<Integer>();
		StringBuffer errorMessage = new StringBuffer();
		Map<String,Map<String,Object>> companyInfoCacheMap = new HashMap<String,Map<String, Object>>();
		Map<String,Map<String,Object>> userInfoCacheMap = new HashMap<String,Map<String, Object>>();
		
		if (dataList.size() > 0) {
			for (int i = 0; i < dataList.size() - 1; i++) {
				Map<String, String> dataMap = dataList.get(i);
				
				String errorFields = "";
				String RMRN = dataMap.get("RMRN");
				String undertakeDep = dataMap.get("UNDERTAKE_DEP");// 承办部门
				String undertakeName = dataMap.get("UNDERTAKE_NAME");// 承办人
				String performName = dataMap.get("PERFORM_NAME");// 履行人
				String performDep = dataMap.get("PERFORM_DEP");// 履行部门
				String valueAddedTaxPrice = dataMap.get("VALUE_ADDED_TAX_PRICE");// 含增值税合同金额
				
				if (StringUtils.isNotEmpty(undertakeDep)) {
					Map<String, Object> companyInfoMap = queryCompanyInfoByName(undertakeDep,companyInfoCacheMap);
					if (!companyInfoMap.isEmpty()) {
						String companyId = companyInfoMap.get("ID").toString();
						dataMap.put(IPublicBusColumn.ORGANIZATION_ID,companyId);
						dataMap.put(IPublicBusColumn.ORGANIZATION_NAME,undertakeDep);
					} else {
						errorFields += "承办部门、";
					}
				} else {
					if (!tokenEntity.isEmpty()) {
						dataMap.put(IPublicBusColumn.ORGANIZATION_ID,tokenEntity.COMPANY.getCompany_id());
						dataMap.put(IPublicBusColumn.ORGANIZATION_NAME,tokenEntity.COMPANY.getCompany_name());
					}
				}
				
				if (StringUtils.isNotEmpty(undertakeName)) {
					Map<String, Object> userInfoMap = queryUserInfoByName(undertakeName,userInfoCacheMap);
					if (!userInfoMap.isEmpty()) {
						String userId = userInfoMap.get("ID").toString();
						dataMap.put("UNDERTAKE_ID", userId);// 承办人主键
						dataMap.put(IPublicBusColumn.CREATOR_ID, userId);
						dataMap.put(IPublicBusColumn.CREATOR_NAME,undertakeName);
					} else {
						errorFields += "承办人、";
					}
				} else {
					if (!tokenEntity.isEmpty()) {
						dataMap.put(IPublicBusColumn.CREATOR_ID,tokenEntity.USER.getId());
						dataMap.put(IPublicBusColumn.CREATOR_NAME,tokenEntity.USER.getName());
					}
				}
				
				if (StringUtils.isNotEmpty(performName)) {
					Map<String, Object> userInfoMap = queryUserInfoByName(undertakeName,userInfoCacheMap);
					if (!userInfoMap.isEmpty()) {
						String userId = userInfoMap.get("ID").toString();
						dataMap.put("PERFORM_ID", userId);// 履行人主键
					} else {
						errorFields += "履行人、";
					}
				}
				
				if (StringUtils.isNotEmpty(performDep)) {
					Map<String,Object> companyInfoMap = queryCompanyInfoByName(performDep,companyInfoCacheMap);
					if (companyInfoMap.isEmpty()) {
						errorFields += "履行部门、";
					}
				}
				
				dataMap.put("BALANCE", valueAddedTaxPrice);// 余额
				dataMap.put("PARTY_MAIN",dataMap.get("PARTY_MAIN").replace(",", ""));// 我方主体
				dataMap.put("IMPORT_TIME", sdf.format(new Date()));// 导入时间
				if (new BigDecimal(valueAddedTaxPrice).compareTo(new BigDecimal(5000000)) == 1) {
					dataMap.put("IF_FIVE_MILLION", "1");// 是否500万合同
				} else if (new BigDecimal(valueAddedTaxPrice).compareTo(new BigDecimal(5000000)) == 0) {
					dataMap.put("IF_FIVE_MILLION", "1");// 是否500万合同
				} else {
					dataMap.put("IF_FIVE_MILLION", "0");// 是否500万合同
				}
				dataMap.put("IF_OFFLINE", "0");// 是否线下合同
				
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
	 * 查询部门信息
	 * @param field
	 * @return
	 */
	public Map<String, Object> queryCompanyInfoByName(String field,Map<String,Map<String,Object>> companyInfoCacheMap){
		Map<String, Object> map = new HashMap<String, Object>();
		if(companyInfoCacheMap.containsKey(field)){
			map = companyInfoCacheMap.get(field);
		}else{
			List<Map<String, Object>> companyInfoList = BaseDao.getBaseDao().query("SELECT * FROM TM_COMPANY WHERE NAME = '" + field + "' ", "");
			if(companyInfoList.size() > 0){
				companyInfoCacheMap.put(field, companyInfoList.get(0));
				map = companyInfoList.get(0);
			}
		}
		return map;
	}
	
	/**
	 * 查询人员信息
	 * @param field
	 * @return
	 */
	public Map<String, Object> queryUserInfoByName(String field,Map<String,Map<String,Object>> userInfoCacheMap){
		Map<String, Object> map = new HashMap<String, Object>();
		if(userInfoCacheMap.containsKey(field)){
			map = userInfoCacheMap.get(field);
		}else{
			List<Map<String, Object>> userInfoList = BaseDao.getBaseDao().query("SELECT * FROM RM_USER WHERE NAME = '" + field + "' ", "");
			if(userInfoList.size() > 0){
				userInfoCacheMap.put(field, userInfoList.get(0));
				map = userInfoList.get(0);
			}
		}
		return map;
	}
}