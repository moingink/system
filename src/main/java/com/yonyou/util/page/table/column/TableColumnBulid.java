package com.yonyou.util.page.table.column;

import java.util.Map;
import com.yonyou.util.sql.SQLUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 列加工类
 * @author moing_ink
 * <p>创建时间 ： 2016年12月27日
 * @version 1.0
 */
public  class  TableColumnBulid extends TableColumn {
	
	
	
	public TableColumnBulid(){
		super();
	}
	
	
	
	public  String findColJosnStr(Map<String,String> showMap){
		if(!isSerialNumber){
			showMap.remove(TableColumnUtil.RMRN);
		}
		JSONArray jsonArray =new JSONArray();
		this.appendCheck(jsonArray);
		for(String filed:showMap.keySet()){
			String val =showMap.get(filed);
			if(!SQLUtil.isEmpty(val)){
				jsonArray.add(bulidColJsonObj(filed, val));
			}else{
				jsonArray.add(bulidColJsonObj(filed, filed));
			}
		}
		return jsonArray.toString();
	}
	
	/**
	 * 构建JSON对象信息
	 * @param field
	 * @param title
	 * @return
	 */
	private JSONObject bulidColJsonObj(String field,String title){
		JSONObject obj =new JSONObject();
		obj.put(TableColumnUtil.FIELD, field);
		obj.put(TableColumnUtil.TITLE, title);
		for(String pubKey:publicArtMap.keySet()){
			obj.put(pubKey, publicArtMap.get(pubKey));
		}
		if(colArtMap.containsKey(field)){
			for(String colKey:colArtMap.get(field).keySet()){
				obj.put(colKey, colArtMap.get(field).get(colKey));
			}
		}
		return obj;
	}
	/**
	 * 添加多选信息
	 * @param jsonArray
	 */
	private  void appendCheck(JSONArray jsonArray){
		if(!"".equals(checkBoxEnum.getVal())){
			JSONObject obj =new JSONObject();
			obj.put(checkBoxEnum.getVal(), true);
			String checkStr =checkBoxEnum.getVal().toUpperCase();
			if(colArtMap.containsKey(checkStr)){
				for(String key:colArtMap.get(checkStr).keySet()){
					obj.put(key, colArtMap.get(checkStr).get(key));
				}
			}
			jsonArray.add(obj);
		}
	}
	
	
	
}
