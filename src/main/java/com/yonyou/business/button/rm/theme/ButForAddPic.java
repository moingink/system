package com.yonyou.business.button.rm.theme;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.yonyou.business.button.ButtonAbs;
import com.yonyou.util.BussnissException;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SqlTableUtil;

public class ButForAddPic extends ButtonAbs {

	@Override
	protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) throws IOException, BussnissException {
		String dataSourceCode =request.getParameter("dataSourceCode"); 
		String tabName = findTableNameByDataSourceCode(dataSourceCode);
		JSONObject json = JSONObject.fromObject(request.getParameter("jsonData"));
		//自动生成编码
		String a =getSerialNumber(dcmsDAO);
		json.put("PICTURE_CODE", a);
		dcmsDAO.insertByTransfrom(tabName, json);
		String jsonMessage = "{\"message\":\"保存成功\"}";
		this.ajaxWrite(jsonMessage, request, response);
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
	
	/**产生四位流水号
	 * @param dcmsDAO
	 * @return 0001~9999
	 */
	protected String getSerialNumber(IBaseDao dcmsDAO){
		SqlTableUtil stu = new SqlTableUtil("dual" ,"");
		stu.appendSelFiled("seq_picid.NEXTVAL");
		Map<String , Object> seqMap = dcmsDAO.find(stu);
		String seq = seqMap.get("NEXTVAL").toString();
		int seqNum = Integer.parseInt(seq);
		if (seqNum / 10 == 0) {
			seq = "000" + seq;
		} else if (seqNum / 10 >= 0 && seqNum / 10 <= 9) {
			seq = "00" + seq;
		} else if (seqNum / 10 >= 10 && seqNum / 10 <= 99) {
			seq = "0" + seq;
		} else {
			seq = "" + seq;
		}
		return seq;
	}

}
