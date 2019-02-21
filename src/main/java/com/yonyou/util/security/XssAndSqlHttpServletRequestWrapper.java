package com.yonyou.util.security;
/** 
 * @author zzh
 * @version 创建时间：2018年10月22日
 * 类说明 
 */


import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import com.yonyou.business.entity.TokenEntity;
import com.yonyou.util.log.BusLogger;
  

public class XssAndSqlHttpServletRequestWrapper extends HttpServletRequestWrapper {  
      
    HttpServletRequest orgRequest = null;  
      
    public XssAndSqlHttpServletRequestWrapper(HttpServletRequest request) {  
        super(request);  
        orgRequest = request;  
    }  
  

	/** 
     * 覆盖getParameter方法，将参数名和参数值都做xss & sql过滤。<br/> 
     * 如果需要获得原始的值，则通过super.getParameterValues(name)来获取<br/> 
     * getParameterNames,getParameterValues和getParameterMap也可能需要覆盖 
     */  
    @Override  
    public String getParameter(String name) {
    	if(null==name){    		
    		return "";
    	}
    	String nameTemp = xssEncodeName(name);
    	if(!name.equals(nameTemp)){    		
    		return "";
    	}

        String value = super.getParameter(name);  
        if (value != null) {  
            value = xssEncode(value);  
        }  
        return value;  
    }  
  
    @Override  
    public Map<String, String[]> getParameterMap() {  
    	
    	Map<String, String[]> map = super.getParameterMap();
    	Map<String, String[]> returnMap =new ConcurrentHashMap<String, String[]>();
    	String keyTemp=null;
    	String valueTemp=null;
    	String[] tempArray=null;
    	if(null==map){
    		return null;
    	}
    	
    	for(String temp:map.keySet()){
    		keyTemp=xssEncodeName(temp);
    	    if(temp.equals(keyTemp)){
    	    	tempArray = new String[map.get(temp).length];
    	    	for(int i=0;i<map.get(temp).length;i++){
    	    		tempArray[i]=xssEncode(map.get(temp)[i]);    	    		
    	    	}     	    	
    	    	returnMap.put(temp, tempArray);
    	    	
    	    }else{
    	    	 System.out.println("XssAndSqlHttpServletRequestWrapper参数非法：getParameterMap" + temp + "=" + map.get(temp).toString()); 
    	    }    		
    		
    	}
    	
    	return returnMap;
    	
    }  
    
    @Override  
    public Enumeration<String> getParameterNames() {  
    	
    	Enumeration paramNames = super.getParameterNames(); 
    	Vector<String> vector = new Vector<String>();
    	String paramName=null;
    	String tempParamName = null;
    	if(null==paramNames){
    		return null;
    	}
    	
		while (paramNames.hasMoreElements()) { 
			paramName = (String) paramNames.nextElement(); 
			tempParamName=xssEncodeName(paramName);
			if(paramName.equals(tempParamName)){
				
				vector.add(paramName);
			}else{
				System.out.println("XssAndSqlHttpServletRequestWrapper参数非法：getParameterNames" + paramName + "=" +tempParamName); 
			}
		} 
		
		
    	return vector.elements();
    	
    }  
    
    @Override  
    public String[] getParameterValues(String name) { 
    	if(!name.equals(xssEncodeName(name))){
    		
    		return new String[0];
    	}
    	
    	String[] parameValues= super.getParameterValues(name);
    	int count =0;
    	if(null==parameValues){
    		return null;
    	}
    		
    	for(int i=0;i<parameValues.length;i++){
    		System.out.println("参数XssAndSqlHttpServletRequestWrapper：getParameterValues:pro" + name+ ":"+parameValues[i]); 
    		if(name.startsWith("SEARCH-")){
    			//System.out.println("参数XssAndSqlHttpServletRequestWrapper：getParameterValues:SEARCH-guolv:" + name+  ":"+guolv(parameValues[i]));   
    			//System.out.println("参数XssAndSqlHttpServletRequestWrapper：getParameterValues:SEARCH-guolv2:" + name+  ":"+guolv2(parameValues[i]));   
    			count = findSingleQuotesCount(parameValues[i]);
    			if(count%2==1){
    				parameValues[i]="";
    			}else{
    				parameValues[i]=parameValues[i].replaceAll("\'", "");
    			}
    
    			System.out.println("参数XssAndSqlHttpServletRequestWrapper：getParameterValues:SEARCH-:" + name+ ":"+parameValues[i]);  
    		} 
    		
    		parameValues[i]=xssEncode(parameValues[i]);
    		
    	}
    	
    	return parameValues;
    	
    }  
    
