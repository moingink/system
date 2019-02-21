package com.yonyou.business.button.rm.party;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.util.RmPartyRelationCode;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.yonyou.business.DataSourceUtil;
import com.yonyou.business.button.demobutton.DemoButton;
import com.yonyou.util.BussnissException;
import com.yonyou.util.JsonUtils;
import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.util.sql.SqlWhereEntity;
import com.yonyou.util.sql.SQLUtil.WhereEnum;

public class PartyButForInsertDepartment extends DemoButton {

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
		JSONArray jsonArray = JSONArray.fromObject(request.getParameter("jsonData"));
		System.out.println("----------------json:"+jsonArray);
		//团体类型：部门
		String partyTypeId = "1000200800000000002";
		//查询父团体信息	(唯一：PARTY_VIEW_ID + CHILD_PARTY_CODE)
		String PARTY_VIEW_ID = "1000200700000000001";
		
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject json = JSONObject.fromObject(jsonArray.get(i));
			String id = (String)json.get("ID");
			SqlTableUtil stu=new SqlTableUtil("RM_PARTY_RELATION","");
			SqlWhereEntity swe=new SqlWhereEntity();
			swe.putWhere("PARTY_VIEW_ID", PARTY_VIEW_ID, WhereEnum.EQUAL_STRING);
			swe.putWhere("CHILD_PARTY_ID", id, WhereEnum.EQUAL_STRING);
			swe.putWhere("DR", "0", WhereEnum.EQUAL_STRING);
			stu.appendSelFiled("count(*)").appendSqlWhere(swe);
			Map<String, Object> find = dcmsDAO.find(stu);
			
