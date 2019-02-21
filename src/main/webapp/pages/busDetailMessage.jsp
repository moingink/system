<%@page import="com.yonyou.util.theme.ThemePath"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<%
	String ContextPath =request.getContextPath();
	String pageCode=request.getParameter("pageCode");
	String busId =request.getParameter("busId");
%>

<html lang="zh-cn">
	<head>
		<meta charset="utf-8">
		<title>单表模板</title>
	</head>
	<body id="body_div">

		<form class="form-horizontal">
				
				<div id="bulidTable">
					<input type="hidden" value='<%=request.getParameter("PARENT_PARTY_ID")%>'  id="PARENT_PARTY_ID" />
					<input type="hidden" value='<%=request.getParameter("PARENT_PARTY_CODE")%>'  id="PARENT_PARTY_CODE" />
				</div>
			
				<!-- 维护页面 -->
				<div class="panel-body" id="insPageDetail">
					
				</div>
		
		<input type="hidden" id="isNewStyle" value="1" />
		</form>
		
	</body>
	<jsp:include page="../include/public.jsp"></jsp:include>
	<script type="text/javascript">
	
		$(function() {
			$insPageDetail =$("#insPageDetail");
			var mainPageParam =pageParamFormat(ParentId +"=<%=busId%>");
		    mainPageParam=mainPageParam+"&showType=INSUP";
			bulidMaintainPage($insPageDetail,"<%=pageCode%>",mainPageParam);
			$insPageDetail.find("[id]").each(function(){
					$(this).attr("disabled",true);
			});
			$insPageDetail.each(function(){
					$(this).attr("onclick","");
			});
		});
	</script>
</html>