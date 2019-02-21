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
import java.math.BigDecimal;
import java.text.DateFormat;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;



public class PoiUtils
{
  public static final int DEFAULT_TYPE = -1;
  public static final int DATE_TYPE = 0;
  public static final int NUM_TYPE = 1;
  public static final byte ENCODING_COMPRESSED_UNICODE = 0;
  public static final byte ENCODING_UTF_16 = 1;

  public static HSSFWorkbook getWorkbook(InputStream fileins)
    throws IOException
  {
    POIFSFileSystem fs = new POIFSFileSystem(fileins);
    if (fileins != null) {
      fileins.close();
    }
    return new HSSFWorkbook(fs);
  }

  public static HSSFWorkbook getWorkbook(String fileName)
    throws FileNotFoundException, IOException
  {
    return getWorkbook(new FileInputStream(fileName));
  }

  public static void writeWorkbook(HSSFWorkbook wb, String fileName)
    throws FileNotFoundException, IOException
  {
    writeWorkbook(wb, new FileOutputStream(fileName));
  }

  public static void writeWorkbook(HSSFWorkbook wb, OutputStream fileouts)
    throws IOException
  {
    wb.write(fileouts);
    if (fileouts != null)
      fileouts.close();
  }

  public static void setCellStrValue(HSSFCell cell, Object value, short encoding)
    throws Exception
  {
    //cell.setEncoding(encoding);
    if (value == null) {
      cell.setCellValue("");
      return;
    }
    if ((value instanceof String)) {
      cell.setCellValue(value.toString());
    } else if ((value instanceof BigDecimal)) {
      cell.setCellValue(new BigDecimal(value.toString()).doubleValue());
    } else if ((value instanceof java.sql.Date))
    {
      java.sql.Date sqlDate = java.sql.Date.valueOf(value.toString());
      java.util.Date utilDate = new java.util.Date(sqlDate.getTime());
      cell.setCellValue(DateFormat.getDateInstance().format(utilDate));
    } else if ((value instanceof java.util.Date)) {
      cell.setCellValue(DateFormat.getDateInstance().format(value));
    } else if ((value instanceof Boolean)) {
      cell.setCellValue(new Boolean(value.toString()).booleanValue());
    } else {
      throw new Exception("" + value.getClass());
    }
  }

  public static String getCellStrValue(HSSFCell cell)
  {
    String value = null;

    if (cell == null) {
      return null;
    }

//    value = cell.getStringCellValue();
    
    switch (cell.getCellType())
    {
    case 1:
      value = cell.getStringCellValue();
      break;
    case 0:
      if (HSSFDateUtil.isCellDateFormatted(cell))
        try {
          java.util.Date date = cell.getDateCellValue();
          value = DateFormat.getDateInstance().format(date);
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      else {
        value = String.valueOf(cell.getNumericCellValue()).replace(".0", "").replace(".00", "");
      }
      break;
    case 4:
      value = new Boolean(cell.getBooleanCellValue()).toString();
    case 2:
    case 3:
    }

    return value;
  }

  public static void main(String[] args)
  {
    try
    {
      short d = 0;

      HSSFWorkbook wb = new HSSFWorkbook();
      HSSFSheet s = wb.createSheet();
      wb.setSheetName(0, "first sheet");
      HSSFRow row = s.createRow(0);

      java.util.Date obj = new java.util.Date(System.currentTimeMillis());
      java.sql.Date obj1 = java.sql.Date.valueOf(DateFormat.getDateInstance().format(obj));

      setCellStrValue(row.createCell(d), obj1, (short)1);

      writeWorkbook(wb, "E://test.xls");
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }
}