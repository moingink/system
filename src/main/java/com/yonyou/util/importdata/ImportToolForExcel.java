package com.yonyou.util.importdata;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.yonyou.business.MetaDataUtil;
import com.yonyou.business.RmDictReferenceUtil;
import com.yonyou.business.entity.TokenEntity;
import com.yonyou.util.BussnissException;
import com.yonyou.util.RmIdFactory;
import com.yonyou.util.SerialNumberUtil;
import com.yonyou.util.SerialNumberUtil.SerialType;
import com.yonyou.util.jdbc.BaseDao;

/**
 * Excel导入工具
 * @ClassName ImportToolForExcel
 * @author Sean
 * @date 2018年11月28日
 */
public class ImportToolForExcel {

	// 换行符
	private static final String NEWLINE = "<br>";
	// 序号
	private static final String SERIAL_NUMBER = "RMRN";
	// 业务处理类错误消息
	private static final String BUS_ERROR_MESSAGE = "_BUS_ERROR_MESSAGE";
	
	// 错误消息
	private static StringBuffer errorMessage = null;
	// 数据数量
	private static Integer DATA_SIZE = 0;
	// 主表数据
	private static List<Map<String, String>> dataList = null;
	// 子表数据
	private static List<Map<String, String>> childDataList = null;
	// 主子表映射关系
	private static Map<Map<String, String>, List<String>> mappingRelationMap = null;
	
	
	/**
	 * Excel导入
	 * @param files Excel文件
	 * @param dataSourceCode 数据源编码
	 * @param tokenEntity token
	 * @return
	 * @throws BussnissException
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static StringBuffer importExcel(MultipartFile file,String dataSourceCode,TokenEntity tokenEntity) throws BussnissException, IOException {
		
		List<Map<String, Object>> configureList = BaseDao.getBaseDao().query("SELECT * FROM EXCEL_IMPORT_CONFIGURE WHERE DATASOURCE_CODE = '"+dataSourceCode+"' AND DR = 0", "");
		if (configureList.size() > 0) {
			Map<String, Object> configureMap = configureList.get(0);
			String metaDataCode = configureMap.get("METADATA_CODE").toString();
			String configureFileName = configureMap.get("FILE_NAME").toString().trim();
			Integer headStartLineIndex = Integer.valueOf(configureMap.get("HEAD_START_LINE_INDEX").toString().trim());
			Integer headStartColumnIndex = Integer.valueOf(configureMap.get("HEAD_START_COLUMN_INDEX").toString().trim());
			Integer bodyStartLineIndex = Integer.valueOf(configureMap.get("BODY_START_LINE_INDEX").toString().trim());
			String warehousingRules = configureMap.get("WAREHOUSING_RULES").toString();
			String onlyValidate = (String) configureMap.get("ONLY_VALIDATE");
			String busHandleClass = (String) configureMap.get("BUS_HANDLE_CLASS");
			String includeChildTable = configureMap.get("INCLUDE_CHILD_TABLE").toString();
			String childMetaDataCode = (String) configureMap.get("CHILD_METADATA_CODE");
			String childOnlyValidate = (String) configureMap.get("CHILD_ONLY_VALIDATE");
			String childBusHandleClass = (String) configureMap.get("CHILD_BUS_HANDLE_CLASS");
			String mappingType = (String) configureMap.get("MAPPING_TYPE");
			String mainMappingField = (String) configureMap.get("MAIN_MAPPING_FIELD");
			String childMappingField = (String) configureMap.get("CHILD_MAPPING_FIELD");
			String fileName = file.getOriginalFilename().trim();

			List<String> headList = new ArrayList<String>();
			List<Map<String, String>> newHeadList = new ArrayList<Map<String, String>>();
			List<String> childHeadList = new ArrayList<String>();
			List<Map<String, String>> newChildHeadList = new ArrayList<Map<String, String>>();
			errorMessage = new StringBuffer();
			dataList = new ArrayList<Map<String, String>>();
			childDataList = new ArrayList<Map<String, String>>();

			Map<String, String> MetaDataFiledTypeMap = MetaDataUtil.getFiledTypeMap(metaDataCode);
			ConcurrentHashMap<String, ConcurrentHashMap<String, String>> MetaDataFields = MetaDataUtil.getMetaDataFields(metaDataCode);
			
			Map<String, String> childMetaDataFiledTypeMap = null;
			ConcurrentHashMap<String, ConcurrentHashMap<String, String>> childMetaDataFields = null;
			if(includeChildTable.equals("1")){
				childMetaDataFiledTypeMap = MetaDataUtil.getFiledTypeMap(childMetaDataCode);
				childMetaDataFields = MetaDataUtil.getMetaDataFields(childMetaDataCode);
			}

			// 文件名称验证
			boolean result = fileNameJudge(fileName, configureFileName);

			if (result) {
				Workbook workbook = null;
				if (fileName.endsWith("xlsx")) {
					workbook = new XSSFWorkbook(file.getInputStream());
				} else {
					workbook = new HSSFWorkbook(file.getInputStream());
				}

				Sheet sheet = workbook.getSheetAt(0);
				DATA_SIZE = sheet.getLastRowNum() - headStartLineIndex;

				// 物理定义的行数
				if (sheet.getPhysicalNumberOfRows() != 0) {
					// 获取Excel表头
					for (int i = headStartLineIndex; i < headStartLineIndex + 1; i++) {
						Row row = sheet.getRow(i);

						int lastColumn = 0;
						if (includeChildTable.equals("0")) {
							lastColumn = row.getLastCellNum();
						} else {
							// 获取合并单元格
							CellRangeAddress mergedRegion = sheet.getMergedRegion(1);
							lastColumn = mergedRegion.getLastColumn() + 1;
							for (int k = lastColumn; k < row.getLastCellNum(); k++) {
								Cell cell = row.getCell(k);
								if (cell != null) {
									childHeadList.add(getCellValue(cell));
								}
							}
						}

						for (int j = headStartColumnIndex; j < lastColumn; j++) {
							Cell cell = row.getCell(j);
							if (cell != null) {
								headList.add(getCellValue(cell));
							}
						}
					}

					// 根据元数据编码获取元数据字段信息,并与Excel表头进行匹配,获取正确的表头信息
					String headFieldErrorMessage = getExcelHeadByMetaDataCode(headList, newHeadList, metaDataCode);
					if (includeChildTable.equals("0")) {
						if (StringUtils.isNotEmpty(headFieldErrorMessage)) {
							headFieldErrorMessage = headFieldErrorMessage.substring(0,headFieldErrorMessage.length() - 1);
							errorMessage.append("Excel中").append(headFieldErrorMessage).append("字段没有在元数据中配置，请配置后重新导入！");
						}
					} else {
						String childHeadFieldErrorMessage = getExcelHeadByMetaDataCode(childHeadList, newChildHeadList, childMetaDataCode);
						if (StringUtils.isNotEmpty(headFieldErrorMessage) || StringUtils.isNotEmpty(childHeadFieldErrorMessage)) {
							if (headFieldErrorMessage.length() > 0) {
								headFieldErrorMessage = headFieldErrorMessage.substring(0,headFieldErrorMessage.length() - 1);
								errorMessage.append("Excel中").append("单据头").append(headFieldErrorMessage).append("字段没有在元数据中配置，请配置后重新导入！").append(NEWLINE);
							}
							if (childHeadFieldErrorMessage.length() > 0) {
								childHeadFieldErrorMessage = childHeadFieldErrorMessage.substring(0,childHeadFieldErrorMessage.length() - 1);
								errorMessage.append("Excel中").append("单据明细").append(childHeadFieldErrorMessage).append("字段没有在元数据中配置，请配置后重新导入！");
							}
						}
					}

					if (StringUtils.isEmpty(errorMessage.toString())) {
						// 获取Excel表体,并验证字段类型是否与元数据一致
						for (int i = bodyStartLineIndex; i < sheet.getLastRowNum() + 1; i++) {
							Row row = sheet.getRow(i);

							String filedTypeErrorMessage = "";
							Map<String, String> dataMap = new HashMap<String, String>();
							for (int j = headStartColumnIndex; j < newHeadList.size() + headStartColumnIndex; j++) {
								Cell cell = row.getCell(j);

								Map<String, String> newHeadMap = newHeadList.get(j - headStartColumnIndex);
								String fieldCode = newHeadMap.get("FIELD_CODE");
								String fieldName = newHeadMap.get("FIELD_NAME");
								String MetaDataFiledType = MetaDataFiledTypeMap.get(fieldCode);
								if (cell != null) {
									String cellValue = getCellValue(cell);
									filedTypeErrorMessage += getExcelBody(cellValue, MetaDataFiledType, fieldCode, fieldName, dataMap);
								}
							}
							dataMap.put(SERIAL_NUMBER, String.valueOf(i + 1));

							if (StringUtils.isEmpty(filedTypeErrorMessage)) {
								dataList.add(dataMap);
							} else {
								filedTypeErrorMessage = filedTypeErrorMessage.substring(0,filedTypeErrorMessage.length() - 1);
								errorMessage.append("Excel中第").append(dataMap.get(SERIAL_NUMBER)).append("行").append(filedTypeErrorMessage).append("字段数据格式不正确！").append(NEWLINE);
							}

							if (includeChildTable.equals("1")) {
								String childFiledTypeErrorMessage = "";
								Map<String, String> childDataMap = new HashMap<String, String>();
								for (int k = headStartColumnIndex + newHeadList.size(); k < newChildHeadList.size() + newHeadList.size() + headStartColumnIndex; k++) {
									Cell cell = row.getCell(k);

									Map<String, String> newChildHeadMap = newChildHeadList.get(k - headStartColumnIndex - newHeadList.size());
									String fieldCode = newChildHeadMap.get("FIELD_CODE");
									String fieldName = newChildHeadMap.get("FIELD_NAME");
									String childMetaDataFiledType = childMetaDataFiledTypeMap.get(fieldCode);
									if (cell != null) {
										String cellValue = getCellValue(cell);
										childFiledTypeErrorMessage += getExcelBody(cellValue, childMetaDataFiledType, fieldCode, fieldName, childDataMap);
									}
								}
								childDataMap.put(SERIAL_NUMBER, String.valueOf(i + 1));

								if (StringUtils.isEmpty(childFiledTypeErrorMessage)) {
									childDataList.add(childDataMap);
								} else {
									childFiledTypeErrorMessage = childFiledTypeErrorMessage.substring(0,childFiledTypeErrorMessage.length() - 1);
									errorMessage.append("Excel中第").append(childDataMap.get(SERIAL_NUMBER)).append("行").append(childFiledTypeErrorMessage).append("字段数据格式不正确！").append(NEWLINE);
								}
							}
						}
					}

					if (StringUtils.isEmpty(errorMessage.toString())) {
						// 数据验证（非空、数据唯一）及字典翻译
						dataList = validateExcelData(onlyValidate, dataList, MetaDataFields);
						if (includeChildTable.equals("1")) {
							// 数据验证（非空、数据唯一）及字典翻译
							childDataList = validateExcelData(childOnlyValidate, childDataList, childMetaDataFields);
						}
						// 入库操作
						errorMessage = insertDataBase(warehousingRules, includeChildTable, metaDataCode, onlyValidate, busHandleClass, childMetaDataCode, childOnlyValidate, childBusHandleClass, mappingType, mainMappingField, childMappingField, dataSourceCode, tokenEntity);
					}
				} else {
					errorMessage.append("文件为空，请修改后重新导入！");
				}
			} else {
				errorMessage.append("文件名称格式错误，请参考正确格式-" + configureFileName + "-调整后重新导入！");
			}
		} else {
			throw new BussnissException("数据源编码-" + dataSourceCode + "-没有在Ecxel导入配置表中配置");
		}
		
		return errorMessage;
	}

	/**
	 * 文件名称验证
	 * @param fileName 文件名称
	 * @param configureFileName 配置文件名称
	 * @return
	 */
	private static boolean fileNameJudge(String fileName,String configureFileName){
		boolean result = false;
		String fileNamePrefix = fileName.substring(0,fileName.lastIndexOf("."));
		if(configureFileName.matches(".*#.*")){
			String[] fileNameSplit = configureFileName.split("#");
			String fileNameSplitOne = fileNameSplit[0];
			int fileNameSplitOneLength = fileNameSplit[0].length();
			String fileNameSplitTwo = "";
			int fileNameSplitTwoLength = 0;
			if(fileNameSplit.length == 2){
				fileNameSplitTwo = fileNameSplit[1];
				fileNameSplitTwoLength = fileNameSplit[1].length();
			}
			int lastIndex = fileName.lastIndexOf(".");
			
			if(StringUtils.isEmpty(fileNameSplitOne)){
				String str = fileNamePrefix.substring(lastIndex-fileNameSplitTwoLength,lastIndex);
				if(str.equals(fileNameSplitTwo)){
					result = true;
				}
			}else if(StringUtils.isNotEmpty(fileNameSplitOne) && StringUtils.isNotEmpty(fileNameSplitTwo)){
				String str1 = fileNamePrefix.substring(0, fileNameSplitOneLength);
				String str2 = fileNamePrefix.substring(lastIndex-fileNameSplitTwoLength, lastIndex);
				if(str1.equals(fileNameSplitOne) && str2.equals(fileNameSplitTwo)){
					result = true;
				}
			}else{
				String str = fileNamePrefix.substring(0, fileNameSplitOneLength);
				if(str.equals(fileNameSplitOne)){
					result = true;
				}
			}
		}else{
			if(configureFileName.equals(fileNamePrefix)){
				result = true;
			}
		}
		return result;
	}
	
