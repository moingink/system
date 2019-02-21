package com.yonyou.util.notity.send;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yonyou.business.entity.TokenUtil;
import com.yonyou.iuap.persistence.vo.pub.BusinessException;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.notity.send.bulid.DataApi;
import com.yonyou.util.notity.send.entity.SendOperationEntity;
import com.yonyou.util.wsystem.service.ORG;

public abstract class SendOperationAbs<GATHER extends DataApi<DATA>,DATA> {
	
	protected final String NF_NOTIFY_LOG_DETAIL="NF_NOTIFY_LOG_DETAIL";
	protected final String NF_NOTICE="NF_NOTICE";
	protected IBaseDao baseDao;
	
	
	public void initBaseDao(IBaseDao _baseDao){
		baseDao =_baseDao;
	}
	
	public  void Send(SendOperationEntity entity) throws BusinessException{
		
		List<Map<String,Object>> logInsertList =new ArrayList<Map<String,Object>>();
		GATHER gather =this.initGather();
		this.bulid(entity, logInsertList,gather);
		this.Befort(entity);
		this.execute(entity,gather);
		this.After(entity);
		
	}
	
	protected void insertLog(List<Map<String,Object>> logInsertList){
		if(baseDao!=null){
			baseDao.insert(NF_NOTIFY_LOG_DETAIL, logInsertList);
		}
	}
	
	protected abstract void execute(SendOperationEntity entity,GATHER gather) throws BusinessException;
	
	protected abstract GATHER initGather();
	
	protected abstract DATA bulidDataMessage(Map<String,Object> userMap,SendOperationEntity entity) throws BusinessException;
	
	protected  abstract SendTypeEnum findType();
	
	public String finTypeValue(){
		return findType().getType();
	}
	
	protected void Befort(SendOperationEntity entity){
		
	}
	
	protected void After(SendOperationEntity entity){
		
	}
	
	protected void testPrint(String typeName,SendOperationEntity entity){
		System.out.println("------------------------"+typeName+"【STATE】-----------------------");
		for(Map<String,Object> userMap:entity.getUserGroupList()){
			System.out.printf("%s 信息内容：%s \n", userMap.get("NAME"),entity.getSendMessage());
		}
		System.out.println("------------------------"+typeName+"【END】-----------------------");
	}
	
	private void bulid(SendOperationEntity entity,List<Map<String,Object>> logInsertList,GATHER gather) throws BusinessException{
		
		List<Map<String, Object>> userGroupList =entity.getUserGroupList();
		if(userGroupList!=null&&userGroupList.size()>0){
			for(Map<String,Object> userMap:userGroupList){
				logInsertList.add(this.bulidLogMap(userMap, entity));
				DATA data =this.bulidDataMessage(userMap, entity);
				if(gather!=null&&data!=null){
					gather.addData(data);
				}
			}
		}else{
			throw new BusinessException("没有获取人员信息！");
		}
		
	}
	
	private Map<String,Object> bulidLogMap(Map<String,Object> userMap,SendOperationEntity entity){
		String user_id =(String)userMap.get(ORG.USER.ID.getCode());
		String user_name=(String)userMap.get(ORG.USER.NAME.getCode());
		Map<String,Object> insertMap =new HashMap<String,Object>();
		insertMap.put("LOG_ID", entity.getLogId());
		insertMap.put("USER_ID", user_id);
		insertMap.put("USER_NAME", user_name);
		insertMap.put("NOTIFY_CODE", entity.getPlanKeyCode());
		insertMap.put("LOG_TYPE", entity.getType());
		insertMap.put("SEND_DATE", TokenUtil.findSystemSqlTimestamp());
		insertMap.put("IS_READ", "0");
		return insertMap;
	}
	
}
