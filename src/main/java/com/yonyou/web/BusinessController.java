package com.yonyou.web;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.sf.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.yonyou.business.DataSourceUtil;
import com.yonyou.business.RmDictReferenceUtil;
import com.yonyou.util.BussnissException;
import com.yonyou.util.JsonUtils;
import com.yonyou.util.SerialNumberUtil;
import com.yonyou.util.SerialNumberUtil.SerialType;
import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.replace.WhereReplaceUtil;
import com.yonyou.util.sql.SQLUtil.WhereEnum;
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.util.sql.SqlWhereEntity;

@RestController
@RequestMapping(value = "/business")
public class BusinessController extends BaseController{

	@Autowired
	protected IBaseDao dcmsDAO;
	
	/**
	 * 判断标准是否重复
	 * @param id ID
	 * @param standard 标准
	 * @return
	 */
	@RequestMapping(value = "isStandardRepeat", method = RequestMethod.GET)
	public String isStandardRepeat(String id,String standard){
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		map.put("valid", true);
		String sql = "SELECT COUNT(1) AS LENGTH FROM grading_standard WHERE pcs_id = '"+id+"' AND standard = '"+standard+"'";
		List<Map<String,Object>> list = dcmsDAO.query(sql, "");
		if(list.size()>0){
			int length = Integer.parseInt(list.get(0).get("LENGTH").toString());
			if(length>0){
				map.put("valid", false);
			}
		}
		return JSON.toJSONString(map);
	}
	
	/**
	 * 获取资费申请编号
	 * @return
	 */
	@RequestMapping(value = "getApplyCode", method = RequestMethod.GET)
	public String getApplyCode(){
		//资费申请编号生成规则：ZF+年月日+3位流水号
		Calendar cal = Calendar.getInstance();
		String code = "ZF"+String.valueOf(cal.get(Calendar.YEAR))+String.valueOf(String.format("%02d",cal.get(Calendar.MONDAY)+1))+String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
		String applyCode = SerialNumberUtil.getSerialCode(SerialType.THREE_BIT_CODE,code);
		return applyCode;
	}
	
