package com.yonyou.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.yonyou.business.RmDictReferenceUtil;
import com.yonyou.util.SerialNumberUtil.SerialType;
import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.jdbc.IBaseDao;

public class projEdition {
   private IBaseDao isDao=BaseDao.getBaseDao();
   //自定义sql
   private String psql="";
   //自定以同步
   private Map<String,String> mapTable=null;
   private List<String> listTable=null;//历史表同步字段
   
   public projEdition(){
	   super();
   }
   public projEdition(String qsql){
	   this.psql=qsql;
   }
   public projEdition(Map<String,String> mapTable,List<String> listTable){
	   this.mapTable=mapTable;
	   this.listTable=listTable;
   }
   
   
   public void projed(Map<String,Object>map){
	   /*
	     table       元数据
	     id          当前id
	     pd          0：默认    1： 配置自定义sql 2：配置自定义同步  3：配置自定义sql&同步
	    
	    *mode_type   模块类型   
	    
	    *y_id        源id   
	    *main_id     主键id  
	    
	     hisdata     自定义历史表同步字段
	   */
	 
	 Date da=new Date();  
	 String nowTime=String.valueOf(da.getTime()); 
	 Map<String,Object> edMap=new HashMap<String,Object>();
	 edMap=map;
	 String projEdition="PROJ_EDITION";
	 String sql="";
	 
	 //默认
	 if(map.get("PD").equals("0")){
		 List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		 sql="select * from "+ map.get("TABLE").toString().toLowerCase()+" id="+map.get("ID"); //查询当前表信息
		 list=isDao.query(map.get("TABLE").toString(),"");
		 if(list.size()<=0){
			 System.err.println("*******************当前数据为空*********************");
		 } 
		 else{
			 list=removedata(list);
	         list.get(0).put("ID",nowTime);
	         isDao.insert(historyName(map.get("TABLE").toString()),list);
	         
	         
	        
	    
	         edMap.put("EDITION_NAME",EditionName(map.get("TABLE").toString()));  //版本名
	         edMap.put("EDITION",revalueString( map.get("ID").toString()+"V1.")); //版本号
	         edMap.put("P_ID", nowTime);                                          //历史表ID
	         //edMap.put("S_ID","8");                                             //排序编码
	         edMap.remove("TABLE");
	         edMap.remove("ID"); 
	         edMap.remove("PD");
	         isDao.insert(projEdition,edMap);
		 }
	 }
	 //配置自定义sql
	 else if(map.get("PD").equals("1")){
		    List<Map<String,Object>> list1=new ArrayList<Map<String,Object>>();
		    list1=isDao.query(psql,"");
			
		    if(list1.size()<=0){
				 System.err.println("*******************当前数据为空*********************");
			 } 
			else{
				 list1=removedata(list1);
		         list1.get(0).put("ID",nowTime);
		         isDao.insert(historyName(map.get("TABLE").toString()),list1);
		         
		         
		         
		      
		         edMap.put("EDITION_NAME",EditionName(map.get("TABLE").toString()));  //版本名
		         edMap.put("EDITION",revalueString( map.get("ID").toString()+"V1.")); //版本号
		         edMap.put("P_ID", nowTime);                                          //历史表ID
		         //edMap.put("S_ID","8");                                             //排序编码
		         edMap.remove("TABLE");
		         edMap.remove("ID"); 
		         edMap.remove("PD");
		         isDao.insert(projEdition,edMap);
			 }
	 }
	 //配置自定义同步
     else if(map.get("PD").equals("2")){
    	 List<Map<String,Object>> list2=new ArrayList<Map<String,Object>>();
		 sql="select * from "+ map.get("TABLE").toString().toLowerCase()+" id="+map.get("ID"); //查询当前表信息
		 list2=isDao.query(map.get("TABLE").toString(),"");
		 if(list2.size()<=0){
			 System.err.println("*******************当前数据为空*********************");
		 } 
		 else{
			 
			 List<Map<String,Object>> listhis=new ArrayList<Map<String,Object>>();
			 
			 list2=removedata(list2);
	       //list2.get(0).put("ID",nowTime);
	         String hissql="select * from  %s where "+map.get("HISDATA").toString()+"="+map.get("ID");
			 
	         for(Map.Entry<String,String> entry:mapTable.entrySet()){
				 listhis=isDao.query(String.format(hissql,entry.getKey()),"");
				 listhis=removedata(listhis);
				 
				 listhis.get(0).put("ID",nowTime);
				 isDao.insert(historyName(entry.getKey()),listhis);
				 
				 listhis.clear();
				 listhis=new ArrayList<Map<String,Object>>();
			 }

	     
	         edMap.put("EDITION_NAME",EditionName(map.get("TABLE").toString()));  //版本名
	         edMap.put("EDITION",revalueString( map.get("ID").toString()+"V1.")); //版本号
	         edMap.put("P_ID", nowTime);                                          //历史表ID
	         //edMap.put("S_ID","8");                                             //排序编码
	         edMap.remove("TABLE"); 
	         edMap.remove("ID"); 
	         edMap.remove("PD");
	         isDao.insert(projEdition,edMap);
		 }
		 
	 }
	 //配置自定义sql&同步
	 else{
		    List<Map<String,Object>> list3=new ArrayList<Map<String,Object>>();
		    list3=isDao.query(psql,"");
			
		    if(list3.size()<=0){
				 System.err.println("*******************当前数据为空*********************");
			 } 
			else{

				 
				 List<Map<String,Object>> listhis=new ArrayList<Map<String,Object>>();
				 
				 list3=removedata(list3);
		       //list2.get(0).put("ID",nowTime);
				 String hissql="";
		            int i=0;
				 //"select * from  %s where "+map.get("HISDATA").toString()+"="+map.get("ID");
				 
				 for(Map.Entry<String,String> entry:mapTable.entrySet()){
					 
					if(map.get("HISDATA").equals(listTable.get(i))){
							 hissql="select * from  %s where "+map.get("HISDATA").toString()+"="+map.get("ID");
						 }
				    else{
							 hissql="select * from  %s  where  "+listTable.get(i)+"="+map.get("ID");
						 }
					 
					 
		        	 listhis=isDao.query(String.format(hissql,entry.getKey()),"");
					 listhis=removedata(listhis);
					 
					 listhis.get(0).put("ID",nowTime);
					 isDao.insert(historyName(entry.getKey()),listhis);
					 
					 listhis.clear();
					 listhis=new ArrayList<Map<String,Object>>();
					 i++;
				 }

		         
		     
		         edMap.put("EDITION_NAME",EditionName(map.get("TABLE").toString()));  //版本名
		         edMap.put("EDITION",revalueString( map.get("ID").toString()+"V1.")); //版本号
		         edMap.put("P_ID", nowTime);                                          //历史表ID
		         //edMap.put("S_ID","8");                                             //排序编码
		         edMap.remove("TABLE");
		         edMap.remove("ID"); 
		         edMap.remove("PD");
		         isDao.insert(projEdition,edMap);
			 
			 }
		 
		 
		 
		 
	 }
	   
   }
   
