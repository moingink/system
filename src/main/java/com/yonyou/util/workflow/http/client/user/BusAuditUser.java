package com.yonyou.util.workflow.http.client.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.yonyou.iuap.persistence.vo.pub.BusinessException;
import com.yonyou.util.SpringContextUtil;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.notity.group.UserGroupFactory;
import com.yonyou.util.workflow.exect.WorkFlowException;
import com.yonyou.util.workflow.http.client.util.AuditUserUtil;
import com.yonyou.util.workflow.type.PromptMessageEnum;
import com.yonyou.util.workflow.util.PromptMessageUtil;
import com.yonyou.util.workflow.util.WorkFlowSql;

public class BusAuditUser implements IWorkOwer{

	
	
	protected IBaseDao getBaseDao(){
		return (IBaseDao)SpringContextUtil.getBean("dcmsDAO");
	}
	
	public Map<String, Object> findAuditUsers(String ower_group, String user,
			String company,String projectBusId, String productIds) {
		// TODO 自动生成的方法存根
		String [] owers =ower_group.split(","); 
		Map<String,Object> users =new HashMap<String,Object>();
		if(owers.length>0){
			if(owers[0].equals("END"))
			{
				users.put("流程结束", "0");
			}
			List<Map<String,Object>> list =this.getBaseDao().query(WorkFlowSql.findSelectSqlForOwerGroup(owers), "");
			Map<String,Map<String,List<String>>> owerTypesMap=this.bulidOwerTypes(list);
			List<Map<String, Object>> userList;
			try {
				userList = this.findListUsers(owerTypesMap, company,projectBusId,productIds);
				for(Map<String,Object> userEntity: userList){
					users.put((String)userEntity.get("NAME"), userEntity.get("ID"));
				}
			} catch (BusinessException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
				throw new WorkFlowException(PromptMessageUtil.findMessage(PromptMessageEnum.CLIENT_FIND_USER_NULL_ERROR,ower_group));
			}
			if(users.size()==0){
				throw new WorkFlowException(PromptMessageUtil.findMessage(PromptMessageEnum.CLIENT_FIND_USER_NULL_ERROR,ower_group));
			}
		}
		return users;
	}
	
	public Map<String,Map<String,List<String>>> bulidOwerTypes(List<Map<String,Object>> list ){
		Map<String,Map<String,List<String>>> bulidOwerTypes =new LinkedHashMap<String, Map<String,List<String>>>();
		
		for(Map<String,Object> enitty:list){
			String post_type =(String)enitty.get("POST_TYPE");
			String type =(String)enitty.get("MAPPING_TYPE");
			String value =enitty.get("MAPPING_ID").toString();
			Map<String,List<String>> groupByType=null;
			if(bulidOwerTypes.containsKey(post_type)){
				groupByType=bulidOwerTypes.get(post_type);
			}else{
				groupByType =new HashMap<String,List<String>>();
				bulidOwerTypes.put(post_type, groupByType);
			}
			if(bulidOwerTypes.get(post_type).containsKey(type)){
				bulidOwerTypes.get(post_type).get(type).add(value);
			}else{
				List<String> valueList =new ArrayList<String>();
				valueList.add(value);
				bulidOwerTypes.get(post_type).put(type, valueList);
			}
		}
		
		return bulidOwerTypes;
	}
	
	
	
