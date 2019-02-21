<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<title>多页签模板</title>
</head>

<body>

	<form>
		<div class="col-md-12">
			<ul id="myTab" class="nav nav-tabs">
				<li>
					<a class="btn btn-inverse" onclick="window.history.back(-1);">
						<span class="glyphicon glyphicon-ban-circle" aria-hidden="true"></span>返回
					</a>
				</li>
				<li class="active"><a href="#1" data-toggle="tab">项目组</a></li>
				<li><a href="#2" data-toggle="tab">项目风险</a></li>
				<li><a href="#3" data-toggle="tab">项目周报</a></li>1
				<li><a href="#4" data-toggle="tab">项目预算</a></li>
				<li><a href="#5" data-toggle="tab"></a></li>
				<li><a href="#6" data-toggle="tab">项目实施计划</a></li>
				<li><a href="#7" data-toggle="tab">采购建议</a></li>
				<li><a href="#8" data-toggle="tab">项目风险问题管控</a></li>
				<li><a href="#9" data-toggle="tab">其他相关计划</a></li>
				<li><a href="#10" data-toggle="tab">附件</a></li>
			</ul>
		</div>
		</br>
		<div class="col-md-12">

			<div class="tab-content">

				<div class="tab-pane fade in active" id='1'>
					<iframe src="childTableModify.jsp?pageCode=PROJ_ORGANIZATION_JYS_0&pageName=项目进组织架构&ParentPKField=PROJ_SOURCE_ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>&state=1" scrolling="no" frameborder="0" width="99%" height="1000"></iframe>
					<iframe src="maintainPage.jsp?pageCode=PROJ_PROPOSAL_GK&menuCode=0&pageName=项目概况&ParentPKField=PROJ_SOURCE_ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>&state=1" scrolling="no" frameborder="0" width="99%" height="1000"></iframe>
				</div>
				<div class="tab-pane fade" id='2'>
					<iframe src="childTableModify.jsp?pageCode=PROJ_RISK_MANAGEMENT_JYS_0&pageName=项目风险问题管控&ParentPKField=PROJ_SOURCE_ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>" scrolling="no" frameborder="0" width="99%" height="1000"></iframe>
					<iframe src="maintainPage.jsp?pageCode=PROJ_PROPOSAL_FW&menuCode=0&pageName=项目范围&ParentPKField=PROJ_SOURCE_ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>" scrolling="no" frameborder="0" width="99%" height="1000"></iframe>
				</div>
				<div class="tab-pane fade" id='3'>
					<iframe src="childTableModify.jsp?pageCode=PROJ_PROGRESS_PHASE_JYS_0&pageName=项目进度阶段划分&ParentPKField=PROJ_SOURCE_ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>" scrolling="no" frameborder="0" width="99%" height="1000"></iframe>
				</div>
				<div class="tab-pane fade " id='4'>
					<iframe src="" scrolling="no" frameborder="0" width="99%" height="1000"></iframe>
				</div>
				<div class="tab-pane fade " id='5'>
					
				</div>
				<div class="tab-pane fade" id='6'>
					<iframe src="maintainPage.jsp?pageCode=PROJ_PROPOSAL_SSJH&menuCode=0&pageName=项目实施计划&ParentPKField=PROJ_SOURCE_ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>" scrolling="no" frameborder="0" width="99%" height="1000"></iframe>
				</div>
				<div class="tab-pane fade" id='7'>
					<iframe src="maintainPage.jsp?pageCode=PROJ_PROPOSAL_CGJY&menuCode=0&pageName=采购建议&ParentPKField=PROJ_SOURCE_ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>" scrolling="no" frameborder="0" width="99%" height="1000"></iframe>
				</div>
				<div class="tab-pane fade" id='8'>
					<iframe src="childTableModify.jsp?pageCode=PROJ_RISK_MANAGEMENT_JYS_0&pageName=项目风险问题管控&ParentPKField=PROJ_SOURCE_ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>" scrolling="no" frameborder="0" width="99%" height="1000"></iframe>
				</div>
				<div class="tab-pane fade" id='9'>
					<iframe src="maintainPage.jsp?pageCode=PROJ_PROPOSAL_QTJH&menuCode=0&pageName=其他相关计划&ParentPKField=PROJ_SOURCE_ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>" scrolling="no" frameborder="0" width="99%" height="1000"></iframe>
				</div>
				<div class="tab-pane fade" id='10'>
					<iframe src="maintainPage.jsp?pageCode=PROJ_PROPOSAL_FJ&menuCode=0&pageName=附件&ParentPKField=PROJ_SOURCE_ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>" scrolling="no" frameborder="0" width="99%" height="1000"></iframe>
				</div>

			</div>
		</div>

	</form>

</body>
<jsp:include page="../include/public.jsp"></jsp:include>

<script type="text/javascript">
	//使用维护页面模板的每次激活页签时向服务器请求最新数据更新此页签
	$('a[data-toggle="tab"]').on('show.bs.tab', function (e) {
		var getRecord = $(e.target.hash+" iframe:first-child")[0].contentWindow.getRecord;
		if(typeof(getRecord) == "function"){
			getRecord();
		}
	})
</script>

</html>