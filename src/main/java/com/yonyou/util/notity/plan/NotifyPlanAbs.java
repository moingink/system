package com.yonyou.util.notity.plan;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yonyou.business.entity.TokenUtil;
import com.yonyou.iuap.persistence.vo.pub.BusinessException;
import com.yonyou.util.BussnissException;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.notity.group.UserGroupAbs;
import com.yonyou.util.notity.group.UserGroupFactory;
import com.yonyou.util.notity.plan.entity.ClassInit;
import com.yonyou.util.notity.send.SendOperationAbs;
import com.yonyou.util.notity.send.entity.SendOperationEntity;
import com.yonyou.util.notity.template.TemplateAbs;
import com.yonyou.util.notity.template.entity.TempEntity;
import com.yonyou.util.sql.SqlTableUtil;

public abstract class NotifyPlanAbs {
	
	protected  final String NF_USER_GROUP ="NF_USER_GROUP";
	protected  final String NF_NOTIFY_PLAN_SEND_TEMP="NF_NOTIFY_PLAN_SEND_TEMP";
	protected  final String NF_TEMPLATE ="NF_TEMPLATE";
	protected  final String NF_SEND_OPERATION ="NF_SEND_OPERATION";
	protected  final String NF_NOTIFY_LOG ="NF_NOTIFY_LOG";
	protected  final String NF_NOTIFY_PLAN ="NF_NOTIFY_PLAN";
	
	protected  Map<String,Object> dataMap =new HashMap<String,Object>();
	
	public NotifyPlanAbs(Map<String,Object> dataMap){
		this.dataMap =dataMap;
	}
	
	public  void Notify(String planCode,Map<String,Object> dataMap,IBaseDao baseDao) throws BussnissException{
		List<Map<String,Object>> userList =null;
		Map<TemplateAbs,List<SendOperationAbs>> tempSendMap =null;
		String error ="";
		try {
			String planId=this.findPlanId(planCode, baseDao);
			userList =this.findNotifyUserList(planId,planCode, baseDao);
			tempSendMap =this.findTempSendMap(planId,planCode, baseDao);
			for(TemplateAbs tempImpl:tempSendMap.keySet()){
				String planKeyCode =this.findPlanPkCode(planCode, dataMap);
				TempEntity tempEntity =tempImpl.bulidTemp(dataMap);
				SendOperationEntity entity =new SendOperationEntity(userList,planKeyCode,tempEntity.getMessage(),tempEntity.getHead(),"");
				entity.setParamMap(dataMap);
				for(SendOperationAbs send:tempSendMap.get(tempImpl)){
					tempEntity.setType(send.finTypeValue());
					String logId=this.recordLog(planKeyCode,tempEntity,baseDao);
					entity.setLogId(logId);
					entity.setType(send.finTypeValue());
					send.initBaseDao(baseDao);
					send.Send(entity);
				}
			}
		} catch (InstantiationException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			error=e.getMessage();
		} catch (IllegalAccessException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			error=e.getMessage();
		} catch (ClassNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			error=e.getMessage();
		} catch (BusinessException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			error=e.getMessage();
		}
		if(error.length()>0){
			throw new BussnissException(error);
		}
	}
	
	
	protected String recordLog(String notifyCode,TempEntity tempEntity,IBaseDao baseDao){
		
		Map<String,Object> insertMap =new HashMap<String,Object>();
		insertMap.put("NOTIFY_CODE", notifyCode);
		insertMap.put("TITLE", tempEntity.getHead());
		insertMap.put("LOG_MESSAGE", tempEntity.getMessage());
		insertMap.put("LOG_DATE", TokenUtil.findSystemSqlTimestamp());
		insertMap.put("LOG_STATE", "1");
		insertMap.put("LOG_TYPE", tempEntity.getType());
		
		return baseDao.insert(NF_NOTIFY_LOG, insertMap);
	}



	
	protected abstract List<Map<String,Object>> findNotifyUserList(String planId,String planCode,IBaseDao baseDao) throws BusinessException, InstantiationException, IllegalAccessException, ClassNotFoundException;
	
	protected abstract Map<TemplateAbs,List<SendOperationAbs>> findTempSendMap(String planId,String planCode,IBaseDao baseDao) throws BusinessException, InstantiationException, IllegalAccessException, ClassNotFoundException;
	
	
	protected UserGroupAbs bulidGroup(String type) throws InstantiationException, IllegalAccessException, ClassNotFoundException, BusinessException{
		return UserGroupFactory.findGroup(type);
	}
	
