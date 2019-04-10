package com.yonyou.business.orangefinancial.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yonyou.business.DataSourceUtil;
import com.yonyou.business.MetaDataUtil;
import com.yonyou.util.BussnissException;
import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.service.BaseService;
import com.yonyou.util.sql.SqlTableUtil;

import net.sf.json.JSONObject;

@Service
public class MService extends BaseService {
	@Autowired
	private IBaseDao baseDao;

	/**
	 * 根据
	 * 
	 * @param smate   元数据主表
	 * @param smate_b 元数据子表
	 * @param sid     元数据行主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public String insertStyle(String smate, String smate_b, String sid) {
		try {
			String[] type = new String[] { "template", "output", "input", "mobile" };
//			存放 映射关系定义里面维护的 对照表名 和 被对照表名
			List<String> tableList = new ArrayList<>();
//			获取 元数据信息 信息 map
			Map<String, Object> mate = new ConcurrentHashMap<>();

//			获取 对照映射 表信息
			SqlTableUtil sqlEntity = DataSourceUtil.dataSourceToSQL(smate);
			Map<String, String> hparam = new HashMap<>();
			hparam.put("ID=", sid);
			sqlEntity.appendWhereMap(hparam);
			List<Map<String, Object>> mmap = baseDao.query(sqlEntity);
//			对照表名
			String source = (String) mmap.get(0).get("SOURCE");
			tableList.add(source);
			System.out.println(mmap);
//			获取被对照表信息
			Map<String, String> bparam = new HashMap<>();
			bparam.put("PID=", sid);
			SqlTableUtil sqlEntityB = DataSourceUtil.dataSourceToSQL(smate_b);
			sqlEntityB.appendWhereMap(bparam);
			List<Map<String, Object>> bmap = baseDao.query(sqlEntityB);
			for (Map<String, Object> b : bmap) {
				tableList.add((String) b.get("DEST"));
			}
			for (String t : tableList) {
				mate.put(t, MetaDataUtil.getMetaDataFields(t));
			}
			System.out.println(tableList);
//			进行保存 样式主表
			for (String t : tableList) {
				if (checkHasInit(t, sid)) {
					continue;
				}
//				每一种表保存四个模板
				for (int i = 0; i < 4; i++) {
					JSONObject mdStyle = new JSONObject();
					String table = "BS_MD_STYLE";
					mdStyle.put("NAME", t);
					mdStyle.put("STABLE", t);
					mdStyle.put("SID", sid);
					mdStyle.put("TYPE", type[i]);
					System.out.println("****************mdStyle:" + mdStyle);
					baseDao.insertByTransfrom(table, mdStyle);
				}
			}
//			int j = 1 / 0; 事物测试
//初始化子表 明细信息
			Map<String, List<Object>> detail = new HashMap<>();
			for (String t : tableList) {
				detail.put(t, queryMetaDetailByName(t));
				for (int i = 0; i < type.length; i++) {
//					主表主键
					String pid = queryId(t, sid, type[i]);
					initStyleBody(detail.get(t), pid);
				}

			}
			System.out.println(detail);
		} catch (BussnissException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sid;
	}

	/**
	 * 检查是否被已经初始化
	 * 
	 * @param mate
	 * @param sid
	 * @return
	 * @throws BussnissException
	 */
	private Boolean checkHasInit(String mate, String sid) throws BussnissException {
//		Map<String, String> param = new HashMap<>();
//		param.put("SID", sid);
//		param.put("and STABLE", "'"+mate+"'");
//		SqlTableUtil sqlEntity = DataSourceUtil.dataSourceToSQL("BS_MD_STYLE");
//		sqlEntity.
		String sql = "select 1 from bs_md_style where dr=0 and sid='" + sid + "' and stable='" + mate + "'";
		List res = BaseDao.getBaseDao().query(sql, "");
		return !res.isEmpty();
	}

	/**
	 * 获取对应数据源明细字段
	 * 
	 * @param metaName
	 * @return
	 */
	private List<Object> queryMetaDetailByName(String metaName) {
		String sql = "select * from  cd_metadata_detail where METADATA_ID =(select  id from  cd_metadata where data_code ='"
				+ metaName + "' and dr=0) and dr = 0";
		List res = BaseDao.getBaseDao().query(sql, "");
		return res;
	}

	/**
	 * 主子分别保存时 获取主表的主键
	 * 
	 * @param mateName
	 * @param sid
	 * @param type
	 * @return
	 */
	private String queryId(String mateName, String sid, String type) {

		String sql = "select id from bs_md_style  where sid='" + sid + "' and  stable = '" + mateName + "' and  type ='"
				+ type + "' and dr = 0";
		List res = BaseDao.getBaseDao().query(sql, "");
		Map map = (Map) res.get(0);
		String id = String.valueOf(map.get("ID"));
		return id;
	}

	private void initStyleBody(List list, String pid) throws BussnissException {
		for (int i = 0; i < list.size(); i++) {
			JSONObject mdStyleB = new JSONObject();
			String table = "BS_MD_STYLE_B";
			mdStyleB.put("COL_NAME", ((Map) list.get(i)).get("FIELD_NAME"));
			mdStyleB.put("COL_CODE", ((Map) list.get(i)).get("FIELD_CODE"));
			mdStyleB.put("COL_ALIGN", "left");
			mdStyleB.put("COL_LENGTH", ((Map) list.get(i)).get("FIELD_LENGTH"));
			mdStyleB.put("COL_CONSTANT", "");
			mdStyleB.put("PID", pid);
//			mdStyleB.put("ISSHOW", "");
			System.out.println("****************mdStyleB:" + mdStyleB);
			baseDao.insertByTransfrom(table, mdStyleB);
		}
	}
}
