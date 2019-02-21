<%@page import="com.yonyou.util.theme.ThemePath"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<%
	String ContextPath =request.getContextPath();
%>
var roleId = '<%=request.getParameter("PARENT_PARTY_ID")%>';

<html lang="zh-cn">
	<head>
		<meta charset="utf-8">
		<title>单表模板</title>
	</head>
	<body id="body_div">

		<form class="form-horizontal">
			<div class="panel-primary">
				
				<div id="bulidTable">
					<input type="hidden" value='<%=request.getParameter("PARENT_PARTY_ID")%>'  id="PARENT_PARTY_ID" />
					<input type="hidden" value='<%=request.getParameter("PARENT_PARTY_CODE")%>'  id="PARENT_PARTY_CODE" />
					
					<div class="panel panel-primary">
						<div class="panel-heading" id ='pageName'>
							单表模板
						</div>
						<div id="bill_date_and_status"></div>
						
						<div class="panel-body" id="queryParam" style="display: none;"></div>
						<div class="input-box-toggle" onclick="moreToggle()">
							<span class="caret"></span>更多搜索条件
						</div>
					</div>

							<!-- 列表页面 -->
						<div id='button_div' class="button-menu"> </div>
						<table id="table" data-row-style="rowStyle"></table>

					
				</div>
			
				<div id="bulidPage" style="display: none">
					<div class="new-box">
						<div class='box-btn button-menu' id="save-back">
								<div class="button-group save"  onclick="save(this)"> 
									<span class="img" ></span> 
									<span>保存</span>
								</div>
								<div class="button-group back" onclick="back(this)">
									<span class="img" ></span> 
									<span>返回</span>
								</div>
						
						<%-- 
								<button type="button" class="btn btn-default" id="btn-save" onclick="save(this)">
									<img src= "<%=ContextPath %>/pages/theme/zw/img/save.png" alt="" />
									<span>保存</span>
								</button>
								<button type="button" class="btn btn-default" onclick="back(this)">
									
									<span class="glyphicon glyphicon-ban-circle" aria-hidden="true"></span>
									<span>返回</span>
								</button>
								 --%>
							</div>
					
					</div>		
					<div class="panel panel-primary">
						<div id="tog_titleName" class="panel-heading">
							新增
						</div>
						<div class="panel-body">
							<div class="alert alert-info" id="message" style="display: none"></div>
							
							<!-- <div>
								<button type="button" class="btn btn-default" onclick="save(this)">
									<span class="glyphicon glyphicon-saved" aria-hidden="true"></span>保存
								</button>
								<button type="button" class="btn btn-default" onclick="back(this)">
									<span class="glyphicon glyphicon-ban-circle" aria-hidden="true"></span>返回
								</button>
							</div> -->
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
		</form>
		
	</body>
	<jsp:include page="../../../include/public.jsp"></jsp:include>
	<jsp:include page="../../buttonjs.jsp"></jsp:include>
	<link rel="stylesheet" type="text/css" href="<%=ThemePath.findPath(request, ThemePath.SUB_SYSTEM_CSS)%>">
	<script type="text/javascript">
		$(function() {
			bulidPage(true,true,true,true);
			//var oFileInput = new FileInput();
		   	//oFileInput.Init("txt_file", "/api/OrderApi/ImportOrder");
		});
	</script>
</html>