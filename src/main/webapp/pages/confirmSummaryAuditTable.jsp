<%@page import="com.yonyou.util.theme.ThemePath"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<%
	String ContextPath =request.getContextPath();
	String tableName = request.getParameter("pageCode");
%>

<html lang="zh-cn">
	<head>
		<meta charset="utf-8">
		<title>子表模板</title>
	</head>
	<style>
		label{
			width:100%;
			text-align:right;
		}
	</style>
	<body style="background-color: rgb(240,240,241)">
		<form >
			<div  id="bulidTable">
				<div class="panel panel-primary">
					<div class="panel-heading" id ='pageName'>
						<%
							if("SUMMARY_AUDIT_TABLE".equals(tableName)){
						%>
						计收汇总审批统计表
						<%
							}else{
						%>
						调整收入统计表
						<%
							}
						%>
					</div>
					<div id="bill_date_and_status"></div>
					<div class="panel-body" id="queryParam" style="display: none;">
						<div class="col-md-4" style="margin-top:10px;display:inline">
							<div class="form-group">
								<div class="col-md-4">
									<label for="CLIENT_NAME">客户名称：</label>
								</div>
								<div class="col-md-8">
									<input type="TEXT" class="form-control" id="CLIENT_NAME">
								</div>
							</div>
						</div>
						<div class="col-md-4" style="margin-top:10px;display:inline">
							<div class="form-group">
								<div class="col-md-4">
									<label for="INCOME_OF_DEPT">部门：</label>
								</div>
								<div class="col-md-8">
									<input type="TEXT" class="form-control" id="INCOME_OF_DEPT">
								</div>
							</div>
						</div>
						<div class="col-md-4" style="margin-top:10px;display:inline">
							<div class="form-group">
								<div class="col-md-4">
									<label for="BUSINESS_TYPE">业务类型：</label>
								</div>
								<div class="col-md-8">
									<input type="TEXT" class="form-control" id="BUSINESS_TYPE">
								</div>
							</div>
						</div>
						<div class="col-md-4" style="margin-top:10px;display:inline">
							<div class="form-group">
								<div class="col-md-4">
									<label for="PRODUCT_TYPE">产品：</label>
								</div>
								<div class="col-md-8">
									<input type="TEXT" class="form-control" id="PRODUCT_TYPE">
								</div>
							</div>
						</div>
						<div class="panel-body">
							<div class="col-md-4" style="margin-top:10px;display:inline">
								<div class="form-group has-feedback">
									<div class="col-md-4">
										<label for="TERM">账期：</label>
									</div>
									<div class="col-md-8">
										<div class="input-group date form_date" style="width:45%;float:left" data-date="" data-link-field="TERM" data-link-format="yyyy-mm">
											<input type="TEXT" class="form-control" id="TERM" readonly="readonly" data-bv-field="TERM">
											<span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
										</div>
										<div class="input-group" style="width:5%;float:left;">
											<label style="min-width:20px !important;">&nbsp;到&nbsp;</label>
										 </div>
										<div class="input-group date form_date" style="width:45%;float:left" data-date="" data-link-field="TERM1" data-link-format="yyyy-mm">
											<input type="TEXT" class="form-control" id="TERM1" readonly="readonly" data-bv-field="TERM1">
											<span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
										</div>
									</div>
									
								</div>
							</div>
						</div>
						<div class="col-md-12"><br></div>
						<div class="col-md-8"></div>
						<div class="col-md-4" align="right" style="display: inline;"><div id="selectButtonPage"></div></div>
					</div>
					<div class="input-box-toggle" onclick="moreToggle()">
						<span class="caret"></span>更多搜索条件
					</div>
					<!-- 列表页面 -->
					<div id='button_div' class="button-menu" style="border:none;">
						<div class="button-group sel" buttontoken="query" onclick="queryTable()"> <span class="img"></span> <span>查询</span> </div>
					</div>
				
					<table id="table" data-row-style="rowStyle">
						<thead>
							<th data-field="RMRN" data-halign="center" data-align="center">序</th>
							<th data-field="INCOME_OF_DEPT" data-halign="center" data-align="center">部门</th>
							<th data-field="CLIENT_NAME" data-halign="center" data-align="center">客户名称</th>
							<th data-field="BUS_NUMBER" data-halign="center" data-align="center">商机编号</th>
							<th data-field="PROJECT_NAME" data-halign="center" data-align="center">项目名称</th>
							<th data-field="MONTH" data-halign="center" data-align="center">计收填报月份</th>
							<th data-field="BUSINESS_TYPE" data-halign="center" data-align="center">业务类型</th>
							<th data-field="PRODUCT_TYPE" data-halign="center" data-align="center">产品</th>
							<th data-field="MARKET_TYPE" data-halign="center" data-align="center">市场类型</th>
							<th data-field="CONTRACT_NAME" data-halign="center" data-align="center">合同名称</th>
							<th data-field="SETTLE_METHOD" data-halign="center" data-align="center">收费结算方式</th>
							<th data-field="SETTILE_RATIO" data-halign="center" data-align="center">结算比例(百分比)</th>
							<th data-field="CONTRACT_TAX" data-halign="center" data-align="center">税率(百分比)</th>
							<th data-field="ESTIMATED_INCOME" data-halign="center" data-align="center">暂估收入(万元)</th>
							<th data-field="CONFIRM_INCOME" data-halign="center" data-align="center">确认收入(万元)</th>
							<th data-field="ADJUST_INCOME" data-halign="center" data-align="center">调整收入(万元)</th>
							<th data-field="ACCOUNTS_INCOME" data-halign="center" data-align="center">账面收入(万元)</th>
							<%
								if("ADJUST_INCOME_STATISTICS".equals(tableName)){
							%>
							<th data-field="OLD_INCOME" data-halign="center" data-align="center">调整前确认收入（元）</th>
							<th data-field="NEW_INCOME" data-halign="center" data-align="center">调整后确认收入（元）</th>
							<th data-field="ADJUST_INCOME1" data-halign="center" data-align="center">收入调整差额（元）</th>
							<th data-field="OLD_MONTH" data-halign="center" data-align="center">调整前实际发生月份</th>
							<th data-field="NEW_MONTH" data-halign="center" data-align="center">调整后实际发生月份</th>
							<th data-field="ADJUST_REASON" data-halign="center" data-align="center">调整原因</th>
							<% } %>
						</thead>
					</table>	
				</div>
			</div>
			<input type="hidden" id="isNewStyle" value="1" />
		</form>
	</body>
	<jsp:include page="../include/public.jsp"></jsp:include>
	<jsp:include page="buttonjs.jsp"></jsp:include>
	<link rel="stylesheet" type="text/css" href="<%= ThemePath.findPath(request, ThemePath.SUB_SYSTEM_CSS)%>">
	<script type="text/javascript">
		var tableName = '<%=tableName%>';
		$(function() {
			//bulidPage(true,true,true,true);
			$("#queryParam .control-label:last").css("display","none");
			$('.form_date').datetimepicker('remove');//去掉时间插件
			$('.form_date').datetimepicker({//月视图
	    		format: 'yyyy-mm',
		        autoclose: true,
		        todayBtn: true,
		        startView: 'year',
		        minView: 'year',
		        maxView: 'decade',
		        language: 'zh-CN',
		        pickerPosition:'bottom-right'
	    	});
	    	
	    	generate_table();
	    	
		});
		
		
		function generate_table(){
			var estimatedIncomeSum = "";
	    	var confirmIncomeSum = "";
	    	var adjustIncomeSum = "";
	    	var accountsIncomeSum = "";
	    	var oldIncome = "";
	    	var newIncome = "";
	    	var adjustIncome1 = "";
	    	
			$('#table').bootstrapTable({
				url : "/system/collect/getConfirmSummaryAuditList"+param(),
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
				onLoadSuccess: function() { //当所有数据被加载时触发
					//debugger;
					var data = $('#table').bootstrapTable('getData');
					$.each(data, function (i){
						estimatedIncomeSum = Number(estimatedIncomeSum) + Number(data[i].ESTIMATED_INCOME);
						confirmIncomeSum = Number(confirmIncomeSum) + Number(data[i].CONFIRM_INCOME);
						adjustIncomeSum = Number(adjustIncomeSum) + Number(data[i].ADJUST_INCOME);
						accountsIncomeSum = Number(accountsIncomeSum) + Number(data[i].ACCOUNTS_INCOME);
						if(tableName=="ADJUST_INCOME_STATISTICS"){
							oldIncome = Number(oldIncome) + Number(data[i].OLD_INCOME);
							newIncome = Number(newIncome) + Number(data[i].NEW_INCOME);
							adjustIncome1 = Number(adjustIncome1) + Number(data[i].ADJUST_INCOME1);
						}
					});
					if(data.length!=0){
						var Json={};
						Json.RMRN="合计";
						Json.INCOME_OF_DEPT="";
						Json.CLIENT_NAME="";
						Json.BUS_NUMBER="";
						Json.PROJECT_NAME="";
						Json.MONTH="";
						Json.BUSINESS_TYPE="";
						Json.PRODUCT_TYPE="";
						Json.MARKET_TYPE="";
						Json.CONTRACT_NAME="";
						Json.SETTLE_METHOD="";
						Json.SETTILE_RATIO="";
						Json.CONTRACT_TAX="";
						Json.ESTIMATED_INCOME=estimatedIncomeSum;
						Json.CONFIRM_INCOME=confirmIncomeSum;
						Json.ADJUST_INCOME=adjustIncomeSum;
						Json.ACCOUNTS_INCOME=accountsIncomeSum;
						if(tableName=="ADJUST_INCOME_STATISTICS"){
							Json.OLD_INCOME=oldIncome;
							Json.NEW_INCOME=newIncome;
							Json.ADJUST_INCOME1=adjustIncome1;
							Json.OLD_MONTH="";
							Json.NEW_MONTH="";
							Json.ADJUST_REASON="";
						}
						$('#table').bootstrapTable('append', Json);
					}
				}
			});
		}
		
		//查询函数
		function queryTable(){
			$('#table').bootstrapTable('destroy');
			generate_table();
		}
		
		//传参数
		function param(){
			var term = $("#TERM").val();//账期开始时间
			var term1 = $("#TERM1").val();//账期结束时间
			var clientName = $("#CLIENT_NAME").val();//客户名称
			var incomeOfDept = $("#INCOME_OF_DEPT").val();//部门
			var businessType = $("#BUSINESS_TYPE").val();//业务类型
			var productType = $("#PRODUCT_TYPE").val();//产品
			var param = "?TABLE_NAME="+tableName+"&TERM="+term+"&TERM1="+term1+"&CLIENT_NAME="+clientName+"&INCOME_OF_DEPT="+incomeOfDept+"&BUSINESS_TYPE="+businessType+"&PRODUCT_TYPE="+productType;
			return param;
		}
		
	</script>
</html>