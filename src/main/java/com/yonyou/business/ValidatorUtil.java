package com.yonyou.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import com.yonyou.business.DataSourceUtil;
import com.yonyou.business.MetaDataUtil;
import com.yonyou.iuap.cache.CacheManager;
import com.yonyou.util.BussnissException;
import com.yonyou.util.PropertyFileUtil;
import com.yonyou.util.SerializationUtil;
import com.yonyou.util.SpringContextUtil;
import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.util.validator.mark.BulidValidMarkAbs;
import com.yonyou.util.validator.mark.IBulidValidMark;
import com.yonyou.util.validator.mark.imp.BulidValidMark;
import com.yonyou.util.validator.type.BulidContainerEnum;
import com.yonyou.util.validator.type.BulidTypeEnum;
import com.yonyou.util.validator.util.BulidValidUtil;

public class ValidatorUtil {
	
	//redis缓存
	public static final String REIDS_VALIDATOR="REIDS_VALIDATOR";
	
    //校验名称
	public static final String VALIDATOR_NAME = "NAME";	
	//校验类型
	public static final String VALIDATOR_TYPE = "VALIDATOR_TYPE";
	//校验正则表达式
	public static final String VALIDATOR_REGEXP = "VALIDATOR_REGEXP";
	//校验提示信息
	public static final String VALIDATOR_MESSAGE = "VALIDATOR_MESSAGE";
	//校验最小值
	public static final String VALIDATOR_MIN = "MIN";
	//校验最大值
	public static final String VALIDATOR_MAX = "MAX";
	//校验最大值
	public static final String VALIDATOR_FORMAT = "FORMAT";
		
	
	//校验最大
	public static final String VALIDATOR_NOEMPTY = "notEmpty";
	
					

	
	public static JSONObject findValidJson(String dataSourceCode){
		JSONObject obj =new JSONObject();
		BulidValidMarkAbs bulidMark =new BulidValidMark();
		

		obj.put(IBulidValidMark.MESSAGE, "输入信息有误！");
		obj.put(IBulidValidMark.CONTAINER, BulidContainerEnum.DIV.getType());
		JSONObject iconsobj =new JSONObject();
		iconsobj.put("valid", "glyphicon glyphicon-ok");
		iconsobj.put("invalid", "glyphicon glyphicon-remove");
		iconsobj.put("validating", "glyphicon glyphicon-refresh");
		
		obj.put(IBulidValidMark.FEEDBACKICONS, iconsobj);
		//headJson.put(IBulidValidMark.EXCLUDED, bulidMark.findJson(BulidTypeEnum.HEAD, IBulidValidMark.EXCLUDED, null));
		

		
		
		JSONObject fieldsJson =new JSONObject();
		
		try {
			 //将字段检验规则存入redis便于
			 CacheManager cacheManager = (CacheManager) SpringContextUtil.getBean("cacheManager");
			 List<Map<String,Object>> mapList =null;
			 byte[] validatorSeria = cacheManager.get(REIDS_VALIDATOR);
			 
			 if(null==validatorSeria){	 	
				
				 SqlTableUtil sqlTableUtil= DataSourceUtil.dataSourceToSQL("CD_METADATA_VALIDATOR");
				 mapList = BaseDao.getBaseDao().query(sqlTableUtil);
				
				 //字段校验在redis中缓存超时时间
				 String timeout = "".equals(PropertyFileUtil.getValue("validator_timeout"))?"1000":PropertyFileUtil.getValue("validator_timeout");
				 cacheManager.setex(REIDS_VALIDATOR, SerializationUtil.serialize(mapList),Integer.parseInt(timeout));			 
			 }else{
				 
				 mapList=(List<Map<String,Object>>)SerializationUtil.deserialize(validatorSeria);
			 }
			
			//将redis缓存的校验规则转换成validatorMap<字段校验id,字段校验规则>,方便根据id查询字段校验规则
			HashMap<String,Map<String,Object>>  validatorMap  = new HashMap<String,Map<String,Object>>();
			for(Map<String,Object> temp:mapList){
				validatorMap.put(((Object) temp.get("ID")).toString(), temp);			
			}
			
			//根据数据源查询元数据所有字段
			ConcurrentHashMap<String,ConcurrentHashMap<String,String>> metaDataFields= MetaDataUtil.getMetaDataFields(DataSourceUtil.getDataSource(dataSourceCode).get(DataSourceUtil.DATASOURCE_METADATA_CODE));
			String[] validators = null;
			String  messagestr= "";
			JSONObject validatorsObject =new JSONObject();
			JSONObject fieldTypesObject =new JSONObject();
			JSONObject msgObject =new JSONObject();
			JSONObject formatObject =new JSONObject();
			//metaDataFields元数据对应的所有字段
			for(ConcurrentHashMap<String,String> tempField:metaDataFields.values()){
				
				validators = tempField.get(MetaDataUtil.FIELD_METADATA_VALIDATOR_ID).split(",");
				
				
				validatorsObject =new JSONObject();
				fieldTypesObject =new JSONObject();
				if("1".equals(tempField.get(MetaDataUtil.FIELD_NULL_FLAG))){ 
					msgObject =new JSONObject();
					msgObject.put(IBulidValidMark.MESSAGE, BulidValidUtil.replaceMarkString(" {FIELD_NAME} 必填不能为空!",tempField) );					
					fieldTypesObject.put(IBulidValidMark.NOTEMPTY, msgObject);
				}

				
				for(String tempIDs :validators){
					if(null==tempField.get(MetaDataUtil.FIELD_METADATA_VALIDATOR_ID) || "".equals(tempField.get(MetaDataUtil.FIELD_METADATA_VALIDATOR_ID).trim())){
						
	                     break;
					}
					msgObject =new JSONObject();
					Map<String,Object> tempMap = validatorMap.get(tempIDs);
					if(tempMap==null){
						continue;
					}
					
					messagestr = (String)tempMap.get(ValidatorUtil.VALIDATOR_MESSAGE);
					if(null!=tempMap.get(ValidatorUtil.VALIDATOR_MIN) && !"".equals(tempMap.get(ValidatorUtil.VALIDATOR_MIN))){
						
						msgObject.put(IBulidValidMark.MIN, tempMap.get(ValidatorUtil.VALIDATOR_MIN));
					}
					 
					if(null!=tempMap.get(ValidatorUtil.VALIDATOR_MAX) && !"".equals(tempMap.get(ValidatorUtil.VALIDATOR_MAX))){
						
						msgObject.put(IBulidValidMark.MAX, tempMap.get(ValidatorUtil.VALIDATOR_MAX));
					}else{
						msgObject.put(IBulidValidMark.MAX, tempField.get(MetaDataUtil.FIELD_LENGTH));
						
					}
					
					
					messagestr=BulidValidUtil.replaceMarkString(messagestr,tempField);
					msgObject.put(IBulidValidMark.MESSAGE, messagestr);
					
					if(null!=tempMap.get(ValidatorUtil.VALIDATOR_FORMAT) && !"".equals(tempMap.get(ValidatorUtil.VALIDATOR_FORMAT))){
						
						msgObject.put(IBulidValidMark.FORMAT, tempMap.get(ValidatorUtil.VALIDATOR_FORMAT));
					}
					if(null!=tempMap.get(ValidatorUtil.VALIDATOR_REGEXP) && !"".equals(tempMap.get(ValidatorUtil.VALIDATOR_REGEXP))){
						
						msgObject.put(IBulidValidMark.REGEXP, tempMap.get(ValidatorUtil.VALIDATOR_REGEXP));
					}
					
					fieldTypesObject.put(tempMap.get(ValidatorUtil.VALIDATOR_TYPE), msgObject);
					
					
				}
				if(fieldTypesObject.size()!=0){
					validatorsObject.put(IBulidValidMark.VALIDATORS, fieldTypesObject);
					fieldsJson.put(tempField.get(MetaDataUtil.FIELD_CODE), validatorsObject);
					
				}
			}
			
			obj.put(IBulidValidMark.FIELDS, fieldsJson);
			
		} catch (BussnissException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		return obj;
	}
	
}
