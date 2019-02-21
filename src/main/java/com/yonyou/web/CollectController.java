package com.yonyou.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.yonyou.business.DataSourceUtil;
import com.yonyou.business.RmDictReferenceUtil;
import com.yonyou.business.button.util.IPublicBusColumn;
import com.yonyou.business.entity.TokenEntity;
import com.yonyou.business.entity.TokenUtil;
import com.yonyou.util.BussnissException;
import com.yonyou.util.ExcelTools;
import com.yonyou.util.JsonUtils;
import com.yonyou.util.busflow.util.IBusFlowOperationType;
import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SQLUtil.WhereEnum;
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.util.sql.SqlWhereEntity;

@RestController
@RequestMapping(value = "/collect")
public class CollectController extends BaseController{

	@Autowired
	protected IBaseDao dcmsDAO;
	
	/**
	 * 计收汇总审批 -- 获取计收汇总审批列表
	 */
	@RequestMapping(value = "getConfirmSummaryAuditList",method = RequestMethod.GET)
	public String getConfirmSummaryAuditList(HttpServletRequest request,HttpServletResponse response) throws IOException,BussnissException{
		String term = request.getParameter("TERM");//账期开始时间
		String term1 = request.getParameter("TERM1");//账期结束时间
		String clientName = request.getParameter("CLIENT_NAME");//客户名称
		String incomeOfDept = request.getParameter("INCOME_OF_DEPT");//部门
		String businessType = request.getParameter("BUSINESS_TYPE");//业务类型
		String productType = request.getParameter("PRODUCT_TYPE");//产品
		String tableName = request.getParameter("TABLE_NAME");
		
		String pagesParam = "";
		if(term!=null && !"".equals(term)){
			pagesParam += " AND term>='"+term+"'";
		}
		if(term1!=null && !"".equals(term1)){
			pagesParam += " AND term<='"+term1+"'";
		}
		if(incomeOfDept!=null && !"".equals(incomeOfDept)){
			pagesParam += " AND income_of_dept LIKE '%"+incomeOfDept+"%'";
		}
		if(clientName!=null && !"".equals(clientName)){
			pagesParam += " AND client_name LIKE '%"+clientName+"%'";
		}
		if(businessType!=null && !"".equals(businessType)){
			pagesParam += " AND business_type LIKE '%"+businessType+"%'";
		}
		if(productType!=null && !"".equals(productType)){
			pagesParam += " AND product_type LIKE '%"+productType+"%'";
		}
		String sql = "";
		if("SUMMARY_AUDIT_TABLE".equals(tableName)){
			sql = "SELECT * FROM confirm_summary_audit WHERE 1=1 "+pagesParam;
		}else{
			sql = "SELECT * FROM view_adjust_income_statistics WHERE 1=1 "+pagesParam;
		}
		List<Map<String,Object>> mapList = dcmsDAO.query(sql,"");
		
		for (int i=0;i<mapList.size();i++) {
			//业务类型
			String business_type = mapList.get(i).get("BUSINESS_TYPE")!=null?mapList.get(i).get("BUSINESS_TYPE").toString():"";
			if(StringUtils.isNotBlank(business_type)){
				if (RmDictReferenceUtil.getDictReference("BUS_TYPE").containsKey(business_type)) {
					String value = RmDictReferenceUtil.getDictReference("BUS_TYPE").get(business_type);
					mapList.get(i).put("BUSINESS_TYPE", value);
				}
			}
			//市场类型
			String marketType = mapList.get(i).get("MARKET_TYPE")!=null?mapList.get(i).get("MARKET_TYPE").toString():"";
			if(StringUtils.isNotBlank(marketType)){
				if (RmDictReferenceUtil.getDictReference("SCLX").containsKey(marketType)) {
					String value = RmDictReferenceUtil.getDictReference("SCLX").get(marketType);
					mapList.get(i).put("MARKET_TYPE", value);
				}
			}
			//收费结算方式
			String settleMethod = mapList.get(i).get("SETTLE_METHOD")!=null?mapList.get(i).get("SETTLE_METHOD").toString():"";
			if(StringUtils.isNotBlank(settleMethod)){
				if (RmDictReferenceUtil.getDictReference("CHARGE_MODE").containsKey(settleMethod)) {
					String value = RmDictReferenceUtil.getDictReference("CHARGE_MODE").get(settleMethod);
					mapList.get(i).put("SETTLE_METHOD", value);
				}
			}
		}
		return JsonUtils.object2json(mapList);
	}
	

	
}
