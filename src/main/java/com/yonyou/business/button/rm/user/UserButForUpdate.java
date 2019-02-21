package com.yonyou.business.button.rm.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.util.RmPartyConstants;
import org.util.RmPartyRelationCode;
import org.util.tools.helper.RmStringHelper;

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

public class UserButForUpdate extends DemoButton {

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
		JSONObject json = JSONObject.fromObject(request.getParameter("jsonData"));
		System.out.println("----------------json:"+json);

		String[] organization_ids = json.getString("ORGANIZATION_ID").split(",");
		String[] organization_names = json.getString("ORGANIZATION_NAME").split(",");
		String[] old_organization_ids = json.getString("OLD_ORGANIZATION_ID").split(",");
		
		//团体类型：用户
		String partyTypeId = RmPartyConstants.PartyType.RM_USER.id();
		//查询父团体信息	(唯一：PARTY_VIEW_ID + CHILD_PARTY_CODE)
		String PARTY_VIEW_ID = RmPartyConstants.PartyView.OA.id();
		
		//更新团体信息
		if(!json.getString("NAME").equals(json.getString("OLD_NAME"))){
			List<Map<String, Object>> entityList = new ArrayList<Map<String, Object>>();
			Map<String, Object> entity = new HashMap<String, Object>();
			entity.put("NAME",json.getString("NAME"));
			entity.put("ID",json.getString("ID"));
			entityList.add(entity);
	 		dcmsDAO.update(RmPartyConstants.rmParty, entityList, new SqlWhereEntity());
		}

		if(organization_ids != null && organization_ids.length>0 && !organization_ids.equals(old_organization_ids)){
			//先删除当前团体关系
			List<Map<String, Object>> entityList = new ArrayList<Map<String, Object>>();
			Map<String, Object> entity = new HashMap<String, Object>();
			entity.put("DR","1");
			entityList.add(entity);
			Map<String,String> whereMap = new HashMap<String,String>();
			//where CHILD_PARTY_ID in (select PR.CHILD_PARTY_ID from RM_USER U join RM_PARTY_RELATION PR on U.ID=PR.CHILD_PARTY_ID where U.USABLE_STATUS='1' and U.ID="+id+") and PARTY_VIEW_ID="+pratyViewId)
			whereMap.put("CHILD_PARTY_ID", "CHILD_PARTY_ID in (select CHILD_PARTY_ID from (select PR.CHILD_PARTY_ID from RM_USER U join RM_PARTY_RELATION PR on U.ID=PR.CHILD_PARTY_ID where U.USABLE_STATUS='1' and U.ID="+json.getString("ID")+") tmp)");
			whereMap.put("PARTY_VIEW_ID", "PARTY_VIEW_ID='"+PARTY_VIEW_ID+"'");
			SqlWhereEntity sqlWhere = new SqlWhereEntity();
			sqlWhere.setWhereMap(whereMap);
	 		dcmsDAO.update(RmPartyConstants.rmPartyRelation, entityList, sqlWhere);
	 		entity.clear();
	 		entityList.clear();
	 		whereMap.clear();
	 		sqlWhere.clear();
	 		
	 		//新增团体关系
	 		String tableRmPartyRelation = RmPartyConstants.rmPartyRelation;
	 		List<Map<String,Object>> lRmPartyRelation = new ArrayList<Map<String,Object>>(); 
	 		for(int i=0;i<organization_ids.length;i++){
	 			Map<String,Object> rmPartyRelation = new HashMap<String,Object>();
	 			rmPartyRelation.put("CHILD_PARTY_ID", json.getString("ID"));
	 			rmPartyRelation.put("PARTY_VIEW_ID", PARTY_VIEW_ID);
	 			//子团体id为团体表主键，需先生成团体表主键后赋值
	 			rmPartyRelation.put("PARENT_PARTY_ID", organization_ids[i]);
	 			rmPartyRelation.put("PARENT_PARTY_NAME", organization_names[i]);
	 			rmPartyRelation.put("CHILD_PARTY_NAME", json.getString("NAME"));
	 			rmPartyRelation.put("CHILD_PARTY_TYPE_ID", partyTypeId);
	 			rmPartyRelation.put("PARENT_PARTY_CODE", "");
				//rmPartyRelation.put("PARENT_PARTY_TYPE_ID", "");
	 			//主关系
	 			rmPartyRelation.put("CHILD_IS_MAIN_RELATION", "1");
	 			//默认是叶子节点
	 			rmPartyRelation.put("CHILD_IS_LEAF", "1");
	 			//rmPartyRelation.put("ORDER_CODE", json.get("CUORDER"));
	 			
	 			//防止调用时没有足够参数，自动初始化补齐 (查sql性能较低，尽可能在前台获得足够参数)
	 			initDefaultValue(rmPartyRelation,PARTY_VIEW_ID);
	 			//从缓存取规则，验证是否符合party链表规则
	 			//validatePartyRelation(rmPartyRelation);
	 			//初始化child_party_code
	 			initPartyChildCode(rmPartyRelation);
	 			System.out.println("****************rmPartyRelation:"+rmPartyRelation);
	 			lRmPartyRelation.add(rmPartyRelation);
	 		}
	 		dcmsDAO.insert(tableRmPartyRelation, lRmPartyRelation);
	 		
	 		//更新父节点关系
			entity.put("CHILD_IS_LEAF","0");
			entityList.add(entity);
			whereMap.put("CHILD_PARTY_ID", "CHILD_PARTY_ID in ("+RmStringHelper.parseToSQLStringApos(organization_ids)+")");
			whereMap.put("PARTY_VIEW_ID", "PARTY_VIEW_ID='"+PARTY_VIEW_ID+"'");
			sqlWhere.setWhereMap(whereMap);
	 		dcmsDAO.update(tableRmPartyRelation, entityList, sqlWhere);
		}
		
