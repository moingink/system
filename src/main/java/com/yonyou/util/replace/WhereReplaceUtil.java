package com.yonyou.util.replace;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.yonyou.business.WorkflowNodeUtil;
import com.yonyou.util.BussnissException;
import com.yonyou.util.DateUtil;
import com.yonyou.util.reflex.ClassEntity;
import com.yonyou.util.reflex.Field.FieldEntity;
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.util.sql.SqlWhereEntity;
import com.yonyou.util.sql.SQLUtil.WhereEnum;

public class WhereReplaceUtil {
		
	
	
	
	
	private static Map<String,ReplaceTypeEnum> REPLACE_ENUM =new HashMap<String,ReplaceTypeEnum>();
	private static String SPLIT_MARK="_";
	private static String WHERE_SPLIT_MARK="#";
	private static String ERROR_REPLACE_MARK="-1";
	static{
		for(WhereReplaceEnum replaceEnum:WhereReplaceEnum.values()){
			REPLACE_ENUM.put(replaceEnum.getCode(), replaceEnum.getTypeEnum());
		}
	}
	
	
	public static String replaceWhere(String where,Map<String,Object> replaceMap){
		StringBuffer replaceWhere =new StringBuffer(where);
		if(where!=null&&where.length()>0){
			String [] wheres =where.split(WHERE_SPLIT_MARK);
			if(wheres.length>1){
				replaceWhere.setLength(0);
				for(int i=0;i<wheres.length;i++){
					if(i>0&&i%2!=0){
						String replace =replaceString(wheres[i],replaceMap);
						if(replace!=null&&!replace.equals(ERROR_REPLACE_MARK)){
							replaceWhere.append(replace);
						}else{
							//如果解析取值失败，则返回为替换数据
							return where;
						}
					}else{
						replaceWhere.append(wheres[i]);
					}
				}
			}
		}
		
		return replaceWhere.toString();
	}
	
	
	private static String replaceString(String replace,Map<String,Object> replaceMap){
		
		String [] repalces =replace.split(SPLIT_MARK);
		if(repalces.length>0){
			String value =repalceType(replace.toString(),"",ReplaceTypeEnum.REPLACE_STRING,replaceMap);
			if(value.length()>0&&!value.equals(ERROR_REPLACE_MARK)){
				return value;
			}else{
				return repalceStringByObj(repalces,replaceMap);
			}
				
		}else{
			return ERROR_REPLACE_MARK;
		}
		
	}
	
	
	private static String repalceStringByObj(String[] replaces,Map<String,Object> replaceMap){
		String column ="";
		ReplaceTypeEnum typeEnum =null;
		String replaceString=replaces[0].toUpperCase();
		column=findReplaceByArrays(replaces);
		if(REPLACE_ENUM.containsKey(replaceString)){
			typeEnum=REPLACE_ENUM.get(replaceString);
		}
		if(typeEnum!=null){
			return repalceType(replaceString,column,typeEnum,replaceMap);
		}else{
			return ERROR_REPLACE_MARK;
		}
	}
	
	
	private static String repalceType(String repalce,String endString,ReplaceTypeEnum typeEnum,Map<String,Object> replaceMap){
		String value=ERROR_REPLACE_MARK;
		Object obj =null;
		repalce=repalce.toUpperCase();
		if(replaceMap.containsKey(repalce)){
			obj=replaceMap.get(repalce);
		}
		if(typeEnum.equals(ReplaceTypeEnum.REPLACE_DATE)){
			value=findDate(endString);
		}else if(typeEnum.equals(ReplaceTypeEnum.REPLACE_OBJ)){
			value=findObj(endString,obj);
		}else if(typeEnum.equals(ReplaceTypeEnum.REPLACE_STRING)){
			value=findString(endString,obj);
		}
		return value;
	}
	
	
	private static String findDate(String column){
		if(column.length()>0){
			String repalceString =ERROR_REPLACE_MARK;
			try{
				repalceString=DateUtil.findSystemDateString(column);
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("##日期格式失败："+column);
			}
			return repalceString;
		}else{
			return DateUtil.findSystemDateString();
		}
	}
	
	private static String findObj(String column,Object obj){
		String value=ERROR_REPLACE_MARK;
		if(column.length()>0&&obj!=null){
			ClassEntity  classEntity =new ClassEntity(obj);
			FieldEntity  filedEntity =classEntity.findFiled(column.toLowerCase());
			if(filedEntity!=null){
				Object v =filedEntity.getValue();
				if(v!=null){
					value=(String)v;
				}
			}else{
				System.out.println("对象获取值："+obj+"\t"+column+"没有找到相关列信息");
			}
		}
		return value;
	}
	
	private static String findString(String column,Object obj){
		if(obj!=null){
			return obj.toString();
		}else{
			return ERROR_REPLACE_MARK;
		}
		
	}
	
	private static String findReplaceByArrays(String [] replaces){
		StringBuffer replace =new StringBuffer();
		for(int i=1;i<replaces.length;i++){
			replace.append(replaces[i]).append(SPLIT_MARK);
		}
		String returnReplace=replace.toString();
		if(returnReplace.length()>1){
			returnReplace=returnReplace.substring(0, returnReplace.length()-1);
		}
		return returnReplace;
	}
	
	
	
	public  static  void appendCheckStateWhere(SqlTableUtil sqlTable,String nodeCode,String dataSourceCode){
		try {
			
			String filed=WorkflowNodeUtil.findWriteFiledByDataCode(nodeCode, dataSourceCode);
			String value=WorkflowNodeUtil.findWriteValueByDataCode(nodeCode, dataSourceCode);
			SqlWhereEntity whereEntity =new SqlWhereEntity();
			if(filed!=null&&filed.length()>0){
				if(sqlTable!=null&&sqlTable.getAlias_table_name()!=null&&sqlTable.getAlias_table_name().length()>0){
					filed=sqlTable.getAlias_table_name()+"."+filed;
				}
				whereEntity.putWhere(filed, value, WhereEnum.EQUAL_STRING);
				sqlTable.appendSqlWhere(whereEntity);
			}
			
		}catch (BussnissException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	
	public static void appendReplaceWhereSql(SqlTableUtil sqlTable,String dataSourceCode,HttpServletRequest request){
		if(sqlTable!=null
				&&dataSourceCode!=null&&dataSourceCode.length()>0
				&&request!=null){
			WhereReplaceEntity replaceEntity =new WhereReplaceEntity(dataSourceCode,request);
			appendReplaceWhereSql(sqlTable, dataSourceCode, replaceEntity, request);
		}
		
	}
	
	
	public static void appendReplaceWhereSql(SqlTableUtil sqlTable,String dataSourceCode,WhereReplaceEntity replaceEntity,HttpServletRequest request){
		if(sqlTable!=null
				&&dataSourceCode!=null&&dataSourceCode.length()>0
				&&request!=null){
			if(replaceEntity!=null){
				sqlTable.appendWhereAnd(replaceEntity.findReplaceWhereSql());
			}
			
		}
	}
	
	public static void main(String [] args){
		Map<String,Object> dataMap =new HashMap<String,Object>();
		dataMap.put("ID", "123");
		dataMap.put("CODE", "CD_METADATA");
		dataMap.put("SYSSTEM_CODE", "WORKflow");
		String url ="http://127.0.0.1:8082/#SYSSTEM_CODE#/#CODE#?cmd=findId&id=#ID#";
		System.out.println(WhereReplaceUtil.replaceWhere(url, dataMap));
	}
}
