package com.yonyou.util.jdbc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yonyou.business.MetaDataUtil;
import com.yonyou.util.BussnissException;
import com.yonyou.util.RmIdFactory;
import com.yonyou.util.SpringContextUtil;
import com.yonyou.util.sql.InsUpSqlEntity;
import com.yonyou.util.sql.SQLUtil;
import com.yonyou.util.sql.SqlStringupUpperToLowerTrans;
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.util.sql.SqlWhereEntity;
import com.yonyou.util.sql.StringToObjUtil;

/**
 * DAO类实现类
 * @author moing_ink
 * <p>创建时间 ： 2016年12月27日
 * @version 1.0
 */
public class BaseDao extends RmJdbcTemplate implements IBaseDao{

	@Override
	public Map<String, Object> find(SqlTableUtil tableEntity) {
		// TODO 自动生成的方法存根
		Map<String,Object>  entity =null;
		List<Map<String, Object>> list =this.query(tableEntity);
		if(list.size()>0){
			entity =list.get(0);
		}else{
			entity =new HashMap<String,Object>();
		}
		this.clearSqlTableUtil(tableEntity);
		return entity;
	}

	@Override
	public List<Map<String, Object>> query(SqlTableUtil tableEntity) {
		// TODO 自动生成的方法存根
		List<Map<String, Object>>  list =null;
		String strsql = SQLUtil.findSelectSql(tableEntity,tableEntity.findJoinType());
		if(!SQLUtil.isEmpty(strsql)){
			strsql=SqlStringupUpperToLowerTrans.transByRmJdbc(strsql);
			list =super.query(strsql,tableEntity.getFiled_class());
		}else{
			list =new ArrayList<Map<String,Object>>();
		}
//		this.clearSqlTableUtil(tableEntity);
		return list;
	}

	@Override
	public List<Map<String, Object>> query(SqlTableUtil tableEntity, int index,
			int size) {
		// TODO 自动生成的方法存根
		List<Map<String, Object>>  list =null;
		String strsql = SQLUtil.findSelectSql(tableEntity,tableEntity.findJoinType());
		if(!SQLUtil.isEmpty(strsql)){
			strsql=SqlStringupUpperToLowerTrans.transByRmJdbc(strsql);
			list =super.query(strsql,tableEntity.getFiled_class(),index,size);
		}else{
			list =new ArrayList<Map<String,Object>>();
		}
//		this.clearSqlTableUtil(tableEntity);
		return list;
	}

	@Override
	public String insert(String table,Map<String, Object> entity) {
		String[] ids =this.bulidInsertMapForId(entity, table);
		InsUpSqlEntity sqlEntity =SQLUtil.findInsert(table, entity);
		String updateSql=SqlStringupUpperToLowerTrans.transByRmJdbc(sqlEntity.getSql());
		super.update(updateSql, sqlEntity.getArrayObj());
		return ids[0];
	}

	@Override
	public String[] insert(String table,List<Map<String, Object>> entityList) {
		// TODO 自动生成的方法存根
		String[] ids =this.bulidInsertListForId(entityList, table);
		InsUpSqlEntity sqlEntity =SQLUtil.findInsert(table, entityList,new SqlWhereEntity());
		String updateSql=SqlStringupUpperToLowerTrans.transByRmJdbc(sqlEntity.getSql());
		super.batchUpdate(updateSql, sqlEntity.getArrayObj());
		return ids;
	}

	@Override
	public int update(String table,Map<String, Object> entity,SqlWhereEntity whereEntity) {
		// TODO 自动生成的方法存根
		InsUpSqlEntity sqlEntity =SQLUtil.findUpdateForObject(table, entity,whereEntity);
		String updateSql=SqlStringupUpperToLowerTrans.transByRmJdbc(sqlEntity.getSql());
		if(sqlEntity.getArrayObj()!=null&&sqlEntity.getArrayObj().length>0){
			return super.update(updateSql, sqlEntity.getArrayObj());
		}else{
			return super.update(updateSql);
		}
		
	}

	@Override
	public int update(String table,List<Map<String, Object>> entityList,SqlWhereEntity whereEntity) {
		// TODO 自动生成的方法存根
		int length=0;
		InsUpSqlEntity sqlEntity =SQLUtil.findUpdateObject(table, entityList,whereEntity);
		String updateSql=SqlStringupUpperToLowerTrans.transByRmJdbc(sqlEntity.getSql());
		int [] length_array = super.batchUpdate(updateSql,sqlEntity.getArrayObj());
		for(int len:length_array){
			length+=len;
		}
		return length;
	}
	
	public void clearSqlTableUtil(SqlTableUtil tableEntity){
		tableEntity.clearTableMap();
		tableEntity=null;
	}
	
	public static BaseDao getBaseDao(){
		
		return (BaseDao)SpringContextUtil.getBean("dcmsDAO");
	}

