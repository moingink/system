package com.yonyou.util;

import com.yonyou.iuap.utils.PropertyUtil;

public final class RmConfigUtil {
	
	/**
	 * 
	* @Title: getClusterIdPrefix 
	* @Description: 取到当前的集群节点ID
	* @return
	 */
    public static String getClusterIdPrefix() {
    	return PropertyUtil.getPropertyByKey("ClusterIdPrefix");
    }
	
    /**
     * 
    * @Title: getIsClusterMode 
    * @Description: 是否集群模式 
    * @return
     */
    public static boolean getIsClusterMode(){
    	String isClusterMode = PropertyUtil.getPropertyByKey("IsClusterMode");
    	if("true".equalsIgnoreCase(isClusterMode)){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    /**
     * 
    * @Title: getSystemDebugMode 
    * @Description: 是否调试模式
    * @return
     */
    public static boolean getIsSystemDebugMode(){
    	String isSystemDebugMode = PropertyUtil.getPropertyByKey("SystemDebugMode");
    	if("true".equalsIgnoreCase(isSystemDebugMode)){
    		return true;
    	}else{
    		return false;
    	}
    }
}
