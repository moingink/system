package com.yonyou.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yonyou.business.MetaDataUtil;
import com.yonyou.util.BussnissException;
import com.yonyou.util.sql.SQLUtil.WhereEnum;
import com.yonyou.util.sql.SqlWhereEntity;

/**
 * 
* @ClassName: MetaDataController 
* @Description: 元数据控制类
* @author 博超
* @date 2016年12月27日 
*
 */
@RestController
@RequestMapping(value = "/metaData")
public class MetaDataController extends BaseController {
	
	@Override
	public void insRow(HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		super.insRow(request, response);
		MetaDataUtil.clear();
		
	}
	
	@Override
	public void delRows(HttpServletRequest request,HttpServletResponse response) throws IOException, BussnissException {
		
		super.delRows(request, response);
		
		//逻辑删除子表记录 
		//FIXME 目前主子表删除不在一个事务中，存在风险
		Map<String, Object> entity = new HashMap<String, Object>();
		entity.put("DR","1");
		JSONArray jsonArray = JSONArray.fromObject(request.getParameter("jsonData"));
		SqlWhereEntity whereEntity  = new SqlWhereEntity();
		String ids = "";
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jb = (JSONObject)jsonArray.get(i);
			ids += jb.getString("ID") + ",";
		}
		whereEntity.putWhere(MetaDataUtil.FIELD_METADATA_ID,ids.substring(0, ids.length()-1), WhereEnum.IN);
		dcmsDAO.update(MetaDataUtil.FIELD_TABLE_NAME, entity, whereEntity);
		
		MetaDataUtil.clear();
		
	}
	//复制元数据主子表
	@RequestMapping(value = "copy", method = RequestMethod.POST)
	public String getProductList(HttpServletRequest request, HttpServletResponse response) throws IOException, BussnissException  {
		
		String  id = request.getParameter("id");
		//先查询  去掉主键
		
		return "";
	}
	@Override
	public void editRow(HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		super.editRow(request, response);
		MetaDataUtil.clear();
		
	}
	
}
