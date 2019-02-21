package com.yonyou.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.util.RmPartyConstants;
import org.util.tools.helper.RmStringHelper;

import com.yonyou.business.DataSourceUtil;
import com.yonyou.util.BussnissException;
import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.util.sql.SqlWhereEntity;


/**
 * 
* @ClassName: PartyController 
* @Description: 角色管理控制类
* @author 潘志伟
* @date 2017年2月15日 
*
 */
@RestController
@RequestMapping(value = "/role")
public class RoleController extends BaseController{

	@RequestMapping(value="authorize", method = RequestMethod.POST)
	 public void ajaxDatas(HttpServletRequest request, HttpServletResponse response) throws BussnissException {
		
		String isSuccess = "false"; 
		//获取参数
		JSONObject json = JSONObject.fromObject(request.getParameter("jsonData"));
		System.out.println("json:"+json);
		String roleId = json.getString("roleId");
		String strSelected = json.getString("selectedNode");
		strSelected = strSelected.replaceAll("\\\"", "\"");
		JSONArray selectedNode = JSONArray.fromObject(strSelected);
		System.out.println(roleId+"****"+selectedNode);
		
		Map<String,Map<String,Object>> mResource = new HashMap<String,Map<String,Object>>();
		//String[] strCodes = new String[selectedNode.size()];
		for(int i=0;i<selectedNode.size();i++){
			JSONObject node = (JSONObject) selectedNode.get(i);
			//适应前台取节点逻辑的改变-获取所有选中以及半选中节点[roleAuthorize]，需排除id为#及100的两节点
			if(selectAllNodes(node)){
				continue;
			}
			JSONObject data = (JSONObject) node.get("data");
			if(data==null){
				continue;
			}
			Map<String,Object> mtotalCode = new HashMap<String,Object>();
			//String nodeId = data.getString("id");
			String nodeName = data.getString("name");
			String totalCode = data.getString("totalCode");
			mtotalCode.put("name",nodeName);
			mtotalCode.put("totalCode", totalCode);
			mResource.put(totalCode, mtotalCode);
			//strCodes[i] = totalCode;
		}
		String[] strCodes = mResource.keySet().toArray(new String[0]);
		//RmStringHelper.parseToSQLStringApos(mAllResource.keySet().toArray(new String[0]))
		String authorize_id = RmPartyConstants.Authorize.FUNCTION_NODE.id();
		//从RM_AUTHORIZE_RESOURCE中查询出本次需要授权的资源
		SqlTableUtil resEntity = DataSourceUtil.dataSourceToSQL(RmPartyConstants.rmAuthorizeResource);
		resEntity.appendWhereAnd(" OLD_RESOURCE_ID IN ("+RmStringHelper.parseToSQLStringApos(strCodes)+") ");
		resEntity.appendWhereAnd(" AUTHORIZE_ID = '"+authorize_id+"' ");
		resEntity.appendWhereAnd(" DR = '0' ");
		List<Map<String, Object>> resList = BaseDao.getBaseDao().query(resEntity);
		//新老资源集合
		Map<String,Object> mAllResource = new HashMap<String,Object>();
		//对比resourceVos，筛选出RM_AUTHORIZE_RESOURCE中未注册的资源
		if(resList.isEmpty()){
			
		}else{
			for(int i=0;i<resList.size();i++){
				String id = resList.get(i).get("ID").toString();
				String oldResourceId = resList.get(i).get("OLD_RESOURCE_ID").toString();
				if(mResource.containsKey(oldResourceId)){
					mAllResource.put(id, resList.get(i));
					mResource.remove(oldResourceId);
				}
			}
		}
		
		//执行初始化未注册的资源
		List<Map<String, Object>> initResource = new ArrayList<Map<String, Object>>();
		for(Map.Entry<String, Map<String,Object>> m : mResource.entrySet()){
			Map<String,Object> resource = m.getValue();
			Map<String,Object> init = new HashMap<String,Object>();
			init.put("AUTHORIZE_ID", authorize_id);
			init.put("DEFAULT_ACCESS", RmPartyConstants.RM_NO);
			init.put("DEFAULT_ACCESS_TYPE", "1");
			init.put("DEFAULT_IS_AFFIX_DATA", RmPartyConstants.RM_NO);
			init.put("DEFAULT_IS_RECURSIVE", RmPartyConstants.RM_NO);
			init.put("NAME", resource.get("name"));
			init.put("OLD_RESOURCE_ID", resource.get("totalCode"));
			init.put("TOTAL_CODE", resource.get("totalCode"));
			initResource.add(init);
		}
		if(initResource.size()>0){
			String[] ids = dcmsDAO.insert(RmPartyConstants.rmAuthorizeResource, initResource);
			for(int i=0;i<ids.length;i++){
				mAllResource.put(ids[i], initResource.get(i));
			}
		}
		
		//清理某些资源的关联授权团体
		if(roleId.isEmpty()){
			/*List<Map<String, Object>> entityList = new ArrayList<Map<String, Object>>();
			Map<String, Object> entity = new HashMap<String, Object>();
			entity.put("DR","1");
			entityList.add(entity);
			Map<String,String> whereMap = new HashMap<String,String>();
			whereMap.put("AUTHORIZE_RESOURCE_ID", "AUTHORIZE_RESOURCE_ID IN ("+RmStringHelper.parseToSQLStringApos(mAllResource.keySet().toArray(new String[0]))+") ");
			//whereMap.put("PARTY_VIEW_ID", "PARTY_VIEW_ID='"+viewId+"'");
			SqlWhereEntity sqlWhere = new SqlWhereEntity();
			sqlWhere.setWhereMap(whereMap);
	 		dcmsDAO.update(RmPartyConstants.rmAuthorizeResourceRecord, entityList, sqlWhere);*/
	 		//物理删除
	 		Map<String,String> mWhere = new HashMap<String,String>();
	 		mWhere.put("AUTHORIZE_RESOURCE_ID", "AUTHORIZE_RESOURCE_ID IN ("+RmStringHelper.parseToSQLStringApos(mAllResource.keySet().toArray(new String[0]))+") ");
			SqlWhereEntity whereEntity = new SqlWhereEntity();
			whereEntity.setWhereMap(mWhere);
			dcmsDAO.delete(RmPartyConstants.rmAuthorizeResourceRecord, whereEntity);
			
		}else if(selectedNode.size()==0){
			/*List<Map<String, Object>> entityList = new ArrayList<Map<String, Object>>();
			Map<String, Object> entity = new HashMap<String, Object>();
			entity.put("DR","1");
			entityList.add(entity);
			Map<String,String> whereMap = new HashMap<String,String>();
			whereMap.put("PARTY_ID", "PARTY_ID ='"+roleId+"'");
			whereMap.put("AUTHORIZE_RESOURCE_ID", "AUTHORIZE_RESOURCE_ID IN (SELECT ID FROM RM_AUTHORIZE_RESOURCE WHERE AUTHORIZE_ID = '"+authorize_id+"')");
			SqlWhereEntity sqlWhere = new SqlWhereEntity();
			sqlWhere.setWhereMap(whereMap);
	 		dcmsDAO.update(RmPartyConstants.rmAuthorizeResourceRecord, entityList, sqlWhere);*/
	 		//物理删除
	 		Map<String,String> mWhere = new HashMap<String,String>();
	 		mWhere.put("PARTY_ID", "PARTY_ID ='"+roleId+"'");
	 		mWhere.put("AUTHORIZE_RESOURCE_ID", "AUTHORIZE_RESOURCE_ID IN (SELECT ID FROM RM_AUTHORIZE_RESOURCE WHERE AUTHORIZE_ID = '"+authorize_id+"')");
			SqlWhereEntity whereEntity = new SqlWhereEntity();
			whereEntity.setWhereMap(mWhere);
			dcmsDAO.delete(RmPartyConstants.rmAuthorizeResourceRecord, whereEntity);
		}else{
			//数据库应当出现的记录
			Set<Party_AuthorizeResource> sToIntoDb = new HashSet<Party_AuthorizeResource>();
			for(String authorize_resource_id : mAllResource.keySet()){
				sToIntoDb.add(new Party_AuthorizeResource(roleId,authorize_resource_id));
			}
			
			//查询出RM_AUTHORIZE_RESOURCE_RECORD已有的相关记录
			SqlTableUtil recordEntity = new SqlTableUtil(RmPartyConstants.rmAuthorizeResourceRecord,"arr");
			recordEntity.appendSelFiled("ID");
			recordEntity.appendSelFiled("AUTHORIZE_RESOURCE_ID");
			recordEntity.appendSelFiled("PARTY_ID");
			recordEntity.appendJoinTable(RmPartyConstants.rmAuthorizeResource, "ar", "arr.AUTHORIZE_RESOURCE_ID=ar.ID");
			recordEntity.appendWhereAnd(" arr.PARTY_ID = '"+roleId+"' ");
			recordEntity.appendWhereAnd(" ar.AUTHORIZE_ID = '"+authorize_id+"' ");
			recordEntity.appendWhereAnd(" arr.DR = '0' ");
			List<Map<String, Object>> lRecordInDb = BaseDao.getBaseDao().query(recordEntity);
			
			//筛选出RM_AUTHORIZE_RESOURCE_RECORD中没有的新授权记录，和需要删除的记录
			Set<Party_AuthorizeResource> sRecordToInsert = new HashSet<Party_AuthorizeResource>();
			Set<Party_AuthorizeResource> sRecordToDelete = new HashSet<Party_AuthorizeResource>();
			for(Map<String, Object> vo : lRecordInDb) {
				Party_AuthorizeResource pa = new Party_AuthorizeResource(vo.get("PARTY_ID").toString(), vo.get("AUTHORIZE_RESOURCE_ID").toString());
				pa.authoroze_resource_record_id = vo.get("ID").toString();
				if(sToIntoDb.contains(pa)) {
					sToIntoDb.remove(pa);
				} else {
					sRecordToDelete.add(pa);
				}
			}
			sRecordToInsert = sToIntoDb;
			//执行插入新授权记录
			if(sRecordToInsert.size()>0){
				List<Map<String, Object>> lRecordToInsert = new ArrayList<Map<String, Object>>();
				for(Party_AuthorizeResource pa : sRecordToInsert){
					Map<String,Object> init = new HashMap<String,Object>();
					init.put("AUTHORIZE_RESOURCE_ID", pa.authorize_resource_id);
					init.put("PARTY_ID", pa.party_id);
					init.put("AUTHORIZE_STATUS", RmPartyConstants.RM_YES);
					init.put("IS_AFFIX_DATA", RmPartyConstants.RM_NO);
					init.put("IS_RECURSIVE", RmPartyConstants.RM_NO);
					init.put("ACCESS_TYPE", "1");
					lRecordToInsert.add(init);
				}
				if(lRecordToInsert.size()>0){
					dcmsDAO.insert(RmPartyConstants.rmAuthorizeResourceRecord, lRecordToInsert);
				}
			}
			//执行删除已消失的授权记录
			if(sRecordToDelete.size()>0){
				Set<String> sRecordIdToDelete = new HashSet<String>();
				for(Party_AuthorizeResource pa : sRecordToDelete) {
					sRecordIdToDelete.add(pa.authoroze_resource_record_id);
					//加入map集合方便取
				}
				//再次连表查询删除按钮的menuid 是否是当前授权id  如果是则移除删除组。只把要删减menu的按钮权限删除即可。   
				
				String sql = "SELECT * FROM( SELECT  r.id ,re.id as RECORDID ,r.TOTAL_CODE,bm.MENU_ID from rm_authorize_resource_record  re INNER JOIN  rm_authorize_resource r   on  re.AUTHORIZE_RESOURCE_ID = r.id INNER JOIN rm_button_relation_menu bm on bm.BUTTON_CODE = r.TOTAL_CODE " +
				" where re.id in ("+RmStringHelper.parseToSQLStringApos(sRecordIdToDelete.toArray(new String[0]))+") )a"; 
				List<Map<String, Object>> menuList = BaseDao.getBaseDao().query(sql, "");
				for(Map<String, Object> menu:menuList){
					String recordId = String.valueOf(menu.get("RECORDID"));
					String code = String.valueOf(menu.get("TOTAL_CODE"));
					//String rid = String.valueOf(menu.get("RID"));
					if(mAllResource.toString().contains(code.substring(0, code.length()-3))){
						sRecordIdToDelete.remove(recordId);
					}
				}
				/*List<Map<String, Object>> entityList = new ArrayList<Map<String, Object>>();
				Map<String, Object> entity = new HashMap<String, Object>();
				entity.put("DR","1");
				entityList.add(entity);
				Map<String,String> whereMap = new HashMap<String,String>();
				whereMap.put("ID", "ID IN ("+RmStringHelper.parseToSQLStringApos(sRecordIdToDelete.toArray(new String[0]))+")");
				SqlWhereEntity sqlWhere = new SqlWhereEntity();
				sqlWhere.setWhereMap(whereMap);
		 		dcmsDAO.update(RmPartyConstants.rmAuthorizeResourceRecord, entityList, sqlWhere);*/
		 		//物理删除
		 		Map<String,String> mWhere = new HashMap<String,String>();
		 		mWhere.put("ID", "ID IN ("+RmStringHelper.parseToSQLStringApos(sRecordIdToDelete.toArray(new String[0]))+")");
				SqlWhereEntity whereEntity = new SqlWhereEntity();
				whereEntity.setWhereMap(mWhere);
				dcmsDAO.delete(RmPartyConstants.rmAuthorizeResourceRecord, whereEntity);
			}
			
		}
		isSuccess = "true";
		response.setCharacterEncoding("utf-8");
		try {
			PrintWriter out = response.getWriter();
			out.print(isSuccess);
			out.flush();
			out.close();
			
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
	}
	
	class Party_AuthorizeResource {
		public Party_AuthorizeResource(String party_id, String authorize_resource_id) {
			this.party_id = party_id;
			this.authorize_resource_id = authorize_resource_id;
		}
		private String party_id;
		private String authorize_resource_id;
		private String authoroze_resource_record_id;
		
		@Override
		public int hashCode() {
			return party_id.hashCode() + authorize_resource_id.hashCode();
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Party_AuthorizeResource) {
				Party_AuthorizeResource o2 = (Party_AuthorizeResource)obj;
				return (party_id == o2.party_id || party_id.equals(o2.party_id))
					&& (authorize_resource_id == o2.authorize_resource_id || authorize_resource_id.equals(o2.authorize_resource_id));
				
			} else {
				return false;
			}
		}
		
		@Override
		public String toString() {
			return party_id + "|" + authorize_resource_id + "|" + authoroze_resource_record_id;
		}
	}
	
	private boolean selectAllNodes(JSONObject nodeInfo){
		String nodeId = nodeInfo.getString("id");
		boolean result = false;
		if(nodeId.equals("#")||nodeId.equals("100")){
			result = true;
		}
		return result;
	}
	
	public static void main(String[] args) throws BussnissException
	{
		RoleController p = new RoleController();
		HttpServletRequest request=null ;
		 HttpServletResponse response = null;
		p.ajaxDatas(request,response);
	}
}
