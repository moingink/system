package com.yonyou.util.translate.imp;

import java.util.Map;
import com.yonyou.util.translate.AbsFieldTranslate;

/**
 *	合同关联信息录入
 * @author moing_ink
 * <p>创建时间 ： 2016年12月27日
 * @version 1.0
 */
public class NavigationTraImp extends AbsFieldTranslate {
	

	@Override
	protected boolean isTranslate() {
		// TODO 自动生成的方法存根
		return true;
	}

	@Override
	protected void appendTranslateData(Map<String, Object> dataMap) {
		boolean flag = dataMap.containsKey("COUNT_LENGTH");
		if(flag == false){
			Object recordState = dataMap.get("RECORD_STATE");//关联信息录入状态
			Object recordState1 = dataMap.get("PLACE_FILE_STATE");//关联信息录入状态
			//添加操作按钮
			dataMap.put("RECORD_STATE", this.findVRuleType(recordState));
			dataMap.put("PLACE_FILE_STATE", this.findVRuleType(recordState1));
		}
	}

	/**
	 * 补录状态
	 * @param val
	 * @return
	 */
	private Object findVRuleType(Object recordState){
		Object returnVal = "";
		if(recordState != null){
			if(recordState.equals("0") || recordState.equals("未归档")){//已补录
				if(recordState.equals("0")){
					returnVal = "<span style='color: red;'>未补录</span>";
				}
				else{
					returnVal = "<span style='color: red;'>未归档</span>";
				}
				
			}
			else   if(recordState.equals("1") || recordState.equals("已归档")){
				if(recordState.equals("1")){
					returnVal = "<span>已补录</span>";
				}
				else{
					returnVal = "<span>已归档</span>";
				}
			}
		}
		return returnVal;
	}

}