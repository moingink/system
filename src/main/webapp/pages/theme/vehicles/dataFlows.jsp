<%@page import="com.yonyou.util.theme.ThemePath"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<%
	String ContextPath =request.getContextPath();
%>

<html lang="zh-cn">
	<head>
		<meta charset="utf-8">
		<title>单表模板</title>
		<%-- <script type="text/javascript" src="<%=request.getContextPath() %>/vendor/bootstrap3-editable/js/bootstrap-editable.min.js"></script>
		<link href="<%=request.getContextPath() %>/vendor/bootstrap3-editable/css/bootstrap-editable.css"> --%>
		<link rel="stylesheet" type="text/css"
	href="<%=ContextPath%>/pages/theme/vehicles/css/dataFlow_b.css">
	</head>
	<body style="background-color: rgb(240,240,241)">

		<form >
				
				<div  id="bulidTable">
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
						<table id="table" data-row-style="rowStyle">
							
						</table>
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
						<button type="button" class="btn btn-inverse" onclick="backs(this)">
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
					<button type="button" class="btn btn-warning">数据预警</button>
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
		
		
		
		<div class="modal fade in" id="ReferenceModal_detail" aria-hidden="false"
		style="padding-left: 17px;">
		<div class="modal-dialog" style="width: 90%; height: 80%">
			<div class="modal-content" id="show-list">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h4 class="modal-title" id="ReferenceModalLabel">参照</h4>
				</div>
				<div id="ReferenceModal_body" class="modal-body"
					style="height: auto">

					<form>

						<div class="panel-bodymodel">
							<div class="panel panel-default">
								<div class="panel-heading">查询条件</div>
								<div class="panel-body" id="reference_query_param">
									<div class="col-md-4 col-xs-4 col-sm-4"
										style="margin-top: 10px; display: inline">
										<div class="form-group">
											<div class="col-md-2 col-xs-2 col-sm-2"
												style="text-align: right;">
												<label class="control-label" style="line-height: 29px;"
													data-toggle="tooltip" title="名称" for="name_flow_b">名称</label>
											</div>
											<div class="col-md-8 col-xs-8 col-sm-8">
												<input type="TEXT" class="form-control" id="name_flow_b"
													name="name_flow_b"
												    autocomplete="off" value=""
													data-toggle="tooltip" placeholder="名称" title="名称">
											</div>
											<div class="col-md-2 col-xs-2 col-sm-2">
												<button type="button" onclick="search()" class="btn btn-success" style="float:left">搜索</button>
											</div>
										</div>
										<br>
									</div>
								</div>
							</div>
							<div class="clearfix"></div>
							<table id="flow_b_table"></table>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-inverse" data-dismiss="modal">关
						闭</button>
				</div>
			</div>
		</div>
	</div>
		
		
		
	</body>
	<jsp:include page="../../../include/public.jsp"></jsp:include>
	<jsp:include page="../../buttonjs.jsp"></jsp:include>
	<script type="text/javascript"
	src="<%=ContextPath%>/pages/theme/vehicles/js/dataFlow_b.js"></script>
	<link rel="stylesheet" type="text/css" href="<%= ThemePath.findPath(request, ThemePath.SUB_SYSTEM_CSS)%>">
	<script type="text/javascript">
		var current_row="";
		var flow_id="";
		var data_str = [];
		var data_detail = [];
		var totalCode = '<%=request.getParameter("totalCode")%>';
		$(function() {
				bulidPage(true,true,true,true);
		});
		$("#table")
		.on(
				"dblclick",
				"tbody tr",
				function() {
					$("#ViewModal_dbclick").modal("hide");
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

					$("#bulidPage_detail").slideDown(1100);
					$("#bulidTable").slideUp();
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
					if(data_detail[0].rows.length<1){
						return;	
					}
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
											+ "<td><input type='text' value='"+data_detail[0].rows[i].FLOW_CODE+"' style='border:0px;background-color:transparent' class='Enable' readonly='readonly' required='required' onclick='focusInput(this)'></td>"
											+ "<td><input type='text' value='"+data_detail[0].rows[i].FLOW_NAME+"'  style='border:0px;background-color:transparent' class='Enable' readonly='readonly' required='required' onclick='focusInput(this)'></td>"
											+ "<td><input type='number' value='"+data_detail[0].rows[i].FLOW_TYPE+"' style='border:0px;background-color:transparent' class='Enable' readonly='readonly' required='required' onclick='focusInput(this)'></td>"
											+ "<td da_id='"+data_detail[0].rows[i].DA_ID+"'><input type='text' value='"+data_detail[0].rows[i].DA_CODE+"' style='border:0px;background-color:transparent' class='Enable' readonly='readonly' required='required' onclick='focusInput(this)'></td>"
											+ "<td><input type='text' value='"+data_detail[0].rows[i].DA_NAME+"' style='border:0px;background-color:transparent' class='Enable' readonly='readonly' required='required' onclick='focusInput(this)'></td>"
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
		dataType : "json",
		success : function(data) {
			if(data.rows.length==0){
				$("#tbody_flow_b").html("<tr data-id=''>"+
						"<td>"+1+"</td>"+
						"<td><input type='text' value='' style='border:0px;background-color:transparent' class='Enable' required='required' onclick='focusInput(this)'></td>"+
						"<td><input type='text' value='' style='border:0px;background-color:transparent' class='Enable' required='required' onclick='focusInput(this)'></td>"+
						"<td><input type='number' value='' style='border:0px;background-color:transparent' class='Enable' required='required' onclick='focusInput(this)'></td>"+
						"<td da_id=''><input type='text' value='' style='border:0px;background-color:transparent' class='Enable' required='required' onclick='focusInput(this)'></td>"+
						"<td><input type='text' value='' style='border:0px;background-color:transparent' class='Enable' required='required' onclick='focusInput(this)'></td>"+
						"<td>"+
							 "<a href='#'><span class='glyphicon glyphicon-plus' onclick='insertRow(this);'></span></a>"+
							 "<a href='#' style='margin:0px 6px'><span class='glyphicon glyphicon-trash' onclick='delRow(this);'></span></a>"+
							 "<a href='#' style='margin:0px 6px 0px 6px'><span class='glyphicon glyphicon-menu-up' onclick='preRow(this);'></span></a>"+
							 "<a href='#'><span class='glyphicon glyphicon-menu-down' onclick='nextRow(this);'></span></a>"+
						"</td>"+
					"</tr>");
			}
			data_detail.push(data);
		}
	});
}
$("#flow_b_table").on("dblclick","tbody tr",function(){
	var id=$(this).children("td:eq(2)").html();
	var code=$(this).children("td:eq(3)").html();
	var name=$(this).children("td:eq(4)").html();
	$(current_row).children("td:eq(4)").attr("da_id",id);
	$(current_row).children("td:eq(4)").find("input").val(code);
	$(current_row).children("td:eq(5)").find("input").val(name);
	$("#ReferenceModal_detail").modal("hide");
});
function backs(temp) {
	$("#bulidPage_detail").slideUp();
	$("#bulidTable").slideDown(1000);
	$("#tbody_flow_b").html("");
}
	</script>
</html>