	/**
	 * 获取Excel单元格内容
	 * @param cell 单元格
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private static String getCellValue(Cell cell) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String cellValue = "";
		switch (cell.getCellType()) {
		case 0:// 数值型
			if (DateUtil.isCellDateFormatted(cell)) {
				cellValue = sdf.format(cell.getDateCellValue());
			} else {
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cellValue = cell.getStringCellValue().trim();
			}
			break;
		case 1:// 字符串型
			cellValue = cell.getStringCellValue().trim();
			break;
		case 2:// 公式型
			cellValue = String.valueOf(cell.getCellFormula()).trim();
			break;
		case 3:// 空值
			cellValue = "";
			break;
		case 4:// 布尔型
			cellValue = String.valueOf(cell.getBooleanCellValue()).trim();
			break;
		case 5:// 错误
			cellValue = "";
			break;
		}
		return cellValue;
	}
	
	/**
	 * 根据元数据编码获取元数据字段信息,并与Excel表头进行匹配,获取正确的表头信息 
	 * @param headList Excel表头
	 * @param newHeadList 正确的表头
	 * @param metaDataCode 元素据编码
	 * @return
	 */
	private static String getExcelHeadByMetaDataCode(List<String> headList,List<Map<String, String>> newHeadList,String metaDataCode){
		List<Map<String, Object>> headFieldList = new ArrayList<Map<String, Object>>();
		Map<String, Object> headFieldMap = new HashMap<String, Object>();
		List<Map<String, Object>> MetaDataFiledList = BaseDao.getBaseDao().query("SELECT * FROM CD_METADATA a,CD_METADATA_DETAIL b WHERE a.ID = b.METADATA_ID AND a.DR = 0 AND a.DATA_CODE = '" + metaDataCode + "' ", "");
		for (Map<String, Object> MetaDataFiledMap : MetaDataFiledList) {
			headFieldMap.put(MetaDataFiledMap.get("FIELD_NAME").toString(), MetaDataFiledMap.get("FIELD_CODE"));
		}
		headFieldList.add(headFieldMap);
		
		String headFieldErrorMessage = "";
		Map<String, Object> headFieldMapMapping = headFieldList.get(0);
		for (int i = 0; i < headList.size(); i++) {
			if (headFieldMapMapping.containsKey(headList.get(i))) {
				Map<String, String> newHeadMap = new HashMap<String, String>();
				newHeadMap.put("FIELD_CODE", headFieldMapMapping.get(headList.get(i)).toString());
				newHeadMap.put("FIELD_NAME", headList.get(i));
				newHeadList.add(newHeadMap);
			} else {
				headFieldErrorMessage += headList.get(i) + "、";
			}
		}
		
		return headFieldErrorMessage;
	}
	
