package com.yonyou.util;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.nosql.redis.JedisTemplate;

import com.yonyou.iuap.utils.PropertyUtil;
import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.jdbc.IBaseDao;

/**
 * 
* @ClassName: RmIdFactory 
* @Description: ID生成器单例Redis方式 
* @author zzh
* @date 2018年6月23日 
*
 */
public class RmIdFactory implements IRmIdFactory { 
	
	private static final Logger logger = LoggerFactory.getLogger(RmIdFactory.class);
	/**
	 * 批查询的数量
	 */
	private static int MAX_BATCH_SIZE = Integer.parseInt(PropertyUtil.getPropertyByKey("MAX_BATCH_SIZE"));
    
	/**
	 * 缓存redis的Key的前缀字符串，后接数据库表的名称
	 */
	private static String  REDIS_ID_PREFIX="RM_ID_"; 
	
	/**
	 * 
	 */
	private static int expiretime= 60*60*24*2;
	
	/**
	 * IP地址压缩成3位
	 */
	private static String ipCompress3 = "000";
	
	@Autowired
	JedisTemplate jedisTemplate;
	

	/**
	 * ID后几位每天从1计数，判断当天的id是否已经取过，当天已经取过缓存本地
	 */
	//private static ConcurrentHashMap<String, String> tableIDByDate= new ConcurrentHashMap<String, String>();
	
	/**
	 * 表名和前缀编码对应关系
	 */
	private static ConcurrentHashMap<String, String> table2TableCode= new ConcurrentHashMap<String, String>();
	
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void initBeanFactory() {
      
    }
    
    /**
     * IP地址压缩成3位
     * @return
     */
    private static String getIpCompress(){
    	
    	InetAddress addr = null;
    	String ipMultiplyStr =null;
		try {
			addr = InetAddress.getLocalHost();
			String ip = addr.getHostAddress();
			
			logger.info("数据库ID初始化获取服务器ip！"+ip);
			long ipMultiply= Integer.parseInt(ip.split("\\.")[0])*Integer.parseInt(ip.split("\\.")[1])*Integer.parseInt(ip.split("\\.")[2])*Integer.parseInt(ip.split("\\.")[3]);
			
			ipMultiplyStr = String.valueOf(ipMultiply);
			return ipMultiplyStr.substring(ipMultiplyStr.length()-3);
		} catch (Exception e) {
			//e.printStackTrace();
			return "000";
		}
		
		
		
    }
    
    String getSqlSelectMax(String tableName) {
        
		StringBuilder sql = new StringBuilder();
		sql.append("select ");
		sql.append("max(id) ");
		sql.append("MAX_ID from ");
		sql.append(tableName);		
		sql.append(" Where  date= '"+DateUtil.getNowDateYYYYMMDD()+"%' and server='"+ipCompress3+"' ");
		
    	return sql.toString();
    }
    
  
    /**
     * 
    * @Title: getIdFactory 
    * @Description: 获得单例
    * @return
     */
    public static IRmIdFactory getIdFactory() {
        if(!isInitId) {
            synchronized (RmIdFactory.class) {
                if(!isInitId) {
                	idFactory = (IRmIdFactory) SpringContextUtil.getBean("IRmIdFactory");
                	idFactory.initBeanFactory();                	
                    isInitId = true;
                    ipCompress3=getIpCompress();
                }
            }
        }
        return idFactory;
    }

