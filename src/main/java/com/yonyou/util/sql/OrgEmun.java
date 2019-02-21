package com.yonyou.util.sql;

public enum OrgEmun {
	
	  USER("RM_USER","US"
			  
			  ,"ID,"
			 + "NAME,"
			 + "LOCK_STATUS,"
			 + "LOGIN_ID,"
			 + "PASSWORD,"
			 + "AUTHEN_TYPE,"
			 + "ORGANIZATION_ID,"
			 + "EMPLOYEE_ID,"
			 + "EMAIL,CUSTOM3,ADMIN_TYPE,"
			 + "DESCRIPTION,"
			 + "AGENT_STATUS,"
			 + "LOGIN_STATUS,"
			 + "LAST_LOGIN_DATE,"
			 + "LAST_LOGIN_IP,"
			 + "LOGIN_SUM,"
			 + "LAST_CUSTOM_CSS,"
			 + "IS_AFFIX,"
			 + "FUNCTION_PERMISSION,"
			 + "DATA_PERMISSION,"
			 + "USABLE_STATUS,"
			 + "MODIFY_DATE,"
			 + "MODIFY_IP,"
			 + "MODIFY_USER_ID",""),
			 
			 
	  COMPANY("WOMEN","W"
			  ,"W1,W2,W3,W4,W5","");
      
      private final SqlTableUtil tableSource;

      //构造器默认也只能是private, 从而保证构造函数只能在内部使用
      OrgEmun(String table,String alis_table,String selFileds,String where) {
    	  tableSource =new SqlTableUtil(table,alis_table);
    	  tableSource.init(selFileds, where);
      }
      
      public SqlTableUtil getValue() {
          return tableSource;
      }
      
      
	 
}
