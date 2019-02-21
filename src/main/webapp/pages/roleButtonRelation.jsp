<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
	<head>
		<meta charset="utf-8">
		<title>角色按钮管理</title>
	</head>
	<body>
	<form class="form-horizontal">
		<div class="panel panel-primary">
				
				<div class="panel-body" id="bulidTable">
					<div class="panel panel-primary">
						<div class="panel-heading" id ='pageName'>
							<%=request.getParameter("pageName")==null ? "角色按钮管理" : request.getParameter("pageName")%>
						</div>
							
						<div id="bill_date_and_status"></div>
						<!-- 查询页面-->
						<div class="panel-body" id="queryParam"> </div>
						<!-- <div class="input-box-toggle" onclick="moreToggle()">
							<span class="caret"></span>更多搜索条件
						</div> -->
							
						<!-- 父类按钮 -->
						<div id='button_div'>
						</div>
						<div style="height:10px"></div>
						<button name="级别授权" type="button" class="btn btn-default" buttonToken="nextAuth2" onclick="nextAuth2(this)">级别授权</button>
						<input type="text" style="width:160px;text-align: -webkit-center" id="page_level"/>
						<!-- 列表页面 -->
						<table id="table"></table>
					</div>
				</div>
				<input type='hidden' name="ParentPK">
				<input type="hidden" id="isNewStyle" value="" />
			</div>		
			
	</form>
</body>
<jsp:include page="../include/public.jsp"></jsp:include>
<script type="text/javascript">
  $(function() {
	var _dataSourceCode= 'RM_AUTHORIZE_RESOURCE_BUTTON';
	var ParentPKField='';
    var mainPageParam =pageParamFormat("");
    //构建查询界面
	bulidSelect($("#queryParam"),dataSourceCode,mainPageParam);
	//构建按钮
	bulidButton($("#button_div"),_dataSourceCode,'');
	//构建列表页面
	bulidListPageForQusUrl($("#table"),dataSourceCode,'',null);
});
</script>
</html>