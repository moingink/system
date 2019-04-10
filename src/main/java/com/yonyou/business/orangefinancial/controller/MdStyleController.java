package com.yonyou.business.orangefinancial.controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.yonyou.business.orangefinancial.service.MService;
/**
 * 数据样式设置节点 初始化按钮控制类
 * @author changjr
 *2019年4月3日13:57:18
 */
@RestController
@RequestMapping(value = "/style")
public class MdStyleController {
	@Autowired
	public MService mService;
	@RequestMapping(value="/init")
	public String init(HttpServletRequest request, HttpServletResponse response) {
		String sid =request.getParameter("sid");
		String smate =request.getParameter("smate");
		String smate_b =request.getParameter("smate_b");
		
		String innerHtml = mService.insertStyle(smate,smate_b, sid);
		return innerHtml;
		
	}

}
