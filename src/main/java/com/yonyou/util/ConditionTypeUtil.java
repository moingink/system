package com.yonyou.util;


import java.util.HashMap;

import com.yonyou.util.page.util.SingleQueryFrameUtil;
import com.yonyou.util.sql.SQLUtil.WhereEnum;

public class ConditionTypeUtil {

	/**
	 * 控制类型枚举类
	 * 
	 * @ClassName ConditionEnum
	 * @author 博超
	 * @date 2017年3月6日
	 */
	public static enum ConditionEnum {

		/**
		 * 和数据字典CONDITION_TYPE一一对应
		 */
		/** 小于 */
		LESS_THAN("0",WhereEnum.LESS_THAN),
		/** 等于 用于字符串 */
		EQUAL_STRING("1",WhereEnum.EQUAL_STRING),
		/** 等于 用于数值 */
		EQUAL_INT("2",WhereEnum.EQUAL_INT),
		/** 不等于 用于字符串 */
		NOT_EQUAL_STRING("3",WhereEnum.NOT_EQUAL_STRING),
		/** 不等于 用于数值 */
		NOT_EQUAL_INT("4",WhereEnum.NOT_EQUAL_INT),
		/** 大于 */
		GREATER_THAN("5",WhereEnum.GREATER_THAN),
		/** 前后匹配 */
		ALL_LIKE("6",WhereEnum.ALL_LIKE),
		/** 前匹配 */
		FRONT_LIKE("7",WhereEnum.FRONT_LIKE),
		/** 后匹配 */
		BACK_LIKE("8",WhereEnum.BACK_LIKE),
		/** IN */
		IN("9",WhereEnum.IN),
		/** IS NULL */
		IS_NULL("10",WhereEnum.IS_NULL),
		/** IS NOT NULL */
		IS_NOT_NULL("11",WhereEnum.IS_NOT_NULL),
		/** 日期大于 */
		TO_DATE_LESS("12",WhereEnum.TO_DATE_LESS),
		/** 日期等于 */
		TO_DATE_EQUAL("13",WhereEnum.TO_DATE_EQUAL),
		/** 日期小于 */
		TO_DATE_GREATER("14",WhereEnum.TO_DATE_GREATER),
		/** NOT IN */
		NOT_IN("15",WhereEnum.NOT_IN);
		
		private final String code;
		private final WhereEnum whereEnum;
		
		private ConditionEnum(String _code, WhereEnum _whereEnum) {
			this.code = _code;
			this.whereEnum = _whereEnum;
		}

		public String getCode() {
			return code;
		}

		public WhereEnum getWhereEnum() {
			return whereEnum;
		}
		
	}
	
	//将ConditionEnum构造为map缓存
	public static HashMap<String, WhereEnum> ConditionMapBase = new HashMap<>();
	static {
		for (ConditionEnum itEnum : ConditionEnum.values()) {
			ConditionMapBase.put(itEnum.getCode(), itEnum.getWhereEnum());
		}
	}
	
	public static HashMap<String, WhereEnum> ConditionMapForInputType = new HashMap<>();
	static {
		ConditionMapForInputType.put(SingleQueryFrameUtil.TYPE_INPUT_TEXT, ConditionEnum.ALL_LIKE.getWhereEnum());
		ConditionMapForInputType.put(SingleQueryFrameUtil.TYPE_SELECT, ConditionEnum.EQUAL_STRING.getWhereEnum());
		ConditionMapForInputType.put(SingleQueryFrameUtil.TYPE_TEXTAREA, ConditionEnum.ALL_LIKE.getWhereEnum());
		ConditionMapForInputType.put(SingleQueryFrameUtil.TYPE_REFERENCE, ConditionEnum.IN.getWhereEnum());
		ConditionMapForInputType.put(SingleQueryFrameUtil.TYPE_DATE, ConditionEnum.TO_DATE_EQUAL.getWhereEnum());
		ConditionMapForInputType.put(SingleQueryFrameUtil.TYPE_INPUT_NUMBER, ConditionEnum.EQUAL_INT.getWhereEnum());
		ConditionMapForInputType.put(SingleQueryFrameUtil.TYPE_TEXT_HIDDEN, ConditionEnum.EQUAL_STRING.getWhereEnum());
		//日期区间做特殊处理
//		ConditionMapForInputType.put(SingleQueryFrameUtil.TYPE_DATE_SECTION, "");
	}

}
