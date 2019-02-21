package com.yonyou.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.jdbc.IBaseDao;

public class AddHistoryVersion {

	
	//添加历史版本
	public void add1(String parentId,IBaseDao dcmsDAO){
		String sql = "select max(VERSION_HISTORY) from  proj_proposal_history where PROJ_RELEASED_ID="+parentId;
		List<Map<String,Object>> lt = BaseDao.getBaseDao().query(sql,"");
		String str = (String) lt.get(0).get("VERSION_HISTORY");
		String strs[] = str.split(".");
		String version;
		if(strs.length>0){
			int a = Integer.parseInt(strs[1]);
			a+=1;
			version = strs[0]+"."+a;
		}else{
			version = "1.0";
		}
		
		
		
		
		//添加wbs到历史表
		String sqlString = " SELECT * from  proj_wbsinfo "+
		"where PROJ_SOURCE_ID = '"+parentId+"'";
		List<Map<String, Object>> list = BaseDao.getBaseDao().query(sqlString, "");
		List<Map<String,Object>> entityList = new ArrayList<Map<String,Object>>();
		for(int i=0;i<list.size();i++){
			
			Map<String,Object> map_addMap = new HashMap<String, Object>();
			for(String s : list.get(i).keySet()){
				map_addMap.put(s, list.get(i).get(s));
			}
			map_addMap.put("PROJ_RELEASED_ID", parentId);
			map_addMap.put("VERSION_HISTORY", version);
			map_addMap.remove("RMRN");
			map_addMap.put("ID", null);
			
			entityList.add(map_addMap);
		}
		
		
		dcmsDAO.insert("proj_wbsinfo_history", entityList);
		
		//添加组织架构到历史表
		String sqlString1 = " SELECT * from  proj_organization "+
				"where PROJ_SOURCE_ID = '"+parentId+"'";
				List<Map<String, Object>> list1 = BaseDao.getBaseDao().query(sqlString1, "");
				List<Map<String,Object>> entityList1 = new ArrayList<Map<String,Object>>();
				for(int i=0;i<list1.size();i++){
					
					Map<String,Object> map_addMap = new HashMap<String, Object>();
					for(String s : list1.get(i).keySet()){
						//System.out.println(s+":"+list.get(i).get(s));
						map_addMap.put(s, list1.get(i).get(s));
					}
					map_addMap.put("PROJ_RELEASED_ID", parentId);
					map_addMap.put("VERSION_HISTORY", version);
					map_addMap.remove("RMRN");
					map_addMap.put("ID", null);
					entityList1.add(map_addMap);
				}
			dcmsDAO.insert("proj_organization_history", entityList1);
			
			//添加项目预算到历史表
			String sqlString2 = " SELECT * from  proj_budget "+
					"where PROJ_SOURCE_ID = '"+parentId+"'";
					List<Map<String, Object>> list2 = BaseDao.getBaseDao().query(sqlString2, "");
					List<Map<String,Object>> entityList2 = new ArrayList<Map<String,Object>>();
					for(int i=0;i<list2.size();i++){
						
						Map<String,Object> map_addMap = new HashMap<String, Object>();
						for(String s : list2.get(i).keySet()){
							//System.out.println(s+":"+list.get(i).get(s));
							map_addMap.put(s, list2.get(i).get(s));
						}
						map_addMap.put("PROJ_RELEASED_ID", parentId);
						map_addMap.put("VERSION_HISTORY", version);
						map_addMap.remove("RMRN");
						map_addMap.put("ID", null);
						entityList2.add(map_addMap);
					}
			dcmsDAO.insert("proj_budget_history", entityList2);
				
			//添加阶段计划到历史表
			String sqlString3 = " SELECT * from  proj_progress_phase "+
					"where PROJ_SOURCE_ID = '"+parentId+"'";
					List<Map<String, Object>> list3 = BaseDao.getBaseDao().query(sqlString3, "");
					List<Map<String,Object>> entityList3 = new ArrayList<Map<String,Object>>();
					for(int i=0;i<list3.size();i++){
						
						Map<String,Object> map_addMap = new HashMap<String, Object>();
						for(String s : list3.get(i).keySet()){
							//System.out.println(s+":"+list.get(i).get(s));
							map_addMap.put(s, list3.get(i).get(s));
						}
						map_addMap.put("PROJ_RELEASED_ID", parentId);
						map_addMap.put("VERSION_HISTORY", version);
						map_addMap.remove("RMRN");
						map_addMap.put("ID", null);
						entityList3.add(map_addMap);
					}
				dcmsDAO.insert("proj_progress_phase_history", entityList3);
					
			//添加风险问题管控到历史表
			String sqlString4 = " SELECT * from  proj_risk_management "+
					"where PROJ_SOURCE_ID = '"+parentId+"'";
					List<Map<String, Object>> list4 = BaseDao.getBaseDao().query(sqlString4, "");
					List<Map<String,Object>> entityList4 = new ArrayList<Map<String,Object>>();
					for(int i=0;i<list4.size();i++){
						
						Map<String,Object> map_addMap = new HashMap<String, Object>();
						for(String s : list4.get(i).keySet()){
							//System.out.println(s+":"+list.get(i).get(s));
							map_addMap.put(s, list4.get(i).get(s));
						}
						map_addMap.put("PROJ_RELEASED_ID", parentId);
						map_addMap.put("VERSION_HISTORY", version);
						map_addMap.remove("RMRN");
						map_addMap.put("ID", null);
						entityList4.add(map_addMap);
					}
			dcmsDAO.insert("proj_risk_management_history", entityList4);
						
		//添加其他计划到历史表
		String sqlString5 = " SELECT * from  proj_otherplan "+
				"where PROJ_SOURCE_ID = '"+parentId+"'";
				List<Map<String, Object>> list5 = BaseDao.getBaseDao().query(sqlString5, "");
				List<Map<String,Object>> entityList5 = new ArrayList<Map<String,Object>>();
				for(int i=0;i<list5.size();i++){
					
					Map<String,Object> map_addMap = new HashMap<String, Object>();
					for(String s : list5.get(i).keySet()){
						//System.out.println(s+":"+list.get(i).get(s));
						map_addMap.put(s, list5.get(i).get(s));
					}
					map_addMap.put("PROJ_RELEASED_ID", parentId);
					map_addMap.put("VERSION_HISTORY", version);
					map_addMap.remove("RMRN");
					map_addMap.put("ID", null);
					entityList5.add(map_addMap);
				}
			dcmsDAO.insert("proj_otherplan_history", entityList5);
								
			//添加项目建议书到历史表
			String sqlString6 = " SELECT * from  proj_proposal "+
					"where PROJ_SOURCE_ID = '"+parentId+"'";
					List<Map<String, Object>> list6 = BaseDao.getBaseDao().query(sqlString6, "");
					List<Map<String,Object>> entityList6 = new ArrayList<Map<String,Object>>();
					for(int i=0;i<list6.size();i++){
						
						Map<String,Object> map_addMap = new HashMap<String, Object>();
						for(String s : list6.get(i).keySet()){
							//System.out.println(s+":"+list.get(i).get(s));
							map_addMap.put(s, list6.get(i).get(s));
						}
						map_addMap.put("PROJ_RELEASED_ID", parentId);
						map_addMap.put("VERSION_HISTORY", version);
						map_addMap.remove("RMRN");
						map_addMap.put("ID", null);
						entityList6.add(map_addMap);
					}
			dcmsDAO.insert("proj_proposal_history", entityList6);
			
			
		
	}
	
	
	//添加前坪估到历史表
	public void addAccHistory1(String parentId,IBaseDao dcmsDAO){
		String sql = "select max(VERSION_HISTORY) from  proj_proposal_history where PROJ_RELEASED_ID="+parentId;
		List<Map<String,Object>> lt = BaseDao.getBaseDao().query(sql,"");
		String str = (String) lt.get(0).get("VERSION_HISTORY");
		String strs[] = str.split(".");
		String version;
		if(strs.length>0){
			int a = Integer.parseInt(strs[1]);
			a+=1;
			version = strs[0]+"."+a;
		}else{
			version = "1.0";
		}
	
		String sqlString = " SELECT * from  pre_assessment "+
				"where BUS_ID = '"+parentId+"'";
		List<Map<String, Object>> list = BaseDao.getBaseDao().query(sqlString, "");
		List<Map<String,Object>> entityList = new ArrayList<Map<String,Object>>();
		for(int i=0;i<list.size();i++){
			
			Map<String,Object> map_addMap = new HashMap<String, Object>();
			for(String s : list.get(i).keySet()){
				map_addMap.put(s, list.get(i).get(s));
			}
			map_addMap.put("PROJ_RELEASED_ID", parentId);
			map_addMap.put("VERSION_HISTORY", version);
			map_addMap.remove("RMRN");
			map_addMap.put("ID", null);
			
			entityList.add(map_addMap);
		}
		dcmsDAO.insert("pre_assessment_history", entityList);
	}
}
