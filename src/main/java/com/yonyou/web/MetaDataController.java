package com.yonyou.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.yonyou.business.MetaDataUtil;
import com.yonyou.util.BussnissException;
import com.yonyou.util.SerialNumberUtil;
import com.yonyou.util.SerialNumberUtil.SerialType;
import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.sql.SQLUtil.WhereEnum;
import com.yonyou.util.sql.SqlWhereEntity;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
* @ClassName: MetaDataController 
* @Description: 元数据控制类
* @author 博超
* @date 2016年12月27日 
*
 */
@RestController
@RequestMapping(value = "/metaData")
public class MetaDataController extends BaseController {
	
	@Override
	public void insRow(HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		super.insRow(request, response);
		MetaDataUtil.clear();
		
	}
	
	@Override
	public void delRows(HttpServletRequest request,HttpServletResponse response) throws IOException, BussnissException {
		
		super.delRows(request, response);
		
		//逻辑删除子表记录 
		//FIXME 目前主子表删除不在一个事务中，存在风险
		Map<String, Object> entity = new HashMap<String, Object>();
		entity.put("DR","1");
		JSONArray jsonArray = JSONArray.fromObject(request.getParameter("jsonData"));
		SqlWhereEntity whereEntity  = new SqlWhereEntity();
		String ids = "";
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jb = (JSONObject)jsonArray.get(i);
			ids += jb.getString("ID") + ",";
		}
		whereEntity.putWhere(MetaDataUtil.FIELD_METADATA_ID,ids.substring(0, ids.length()-1), WhereEnum.IN);
		dcmsDAO.update(MetaDataUtil.FIELD_TABLE_NAME, entity, whereEntity);
		