	public List<Map<String,Object>> findListUsers(Map<String,Map<String,List<String>>> owerTypes,String company,String projectBusId, String productIds) throws BusinessException{
		List<Map<String,Object>> listUsers =new ArrayList<Map<String,Object>>();
		for(String post_type:owerTypes.keySet()){
			Map<String,Map<String,Object>> groupUserMap =new HashMap<String,Map<String,Object>>();
			Map<String,List<String>>  groupByType =owerTypes.get(post_type);
			for(String type:groupByType.keySet()){
				if(groupByType.get(type).size()>0){
					String [] messages =new String[groupByType.get(type).size()];
					messages=groupByType.get(type).toArray(messages);
					groupUserMap.putAll(this.bulidGroupUserMap(type, messages));
				}
			}
			switch(post_type){
				case  AuditUserUtil.POST_TYPE_COMPANY:
					this.appendListByCompany(company, groupUserMap, listUsers);
					break;
				case  "2"://查看所有产品负责人
					this.appendListByProducts(productIds,groupUserMap,listUsers);
					break;
				case  "3"://查看分管领导
					this.appendListByBus(company, groupUserMap, listUsers);
					break;
				case  "4"://查找项目负责人
					this.appendManagerByProjectID(projectBusId, groupUserMap, listUsers);//客户项目负责人
					this.appendManagerByNLProject(projectBusId, groupUserMap, listUsers);//能力项目负责人
					this.appendManagerByProjectBG(projectBusId, groupUserMap, listUsers);////项目变更负责人
					break;
				case  "5"://查找项目组内部成员
					this.appendListByProjectID(projectBusId, groupUserMap, listUsers);
					//this.appendListByNLProjectID(projectBusId, groupUserMap, listUsers);
					break;
				case  "6"://是否区分营销单元
					this.appendListByMutilation(company,productIds, groupUserMap, listUsers);
					break;
				case  "7"://项目经理
					this.appendProManagerByProjectID(projectBusId, groupUserMap, listUsers);
					this.appendProManagerByNLProject(projectBusId, groupUserMap, listUsers);
					this.appendProManagerByProjectBG(projectBusId, groupUserMap, listUsers);
					break;
				case  "8"://部门负责人
					this.appendListByManager(company, groupUserMap, listUsers);
					break;
				case  "9"://能力项目需求部门负责人
					this.appendProManagerByXQProject(projectBusId, groupUserMap, listUsers);
					break;
				case  "S"://项目建议书对应商机的营销单元负责人
					this.appendProManagerBySJProject(projectBusId, groupUserMap, listUsers);
					break;
				default :
					this.appendListByMap(listUsers, groupUserMap);
					break;
			}
		}
		return listUsers;
 	}
	
	private Map<String,Map<String,Object>> bulidGroupUserMap(String type,String [] messages) throws BusinessException{
		Map<String,Map<String,Object>> userMap =new HashMap<String,Map<String,Object>>();
		List<Map<String,Object>> list=UserGroupFactory.findGroup(type).findUserGroup(getBaseDao(), null, messages);
		for(Map<String,Object> user:list){
			if(user.containsKey("ID")&&user.get("ID")!=null){
				String id =user.get("ID").toString();
				userMap.put(id, user);
			}
			
		}
		return userMap;
	}
	
	
	private void appendListByMap(List<Map<String,Object>> list ,Map<String,Map<String,Object>> userMaps){
		list.addAll(userMaps.values());
	}
	
	private void appendListByCompany(String company,Map<String,Map<String,Object>> groupUserMaps,List<Map<String,Object>> list) throws BusinessException{
		String[] companys =company.split(":");
		if(companys.length==2){
			String id =companys[1];
			Map<String,Map<String,Object>>  companyUser =this.bulidGroupUserMap(AuditUserUtil.GROUP_TYPE_COMPANY_INDEX, new String[]{id});
			for(String userId:companyUser.keySet()){
				if(groupUserMaps.containsKey(userId)){
					list.add(companyUser.get(userId));
				}
			}
		}else{
			this.appendListByMap(list, groupUserMaps);
		}
		
	}
	
	
	//by pzw 查找所有业务负责人
	private void appendListByProducts(String procductList,Map<String,Map<String,Object>> groupUserMaps,List<Map<String,Object>> list) throws BusinessException
	{
		//查找产品部门负责人
		if(procductList!=null){
			Map<String,Map<String,Object>> companyUser =this.bulidUserMapByProducts(AuditUserUtil.GROUP_TYPE_COMPANY_INDEX, procductList);
			if(!companyUser.isEmpty()){
			for(String userId:companyUser.keySet()){
				if(groupUserMaps.containsKey(userId)){
					list.add(companyUser.get(userId));
				}
			}
			}else
			{
				this.appendListByMap(list, groupUserMaps);
			}
		}else
		{
			this.appendListByMap(list, groupUserMaps);
		}
		
	}
	//by pzw 查找项目负责人
	private void appendManagerByProjectID(String projectID,Map<String,Map<String,Object>> groupUserMaps,List<Map<String,Object>> list) throws BusinessException
		{
			//查找产品部门负责人
			if(projectID!=null&&projectID.length()>0){
				Map<String,Map<String,Object>> companyUser=bulidUserMapByProject("",projectID);
				if(!companyUser.isEmpty()){
					for(String userId:companyUser.keySet()){
						list.add(companyUser.get(userId));
					}
				}
			}else
			{
				this.appendListByMap(list, groupUserMaps);
			}
			
		}
	//by pzw 查找项目经理
		private void appendProManagerByProjectID(String projectID,Map<String,Map<String,Object>> groupUserMaps,List<Map<String,Object>> list) throws BusinessException
			{
				//查找产品部门负责人
				if(projectID!=null&&projectID.length()>0){
					Map<String,Map<String,Object>> companyUser=this.bulidManagerByProject("",projectID);
					if(!companyUser.isEmpty()){
						for(String userId:companyUser.keySet()){
							list.add(companyUser.get(userId));
						}
					}
				}else
				{
					this.appendListByMap(list, groupUserMaps);
				}
				
			}
	