	/**
	 * 刪除资费明细多余数据
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "delSurplusData",method = RequestMethod.GET)
	public int delSurplusDataById (HttpServletRequest request,HttpServletResponse response){
		String deleteIds = request.getParameter("deleteIds"); 
		SqlWhereEntity whereEntity =new SqlWhereEntity();
		whereEntity.putWhere("ID", deleteIds, WhereEnum.IN);
		return dcmsDAO.delete("postage_detail", whereEntity);
	}
	
	/**
	 * 获取申请人电话
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "getaApplyTel",method = RequestMethod.GET)
	public String getaApplyTel(HttpServletRequest request,HttpServletResponse response){
		String phone = "";
		String userId = request.getParameter("userId");
		String sql = " SELECT * FROM rm_user WHERE ID = "+userId+" ";
		List<Map<String,Object>> list = dcmsDAO.query(sql, "");
		for(Map<String,Object> map : list){
			phone = map.get("CUSTOM3")!=null?map.get("CUSTOM3").toString():"";
		}
		return JSON.toJSONString(phone);
	}
	
	/**
	 * 
	* @Title: init 
	* @Description: 查询数据库记录 
	* @param request
	* @param response
	* @throws Exception
	 */
	@RequestMapping(params = "initPostageDetail")
	public void init(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String dataSourceCode = request.getParameter("dataSourceCode");
		SqlTableUtil sqlEntity = DataSourceUtil.dataSourceToSQL(dataSourceCode);
		String pageParam=request.getParameter("pageParam");
		if(pageParam!=null&&pageParam.length()>2){
			pageParam=pageParam.substring(1, pageParam.length()-1);
			sqlEntity.appendWhereAnd(pageParam);
		}
		sqlEntity.appendSqlWhere(getQueryCondition(request));
		//添加替换数据
		WhereReplaceUtil.appendReplaceWhereSql(sqlEntity, dataSourceCode, request);
		List<Map<String, Object>> mapList = dcmsDAO.query(sqlEntity, 0, 10000);
		mapList = RmDictReferenceUtil.transByDict(mapList, dataSourceCode);
		for(int i=0;i<mapList.size();i++){
			String singleOrTotal = mapList.get(i).get("SINGLE_OR_TOTAL")!=null?mapList.get(i).get("SINGLE_OR_TOTAL").toString():null;
			if(StringUtils.isNoneBlank(singleOrTotal)){
				String flow = mapList.get(i).get("FLOW")!=null?mapList.get(i).get("FLOW").toString():"";//流量
				String standardPrice = mapList.get(i).get("STANDARD_PRICE")!=null?mapList.get(i).get("STANDARD_PRICE").toString():"";//流量标准价格
				String applicationPrice = mapList.get(i).get("APPLICATION_PRICE")!=null?mapList.get(i).get("APPLICATION_PRICE").toString():"";//流量申请价格
				String discountRate = mapList.get(i).get("DISCOUNT_RATE")!=null?mapList.get(i).get("DISCOUNT_RATE").toString():"";//流量折扣率
				String phonetics = mapList.get(i).get("PHONETICS")!=null?mapList.get(i).get("PHONETICS").toString():"";//语音
				String phoneticsStandardPrice = mapList.get(i).get("PHONETICS_STANDARD_PRICE")!=null?mapList.get(i).get("PHONETICS_STANDARD_PRICE").toString():"";//语音标准价格
				String phoneticsApplicationPrice = mapList.get(i).get("PHONETICS_APPLICATION_PRICE")!=null?mapList.get(i).get("PHONETICS_APPLICATION_PRICE").toString():"";//语音申请价格
				String phoneticsDiscountRate = mapList.get(i).get("PHONETICS_DISCOUNT_RATE")!=null?mapList.get(i).get("PHONETICS_DISCOUNT_RATE").toString():"";//语音折扣率
				String message = mapList.get(i).get("MESSAGE")!=null?mapList.get(i).get("MESSAGE").toString():"";//短信
				String messageStandardPrice = mapList.get(i).get("MESSAGE_STANDARD_PRICE")!=null?mapList.get(i).get("MESSAGE_STANDARD_PRICE").toString():"";//短信标准价格
				String messageApplicationPrice = mapList.get(i).get("MESSAGE_APPLICATION_PRICE")!=null?mapList.get(i).get("MESSAGE_APPLICATION_PRICE").toString():"";//短信申请价格
				String messageDiscountRate = mapList.get(i).get("MESSAGE_DISCOUNT_RATE")!=null?mapList.get(i).get("MESSAGE_DISCOUNT_RATE").toString():"";//短信折扣率
				String standardPriceSum = mapList.get(i).get("STANDARD_PRICE_SUM")!=null?mapList.get(i).get("STANDARD_PRICE_SUM").toString():"";//标准价格总计
				String applicationPriceSum = mapList.get(i).get("APPLICATION_PRICE_SUM")!=null?mapList.get(i).get("APPLICATION_PRICE_SUM").toString():"";//申请价格总计
				String discountRateSum = mapList.get(i).get("DISCOUNT_RATE_SUM")!=null?mapList.get(i).get("DISCOUNT_RATE_SUM").toString():"";//折扣率
				if(singleOrTotal.equals("single")){//单项
					String zfmx = "流量："+flow+"，流量标准价格："+standardPrice+"，流量申请价格："+applicationPrice+"，流量折扣率："+discountRate+"<br/>"+
								  "语音："+phonetics+"，语音标准价格："+phoneticsStandardPrice+"，语音申请价格："+phoneticsApplicationPrice+"，语音折扣率："+phoneticsDiscountRate+"<br/>"+
								  "短信："+message+"，短信标准价格："+messageStandardPrice+"，短信申请价格："+messageApplicationPrice+"，短信折扣率："+messageDiscountRate+"<br/>"+
								  "标准价格总计："+standardPriceSum+"，申请价格总计："+applicationPriceSum;
					 mapList.get(i).put("ZFMX", zfmx);
				}else if(singleOrTotal.equals("total")){//总和
					String zfmx = "流量："+flow+"，流量标准价格："+standardPrice+"<br/>"+
								  "语音："+phonetics+"，语音标准价格："+phoneticsStandardPrice+"<br/>"+
								  "短信："+message+"，短信标准价格："+messageStandardPrice+"<br/>"+
								  "标准价格总计："+standardPriceSum+"，申请价格总计："+applicationPriceSum+"，折扣率："+discountRateSum;
				 mapList.get(i).put("ZFMX", zfmx);
				}
			}
		}
		int total = dcmsDAO.queryLength(sqlEntity);
		sqlEntity.clearTableMap();
		// 需要返回的数据有总记录数和行数据
		String json = "{\"total\":" + total + ",\"rows\":" + JsonUtils.object2json(mapList) + "}";
		System.out.println("############" + json);
		out.print(json);
		out.flush();
		out.close();
	}
	
