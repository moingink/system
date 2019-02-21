package com.yonyou.util.page.table;


import java.util.HashMap;
import java.util.Map;

import com.yonyou.util.page.table.column.AlignEnum;
import com.yonyou.util.page.table.column.CheckBoxEnum;
import com.yonyou.util.page.table.column.IsShowEnum;
import com.yonyou.util.page.table.column.TableColumn;
import com.yonyou.util.page.table.column.TableColumnUtil;

/**
 * 列测试类
 * @author moing_ink
 * <p>创建时间 ： 2016年12月27日
 * @version 1.0
 */
public class Test {

	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		Map<String,String> showMap =new HashMap<String,String>();
		showMap.put("ID", "主键");
		showMap.put("NAME", "姓名");
		showMap.put("AGE", "年龄");
		
		TableColumn tableColumn =TableColumnUtil.findTableColumn();
		tableColumn.setCheckBoxEnum(CheckBoxEnum.CHECK)
				.setAlign(AlignEnum.RIGTH).setEditableByColumn("name", IsShowEnum.TRUE)
				.setVisibleByColumn("id", IsShowEnum.FALSE)
				.setTitleByColumn("name", "姓名1")
				.setFormatterByColumn("age", "aabbc_function");
		
		System.out.println(tableColumn.findColJosnStr(showMap));
	}

}