	/**
	 * 获取Excel表体,并验证字段类型是否与元数据一致
	 * @param cellValue 单元格内容
	 * @param MetaDataFiledTypeMap 元数据字段类型
	 * @param fieldCode 字段英文
	 * @param fieldName 字段中文
	 * @param dataMap 行数据
	 * @return
	 */
	private static String getExcelBody(String cellValue, String MetaDataFiledType, String fieldCode, String fieldName, Map<String, String> dataMap) {
		String filedTypeErrorMessage = "";
		if(cellValue.equals("-")){
			cellValue = "";
		}
		
		switch (MetaDataFiledType) {
		case "DATE" :
			if(StringUtils.isNotEmpty(cellValue)){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				sdf.setLenient(false);
				try {
					cellValue = sdf.format(sdf.parse(cellValue));
				} catch (ParseException e) {
					filedTypeErrorMessage = fieldName + "、";
				}
			}
			break;
		case "TIMESTAMP" :
			if(StringUtils.isNotEmpty(cellValue)){
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				sdf1.setLenient(false);
				try {
					cellValue = sdf1.format(sdf1.parse(cellValue));
				} catch (ParseException e) {
					filedTypeErrorMessage = fieldName + "、";
				}
			}
			break;
		case "BIGINT" :
			if (!cellValue.matches("^\\+?\\d*$")) {
				filedTypeErrorMessage = fieldName + "、";
			} 
			break;
		case "INT" :
			if (!cellValue.matches("^\\+?\\d*$")) {
				filedTypeErrorMessage = fieldName + "、";
			} 
			break;
		case "DECIMAL" :
			cellValue = cellValue.replaceAll(",", "");
			if (!cellValue.matches("^(([1-9][0-9]+|[0-9])(\\.[0-9]+)?|\\.[0-9]+|)$")) {
				filedTypeErrorMessage = fieldName + "、";
			}
			break;
		default: 
			break;
		}
		dataMap.put(fieldCode, cellValue);
		
		return filedTypeErrorMessage;
	}
	
