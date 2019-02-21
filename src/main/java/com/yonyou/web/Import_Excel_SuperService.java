package com.yonyou.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yonyou.business.button.util.ButForInsert;
import com.yonyou.util.BussnissException;
import com.yonyou.util.Import_Excel_SuperTools;
import com.yonyou.util.RmIdFactory;
import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.web.entity.ResponseDataEntity;

@Service
public class Import_Excel_SuperService {
	
	private static Logger logger = Logger.getLogger(Import_Excel_SuperService.class);

	public static String fail= "com.yonyou.web.Import_Excel_SuperService";
	
	@Autowired
	protected IBaseDao dcmsDAO;
	
	static Map<String, String> map = new HashMap<String, String>();//缓存单行数据做数据库操作的Map
	static List<Map<String,String>> listmap =new ArrayList<Map<String,String>>();//缓存所有行数据做数据库操作的Map
	static String mainID = null;//ID

	//private static int CountImpSum=0;//导入条数 以主表为主
	static StringBuffer CountMainValues=new StringBuffer();//重复数据行号集合
	static StringBuffer CountErrValues=new StringBuffer();//重复数据行号集合
	
	/**
	 * EC_PURORDER_H pageCode 导入主子表操作
	 * @param cell
	 * @param pageCode
	 * @param k 当前行单元格循坏数BD_SUPPLIER_Operation
	 * @param rowNum 当前循环行数
	 */
	@SuppressWarnings("all")
	public static ResponseDataEntity EC_PURORDER_H_Operation(Cell cell,String pageCode,int k,int rowNum) {
		// TODO Auto-generated method stub
		ResponseDataEntity rde=new ResponseDataEntity();
		String[] pageCodeBody=Import_Excel_SuperTools.pageCodeByBody(pageCode);//初始化表字段
		//logger.info("");
		map.put(pageCodeBody[k].toUpperCase(), Import_Excel_SuperTools.getStringVal(cell));//加入
		if (k<15) {//主表字段EC_PURORDER_H_MAIN
			if (k == 0) {
				// 验证是否有此采购订单号
				String[] requestId = RmIdFactory.requestId("EC_PURORDER_H_MAIN",1);
				if(requestId.length==0){
					CountMainValues.append((rowNum+1)+"、");
					rde.setState(ResponseDataEntity.Fail);
					rde.setMessage("第["+ (rowNum+1) +"]条数据[采购订单号]自动生成失败");
					rde.setBody("");
					return rde;
				}
				map.put("BILLNO".toUpperCase(),requestId[0]);// 项目编号
			}else if (k == 6) {
				// 验证是否有此项目编号
				SqlTableUtil sqlEntity = new SqlTableUtil("proj_released", "").appendSelFiled("ID").appendWhereAnd(
								"proj_code = " + "'" + Import_Excel_SuperTools.getStringVal(cell) + "'");
				List<Map<String, Object>> lDataSource = BaseDao.getBaseDao().query(sqlEntity);
				if (lDataSource.size() != 1) {
					rde.setState(ResponseDataEntity.Fail);
					rde.setMessage("第["+ (rowNum+1) +"]条数据无此[项目编号],请修改！");
					return rde;
				}
				map.put("proj_code_id".toUpperCase(),String.valueOf((long)lDataSource.get(0).get("ID")));// 项目编号
			}else if(k==12){
				// 验证是否有此合同编号
				SqlTableUtil sqlEntity = new SqlTableUtil("bus_contract_admin","").appendSelFiled(
								"ID").appendWhereAnd("con_id = '" + Import_Excel_SuperTools.getStringVal(cell)+"'");
				List<Map<String, Object>> lDataSource = BaseDao.getBaseDao().query(sqlEntity);
				if (lDataSource.size() != 1) {
					rde.setState(ResponseDataEntity.Fail);
					rde.setMessage("第["+ (rowNum+1) +"]条数据无此[合同编号]");
					return rde;
				}
				map.put("bus_contract_admin_id".toUpperCase(),lDataSource.get(0).get("ID").toString());// 合同编号
			}else if(k==14){
				//做主表插入之前判断map内容是否在主表已有 有的话取出记录的iD以便子表使用 没有的话新建ID做插入
				
				//根据excel主表字段【采购订单号】判断是不是已有记录有的话就用医用记录的ID
				SqlTableUtil sqlEntity = new SqlTableUtil("EC_PURORDER_H_MAIN", "").appendSelFiled("ID").appendWhereAnd(
								"BILLNO = " + "'" + map.get("BILLNO") + "'" );
				List<Map<String, Object>> lDataSource = BaseDao.getBaseDao().query(sqlEntity);
				if (lDataSource.size() != 0 ) {
					mainID= Long.toString((long) lDataSource.get(0).get("ID")) ;
					//String sR=String.valueOf(longVal)
				}else {
					String[] mainID_=RmIdFactory.requestId("ec_purorder_h_main",1);
					mainID=mainID_[0];
					try {
						map.put("ID",(mainID));// 'ID'
						listmap.add(map);
						map = new HashMap<String, String>();
						map.clear();
						BaseDao.getBaseDao().insertByTransfrom("EC_PURORDER_H_MAIN",listmap);
						listmap.clear();
						Import_Excel_SuperTools.CountImpSum++;
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						rde.setState(ResponseDataEntity.Fail);
						rde.setMessage("程序异常");
						return rde;
					}
				}
			}
		} else if (k > 14){//子表字段EC_PURORDER_H
			if (k == 29) {
				//子表插入 主表外键用 mainID
				
				SqlTableUtil sqlEntity = new SqlTableUtil("EC_PURORDER_H", "").appendSelFiled("*").appendWhereAnd(
						pageCodeBody[0]+" = " + "'" + map.get(pageCodeBody[0]) + "'" )
						.appendWhereAnd(
						pageCodeBody[1]+" = " + "'" + map.get(pageCodeBody[1]) + "'" )
						.appendWhereAnd(
						pageCodeBody[2]+" = " + "'" + map.get(pageCodeBody[2]) + "'" )
						.appendWhereAnd(
						pageCodeBody[3]+" = " + "'" + map.get(pageCodeBody[3]) + "'" )
						.appendWhereAnd(
								pageCodeBody[4]+" = " + "'" + map.get(pageCodeBody[4]) + "'" )
						.appendWhereAnd(
								pageCodeBody[5]+" = " + "'" + map.get(pageCodeBody[5]) + "'" )
						.appendWhereAnd(
								pageCodeBody[6]+" = " + "'" + map.get(pageCodeBody[6]) + "'" )
						.appendWhereAnd(
								pageCodeBody[7]+" = " + "'" + map.get(pageCodeBody[7]) + "'" )
						.appendWhereAnd(
								pageCodeBody[8]+" = " + "'" + map.get(pageCodeBody[8]) + "'" )
						.appendWhereAnd(
								pageCodeBody[9]+" = " + "'" + map.get(pageCodeBody[9]) + "'" )
						.appendWhereAnd(
								pageCodeBody[10]+" = " + "'" + map.get(pageCodeBody[10]) + "'" )
						.appendWhereAnd(
								pageCodeBody[11]+" = " + "'" + map.get(pageCodeBody[11]) + "'" )
						.appendWhereAnd(
								pageCodeBody[12]+" = " + "'" + map.get(pageCodeBody[12]) + "'" )
						.appendWhereAnd(
								pageCodeBody[13]+" = " + "'" + map.get(pageCodeBody[13]) + "'" )
						.appendWhereAnd(
								pageCodeBody[14]+" = " + "'" + map.get(pageCodeBody[14]) + "'" )
						.appendWhereAnd("EC_PURORDER_H_MAIN_ID="+mainID);
							
				List<Map<String, Object>> lDataSource = BaseDao.getBaseDao().query(sqlEntity);
				if (lDataSource.size() < 1 ) {//不允许重复数据插入
					map.put("ID","");// 'ID'
					map.put("ec_purorder_h_main_id".toUpperCase(),mainID);// '主表外键'
					listmap.add(map);
					map = new HashMap<String, String>();
					map.clear();
					try {
						if (true) {//阻止相同数据插入到字表
							
						}
						BaseDao.getBaseDao().insertByTransfrom("EC_PURORDER_H",listmap);
					} catch (BussnissException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						rde.setState(ResponseDataEntity.Fail);
						rde.setMessage("程序异常");
						return rde;
					}
					listmap.clear();
				}
			}
		}
		rde.setState(ResponseDataEntity.Succ);
		rde.setMessage("操作正常");
		return rde;
	}
	
