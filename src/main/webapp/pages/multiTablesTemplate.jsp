<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
<meta name="viewport"
	content="width=device-width, minimum-scale=1, maximum-scale=1">
<head>
<meta charset="utf-8">
<title>多表展示模板</title>
</head>
<body>
	<form class="form-horizontal">
		<div class="panel-body" id="superInsertPage"></div>
	</form>
</body>
<jsp:include page="../include/public.jsp"></jsp:include>
<script type="text/javascript">
	var dataSourceCodes = '<%=request.getParameter("dataSourceCodes")%>'.split(",");
	var joinField = '<%=request.getParameter("joinField")%>'.split(",");
	var fieldValue = '<%=request.getParameter("fieldVal")%>';
	
	$(function() {
		console.log(dataSourceCodes.length + " " + joinField.length);
		console.log(dataSourceCodes);
		console.log(joinField);
		if(joinField != null && joinField != ""){
			if(dataSourceCodes.length != joinField.length + 1){
				alert("请求参数错误");
				return;
			}
			alert("非多表请求");
		}
		//首先构建主表页面
		buildDiv("ID", fieldValue, dataSourceCodes[0]);
		for(var i=1; i<dataSourceCodes.length;i++){
			buildDiv(joinField[i-1], fieldValue, dataSourceCodes[i]);
		}
		
		
		
		$("#superInsertPage").find("[id]").each(function(){
			$(this).attr("disabled",true);
		});
		$("#superInsertPage").find("[span]").each(function(){
			$(this).attr("onclick","");
		});
	});
	
	function buildDiv(field, value, _dataSourceCode){
		var mainPageParam =pageParamFormat(field +" ="+value);
		mainPageParam=mainPageParam+"&showType=INSUP";
		bulidMaintainPage($("#superInsertPage"),_dataSourceCode,mainPageParam);
	}
	</script>
</html>