    @Override  
    public Object getAttribute(String name) {  
    	
    	 String tempName =xssEncodeName(name);
    	if(!name.equals(tempName)){
    		System.out.println("XssAndSqlHttpServletRequestWrapper参数非法：getAttribute" + name + "=" +tempName); 
    		return null;
    	}
    	
    	return super.getAttribute(name);
    	
    }  
    
    
    @Override  
    public Enumeration<String> getAttributeNames() {   
    	
    	Vector<String> vector = new Vector<String>();
    	Enumeration paramNames = super.getAttributeNames(); 
    	String paramName = null;
    	String tempParamName=null;
    	if(null==paramNames){
    		return null;
    	}
		while (paramNames.hasMoreElements()) { 
			paramName = (String) paramNames.nextElement(); 
			tempParamName=xssEncodeName(paramName);
			if(paramName.equals(tempParamName)){
				
				vector.add(paramName);
			}else{
				System.out.println("XssAndSqlHttpServletRequestWrapper参数非法：getAttributeNames" + paramName + "=" +tempParamName); 
			}
			
		} 
		
    	return vector.elements();
    	
    }  
    
    /** 
     * 覆盖getHeader方法，将参数名和参数值都做xss & sql过滤。<br/> 
     * 如果需要获得原始的值，则通过super.getHeaders(name)来获取<br/> 
     * getHeaderNames 也可能需要覆盖 
     */  
    @Override  
    public String getHeader(String name) {  
  
        String value = super.getHeader(xssEncode(name));  
        if (value != null) {  
            value = xssEncode(value);  
        }  
        return value;  
    }  
  
    /** 
     * 将容易引起xss & sql漏洞的半角字符直接替换成全角字符 
     *  
     * @param s 
     * @return 
     */  
    private String xssEncode(String s) {  

    	
    	String temp ="";
    	
        if (s == null || s.isEmpty()) {  
            return s;  
        }else{  
        	temp = stripXSSAndSql(s);
        	if(s.toLowerCase().contains("__turn__")){
        		
        		temp=s.replace("__turn__", "");
        	}
        	//temp=stripNonCharCodepoints(temp);
        }  
   
        //有值替换后记录日志
        xss2Log(s,temp);
        
        if(null==temp){
        	return "";
        }
        return temp.toString();  
    }  
  