	/**
	 * 供应商
	 * @param cell
	 * @param pageCode
	 * @param k
	 * @param rowNum
	 * @return
	 */
	public static ResponseDataEntity BD_SUPPLIER_Operation(Cell cell,String pageCode,int k,int rowNum) {
		// TODO Auto-generated method stub
		ResponseDataEntity rde=new ResponseDataEntity();
		String[] pageCodeBody=Import_Excel_SuperTools.pageCodeByBody(pageCode);//初始化表字段
		//logger.info("");
		map.put(pageCodeBody[k].toUpperCase(), Import_Excel_SuperTools.getStringVal(cell));//加入
		if (k == 14){
			// 验证是否有此供应商编码
			SqlTableUtil sqlEntity = new SqlTableUtil(pageCode, "").appendSelFiled("ID").appendWhereAnd(
							"CODE = " + "'" + Import_Excel_SuperTools.getStringVal(cell) + "'");
			List<Map<String, Object>> lDataSource = BaseDao.getBaseDao().query(sqlEntity);
			if (lDataSource.size() != 0) {
				CountMainValues.append((rowNum+1)+"、");
				rde.setState(ResponseDataEntity.Fail);
				rde.setMessage("第["+ (rowNum+1) +"]条数据[供应商编码]跟已有供应商编码重复,是已导入数据");
				rde.setBody("");
				return rde;
			}//
			
			try {
				map.put("ID","");// 'ID's
				listmap.add(map);
				map.clear();
				BaseDao.getBaseDao().insertByTransfrom("BD_SUPPLIER",listmap);
				listmap.clear();
				Import_Excel_SuperTools.CountImpSum++;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				rde.setState(ResponseDataEntity.Fail);
				rde.setMessage("程序异常");
				rde.setBody("");
				return rde;
			}
		}
		rde.setState(ResponseDataEntity.Succ);
		rde.setMessage("操作正常");
		return rde;
	}

