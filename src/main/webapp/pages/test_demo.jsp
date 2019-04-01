<%@page import="com.yonyou.util.PropertyFileUtil"%>
<%@page import="com.yonyou.util.theme.ThemePath"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<title>多页签模板</title>
</head>
<body>
<jsp:include page="../include/public.jsp"></jsp:include>
<jsp:include page="buttonjs.jsp"></jsp:include>
<%
	String PROJECT_COST=request.getParameter("PROJECT_COST");
	
%>

	<form>
		<div class="col-md-12">
			<ul id="myTab" class="nav nav-tabs" style="width:1080px;height:42px;">
				<li>
					<btn id="show" class="btn btn-inverse" onclick="window.history.back(-1);" style="margin-top:4px;margin-right:5px;">
						<span class="glyphicon glyphicon-ban-circle" aria-hidden="true"></span>返回
					</btn>
				</li>
				<li class="active"><a id="show" href="#1" data-toggle="tab">子表A</a></li>
				<li><a id="show" href="#5" data-toggle="tab">子表B</a></li>
				<li><a id="show" href="#4" data-toggle="tab" onclick="refreshFrameByBudget()">子表C</a></li>
				<li><a id="show" href="#3" data-toggle="tab">子表D</a></li>
				<li><a id="show" href="#8" data-toggle="tab">子表E</a></li>
				<li><a id="show" href="#9" data-toggle="tab">子表F</a></li>
			</ul>
		</div>
		</br>
		<div class="col-md-12">
			<div class="tab-content">

				<div class="tab-pane fade in active" id='1'>
					 <iframe src="maintainPage_test_demo.jsp?isHide=<%=request.getParameter("isHide")%>&pageCode=MD_PERSONNEL&pageName=项目基本信息&ParentPKField=PROJ_SOURCE_ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>&state=1&token=<%=request.getParameter("token") %>&state=1" scrolling="no" frameborder="0" width="99%" height="1380"  style="min-width:1050px;"></iframe>
				</div>
<!-- 				<div class="tab-pane fade" id='3'> -->
<%-- 				<iframe src="project/progressPlan.jsp?isHide=<%=request.getParameter("isHide")%>&pageCode=PROJ_PROGRESS_PHASE&pageName=进度计划&ParentPKField=PROJ_SOURCE_ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>&state=1" scrolling="no" frameborder="0" width="99%" height="1000"></iframe> --%>
					
<!-- 				</div> -->
<!-- 				<div class="tab-pane fade " id='4'> -->
<%-- 					<iframe id="budget" src="project/budget.jsp?isHide=<%=request.getParameter("isHide")%>&pageCode=PROJ_BUDGET&pageName=项目预算&ParentPKField=PROJ_SOURCE_ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>&state=1" scrolling="no" frameborder="0" width="99%" height="1000"></iframe> --%>
<!-- 				</div> -->
				<div class="tab-pane fade " id='5'>
					<iframe src="project/projectOrganization.jsp?isHide=<%=request.getParameter("isHide")%>&pageCode=PROJ_ORGANIZATION&pageName=组织架构&ParentPKField=PROJ_SOURCE_ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>&state=1"  scrolling="no" frameborder="0" width="99%" height="1000"></iframe>
				</div>
<!-- 				<div class="tab-pane fade " id='6'> -->
<%-- 					<iframe id= "wbs" src="ganttmessage/wbs_gantts.jsp?isHide=<%=request.getParameter("isHide")%>&pageCode=PROJ_WBSINFO&pageName=wbs&ParentPKField=PROJ_SOURCE_ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>&state=1" scrolling="no" frameborder="0" width="99%" height="1000"></iframe> --%>
<!-- 				</div> -->
<!-- 				<div class="tab-pane fade" id='8'> -->
<%-- 					<iframe src="project/riskTracking.jsp?isHide=<%=request.getParameter("isHide")%>&pageCode=PROJ_RISK_MANAGEMENT&pageName=项目风险问题管控&ParentPKField=PROJ_SOURCE_ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>&state=1" scrolling="no" frameborder="0" width="99%" height="1000"></iframe> --%>
<!-- 				</div> -->
<!-- 				<div class="tab-pane fade" id='9'> -->
<%-- 					<iframe src="otherPlans.jsp?isHide=<%=request.getParameter("isHide")%>&pageCode=PROJ_OTHERPLAN&pageName=客户建设项目其他计划&ParentPKField=PROJ_SOURCE_ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>&state=1" scrolling="no" frameborder="0" width="99%" height="1000" style="min-width:1000px;"></iframe> --%>
<!-- 				</div> -->
			</div>
		</div>

	</form>
<div class="modal fade" id="modalX" tabindex="-1" role="dialog" aria-labelledby="modalTitle" aria-hidden="true">
	<div class="modal-dialog" style="width: 90%; min-width:1030px;" >
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h4 class="modal-title" id="modalTitle"></h4>
			</div>
			<div id="myModalX" class="modal-body"></div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" id="qd_modal">确定</button>
			</div>
		</div>
	</div>
</div>
</body>
<script type="text/javascript">
	
	$(function (){
		 setParntHeigth(1500);
	});

	token = '<%=request.getParameter("token")!=null?request.getParameter("token"):""%>';
	//使用维护页面模板的每次激活页签时向服务器请求最新数据更新此页签
	$('a[data-toggle="tab"]').on('show.bs.tab', function (e) {
		console.log(e.target.hash);
		var getRecord = $(e.target.hash+" iframe:first-child")[0].contentWindow.getRecord;
		if(typeof(getRecord) == "function"){
			getRecord();
		}
	});

	function refreshFrame(){
   		document.getElementById('wbs').contentWindow.location.reload(true);
	}
	function refreshFrameByBudget(){
   		document.getElementById('budget').contentWindow.location.reload(true);
	}
</script>

</html>