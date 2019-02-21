package com.yonyou.business.button.system.button;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;






import net.sf.json.JSONArray;

import com.yonyou.business.button.system.SystemButtonUtil;
import com.yonyou.util.BussnissException;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SQLUtil.WhereEnum;
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.util.sql.SqlWhereEntity;

/**按钮菜单关联管理---绑定
 * @author XIANGJIAN
 * @date 创建时间：2017年3月8日
 * @version 1.0
 */
public class SystemButtonButForBind extends SystemButtonUtil {

	@Override
	protected boolean befortOnClick(IBaseDao dcmsDAO,
			HttpServletRequest request, HttpServletResponse response) {
		
		return false;
	}

	@Override
	protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) throws IOException, BussnissException {
		String jsonMessage = "{\"message\":\"绑定成功\"}";
		String menu_id = request.getParameter("menu_id")==null?"":request.getParameter("menu_id");
		String menu_code = request.getParameter("menu_code")==null?"":request.getParameter("menu_code");
		String button_ids = request.getParameter("button_ids")==null?"":request.getParameter("button_ids");
		String butId[] = button_ids.split(",");
		
		SqlTableUtil rexStu = new SqlTableUtil("RM_BUTTON_RELATION_MENU","");
		SqlWhereEntity rexWhere = new SqlWhereEntity();
		rexWhere.putWhere("DR", "0", WhereEnum.EQUAL_STRING).putWhere("BUTTON_ID", button_ids, WhereEnum.IN)
		.putWhere("MENU_ID", menu_id, WhereEnum.EQUAL_STRING);
		rexStu.appendSelFiled("BUTTON_ID").appendSqlWhere(rexWhere);
		List<Map<String, Object>>  rexList =  dcmsDAO.query(rexStu);
		String ids = "" ;
		
		if(butId.length == rexList.size()){
			jsonMessage = "{\"message\":\"您所选按钮在该菜单下已存在！\"}";
		}else if(rexList.size()==0){
			ids = button_ids;
		}else if(butId.length > rexList.size()&&rexList.size()>0){
			Map<String,String> butmap = new HashMap<String, String>();
			for(int i = 0 ; i <butId.length ; i++){
				butmap.put(butId[i], butId[i]);
			}
			for(Map<String, Object> map : rexList){
				if(butmap.containsKey(map.get("BUTTON_ID"))){
					butmap.remove(map.get("BUTTON_ID"));
				}
			}
			for(String str : butmap.keySet()){
				ids +=str+",";
			}
			ids = ids.substring(0, ids.length()-1);
		}
		
		if(ids.length()>0&&menu_id.length()>0){
			
			//获取按钮表最新数据
			SqlTableUtil butStu = new SqlTableUtil("RM_BUTTON","");
			SqlWhereEntity butWhere = new SqlWhereEntity();
			butWhere.putWhere("DR", "0", WhereEnum.EQUAL_STRING).putWhere("ID", ids, WhereEnum.IN);
			butStu.appendSelFiled("ID,BUTTON_NAME,BUTTON_CODE").appendSqlWhere(butWhere);
			List<Map<String, Object>>  butList =  dcmsDAO.query(butStu);
			
			//'获取菜单表最新数据
			SqlTableUtil menuStu = new SqlTableUtil("RM_FUNCTION_NODE","");
			SqlWhereEntity menuWhere = new SqlWhereEntity();
			menuWhere.putWhere("DR", "0", WhereEnum.EQUAL_STRING).putWhere("ID", menu_id, WhereEnum.EQUAL_STRING);
			menuStu.appendSelFiled("ID,NAME,TOTAL_CODE").appendSqlWhere(menuWhere);
			Map<String, Object> menuMap = dcmsDAO.find(menuStu);

			//获取菜单节点下，最大的按钮编码
			String maxCode = "" ;
			SqlTableUtil stu = new SqlTableUtil("RM_BUTTON_RELATION_MENU" , "");
			SqlWhereEntity whereEntity  = new SqlWhereEntity();
			whereEntity.putWhere("MENU_ID", menu_id, WhereEnum.EQUAL_STRING).putWhere("DR", "0", WhereEnum.EQUAL_STRING).putWhere("MENU_CODE", menu_code, WhereEnum.EQUAL_STRING);
			stu.appendSelFiled("MAX(BUTTON_CODE)").appendSqlWhere(whereEntity);
			Map<String, Object> funMap = dcmsDAO.find(stu);
			if(funMap.get("MAX(BUTTON_CODE)") == null){
				maxCode = menu_code + "301" ; 
			}else{
				maxCode = (Long.parseLong(funMap.get("MAX(BUTTON_CODE)").toString()) + 1) + "" ;
			}
			
			List<Map<String, Object>> relList = new ArrayList<Map<String, Object>>();
			long i =  0 ; 
			for(Map<String, Object> map : butList){
				map.put("BUTTON_ID",  map.get("ID"));
				map.put("BUTTON_CODE",Long.parseLong(maxCode)+ i );
				map.put("MENU_ID", 	  menuMap.get("ID"));
				map.put("MENU_CODE",  menuMap.get("TOTAL_CODE"));
				map.put("MENU_NAME",  menuMap.get("NAME"));
				map.remove("ID");
				map.remove("RMRN");
				relList.add(map);
				i++;
			}
			dcmsDAO.insert("RM_BUTTON_RELATION_MENU", relList);
		}
		this.ajaxWrite(jsonMessage, request, response);
		return null;
	}

	@Override
	protected void afterOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
	}

}