	//by pzw 查找项目组成员
	private void appendListByProjectID(String projectID,Map<String,Map<String,Object>> groupUserMaps,List<Map<String,Object>> list) throws BusinessException
	{			
		//查找产品部门负责人
		if(projectID!=null&&projectID.length()>0){
			Map<String,Map<String,Object>> companyUser=bulidUserMapGroupByProject("",projectID);
			if(!companyUser.isEmpty()){
				for(String userId:companyUser.keySet()){
					list.add(companyUser.get(userId));
				}
				}else
				{
					this.appendListByMap(list, groupUserMaps);
				}
		}else
		{
			this.appendListByMap(list, groupUserMaps);
		}	
	}
	
	//by pzw 查找分管领导
	private void appendListByBus(String company,Map<String,Map<String,Object>> groupUserMaps,List<Map<String,Object>> list) throws BusinessException
	{
		//查找产品部门负责人
		String[] companys =company.split(":");
		if(companys.length==2){
			String id =companys[1];
			Map<String,Map<String,Object>>  companyUser =this.bulidUserMapByBus(AuditUserUtil.GROUP_TYPE_COMPANY_INDEX, id);
			if(!companyUser.isEmpty()){
			for(String userId:companyUser.keySet()){
				if(groupUserMaps.containsKey(userId)){
					list.add(companyUser.get(userId));
				}
			}
			}else
			{
				this.appendListByMap(list, groupUserMaps);
			}
			
		}else{
			this.appendListByMap(list, groupUserMaps);
		}
	}
	
	//by pzw 查找部门负责人
	private void appendListByManager(String company,Map<String,Map<String,Object>> groupUserMaps,List<Map<String,Object>> list) throws BusinessException
	{
		//查找产品部门负责人
		String[] companys =company.split(":");
		if(companys.length==2){
			String id =companys[1];
			Map<String,Map<String,Object>>  companyUser =this.bulidUserMapByManager(AuditUserUtil.GROUP_TYPE_COMPANY_INDEX, id);
			if(!companyUser.isEmpty()){
				for(String userId:companyUser.keySet()){
					list.add(companyUser.get(userId));
				}
				}else
				{
					this.appendListByMap(list, groupUserMaps);
				}
			
		}else{
			this.appendListByMap(list, groupUserMaps);
		}
	}
	
