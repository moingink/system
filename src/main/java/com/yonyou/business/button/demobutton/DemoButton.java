package com.yonyou.business.button.demobutton;

import java.text.SimpleDateFormat;

import com.yonyou.business.button.ButtonAbs;
//RaAdDailyData
public abstract class DemoButton extends ButtonAbs{
	
	/** 日期格式转换*/
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	//yyyy-MM-dd HH:mm:ss
	
	/**获取日期格式转换类
	 * @return 日期格式转换类
	 */
	public SimpleDateFormat getSdf() {
		return sdf;
	}
}