   public List<Map<String,Object>> removedata(List<Map<String,Object>> lm){
	   for(Map<String,Object> m:lm){
		   m.remove("RMRN");
	   }   
	   return lm;
   }
   
  
   
   
   //获取历史版本元数据
   private String history="_HISTORY";
   public String historyName(String name){return name+history;} 
   
   //获取版本名
   private String  EditionName(String table){
		
   	try {
			ConcurrentHashMap<String, String> selectMap=RmDictReferenceUtil.getDictReference("EDITION_NAME");
		    for(Map.Entry<String,String> entry:selectMap.entrySet()){
		    	if(table.equals(entry.getKey())){
		    	   table=entry.getValue();
		    	}
		    }
   	
   	} catch (BussnissException e) {		
			e.printStackTrace();				
		}
   	return table;
   }
   //版本号
   public String   revalueString(String values){
	   
	   String value=SerialNumberUtil.getSerialCode(SerialType.PROJ_BIT_CODE,values);
	   
	   int isa=value.indexOf("V");
	   value=value.substring(isa,value.length());
	   String number=value.substring(3,4);
	   if(number.equals("0")){
		  String mString=value.substring(4,5);
		  int i=Integer.parseInt(mString)-1;
		  value=value.substring(0,3)+i;
	   }
	   else{
	   value=value.substring(0,5);   // value.substring(0,value.indexOf("."))+"."+ (Integer.parseInt(value.substring(value.indexOf(".")+1)) - 1);
	   }
	   return value;	   
   }
   
   
   
   
   
   
   
   
  public static void main(String[] args) {
	 
  }
   
   
   
}
