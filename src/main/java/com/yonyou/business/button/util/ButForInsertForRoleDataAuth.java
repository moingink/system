package com.yonyou.business.button.util;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.yonyou.business.button.ButtonAbs;
import com.yonyou.business.entity.TokenEntity;
import com.yonyou.business.entity.TokenUtil;
import com.yonyou.util.BussnissException;
import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SqlWhereEntity;
import com.yonyou.util.sql.SQLUtil.WhereEnum;

public class ButForInsertForRoleDataAuth extends ButtonAbs{

	@Override
	protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) throws IOException, BussnissException {
		
		String tabName = request.getParameter("table");
		JSONObject jsonObject = JSONObject.parseObject(request.getParameter("jsonData"));
		try {
			//确认新增还是修改 如果roleid已存在则更新  2 判断是用户新增还是修改
			String roleIdString = jsonObject.getString("RM_ROLE_ID");
			String userIdString = jsonObject.getString("RM_USER_ID");
			String sqlString = " select * from rm_role_auth where dr=0 and rm_role_id =' "+roleIdString+"'";
			List<Map<String, Object>> list = BaseDao.getBaseDao().query(sqlString, "");
			String userSqlString = " select * from rm_role_auth_user where dr=0 and rm_user_id =' "+userIdString+"'";
			List<Map<String, Object>> userList = BaseDao.getBaseDao().query(userSqlString, "");
			//TokenEntity tokenEntity = TokenUtil.initTokenEntity(request);
			//如果存在roid则是修改  
			SqlWhereEntity whereEntity =new SqlWhereEntity();
			if(list!=null &&list.size()>0){
				//第二次查询用户是否存在
				Map json = jsonObject;
				if(!userIdString.equals("") &&!roleIdString.equals("")){
					//更新字表用户信息 COMPANY  
					if(null!=jsonObject.getString("GOLBAL")&&jsonObject.getString("GOLBAL").equals("0")){
						jsonObject.remove("RM_USER_ID");
						json = jsonObject;
						whereEntity.putWhere("RM_ROLE_ID", roleIdString, WhereEnum.EQUAL_INT);
						dcmsDAO.update("rm_role_auth", json, whereEntity);
					}else if(null!=userList && userList.size()>0){
						whereEntity.putWhere("RM_USER_ID", userIdString, WhereEnum.EQUAL_INT);
						if(jsonObject.getString("PERSONAL").equals("0")){
							jsonObject.put("USER_PERSONAL", "0");
						}else{
							jsonObject.put("USER_PERSONAL", "1");
						}
						if(jsonObject.getString("COMPANY").equals("0")){
							jsonObject.put("USER_COMPANY", "0");
						}else{
							jsonObject.put("USER_COMPANY", "1");
						}
						jsonObject.remove("COMPANY");
						jsonObject.remove("GOLBAL");
						jsonObject.remove("PERSONAL");
						json = jsonObject;
						dcmsDAO.update("rm_role_auth_user", json, whereEntity);
					}else{
						jsonObject.remove("COMPANY");
						jsonObject.remove("GOLBAL");
						jsonObject.remove("PERSONAL");
						json = jsonObject;
						dcmsDAO.insertByTransfrom("RM_ROLE_AUTH_USER", json);
					}
				}else{
					//更新主表 
					jsonObject.remove("RM_USER_ID");
					json = jsonObject;
					whereEntity.putWhere("RM_ROLE_ID", roleIdString, WhereEnum.EQUAL_INT);
					dcmsDAO.update(tabName, json, whereEntity);
				}
			}else{
				//新增主表和子表 
				Map json = jsonObject;
				this.appendData(json,request);
				dcmsDAO.insertByTransfrom(tabName, json);
				/*if(null != userIdString &&!userIdString.equals("")){
					dcmsDAO.insertByTransfrom("rm_role_auth_user", json);
				}*/
			}
		} catch (BussnissException e) {
			e.printStackTrace();
		}
		String jsonMessage = "{\"message\":\"保存成功\"}";
		this.ajaxWrite(jsonMessage, request, response);
		return null;
	}

	@Override
	protected boolean befortOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	protected void afterOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO 自动生成的方法存根
		
	}
	
	@Override
	protected void appendData(Map<String, String> dataMap,
			HttpServletRequest request) {
		// TODO 自动生成的方法存根
		if(!TOKEN_ENTITY.isEmpty()){
			//制单人
			dataMap.put(IPublicBusColumn.CREATOR_ID, TOKEN_ENTITY.USER.getId());
			dataMap.put(IPublicBusColumn.CREATOR_NAME, TOKEN_ENTITY.USER.getName());
			//部门信息
			dataMap.put(IPublicBusColumn.COMPANY_ID, TOKEN_ENTITY.COMPANY.getCompany_id());
			dataMap.put(IPublicBusColumn.COMPANY_NAME, TOKEN_ENTITY.COMPANY.getCompany_name());
			
			dataMap.put(IPublicBusColumn.BILL_STATUS, "0");
		}
	}
	public String calLeaveDays(Date startTime,Date endTime){
		Double leaveDays = 0.0;
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
		return leaveDays.toString();
	}

}