	/**
	 * 数据去重
	 * @param dataList 数据
	 * @return
	 */
	private static List<Map<String, String>> dataRemoveDuplication(List<Map<String, String>> dataList) {
		Map<Object, Map<String, String>> dataHashCodeMap = new HashMap<Object, Map<String, String>>();
		List<Integer> duplicationDataIndexList = new ArrayList<Integer>();
		mappingRelationMap = new HashMap<Map<String, String>, List<String>>();
		
		for (int i = 0; i < dataList.size(); i++) {
			Map<String, String> dataMap = dataList.get(i);

			String serialNumber = "";
			if (dataMap.containsKey(SERIAL_NUMBER)) {
				serialNumber = dataMap.get(SERIAL_NUMBER);
				dataMap.remove(SERIAL_NUMBER);
			}

			Map<String, String> newDataMap = null;
			if (dataHashCodeMap.containsKey(dataMap.hashCode())) {
				duplicationDataIndexList.add(i);
				newDataMap = dataHashCodeMap.get(dataMap.hashCode());
			} else {
				dataHashCodeMap.put(dataMap.hashCode(), dataMap);
				newDataMap = dataMap;
			}

			if (newDataMap != null) {
				List<String> childSerialNumberList = null;
				if (mappingRelationMap.containsKey(newDataMap)) {
					childSerialNumberList = mappingRelationMap.get(newDataMap);
				} else {
					childSerialNumberList = new ArrayList<String>();
					mappingRelationMap.put(newDataMap, childSerialNumberList);
				}
				childSerialNumberList.add(serialNumber);
			}
		}
		if (duplicationDataIndexList.size() > 0) {
			for (int j = duplicationDataIndexList.size() - 1; j >= 0; j--) {
				dataList.remove(duplicationDataIndexList.get(j).intValue());
			}
		}
		
		return dataList;
	}
	
