/**
 * 
//*******************************************************
//SQLUtil sqlUtl= new SQLUtil("表名", <"field1,... 需要更新的列名">,<"field_id,state ... 更新的条件列名">);
//看看是否能使用 copyProperties 方法来批量设置各个字段的值。

//赋值需要更新的列---开始。。。
sqlUtl.setValue("sigle_cust_total_amt",  field_value );
sqlUtl.setValue("update_date",  field_value );
sqlUtl.setValue("prod_code",  field_value );
sqlUtl.setValue("state",  field_value );
sqlUtl.setValue("operator",  field_value );
sqlUtl.setValue("start_date",  field_value );
sqlUtl.setValue("single_amt",  field_value );
sqlUtl.setValue("org_code",  field_value );
//赋值需要更新的列---结束
//赋值更新的条件列---开始。。。
sqlUtl.setValue("invt_type", field_value );
sqlUtl.setValue("auth_id", field_value );
//赋值更新的条件列---结束
String sql =  sqlUtl.getSQL();
Object param [] = sqlUtl.getParam();

//下面调用spring Template的方法
//********************************************************
 *
 */
package com.yonyou.util;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;

/** 
 * @author zzh
 * @version 创建时间：2016年9月14日
 * SQL组装类
 */
public class SQLUtil {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private Field [] fields_front = null; 				//对insert 来说是 需要插入的列,对update是需要update的列
	private Field [] fields_behind = null;				//对update 有用，是修改的条件列
	private String table_name = null;					//表名
	//private HashMap hm_position_front = new HashMap(); //前面的列位置信息
	//private HashMap hm_position_behind = new HashMap();//保存后面的列位置信息
	
	private int FLAG = 0;								//0-insert, 1-update, 2-删除, 3-查询
	
	public static void main(String[] args) {
	}
	
	//屏蔽没有参数的构造方法
	private SQLUtil(){}
	
	/**
	 * 查询 使用的构造方法
	 * @param tableName 表名
	 * @param fields_front_clause 例如： fields_sql_clause=" id,name,remark";
	 */
	public SQLUtil(String fields_front_clause)
	{	
		String [] fields = fields_front_clause.split(",");
		
		fields_front = new Field[fields.length];
		fields_behind = new Field[0]; 				//初始化为空的数组避免空指针的错误
		
		deal(fields,fields_front);
		
		FLAG =3;
	}

	
	/**
	 * delete 使用的构造方法
	 * @param tableName 表名
	 * @param fields_front_clause 例如： fields_sql_clause=" id,name,remark";
	 * @param DELETE_FLAG 输入任意值均可
	 */
	public SQLUtil(String table_name,String fields_front_clause, int DELETE_FLAG)
	{
		this.table_name = table_name.toLowerCase();		
		String [] fields = fields_front_clause.split(",");
		
		fields_front = new Field[fields.length];
		fields_behind = new Field[0]; 				//初始化为空的数组避免空指针的错误
		
		deal(fields,fields_front);
		FLAG=2;
	}	
	
	/**
	 * insert 使用的构造方法
	 * @param tableName 表名
	 * @param fields_front_clause 例如： fields_sql_clause=" id,name,remark";
	 */
	public SQLUtil(String table_name,String fields_front_clause)
	{
		this.table_name = table_name.toLowerCase();		
		String [] fields = fields_front_clause.split(",");
		
		fields_front = new Field[fields.length];
		fields_behind = new Field[0]; 				//初始化为空的数组避免空指针的错误
		
		deal(fields,fields_front);
		FLAG=0;
	}

	
	/**
	 * update 使用的构造方法
	 * @param tableName 表名
	 * @param fields_front_clause  需要被更新的列  例如： fields_front_clause="name,remark";
	 * @param fields_behind_clause 更新的条件列    例如： fields_behind_clause="id,state"
	 */
	public SQLUtil(String table_name,String fields_front_clause,String fields_behind_clause)
	{
		this.table_name = table_name.toLowerCase();		
		String [] fields = fields_front_clause.split(",");		
		fields_front = new Field[fields.length];	
		deal(fields,fields_front);
		
		fields = fields_behind_clause.split(",");		
		fields_behind = new Field[fields.length];	
		//hm_position_behind = new HashMap();
		deal(fields,fields_behind);	
		
		FLAG = 1;	
	}
	