    public void xss2Log(String source,String target){
    	
    	if(!source.equals(target)){
        	
        	System.out.println("XssAndSqlHttpServletRequestWrapper参数非法：xssEncode" + source + "!=" +target+"，URI:"+this.getRequestURI()); 
        	try{
        		TokenEntity tokenEntity = new TokenEntity(findTokenByCookie(this));
            	HashMap<String, String> map = new HashMap<>();	
         		map.put(BusLogger.LOG_ACTION_TYPE, "login_log");
         		map.put(BusLogger.LOG_USER_ID, tokenEntity.USER.getId());
         		map.put(BusLogger.LOG_USER_ID_NAME, tokenEntity.USER.getName());
         		map.put(BusLogger.LOG_OWNER_ORG_ID, tokenEntity.COMPANY.getCompany_id());
         		
         		map.put(BusLogger.LOG_CONTENT,"XssAndSqlHttpServletRequestWrapper参数非法：xssEncode" + source + "!=" +target+"，URI:"+this.getRequestURI()+"。"+ "登录人:"+tokenEntity.USER.getLoginId()+";角色:"+tokenEntity.ROLE.getRoleId()+";公司:"+tokenEntity.COMPANY.getCompany_name()+";token:"+tokenEntity.getToken()
         				);
         		map.put(BusLogger.LOG_OPERATION_INFO, "login");		
         		map.put(BusLogger.LOG_ACTION_MODULE, "login");	
                BusLogger.record_log(map);
        	}catch(Exception ex){        		
        		try{
        			HashMap<String, String> map = new HashMap<>();	
             		map.put(BusLogger.LOG_ACTION_TYPE, "login_log");         		
             		map.put(BusLogger.LOG_CONTENT,"XssAndSqlHttpServletRequestWrapper参数非法Exception：xssEncode" + source + "!=" +target+"，URI:"+this.getRequestURI()+"。"+ex.getMessage());
             		map.put(BusLogger.LOG_OPERATION_INFO, "login");		
             		map.put(BusLogger.LOG_ACTION_MODULE, "login");	
                    BusLogger.record_log(map);
        		}catch(Exception e){
        			e.printStackTrace();
        		}           	
                
        		ex.printStackTrace();
        	}
        	
        }
    }
    private String xssEncodeName(String s) {  

    	String temp =null;
    	
        if (s == null || s.isEmpty()) {  
            return s;  
        }else{  
        	temp = stripXSSAndSql(s); 
        	if(s.toLowerCase().contains("__turn__")){
        		
        		temp=s.replace("__turn__", "");
        	}
        	//temp=stripNonCharCodepoints(temp);
        }  
        if(s.contains("and")){
        	System.out.println("过滤器过滤内容 and:"+s);
        	
        }
        //有值替换后记录日志
        xss2Log(s,temp);
        
        if(null==temp){
        	return "";
        }
        StringBuilder sb = new StringBuilder(temp.length() + 16);  
        for (int i = 0; i < s.length(); i++) {  
            char c = s.charAt(i);  
            switch (c) {  
            case '>':  
                sb.append("");// 转义大于号  
                break;  
            case '<':  
                sb.append("");// 转义小于号  
                break;  
            case '\'':  
                sb.append("");// 转义单引号  
                break;  
            case '\"':  
                sb.append("");// 转义双引号   
                break;  
            case '&':  
                sb.append("");// 转义&  
                break;  
            case '#':  
                sb.append("");// 转义#  
                break;  
            default:  
                sb.append(c);  
                break;  
            }  
        }  
        return sb.toString();  
    }  
    
    /** 
     * 获取最原始的request 
     *  
     * @return 
     */  
    public HttpServletRequest getOrgRequest() {  
        return orgRequest;  
    }  
  
    /** 
     * 获取最原始的request的静态方法 
     *  
     * @return 
     */  
    public static HttpServletRequest getOrgRequest(HttpServletRequest req) {  
        if (req instanceof XssAndSqlHttpServletRequestWrapper) {  
            return ((XssAndSqlHttpServletRequestWrapper) req).getOrgRequest();  
        }  
  
        return req;  
    }  
  
    /** 
     *  
     * 防止xss跨脚本攻击（替换，根据实际情况调整） 
     */  
  
