package com.yonyou.business.button.rm.role;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.util.RmPartyConstants;

import net.sf.json.JSONObject;

import com.yonyou.business.DataSourceUtil;
import com.yonyou.business.button.ButtonAbs;
import com.yonyou.util.BussnissException;
import com.yonyou.util.RmIdFactory;
import com.yonyou.util.jdbc.IBaseDao;

public class RoleButForInsert extends ButtonAbs{

	@Override
	protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) throws IOException, BussnissException {
		// TODO 自动生成的方法存根
		
		String dataSourceCode =request.getParameter("dataSourceCode");
		String tabName = findTableNameByDataSourceCode(dataSourceCode);
		JSONObject json = JSONObject.fromObject(request.getParameter("jsonData"));
		System.out.println("************json1:"+json);
//		//全局角色没有所属组织ID
//        if("1".equals(json.getString("IS_SYSTEM_LEVEL"))){
//        	json.put("OWNER_ORG_ID", null);
//        }
//        System.out.println("************json2:"+json);
        
        //组装团体rm_party信息
		//团体类型为：角色
		String partyTypeId = RmPartyConstants.PartyType.RM_ROLE.id();
		//组装团体信息
		JSONObject rmParty = new JSONObject();
		rmParty.put("PARTY_TYPE_ID", partyTypeId);
		rmParty.put("NAME", json.get("NAME"));
		//自动生成器生成rm_party的id
		String [] ids = RmIdFactory.requestId(RmPartyConstants.rmParty, 1);
		rmParty.put("ID", ids[0]);
		rmParty.put("OLD_PARTY_ID", ids[0]);
		//是否继承权限,否
		rmParty.put("IS_INHERIT", "0");
		System.out.println("****************rmParty:"+rmParty);
		dcmsDAO.insertByTransfrom(RmPartyConstants.rmParty, rmParty);
		
		//对角色ID赋值
		json.put("ID", ids[0]);
		try {
			dcmsDAO.insertByTransfrom(tabName, json);
		} catch (BussnissException e) {
			// TODO 自动生成的 catch 块
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

}
