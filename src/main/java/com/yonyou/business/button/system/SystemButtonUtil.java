package com.yonyou.business.button.system;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import net.sf.json.JSONArray;

import com.yonyou.business.DataSourceUtil;
import com.yonyou.business.button.ButtonAbs;
import com.yonyou.util.BussnissException;
import com.yonyou.util.ZipUtils;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.util.wsystem.service.ORG;

/**系统管理----按钮管理
 * @author XIANGJIAN
 * @date 创建时间：2017年3月7日
 * @version 1.0
 */
public abstract class SystemButtonUtil extends ButtonAbs {

	@Autowired
	protected IBaseDao dcmsDAO;
	
	private String dateRex = "yyyy-MM-dd HH:mm:ss";
	private String dateRexNoHMS = "yyyy-MM-dd";
	private String loginUser = "";

	/** 日期格式转换，包括时分秒 */
	private SimpleDateFormat sdf = new SimpleDateFormat(dateRex);

	/** 日期格式转换，不包括时分秒 */
	private SimpleDateFormat sdfNoHMS = new SimpleDateFormat(dateRexNoHMS);

	/** 获取当前时间，包括年月日时分秒 */
	private Date utilDate = new Date(System.currentTimeMillis());

	/** util.Date转换成sql.Timestamp存入数据库 */
	private Timestamp sqlDate = new Timestamp(utilDate.getTime());

	/**
	 * 获取日期格式转换类
	 * 
	 * @return 日期格式转换类，包括时分秒
	 */
	protected SimpleDateFormat getSdf() {
		return this.sdf;
	}

	/**
	 * 获取日期格式转换类
	 * 
	 * @return 日期格式转换类，不包括时分秒
	 */
	protected SimpleDateFormat getSdfNoHMS() {
		return this.sdfNoHMS;
	}

	/**
	 * 获取当前时间
	 * 
	 * @return Timestamp当前系统时间
	 */
	protected Timestamp getCurrentTimestampToSql() {
		return sqlDate;
	}

	/**
	 * 获取当前时间
	 * 
	 * @return Date当前系统时间
	 */
	protected Date getCurrentDateToSql() {
		return this.utilDate;
	}

	/**
	 * 获取当前时间
	 * 
	 * @return Timestamp当前系统时间字符串，包括时分秒
	 */
	protected String getCurrentTimestampBySql() {
		return this.sdf.format(sqlDate);
	}

	/**
	 * 获取当前时间
	 * 
	 * @return Timestamp当前系统时间字符串，不包括时分秒
	 */
	protected String getCurrentTimestampNoHMSBySql() {
		return this.sdfNoHMS.format(sqlDate);
	}

	/**
	 * 获取当前登录人名称
	 * 
	 * @return 当前登录人名称
	 */
	protected String getCurrentLoginUser() {
		return this.loginUser;
	}

	/**
	 * 判断对象是否为空
	 * 
	 * @param obj
	 * @return 为空返回true，否则返回false
	 */
	protected static boolean isNullOrEmpty(Object obj) {
		if (obj == null)
			return true;

		if (obj instanceof CharSequence)
			return ((CharSequence) obj).length() == 0;

		if (obj instanceof Collection)
			return ((Collection) obj).isEmpty();

		if (obj instanceof Map)
			return ((Map) obj).isEmpty();

		if (obj instanceof Object[]) {
			Object[] object = (Object[]) obj;
			if (object.length == 0) {
				return true;
			}
			boolean empty = true;
			for (int i = 0; i < object.length; i++) {
				if (!isNullOrEmpty(object[i])) {
					empty = false;
					break;
				}
			}
			return empty;
		}
		return false;
	}
	
	
	/**通过数据源获取表名
	 * @param dataSourceCode
	 * @return
	 * @throws BussnissException
	 */
	protected String findTableNameByDataSourceCode(String dataSourceCode) throws BussnissException{
		String tableName="";
		Map<String,String> dataMap=DataSourceUtil.getDataSource(dataSourceCode);
		if(dataMap!=null&&dataMap.containsKey(DataSourceUtil.DATASOURCE_METADATA_CODE)){
			tableName= dataMap.get(DataSourceUtil.DATASOURCE_METADATA_CODE);
		}
		return tableName;
	}
	