	//by pzw 多维度报账单
		private void appendListByMutilation(String company,String procductList,Map<String,Map<String,Object>> groupUserMaps,List<Map<String,Object>> list) throws BusinessException
		{
			//判断是都为营销单元
			if(company!=null)
			{
				String[] companys =company.split(":");
				if(isYxdy(companys[1]))//营销单元产品部门会签
				{
					this.appendListByProducts(procductList, groupUserMaps, list);
				}else//非营销单元提出 营销单元会签
				{
					Map<String,Map<String,Object>>  companyUser =this.bulidUserMapByYXDY("", procductList);
					if(!companyUser.isEmpty()){
						for(String userId:companyUser.keySet()){
							list.add(companyUser.get(userId));
						}
						}else
						{
							this.appendListByMap(list, groupUserMaps);
						}
					
					}
				}
		}
			
	
	
	
	private Map<String,Object> test(String ower_group){
		Map<String,Object> users =new HashMap<String,Object>();
		switch(ower_group){
		case "DEP":
			users.put("张三", "12312");
			users.put("李四", "12312");
			users.put("dep_lishi2", "12312");
			users.put("dep_lishi3", "12312");
			break;
		case "ZHANLUO":
			users.put("网五", "12312");
			users.put("战略1", "12312");
			users.put("zhanluo_wangwu2", "12312");
			users.put("zhanluo_wangwu3", "12312");
			break;
		}
		return users;
	}
	//panzhiwei 查找分管领导
	private Map<String,Map<String,Object>> bulidUserMapByBus(String type,String companyID) throws BusinessException{
		Map<String,Map<String,Object>> userMap =new HashMap<String,Map<String,Object>>();
		String strsql = "select * from RM_USER where ID in (select corp_leader_id from tm_company where ID='"+companyID+"')";
		
		List<Map<String,Object>> list=getBaseDao().query(strsql, "");
		if(list!=null){
		for(Map<String,Object> user:list){
			if(user.containsKey("ID")&&user.get("ID")!=null){
				String id =user.get("ID").toString();
				userMap.put(id, user);
			}
			
		}
		
		}
		return userMap;
	}

	//panzhiwei 产品部门负责人
		private Map<String,Map<String,Object>> bulidUserMapByProducts(String type,String productList) throws BusinessException{
			Map<String,Map<String,Object>> userMap =new HashMap<String,Map<String,Object>>();
			 String strsql = "select * from rm_user where ID in(select c.dept_leader_id  from tm_company c,product_info p where p.org_dept_code=c.ID AND  c.dept_leader_id is not NULL  "
						+ "AND  p.id in("+productList+") group by `NAME`)";	
			 System.out.println("产品负责人查询："+strsql);

			List<Map<String,Object>> list=getBaseDao().query(strsql, "");
			if(list!=null){
			for(Map<String,Object> user:list){
				if(user.containsKey("ID")&&user.get("ID")!=null){
					String id =user.get("ID").toString();
					userMap.put(id, user);
				}
				
			}
			
			}
			return userMap;	
		}
		//panzhiwei 项目负责人
		private Map<String,Map<String,Object>> bulidUserMapByProject(String type,String projectID) throws BusinessException{
			Map<String,Map<String,Object>> userMap =new HashMap<String,Map<String,Object>>();
			String strsql = "select * from RM_USER where ID in (select duty_id from proj_released where ID='"+projectID+"')";
			System.out.println("项目组负责人查询："+strsql);
			List<Map<String,Object>> list=getBaseDao().query(strsql, "");
			if(list!=null){
			for(Map<String,Object> user:list){
				if(user.containsKey("ID")&&user.get("ID")!=null){
					String id =user.get("ID").toString();
					userMap.put(id, user);
				}
				
			}
			
			}
			return userMap;
		}
		// 查找项目组负责人
		private Map<String,Map<String,Object>> bulidUserMapGroupByProject(String type,String projectID) throws BusinessException{
			Map<String,Map<String,Object>> userMap =new HashMap<String,Map<String,Object>>();
			 String strsql = "select * from rm_user where ID in(select user_id from proj_organization where  PROJ_SOURCE_ID='"+projectID+"' AND user_id is not null)";	
			 System.out.println("项目组成员查询："+strsql);

			List<Map<String,Object>> list=getBaseDao().query(strsql, "");
			if(list!=null){
			for(Map<String,Object> user:list){
				if(user.containsKey("ID")&&user.get("ID")!=null){
					String id =user.get("ID").toString();
					userMap.put(id, user);
				}
				
			}
			
			}
			return userMap;
		}
		//panzhiwei 项目经理
				private Map<String,Map<String,Object>> bulidManagerByProject(String type,String projectID) throws BusinessException{
					Map<String,Map<String,Object>> userMap =new HashMap<String,Map<String,Object>>();
					String strsql = "select * from RM_USER where ID in (select manager_id from proj_released where ID='"+projectID+"')";
					System.out.println("项目经理人员查询："+strsql);
					List<Map<String,Object>> list=getBaseDao().query(strsql, "");
					if(list!=null){
					for(Map<String,Object> user:list){
						if(user.containsKey("ID")&&user.get("ID")!=null){
							String id =user.get("ID").toString();
							userMap.put(id, user);
						}
						
					}
					
					}
					return userMap;
				}
				
