<%@page import="com.yonyou.util.theme.ThemePath"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<%
	String ContextPath =request.getContextPath();
%>

<html lang="zh-cn">
	<head>
		<meta charset="utf-8">
		<title>单表模板</title>
	</head>
	<body>
		<form >
			<div id="bulidTable">
				<table id="table" data-row-style="rowStyle"></table>
			</div>
			<input type="hidden" id="isNewStyle" value="1" />
		</form>
	</body>
	<jsp:include page="../include/public.jsp"></jsp:include>
	<jsp:include page="buttonjs.jsp"></jsp:include>
	<link rel="stylesheet" type="text/css" href="<%= ThemePath.findPath(request, ThemePath.SUB_SYSTEM_CSS)%>">
	<script type="text/javascript">
		var totalCode = '<%=request.getParameter("totalCode")%>';
		var term = '<%=request.getParameter("TERM")%>';
		var BUSINESS_TYPE1 = '<%=request.getParameter("BUSINESS_TYPE1")%>';
		var PRODUCT_TYPE1 = '<%=request.getParameter("PRODUCT_TYPE1")%>';
		$(function() {
			bulidPage(false,true,false,true);
		});
		
	</script>
</html>