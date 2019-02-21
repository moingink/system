package com.yonyou.util.reflex;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SqlTableUtil;

public class ClassTest {
	
	public void ABC(){
		System.out.println("111222333");
	}
	
	public void updateTest(HttpServletRequest request,HttpServletResponse response,IBaseDao baseDao){
		System.out.println("-----buttonToken:"+request.getParameter("buttonToken"));
		SqlTableUtil tableUtil =new SqlTableUtil("CD_DATASOURCE","");
		tableUtil.appendSelFiled("*");
		List<Map<String,Object>> list =baseDao.query(tableUtil);
		
		for(Map<String,Object> map :list){
			System.out.println("-----tableUtil-----Map"+map);
		}
	}
	
	public String test(Object abc){
		System.out.println("-----test----"+abc);
		return "";
	}
	
	
	public String testAbc(String abc){
		System.out.println("-----testAbc----"+abc);
		return "";
	}
	
	public String testInt(int abc){
		System.out.println("-----testInt----"+abc);
		return "";
	}
}
