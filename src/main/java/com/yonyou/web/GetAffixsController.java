package com.yonyou.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yonyou.util.OutPrintWriterTools;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.DataSourceCodeConstants;
import com.yonyou.util.sql.SQLUtil.WhereEnum;
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.util.sql.SqlWhereEntity;

/**
 * 附件查询
 * @author yansu
 *
 */
@RestController
@RequestMapping(value = "/getaffixs")
public class GetAffixsController  implements DataSourceCodeConstants{

	private static Logger logger = LoggerFactory.getLogger(GetAffixsController.class);
	
	@Autowired
	protected IBaseDao dcmsDAO;

	/**
	 * Test~
	 * @param request
	 * @param response
	 */
	@RequestMapping("/test")
	public void test(HttpServletRequest request,
			HttpServletResponse response){
		System.out.println("test");
	}
	
	
	/**
	 * 操作多节点附件数据
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("all")
	@RequestMapping("/getnodeafs")
	public void getNodeAfs(HttpServletRequest request, HttpServletResponse response) {
		JSONArray returnJsonArr = new JSONArray();
		JSONObject returnJsonEventually = new JSONObject();
		ArrayList<Map<String, Object>> listArr=new ArrayList<Map<String, Object>>();
		List listEventually=new ArrayList();
		JSONObject jsonobject = JSONObject.fromObject(request.getParameter("jsonData"));
		logger.info("入参{}" + jsonobject.toString());
		String dataSourceCode=jsonobject.getString("dataSourceCode");//页面数据源编码
		String _OPPORTUNITY_CODE=jsonobject.getString("_OPPORTUNITY_CODE");//商机编号
		
		switch (dataSourceCode) {
		case "PROJ_RELEASED":
			//本页面挂钩业务数组共有4个
			String tableName[]={"pre_assessment","bus_win_bid_result","BUS_CONTRACT_ADMIN"};//[前评估,应标文件及中标依据]
			String whereName[]={"BUSINESS_NUMBER","business_id","business_id"};//存放商机编号的字段
			String fieldName[]={"ENCLOSURE","UPLODING_ATT","ele_scanning"};//存放批次号的字段
			String niceName[]={"前评估资料","应标文件及中标依据","客户合同"};
			//DOC00259
			{//1
				SqlTableUtil stu = new SqlTableUtil("pre_assessment", "");
				SqlWhereEntity swe = new SqlWhereEntity();
				swe.putWhere("BUSINESS_NUMBER", _OPPORTUNITY_CODE, WhereEnum.EQUAL_STRING);
				stu.appendSelFiled("ENCLOSURE").appendSqlWhere(swe);// 返回selectfieldname
				List<Map<String, Object>> list = dcmsDAO.query(stu);
				for (Map<String, Object> maplist : list) {
					if (!"".equals(maplist.get("ENCLOSURE"))) {
						String batchno=(String) maplist.get("ENCLOSURE");
						JSONObject jo= new JSONObject();
						jo.put("batchNo", batchno);
						List<Map<String, Object>> fileinfo= getDataList(jo);
						for (int jj = 0; jj < fileinfo.size(); jj++) {
							JSONObject mm=(JSONObject.fromObject(fileinfo.get(jj)));
							mm.put("filename", "前评估资料");//niceName
							listArr.add(mm);
						}
					}
				}
				returnJsonEventually.put("pre_assessment_business_number", listArr);
				listArr.clear();
				//System.out.println(returnJsonEventually);
			}
			
			{//2
				SqlTableUtil stu = new SqlTableUtil("bus_win_bid_result", "");
				SqlWhereEntity swe = new SqlWhereEntity();
				swe.putWhere("business_id", _OPPORTUNITY_CODE, WhereEnum.EQUAL_STRING);
				stu.appendSelFiled("UPLODING_ATT").appendSqlWhere(swe);// 返回selectfieldname
				List<Map<String, Object>> list = dcmsDAO.query(stu);
				for (Map<String, Object> maplist : list) {
					if (!"".equals(maplist.get("UPLODING_ATT"))) {
						String batchno=(String) maplist.get("UPLODING_ATT");
						JSONObject jo= new JSONObject();
						jo.put("batchNo", batchno);
						List<Map<String, Object>> fileinfo= getDataList(jo);
						for (int jj = 0; jj < fileinfo.size(); jj++) {
							JSONObject mm=(JSONObject.fromObject(fileinfo.get(jj)));
							mm.put("filename", "应标文件及中标依据");//niceName
							listArr.add(mm);
						}
					}
				}
				returnJsonEventually.put("bus_win_bid_result_business_id", listArr);
				listArr.clear();
				//System.out.println(returnJsonEventually);
			}
			
			{//3
				SqlTableUtil stu = new SqlTableUtil("BUS_CONTRACT_ADMIN", "");
				SqlWhereEntity swe = new SqlWhereEntity();
				swe.putWhere("business_id", _OPPORTUNITY_CODE, WhereEnum.EQUAL_STRING);
				stu.appendSelFiled("ele_scanning").appendSqlWhere(swe);// 返回selectfieldname
				List<Map<String, Object>> list = dcmsDAO.query(stu);
				for (Map<String, Object> maplist : list) {
					if (!"".equals(maplist.get("ELE_SCANNING"))) {
						String batchno=(String) maplist.get("ELE_SCANNING");
						JSONObject jo= new JSONObject();
						jo.put("batchNo", batchno);
						List<Map<String, Object>> fileinfo= getDataList(jo);
						for (int jj = 0; jj < fileinfo.size(); jj++) {
							JSONObject mm=(JSONObject.fromObject(fileinfo.get(jj)));
							mm.put("filename", "客户合同");//niceName
							listArr.add(mm);
						}
					}
				}
				returnJsonEventually.put("bus_contract_admin_business_id", listArr);
				listArr.clear();
				//System.out.println(returnJsonEventually);
			}
			
			
			System.out.println(returnJsonEventually);
			
			
			
			
			
//			for (int i = 0; i < tableName.length; i++) {
//				SqlTableUtil stu = new SqlTableUtil(tableName[i], "");
//				SqlWhereEntity swe = new SqlWhereEntity();
//				swe.putWhere(whereName[i], _OPPORTUNITY_CODE, WhereEnum.EQUAL_STRING);
//				stu.appendSelFiled(fieldName[i]).appendSqlWhere(swe);// 返回selectfieldname
//				List<Map<String, Object>> list = dcmsDAO.query(stu);
//				for (Map<String, Object> maplist : list) {
//					Map ma=new HashMap();
//					ma.put(key, value)
//					if (!"".equals(maplist.get("ENCLOSURE"))) {
//						maplist.put("niceName", niceName[i]);
//						listArr.add(maplist);
//					}
//				}
//			}
//			
//				for (int ii = 0; ii < listArr.size(); ii++) {
//					JSONObject jo=(JSONObject) new JSONObject();
//					String aa= listArr.get(ii).toString();
//					String aaa[]=aa.split(",");
//					String aaaa[]=aaa[1].split("=");
//					String ka=aaaa[0];
//					jo.put("batchNo", aaaa[1]);
//					jo.put("niceName",  listArr.get(ii).get("niceName").toString());
//					List<Map<String, Object>> fileinfo= getDataList(jo);
//					for (int jj = 0; jj < fileinfo.size(); jj++) {
//						System.out.println(fileinfo.get(jj));
//						JSONObject mm=(JSONObject.fromObject(fileinfo.get(jj)));
//						mm.put("filename", (String)listArr.get(ii).get("niceName"));//niceName
//						listEventually.add(mm);
//					}
//				}
			
			OutPrintWriterTools.outPrW(response, returnJsonEventually);
			break;

		case "PROJ_RELEASED_NLXM1":
			System.out.println(_OPPORTUNITY_CODE);
			JSONArray jsobj=JSONArray.fromObject(_OPPORTUNITY_CODE);
			int i=0;
			for (Object jsobj_ : jsobj) {
				System.out.println(jsobj_);
				JSONObject jobj=jsonobject.fromObject(jsobj_);
				String batchno=(String) jobj.get("affixvalue");
				JSONObject jo= new JSONObject();
				jo.put("batchNo", batchno);
				List<Map<String, Object>> fileinfo= getDataList(jo);
				for (int jj = 0; jj < fileinfo.size(); jj++) {
					JSONObject mm=(JSONObject.fromObject(fileinfo.get(jj)));
					
					if (i==0) {
						mm.put("filename", "需求附件");//niceName
					}if (i==1) {
						mm.put("filename", "建议书附件");//niceName
					}
					listArr.add(mm);
					
				}
				
				if (i==0) {
					returnJsonEventually.put("req_affix", listArr);
				}if (i==1) {
					returnJsonEventually.put("pro_affix", listArr);
				}
				i++;
				listArr.clear();
			}

			System.out.println(returnJsonEventually);
			OutPrintWriterTools.outPrW(response, returnJsonEventually);
			break;
		
		case "BUS_CONTRACT_ADMIN":
			{//合同扫描件
				SqlTableUtil stu = new SqlTableUtil("BUS_CONTRACT_ADMIN", "");
				SqlWhereEntity swe = new SqlWhereEntity();
				swe.putWhere("ID", _OPPORTUNITY_CODE, WhereEnum.EQUAL_INT);
				stu.appendSelFiled("ELE_SCANNING").appendSqlWhere(swe);// 返回selectfieldname
				List<Map<String, Object>> list = dcmsDAO.query(stu);
				for (Map<String, Object> maplist : list) {
					if (!"".equals(maplist.get("ELE_SCANNING"))) {
						String batchno=(String) maplist.get("ELE_SCANNING");
						JSONObject jo= new JSONObject();
						jo.put("batchNo", batchno);
						List<Map<String, Object>> fileinfo= getDataList(jo);
						for (int jj = 0; jj < fileinfo.size(); jj++) {
							JSONObject mm=(JSONObject.fromObject(fileinfo.get(jj)));
							mm.put("filename", "合同扫描件");//niceName
							listArr.add(mm);
						}
					}
				}
				returnJsonEventually.put("bus_contract_admin_ele_scanning", listArr);
				listArr.clear();
			}
			{//合同审批单
				SqlTableUtil stu = new SqlTableUtil("BUS_CONTRACT_ADMIN", "");
				SqlWhereEntity swe = new SqlWhereEntity();
				swe.putWhere("ID", _OPPORTUNITY_CODE, WhereEnum.EQUAL_INT);
				stu.appendSelFiled("UPLOADING_ATT").appendSqlWhere(swe);// 返回selectfieldname
				List<Map<String, Object>> list = dcmsDAO.query(stu);
				for (Map<String, Object> maplist : list) {
					if (!"".equals(maplist.get("UPLOADING_ATT"))) {
						String batchno=(String) maplist.get("UPLOADING_ATT");
						JSONObject jo= new JSONObject();
						jo.put("batchNo", batchno);
						List<Map<String, Object>> fileinfo= getDataList(jo);
						for (int jj = 0; jj < fileinfo.size(); jj++) {
							JSONObject mm=(JSONObject.fromObject(fileinfo.get(jj)));
							mm.put("filename", "合同审批单");//niceName
							listArr.add(mm);
						}
					}
				}
				returnJsonEventually.put("bus_contract_admin_uploading_att", listArr);
				listArr.clear();
			}
			{//合同授权书
				SqlTableUtil stu = new SqlTableUtil("BUS_CONTRACT_ADMIN", "");
				SqlWhereEntity swe = new SqlWhereEntity();
				swe.putWhere("ID", _OPPORTUNITY_CODE, WhereEnum.EQUAL_INT);
				stu.appendSelFiled("OTHER_FILE").appendSqlWhere(swe);// 返回selectfieldname
				List<Map<String, Object>> list = dcmsDAO.query(stu);
				for (Map<String, Object> maplist : list) {
					if (!"".equals(maplist.get("OTHER_FILE"))) {
						String batchno=(String) maplist.get("OTHER_FILE");
						JSONObject jo= new JSONObject();
						jo.put("batchNo", batchno);
						List<Map<String, Object>> fileinfo= getDataList(jo);
						for (int jj = 0; jj < fileinfo.size(); jj++) {
							JSONObject mm=(JSONObject.fromObject(fileinfo.get(jj)));
							mm.put("filename", "合同授权书");//niceName
							listArr.add(mm);
						}
					}
				}
				returnJsonEventually.put("bus_contract_admin_other_file", listArr);
				listArr.clear();
			}
			OutPrintWriterTools.outPrW(response, returnJsonEventually);
			break;
			
		case "PROJ_REQUIREMENT": 
			{// 需求规划方案
				SqlTableUtil stu = new SqlTableUtil("PROJ_REQUIREMENT", "");
				SqlWhereEntity swe = new SqlWhereEntity();
				swe.putWhere("ID", _OPPORTUNITY_CODE, WhereEnum.EQUAL_INT);
				stu.appendSelFiled("REQUIREMENT_AFFIX").appendSqlWhere(swe);
				List<Map<String, Object>> list = dcmsDAO.query(stu);
				for (Map<String, Object> maplist : list) {
					if (!"".equals(maplist.get("REQUIREMENT_AFFIX"))) {
						String batchno = (String) maplist.get("REQUIREMENT_AFFIX");
						JSONObject jo = new JSONObject();
						jo.put("batchNo", batchno);
						List<Map<String, Object>> fileinfo = getDataList(jo);
						for (int jj = 0; jj < fileinfo.size(); jj++) {
							JSONObject mm = (JSONObject.fromObject(fileinfo.get(jj)));
							mm.put("filename", "需求规划方案");
							listArr.add(mm);
						}
					}
				}
				returnJsonEventually.put("bus_contract_admin_ele_scanning", listArr);
				listArr.clear();
			}
			{// 总办会汇报材料
				SqlTableUtil stu = new SqlTableUtil("PROJ_REQUIREMENT", "");
				SqlWhereEntity swe = new SqlWhereEntity();
				swe.putWhere("ID", _OPPORTUNITY_CODE, WhereEnum.EQUAL_INT);
				stu.appendSelFiled("REQUIREMENT_AFFIX_2").appendSqlWhere(swe);
				List<Map<String, Object>> list = dcmsDAO.query(stu);
				for (Map<String, Object> maplist : list) {
					if (!"".equals(maplist.get("REQUIREMENT_AFFIX_2"))) {
						String batchno = (String) maplist.get("REQUIREMENT_AFFIX_2");
						JSONObject jo = new JSONObject();
						jo.put("batchNo", batchno);
						List<Map<String, Object>> fileinfo = getDataList(jo);
						for (int jj = 0; jj < fileinfo.size(); jj++) {
							JSONObject mm = (JSONObject.fromObject(fileinfo.get(jj)));
							mm.put("filename", "总办会汇报材料");
							listArr.add(mm);
						}
					}
				}
				returnJsonEventually.put("bus_contract_admin_uploading_att", listArr);
				listArr.clear();
			}
			{// 总办会会议纪要
				SqlTableUtil stu = new SqlTableUtil("PROJ_REQUIREMENT", "");
				SqlWhereEntity swe = new SqlWhereEntity();
				swe.putWhere("ID", _OPPORTUNITY_CODE, WhereEnum.EQUAL_INT);
				stu.appendSelFiled("REQUIREMENT_AFFIX_3").appendSqlWhere(swe);
				List<Map<String, Object>> list = dcmsDAO.query(stu);
				for (Map<String, Object> maplist : list) {
					if (!"".equals(maplist.get("REQUIREMENT_AFFIX_3"))) {
						String batchno = (String) maplist.get("REQUIREMENT_AFFIX_3");
						JSONObject jo = new JSONObject();
						jo.put("batchNo", batchno);
						List<Map<String, Object>> fileinfo = getDataList(jo);
						for (int jj = 0; jj < fileinfo.size(); jj++) {
							JSONObject mm = (JSONObject.fromObject(fileinfo.get(jj)));
							mm.put("filename", "总办会会议纪要");
							listArr.add(mm);
						}
					}
				}
				returnJsonEventually.put("bus_contract_admin_other_file", listArr);
				listArr.clear();
			}
			System.out.println("returnJsonEventually : " + returnJsonEventually);
			OutPrintWriterTools.outPrW(response, returnJsonEventually);
			break;
		case "BUS_SEND_ASSESSMENT": 
			{// 发起应标
				SqlTableUtil stu = new SqlTableUtil("BUS_SEND_ASSESSMENT", "");
				SqlWhereEntity swe = new SqlWhereEntity();
				swe.putWhere("ID", _OPPORTUNITY_CODE, WhereEnum.EQUAL_INT);
				stu.appendSelFiled("UPLOADING_ATT").appendSqlWhere(swe);
				List<Map<String, Object>> list = dcmsDAO.query(stu);
				for (Map<String, Object> maplist : list) {
					if (!"".equals(maplist.get("UPLOADING_ATT"))) {
						String batchno = (String) maplist.get("UPLOADING_ATT");
						JSONObject jo = new JSONObject();
						jo.put("batchNo", batchno);
						List<Map<String, Object>> fileinfo = getDataList(jo);
						for (int jj = 0; jj < fileinfo.size(); jj++) {
							JSONObject mm = (JSONObject.fromObject(fileinfo.get(jj)));
							mm.put("filename", "上传附件");
							listArr.add(mm);
						}
					}
				}
				returnJsonEventually.put("bus_contract_admin_ele_scanning", listArr);
				listArr.clear();
			}
			System.out.println("returnJsonEventually : " + returnJsonEventually);
			OutPrintWriterTools.outPrW(response, returnJsonEventually);
			break;
		case "PRE_ASSESSMENT": 
			{// 前评估
				SqlTableUtil stu = new SqlTableUtil("PRE_ASSESSMENT", "");
				SqlWhereEntity swe = new SqlWhereEntity();
				swe.putWhere("ID", _OPPORTUNITY_CODE, WhereEnum.EQUAL_INT);
				stu.appendSelFiled("ENCLOSURE").appendSqlWhere(swe);
				List<Map<String, Object>> list = dcmsDAO.query(stu);
				for (Map<String, Object> maplist : list) {
					if (!"".equals(maplist.get("ENCLOSURE"))) {
						String batchno = (String) maplist.get("ENCLOSURE");
						JSONObject jo = new JSONObject();
						jo.put("batchNo", batchno);
						List<Map<String, Object>> fileinfo = getDataList(jo);
						for (int jj = 0; jj < fileinfo.size(); jj++) {
							JSONObject mm = (JSONObject.fromObject(fileinfo.get(jj)));
							mm.put("filename", "前评估附件");
							listArr.add(mm);
						}
					}
				}
				returnJsonEventually.put("pre_assessment_enclosure", listArr);
				listArr.clear();
			}
			System.out.println("returnJsonEventually : " + returnJsonEventually);
			OutPrintWriterTools.outPrW(response, returnJsonEventually);
			break;
		default:
			break;
		}
		

		
	}

	/**
	 * 操作附件数据
	 * @param request
	 * @param response
	 * @outprw [{"affixname":"文件名","affixfiledata":[{FILE_NAME=4A.txt, RMRN=1, ID=2018092800000000010},{}...]},{}]
	 */
	@SuppressWarnings("all")
	@RequestMapping("/getafs")
	public void getAfs(HttpServletRequest request, HttpServletResponse response ) {
		JSONArray returnJsonArr = new JSONArray();
		JSONArray jsonData=JSONArray.fromObject(request.getParameter("jsonData"));
		logger.info("入参{}"+jsonData.toString());
		for (Object _jsonobject : jsonData) {
			JSONObject paramJson = new JSONObject();
			if (((JSONObject) _jsonobject).size()>0) {
				JSONObject jsonobject=JSONObject.fromObject(_jsonobject);
				paramJson.put("affixname", jsonobject.getString("affixname"));
				if (jsonobject.containsKey("batchNo")) {
					List affixfiledataList=getDataList(jsonobject);
					paramJson.put("affixfiledata", affixfiledataList);
				}else {
					//没有批次号 则根据数据源编码查元数据名称 在根据_bs_code_field数据源表中存放批次号字段得到批次号
					String _bs_code=jsonobject.get("_bs_code").toString();
				}
			}
			if ((paramJson).size()>0) {
				returnJsonArr.add(paramJson);
			}
		}
		
		OutPrintWriterTools.outPrWArr(response, returnJsonArr);
	
	}
	

