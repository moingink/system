package com.yonyou.util.translate.imp;

import java.util.List;
import java.util.Map;
import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.translate.AbsFieldTranslate;

/**
 *	客户计划拜访翻译
 * @author moing_ink
 * <p>创建时间 ： 2016年12月27日
 * @version 1.0
 */
public class CusPlanVisitTraImp extends AbsFieldTranslate {
	

	@Override
	protected boolean isTranslate() {
		// TODO 自动生成的方法存根
		return true;
	}

	@Override
	protected void appendTranslateData(Map<String, Object> dataMap) {
		boolean flag = dataMap.containsKey("COUNT_LENGTH");
		if(flag == false){
			//操作按钮初始化
			Object executionStatus = dataMap.get("EXECUTION_STATUS");
			Object id = dataMap.get("ID");
			dataMap.put("OPERATION", this.findVRuleType(executionStatus,id));
			//执行状态变颜色
			dataMap.put("VEXECUTION_STATUS", this.findVRuleType1(executionStatus));
			Object planId = dataMap.get("PLAN_ID");
			dataMap.put("PLAN_NAME", this.findVRuleType2(planId));
		}
	}

	/**
	 * 操作按钮初始化
	 * @param val
	 * @return
	 */
	private Object findVRuleType(Object val,Object id){
		Object returnVal = "";
		if(val != null){
			if(val.toString().equals("0")){//未执行
				returnVal += "<input onclick='xz(\""+id+"\");' value='新增' type='button' class='btn btn-primary' />";
			}else if(val.toString().equals("1")){//已执行	
				returnVal += "<input onclick='xg(\""+id+"\");' value='修改' type='button' class='btn btn-warning' />&nbsp;&nbsp;<input onclick='ck(\""+id+"\");' value='查看' type='button' class='btn btn-default' />";
			}
		}
		return returnVal;
	}
	
	/**
	 * 变更所处阶段
	 * @param val
	 * @return
	 */
	private Object findVRuleType1(Object val){
		Object returnVal = "";
		if(val != null){
			if(val.toString().equals("0")){//未执行
				returnVal += "未执行";
			}else if(val.toString().equals("1")){//已执行
				returnVal += "已执行";
			}
		}
		return returnVal;
	}
	
	/**
	 * 变更所处阶段
	 * @param val
	 * @return
	 */
	private Object findVRuleType2(Object val){
		Object returnVal = "";
		if(val != null){
			String sql = "select * from vis_plan where id = "+"\""+val.toString()+"\"";
			List<Map<String, Object>> listMap =BaseDao.getBaseDao().query(sql,"");
			if(listMap.size()>0){
				if(listMap.get(0).get("PLAN_NAME")!=null && listMap.get(0).get("PLAN_NAME")!=""){
					returnVal = listMap.get(0).get("PLAN_NAME").toString();
				}
			}
		}
		return returnVal;
	}

}