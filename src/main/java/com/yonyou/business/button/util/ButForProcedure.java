package com.yonyou.business.button.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.yonyou.business.DataSourceUtil;
import com.yonyou.business.MetaDataUtil;
import com.yonyou.business.RmDictReferenceUtil;
import com.yonyou.business.button.ButtonAbs;
import com.yonyou.util.BussnissException;
import com.yonyou.util.JsonUtils;
import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.JoinEnum;
import com.yonyou.util.sql.SQLUtil.WhereEnum;
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.util.sql.SqlWhereEntity;

public class ButForProcedure extends ButtonAbs {

	@Override
	protected boolean befortOnClick(IBaseDao dcmsDAO,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) throws IOException, BussnissException {
		String tableNameValues = request.getParameter("tableNameValues");
		String[] tVlues = tableNameValues.split(",");
		

		// 表名
		List<String> tableList = new ArrayList<String>();
		for (int j = 0; j < tVlues.length; j++) {
			// 表字段COLUMN_NAME集合
			/*
			 * List<String> fieldCodeList = new ArrayList<String>(); JSONObject
			 * jb = (JSONObject)jsonArray.get(j); String str = (String)
			 * jb.get("TABLE_NAME"); tableList.add(str);
			 */
			List<Map<String, String>> entityList = new ArrayList<Map<String, String>>();
			List<String> fieldCodeList = new ArrayList<String>();
			String str = tVlues[j];
			tableList.add(str);
			// 遍历metadatafileds拿出所有的fieldname
			/*
			 * for (Entry<String, ConcurrentHashMap<String, String>> entry :
			 * MetaDataUtil.getMetaDataFields(str).entrySet()) {
			 * 
			 * System.out.println("key= " + entry.getKey() + " and value= " +
			 * entry.getValue());
			 * fieldCodeList.add(entry.getValue().get("FIELD_CODE"));
			 * 
			 * }
			 */
			// meta_id
			SqlTableUtil bStu = new SqlTableUtil("CD_METADATA", "");
			SqlWhereEntity bWhere = new SqlWhereEntity();
			bWhere.putWhere("DATA_CODE", str, WhereEnum.EQUAL_STRING);
			bStu.appendSelFiled("ID").appendSqlWhere(bWhere);
			Map<String, Object> findId = dcmsDAO.find(bStu);
			String meta_id = findId.get("ID").toString();
			//判断meta_id是否存在
			if(meta_id == null){
				String jsonMessage = "{\"message\":\"元数据无此表信息\"}";
				this.ajaxWrite(jsonMessage, request, response);
				return null;
			}

			// 根据meta_id查看明细表信息
			SqlTableUtil tableUtil = new SqlTableUtil("CD_METADATA_DETAIL", "");
			SqlWhereEntity sqlWhere = new SqlWhereEntity();
			sqlWhere.putWhere("METADATA_ID", meta_id, WhereEnum.EQUAL_STRING)
					.putWhere("DR", "0", WhereEnum.EQUAL_STRING);
			tableUtil.appendSelFiled("FIELD_CODE").appendSqlWhere(sqlWhere);
			List<Map<String, Object>> tableInfo = dcmsDAO.query(tableUtil);

			for (int i = 0; i < tableInfo.size(); i++) {
				for (Entry<String, Object> entry : tableInfo.get(i).entrySet()) {
					if (entry.getKey().equals("FIELD_CODE")) {
						fieldCodeList.add((String) entry.getValue());
					}
				}

			}
			// //查询表信息
			SqlTableUtil butStu = new SqlTableUtil("USER_TAB_COLUMNS", "col");
			SqlWhereEntity butWhere = new SqlWhereEntity();
			butWhere.putWhere("col.TABLE_NAME", str, WhereEnum.EQUAL_STRING);
			// butStu.appendJoinTable("", "col",
			// "COL.COLUMN_NAME = COM.COLUMN_NAME");
			butStu.appendJoinTable("USER_COL_COMMENTS", "com",
					"COL.TABLE_NAME = COM.TABLE_NAME AND COL.COLUMN_NAME = COM.COLUMN_NAME");
			butStu.setJoinType(JoinEnum.LEFT);
			butStu.appendSelFiled(
					"COL.COLUMN_NAME, COL.DATA_TYPE, COL.DATA_LENGTH, COL.NULLABLE, COM.COMMENTS")
					.appendSqlWhere(butWhere);
			// 查询表信息
			List<Map<String, Object>> query = dcmsDAO.query(butStu);
			//System.out.println("-------------------------------" + query);
			// 表字段COLUMN_NAME集合
			List<String> tableCodeValues = new ArrayList<String>();
			for (int i = 0; i < query.size(); i++) {
				for (Entry<String, Object> entry : query.get(i).entrySet()) {
					// System.out.println("key= " + entry.getKey() +
					// " and value= " + entry.getValue());
					if (entry.getKey().equals("COLUMN_NAME")) {
						tableCodeValues.add((String) entry.getValue());
					}
				}

			}
			// 判断主键
			SqlTableUtil sl = new SqlTableUtil("user_cons_columns", "a");
			SqlWhereEntity sy = new SqlWhereEntity();
			sy.putWhere("a.table_name", str, WhereEnum.EQUAL_STRING);
			sl.appendJoinTable("user_constraints", "b",
					"a.constraint_name = b.constraint_name and b.constraint_type = 'P'");
			sl.appendSelFiled("a.constraint_name,a.column_name")
					.appendSqlWhere(sy);
			Map<String, Object> findPK = dcmsDAO.find(sl);
			String CONSTRAINT_NAME = (String) findPK.get("CONSTRAINT_NAME");
			String FIELD_NAME = (String) findPK.get("COLUMN_NAME");
			// 消除重复元素
			tableCodeValues.removeAll(fieldCodeList);
			if (tableCodeValues.size() == 0) {
				continue;
			}

			for (int i = 0; i < query.size(); i++) {

				for (Entry<String, Object> entry : query.get(i).entrySet()) {

					// 判断tablecodevalues集合里的值和map值相同
					for (int k = 0; k < tableCodeValues.size(); k++) {
						if (entry.getKey().equals("COLUMN_NAME")
								&& tableCodeValues.get(k).equals(
										entry.getValue())) {
							// 得到需要的map
							Map<String, Object> needMap = query.get(i);
							HashMap<String, String> hashMap = new HashMap<String, String>();
							String COLUMN_NAME = (String) needMap
									.get("COLUMN_NAME");
							hashMap.put("FIELD_CODE", COLUMN_NAME);

							String COMMENTS = (String) needMap.get("COMMENTS");
							hashMap.put("FIELD_NAME", COMMENTS);

							String DATA_TYPE = (String) needMap
									.get("DATA_TYPE");
							hashMap.put("FIELD_TYPE", DATA_TYPE);

							String DATA_LENGTH = (String) needMap.get(
									"DATA_LENGTH").toString();
							hashMap.put("FIELD_LENGTH", DATA_LENGTH);

							if (needMap.get("NULLABLE").equals("Y")) {
								String NULLABLE = "0";
								hashMap.put("NULL_FLAG", NULLABLE);
							} else if (needMap.get("NULLABLE").equals("N")) {
								String NULLABLE = "1";
								hashMap.put("NULL_FLAG", NULLABLE);
							}

						

							if (query.get(i).get("COLUMN_NAME")
									.equals(FIELD_NAME)) {

								hashMap.put("KEY_FLAG", "1");
							} else {
								hashMap.put("KEY_FLAG", "0");
							}

							hashMap.put("SORT", null);
							hashMap.put("INPUT_TYPE", "0");
							hashMap.put("INPUT_FORMART", null);
							hashMap.put("INPUT_HTML", null);
							hashMap.put("SUMMARY", null);
							hashMap.put("VERSION", "1");
							hashMap.put("DR", "0");

							entityList.add(hashMap);
							

							

							
						}
					}

				}
			}
		

			dcmsDAO.insertByTransfrom("CD_METADATA_DETAIL", entityList);

		}
		String jsonMessage = "{\"message\":\"修改成功\"}";
		this.ajaxWrite(jsonMessage, request, response);
		return null;
	}

	@Override
	protected void afterOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO 自动生成的方法存根

	}

	protected void appendSqlTableUtil(SqlTableUtil sqlEntity,
			HttpServletRequest request) {

	}

}
