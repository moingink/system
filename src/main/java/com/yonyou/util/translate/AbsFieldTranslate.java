package com.yonyou.util.translate;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据翻译抽象列
 * @author moing_ink
 * <p>创建时间 ： 2016年12月27日
 * @version 1.0
 */
public abstract class AbsFieldTranslate {
	
	/**
	 * 子类中自己做局部缓存
	 */
	protected Map<String,Map<Object,Object>> cacheMap =new HashMap<String,Map<Object,Object>>();
	
	protected void initTranslateMap(){
		
	}
	
	/**
	 * 追加数据信息 
	 * @param dataMap 数据信息
	 */
	protected  abstract  void appendTranslateData(Map<String,Object> dataMap);
	/**
	 * 判断是否翻译
	 * @return TRUE OR FALSE
	 */
	protected  abstract  boolean isTranslate();
	/**
	 * 获取追加翻译字段的标识
	 * @param col
	 * @return
	 */
	protected  String findAppendMarkCol(String col){
		return TranslateUtil.TRANSLATE_MARK+col;
	}
	
	/**
	 * 构建数据
	 * @param map
	 */
	public void bulidFiledDataMap(Map<String,Object> map){
		
		if(isTranslate()){
			
			appendTranslateData(map);
			
		}
		
	}
	
	
}