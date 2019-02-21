package com.yonyou.util;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProjectUtils {
	
	/**
	 * 
	 * @param startDate 开始时间
	 * @param expectedDate 预计结束时间
	 * @param actualDate 实际完成时间
	 * @param currentDate 当前时间
	 * @param workDate 工时
	 * @return
	 */
	public static final Map<String,Object> calDate(Date startDate,Date expectedDate,Date actualDate,Date currentDate,String workDate){
		
		Map<String,Object> map = new HashMap<>();
		if(actualDate!=null){
			map.put("speed", "100");
			if(actualDate.after(expectedDate)){
				//double differDays = calLeaveDays(startDate, actualDate);
				map.put("state", "延期");
				map.put("workingHours", workDate);
				//map.put("workingHours", differDays);
			}else{
				map.put("state", "正常");
				map.put("workingHours", workDate);
				//double differDays = calLeaveDays(startDate, actualDate);
			}
		}else{
			if (currentDate.before(startDate)){
				map.put("speed", "0");
				map.put("state", "正常");
				map.put("workingHours", workDate);
			} else if (currentDate.after(expectedDate)) {
				map.put("speed", "100");
				map.put("state", "延期");
				map.put("workingHours", workDate);
				// map.put("workingHours", calLeaveDays(startDate,currentDate));
			} else {
				double differDays = calLeaveDays(startDate, currentDate);
				if (differDays > Double.valueOf(workDate)) {
					differDays = Double.valueOf(workDate);
					map.put("state", "延期");
					map.put("workingHours", workDate);
					// map.put("workingHours",differDays);
				} else {
					map.put("state", "正常");
					map.put("workingHours", workDate);
				}
				long speed = Math.round(differDays / Double.valueOf(workDate)* 100);
				map.put("speed", speed);
			}
		}
		return map;
	}
	
	public static double calLeaveDays(Date startTime,Date endTime){
		double leaveDays = 0;
		//从startTime开始循环，若该日期不是节假日或者不是周六日则请假天数+1
		Date flag = startTime;//设置循环开始日期
		Calendar cal = Calendar.getInstance();
		//循环遍历每个日期
		while(flag.compareTo(endTime)!=1){
		    cal.setTime(flag);
		    //判断是否为周六日
		    int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
		    if(week == 0 || week == 6){//0为周日，6为周六
		        //跳出循环进入下一个日期
		        cal.add(Calendar.DAY_OF_MONTH, +1);
		        flag = cal.getTime();
		        continue;
		    }
		    //不是节假日或者周末，天数+1
		    leaveDays = leaveDays + 1;
		    //日期往后加一天
		   	cal.add(Calendar.DAY_OF_MONTH, +1);
		   	flag = cal.getTime();
		}
		return leaveDays;
		
	}
	
}
