package com.yonyou.web;


import com.yonyou.business.button.ButtonAbs;
import com.yonyou.util.BussnissException;
import com.yonyou.util.jdbc.IBaseDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

import net.sf.json.JSONObject;

public class ButForDirectoryTest extends ButtonAbs {

    @Override
    protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request, HttpServletResponse response) throws IOException, BussnissException {
        String jsonobject = request.getParameter("jsonData");
        //String dataSourceCode = request.getParameter("dataSourceCode");
        JSONObject json = JSONObject.fromObject(jsonobject);
        String WORKING_NAME = json.getString("WORKING_NAME");
        String WORKING_PATH = json.getString("WORKING_PATH");
        File f = new File(WORKING_PATH);
        Boolean flag = false;
        if (!f.exists()) {
            f.mkdirs(); //创建目录
            flag = true;
        }
        String fileName = "vv.txt"; //文件名及类型
        File file = new File(WORKING_PATH, fileName);
        if (!file.exists()) { //surround with try/catch
            try {
                file.createNewFile();
                //删除文件
                // delFile(WORKING_PATH,fileName);
                //删除目录
                // if(flag == true){
                File dirFile = new File(WORKING_PATH);
                //  dirFile.delete();
                // }
                //目录地址入库
                json.put("ID", "");
                dcmsDAO.insertByTransfrom("BS_WORKING_DIRECTORY", json);
                System.out.println("=======================你很好=====================");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                String jsonMessage = "{\"message\":\"创建文件失败\"}";
                this.ajaxWrite(jsonMessage, request, response);
                e.printStackTrace();
                return null;
            }
        }
        String jsonMessage = "{\"message\":\"保存成功\"}";
        this.ajaxWrite(jsonMessage, request, response);
        return null;
    }

    public void delFile(String path, String filename) {
        File file = new File(path + "/" + filename);
        if (file.exists() && file.isFile())
            file.delete();
    }

    @Override
    protected void afterOnClick(IBaseDao dcmsDAO, HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    protected boolean befortOnClick(IBaseDao dcmsDAO, HttpServletRequest request, HttpServletResponse response) {
        return false;
    }

}
