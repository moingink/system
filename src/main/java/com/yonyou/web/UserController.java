package com.yonyou.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.util.RmPartyConstants;
import org.util.tools.helper.RmStringHelper;

import com.yonyou.business.DataSourceUtil;
import com.yonyou.business.RmDictReferenceUtil;
import com.yonyou.business.button.ButtonAbs;
import com.yonyou.util.BussnissException;
import com.yonyou.util.IdRmrnUtil;
import com.yonyou.util.JsonUtils;
import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.util.sql.SqlWhereEntity;
import com.yonyou.util.sql.SQLUtil.WhereEnum;


/**
 * 
* @ClassName: PartyController 
* @Description: 角色管理控制类
* @author 潘志伟
* @date 2017年2月15日 
*
 */
@RestController
@RequestMapping(value = "/user")
public class UserController extends BaseController{

	@RequestMapping(value="passwordStrategy", method = RequestMethod.POST)
	 public void passwordStrategy(HttpServletRequest request, HttpServletResponse response) throws BussnissException {
		
		//获取参数
		JSONObject json = JSONObject.fromObject(request.getParameter("jsonData"));
		System.out.println("json:"+json);
		String userId = json.getString("userId");
		
		SqlTableUtil resEntity = new SqlTableUtil(RmPartyConstants.rmPasswordStrategyUser,"");
		resEntity.appendSelFiled("PASSWORD_STRATEGY_ID");
		resEntity.appendWhereAnd(" USER_ID = '"+userId+"' ");
		resEntity.appendWhereAnd(" DR = '0' ");
		resEntity.appendWhereAnd(" USABLE_STATUS = '1' ");
		List<Map<String, Object>> resList = BaseDao.getBaseDao().query(resEntity);
		String resJson = "";
		Map<String,Object> map = new HashMap<String,Object>();
		if(resList.size()>0){
			String strategyIds[] = new String[resList.size()];
			for(int i=0;i<resList.size();i++){
				strategyIds[i] = resList.get(i).toString();
			}
			map.put("strategyIds", RmStringHelper.parseToString(strategyIds));
			map.put("isUsing", true);
		}else{
			map.put("isUsing", false);
		}
		resJson = JsonUtils.object2json(map);
		
		System.out.println("result:"+resJson);
		response.setCharacterEncoding("utf-8");
		try {
			PrintWriter out = response.getWriter();
			
			out.print(resJson);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
	}
}
