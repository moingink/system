package com.yonyou.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.yonyou.business.RmDictReferenceUtil;
import com.yonyou.business.button.util.IPublicBusColumn;
import com.yonyou.business.entity.TokenEntity;
import com.yonyou.business.entity.TokenUtil;
import com.yonyou.util.SerialNumberUtil.SerialType;
import com.yonyou.util.jdbc.BaseDao;

public class Import_Excel {
	@SuppressWarnings({ "deprecation", "resource", "unused" })
	public static String uploadAffixs(MultipartFile files, String pageCode,HttpServletRequest request)throws Exception {
		TokenEntity tokenEntity = TokenUtil.initTokenEntity(request);
		String sign = "false";//true:成功；false：失败；
		Map<String, String> map = new HashMap<String, String>();
		List<Map<String,String>>listmap = new ArrayList<Map<String,String>>();
		String fileName = files.getOriginalFilename();
		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
		int x = 10000, y = 10000;
		if ("FIN_TICKET_IMPORT".equals(pageCode)) {
			if ("xls".equals(suffix)) {
				HSSFWorkbook workbook = new HSSFWorkbook(files.getInputStream());
				// 读取获取到的第一个sheet
				HSSFSheet sheet = workbook.getSheetAt(0);
				// 用于存放excel内容体
				List<List<String>> bodyList = new ArrayList<List<String>>();
				// 声明一个引用,用于临时存放每一行数据
				List<String> tmpList = null;
				for (int j = 0; j < sheet.getLastRowNum() + 1; j++) {
					// 创建一个行对象
					if (sheet.getRow(j) == null) {
						j++;
					}
					HSSFRow row = sheet.getRow(j);
					// 创建一个临时对象
					tmpList = new ArrayList<String>();
					// 把一行里的每一个字段遍历出来
					for (int i = 0; i < row.getLastCellNum(); i++) {
						// 创建一个行里的一个字段的对象，也就是获取到的一个单元格中的值
						HSSFCell cell = row.getCell(i);
						// if(row.getCell(i)!=null){
						// row.getCell(i).setCellType(cell.CELL_TYPE_STRING);
						// }
						if (cell != null) {
							switch (cell.getCellType()) {
							case HSSFCell.CELL_TYPE_STRING:
								if ("发票类别：专用发票".equals(cell
										.getRichStringCellValue().getString())) {
									System.err
											.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~="
													+ j);
									for (int l = j + 2; l < sheet
											.getLastRowNum(); l++) {
										if (l > x) {
											continue;
										}
										if (sheet.getRow(l) == null) {
											l++;
										}
										row = sheet.getRow(l);

										for (int k = 0; k < row
												.getLastCellNum(); k++) {
											cell = row.getCell(k);
											if (cell != null) {
												switch (cell.getCellType()) {
												case HSSFCell.CELL_TYPE_STRING:
													map.put("INVOICE_TYPE",
															"专用发票");
													if (cell.getRichStringCellValue()
															.getString()
															.contains("份数")) {
														String fjs[] = jq(cell
																.getRichStringCellValue()
																.getString());
														map.put("number_cop"
																.toUpperCase(),
																fjs[0]); // '份数'
														map.put("amount_money"
																.toUpperCase(),
																fjs[1]);// '金额'
														map.put("tax_money"
																.toUpperCase(),
																fjs[2]);// '税额'
														x = l;
														System.err
																.println("^^^^^^^^="
																		+ x);
														continue;
													} else {
														if (k == 0
																&& !"发票类别：专用发票"
																		.equals(cell
																				.getRichStringCellValue()
																				.getString())) {
															map.put("ticket_code"
																	.toUpperCase(),
																	cell.getRichStringCellValue()
																			.getString()); // '发票代码'
														} else if (k == 1) {
															map.put("ticket_number"
																	.toUpperCase(),
																	cell.getRichStringCellValue()
																			.getString());// '发票号码'
														} else if (k == 2) {
															map.put("pur_enter_name"
																	.toUpperCase(),
																	cell.getRichStringCellValue()
																			.getString());// '购方企业名称'
														} else if (k == 3) {
															map.put("enter_tax_number"
																	.toUpperCase(),
																	cell.getRichStringCellValue()
																			.getString());// '购方税号'
														} else if (k == 4) {
															map.put("bank_account"
																	.toUpperCase(),
																	cell.getRichStringCellValue()
																			.getString());// '银行账号'
														} else if (k == 5) {
															map.put("address_phone"
																	.toUpperCase(),
																	cell.getRichStringCellValue()
																			.getString());// '地址电话'
														}
														// else if(k==6){
														// map.put("open_ticket_time".toUpperCase(),cell.getRichStringCellValue().getString());//'开票日期'
														// }
														else if (k == 7) {
															map.put("bus_code_id"
																	.toUpperCase(),
																	cell.getRichStringCellValue()
																			.getString());// '商品编码版本'
														} else if (k == 8) {
															map.put("document_number"
																	.toUpperCase(),
																	cell.getRichStringCellValue()
																			.getString());// '单据号'
														} else if (k == 9) {
															map.put("bus_name"
																	.toUpperCase(),
																	cell.getRichStringCellValue()
																			.getString());// '商品名称'
														} else if (k == 10) {
															map.put("specifications"
																	.toUpperCase(),
																	cell.getRichStringCellValue()
																			.getString());// '规格'
														} else if (k == 11) {
															map.put("company"
																	.toUpperCase(),
																	cell.getRichStringCellValue()
																			.getString());// '单位'
														} else if (k == 12) {
															map.put("number"
																	.toUpperCase(),
																	cell.getRichStringCellValue()
																			.getString());// '数量'
														} else if (k == 13) {
															map.put("price"
																	.toUpperCase(),
																	cell.getRichStringCellValue()
																			.getString());// '单价'
														} else if (k == 14) {
															map.put("amount_money1"
																	.toUpperCase(),
																	cell.getRichStringCellValue()
																			.getString());// '金额'
														} else if (k == 15) {
															map.put("tax"
																	.toUpperCase(),
																	cell.getRichStringCellValue()
																			.getString());// '税率'
														} else if (k == 16) {
															map.put("tax_money1"
																	.toUpperCase(),
																	cell.getRichStringCellValue()
																			.getString());// '税额'
														} else if (k == 17) {
															map.put("tax_type_code"
																	.toUpperCase(),
																	cell.getRichStringCellValue()
																			.getString());// '税收分类编码'
														}
														break;
													}

												case HSSFCell.CELL_TYPE_NUMERIC:
													map.put("INVOICE_TYPE",
															"专用发票");
													if (String
															.valueOf(
																	cell.getNumericCellValue())
															.contains("份数")) {
														String fjs[] = jq(String
																.valueOf(cell
																		.getNumericCellValue()));
														map.put("number_cop"
																.toUpperCase(),
																fjs[0]); // '份数'
														map.put("amount_money"
																.toUpperCase(),
																fjs[1]);// '金额'
														map.put("tax_money"
																.toUpperCase(),
																fjs[2]);// '税额'
														x = l;
														System.err
																.println("************************="
																		+ x);
														continue;
													} else {
														if (k == 0
																&& !"发票类别：专用发票"
																		.equals(String
																				.valueOf(cell
																						.getNumericCellValue()))) {
															map.put("ticket_code"
																	.toUpperCase(),
																	String.valueOf(cell
																			.getNumericCellValue())); // '发票代码'
														} else if (k == 1) {
															map.put("ticket_number"
																	.toUpperCase(),
																	String.valueOf(cell
																			.getNumericCellValue()));// '发票号码'
														} else if (k == 2) {
															map.put("pur_enter_name"
																	.toUpperCase(),
																	String.valueOf(cell
																			.getNumericCellValue()));// '购方企业名称'
														} else if (k == 3) {
															map.put("enter_tax_number"
																	.toUpperCase(),
																	String.valueOf(cell
																			.getNumericCellValue()));// '购方税号'
														} else if (k == 4) {
															map.put("bank_account"
																	.toUpperCase(),
																	String.valueOf(cell
																			.getNumericCellValue()));// '银行账号'
														} else if (k == 5) {
															map.put("address_phone"
																	.toUpperCase(),
																	String.valueOf(cell
																			.getNumericCellValue()));// '地址电话'
														}
														// else if(k==6){
														// map.put("open_ticket_time".toUpperCase(),String.valueOf(cell.getNumericCellValue()));//'开票日期'
														// }
														else if (k == 7) {
															map.put("bus_code_id"
																	.toUpperCase(),
																	String.valueOf(cell
																			.getNumericCellValue()));// '商品编码版本'
														} else if (k == 8) {
															map.put("document_number"
																	.toUpperCase(),
																	String.valueOf(cell
																			.getNumericCellValue()));// '单据号'
														} else if (k == 9) {
															map.put("bus_name"
																	.toUpperCase(),
																	String.valueOf(cell
																			.getNumericCellValue()));// '商品名称'
														} else if (k == 10) {
															map.put("specifications"
																	.toUpperCase(),
																	String.valueOf(cell
																			.getNumericCellValue()));// '规格'
														} else if (k == 11) {
															map.put("company"
																	.toUpperCase(),
																	String.valueOf(cell
																			.getNumericCellValue()));// '单位'
														} else if (k == 12) {
															map.put("number"
																	.toUpperCase(),
																	String.valueOf(cell
																			.getNumericCellValue()));// '数量'
														} else if (k == 13) {
															map.put("price"
																	.toUpperCase(),
																	String.valueOf(cell
																			.getNumericCellValue()));// '单价'
														} else if (k == 14) {
															map.put("amount_money1"
																	.toUpperCase(),
																	String.valueOf(cell
																			.getNumericCellValue()));// '金额'
														} else if (k == 15) {
															map.put("tax"
																	.toUpperCase(),
																	String.valueOf(cell
																			.getNumericCellValue()));// '税率'
														} else if (k == 16) {
															map.put("tax_money1"
																	.toUpperCase(),
																	String.valueOf(cell
																			.getNumericCellValue()));// '税额'
														} else if (k == 17) {
															map.put("tax_type_code"
																	.toUpperCase(),
																	String.valueOf(cell
																			.getNumericCellValue()));// '税收分类编码'
														}
														break;
													}

												default:
													break;
												}
											}

										}
//										BaseDao.getBaseDao().insertByTransfrom(
//												pageCode, map);
//										map.clear();
										listmap.add(map);
										map=new HashMap<String, String>();
										map.clear();
									}
								}// 外if结束
								else if ("发票类别：普通发票".equals(cell
										.getRichStringCellValue().getString())) {

									
									for (int l = j + 2; l < sheet
											.getLastRowNum(); l++) {

										if (l > y) {
											continue;
										}
										if (sheet.getRow(l) == null) {
											l++;
										}
										row = sheet.getRow(l);

										for (int k = 0; k < row
												.getLastCellNum(); k++) {
											cell = row.getCell(k);
											if (cell != null) {
												switch (cell.getCellType()) {
												case HSSFCell.CELL_TYPE_STRING:
													map.put("INVOICE_TYPE",
															"普通发票");
													if (cell.getRichStringCellValue()
															.getString()
															.contains("份数")) {
														String fjs[] = jq(cell
																.getRichStringCellValue()
																.getString());
														map.put("number_cop"
																.toUpperCase(),
																fjs[0]); // '份数'
														map.put("amount_money"
																.toUpperCase(),
																fjs[1]);// '金额'
														map.put("tax_money"
																.toUpperCase(),
																fjs[2]);// '税额'
														y = l;
													
														continue;
													} else {
														if (k == 0
																&& !"发票类别：普通发票"
																		.equals(cell
																				.getRichStringCellValue()
																				.getString())) {
															map.put("ticket_code"
																	.toUpperCase(),
																	cell.getRichStringCellValue()
																			.getString()); // '发票代码'
														} else if (k == 1) {
															map.put("ticket_number"
																	.toUpperCase(),
																	cell.getRichStringCellValue()
																			.getString());// '发票号码'
														} else if (k == 2) {
															map.put("pur_enter_name"
																	.toUpperCase(),
																	cell.getRichStringCellValue()
																			.getString());// '购方企业名称'
														} else if (k == 3) {
															map.put("enter_tax_number"
																	.toUpperCase(),
																	cell.getRichStringCellValue()
																			.getString());// '购方税号'
														} else if (k == 4) {
															map.put("bank_account"
																	.toUpperCase(),
																	cell.getRichStringCellValue()
																			.getString());// '银行账号'
														} else if (k == 5) {
															map.put("address_phone"
																	.toUpperCase(),
																	cell.getRichStringCellValue()
																			.getString());// '地址电话'
														}
														// else if(k==6){
														// map.put("open_ticket_time".toUpperCase(),cell.getRichStringCellValue().getString());//'开票日期'
														// }
														else if (k == 7) {
															map.put("bus_code_id"
																	.toUpperCase(),
																	cell.getRichStringCellValue()
																			.getString());// '商品编码版本'
														} else if (k == 8) {
															map.put("document_number"
																	.toUpperCase(),
																	cell.getRichStringCellValue()
																			.getString());// '单据号'
														} else if (k == 9) {
															map.put("bus_name"
																	.toUpperCase(),
																	cell.getRichStringCellValue()
																			.getString());// '商品名称'
														} else if (k == 10) {
															map.put("specifications"
																	.toUpperCase(),
																	cell.getRichStringCellValue()
																			.getString());// '规格'
														} else if (k == 11) {
															map.put("company"
																	.toUpperCase(),
																	cell.getRichStringCellValue()
																			.getString());// '单位'
														} else if (k == 12) {
															map.put("number"
																	.toUpperCase(),
																	cell.getRichStringCellValue()
																			.getString());// '数量'
														} else if (k == 13) {
															map.put("price"
																	.toUpperCase(),
																	cell.getRichStringCellValue()
																			.getString());// '单价'
														} else if (k == 14) {
															map.put("amount_money1"
																	.toUpperCase(),
																	cell.getRichStringCellValue()
																			.getString());// '金额'
														} else if (k == 15) {
															map.put("tax"
																	.toUpperCase(),
																	cell.getRichStringCellValue()
																			.getString());// '税率'
														} else if (k == 16) {
															map.put("tax_money1"
																	.toUpperCase(),
																	cell.getRichStringCellValue()
																			.getString());// '税额'
														} else if (k == 17) {
															map.put("tax_type_code"
																	.toUpperCase(),
																	cell.getRichStringCellValue()
																			.getString());// '税收分类编码'
														}
														break;
													}

												case HSSFCell.CELL_TYPE_NUMERIC:
													map.put("INVOICE_TYPE",
															"普通发票");
													if (String
															.valueOf(
																	cell.getNumericCellValue())
															.contains("份数")) {
														String fjs[] = jq(String
																.valueOf(cell
																		.getNumericCellValue()));
														map.put("number_cop"
																.toUpperCase(),
																fjs[0]); // '份数'
														map.put("amount_money"
																.toUpperCase(),
																fjs[1]);// '金额'
														map.put("tax_money"
																.toUpperCase(),
																fjs[2]);// '税额'
														x = l;
														System.err
																.println("************************="
																		+ x);
														continue;
													} else {
														if (k == 0
																&& !"发票类别：普通发票"
																		.equals(String
																				.valueOf(cell
																						.getNumericCellValue()))) {
															map.put("ticket_code"
																	.toUpperCase(),
																	String.valueOf(cell
																			.getNumericCellValue())); // '发票代码'
														} else if (k == 1) {
															map.put("ticket_number"
																	.toUpperCase(),
																	String.valueOf(cell
																			.getNumericCellValue()));// '发票号码'
														} else if (k == 2) {
															map.put("pur_enter_name"
																	.toUpperCase(),
																	String.valueOf(cell
																			.getNumericCellValue()));// '购方企业名称'
														} else if (k == 3) {
															map.put("enter_tax_number"
																	.toUpperCase(),
																	String.valueOf(cell
																			.getNumericCellValue()));// '购方税号'
														} else if (k == 4) {
															map.put("bank_account"
																	.toUpperCase(),
																	String.valueOf(cell
																			.getNumericCellValue()));// '银行账号'
														} else if (k == 5) {
															map.put("address_phone"
																	.toUpperCase(),
																	String.valueOf(cell
																			.getNumericCellValue()));// '地址电话'
														}
														// else if(k==6){
														// map.put("open_ticket_time".toUpperCase(),String.valueOf(cell.getNumericCellValue()));//'开票日期'
														// }
														else if (k == 7) {
															map.put("bus_code_id"
																	.toUpperCase(),
																	String.valueOf(cell
																			.getNumericCellValue()));// '商品编码版本'
														} else if (k == 8) {
															map.put("document_number"
																	.toUpperCase(),
																	String.valueOf(cell
																			.getNumericCellValue()));// '单据号'
														} else if (k == 9) {
															map.put("bus_name"
																	.toUpperCase(),
																	String.valueOf(cell
																			.getNumericCellValue()));// '商品名称'
														} else if (k == 10) {
															map.put("specifications"
																	.toUpperCase(),
																	String.valueOf(cell
																			.getNumericCellValue()));// '规格'
														} else if (k == 11) {
															map.put("company"
																	.toUpperCase(),
																	String.valueOf(cell
																			.getNumericCellValue()));// '单位'
														} else if (k == 12) {
															map.put("number"
																	.toUpperCase(),
																	String.valueOf(cell
																			.getNumericCellValue()));// '数量'
														} else if (k == 13) {
															map.put("price"
																	.toUpperCase(),
																	String.valueOf(cell
																			.getNumericCellValue()));// '单价'
														} else if (k == 14) {
															map.put("amount_money1"
																	.toUpperCase(),
																	String.valueOf(cell
																			.getNumericCellValue()));// '金额'
														} else if (k == 15) {
															map.put("tax"
																	.toUpperCase(),
																	String.valueOf(cell
																			.getNumericCellValue()));// '税率'
														} else if (k == 16) {
															map.put("tax_money1"
																	.toUpperCase(),
																	String.valueOf(cell
																			.getNumericCellValue()));// '税额'
														} else if (k == 17) {
															map.put("tax_type_code"
																	.toUpperCase(),
																	String.valueOf(cell
																			.getNumericCellValue()));// '税收分类编码'
														}
														break;
													}

												default:
													break;
												}
											}

										}
//										BaseDao.getBaseDao().insertByTransfrom(
//												pageCode, map);
//										map.clear();
										listmap.add(map);
										map=new HashMap<String, String>();
										map.clear();
									}
								}
								break;// 外循环
							default:
								break;
							}
						} else {
							//tmpList.add(null);
						}

					}

					// bodyList.add(tmpList);

				}
				if(listmap.toString()!=null){
					sign = "true";
				}
                BaseDao.getBaseDao().insertByTransfrom(pageCode, listmap);
			} 
		}else if ("FIN_ICBC_ACCOUNT".equals(pageCode)) {//工行银行流水导入
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			String code = String.valueOf(cal.get(Calendar.YEAR))+String.valueOf(String.format("%02d",cal.get(Calendar.MONDAY)+1))+String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
			String importBatch = SerialNumberUtil.getSerialCode(SerialType.THREE_BIT_CODE,code);
			if (suffix.equals("xlsx")) {
				XSSFWorkbook workbook = new XSSFWorkbook(files.getInputStream());
				//获取第一个sheet
				XSSFSheet sheet = workbook.getSheetAt(0);
				//循环行
				for (int i = 2; i < sheet.getLastRowNum() + 1; i++) {
					//当前行
					XSSFRow row = sheet.getRow(i);
					HashMap<String, String> rowMap = new HashMap<String,String>();
					//循环列
					for(int j = 0; j < row.getLastCellNum(); j++){
						//当前列
						XSSFCell cell = row.getCell(j);
						String cellValue = "";
						if(cell != null){
							switch (cell.getCellType()) {
							case 0:// 数值型
								if (DateUtil.isCellDateFormatted(cell)) {
									cellValue = sdf.format(cell.getDateCellValue());
								} else {
									cellValue = String.valueOf(cell.getNumericCellValue()).trim();
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
							if (j == 0) {
								rowMap.put("VOUCHER_ID", cellValue);// 凭证号
							} else if (j == 1) {
								rowMap.put("OTHER_ACCOUNT_NUM", cellValue);// 对方账号
							} else if (j == 2) {
								rowMap.put("TRADING_TIME", cellValue);// 交易时间
							} else if (j == 3) {
								rowMap.put("LOAN_SIGN", cellValue);// 借贷标志
							} else if (j == 4) {
								rowMap.put("OTHER_DEP", cellValue);// 对方单位
							} else if (j == 5) {
								rowMap.put("OTHER_BANK_NUM", cellValue);// 对方行号
							} else if (j == 6) {
								rowMap.put("PURPOSE", cellValue);// 用途
							} else if (j == 7) {
								rowMap.put("ABSTRACT", cellValue);// 摘要
							} else if (j == 8) {
								rowMap.put("AN_ACC_STATEMENT", cellValue);// 附言
							} else if (j == 9) {
								rowMap.put("RETURN_PERSON_MESSAGE", cellValue);// 回单个性化信息
							} else if (j == 10) {
								rowMap.put("ADMISSION_TIME", cellValue);// 入账日期
							} else if (j == 11) {
								rowMap.put("INTO_PRICE", replaceByComma(cellValue));// 转入金额
							} else if (j == 12) {
								rowMap.put("INOUT_PRICE", replaceByComma(cellValue));// 转出金额
							} else if (j == 13) {
								rowMap.put("HAPPEN_PRICE", replaceByComma(cellValue));// 发生额
							} else if (j == 14) {
								rowMap.put("TIME_STAMP", cellValue);// 时间戳
							}
							rowMap.put("IMPORT_NAME", tokenEntity.USER.getName());// 导入人
							rowMap.put("IMPORT_TIME", sdf.format(new Date()));// 导入时间
							rowMap.put("IMPORT_BATCH", importBatch);// 导入批次号
							if(!tokenEntity.isEmpty()){
								//制单人
								rowMap.put(IPublicBusColumn.CREATOR_ID, tokenEntity.USER.getId());
								rowMap.put(IPublicBusColumn.CREATOR_NAME, tokenEntity.USER.getName());
						        	
								//组织字段
								rowMap.put(IPublicBusColumn.ORGANIZATION_ID, tokenEntity.COMPANY.getCompany_id());
								rowMap.put(IPublicBusColumn.ORGANIZATION_NAME, tokenEntity.COMPANY.getCompany_name());
								
								//部门信息
								rowMap.put(IPublicBusColumn.COMPANY_ID, tokenEntity.COMPANY.getCompany_id());
								rowMap.put(IPublicBusColumn.COMPANY_NAME, tokenEntity.COMPANY.getCompany_name());
								rowMap.put(IPublicBusColumn.BILL_STATUS, "0");
							}
						}
					}
					
					//银行流水数据唯一标识：对方单位、金额、交易时间；导入时根据此唯一标识校验数据唯一性
					StringBuffer ICBCAccountSql = new StringBuffer();
					String otherDep =  rowMap.get("OTHER_DEP");
					String tradingTime = rowMap.get("TRADING_TIME");
					String intoPrice = rowMap.get("INTO_PRICE");
					String inoutPrice = rowMap.get("INOUT_PRICE");
					if(otherDep != null){
						ICBCAccountSql.append(" AND OTHER_DEP = '"+otherDep+"' ");
					}
					if(tradingTime != null){
						ICBCAccountSql.append(" AND TRADING_TIME = '"+tradingTime+"' ");
					}
					if(intoPrice != null){
						ICBCAccountSql.append(" AND INTO_PRICE = '"+intoPrice+"' ");
					}
					if(inoutPrice != null){
						ICBCAccountSql.append("	AND INOUT_PRICE = '"+inoutPrice+"' ");
					}
					if(ICBCAccountSql.length() > 0){
						ICBCAccountSql.insert(0, "SELECT * FROM FIN_ICBC_ACCOUNT WHERE 1 = 1 ");
					}
					List<Map<String,Object>> ICBCAccountList = BaseDao.getBaseDao().query(ICBCAccountSql.toString(), "");
					if(ICBCAccountList.size() > 0){
						sign = "";
						sign += i + 1 + "、";
					}
					listmap.add(rowMap);
				}
			}else if(suffix.equals("xls")){
				HSSFWorkbook workbook = new HSSFWorkbook(files.getInputStream());
				//读取获取到的第一个sheet
				HSSFSheet sheet = workbook.getSheetAt(0);
				//循环行
				for (int i = 2; i < sheet.getLastRowNum() + 1; i++) {
					//当前行
					HSSFRow row = sheet.getRow(i);
					HashMap<String, String> rowMap = new HashMap<String,String>();
					//循环列
					for(int j = 0; j < row.getLastCellNum(); j++){
						//当前列
						HSSFCell cell = row.getCell(j);
						String cellValue = "";
						if(cell != null){
							switch (cell.getCellType()) {
							case 0:// 数值型
								if (DateUtil.isCellDateFormatted(cell)) {
									cellValue = sdf.format(cell.getDateCellValue());
								} else {
									cellValue = String.valueOf(cell.getNumericCellValue()).trim();
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
							if (j == 0) {
								rowMap.put("VOUCHER_ID", cellValue);// 凭证号
							} else if (j == 1) {
								rowMap.put("OTHER_ACCOUNT_NUM", cellValue);// 对方账号
							} else if (j == 2) {
								rowMap.put("TRADING_TIME", cellValue);// 交易时间
							} else if (j == 3) {
								rowMap.put("LOAN_SIGN", cellValue);// 借贷标志
							} else if (j == 4) {
								rowMap.put("OTHER_DEP", cellValue);// 对方单位
							} else if (j == 5) {
								rowMap.put("OTHER_BANK_NUM", cellValue);// 对方行号
							} else if (j == 6) {
								rowMap.put("PURPOSE", cellValue);// 用途
							} else if (j == 7) {
								rowMap.put("ABSTRACT", cellValue);// 摘要
							} else if (j == 8) {
								rowMap.put("AN_ACC_STATEMENT", cellValue);// 附言
							} else if (j == 9) {
								rowMap.put("RETURN_PERSON_MESSAGE", cellValue);// 回单个性化信息
							} else if (j == 10) {
								rowMap.put("ADMISSION_TIME", cellValue);// 入账日期
							} else if (j == 11) {
								rowMap.put("INTO_PRICE", replaceByComma(cellValue));// 转入金额
							} else if (j == 12) {
								rowMap.put("INOUT_PRICE", replaceByComma(cellValue));// 转出金额
							} else if (j == 13) {
								rowMap.put("HAPPEN_PRICE", replaceByComma(cellValue));// 发生额
							} else if (j == 14) {
								rowMap.put("TIME_STAMP", cellValue);// 时间戳
							}
							rowMap.put("IMPORT_NAME", tokenEntity.USER.getName());// 导入人
							rowMap.put("IMPORT_TIME", sdf.format(new Date()));// 导入时间
							rowMap.put("IMPORT_BATCH", importBatch);// 导入批次号
							if(!tokenEntity.isEmpty()){
								//制单人
								rowMap.put(IPublicBusColumn.CREATOR_ID, tokenEntity.USER.getId());
								rowMap.put(IPublicBusColumn.CREATOR_NAME, tokenEntity.USER.getName());
						        	
								//组织字段
								rowMap.put(IPublicBusColumn.ORGANIZATION_ID, tokenEntity.COMPANY.getCompany_id());
								rowMap.put(IPublicBusColumn.ORGANIZATION_NAME, tokenEntity.COMPANY.getCompany_name());
								
								//部门信息
								rowMap.put(IPublicBusColumn.COMPANY_ID, tokenEntity.COMPANY.getCompany_id());
								rowMap.put(IPublicBusColumn.COMPANY_NAME, tokenEntity.COMPANY.getCompany_name());
								rowMap.put(IPublicBusColumn.BILL_STATUS, "0");
							}
						}
					}
					
					//银行流水数据唯一标识：对方单位、金额、交易时间；导入时根据此唯一标识校验数据唯一性
					StringBuffer ICBCAccountSql = new StringBuffer();
					String otherDep =  rowMap.get("OTHER_DEP");
					String tradingTime = rowMap.get("TRADING_TIME");
					String intoPrice = rowMap.get("INTO_PRICE");
					String inoutPrice = rowMap.get("INOUT_PRICE");
					if(otherDep != null){
						ICBCAccountSql.append(" AND OTHER_DEP = '"+otherDep+"' ");
					}
					if(tradingTime != null){
						ICBCAccountSql.append(" AND TRADING_TIME = '"+tradingTime+"' ");
					}
					if(intoPrice != null){
						ICBCAccountSql.append(" AND INTO_PRICE = '"+intoPrice+"' ");
					}
					if(inoutPrice != null){
						ICBCAccountSql.append("	AND INOUT_PRICE = '"+inoutPrice+"' ");
					}
					if(ICBCAccountSql.length() > 0){
						ICBCAccountSql.insert(0, "SELECT * FROM FIN_ICBC_ACCOUNT WHERE 1 = 1 ");
					}
					List<Map<String,Object>> ICBCAccountList = BaseDao.getBaseDao().query(ICBCAccountSql.toString(), "");
					if(ICBCAccountList.size() > 0){
						sign = "";
						sign += i + 1 + "、";
					}
					listmap.add(rowMap);
				}
			}
			
			//工行账户中对方单位为联通集团财务有限公司的数据及收入金额为0的数据不导入
			Iterator<Map<String, String>> iterator = listmap.iterator();
	        while(iterator.hasNext()){
	        	Map<String, String> iteratorMap = iterator.next();
	        	if(iteratorMap.get("OTHER_DEP").equals("联通集团财务有限公司") || iteratorMap.get("INTO_PRICE").equals("") || new BigDecimal(iteratorMap.get("INTO_PRICE")).compareTo(new BigDecimal("0")) == 0){
	        		 iterator.remove();
				}
	        }
	        
			if(!sign.equals("true") && !sign.equals("false")){
				return sign.substring(0,sign.length()-1);
			}
			
			if(listmap.size() > 0){
				sign = "true";
				BaseDao.getBaseDao().insertByTransfrom(pageCode, listmap);
			}
		} else if ("FIN_COMPANY_ACCOUNT".equals(pageCode)) {
			if ("xls".equals(suffix)) {
				HSSFWorkbook workbook = new HSSFWorkbook(files.getInputStream());
				// 读取获取到的第一个sheet
				HSSFSheet sheet = workbook.getSheetAt(0);
				// 用于存放excel内容体
				List<List<String>> bodyList = new ArrayList<List<String>>();
				// 声明一个引用,用于临时存放每一行数据
				List<String> tmpList = null;
				for (int j = 0; j < sheet.getLastRowNum() + 1; j++) {
					// 创建一个行对象
					if (sheet.getRow(j) == null) {
						j++;
					}
					HSSFRow row = sheet.getRow(j);
					for (int i = 0; i < row.getLastCellNum(); i++) {
						HSSFCell cell = row.getCell(i);
						if (cell != null) {
							switch (cell.getCellType()) {
							case HSSFCell.CELL_TYPE_STRING:
								if (cell.getRichStringCellValue().getString()
										.contains("记账日期")) {
									// System.err.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~="+j);
									for (int l = j + 1; l <= sheet
											.getLastRowNum(); l++) {
										
									   // System.err.println(")))))))))))))))))))))))))))))))))="+l);
										if (sheet.getRow(l) == null) {
											l++;
										}
										row = sheet.getRow(l);
										for (int k = 0; k < row
												.getLastCellNum(); k++) {
											cell = row.getCell(k);
											if (cell != null) {
												switch (cell.getCellType()) {
												case HSSFCell.CELL_TYPE_STRING:
													// if(k==0){
													// map.put("bookkeep_time".toUpperCase(),jq1(cell.getRichStringCellValue().getString()));
													// //'发票代码'
													// }
													if (k == 1) {
														map.put("account_num"
																.toUpperCase(),
																jq1(cell.getRichStringCellValue()
																		.getString()));// '发票号码'
													} else if (k == 2) {
														map.put("hou_name"
																.toUpperCase(),
																jq1(cell.getRichStringCellValue()
																		.getString()));// '购方企业名称'
													} else if (k == 3) {
														map.put("exp_price"
																.toUpperCase(),
																jq1(cell.getRichStringCellValue()
																		.getString()));// '购方税号'
													} else if (k == 4) {
														map.put("income_price"
																.toUpperCase(),
																jq1(cell.getRichStringCellValue()
																		.getString()));// '银行账号'
													} else if (k == 5) {
														map.put("other_account_num"
																.toUpperCase(),
																jq1(cell.getRichStringCellValue()
																		.getString()));// '地址电话'
													} else if (k == 6) {
														map.put("other_hus_name"
																.toUpperCase(),
																jq1(cell.getRichStringCellValue()
																		.getString()));// '开票日期'
													} else if (k == 7) {
														map.put("other_bank"
																.toUpperCase(),
																jq1(cell.getRichStringCellValue()
																		.getString()));// '商品编码版本'
													} else if (k == 8) {
														map.put("abstract"
																.toUpperCase(),
																jq1(cell.getRichStringCellValue()
																		.getString()));// '单据号'
													} else if (k == 9) {
														map.put("remarks"
																.toUpperCase(),
																jq1(cell.getRichStringCellValue()
																		.getString()));// '商品名称'
													} else if (k == 10) {
														map.put("voucher_type"
																.toUpperCase(),
																jq1(cell.getRichStringCellValue()
																		.getString()));// '规格'
													} else if (k == 11) {
														map.put("voucher_number"
																.toUpperCase(),
																jq1(jq1(cell
																		.getRichStringCellValue()
																		.getString())));// '单位'
													} else if (k == 12) {
														map.put("bookkeep_num"
																.toUpperCase(),
																jq1(jq1(cell
																		.getRichStringCellValue()
																		.getString())));// '数量'
													}
													// else if(k==13){
													// map.put("bookkeep_now_time".toUpperCase(),jq1(jq1(cell.getRichStringCellValue().getString())));//'单价'
													// }
													else if (k == 14) {
														map.put("recon_marking"
																.toUpperCase(),
																jq1(cell.getRichStringCellValue()
																		.getString()));// '金额'
													} else if (k == 15) {
														map.put("launch_channel"
																.toUpperCase(),
																jq1(cell.getRichStringCellValue()
																		.getString()));// '金额'
													} else if (k == 16) {
														map.put("channel_id"
																.toUpperCase(),
																jq1(cell.getRichStringCellValue()
																		.getString()));// '金额'
													} else if (k == 17) {
														map.put("run"
																.toUpperCase(),
																jq1(cell.getRichStringCellValue()
																		.getString()));// '金额'
													} else if (k == 18) {
														map.put("to_review"
																.toUpperCase(),
																jq1(cell.getRichStringCellValue()
																		.getString()));// '金额'
													} else if (k == 19) {
														map.put("exa_app"
																.toUpperCase(),
																jq1(cell.getRichStringCellValue()
																		.getString()));// '金额'
													}
													break;
												case HSSFCell.CELL_TYPE_NUMERIC:
													// if(k==0){
													// map.put("bookkeep_time".toUpperCase(),jq1(jq1(String.valueOf(cell.getNumericCellValue()))));
													// //'发票代码'
													// }
													if (k == 1) {
														map.put("account_num"
																.toUpperCase(),
																jq1(jq1(String
																		.valueOf(cell
																				.getNumericCellValue()))));// '发票号码'
													} else if (k == 2) {
														map.put("hou_name"
																.toUpperCase(),
																jq1(jq1(String
																		.valueOf(cell
																				.getNumericCellValue()))));// '购方企业名称'
													} else if (k == 3) {
														map.put("exp_price"
																.toUpperCase(),
																jq1(jq1(String
																		.valueOf(cell
																				.getNumericCellValue()))));// '购方税号'
													} else if (k == 4) {
														map.put("income_price"
																.toUpperCase(),
																jq1(jq1(String
																		.valueOf(cell
																				.getNumericCellValue()))));// '银行账号'
													} else if (k == 5) {
														map.put("other_account_num"
																.toUpperCase(),
																jq1(jq1(String
																		.valueOf(cell
																				.getNumericCellValue()))));// '地址电话'
													} else if (k == 6) {
														map.put("other_hus_name"
																.toUpperCase(),
																jq1(jq1(String
																		.valueOf(cell
																				.getNumericCellValue()))));// '开票日期'
													} else if (k == 7) {
														map.put("other_bank"
																.toUpperCase(),
																jq1(jq1(String
																		.valueOf(cell
																				.getNumericCellValue()))));// '商品编码版本'
													} else if (k == 8) {
														map.put("abstract"
																.toUpperCase(),
																jq1(jq1(String
																		.valueOf(cell
																				.getNumericCellValue()))));// '单据号'
													} else if (k == 9) {
														map.put("remarks"
																.toUpperCase(),
																jq1(jq1(String
																		.valueOf(cell
																				.getNumericCellValue()))));// '商品名称'
													} else if (k == 10) {
														map.put("voucher_type"
																.toUpperCase(),
																jq1(jq1(String
																		.valueOf(cell
																				.getNumericCellValue()))));// '规格'
													} else if (k == 11) {
														map.put("voucher_number"
																.toUpperCase(),
																jq1(jq1(jq1(String
																		.valueOf(cell
																				.getNumericCellValue())))));// '单位'
													} else if (k == 12) {
														map.put("bookkeep_num"
																.toUpperCase(),
																jq1(jq1(jq1(String
																		.valueOf(cell
																				.getNumericCellValue())))));// '数量'
													}
													// else if(k==13){
													// map.put("bookkeep_now_time".toUpperCase(),jq1(jq1(jq1(String.valueOf(cell.getNumericCellValue())))));//'单价'
													// }
													else if (k == 14) {
														map.put("recon_marking"
																.toUpperCase(),
																jq1(jq1(String
																		.valueOf(cell
																				.getNumericCellValue()))));// '金额'
													} else if (k == 15) {
														map.put("launch_channel"
																.toUpperCase(),
																jq1(jq1(String
																		.valueOf(cell
																				.getNumericCellValue()))));// '金额'
													} else if (k == 16) {
														map.put("channel_id"
																.toUpperCase(),
																jq1(jq1(String
																		.valueOf(cell
																				.getNumericCellValue()))));// '金额'
													} else if (k == 17) {
														map.put("run"
																.toUpperCase(),
																jq1(jq1(String
																		.valueOf(cell
																				.getNumericCellValue()))));// '金额'
													} else if (k == 18) {
														map.put("to_review"
																.toUpperCase(),
																jq1(jq1(String
																		.valueOf(cell
																				.getNumericCellValue()))));// '金额'
													} else if (k == 19) {
														map.put("exa_app"
																.toUpperCase(),
																jq1(jq1(String
																		.valueOf(cell
																				.getNumericCellValue()))));// '金额'
													}
													break;
												default:
													break;
												}// 类型判断
											}// 非空
										}// 行内容
//										BaseDao.getBaseDao().insertByTransfrom(
//												pageCode, map);
//										map.clear();
										
										listmap.add(map);
										map=new HashMap<String, String>();
										map.clear();
									}
								}// 外if结束

								break;// 外循环
							default:
								break;
							}
						} else {
							//tmpList.add(null);
						}
					}
				}
				if(listmap.toString()!=null){
					sign = "true";
				}
				BaseDao.getBaseDao().insertByTransfrom(pageCode,listmap);
			}
		} else if ("FIN_BUSINESS_IMPORT".equals(pageCode)) {//事业部制记账表导入
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (suffix.equals("xlsx")) {
				XSSFWorkbook workbook = new XSSFWorkbook(files.getInputStream());
				//获取第一个sheet
				XSSFSheet sheet = workbook.getSheetAt(0);
				//循环行
				for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
					//当前行
					XSSFRow row = sheet.getRow(i);
					HashMap<String, String> rowMap = new HashMap<String,String>();
					//循环列
					for(int j = 0; j < row.getLastCellNum(); j++){
						//当前列
						XSSFCell cell = row.getCell(j);
						String cellValue = "";
						if(cell != null){
							switch (cell.getCellType()) {
							case 0:// 数值型
								if (DateUtil.isCellDateFormatted(cell)) {
									cellValue = sdf.format(cell.getDateCellValue());
								} else {
									cellValue = String.valueOf(cell.getNumericCellValue()).trim();
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
							if (j == 0) {
								rowMap.put("PERIOD_OF", cellValue);// 期间
							} else if (j == 1) {
								rowMap.put("JOURNAL_SOURCE", cellValue);// 日记账来源
							} else if (j == 2) {
								rowMap.put("JOURNAL_CATEGORY", cellValue);// 日记账类别
							} else if (j == 3) {
								rowMap.put("GROUP_NAME", cellValue);// 批名
							} else if (j == 4) {
								rowMap.put("JOURNAL_NAME", cellValue);// 日记账名
							} else if (j == 5) {
								rowMap.put("ACCOUNT_FOR", cellValue);// 账户段
							} else if (j == 6) {
								rowMap.put("PERFORM_OF", cellValue);// 执行
							} else if (j == 7) {
								rowMap.put("THE_BUDGET", cellValue);// 预算
							} else if (j == 8) {
								rowMap.put("ABSTRACT_OF", cellValue);// 摘要
							} else if (j == 9) {
								rowMap.put("REFERENCE_INFORMATION", cellValue);// 参考信息
							} else if (j == 10) {
								rowMap.put("BILL_NO", cellValue);// 报账单号
							} else if (j == 11) {
								rowMap.put("DEPARTMENT_OF", cellValue);// 部门
							} else if (j == 12) {
								rowMap.put("DEPARTMENT_PROCESSING", cellValue);//部门（加工）
							} else if (j == 13) {
								rowMap.put("LEVEL_SUBJECT", cellValue);// 一级科目
							} else if (j == 14) {
								rowMap.put("DETAIL_COURSE", cellValue);// 明细科目
							} else if (j == 15) {
								rowMap.put("AFFILIATED_PARTY", cellValue);// 关联方
							} else if (j == 16) {
								rowMap.put("PROJECT_OF", cellValue);// 项目
							} else if (j == 17) {
								rowMap.put("SUBJECT_LEVEL_1", cellValue);// 预算科目一级
							} else if (j == 18) {
								rowMap.put("SUBJECT_LEVEL_2", cellValue);// 预算科目二级
							} else if (j == 19) {
								rowMap.put("DEPARTMENT_REGULATIONS", cellValue);// 部门（双轨）
							} else if (j == 20) {
								rowMap.put("LINES_BUSINESS", cellValue);// 业务线
							} else if (j == 21) {
								rowMap.put("PRODUCT_OF", cellValue);// 产品
							} else if (j == 22) {
								rowMap.put("DEPARTMENT_REGULATION", cellValue);// 部门(损益双规)
							}
							rowMap.put("IMPORT_PEOPLE", tokenEntity.USER.getName());// 导入人
							rowMap.put("IMPORT_TIME", sdf.format(new Date()));// 导入时间
							if(!tokenEntity.isEmpty()){
								//制单人
								rowMap.put(IPublicBusColumn.CREATOR_ID, tokenEntity.USER.getId());
								rowMap.put(IPublicBusColumn.CREATOR_NAME, tokenEntity.USER.getName());
						        	
								//组织字段
								rowMap.put(IPublicBusColumn.ORGANIZATION_ID, tokenEntity.COMPANY.getCompany_id());
								rowMap.put(IPublicBusColumn.ORGANIZATION_NAME, tokenEntity.COMPANY.getCompany_name());
								
								//部门信息
								rowMap.put(IPublicBusColumn.COMPANY_ID, tokenEntity.COMPANY.getCompany_id());
								rowMap.put(IPublicBusColumn.COMPANY_NAME, tokenEntity.COMPANY.getCompany_name());
								rowMap.put(IPublicBusColumn.BILL_STATUS, "0");
							}
						}
					}
					listmap.add(rowMap);
				}
			} else if (suffix.equals("xls")) {
				HSSFWorkbook workbook = new HSSFWorkbook(files.getInputStream());
				//读取获取到的第一个sheet
				HSSFSheet sheet = workbook.getSheetAt(0);
				//循环行
				for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
					//当前行
					HSSFRow row = sheet.getRow(i);
					HashMap<String, String> rowMap = new HashMap<String,String>();
					//循环列
					for(int j = 0; j < row.getLastCellNum(); j++){
						//当前列
						HSSFCell cell = row.getCell(j);
						String cellValue = "";
						if(cell != null){
							switch (cell.getCellType()) {
							case 0:// 数值型
								if (DateUtil.isCellDateFormatted(cell)) {
									cellValue = sdf.format(cell.getDateCellValue());
								} else {
									cellValue = String.valueOf(cell.getNumericCellValue()).trim();
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
							if (j == 0) {
								rowMap.put("PERIOD_OF", cellValue);// 期间
							} else if (j == 1) {
								rowMap.put("JOURNAL_SOURCE", cellValue);// 日记账来源
							} else if (j == 2) {
								rowMap.put("JOURNAL_CATEGORY", cellValue);// 日记账类别
							} else if (j == 3) {
								rowMap.put("GROUP_NAME", cellValue);// 批名
							} else if (j == 4) {
								rowMap.put("JOURNAL_NAME", cellValue);// 日记账名
							} else if (j == 5) {
								rowMap.put("ACCOUNT_FOR", cellValue);// 账户段
							} else if (j == 6) {
								rowMap.put("PERFORM_OF", cellValue);// 执行
							} else if (j == 7) {
								rowMap.put("THE_BUDGET", cellValue);// 预算
							} else if (j == 8) {
								rowMap.put("ABSTRACT_OF", cellValue);// 摘要
							} else if (j == 9) {
								rowMap.put("REFERENCE_INFORMATION", cellValue);// 参考信息
							} else if (j == 10) {
								rowMap.put("BILL_NO", cellValue);// 报账单号
							} else if (j == 11) {
								rowMap.put("DEPARTMENT_OF", cellValue);// 部门
							} else if (j == 12) {
								rowMap.put("DEPARTMENT_PROCESSING", cellValue);//部门（加工）
							} else if (j == 13) {
								rowMap.put("LEVEL_SUBJECT", cellValue);// 一级科目
							} else if (j == 14) {
								rowMap.put("DETAIL_COURSE", cellValue);// 明细科目
							} else if (j == 15) {
								rowMap.put("AFFILIATED_PARTY", cellValue);// 关联方
							} else if (j == 16) {
								rowMap.put("PROJECT_OF", cellValue);// 项目
							} else if (j == 17) {
								rowMap.put("SUBJECT_LEVEL_1", cellValue);// 预算科目一级
							} else if (j == 18) {
								rowMap.put("SUBJECT_LEVEL_2", cellValue);// 预算科目二级
							} else if (j == 19) {
								rowMap.put("DEPARTMENT_REGULATIONS", cellValue);// 部门（双轨）
							} else if (j == 20) {
								rowMap.put("LINES_BUSINESS", cellValue);// 业务线
							} else if (j == 21) {
								rowMap.put("PRODUCT_OF", cellValue);// 产品
							} else if (j == 22) {
								rowMap.put("DEPARTMENT_REGULATION", cellValue);// 部门(损益双规)
							}
							rowMap.put("IMPORT_PEOPLE",tokenEntity.USER.getName());// 导入人
							rowMap.put("IMPORT_TIME", sdf.format(new Date()));// 导入时间
							if(!tokenEntity.isEmpty()){
								//制单人
								rowMap.put(IPublicBusColumn.CREATOR_ID, tokenEntity.USER.getId());
								rowMap.put(IPublicBusColumn.CREATOR_NAME, tokenEntity.USER.getName());
						        	
								//组织字段
								rowMap.put(IPublicBusColumn.ORGANIZATION_ID, tokenEntity.COMPANY.getCompany_id());
								rowMap.put(IPublicBusColumn.ORGANIZATION_NAME, tokenEntity.COMPANY.getCompany_name());
								
								//部门信息
								rowMap.put(IPublicBusColumn.COMPANY_ID, tokenEntity.COMPANY.getCompany_id());
								rowMap.put(IPublicBusColumn.COMPANY_NAME, tokenEntity.COMPANY.getCompany_name());
								rowMap.put(IPublicBusColumn.BILL_STATUS, "0");
							}
						}
					}
					listmap.add(rowMap);
				}
			}
			if(listmap.size() > 0){
				sign = "true";
				BaseDao.getBaseDao().insertByTransfrom(pageCode, listmap);
			}
		} else if("BUS_CONTRACT_ADMIN".equals(pageCode)){ //商机管理合同导入
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String depId = "";//部门主键
			String undertakeId = "";//承办人主键
			if (suffix.equals("xlsx")) {
				XSSFWorkbook workbook = new XSSFWorkbook(files.getInputStream());
				XSSFSheet sheet = workbook.getSheetAt(0);
				//循环行
				for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
					XSSFRow row = sheet.getRow(i);
					HashMap<String, String> rowMap = new HashMap<String,String>();
					//循环列
					for(int j = 1; j < row.getLastCellNum(); j++){
						XSSFCell cell = row.getCell(j);
						String cellValue = "";
						if(cell != null){
							switch (cell.getCellType()) {
							case 0:// 数值型
								if (DateUtil.isCellDateFormatted(cell)) {
									cellValue = sdf.format(cell.getDateCellValue());
								} else {
									cellValue = String.valueOf(cell.getNumericCellValue()).trim();
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
							if (j == 1) {
								rowMap.put("CON_NAME", cellValue);//合同名称
							} else if (j == 2) {
								rowMap.put("CON_ID", cellValue);//合同编号
							} else if (j == 3) {
								rowMap.put("CON_FLOWING_ID", cellValue);//合同流水号
							} else if (j == 4) {
								rowMap.put("UNDERTAKE_DEP", cellValue);//承办部门
							} else if (j == 5) {
								rowMap.put("UNDERTAKE_NAME", cellValue);//承办人
							} else if (j == 6) {
								rowMap.put("VALUE_ADDED_TAX_PRICE", cellValue);//含增值税合同金额
								rowMap.put("BALANCE", cellValue);//余额
							} else if (j == 7) {
								rowMap.put("UNDERTAKE_RALE", replaceByTransverse(cellValue));//增值税率
							} else if (j == 8) {
								rowMap.put("NO_UNDERTAKE_PRICE", replaceByTransverse(cellValue));//不含增值税合同金额
							} else if (j == 9) {
								rowMap.put("PARTY_MAIN", replaceByComma(cellValue));//我方主体
							} else if (j == 10) {
								rowMap.put("OTHER_PARTY_MAIN", replaceByComma(cellValue));//对方主体
							} else if (j == 11) {
								rowMap.put("CONTRACT_TYPE", getDictionariesKey("BUS_CONTRACT_TYPE",cellValue));//合同类型
							} else if (j == 12) {
								rowMap.put("IMPORTANCE", getDictionariesKey("BUS_IMPORTANCE",cellValue));//重要程度
							} else if (j == 13) {
								rowMap.put("EMER_DEGREE", getDictionariesKey("BUS_EMERGENCY_DEGREE",cellValue));//紧急程度
							} else if (j == 14) {
								rowMap.put("FINALLY_NAME", cellValue);//最终审批人
							} else if (j == 15) {
								rowMap.put("CHECK_SIGN_TYPE", replaceByTransverse(cellValue));//审签流程类型
							} else if (j == 16) {
								rowMap.put("EX_APP_TIME", cellValue);//审批通过日期
								if(cellValue.equals("-")){
									rowMap.put("EX_APP_TIME","");
								}
							} else if (j == 17) {
								rowMap.put("SIGN_TIME", cellValue);//签订盖章日期
								if(cellValue.equals("-")){
									rowMap.put("SIGN_TIME","");
								}
							} else if (j == 18) {
								rowMap.put("CONTRACT_PERFORM_TIME", cellValue);//合同履行期限
							} else if (j == 19) {
								rowMap.put("CONTRACT_STATUS", getDictionariesKey("BUS_CONTRACT_STATE",cellValue));//合同状态
							} else if (j == 20) {
								rowMap.put("PERFORM_DEP", cellValue);//履行部门
							} else if (j == 21) {
								rowMap.put("PERFORM_NAME", cellValue);//履行人
							} else if (j == 22) {
								rowMap.put("RECE_COST", cellValue);//应收/付金额
							} else if (j == 23) {
								rowMap.put("NET_RECE_COST", cellValue);//实收/付金额
							} else if (j == 24) {
								rowMap.put("CONTRACT_SUB_TIME", cellValue);//合同送审日期
								if(cellValue.equals("-")){
									rowMap.put("CONTRACT_SUB_TIME","");
								}
							} else if (j == 25) {
								rowMap.put("PRO_ID", replaceByTransverse(cellValue));//项目编号
							} else if (j == 26) {
								rowMap.put("PRO_NAME", replaceByTransverse(cellValue));//项目名称
							} else if (j == 27) {
								rowMap.put("CONTRACT_NATURE", getDictionariesKey("BUS_HTXX",cellValue));//合同性质
							} else if (j == 28) {
								rowMap.put("SIGN_SURE_TIME", cellValue);//签订盖章信息确认时间
								if(cellValue.equals("-")){
									rowMap.put("SIGN_SURE_TIME","");
								}
							} else if (j == 29) {
								rowMap.put("HIS_CONTRACT", cellValue);//历史合同
							} else if (j == 30) {
								rowMap.put("SIGN_MODE", replaceByTransverse(cellValue));//签约方式
							} else if (j == 31) {
								rowMap.put("PAYMENT_MODE", replaceByTransverse(cellValue));//付款方式
							} else if (j == 32) {
								rowMap.put("DEP_NAME", cellValue);//单位名称
							} else if (j == 33) {
								rowMap.put("QUOTE_MODEL_NAME", replaceByTransverse(cellValue));//引用模板名称
							} else if (j == 34) {
								rowMap.put("USE_CONTRACT_STANDARD", cellValue);//合同标准使用情况
							} else if (j == 35) {
								rowMap.put("SIGN_GRANT", cellValue);//建议授权签约
							} else if (j == 36) {
								rowMap.put("MEE_SIGN_NAME", replaceByTransverse(cellValue));//会签人
							} else if (j == 37) {
								rowMap.put("ORI_SCRIPT", cellValue);//原件份数
								rowMap.put("CON_SIGN_NUMBER",cellValue);//合同签订份数
							} else if (j == 38) {
								rowMap.put("DELETE_REASON", replaceByTransverse(cellValue));//删除原因
							}
						}
					}
					if(rowMap.get("SIGN_SURE_TIME") != null && !rowMap.get("SIGN_SURE_TIME").equals("")){
						rowMap.put("CON_SIGN_STATUS","2");//合同签订状态
						rowMap.put("CON_SEAL_STATUS","0");//合同盖章状态
					}else{
						rowMap.put("CON_SIGN_STATUS","1");//合同签订状态
						rowMap.put("CON_SEAL_STATUS","1");//合同盖章状态
					}
					if(rowMap.get("UNDERTAKE_DEP") != null && !rowMap.get("UNDERTAKE_DEP").equals("")){
						String sql = "SELECT * FROM TM_COMPANY WHERE NAME = '"+map.get("UNDERTAKE_DEP")+"' ";
						List<Map<String,Object>> list = BaseDao.getBaseDao().query(sql, "");
						if (list.size()>0) {
							depId = list.get(0).get("ID").toString();
						}
					}
					if(rowMap.get("UNDERTAKE_NAME") != null && !rowMap.get("UNDERTAKE_NAME").equals("")){
						StringBuffer sb = new StringBuffer();
						sb.append("SELECT * FROM RM_USER WHERE NAME = '"+rowMap.get("UNDERTAKE_NAME")+"'");
						if(!depId.equals("")){
							sb.append(" AND ORGANIZATION_ID = '"+depId+"'");
						}
						List<Map<String,Object>> list = BaseDao.getBaseDao().query(sb.toString(), "");
						if(list.size()>0){
							undertakeId = list.get(0).get("ID").toString();
						}
					}
					rowMap.put("UNDERTAKE_ID", undertakeId);//承办人主键
					rowMap.put("ENTRY_MODE", "导入");//录入方式
					rowMap.put("IMPORT_TIME", sdf.format(new Date()));// 导入时间
					if(!tokenEntity.isEmpty()){
						//制单人
						rowMap.put(IPublicBusColumn.CREATOR_ID, tokenEntity.USER.getId());
						rowMap.put(IPublicBusColumn.CREATOR_NAME, tokenEntity.USER.getName());
				        	
						//组织字段
						rowMap.put(IPublicBusColumn.ORGANIZATION_ID, tokenEntity.COMPANY.getCompany_id());
						rowMap.put(IPublicBusColumn.ORGANIZATION_NAME, tokenEntity.COMPANY.getCompany_name());
						
						//部门信息
						rowMap.put(IPublicBusColumn.COMPANY_ID, tokenEntity.COMPANY.getCompany_id());
						rowMap.put(IPublicBusColumn.COMPANY_NAME, tokenEntity.COMPANY.getCompany_name());
						rowMap.put(IPublicBusColumn.BILL_STATUS, "0");
					}
					listmap.add(rowMap);
				}
			} else if (suffix.equals("xls")) {
				HSSFWorkbook workbook = new HSSFWorkbook(files.getInputStream());
				HSSFSheet sheet = workbook.getSheetAt(0);
				//循环行
				for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
					HSSFRow row = sheet.getRow(i);
					HashMap<String, String> rowMap = new HashMap<String,String>();
					//循环列
					for(int j = 1; j < row.getLastCellNum(); j++){
						HSSFCell cell = row.getCell(j);
						String cellValue = "";
						if(cell != null){
							switch (cell.getCellType()) {
							case 0:// 数值型
								if (DateUtil.isCellDateFormatted(cell)) {
									cellValue = sdf.format(cell.getDateCellValue());
								} else {
									cellValue = String.valueOf(cell.getNumericCellValue()).trim();
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
							if (j == 1) {
								rowMap.put("CON_NAME", cellValue);//合同名称
							} else if (j == 2) {
								rowMap.put("CON_ID", cellValue);//合同编号
							} else if (j == 3) {
								rowMap.put("CON_FLOWING_ID", cellValue);//合同流水号
							} else if (j == 4) {
								rowMap.put("UNDERTAKE_DEP", cellValue);//承办部门
							} else if (j == 5) {
								rowMap.put("UNDERTAKE_NAME", cellValue);//承办人
							} else if (j == 6) {
								rowMap.put("VALUE_ADDED_TAX_PRICE", cellValue);//含增值税合同金额
								rowMap.put("BALANCE", cellValue);//余额
							} else if (j == 7) {
								rowMap.put("UNDERTAKE_RALE", replaceByTransverse(cellValue));//增值税率
							} else if (j == 8) {
								rowMap.put("NO_UNDERTAKE_PRICE", replaceByTransverse(cellValue));//不含增值税合同金额
							} else if (j == 9) {
								rowMap.put("PARTY_MAIN", replaceByComma(cellValue));//我方主体
							} else if (j == 10) {
								rowMap.put("OTHER_PARTY_MAIN", replaceByComma(cellValue));//对方主体
							} else if (j == 11) {
								rowMap.put("CONTRACT_TYPE", getDictionariesKey("BUS_CONTRACT_TYPE",cellValue));//合同类型
							} else if (j == 12) {
								rowMap.put("IMPORTANCE", getDictionariesKey("BUS_IMPORTANCE",cellValue));//重要程度
							} else if (j == 13) {
								rowMap.put("EMER_DEGREE", getDictionariesKey("BUS_EMERGENCY_DEGREE",cellValue));//紧急程度
							} else if (j == 14) {
								rowMap.put("FINALLY_NAME", cellValue);//最终审批人
							} else if (j == 15) {
								rowMap.put("CHECK_SIGN_TYPE", replaceByTransverse(cellValue));//审签流程类型
							} else if (j == 16) {
								rowMap.put("EX_APP_TIME", cellValue);//审批通过日期
								if(cellValue.equals("-")){
									rowMap.put("EX_APP_TIME","");
								}
							} else if (j == 17) {
								rowMap.put("SIGN_TIME", cellValue);//签订盖章日期
								if(cellValue.equals("-")){
									rowMap.put("SIGN_TIME","");
								}
							} else if (j == 18) {
								rowMap.put("CONTRACT_PERFORM_TIME", cellValue);//合同履行期限
							} else if (j == 19) {
								rowMap.put("CONTRACT_STATUS", getDictionariesKey("BUS_CONTRACT_STATE",cellValue));//合同状态
							} else if (j == 20) {
								rowMap.put("PERFORM_DEP", cellValue);//履行部门
							} else if (j == 21) {
								rowMap.put("PERFORM_NAME", cellValue);//履行人
							} else if (j == 22) {
								rowMap.put("RECE_COST", cellValue);//应收/付金额
							} else if (j == 23) {
								rowMap.put("NET_RECE_COST", cellValue);//实收/付金额
							} else if (j == 24) {
								rowMap.put("CONTRACT_SUB_TIME", cellValue);//合同送审日期
								if(cellValue.equals("-")){
									rowMap.put("CONTRACT_SUB_TIME","");
								}
							} else if (j == 25) {
								rowMap.put("PRO_ID", replaceByTransverse(cellValue));//项目编号
							} else if (j == 26) {
								rowMap.put("PRO_NAME", replaceByTransverse(cellValue));//项目名称
							} else if (j == 27) {
								rowMap.put("CONTRACT_NATURE", getDictionariesKey("BUS_HTXX",cellValue));//合同性质
							} else if (j == 28) {
								rowMap.put("SIGN_SURE_TIME", cellValue);//签订盖章信息确认时间
								if(cellValue.equals("-")){
									rowMap.put("SIGN_SURE_TIME","");
								}
							} else if (j == 29) {
								rowMap.put("HIS_CONTRACT", cellValue);//历史合同
							} else if (j == 30) {
								rowMap.put("SIGN_MODE", replaceByTransverse(cellValue));//签约方式
							} else if (j == 31) {
								rowMap.put("PAYMENT_MODE", replaceByTransverse(cellValue));//付款方式
							} else if (j == 32) {
								rowMap.put("DEP_NAME", cellValue);//单位名称
							} else if (j == 33) {
								rowMap.put("QUOTE_MODEL_NAME", replaceByTransverse(cellValue));//引用模板名称
							} else if (j == 34) {
								rowMap.put("USE_CONTRACT_STANDARD", cellValue);//合同标准使用情况
							} else if (j == 35) {
								rowMap.put("SIGN_GRANT", cellValue);//建议授权签约
							} else if (j == 36) {
								rowMap.put("MEE_SIGN_NAME", replaceByTransverse(cellValue));//会签人
							} else if (j == 37) {
								rowMap.put("ORI_SCRIPT", cellValue);//原件份数
								rowMap.put("CON_SIGN_NUMBER",cellValue);//合同签订份数
							} else if (j == 38) {
								rowMap.put("DELETE_REASON", replaceByTransverse(cellValue));//删除原因
							}
						}
					}
					if(rowMap.get("SIGN_SURE_TIME") != null && !rowMap.get("SIGN_SURE_TIME").equals("")){
						rowMap.put("CON_SIGN_STATUS","2");//合同签订状态
						rowMap.put("CON_SEAL_STATUS","0");//合同盖章状态
					}else{
						rowMap.put("CON_SIGN_STATUS","1");//合同签订状态
						rowMap.put("CON_SEAL_STATUS","1");//合同盖章状态
					}
					if(rowMap.get("UNDERTAKE_DEP") != null && !rowMap.get("UNDERTAKE_DEP").equals("")){
						String sql = "SELECT * FROM TM_COMPANY WHERE NAME = '"+map.get("UNDERTAKE_DEP")+"' ";
						List<Map<String,Object>> list = BaseDao.getBaseDao().query(sql, "");
						if (list.size()>0) {
							depId = list.get(0).get("ID").toString();
						}
					}
					if(rowMap.get("UNDERTAKE_NAME") != null && !rowMap.get("UNDERTAKE_NAME").equals("")){
						StringBuffer sb = new StringBuffer();
						sb.append("SELECT * FROM RM_USER WHERE NAME = '"+rowMap.get("UNDERTAKE_NAME")+"'");
						if(!depId.equals("")){
							sb.append(" AND ORGANIZATION_ID = '"+depId+"'");
						}
						List<Map<String,Object>> list = BaseDao.getBaseDao().query(sb.toString(), "");
						if(list.size()>0){
							undertakeId = list.get(0).get("ID").toString();
						}
					}
					rowMap.put("UNDERTAKE_ID", undertakeId);//承办人主键
					rowMap.put("ENTRY_MODE", "导入");//录入方式
					rowMap.put("IMPORT_TIME", sdf.format(new Date()));// 导入时间
					if(!tokenEntity.isEmpty()){
						//制单人
						rowMap.put(IPublicBusColumn.CREATOR_ID, tokenEntity.USER.getId());
						rowMap.put(IPublicBusColumn.CREATOR_NAME, tokenEntity.USER.getName());
				        	
						//组织字段
						rowMap.put(IPublicBusColumn.ORGANIZATION_ID, tokenEntity.COMPANY.getCompany_id());
						rowMap.put(IPublicBusColumn.ORGANIZATION_NAME, tokenEntity.COMPANY.getCompany_name());
						
						//部门信息
						rowMap.put(IPublicBusColumn.COMPANY_ID, tokenEntity.COMPANY.getCompany_id());
						rowMap.put(IPublicBusColumn.COMPANY_NAME, tokenEntity.COMPANY.getCompany_name());
						rowMap.put(IPublicBusColumn.BILL_STATUS, "0");
					}
					listmap.add(rowMap);
				}
			}
			if(listmap.size() > 0){
				sign = "true";
				BaseDao.getBaseDao().insertByTransfrom(pageCode, listmap);
			}
		}
		return sign;
	}

	//字典翻译
	public static String getDictionariesKey(String dictionaries,String text){
		String keyString = "";
		ConcurrentHashMap<String, String> dictReference = null;
		try {
			dictReference = RmDictReferenceUtil.getDictReference(dictionaries);
		} catch (BussnissException e) {
			e.printStackTrace();
		}
		if(dictReference.size()>0){
			for(String key:dictReference.keySet()){
				if(text.contains(dictReference.get(key))){
					keyString = key;
					break;
				}
			}
		}
		return keyString;
	}
	
	public static String[] jq(String str) {
		String a = str;
		String b = StringUtils.substringBefore(a, " ");

		String c = StringUtils.substringBefore(a, "税");
		String d = StringUtils.substringAfter(c, " ");
		String d_1 = d.replaceAll(" ", "");
		String d_2 = d_1.replace("元", "");

		String e = StringUtils.substringAfterLast(a, " ");
		String e_1 = e.replace("元", "");

		String aa[] = { b.substring(3), d_2.substring(3), e_1.substring(3) };
		return aa;
	}

	public static String jq1(String str) {
		if (str.contains("0") || str.contains("1") || str.contains("2")
				|| str.contains("3") || str.contains("4") || str.contains("5")
				|| str.contains("6") || str.contains("7") || str.contains("8")
				|| str.contains("9")) {
			String string = str.replace(",", "");
			String string2 = string.replaceAll("	", "");
			str = string2;
		} else {
			String string = str.replaceAll("	", "");
			str = string;
		}
		return str;
	}
	
	/**
	 * 字符串逗号替换
	 * @param str
	 * @return
	 */
	public static String replaceByComma(String str){
		return str.replace(",", "");
	}
	
	/**
	 * 字符串横线替换
	 * @param str
	 * @return
	 */
	public static String replaceByTransverse(String str) {
		return str.replaceAll("-", "");
	}
}