	public SQLUtil(String table_name,HashMap fields_values)
	{
		//FLAG没传值默认查询
		FLAG=3;
//		this.table_name = table_name.toLowerCase();		
//		String [] fields = fields_front_clause.split(",");		
//		fields_front = new Field[fields.length];	
//		deal(fields,fields_front);
//		
//		fields = fields_behind_clause.split(",");		
//		fields_behind = new Field[fields.length];	
//		//hm_position_behind = new HashMap();
//		deal(fields,fields_behind);	
//		
//		FLAG = 1;	
	}
	
	public SQLUtil(String table_name,HashMap fields_values,HashMap fields_behind_values,String FLAG)
	{
//		this.table_name = table_name.toLowerCase();		
//		String [] fields = fields_front_clause.split(",");		
//		fields_front = new Field[fields.length];	
//		deal(fields,fields_front);
//		
//		fields = fields_behind_clause.split(",");		
//		fields_behind = new Field[fields.length];	
//		//hm_position_behind = new HashMap();
//		deal(fields,fields_behind);	
//		
//		FLAG = 1;	
	}
	
	
	private void deal(String [] fields, Field [] oFields)
	{
		for(int i=0;i<fields.length;i++)
		{
			fields[i] = fields[i].toLowerCase().trim();			
			
			oFields[i] = new Field();
			oFields[i].setFieldName(fields[i]);
		}		
	}


	private String getDeleteSQL() {
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM "+ table_name);

		if(fields_front.length>0)	sql.append(" WHERE ");
		
		for(int i=0;i<fields_front.length;i++)
		{
			if(i != 0)
			{
				sql.append(" AND ");
			}
			sql.append(fields_front[i].getFieldName()+"=?");
		}				
		return sql.toString();		
	}
	
