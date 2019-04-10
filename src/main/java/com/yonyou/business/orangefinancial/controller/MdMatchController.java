package com.yonyou.business.orangefinancial.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yonyou.business.orangefinancial.service.MService;
import com.yonyou.business.orangefinancial.service.MatchService;

/**
 * 数据映射维护 控制器
 * 
 * @author changjr 2019年4月9日17:30:46
 */
@RestController
@RequestMapping(value = "/match")
public class MdMatchController {
	@Autowired
	public MatchService matchService;

	@RequestMapping(value = "/init")
	public String initH(HttpServletRequest request, HttpServletResponse response) {
		String id =request.getParameter("defid");
		String innerHtml = matchService.initH(id);
		return innerHtml;
	}

}
