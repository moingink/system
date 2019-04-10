package com.yonyou.web;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.yonyou.util.BussnissException;
import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.jdbc.IBaseDao;

@RestController
@RequestMapping(value = "/test")
public class TestDemoController extends BaseController {
	
	@Autowired
	protected IBaseDao dcmsDAO;
	
	// 查询子表数据信息
	@RequestMapping(value = "getDetailList", method = RequestMethod.POST)
	public String getProductList(HttpServletRequest request, HttpServletResponse response) throws IOException, BussnissException  {
		String  parent_id = request.getParameter("parent_id");
		String sqlStringDetail = "SELECT  * from md_personnel where  parent_id= '"+parent_id+"'";
		List<Map<String, Object>> listDetail = BaseDao.getBaseDao().query(sqlStringDetail, "");
		JSONArray array =new JSONArray();
		for(Map<String, Object> dataMap:listDetail){
			String idString = String.valueOf(dataMap.get("ID"));
			dataMap.remove("ID");
			dataMap.put("ID", idString);
			array.add(dataMap);
		}
		return JSON.toJSONString(array);
	}
		
}
