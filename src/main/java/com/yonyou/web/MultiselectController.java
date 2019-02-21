package com.yonyou.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yonyou.business.DataSourceUtil;
import com.yonyou.business.MetaDataUtil;
import com.yonyou.business.RmDictReferenceUtil;
import com.yonyou.util.BussnissException;
import com.yonyou.util.JsonUtils;
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.util.sql.SqlWhereEntity;
import com.yonyou.util.sql.SQLUtil.WhereEnum;

/**	元数据左右选择框控制类
 * @author XIANGJIAN
 * @date 创建时间：2017年4月6日
 * @version 1.0
 */
@RestController
@RequestMapping(value = "/multiselect")
public class MultiselectController extends BaseController {
	
	@RequestMapping(params = "cmd=getOptions")
	public void getColumns(HttpServletRequest request,HttpServletResponse response) throws IOException, BussnissException {
		String dataSourceCode = request.getParameter("dataSourceCodeUrl");
		String dataCodes = request.getParameter("dataCodes");
		SqlTableUtil sqlEntity = DataSourceUtil.dataSourceToSQL(dataSourceCode);
		sqlEntity.appendSqlWhere(new SqlWhereEntity().putWhere("CM.DATA_CODE", dataCodes, WhereEnum.IN));
		List<Map<String, Object>> mapList = dcmsDAO.query(sqlEntity);
		mapList = RmDictReferenceUtil.transByDict(mapList, dataSourceCode);
		//int total = dcmsDAO.queryLength(sqlEntity);
		sqlEntity.clearTableMap();
		String json = "{\"message\":" + JsonUtils.object2json(mapList) + "}";
		System.out.println(json);
		this.ajaxWrite(json, request, response);
	}
	
	/**
	 * 根据meta_id返回元数据字段信息
	 */
	@RequestMapping(params = "cmd=getColumnInfo")
	public void  getColumnInfo(HttpServletRequest request  , HttpServletResponse response) throws IOException{
		String metaId = request.getParameter("metaDataId");
		SqlTableUtil tableUtil = new SqlTableUtil("CD_METADATA_DETAIL","");
		tableUtil.appendSelFiled("ID,METADATA_ID,FIELD_CODE,FIELD_NAME");
		SqlWhereEntity whereEntity = new SqlWhereEntity();
		whereEntity.putWhere("METADATA_ID", metaId, WhereEnum.EQUAL_STRING).putWhere("DR","0", WhereEnum.EQUAL_STRING);
		tableUtil.appendSqlWhere(whereEntity);
		List<Map<String, Object>> queryColumnInfoList = dcmsDAO.query(tableUtil);
		tableUtil.clearTableMap();
		String json = "{\"message\":" + JsonUtils.object2json(queryColumnInfoList) + "}";
		this.ajaxWrite(json, request, response);
	}
	/**
	 * 根据数据源编码获取返回字段信息
	 * @throws IOException 
	 * @throws BussnissException 
	 * 
	 */
	@RequestMapping(params = "cmd=getReturnField")
	public void getReturnField(HttpServletRequest request  , HttpServletResponse response) throws IOException, BussnissException{
		String dataSourceCode = request.getParameter("dataSourceCode");
		ConcurrentHashMap<String, String> mDataSource= DataSourceUtil.getDataSource(dataSourceCode);
		String metadataCode =mDataSource.get(DataSourceUtil.DATASOURCE_METADATA_CODE);//DATASOURCE_RETURN_FIELD
		String dReturnField = mDataSource.get(DataSourceUtil.DATASOURCE_RETURN_FIELD) ; 
		String returnField[] = dReturnField.split(",");
		ConcurrentHashMap<String,ConcurrentHashMap<String,String>> mMetadata = MetaDataUtil.getMetaDataFields(metadataCode); 
		List<Map<String , Object>> list = new ArrayList<Map<String,Object>>();
		if("*".equals(dReturnField) || dReturnField.contains("*")){
			for(Map.Entry<String,ConcurrentHashMap<String,String>> e: mMetadata.entrySet()){
				Map<String, Object> map = new HashMap<String, Object>() ; 
				map.put("FIELD_CODE", e.getKey());
				map.put("FIELD_NAME", e.getValue().get("FIELD_NAME"));
				list.add(map) ; 
			}
		}else{
			for(int i = 0 ; i < returnField.length ; i ++){
				Map<String, Object> map = new HashMap<String, Object>() ; 
				if(mMetadata.containsKey(returnField[i])){
					map.put("FIELD_CODE", returnField[i]) ; 
					map.put("FIELD_NAME", mMetadata.get(returnField[i]).get("FIELD_NAME")) ; 
					list.add(map) ; 
				}
			}
		}
		String json = "{\"message\":" + JsonUtils.object2json( list) + "}";
		this.ajaxWrite(json, request, response);
	}
	
	private void ajaxWrite(String ajaxMessage,HttpServletRequest request,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.print(ajaxMessage);
		out.flush();
		out.close();
	}
	
}
