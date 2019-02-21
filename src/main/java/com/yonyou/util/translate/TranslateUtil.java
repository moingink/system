package com.yonyou.util.translate;

import java.util.HashMap;
import java.util.Map;

import com.yonyou.util.translate.imp.DefaultTraImp;
import com.yonyou.util.translate.imp.SchemeRuleTraImp;

/**
 * 翻译的工具列
 * @author moing_ink
 * <p>创建时间 ： 2016年12月27日
 * @version 1.0
 */
public class TranslateUtil {
	
	/**
	 * 翻译数据
	 */
	private static Map<String,String> TRANSLATEMAP =new HashMap<String,String>();
	/**
	 * 翻译字段标示
	 */
	public static String  TRANSLATE_MARK="V#";
	/**
	 * 初始化
	 */
	static{
		TRANSLATEMAP.put("default", "com.yonyou.util.translate.imp.DefaultTraImp");
		TRANSLATEMAP.put("SchemeRuleTraImp", "com.yonyou.util.translate.imp.SchemeRuleTraImp");
		TRANSLATEMAP.put("CheckDetailTraImp", "com.yonyou.util.translate.imp.CheckDetailTraImp");
		TRANSLATEMAP.put("ProjectModifyTraImp", "com.yonyou.util.translate.imp.ProjectModifyTraImp");
		TRANSLATEMAP.put("CusPlanVisitTraImp", "com.yonyou.util.translate.imp.CusPlanVisitTraImp");
		TRANSLATEMAP.put("CachetUseSealApplyTraImp", "com.yonyou.util.translate.imp.CachetUseSealApplyTraImp");
		TRANSLATEMAP.put("CollReportTraImp", "com.yonyou.util.translate.imp.CollReportTraImp");
		TRANSLATEMAP.put("BusContractSupplementTraImp", "com.yonyou.util.translate.imp.BusContractSupplementTraImp");
		TRANSLATEMAP.put("NavigationTraImp", "com.yonyou.util.translate.imp.NavigationTraImp");
		
		
	
	}
	/**
	 * 获取翻译处理类
	 * @param filed_class
	 * @return
	 */
	public static AbsFieldTranslate findTranslateClass(String filed_class) {
		filed_class=filed_class.trim();
		String class_path="";
		if(TRANSLATEMAP.containsKey(filed_class)){
			class_path= TRANSLATEMAP.get(filed_class);
		}else{
			class_path= TRANSLATEMAP.get("default");
		}
		AbsFieldTranslate trans =null;
		try {
			trans=(AbsFieldTranslate)Class.forName(class_path).newInstance();
		} catch (InstantiationException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} finally{
			if(trans==null){
				trans =new DefaultTraImp();
			}
		}
		return trans;
	}
}
