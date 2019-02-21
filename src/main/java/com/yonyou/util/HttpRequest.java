package com.yonyou.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URLEncoder;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yonyou.util.wsystem.api.HttpRequestAbs;
import com.yonyou.util.wsystem.util.SoaFlowEncapsulation;


public class HttpRequest extends HttpRequestAbs {

	// 系统管理
	static String SYSTEM_URL = "http://127.0.0.1:8080/system/service/System";

	private static Logger logger = LoggerFactory.getLogger(HttpRequest.class);

	/**
	 * HTTP请求，返回json数据
	 * @param url
	 * @param jsonString
	 * @return
	 */
	@Override
	public  String httpRespString(String url, String jsonString) {
		PostMethod post = new PostMethod(url);
		HttpClient client = new HttpClient();
		String repsString = null;
		post.setParameter("jsonData", jsonString);
		try {
			client.executeMethod(post);
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int code = post.getStatusCode();
		if (code == 200) {
			
			try {
				InputStream inputStream = post.getResponseBodyAsStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
				StringBuffer stringBuffer = new StringBuffer();
				String str = "";
				while ((str = br.readLine()) != null) {
					stringBuffer.append(str);
				}
				repsString = stringBuffer.toString();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else {
			logger.error("请求失败！请求地址：" + url + "请求数据：" + jsonString + "状态码：" + code);
			}
		
		post.releaseConnection();
		return repsString;
	}

	
	/**
	 * HttpClient post 传jsonString自动setParameter("jsonData", jsonString)
	 * @v 20180917 ys
	 * @param url
	 * @param jsonString
	 * @return
	 */
	public String httpToStringPost(String url, String jsonString){
		PostMethod post = new PostMethod(url);
		HttpClient client = new HttpClient();
		String repsString = null;
		jsonString=SoaFlowEncapsulation.encode(jsonString);
		System.out.println("请求加密：josnString"+jsonString);
		post.setParameter("jsonData", jsonString);
		try {
			client.executeMethod(post);
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int code = post.getStatusCode();
		if (code == 200) {
			
			try {
				InputStream inputStream = post.getResponseBodyAsStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
				StringBuffer stringBuffer = new StringBuffer();
				String str = "";
				while ((str = br.readLine()) != null) {
					stringBuffer.append(str);
				}
				repsString = stringBuffer.toString();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else {
			logger.error("请求失败！请求地址：" + url + "请求数据：" + jsonString + "状态码：" + code);
			}
		
		post.releaseConnection();
		return repsString;
	
	}
	


	/**
	 * HttpClientPost以二进制提交数据 (文件) 
	 * @param url
	 * @param fileInputStream 要传的文件的流
	 * @param fileName 文件名称@getisn()
	 * @param paramName Form表单提交的file框的name值
	 * @param paramExtraValues 附加参数值 (Key=paramExtra)
	 * @return 请求结果
	 */
	public String httpPostByInputStreamUpload(String url,
			InputStream fileInputStream, 
				String fileName,
					String paramName,
							String paramExtraValues) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        InputStream inputStream = null;
        String repsString = null;
      //设置请求的报文头部的编码
       // post.setHeader(new BasicHeader("Content-Type", "charset=utf-8"));

        //设置期望服务端返回的编码
       // post.setHeader(new BasicHeader("Accept", "text/plain;charset=utf-8"));
        try {
            inputStream = fileInputStream;
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            //第一个参数为 相当于 Form表单提交的file框的name值 第二个参数就是我们要发送的InputStream对象了
            //第三个参数是文件名
            //3)
            builder.addBinaryBody(paramName, inputStream, ContentType.create("multipart/form-data"), URLEncoder.encode(fileName));
//            //4)构建请求参数 普通表单项
            StringBody stringBody = new StringBody(paramExtraValues,ContentType.MULTIPART_FORM_DATA);
            builder.addPart("paramExtra",stringBody);
            HttpEntity entity = builder.build();
            post.setEntity(entity);
            //发送请求
            HttpResponse response = client.execute(post);
            //System.out.println("Status:"+response.getStatusLine().getStatusCode());
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
            	 entity = response.getEntity();
                 if (entity != null) {
                     inputStream = entity.getContent();
                     //转换为字节输入流
                     BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, Consts.UTF_8));
                     String body = "";
                     StringBuffer stringBuffer = new StringBuffer();
                     while ((body = br.readLine()) != null) {
                     	System.out.println(body);
                     	stringBuffer.append(body);
                     }
                     //logger.info( stringBuffer.toString() );
                     repsString = stringBuffer.toString();
                 }
            }else{
            	 logger.info( Thread.currentThread().getStackTrace()[1].getMethodName() +" 请求状态{}"+statusCode);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return repsString;
    }

 	public static String getisn(InputStream inputStream) throws IllegalArgumentException, IllegalAccessException{
	      Field[] fields = FileInputStream.class.getDeclaredFields();
	      String fileName=null;
	      for (int i = 0; i < fields.length; i++) {
	         if (fields[i].getName().equals("path")) {
	            fields[i].setAccessible(true);
	            fileName=new File((java.lang.String) fields[i].get(inputStream)).getName();
	            System.out.println(fileName);
	         }
	      }
	      return fileName;
  	}
	
	/**
	 * 测试方法
	 * 
	 * @param args
	 * @throws IOException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static void main(String[] args) throws IOException, IllegalArgumentException, IllegalAccessException {
		HttpRequest http =new HttpRequest();
//		//系统管理用户信息
//		String reqJsonString = "{'serviceCode':'001005001#001','customer':'','reqTime':'','serialNo':'','USERID':'1000201100000403588','COMPANYID':'wangwx25'}";
//		JSONObject jsonObject = http.httpRespJSON(SYSTEM_URL, reqJsonString);
//		System.out.println(jsonObject.get("retMsg"));
//		if(jsonObject.get("retCode").equals("000000")){
//			System.out.println(((JSONObject)jsonObject.get("data")).get("NAME"));
//		}
//		Map map = http.httpRespMap(SYSTEM_URL, reqJsonString);
//		System.out.println("httpRespMap" + map);
		
		FileInputStream fi=new FileInputStream("E:"+ File.separator +"123.xls");
		String param="{\"bs_code\":\"EC_PURORDER_H\",\"batchno\":\"222\"}";
		String res=http.httpPostByInputStreamUpload("http://localhost/system/import/uploadAffix", fi, "123.xls","files" ,param );
		if (res!=null) {
			System.out.println(res);
		}
		
		//System.out.println(Thread.currentThread().getName());
//		String batchno="";
//		String bs_code="BD_SUPPLIER";
//		String fieldCode="";
//		String uploadAffixUrl = "http://localhost/document/base?cmd=uploadAffix&fieldCode="+ fieldCode +"&batchNo=" + batchno + "&bs_code=" + bs_code;
//		String result=http.httpPostByInputStreamUpload( "http://localhost/system/import?uploadAffix", fi, "123.xls","files","");
//		System.out.println(result);
//		if ("".equals(result)) {
//			System.out.println();
//		}
		
	}

}