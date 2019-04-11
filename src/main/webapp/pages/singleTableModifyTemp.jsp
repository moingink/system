<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String ContextPath =request.getContextPath();
%>
<!DOCTYPE html>
<html lang="zh-cn">
	<head>
		<meta charset="utf-8">
		<title>单表模板</title>
		<link rel="stylesheet" type="text/css" href="<%=ContextPath%>/pages/theme/vehicles/css/dataFlow_b.css">
		<script type="text/javascript" src="<%=ContextPath%>/pages/theme/vehicles/js/metadata.js"></script>
	</head>
	<body>
		<form class="form-horizontal">
				
				<div class="panel-body" id="bulidTable">
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
							<!-- 列表页面 -->
						<div id='button_div' class="button-menu"> </div>
						<table id="table" data-row-style="rowStyle"></table>
						
					
					</div>
				</div>
			
				<div class="panel-body" id="bulidPage" style="display: none">
					<div class="panel panel-primary">
						<div id="tog_titleName" class="panel-heading">
							新增
						</div>
						<div class="panel-body">
							<div class="alert alert-info" id="message" style="display: none"></div>
							<div>
								<button type="button" class="btn btn-success" onclick="save(this)">
									<span class="glyphicon glyphicon-saved" aria-hidden="true"></span>保存
								</button>
								<button type="button" class="btn btn-inverse" onclick="back(this)">
									<span class="glyphicon glyphicon-ban-circle" aria-hidden="true"></span>返回
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
		</form>
		<div  id="bulidPage_detail" style="display: none;">
		<div class="panel panel-primary">
			<div id="tog_titleName" class="panel-heading">
				<span class="img"></span> <span>列修改</span>
			</div>
			<div class="panel-body">
				<form class="form-inline" id="form" action="javaScript:update(this)">
					<div class="alert alert-info" id="message" style="display: none"></div>

					<div>
						<button type="submit" class="btn btn-success">
							<span class="glyphicon glyphicon-saved" aria-hidden="true"></span>保存
						</button>
						<button type="button" class="btn btn-inverse" onclick="backs(this)">
							<span class="glyphicon glyphicon-ban-circle" aria-hidden="true"></span>返回
						</button>
					</div>
					<div class="form-group"></div>
					<!-- 维护页面 -->
					
					<table id="flow_b"
						class="table table-bordered table-hover table-striped">
						<thead id="thead_flow_b">

						</thead>
						<tbody id="tbody_flow_b">

						</tbody>
					</table>
				</form>
			</div>
		</div>
	</div>
	</body>
	<jsp:include page="../include/public.jsp"></jsp:include>
	<jsp:include page="./buttonjs.jsp"></jsp:include>
	<script type="text/javascript">
		var flow_id="";
		$(function() {
				bulidPage(true,true,true,true);
		});
		function backs(temp) {
			$("#bulidPage_detail").slideUp();
			$("#bulidTable").slideDown(1000);
			$("#tbody_flow_b").html("");
		}
	</script>
</html>