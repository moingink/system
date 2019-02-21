package com.yonyou.util;

/** 
 * @author zzh
 * @version 创建时间：2016年10月31日
 * 类说明 
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

public class ExcelTools {

	public static String NO_DEFINE = "no_define";// 未定义的字段
	public static String DEFAULT_DATE_PATTERN = "yyyy年MM月dd日";// 默认日期格式
	public static int DEFAULT_COLOUMN_WIDTH = 17;

	public static ConcurrentHashMap<String, List<Map<String, String>>> parse(
			String fileName) throws BussnissException {

		try {
			return parse(new FileInputStream(fileName));
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return null;
	}

	public static ConcurrentHashMap<String, List<Map<String, String>>> parse(
			InputStream fileInputStream) throws BussnissException {
		HSSFWorkbook wb = null;
		HSSFSheet sheet = null;
		ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
		ConcurrentHashMap<String, String> temp = null;
		ConcurrentHashMap<String, String> key = new ConcurrentHashMap<String, String>();

		ConcurrentHashMap<String, List<Map<String, String>>> result = new ConcurrentHashMap<String, List<Map<String, String>>>();
		try {
			wb = PoiUtils.getWorkbook(fileInputStream);
			if (wb == null) {
				throw new Exception("Workbook is null");
			}

			sheet = wb.getSheetAt(0);

			HSSFRow dataSourceRow = sheet.getRow(0);
			String dataSourceCode = PoiUtils.getCellStrValue(dataSourceRow
					.getCell((short) 0));
			System.out.println("dataSourceCode" + dataSourceCode);
			if (null == dataSourceCode || "".equals(dataSourceCode.trim())) {
				throw new BussnissException("第一行第一列没有配置数据源编码！");
			}

			result.put(dataSourceCode, data);
			// ConcurrentHashMap<String,String> dataSource =
			// DataSourceUtil.getDataSource(dataSourceCode);
			// if(null==dataSource ){
			// throw new BussnissException("系统没有配置["+dataSourceCode+"]编码的数据源！");
			// }

			HSSFRow headRowCell = sheet.getRow(1);
			System.out.println("headRowCell:");
			for (int j = 0; j < headRowCell.getLastCellNum(); j++) {
				HSSFCell cell = headRowCell.getCell((short) j);
				key.put("" + j, PoiUtils.getCellStrValue(headRowCell
						.getCell((short) j)));
				System.out.println("headRowCell"
						+ PoiUtils.getCellStrValue(headRowCell
								.getCell((short) j)));
			}

			for (int i = 2; i <= sheet.getLastRowNum(); i++) {
				HSSFRow row = sheet.getRow(i);
				temp = new ConcurrentHashMap<String, String>();

				int cnum = row.getLastCellNum();
				for (int j = 0; j < cnum; j++) {
					HSSFCell cell = row.getCell((short) j);
					if (j == 0) {
						if (cell == null) {
							break;
						}
						if ((PoiUtils.getCellStrValue(cell) == null)
								|| (PoiUtils.getCellStrValue(cell).equals(""))) {
							break;
						}
					}

					if (cell != null) {
						String msg = PoiUtils.getCellStrValue(cell);
						if ((msg == null) || (msg.length() == 0)) {
							msg = "";
						}
						temp.put(key.get("" + j), msg);// key.get(""+j)
						System.out.println("dataRowCell"
								+ PoiUtils.getCellStrValue(cell));
					}

				}

				data.add(temp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new BussnissException("");
		}
		if (data.size() == 0) {
			throw new BussnissException("没有读取到业务数据！");
		}
		return result;
	}

	/**
	 * 导出Excel 97(.xls)格式 ，少量数据
	 * 
	 * @param title 标题行
	 * @param headMap 属性-列名
	 * @param jsonArray 数据集
	 * @param datePattern 日期格式，null则用默认日期格式
	 * @param colWidth 列宽 默认 至少17个字节
	 * @param out 输出流
	 */
	public static void exportExcel(String title, Map<String, String> headMap,
			List<Map<String, Object>> dataMap, String datePattern,
			int colWidth, OutputStream out) {
		if (datePattern == null)
			datePattern = DEFAULT_DATE_PATTERN;
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// workbook.c.createInformationProperties();
		// workbook.getDocumentSummaryInformation().setCompany("*****公司");
		// SummaryInformation si = workbook.getSummaryInformation();
		// si.setAuthor("JACK"); //填加xls文件作者信息
		// si.setApplicationName("导出程序"); //填加xls文件创建程序信息
		// si.setLastAuthor("最后保存者信息"); //填加xls文件最后保存者信息
		// si.setComments("JACK is a programmer!"); //填加xls文件作者信息
		// si.setTitle("POI导出Excel"); //填加xls文件标题信息
		// si.setSubject("POI导出Excel");//填加文件主题信息
		// si.setCreateDateTime(new Date());
		
		// 创建标题单元格，并设置标题样式
		HSSFCellStyle titleStyle = workbook.createCellStyle();
		// 设置标题居中
		titleStyle.setAlignment(HorizontalAlignment.CENTER);
		// 设置标题字体样式
		HSSFFont titleFont = workbook.createFont();
		// 设置字体大小
		titleFont.setFontHeightInPoints((short) 20);
		// 设置字体宽度
		titleFont.setBold(true);
		titleStyle.setFont(titleFont);
		
		// 创建表头单元格，并设置表头样式
		HSSFCellStyle headerStyle = workbook.createCellStyle();
		// 设置字体居中
		headerStyle.setAlignment(HorizontalAlignment.CENTER);
		// 设置边框
		//headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		//headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		//headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		//headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		//headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		//headerStyle.setFillBackgroundColor(HSSFColor.PALE_BLUE.index);
		//headerStyle.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
		// 设置表头字体样式
		HSSFFont headerFont = workbook.createFont();
		// 设置字体
		headerFont.setFontName("微软雅黑");
		// 设置字体大小
		headerFont.setFontHeightInPoints((short) 13);
		// 设置字体宽度
		// headerFont.setBold(true);
		headerStyle.setFont(headerFont);
		
		// 创建单元格，并设置单元格样式
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		// 设置字体居中
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		// 设置自动换行 
		cellStyle.setWrapText(true);
		//cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		//cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		//cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		//cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		//cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		//cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		//cellStyle.setFillBackgroundColor(HSSFColor.WHITE.index);
		//cellStyle.setFillForegroundColor(HSSFColor.WHITE.index);
		// 设置单元格字体样式
		HSSFFont cellFont = workbook.createFont();
		// 设置字体
		cellFont.setFontName("微软雅黑");
		// 设置字体大小
		cellFont.setFontHeightInPoints((short) 10);
		// 设置字体宽度
		// cellFont.setBold(true);
		// 设置字体样式
		cellFont.setColor(HSSFFont.COLOR_NORMAL);
		cellStyle.setFont(cellFont);
		
		// 生成一个(带标题)表格
		HSSFSheet sheet = workbook.createSheet();
		// 声明一个画图的顶级管理器
		// HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		// 定义注释的大小和位置,详见文档
		// HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
		// 设置注释内容
		// comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
		// 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
		// comment.setAuthor("JACK");
		// 设置列宽
		// int minBytes = colWidth < DEFAULT_COLOUMN_WIDTH ? DEFAULT_COLOUMN_WIDTH : colWidth;// 至少字节数
		// short[] arrColWidth = new short[headMap.size()];
		// 产生表格标题行,以及设置列宽
		
		String[] properties = new String[headMap.size()];
		String[] headers = new String[headMap.size()];
		short ii = 0;

		for (Iterator<String> iter = headMap.keySet().iterator(); iter.hasNext();) {
			String fieldName = iter.next();

			properties[ii] = fieldName;
			headers[ii] = headMap.get(fieldName);

			// short bytes = new short(fieldName.getBytes().length);
			// arrColWidth[ii] = bytes < minBytes ? minBytes : bytes;
			// sheet.setColumnWidth(ii,arrColWidth[ii]*256);
			ii++;
		}
		
		// 创建表头行， 表头行rowIndex =1
		HSSFRow headerRow = sheet.createRow(0); 
		for (int i = 0; i < headers.length; i++) {
			headerRow.createCell(Short.parseShort(String.valueOf(i))).setCellValue(headers[i]);
			headerRow.getCell(i).setCellStyle(headerStyle);
		}
		
		// 遍历集合数据，产生数据行
		int rowIndex = 0;
		for (Map<String, Object> data : dataMap) {
			if (rowIndex == 65535 || rowIndex == 0) {
				if (rowIndex != 0)
					// 创建表格，如果数据超过了，则在第二页显示
					sheet = workbook.createSheet();
				
					// 创建标题行，标题 rowIndex=0
					// HSSFRow titleRow = sheet.createRow(0);
					// 设置标题
					// titleRow.createCell((short) 0).setCellValue(title);
					// 设置标题样式
					// titleRow.getCell(0).setCellStyle(titleStyle);
					// 合并单元格
					// sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headMap.size() - 1));
					// 数据内容从 rowIndex=2开始
					rowIndex = 1;
			}

			HSSFRow dataRow = sheet.createRow(rowIndex);
			for (int i = 0; i < properties.length; i++) {
				HSSFCell newCell = dataRow.createCell(Short.parseShort(String.valueOf(i)));
				Object o = data.get(properties[i]);
				String cellValue = "";
				if (o == null || "".equals(o))
					cellValue = "";
				else if (o instanceof Date)
					cellValue = new SimpleDateFormat(datePattern).format(o);
				else
					cellValue = o.toString();

				newCell.setCellValue(cellValue);
				newCell.setCellStyle(cellStyle);
			}
			rowIndex++;
		}
		// 自动调整宽度
		for (int i = 0; i < headers.length; i++) {
			sheet.autoSizeColumn((short) i);
		}
		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Web 导出excel
	public static void downloadExcelFile(String title,
			Map<String, String> headMap, List<Map<String, Object>> dataMap,
			HttpServletResponse response) {
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			// 设置response参数，可以打开下载页面
			response.reset();
			response.setContentType("Content-Type:application/vnd.ms-excel;charset=utf-8 ");
			response.setHeader("Content-Disposition", "attachment;filename="
					+ new String(title.getBytes("gb2312"), "iso-8859-1") + "_"
					+ new String(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
					+ ".xls");
			ExcelTools.exportExcel(title, headMap, dataMap, null, 0, out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws IOException {
		int count = 10;
		List<Map<String, Object>> dataMap = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 10; i++) {
			Map<String, Object> s = new LinkedHashMap<String, Object>();
			s.put("name", "name" + i);
			s.put("age", "age" + i);
			s.put("weight", "weight" + i);
			s.put("sex", "sex" + i);
			s.put("birthday", "birthday" + i);
			s.put("height", "height" + i);
			dataMap.add(s);
		}

		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("name", "姓名");
		headMap.put("birthday", "生日");
		headMap.put("weight", "体重");
		headMap.put("sex", "性别");
		headMap.put("height", "身高");
		headMap.put("age", "年龄");
		String title = "测试";
		/*
		 * OutputStream outXls = new FileOutputStream("E://a.xls");
		 * System.out.println("正在导出xls...."); Date d = new Date();
		 * ExcelUtil.exportExcel(title,headMap,ja,null,outXls);
		 * System.out.println("共"+count+"条数据,执行"+(new
		 * Date().getTime()-d.getTime())+"ms"); outXls.close();
		 */
		//
		OutputStream outXlsx = new FileOutputStream("E://b.xls");
		System.out.println("正在导出xlsx....");
		Date d2 = new Date();
		ExcelTools.exportExcel(title, headMap, dataMap, null, 0, outXlsx);
		System.out.println("共" + count + "条数据,执行"
				+ (new Date().getTime() - d2.getTime()) + "ms");
		outXlsx.close();

	}

}