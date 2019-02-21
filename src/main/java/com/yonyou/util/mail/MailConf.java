/**
 * 
 */
package com.yonyou.util.mail;

import java.util.HashMap;
import com.yonyou.util.PropertyFileUtil;



public class MailConf {


	private static MailConf instance = new MailConf();

	public static MailConf getInstance(){
		return instance;
	}

	/**
	 * 获取邮件发送者
	 * KEY:host、user、pwd	
	 * @return
	 */
	public HashMap<String, String> getMailSender(){
		HashMap<String, String> map = new HashMap<String, String>();	
		map.put("host", PropertyFileUtil.getValue("mail.host"));
		map.put("user", PropertyFileUtil.getValue("mail.username"));
		map.put("pwd", PropertyFileUtil.getValue("mail.password"));
		return map;
	}
	/**
	 * 获取邮件头信息
	 * key:mailFrom、title
	 * @return
	 */
	public HashMap<String, String> getMailInfo(){
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("host", PropertyFileUtil.getValue("mail.host"));
		map.put("username", PropertyFileUtil.getValue("mail.username"));
		map.put("password", PropertyFileUtil.getValue("mail.password"));
		map.put("mailFrom", PropertyFileUtil.getValue("mail.from"));
		return map;
	}
	public String getMailRecive(String bizType,String province_code){
		String baseKey = "bizType."+bizType;
		String node = null;
		try{
			node = PropertyFileUtil.getValue(baseKey + "."+province_code);
			if(node==""){
				node = PropertyFileUtil.getValue(baseKey+".default");
			}
			return node;
		}catch(Exception e){
			e.printStackTrace();
			if(node==null){
				node = PropertyFileUtil.getValue(baseKey+".default");
				return node;
			}
		}finally{
			node = null;
		}

		return null;
	}
	
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(MailConf.getInstance().getMailSender());
		System.out.println(MailConf.getInstance().getMailInfo());
		System.out.println(MailConf.getInstance().getMailRecive("ERP", "10"));

	}
	

}
