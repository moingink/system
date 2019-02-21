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
				<li class="active"><a href="#1" data-toggle="tab">项目概况22</a></li>
				<li><a href="#3" data-toggle="tab">项目进度阶段划分</a></li>
				<li><a href="#4" data-toggle="tab">项目预算</a></li>
				<li><a href="#5" data-toggle="tab">项目组织架构</a></li>
				<li><a href="#8" data-toggle="tab">项目风险问题管控</a></li>
				<li>
					<a class="btn btn-success" onclick="audit(this);" buttontoken="audit">
						<span class="glyphicon glyphicon-check" aria-hidden="true"></span>提交
					</a>
				</li>
			</ul>
		</div>
		</br>
		<div class="col-md-12">

			<div class="tab-content">

				<div class="tab-pane fade in active" id='1'>
					<iframe src="maintainPage.jsp?pageCode=PROJ_PROPOSAL&menuCode=0&pageName=项目基本信息&ParentPKField=PROJ_SOURCE_ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>&state=1" scrolling="no" frameborder="0" width="99%" height="1000"></iframe>
				</div>
				<div class="tab-pane fade" id='3'>
					<iframe src="childTableModify.jsp?pageCode=PROJ_PROGRESS_PHASE_JYS_0&pageName=项目进度阶段划分&ParentPKField=PROJ_SOURCE_ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>" scrolling="no" frameborder="0" width="99%" height="1000"></iframe>
				</div>
				<div class="tab-pane fade " id='4'>
					<iframe src="" scrolling="no" frameborder="0" width="99%" height="1000"></iframe>
				</div>
				<div class="tab-pane fade " id='5'>
					<iframe src="childTableModify.jsp?pageCode=PROJ_ORGANIZATION_JYS_0&pageName=项目组织架构&ParentPKField=PROJ_SOURCE_ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>" scrolling="no" frameborder="0" width="99%" height="1000"></iframe>
				</div>
				<div class="tab-pane fade" id='8'>
					<iframe src="childTableModify.jsp?pageCode=PROJ_RISK_MANAGEMENT_JYS_0&pageName=项目风险问题管控&ParentPKField=PROJ_SOURCE_ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>" scrolling="no" frameborder="0" width="99%" height="1000"></iframe>
				</div>

			</div>
		</div>

	</form>

</body>
<jsp:include page="../include/public.jsp"></jsp:include>

<script type="text/javascript">

    

	//使用维护页面模板的每次激活页签时向服务器请求最新数据更新此页签
	$('a[data-toggle="tab"]').on('show.bs.tab', function (e) {
		console.log(e.target.hash);
		var getRecord = $(e.target.hash+" iframe:first-child")[0].contentWindow.getRecord;
		if(typeof(getRecord) == "function"){
			getRecord();
		}
	})
	
	function audit(t){
		var type =1;
		var audit_column = findAuditColumn();
		if(audit_column.length == 0){
			oTable.showModal('提示', "没有设置审批字段");
		}
		var selected = getJson($("#1 iframe:first-child").contents().find(insPageDivId));
		var billStatus = selected[audit_column];
		if(billStatus==undefined||billStatus==''||billStatus.length==0){
			oTable.showModal('提示', "审批状态为空，不能提交！");
			return;
		}
		
		if(billStatus != 0 && billStatus != 7){
			oTable.showModal('提示', "只能提交  已保存、已退回单据");
			return;
		}
		var apprType="&audit_type="+type+"&audit_column="+audit_column+"&tenant_code="+tenant_code;
		var message = transToServer(findBusUrlByPublicParam(t,apprType,dataSourceCode),JSON.stringify(selected));
		oTable.showModal('提示', message);
	}
</script>

</html>