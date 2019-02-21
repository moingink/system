package com.yonyou.util.page;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.yonyou.business.DataSourceUtil;
import com.yonyou.util.BussnissException;
import com.yonyou.util.page.proxy.PageBulidApi;
import com.yonyou.util.page.proxy.PageBulidHtmlAbs;
import com.yonyou.util.page.proxy.bulid.PageBulidHtmlByIndUpd;
import com.yonyou.util.page.proxy.bulid.PageBulidHtmlByInsOrUp;
import com.yonyou.util.page.proxy.bulid.PageBulidHtmlByInsOrUpText;
import com.yonyou.util.page.proxy.bulid.PageBulidHtmlByList;
import com.yonyou.util.page.proxy.bulid.PageBulidHtmlBySel;
import com.yonyou.util.page.proxy.impl.PageBulidByMeta;
import com.yonyou.util.page.proxy.impl.PageBulidForProxy;
import com.yonyou.util.page.util.SingleQueryFrameUtil;

/**
 * 页面工具类
 * @author moing_ink
 * <p>创建时间 ： 2016年12月27日
 * @version 1.0
 */
public class PageUtil {
	
	/**
	 * 新增修改页面标示
	 */
	public static final String PAGE_TYPE_FOR_INSERT_UPDATE ="INSUP";
	
	/**
	 * 独立修改页面标示
	 */
	public static final String PAGE_TYPE_FOR_INDEPENDENT_UPDATE ="INDUPD";
	
	/**
	 * 新增修改查看页面
	 */
	public static String PAGE_TYPE_FOR_INSERT_UPDATE_TEXT ="INSUPTEXT";
	/**
	 * 查询页面标示
	 */
	public static final String PAGE_TYPE_FOR_SELECT ="SELECT";
	
	/**
	 * 查询列表标示
	 */
	public static final String PAGE_TYPE_FOR_TABLE ="TABLE";
	
	/**
	 * 查询公共标示
	 */
	public static final String PAGE_TYPE_FOR_UTIL ="UTIL";
	
	public static final String BULID_PROXY ="PROXY";
	
	public static final String BULID_META ="META";
	
	/**
	 * 页面信息
	 */
	private static Map<String,PageAbs> pageMap =new HashMap<String,PageAbs>();
	
	private static Map<String,PageBulidApi> pageBulidMap =new HashMap<String,PageBulidApi>();
	
	private static Map<String,PageBulidHtmlAbs> pageBulidHtmlMap =new HashMap<String,PageBulidHtmlAbs>();
	
	private static Map<String,String> pageTypeToDataSourceColumnsMap =new HashMap<String,String>();
	/**
	 * 初始化
	 */
	static{
		
		pageMap.put(PAGE_TYPE_FOR_INSERT_UPDATE, new PageForInsUp());
		pageMap.put(PAGE_TYPE_FOR_INDEPENDENT_UPDATE, new PageForIndUpd());
		pageMap.put(PAGE_TYPE_FOR_INSERT_UPDATE_TEXT, new PageForInsUpTest());
		pageMap.put(PAGE_TYPE_FOR_SELECT, new PageForSelect());
		pageBulidMap.put(BULID_PROXY, new PageBulidForProxy());
		pageBulidMap.put(BULID_META, new PageBulidByMeta());
		pageTypeToDataSourceColumnsMap.put(PAGE_TYPE_FOR_TABLE, DataSourceUtil.DATASOURCE_DISPLAY_FIELD);
		
		pageBulidHtmlMap.put(PAGE_TYPE_FOR_INSERT_UPDATE, new PageBulidHtmlByInsOrUp());
		pageBulidHtmlMap.put(PAGE_TYPE_FOR_INDEPENDENT_UPDATE, new PageBulidHtmlByIndUpd());
		pageBulidHtmlMap.put(PAGE_TYPE_FOR_TABLE, new PageBulidHtmlByList());
		pageBulidHtmlMap.put(PAGE_TYPE_FOR_SELECT, new PageBulidHtmlBySel());
		pageBulidHtmlMap.put(PAGE_TYPE_FOR_INSERT_UPDATE_TEXT, new PageBulidHtmlByInsOrUpText());
	}
	
	
	/**
	 * 获取构建HTML类
	 * @param dataSourceCode
	 * @param pageType
	 * @return
	 */
	private static PageBulidHtmlAbs findBulidHtmlClass(String dataSourceCode,String pageType){
		PageBulidHtmlAbs bulidHtml =null;
		pageType=pageType.toUpperCase();
		if(pageBulidHtmlMap.containsKey(pageType)){
			bulidHtml=pageBulidHtmlMap.get(pageType);
		}else{
			bulidHtml=pageBulidHtmlMap.get(PAGE_TYPE_FOR_INSERT_UPDATE);
		}
		return bulidHtml;
	}
	
