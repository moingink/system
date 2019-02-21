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

public class FinConfirmIncomeDetailForExcel extends ExcelHandleData{

	@Override
	public List<Map<String, String>> HandleList(String dataSourceCode, List<Map<String, String>> dataList, TokenEntity tokenEntity) {
		List<Integer> errorDataIndexList = new ArrayList<Integer>();
		StringBuffer errorMessage = new StringBuffer();
		
		Map<String,Map<String,Object>> productInfoCacheMap = new HashMap<String,Map<String, Object>>();
		Map<String,Map<String,Object>> businessTypeInfoCacheMap = new HashMap<String,Map<String, Object>>();
		Map<String,Map<String,Object>> productMoldInfoCacheMap = new HashMap<String,Map<String, Object>>();
		Map<String,Map<String,Object>> estimatedIncomeCacheMap = new HashMap<String,Map<String, Object>>();
		Map<String,Map<String,Object>> companyInfoCacheMap = new HashMap<String,Map<String, Object>>();
		Map<String,Map<String,Object>> provinceArchiveCacheMap = new HashMap<String,Map<String, Object>>();
		
		if (dataList.size() > 0) {
			for (int i = 0; i < dataList.size() - 1; i++) {
				Map<String, String> dataMap = dataList.get(i);
				
				String errorFields = "";
				String RMRN = dataMap.get("RMRN");
				String product = dataMap.get("PRODUCT_TYPE");// 产品
				String businessType = dataMap.get("BUSINESS_TYPE");// 业务类型
				String productMold = dataMap.get("PRODUCT_MOLD");// 产品类型
				String estimatedBillNo = dataMap.get("ESTIMATED_BILL_NO");// 计收单据号
				String incomeOfDept = dataMap.get("INCOME_OF_DEPT");// 收入归属部门
				String provinceCompany = dataMap.get("PROVINCE_COMPANY");// 省份公司
				String auditTax = dataMap.get("AUDIT_TAX");// 本次计收税率
				String settleMethod = dataMap.get("SETTLE_METHOD");// 收费结算方式
				String settileRatio = dataMap.get("SETTILE_RATIO");// 智网结算比例
				String confirmMonth = dataMap.get("CONFIRM_MONTH");// 收入实际发生月份
				
				if (StringUtils.isNotEmpty(product)) {
					Map<String, Object> productInfoMap = queryBusinessInfo("PRODUCT_INFO", "PRO_NAME", product, productInfoCacheMap);
					if (!productInfoMap.isEmpty()) {
						String productId = productInfoMap.get("ID").toString();
						dataMap.put("PRODUCT_ID", productId);// 产品主键
					} else {
						errorFields += "产品、";
					}
				}
				
				if(StringUtils.isNotEmpty(businessType)){
					Map<String, Object> businessTypeInfoMap = queryBusinessInfo("BUSS_TYPE", "TYPE_NAME", businessType, businessTypeInfoCacheMap);
					if (!businessTypeInfoMap.isEmpty()) {
						String businessTypeId = businessTypeInfoMap.get("ID").toString();
						dataMap.put("BUSINESS_ID", businessTypeId);// 业务类型主键
					} else {
						errorFields += "业务类型、";
					}
				}
				
				if(StringUtils.isNotEmpty(productMold)){
					Map<String, Object> productMoldInfoMap = queryBusinessInfo("PRODUCT_TYPE_MESSAGE", "PRO_TYPE_NAME", productMold, productMoldInfoCacheMap);
					if (!productMoldInfoMap.isEmpty()) {
						String productMoldId = productMoldInfoMap.get("ID").toString();
						dataMap.put("PRODUCT_MOLD_ID", productMoldId);// 产品类型主键
					} else {
						errorFields += "产品类型、";
					}
				}
				
				if (StringUtils.isNotEmpty(estimatedBillNo)) {
					Map<String, Object> estimatedIncomeMap = queryBusinessInfo("FIN_ESTIMATED_INCOME", "BILL_NO", estimatedBillNo, estimatedIncomeCacheMap);
					if (!estimatedIncomeMap.isEmpty()) {
						String estimatedId = estimatedIncomeMap.get("ID").toString();
						dataMap.put("ESTIMATED_ID", estimatedId);// 暂估主键
					} else {
						errorFields += "计收单据号、";
					}
				}
				
				if (StringUtils.isNotEmpty(incomeOfDept)) {
					Map<String, Object> companyInfoMap = queryBusinessInfo("TM_COMPANY", "NAME", incomeOfDept, companyInfoCacheMap);
					if (!companyInfoMap.isEmpty()) {
						String companyId = companyInfoMap.get("ID").toString();
						dataMap.put("INCOME_OF_DEPT_ID", companyId);// 收入归属部门主键
					} else {
						errorFields += "收入归属部门、";
					}
				}
				
				if (StringUtils.isNotEmpty(provinceCompany)) {
					Map<String, Object> provinceArchiveMap = queryBusinessInfo("PROVINCE_ARCHIVE", "PROVINCE_NAME", provinceCompany, provinceArchiveCacheMap);
					if (!provinceArchiveMap.isEmpty()) {
						String provinceArchiveCode = provinceArchiveMap.get("PROVINCE_CODE").toString();
						dataMap.put("PROVINCE_CODE", provinceArchiveCode);// 省份编码
					} else {
						errorFields += "省份公司、";
					}
				}
				
				if(StringUtils.isNotEmpty(auditTax)){
					Map<String, Object> dictionaryInfoMap = queryDictionaryInfo("TAX_RATET", auditTax);
					if(dictionaryInfoMap.size() > 0){
						dataMap.put("AUDIT_TAX_ID", auditTax);// 本次计收税率主键
						dataMap.put("AUDIT_TAX", (String)dictionaryInfoMap.get("DATA_VALUE"));// 本次计收税率
					}
				}
				
				if(StringUtils.isNotEmpty(settleMethod)){
					Map<String, Object> dictionaryInfoMap = queryDictionaryInfo("CHARGE_MODE", settleMethod);
					if(!dictionaryInfoMap.isEmpty()){
						dataMap.put("SETTLE_METHOD_ID", settleMethod);// 收费结算方式
						dataMap.put("SETTLE_METHOD", (String)dictionaryInfoMap.get("DATA_VALUE"));// 收费结算方式主键
					}
				}
				
				if(StringUtils.isNotEmpty(settileRatio)){
					Map<String, Object> dictionaryInfoMap = queryDictionaryInfo("SETTILE_RATIO", settileRatio);
					if(!dictionaryInfoMap.isEmpty()){
						dataMap.put("SETTILE_RATIO_ID", settileRatio);// 智网结算比例
						dataMap.put("SETTILE_RATIO", (String)dictionaryInfoMap.get("DATA_VALUE"));// 智网结算比例主键
					}
				}
				
				if(StringUtils.isNotEmpty(confirmMonth) && StringUtils.isEmpty(errorFields)){
					boolean sign = true;
					String[] confirmMonthSplit = confirmMonth.split(";");
					if(confirmMonthSplit.length > 0){
						//添加子表信息
						List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
						for (int j = 0; j < confirmMonthSplit.length; j++) {
							Map<String, Object> map = new HashMap<String, Object>();
							String[] confirmMonthSplits = confirmMonthSplit[j].split(":");
							if(confirmMonthSplits.length > 0){
								map.put("HAPPEN_DATE", confirmMonthSplits[0]);
								map.put("CHARGE",  confirmMonthSplits[1]);
								map.put("PARENT_ID", dataMap.get("ID"));
								if(!tokenEntity.isEmpty()){
									//制单人
									map.put(IPublicBusColumn.CREATOR_ID, tokenEntity.USER.getId());
									map.put(IPublicBusColumn.CREATOR_NAME, tokenEntity.USER.getName());
							        	
									//组织字段
									map.put(IPublicBusColumn.ORGANIZATION_ID, tokenEntity.COMPANY.getCompany_id());
									map.put(IPublicBusColumn.ORGANIZATION_NAME, tokenEntity.COMPANY.getCompany_name());
								}
							    list.add(map);
							}else{
								sign = false;
							}
						}
						if(list.size() > 0){
							try {
								BaseDao.getBaseDao().insert("FIN_AUDIT_CHARGE_DETAIL_HAPPEN_DATE", list);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}else{
						sign = false;
					}
					if(!sign){
						errorMessage.append("Excel中第").append(RMRN).append("行数据").append(errorFields).append("字段格式错误，请参照正确格式-日期：金额；日期：金额；-以此类推，查证修改后重新导入！").append("<br>");
					}
				}
				
				dataMap.put("DETAIL_TYPE", "1");// 详情类型
				
				if (!tokenEntity.isEmpty()) {
					// 制单人
					dataMap.put(IPublicBusColumn.CREATOR_ID,tokenEntity.USER.getId());
					dataMap.put(IPublicBusColumn.CREATOR_NAME,tokenEntity.USER.getName());
					// 组织字段
					dataMap.put(IPublicBusColumn.ORGANIZATION_ID,tokenEntity.COMPANY.getCompany_id());
					dataMap.put(IPublicBusColumn.ORGANIZATION_NAME,tokenEntity.COMPANY.getCompany_name());
				}
				
				if (StringUtils.isNotEmpty(errorFields) || StringUtils.isNotEmpty(errorMessage.toString())) {
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
	
	/**
	 * 查询字典信息
	 * @param typeKeyword 类型关键字
	 * @param dataKey 数据关键字
	 * @return
	 */
	public Map<String, Object> queryDictionaryInfo(String typeKeyword, String dataKey) {
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "SELECT b.DATA_VALUE FROM RM_CODE_TYPE a,RM_CODE_DATA b WHERE a.ID = b.CODE_TYPE_ID AND a.TYPE_KEYWORD = '" + typeKeyword + "' AND b.DATA_KEY = '" + dataKey + "' AND a.DR = 0 AND b.DR = 0 ";
		List<Map<String, Object>> list = BaseDao.getBaseDao().query(sql, "");
		if (list.size() > 0) {
			map = list.get(0);
		}
		return map;
	}
}