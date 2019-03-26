package com.yonyou.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.*;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.web.entity.ResponseDataEntity;
import com.yonyou.web.Import_Excel_SuperService;

/**
 * Excel导入——超级版 支持自定义复杂业务 支持.xls&.xlsx 格式 可兼容日后可配置式
 * 
 * @author yansu
 * @add 新增poi jar
 *         https://mvnrepository.com/artifact/org.apache.xmlbeans/xmlbeans/3.0.1
 *         http://central.maven.org/maven2/org/apache/poi/
 */
@Component
public class Import_Excel_SuperTools {
	
	@Autowired
	protected IBaseDao dcmsDAO;
	
	 private static Logger logger = Logger.getLogger(Import_Excel_SuperTools.class);
	 
	 @Autowired
	 public Import_Excel_SuperService import_excel_superservice;
	
	/**
	 * 订单采购表头验证字段
	 */
	public final static String[] EC_PURORDER_H_MAIN_HEAD={"采购订单ID","采购订单日期","采购类型","订单申请类型","订单申请名称","紧急程度","项目编号","项目名称","采购预算（万元）","需求部门","填写人","收货地点","合同编号","合同名称","价税合计（万元）","物资编码","物资名称","规格型号","计量单位","物资分类","物资类型","供应商名称","数量","不含税单价（元）","含税单价（元）","税率","不含税金额","税额","价税合计","备注"};
	
	/**
	 * @A EC_PURORDER_H_MAIN表导入字段
	 * @B
	 * BILLNO采购订单号0 PURCHASE_ORDER_DATE采购订单日期1 PROCUREMENT_TYPE采购类型2
	 * ORDER_APPLICATION订单申请类型3 ORDER_APPLICATION_NAME订单申请名称4
	 * DEGREE_OF_EMERGENCY紧急程度5 ITEM_NO项目编号6 PROJECT_NAME项目名称7
	 * PROCUREMENT_BUDGET采购预算8 DEPARTMENT_DEMAND需求部门9 FILL_PERSON填写人10
	 * PLACE_RECEIPT收货地点11 CONTRACT_NO合同编号12 NAME_CONTRACT合同名称13
	 * REVIEW_STATUS审核状态14
	 */
	public final static String[] EC_PURORDER_H_MAIN_BODY={"BILLNO","PURCHASE_ORDER_DATE","PROCUREMENT_TYPE","ORDER_APPLICATION","ORDER_APPLICATION_NAME","DEGREE_OF_EMERGENCY","ITEM_NO","PROJECT_NAME","PROCUREMENT_BUDGET","DEPARTMENT_DEMAND","FILL_PERSON","PLACE_RECEIPT","CONTRACT_NO","NAME_CONTRACT","LEVIED_TOTAL","MATERIAL_CODE","NAME_GOODS","SPECIFICATIONS_OF","MEASURING_UNIT","MATERIAL_CLASSIFICATION","MATERIAL_TYPE","SUPPLIER_NAME","NUMBER_OF","TAX_PRICE","UNIT_PRICE","RATE_OF","TAX_FREE","TAX_OF","LEVIED_TOTAL","MEMO"};
	
	/**
	 * 
	 */
	public final static String[] EC_PURORDER_H_FIELD={"MATERIAL_CLASSIFICATION","MATERIAL_TYPE","SUPPLIER_NAME","NUMBER_OF","TAX_PRICE","UNIT_PRICE","RATE_OF","PRICE_DISCOUNT","TAX_FREE","TAX_OF","MATERIAL_CODE","NAME_GOODS","SPECIFICATIONS_OF","MEASURING_UNIT","MEMO"};
	
	/**
	 * 测试表头字段
	 */
	final static String[] test={"名称","年龄"};

	/**
	 * 供应商目录表头验证字段
	 */
	public final static String[] BD_SUPPLIER_HEAD={"供应商编码","供应商名称","公司法人","注册资金币种","注册资金","营业执照号码","公司地址","联系人","联系电话","邮箱","银行账户","开户银行","准入方式","经营范围","供应商状态"};
	public final static String[] BD_SUPPLIER_BODY={"CODE","NAME","LEGAL_PERSON","PK_CURRTYPE","REGISTERFUND","BUSLICENSENUM","COMPANY_ADDRESS","CONTACT_PERSON","CONTACT_PHONENUM","EMAIL_EE","BANK_ACCOUNT","BANK_OF","ACCESS_WAY","BUSINESS_SCOPE","SUPSTATE"};

