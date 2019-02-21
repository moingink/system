package com.yonyou.util.quartz.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.quartz.utils.ConnectionProvider;

import com.yonyou.util.SpringContextUtil;
import com.yonyou.util.jdbc.IGlobalConstants;

public class RmConnectionProvider implements ConnectionProvider {
	public Connection getConnection() throws SQLException {
		//return default dataSource
		DataSource ds = (DataSource) SpringContextUtil.getBean("dataSource");
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			System.out.println("获取数据源地址出错");
		}
		return conn;
	}

	public void shutdown() throws SQLException {
		//do nothing
	}
	
	
}
