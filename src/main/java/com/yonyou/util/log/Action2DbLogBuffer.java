package com.yonyou.util.log;
/** 
 * @author zzh
 * @version 创建时间：2017年3月8日
 * 类说明 
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yonyou.util.SpringContextUtil;
import com.yonyou.util.buffer.CommonBuffer;
import com.yonyou.util.jdbc.BaseDao;

public class Action2DbLogBuffer extends CommonBuffer<HashMap> {
	
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	/**
	 * @return 缓冲器名称
	 */
	public String getName() {
		return "业务日志缓冲器";
	}
	
    public static BaseDao getBaseDao(){		
		return (BaseDao)SpringContextUtil.getBean("dcmsDAO");
	}


	@Override
	protected void flush(Queue<HashMap> buf) {
		try {
			
			List<Map<String, Object>> logs = new ArrayList<Map<String, Object>>();
			HashMap vo;
			while((vo=buf.poll()) != null) {
				logs.add(vo);
			}
			
			if(logs.size()==0){
				return ;
			}
			getBaseDao().insert(BusLogger.LOG_TABLE_NAME, logs);
			
		} catch (Throwable e) {
			e.printStackTrace();
			logger.error("flush(): " + e.toString());
		}
	}
}
