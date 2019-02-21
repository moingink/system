<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
	<head>
		<meta charset="utf-8">
		<title>单表模板</title>
	</head>
	<body>
		<form class="form-horizontal">
			<div class="panel panel-default">
				<div class="panel-heading">
					信息
				</div>
				<!-- 维护页面-->
				<div class="panel-body" id="superInsertPage">			
				</div>
			</div>
			<!-- 列表页面 -->
			<table id="table"></table>
		</form>
	</body>
	<jsp:include page="../include/public.jsp"></jsp:include>
	<script type="text/javascript">
		var id = '<%=request.getParameter("id")%>';//父表ID
		var param = "&dataSourceCode="+dataSourceCode+"&SEARCH-ID="+id;
		var record = querySingleRecord(param);
		var parentVal = record["BILL_NO"];//父表键值
		var childCode = '<%=request.getParameter("childCode")%>';//子表数据源编码
		var childField = '<%=request.getParameter("childField")%>';//子表外键
		
		$(function() {
			//父表数据初始化
			var parentPageParam = pageParamFormat("id="+id)+"&showType=INSUP";
			bulidMaintainPage($("#superInsertPage"),dataSourceCode,parentPageParam);
			$("#BILL_STATUS").parent().parent().parent().css("display","none");//审核状态隐藏
			//置灰
			$("#superInsertPage").find("[id]").each(function(){
				$(this).attr("disabled",true);
			});
			$("#superInsertPage").find("[span]").each(function(){
				$(this).attr("onclick","");
			});
			//子表数据初始化
			var childPageParam = pageParamFormat(childField+"="+"\""+id+"\"");
			bulidListPage($("#table"),childCode,childPageParam);
		});
		
		function dblClickFunction(row,tr){}
	</script>
</html>