				//panzhiwei 部门负责人
				private Map<String,Map<String,Object>> bulidUserMapByManager(String type,String companyID) throws BusinessException{
					Map<String,Map<String,Object>> userMap =new HashMap<String,Map<String,Object>>();
					String strsql = "select * from RM_USER where ID in (select dept_leader_id from tm_company where ID='"+companyID+"')";
					
					List<Map<String,Object>> list=getBaseDao().query(strsql, "");
					if(list!=null){
					for(Map<String,Object> user:list){
						if(user.containsKey("ID")&&user.get("ID")!=null){
							String id =user.get("ID").toString();
							userMap.put(id, user);
						}
						
					}
					
					}
					return userMap;
				}	
				
				//panzhiwei 营销单元负责人查询
				private Map<String,Map<String,Object>> bulidUserMapByYXDY(String type,String companyID) throws BusinessException{
					Map<String,Map<String,Object>> userMap =new HashMap<String,Map<String,Object>>();
					String strsql = "select * from RM_USER where ID in (select dept_leader_id from tm_company where ID in("+companyID+"))";
					
					List<Map<String,Object>> list=getBaseDao().query(strsql, "");
					if(list!=null){
					for(Map<String,Object> user:list){
						if(user.containsKey("ID")&&user.get("ID")!=null){
							String id =user.get("ID").toString();
							userMap.put(id, user);
						}
						
					}
					
					}
					return userMap;
				}	
		//是否营销单元
			private boolean isYxdy(String companyID)
			{
				String mySql="select * from tm_company where ID='"+companyID+"' AND OU_TYPE='0'";
				List list =this.getBaseDao().query(mySql, "");
				if(list!=null&&list.size()>0)
				{
					return true;
				}
				return false;
			}
			
			//by pzw 查找项目变更负责人
			private void appendManagerByProjectBG(String projectID,Map<String,Map<String,Object>> groupUserMaps,List<Map<String,Object>> list) throws BusinessException
				{
					//查找产品部门负责人
					if(projectID!=null&&projectID.length()>0){
						Map<String,Map<String,Object>> companyUser=bulidUserMapByProjectBG("",projectID);
						if(!companyUser.isEmpty()){
							for(String userId:companyUser.keySet()){
								list.add(companyUser.get(userId));
							}
						}
					}else
					{
						this.appendListByMap(list, groupUserMaps);
					}
					
				}
			
			
		//判断能力项目变更项目变更取人
			private Map<String,Map<String,Object>> bulidUserMapByProjectBG(String type,String projectID) throws BusinessException{
				Map<String,Map<String,Object>> userMap =new HashMap<String,Map<String,Object>>();
				String strsql = "select * from RM_USER where ID in (select p.duty_id from proj_released p,proj_requirement r where p.BID_BUSS_NO=r.REQUIREMENT_CODE and p.duty_id is not null AND r.ID='"+projectID+"')";
				System.out.println("能力项目变更负责人："+strsql);
				List<Map<String,Object>> list=getBaseDao().query(strsql, "");
				if(list!=null){
				for(Map<String,Object> user:list){
					if(user.containsKey("ID")&&user.get("ID")!=null){
						String id =user.get("ID").toString();
						userMap.put(id, user);
					}
					
				}
				
				}
				return userMap;
			}	
			
