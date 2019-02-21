<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<title>子表模板</title>
</head>
<%
	String ContextPath = request.getContextPath();
%>
<body>
	<form class="form-horizontal" id="form">
		<div class="panel panel-primary">
			<div id="tog_titleName" class="panel-heading">
				<span>审批</span>
			</div>
			<div class="panel-body" id="bulidTable">
				<div class="col-md-4" style="margin-top: 10px; display: inline;margin-left: 10%;">
					<div class="form-group has-feedback">
						<div class="col-md-4" style="white-space: nowrap;">
							<label class="control-label" style="text-align: left; line-height: 29px;" for="APPLY_DATE">申请日期：</label>
						</div>
						<div class="col-md-8">
							<input type="TEXT" class="form-control" id="APPLY_DATE" name="APPLY_DATE" value="" data-bv-field="APPLY_DATE" disabled="disabled">
						</div>
					</div>
				</div>
				<div class="col-md-4" style="margin-top: 10px; display: inline;margin-left: 10%;">
					<div class="form-group has-feedback">
						<div class="col-md-4" style="white-space: nowrap;">
							<label class="control-label" style="text-align: left; line-height: 29px;" for="DEPT">部门：</label>
						</div>
						<div class="col-md-8">
							<input type="TEXT" class="form-control" id="DEPT" name="DEPT" value="" data-bv-field="DEPT" disabled="disabled">
						</div>
					</div>
				</div>
				<div class="col-md-4" style="margin-top: 10px; display: inline;margin-left: 10%;">
					<div class="form-group has-feedback">
						<div class="col-md-4" style="white-space: nowrap;">
							<label class="control-label" style="text-align: left; line-height: 29px;" for="CLIENT_MANAGER">客户经理：</label>
						</div>
						<div class="col-md-8">
							<input type="TEXT" class="form-control" id="CLIENT_MANAGER" name="CLIENT_MANAGER" value="" data-bv-field="CLIENT_MANAGER" disabled="disabled">
						</div>
					</div>
				</div>
				<div class="col-md-4" style="margin-top: 10px; display: inline;margin-left: 10%;">
					<div class="form-group has-feedback">
						<div class="col-md-4" style="white-space: nowrap;">
							<label class="control-label" style="text-align: left; line-height: 29px;" for="APPLY_OPEN_BUSINESS">申请开通业务：</label>
						</div>
						<div class="col-md-8">
							<select class="form-control" id="APPLY_OPEN_BUSINESS" name="APPLY_OPEN_BUSINESS" data-bv-field="APPLY_OPEN_BUSINESS" disabled="disabled">
								<option selected="selected" value="">==请选择==</option>
								<option value="1">暂估收入</option>
								<option value="2">确认收入</option>
							</select>
						</div>
					</div>
				</div>
				<div class="col-md-12">
					<div class="form-group has-feedback">
						<div class="col-md-2" style="white-space: nowrap;">
							<label class="control-label" style="text-align: left; line-height: 90px; margin-left: 80px;" for="APPLY_REASON" >申请理由：</label>
						</div>
						<div class="col-md-10" style="margin-left: -6px; margin-top: 10px">
							<textarea class=" form-control" id="APPLY_REASON" name="APPLY_REASON" rows="3" data-bv-field="APPLY_REASON" disabled="disabled"></textarea>
						</div>
					</div>
				</div>

				<div class="col-md-4" style="margin-top: 10px; display: inline;margin-left: 10%;">
					<div class="form-group has-feedback">
						<div class="col-md-4" style="white-space: nowrap;">
							<label class="control-label" style="text-align: left; line-height: 29px;" for="OPEN_TERM">开通期限：</label>
						</div>
						<div class="col-md-6" style="width: 239px;">
							<input type="TEXT" class="form-control" id="OPEN_TERM" name="OPEN_TERM" value="" data-bv-field="OPEN_TERM" readonly="readonly">
						</div>
					</div>
				</div>
				
				<div class="col-md-4" style="margin-top: 10px; display: inline; margin-left: 40%;">
					<div id="toolbar">
						<button type="button" class="btn btn-primary" id="makesure" onclick="agree()">
							<span class="glyphicon glyphicon" aria-hidden="true"></span>通过
						</button>
						<button type="button" class="btn btn-danger" style="margin-left: 30px;" onclick="disagree()">
							<span class="glyphicon glyphicon" aria-hidden="true"></span>不通过
						</button>
					</div>
				</div>
			</div>
		</div>
		<input type="hidden" class="form-control" id="CLIENT_MANAGER_ID" name="CLIENT_MANAGER_ID" value="" data-bv-field="CLIENT_MANAGER_ID">
		<input type="hidden" id="ID" value=""> 
		<input type="hidden" id="APPLY_STATE" name="APPLY_STATE" value="">
		<input type="hidden" id="DR" name="DR" value="">
	</form>
</body>
<jsp:include page="../include/public.jsp"></jsp:include>

<script type="text/javascript">
	$(function() {
	var id = '<%=request.getParameter("id")%>';
		$.ajax({
			type : "GET",
			url :  context+"/finance?getConfirmBusinessOpen",
			data : {'id':id},
			dataType : "json",
			async : false,
			cache : false,
			success : function(data) {
				$('#form').find("[id]").each(function() {
					$(this).val(data[0][$(this).attr('id')]);
				});
			},
			error : function(data) {
				oTable.showModal('提示', "获取计收业务开通申请数据失败！");
			}
		});		
		$("#OPEN_TERM").datetimepicker({
		     language:  'zh-CN',
		     format: 'yyyy-mm-dd hh:ii',
		     autoclose: true,
		     minView: 0
	   	});
	});
	
	function commitData(){
		var openTerm = $("#OPEN_TERM").val();
		var applyState = $("#APPLY_STATE").val();
		var id = $("#ID").val();
		if(openTerm == ""){
			oTable.showModal('提示', "请选择开通期限！");
			return;
		}
		$.ajax({
			type : "POST",
			url :  "/system/finance?auditConfirmBusinessOpen&openTerm="+openTerm+"&applyState="+applyState+"&id="+id,
			data : {},
			dataType : "json",
			async : false,
			cache : false,
			contentType : false,
			processData : false,
			success : function(data) {
				if(data == "1"){
					oTable.showModal('提示', "审批成功！");
					window.history.back(-1);
				}
			},
			error : function(data) {
				oTable.showModal('提示', "审批失败！");
			}
		});		
	}
	
	//通过
	function agree(){
		$("#APPLY_STATE").val("3");//审核完成
		commitData();
	}
	
	//不通过
	function disagree(){
		$("#APPLY_STATE").val("7");//审核退回
		commitData();
	}
</script>
</html>