		//json.put("ORGANIZATION_ID", "");
		//json.remove("PARENT_PARTY_ID");
		System.out.println("****************"+json);
		json.remove("OLD_NAME");
		json.remove("OLD_ORGANIZATION_ID");
		json.remove("ORGANIZATION_NAME");
		dcmsDAO.update(tabName, json, new SqlWhereEntity());
		
		jsonMessage = "{\"message\":\"保存成功\"}";
		this.ajaxWrite(jsonMessage, request, response);
		return null;
	}

	protected void initPartyChildCode(Map<String,Object> rmPartyRelation){
		String tableName = RmPartyConstants.rmPartyRelation;
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
	/**
	 * 防止调用时没有足够参数，自动初始化补齐 (查sql性能较低，尽可能在前台获得足够参数)
	 * @param rmPartyRelation
	 * @param viewId
	 */
	protected void initDefaultValue(Map<String,Object> rmPartyRelation,String viewId){
		
    	//外键的空字符串成null
		/*if(rmPartyRelation.get("PARENT_PARTY_ID")==null || rmPartyRelation.get("PARENT_PARTY_ID").toString().length()==0){
			rmPartyRelation.put("PARENT_PARTY_ID", null);
		}*/
		
        //如果parent_party_code为空，则自动初始化parent_party_code (如取到多值, 取按CHILD_IS_MAIN_RELATION倒序排列的第1个)
		if(rmPartyRelation.get("PARENT_PARTY_CODE")==null || rmPartyRelation.get("PARENT_PARTY_CODE").toString().length()==0){
			if(rmPartyRelation.get("PARENT_PARTY_ID")!=null || rmPartyRelation.get("PARENT_PARTY_ID").toString().length()>0){
				SqlTableUtil sql = new SqlTableUtil(RmPartyConstants.rmPartyRelation,"");
				sql.init("CHILD_PARTY_CODE", "");
				sql.appendWhereAnd(" PARTY_VIEW_ID = '"+viewId+"' ");
				sql.appendWhereAnd(" CHILD_PARTY_ID = '"+rmPartyRelation.get("PARENT_PARTY_ID").toString()+"' ");
				List<Map<String, Object>> mapList = BaseDao.getBaseDao().query(sql);
				if(mapList.get(0)==null){
					String msg = "未找到父Party对应的Relation记录";
				}else{
					rmPartyRelation.put("PARENT_PARTY_CODE", mapList.get(0).get("CHILD_PARTY_CODE"));
				}
			}
		}
		//初始化parent和child的party_type_id(这是冗余字段)
		if(rmPartyRelation.get("PARENT_PARTY_ID")!=null && rmPartyRelation.get("PARENT_PARTY_ID").toString().length()>=0 && rmPartyRelation.get("PARENT_PARTY_TYPE_ID")==null){
			SqlTableUtil sql = new SqlTableUtil(RmPartyConstants.rmParty,"");
			sql.init("PARTY_TYPE_ID", "");
			sql.appendWhereAnd(" ID = '"+rmPartyRelation.get("PARENT_PARTY_ID")+"' ");
			List<Map<String, Object>> mapList = BaseDao.getBaseDao().query(sql);
			if(mapList.get(0)==null){
				String msg = "未找到父Party对应的记录";
			}else{
				rmPartyRelation.put("PARENT_PARTY_TYPE_ID", mapList.get(0).get("PARTY_TYPE_ID"));
			}
		}
	}
	
	protected boolean validateLoginId(String tabName,String loginId){
		boolean status = false;
		if(!loginId.isEmpty()){
			SqlTableUtil sql = new SqlTableUtil(tabName,"");
			sql.init("ID", "");
			sql.appendWhereAnd(" LOGIN_ID = '"+loginId+"' ");
			sql.appendWhereAnd(" USABLE_STATUS = '1' ");
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