			System.out.println(Integer.parseInt(find.get("COUNT(*)").toString()));   
			if(Integer.parseInt(find.get("COUNT(*)").toString()) != 0){
				jsonMessage = "{\"message\":\"保存失败，组织树上已经存在此组织，不能重复挂载！\"}";
				this.ajaxWrite(jsonMessage, request, response);
				return null;
			}
		}
		
		
		//String parentPartyId = (String)request.getParameter("PARENT_PARTY_ID");
		String parentPartyCode =(String)request.getParameter("PARENT_PARTY_CODE");
		if(parentPartyCode==null){
				jsonMessage = "{\"message\":\"保存失败，PARENT_PARTY_CODE值为空！\"}";
				this.ajaxWrite(jsonMessage, request, response);
				return null;
		}
		//String parentPartyCode = json.get("PARENT_PARTY_CODE").toString();
		String parentDataSourceCode = "RM_PARTY_RELATION";
		SqlTableUtil sqlEntity = DataSourceUtil.dataSourceToSQL(parentDataSourceCode);
		String condition = " CHILD_PARTY_CODE = '"+parentPartyCode+"' ";
		sqlEntity.appendWhereAnd(condition);
		sqlEntity.appendWhereAnd(" PARTY_VIEW_ID = '"+PARTY_VIEW_ID+"' ");
		sqlEntity.appendWhereAnd(" DR = '0' ");
		List<Map<String, Object>> mapList = BaseDao.getBaseDao().query(sqlEntity);
		if(mapList.isEmpty())
			{
				jsonMessage = "{\"message\":\"保存失败，父团体查询失败！\"}";
				this.ajaxWrite(jsonMessage, request, response);
				return null;
			}
		
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject json = JSONObject.fromObject(jsonArray.get(i));
			System.out.println("----------------json:"+json);
			
			Map<String,Object> parentMap= mapList.get(0);
			System.out.println("*****************parentMap:"+parentMap);
			//组装团体信息
			JSONObject rmParty = new JSONObject();
			String tableRmParty = "RM_PARTY";
			rmParty.put("NAME", json.get("DISPLAYNAME"));
			rmParty.put("OLD_PARTY_ID", json.get("TM_CODE"));
			rmParty.put("PARTY_TYPE_ID", partyTypeId);
			//是否继承权限？？？
			rmParty.put("IS_INHERIT", "1");
			System.out.println("****************rmParty:"+rmParty);
			 dcmsDAO.insertByTransfrom(tableRmParty, rmParty);
			 rmParty.put("ID", json.get("ID"));
			
			//组装团体关系信息
			JSONObject rmPartyRelation = new JSONObject();
			String tableRmPartyRelation = "RM_PARTY_RELATION";
			rmPartyRelation.put("PARTY_VIEW_ID", PARTY_VIEW_ID);
			rmPartyRelation.put("PARENT_PARTY_ID", parentMap.get("CHILD_PARTY_ID").toString());
			//子团体编码为团体表主键，需先生成团体表主键后赋值
			rmPartyRelation.put("CHILD_PARTY_ID", rmParty.get("ID").toString());
			rmPartyRelation.put("PARENT_PARTY_CODE", parentMap.get("CHILD_PARTY_CODE"));
			rmPartyRelation.put("CHILD_IS_MAIN_RELATION", "1");
			rmPartyRelation.put("PARENT_PARTY_NAME", parentMap.get("CHILD_PARTY_NAME"));
			rmPartyRelation.put("ORDER_CODE", json.get("CUORDER"));
			rmPartyRelation.put("CHILD_PARTY_NAME", rmParty.get("NAME"));
			rmPartyRelation.put("PARENT_PARTY_TYPE_ID", parentMap.get("CHILD_PARTY_TYPE_ID").toString());
			
			rmPartyRelation.put("CHILD_PARTY_TYPE_ID", partyTypeId);
			//默认是叶子节点
			rmPartyRelation.put("CHILD_IS_LEAF", "1");
			//防止调用时没有足够参数，自动初始化补齐 (查sql性能较低，尽可能在前台获得足够参数)
			//initDefaultValue(rmPartyRelation);
			//从缓存取规则，验证是否符合party链表规则
			//validatePartyRelation(rmPartyRelation);
			//初始化child_party_code
			initPartyChildCode(rmPartyRelation);
			System.out.println("****************rmPartyRelation:"+rmPartyRelation);
			dcmsDAO.insertByTransfrom(tableRmPartyRelation, rmPartyRelation);
		}
	
		jsonMessage = "{\"message\":\"保存成功\"}";
		this.ajaxWrite(jsonMessage, request, response);
		return null;
	}

	protected void initPartyChildCode(JSONObject rmPartyRelation){
		String tableName = "RM_PARTY_RELATION";
		String parentPartyCode = rmPartyRelation.get("PARENT_PARTY_CODE").toString();
		SqlTableUtil sql = new SqlTableUtil(tableName,"");
		sql.init("MAX(CHILD_PARTY_CODE)", "");
		sql.appendWhereAnd(" PARENT_PARTY_CODE = '"+parentPartyCode+"' ");
		List<Map<String, Object>> mapList = BaseDao.getBaseDao().query(sql);
		if(mapList.get(0).get("MAX(CHILD_PARTY_CODE)")==null){
			RmPartyRelationCode prc = new RmPartyRelationCode(RmPartyRelationCode.MIN_VALUE);
			String[] newCodes = prc.getNext(1);
			String childPartyCode = parentPartyCode+newCodes[0];
			System.out.println(newCodes[0]);
			rmPartyRelation.put("CHILD_PARTY_CODE", childPartyCode);
		}else{
			String maxChildPartyCode = mapList.get(0).get("MAX(CHILD_PARTY_CODE)").toString();
			RmPartyRelationCode prc = new RmPartyRelationCode(parentPartyCode,maxChildPartyCode);
			String[] newCodes = prc.getNext(1);
			String childPartyCode = parentPartyCode+newCodes[0];
			System.out.println(newCodes[0]);
			rmPartyRelation.put("CHILD_PARTY_CODE", childPartyCode);
		}
	}
	
	protected boolean validatePartyRelation(String tabName,String tmCode){
		boolean status = false;
		if(!tmCode.isEmpty()){
			SqlTableUtil sql = new SqlTableUtil(tabName,"");
			sql.init("ID", "");
			sql.appendWhereAnd(" TM_CODE = '"+tmCode+"' ");
			sql.appendWhereAnd(" DR = '0' ");
			List<Map<String, Object>> mapList = BaseDao.getBaseDao().query(sql);
			if(mapList.isEmpty()){
				status = true;
			}
		}
		return status;
	}
	
	@Override
	protected void afterOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO 自动生成的方法存根
		
	}

}