			//查找能力项目负责人
			private void appendManagerByNLProject(String projectID,Map<String,Map<String,Object>> groupUserMaps,List<Map<String,Object>> list) throws BusinessException
			{
				//查找产品部门负责人
				if(projectID!=null&&projectID.length()>0){
					Map<String,Map<String,Object>> companyUser=bulidManagerMapByNLProject("",projectID);
					if(!companyUser.isEmpty()){
						for(String userId:companyUser.keySet()){
							list.add(companyUser.get(userId));
						}
					}
				}else
				{
					this.appendListByMap(list, groupUserMaps);
				}
				
			}
			
			//判断能力项目取人  在建议书中取得项目经理
			private Map<String,Map<String,Object>> bulidManagerMapByNLProject(String type,String projectID) throws BusinessException{
				Map<String,Map<String,Object>> userMap =new HashMap<String,Map<String,Object>>();
				String strsql = "select * from RM_USER where ID in (select duty_id from proj_proposal where PROJ_SOURCE_ID='"+projectID+"')";
				System.out.println("能力项目建议书负责人："+strsql);
				List<Map<String,Object>> list=getBaseDao().query(strsql, "");
				if(list!=null){
				for(Map<String,Object> user:list){
					if(user.containsKey("ID")&&user.get("ID")!=null){
						String id =user.get("ID").toString();
						userMap.put(id, user);
					}
					
				}
				
				}
				return userMap;
			}
			
			
			//查找能力项目项目经理
			private void appendProManagerByNLProject(String projectID,Map<String,Map<String,Object>> groupUserMaps,List<Map<String,Object>> list) throws BusinessException
			{
				//查找产品部门负责人
				if(projectID!=null&&projectID.length()>0){
					Map<String,Map<String,Object>> companyUser=bulidProManagerMapByNLProject("",projectID);
					if(!companyUser.isEmpty()){
						for(String userId:companyUser.keySet()){
							list.add(companyUser.get(userId));
						}
					}
				}else
				{
					this.appendListByMap(list, groupUserMaps);
				}
				
			}
			
			//判断能力项目取人  在建议书中取得负责人
			private Map<String,Map<String,Object>> bulidProManagerMapByNLProject(String type,String projectID) throws BusinessException{
				Map<String,Map<String,Object>> userMap =new HashMap<String,Map<String,Object>>();
				String strsql="select * from rm_user where ID in (select proj_manager_id from proj_requirement r where proj_manager_id is not null AND r.ID='"+projectID+"')";
				//String strsql = "select * from RM_USER where ID in (select r.proj_manager_id from proj_released p,proj_requirement r where p.BID_BUSS_NO=r.REQUIREMENT_CODE and p.duty_id is not null AND p.ID='"+projectID+"')";
				System.out.println("能力项目建议书项目经理："+strsql);
				List<Map<String,Object>> list=getBaseDao().query(strsql, "");
				if(list!=null){
				for(Map<String,Object> user:list){
					if(user.containsKey("ID")&&user.get("ID")!=null){
						String id =user.get("ID").toString();
						userMap.put(id, user);
					}
					
				}
				
				}
				return userMap;
			}
			
			///能力项目变更项目经理
			private void appendProManagerByProjectBG(String projectID,Map<String,Map<String,Object>> groupUserMaps,List<Map<String,Object>> list) throws BusinessException
			{
				//查找产品部门负责人
				if(projectID!=null&&projectID.length()>0){
					Map<String,Map<String,Object>> companyUser=bulidProManagerMapByNLProjectGB("",projectID);
					if(!companyUser.isEmpty()){
						for(String userId:companyUser.keySet()){
							list.add(companyUser.get(userId));
						}
					}
				}else
				{
					this.appendListByMap(list, groupUserMaps);
				}
				
			}
			
