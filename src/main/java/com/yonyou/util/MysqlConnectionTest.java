package com.yonyou.util;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class MysqlConnectionTest {


    static private MysqlConnectionPoolDataSource ds;

    private MysqlConnectionTest(String servername, String databasename, String user, String password) {
        ds = new MysqlConnectionPoolDataSource();
        ds.setServerName(servername);
        ds.setDatabaseName(databasename);
        ds.setUser(user);
        ds.setPassword(password);

    }

    public static Connection getConnection(String servername, String databasename, String user, String password) throws Exception {
        if (ds == null) {
            new MysqlConnectionTest(servername, databasename, user, password);
        }
        Connection con = null;

        con = ds.getConnection();

        return con;
    }
//        public static void main(String []arg)
//        {
//
//            System.out.print("123");
//            Connection co=getConnection();
//            System.out.print(co);
//            System.out.print("123");
//        }
}



