package com.yonyou.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yonyou.business.DataSourceUtil;
import com.yonyou.util.BussnissException;

/**
 * 
* @ClassName: DataSourceController 
* @Description: 数据源控制类 
* @author 博超
* @date 2016年12月27日 
*
 */
@RestController
@RequestMapping(value = "/dataSource")
public class DataSourceController extends BaseController {
	
	@Override
	public void insRow(HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		super.insRow(request, response);
		DataSourceUtil.clear();
		
	}
	
	@Override
	public void delRows(HttpServletRequest request,HttpServletResponse response) throws IOException, BussnissException {
		
		super.delRows(request, response);
		DataSourceUtil.clear();
		
	}
	
	@Override
	public void editRow(HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		super.editRow(request, response);
		DataSourceUtil.clear();
		
	}
	
}