	/**
	 * 获取资费标准数据
	 * @return
	 */
	@RequestMapping(params = "getCharges", method = RequestMethod.GET)
	public String getCharges(){
		String sql = "SELECT CONCAT(traffic,traffic_unit) AS TITLE,standard_price AS STANDARD_PRICE FROM charges";
		List<Map<String, Object>> listMap = BaseDao.getBaseDao().query(sql, "");
		StringBuffer json = new StringBuffer().append("[");
		for(int i=0;i<listMap.size();i++){
			String title = listMap.get(i).get("TITLE")!=null?listMap.get(i).get("TITLE").toString():null;
			String standardPrice = listMap.get(i).get("STANDARD_PRICE")!=null?listMap.get(i).get("STANDARD_PRICE").toString():null;
			if(StringUtils.isNoneBlank(title) && StringUtils.isNoneBlank(standardPrice)){
				json.append("{").append("\"title\"").append(":").append("\""+title+"\"").append(",")
					.append("\"standardPrice\"").append(":").append("\""+standardPrice+"\"").append("},");
			}else{
				continue;
			}
		}
		json = json.length()>1?json.deleteCharAt(json.length() - 1).append("]"):json.append("]");
		return json.toString();
	}
	
	/**
	 * 获取 流量标准价格
	 * @param flow 流量
	 * @return
	 */
	@RequestMapping(params = "getStandardPrice", method = RequestMethod.GET)
	public String getStandardPrice(String flow){
		BigDecimal jieguo = new BigDecimal(0);
		if(StringUtils.isBlank(flow)){
			return "0";
		}
		String equalSql =  "select traffic_w AS TRAFFIC_W, standard_price AS STANDARD_PRICE FROM charges WHERE traffic_w = (SELECT MIN(traffic_w) FROM charges WHERE traffic_w = "+flow+" )";
		List<Map<String, Object>> equalListMap = BaseDao.getBaseDao().query(equalSql, "");
		if(equalListMap.size()>0){
			String standardPrice = equalListMap.get(0).get("STANDARD_PRICE")!=null?equalListMap.get(0).get("STANDARD_PRICE").toString():null;
			if(StringUtils.isNotBlank(standardPrice)){
				jieguo = new BigDecimal(standardPrice);
			}
		}else{
			String topSql =  "select traffic_w AS TRAFFIC_W, standard_price AS STANDARD_PRICE FROM charges WHERE traffic_w = (SELECT MIN(traffic_w) FROM charges WHERE traffic_w > "+flow+" )";
			List<Map<String, Object>> topListMap = BaseDao.getBaseDao().query(topSql, "");
			String bottomSql =  "select traffic_w AS TRAFFIC_W, standard_price AS STANDARD_PRICE FROM charges WHERE traffic_w = (SELECT MAX(traffic_w) FROM charges WHERE traffic_w < "+flow+" )";
			List<Map<String, Object>> bottomListMap = BaseDao.getBaseDao().query(bottomSql, "");
			BigDecimal topTraffic = new BigDecimal(0);//x2
			BigDecimal topStandardPrice = new BigDecimal(0);//m2
			BigDecimal bottomTraffic = new BigDecimal(0);//x1
			BigDecimal bottomStandardPrice = new BigDecimal(0);//m1
			if(topListMap.size()>0){
				topTraffic = topListMap.get(0).get("TRAFFIC_W")!=null?new BigDecimal(topListMap.get(0).get("TRAFFIC_W").toString()):new BigDecimal(0);
				topStandardPrice = topListMap.get(0).get("STANDARD_PRICE")!=null?new BigDecimal(topListMap.get(0).get("STANDARD_PRICE").toString()):new BigDecimal(0);
			}
			if(bottomListMap.size()>0){
				bottomTraffic = bottomListMap.get(0).get("TRAFFIC_W")!=null?new BigDecimal(bottomListMap.get(0).get("TRAFFIC_W").toString()):new BigDecimal(0);
				bottomStandardPrice = bottomListMap.get(0).get("STANDARD_PRICE")!=null?new BigDecimal(bottomListMap.get(0).get("STANDARD_PRICE").toString()):new BigDecimal(0);
			}
			BigDecimal chu = new BigDecimal(0);
			if(topTraffic.compareTo(bottomTraffic) != 0){
				BigDecimal xjianx1 = new BigDecimal(flow).subtract(bottomTraffic);
				BigDecimal x2jianx1 = topTraffic.subtract(bottomTraffic);
				chu = xjianx1.divide(x2jianx1, 10, BigDecimal.ROUND_HALF_DOWN);
			}
			BigDecimal m2jianm1 = topStandardPrice.subtract(bottomStandardPrice);
			BigDecimal chuchangm2jianm1 = chu.multiply(m2jianm1);
	        jieguo = bottomStandardPrice.add(chuchangm2jianm1);
	        jieguo = jieguo.setScale(2, BigDecimal.ROUND_HALF_UP).stripTrailingZeros();
		}
		return jieguo.toPlainString();
	}
	
