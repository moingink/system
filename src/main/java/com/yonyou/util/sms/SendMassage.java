package com.yonyou.util.sms;

import java.util.Properties;
import javax.xml.namespace.QName;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.dom4j.DocumentHelper;
import com.yonyou.util.PropertyFileUtil;


public class SendMassage {
	public static RPCServiceClient serviceClient = null;
	
	
	public String sendMassage(SmsVo vo) throws Exception{
		String result="1";
		String errList=new String();
		if(vo.getContent() == null){
			errList += "内容不能为空";
			result = "0";
		}
		
		if(vo.getName() == null){
			errList += "姓名不能为空";
			result = "0";
		}
		

		
		if(vo.getMobileNumber() == null || vo.getMobileNumber().trim().length()==0){
			errList +="电话号码为空";
			result = "0";
		}else if (vo.getMobileNumber().trim().length()<11){
			errList +="电话号码位数不符";
			result = "0";
		}
		
		if(result.equals("1")){
			Properties prop = new Properties();
			
			serviceClient = getRpcServiceClient(prop);
			// 指定getGreeting方法的参数值
			String str="<request><strContent>"+vo.getContent()+"</strContent><strmobileNumber>"+vo.getMobileNumber()+"</strmobileNumber><senderName>"+vo.getName()+"</senderName><userId>"+vo.getId()+"</userId><customerId>"+prop.getProperty("sms.customerId")+"</customerId><customerPassport>"+prop.getProperty("sms.customerPassport")+"</customerPassport></request>";
			Object[] opAddEntryArgs = new Object[] {str};
			// 指定getGreeting方法返回值的数据类型的Class对象
			Class[] classes = new Class[] {String.class};
			// 指定要调用的getGreeting方法及WSDL文件的命名空间
			QName opAddEntry = new QName("http://service.com", "userAppSmsSend");
			//调用 getGreeting方法并输出该方法的返回值
			String msg="";
			try {
				msg = String.valueOf(serviceClient.invokeBlocking(opAddEntry,
						opAddEntryArgs, classes)[0]);
				return DocumentHelper.parseText(msg).valueOf("records/returnvalue");
			} catch (Exception e) {
				serviceClient=null;
				serviceClient = getRpcServiceClient(prop);
			}			
		}
		return result;
	}


	private RPCServiceClient getRpcServiceClient(Properties prop)
			throws AxisFault {
		if(serviceClient!=null){
			return serviceClient;
		}
		// 使用 RPC 方式调用WebService
		serviceClient = new RPCServiceClient();
		Options options = serviceClient.getOptions();
		// 指定调用WebService的URL
		EndpointReference targetEPR = new EndpointReference(PropertyFileUtil.getValue("sms.URL"));
		options.setTo(targetEPR);
		return serviceClient;
	}	
		private String toReturn(String result,String errList){
			return "result="+result+";errList="+errList;
	}

}