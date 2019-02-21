<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
	<meta name="viewport" content="width=device-width, minimum-scale=1, maximum-scale=1">
	<head>
		<meta charset="utf-8">
		<title>单表模板</title>
	</head>
	<body>
		<form class="form-horizontal">
			<div class="panel-body" id="superInsertPage"></div>
			<table id="table" data-row-style="rowStyle"></table>
		</form>
	</body>
	<jsp:include page="../include/public.jsp"></jsp:include>
	<script type="text/javascript">
		$(function() {
			var mainPageParam =pageParamFormat(ParentId +" =<%=request.getParameter("id")%>");
			mainPageParam=mainPageParam+"&showType=INSUP";
			bulidMaintainPage($("#superInsertPage"),"DOUBLE_IMPORT",mainPageParam);
			bulidListPage($("#table"),'DOUBLE_IMPORT_DETAIL',pageParamFormat("PARENT_ID = <%=request.getParameter("id")%>"));
			$("#superInsertPage").find("[id]").each(function(){
				$(this).attr("disabled",true);
			});
			$("#superInsertPage").find("[span]").each(function(){
				$(this).attr("onclick","");
			});	
		});
	</script>
</html>