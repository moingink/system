package com.yonyou.util.page.table.column;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import com.yonyou.util.page.table.editable.entity.EditableEntity;

/**
 * 列工具列
 * @author moing_ink
 * <p>创建时间 ： 2016年12月27日
 * @version 1.0
 */
public abstract class TableColumn {
	
	
	/**
	 * 公共属性信息
	 */
	protected Map<String,Object> publicArtMap =new HashMap<String,Object>();
	/**
	 * 列属性信息
	 */
	protected Map<String,Map<String,Object>> colArtMap =new HashMap<String,Map<String,Object>>();
	/**
	 * 是否显示序号
	 */
	protected boolean isSerialNumber =true;
	
	/**
	 * 默认多选框
	 */
	protected CheckBoxEnum checkBoxEnum =CheckBoxEnum.CHECK;
	
	public TableColumn(){
		initDefaultPublic();
	}
	/**
	 * 初始化默认信息
	 */
	protected void initDefaultPublic(){
		publicArtMap.put(TableColumnUtil.ALIGN, AlignEnum.CENTER.getVal());
		publicArtMap.put(TableColumnUtil.VALIGN, ValignEnum.MIDDLE.getVal());
		publicArtMap.put(TableColumnUtil.SORTABLE, IsShowEnum.TRUE.getVal());
		publicArtMap.put(TableColumnUtil.VISIBLE, IsShowEnum.TRUE.getVal());
		//publicArtMap.put(TableColumnUtil.EDITABLE, IsShowEnum.TRUE.getVal());
	}
	/**
	 * 获取列的json信息
	 * @param showMap 展示字段数据
	 * @return 列json信息
	 */
	public abstract  String findColJosnStr(Map<String,String> showMap);
	
	/**
	 * 设置列展示名称
	 * @param column 列
	 * @param title 名称
	 * @return 列工具类
	 */
	public TableColumn setTitleByColumn(String column,String title) {
		putColMap(column,TableColumnUtil.TITLE,title);
		return this;
	}
	
	/**
	 * 设置列展示别名
	 * @param column 列
	 * @param sortname 别名
	 * @return 列工具类
	 */
	public TableColumn setSortnameByColumn(String column,String sortname) {
		putColMap(column,TableColumnUtil.SORTNAME,sortname);
		return this;
	}
	
	/**
	 * 设置列的可见性
	 * @param column 列
	 * @param IsShowEnum 是否可见枚举类
	 * @return 列工具类
	 */
	public TableColumn setVisibleByColumn(String column,IsShowEnum IsShowEnum) {
		putColMap(column,TableColumnUtil.VISIBLE,IsShowEnum.getVal());
		return this;
	}
	
	/**
	 * 设置列的排序
	 * @param AlignEnum 排序枚举  
	 * @return 列工具类
	 */
	public TableColumn setAlign(AlignEnum AlignEnum) {
		publicArtMap.put(TableColumnUtil.ALIGN, AlignEnum.getVal());
		return this;
	}
	
	/**
	 * 设置列头排序
	 * @param AlignEnum 排序枚举
	 * @return 列工具类
	 */
	public TableColumn setHalign(AlignEnum AlignEnum) {
		publicArtMap.put(TableColumnUtil.HALIGN, AlignEnum.getVal());
		return this;
	}
	
	/**
	 * 设置列为排序
	 * @param AlignEnum 排序枚举
	 * @return 列工具类
	 */
	public TableColumn setFalign(AlignEnum AlignEnum) {
		publicArtMap.put(TableColumnUtil.FALIGN, AlignEnum.getVal());
		return this;
	}
	
	/**
	 * 设置列垂直排序
	 * @param ValignEnum 垂直排序枚举
	 * @return 列工具类
	 */
	public TableColumn setValign(ValignEnum ValignEnum) {
		publicArtMap.put(TableColumnUtil.VALIGN, ValignEnum.getVal());
		return this;
	}
	
	/**
	 * 设置列的分类
	 * @param sortable 分类信息
	 * @return 列工具类
	 */
	public TableColumn setSortable(String sortable) {
		publicArtMap.put(TableColumnUtil.SORTABLE,sortable);
		return this;
	}
	
	/**
	 * 设置列 格式化方式
	 * @param column 列
	 * @param functionName 格式化方法名
	 * @return 列工具类
	 */
	public TableColumn setFormatterByColumn(String column,String functionName) {
		this.putColMap(column, TableColumnUtil.FORMATTER, functionName);
		return this;
	}
	
	/**
	 * 设置列 是否可编辑
	 * @param column 列
	 * @param IsShowEnum 是否枚举
	 * @return 列工具类
	 */
	public TableColumn setEditableByColumn(String column,IsShowEnum IsShowEnum) {
		if(IsShowEnum.getVal()){
			EditableEntity entity =new EditableEntity();
			this.putColMap(column, TableColumnUtil.EDITABLE, entity.getJson());
		}else{
			this.putColMap(column, TableColumnUtil.EDITABLE, IsShowEnum.getVal());
		}
		
		return this;
	}
	
	
	/**
	 * 设置列 是否可编辑
	 * @param column 列
	 * @param IsShowEnum 是否枚举
	 * @return 列工具类
	 */
	public TableColumn setEditableByColumn(String column,IsShowEnum IsShowEnum,EditableEntity entity) {
		if(IsShowEnum.getVal()){
			this.putColMap(column, TableColumnUtil.EDITABLE, entity.getJson());
		}else{
			this.putColMap(column, TableColumnUtil.EDITABLE, IsShowEnum.getVal());
		}
		
		return this;
	}
	
	/**
	 * 设置列 事件信息
	 * @param column 列
	 * @param functionName 事件方法名
	 * @return 列工具类
	 */
	public TableColumn setEventsByColumn(String column,String functionName) {
		this.putColMap(column, TableColumnUtil.EVENTS, functionName);
		return this;
	}

	/**
	 * 设置序是否显示
	 * @param isSerialNumber 是否显示序号
	 * @return 列工具类
	 */
	public TableColumn setSerialNumber(boolean isSerialNumber) {
		this.isSerialNumber = isSerialNumber;
		return this;
	}
	
	/**
	 * 设置多选框
	 * @param CheckBoxEnum 多选框枚举
	 * @return 列工具类
	 */
	public TableColumn setCheckBoxEnum(CheckBoxEnum CheckBoxEnum) {
		this.checkBoxEnum = CheckBoxEnum;
		return this;
	}
	/**
	 * 获取当前多选框枚举
	 * @return 多选框枚举
	 */
	public CheckBoxEnum getCheckBoxEnum(){
		return this.checkBoxEnum;
	}
	
	/**
	 * 设置列信息
	 * @param column 列
	 * @param type 类型
	 * @param val 值
	 */
	private void putColMap(String column,String type,Object val){
		column=column.toUpperCase();
		Map<String,Object> colTypeMap =null;
		if(colArtMap.containsKey(column)){
			colTypeMap=colArtMap.get(column);
		}else{
			colTypeMap =new HashMap<String,Object>();
			colArtMap.put(column, colTypeMap);
		}
		colTypeMap.put(type, val);
	}
	
	
	public void putPucAtr(String type,Object val){
		this.publicArtMap.put(type, val);
	}
}