	/**
	 * 物资管理
	 */
	public final static String[] BD_MATERIAL_V_HEAD={"物资编码","物资名称","规格型号","计量单位","供应商名称","物资分类","物资类型","税率","不含税单价","含税单价","价格折扣","备注","创建人","创建时间"};
	public final static String[] BD_MATERIAL_V_BODY={"MATERIAL_CODE","NAME_GOODS","SPECIFICATIONS_OF","PK_MEASDOC","G_NAME","MATERIAL_CLASSIFICATION","MATERIAL_TYPE","RATE_OF","TAX_PRICE","UNIT_PRICE","DISCOUNTFLAG","MEMO","CREATOR","CREATIONTIME"};

	/**
	 * 项目制采购订单
	 */
	public final static String[] EC_PURORDER_H2_HEAD={"需求审批编号","项目编号","项目名称","合同编号","合同名称","采购单据号","采购项目名称","需求部门","项目经理","采购经理","预算金额（万元）","采购内容","物资类别","采购方式","数量","中标金额（不含税）","中标金额（含税）","中标供应商","联系人","电话","邮箱","成本节约金额（万元）","价税合计"};
	public final static String[] EC_PURORDER_H2_BODY={"REQUIREMENT_NUMBER","ITEM_NO","PROJECT_NAME","CONTRACT_NO","NAME_CONTRACT","PURCHASE_NUMBER","PROCUREMENT_NAME","DEPARTMENT_DEMAND","PROJECT_MANAGER","PURCHASING_MANAGERS","BUDGET_AMOUNT","PURCHASING_CONTENT","SUPPLIES_CATEGORY","PURCHASE_WAY","NUMBER_OF","WINNING_AMOUNT","WINNING_TAX_INCLUDED","WINNING_SUPPLIER","THE_CONTACT","THE_PHONE","EMAIL_OF","COST_SAVING_AMOUNT","LEVIED_TOTAL"};
	//测试demo
	public final static String[] TEST_DEMO_HEAD={"用户名","备注","注册日期"};
	public final static String[] TEST_DEMO_BODY={"USER_NAME","REMARK","REGIST_DATE"};
	