	/**
	 * 获取 语音标准价格
	 * @param flow 流量
	 * @return
	 */
	@RequestMapping(params = "getPhoneticsStandardPrice", method = RequestMethod.GET)
	public String getPhoneticsStandardPrice(String phonetics){
		BigDecimal phoneticsStandardPrice = new BigDecimal(0);//语音标准价格
		if(StringUtils.isNotBlank(phonetics)){
			String sql = "SELECT voice_price FROM voice_or_message";
			List<Map<String, Object>> listMap = BaseDao.getBaseDao().query(sql, "");
			if(listMap.size()>0){
				String voicePrice = listMap.get(0).get("VOICE_PRICE")!=null?listMap.get(0).get("VOICE_PRICE").toString():null;
				if(StringUtils.isNotBlank(voicePrice)){
					phoneticsStandardPrice = new BigDecimal(phonetics).multiply(new BigDecimal(voicePrice));
				}
			}
		}
		phoneticsStandardPrice = phoneticsStandardPrice.setScale(2, BigDecimal.ROUND_HALF_UP).stripTrailingZeros();
		return phoneticsStandardPrice.toPlainString();
	}
	
	/**
	 * 获取 短信标准价格
	 * @param flow 流量
	 * @return
	 */
	@RequestMapping(params = "getMessageStandardPrice", method = RequestMethod.GET)
	public String getMessageStandardPrice(String message){
		BigDecimal messageStandardPrice = new BigDecimal(0);//短信标准价格
		if(StringUtils.isNotBlank(message)){
			String sql = "SELECT message_price FROM voice_or_message";
			List<Map<String, Object>> listMap = BaseDao.getBaseDao().query(sql, "");
			if(listMap.size()>0){
				String messagePrice = listMap.get(0).get("MESSAGE_PRICE")!=null?listMap.get(0).get("MESSAGE_PRICE").toString():null;
				if(StringUtils.isNotBlank(messagePrice)){
					messageStandardPrice = new BigDecimal(message).multiply(new BigDecimal(messagePrice));
				}
			}
		}
		messageStandardPrice = messageStandardPrice.setScale(2, BigDecimal.ROUND_HALF_UP).stripTrailingZeros();
		return messageStandardPrice.toPlainString();
	}
	