	/**
	 * 数据验证（非空、数据唯一）及字典翻译
	 * @param onlyValidate 唯一验证条件
	 * @param MetaDataFields 元数据字段
	 * @return
	 * @throws BussnissException 
	 */
	private static List<Map<String, String>> validateExcelData(String onlyValidate, List<Map<String, String>> dataList, ConcurrentHashMap<String, ConcurrentHashMap<String, String>> MetaDataFields) throws BussnissException {
		List<Integer> errorDataIndexList = new ArrayList<Integer>();
		HashMap<String, String> validateOnlyValueMap = new HashMap<String, String>();
		
		HashMap<String, String> validateOnlyKeyMap = new HashMap<String, String>();
		if (StringUtils.isNotEmpty(onlyValidate)) {
			String[] validateOnlyArray = onlyValidate.split(",");
			for (String key : validateOnlyArray) {
				validateOnlyKeyMap.put(key, "");
			}
		}
		
		for (int i = 0; i < dataList.size(); i++) {
			Map<String, String> dataMap = dataList.get(i);
			String serialNumber = dataMap.get(SERIAL_NUMBER);

			int nullSign = 0;
			int onlySign = 0;
			boolean judgeSign = true;
			String fieldNullErrorMessage = "";
			String validateOnlyValue = "";
			for (Map.Entry<String, String> entry : dataMap.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				ConcurrentHashMap<String, String> concurrentHashMap = MetaDataFields.get(key);
				if (concurrentHashMap != null) {
					String fieldName = concurrentHashMap.get("FIELD_NAME");
					String nullFlag = concurrentHashMap.get("NULL_FLAG");
					String inputType = concurrentHashMap.get("INPUT_TYPE");
					String inputFormart = concurrentHashMap.get("INPUT_FORMART");
					if (nullFlag.equals("1")) {
						if (StringUtils.isEmpty(value)) {
							nullSign++;
							fieldNullErrorMessage += fieldName + "、";
							if (validateOnlyKeyMap.containsKey(key)) {
								judgeSign = false;
							}
						}
					}
					if (inputType.equals("1")) {
						if (StringUtils.isNotEmpty(inputFormart)) {
							if (StringUtils.isNotEmpty(value)) {
								dataMap.put(key,getDictionariesKey(inputFormart, value));
							}
						} else {
							throw new BussnissException(fieldName + "字段未配置字典翻译");
						}
					}
					if (validateOnlyKeyMap.containsKey(key)) {
						validateOnlyValue += value;
					}
				}
			}

			if (StringUtils.isNotEmpty(fieldNullErrorMessage)) {
				fieldNullErrorMessage = fieldNullErrorMessage.substring(0,fieldNullErrorMessage.length() - 1);
				errorMessage.append("Excel中第").append(serialNumber).append("行").append(fieldNullErrorMessage).append("字段为必填项！").append(NEWLINE);
			}

			if (judgeSign) {
				if (StringUtils.isNotEmpty(onlyValidate)) {
					if (validateOnlyValueMap.containsKey(validateOnlyValue)) {
						onlySign++;
						String rman = validateOnlyValueMap.get(validateOnlyValue);
						errorMessage.append("Excel中第").append(serialNumber).append("行数据与第").append(rman).append("行数据重复！").append(NEWLINE);
					}
					validateOnlyValueMap.put(validateOnlyValue, serialNumber);
				}
			}

			if (nullSign > 0 || onlySign > 0) {
				errorDataIndexList.add(i);
			}
		}
		
		if (errorDataIndexList.size() > 0) {
			for (int j = errorDataIndexList.size() - 1; j >= 0; j--) {
				dataList.remove(errorDataIndexList.get(j).intValue());
			}
		}
		
		return dataList;
	}

