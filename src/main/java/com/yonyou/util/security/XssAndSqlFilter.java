package com.yonyou.util.security;

import com.yonyou.business.entity.TokenEntity;
import com.yonyou.util.PropertyFileUtil;

import java.io.IOException;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class XssAndSqlFilter
  implements Filter
{
  private Set<String> ignoreSet = new HashSet();

  public void destroy()
  {
  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    throws IOException, ServletException
  {
    HttpServletRequest httpRequest = (HttpServletRequest)request;
    HttpServletResponse httpResponse = (HttpServletResponse)response;
    String method = httpRequest.getMethod();

    if (isIgnore(httpRequest)) {
      chain.doFilter(request, response);
      return;
    }

    String nginx_portal_url = PropertyFileUtil.getValue("NGINX_PORTAL_URL");
    String token1 = httpRequest.getParameter("token");
    TokenEntity tes = new TokenEntity(findTokenByCookie(httpRequest));
    String token2 = tes.getToken();
    String token3 = (String)httpRequest.getSession().getAttribute("token");
    String tokens = null;
    if ((token1 != null) && (!"".equals(token1)))
      tokens = token1;
    else {
      token1 = null;
    }
    if ((token2 != null) && (!"".equals(token2)))
      tokens = token2;
    else {
      token2 = null;
    }
    if ((token3 != null) && (!"".equals(token3)))
      tokens = token3;
    else {
      token3 = null;
    }
    if ((token1 == null) && (token2 == null) && (token3 == null)) {
      httpResponse.sendRedirect(nginx_portal_url + "/login");
      System.out.println("XssAndSqlFilter未获取token信息调整登录页面:" + nginx_portal_url);  
      return;
    }
    httpRequest.getSession().setAttribute("token", tokens);

    if ((method.equalsIgnoreCase("post")) || (method.equalsIgnoreCase("get")) || 
      (method.equalsIgnoreCase("head"))) {
      if (isIgnore(httpRequest)) {
        chain.doFilter(request, response);
      } else {
        String contextPath = httpRequest.getServletPath().toLowerCase();
        contextPath = contextPath.substring(contextPath
          .lastIndexOf("/"));
        if ((contextPath.contains(".")) && (!contextPath.endsWith(".jsp")) && 
          (!contextPath.endsWith(".js")) && 
          (!contextPath.endsWith(".html")) && 
          (!contextPath.endsWith(".png")) && 
          (!contextPath.endsWith(".ico")) && 
          (!contextPath.endsWith(".jpg")) && 
          (!contextPath.endsWith(".css")) && 
          (!contextPath.endsWith(".ftl")) && 
          (!contextPath.endsWith(".woff")) && 
          (!contextPath.endsWith(".woff2")) && 
          (!contextPath.endsWith(".map")) && 
          (!contextPath.endsWith(".ttf")) && 
          (!contextPath.endsWith(".xls")) && 
          (!contextPath.endsWith(".xlsx")) && 
          (!contextPath.endsWith(".pdf")) && 
          (!contextPath.endsWith(".doc")) && 
          (!contextPath.endsWith(".docx")) && 
          (!contextPath.endsWith(".gif"))) {
          throw new ServletException("非法请求!" + 
            httpRequest.getServletPath());
        }
        XssAndSqlHttpServletRequestWrapper xssRequest = new XssAndSqlHttpServletRequestWrapper(
          (HttpServletRequest)request);
        chain.doFilter(xssRequest, response);
        if (contextPath.endsWith(".js")) {
          httpResponse.setHeader("X-Content-Type-Options", "nosniff");
          httpResponse.setHeader("Content-Security-Policy", 
            "default-src 'self'");
          httpResponse.setHeader("X-XSS-Protection", "1");
        }

      }

      httpResponse.setHeader("X-Frame-Options", "SAMEORIGIN");
    }
  }

  public void init(FilterConfig filterConfig) throws ServletException
  {
    String cp = filterConfig.getServletContext().getContextPath();
    String ignoresParam = filterConfig.getInitParameter("ignores");
    String[] ignoreArray = ignoresParam.split(",");
    for (String s : ignoreArray) {
      System.out.println("$$$$$" + cp + s);
      this.ignoreSet.add(cp + s);
    }
  }

  private boolean isIgnore(HttpServletRequest httpRequest)
  {
    String uri = httpRequest.getRequestURI();
    for (String ignore : this.ignoreSet) {
      if (uri.startsWith(ignore)) {
        return true;
      }
    }
    return false;
  }

  private String findTokenByCookie(HttpServletRequest request)
  {
    String token = "";
    Cookie[] cookies = request.getCookies();
    if ((cookies != null) && (cookies.length > 0)) {
      for (Cookie cookie : cookies) {
        if ("token".equals(cookie.getName())) {
          token = cookie.getValue();
        }
      }
      return token;
    }
    return token;
  }
}