	/**
	 * 生成商机申请编号
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "getBusinessCode",method = RequestMethod.GET)
	public String getBusinessCode(HttpServletRequest request,HttpServletResponse response){
		String noFourCode = "";
		String busMainId = request.getParameter("busMainId");//包括业务主键
		if(StringUtils.isNotBlank(busMainId)){
			String[] split = busMainId.split(",");
			if(split.length>1){
				noFourCode = "I";
			}else{
				for (String string : split) {
					String sql = "SELECT type_value FROM buss_type WHERE ID = "+string;
					List<Map<String,Object>> list = dcmsDAO.query(sql, "");
					if(list.size()>0){
						noFourCode = list.get(0).get("TYPE_VALUE")!=null?list.get(0).get("TYPE_VALUE").toString():"";
					}
				}
			}
		}
		Calendar cal = Calendar.getInstance();
		String yearMonthDay = cal.get(Calendar.YEAR) + String.format("%02d", cal.get(Calendar.MONTH)+1) + cal.get(Calendar.DAY_OF_MONTH);
		String businessCode = SerialNumberUtil.getSerialCode(SerialType.FOUR_BIT_CODE, "Z9FCU" + noFourCode + yearMonthDay);
		return businessCode;
	}
	
	/**
	 * 获取应标项目组管理成员部门名称
	 * @param depId 部门Id
	 * @return
	 */
	@RequestMapping(value = "getDep", method = RequestMethod.GET)
	public String getDep(String depId){
		String sql = "SELECT NAME FROM tm_company WHERE id = "+depId;
		List<Map<String,Object>> list = dcmsDAO.query(sql, "");
		return list.size()>0?list.get(0).get("NAME").toString():"";
	}
	
	/**
	 * 生成公告编号
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "getNoticeCode",method = RequestMethod.GET)
	public String getNoticeCode(HttpServletRequest request,HttpServletResponse response){
		//String id = request.getParameter("ID");
		Calendar cal = Calendar.getInstance();
		String yearMonthDay = cal.get(Calendar.YEAR) + String.format("%02d", cal.get(Calendar.MONTH)+1) + cal.get(Calendar.DAY_OF_MONTH);
		String noticeCode = SerialNumberUtil.getSerialCode(SerialType.FOUR_BIT_CODE, "GG" + yearMonthDay);
		return noticeCode;
	}
	
	/**
	 * 生成中标结果中标业务号
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "getBusWinBidResult",method = RequestMethod.GET)
	public String getBusWinBidResult(HttpServletRequest request,HttpServletResponse response){
		Calendar cal = Calendar.getInstance();
		String yearMonthDay = cal.get(Calendar.YEAR) + String.format("%02d", cal.get(Calendar.MONTH)+1) + cal.get(Calendar.DAY_OF_MONTH);
		String businessCode = SerialNumberUtil.getSerialCode(SerialType.THREE_BIT_CODE, yearMonthDay);
		return businessCode;
	}
	
	/**
	 * 更改商机阶段
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "updateBusinessStage",method = RequestMethod.POST)
	public int updateBusinessStage(HttpServletRequest request,HttpServletResponse response){
		String businessNumber = request.getParameter("businessNumber"); 
		String sql = "UPDATE bus_business_message SET business_tage = '3' WHERE business_id = '"+businessNumber+"'";
		return dcmsDAO.updateBySql(sql);
	}
	
	/**
	 * 更改商机阶段(合同)
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "contractUpdateBusinessState",method = RequestMethod.POST)
	public String contractUpdateBusinessState(HttpServletRequest request,HttpServletResponse response){
		String state = request.getParameter("state"); 
		String businessNumber = request.getParameter("businessNumber"); 
		String sql = "UPDATE bus_business_message SET business_state = '"+state+"' WHERE business_id = '"+businessNumber+"'";
		dcmsDAO.updateBySql(sql);
		return null;
	}
	
	/**
	 * 更新商机信息前评估字段
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "updateBusinessPreAssessment",method = RequestMethod.POST)
	public int updateBusinessPreAssessment(HttpServletRequest request,HttpServletResponse response){
		String busId = request.getParameter("busId");
		String sql = " UPDATE bus_business_message SET pre_assessment = '0' WHERE id = '"+busId+"'";
		return dcmsDAO.updateBySql(sql);
	}
	
	/**
	 * 获取产品信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "getProductInfo",method = RequestMethod.GET)
	public String getProductInfo(HttpServletRequest request,HttpServletResponse response){
		String ids = "";
		String[] id = request.getParameter("ids").split(",");
		for(String str :id){
			ids += "'" + str + "'" + ",";
		}
		ids = ids.substring(0,ids.length()-1);
		String sql = "SELECT * FROM PRODUCT_INFO WHERE BUS_IDS IN ("+ids+")";
		List<Map<String,Object>> list = dcmsDAO.query(sql, "");
		return JsonUtils.object2json(list);
	}
	
	/**
	 * 判断应标项目组是否存在项目组成员
	 * @param parentId
	 * @return
	 */
	@RequestMapping(value = "isProjectGroupDetail", method = RequestMethod.GET)
	public boolean isProjectGroupDetail(String parentId){
		boolean flag = true;
		List<Map<String, Object>> queryList = dcmsDAO.query("SELECT COUNT(1) AS LENGTH FROM bus_y_project_group_detail WHERE parent_id = "+parentId, "");
		if(queryList.size()>0){
			if(Integer.valueOf(queryList.get(0).get("LENGTH").toString())>0){
				flag = false;
			}
		}
		return flag;
	}
	
