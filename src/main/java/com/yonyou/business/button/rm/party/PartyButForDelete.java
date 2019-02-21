package com.yonyou.business.button.rm.party;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.util.RmPartyConstants;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.yonyou.business.DataSourceUtil;
import com.yonyou.business.button.ButtonAbs;
import com.yonyou.util.BussnissException;
import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SQLUtil.WhereEnum;
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.util.sql.SqlWhereEntity;

public class PartyButForDelete extends ButtonAbs {

	@Override
	protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) throws IOException, BussnissException {
		// TODO 自动生成的方法存根
		String jsonMessage = "";
		String msg = "";
		String dataSourceCode =request.getParameter("dataSourceCode");
		String tabName = findTableNameByDataSourceCode(dataSourceCode);
		JSONObject json = JSONObject.fromObject(request.getParameter("jsonData"));
		System.out.println("json:"+json);
		String partyId = json.get("ID").toString();
		String partyCode = json.get("PARENT_PARTY_CODE").toString();
		
		//此方法暂时只支持团体视图唯一的团体删除
		String viewId = "1000200700000000001";
		String tabRmPartyRelation = "RM_PARTY_RELATION";
		System.out.println(RmPartyConstants.viewMap);
		if(RmPartyConstants.viewMap.get("C").equals(viewId)){
			//如果该团体在资金计划中已经引用，不可删除
			SqlTableUtil sqlEntity = DataSourceUtil.dataSourceToSQL(tabRmPartyRelation);
			sqlEntity.appendWhereAnd(" CHILD_PARTY_ID = '"+partyId+"' ");
			sqlEntity.appendWhereAnd(" PARTY_VIEW_ID = '"+RmPartyConstants.viewMap.get("B")+"' ");
			sqlEntity.appendWhereAnd(" DR = '0' ");
			List<Map<String, Object>> mapList = BaseDao.getBaseDao().query(sqlEntity);
			if(mapList.size()==0 || mapList.isEmpty()){
				//删除各团体数据
				msg = deleteParty(dcmsDAO,tabName,partyCode,viewId,partyId);
			}else{
				msg = "删除失败，该组织被资金计划引用，建议先删除相关的引用！";
			}
		}else{
			//删除各团体数据
			msg = deleteParty(dcmsDAO,tabName,partyCode,viewId,partyId);
		}
		jsonMessage = "{\"message\":\""+msg+"\"}";
		this.ajaxWrite(jsonMessage, request, response);
		return null;
	}

	
	protected String deleteParty(IBaseDao dcmsDAO,String tabName,String partyCode,String viewId,String partyId) throws BussnissException, IOException{
		String msg = "";
		
		List<Map<String, Object>> entityList = new ArrayList<Map<String, Object>>();
		Map<String, Object> entity = new HashMap<String, Object>();
		
		//删除各团体数据TM_COMPANY
		entity.put("ID", partyId);
		entity.put("DR","1");
		entity.put("USABLE_STATUS", "0");
		entityList.add(entity);
 		dcmsDAO.update(tabName, entityList, new SqlWhereEntity());
 		entity.clear();
 		entityList.clear();
 		System.out.println("delete tm_company or tm_department");
 		
		//根据视图查询当前节点下是否有子节点
 		System.out.println(RmPartyConstants.rmPartyRelation);
		SqlTableUtil subEntity = DataSourceUtil.dataSourceToSQL(RmPartyConstants.rmPartyRelation);
		subEntity.appendWhereAnd(" PARENT_PARTY_CODE = '"+partyCode+"' ");
		subEntity.appendWhereAnd(" PARTY_VIEW_ID = '"+viewId+"' ");
		subEntity.appendWhereAnd(" DR = '0' ");
		List<Map<String, Object>> subList = BaseDao.getBaseDao().query(subEntity);
		System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&"+subList.size()+":"+subList);
		//选中组织下有子节点
		if(subList.size()!=0 || !subList.isEmpty()){
			for (Map<String, Object> map : subList) {
				System.out.println(map.get("CHILD_PARTY_CODE"));
				SqlTableUtil stu=new SqlTableUtil("RM_PARTY_RELATION", "");
				SqlWhereEntity swe=new SqlWhereEntity();
				swe.putWhere("PARENT_PARTY_CODE", map.get("CHILD_PARTY_CODE").toString(), WhereEnum.EQUAL_STRING);
				swe.putWhere("PARTY_VIEW_ID", viewId, WhereEnum.EQUAL_STRING);
				swe.putWhere("DR", "0", WhereEnum.EQUAL_STRING);
				stu.appendSelFiled("*").appendSqlWhere(swe);
				List<Map<String, Object>> query = dcmsDAO.query(stu);
				if(query.size()!=0 || !query.isEmpty()){
					
					msg = "删除失败!原因:选中组织子节点下还有子节点，不能删除!";
					return msg;
				}
					map.put("DR","1");
					map.put("USABLE_STATUS","0");
					map.remove("RMRN");
					System.out.println(map);
			}
			dcmsDAO.update("RM_PARTY_RELATION", subList,new SqlWhereEntity());
		}
		subList = BaseDao.getBaseDao().query(subEntity);
		System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&"+subList.size()+":"+subList);
	    //选中节点下没有子节点
			//删除团体关系RM_PARTY_RELATION
			//entity.put("CHILD_PARTY_CODE", partyCode);
			entity.put("DR","1");
			entityList.add(entity);
			Map<String,String> whereMap = new HashMap<String,String>();
			whereMap.put("CHILD_PARTY_CODE", "CHILD_PARTY_CODE='"+partyCode+"' ");
			whereMap.put("PARTY_VIEW_ID", "PARTY_VIEW_ID='"+viewId+"'");
			SqlWhereEntity sqlWhere = new SqlWhereEntity();
			sqlWhere.setWhereMap(whereMap);
	 		dcmsDAO.update(RmPartyConstants.rmPartyRelation, entityList, sqlWhere);
	 		entity.clear();
	 		entityList.clear();
	 		whereMap.clear();
	 		sqlWhere.clear();
	 		System.out.println("delete rm_party_relation");
	 		
			//查询当前节点的父节点下是否有子节点,无则更新CHILD_IS_LEAF='1'
			SqlTableUtil parentEntity = DataSourceUtil.dataSourceToSQL(RmPartyConstants.rmPartyRelation);
			String code = partyCode.substring(0, partyCode.length()-3); 
			System.out.println("code:"+code);
			parentEntity.appendWhereAnd(" PARENT_PARTY_CODE = '"+code+"' ");
			//parentEntity.appendWhereAnd(" CHILD_PARTY_CODE != '"+partyCode+"' ");
			parentEntity.appendWhereAnd(" PARTY_VIEW_ID = '"+viewId+"' ");
			parentEntity.appendWhereAnd(" DR = '0' ");
			List<Map<String, Object>> parentList = BaseDao.getBaseDao().query(parentEntity);
			if(parentList.size()==0 || parentList.isEmpty()){
				//更新父节点信息RM_PARTY_RELATION:	CHILD_IS_LEAF='1'
				//entity.put("CHILD_PARTY_CODE", code);
				entity.put("CHILD_IS_LEAF","1");
				entityList.add(entity);
				whereMap.put("CHILD_PARTY_CODE", "CHILD_PARTY_CODE='"+code+"'");
				whereMap.put("PARTY_VIEW_ID", "PARTY_VIEW_ID='"+viewId+"'");
				sqlWhere.setWhereMap(whereMap);
		 		int i = dcmsDAO.update(RmPartyConstants.rmPartyRelation, entityList, sqlWhere);
		 		entity.clear();
		 		entityList.clear();
		 		whereMap.clear();
		 		sqlWhere.clear();
		 		System.out.println("update "+i+" rm_party_relation for parent");
			}
			
			//查询团体在所有视图下是否还被引用
			SqlTableUtil allEntity = DataSourceUtil.dataSourceToSQL(RmPartyConstants.rmPartyRelation);
			allEntity.appendWhereAnd(" CHILD_PARTY_CODE = '"+partyCode+"' ");
			//allEntity.appendWhereAnd(" PARTY_VIEW_ID = '"+viewId+"' ");
			allEntity.appendWhereAnd(" DR = '0' ");
			List<Map<String, Object>> allList = BaseDao.getBaseDao().query(allEntity);
			if(allList.size()==0 || allList.isEmpty()){
				//只被选中删除的数据引用，可以删除团体RM_PARTY
				entity.put("ID", partyId);
				entity.put("DR","1");
				entityList.add(entity);
		 		dcmsDAO.update(RmPartyConstants.rmParty, entityList, new SqlWhereEntity());
		 		entity.clear();
		 		entityList.clear();
		 		System.out.println("delete rm_party");
			}
			msg="删除成功";
		
		return msg;
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
