package com.yonyou.util.replace;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.yonyou.business.DataSourceUtil;
import com.yonyou.business.entity.TokenEntity;
import com.yonyou.business.entity.TokenUtil;
import com.yonyou.util.BussnissException;

public class WhereReplaceEntity {
	
	private String where;
	
	private Map<String,Object> replaceMap =new HashMap<String,Object>();
	
	
	public WhereReplaceEntity(String dataSourceCode,HttpServletRequest request){
		this.init(dataSourceCode, request);
	}
	
	public WhereReplaceEntity(String _where,Map<String,Object> _replaceMap){
		where=_where;
		replaceMap=_replaceMap;
	}
	
	public WhereReplaceEntity(){
		
	}
	
	private void init(String dataSourceCode,HttpServletRequest request){
		try {
			Map<String,String> dataSourceMap =DataSourceUtil.getDataSource(dataSourceCode);
			if(dataSourceMap!=null){
				if(dataSourceMap.containsKey(DataSourceUtil.DATASOURCE_MAINTAIN_REPLACEDATA)){
					where =dataSourceMap.get(DataSourceUtil.DATASOURCE_MAINTAIN_REPLACEDATA);
				}
			}
			TokenEntity tokenEntity =TokenUtil.initTokenEntity(request);
			if(tokenEntity!=null){
				replaceMap.put(WhereReplaceEnum.COMPANY.getCode(), tokenEntity.COMPANY);
				replaceMap.put(WhereReplaceEnum.USER.getCode(), tokenEntity.USER);
			}
			
		} catch (BussnissException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
	}
	
	
	public void appendWhereAnd(String appendWhere){
		this.appendWhere(appendWhere, "AND");
	}
	
	public void appendWhereOr(String appendWhere){
		if(appendWhere!=null&&appendWhere.length()>0){
			appendWhere="("+appendWhere+")";
		}
		this.appendWhere(appendWhere, "OR");
	}
	
	private void appendWhere(String appendWhere,String type){
		
		if(appendWhere!=null&&appendWhere.length()>0){
			if(where!=null&&where.length()>0){
				where=where+""+type+" "+appendWhere;
			}else{
				where=appendWhere;
			}
		}
	}
	
	public void appendReplaceData(String column,Object obj){
		replaceMap.put(column, obj);
	}
	
	public String findReplaceWhereSql(){
		if(where!=null&&where.length()>0){
			return WhereReplaceUtil.replaceWhere(where, replaceMap);
		}else{
			return "";
		}
	}
	

	public String getWhere() {
		return where;
	}


	public Map<String, Object> getReplaceMap() {
		return replaceMap;
	}

	
	
}