	protected TemplateAbs bulidTmep(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException, BusinessException{
		ClassInit<TemplateAbs> tmep =new ClassInit<TemplateAbs>();
		return tmep.newInstance(className);
	}

	protected SendOperationAbs bulidSend(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException, BusinessException{
		ClassInit<SendOperationAbs> send =new ClassInit<SendOperationAbs>();
		return send.newInstance(className);
	}
	
	
	protected Map<String,List<String>> bulidGroupMap(List<Map<String,Object>> list,String keyName,String valueName,String error) throws BusinessException{
		Map<String,List<String>> typeGroup =new HashMap<String,List<String>>();
		if(list!=null&&list.size()>0){
			for(Map<String,Object> dataMap:list){
				String type ="";
				String value ="";
				if(dataMap.containsKey(keyName)&&dataMap.containsKey(valueName)){
					type =dataMap.get(keyName).toString();
					value =dataMap.get(valueName).toString();
				}else{
					throw new BusinessException(String.format("dataMap 对象没有 没有 【%s、%s】属性信息，dataMap信息 %s", keyName,valueName,dataMap));
				}
				if(typeGroup.containsKey(type)){
					typeGroup.get(type).add(value);
				}else{
					List<String> values =new ArrayList<String>();
					values.add(value);
					typeGroup.put(type, values);
				}
			}
			return typeGroup;
		}else{
			throw new BusinessException(error+" 没有获取相关数据 ");
		}
	}
	
	protected String findStringBySelColumnMap(String id,String tableName,String selColumn,String errorMessage,IBaseDao baseDao) throws BusinessException{
		SqlTableUtil tempTable =new SqlTableUtil(tableName,"");
		tempTable.appendSelFiled(selColumn).appendSelFiled("ID")
			     .appendWhereAnd(" id ="+id);
		Map<String,Object> dataMap =baseDao.find(tempTable);
		if(dataMap!=null&&dataMap.size()>0){
			String returnString =(String)dataMap.get(selColumn);
			if(returnString!=null&&returnString.length()>0){
				return returnString;
			}else{
				throw new BusinessException(tableName+" 获取值 "+selColumn +" 内容为空");
			}
		}else{
			throw new BusinessException(errorMessage+ " id = "+id);
		}
	}
	
	protected List<Map<String,Object>> findSendTempList(String planId,String planCode,IBaseDao baseDao){
		String alias_table_name ="send_temp";
		SqlTableUtil sendTemp =new SqlTableUtil("NF_NOTIFY_PLAN_SEND_TEMP","send_temp");
		sendTemp.appendSelFiled(alias_table_name, "SEND_ID").appendSelFiled(alias_table_name, "TEMP_ID")
				.appendWhereAnd(alias_table_name+".PLAN_ID ='"+planId+"'");
		this.appendDrWhere(sendTemp);
		return baseDao.query(sendTemp);
	}
	
	
	protected List<Map<String,Object>> findUserGroupList(String planId,String planCode,IBaseDao baseDao){
		String alias_table_name ="g";
		String subquery =" select ng.GROUP_ID from NF_NOTIFY_USER_GROUP ng where ng.PLAN_ID = '"+planId+"'";
		SqlTableUtil userTable =new SqlTableUtil(NF_USER_GROUP,alias_table_name);
		userTable.appendSelFiled(alias_table_name, "GROUP_TYPE")
				 .appendSelFiled(alias_table_name, "GROUP_VALUE")
				 .appendWhereAnd(alias_table_name+".id in ("+subquery+")");
		this.appendDrWhere(userTable);
		List<Map<String,Object>> groupList =baseDao.query(userTable);
		return groupList;
	}
	
	private String findPlanId(String planCode,IBaseDao baseDao) throws BussnissException{
		String planId ="";
		SqlTableUtil table =new SqlTableUtil(NF_NOTIFY_PLAN,"");
		table.appendSelFiled("ID").appendWhereAnd(" PLAN_CODE ='"+planCode+"'");
		this.appendDrWhere(table);
		Map<String,Object> dataMap =baseDao.find(table);
		
		if(dataMap.size()>0){
			if(dataMap.containsKey("ID")){
				 planId =dataMap.get("ID").toString();
			}
		}else{
			throw new BussnissException(String.format("没有根据 PLAN_CODE [%S] 获取到计划信息", planCode));
		}
		return planId;
	}
	
	private void appendDrWhere(SqlTableUtil table){
		if(table!=null){
			String drWhere ="DR IS NULL";
			String alias =table.getAlias_table_name();
			if(alias!=null&&alias.length()>0){
				drWhere=alias+"."+drWhere;
			}
			table.appendWhereAnd(drWhere);
		}
	}
	
	
	private String findPlanPkCode(String planCode,Map<String,Object> dataMap){
		String markKey ="PUBLIC_PLANPKCODE";
		String planPkCode =planCode+"_"+(new Date()).getTime();
		if(dataMap.containsKey(markKey)){
			return (String)dataMap.get(markKey);
		}
		return planPkCode;
		
	}
}