	/**
	 * 得到Insert SQL 语句
	 * @param tableName
	 * @param fields
	 * @return
	 */
	private String getInsertSQL() {
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO "+ table_name +" (") ;
		
		StringBuffer sql1 = new StringBuffer();
		
		for(int i=0;i<fields_front.length;i++)
		{
			if(i != 0)
			{
				sql.append(",");
				sql1.append(",");
			}
			sql.append(fields_front[i].getFieldName());
			sql1.append("?");
		}
		
		sql.append(") VALUES (");
		sql.append(sql1);
		sql.append(")");	
		return sql.toString();
	}

	/**
	 * 得到Update SQL 语句
	 * @param tableName
	 * @param fields
	 * @return
	 */
	private String getUpdateSQL() {
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE "+ table_name +" SET ") ;
		
		StringBuffer sql1 = new StringBuffer();
		
		for(int i=0;i<fields_front.length;i++)
		{
			if(i != 0)
			{
				sql.append(",");
			}
			sql.append(fields_front[i].getFieldName()+"=?");
		}
		
		if(fields_behind.length>0)	sql.append(" WHERE ");
		
		for(int i=0;i<fields_behind.length;i++)
		{
			if(i != 0)
			{
				sql.append(" AND ");
			}
			sql.append(fields_behind[i].getFieldName()+"=?");
		}				
		return sql.toString();
	}	
	
	/**
	 * 得到SQL信息。当Insert,update，delete时有用
	 * 
	 * 如果update中有类似 set amt=amt+? 的语义时不能使用。
	 *  
	 * @return
	 * @throws BaseDataAccessException 
	 */
	public String getSQL() throws BaseDataAccessException
	{
		if(FLAG ==0 ) 
			return getInsertSQL();
		else if(FLAG ==1 )
			return getUpdateSQL();
		else if(FLAG ==2 )
			return getDeleteSQL();		
		else
			throw new BaseDataAccessException("您调用的构造方法， 不能提供足够的信息得到相应的SQL信息");
	}
	
	/**
	 * 设置字段的值
	 * 
	 * 此方法对于一个SQL中一个字段多次需要赋值，且值不同时使用。。。
	 * 
	 * 
	 * 
	 * 例如：sql = " UPDATE table_name SET state=?,remark=? Where id=? AND state=?
	 * 
	 * 		需要使用：	sqlUt.setValue(0,"state",value1); //第一次出现的值
	 * 					sqlUt.setValue(1,"state",value2); //第二次出现的值
	 * 		进行 赋值
	 * 
	 * 
	 * 如果多次出现，但是值相等，调用一次 setValue(field_name,field_value); 即可
	 * 
	 * @param index 下标值
	 * @param field_name
	 * @param value
	 * @throws BaseDataAccessException 
	 */
	public void  setValue(int index,String field_name,Object value) throws BaseDataAccessException
	{
		field_name = field_name.toLowerCase();
		
		
		boolean flag = true;
		int counter = 0;
		
		for(int i=0;i<fields_front.length;i++)
		{
			if(field_name.equals(fields_front[i].getFieldName()))
			{
				if(index == counter)
				{
					fields_front[i] .setValue(value);
					flag = false;
					return;
				}
				counter ++;
			}
		}
		
		for(int i=0;i<fields_behind.length;i++)
		{
			if(field_name.equals(fields_behind[i].getFieldName()))
			{
				if(index == counter)
				{
					fields_behind[i] .setValue(value);
					flag = false;
					return;
				}
				counter ++;
			}
		}		
		
		if(flag){
			throw new BaseDataAccessException("错误的列名：" +field_name);
		}		
			
	
	}
	
	
	
	/**
	 * 设置字段的值
	 * @param field_name
	 * @param value
	 * @throws BaseDataAccessException 
	 */
	public void  setValue(String field_name,Object value) throws BaseDataAccessException
	{
		field_name = field_name.toLowerCase();
		

		boolean flag = true;
		
		
		for(int i=0;i<fields_front.length;i++)
		{
			if(field_name.equals(fields_front[i].getFieldName()))
			{
				fields_front[i] .setValue(value);
				flag = false;
			}
		}
		
		for(int i=0;i<fields_behind.length;i++)
		{
			if(field_name.equals(fields_behind[i].getFieldName()))
			{
				fields_behind[i] .setValue(value);
				flag = false;
			}
		}		
		
		if(flag){
			throw new BaseDataAccessException("错误的列名：" +field_name);
		}		
	
	}

	
	public void printParam ()
	{
		for(int i=0;i<fields_front.length + fields_behind.length;i++)
		{
			String field_name = null;
			Object field_value = null;
			
			
			if(i<fields_front.length)
			{
				field_name=fields_front[i].getFieldName();		
				field_value = fields_front[i].getValue();
			}
			else
			{
				field_name=fields_behind[i - fields_front.length].getFieldName();
				field_value = fields_behind[i - fields_front.length].getValue();
			}
			logger.debug("Index=" +(i+1) +",field_name="+field_name+",value=" +field_value);
		}			
	
	}
	/**
	 * 得到参数
	 * @return
	 * @throws BaseDataAccessException 
	 */
	public Object[] getParam() throws BaseDataAccessException
	{
		Object [] param = new Object[fields_front.length + fields_behind.length];
		ArrayList list = new ArrayList();
		
		String field_name = null;
		
		for(int i=0;i<fields_front.length + fields_behind.length;i++)
		{
			
			if(i<fields_front.length)
			{
				param[i]=fields_front[i].getValue();

				//如果已经设值
				if(fields_front[i].getFlag()) continue;
				
				field_name = fields_front[i].getFieldName();
				
				list.add(field_name);
			}
			else
			{
				param[i] =fields_behind[i - fields_front.length].getValue();
				//如果已经设值
				if(fields_behind[i - fields_front.length].getFlag()) continue;
				
				field_name = fields_behind[i - fields_front.length].getFieldName();
				
				list.add(field_name);
			}
		}
		
		if(list.size()>0) throw new BaseDataAccessException("下列字段尚未绑定值：" +list);
		
		return param;
	}
	
	/**
	 *  将一个对象（通常是一个VO）中属性名字和 字段名相同的值进行批量的设置。
	 * 实现逻辑：对需要 插入、更新的字段进行循环，如果对象中有名字相同的属性（利用java反射），进行属性拷贝。
	 * @param vo
	 * @throws BaseDataAccessException 
	 * @throws BeansException 
	 */
	public void copyProperties(Object vo) throws BeansException, BaseDataAccessException
	{
		org.springframework.beans.BeanWrapper bw = new org.springframework.beans.BeanWrapperImpl(vo);
		

		for(int i=0;i<fields_front.length + fields_behind.length;i++)
		{
			String field_name = null;
			if(i<fields_front.length)
			{
				field_name=fields_front[i].getFieldName();				
			}
			else
			{
				field_name=fields_behind[i - fields_front.length].getFieldName();
			}
			
			//如果有可读的、和字段名同名的属性，进行值的设置
			if(bw.isReadableProperty(field_name))
			{
				setValue(field_name,bw.getPropertyValue(field_name) );
			}			
		}		
		
	}

	/**
	 *  将一个HashMap对象 中键值名字和 字段名相同的值进行批量的设置。
	 * 实现逻辑：
	 * @param vo
	 * @throws BaseDataAccessException 
	 */
	public void copyProperties(HashMap hm) throws BaseDataAccessException
	{
		for(int i=0;i<fields_front.length + fields_behind.length;i++)
		{
			String field_name = null;
			if(i<fields_front.length)
			{
				field_name=fields_front[i].getFieldName();				
			}
			else
			{
				field_name=fields_behind[i - fields_front.length].getFieldName();
			}
			
			//如果有和字段名同名的键值，进行值的设置
			if(hm.containsKey(field_name))
			{
				setValue(field_name, hm.get(field_name) );
			}			
		}		
		
	}	
	
	/**
	 * 此对象用来描述、存储一个字段的字段名、值等。主要是为了增加程序的可读性。可使用HashMap 代替。
	 * 
	 * comment for Field
	 * 
	 * @author zhengyan
	 *
	 */
	public class Field {
		private Object value = null;

		private String fieldName = null;
		
		//是否已经设置值的标志。供调试警告使用。
		private boolean flag = false; 	

		/**
		 * @return 返回 flag。
		 */
		public boolean getFlag() {
			return flag;
		}
		/**
		 * @return 返回 value。
		 */
		public Object getValue() {
			return value;
		}

		/**
		 * @param value 要设置的 value。
		 */
		public void setValue(Object value) {
			this.value = value==null? "" : value;
			flag = true;
		}

		/**
		 * @return 返回 fieldName。
		 */
		public String getFieldName() {
			return fieldName;
		}

		/**
		 * @param fieldName 要设置的 fieldName。
		 */
		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}
	}
	
