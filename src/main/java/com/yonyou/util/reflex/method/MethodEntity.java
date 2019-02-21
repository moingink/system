package com.yonyou.util.reflex.method;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yonyou.util.jdbc.IBaseDao;

public class MethodEntity {
	
	private Method  method ;
	private HttpServletRequest request ;
	private HttpServletResponse response;
	private IBaseDao baseDao; 
	private Object classObj ;
	
	public Map<String,Object> paramsObject =new HashMap<String,Object>();
	private Class<?>[] paramClass ;
	
	
	{
		paramsObject.put(int.class.getName(), 0);
		paramsObject.put(Integer.class.getName(), 1);
		
	}
	
	public MethodEntity(Method _method,Object _classObj){
		method =_method;
		classObj=_classObj;
		this.init();
	}
	
	private void init(){
		Type [] types=method.getGenericParameterTypes();
		if(types.length>0){
			paramClass =new Class<?>[types.length];
			int i=0;
			for(Class<?> paramType:method.getParameterTypes()){
				paramClass[i]=paramType;
				i++;
			}
		}else{
			paramClass =new Class<?>[]{};
		}
		
	}
	
	public MethodEntity initParams(HttpServletRequest _request,HttpServletResponse _response,IBaseDao _baseDao){
		this.initHttpParams(_request, _response);
		this.initBaseDao(_baseDao);
		return this;
	}
	
	public void initHttpParams(HttpServletRequest _request,HttpServletResponse _response){
		request=_request;
		response=_response;
		paramsObject.put(HttpServletRequest.class.getName(), request);
		paramsObject.put(HttpServletResponse.class.getName(), response);
	}
	
	public void initBaseDao(IBaseDao _baseDao){
		baseDao=_baseDao;
		paramsObject.put(IBaseDao.class.getName(), baseDao);
	}
	
	public boolean  invoke(Object obj) {
		boolean returnFlay =false;
		
		try {
			method.invoke(obj,MethodUtil.bulidMethodParams(this));
			returnFlay=true;
		} catch (IllegalAccessException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		return returnFlay;
	}
	
	
	public boolean  invoke() {
		return invoke(classObj);
	}
	
	

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public IBaseDao getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(IBaseDao baseDao) {
		this.baseDao = baseDao;
	}

	public Class<?>[] getParamClass() {
		return paramClass;
	}

	public void setParamClass(Class<?>[] paramClass) {
		this.paramClass = paramClass;
	}
	
	
	
	
}
