package com.yonyou.business.button.rm.user;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.util.MD5Util;
import org.util.RmPartyConstants;
import org.util.RmPartyRelationCode;
import org.util.tools.helper.RmStringHelper;
import org.util.tools.helper.SendPassword;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.yonyou.business.DataSourceUtil;
import com.yonyou.business.button.demobutton.DemoButton;
import com.yonyou.util.BussnissException;
import com.yonyou.util.JsonUtils;
import com.yonyou.util.RmIdFactory;
import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.util.sql.SqlWhereEntity;

public class UserButForResetPassword extends DemoButton {

	@Override
	protected boolean befortOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) throws IOException, BussnissException {

		// TODO 自动生成的方法存根
		String jsonMessage = "";
		String dataSourceCode =request.getParameter("dataSourceCode");
		String tabName = findTableNameByDataSourceCode(dataSourceCode);
		JSONArray jsonArray = JSONArray.fromObject(request.getParameter("jsonData"));
		System.out.println("jsonArray:"+jsonArray);
		String[] ids = new String[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jb = (JSONObject) jsonArray.get(i);
			String id = jb.getString("ID");
			ids[i] = id;
		}
		
		String password = SendPassword.getRandomPassword(8);
		if(ids.length>0){
			//rm_user
			SqlTableUtil sqlEntity = DataSourceUtil.dataSourceToSQL(RmPartyConstants.rmUser);
			sqlEntity.appendWhereAnd(" ID IN ("+RmStringHelper.parseToString(ids)+") ");
			sqlEntity.appendWhereAnd(" DR = '0' ");
			List<Map<String, Object>> userList = BaseDao.getBaseDao().query(sqlEntity);
			
			//密码重置
			if(userList.size()>0){
				for(Map<String, Object> map : userList){
					map.put("CUSTOM5", null);
					if(map.get("EMAIL")==null || "".equals(map.get("EMAIL").toString())){
						
					}
					
					// 加密
					String md5Password = MD5Util.getStringMD5(password);
					//map.put("PASSWORD", md5Password);
					map.put("PASSWORD", md5Password);
					map.put("CUSTOM5", "sent_mail");
					//删除多余字段
					//map.remove("RMRN");
					//更新rm_user
					List<Map<String, Object>> entityList = new ArrayList<Map<String, Object>>();
					Map<String,String> whereMap = new HashMap<String,String>();
					SqlWhereEntity sqlWhere = new SqlWhereEntity();
					Map<String, Object> entity = new HashMap<String, Object>();
					entity.put("CUSTOM5", "sent_mail");
					entity.put("PASSWORD",md5Password);
					entityList.add(entity);
					whereMap.put("ID", "ID = '"+map.get("ID")+"' " );
					sqlWhere.setWhereMap(whereMap);
			 		dcmsDAO.update(RmPartyConstants.rmUser, entityList, sqlWhere);
			 		entityList.clear();
			 		whereMap.clear();
			 		sqlWhere.clear();
			 		
					// 记录历史密码
					Map<String, String> history = new HashMap<String, String>();
					history.put("USER_ID", map.get("ID").toString());
					history.put("OLD_PASSWORD", map.get("PASSWORD")==null?"":map.get("PASSWORD").toString());
					
					//RmVoHelper.markCreateStamp(RmProjectHelper.getCurrentThreadRequest(), history);
					history.put("FIRST_CONFIG_DATE", map.get("MODIFY_DATE")==null?"":map.get("MODIFY_DATE").toString());
					dcmsDAO.insertByTransfrom(RmPartyConstants.rmPasswordHistory,history);
					
					//IRmMailService ms = new RmMailService(); 
					String sendEmail = map.get("EMAIL")==null?"":map.get("EMAIL").toString();
					String ts = new Timestamp(System.currentTimeMillis()).toString();
					String seanData = ts.substring(0, 4) + "年" + ts.substring(5, 7) + "月" + ts.substring(8, 10) + "日";
					String content = "<b>"
							+ map.get("NAME")
							+ "</b>&nbsp;&nbsp;您好！<br><br>"
							+ "您登录资金管理系统的用户信息如下：<br>"
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;登录名称："
							+ map.get("LOGIN_ID")
							+ "<br>"
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;初始密码："
							+ password
							+ "<br>"
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您的邮箱地址："
							+ RmStringHelper.prt(map.get("EMAIL"))
							+ "<br><br>"
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;资金管理系统维护组<br>" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + seanData + "";
				
				}
			}
		}
 		jsonMessage = "{\"message\":\"密码重置成功,新密码为："+password+"\"}";
		
		this.ajaxWrite(jsonMessage, request, response);
		return null;
	}

	@Override
	protected void afterOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO 自动生成的方法存根
		
	}

}