	/**
	 * 物资管理
	 * @param cell
	 * @param pageCode
	 * @param k
	 * @param rowNum
	 * @param sup_name 导入表中供应商名称
	 * @param list 供应商名单集合
	 * @return
	 */
	public static ResponseDataEntity BD_MATERIAL_V_Operation(Cell cell,String pageCode,int k,int rowNum,String sup_name,List<String> list,String mat_code) {
		// TODO Auto-generated method stub
		ResponseDataEntity rde=new ResponseDataEntity();
		String[] pageCodeBody=Import_Excel_SuperTools.pageCodeByBody(pageCode);//初始化表字段
		//logger.info("");
		map.put(pageCodeBody[k].toUpperCase(), Import_Excel_SuperTools.getStringVal(cell));//加入
		if (k == 13){
			// 验证是否有此物资编码
			SqlTableUtil sqlEntity = new SqlTableUtil(pageCode, "").appendSelFiled("ID").appendWhereAnd(
							"MATERIAL_CODE = " + "'" + mat_code + "'");
			List<Map<String, Object>> lDataSource = BaseDao.getBaseDao().query(sqlEntity);
			if (lDataSource.size() != 0) {
				CountMainValues.append((rowNum+1)+"、");
				rde.setState(ResponseDataEntity.Fail);
				rde.setMessage("第["+ (rowNum) +"]条数据<font color='red'>["+mat_code+"]</font>跟已有物资编码重复,是已导入数据");
				rde.setBody("");
				return rde;
			}//
			
			try {
				
				if(!list.contains(sup_name)){
					CountMainValues.append((rowNum+1)+"、");
					rde.setState(ResponseDataEntity.Fail);
					rde.setMessage("第["+ (rowNum) +"]条数据<font color='red'>["+ sup_name+"]</font>为无效供应商");
					rde.setBody("");
					return rde;
				}
				
				map.put("ID","");// 'ID's
				listmap.add(map);
				BaseDao.getBaseDao().insertByTransfrom("BD_MATERIAL_V",listmap);
				map.clear();
				listmap.clear();
				Import_Excel_SuperTools.CountImpSum++;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				rde.setState(ResponseDataEntity.Fail);
				rde.setMessage("程序异常");
				rde.setBody("");
				return rde;
			}
		}
		rde.setState(ResponseDataEntity.Succ);
		rde.setMessage("操作正常");
		return rde;
	}
	
	/**
	 *	项目制采购 
	 * @param cell
	 * @param pageCode
	 * @param tPageCode
	 * @param k
	 * @param rowNum
	 * @param last
	 * @return
	 */
	public static ResponseDataEntity EC_PURORDER_H2Operation(Cell cell,String pageCode ,String tPageCode,int k,int rowNum,int last) {
		// TODO Auto-generated method stub
		ResponseDataEntity rde=new ResponseDataEntity();
		String[] pageCodeBody=Import_Excel_SuperTools.pageCodeByBody(pageCode);//初始化表字段
		map.put(pageCodeBody[k].toUpperCase(), Import_Excel_SuperTools.getStringVal(cell));//加入
		if (k == last){
			try {
				map.put("ID","");// 'ID's
				listmap.add(map);
				BaseDao.getBaseDao().insertByTransfrom(tPageCode,listmap);
				map.clear();
				listmap.clear();
				Import_Excel_SuperTools.CountImpSum++;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				rde.setState(ResponseDataEntity.Fail);
				rde.setMessage("程序异常");
				rde.setBody("");
				return rde;
			}
		}
		rde.setState(ResponseDataEntity.Succ);
		rde.setMessage("操作正常");
		return rde;
	}
	
