package org.util;

import java.util.HashMap;
import java.util.Map;

import com.yonyou.util.jdbc.RmJdbcTemplate;

public class RmPartyConstants {

	public static String viewId = "1000200700000000001";
	//团体相关表名
	public static String tmCompany = "TM_COMPANY";
	public static String tmDepartment = "TM_DEPARTMENT";
	public static String rmParty = "RM_PARTY";
	public static String rmPartyRelation = "RM_PARTY_RELATION";
	public static String rmPartyView = "RM_PARTY_VIEW";
	public static String rmPartyType = "RM_PARTY_TYPE";
	public static String rmPartyRole = "RM_PARTY_ROLE";
	//权限管理相关表
	public static String rmRole = "RM_ROLE";
	public static String rmRoleMutex = "RM_ROLE_MUTEX";
	public static String rmFunctionNode = "RM_FUNCTION_NODE";
	public static String rmAuthorize = "RM_AUTHORIZE";
	public static String rmAuthorizeResource = "RM_AUTHORIZE_RESOURCE";
	public static String rmAuthorizeResourceRecord = "RM_AUTHORIZE_RESOURCE_RECORD";
	
	
	public static String rmUser = "RM_USER";
	public static String rmUserOnlineRecord = "RM_USER_ONLINE_RECORD";
	public static String rmPasswordLogin = "RM_PASSWORD_LOGIN";
	public static String rmPasswordHistory = "RM_PASSWORD_HISTORY";
	public static String rmPasswordStrategy = "RM_PASSWORD_STRATEGY";
	public static String rmPasswordStrategyUser = "RM_PASSWORD_STRATEGY_USER";
	
    public final static String RM_YES = "1";  //是的定义
    public final static String RM_NO = "0";  //否的定义
	//相关数据源
	
	
	
	public static Map<String,String> viewMap = new HashMap<String,String>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("A","1000200700000000001");//门户系统组织
			put("B","1000200700000000002");//资金业务主体关系
			put("C","1000200700000000003");//资金管理系统组织
		}
		/*DEFAULT("1000200700000000003", "资金组织结构视图"),
    	OA("1000200700000000001", "门户组织结构视图"),
    	YSJH("1000200700000000003", "营收稽核组织结构视图"),
    	TM_PLAN("1000200700000000002", "资金计划组织结构视图"); */
	};
	
	/**
     * 定义视图
     */
    public enum PartyView { 
    	DEFAULT("1000200700000000003", "资金组织结构视图"),
    	OA("1000200700000000001", "门户组织结构视图"),
    	YSJH("1000200700000000003", "营收稽核组织结构视图"),
    	TM_PLAN("1000200700000000002", "资金计划组织结构视图"); 
    	private String id;
    	private String viewName;
    	PartyView(String id_, String viewName_) {
    		id = id_;
    		viewName = viewName_;
    	}
    	
		/**
		 * 返回视图的ID
		 * @return
		 */
		public String id() {
			return id;
		}
		
		/**
		 * 返回视图名称
		 * @return
		 */
		public String viewName() {
			return viewName;
		}
    }
	
	/**
     * 组织类型
     */
    public enum PartyType {
    	PLAN_SUBJECT("1000200800000000014", "主体"),
    	TM_COMPANY("1000200800000000001", "公司"),
    	TM_DEPARTMENT("1000200800000000002", "部门"),
    	RM_ROLE("1000200800000000008", "角色"),
    	TM_EMPLOYEE("1000200800000000003", "员工"),
    	RA_BASE_BUSINESS_OFFICE_INFO("1000200800000000004", "营业厅"),
    	RM_USER("1000200800000000013", "用户"),
    	ORG_VIRTUAL_NODE("1000200800000000015", "虚节点");
    	private String id;
    	private String typeName;
    	PartyType(String id, String typeName){
    		this.id = id;
    		this.typeName = typeName;
    	}
    	
		/**
		 * 返回组织类型的ID
		 * @return
		 */
		public String id() {
			return id;
		}
		
		/**
		 * 返回组织类型名称
		 * @return
		 */
		public String typeName() {
			return typeName;
		}
    }
    
    /**
     * 定义权限类别
     */
    public enum Authorize {
    	FUNCTION_NODE("FUNCTION_NODE", "1000202400000000001", "菜单树权限"),
    	DA_ACCOUNT("DA_ACCOUNT", "1000202400000000002", "数据权限（账户）");
    	private String bsKeword;
    	private String id;
    	private String authorizeName;
    	Authorize(String bsKeword_, String id_, String authorizeName_) {
    		bsKeword = bsKeword_;
    		id = id_;
    		authorizeName = authorizeName_;
    	}
		/**
		 * 返回权限类别的关键字
		 * @return
		 */
		public String bsKeyword() {
			return bsKeword;
		}
		/**
		 * 返回权限类别的id
		 * @return
		 */
		public String id() {
			return id;
		}
		/**
		 * 返回权限类别的名称
		 * @return
		 */
		public String authorizeName() {
			return authorizeName;
		}
    }
    
    /**
     * 遍历上级团体关键sql
    * @param partyId 当前团体ID
    * @return
     */
    public static String recurPartySql(String partyId){
    	String sql = "";
    	if (RmJdbcTemplate.dataBase.equals("MySQL")) {
			sql = "AND FIND_IN_SET(CHILD_PARTY_ID,getParentParty(" + partyId + "," + viewId + "))";
		}else{
			//默认oracle
			sql = "START WITH rr.child_party_id = '" + partyId + "' CONNECT BY nocycle PRIOR rr.parent_party_id = rr.child_party_id";
		}
    	return sql;
    }
    
}
