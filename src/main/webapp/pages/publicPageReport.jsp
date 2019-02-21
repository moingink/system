<%@page import="com.yonyou.util.theme.ThemePath"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<%
	String ContextPath =request.getContextPath();
%>

<html lang="zh-cn">
<head>
<meta charset="utf-8">
<title>页签模板</title>
<link href="dist/dialog.css" rel="stylesheet">
</head>
<script src="dist/mDialogMin.js"></script>
<style type="text/css">
.zt {
	color: #F00;
}
.pdr{
	padding-right: 0!important;
}
</style>


</head>
	<body id="body_div">
		<form class="form-horizontal">
			<div class="panel-body" id="bulidPage">
				<div class="panel panel-primary">
					<div class="panel-body">
						<div class="alert alert-info" id="message" style="display: none"></div>
						<div>
							<button type="button" class="btn btn-success" onclick="save(this)">
								<span class="glyphicon glyphicon-saved" aria-hidden="true"></span>保存
							</button>
						</div>
						<div class="form-group">
							<div class="col-md-12">
								<div id="errors"></div>
							</div>
						</div>
						<!-- 维护页面 -->
						<div class="panel-body" id="insPage"></div>
					</div>
				</div>
			</div>
			<input type="hidden" id="isNewStyle" value="1" />
		</form>
	</body>
	<jsp:include page="../include/public.jsp"></jsp:include>
	<jsp:include page="buttonjs.jsp"></jsp:include>
	<link rel="stylesheet" type="text/css" href="<%=ThemePath.findPath(request, ThemePath.SUB_SYSTEM_CSS)%>">
	<script type="text/javascript">
		//主表主键在子表字段名
		var ParentPKField = '<%=request.getParameter("ParentPKField")%>';
		//主表主键值
		var ParentPKValue = '<%=request.getParameter("ParentPKValue")%>';
		//关键字
		var keyWord = '<%=request.getParameter("keyWord")!=null?request.getParameter("keyWord"):""%>';
		//是否需要加载后台数据
		var state = '<%=request.getParameter("state")%>';
		//是否审批查看
		var isWorkFlow = '<%=request.getParameter("isWorkFlow")!=null?request.getParameter("isWorkFlow"):""%>';
		
		$(function() {
			bulidPage(false,true,false,true);//获取按钮页面为true是为了加载数据源对应js
			//默认为新增
			$("#ins_or_up_buttontoken").val('add');
			$("#"+ParentPKField).val(ParentPKValue);
			//第一次进入默认激活的页面需要手动请求下最新数据
			if(state == 1){
				getRecord();
			}
		});
		
		function getRecord(){
			var param = "&dataSourceCode="+dataSourceCode+"&SEARCH-"+ParentPKField+"="+ParentPKValue;
			if(typeof(specifyParam) != "undefined"){
				param += specifyParam;
			}
			var record = querySingleRecord(param);
			if(!jQuery.isEmptyObject(record)){
				//已存在记录时为修改
				$("#ins_or_up_buttontoken").val('update');
				$("#tog_titleName").html("修改");
				$inspage.find("[id]").each(function() {			
	  				$(this).val(record[$(this).attr("id")]);
				});
			}
		}
		
		//重写方法，保存后不清空输入框而是设为只读，保存按钮置灰
		function savaByQuery(t,_dataSourceCode,$div){
			var message ="";
			var buttonToken =$("#ins_or_up_buttontoken").val();
			if(typeof(specifyButtonToken) == "function"){
				buttonToken = specifyButtonToken(buttonToken);
			}
			message = transToServer(findBusUrlByButtonTonken(buttonToken,'',_dataSourceCode),getJson($div));
			oTable.showModal('提示', message);
			setReadonlyByDiv($("#insPage input[type='text']"));
			$("#bc").attr("disabled", true);
		}
		 
	</script>
</html>