	/**
	 * 字典翻译
	 * @param dictionaries 类型关键字
	 * @param text 数据值
	 * @return
	 */
	private static String getDictionariesKey(String dictionaries, String text) {
		String keyString = "";
		ConcurrentHashMap<String, String> dictReference = null;
		try {
			dictReference = RmDictReferenceUtil.getDictReference(dictionaries);
		} catch (BussnissException e) {
			e.printStackTrace();
		}
		if (dictReference.size() > 0) {
			for (String key : dictReference.keySet()) {
				if (text.contains(dictReference.get(key))) {
					keyString = key;
					break;
				}
			}
		}
		return keyString;
	}
	
	/**
	 * 入库操作
	 * @param warehousingRules 入库规则
	 * @param includeChildTable 包含子表
	 * @param mainMetaDataCode 主表元数据编码
	 * @param mainOnlyValidate 主表唯一验证条件
	 * @param mainBusHandleClass 主表业务处理类
	 * @param childMetaDataCode 子表元数据编码
	 * @param childOnlyValidate 子表唯一验证条件
	 * @param childBusHandleClass 子表业务处理类
	 * @param dataSourceCode 数据源编码
	 * @param tokenEntity token
	 * @return
	 * @throws BussnissException 
	 */
	private static StringBuffer insertDataBase(String warehousingRules, String includeChildTable, String mainMetaDataCode, String mainOnlyValidate, String mainBusHandleClass, String childMetaDataCode, String childOnlyValidate, String childBusHandleClass, String mappingType, String mainMappingField, String childMappingField, String dataSourceCode, TokenEntity tokenEntity) throws BussnissException {
		switch (warehousingRules) {
		case "1":
			errorMessage.append(NEWLINE).append("入库规则描述 : 如果存在错误数据，所有数据不保存数据库，返回提示信息。");
			break;
		case "2":
			// 与数据库进行验证，保证数据唯一
			dataList = validateWithDataBase(mainMetaDataCode, mainOnlyValidate, dataList);

			if (dataList.size() > 0) {
				String[] insertByTransfrom = {};
				if (includeChildTable.equals("0")) {
					if (StringUtils.isNotEmpty(mainBusHandleClass)) {
						// 调用业务处理之前
						dataList = beforeCallBusHandleClass(dataList);
						// 调用业务处理类
						dataList = excelBusHandleClass(mainBusHandleClass, dataSourceCode, dataList, tokenEntity);
						// 调用业务处理之后
						dataList = afterCallBusHandleClass(dataList);
					}
					
					insertByTransfrom = BaseDao.getBaseDao().insertByTransfrom(mainMetaDataCode, dataList);
					errorMessage.append("本次共计导入" + DATA_SIZE + "条数据，导入成功" + insertByTransfrom.length + "条，导入失败" + (DATA_SIZE - insertByTransfrom.length) + "条！").append(NEWLINE);
				} else {
					// 与数据库进行验证，保证数据唯一
					childDataList = validateWithDataBase(childMetaDataCode, childOnlyValidate, childDataList);
					
					// 数据去重
					dataList = dataRemoveDuplication(dataList);
					
					// 添加映射关系
					List<Map<String, String>> newChildDataList = addMappingRelation(mappingType, dataSourceCode, mainMetaDataCode, childMappingField, mainMappingField, childMetaDataCode);
					
					if (StringUtils.isNotEmpty(mainBusHandleClass)) {
						// 调用业务处理之前
						dataList = beforeCallBusHandleClass(dataList);
						// 调用业务处理
						dataList = excelBusHandleClass(mainBusHandleClass, dataSourceCode, dataList, tokenEntity);
						// 调用业务处理之后
						dataList = afterCallBusHandleClass(dataList);
					}
					
					if (StringUtils.isNotEmpty(childBusHandleClass)) {
						// 调用业务处理之前
						childDataList = beforeCallBusHandleClass(newChildDataList);
						// 调用业务处理
						childDataList = excelBusHandleClass(childBusHandleClass, dataSourceCode, childDataList, tokenEntity);
						// 调用业务处理之后
						childDataList = afterCallBusHandleClass(childDataList);
					}
					
					if(dataList.size() > 0){
						insertByTransfrom = BaseDao.getBaseDao().insertByTransfrom(mainMetaDataCode, dataList);
						errorMessage.append("本次导入成功单据数共" + insertByTransfrom.length + "张!").append(NEWLINE);
					}
					
					if(childDataList.size() > 0){
						insertByTransfrom = BaseDao.getBaseDao().insertByTransfrom(childMetaDataCode, childDataList);
						errorMessage.append("其中单据明细数共" + DATA_SIZE + "条，导入成功" + insertByTransfrom.length + "条，导入失败" + (DATA_SIZE - insertByTransfrom.length) + "条！").append(NEWLINE);
					}
				}
			}

			errorMessage.append(NEWLINE).append("入库规则描述 : 如果存在错误数据，错误数据返回提示信息，正确数据保存数据库。");
			break;
		default:
			break;
		}
		
		return errorMessage;
	}
	
