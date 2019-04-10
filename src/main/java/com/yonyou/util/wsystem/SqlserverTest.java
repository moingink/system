package com.yonyou.util.wsystem;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;

import java.sql.*;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlserverTest {

    public static void getConnection(String DS_STRING, String DS_NAME, String DS_USERNAME, String DS_PASSWORD, String DS_DRIVER) {

        String url = "jdbc:sqlserver://" + DS_STRING + ";databaseName=" + DS_NAME + ";user=" + DS_USERNAME + ";password=" + DS_PASSWORD;//sa身份连接
        try {
            Class.forName(DS_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(url);
        try {
            Connection conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("数据库连接成功！！！");

    }
}
