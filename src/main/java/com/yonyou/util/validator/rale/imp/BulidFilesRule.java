package com.yonyou.util.validator.rale.imp;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.yonyou.util.page.util.SingleQueryFrameUtil;
import com.yonyou.util.sql.MetaCodeMappingType;
import com.yonyou.util.validator.mark.BulidValidMarkAbs;
import com.yonyou.util.validator.mark.IBulidValidMark;
import com.yonyou.util.validator.rale.IBulidFilesRule;
import com.yonyou.util.validator.type.BulidTypeEnum;
import com.yonyou.util.validator.util.BulidValidUtil;

public class BulidFilesRule implements IBulidFilesRule{
	
	
	@Override 
	public Map<String,JSONObject> queryFiledsJsons(BulidValidMarkAbs _bulidMark, Map<String, String> values) {
		// TODO 自动生成的方法存根
		String FIELD_TYPE =BulidValidUtil.findValue("FIELD_TYPE", values);
		String NULL_FLAG=BulidValidUtil.findValue("NULL_FLAG", values);
		String FIELD_LENGTH=BulidValidUtil.findValue("FIELD_LENGTH", values);
		String INPUT_HTML =BulidValidUtil.findValue("INPUT_HTML", values);
		String INPUT_TYPE =BulidValidUtil.findValue("INPUT_TYPE", values);
		//转换类型
		//FIELD_TYPE=MetaCodeMappingType.mappingType(FIELD_TYPE);
		return this.bulidFiledsList(this.bulidRuleList(FIELD_TYPE,INPUT_TYPE, NULL_FLAG, FIELD_LENGTH), _bulidMark, INPUT_HTML, values);
	}
	
	/**
	 * 获取字段json信息
	 * @param list
	 * @param _bulidMark
	 * @param INPUT_HTML
	 * @param values
	 * @return
	 */
	protected Map<String,JSONObject> bulidFiledsList (List<String> list,BulidValidMarkAbs _bulidMark,String INPUT_HTML,Map<String, String> values){
		Map<String,JSONObject> typeMap =new LinkedHashMap<String,JSONObject>();
		for(String type:list){
			JSONObject typeObj =_bulidMark.findJson(BulidTypeEnum.FILED, type, values);
			
			switch(type){
				
				case IBulidValidMark.DECIMAL:
					this.appendInputType(typeMap, _bulidMark.findAnalysisType(type,values), _bulidMark, values);
					break;
				default: 
					if(!typeObj.isNullObject()){
						typeMap.put(type, _bulidMark.findJson(BulidTypeEnum.FILED, type, values));
					}
					break;
			}
			
			
		}
		this.appendInputType(typeMap, INPUT_HTML, _bulidMark, values);
		return typeMap;
	}
	
	/**
	 * 构建功能类型信息
	 * @param FIELD_TYPE
	 * @param NULL_FLAG
	 * @param FIELD_LENGTH
	 * @return
	 */
	protected List<String> bulidRuleList(String FIELD_TYPE,String INPUT_TYPE,String NULL_FLAG,String FIELD_LENGTH){
		
		List<String> rulelist =new ArrayList<String>();
		if(!INPUT_TYPE.equals(SingleQueryFrameUtil.TYPE_AFFIX)){
			if("1".equals(NULL_FLAG)){
				rulelist.add(IBulidValidMark.NOTEMPTY);
			}
			
			switch(FIELD_TYPE){
				
				case "NUMBER":
				case "BIGINT":
					if(FIELD_LENGTH.indexOf(",")!=-1){
						rulelist.add(IBulidValidMark.DECIMAL); break;
					}else{
						rulelist.add(IBulidValidMark.DIGITS);
					}
					break;
				case "DECIMAL":
					rulelist.add(IBulidValidMark.DECIMAL); break;
				case "CHAR" :
				case "VARCHAR" :
				case "VARCHAR2" :
					if(FIELD_LENGTH.length()>0){
						rulelist.add(IBulidValidMark.STRINGLENGTH);
					}
					break;
			}
		}
		return rulelist ;
	}
	
	/**
	 * 追加页面输入规则
	 * @param filedObj
	 * @param INPUT_HTML
	 */
	protected void appendInputType(Map<String,JSONObject> typeMap,String INPUT_HTML,BulidValidMarkAbs _bulidMark,Map<String, String> values){
		
		Map<String,String []> htmlFuns=BulidValidUtil.analysisPageFun(INPUT_HTML, values);
		this.appendInputType(typeMap, htmlFuns, _bulidMark, values);
	}
	
	/**
	 * 追加页面输入规则
	 * @param filedObj
	 * @param INPUT_HTML
	 */
	protected void appendInputType(Map<String,JSONObject> typeMap,Map<String,String []> analysis,BulidValidMarkAbs _bulidMark,Map<String, String> values){
		
		Map<String,String []> htmlFuns=analysis;
		for(String type:htmlFuns.keySet()){
			JSONObject typeObj =_bulidMark.findJson(BulidTypeEnum.FILED, type, values);
			if(typeObj!=null&&!typeObj.isNullObject()&&typeObj.size()>0){
				int i=0;
				String [] messages=htmlFuns.get(type);
				if(messages!=null&&messages.length>0){
					for(Object key:typeObj.keySet()){
						if(i<messages.length&&messages[i]!=null&&messages[i].trim().length()>0){
							typeObj.put(key, messages[i]);
						}
						i++;
					}
				}
				
			}
			typeMap.put(type, typeObj);
		}
	}
}
