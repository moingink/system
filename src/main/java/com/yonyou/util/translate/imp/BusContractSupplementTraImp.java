package com.yonyou.util.translate.imp;

import java.util.Map;
import com.yonyou.util.translate.AbsFieldTranslate;

/**
 *	合同关联信息录入
 * @author moing_ink
 * <p>创建时间 ： 2016年12月27日
 * @version 1.0
 */
public class BusContractSupplementTraImp extends AbsFieldTranslate {
	

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
			//添加操作按钮
			dataMap.put("RECORD_STATE", this.findVRuleType(recordState));
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
			if(recordState.equals("1")){//已补录
				returnVal = "<span>已补录</span>";
			}else if(recordState.equals("0")){//未补录
				returnVal = "<span style='color: red;'>未补录</span>";
			}
		}
		return returnVal;
	}

}