package com.yonyou.util;

import java.lang.reflect.Field;  
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;  
import java.sql.SQLException;  
import java.sql.Timestamp;
import java.sql.Types;

import org.springframework.jdbc.core.RowMapper;  
	  
/** 
 * @author zzh
 * @version 创建时间：2016年9月14日
 * 数据稽类型转换类类
 */
public class RowMapperUtil implements RowMapper<Object>{  
    private Class<?> className;  
      
    public RowMapperUtil(Class<?> className){  
        this.className = className;  
    }  
      
    /* 
	 * 该方法自动将数据库字段对应到Object中相应字段 
	 * 要求：数据库与Object中字段名相同 
	 *  
	 */  
	public Object  mapRow(ResultSet rs, int rowNum) throws SQLException {  
	      
	    Object nt = new Object();  
	    Field[] fields = className.getDeclaredFields();  
	    try {  
	        nt = className.newInstance();  
	        for (Field field : fields) {  
	            //如果结果中没有改field项则跳过  
				try {
				    rs.findColumn(getDatabaseColumn(field.getName()));  
				} catch (Exception e) {
				    continue;  
				}  
				//修改相应filed的权限  
				boolean accessFlag = field.isAccessible();  
				field.setAccessible(true);  
				if (field.getType().toString().indexOf("Date") != -1){
					field.set(nt, rs.getDate(getDatabaseColumn(field.getName())));
				} else if (field.getType().toString().indexOf("Clob") != -1){
					field.set(nt, rs.getClob(getDatabaseColumn(field.getName())));
				} else{
					String value = rs.getString(getDatabaseColumn(field.getName()));  
					value = value==null?"":value;  
					setFieldValue(nt, field, value);  
				}
			  
				//恢复相应field的权限  
		        field.setAccessible(accessFlag);  
	        }  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    }  
	    return nt;  
	}  
	
	/* 
	 * 根据类型对具体对象属性赋值 
	 */  
    private static void setFieldValue(Object form, Field field, String value) {  
  
    	if(null==value || "".equals(value)){
    		return ;
    	}
        String elemType = field.getType().toString();  
  
        if (elemType.indexOf("boolean") != -1||elemType.indexOf("Boolean") != -1) {  
		    try {  
		        field.set(form, Boolean.valueOf(value));  
		    } catch (IllegalAccessException e) {  
		        e.printStackTrace();  
		    }  
		} else if (elemType.indexOf("byte") != -1||elemType.indexOf("Byte") != -1) {  
		    try {  
		        field.set(form, Byte.valueOf(value));  
		    } catch (IllegalAccessException e) {  
		        e.printStackTrace();  
		    }  
		} else if (elemType.indexOf("char") != -1||elemType.indexOf("Character") != -1) {  
		    try {  
		        field.set(form, Character.valueOf(value.charAt(0)));  
		    } catch (IllegalAccessException e) {  
		        e.printStackTrace();  
		    }  
		} else if (elemType.indexOf("double") != -1||elemType.indexOf("Double") != -1) {  
		    try {  
		        field.set(form, Double.valueOf(value));  
		    } catch (IllegalAccessException e) {  
		        e.printStackTrace();  
		    }  
		} else if (elemType.indexOf("float") != -1||elemType.indexOf("Float") != -1) {  
		    try {  
		        field.set(form, Float.valueOf(value));  
		    } catch (IllegalAccessException e) {  
		        e.printStackTrace();  
		    }  
		} else if (elemType.indexOf("int") != -1||elemType.indexOf("Integer") != -1) {  
		    try {  
		        field.set(form, Integer.valueOf(value));  
		    } catch (IllegalAccessException e) {  
		        e.printStackTrace();  
		    }  
		} else if (elemType.indexOf("long") != -1||elemType.indexOf("Long") != -1) {  
		    try {  
		    	if ("".equals(value))
		    		field.set(form, 0l);
		    	else
		        field.set(form, Long.valueOf(value));  
		    } catch (IllegalAccessException e) {  
		        e.printStackTrace();  
		    }  
		} else if (elemType.indexOf("short") != -1||elemType.indexOf("Short") != -1) {  
		        try {  
		            field.set(form, Short.valueOf(value));  
		        } catch (IllegalAccessException e) {  
		            e.printStackTrace();  
		        }  
	    }else if (elemType.indexOf("date") != -1||elemType.indexOf("Date") != -1) {  
	        try {  
	            field.set(form, Date.valueOf(value));  
	        } catch (IllegalAccessException e) {  
	            e.printStackTrace();  
	        }  
	    } else if (elemType.indexOf("timestamp") != -1||elemType.indexOf("Timestamp") != -1) {  
	        try {  
	            field.set(form, Timestamp.valueOf(value));  
	        } catch (IllegalAccessException e) {  
	            e.printStackTrace();  
	        }  
    	} else if (elemType.indexOf("bigdecimal") != -1||elemType.indexOf("BigDecimal") != -1) {  
	        try {  
	            field.set(form, BigDecimal.valueOf(Double.valueOf(value)));  
	        } catch (IllegalAccessException e) {  
	            e.printStackTrace();  
	        }  
    	}else {  
	        try {  
	            field.set(form, (Object) value);  
	        } catch (IllegalAccessException e) {  
	            e.printStackTrace();  
	        }  
	    }  
	} 
    
    /**
     * 转换为数据库字段类型
	 * @param args
	 * @return
	 */
	private static int[] getSqlTypeFromArgs(Object[] args) {
		int types[] = new int[args.length];
		for (int i = 0; i < args.length; i++) {
			if (args[i] == null) {
				types[i] = Types.VARCHAR;
			} else if (args[i] instanceof java.sql.Timestamp) {
				types[i] = Types.TIMESTAMP;
			}else if (args[i] instanceof java.util.Date) {
				types[i] = Types.DATE;
			} else if (args[i] instanceof java.sql.Date) {
				types[i] = Types.DATE;
			} else if (args[i] instanceof java.sql.Time) {
				types[i] = Types.TIME;
			} else if (args[i] instanceof java.math.BigDecimal) {
				types[i] = Types.DECIMAL;
			} else if (args[i] instanceof Integer) {
				types[i] = Types.INTEGER;
			} else if (args[i] instanceof Long) {
				types[i] = Types.BIGINT;
			} else if (args[i] instanceof Short) {
				types[i] = Types.SMALLINT;
			} else if (args[i] instanceof Float) {
				types[i] = Types.FLOAT;
			} else if (args[i] instanceof Double) {
				types[i] = Types.DOUBLE;
			} else if (args[i] instanceof byte[]) {
				types[i] = Types.BLOB;
			} else {
				types[i] = Types.VARCHAR;
			}
		}
		return types;
	}
	
	private String getDatabaseColumn( String field){
		
		StringBuffer bf = new StringBuffer();
		if ( field != null){
			char[] chararray = field.toCharArray();
			for (char c : chararray) {
				if (Character.isUpperCase(c))
					bf.append("_"+ c);
				else
					bf.append(c);
			}
			
		}
		return bf.toString().toUpperCase();
	}
}
