package com.yonyou.util.printPreview;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPTable;
import com.yonyou.util.jdbc.IBaseDao;

/**
 * 打印预览工具类
 * @ClassName PrintPreviewUtil 
 * @date 2018年11月23日
 */
public abstract class PrintPreviewUtil {
	
	/**
	 * 打印字段替换
	 * @param listMap
	 * @param previewFiledList
	 * @param request
	 * @param response
	 * @return
	 */
	protected abstract List<String> replaceFiledList(List<Map<String, Object>> listMap, List<String> previewFiledList, IBaseDao dcmsDAO, HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * 创建pdf
	 * @param dcmsDAO
	 * @param request
	 * @param response
	 * @param os
	 * @throws DocumentException
	 * @throws IOException
	 */
	public abstract void createPdf(IBaseDao dcmsDAO, HttpServletRequest request, HttpServletResponse response, OutputStream os) throws DocumentException,IOException;

	/**
	 * 创建pdfTbale
	 * @param dcmsDAO
	 * @param request
	 * @param response
	 * @throws DocumentException
	 * @throws IOException
	 */
	protected abstract PdfPTable createPdfTbale(Font font, List<String> filedList, List<Map<String, Object>> listMap, IBaseDao dcmsDAO, HttpServletRequest request, HttpServletResponse response) throws DocumentException,IOException;
}
