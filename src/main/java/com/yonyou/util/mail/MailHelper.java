package com.yonyou.util.mail;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;




public class MailHelper extends JavaMailSenderImpl{
		
	private static SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static Logger logger = Logger.getLogger(MailHelper.class);
	private SimpleMailMessage mailMessage ;
	private void  initMailConf(String host,String username,String password)
	{	
		setDefaultEncoding("utf-8");
		setHost(host);
		setUsername(username) ; 
		setPassword(password) ; 
		Properties prop = new Properties() ;
		prop.put("mail.smtp.auth", "true") ; // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确
		setJavaMailProperties(prop) ;
	}
	public void sendMail(String bizType,String title, String sendMessage,String to){
		String mailText = getMailTextByErrorInfo(bizType,title,sendMessage,to);
		this.mailMessage.setText(mailText);
		this.mailMessage.setSubject(title);

		this.mailMessage.setTo(to.split(";"));
		try {
			this.send(mailMessage);
			logger.info("邮件发送成功！");
		} catch (MailException e) {
			logger.info("邮件发送失败！\r\n"+e.getMessage());
			e.printStackTrace();
		}
	}
	
	public boolean sendMailSuper(String bizType,String title, String sendMessage,String to){
		boolean res=false;
		String mailText = getMailTextByErrorInfo(bizType,title,sendMessage,to);
		this.mailMessage.setText(mailText);
		this.mailMessage.setSubject(title);

		this.mailMessage.setTo(to.split(";"));
		try {
			this.send(mailMessage);
			logger.info("邮件发送成功！");
			res=true;
		} catch (MailException e) {
			logger.info("邮件发送失败！\r\n"+e.getMessage());
			e.printStackTrace();
		}
		return res;
	}
	
	/**
	 * 带抄送人
	 * @param bizType
	 * @param title
	 * @param sendMessage
	 * @param to
	 * @param cc
	 * @return res
	 */
	public boolean sendMail(String bizType,String title, String sendMessage,String to ,String cc ){
		boolean res=false;
		String mailText = getMailTextByErrorInfo(bizType,title,sendMessage,to);
		this.mailMessage.setText(mailText);
		this.mailMessage.setSubject(title);
		this.mailMessage.setCc(cc);

		this.mailMessage.setTo(to.split(";"));
		try {
			this.send(mailMessage);
			logger.info("邮件发送成功！");
			res=true;
		} catch (MailException e) {
			logger.info("邮件发送失败！\r\n"+e.getMessage());
			e.printStackTrace();
		}
		return res;
	}
	/**
	 * 拼装错误信息
	 * @param bizType     --业务类型 ERP、HR、OA等等
	 * @param serviceName --服务名称
	 * @param errorInfo   --错误信息
	 * @return            --返回拼装的错误信息
	 */
	private static String getMailTextByErrorInfo(String bizType,String title, String sendMessage,String to){
		StringBuffer mailText = new StringBuffer();
		mailText.append(sendMessage);
		return mailText.toString();
	}


	

	public SimpleMailMessage getMailMessage() {
		return mailMessage;
	}

	public void setMailMessage(SimpleMailMessage mailMessage) {
		this.mailMessage = mailMessage;
	}

	public void sendEmail() {
		try{
			send(mailMessage);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public SimpleMailMessage init(){
		Map<String,String> mailInfo=MailConf.getInstance().getMailInfo();
		String host=mailInfo.get("host");
		String username=mailInfo.get("username");
		String password=mailInfo.get("password");
		String mainFrom=mailInfo.get("mailFrom");
		initMailConf(host,username,password);
		SimpleMailMessage mailMessage=new SimpleMailMessage();
		mailMessage.setFrom(mainFrom);
		setMailMessage(mailMessage);
		//this.mailMessage=mailMessage;
		return mailMessage;
	}

}
