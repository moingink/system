package com.yonyou.util.page.select;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

import com.yonyou.business.RmDictReferenceUtil;
import com.yonyou.util.BussnissException;
import com.yonyou.util.page.SingleQueryFrameAbs;
/**
 * 下拉框生成类
 * @author moing_ink
 * <p>创建时间 ： 2016年12月27日
 * @version 1.0
 */
@SuppressWarnings("all")
public class SingleQueryFrameForSelect extends SingleQueryFrameAbs{

	@Override
	public String findSingleFrame(String id, String name, String value,Map<String,String> message) {
		
		StringBuffer html =new StringBuffer();
		
		html.append("<select class='").append(this.findClass()).append("'")
			.append(" id='").append(id).append("'").append(" name='").append(id).append("'")
			.append(" >")
			.append(this.findDefaultSelect());
		
		String input_formart =message.get("input_formart");
		ConcurrentHashMap<String, Object> selectMap;
		try {
			selectMap = RmDictReferenceUtil.getDictReferenceAll(input_formart);
			if(selectMap!=null&&selectMap.size()>0){
				for (Entry<String, Object> entry : selectMap.entrySet()) {
					String tempKey = entry.getKey();
					Map<String, Object> tempMap = (Map<String, Object>) selectMap.get(tempKey);
					String tempValue = (String) tempMap.get("DATA_VALUE");
					String tempRemark = (String) tempMap.get("REMARK");
					html.append("<option value='" + tempKey + "' ");
					if(tempKey.equalsIgnoreCase(value)){
						html.append(" selected = 'selected' ");
					}
					if(StringUtils.isNoneBlank(tempRemark)){
						html.append("title = '"+tempRemark+"' ");
					}
					html.append(">" + tempValue + "</option>");
				}
			}
		} catch (BussnissException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		html.append("</select>");
		// TODO 自动生成的方法存根
		return html.toString();
	}
	/**
	 * 获取样式
	 * @return
	 */
	public String findClass(){
		return "form-control";
	}
	/**
	 * 获取默认信息
	 * @return
	 */
	public String findDefaultSelect(){
		return "<option  selected = 'selected' value=''>==请选择==</option>";
	}


}
