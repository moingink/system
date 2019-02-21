<%@page import="com.yonyou.util.PropertyFileUtil"%>
<%@page import="com.yonyou.util.theme.ThemePath"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%String ContextPath =request.getContextPath();%>
<html lang="zh-cn">
	<head>
		<meta charset="utf-8">
		<title>项目历史版本</title>
	</head>
	<body id="body_div" style="overflow: scroll;">
		<form class="form-horizontal">
			<div class="panel panel-primary">
				<div class="panel-body" id="bulidPage">
					<div class="panel panel-primary">
						<div id="tog_titleName" class="panel-heading">
							项目历史版本
						</div>
						<button id="fh" type="button" class="btn btn-inverse" onclick="window.history.go(-1);" style="margin-left:15px;margin-top:5px">
							<span class="glyphicon glyphicon-ban-circle" aria-hidden="true"></span>返回
						</button>
						
						<div class="panel-body">
							<div class="alert alert-info" id="message" style="display: none"></div>
							
							
							<!-- 历史版本控制 -->
							<table style="width: 500px;" class="table table-bordered table-hover">
								<tr class="info">
									<th style="text-align:center">项目阶段</th>
									<th style="text-align:center">文档版本</th>
								</tr>
								<tr align="center">
									<td>项目建议书</td>
									<td id="td"></td>
								</tr>
								<tr align="center">
									<td>前坪估文档</td>
									<td id="td1"></td>
								</tr>
								
							</table>
							
							
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
			</div>
		</form>
	</body>
	<jsp:include page="../include/public.jsp"></jsp:include>
	<jsp:include page="buttonjs.jsp"></jsp:include>
	<link rel="stylesheet" type="text/css" href="<%=ThemePath.findPath(request, ThemePath.SUB_SYSTEM_CSS)%>">
	<script type="text/javascript">
		var ParentPKValue = '<%=request.getParameter("ParentPKValue")%>';
		window.onload = function(){
			//获取建议书历史版本
			var fill = new Object();
			$.ajax({
				url : "/system/projectHistory/selectHisVersion?proj_released_id="+ParentPKValue,  
                dataType : "json",  
                type : "GET",  
                async: false, 
                success : function(data) {
                	fill = data;
                }
		    		
		    });
		    
		    //获取前坪估历史版本
		    var fill1 = new Object();
		    $.ajax({
				url : "/system/projectHistory/selectAccHisVersion?proj_released_id="+ParentPKValue,  
                dataType : "json",  
                type : "GET",  
                async: false, 
                success : function(data) {
                	fill1 = data;
                }
		    		
		    });
		    
		    if($.isEmptyObject(fill) == false){
		    	for(var i=0;i<fill.length;i++){
					$("#td").append('<a href="projectSetUpHistory.jsp?isHide=true&pageCode=&menuCode=0&pageName=&ParentPKField=PROJ_RELEASED_ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>&version_history='+fill[i]["VERSION_HISTORY"]+'&state=1" scrolling="no" frameborder="0" width="99%" height="1000""> v'+fill[i]["VERSION_HISTORY"]+'</a><br>');
		    	}
		    }
		    
		    if($.isEmptyObject(fill) == false){
		    	for(var i=0;i<fill1.length;i++){
					$("#td1").append('<a href="preAssessmentHistory.jsp?isHide=true&pageCode=PRE_ASSESSMENT_HISTORY&menuCode=0&pageName=前评估&ParentPKField=PROJ_RELEASED_ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>&version_history='+fill[i]["VERSION_HISTORY"]+'&state=1&token=<%=request.getParameter("token") %>" scrolling="no" frameborder="0" width="99%" height="1000""> v'+fill1[i]["VERSION_HISTORY"]+'</a><br>');
		    	}
		    }
		
		
			
		}
		
		
		/*function Check(){
			$.ajax({
				url : "/system/projectHistory/addHistoryVersion?PROJ_SOURCE_ID=201807230000000001",  
                dataType : "json",  
                type : "GET",  
                async: false, 
                success : function() {
                	alert("success");
                },
                error:function(){
                	alert("error");
                }
		    		
		    });
		}*/
		
	</script>
	
</html>