<%@page import="com.yonyou.util.theme.ThemePath"%>
<%@page import="com.yonyou.util.RmIdFactory"%>
<%@page import="com.yonyou.util.PropertyFileUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
	String ContextPath =request.getContextPath();
%>
<html lang="zh-cn">
<head>
	<meta charset="utf-8">
	<title>单表模板</title>
</head>
<body style="background-color: rgb(240,240,241)">
	<form>
		<div id="bulidTable">
			<input type="hidden" value='<%=request.getParameter("PARENT_PARTY_ID")%>' id="PARENT_PARTY_ID" /> 
			<input type="hidden" value='<%=request.getParameter("PARENT_PARTY_CODE")%>' id="PARENT_PARTY_CODE" />

			<div class="panel panel-primary">
				<div class="panel-heading" id='pageName'>单表模板</div>
				<div id="bill_date_and_status" style="display: none;"></div>
				<div class="panel-body" id="queryParam" style="display: none;"></div>
				<div class="input-box-toggle" onclick="moreToggle()">
					<span class="caret"></span>更多搜索条件
				</div>
				<!-- 列表页面 -->
				<div id='button_div' class="button-menu"></div>
				<table id="table" data-row-style="rowStyle"></table>
			</div>
		</div>

		<div class="panel-body" id="bulidPage" style="display: none">
			<div class="panel panel-primary">
				<div id="tog_titleName" class="panel-heading">新增</div>
				<div class="panel-body">
					<div class="alert alert-info" id="message" style="display: none"></div>
					<div>
						<button type="button" id="btnSave" class="btn btn-success" onclick="save(this)">
							<span class="glyphicon glyphicon-saved" aria-hidden="true"></span>保存
						</button>
						<button type="button" class="btn btn-inverse" onclick="back(this)">
							<span class="glyphicon glyphicon-ban-circle" aria-hidden="true"></span>返回
						</button>
						<button type="button" id="audit" class="btn btn-primary" onclick="singlePubRestAuditByTenant(this)" buttontoken="audit" disabled="true">
							<span class="glyphicon glyphicon-check" aria-hidden="true"></span>提交
						</button>
						<button type="button" id="auditProcess" class="btn btn-primary" onclick="singlePubVisitHis(this)" buttontoken="audit">
							<span class="glyphicon glyphicon-film" aria-hidden="true"></span>审批进程
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
		<input type="hidden" id="isNewStyle" value="1"/>
	</form>
</body>

<jsp:include page="buttonjs.jsp"></jsp:include>
<jsp:include page="../include/public.jsp"></jsp:include>
<jsp:include page="postageDetailPage.jsp"></jsp:include>
<link rel="stylesheet" type="text/css" href="<%= ThemePath.findPath(request, ThemePath.SUB_SYSTEM_CSS)%>">
<script type="text/javascript">
	var nginxPortalUrl = '<%=PropertyFileUtil.getValue("NGINX_PORTAL_URL")%>';
	$(function() {
		bulidPage(true,true,true,true);
	});
	function getId(){
		var rmId = '<%= RmIdFactory.requestId("OPEN_APPLY", 1)[0]%>';
		return rmId;
	}
</script>
</html>