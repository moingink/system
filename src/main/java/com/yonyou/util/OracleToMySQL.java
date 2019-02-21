package com.yonyou.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.Set;

public class OracleToMySQL {

	static String folder = "C:\\Users\\Think\\Desktop\\手动导表";
	static String sourceFolder = folder + "\\原sql";
	static String targetFolder = folder + "\\" + getSysDateByCalendar();
	
	public static void main(String[] args) throws IOException {
		
		File target = new File(targetFolder);
		target.mkdir();
		
		File[] source = new File(sourceFolder).listFiles();
		//文件中文件名与文件内表名不符
		Set<String> errorTab = new HashSet<String>();
		//表文件可能存在缺少注释
		Set<String> promptTab = new HashSet<String>();
		//字段可能为外键——类型需转为BIGINT
		HashMap<String, Set<String>> promptCol = new HashMap<String, Set<String>>();
		
		outer: for (File sou : source) {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(sou), "gb2312"));
			boolean flag = false;//主体
			boolean rename = false;//文件名是否需要特殊标识
			String temp = "";
			String fileName = sou.getName();
			String tableName = fileName.substring(0, fileName.indexOf("."));
			StringBuffer body = new StringBuffer("");
			//<表CODE,表COMMENT>
			HashMap<String, String> tabComment = new HashMap<String, String>();
			//<字段CODE,字段COMMENT>
			HashMap<String, String> colComment = new HashMap<String, String>();
			
			while( (temp=br.readLine()) != null){
				if(!flag && temp.contains("CREATE TABLE")){
					String tableNameInFile = temp.substring(temp.indexOf('.')+2, temp.length()-2);
					if(!tableNameInFile.equals(tableName)){
						errorTab.add(fileName);
						continue outer;
					}else{
						flag = true;
						System.out.println("-- START解析文件：" + fileName);
					}
				}else if(flag && temp.contains("\"ID\"")){
					//ID的类型由VARCHAR2(19)改为BIGINT(19)
					String baseTemp = temp;
					if(temp.contains("(")){
						temp = "(\"ID\" BIGINT(19) PRIMARY KEY";
					}else{
						temp = "\"ID\" BIGINT(19) PRIMARY KEY";
						}
					if(baseTemp.contains(",")){
						temp += ",";
					}
				}else if(flag && temp.contains("PCTFREE")){
					body.append(")");
					flag = false;
					continue;
				}
				if(flag && !temp.contains("\"ID\"") && temp.contains("VARCHAR2(19 BYTE)")){
					Set<String> tempSet = new HashSet<>();
					if(promptCol.containsKey(tableName)){
						tempSet = promptCol.get(tableName);
						tempSet.add(temp.split("\"")[1]);
					}else{
						tempSet.add(temp.split("\"")[1]);
					}
					promptCol.put(tableName, tempSet);
				}
				if(temp.contains("NUMBER")){
					System.out.println("#");
				}
				if(flag && !temp.contains("CREATE TABLE")){//语句主体-()中的内容,包含注释
					//注意主体中最后一句没有逗号
					if(temp.trim().endsWith(",")){
						temp = temp.substring(0, temp.lastIndexOf(",")) + " COMMENT #" + temp.split("\"")[1] + ",";
					}else{
						//TODO 最后一句强行加个逗号——为了后面替换的准确 -line123
						temp = temp + " COMMENT #" + temp.split("\"")[1] + ",";
					}
				}
				if(flag){
					//number得逐行特殊处理
					if(temp.contains("NUMBER")){
						if(temp.contains("(") && temp.contains(")")){
							temp = temp.replace("NUMBER", "DECIMAL");
						}else{
							temp = temp.replace("NUMBER", "INT");
						}
					}
					body.append(temp).append("\n");
				}
				if(!flag){
					if(temp.contains("COMMENT ON COLUMN")){
						String[] tempSplit = temp.split("\"");
						String colName = tempSplit[5];
//						System.out.println(a);
						String commentTemp = tempSplit[tempSplit.length - 1];// IS '编码';
//						System.out.println(b);
						String comment = commentTemp.substring(commentTemp.indexOf("'"), commentTemp.lastIndexOf("'")+1);//含引号
//						System.out.println(c);
						colComment.put(colName,comment);
					}else if(temp.contains("COMMENT ON TABLE")){
						//FIXME
//						tabComment.put(tableName, "");
					}
				}
			}
			br.close();
			
//			System.out.println(body);
			System.out.println(colComment);
			