	/**
	 * 与数据库进行验证，保证数据唯一
	 * @param metaDataCode 元数据编码
	 * @param onlyValidate 唯一验证条件
	 * @param dataList 数据
	 * @return
	 */
	private static List<Map<String, String>> validateWithDataBase(String metaDataCode, String onlyValidate, List<Map<String, String>> dataList) {
		List<Integer> duplicationDataIndexList = new ArrayList<Integer>();

		if (StringUtils.isNotEmpty(onlyValidate)) {
			String[] onlyValidateArray = onlyValidate.split(",");
			for (int i = 0; i < dataList.size(); i++) {
				Map<String, String> dataMap = dataList.get(i);
				String serialNumber = dataMap.get(SERIAL_NUMBER);

				String sql = "SELECT * FROM " + metaDataCode + " WHERE 1=1";
				for (int j = 0; j < onlyValidateArray.length; j++) {
					sql += " AND " + onlyValidateArray[j] + " = '" + dataMap.get(onlyValidateArray[j]) + "' ";
				}
				List<Map<String, Object>> onlyValidateList = BaseDao.getBaseDao().query(sql, "");

				if (onlyValidateList.size() > 0) {
					duplicationDataIndexList.add(i);
					errorMessage.append("Excel中第").append(serialNumber).append("行数据在数据库中已存在！").append(NEWLINE);
				}
			}
		}

		if (duplicationDataIndexList.size() > 0) {
			for (int i = duplicationDataIndexList.size() - 1; i >= 0; i--) {
				dataList.remove(duplicationDataIndexList.get(i).intValue());
			}
		}

		return dataList;
	}
	