	/**
	 * 根据批次号去文档表取数据
	 * @param jsonobject
	 * @return List(ID,文件名List)
	 */
	@SuppressWarnings("all")
	public List getDataList(JSONObject jsonobject){
		List  list;
		List trsLit=new ArrayList();
		JSONObject jso=new JSONObject();
		//根据批次号去文档表取数据
		SqlTableUtil stu = new SqlTableUtil("DOC_DOCUMENT","");
		SqlWhereEntity swe = new SqlWhereEntity();
		String batchNo ="";
		if(jsonobject.containsKey("batchNo")){
			batchNo=jsonobject.getString("batchNo");
		}
		swe.putWhere("BATCH_NO", batchNo, WhereEnum.EQUAL_STRING);
		stu.appendSelFiled("ID").appendSelFiled("FILE_NAME").appendSqlWhere(swe);//返回ID,文件名List
		list =  dcmsDAO.query(stu);
		for (int i = 0; i < list.size(); i++) {
			Map m= (Map) list.get(i);
		//	trsLit.add("\"ID\":\""+m.get("ID").toString()+"\",\"FILE_NAME\":\""+m.get("FILE_NAME").toString()+"\"");//"ID":"2018092600000000004","FILE_NAME":"HJLiveChat.exe"
			
			jso.put("ID", m.get("ID").toString());
			jso.put("FILE_NAME", m.get("FILE_NAME").toString());
			trsLit.add(jso.toString());
			jso.clear();
		}
		//System.out.println(trsLit.toString());
		
		return  trsLit;
	}
	
	public static void main(String[] args) {
//		String a="[{\"aa\":\"11\"},{}]";
//		JSONArray jsonobject=JSONArray.fromObject(a);
//		jsonobject.size();
//		for (Object object : jsonobject) {
//			System.out.println(((JSONObject) object).size());
//			JSONObject jsonobject1=JSONObject.fromObject(object);
//			System.out.println(jsonobject1.get("aa"));
//		}
		
		
		String k[]={"1","2"};
		for (int i = 0; i < k.length; i++) {
			System.out.println(k[i]);
		}
	}

}
