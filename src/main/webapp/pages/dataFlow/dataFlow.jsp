<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<%
	String ContextPath = request.getContextPath();
%>
<meta charset="utf-8">
<title>数据流程</title>
<script type="text/javascript"
	src="<%=ContextPath%>/vendor/jquery/jquery.min.js"></script>
<script type="text/javascript"
	src="<%=ContextPath%>/vendor/bootstrap/css/3.3.7/bootstrap.min.js"></script>
<script type="text/javascript"
	src="<%=ContextPath%>/vendor/bootstrap-table/src/bootstrap-table.js"></script>
<link rel="stylesheet" type="text/css"
	href="<%=ContextPath%>/vendor/bootstrap-table/src/bootstrap-table.css">
<link rel="stylesheet" type="text/css"
	href="<%=ContextPath%>/vendor/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css"
	href="<%=ContextPath%>/pages/dataFlow/css/dataFlow_b.css">
<script type="text/javascript"
	src="<%=ContextPath%>/pages/dataFlow/js/dataFlow_b.js"></script>
</head>
<body style="background-color: rgb(240, 240, 241)">
	<div id="bulidTable" style="display: block;">
		<input type="hidden" value="null" id="PARENT_PARTY_ID"> <input
			type="hidden" value="null" id="PARENT_PARTY_CODE">

		<div class="panel panel-primary">
			<div class="panel-heading" id="pageName">数据流程</div>
			<div id="bill_date_and_status"></div>

			<div class="panel-body" id="queryParam" style="display: none;"></div>
						<div class="input-box-toggle" onclick="moreToggle()">
							<span class="caret"></span>更多搜索条件
						</div>
			<div id="button_div" class="button-menu"><div class="button-group sel" buttontoken="query" onclick="queryTable(this)"> <span class="img"></span> <span>查询</span> </div>&nbsp;<div class="button-group ad" buttontoken="add" onclick="tog(this)"> <span class="img"></span> <span>新增</span> </div>&nbsp;<div class="button-group up" buttontoken="update" onclick="updateRow(this)"> <span class="img"></span> <span>修改</span> </div>&nbsp;<div class="button-group de" buttontoken="delete" onclick="deleteRowCheck(this)"> <span class="img"></span> <span>删除</span> </div>&nbsp;</div>
			<!-- 列表页面 -->
			<div>
				<table id="table"></table>
			</div>
		</div>
	</div>

	<div class="panel-body" id="bulidPage" style="display: none;">
		<div class="panel panel-primary">
			<div id="tog_titleName" class="panel-heading">
				<span class="img"></span> <span>修改</span>
			</div>
			<div class="panel-body">
				<form class="form-inline" id="form" action="javaScript:update(this)">
					<div class="alert alert-info" id="message" style="display: none"></div>

					<div>
						<button type="submit" class="btn btn-success"
							>
							<span class="glyphicon glyphicon-saved" aria-hidden="true"></span>保存
						</button>
						<button type="button" class="btn btn-inverse" onclick="back(this)">
							<span class="glyphicon glyphicon-ban-circle" aria-hidden="true"></span>返回
						</button>
					</div>
					<div class="form-group"></div>
					<!-- 维护页面 -->

					<div class="form-group">
						<label for="flow_code">编码</label>
						<input type="text" class="form-control" id="flow_code"
							placeholder="编码" readonly="readonly" data-id="">
					</div>
					<div class="form-group" id="interval">
						<label for="flow_name">名称</label> <input type="text"
							class="form-control" id="flow_name" placeholder="名称"
							required="required">
					</div>
					<div class="form-group">
						<label for="task_type">任务类型</label> <input type="text"
							class="form-control" id="task_type" placeholder="任务类型"
							readonly="readonly">
					</div>
					<br>
					<div class="form-group">
						<label for="sy_regeist_code">&nbsp;系统编码</label> <input type="text"
							class="form-control" id="sy_regeist_code" placeholder="系统编码"
							readonly="readonly" style='margin-left:2px'>
					</div>
					<div class="form-group" id="sy_regeist_name_interval">
						<label for="sy_regeist_name">系统名称</label> <input type="text"
							class="form-control" id="sy_regeist_name" placeholder="系统名称"
							readonly="readonly" style='margin-left:28px'>
					</div>
					<br>
					<button type="button" class="btn btn-info" onclick="enable()">编辑（table
						Status）</button>
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
	<%-- <jsp:include page="../../include/public.jsp"></jsp:include> --%>
