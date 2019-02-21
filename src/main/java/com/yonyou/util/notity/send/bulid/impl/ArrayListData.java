package com.yonyou.util.notity.send.bulid.impl;



import java.util.ArrayList;

import com.yonyou.util.notity.send.bulid.DataApi;

public class ArrayListData<DATA> extends ArrayList<DATA> implements DataApi<DATA> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3553444139798960373L;

	@Override
	public void addData(DATA data) {
		// TODO 自动生成的方法存根
		this.add(data);
	}

}
