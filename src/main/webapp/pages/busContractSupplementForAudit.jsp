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
				<div class="panel-heading"></div>
				<div class="panel-body" id="superInsertPage">			
				</div>
			</div>
			<div class="col-md-12 col-xs-12 col-sm-12 classifiedtitle">合同标的</div>
			<table id="BUS_CONTRACT_TARGET_TABLE"></table>
			<div class="col-md-12 col-xs-12 col-sm-12 classifiedtitle">收付款计划</div>
			<table id="BUS_CONTRACT_COLLECT_PAY_PLAN_TABLE"></table>
		</form>
	</body>
	<jsp:include page="../include/public.jsp"></jsp:include>
	<script type="text/javascript">
		var id = '<%=request.getParameter("id")%>';//父表ID
		var param = "&dataSourceCode="+dataSourceCode+"&SEARCH-ID="+id;
		var record = querySingleRecord(param);
		$(function() {
			//父表数据初始化
			var parentPageParam = pageParamFormat("id="+id)+"&showType=INSUP";
			bulidMaintainPage($("#superInsertPage"),dataSourceCode,parentPageParam);
			//置灰
			$("#superInsertPage").find("[id]").each(function(){
				$(this).attr("disabled",true);
			});
			$("#superInsertPage").find("[span]").each(function(){
				$(this).attr("onclick","");
			});
			//子表数据初始化
			bulidListPage($('#BUS_CONTRACT_TARGET_TABLE'), 'BUS_CONTRACT_TARGET', pageParamFormat("PARENT_ID = '"+id+"'"));
    		bulidListPage($('#BUS_CONTRACT_COLLECT_PAY_PLAN_TABLE'), 'BUS_CONTRACT_COLLECT_PAY_PLAN', pageParamFormat("PARENT_ID = '"+id+"'"));
		});
		
		function dblClickFunction(row,tr){}
	</script>
</html>