			String createSql= body.toString().replace("\"ZW_SYSTEM\".", "").replace(" BYTE", "")
					.replace(" VARCHAR2", " VARCHAR")
					.replace(" DATE DEFAULT SYSDATE", " TIMESTAMP NOT NULL DEFAULT NOW()")
					.replace(" DATE", " TIMESTAMP")
					.replace(" sysdate", " NOW()")
					.replace("\"", "");
			for(Entry<String, String> entry : colComment.entrySet()){
				createSql = createSql.replace("#"+entry.getKey()+",", entry.getValue()+",");//DEF1&DEF10
			}
			//结尾强行逗号，navicat不认，mmp
			createSql = createSql.replace(",\n)", "\n)");
			if(createSql.contains("#")){
				promptTab.add(tableName);
				//DR NUMBER(10,0) DEFAULT 0 COMMENT #DR,
				//FIXME 无注释，暂时去掉
				String regex = "COMMENT #.*,";
				Pattern p = Pattern.compile(regex);
				createSql = p.matcher(createSql).replaceAll(",");
				rename = true;
			}
			System.out.println("--" + tableName + "\n" + createSql);
			
			//表文件存在缺少注释的情况时文件名加上特殊标识
			if(rename){
				fileName = "#" + fileName;
			}
			File targetFile = new File(targetFolder +"\\" + fileName);
			FileWriter fw = new FileWriter(targetFile);
			fw.write(createSql);
			fw.flush();
			fw.close();
			targetFile.createNewFile();
			
		}
		
//		System.err.println("--以下文件中文件名与文件内表名不符！生成SQL失败：" + errorTab);
//		System.out.println("--以下表文件可能存在缺少注释的情况：" + promptTab);
//		System.out.println("--以下【表~字段】可能为外键，类型需转为BIGINT" + promptCol);
		File resultFile = new File(folder + "\\result.txt");
		FileWriter fw2 = new FileWriter(resultFile);
		fw2.write("【警告】以下文件中文件名与文件内表名不符！生成SQL失败：\n" + errorTab + "\n\n");
		fw2.write("【注意】以下表文件可能存在缺少注释的情况：\n" + promptTab + "\n\n");
		fw2.write("【注意】以下[表~字段]可能为外键，类型需转为BIGINT\n" + promptCol + "\n\n");
		fw2.flush();
		fw2.close();
		resultFile.createNewFile();
	}
	
	private static String getSysDateByCalendar() {
		StringBuffer str = new StringBuffer();
		Calendar rightNow = Calendar.getInstance();
		int iYear = rightNow.get(Calendar.YEAR);
		int iMonth = rightNow.get(Calendar.MONTH) + 1;
		int iDate = rightNow.get(Calendar.DATE);
		int iHour = rightNow.get(Calendar.HOUR_OF_DAY);
		int iMinute = rightNow.get(Calendar.MINUTE);
		str.append(iYear);
		if (iMonth < 10) {
			str.append("0");
		}
		str.append(iMonth);
		if (iDate < 10) {
			str.append("0");
		}
		str.append(iDate);
		if (iHour < 10) {
			str.append("0");
		}
		str.append(iHour);
		if (iMinute < 10) {
			str.append("0");
		}
		str.append(iMinute);
		return str.toString();
	}
	
}
