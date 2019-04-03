package com.yonyou.business.orangefinancial.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/style")
public class MdStyleController {
	@RequestMapping(value = "/init")
	public String init(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("test");
		return null;
		
	}

}
