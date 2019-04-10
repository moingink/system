<%@page import="com.yonyou.util.RmIdFactory"%>
<%@page import="com.yonyou.util.theme.ThemePath"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<%
String ContextPath =request.getContextPath();
%>

<html lang="zh-cn">
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"> 
		<title>页签模板</title>
	</head>
	<body id="body_div">
		<form class="form-horizontal">
			<div class="panel panel-primary">
				<div class="panel-body" id="bulidPage">
					<div class="panel panel-primary">
						<div id="tog_titleName" class="panel-heading">
							新增 
						</div>
						<div class="panel-body">
							<div class="alert alert-info" id="message" style="display: none"></div>
							<div>
								<button id="ins_or_up_buttontoken" type="button" class="btn btn-success" onclick="save(this)">
									<span class="glyphicon glyphicon-saved" aria-hidden="true"></span>保存
								</button>
							</div>
							<div class="form-group">
								<div class="col-md-12">
									<div id="errors"></div>
								</div>
							</div>
							<!-- 维护页面 -->
							<div class="panel-body" id="insPage">
	
							</div>
						</div>
					</div>
				</div>
			<input type="hidden" id="isNewStyle" value="1" />
		</div>
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
		var proj_source_id = '<%=request.getParameter("proj_source_id")%>';
		var pageCode = '<%=request.getParameter("pageCode")%>';
		//是否需要加载后台数据
		var state = '<%=request.getParameter("state")%>';
		var button ="add";
		var uname = '<%=request.getParameter("userName")%>';
		var comname = '<%=request.getParameter("companyName")%>';
            
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
			$('#back').removeAttr("disabled");
			
			if(!jQuery.isEmptyObject(record)){
				//已存在记录时为修改
				button ="";
				$("#ins_or_up_buttontoken").val('update');
				$("#tog_titleName").html("修改");
				$inspage.find("[id]").each(function() {			
	  				$(this).val(record[$(this).attr("id")]);
				});
			}
		}
		
		//重写方法，保存后不清空输入框而是设为只读，保存按钮置灰
		function savaByQuery(t,_dataSourceCode,$div){
			console.log($div);
			var message ="";
			var buttonToken=$("#ins_or_up_buttontoken").val();
			console.log(buttonToken);
// 			if(pageCode=="PROJ_RELEASED_NLXM1"){
// 				if(button =="add"){
<%-- 					$("#ID").val('<%= RmIdFactory.requestId("PROJ_RELEASED", 1)[0]%>'); --%>
// 					buttonToken ="insertReleasedNL";
// 				}
// 				message = transToServer(findBusUrlByButtonTonken(buttonToken,'&table=PROJ_RELEASED',_dataSourceCode),getJson($div));
// 			}else{
// 				if(button=="add"){
<%-- 					$("#ID").val('<%= RmIdFactory.requestId("PROJ_RELEASED", 1)[0]%>'); --%>
// 				}
// 				message = transToServer(findBusUrlByButtonTonken(buttonToken,'',_dataSourceCode),getJson($div));
// 			}
			if(buttonToken=="add"){
				$("#ID").val('<%= RmIdFactory.requestId("PROJ_RELEASED", 1)[0]%>');
	 		}
			message = transToServer(findBusUrlByButtonTonken(buttonToken,'',_dataSourceCode),getJson($div));
			if(message=="保存成功"){
				location.reload();
			}
			setReadonlyByDiv($("#insPage input[type='text']"));
			$("#ins_or_up_buttontoken").attr("disabled", true);
			
		}
	
	
	</script>
</html>