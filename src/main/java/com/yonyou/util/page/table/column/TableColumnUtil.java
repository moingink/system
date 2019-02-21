package com.yonyou.util.page.table.column;

/**
 * 列工具类
 * @author moing_ink
 * <p>创建时间 ： 2016年12月27日
 * @version 1.0
 */
public class TableColumnUtil {
		//字段
		public static String FIELD="field";
		//列名
		public static String TITLE="title";
		//别名
		public static String SORTNAME="sortName";
		//是否可见
		public static String VISIBLE="visible";
		//排列方式 'left', 'right', 'center'
		public static String ALIGN="align";
		//header align 列头排序 
		public static String HALIGN="halign";
		//footer align 列尾排序
		public static String FALIGN="falign";
		//垂直对齐  'top', 'middle', 'bottom' 
		public static String VALIGN="valign";
		//排序
		public static String SORTABLE="sortable";
		//格式化程序
		public static String FORMATTER="formatter";
		//可编辑
		public static String EDITABLE="editable";
		//事件
		public static String EVENTS="events";
		
		public static String TRUE="true";
		
		public static String FALSE="false";
		
		public static String RMRN="RMRN";
		
		public static String CHECK_FIELD="check";
		
		/**
		 * 获取列加工类
		 * @return
		 */
		public static TableColumn findTableColumn(){
			return new TableColumnBulid();
		}
		
		
}