    public static String stripXSSAndSql(String value) {  
        if (value != null) {  
            // NOTE: It's highly recommended to use the ESAPI library and  
            // uncomment the following line to  
            // avoid encoded attacks.  
            // value = ESAPI.encoder().canonicalize(value);  
            // Avoid null characters  
/**         value = value.replaceAll("", "");***/  
            // Avoid anything between script tags  
            Pattern scriptPattern = Pattern.compile("<[\r\n| | ]*script[\r\n| | ]*>(.*?)</[\r\n| | ]*script[\r\n| | ]*>", Pattern.CASE_INSENSITIVE);   
            value = scriptPattern.matcher(value).replaceAll("");  
            // Avoid anything in a src="http://www.yihaomen.com/article/java/..." type of e-xpression  
            scriptPattern = Pattern.compile("src[\r\n| | ]*=[\r\n| | ]*[\\\"|\\\'](.*?)[\\\"|\\\']", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
            value = scriptPattern.matcher(value).replaceAll("");  
            // Remove any lonesome </script> tag  
            scriptPattern = Pattern.compile("</[\r\n| | ]*script[\r\n| | ]*>", Pattern.CASE_INSENSITIVE);  
            value = scriptPattern.matcher(value).replaceAll("");  
            // Remove any lonesome <script ...> tag  
            scriptPattern = Pattern.compile("<[\r\n| | ]*script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
            value = scriptPattern.matcher(value).replaceAll("");  
            // Avoid eval(...) expressions  
            scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
            value = scriptPattern.matcher(value).replaceAll("");  
            // Avoid e-xpression(...) expressions  
            scriptPattern = Pattern.compile("e-xpression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
            value = scriptPattern.matcher(value).replaceAll("");  
            // Avoid javascript:... expressions  
            scriptPattern = Pattern.compile("javascript[\r\n| | ]*:[\r\n| | ]*", Pattern.CASE_INSENSITIVE);  
            value = scriptPattern.matcher(value).replaceAll("");  
            // Avoid vbscript:... expressions  
            scriptPattern = Pattern.compile("vbscript[\r\n| | ]*:[\r\n| | ]*", Pattern.CASE_INSENSITIVE);  
            value = scriptPattern.matcher(value).replaceAll("");  
            // Avoid onload= expressions  
            scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
            value = scriptPattern.matcher(value).replaceAll("");  
            
            scriptPattern = Pattern.compile("alert[\r\n| | ]*\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
            value = scriptPattern.matcher(value).replaceAll("");  
            
            scriptPattern = Pattern.compile("confirm[\r\n| | ]*\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
            value = scriptPattern.matcher(value).replaceAll("");
            
            scriptPattern = Pattern.compile("prompt[\r\n| | ]*\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
            value = scriptPattern.matcher(value).replaceAll("");
            
            scriptPattern = Pattern.compile("\\s+function[\r\n| | ]*\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
            value = scriptPattern.matcher(value).replaceAll("");
            
            scriptPattern = Pattern.compile("\\s*select\\s+", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
            value = scriptPattern.matcher(value).replaceAll("");
            
            scriptPattern = Pattern.compile("\\s*update\\s+", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
            value = scriptPattern.matcher(value).replaceAll("");
            
            scriptPattern = Pattern.compile("\\s*delete\\s+", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
            value = scriptPattern.matcher(value).replaceAll("");
            
            scriptPattern = Pattern.compile("\\s*insert\\s+", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
            value = scriptPattern.matcher(value).replaceAll("");
            
            scriptPattern = Pattern.compile("\\s*trancate\\s+", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
            value = scriptPattern.matcher(value).replaceAll("");
            
            scriptPattern = Pattern.compile("\\s*exec\\s+", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
            value = scriptPattern.matcher(value).replaceAll("");
            
            scriptPattern = Pattern.compile("\\s*drop\\s+", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
            value = scriptPattern.matcher(value).replaceAll("");
            
            scriptPattern = Pattern.compile("\\s*execute\\s+", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
            value = scriptPattern.matcher(value).replaceAll("");
            
            scriptPattern = Pattern.compile("\\s*declare\\s+", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
            value = scriptPattern.matcher(value).replaceAll("");
            
            scriptPattern = Pattern.compile("\\s+union\\s+", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
            value = scriptPattern.matcher(value).replaceAll("");
            
            scriptPattern = Pattern.compile("\\s+and\\s+", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
            value = scriptPattern.matcher(value).replaceAll("");
            
            scriptPattern = Pattern.compile("\\s+or\\s+", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
            value = scriptPattern.matcher(value).replaceAll("");
            
            scriptPattern = Pattern.compile("\\s+1=1\\s+", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
            value = scriptPattern.matcher(value).replaceAll("");
            
            scriptPattern = Pattern.compile("\\s+system_user()\\s+", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
            value = scriptPattern.matcher(value).replaceAll("");
            
            scriptPattern = Pattern.compile("\\s+user()\\s+", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
            value = scriptPattern.matcher(value).replaceAll("");
            
            scriptPattern = Pattern.compile("\\s+current_user()\\s+", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
            value = scriptPattern.matcher(value).replaceAll("");
            
            scriptPattern = Pattern.compile("\\s+session_user()\\s+", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
            value = scriptPattern.matcher(value).replaceAll("");
            
            
            scriptPattern = Pattern.compile("\\s+database()\\s+", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
            value = scriptPattern.matcher(value).replaceAll("");
            scriptPattern = Pattern.compile("\\s+version()\\s+", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
            value = scriptPattern.matcher(value).replaceAll("");
            
            scriptPattern = Pattern.compile("\\s+load_file()\\s+", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
            value = scriptPattern.matcher(value).replaceAll("");
            
            
            
            scriptPattern = Pattern.compile("\\s+substr[\r\n| | ]*\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
            value = scriptPattern.matcher(value).replaceAll("");
            
//            String reg = "(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|"
//                    + "(\\b(|||||char|into||ascii|||count|master|into||)\\b)";
//            
//            scriptPattern = Pattern.compile(reg, 2);
//            value = scriptPattern.matcher(value).replaceAll("");
            
            String reg = "(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|"
                    + "(\\b(union|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";
            
            scriptPattern = Pattern.compile(reg, 2);
            value = scriptPattern.matcher(value).replaceAll("");
            
            
            scriptPattern = Pattern.compile("<script>(.*?)</script>", 2);
            value = scriptPattern.matcher(value).replaceAll("");

            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\'(.*?)\\'", 42);
            value = scriptPattern.matcher(value).replaceAll("");
            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", 42);
            value = scriptPattern.matcher(value).replaceAll("");

            scriptPattern = Pattern.compile("</script>", 2);
            value = scriptPattern.matcher(value).replaceAll("");

            scriptPattern = Pattern.compile("<script(.*?)>", 42);
            value = scriptPattern.matcher(value).replaceAll("");

            scriptPattern = Pattern.compile("eval\\((.*?)\\)", 42);
            value = scriptPattern.matcher(value).replaceAll("");

            scriptPattern = Pattern.compile("expression\\((.*?)\\)", 42);
            value = scriptPattern.matcher(value).replaceAll("");

            scriptPattern = Pattern.compile("<iframe>(.*?)</iframe>", 2);
            value = scriptPattern.matcher(value).replaceAll("");

            scriptPattern = Pattern.compile("</iframe>", 2);
            value = scriptPattern.matcher(value).replaceAll("");

            scriptPattern = Pattern.compile("<iframe(.*?)>", 42);
            value = scriptPattern.matcher(value).replaceAll("");
            
           value = value.replaceAll("1=1", "");
           value = value.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "*");
//            value = value.replaceAll("script", "");
//            value = value.replaceAll("javascript", "");
          
            
        }  
        
        return value;  
    }  
    
     public static  String dealString(String value) {
    	if (value != null) {
    	// 采用spring的StringEscapeUtils工具类 实现
    	StringEscapeUtils.escapeHtml(value);
    	StringEscapeUtils.escapeJavaScript(value);
    	StringEscapeUtils.escapeSql(value);
    	}
    	return value;
    	}
     
     /**
	  * 
	  * @param args
	  * SQL注入过滤
	  */
	 
	 public static String stripSql(String value) {   
	        if (value != null) {  
	            // NOTE: It's highly recommended to use the ESAPI library and  
	            // uncomment the following line to  
	            // avoid encoded attacks.  
	            // value = ESAPI.encoder().canonicalize(value);  
	            // Avoid null characters  
	/**         value = value.replaceAll("", "");***/  
	            // Avoid anything between script tags  
	            Pattern scriptPattern =  Pattern.compile("\\s*select\\s+", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
	            value = scriptPattern.matcher(value).replaceAll("");
	            
	            scriptPattern = Pattern.compile("\\s*update\\s+", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
	            value = scriptPattern.matcher(value).replaceAll("");
	            
	            scriptPattern = Pattern.compile("\\s*delete\\s+", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
	            value = scriptPattern.matcher(value).replaceAll("");
	            
	            scriptPattern = Pattern.compile("\\s*insert\\s+", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
	            value = scriptPattern.matcher(value).replaceAll("");
	            
	            scriptPattern = Pattern.compile("\\s*trancate\\s+", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
	            value = scriptPattern.matcher(value).replaceAll("");
	            
	            scriptPattern = Pattern.compile("\\s*exec\\s+", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
	            value = scriptPattern.matcher(value).replaceAll("");
	            
	            scriptPattern = Pattern.compile("\\s*drop\\s+", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
	            value = scriptPattern.matcher(value).replaceAll("");
	            
	            scriptPattern = Pattern.compile("\\s*execute\\s+", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
	            value = scriptPattern.matcher(value).replaceAll("");
	            
	            scriptPattern = Pattern.compile("\\s*declare\\s+", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
	            value = scriptPattern.matcher(value).replaceAll("");
	            
	            scriptPattern = Pattern.compile("\\s+union\\s+", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
	            value = scriptPattern.matcher(value).replaceAll("");
	            
	            scriptPattern = Pattern.compile("\\s+and\\s+", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
	            value = scriptPattern.matcher(value).replaceAll("");
	            
	            scriptPattern = Pattern.compile("\\s+or\\s+", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
	            value = scriptPattern.matcher(value).replaceAll("");
	            
	            scriptPattern = Pattern.compile(" 1=1 ", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
	            value = scriptPattern.matcher(value).replaceAll("");
	            
	            scriptPattern = Pattern.compile("\\s+system_user()*\\s+", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
	            value = scriptPattern.matcher(value).replaceAll("");
	            
	            scriptPattern = Pattern.compile("\\s+substr[\r\n| | ]*\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
	            value = scriptPattern.matcher(value).replaceAll("");
	            
	          
	            
	        }  
	        
	        return value;  
	    }  
	 
	 public static String transEncoding(String str){

		String strTemp;
		try {
			strTemp = new String(str.getBytes("UTF-8"), "GBK");
			strTemp= strTemp.replace("?", "");
			
			strTemp= new String(str.getBytes("GBK"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO 自动生成的 catch 块 
			strTemp=str;
		}
		
		return strTemp;
	 }
	 
	 public static String getEncoding(String str) {    
         String encode = "GB2312";    
        try {    
            if (str.equals(new String(str.getBytes(encode), encode) )) {    
                 String s = encode;    
                return s;    
             }    
         } catch (Exception exception) {    
         }    
         encode = "ISO-8859-1";    
        try {    
            if (str.equals(new String(str.getBytes(encode), encode))) {    
                 String s1 = encode;    
                return s1;    
             }    
         } catch (Exception exception1) {    
         }    
         encode = "UTF-8";    
        try {    
            if (str.equals(new String(str.getBytes(encode), encode))) {    
                 String s2 = encode;    
                return s2;    
             }    
         } catch (Exception exception2) {    
         }    
         encode = "GBK";    
        try {    
            if (str.equals(new String(str.getBytes(encode), encode))) {    
                 String s3 = encode;    
                return s3;    
             }    
         } catch (Exception exception3) {    
         }    
        return "";    
     } 
	 
	 public static String guolv(String a) {  
	        a = a.replaceAll("%22", "");  
	        a = a.replaceAll("%27", "");  
	        a = a.replaceAll("%3E", "");  
	        a = a.replaceAll("%3e", "");  
	        a = a.replaceAll("%3C", "");  
	        a = a.replaceAll("%3c", "");  
	        a = a.replaceAll("<", "");  
	        a = a.replaceAll(">", "");  
	        a = a.replaceAll("\"", "");  
	        a = a.replaceAll("'", "");  
	        a = a.replaceAll("\\+", "");  
	        a = a.replaceAll("\\(", "");  
	        a = a.replaceAll("\\)", "");  
	        a = a.replaceAll(" and ", "");  
	        a = a.replaceAll(" or ", "");  
	        a = a.replaceAll(" 1=1 ", "");  
	        return a;  
	    }
	 
	
	 
	 public String guolv2(String a) {  
	        if (StringUtils.isNotEmpty(a)) {  
	            if (a.contains("%22") || a.contains("%3E") || a.contains("%3e")  
	                    || a.contains("%3C") || a.contains("%3c")  
	                    || a.contains("<") || a.contains(">") || a.contains("\"")  
	                    || a.contains("'") || a.contains("+") || /* 
	                                                             * a.contains("%27") 
	                                                             * || 
	                                                             */  
	                    a.contains(" and ") || a.contains(" or ")  
	                    || a.contains("1=1") || a.contains("(") || a.contains(")")) {  
	                return "bingo";  
	            }  
	        }  
	        return a;  
	    }  
	 
	 public static int findSingleQuotesCount(String str) {  
		  if(null==str){			  
			  return 0;
		  }
		  str=str.trim();
		  int count=0;
		  char singleQuotes='\'';
		  for(int i=0;i<str.toCharArray().length;i++){
			  if(singleQuotes==str.toCharArray()[i]){
				  count++;
			  }			  
		  }	      
	      return count;  
	    }  
	 
	 /**
		 * 根据cookie 获取 token信息
		 * 
		 * @param request
		 * @return
		 */
		private String findTokenByCookie(HttpServletRequest request) {
			String token = "";
			Cookie[] cookies = request.getCookies();
			if(cookies != null && cookies.length > 0){
				for (Cookie cookie : cookies) {
					if ("token".equals(cookie.getName())) {
						token = cookie.getValue();
					}
				}
				return token;
			}
			return token;
		}
		

//	 public static String filterOffUtf8Mb4(String text) throws UnsupportedEncodingException {
//
//			 System.out.println("filterOffUtf8Mb4:"+text);
//	
//			byte[] bytes = text.getBytes("UTF-8");
//			ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
//			int i = 0;
//			while (i < bytes.length) {
//				short b = bytes[i];
//				if (b > 0) {
//					buffer.put(bytes[i++]);
//					continue;
//				}
//				b += 256;
//				if ((b ^ 0xC0) >> 4 == 0) {
//					buffer.put(bytes, i, 2);
//					i += 2;
//				}
//				else if ((b ^ 0xE0) >> 4 == 0) {
//					buffer.put(bytes, i, 3);
//					i += 3;
//				}
//				else if ((b ^ 0xF0) >> 4 == 0) {
//					i += 4;
//				}
//			}
//			buffer.flip();
//			return new String(buffer.array(), "utf-8");
//	}

	 public static String stripNonCharCodepoints(String input) {
		 
		 System.out.println("stripNonCharCodepoints:begin:"+input);
		 
		  StringBuilder retval = new StringBuilder();
		  char ch;
		 
		  if(input.contains("aaa")){
			  
			  System.out.println("stripNonCharCodepoints:begin:"+input);
			  for (int i = 0; i < input.length(); i++) {
				    ch = input.charAt(i);
				    Character temp = Character.valueOf(ch);
				    System.out.println("stripNonCharCodepoints:ch:"+ch);
				    System.out.println("stripNonCharCodepoints:Character,MAX:"+temp.MAX_VALUE);
				    System.out.println("stripNonCharCodepoints:Character,MIN:"+temp.MIN_VALUE); 
				    System.out.println("stripNonCharCodepoints:Character,isDefined:"+temp.isDefined(ch));
				    System.out.println("stripNonCharCodepoints:Character,isIdentifierIgnorable:"+Character.isJavaIdentifierPart(ch));
				    // Strip all non-characters http://unicode.org/cldr/utility/list-unicodeset.jsp?a=[:Noncharacter_Code_Point=True:]
				    // and non-printable control characters except tabulator, new line and carriage return
				    if (ch % 0x10000 != 0xffff && // 0xffff - 0x10ffff range step 0x10000
				        ch % 0x10000 != 0xfffe && // 0xfffe - 0x10fffe range
				        (ch <= 0xfdd0 || ch >= 0xfdef) && // 0xfdd0 - 0xfdef
				        (ch > 0x1F || ch == 0x9 || ch == 0xa || ch == 0xd)) {
				 
				    	if(Character.MAX_VALUE<ch || Character.MIN_VALUE>ch){
				    				    		
				    	} else{
				    		  retval.append(ch);
				    	}
				    
				    }
				  }
				  System.out.println("stripNonCharCodepoints:end:"+retval.toString());
				  return retval.toString();
		  }else{
			  
			  return input;
		  }
		 
		}

     public static void main(String[] args) {
    	
       		System.out.println("");

   }
 
}