    public synchronized String[] requestIdInner(String tableName, int length) {
		
    	if(null==jedisTemplate){    		
    		 logger.info("数据库ID初始化,没有获取Redis连接！", "没有获取Redis连接！");
	   		 logger.error("数据库ID初始化:没有获取Redis连接！");
	   		 return null;
   	    }
    	tableName="rm_id_cache";
    	IBaseDao dcmsDao = BaseDao.getBaseDao();
    	
        //redis缓存ID的计数
    	String redisSeq ="";
    	String prefix = tableName+DateUtil.getNowDateYYYYMMDD();
    	
    	String tempid=jedisTemplate.get(REDIS_ID_PREFIX+prefix);
    	if(null==tempid){
			
			String sqlSingle = getSqlSelectMax(tableName);
			
			List<Map<String, Object>> lResult= dcmsDao.query(sqlSingle, "MAX_ID");

			if(null==lResult || lResult.size()==0 || null==lResult.get(0).get("MAX_ID")){
				logger.info("数据库ID初始化"+"没有获取到"+tableName+"最大ID！", "没有获取到"+tableName+"最大ID！");
				jedisTemplate.setex(REDIS_ID_PREFIX+prefix,"0",expiretime);
				logger.info("数据库ID初始化"+"Redis设置"+tableName+"id:"+"0", "Redis设置"+tableName+"id:"+"0");
			}else{
				Long maxid=(Long)lResult.get(0).get("MAX_ID");
				if(maxid.toString().startsWith(DateUtil.getNowDateYYYYMMDD())){
					logger.info("数据库ID初始化"+"获取"+REDIS_ID_PREFIX+prefix+"最大id:"+maxid, "获取"+REDIS_ID_PREFIX+prefix+"最大id:"+maxid);
					jedisTemplate.setex(REDIS_ID_PREFIX+prefix,maxid.toString().substring(11).replaceAll("^(0+)", ""),expiretime);
					logger.info("数据库ID初始化"+"Redis设置"+REDIS_ID_PREFIX+prefix+"id:"+prefix,prefix.substring(11));
				}else{
					logger.info("数据库ID初始化"+"未获取"+REDIS_ID_PREFIX+prefix+"id:数据库最大id:"+maxid, "未获取"+REDIS_ID_PREFIX+prefix+"id:数据库最大id:"+maxid);
					jedisTemplate.setex(REDIS_ID_PREFIX+prefix,"0",expiretime);
					logger.info("数据库ID初始化"+"Redis设置"+REDIS_ID_PREFIX+prefix+"id:0", "Redis设置"+REDIS_ID_PREFIX+prefix+"id:0");
				}
			}
			redisSeq=jedisTemplate.incrBy(REDIS_ID_PREFIX+prefix,length).toString();
		}else{    
             if(tempid.startsWith("0")){
				
				tempid=tempid.replaceAll("^(0+)", "");
				jedisTemplate.setex(REDIS_ID_PREFIX+prefix,tempid,expiretime);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
			//服务器已经初始化当天id，但本地没有记录，该结点第一次获取该表记录 
			redisSeq=jedisTemplate.incrBy(REDIS_ID_PREFIX+prefix,length).toString();
			
		}
    	logger.info("获取数据库ID:"+"获取Redis最大id:"+redisSeq, "获取Redis最大id:"+redisSeq);    	
    	if(redisSeq.length()>7){    		
    		redisSeq=redisSeq.substring(3);    		
    	}
    	logger.info("获取数据库ID:"+"获取Redis最大id:"+redisSeq, "获取Redis最大id:"+redisSeq);    	
    	
    	//批量生成时的第一个id
    	long redisSeqInt=Long.parseLong(redisSeq)-length+1;
    	
        String[] ids = new String[length];
        String prefixstr=DateUtil.getNowDateYYYYMMDD();
        //批量获取id
        for (int i = 0; i < ids.length; i++) {    
        	//根据数据库表编码+8位年月日+每天从1计数 
        	ids[i]=prefixstr+ipCompress3+String.format("%8d", redisSeqInt+i).replace(" ", "0");
        	ids[i]=ids[i].replace("null", "000");
		}
       

        String sql="INSERT INTO rm_id_cache (ID, DATE, SERVER) VALUES ('"+ids[ids.length-1]+"', '"+DateUtil.getNowDateYYYYMMDD()+"', '"+ipCompress3+"');";
        dcmsDao.updateBySql(sql);
        
        return ids;
    }
    
    
    
    /**
     * 批量获取唯一ID
     * @param tableName 表名
     * @param length 批量数
     * @return 返回内存中自增长的ID，未找到返回null
     */
    public static String[] requestId(String tableName, int length) {
    	if(length < 1) {
    		return new String[0];
    	}
    	return getIdFactory().requestIdInner(tableName.toUpperCase(), length);
    }
    
	/**
	 * 全局单例
	 */
	private static IRmIdFactory idFactory = null;
	/**
	 * 全局单例的初始化标记，用于双检锁安全判断
	 */
	private static boolean isInitId = false;
	
	
	public static void main(String[] args) {
		
	}

}