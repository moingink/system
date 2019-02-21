package com.yonyou.util.reflex.method;

public class MethodUtil {
	


	public static  Object [] bulidMethodParams(MethodEntity methodEntity){
		
		Object [] objects =null;
		int paramsLength =methodEntity.getParamClass().length;
		if(paramsLength>0){
			objects =new Object[paramsLength];
			int i=0;
			for(Class<?> c:methodEntity.getParamClass()){
				objects[i]=bulidObject(methodEntity,c);
				i++;
			}
		}else{
			objects =new Object[]{};
		}
		
		return objects;
	}
	
	
	private static Object bulidObject(MethodEntity methodEntity,Class<?> c){
		
		Object obj =null;
		if(methodEntity.paramsObject.containsKey(c.getName())){
			obj=methodEntity.paramsObject.get(c.getName());
			
		}else{
			try {
				obj =c.newInstance();
			} catch (InstantiationException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} 
		}
		return obj;
	}
}