	/**通过数据源获取表名
	 * @param request
	 * @return 数据库表名
	 */
	protected String getTableName(HttpServletRequest request){
		String dataSourceCode =request.getParameter("dataSourceCode")==null ? "" : request.getParameter("dataSourceCode");
		if("".equals(dataSourceCode)){
			return "";
		}
		try {
			return findTableNameByDataSourceCode(dataSourceCode);
		} catch (BussnissException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**获取jsonarray
	 * @param request
	 * @return
	 */
	protected JSONArray getJsonArray(HttpServletRequest request){
		JSONArray jsonArray = JSONArray.fromObject(request.getParameter("jsonData"));
		return jsonArray;
	}
	
	/**产生四位流水号
	 * @param dcmsDAO
	 * @return 0001~9999
	 */
	protected String getSerialNumber(IBaseDao dcmsDAO){
		SqlTableUtil stu = new SqlTableUtil("dual" ,"");
		stu.appendSelFiled("SEQ_TEMP.NEXTVAL");
		Map<String , Object> seqMap = dcmsDAO.find(stu);
		String seq = seqMap.get("NEXTVAL").toString();
		int seqNum = Integer.parseInt(seq);
		if (seqNum / 10 == 0) {
			seq = "000" + seq;
		} else if (seqNum / 10 >= 0 && seqNum / 10 <= 9) {
			seq = "00" + seq;
		} else if (seqNum / 10 >= 10 && seqNum / 10 <= 99) {
			seq = "0" + seq;
		} else {
			seq = "" + seq;
		}
		return seq;
	}
	
	/**产生12位流水号
	 * @return 年月日+0001~9999
	 */
	protected String getSerialNumberForTwelve(IBaseDao dcmsDAO) {
		return new SimpleDateFormat("yyyyMMdd").format(new Date())+this.getSerialNumber(dcmsDAO);
	}
	
	/**产生18位流水号
	 * @return 年月日时分秒+0001~9999
	 */
	protected String getSerialNumberForEighteen(IBaseDao dcmsDAO) {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+this.getSerialNumber(dcmsDAO);
	}
	
	/**实体银行开户申请
	 * @param dcmsDAO
	 * @return STKHSQ+年月日+4位流水号
	 */
	protected String getSerialNumOfOpenOpen(IBaseDao dcmsDAO){
		return "STKHSQ"+getSerialNumberForTwelve(dcmsDAO);
	}
	
	/**实体银行账户变更
	 * @param dcmsDAO
	 * @return STZHBG+年月日时分秒+4位流水号
	 */
	protected String getSerialNumOfChange(IBaseDao dcmsDAO){
		return "STZHBG"+getSerialNumberForTwelve(dcmsDAO);
	}
	
	/**实体银行账户销户
	 * @param dcmsDAO
	 * @return STZHXH+年月日时分秒+4位流水号
	 */
	protected String getSerialNumOfDestory(IBaseDao dcmsDAO){
		return "STZHXH"+getSerialNumberForTwelve(dcmsDAO);
	}
	
	/**实体银行账户冻结
	 * @param dcmsDAO
	 * @return STZHDJ+年月日时分秒+4位流水号
	 */
	protected String getSerialNumOfFrozen(IBaseDao dcmsDAO){
		return "STZHDJ"+getSerialNumberForTwelve(dcmsDAO);
	}
	
	/**实体银行账户解冻
	 * @param dcmsDAO
	 * @return STZHJD+年月日时分秒+4位流水号
	 */
	protected String getSerialNumOfUnfrozen(IBaseDao dcmsDAO){
		return "STZHJD"+getSerialNumberForTwelve(dcmsDAO);
	}
	
	/**压缩字符串
	 * @param str
	 * @return
	 */
	protected String zipString(String str) {
		return ZipUtils.gzip(str);
	}
	
	/**解压字符串
	 * @param str
	 * @return
	 */
	protected String unZipString(String str) {
		return ZipUtils.gunzip(str);
	}
	
}