	/**
	 * 学习这个方法的使用
	 *
	 */
	public void learn()
	{
		//如果是插入
		if(FLAG == 0)
		{
			System.out.println("//*******************************************************");
			System.out.println("//SQLUtil sqlUtl= new SQLUtil(\"表名\", <\"field1,field2... 需要替换\">);");
			System.out.println("//看看是否能使用 copyProperties 方法来批量设置各个字段的值。\n");
			System.out.println("//赋值---开始。。。");
			
			for(int i=0;i<fields_front.length;i++)
			{
			System.out.println("sqlUtl.setValue(\""+fields_front[i].getFieldName()+ "\", field_value );");
			
			}
			System.out.println("//赋值---结束");
			System.out.println("String sql =  sqlUtl.getSQL();");
			System.out.println("Object param [] = sqlUtl.getParam();");
			
			System.out.println("\n//下面调用spring Template的方法");				
			System.out.println("//********************************************************");						
			
		}
		else if(FLAG == 1)//修改
		{
			
			System.out.println("//*******************************************************");
			System.out.println("//SQLUtil sqlUtl= new SQLUtil(\"表名\", <\"field1,... 需要更新的列名\">,<\"field_id,state ... 更新的条件列名\">);");
			System.out.println("//看看是否能使用 copyProperties 方法来批量设置各个字段的值。\n");
			
			System.out.println("//赋值需要更新的列---开始。。。");
			
			for(int i=0;i<fields_front.length;i++)
			{
			System.out.println("sqlUtl.setValue(\""+fields_front[i].getFieldName()+ "\",  field_value );");
			
			}
			System.out.println("//赋值需要更新的列---结束");
			System.out.println("//赋值更新的条件列---开始。。。");

			for(int i=0;i<fields_behind.length;i++)
			{
			System.out.println("sqlUtl.setValue(\""+fields_behind[i].getFieldName()+ "\", field_value );");
			
			}			
			System.out.println("//赋值更新的条件列---结束");
							
			System.out.println("String sql =  sqlUtl.getSQL();");
			System.out.println("Object param [] = sqlUtl.getParam();");
			
			System.out.println("\n//下面调用spring Template的方法");				
			System.out.println("//********************************************************");						
			
		}
		else if(FLAG == 2)//删除
		{
			System.out.println("//*******************************************************");
			System.out.println("//SQLUtil sqlUtl= new SQLUtil(\"表名\", <\"field1,field2... 删除的条件列名\">,\"这个参数仅仅是为了告诉程序你希望做删除操作\");");
			System.out.println("//看看是否能使用 copyProperties 方法来批量设置各个字段的值。\n");
			System.out.println("//赋值---开始。。。");
			
			for(int i=0;i<fields_front.length;i++)
			{
			System.out.println("sqlUtl.setValue(\""+fields_front[i].getFieldName()+ "\", field_value );");
			
			}
			System.out.println("//赋值---结束");
			System.out.println("String sql =  sqlUtl.getSQL();");
			System.out.println("Object param [] = sqlUtl.getParam();");
			
			System.out.println("\n//下面调用spring Template的方法");				
			System.out.println("//********************************************************");						
			
		}
	}

}