<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%
String ParentPKField = request.getParameter("ParentPKField");
String ParentPKValue = request.getParameter("ParentPKValue");
String ParentPKField1 = request.getParameter("ParentPKField1");
String ParentPKValue1 = request.getParameter("ParentPKValue1");
%>
<!DOCTYPE html>
<html lang="zh-cn">
	<head>
		<meta charset="utf-8">
		<title>子表模板</title>
	</head>
	<body>
	<form class="form-horizontal" action="" method="">
		<div class="panel panel-primary">

			<div class="panel-body" id="bulidTable">
				<div class="panel panel-primary">
					<div class="panel-heading" id='pageName'>评价</div>

					<div class="panel-body">
						<div id="toolbar">
							<button type="button" class="btn btn-inverse"
								onclick="window.history.back(-1);">
								<span class="glyphicon glyphicon-ban-circle" aria-hidden="true"></span>返回主表
							</button>
							<button type="button" class="btn btn-success"
								onclick="getinput(this)">
								<span class="glyphicon glyphicon-saved" aria-hidden="true"></span>保存
							</button>
							<!-- <button type="button" class="btn btn-default"
								onclick="back(this)">
								<span class="glyphicon glyphicon-ban-circle" aria-hidden="true"></span>返回
							</button> -->
							<div id='button_div'></div>
						</div>
<br>
						<div class="table-responsive" style="width: 98%;height: 400px;overflow:auto">
							<table class="table" style="margin: 0px auto">
								<!-- <caption>响应式表格布局</caption> -->
								<thead>
									<tr>
										<th>序</th>
										<th>供应商名称</th>
										<th>评价方案名称</th>
										<th>评价标准维度名称</th>
										<th>评分区间</th>
										<th>分数</th>
									</tr>
								</thead>
								<tbody id="tbodyy">
									
								</tbody>
							</table> 
				</div>
			</div>
		</div>
		</div>
	</form>
</body>
	<jsp:include page="../include/public.jsp"></jsp:include>
	<script src="./busJs/SU_EVAL_PLAN_CT_C.js" type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript">
	$(function() {
		//bulidPage(true,true,true,true);
		inins();
		});

		function inins(){
		var jsonData='<%=ParentPKField%>=<%=ParentPKValue%> and <%=ParentPKField1%>=<%=ParentPKValue1%>';
		$.ajax({
		url : "/system/score/score",
		dataType : "json",
		type : "POST",
		data:{"jsonData": jsonData },
		success : function(data) {
			//console.info(data)
			insto(data);
		}
		});

		}
		
	</script>

</html>