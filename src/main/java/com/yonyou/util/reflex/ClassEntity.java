package com.yonyou.util.reflex;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.yonyou.util.reflex.Field.FieldEntity;
import com.yonyou.util.reflex.method.MethodEntity;

public class ClassEntity {
	
	
	private Map<String,MethodEntity> methodMaps =new HashMap<String,MethodEntity>();
	private Map<String,FieldEntity> filedMaps =new HashMap<String,FieldEntity>();
	
	public ClassEntity(Object obj){
		this.initMethodMaps(obj);
		this.initFieldMaps(obj);
	}
	
	private void initMethodMaps(Object obj){
		for(Method method:obj.getClass().getMethods()){
			methodMaps.put(method.getName(), new MethodEntity(method,obj));
		}
	}
	
	private void initFieldMaps(Object obj){
		for(Field filed:obj.getClass().getFields()){
			System.out.println(filed.getName());
			filedMaps.put(filed.getName(), new FieldEntity(filed,obj));
		}
		for(Field filed:obj.getClass().getDeclaredFields()){
			System.out.println(filed.getName());
			filedMaps.put(filed.getName(), new FieldEntity(filed,obj));
		}
	}
	
	public MethodEntity findMethod(String methodName){
		if(methodMaps.containsKey(methodName)){
			return methodMaps.get(methodName);
		}else{
			return null;
		}
	}
	
	public FieldEntity findFiled(String filedName){
		if(filedMaps.containsKey(filedName)){
			return filedMaps.get(filedName);
		}else{
			return null;
		}
	}
	
}
