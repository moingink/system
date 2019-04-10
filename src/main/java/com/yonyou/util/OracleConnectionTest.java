package com.yonyou.util;

import oracle.jdbc.pool.OracleDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class OracleConnectionTest {
    static private OracleDataSource ods;

    private OracleConnectionTest(String servername, String user, String password) {
        if (ods == null) {
            try {
                ods = new OracleDataSource();
                ods.setURL(servername);
                ods.setUser(user);
                ods.setPassword(password);
            } catch (SQLException ex) {
            }
        }
    }

    public static Connection getConnection(String servername, String user, String password) {
        if (ods == null) {
            new OracleConnectionTest(servername, user, password);
        }
        Connection con = null;
        try {
            con = ods.getConnection();
        } catch (SQLException ex) {
        }
        return con;
    }

}
