package com.yonyou.util.page;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.yonyou.business.MetaDataUtil;
import com.yonyou.util.BussnissException;
import com.yonyou.util.page.util.SingleQueryFrameUtil;
/**
 * 页面抽象类
 * @author moing_ink
 * <p>创建时间 ： 2016年12月27日
 * @version 1.0
 */
public abstract class PageAbs {
	
	
	
	protected abstract String findPageType();
	
	protected abstract void appendParamMap(Map<String,String> paramMap);
	
	public String findHtmlUtil(String [] colArray,Map<String, ConcurrentHashMap<String, String>> filedMap, Map<String,Object> dataMap,int rows){
		Map<String,String> pageHtmlMap =new LinkedHashMap<String, String>();
		Map<String,String> publicPageMap =new HashMap<String,String>();
		for(String col:colArray){
			
			if(filedMap.containsKey(col)){
				Map<String,String> paramMap =new HashMap<String, String>();
				ConcurrentHashMap<String, String> filedColMap =filedMap.get(col);
				String input_type =filedColMap.get(MetaDataUtil.FIELD_INPUT_TYPE);
				String input_formart=filedColMap.get(MetaDataUtil.FIELD_INPUT_FORMART);
				String input_html =filedColMap.get(MetaDataUtil.FIELD_INPUT_HTML);
				String field_name =filedColMap.get(MetaDataUtil.FIELD_NAME);
				String field_code =filedColMap.get(MetaDataUtil.FIELD_CODE);
				String field_summary =filedColMap.get(MetaDataUtil.FIELD_SUMMARY);
				
				field_name=field_name.trim().equals("")?field_code:field_name;
				field_name=this.findName(field_name, filedColMap);
				input_type=this.findInputType(input_type.toUpperCase());
				String value=this.findVal(col, dataMap);
				String id =this.findId(col,input_type);
				String key=this.findPageHtmlKey(input_type, id);
				paramMap.put("input_formart", input_formart);
				paramMap.put("input_html", input_html);
				paramMap.put("page_type", this.findPageType());
				paramMap.put("summary", field_summary);
				this.appendParamMap(paramMap);
				//根据input_data  查询数据字典并获取信息
				if(this.isPutPageHtml(id, input_type, value)){
					pageHtmlMap.put(key, SingleQueryFrameUtil.findSingleQuery(input_type).findPageHtml(id, field_name, value, paramMap));
				}
				PageUtil.bulidPublicHtmlByInputType(publicPageMap, input_type);
			}
			
		}
		return appendPublicPageHtml(this.bulidPageHtml(pageHtmlMap,dataMap,rows),publicPageMap);
	}
	
	/**
	 * 获取页面HTML
	 * @param metaCode 元数据编码
	 * @param cols 列信息
	 * @param dataMap 数据信息
	 * @return 页面HTML信息
	 * @throws BussnissException
	 */
	@Deprecated
	public String findHtml(String metaCode,String cols,Map<String,Object> dataMap,int rows) throws BussnissException{
		
		String [] colArray =cols.split(findMark());
		Map<String, ConcurrentHashMap<String, String>> filedMap =MetaDataUtil.getMetaDataFields(metaCode);
		
		return findHtmlUtil(colArray,filedMap,dataMap,rows);
	}
	
	/**
	 * 获取页面HTML——页面信息经过滤器处理
	 * @param metaCode 元数据编码
	 * @param cols 列信息
	 * @param dataMap 数据信息
	 * @param queryFliter 查询过滤器名称
	 * @return 页面HTML信息
	 * @throws BussnissException
	 */
	@Deprecated
	public String findHtml(String metaCode,String cols,Map<String,Object> dataMap,String queryFliter,int rows) throws BussnissException{
		
		String [] colArray =cols.split(findMark());
		Map<String, ConcurrentHashMap<String, String>> filedMap = MetaDataUtil.getProcessedFields(metaCode, queryFliter);
		
		return findHtmlUtil(colArray,filedMap,dataMap,rows);
	}
	
	/**
	 * 构建页面HTML信息
	 * @param pageHtmlMap
	 * @return
	 */
	protected  abstract String bulidPageHtml(Map<String,String> pageHtmlMap,Map<String,Object> dataMap,int rows);
	/**
	 * 获取页面ID
	 * @param filed_code 字段编码
	 * @param input_type input类型
	 * @return
	 */
	protected  abstract String findId(String filed_code,String input_type);
	
	protected  String findName(String field_name,ConcurrentHashMap<String, String> filedColMap){
		return field_name;
	}
	/**
	 * 获取页面列内容值
	 * @param col 列
	 * @param dataMap 数据
	 * @return 页面内容值
	 */
	protected String findVal(String col,Map<String,Object> dataMap){
		String val ="";
		if(dataMap!=null){
			if(dataMap.containsKey(col)){
				if(dataMap.get(col)!=null){
					val=dataMap.get(col).toString();
				}
			}
		}
		return val;
	}
	/**
	 * 获取页面关联KEY
	 * @param input_type input类型
	 * @param id ID
	 * @return 关联KEY
	 */
	protected String findPageHtmlKey(String input_type,String id){
		return input_type+","+id;
	}
	
	/**
	 * 追加页面公共HTML信息
	 * @param html html内容
	 * @param publicPageMap 公共信息
	 * @return 页面html信息
	 */
	protected String appendPublicPageHtml(String html,Map<String,String> publicPageMap){
		String pageHtml =html;
		if(publicPageMap!=null){
			for(String key:publicPageMap.keySet()){
				pageHtml=pageHtml+publicPageMap.get(key);
			}
		}
		return pageHtml;
	}
	/**
	 * 获取字段分割标示
	 * @return
	 */
	protected String findMark(){
		return ",";
	}
	
	protected String findInputType(String inputType){
		return inputType;
	}
	
	/**
	 * 判断是否添加html信息  默认添加
	 * @param id
	 * @param input_type
	 * @param value
	 * @return
	 */
	protected boolean isPutPageHtml(String id,String input_type,String value){
		return true;
	}
	
	
	protected String appendRequiredHtml(){
		return "<font color ='red' size='3' >*&nbsp;</font>";
	}



}
