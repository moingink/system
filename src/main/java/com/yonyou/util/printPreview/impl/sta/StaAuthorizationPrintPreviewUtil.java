package com.yonyou.util.printPreview.impl.sta;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.yonyou.business.DataSourceUtil;
import com.yonyou.business.MetaDataUtil;
import com.yonyou.util.BussnissException;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.printPreview.PrintPreviewUtil;

public class StaAuthorizationPrintPreviewUtil extends PrintPreviewUtil{

	/**
	 * 综合审批管理-公章用印申请-授权书
	 */
	@Override
	public void createPdf(IBaseDao dcmsDAO, HttpServletRequest request, HttpServletResponse response, OutputStream os) throws DocumentException, IOException {
		String id = request.getParameter("id");
		String dataSourceCode = request.getParameter("dataSourceCode");
		List<Map<String, Object>> queryList = dcmsDAO.query("SELECT * FROM cachet_certificate_of_authorization WHERE id = '"+ id + "'", "");
		if(queryList.size() > 0){
			// 创建文档对象(页面的大小为A4,左、右、上、下的页边距为10)
	        Document document = new Document(PageSize.A4, 10, 10, 50, 10);
	        PdfWriter.getInstance(document, os);
			// 打开文档
	        document.open();
			// 使用远东字体包iTextAsian.jar
	        BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
	        // 设置文档标题
	        document.addTitle("授权书");
	        // 标题
			Paragraph title = new Paragraph("授权书",new Font(baseFont, 20, Font.BOLD, BaseColor.RED));
			title.setAlignment(1);// 对齐方式(1为居中对齐、2为右对齐、3为左对齐)
			title.setSpacingAfter(30);// 段落间距
	        document.add(title);
	        // 正文内容
	        Map<String, Object> certificateOfAuthorization = queryList.get(0);
	        Font font = new Font(baseFont, 11);
	        Font fontBold = new Font(baseFont, 11, Font.BOLD);
	        
	        //授权码
	        Paragraph wordToWord = new Paragraph(certificateOfAuthorization.get("WORD_TO_WORD").toString(), font);//授权码
	        wordToWord.setAlignment(2);
	        wordToWord.setSpacingAfter(10);
	        wordToWord.setIndentationRight(40);
        	document.add(wordToWord);
        	
        	Map<String, String> headMap = null;
			try {
				headMap = MetaDataUtil.getFiledNameMap(DataSourceUtil.getDataSource(dataSourceCode).get(DataSourceUtil.DATASOURCE_METADATA_CODE), "");
			} catch (BussnissException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	//授权单位
        	Chunk authorizedUnitTitleChunk = new Chunk(headMap.get("AUTHORIZED_UNIT")+"：", fontBold);
			Chunk authorizedUnitContentChunk = new Chunk(certificateOfAuthorization.get("AUTHORIZED_UNIT").toString(), font);
			Paragraph authorizedUnit = new Paragraph();
			authorizedUnit.add(authorizedUnitTitleChunk);
			authorizedUnit.add(authorizedUnitContentChunk);
			authorizedUnit.setAlignment(3);
			authorizedUnit.setSpacingAfter(10);
			authorizedUnit.setIndentationLeft(40);
			document.add(authorizedUnit);
        	
			//法定代表人/负责人
        	Chunk legalRepresentativeTitleChunk = new Chunk(headMap.get("LEGAL_REPRESENTATIVE")+"：", fontBold);
			Chunk legalRepresentativeContentChunk = new Chunk(certificateOfAuthorization.get("LEGAL_REPRESENTATIVE").toString(), font);
			Paragraph legalRepresentative = new Paragraph();
			legalRepresentative.add(legalRepresentativeTitleChunk);
			legalRepresentative.add(legalRepresentativeContentChunk);
			legalRepresentative.setAlignment(3);
			legalRepresentative.setSpacingAfter(10);
			legalRepresentative.setIndentationLeft(40);
			document.add(legalRepresentative);
			
			//委托代理人
        	Chunk agentPeopleTitleChunk = new Chunk(headMap.get("AGENT_PEOPLE")+"：", fontBold);
			Chunk agentPeopleContentChunk = new Chunk(certificateOfAuthorization.get("AGENT_PEOPLE").toString(), font);
			Paragraph agentPeople = new Paragraph();
			agentPeople.add(agentPeopleTitleChunk);
			agentPeople.add(agentPeopleContentChunk);
			agentPeople.setAlignment(3);
			agentPeople.setSpacingAfter(10);
			agentPeople.setIndentationLeft(40);
			document.add(agentPeople);
			
			//所属部门+职务
        	Chunk subordinateDepartmentTitleChunk = new Chunk(headMap.get("SUBORDINATE_DEPARTMENT")+"：", fontBold);
			Chunk subordinateDepartmentContentChunk = new Chunk(certificateOfAuthorization.get("SUBORDINATE_DEPARTMENT").toString()+"       ", font);
			Chunk dutiesTitleChunk = new Chunk(headMap.get("DUTIES")+"：", fontBold);
			Chunk dutiesContentChunk = new Chunk(certificateOfAuthorization.get("DUTIES").toString(), font);
			Paragraph departmentAndduties = new Paragraph();
			departmentAndduties.add(subordinateDepartmentTitleChunk);
			departmentAndduties.add(subordinateDepartmentContentChunk);
			departmentAndduties.add(dutiesTitleChunk);
			departmentAndduties.add(dutiesContentChunk);
			departmentAndduties.setAlignment(3);
			departmentAndduties.setSpacingAfter(10);
			departmentAndduties.setIndentationLeft(40);
			document.add(departmentAndduties);
			
			//授权事项及权利
        	Chunk authorizationMattersRightsTitleChunk = new Chunk(headMap.get("AUTHORIZATION_MATTERS_RIGHTS")+"：", fontBold);
			Chunk authorizationMattersRightsContentChunk = new Chunk("授权以上委托代理人审签以下事项，"+certificateOfAuthorization.get("AUTHORIZATION_MATTERS_RIGHTS").toString(), font);
			Paragraph authorizationMattersRights = new Paragraph();
			authorizationMattersRights.add(authorizationMattersRightsTitleChunk);
			authorizationMattersRights.add(authorizationMattersRightsContentChunk);
			authorizationMattersRights.setAlignment(3);
			authorizationMattersRights.setSpacingAfter(10);
			authorizationMattersRights.setIndentationLeft(40);
			document.add(authorizationMattersRights);
			
			//本授权书有效日期
        	Chunk letterOfAuthorizationDateTitleChunk = new Chunk(headMap.get("LETTER_OF_AUTHORIZATION_DATE")+"：", fontBold);
			Chunk letterOfAuthorizationDateContentChunk = new Chunk(certificateOfAuthorization.get("LETTER_OF_AUTHORIZATION_DATE").toString(), font);
			Paragraph letterOfAuthorizationDate = new Paragraph();
			letterOfAuthorizationDate.add(letterOfAuthorizationDateTitleChunk);
			letterOfAuthorizationDate.add(letterOfAuthorizationDateContentChunk);
			letterOfAuthorizationDate.setAlignment(3);
			letterOfAuthorizationDate.setSpacingAfter(10);
			letterOfAuthorizationDate.setIndentationLeft(40);
			document.add(letterOfAuthorizationDate);
        	
			//授权人
        	Chunk authorizingPersonTitleChunk = new Chunk(headMap.get("AUTHORIZING_PERSON")+"：", fontBold);
			Chunk authorizingPersonContentChunk = new Chunk(certificateOfAuthorization.get("AUTHORIZING_PERSON").toString(), font);
			Paragraph authorizingPerson = new Paragraph();
			authorizingPerson.add(authorizingPersonTitleChunk);
			authorizingPerson.add(authorizingPersonContentChunk);
			authorizingPerson.setAlignment(2);
			authorizingPerson.setSpacingAfter(10);
			authorizingPerson.setIndentationRight(40);
			document.add(authorizingPerson);
			
			//日期
        	Chunk authorizingDateTitleChunk = new Chunk(headMap.get("AUTHORIZING_DATE")+"：", fontBold);
			Chunk authorizingDateContentChunk = new Chunk(certificateOfAuthorization.get("AUTHORIZING_DATE").toString(), font);
			Paragraph authorizingDate = new Paragraph();
			authorizingDate.add(authorizingDateTitleChunk);
			authorizingDate.add(authorizingDateContentChunk);
			authorizingDate.setAlignment(2);
			authorizingDate.setSpacingAfter(10);
			authorizingDate.setIndentationRight(40);
			document.add(authorizingDate);
	        
	        document.close();
		}
	}

	@Override
	protected List<String> replaceFiledList(List<Map<String, Object>> listMap,
			List<String> previewFiledList, IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected PdfPTable createPdfTbale(Font font, List<String> filedList,
			List<Map<String, Object>> listMap, IBaseDao dcmsDAO,
			HttpServletRequest request, HttpServletResponse response)
			throws DocumentException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	
}
