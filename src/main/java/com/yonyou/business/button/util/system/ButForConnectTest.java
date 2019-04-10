package com.yonyou.business.button.util.system;

//import com.alibaba.fastjson.JSONObject;

import com.yonyou.business.button.ButtonAbs;
import com.yonyou.util.BussnissException;
import com.yonyou.util.jdbc.IBaseDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.net.ftp.FTPReply;


public class ButForConnectTest extends ButtonAbs {

    @Override
    protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request, HttpServletResponse response) {
        String dataSourceCode = request.getParameter("dataSourceCode");
        JSONObject json = (JSONObject) JSONArray.fromObject(request.getParameter("jsonData")).get(0);
        String FTP_PASSWORD = json.getString("FTP_PASSWORD");
        int FTP_PORT = Integer.parseInt(json.getString("FTP_PORT"));
        String FTP_USERNAME = json.getString("FTP_USERNAME");
        String FTP_PATH = json.getString("FTP_PATH");
        String FTP_ADDRESS = json.getString("FTP_ADDRESS");
        String FTP_NAME = json.getString("FTP_NAME");

        FTPClient ftp = new FTPClient();
        int reply = 0;
        try {
            ftp.connect(FTP_ADDRESS, FTP_PORT);
            ftp.login(FTP_USERNAME, FTP_PASSWORD);
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            String jsonMessage = "{\"message\":\"连接失败\"}";
            try {
                this.ajaxWrite(jsonMessage, request, response);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            return null;
        }
        String jsonMessage = "{\"message\":\"连接成功\"}";
        try {
            this.ajaxWrite(jsonMessage, request, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected boolean befortOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
                                    HttpServletResponse response) {
        // TODO 自动生成的方法存根
        return false;
    }

    @Override
    protected void afterOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
                                HttpServletResponse response) {
        // TODO Auto-generated method stub

    }

}
