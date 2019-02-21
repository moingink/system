package com.yonyou.util.validator.bulid.imp;

import net.sf.json.JSONObject;

import com.yonyou.util.validator.bulid.IBulidValidHead;
import com.yonyou.util.validator.mark.BulidValidMarkAbs;
import com.yonyou.util.validator.mark.IBulidValidMark;
import com.yonyou.util.validator.type.BulidContainerEnum;
import com.yonyou.util.validator.type.BulidTypeEnum;

public class BulidValidHead implements IBulidValidHead{

	@Override
	public void appendJson(JSONObject obj, BulidValidMarkAbs bulidMark) {
		// TODO 自动生成的方法存根
		this.appendDefMessage(obj, bulidMark);
		this.appendContainer(obj, bulidMark);
		this.appendFeedbackIcons(obj, bulidMark);
	}
	
	/**
	 * 添加默认提示信息
	 * @param obj
	 * @param bulidMark
	 */
	protected void appendDefMessage(JSONObject obj, BulidValidMarkAbs bulidMark){
		obj.put(IBulidValidMark.MESSAGE, "输入信息有误！");
	}
	/**
	 * 添加容器信息
	 * @param obj
	 * @param bulidMark
	 */
	protected void appendContainer(JSONObject obj, BulidValidMarkAbs bulidMark){
		obj.put(IBulidValidMark.CONTAINER, BulidContainerEnum.DIV.getType());
	}
	/**
	 * 添加图标信息
	 * @param obj
	 * @param bulidMark
	 */
	protected void appendFeedbackIcons(JSONObject obj, BulidValidMarkAbs bulidMark){
		obj.put(IBulidValidMark.FEEDBACKICONS, bulidMark.findJson(BulidTypeEnum.HEAD, IBulidValidMark.FEEDBACKICONS, null));
	}
	
	protected void appendExcluded(JSONObject obj, BulidValidMarkAbs bulidMark){
		obj.put(IBulidValidMark.EXCLUDED, bulidMark.findJson(BulidTypeEnum.HEAD, IBulidValidMark.EXCLUDED, null));
	}
}
