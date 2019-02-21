package com.yonyou.util.sql;

/**
 * 新增、修改数据实体类
 * @author moing_ink
 * <p>创建时间 ： 2016年12月26日
 * @version 1.0
 */
public class InsUpSqlEntity {
	
	
	public InsUpSqlEntity(){
		
	}
	
	public void init(String _sql,Object [][] _objs){
		this.sql=_sql;
		this.arrayObj=_objs;
	}
	/**
	 * 查询语句
	 */
	private String sql ="";
	/**
	 * 预处理信息
	 */
	private Object [][] arrayObj =new Object[][]{};
	
	/**
	 * 获取SQL语句
	 * @return
	 */
	public String getSql() {
		return sql;
	}
	/**
	 * 设置修改语句
	 * @param sql 语句
	 */
	public void setSql(String sql) {
		this.sql = sql;
	}
	/**
	 * 获取预留值数据
	 * @return 对象数组
	 */
	public Object[][] getArrayObj() {
		return arrayObj;
	}
	/**
	 * 设置预留数据
	 * @param arrayObj 对象数组信息
	 */
	public void setArrayObj(Object[][] arrayObj) {
		this.arrayObj = arrayObj;
	}
	
	/**
	 * 获取SQL语句数据
	 * @return
	 */
	public String [] findSqls(){
		String [] sqls =new String[this.arrayObj.length];
		for(int i=0;i<arrayObj.length;i++){
			sqls[i]=String.format(sql.replaceAll("\\?","%s"),findVals(i));
		}
		return sqls;
	}
	/**
	 * 根据标示获取预留值数据信息
	 * @param i 标示
	 * @return 数据信息
	 */
	public String [] findVals(int i){
		String [] vals =new String[arrayObj[i].length];
		for(int j=0;j<vals.length;j++){
			if(arrayObj[i][j]!=null&&arrayObj[i][j].getClass().equals(String.class)){
				vals[j]="'"+arrayObj[i][j]+"'";
			}else{
				if(arrayObj[i][j]==null){
					vals[j]=null;
				}else{
					vals[j]=arrayObj[i][j].toString();
				}
				
			}
		}
		return vals;
	}
	
	public static void main(String [] args){
		String ss="update set id=?,name=? where v=?";
		Object[][] objs =new Object[][]{new String[]{"11","ssss","vvv"}};
		InsUpSqlEntity ins =new InsUpSqlEntity();
		ins.setSql(ss);
		ins.setArrayObj(objs);
		for(String sql:ins.findSqls()){
			System.out.println(sql);
		}
		
	}
}
