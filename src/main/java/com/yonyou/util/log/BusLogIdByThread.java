package com.yonyou.util.log;

import java.net.InetAddress;
import java.net.UnknownHostException;
import com.yonyou.iuap.persistence.oid.DefaultIdProvider;

/** 
 * @author zzh
 * @version 创建时间：2017年3月2日
 * 类说明 
 */
public class BusLogIdByThread {
	
	 public static void writeBusLogId() {
			
			DefaultIdProvider ss = new DefaultIdProvider();			
			InetAddress addr=null;
			try {
				addr = InetAddress.getLocalHost();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			String ip=addr.getHostAddress();	
			StringBuffer strBuffer = new StringBuffer();
			strBuffer.append("BU:");
			strBuffer.append(ip);
			strBuffer.append(":");
			strBuffer.append(ss.generatorID("BusinessUUID"));		
			
			Thread.currentThread().setName(strBuffer.toString());
			System.out.println(Thread.currentThread().getName());

		}
	 
	 
	 public static  void writeBusLogId(String clientIP) {
			
			DefaultIdProvider ss = new DefaultIdProvider();			
			InetAddress addr=null;
			try {
				addr = InetAddress.getLocalHost();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			String ip=addr.getHostAddress();	
			StringBuffer strBuffer = new StringBuffer();
			strBuffer.append("BU:");
			strBuffer.append(ip);
			strBuffer.append(":");
			strBuffer.append(ss.generatorID("BusinessUUID"));		
				
			strBuffer.append(":"+clientIP);
			Thread.currentThread().setName(strBuffer.toString());

		}
	 
}
