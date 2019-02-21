package com.yonyou.util.bus.bus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.yonyou.business.button.util.IPublicBusColumn;
import com.yonyou.util.RmIdFactory;
import com.yonyou.util.bus.AuditWorkEvent;
import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.sql.SqlWhereEntity;

public class busContractEntryImportWorkFlow extends AuditWorkEvent{
	
		@Override
		public String findAuditColumn() {
			// TODO 自动生成的方法存根
			return "BILL_STATUS";
		}
		
		//审批完成
		@Override
		public void executePassAudit(String busId, String nodeCode,
				Map<String, Object> dataMap) {
			String sql = "SELECT * FROM BUS_CONTRACT_ADMIN WHERE id = "+busId;
			List<Map<String,Object>> list = this.getBaseDao().query(sql,"");
			if(list.size()>0){
				/*String signTime = list.get(0).get("SIGN_TIME")!=null?list.get(0).get("SIGN_TIME").toString():null;//合同签订盖章日期
				if(StringUtils.isNoneBlank(signTime)){
					Map<String,Object> map = new HashMap<String, Object>();
					String id = list.get(0).get("ID")!=null?list.get(0).get("ID").toString():null;//合同Id
					String conId = list.get(0).get("CON_ID")!=null?list.get(0).get("CON_ID").toString():null;//合同编号
					String oriScript = list.get(0).get("ORI_SCRIPT")!=null?list.get(0).get("ORI_SCRIPT").toString():null;//合同原件份数
					String entryMode = list.get(0).get("ENTRY_MODE")!=null?list.get(0).get("ENTRY_MODE").toString():null;//录入方式
					map.put("CON_CODE",conId);
					map.put("CON_SIGN_DATE", signTime);
					String conSignPeople = "";
					List<Map<String,Object>> list1 = this.getBaseDao().query("SELECT r.id FROM rm_role r WHERE r.dr = '0' AND NAME = '公司合同管理员'","");
					if(list1.size()>0){
						List<Map<String,Object>> list2 = this.getBaseDao().query("SELECT u.NAME FROM rm_party_role pr,rm_user u WHERE pr.dr = '0' AND pr.role_id = '"+list1.get(0).get("ID")+"' AND u.id = pr.owner_party_id","");
						if(list2.size()>0){
							conSignPeople = list2.get(0).get("NAME")!=null?list2.get(0).get("NAME").toString():null;//公司合同管理员
						}
					}
					map.put("CON_SIGN_PEOPLE", conSignPeople);
					map.put("CON_SIGN_NUMBER", oriScript);
					map.put("CON_SIGN_STATUS", "2");
					map.put("CON_SEAL_DATE", signTime);
					map.put("CON_SEAL_STATUS", "0");
					map.put("PARENT_ID", id);
					map.put("SAVEORSUBMIT", "1");
					map.put("ENTRY_MODE", entryMode);
					map.put("ID", RmIdFactory.requestId("BUS_CONTRACT_SIGNANDSEAL", 1)[0]);
					//录入字段
					map.put(IPublicBusColumn.CREATOR_ID, list.get(0).get("CREATOR_ID"));
					map.put(IPublicBusColumn.CREATOR_NAME, list.get(0).get("CREATOR_NAME"));
					//组织字段
					map.put(IPublicBusColumn.ORGANIZATION_ID, list.get(0).get("ORGANIZATION_ID"));
					map.put(IPublicBusColumn.ORGANIZATION_NAME, list.get(0).get("ORGANIZATION_NAME"));
					List<Map<String,Object>> entityList = Arrays.asList(map);
					BaseDao.getBaseDao().insert("BUS_CONTRACT_SIGNANDSEAL", entityList);
					
					//更新合同信息
					Map<String,Object> udpate_messageMap = new HashMap<String, Object>();
					udpate_messageMap.put("ID",busId);
					udpate_messageMap.put("CON_SIGN_STATUS","2");
					udpate_messageMap.put("CON_SIGN_NUMBER",oriScript);
					udpate_messageMap.put("CON_SIGN_DATE",signTime);
					udpate_messageMap.put("CON_SEAL_STATUS","0");
					udpate_messageMap.put("CON_SEAL_DATE",signTime);
				    this.getBaseDao().update("BUS_CONTRACT_ADMIN", udpate_messageMap,new SqlWhereEntity());
				}*/
			}
			//更改审批状态
			Map<String,Object> udpate_messageMap = new HashMap<String, Object>();
			udpate_messageMap.put("ID",busId);
			udpate_messageMap.put("BILL_STATUS","3");
		    this.getBaseDao().update("BUS_CONTRACT_ADMIN", udpate_messageMap,new SqlWhereEntity());
		}	
}
