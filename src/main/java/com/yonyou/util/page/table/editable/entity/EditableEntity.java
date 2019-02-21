package com.yonyou.util.page.table.editable.entity;

import net.sf.json.JSONObject;

import com.yonyou.util.page.table.editable.util.EditableModeEnum;
import com.yonyou.util.page.table.editable.util.EditableTypeEnum;
import com.yonyou.util.page.table.editable.util.IEditable;

public class EditableEntity implements IEditable {
	
	private JSONObject editableJson =new JSONObject();
	
	public EditableEntity(){
		init();
	}
	
	public JSONObject getJson(){
		return editableJson;
	}
	
	private void init(){
		//默认文本框
		editableJson.put(TYPE,EditableTypeEnum.TEXT.getType());
		//默认不可编辑
		editableJson.put(DISABLED, false);
		//默认数值为空
		editableJson.put(EMPTY_TEXT, "");
		//默认展示方位，为内嵌编辑
		editableJson.put(MODE, EditableModeEnum.POPUP.getType());
	}
	
	/**
	 * 设置标题
	 * @param title
	 * @return
	 */
	public EditableEntity setTitle(String title){
		editableJson.put(TITLE, title);
		return this;
	}
	
	
	public EditableEntity setType(EditableTypeEnum type){
		editableJson.put(TYPE,type.getType());
		return this;
	}
	
	public EditableEntity setValidate(String validate){
		editableJson.put(VALIDATE,validate);
		return this;
	}
	
	public EditableEntity setSource(String source){
		editableJson.put(SOURCE,source);
		return this;
	}
	
	public EditableEntity setMode(String mode){
		editableJson.put(MODE,mode);
		return this;
	}
	
}