</body>
<script type="text/javascript">
var flow_id="";
var code="<%=request.getParameter("pageCode")%>";
	var data_str = [];
	var data_detail = [];
	$(function() {
		initCol(code, null);
		initTab($("#table"), code, null);
	})
	function initTab($e, code, pid) {
		$e.bootstrapTable({
			url : "/system/base?cmd=init&dataSourceCode=" + code, //请求后台的URL（*）
			method : 'get', //请求方式（*）
			toolbar : '#toolbar', //工具按钮用哪个容器
			striped : true, //是否显示行间隔色
			cache : false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination : true, //是否显示分页（*）
			sortable : false, //是否启用排序
			sortOrder : "asc", //排序方式
			//queryParams: oTableInit.queryParams,//传递参数（*）
			sidePagination : "server", //分页方式：client客户端分页，server服务端分页（*）
			pageNumber : 1, //初始化加载第一页，默认第一页
			pageSize : 10, //每页的记录行数（*）
			pageList : [ 10, 25, 50, 100 ], //可供选择的每页的行数（*）
			search : false, //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
			strictSearch : false,
			showColumns : false, //是否显示所有的列
			showRefresh : false, //是否显示刷新按钮
			minimumCountColumns : 2, //最少允许的列数
			clickToSelect : true, //是否启用点击选中行
			//height:526 ,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			uniqueId : "ID", //每一行的唯一标识，一般为主键列
			showToggle : false, //是否显示详细视图和列表视图的切换按钮
			cardView : false, //是否显示详细视图
			detailView : false, //是否显示父子表
			columns : data_str
		});
	}
	function initCol(code, pid) {
		$.ajax({
			async : false,
			type : "post",
			url : "/system/base?cmd=queryColumns&dataSourceCode=" + code,
			dataType : "text",
			success : function(data) {

				data_str.push(JSON.parse(data));
				for (var i = 0; i < data_str.length; i++) {
					data_str[i][i].formatter = "";
				}
			}
		});
	}
	$("#table")
			.on(
					"dblclick",
					"tbody tr",
					function() {
						flow_id = "";
						flow_id = $(this).attr("data-uniqueid");
						$("#flow_code").attr("data-id", flow_id);
						$("#flow_code").val($(this).find("td:eq(2)").text());
						$("#flow_name").val($(this).find("td:eq(3)").text());
						$("#task_type").val($(this).find("td:eq(4)").text());
						$("#sy_regeist_code").val(
								$(this).find("td:eq(7)").text());
						$("#sy_regeist_name").val(
								$(this).find("td:eq(8)").text());

						$("#bulidPage").slideDown();
						$("#bulidTable").slideUp();
						data_str.splice(0, data_str.length);
						data_detail.splice(0, data_detail.length);
						flow_detail("BS_DATA_FLOW_B", flow_id);
						$("#thead_flow_b").html("");
						$("#thead_flow_b").html(
								"<tr style='background-color:#d9edf7;'>"
										+ "<td>序号</td>" + "<td>编码</td>"
										+ "<td>名称</td>" + "<td>流程类型</td>"
										+ "<td>数据编码</td>" + "<td>数据名称</td>"
										+ "<td>操作</td>" + "</tr>");
						var str = "";
						setTimeout(
								function() {
									for(var i=0;i<data_detail[0].rows.length-1;i++){
										for(var j=0;j<data_detail[0].rows.length-1-i;j++){
											var temp;
											if(data_detail[0].rows[j].FLOW_SORT>data_detail[0].rows[j+1].FLOW_SORT){
												temp=data_detail[0].rows[j];
												data_detail[0].rows[j]=data_detail[0].rows[j+1];
												data_detail[0].rows[j+1]=temp
											}
										}
									}
									for (var i = 0; i <data_detail[0].rows.length; i++) {
										str += "<tr data-id='"+data_detail[0].rows[i].ID+"'>"
												+ "<td>"
												+ (i + 1)
												+ "</td>"
												+ "<td><input type='text' value='"+data_detail[0].rows[i].FLOW_CODE+"' style='border:0px;background-color:transparent' class='Enable' readonly='readonly' required='required'></td>"
												+ "<td><input type='text' value='"+data_detail[0].rows[i].FLOW_NAME+"'  style='border:0px;background-color:transparent' class='Enable' readonly='readonly' required='required'></td>"
												+ "<td><input type='number' value='"+data_detail[0].rows[i].FLOW_TYPE+"' style='border:0px;background-color:transparent' class='Enable' readonly='readonly' required='required'></td>"
												+ "<td da_id='"+data_detail[0].rows[i].DA_ID+"'><input type='text' value='"+data_detail[0].rows[i].DA_CODE+"' style='border:0px;background-color:transparent' class='Enable' readonly='readonly' required='required'></td>"
												+ "<td><input type='text' value='"+data_detail[0].rows[i].DA_NAME+"' style='border:0px;background-color:transparent' class='Enable' readonly='readonly' required='required'></td>"
												+ "<td>"
												+ "<a href='#'><span class='glyphicon glyphicon-plus' onclick='insertRow(this);'></span></a>"
												+ "<a href='#' style='margin:0px 6px'><span class='glyphicon glyphicon-trash' onclick='delRow(this);'></span></a>"
												+ "<a href='#' style='margin:0px 6px 0px 6px'><span class='glyphicon glyphicon-menu-up' onclick='preRow(this);'></span></a>"
												+ "<a href='#'><span class='glyphicon glyphicon-menu-down' onclick='nextRow(this);'></span></a>"
												+ "</td>" + "</tr>";
									}
									$("#tbody_flow_b").html(str);
									
								}, 1000);
					});
	function flow_detail(code, id) {
		$.ajax({
			async : false,
			type : "get",
			url : "/system/base?cmd=init&dataSourceCode=" + code
					+ "&pageParam=@P_ID=" + id + "@",
			dataType : "text",
			success : function(data) {
				data_detail.push(JSON.parse(data));
			}
		});
	}
	function back(temp) {
		$("#bulidPage").slideUp();
		$("#bulidTable").slideDown(1000);
		initCol(code, null);
		initTab($("#table"), code, null);
		$("#tbody_flow_b").html("");
	}
</script>
</html>