	/**
	 * exice行级数据插入 公共的 只做单一字段校验 如有复杂功能需另写
	 * @param cell 单元格对象
	 * @param pageCode 功能编码
	 * @param tPageCode Sql操作的表名
	 * @param k 遍历行的列值
	 * @param rowNum 循环中的行号
	 * @param last 数据拼接可插入列的最后一个单元格值从0开始（最后一列时做数据重复验证）
	 * @param whereSum where条件与中文字段下标（数据重复验证的字段是 初始化的***_FIELD）
	 * @param permissionsMap 20181112在数据导入数据库插入时增加数据权限字段
	 * @return
	 */
	public static ResponseDataEntity publicTableOperation(Cell cell,String pageCode ,String tPageCode,int k,int rowNum,int last,int whereSum ,Map<String,Object> permissionsMap) {
		// TODO Auto-generated method stub
		ResponseDataEntity rde=new ResponseDataEntity();
		String[] pageCodeBody=Import_Excel_SuperTools.pageCodeByBody(pageCode);//初始化表字段
		//logger.info("");
		map.put(pageCodeBody[k].toUpperCase(), Import_Excel_SuperTools.getStringVal(cell));//加入
		if (k == last){
			// 验证是否有此参数数据主键
			SqlTableUtil sqlEntity = new SqlTableUtil(tPageCode, "").appendSelFiled("ID").appendWhereAnd(
					pageCodeBody[whereSum] +" = " + "'" + map.get(pageCodeBody[whereSum]) + "'");
			List<Map<String, Object>> lDataSource = BaseDao.getBaseDao().query(sqlEntity);
			if (lDataSource.size() != 0) {
				CountMainValues.append((rowNum+1)+"、");
				rde.setState(ResponseDataEntity.Fail);
				rde.setMessage("第["+ (rowNum+1) +"]条数据 ["+ Import_Excel_SuperTools.pageCodeByHead(pageCode)[whereSum] +"] 主键字段在表中已存在");
				rde.setBody("");
				return rde;
			}//
			
			try {
				map.put("ID","");// 'ID'
//				addDataPermissions(map, permissionsMap);
//				listmap.add(map);
				listmap.add(addDataPermissions(map, permissionsMap));
				BaseDao.getBaseDao().insertByTransfrom(tPageCode,listmap);
				map.clear();
				listmap.clear();
				Import_Excel_SuperTools.CountImpSum++;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				rde.setState(ResponseDataEntity.Fail);
				rde.setMessage("程序异常");
				rde.setBody("");
				return rde;
			}
		}
		rde.setState(ResponseDataEntity.Succ);
		rde.setMessage("操作正常");
		return rde;
	}
	
	/**
	 * 数据权限问题 
	 * @param map2
	 * @return
	 */
	private static Map<String, String> addDataPermissions(Map<String, String> sourceMap,Map<String,Object> permissionsMap) {
//		Map<String,Object> m=new HashMap<String,Object>();
//		m.put("CREATOR_ID1", "1");
//		m.put("CREATOR_NAME2", "2");
//		m.put("ORGANIZATION_ID3", "3");
//		m.put("ORGANIZATION_NAME4", "4");
		try {
//			ButForInsert bfi=new ButForInsert();
//			Map<String, Object> mapo=new HashMap<String, Object>();
//			return bfi.No_Common(sourceMap, request);
			for (Map.Entry<String,Object> entry : permissionsMap.entrySet()) { 
				 // System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue()); 
				  sourceMap.put((String) entry.getKey(), (String) entry.getValue());
				}
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("ERR:com.yonyou.web.Import_Excel_SuperService.addDataPermissions(Map<String, String>, Map<String, Object>)");
			return sourceMap;
		}
		
		return sourceMap;
	}

	public static void main(String[] args) {
		Map<String,String> m=new HashMap<String,String>();
//		m.put("CREATOR_ID1", "1");
//		m.put("CREATOR_NAME2", "2");
//		m.put("ORGANIZATION_ID3", "3");
//		m.put("ORGANIZATION_NAME4", "4");
		
		
		
		m.put("CREATOR_ID", "1");
		m.put("CREATOR_NAME", "2");
		m.put("ORGANIZATION_ID", "3");
		m.put("ORGANIZATION_NAME", "4");
		
		Map map = new HashMap(); 
//		for (Map.Entry<String,Object> entry : m.entrySet()) { 
//		  System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue()); 
//		}
		System.out.println(addDataPermissions(m ,null));
	}
}
