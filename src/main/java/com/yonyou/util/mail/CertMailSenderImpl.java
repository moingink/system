package com.yonyou.util.mail;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.internet.MimeUtility;

import org.apache.log4j.Logger;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**  
 *	  凭证信息邮件发送接口类
 *  @time 	2011-1-4
 * 	@author   guomao     
 */
public class CertMailSenderImpl extends JavaMailSenderImpl{
	private static Logger log = Logger.getLogger(CertMailSenderImpl.class);
	SimpleMailMessage mailMessage ;

	public CertMailSenderImpl() {
		super();
		Properties prop = new Properties() ;
		prop.put("mail.smtp.auth", "true") ;
		setJavaMailProperties(prop) ;
		mailMessage = new SimpleMailMessage();
	}
	public CertMailSenderImpl(String host,String username,String password){
		super();
		setHost(host);
		setUsername(username) ; 
		setPassword(password) ; 
		Properties prop = new Properties() ;
		prop.put("mail.smtp.auth", "true") ; 
		setJavaMailProperties(prop) ;
	}
	
	public SimpleMailMessage getMailMessage() {
		return mailMessage;
	}

	public void setMailMessage(SimpleMailMessage mailMessage) {
		this.mailMessage = mailMessage;
	}
	
	/**将邮件别名转码，使中文别名可正常显示
	 * @param fullName 包含或没有包含别名的单个邮箱地址
	 * @return 别名转码后的邮箱地址。
	 */
	public String tranferAlias(String fullName){
		String result=fullName;
		try {
			String fromAlias="";
			if(fullName.indexOf("<")!=-1){
				int dex=fullName.indexOf("<");
				fromAlias = MimeUtility.encodeText(fullName.substring(0, dex),"utf-8","Q"); 
				result=fromAlias+fullName.substring(dex);
			}
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		return result;
	}
	
	/** 用于导入总账凭证产生错误时，发送邮件
	 * @param provinceCode 省份代码
	 * @param text 邮件内容
	 */
//	public static void sendImportEmail(String provinceCode,String text){
//		log.info("sendImportEmail start with <provinceCode="+provinceCode+">, <context="+text+"> ...");
//		MailHelper mh=new MailHelper();
//		mh.init();
//		mh.setMailTo("ERP", provinceCode);
//		mh.sendMail("ERP", "<总账凭证生成服务>", text);
//		log.info("sendImportEmail end ...");
//	}
	
	/**用于验证总账凭证产生错误时，发送邮件
	 * @param provinceCode 省份代码
	 * @param text 邮件内容
	 */
//	public static void sendValidateEmail(String provinceCode,String text){
//		log.info("sendValidateEmail start with <provinceCode="+provinceCode+">, <context="+text+"> ...");
//		MailHelper mh=new MailHelper();
//		mh.init();
//		mh.setMailTo("ERP", provinceCode);
//		mh.sendMail("ERP", "<总账凭证验证服务>", text);
//		log.info("sendValidateEmail end ...");
//	}
	

	public void sendEmail() {
		try{
			send(mailMessage);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
