<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
	<head>
		<meta charset="utf-8">
		<title>单表模板</title>
	</head>
	<body>

		<form class="form-horizontal">
			<div class="panel panel-primary">
				
				<div class="panel-body" id="bulidTable">
					<div class="panel panel-primary">
						<div class="panel-heading" id ='pageName'>
							单表模板
						</div>
						<!-- 查询页面 -->
						<div class="panel-body" id="queryParam"></div>
						<!-- 按钮 -->
						<div id='button_div' ></div>
						<!-- 列表页面 -->
						<table id="table"></table>
						<!-- 维护 -->
						<div class="panel-body" id="insPage"></div>
						111111
						<!-- 维护 -->
						<div class="panel-body" id="insPage2"></div>
					</div>
				</div>
				
		</form>
		<!-- 是否为修改页面 -->
		<input type="hidden" id="ins_or_up_buttontoken"/>
		<input type="text" id="query_buttontoken"/>
		<input type="hidden" id="#cache_dataSourceCode" />
		
	</body>
	<jsp:include page="../include/public.jsp"></jsp:include>
	<script type="text/javascript">
		$(function() {
				//bulidPage(false,false,false,true);
				var mainPageParam =pageParamFormat('ID =1');
				mainPageParam=mainPageParam+"&showType=text";
				bulidMaintainPage($("#insPage"),"CD_METADATA",mainPageParam);
				bulidMaintainPage($("#insPage2"),"CD_DATASOURCE",mainPageParam);
				//设置 只读
				setReadonlyByDiv($("#insPage2 input[type='text']"));
				
		});
	</script>
</html>