package com.yonyou.web.entity;

import com.yonyou.util.MysqlConnectionTest;
import com.yonyou.util.OracleConnectionTest;
import com.yonyou.util.wsystem.SqlserverTest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/basic")
public class BasicSettingControl {

    //获取业务类型产品
    @RequestMapping(value = "ConnectTest", method = RequestMethod.GET)
    public String getProductDept(HttpServletRequest request, HttpServletResponse response) {
        String SELECTED = request.getParameter("resource");
        String DS_CODE = request.getParameter("resource");
        String DS_DATABASETYPE = request.getParameter("resource");
        String DS_DRIVER = request.getParameter("resource");
        String DS_NAME = request.getParameter("resource");
        String DS_PASSWORD = request.getParameter("resource");
        String DS_STRING = request.getParameter("resource");
        String DS_USERNAME = request.getParameter("resource");
        //驱动加载判断

        try {
            if ("0".equals(DS_DATABASETYPE)) {
                MysqlConnectionTest.getConnection(DS_STRING, DS_NAME, DS_USERNAME, DS_PASSWORD);
            } else if ("1".equals(DS_DATABASETYPE)) {
                OracleConnectionTest.getConnection(DS_STRING, DS_USERNAME, DS_PASSWORD);
            } else {
                SqlserverTest.getConnection(DS_STRING, DS_NAME, DS_USERNAME, DS_PASSWORD, DS_DRIVER);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "连接失败";
        }

        return "连接成功";
    }

}
