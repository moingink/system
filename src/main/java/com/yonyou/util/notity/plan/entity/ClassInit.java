package com.yonyou.util.notity.plan.entity;

import com.yonyou.iuap.persistence.vo.pub.BusinessException;

public class ClassInit<T extends Object> {
	
	
	public T newInstance (String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException, BusinessException{
		
		Object obj =Class.forName(className).newInstance();
		T t =null;
		try{
			t =(T)obj;
		}catch ( Exception e){
			throw new BusinessException(className+"转换失败!");
		}
		return t;
	}
	
	
}