		MetaDataUtil.clear();
		
	}
	//复制元数据主子表
	@RequestMapping(value = "copy", method = RequestMethod.POST)
	public String getProductList(HttpServletRequest request, HttpServletResponse response) throws IOException, BussnissException  {
		
		String  id = request.getParameter("id");
		//先查询  先复制以时间戳为结尾复制到元数据主表中 子表直接复制
		String metadataSql = "select * from cd_metadata where dr=0 and  id ="+id ;
		String metadatadetailSql = "select * from cd_metadata_detail where dr=0 and  metadata_id ="+id ;
		List<Map<String, Object>> list = BaseDao.getBaseDao().query(metadataSql, "");
		List<Map<String, Object>> detailjson = BaseDao.getBaseDao().query(metadatadetailSql, "");
		list.get(0).remove("ID");
		
		//移除id  更改data_code 
		String code = list.get(0).get("DATA_CODE").toString();
		list.get(0).remove("DATA_CODE");
		String serialCode = SerialNumberUtil.getSerialCode(SerialType.PROJECT_CODE, code);
		list.get(0).put("DATA_CODE", serialCode);
		//list.get(0).put("ID", "");
		//插入
		Map<String, Object> dataMap  = list.get(0);
		JSONObject json = JSONObject.fromObject(dataMap);
		
		String save_id= dcmsDAO.insertByTransfrom("CD_METADATA", json);
		//辅助子表字段信息
		for(int i=0; i<detailjson.size();i++){
			detailjson.get(i).remove("ID");
			detailjson.get(i).replace("METADATA_ID", save_id);
			Map<String, Object> detaildataMap = detailjson.get(i);
			for (Entry<String, Object> entry : detaildataMap.entrySet()) {
				if(StringUtils.isEmpty(entry.getValue())) {
					entry.setValue("");
				}
			}
			JSONObject detailJson = JSONObject.fromObject(detaildataMap);
			dcmsDAO.insertByTransfrom("CD_METADATA_DETAIL", detailJson);
		}
		
		
		
		
		return JSON.toJSONString("操作成功");
	}
	//建表语句
	@RequestMapping(value = "createTable", method = RequestMethod.POST)
	public String createTable(HttpServletRequest request, HttpServletResponse response) throws IOException, BussnissException  {
		
		String  id = request.getParameter("id");
		String tableName = request.getParameter("table");
		//查询元数据和字段信息拼接创建表语句  执行建标
		//区分数据库类型  后续实现多数据库类型查询字段类型
		
		
		//获取key获取数据库类型字段 从MYSQL_DATEBASE_TYPE获取
		String columnSql = "select* from cd_metadata_detail where dr=0 and  metadata_id ="+id ;
		
		
		List<Map<String, Object>> list = BaseDao.getBaseDao().query(columnSql, "");
		StringBuffer tempColumnSql = new StringBuffer(" CREATE TABLE ");
		
		
		tempColumnSql.append(tableName+" ( ");
		int i= 0;
		for(Map<String, Object> dataMap:list){
			i++;
			String columnName = String.valueOf(dataMap.get("FIELD_CODE"));//字段名
			String fieldType = String.valueOf(dataMap.get("FIELD_TYPE"));//字段类型
			String length = String.valueOf(dataMap.get("FIELD_LENGTH"));//字段长度
			String keyFlag = String.valueOf(dataMap.get("KEY_FLAG"));//主键标志
			//默认值
			//唯一约束
			String fieldTypeSql = "SELECT d.* from rm_code_type   t INNER JOIN rm_code_data  d on t.id= d.CODE_TYPE_ID where t.TYPE_KEYWORD = 'MYSQL_DATEBASE_TYPE' and d.DATA_KEY = "+fieldType;
			List<Map<String, Object>> fieldList = BaseDao.getBaseDao().query(fieldTypeSql, "");
			fieldType = fieldList.get(0).get("DATA_VALUE").toString();
			String nullFlag = String.valueOf(dataMap.get("NULL_FLAG"));//空值标识——0：能为空；1：不能为空
			tempColumnSql.append(columnName + " "  +fieldType );
			if(null != length && !"".equals(length.trim())){
				tempColumnSql.append(" (" + length +")");
			}
			if(keyFlag.equals("1")){
				tempColumnSql.append(" not null primary key");
			}else if(nullFlag.equals("1")){
				tempColumnSql.append(" not null");
			}else if("TIMESTAMP".equals(fieldType)){
				tempColumnSql.append(" null");//mysql中TIMESTAMP类型默认为非空
			}
			if(i!=list.size()) {
				tempColumnSql.append(" , ");
			}
		}
		tempColumnSql.append(" )");
		
		//判断表是否存在
		
		//存在的确认有没有数据,无数据才可以drop 重新执行建标 否则返回异常提示
		try {
			if(checkExit(tableName)){
				//存在的话 查询是否有数据
				String countSql = "select count(*) as COUNT from "+ tableName;
				List<Map<String, Object>> countList = BaseDao.getBaseDao().query(countSql, "");
				if(Integer.valueOf(countList.get(0).get("COUNT").toString())>0){
					return JSON.toJSONString("表已存在数据,无法重新创建");
				}else {
					//执行 drop
					dcmsDAO.updateBySql(" DROP TABLE " + tableName);
					dcmsDAO.updateBySql(tempColumnSql.toString());
					return JSON.toJSONString("创建成功");
				}
			}else {
				dcmsDAO.updateBySql(tempColumnSql.toString());
			}
		}catch(Exception e) {
			//e.printStackTrace();
			return "{\"message\":\"保存失败，请联系管理员\"}";
		}
		return JSON.toJSONString("创建成功");
	}
	
	@Override
	public void editRow(HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		super.editRow(request, response);
		MetaDataUtil.clear();
		
	}
	
	public boolean checkExit(String tableName) {
		String tableSql = "select * from information_schema.tables where table_name='"+tableName+"'" ;
		
		
		List<Map<String, Object>> list = BaseDao.getBaseDao().query(tableSql, "");
		if(list.size()>0) {
			return  true;
		}
		return  false;
	}
}