	/**
	 * 导入Excel
	 * 
	 * @ideas
	 * @1.先用InputStream获取excel文件的io流
	 * @2.然后穿件一个内存中的excel文件HSSFWorkbook||XSSFWorkbook类型对象，这个对象表示了整个excel文件。
	 * @3.对这个excel文件的每页做循环处理
	 * @4.对每页中每行做循环处理
	 * @5.对每行中的每个单元格做处理
	 * @param files
	 *            文件
	 * @param pageCode
	 *            功能编码
	 * @return JSONObject
	 */
	@SuppressWarnings({ "resource", "deprecation" , "unused" })
	public static ResponseDataEntity ExcelImport(MultipartFile files, String pageCode,Map<String,Object> permissionsMap){
		JSONObject jsonVal=new JSONObject();
		ResponseDataEntity rde=new ResponseDataEntity();
		List<String> rowList = new ArrayList<String>();//缓存行数据list
		List<List<String>> result = new ArrayList<List<String>>();//缓存所有行数据List
		InputStream fis = null;//缓存文件
		Workbook wookbook = null;//缓存Excel数据
		String fileName = files.getOriginalFilename();
		String[] pageCodeHead=pageCodeByHead(pageCode);//初始化表头字段
		boolean TableHeaderValidation=false;//表头验证变量
		int CountRowSum=0;//当前文件数据总行数
		String sup_name = "";//供应商名称
		String mat_code = "";//物资编码
		List<Map<String, Object>> list_map = BaseDao.getBaseDao().query("select NAME from BD_SUPPLIER", "");//供应商名称集合
		List<String> list = new ArrayList<String>();
		for(int i =0;i<list_map.size();i++){
			list.add((String) list_map.get(i).get("NAME"));
		}
		
		try {
			fis = files.getInputStream();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx")) {
			System.out.println("文件不是excel类型");
			rde.setState(ResponseDataEntity.Fail);
			rde.setMessage("文件不是excel类型");
			rde.setBody("");
			return rde;
		}
		/****初始化工作簿Workbook开始****/
		try {
			// 2003版本的excel，用.xls结尾
			wookbook = new HSSFWorkbook(fis);// 得到工作簿
		} catch (Exception ex) {
			// ex.printStackTrace();
			try {
				fis = files.getInputStream();
				// 2007版本的excel，用.xlsx结尾
				wookbook = new XSSFWorkbook(fis);// 得到工作簿
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/****初始化工作簿Workbook完毕****/
//		switch (pageCode) {//switch功能编码
//		case "EC_PURORDER_H":
			//获取wookbook中的Sheet页数
			int sheetSize = wookbook.getNumberOfSheets();
			// 循环每一页，并处理当前循环页
			for (int numSheet = 0; numSheet < sheetSize; numSheet++) {
				Sheet sheet = wookbook.getSheetAt(numSheet);
				// sheet 当前页对象
				if (sheet == null) {
					continue;
				}
				//行数
				int rowSize = sheet.getLastRowNum();
				// 处理当前页，循环读取每一行
				for (int rowNum = 0; rowNum <= rowSize; rowNum++) {
					if (sheet.getRow(rowNum) == null) {
						//rowNum++;
						logger.info("当前循环行号{}" + rowNum + "{}是空行");
						continue;
					}
					logger.info("当前循环行号{}" + rowNum);
					Row row = sheet.getRow(rowNum);//一行对象
					//验证行是否为null行
					if (isRowNull(row)) {
						continue;
					}
					CountRowSum++;
					//列数
					int CellNum = row.getLastCellNum();
					if (rowNum == 0) {//rowNum=0为表头
						int visum=0;
						//验证表头字段
						for (int i = 0; i < CellNum ; i++) {//遍历行的列
							// 创建一个行里的一个字段的对象，也就是获取到的一个单元格中的值
							Cell cell = row.getCell(i);
							if (cell != null && CellNum == pageCodeHead.length) {//单元格内容不为null
								if (pageCodeHead[i].equals(getStringVal(cell))){
									visum=visum+1;
								};
							}else{
								rde.setState(ResponseDataEntity.Fail);
								rde.setMessage("excel表头数据异常");
								rde.setBody("");
								return rde;
							}//单元格内容不为null
						}//遍历行的列
						if (pageCodeHead.length==visum) {
							TableHeaderValidation = true;
						}else{
							rde.setState(ResponseDataEntity.Fail);
							rde.setMessage("excel表头字段顺序异常");
							rde.setBody("");
							return rde;
						}
					} else if(TableHeaderValidation) {//内容操作
						//数据解析
						for (int k = 0; k < CellNum; k++) {//遍历行的列
							// 创建一个行里的一个字段的对象，也就是获取到的一个单元格中的值
							Cell cell = row.getCell(k);
							if (cell != null) {//单元格内容不为null
								cell.setCellType(HSSFCell.CELL_TYPE_STRING);// 强制设置单元格值类型为STRING
								rowList.add(getStringVal(cell));//保存内容
								ResponseDataEntity rdee = null;
								switch (pageCode) {//此处区别功能
								case "BD_SUPPLIER"://供应商目录
									rdee=Import_Excel_SuperService.publicTableOperation(cell, pageCode,"BD_SUPPLIER", k, rowNum, 14, 0 ,permissionsMap);
									if(ResponseDataEntity.Fail.equals(rdee.getState())) {
										return rdee;
									}
									break;
								case "BD_MATERIAL_V"://物资管理
									if(k==0){
										mat_code = getStringVal(cell);
									}
									if(k==4){
										sup_name = getStringVal(cell);
									}
									rdee=Import_Excel_SuperService.BD_MATERIAL_V_Operation(cell, pageCode,k, rowNum,sup_name,list,mat_code);
									if(ResponseDataEntity.Fail.equals(rdee.getState())) {
										return rdee;
									}
									
									break;
								case "EC_PURORDER_H"://订单采购
									/*主子表特殊操作star*/
									//EC_PURORDER_H_Operation(*,*,...)此处可对excel每行每个单元格字段做Hibernate Validator校验com.yonyou.web.entity.EcPurorderHMainTest
									rdee=Import_Excel_SuperService.EC_PURORDER_H_Operation(cell,pageCode, k,rowNum);
									/*主子表特殊操作end*/
									if(ResponseDataEntity.Fail.equals(rdee.getState())) {
										return rdee;
									}
									break;
								case "EC_PURORDER_H2":
									rdee=Import_Excel_SuperService.EC_PURORDER_H2Operation(cell, pageCode,"EC_PURORDER_H", k, rowNum, 22);
									if(ResponseDataEntity.Fail.equals(rdee.getState())) {
										return rdee;
									}
									break;
								default:
									
									break;
								}
							}//单元格内容不为null
						}//遍历行的列
						if (rowList.size()> 1) {//
							result.add(rowList);
							rowList.clear();
						}
					}//内容操作
				}// 处理当前页，循环读取每一行
			}// 循环每一页，并处理当前循环页 
			
//			break;
//
//		default:
//			break;
//		}//switch功能编码
		
		rde.setState(ResponseDataEntity.Succ);
		rde.setMessage("导入正常");
		jsonVal.put("CountRowSum", CountRowSum);//总行数
		jsonVal.put("CountImpSum", CountImpSum);//导入条数 主表为主
		if (CountMainValues.length()>0) {
			jsonVal.put("CountMainValues", CountMainValues.substring(0,CountMainValues.toString().length() - 1));
		}
		jsonVal.put("CountMainValues", "");
		CountMainValues.setLength(0);
		rde.setBody(jsonVal.toString());
		CountImpSum=0;
		logger.info(rde.toString()+"%%%"+result);
		return rde;
	}
	
	
	static Map<String, String> map = new HashMap<String, String>();//缓存单行数据做数据库操作的Map
	static List<Map<String,String>> listmap =new ArrayList<Map<String,String>>();//缓存所有行数据做数据库操作的Map
	static String mainID = null;//ID

	public static int CountImpSum=0;//导入条数 以主表为主
	static StringBuffer CountMainValues=new StringBuffer();//重复数据行号集合
	static StringBuffer CountErrValues=new StringBuffer();//重复数据行号集合
	
	
	/**
	 * @desc 判断此行是否为空行
	 * 	(规则:遍历当前行判断每个单元格，全部为null则此行为null)
	 * @param Row
	 * @return true:是空行；false:不是空行
	 */
	private static boolean isRowNull(Row row) {
		boolean returnV=true;
		if (row!=null) {//一行中 所有单元格都为null 则为空行
			int rowsum=0;
			for (int i = 0; i <= row.getLastCellNum()-1; i++) {
				if (row.getCell(i)==null) {
					rowsum++;
				}
			}
			if (rowsum-1 != row.getLastCellNum()-1) {//rowsum-1是因为rowsum从0开始算的
				logger.info("当前行总列数为{}" + (row.getLastCellNum()) + "，null的列数:" + rowsum);
				returnV=false;
			}
		}
		return returnV;
	}

	/**
	 * 改造poi默认的toString() 用于读取单元格各种类型的值；方法如下
	 * 
	 * @Title: getStringVal
	 * @Description: 1.对于不熟悉的类型，或者为空则返回""控制串
	 *                2.如果是数字，则修改单元格类型为String，然后返回String，这样就保证数字不被格式化了
	 * @param cell
	 * @return
	 */
	@SuppressWarnings("all")
	public static String getStringVal(Cell cell) {
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue() ? "TRUE" : "FALSE";
		case Cell.CELL_TYPE_FORMULA:
			return cell.getCellFormula();
		case Cell.CELL_TYPE_NUMERIC:
			cell.setCellType(Cell.CELL_TYPE_STRING);
			return cell.getStringCellValue();
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue();
		default:
			return "";
		}
	}

	/**
	 * 根据pageCode 返回相应excel表头字段
	 * @param pageCode
	 * @return
	 * @throws Exception 
	 */
	public static String[] pageCodeByHead(String pageCode) {
		switch (pageCode.toUpperCase()) {
		case "BD_SUPPLIER":
			return BD_SUPPLIER_HEAD;
		case "EC_PURORDER_H":
			return EC_PURORDER_H_MAIN_HEAD;
		case "BD_MATERIAL_V":
			return BD_MATERIAL_V_HEAD;
		case "EC_PURORDER_H2":
			return	EC_PURORDER_H2_HEAD;
		default:
			break;
		}

		try {
			throw new Exception(pageCode+"此字段还未配置pageCodeByHead"+"{}位置{}"+ Import_Excel_SuperTools.class.getName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * 根据pageCode 返回数据插入表相应字段
	 * @param pageCode
	 * @return
	 */
	public static String[] pageCodeByBody(String pageCode) {
		switch (pageCode) {
		case "BD_SUPPLIER":
			return BD_SUPPLIER_BODY;
		case "EC_PURORDER_H":
			return EC_PURORDER_H_MAIN_BODY;
		case "BD_MATERIAL_V":
			return BD_MATERIAL_V_BODY;
		case "EC_PURORDER_H2":
			return	EC_PURORDER_H2_BODY;
		default:
			break;
		}

		try {
			throw new Exception(pageCode+"此字段还未配置pageCodeByBody"+"{}位置{}"+ Import_Excel_SuperTools.class.getName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return null;
	}
	
	public static void main(String[] args) {
		// /System.out.println("E:" + File.separator + "atest.xlsx");
	//	getDataFromExcel("E:" + File.separator + "atest.xlsx");
	//	getDataFromExcel("E:" + File.separator + "123.xls");

		String pageCode="EC_PURORDER_H";
		String[] pageCodeByHead=pageCodeByHead(pageCode);
		String[] pageCodeByBody=pageCodeByBody(pageCode);
		System.out.println(pageCodeByHead.length);
		System.out.println(pageCodeByBody.length);
		/*for (int i = 0; i < pageCodeByHead.length; i++) {
			if(i+1==15){
				pageCodeByHead[i]="价税合计（万元）";
				pageCodeByBody[i]="LEVIED_TOTAL";
			}
			if(i+1==16){
				pageCodeByHead[i]="物资编码";
				pageCodeByBody[i]="MATERIAL_CODE";
			}
			if(i+1==17){
				pageCodeByHead[i]="物资名称";
				pageCodeByBody[i]="NAME_GOODS";
			}
			if(i+1==18){
				pageCodeByHead[i]="规格型号";
				pageCodeByBody[i]="SPECIFICATIONS_OF";
			}
			if(i+1==19){
				pageCodeByHead[i]="计量单位";
				pageCodeByBody[i]="MEASURING_UNIT";
			}
			if(i+1==20){
				pageCodeByHead[i]="物资分类";
				pageCodeByBody[i]="MATERIAL_CLASSIFICATION";
			}
			if(i+1==21){
				pageCodeByHead[i]="物资类型";
				pageCodeByBody[i]="MATERIAL_TYPE";
			}
			if(i+1==22){
				pageCodeByHead[i]="供应商名称";
				pageCodeByBody[i]="SUPPLIER_NAME";
			}
			if(i+1==23){
				pageCodeByHead[i]="数量";
				pageCodeByBody[i]="NUMBER_OF";
			}
			if(i+1==24){
				pageCodeByHead[i]="不含税单价（元）";
				pageCodeByBody[i]="TAX_PRICE";
			}
			if(i+1==25){
				pageCodeByHead[i]="含税单价（元）";
				pageCodeByBody[i]="UNIT_PRICE";
			}
			if(i+1==26){
				pageCodeByHead[i]="税率";
				pageCodeByBody[i]="RATE_OF";
			}
			if(i+1==27){
				pageCodeByHead[i]="不含税金额";
				pageCodeByBody[i]="TAX_FREE";
			}
			if(i+1==28){
				pageCodeByHead[i]="税额";
				pageCodeByBody[i]="TAX_OF";
			}
			if(i+1==29){
				pageCodeByHead[i]="价税合计";
				pageCodeByBody[i]="LEVIED_TOTAL";
			}
			
			System.out.println(pageCodeByHead[i]+"******"+i+"******"+pageCodeByBody[i]);
		}
		for (int i = 0; i < pageCodeByHead.length; i++) {
			if(i==0){
				System.out.print("{");
			}
			System.out.print("\"");
			System.out.print(pageCodeByHead[i]);
			System.out.print("\"");
			if(i != pageCodeByHead.length-1)
				System.out.print(",");
			else
				System.out.print("}");
		}
		System.out.println();
		for (int i = 0; i < pageCodeByBody.length; i++) {
			if(i==0){
				System.out.print("{");
			}
			System.out.print("\"");
			System.out.print(pageCodeByBody[i]);
			System.out.print("\"");
			if(i != pageCodeByBody.length-1)
				System.out.print(",");
			else
				System.out.print("}");
		}
		System.out.println();*/
//		CountMainValues.append((2+1)+"、");
//		CountMainValues.append((21+1)+"、");
//		CountMainValues.append((211+1)+"、");
//		System.out.println(CountMainValues.substring(0,CountMainValues.toString().length() - 1));
//		System.out.println(pageCodeByHead(pageCode).length);
//System.out.println(Import_Excel_SuperTools.class.getName());
	}
	


}
