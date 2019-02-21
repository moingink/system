package com.yonyou.util.printPreview.impl.workFlowPor;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.printPreview.PrintPreviewUtil;
import com.yonyou.util.sql.SqlTableUtil;

@SuppressWarnings("all")
public class WorkFlowPorcessPrintPreviewUtil extends PrintPreviewUtil{

	@Override
	protected List<String> replaceFiledList(List<Map<String, Object>> listMap,
			List<String> previewFiledList, IBaseDao dcmsDAO,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createPdf(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response, OutputStream os)
			throws DocumentException, IOException {
		
		/*创建文档对象*/
		Document document = new Document(PageSize.A4, 10, 10, 50, 10);
		PdfWriter.getInstance(document, os);
        document.open();
        BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        Font font = new Font(baseFont, 11);
        
        String needProcessCode = request.getParameter("needProcessCode");
        String busId = request.getParameter("busId");
        
        /*标题*/
        document.addTitle("审批进程");
        
        /*表格*/
	        //打印字段
	        List<String> auditFiledList = Arrays.asList("NEED_USER_NAME,TASK_NAME,AUDIT_MESSAGE,TASK_OPERATION_NAME,NEED_END_TIME".split(","));
	        SqlTableUtil table =new SqlTableUtil("WF_NEED_HANDLE", "");
			table.appendSelFiled("*")
				 .appendWhereAnd(" need_state in ('2','1','4') ")
				 .appendWhereAnd(" NEED_PROCESS_CODE ='"+needProcessCode+"'")
				 .appendWhereAnd(" BUS_ID ='"+busId+"'")
				 .appendOrderBy("need_end_time", "DESC");
			List<Map<String, Object>> auditListMap = dcmsDAO.query(table);
			String title = auditListMap.get(0).get("TITLE")!=null?auditListMap.get(0).get("TITLE").toString():"";
			
			Paragraph titleParagraph = new Paragraph(title,new Font(baseFont, 20, Font.BOLD));
			titleParagraph.setAlignment(1);// 对齐方式(1为居中对齐、2为右对齐、3为左对齐)
			titleParagraph.setSpacingAfter(30);// 段落间距
	        document.add(titleParagraph);
			
			PdfPTable auditPdfPTable = createPdfTbale(font, auditFiledList, auditListMap, dcmsDAO, request, response);
			document.add(auditPdfPTable);
        document.close();
	}

	@Override
	protected PdfPTable createPdfTbale(Font font, List<String> filedList,
			List<Map<String, Object>> listMap, IBaseDao dcmsDAO,
			HttpServletRequest request, HttpServletResponse response)
			throws DocumentException, IOException {
		
		PdfPTable pdfPTable = new PdfPTable(5);
		pdfPTable.setTotalWidth(550);
		pdfPTable.setLockedWidth(true);
		
		//表头
		for(int i=0; i<filedList.size(); i++){
			String fieldCode = filedList.get(i);
			String fieldName = "";
			if("NEED_USER_NAME".equals(fieldCode)){
				fieldName = "处理人";
			}else if("TASK_NAME".equals(fieldCode)){
				fieldName = "岗位";
			}else if("AUDIT_MESSAGE".equals(fieldCode)){
				fieldName = "审批意见";
			}else if("TASK_OPERATION_NAME".equals(fieldCode)){
				fieldName = "处理状态";
			}else if("NEED_END_TIME".equals(fieldCode)){
				fieldName = "审批时间";
			}
			Font titleCellFont = new Font(BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED), 11, 0, new BaseColor(225, 225, 225));
			PdfPCell titleCell = new PdfPCell(new Paragraph(fieldName, titleCellFont));
			titleCell.setMinimumHeight(30);
			titleCell.setHorizontalAlignment(titleCell.ALIGN_CENTER);
			titleCell.setVerticalAlignment(titleCell.ALIGN_MIDDLE);
			titleCell.setBackgroundColor(new BaseColor(64, 156, 201));
        	pdfPTable.addCell(titleCell);
		}
		
		//表体
		for(int i=0; i<listMap.size(); i++){
			for(int j=0; j<filedList.size(); j++){
				String filedContent = listMap.get(i).get(filedList.get(j))!=null?listMap.get(i).get(filedList.get(j)).toString():"";
				String needState = listMap.get(i).get("NEED_STATE")!=null?listMap.get(i).get("NEED_STATE").toString():"";
				String filedCode = filedList.get(j);
				if(needState.equals("1")){
					if(filedCode.equals("AUDIT_MESSAGE") || filedCode.equals("TASK_OPERATION_NAME") || filedCode.equals("NEED_END_TIME")){
						filedContent = "";
					}
				}
				PdfPCell titleCell = new PdfPCell(new Paragraph(filedContent, font));
				titleCell.setMinimumHeight(30);
				titleCell.setHorizontalAlignment(titleCell.ALIGN_CENTER);
				titleCell.setVerticalAlignment(titleCell.ALIGN_MIDDLE); 
	        	pdfPTable.addCell(titleCell);
			}
		}
		
		return pdfPTable;
	}

}
