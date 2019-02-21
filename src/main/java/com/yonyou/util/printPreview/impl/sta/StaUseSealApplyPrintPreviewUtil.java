package com.yonyou.util.printPreview.impl.sta;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.yonyou.business.DataSourceUtil;
import com.yonyou.business.MetaDataUtil;
import com.yonyou.business.RmDictReferenceUtil;
import com.yonyou.util.BussnissException;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.printPreview.PrintPreviewUtil;

public class StaUseSealApplyPrintPreviewUtil extends PrintPreviewUtil{

	/*创建文档对象*/
	Document document = new Document(PageSize.A4, 10, 10, 50, 10);

	@Override
	protected List<String> replaceFiledList(
			List<Map<String, Object>> listMap, List<String> previewFiledList, IBaseDao dcmsDAO,
			HttpServletRequest request, HttpServletResponse response) {

		String fieldOrder = "CACHET_APPLY_PROCESS_NUMBER,APPLY_PEOPLE,APPLY_DATE,USE_CACHET_DEPARTMENT,USE_CACHET_TIME,CACHET_TYPE,USE_CACHET_CAUSE,CACHET_NUMBER,IS_FINANCIAL_APPROVAL,SEAL_TYPE,IS_FIRST,RELATION_FIRST_PROCESS,IS_LEND_OUT,LEND_OUT_DATE,SEND_BACK_DATE,LEND_OUT_DEPARTMENT,LEND_OUT_PEOPLE,CAUSE,LAST_AUDIT_PEOPLE,AUDIT_ADOPT_DATE,LAST_AUDIT_OPINION,SEAL_CONFIRM_PEOPLE,SEAL_CONFIRM_DATE,SEAL_CONFIRM_EXPLAIN";
		List<String> list = new ArrayList<>(Arrays.asList(fieldOrder.split(",")));
		
		String sealType = listMap.get(0).get("SEAL_TYPE")!=null?listMap.get(0).get("SEAL_TYPE").toString():"";//盖章类型
		String isLendOut = listMap.get(0).get("IS_LEND_OUT")!=null?listMap.get(0).get("IS_LEND_OUT").toString():"";//是否外借
		String isFirst = listMap.get(0).get("IS_FIRST")!=null?listMap.get(0).get("IS_FIRST").toString():"";//是否首次
		
		List<String> removeList = new ArrayList<String>();
		
		if("1".equals(isLendOut)){//是
			removeList.add("SEAL_CONFIRM_PEOPLE");//盖章确认人
			removeList.add("SEAL_CONFIRM_EXPLAIN");//确认说明
			removeList.add("SEAL_CONFIRM_DATE");//盖章确认日期
		}else if("0".equals(isLendOut)){//否
			removeList.add("LEND_OUT_DATE");//借出日期
			removeList.add("SEND_BACK_DATE");//归还日期
			removeList.add("LEND_OUT_DEPARTMENT");//借用部门
			removeList.add("LEND_OUT_PEOPLE");//借用人
			removeList.add("CAUSE");//事由
			removeList.add("LAST_AUDIT_OPINION");//最后审批意见
			removeList.add("LAST_AUDIT_PEOPLE");//最后审核人
			removeList.add("AUDIT_ADOPT_DATE");//审批通过日期
		}
		
		if("0".equals(sealType)){//单次盖章
			removeList.add("IS_FIRST");//是否首次
			removeList.add("RELATION_FIRST_PROCESS");//关联首次申请流程
		}else if("1".equals(sealType)){//多次盖章
			if("1".equals(isFirst)){//是
				removeList.add("RELATION_FIRST_PROCESS");//关联首次申请流程
			}
		}
		
		list.removeAll(removeList);
		return list;
	}

	@Override
	public void createPdf(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response, OutputStream os)
			throws DocumentException, IOException {
		
		String id = request.getParameter("id");
		String dataSourceCode = request.getParameter("dataSourceCode");
		String pafName = request.getParameter("pafName");
		
        PdfWriter.getInstance(document, os);
        document.open();
        BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        Font font = new Font(baseFont, 11);
        
        /*标题*/
 		Paragraph title = new Paragraph("公章用印申请单",new Font(baseFont, 20, Font.BOLD));
 		title.setAlignment(1);// 对齐方式(1为居中对齐、2为右对齐、3为左对齐)
 		title.setSpacingAfter(30);// 段落间距
        document.add(title);
        document.addTitle(pafName);
        
        /*基本信息*/
        
        	//表格标题
	        Paragraph tableTitle = new Paragraph("基本信息：",new Font(baseFont, 15, Font.BOLD));
			tableTitle.setAlignment(3);// 对齐方式(1为居中对齐、2为右对齐、3为左对齐)
			tableTitle.setSpacingAfter(10);// 段落间距
	        document.add(tableTitle);
        
			//数据查询
			List<Map<String, Object>> basicListMap = dcmsDAO.query("SELECT * FROM "+dataSourceCode+" WHERE id = "+id, "");
			//打印字段
			List<String> basicFiledList = new ArrayList<String>();
			basicFiledList = replaceFiledList(basicListMap, basicFiledList, dcmsDAO, request, response);
			PdfPTable basicPdfPTable = createPdfTbale(font, basicFiledList, basicListMap, dcmsDAO, request, response);
			document.add(basicPdfPTable);
		
		/*审批信息*/
			
			//表格标题
	 		Paragraph approvalTitle = new Paragraph("审批意见：",new Font(baseFont, 15, Font.BOLD));
	 		approvalTitle.setAlignment(3);// 对齐方式(1为居中对齐、2为右对齐、3为左对齐)
	 		approvalTitle.setSpacingAfter(10);// 段落间距
	 		document.add(approvalTitle);
	 		
	 		//数据查询
	 		String needSql = "SELECT * FROM WF_NEED_HANDLE WHERE NEED_PROCESS_CODE = 'system@CACHET_USE_SEAL_APPLY@' and BUS_ID = '"+id+"' and NEED_STATE = '2' ";
	        List<Map<String,Object>> auditListMap = dcmsDAO.query(needSql, "");
	        //打印字段
	        List<String> auditFiledList = Arrays.asList("NEED_USER_NAME,TASK_NAME,AUDIT_MESSAGE,TASK_OPERATION_NAME,NEED_END_TIME".split(","));
	        PdfPTable auditPdfPTable = createAuditPdfTbale(font, auditFiledList, auditListMap, dcmsDAO, request, response);
	        document.add(auditPdfPTable);
		
	    document.close();
	}

