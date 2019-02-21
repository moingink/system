package com.yonyou.util.sql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.yonyou.util.BussnissException;
import com.yonyou.util.sql.SQLUtil.WhereEnum;
/**
 * 测试类
 * @author moing_ink
 *
 */
public class TestSql {

	public static void main(String[] args) {
		
		String aaaaa ="A,,B";
		System.out.println(aaaaa.split(",").length);
		// TODO 自动生成的方法存根
		 Map<String,Object> entity =new HashMap<String,Object>();
		 entity.put("name", new String("ZHANGSAN"));
		 entity.put("age", new Integer("18"));
		 entity.put("sex", new String("男"));
		 entity.put("id", "1001001000001,1001001000002");
		 
		 
		 Map<String,Object> entity1 =new HashMap<String,Object>();
		 entity1.put("name", new String("WANGWU"));
		 entity1.put("age", new Integer("19"));
		 entity1.put("sex", new String("女"));
		 entity1.put("id", "1001001000002");
		 
		 List<Map<String,Object>> entityList =new ArrayList<Map<String,Object>>();
		 entityList.add(entity1);
		 entityList.add(entity);
		 SqlWhereEntity whereEntity =new SqlWhereEntity();
		 
		 
		 SQLUtil.findUpdateObject("aaa", entityList, whereEntity);
		 
		 //---------------------------------------------------------------
		 SqlTableUtil message = new SqlTableUtil("rm_user","user");
			
			message.appendSelFiled("name")
			   	   .appendSelFiled("login_id")
			       .appendWhereAnd("user.id=1")
			       //.appendWhereAnd("user.login_id ='zhangfeng' ")
			       .appendOrderBy("id")
			       .appendOrderBy("name", "asc")
				   //.appendJoinTable("rm_aaa a","user.id = a.id")
			       //.appendJoinTable("rm_bb b","user.id = b.id")
				   .appendSelFiled("id")
				   ;
			
			SqlTableUtil message1 = new SqlTableUtil("rm_user_bak","bak");
			
			message1.appendSelFiled("name")
			   	   .appendSelFiled("login_id")
			       .appendWhereAnd("bak.id=1")
			       //.appendWhereAnd("user.login_id ='zhangfeng' ")
			       .appendOrderBy("id")
			       .appendOrderBy("name", "asc")
				   //.appendJoinTable("rm_aaa a","user.id = a.id")
			       //.appendJoinTable("rm_bb b","user.id = b.id")
				   .appendSelFiled("id")
				   ;
			
			SQLUtil sql =new SQLUtil();
			
			message.joinTable(message1, "user.id =bak.id");
			
			SqlTableUtil rm_party_relation = new SqlTableUtil("RM_PARTY_RELATION","rmp");
			rm_party_relation.init("substr(id),ID,PARTY_VIEW_ID,PARENT_PARTY_ID,CHILD_PARTY_ID,PARENT_PARTY_CODE,CHILD_PARTY_CODE,"
					+ "CHILD_PARTY_LEVEL,CHILD_IS_MAIN_RELATION,ORDER_CODE,PARENT_PARTY_NAME,"
					+ "CHILD_PARTY_NAME,PARENT_PARTY_TYPE_ID,CHILD_PARTY_TYPE_ID,"
					+ "CHILD_IS_LEAF,CUSTOM1,CUSTOM2,CUSTOM3,CUSTOM4,"
					+ "CUSTOM5,USABLE_STATUS,MODIFY_DATE,MODIFY_IP,"
					+ "MODIFY_USER_ID", "");
			
			rm_party_relation.appendJoinTable("AA a1", "rmp.id =a1.id").appendSelFiled("A1.A1");
			
			//rm_party_relation.appendJoinTable(System.ORG.TABLENAME+ ALIES , "rmp.id ="+System.ORG.ID).appendSelFiled(System.ORG.GETfIELDS);
			
			
			//OrgEmun.USER.getValue().joinTable(rm_party_relation, "us.id = rmp.child_party_id").joinTable(join, join_on_str);
			
		   SqlTableUtil rm_party_relation_bak =rm_party_relation.copyTable();
		   System.out.println(""+sql.findSelectSql(rm_party_relation.appendWhereAnd("ID =100000861"),"inner"));
		   System.out.println(""+sql.findSelectSql(rm_party_relation_bak));
		   
		   System.out.println("V#ABS".substring(2,"V#ABS".length() ));
		   Set<String> s =new HashSet<String>();
		   s.add("aa");
		   s.add("aa");
		   s.add("bb");
		   s.add("cc");
		   
		   Map<String,String>  abc =new HashMap<String,String>();
		   
		   abc.put("a", "1");
		   abc.put("b", "1");
		   abc.put("b", "2");
		   abc.put("b", "3");
		   abc.put("b", "4");
		   abc.put("b", "5");
		   abc.put("b", "6");
		   
		   for(String val:abc.keySet()){
			   System.out.println(val+"----------------"+abc.get(val));
		   }
		   
		   System.out.println(s.size());
		   
		   SqlWhereEntity  where =new SqlWhereEntity();
		   where.putWhere("name", "1,2,3,4,5", WhereEnum.IN);
		   where.putWhere("name", "1122", WhereEnum.EQUAL_INT);
		   Map<String,Object> entityIn =new HashMap<String,Object>();
		   entityIn.put("name", "zhangsan");
		
			InsUpSqlEntity insUp =SQLUtil.findUpdateForObject("t_test", entityIn, where);
			System.out.println(insUp.getSql());
		
	}

}
