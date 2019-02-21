package com.yonyou.util.quartz.test;

import com.yonyou.util.quartz.QuartzManager;

public class Test {
	
	public static void main(String [] args){
		QuartzManager.addJob("测试1", "com.yonyou.util.quartz.job.TestJob", "123", "*/20 * * * * ?");
	}
}