			//判断能力项目取人  在建议书中取得负责人
			private Map<String,Map<String,Object>> bulidProManagerMapByNLProjectGB(String type,String projectID) throws BusinessException{
				Map<String,Map<String,Object>> userMap =new HashMap<String,Map<String,Object>>();
				String strsql = "select * from RM_USER where ID in (select p.manager_id from proj_released p,proj_requirement r where p.BID_BUSS_NO=r.REQUIREMENT_CODE and p.duty_id is not null AND r.ID='"+projectID+"')";
				System.out.println("能力项目建议书项目经理："+strsql);
				List<Map<String,Object>> list=getBaseDao().query(strsql, "");
				if(list!=null){
				for(Map<String,Object> user:list){
					if(user.containsKey("ID")&&user.get("ID")!=null){
						String id =user.get("ID").toString();
						userMap.put(id, user);
					}
					
				}
				
				}
				return userMap;
			}
			
			
			//判断能力项目建议书对应的 需求部门负责人
			private void appendProManagerByXQProject(String projectID,Map<String,Map<String,Object>> groupUserMaps,List<Map<String,Object>> list) throws BusinessException
			{
				//查找产品部门负责人
				if(projectID!=null&&projectID.length()>0){
					Map<String,Map<String,Object>> companyUser=bulidProManagerMapByXQProject("",projectID);
					if(!companyUser.isEmpty()){
						for(String userId:companyUser.keySet()){
							list.add(companyUser.get(userId));
						}
					}
				}else
				{
					this.appendListByMap(list, groupUserMaps);
				}
				
			}
			//判断能力项目建议书对应的 需求部门负责人
			private Map<String,Map<String,Object>> bulidProManagerMapByXQProject(String type,String projectID) throws BusinessException{
				Map<String,Map<String,Object>> userMap =new HashMap<String,Map<String,Object>>();
				String strsql = "select * from RM_USER where ID in (select dept_leader_id from tm_company where ID=(select dept_id from proj_requirement r where dept_id is not null AND r.ID='"+projectID+"'))";
				System.out.println("能力项目建议书需求部门负责人："+strsql);
				List<Map<String,Object>> list=getBaseDao().query(strsql, "");
				if(list!=null){
				for(Map<String,Object> user:list){
					if(user.containsKey("ID")&&user.get("ID")!=null){
						String id =user.get("ID").toString();
						userMap.put(id, user);
					}
					
				}
				
				}
				return userMap;
			}
			//select * from rm_user where id in(select dept_leader_id from tm_company where ID=(select b.bus_dep from proj_released r,bus_bus_examine b where b.business_id=r.OPPORTUNITY_CODE AND r.ID='2018101300000000002'))
			//判断能力项目建议书对应的 需求部门负责人
			private void appendProManagerBySJProject(String projectID,Map<String,Map<String,Object>> groupUserMaps,List<Map<String,Object>> list) throws BusinessException
			{
				//查找产品部门负责人
				if(projectID!=null&&projectID.length()>0){
					Map<String,Map<String,Object>> companyUser=bulidProManagerMapBySJProject("",projectID);
					if(!companyUser.isEmpty()){
						for(String userId:companyUser.keySet()){
							list.add(companyUser.get(userId));
						}
					}
				}else
				{
					this.appendListByMap(list, groupUserMaps);
				}
				
			}
			//判断能力项目建议书对应的 需求部门负责人
			private Map<String,Map<String,Object>> bulidProManagerMapBySJProject(String type,String projectID) throws BusinessException{
				Map<String,Map<String,Object>> userMap =new HashMap<String,Map<String,Object>>();
				String strsql = "select * from rm_user where id in(select dept_leader_id from tm_company where ID=(select b.bus_dep from proj_released r,bus_bus_examine b where b.business_id=r.OPPORTUNITY_CODE AND r.ID='"+projectID+"'))";
				System.out.println("项目建议书商机报送单位负责人："+strsql);
				List<Map<String,Object>> list=getBaseDao().query(strsql, "");
				if(list!=null){
				for(Map<String,Object> user:list){
					if(user.containsKey("ID")&&user.get("ID")!=null){
						String id =user.get("ID").toString();
						userMap.put(id, user);
					}
				}
				
				}
				return userMap;
			}

			public Map<String, Object> findAuditUsers(String arg0, String arg1, String arg2) {
				// TODO Auto-generated method stub
				return null;
			}
}
