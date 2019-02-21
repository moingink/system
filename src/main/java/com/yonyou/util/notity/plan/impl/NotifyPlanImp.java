package com.yonyou.util.notity.plan.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.yonyou.iuap.persistence.vo.pub.BusinessException;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.notity.plan.NotifyPlanAbs;
import com.yonyou.util.notity.send.SendOperationAbs;
import com.yonyou.util.notity.template.TemplateAbs;
import com.yonyou.util.sql.SqlTableUtil;

public class NotifyPlanImp extends NotifyPlanAbs{
	
	
	public NotifyPlanImp(Map<String, Object> dataMap) {
		super(dataMap);
		// TODO 自动生成的构造函数存根
	}

	@Override
	protected List<Map<String,Object>> findNotifyUserList(String planId,String planCode,IBaseDao baseDao) throws BusinessException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		
		Map<String,List<String>> typeGroup =bulidGroupMap(this.findUserGroupList(planId,planCode, baseDao), "GROUP_TYPE", "GROUP_VALUE", "没有用户组信息！");
		List<Map<String,Object>> notifyUserList =new ArrayList<Map<String,Object>>();
		for(String type:typeGroup.keySet()){
			List<String> typeList =typeGroup.get(type);
			notifyUserList.addAll(this.bulidGroup(type).findUserGroup(baseDao,dataMap, typeList.toArray(new String[typeList.size()])));
		}
		return notifyUserList;
	}
	
	@Override
	protected Map<TemplateAbs,List<SendOperationAbs>> findTempSendMap(String planId,String planCode,IBaseDao baseDao) throws BusinessException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		Map<TemplateAbs,List<SendOperationAbs>> tempSendMap =new HashMap<TemplateAbs,List<SendOperationAbs>>();
		Map<String,List<String>> sendGroup =bulidGroupMap(findSendTempList(planId,planCode, baseDao), "TEMP_ID", "SEND_ID", "没有模板以及消息信息！");
		Map<String,TemplateAbs> tempCache =new HashMap<String,TemplateAbs>();
		Map<String,SendOperationAbs> sendCache =new HashMap<String,SendOperationAbs>();
		
		for(String tempId:sendGroup.keySet()){
			List<SendOperationAbs> sendList =new ArrayList<SendOperationAbs>();
			TemplateAbs tempImplKey =null;
			Set<SendOperationAbs> sendSet =new HashSet<SendOperationAbs>();
			if(!tempCache.containsKey(tempId)){
				String tempClassName =this.findStringBySelColumnMap(tempId, NF_TEMPLATE, "TEMP_CLASS", "根据模板ID没有找到模板信息", baseDao);
				SqlTableUtil tempTable =new SqlTableUtil("NF_TEMPLATE","").appendSelFiled(" * ").appendWhereAnd(" id ="+tempId);
				TemplateAbs tempImpl =this.bulidTmep(tempClassName);
				tempImpl.initTempData(baseDao.find(tempTable));
				tempCache.put(tempId, tempImpl);
			}
			for(String sendId:sendGroup.get(tempId)){
				SendOperationAbs sendImpl =null;
				if(!sendCache.containsKey(sendId)){
					String sendClassName =this.findStringBySelColumnMap(sendId, NF_SEND_OPERATION, "SEND_CLASS", "根据消息ID没有找到消息信息", baseDao);
					sendImpl =this.bulidSend(sendClassName);
					sendCache.put(sendId, sendImpl);
				}
				sendImpl =sendCache.get(sendId);
				if(!sendSet.contains(sendImpl)){
					sendList.add(sendImpl);
					sendSet.add(sendImpl);
				}
			}
			tempImplKey =tempCache.get(tempId);
			tempSendMap.put(tempImplKey, sendList);
		}
		
		return tempSendMap;
	}
	
	
	
	

}
