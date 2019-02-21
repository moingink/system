package com.yonyou.business.button.util;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yonyou.business.DataSourceUtil;
import com.yonyou.business.RmDictReferenceUtil;
import com.yonyou.business.button.ButtonAbs;
import com.yonyou.business.entity.TokenEntity;
import com.yonyou.business.entity.TokenUtil;
import com.yonyou.util.BussnissException;
import com.yonyou.util.JsonUtils;
import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.replace.WhereReplaceEntity;
import com.yonyou.util.replace.WhereReplaceUtil;
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.web.BaseController;

public class ButForSelect extends ButtonAbs{

	@Override
	protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) throws IOException, BussnissException {
		// TODO 自动生成的方法存根
		String dataSourceCode = request.getParameter("dataSourceCode");
		boolean boo = false;
		//处理供应商评价发布后指定部门 不需要数据权限 过滤掉
		String  noAuthString[]={"SU_EVAL_PLAN_CT_EVALUATION","FIN_INVOICE_REAL_OPEN","FIN_RECEIVED_PAYMENT"};
		for(String string :noAuthString){
			if(dataSourceCode.equals(string)){
				boo = true;
			}
		}
		if(dataSourceCode.startsWith("VIEW")||dataSourceCode.endsWith("REF")){
			boo = true;
		}
		//暂时处理以VIEW开头的视图 需要数据权限的功能 
		String authString[] ={"VIEW_CUS_LEVEL_MESSAGE","FIN_VIEW_COLL_SUMMARY","VIEW_RECEIVED_CLAIM"};
		for(String string :authString){
			if(dataSourceCode.equals(string)){
				boo = false;
			}
		}
		SqlTableUtil sqlEntity = DataSourceUtil.dataSourceToSQL(dataSourceCode);
		sqlEntity.appendSqlWhere(getQueryCondition(request));
		String pageParam=this.findPageParam(request);
		if(pageParam!=null&&pageParam.length()>0){
			sqlEntity.appendWhereAnd(pageParam);
		}
		TokenEntity tokenEntity = TokenUtil.initTokenEntity(request);
		//判断数据源code 是否系统   非系统则进行数据权限校验
		String sqlString = "SELECT DISTINCT meta.*  from cd_datasource  sour INNER JOIN cd_metadata meta  on  meta.DATA_CODE = sour.METADATA_CODE where  sour.DATASOURCE_CODE ='"+dataSourceCode+"'";
		List<Map<String, Object>> list = BaseDao.getBaseDao().query(sqlString, "");
		String systemFlag = "";
		for(Map<String, Object> dataMap:list){
			systemFlag = String.valueOf(dataMap.get("SYSTEM_FLAG"));
		}
		if(null != tokenEntity.getToken()&&!"".equals(tokenEntity.getToken())){
			if(!systemFlag.equals("1")){
				if(boo==false){
					sqlEntity =	BaseController.collectDataSQL(tokenEntity,sqlEntity,dataSourceCode);
				}
			}
		}
		
		String limitStr = request.getParameter("limit");
		String offsetStr = request.getParameter("offset");
		int limit = limitStr != null ? Integer.parseInt(limitStr) : 10;
		int offset = offsetStr != null ? Integer.parseInt(offsetStr) : 0;
		
		WhereReplaceEntity replaceEntity =new WhereReplaceEntity(dataSourceCode,request);
		//添加公司替换
		//replaceEntity.appendWhereAnd(" COMPANY_ID = '#COMPANY_COMPANY_ID#'");
		//添加替换数据
		WhereReplaceUtil.appendReplaceWhereSql(sqlEntity, dataSourceCode,replaceEntity, request);
		
		List<Map<String, Object>> mapList = dcmsDAO.query(sqlEntity, offset, limit);
		mapList = RmDictReferenceUtil.transByDict(mapList, dataSourceCode);
		int total = 10;
		total = dcmsDAO.queryLength(sqlEntity);
		sqlEntity.clearTableMap();
		// 需要返回的数据有总记录数和行数据
		String json = "{\"total\":" + total + ",\"rows\":" + JsonUtils.object2json(mapList) + "}";
		System.out.println("*********************"+"\t"+TOKEN_ENTITY.USER.getName()+
				"\t"+TOKEN_ENTITY.COMPANY.getCompany_name()+"****\t"+TokenUtil.findSystemDateString());
		this.ajaxWrite(json, request, response);
		return null;
	}

	@Override
	protected boolean befortOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	protected void afterOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO 自动生成的方法存根
		
	}
}
