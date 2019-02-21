package com.yonyou.util.translate.imp;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.translate.AbsFieldTranslate;

/**
 *	确认收入汇总表反应
 * @version 1.0
 */
public class CollReportTraImp extends AbsFieldTranslate {
	

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
			//添加操作按钮
			dataMap.put("CONFIRM_MONTH", this.findVRuleType(id));
		}
	}

	/**
	 * 实际发生月份
	 * @param val
	 * @return
	 */
	private Object findVRuleType(Object id){
		String idString = id!=null?id.toString():null;
		String happen_dateStr="";
		if(StringUtils.isNotBlank(idString)){
			String sqlString="SELECT HAPPEN_DATE,CHARGE FROM FIN_AUDIT_CHARGE_DETAIL_HAPPEN_DATE WHERE parent_id = '"+idString+"'";	 
			List<Map<String, Object>> list = BaseDao.getBaseDao().query(sqlString.toLowerCase(),"");
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				happen_dateStr+=map.get("HAPPEN_DATE");
				happen_dateStr+=":";
				happen_dateStr+=map.get("CHARGE");
				if(i < list.size()-1)happen_dateStr+=";";
			}
		}
		return happen_dateStr;
	}

}