package com.yonyou.web;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.yonyou.business.button.util.ButForInsert;
import com.yonyou.business.entity.TokenEntity;
import com.yonyou.business.entity.TokenEntity.User;
import com.yonyou.business.entity.TokenUtil;
import com.yonyou.iuap.cache.CacheManager;
import com.yonyou.util.HttpRequest;
import com.yonyou.util.Import_Excel_SuperTools;
import com.yonyou.util.OutPrintWriterTools;
import com.yonyou.util.PropertyFileUtil;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.DataSourceCodeConstants;
import com.yonyou.web.entity.ResponseDataEntity;

/**
 * 导入
 * @author yansu
 *
 */
@RestController
@RequestMapping(value = "/import")
public class ImportController  implements DataSourceCodeConstants{

	private static Logger logger = LoggerFactory.getLogger(ImportController.class);
	
	@Autowired
	protected IBaseDao dcmsDAO;
	
	@Autowired
	protected CacheManager cacheManager;
	
	/**
	 * 批次号
	 */
	private String batchNo="";

	@RequestMapping("/test")
	public void uploadAffix(HttpServletRequest request,
			HttpServletResponse response){
		System.out.println("test");
	}
	
	/**
	 * 上传文档
	 * @param request
	 * @param response
	 * @param files
	 * @throws Exception
	 */
	@RequestMapping("/archive")
	public void affixArchive(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "files") MultipartFile files ,
			@RequestParam(value = "paramExtra") String paramExtra) throws Exception {
		JSONObject jo=new JSONObject();
		String bs_code= JSONObject.fromObject(paramExtra).getString("bs_code");//业务编码
		String batchno= JSONObject.fromObject(paramExtra).getString("batchno");//批次号
		logger.info("上传归档文件时额外传递的参数{}"+paramExtra);
		JSONObject returnJson=new JSONObject();
		HttpRequest http =new HttpRequest();
		String fileName = files.getOriginalFilename();
		System.out.println("提交来的文件名称{}"+fileName);
		String uploadAffixUrl = PropertyFileUtil.getValue("doc.url")+"/base?cmd=uploadAffix&batchNo=" + batchno + "&bs_code=" + bs_code;
		String result=http.httpPostByInputStreamUpload( uploadAffixUrl, files.getInputStream(), fileName,"files","");
		if (result!=null) {
			JSONObject jsonResult=JSONObject.fromObject(result);
			logger.info("访问文档服务器返回值{}" + jsonResult.toString());
			String batchno_=(String) jsonResult.get("batchno");
			String fileid=(String) jsonResult.get("fileid");
			String filename=(String) jsonResult.get("filename");
				jo.put("batchno", batchno_);
				jo.put("fileid", fileid);
				jo.put("filename", filename);
			returnJson.put("state", "success");
			returnJson.put("msg", "归档成功");
			returnJson.put("body", jo);
		}else{
			returnJson.put("state", "failed");
			returnJson.put("msg", "归档失败");
			returnJson.put("body", jo);
		}
		
		try {
			response.sendRedirect("/system/pages/importarchive.jsp?respData=" + URLEncoder.encode(returnJson.toString()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * 上传文档
	 * @param request
	 * @param response
	 * @param files
	 * @throws Exception
	 */
	@RequestMapping("/uploadAffix")
	public void uploadAffix(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "files") MultipartFile files ,
			@RequestParam(value = "paramExtra") String paramExtra) throws Exception {
		String bs_code= JSONObject.fromObject(paramExtra).getString("bs_code");//业务编码
		String batchno= JSONObject.fromObject(paramExtra).getString("batchno");//批次号
		logger.info("上传文件时额外传递的参数【业务编码】{}" + bs_code + "\t" + "上传文件时额外传递的参数【批次号】{}" + batchno);
		JSONObject returnJson=new JSONObject();
		HttpRequest http =new HttpRequest();
		String fileName = files.getOriginalFilename();
		System.out.println("提交来的文件名称{}"+fileName);
		String uploadAffixUrl =  PropertyFileUtil.getValue("doc.url")+"/base?cmd=uploadAffix&batchNo=" + batchno + "&bs_code=" + bs_code;
		String result=http.httpPostByInputStreamUpload( uploadAffixUrl, files.getInputStream(), fileName,"files","");
		if (result!=null) {
			JSONObject jsonResult=JSONObject.fromObject(result);
			logger.info("访问文档服务器返回值{}" + jsonResult.toString());
			String batchno1=(String) jsonResult.get("batchno");
			String fileid=(String) jsonResult.get("fileid");
			returnJson.put("state", true);
			returnJson.put("msg", "test");
			returnJson.put("body", "{\"batchno\":\"123456789\"}");
		}
		
		
		OutPrintWriterTools.outPrW(response, returnJson);
		
	}
	
	
	/**
	 * out页面
	 * @param response
	 * @param msg
	 * @param jsonV
	 * @throws IOException
	 * @throws ServletException 
	 */
	public void outPrintWriter(HttpServletRequest request, HttpServletResponse response, String msg,
			JSONObject jsonBody) throws IOException, ServletException {
		request.setCharacterEncoding("utf-8");
    	response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
//        PrintWriter out = response.getWriter();
//        out.println("<!DOCTYPE HTML PUBLIC \"-//WC//DTD HTML . Transitional//EN\">");
//        out.println("<HTML>");
//        out.println("  <HEAD><TITLE>导入结果</TITLE></HEAD>");
//        out.println("  <BODY>");
//        out.print("状态:" + msg);
//        out.print("<hr>");
//        out.print("    This is ");
//        out.print(this.getClass());
//        out.println(", using the GET method");
//        out.println("  </BODY>");
//        out.println("</HTML>");
//        out.flush();
//        out.close();
        
		StringBuffer sb=new StringBuffer();
		sb.append("<ul class=\"list-group\">");
		sb.append("<li class=\"list-group-item\"></li>");
		sb.append("</ul>");
		
		System.out.println("123"); 
		response.sendRedirect("/system/pages/excelImport.jsp?respData=" + "123456");
    }
	
	@SuppressWarnings("deprecation")
	private void sendRedirects(HttpServletResponse response ,ResponseDataEntity receiveVal) {
		// TODO Auto-generated method stub
		JSONObject jsr=new JSONObject();
		jsr.put("msg", receiveVal.getMessage());
		jsr.put("info", receiveVal.getBody());
		try {
			response.sendRedirect("/system/pages/excelImport.jsp?respData=" + URLEncoder.encode(jsr.toString()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 导入
	 * @param files
	 * @param pageCode
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "importExcel" ,produces = {"text/html;charset=utf-8"}) 
	public void importExcel(@RequestParam(value="files") MultipartFile files,String pageCode,HttpServletRequest request,HttpServletResponse response) throws Exception {
		ResponseDataEntity receiveVal;
		ButForInsert bfi=new ButForInsert();
		Map<String, Object> mapo=new HashMap<String, Object>();
		receiveVal=Import_Excel_SuperTools.ExcelImport(files, pageCode,bfi.No_Common(mapo, request));
		sendRedirects(response, receiveVal);
		
//		switch (pageCode.toUpperCase()) {
//		case "BD_SUPPLIER"://供应商目录
//			sendRedirects(response, receiveVal);
//			break;
//		case "BD_MATERIAL_V"://物资管理
//			sendRedirects(response, receiveVal);			
//			break;
//		case "EC_PURORDER_H"://订单采购
//			sendRedirects(response, receiveVal);			
//			break;
//
//		default:
//			break;
//		}
		
	}
	
	
	
	
	public String getBatchNo() {
		return batchNo;
	}


	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}


}