	@Override
	public int queryLength(SqlTableUtil tableEntity) {
		// TODO 自动生成的方法存根
		int length=0;
		String sql =SQLUtil.queryLengthSql(tableEntity);
		sql=SqlStringupUpperToLowerTrans.transByRmJdbc(sql);
		List<Map<String,Object>> entityList =super.query(sql,tableEntity.getFiled_class());
		if(entityList.size()==1){
			Map<String,Object> entity =entityList.get(0);
			if(entity.containsKey(SQLUtil.COUNT_LENGTH)){
				length=Integer.valueOf(entity.get(SQLUtil.COUNT_LENGTH).toString());
			}
		}
		
		return length;
	}
	
	
//	private int[] findIds(String table,int size){
//		String mark ="MID";
//		int [] ids =new int[size];
//		int id =1001001000;
//		SqlTableUtil tableEntity =new SqlTableUtil(table,"");
//		tableEntity.appendSelFiled("max(to_number(ID)) as "+mark);
//		Map<String,Object> MidMap =this.find(tableEntity);
//		if(MidMap.containsKey("MID")){
//			if(MidMap.get(mark)!=null){
//				id=Integer.parseInt(MidMap.get(mark).toString());
//			}
//		}
//		for(int i=0;i<size;i++){
//			id++;
//			ids[i]=id;
//			
//		}
//		return ids;
//	}
	
	private synchronized String [] bulidInsertMapForId(Map<String,Object> entityMap,String table){
		List<Map<String, Object>> entityList =new ArrayList<Map<String, Object>>();
		entityList.add(entityMap);
		return this.bulidInsertListForId(entityList, table);
	}
	
	private synchronized String[] bulidInsertListForId(List<Map<String, Object>> entityList,String table){
//		int [] ids =this.findIds(table, entityList.size());
		table=SqlStringupUpperToLowerTrans.transByRmJdbc(table);
		String [] ids = RmIdFactory.requestId(table, entityList.size());
//		int [] ids = new int[idsStr.length];
//		for(int i = 0;i<idsStr.length;i++){
//			ids[i] = Integer.parseInt(idsStr[i]);
//		}
		for(int i=0;i<entityList.size();i++){
			Map<String,Object> entity=entityList.get(i);
			boolean isUpdateId =false;
			BigDecimal zero=new BigDecimal("0");
			if(entity.containsKey("ID")){
				if((zero).equals(entity.get("ID"))||"".equals(entity.get("ID"))){
					isUpdateId=true;
				}
			}else{
				isUpdateId=true;
			}
			if(isUpdateId){
				entity.put("ID", new BigDecimal(ids[i]));
			}
		}
		return ids;
	}

	@Override
	public int delete(String table, SqlWhereEntity whereEntity) {
		// TODO 自动生成的方法存根
		InsUpSqlEntity sqlEntity =SQLUtil.findDeleteForObject(table, null,whereEntity);
		String updateSql=SqlStringupUpperToLowerTrans.transByRmJdbc(sqlEntity.getSql());
		if(sqlEntity.getArrayObj()!=null&&sqlEntity.getArrayObj().length>0){
			return super.update(updateSql, sqlEntity.getArrayObj());
		}else{
			return super.update(updateSql);
		}
	}
	
	@Override
	public List<Map<String, Object>> query(String strsql, String filed_class) {
		// TODO Auto-generated method stub
		strsql=SqlStringupUpperToLowerTrans.transByRmJdbc(strsql);
		return super.query(strsql, filed_class);
	}

	@Override
	public String insertByTransfrom(String table, Map<String, String> entity) throws BussnissException {
		// TODO 自动生成的方法存根
		return this.insert(table, StringToObjUtil.stringToObjForMap(entity, MetaDataUtil.getFiledTypeMap(table)));
	}

	@Override
	public String[] insertByTransfrom(String table, List<Map<String, String>> entityList) throws BussnissException {
		// TODO 自动生成的方法存根
		return this.insert(table,  StringToObjUtil.stringToObjForListMap(entityList, MetaDataUtil.getFiledTypeMap(table)));
	}

	@Override
	public int updateByTransfrom(String table, Map<String, String> entity,
			SqlWhereEntity whereEntity)
			throws BussnissException {
		// TODO 自动生成的方法存根
		return this.update(table, StringToObjUtil.stringToObjForMap(entity, MetaDataUtil.getFiledTypeMap(table)), whereEntity);
	}

	@Override
	public int updateByTransfrom(String table, List<Map<String, String>> entityList,
			SqlWhereEntity whereEntity)
			throws BussnissException {
		// TODO 自动生成的方法存根
		return this.update(table, StringToObjUtil.stringToObjForListMap(entityList, MetaDataUtil.getFiledTypeMap(table)), whereEntity);
	}

	@Override
	public int deleteByTransfrom(String table, SqlWhereEntity whereEntity)
			throws BussnissException {
		// TODO 自动生成的方法存根
		return this.delete(table, whereEntity);
	}

	@Override
	public int updateBySql(String sql) {
		// TODO 自动生成的方法存根
		sql=SqlStringupUpperToLowerTrans.transByRmJdbc(sql);
		return super.update(sql);
	}
}