	/**
	 * 获取页面处理类
	 * @param pageType
	 * @return
	 * @throws BussnissException 
	 */
	public static String findPageHtml(String dataSourceCode,String pageType,Map<String,Object> dataMap) throws BussnissException{
		return findBulidHtmlClass(dataSourceCode,pageType).findPageHtml(dataSourceCode, dataMap);
	}
	
	
	public static String findPageHtml(String dataSourceCode,String pageType) throws BussnissException{
		
		return findPageHtml( dataSourceCode, pageType, null);
	}
	/**
	 * 获取类型元数据信息
	 * @param dataSourceCode
	 * @param pageType
	 * @return
	 * @throws BussnissException
	 */
	public static Map<String,ConcurrentHashMap<String, String>> findFiledData(String dataSourceCode,String pageType) throws BussnissException{
		return findBulidHtmlClass(dataSourceCode,pageType).findData(dataSourceCode);
	}
	
	
	/**
	 * 获取页面处理类
	 * @param pageType
	 * @return
	 */
	public static PageAbs findPageClass(String pageType){
		
		pageType=pageType.toUpperCase();
		if(pageMap.containsKey(pageType)){
			return pageMap.get(pageType);
		}else{
			return pageMap.get(PAGE_TYPE_FOR_INSERT_UPDATE);
		}
	}
	
	/**
	 * 获取页面处理类
	 * @param pageType
	 * @return
	 */
	public static PageBulidApi findPageBulidClass(String pageType){
		
		pageType=pageType.toUpperCase();
		if(pageBulidMap.containsKey(pageType)){
			return pageBulidMap.get(pageType);
		}else{
			return pageBulidMap.get(BULID_META);
		}
	}
	
	public static  String findShowFiled(String showFiled){
		String filedColumn= showFiled.toUpperCase();
		if (filedColumn.startsWith("TNAME") && filedColumn.endsWith(")")) {
			//函数有无参数影响取字段名逻辑
			int endIndex = filedColumn.length() - 1;
			if(filedColumn.contains(",")){
				endIndex = filedColumn.split(",")[0].length();
			}
			filedColumn = filedColumn.substring("TNAME(".length(), endIndex);
		}
		return filedColumn;
	}
	
	/**
	 * 根据INPUT类型构建页面公共HTML
	 * @param message 信息
	 * @param inputType input类型
	 */
	public static void bulidPublicHtmlByInputType(Map<String,String> message,String inputType){
		
		if(message!=null){
			if(!message.containsKey(inputType)){
				StringBuffer appendPublicMessage =new StringBuffer();
				if(inputType.equals(SingleQueryFrameUtil.TYPE_REFERENCE)){
					
//					appendPublicMessage.append("<div class='modal fade' id='ReferenceModal'>")
//								.append("<div class='modal-dialog'  style='width: 80%;height: 80%'>")
//								.append("<div class='modal-content' >")
//								.append("<div class='modal-header'>")
//										.append("<button type='button' class='close' data-dismiss='modal' aria-hidden='true'>")
//										.append("×")
//										.append("</button>")
//										.append("<h4 class='modal-title' id='ReferenceModalLabel'>参照</h4>")
//										.append("</div>")
//										.append("<div id='ReferenceModal_body' class='modal-body' style='height: auto' ></div>")
//										.append("<div class='modal-footer'>")
//										.append("<button type='button' class='btn btn-inverse ' data-dismiss='modal'>")
//												.append("关  闭")
//										.append("</button>")
//										.append("</div>")
//										.append("</div>")
//								.append("</div>")
//								.append("</div>");
//					
//					appendPublicMessage.append("<input type='text' id='ReferenceDataSourceCode' />")
//									   .append("<input type='text' id='ReferenceMapping' value='' />");
					
//					 appendPublicMessage.append("<script>")
//									    .append("function reference_remote(u,mapping,isRadio){")
//												.append("url = '").append(PAGE_URL).append("referencePage.html?dataSourceCode='+u+'&isRadio='+isRadio+'&t=' + Math.random(1000);")
//												.append("$('#ReferenceDataSourceCode').val(u);")
//												.append("$('#ReferenceMapping').val(mapping);")
//												.append("$.get(url, '', function(data){")
//												.append("$('#ReferenceModal .modal-body').html(data);")
//												.append("})")
//												.append("$('#ReferenceModal').modal({show:true,backdrop:true})")
//										.append("}")
//										.append("function reference_removeVal(id){")
//												.append("$('#'+id).val('');")
//										.append("}")
//										.append("$('#ReferenceModal').on('hidden', function() {")
//												.append("$(this).removeData('modal');")
//										.append("});")
//										.append("</script>");
					 message.put(inputType, appendPublicMessage.toString());
				}
				
			}
		}
		
	}
}
