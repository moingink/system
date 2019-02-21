package com.yonyou.util.translate.imp;

import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import com.yonyou.util.translate.AbsFieldTranslate;

/**
 *	公章用印申请翻译
 * @author moing_ink
 * <p>创建时间 ： 2016年12月27日
 * @version 1.0
 */
public class CachetUseSealApplyTraImp extends AbsFieldTranslate {
	

	@Override
	protected boolean isTranslate() {
		// TODO 自动生成的方法存根
		return true;
	}

	@Override
	protected void appendTranslateData(Map<String, Object> dataMap) {
		boolean flag = dataMap.containsKey("COUNT_LENGTH");
		if(flag == false){
			Object id = dataMap.get("ID");//ID
			Object isLendOut = dataMap.get("IS_LEND_OUT");//是否外借
			Object billStatus = dataMap.get("BILL_STATUS");//审批状态
			Object processState = dataMap.get("PROCESS_STATE");//流程状态
			//添加操作按钮
			dataMap.put("OPERATION", this.findVRuleType(id, isLendOut, billStatus, processState));
		}
	}

	/**
	 * 操作按钮初始化
	 * @param val
	 * @return
	 */
	private Object findVRuleType(Object id, Object isLendOut, Object billStatus, Object processState){
		Object returnVal = "";
		String billStatusString = billStatus!=null?billStatus.toString():"";
		String processStateString = processState!=null?processState.toString():"";
		if(isLendOut.toString().equals("0")){//否
			if(billStatusString.equals("3")){
				if(StringUtils.isBlank(processStateString)){
					returnVal += "<input id='queren' onclick='qr(\""+id+"\");' value='确认' type='button' class='btn btn-success' style='margin-left: 20px;'/>";
				}
			}
		}else if(isLendOut.toString().equals("1")){//是
			if(billStatusString.equals("7")){//驳回
				if(StringUtils.isBlank(processStateString)){
					returnVal += "<input id='queren' onclick='qr(\""+id+"\");' value='确认' type='button' class='btn btn-success' style='margin-left: 20px;'/>";
				}
			}
		}
		returnVal += "<input onclick='ck(\""+id+"\");' value='查看' type='button' class='btn btn-info' />";
		return returnVal;
	}

}