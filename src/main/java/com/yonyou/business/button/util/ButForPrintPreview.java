package com.yonyou.business.button.util;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.itextpdf.text.DocumentException;
import com.yonyou.business.button.ButtonAbs;
import com.yonyou.util.BussnissException;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.printPreview.PrintPreviewUtil;

public class ButForPrintPreview extends ButtonAbs {

	@Override
	protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request, HttpServletResponse response) throws IOException, BussnissException {
		String dealWithClass = request.getParameter("dealWithClass");//打印预览处理类
		String pafName = request.getParameter("pafName");//pdf名称
		response.setContentType("application/pdf");
		response.addHeader("Content-Disposition", "inline; fileName="+new String(pafName.getBytes("UTF-8"),"ISO-8859-1")+".pdf");
		OutputStream os = null;
		try {
			if(StringUtils.isNoneBlank(dealWithClass)){
				PrintPreviewUtil printPreviewUtil = (PrintPreviewUtil)Class.forName(dealWithClass).newInstance();
				os = new BufferedOutputStream(response.getOutputStream());
				printPreviewUtil.createPdf(dcmsDAO, request, response, os);
	            os.flush();
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(os != null){
				os.close();
			}
		}
		return null;
	}

	@Override
	protected boolean befortOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	protected void afterOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}

}