	/**
	 * 判断发起应标是否被引用
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "isSendAssessmentQuote", method = RequestMethod.GET)
	public boolean isSendAssessmentQuote(String id){
		boolean flag = true;
		//应标项目组
		List<Map<String, Object>> queryList = dcmsDAO.query("SELECT COUNT(1) AS LENGTH FROM bus_y_project_group WHERE business_id = "+id, "");
		if(queryList.size()>0){
			if(Integer.valueOf(queryList.get(0).get("LENGTH").toString())>0){
				flag = false;
			}
		}
		return flag;
	}
	
	/**
	 * 判断商机是否评过分
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "isBusScore", method = RequestMethod.GET)
	public boolean isBusScore(String id){
		boolean flag = true;
		List<Map<String, Object>> queryList = dcmsDAO.query("SELECT COUNT(1) AS LENGTH FROM bus_bus_score WHERE bus_bus_examine_id = "+id, "");
		if(queryList.size()>0){
			if(Integer.valueOf(queryList.get(0).get("LENGTH").toString())>0){
				flag = false;
			}
		}
		return flag;
	}
	
	/**
	 * 合同补录编号申请分配合同号操作
	 * @param json
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "distributeContractNumber", method = RequestMethod.GET)
	public int distributeContractNumber(String json) {
		JSONObject jsonData = JSONObject.fromObject(json);
		if(jsonData.getString("BILL_STATUS").equals("7")){
			jsonData.remove("CONTRACT_NUMBER");
			jsonData.remove("DISTRIBUTION_PERSON");
			jsonData.remove("DISTRIBUTION_DATE");
		}
		SqlWhereEntity whereEntity = new SqlWhereEntity();
		whereEntity.putWhere("ID", jsonData.getString("ID"), WhereEnum.EQUAL_INT);
		int updateByTransfrom = 0;
		try {
			updateByTransfrom = dcmsDAO.updateByTransfrom("CONTRACT_SUPPLEMENT_NUMBER_APPLY", jsonData, whereEntity);
		} catch (BussnissException e) {
			e.printStackTrace();
		}
		return updateByTransfrom;
	}
	
	/**
	 * 获取数据字典信息
	* @param typeKeyword
	* @return
	* @throws BussnissException
	 */
	@RequestMapping(value = "getCodeType", method = RequestMethod.GET)
	public String getCodeType(String typeKeyword) throws BussnissException{
		ConcurrentHashMap<String, String> dictReference = RmDictReferenceUtil.getDictReference(typeKeyword);
		return JsonUtils.object2json(dictReference);
	}
	
	/**
	 * 根据项目类型查询项目分级区间总数
	* @param dataSourceCode
	* @param projectType
	* @param id
	* @return
	* @throws BussnissException
	 */
	@RequestMapping(value = "validProjectType", method = RequestMethod.GET)
	public int validProjectType(String dataSourceCode, String projectType ,String id) throws BussnissException{
		SqlTableUtil sqlEntity = DataSourceUtil.dataSourceToSQL(dataSourceCode);
		SqlWhereEntity whereEntity = new SqlWhereEntity();
		whereEntity.putWhere("PROJECT_TYPE", projectType, WhereEnum.EQUAL_INT);
		if(StringUtils.isNoneBlank(id)){
			whereEntity.putWhere("ID", id, WhereEnum.NOT_IN);
		}
		sqlEntity.appendSqlWhere(whereEntity);
		int querylength = dcmsDAO.queryLength(sqlEntity);
		return querylength;
	}
}