	/**
	 * 业务处理类之前
	 * @param dataList 数据
	 * @return
	 */
	private static List<Map<String, String>> beforeCallBusHandleClass(List<Map<String, String>> dataList){
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(BUS_ERROR_MESSAGE, "");
		dataList.add(map);
		return dataList;
	}
	
	/**
	 * 业务处理类
	 * @param busHandleClass 业务处理类
	 * @param dataSourceCode 数据源编码
	 * @param bodyList 数据
	 * @param tokenEntity token
	 * @return
	 */
	private static List<Map<String, String>> excelBusHandleClass(String busHandleClass, String dataSourceCode, List<Map<String, String>> dataList, TokenEntity tokenEntity) {
		try {
			ExcelHandleData excelHandleData = (ExcelHandleData) Class.forName(busHandleClass).newInstance();
			dataList = excelHandleData.HandleList(dataSourceCode, dataList, tokenEntity);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return dataList;
	}
	
	
	/**
	 * 业务处理类之后
	 * @param dataList 数据
	 * @return
	 */
	private static List<Map<String, String>> afterCallBusHandleClass(List<Map<String, String>> dataList){
		String busErrorMessage = dataList.get(dataList.size() - 1).get(BUS_ERROR_MESSAGE);
		if (StringUtils.isNotEmpty(busErrorMessage)) {
			errorMessage.append(busErrorMessage);
		}
		dataList.remove(dataList.size() - 1);
		return dataList;
	}
	
	/**
	 * 添加映射关系
	 * @param mappingType 映射关系
	 * @param dataSourceCode 数据源编码
	 * @param mainMetaDataCode 主表元数据编码
	 * @param childMappingField 子表映射字段
	 * @param mainMappingField 主表映射字段
	 * @return
	 */
	private static List<Map<String, String>> addMappingRelation(String mappingType, String dataSourceCode, String mainMetaDataCode, String childMappingField, String mainMappingField, String childMetaDataCode) {
		List<Map<String, String>> newChildDataList = new ArrayList<Map<String, String>>();

		Map<String, Map<String, String>> childData = new HashMap<String, Map<String, String>>();
		for (Map<String, String> childDataMap : childDataList) {
			childData.put(childDataMap.get(SERIAL_NUMBER), childDataMap);
		}
		
		String[] relationSign = {};
		if (mappingType.equals("1")) {
			String code = "";
			if (dataSourceCode.equals("FIN_ESTIMATED_INCOME")) {
				code = "jszg";
			} else if (dataSourceCode.equals("FIN_CONFIRM_INCOME")) {
				code = "jsqr";
			}
			Calendar calendar = Calendar.getInstance();
			String yearMonth = calendar.get(Calendar.YEAR) + String.format("%02d", calendar.get(Calendar.MONTH) + 1);
			relationSign = SerialNumberUtil.getSerialCode(SerialType.THREE_BIT_CODE, code + yearMonth, dataList.size());
		} else {
			relationSign = RmIdFactory.requestId(mainMetaDataCode, dataList.size());
		}
		
		for (int i = 0; i < dataList.size(); i++) {
			Map<String, String> dataMap = dataList.get(i);
			List<String> childSerialNumberList = mappingRelationMap.get(dataMap);
			if (childSerialNumberList.size() > 0) {
				for (String childSerialNumber : childSerialNumberList) {
					Map<String, String> childMap = childData.get(childSerialNumber);
					if(childMap != null){
						childMap.put(childMappingField, relationSign[i]);
						newChildDataList.add(childMap);
					}
				}
			}
			dataMap.put(mainMappingField, relationSign[i]);
			dataMap.put(SERIAL_NUMBER, childSerialNumberList.get(0));
		}
		
		if(dataSourceCode.equals("FIN_CONFIRM_INCOME")){
			String[] ids = RmIdFactory.requestId(childMetaDataCode, newChildDataList.size());
			for(int j = 0;j < newChildDataList.size();j++){
				Map<String, String> newChildDataMap = newChildDataList.get(j);
				newChildDataMap.put("ID", ids[j]);
			}
		}
		
		return newChildDataList;
	}
	
}