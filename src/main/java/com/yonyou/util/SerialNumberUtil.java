package com.yonyou.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SQLUtil.WhereEnum;
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.util.sql.SqlWhereEntity;

/**
 * 序列号生成工具类
* @ClassName SerialNumberUtil 
* @author hubc
* @date 2018年6月4日
 */
public class SerialNumberUtil {

	public static Logger logger = LoggerFactory.getLogger(SerialNumberUtil.class);
	
	private static IBaseDao dao = BaseDao.getBaseDao();
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("YYYYMMddHHmmss");
	
	/**
	 * 序号管理表
	 */
	private final static String TABLE_NAME = "CD_SERIAL_NUMBER";
	/**
	 * 序号类型
	 */
	private final static String FIELD_SERIAL_TYPE = "SERIAL_TYPE";
	/**
	 * 个性编码
	 */
	private final static String FIELD_INDIVIDUAL_CODE = "INDIVIDUAL_CODE";
	/**
	 * 当前序号
	 */
	private final static String FIELD_SERIAL_NUMBER = "SERIAL_NUMBER";
	/**
	 * 创建日期-varchar2类型格式：YYYYMMddHHmmss
	 */
	private final static String FIELD_CREATE_DATE = "CREATE_DATE";
	
	/**
	 * 序号类型枚举对应字典关键字
	 */
	private final static String TYPE_KEYWORD = "SERIAL_TYPE";
	
	/**
	 * 序列类型
	* @ClassName SerialType 
	* @author hubc
	* @date 2018年6月5日
	 */
	public enum SerialType{
		/**
		 * 项目编码
		 */
		PROJECT_CODE("0"),
		/**
		 * 文档上传批次号
		 */
		DOC_BATCH_NO("1"),
		/**
		 * 商机编号
		 */
		BUS_OPPTY_CODE("2"),
		/**
		 * 三位流水号
		 */
		THREE_BIT_CODE("3"),
		
		
		THREE_BIT_CODES("5"),
		
		/**
		 * 四位流水号
		 */
		FOUR_BIT_CODE("4"),
		
		/**
		 * 版本号1
		 */
		PROJ_BIT_CODE("2"),
		
		/**
		 * 版本号2
		 */
		PROJ_BIT_CODE1("2"),
		
		/**
		 * 版本号3
		 */
		PROJ_BIT_CODE2("2"),
		
		/**
		 * 版本号4
		 */
		PROJ_BIT_CODE3("2"),
		
		/**
		 * 版本号5
		 */
		PROJ_BIT_CODE4("2"),
		
		/**
		 * 版本号6
		 */
		PROJ_BIT_CODE5("2");
		
		/**
		 * 类型枚举值
		 */
		private String dbVal;
		private SqlWhereEntity whereEntity = new SqlWhereEntity();
		
		SerialType(String dbVal){
			this.dbVal = dbVal;
			this.whereEntity.putWhere("SERIAL_TYPE", dbVal, WhereEnum.EQUAL_STRING).putWhere("DR", "0", WhereEnum.EQUAL_STRING);
		}
		
		public String getDbVal(){
			return this.dbVal;
		}
		
		public SqlWhereEntity getSqlWhere(){
			return CloneUtil.clone(this.whereEntity);
		}
		
	}
	
	/**
	 * 获取最新序列号
	* @param type
	* @param indCode
	* @return
	 */
	public static String getSerialCode(SerialType type, String indCode) {
		String serialcode = null;
		String[] serialCodes = getSerialCode(type, indCode, 1);
		if (serialCodes != null) {
			serialcode = serialCodes[0];
		}
		return serialcode;
	}
	
