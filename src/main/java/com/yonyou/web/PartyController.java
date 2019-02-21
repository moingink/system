package com.yonyou.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.yonyou.business.DataSourceUtil;
import com.yonyou.business.RmDictReferenceUtil;
import com.yonyou.util.BussnissException;
import com.yonyou.util.JsonUtils;
import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.sql.SqlTableUtil;


/**
 * 
* @ClassName: PartyController 
* @Description: 组织结构控制类
* @author 潘志伟
* @date 2017年2月15日 
*
 */
@RestController
@RequestMapping(value = "/party")
public class PartyController extends BaseController{

	@RequestMapping(value="ajax", method = RequestMethod.GET)
	 public void ajaxDatas(HttpServletRequest request, HttpServletResponse response) throws BussnissException {
		String dataSourceCode = "RM_PARTY_RELATION_02";
		
		SqlTableUtil sqlEntity = DataSourceUtil.dataSourceToSQL(dataSourceCode);
		String parent = request.getParameter("PARENT_PARTY_CODE");
		System.out.println("parent:"+parent);
		
	/*	String condition = " PARENT_PARTY_CODE="+parent+" ";
		
		sqlEntity.appendWhereAnd(condition);*/
		
		//sqlEntity.appendSqlWhere(getQueryCondition(request));
		//System.out.println("PARENT_PARTY_CODE:"+sqlEntity.getWhereMap().get("PARENT_PARTY_CODE"));
		//sqlEntity.appendSqlWhere(sqlWhere);
		List<Map<String, Object>> mapList = BaseDao.getBaseDao().query(sqlEntity);
		mapList = RmDictReferenceUtil.transByDict(mapList, dataSourceCode);
		int total = 10;
		total = BaseDao.getBaseDao().queryLength(sqlEntity);
		//sqlEntity.clearTableMap();
		// 需要返回的数据有总记录数和行数据
		String json1 = "{\"total\":" + total + ",\"rows\":" + JsonUtils.object2json(mapList) + "}";
		//this.ajaxWrite(json, request, response);
		System.out.println("json1:"+json1);
		
		//List<Map<String, Object>> mylist=new ArrayList();
		//String num=request.getParameter("num");
		response.setCharacterEncoding("utf-8");
		try {
			PrintWriter out = response.getWriter();
		//int mynum=Integer.valueOf(num);
//		for(int i=0;i<mapList.size();i++)
//		{
//			Map<String, Object> map = mapList.get(i);
//			Map<String,Object> jsoMap=new HashMap();
//			jsoMap.put("CHILD_PARTY_CODE", map.get("CHILD_PARTY_CODE"));
//			jsoMap.put("CHILD_PARTY_NAME", map.get("CHILD_PARTY_NAME"));
//			jsoMap.put("CHILD_PARTY_ID", map.get("CHILD_PARTY_ID"));
//			jsoMap.put("PARENT_PARTY_CODE",map.get("PARENT_PARTY_CODE"));
//			
//			mylist.add(jsoMap);
//		}
		String json=toJOSNString(mapList);
		System.out.println("json:"+json);
		out.print(json);
		out.flush();
		out.close();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
	}
	//生成机构树
	private String toJOSNString(List<Map<String, Object>> mylist)
	{
		List<Map<String, Object>> jsonList=new ArrayList();
		if(!mylist.isEmpty())
		{
			for(int i=0;i<mylist.size();i++)
			{
				Map<String,Object> jsoMap=new HashMap();
				Map searchParams=mylist.get(i);
				Map<String,Object> data = new HashMap<String, Object>();
				data.put("CHILD_PARTY_TYPE_ID", searchParams.get("CHILD_PARTY_TYPE_ID"));
				data.put("CHILD_PARTY_ID",searchParams.get("CHILD_PARTY_ID"));
				data.put("CHILD_PARTY_CODE", searchParams.get("CHILD_PARTY_CODE"));
				jsoMap.put("id", searchParams.get("CHILD_PARTY_CODE"));
				jsoMap.put("text", searchParams.get("CHILD_PARTY_NAME"));
				jsoMap.put("data", data);
				jsoMap.put("icon","");
				if(searchParams.get("PARENT_PARTY_CODE").equals("A")){
					
					jsoMap.put("parent", "#");
				}else{
					jsoMap.put("parent", searchParams.get("PARENT_PARTY_CODE"));

				}
				jsonList.add(jsoMap);
			}

			return JsonUtils.object2json(jsonList);
		}
		return null;
	}
	
	@RequestMapping(value="getPartyTree", method = RequestMethod.GET)
	 public void getPartyTree(HttpServletRequest request, HttpServletResponse response) throws BussnissException {
		String dataSourceCode = "RM_PARTY_RELATION_FOR_TREE";
		String parent = request.getParameter("PARENT_PARTY_CODE");
		String viewId = request.getParameter("PARTY_VIEW_ID");
		
		SqlTableUtil sqlEntity = DataSourceUtil.dataSourceToSQL(dataSourceCode);
		sqlEntity.appendWhereAnd(" PARTY_VIEW_ID = '"+viewId+"' ");
		List<Map<String, Object>> mapList = BaseDao.getBaseDao().query(sqlEntity);
		mapList = RmDictReferenceUtil.transByDict(mapList, dataSourceCode);
		int total = 10;
		total = BaseDao.getBaseDao().queryLength(sqlEntity);
		// 需要返回的数据有总记录数和行数据
		String json1 = "{\"total\":" + total + ",\"rows\":" + JsonUtils.object2json(mapList) + "}";
		//this.ajaxWrite(json, request, response);
		System.out.println("json1:"+json1);
		response.setCharacterEncoding("utf-8");
		try {
			PrintWriter out = response.getWriter();
		String json=toJsonTreeString(mapList,parent);
		System.out.println("json:"+json);
		out.print(json);
		out.flush();
		out.close();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
	}
	
	//生成机构树
		private String toJsonTreeString(List<Map<String, Object>> mylist,String parent)
		{
			List<Map<String, Object>> jsonList=new ArrayList();
			if(!mylist.isEmpty())
			{
				for(int i=0;i<mylist.size();i++)
				{
					Map<String,Object> jsoMap=new HashMap();
					Map searchParams=mylist.get(i);
					Map<String,Object> data = new HashMap<String, Object>();
					data.put("CHILD_PARTY_TYPE_ID", searchParams.get("CHILD_PARTY_TYPE_ID"));
					data.put("CHILD_PARTY_ID",searchParams.get("CHILD_PARTY_ID"));
					data.put("CHILD_PARTY_CODE", searchParams.get("CHILD_PARTY_CODE"));
					jsoMap.put("id", searchParams.get("CHILD_PARTY_CODE"));
					jsoMap.put("text", searchParams.get("CHILD_PARTY_NAME"));
					jsoMap.put("data", data);
					jsoMap.put("icon","");
					if(searchParams.get("PARENT_PARTY_CODE").equals(parent)){
						
						jsoMap.put("parent", "#");
					}else{
						jsoMap.put("parent", searchParams.get("PARENT_PARTY_CODE"));

					}
					jsonList.add(jsoMap);
				}

				return JsonUtils.object2json(jsonList);
			}
			return null;
		}
	
	
	public static void main(String[] args) throws BussnissException
	{
		PartyController p = new PartyController();
		HttpServletRequest request=null ;
		 HttpServletResponse response = null;
		p.ajaxDatas(request,response);
	}
}
