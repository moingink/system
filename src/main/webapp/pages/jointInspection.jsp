<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>客户回款认领联查</title>
</head>

<body style="background-color: rgb(240, 240, 241)">
	<div class="panel panel-primary">
		<div class="panel-body">
			<button type="button" class="btn btn-inverse" onclick="javascript:history.back(-1);">
				<span class="glyphicon glyphicon-log-out" aria-hidden="true"></span>&nbsp;返回
			</button>
		</div>
	</div>
	<div class="panel panel-primary">
		<div class="panel-heading">确认收入明细</div>
		<table id="table1" data-row-style="rowStyle"></table>
	</div>
	<div class="panel panel-primary">
		<div class="panel-heading">发票明细</div>
		<table id="table2" data-row-style="rowStyle">
			<thead>
				<th data-field="RMRN" data-halign="center" data-align="center">序</th>
				<th data-field="BILL_NO" data-halign="center" data-align="center">发票申请单号</th>
				<th data-field="APPLY_DATE" data-halign="center" data-align="center">申请日期</th>
				<th data-field="DEPT" data-halign="center" data-align="center">部门</th>
				<th data-field="CREATOR_NAME" data-halign="center" data-align="center">申请人</th>
				<th data-field="CONTRACT_NAME" data-halign="center" data-align="center">合同名称</th>
				<th data-field="CONFIRM_AMOUNT" data-halign="center" data-align="center">确认收入金额（元）</th>
				<th data-field="APPLY_AMOUNT" data-halign="center" data-align="center">发票申请金额（元）</th>
				<th data-field="MAX_APPLY_AMOUNT" data-halign="center" data-align="center">发票申请余额（元）</th>
				<th data-field="OPEN_AMOUNT" data-halign="center" data-align="center">已开票金额（元）</th>
			</thead>
		</table>
	</div>
</body>

<jsp:include page="../include/public.jsp"></jsp:include>

<script type="text/javascript">
	$(function() {
		bulidListPage($("#table1"),'VIEW_CONFIRM_AUDIT_CHARGE_REF',pageParamFormat("RECEIVED_PAYMENT_ID = <%=request.getParameter("ID")%>"));
		var pid = '<%=request.getParameter("ID")%>';
		var confirm_amount = "";
		var apply_amount = "";
		var open_amount = "";
		var max_apply_amount = "";
		$('#table2').bootstrapTable({
			url : '/system/finance/getInvoiceDetail?Pid='+pid,
			method : 'get',
			dataType: "json",
			toolbar: "#toolbar",
	        cache:false, //默认缓存ajax请求，设为false则禁用缓存
	        striped: false, // 是否显示斑马条
	        pagination: false, //是否启用分页
	        pageSize: 10, //设置每页初始显示条数
	        pageList: [10, 25, 50, 100], //设置选择每页显示条数
	        height : 500, //表格的高度,
	        clickToSelect: true, //是否启用点击选中行
	        formatLoadingMessage: function () {  
				return "请稍等，正在加载中...";  
			}, 
	        formatNoMatches: function () {
				return '无符合条件的记录';  
			},
			onLoadSuccess: function(data) { //当所有数据被加载时触发
				var data = $('#table2').bootstrapTable('getData');
				$.each(data, function (i){
					confirm_amount = Number(confirm_amount) + Number(data[i].CONFIRM_AMOUNT);
					apply_amount = Number(apply_amount) + Number(data[i].APPLY_AMOUNT);
					open_amount = Number(open_amount) + Number(data[i].OPEN_AMOUNT);
					max_apply_amount = Number(max_apply_amount) + Number(data[i].MAX_APPLY_AMOUNT);
				})
				var Json={};
				Json.BILL_NO="";
				Json.APPLY_DATE="";
				Json.DEPT="";
				Json.CREATOR_NAME="";
				Json.CONTRACT_NAME="";
				Json.CONFIRM_AMOUNT=confirm_amount;
				Json.APPLY_AMOUNT=apply_amount;
				Json.MAX_APPLY_AMOUNT=max_apply_amount;
				Json.OPEN_AMOUNT=open_amount;
				$('#table2').bootstrapTable('append', Json);
				//将最后一行序号改名为合计
				$("#table2 tbody tr:last td:eq(0)").text('合计');
			}
		})
	});
	
	var confirmIncome = "";
	var claimBalance = "";
	//数据加载成功事件
	$('#table1').on('load-success.bs.table', function (e) {
		var data = $('#table1').bootstrapTable('getData');
		$.each(data, function (i){
			confirmIncome = Number(confirmIncome) + Number(data[i].CONFIRM_INCOME);
			claimBalance = Number(claimBalance) + Number(data[i].CLAIM_BALANCE);
		})
		var Json={};
		Json.BILL_NO="";
		Json.CONFIRM_DATE="";
		Json.DEPT="";
		Json.CLIENT_MANAGER="";
		Json.CONFIRM_INCOME=confirmIncome;
		Json.CLAIM_BALANCE=claimBalance;
		$('#table1').bootstrapTable('append', Json);
		//设置复选框不显示
		$(".bs-checkbox").each(function(){
			$(this).css("display","none");
		});
		//将最后一行序号改名为合计
		$("#table1 tbody tr:last td:eq(1)").text('合计');
	});
</script>

</html>