	/**
	 * 批量获取最新序列号
	* @param type
	* @param indCode
	* @param size 序列号数量
	* @return
	 */
	public static String[] getSerialCode(SerialType type, String indCode, int size){
		String[] serialCodes = null;
		SqlTableUtil sqlTableUtil = new SqlTableUtil(TABLE_NAME, "").appendSelFiled("ID,VERSION").appendSelFiled(FIELD_SERIAL_NUMBER);
		//通用过滤	type+INDIVIDUAL_CODE
		SqlWhereEntity sqlWhereEntity = type.getSqlWhere();
		if(indCode != null && indCode.trim() != ""){
			sqlWhereEntity.putWhere(FIELD_INDIVIDUAL_CODE, indCode, WhereEnum.EQUAL_STRING);
		}
		sqlTableUtil.appendSqlWhere(sqlWhereEntity);
		String date = dateFormat.format(new Date());
		
		switch (type) {
		case PROJECT_CODE:
			//年份过滤--年份为手填，去除过滤	2018-6-25 by hubc
//			sqlWhereEntity.putWhere(FIELD_CREATE_DATE, date.substring(0, 4), WhereEnum.BACK_LIKE);
//			serialCodes = makeProjectCode(sqlTableUtil.appendSqlWhere(sqlWhereEntity), date.substring(0, 4) + indCode, size);
			serialCodes = makeSerialNum(sqlTableUtil, indCode, size, 3, type);
			break;
		case DOC_BATCH_NO:
			serialCodes = makeSerialNum(sqlTableUtil, "DOC", size, 15, type);
			break;
		case BUS_OPPTY_CODE:
			//商机编号规则： Z9FCUx201xxxxx0058，前五位固定 第六位用 A-H代表 8 个业务，多业务为 I，后面是年月日和四位编码，四位编码每自然年进行一次重算；
			serialCodes = makeSerialNum(sqlTableUtil, indCode+date.substring(0,4)+"#", size, 4, type);//年月日只入库年份，保证每自然年进行重算
			for (int i = 0; i < serialCodes.length; i++) {
				serialCodes[i] = serialCodes[i].replaceFirst("#", date.substring(4,8));
			}
			break;
		case THREE_BIT_CODE:
			serialCodes = makeSerialNum(sqlTableUtil, indCode, size, 3, type);
			break;
		case THREE_BIT_CODES:
			serialCodes = makeSerialNum(sqlTableUtil, indCode, size, 5, type);	
			break;
		case FOUR_BIT_CODE:
			serialCodes = makeSerialNum(sqlTableUtil, indCode, size, 4, type);
			break;
			
		case PROJ_BIT_CODE:
			serialCodes = makeSerialNum(sqlTableUtil, indCode, size, 2, type);
			break;
		case PROJ_BIT_CODE1:
			serialCodes = makeSerialNum(sqlTableUtil, indCode, size, 2, type);
			break;	
		case PROJ_BIT_CODE2:
			serialCodes = makeSerialNum(sqlTableUtil, indCode, size, 2, type);
			break;	
		case PROJ_BIT_CODE3:
			serialCodes = makeSerialNum(sqlTableUtil, indCode, size, 2, type);
			break;	
		case PROJ_BIT_CODE4:
			serialCodes = makeSerialNum(sqlTableUtil, indCode, size, 2, type);
			break;	
		case PROJ_BIT_CODE5:
			serialCodes = makeSerialNum(sqlTableUtil, indCode, size, 2, type);
			break;	
		
		default:
			break;
		}
		return serialCodes;
	}
	
	/**
	 * 生成流水号
	* @param sqlTableUtil
	* @param prefixCode
	* @param size
	* @param length
	* @return
	 */
	private static String[] makeSerialNum(SqlTableUtil sqlTableUtil, String prefixCode, int size, int length, SerialType type){
		
		String[] codeArray = null;
		
		synchronized (prefixCode) {
			//最新序号
			int serialNum = 0;
			//序号长度,不足用0补足
			int serialLength = length;
						
			List<Map<String, Object>> result = dao.query(sqlTableUtil);
			if(result.size() == 1){
				Map<String, Object> serialEntity = result.get(0);
				serialNum = (int) serialEntity.get(FIELD_SERIAL_NUMBER) + size;
				if(String.valueOf(serialNum).length() <= serialLength){
					Map<String, Object> entity = new HashMap<>();
					entity.put("ID", serialEntity.get("ID"));
					entity.put(FIELD_SERIAL_NUMBER, serialNum);
					entity.put("VERSION", "");//版本号传空即可自增
//					dao.update("update "+ TABLE_NAME + " set " + FIELD_SERIAL_NUMBER + " = " + serialNum + ",VERSION = VERSION + 1 where id = " + serialEntity.get("ID") + " and version = " + serialEntity.get("VERSION"));
					int i = dao.update(TABLE_NAME, entity, new SqlWhereEntity());
					if(i == 1){
						codeArray = new String[size];
						for (int j = 0; j < codeArray.length; j++) {
							codeArray[j] = prefixCode + String.format("%0"+serialLength+"d", serialNum-size+1+j);
						}
					}else{
						//更新失败，原纪录被修改，重新获取
//						makeProjectCode(sqlTableUtil, prefixCode, size);
						logger.error("获取流水号失败，数据库流水记录更新异常，id：" + serialEntity.get("ID"));						
					}
				}else{
					logger.error("获取流水号失败，数据库流水号已到达指定位数极限");
				}
			}else if(result.size() == 0){
				HashMap<String, String> entity = new HashMap<>();
				entity.put(FIELD_SERIAL_TYPE, type.getDbVal());
				entity.put(FIELD_INDIVIDUAL_CODE, prefixCode);
				entity.put(FIELD_SERIAL_NUMBER, String.valueOf(size));
				entity.put(FIELD_CREATE_DATE, dateFormat.format(new Date()));
				try {
					dao.insertByTransfrom(TABLE_NAME, entity);
					codeArray = new String[size];
					for (int j = 0; j < codeArray.length; j++) {
						codeArray[j] = prefixCode + String.format("%0"+serialLength+"d", serialNum+1+j);
					}
				} catch (BussnissException e) {
					logger.error("获取流水号失败，新增流水号记录入库失败 " + e);
				}
			}else{
				logger.error("获取流水号失败，数据库中匹配到多条流水号记录");
			}
			return codeArray;
		}
	}
	
	public static void main(String[] args) {
		System.out.println(dateFormat.format(new Date()));
		System.out.println(SerialType.PROJECT_CODE.toString());
	}
	
}
