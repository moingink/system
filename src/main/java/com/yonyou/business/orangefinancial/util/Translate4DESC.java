package com.yonyou.business.orangefinancial.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yonyou.util.BussnissException;
import com.yonyou.util.SpringContextUtil;
import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.jdbc.IBaseDao;

import net.sf.json.JSONObject;

/**
 * 解析 DESC 文件保存到数据库 bs_filemanage
 * 
 * @author changjr 2019年4月10日17:05:33
 */
@Service
public class Translate4DESC {

	@Autowired
	private IBaseDao baseDao;

	/**
	 * DESC文件解析入库
	 * 
	 * @param fileName
	 * @return
	 */
	@Transactional
	public List<String> execute(String fileName) {
		List<String> context = new ArrayList<>();
		try {
			context = readFile(fileName);
			saveInfo4DESC(context, fileName);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BussnissException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return context;
	}

	/**
	 * 文件输入流
	 * 
	 * @throws FileNotFoundException
	 */
	public List<String> readFile(String fileName) throws FileNotFoundException {

//		存储DESC文件解析内容
		List<String> context = new ArrayList<>();
		FileReader reader = null;
		BufferedReader br = null;
//		if (!file.exists()) {
//			throw new FileNotFoundException();
//		}
		try {
			reader = new FileReader(fileName);
			br = new BufferedReader(reader);
			String line;
			while ((line = br.readLine()) != null) {
				// 一次读入一行数据
				context.add(line);
//				System.out.println(line);
			}
			br.close();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return context;
	}

	@Transactional
	public List<String> saveInfo4DESC(List<String> context, String fileName)
			throws UnsupportedEncodingException, BussnissException {

//		fileName = "D:\\soft\\M002-COMPANY001-FMP-20190313-1-001-A.DESC";

		String[] arr = fileName.split("\\\\");
		String descName = arr[arr.length - 1];
		String[] values = descName.split("-");
//		系统编码
		String sys_code = values[0];
//		接口编码
		String itf_code = values[1];
//		平台编码
		String plat_code = values[2];
//		日期编码
		String date_code = values[3];
//		文件总数
		String count = String.valueOf(context.size() - 1);
//		传输类型（全量更新增量）
		String type = values[values.length - 1].replaceAll(".DESC", "");
		String allname = descName;
//获取字段序列长度  如果大于4000 则分段
		int len = context.get(0).getBytes("UTF-8").length;

		if (len > 4000) {

		}
		String field_1 = context.get(0);
		String field_2 = "null";
		String table = "BS_FILEMANAGE";
		String did = null;
		for (int i = 0; i < context.size(); i++) {
			if (i == 0) {
				Map<String, Object> fileManage = new HashMap<>();
				fileManage.put("sys_code", sys_code);
				fileManage.put("itf_code", itf_code);
				fileManage.put("plat_code", plat_code);
				fileManage.put("date_code", date_code);
				fileManage.put("count", count);
				fileManage.put("type", type);
				fileManage.put("allname", allname);
				fileManage.put("field_1", field_1);
				fileManage.put("field_2", field_2);
				fileManage.put("plat_code", plat_code);
				fileManage.put("date_code", date_code);
				System.out.println("****************fileManage:" + fileManage);
//			返回对应DESC主键
				did = baseDao.insert(table, fileManage);
				System.out.println(did);
			} else {
				String[] txt = context.get(i).split(",");
				Map<String, Object> fileManage = new HashMap<>();
				fileManage.put("sys_code", sys_code);
				fileManage.put("itf_code", itf_code);
				fileManage.put("plat_code", plat_code);
				fileManage.put("date_code", date_code);
				fileManage.put("count", count);
				fileManage.put("type", type);
				fileManage.put("allname", allname);
				fileManage.put("field_1", field_1);
				fileManage.put("field_2", field_2);
				fileManage.put("did", did);
				fileManage.put("dataname", txt[0]);
				fileManage.put("datasize", txt[1]);
				fileManage.put("filenum", i);
				System.out.println("****************fileManage:" + fileManage);
//			返回对应DESC主键
				baseDao.insert(table, fileManage);
				System.out.println(did);
			}
		}

		return context;
	}

	/**
	 * 更新数据文件状态 fileName 文件名称 status 稳健状态
	 **/

	@Transactional
	public int updataFileStatus(String fileName, String status) {
		String sql = "update bs_filemanage set status = '" + status + "'" + "where dataname ='" + fileName + "'";
		int flag = baseDao.updateBySql(sql);
		if (status.equals(FileStatus.FINISH)) {
			UpdateDescFileStatus(fileName);
		}
		return flag;

	}

	/**
	 * 检查数据文件是否存在 文件名
	 */
	public boolean checkFileExist(String fileName) {
		String sql = "select 1 from bs_filemanage where  dataname ='" + fileName + "'";
		List res = BaseDao.getBaseDao().query(sql, "");
		return !res.isEmpty();

	}

	/**
	 * 检查Desc文件是否存在 文件名
	 */
	public boolean checkDescExist(String descName) {
		String sql = "select 1 from bs_filemanage where  allname ='" + descName + "'";
		List res = BaseDao.getBaseDao().query(sql, "");
		return !res.isEmpty();

	}

	/**
	 * 查询desc文件所有状态的数据文件
	 * descName 文件名称
	 * status 对应的状态
	 * 返回 对应状态的文件名
	 */
	public List queryFileByStatus(String descName, String status) {
		String sql = "select dataname from bs_filemanage where  allname ='" + descName + "' and status ='" + status
				+ "' and did is not null";
		List res = BaseDao.getBaseDao().query(sql, "");
		return res;
	}

	@Transactional
	public int UpdateDescFileStatus(String fileName) {
		int flag = 0;
		String sql = "select 1 from bs_filemanage where allname =(select allname from bs_filemanage where dataname ='"
				+ fileName + "') and status = '0' and did is not null";
		List res = BaseDao.getBaseDao().query(sql, "");
		if (res.isEmpty()) {
			String sql1 = "update bs_filemanage set status = '1'"
					+ "where allname =(select allname from bs_filemanage where dataname ='" + fileName
					+ "') and did is null";
			flag = baseDao.updateBySql(sql1);
		}
		return flag;

	}
}
