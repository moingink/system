package com.yonyou.util.notity.send.impl;

import java.util.HashMap;
import java.util.Map;

import com.yonyou.business.entity.TokenUtil;
import com.yonyou.iuap.persistence.vo.pub.BusinessException;
import com.yonyou.util.DateUtil;
import com.yonyou.util.notity.send.SendOperationAbs;
import com.yonyou.util.notity.send.SendTypeEnum;
import com.yonyou.util.notity.send.bulid.impl.ArrayListData;
import com.yonyou.util.notity.send.entity.SendOperationEntity;
import com.yonyou.util.wsystem.service.ORG;

public class SendForNotice extends SendOperationAbs<ArrayListData<Map<String,Object>>,Map<String,Object>>{


	@Override
	public SendTypeEnum findType() {
		// TODO 自动生成的方法存根
		return SendTypeEnum.NOTICE;
	}

	@Override
	protected void execute(SendOperationEntity entity,ArrayListData<Map<String, Object>> list) throws BusinessException {
		// TODO 自动生成的方法存根
		this.baseDao.insert(NF_NOTICE, list);
	}

	@Override
	protected ArrayListData<Map<String, Object>> initGather() {
		// TODO 自动生成的方法存根
		return new ArrayListData<Map<String, Object>>();
	}

	@Override
	protected Map<String, Object> bulidDataMessage(Map<String, Object> userMap,
			SendOperationEntity entity) throws BusinessException {
		Map<String,Object> insertMap =new HashMap<String,Object>();
		// TODO 自动生成的方法存根
		String user_id =(String)userMap.get(ORG.USER.ID.getCode());
		String user_name=(String)userMap.get(ORG.USER.NAME.getCode());
		insertMap.put("USRE_ID", user_id);
		insertMap.put("USER_NAME", user_name);
		insertMap.put("NOTICE_MESSAGE", entity.getSendMessage());
		insertMap.put("NOTICE_TYPE", entity.getType());
		insertMap.put("TITLE", entity.getTitle());
		insertMap.put("NOTICE_CODE",entity.getPlanKeyCode() );
		insertMap.put("USRE_ID", user_id);
		insertMap.put("IS_READ", "0");
		insertMap.put("DR", "0");
		insertMap.put("NOTICE_DATE", TokenUtil.findSystemSqlTimestamp());
		insertMap.put("STATE_DATE", TokenUtil.findSystemSqlTimestamp());
		this.appendParamColumn(entity, insertMap);
		return insertMap;
	}
	
	private void appendParamColumn(SendOperationEntity entity,Map<String,Object> insertMap){
		if(entity.getParamMap()!=null){
			Map<String,Object> paramMap =entity.getParamMap();
			if(paramMap.containsKey("STATE_DATE")){
				insertMap.put("STATE_DATE", DateUtil.findSqlDate((String)paramMap.get("STATE_DATE")));
			}
			if(paramMap.containsKey("END_DATE")){
				insertMap.put("END_DATE", DateUtil.findSqlDate((String)paramMap.get("END_DATE")));
			}
		}
	}




}