	@SuppressWarnings("static-access")
	@Override
	protected PdfPTable createPdfTbale(Font font, List<String> filedList,
			List<Map<String, Object>> listMap, IBaseDao dcmsDAO,
			HttpServletRequest request, HttpServletResponse response)
			throws DocumentException, IOException {
		
		String dataSourceCode = request.getParameter("dataSourceCode");
		
		PdfPTable pdfPTable = new PdfPTable(6);
		try {
			pdfPTable.setWidths(new int[]{95, 91, 91, 91, 91, 91});
			pdfPTable.setTotalWidth(570);
			pdfPTable.setLockedWidth(true);
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//元数据详情
		Map<String, ConcurrentHashMap<String, String>> tableMap = null;
		try {
			tableMap = MetaDataUtil.getMetaDataFields(DataSourceUtil.getDataSource(dataSourceCode).get(DataSourceUtil.DATASOURCE_METADATA_CODE));
			System.err.println(tableMap);
		} catch (BussnissException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int index = 0;
		
		//表格填充
		for(int i=0; i<filedList.size(); i++){
			String fieldCode = filedList.get(i);//字段编号
			String fieldName = tableMap.get(fieldCode)!=null?tableMap.get(fieldCode).get("FIELD_NAME").toString():"";//字段名称
			String inputType = tableMap.get(fieldCode)!=null?tableMap.get(fieldCode).get("INPUT_TYPE").toString():"";//输入类型
			String inputFormart = tableMap.get(fieldCode)!=null?tableMap.get(fieldCode).get("INPUT_FORMART").toString():"";//输入格式
			
			//标题列
			PdfPCell titleCell = new PdfPCell(new Paragraph(fieldName+"：" ,font));
			titleCell.setMinimumHeight(30);
			titleCell.setHorizontalAlignment(titleCell.ALIGN_LEFT);
			titleCell.setVerticalAlignment(titleCell.ALIGN_MIDDLE); 
        	pdfPTable.addCell(titleCell);
        	//内容列
        	String fieldContent = listMap.get(0).get(fieldCode)!=null?listMap.get(0).get(fieldCode).toString():"";//字段内容
        	if("1".equals(inputType)){//下拉
        		try {
					ConcurrentHashMap<String, String> dictReference = RmDictReferenceUtil.getDictReference(inputFormart);
					fieldContent = dictReference.get(fieldContent)!=null?dictReference.get(fieldContent).toString():"";
				} catch (BussnissException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        	
        	int colspanSize = 0;
        	if("2".equals(inputType)){//文本域
        		colspanSize = 5;
        	}else{
        		if(i!=filedList.size()){
        			String nextInputType = tableMap.get(filedList.get(i+1))!=null?tableMap.get(filedList.get(i+1)).get("INPUT_TYPE").toString():"";
        			if("2".equals(nextInputType)){
        				if(index == 1){
        					colspanSize = 5;
        				}else if(index == 2){
        					colspanSize = 3;
        				}
        			}
        		}
        	}
        	PdfPCell contentCell = new PdfPCell(new Paragraph(fieldContent ,font));
        	contentCell.setMinimumHeight(20);
        	contentCell.setHorizontalAlignment(contentCell.ALIGN_LEFT);
        	contentCell.setVerticalAlignment(contentCell.ALIGN_MIDDLE); 
        	contentCell.setColspan(colspanSize);//横向合并单元
        	pdfPTable.addCell(contentCell);
        	
        	index++;
        	if(index == 3){
        		index = 0;
        	}
		}
		
		return pdfPTable;
	}

	@SuppressWarnings("static-access")
	protected PdfPTable createAuditPdfTbale(Font font, List<String> filedList,
			List<Map<String, Object>> listMap, IBaseDao dcmsDAO,
			HttpServletRequest request, HttpServletResponse response)
			throws DocumentException, IOException {
		
		PdfPTable pdfPTable = new PdfPTable(5);
		pdfPTable.setTotalWidth(570);
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
			PdfPCell titleCell = new PdfPCell(new Paragraph(fieldName ,font));
			titleCell.setMinimumHeight(30);
			titleCell.setHorizontalAlignment(titleCell.ALIGN_CENTER);
			titleCell.setVerticalAlignment(titleCell.ALIGN_MIDDLE); 
        	pdfPTable.addCell(titleCell);
		}
		
		//表体
		for(int i=0; i<listMap.size(); i++){
			for(int j=0; j<filedList.size(); j++){
				String filedContent = listMap.get(i).get(filedList.get(j))!=null?listMap.get(i).get(filedList.get(j)).toString():"";
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
