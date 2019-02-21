package com.yonyou.util.reflex.Field;

import java.lang.reflect.Field;

public class FieldEntity {
	
	
	private   Object classObj ;
	
	private   Field field ;
	
	
	public FieldEntity(Field _field,Object _classObj){
		field=_field;
		field.setAccessible(true);
		classObj=_classObj;
	}
	
	
	public Object getValue(){
		try {
			return field.get(classObj);
		} catch (IllegalArgumentException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return null;
	}
	
	
	public void setObject(Object objectValue){
		
		try {
			field.set(classObj, objectValue);
		} catch (IllegalArgumentException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
	}
	
}	
