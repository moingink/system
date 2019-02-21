package com.yonyou.util.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.orm.ObjectRetrievalFailureException;

import com.yonyou.util.page.table.column.TableColumnUtil;
import com.yonyou.util.translate.AbsFieldTranslate;
import com.yonyou.util.translate.TranslateUtil;

/**
 * 数据转换类
 * @author moing_ink
 *
 * @param <T>
 */
public class RmRowMapperResultset<T extends List<Map<String,Object>>>  implements ResultSetExtractor{
	
	private String filed_class ="";
	private int rmrn =0;
	public RmRowMapperResultset(String _filed_class){
		filed_class=_filed_class;
		rmrn =0;
	}
	
	public RmRowMapperResultset(String _filed_class,int startIndex){
		filed_class=_filed_class;
		rmrn =startIndex;
	}
	
	
	/*
	 * （非 Javadoc）
	 * @see org.springframework.jdbc.core.ResultSetExtractor#extractData(java.sql.ResultSet)
	 */
	public T extractData(ResultSet rs) throws SQLException, DataAccessException {  
       T list = (T) new ArrayList();
       AbsFieldTranslate filedTSranslate =TranslateUtil.findTranslateClass(filed_class);
        try {  
            while (rs.next()) {  
            	rmrn++;
                Map<String, Object> map = new HashMap<String, Object>();  
                ResultSetMetaData rsMetaData =  rs.getMetaData();  
                int columnCount = rsMetaData.getColumnCount();  
                for(int colIndex = 1; colIndex <= columnCount; colIndex ++){  
                    String colName = rsMetaData.getColumnName(colIndex);  
                    Object value = JdbcUtils.getResultSetValue(rs, colIndex);
                    map.put(colName.toUpperCase(), value);
                }
                filedTSranslate.bulidFiledDataMap(map);
                this.appendRmrn(rmrn, map);
                list.add(map);  
            }  
        } catch (Throwable e) {  
            throw new ObjectRetrievalFailureException("拼装Map对象出错！", e);  
        }
        return list;  
    } 
	
	private void appendRmrn(int rmrn,Map<String,Object> dataMap){
		 if(!dataMap.containsKey(TableColumnUtil.RMRN)){
			 dataMap.put(TableColumnUtil.RMRN, rmrn);
         }
	}

}
