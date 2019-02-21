package com.yonyou.iuap.system.web.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yonyou.business.entity.TokenEntity;
import com.yonyou.business.entity.TokenUtil;
import com.yonyou.iuap.cache.CacheManager;
import com.yonyou.iuap.utils.PropertyUtil;
import com.yonyou.util.PropertyFileUtil;
import com.yonyou.util.SpringContextUtil;
import com.yonyou.util.log.BusLogger;

public class TimeOutFilter implements Filter {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	//redis配置
	public static String REDIS_KEY_ACCESSTIME = "ACCESSTIME";
	private CacheManager cacheManager = (CacheManager) SpringContextUtil.getBean("cacheManager");
	//超时时间
	private int limitTime = Integer.parseInt(PropertyUtil.getPropertyByKey("TIMEOUT_LIMIT")) * 60 * 1000;
	//排除URI
	private Set<String> ignoreSet = new HashSet<String>();
	
	@Override
	public void destroy() {
		
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
        
		String cp = filterConfig.getServletContext().getContextPath();
		String ignoresParam = filterConfig.getInitParameter("ignores");  
        String[] ignoreArray = ignoresParam.split(",");  
        for (String s : ignoreArray) {
        	System.out.println("$$$$$"+cp+s);
            ignoreSet.add(cp + s);
        }  
		
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String pageCode = request.getParameter("pageCode");   
        String module= request.getParameter("dataSourceCode");   
      
        
		if(module!=null || pageCode!=null){
			StringBuilder strBuilder = new StringBuilder();
			strBuilder.append("URL:"+httpRequest.getRequestURL());
			
			Map map = new HashMap(); 
			Enumeration paramNames = request.getParameterNames(); 
			while (paramNames.hasMoreElements()) { 
				String paramName = (String) paramNames.nextElement(); 
				
				String[] paramValues = request.getParameterValues(paramName); 
				if (paramValues.length == 1) { 
					String paramValue = paramValues[0]; 
					if (paramValue.length() != 0) { 
						System.out.println("参数：" + paramName + "=" + paramValue); 
						map.put(paramName, paramValue); 
						strBuilder.append(paramName+"="+paramValue+";");
					}
				} 		
			} 
			
			  String moduleStr = pageCode+module;
			BusLogger.record_log(moduleStr, strBuilder.toString(), httpRequest.getParameter("cmd"), httpRequest, httpResponse);
		}
		
		//位于忽略列表中或无token的不做处理——无token情况：1.未登录； 2.请求url非公共，漏了token（cookie中token只有在门户里能用-待解决）
		String token = TokenUtil.getToken(httpRequest);
		//超时时间设为0表示不启用超时机制
		if(limitTime!=0 && !isIgnore(httpRequest) && !"".equals(token)){
			if (cacheManager.hexists(REDIS_KEY_ACCESSTIME, token)) {
				long accessTime = System.currentTimeMillis();
				long lastTime = cacheManager.hget(REDIS_KEY_ACCESSTIME, token);
				if (accessTime - lastTime > limitTime) {
					timeOutHandler(httpRequest, httpResponse, token);
					// 超时终止请求
					return;
				}else{
					// 不超时则刷新访问时间
					cacheManager.hset(REDIS_KEY_ACCESSTIME, token, accessTime);
				}
			}else{
				//redis无登录时间则表明已被清除-终止请求[情景eg：前台一个操作往后台发送了多个请求]
				return ;
			}
		}
		filterChain.doFilter(request, response);
	}
	
	/**
	 * 请求是否存在于过滤忽略列表中
	* @param httpRequest
	* @return
	 */
	private boolean isIgnore(HttpServletRequest httpRequest){
		String uri = httpRequest.getRequestURI();
		for(String ignore : ignoreSet){
			if(uri.startsWith(ignore)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 超时处理
	* @param httpRequest
	* @param httpResponse
	* @param token
	 */
	private void timeOutHandler(HttpServletRequest httpRequest, HttpServletResponse httpResponse, String token){
		
		// 登录超时，移除redis的相关token记录及登陆时间记录
		TokenEntity tokenEntity = cacheManager.get("SESSION_"+token);
		logger.error("***登录超时*** 用户:" + tokenEntity.USER.getLoginId());
		cacheManager.removeCache("SESSION_" + token);
		cacheManager.removeCache("MENU_" + token);
		cacheManager.hdel(REDIS_KEY_ACCESSTIME, token);
		
		//跳转至超时页面
		String portalUrl = PropertyFileUtil.getValue("NGINX_PORTAL_URL");
		String timeoutUrl = portalUrl+"/pages/timeout.html";
		//ajax请求与普通请求需要分开处理
		if("XMLHttpRequest".equals(httpRequest.getHeader("X-Requested-With"))){
			httpResponse.setHeader("TIMEOUTURL", timeoutUrl);
			httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
		}else{
			PrintWriter out;
			try {
				out = httpResponse.getWriter();
				out.println("<html>");
		        out.println("<script>");
		        out.println("window.open ('"+timeoutUrl+"','_top')");
		        out.println("</script>");
		        out.println("</html>");
//				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
