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
	</form>
		<!-- 缓存参数 -->
		<!-- 是否为修改页面 -->
		</body>
	<jsp:include page="../include/public.jsp"></jsp:include>
	<script type="text/javascript">
	  $(function() {
			    var mainPageParam =pageParamFormat(ParentId +" ="+ParentPKValue);
			    mainPageParam=mainPageParam+"&showType=INSUP";
				bulidMaintainPage($("#superInsertPage"),dataSourceCode,mainPageParam);
				$("#superInsertPage").find("[id]").each(function(){
						$(this).attr("disabled",true);
				});
				$("#superInsertPage").find("[span]").each(function(){
						$(this).attr("onclick","");
				});
				
	  });
	</script>
</html>