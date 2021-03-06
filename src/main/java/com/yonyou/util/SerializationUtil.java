package com.yonyou.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/** 
 * @author zzh
 * @version 创建时间：2018年7月11日
 * 类说明 
 */
public class SerializationUtil {

	//序列化 
    public static byte[] serialize(Object object) {  
       ObjectOutputStream oos = null;  
       ByteArrayOutputStream baos = null;  
       try {  
           baos = new ByteArrayOutputStream();  
           oos = new ObjectOutputStream(baos);  
           oos.writeObject(object);  
           byte[] bytes = baos.toByteArray();  
           return bytes;  
       } catch (Exception e) {  
       }  
       return null;  
    }  

    //反序列化 
    public static Object deserialize(byte[] bytes) {  
       ByteArrayInputStream bais = null;  
       try {  
           bais = new ByteArrayInputStream(bytes);  
           ObjectInputStream ois = new ObjectInputStream(bais);  
           return ois.readObject();  
       } catch (Exception e) {  
       }  
       return null;  
    } 
    
}
