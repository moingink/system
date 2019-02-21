package com.yonyou.util.mail;

import java.util.Properties;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class HrMailSenderImpl extends JavaMailSenderImpl {
	
	SimpleMailMessage mailMessage ;
	
	public HrMailSenderImpl(){
		super();
		Properties prop = new Properties() ;
		prop.put("mail.smtp.auth", "true") ;
		setJavaMailProperties(prop) ;
		mailMessage = new SimpleMailMessage();
	}
	
	public HrMailSenderImpl(String host,String username,String password){
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

	public void sendEmail() {
		try{
			send(mailMessage);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
