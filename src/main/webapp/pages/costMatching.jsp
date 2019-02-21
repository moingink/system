<%@page import="com.yonyou.business.entity.TokenUtil"%>
<%@page import="com.yonyou.business.entity.TokenEntity"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<title>事业部多维度成本匹配</title>
<link rel="stylesheet" href="../vendor/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="../vendor/bootstrap-table/src/bootstrap-table.css">
<link rel="stylesheet" href="../vendor/bootstrap/css/bootstrap-datetimepicker.min.css" media="screen">
<link rel="stylesheet" href="../vendor/bootstrap-fileinput/css/fileinput.min.css"  media="all">
<script src="../vendor/jquery/jquery.min.js"></script>
<script src="../vendor/bootstrap/js/bootstrap.min.js"></script>
<script src="../vendor/bootstrap-table/src/bootstrap-table.js"></script>
<script src="../vendor/bootstrap-fileinput/js/fileinput.js"></script>
<script src="../vendor/bootstrap-fileinput/js/locales/zh.js"></script>
<script src="../vendor/bootstrap/js/bootstrap-datetimepicker.js" charset="UTF-8"></script>
<script src="../vendor/bootstrap/js/locales/bootstrap-datetimepicker.zh-CN.js" charset="UTF-8"></script>
<script src="../pages/js/reference.js"></script>
<script src="../js/common.js"></script>
<script src="../js/public.js"></script>
<script src="../js/bootTables.js"></script>
<link rel="stylesheet" type="text/css" href="../css/css01.css">
<jsp:include page="buttonjs.jsp"></jsp:include>
</head>
	<body>
		<form class="form-horizontal">
			<div class="panel panel-primary">
				
				<div class="panel-body" id="bulidTable">
					<div class="panel panel-primary">
						<div class="panel-heading">
							事业部多维度成本匹配
						</div>
						
						<div class="panel-body">
							<div class="panel panel-default">
								<div class="panel-heading">
									查询条件
								</div>
								<div class="panel-body" id="queryParam" style="display: none;"></div>
								<div class="input-box-toggle" onclick="moreToggle()">
									<span class="caret"></span>更多搜索条件
								</div>
							</div>
							
							<div class="panel" id="operate" style="margin-top:10px;margin-bottom:10px;padding-left:8px;">
								<button type="button" class="btn btn-primary" onclick="queryTable()" buttontoken="query">
									<span class="glyphicon" aria-hidden="true">查询
								</button>
								<button type="button" class="btn btn-primary" onclick="autoMatch()" buttontoken="autoMatch">
									<span class="glyphicon" aria-hidden="true">自动匹配
								</button>
								<button type="button" class="btn btn-primary" onclick="revokeMatch()" buttontoken="revokeMatch">
									<span class="glyphicon" aria-hidden="true">撤销匹配
								</button>
								<button type="button" class="btn btn-primary" onclick="manualMatch()" buttontoken="manualMatch">
									<span class="glyphicon" aria-hidden="true">手工匹配
								</button>
								<button type="button" class="btn btn-primary" onclick="style_switch()" buttontoken="styleSwitch">
									<span class="glyphicon" aria-hidden="true">布局切换
								</button>
							</div>
								
							<div id="in"></div>
							<div id="up"></div>
							
							<div id="style_inup">			
								<div class="col-md-6" id="left">
									<div class="panel panel-default" id='i_table_div'>
										<div class="panel-heading">
											多维度报账单数据
										</div>
										<table id="leftTable" data-row-style="rowStyle"></table>
									</div>
								</div>
								<div class="col-md-6" id="rigth">
									<div class="panel panel-default" id='l_table_div'>
										<div class="panel-heading">
											项目成本明细数据
										</div>
										<table id="rightTable" data-row-style="rowStyle"  ></table>
									</div>
								</div>	
							</div>
						</div>
					</div>
				</div>
				
			</div>
		</form>
		<!-- 是否为修改页面 -->
		<input type="hidden" id="is_edit"/>
		<% 
			TokenEntity tokenEntity = TokenUtil.initTokenEntity(request);
		%>
		<script>
			var token = '<%=request.getParameter("token")!=null?request.getParameter("token"):""%>';
			var context = '<%=request.getContextPath()%>';
			
			var $leftTable = $('#leftTable');
			var $rightTable = $('#rightTable');
			var leftoTable = new TablesInit($leftTable);
			var rightoTable = new TablesInit($rightTable);
			var $queryParam = $("#queryParam");
			var $inspage = $("#insPage");
			
			var paramurl = context+"/base?cmd=queryParam";
			var colurl = context+'/base?cmd=queryColumns';
			var qusurl = context+'/base?cmd=init';
			var checkurl = context+'/base?cmd=check';
			
			var leftDataSourceCode = '&dataSourceCode=VIEW_DOUBLE_IMPORT_MATCH';//多维度报账单
			var rightDataSourceCode = '&dataSourceCode=FIN_BUSINESS_IMPORT';//项目成本明细
			
			var matchUrl = context+'/buttonBase?cmd=button&buttonToken=match&token='+token;

			$(function() {
				bulidTable();
			});
			
			function bulidTable() {
				//公共查询条件，在左表数据源中配置
				leftoTable.initQueryParam($queryParam,paramurl + leftDataSourceCode);
				leftoTable.initCols(colurl + leftDataSourceCode, qusurl + leftDataSourceCode);
				rightoTable.initCols(colurl + rightDataSourceCode, qusurl + rightDataSourceCode);
			}

			//查询
			function queryTable() {
				leftoTable.queryTable(qusurl + leftDataSourceCode + findPageParamByDataSourceCode($queryParam,"VIEW_DOUBLE_IMPORT_MATCH"));
				rightoTable.queryTable(qusurl + rightDataSourceCode + findPageParamByDataSourceCode($queryParam,"FIN_BUSINESS_IMPORT"));
			}
			
			function findPageParamByDataSourceCode($queryParam,dataSourceCode) {
				var pageParam = "";
				//组装查询参数
				$("[id^=SEARCH-]").each(function() {
					if($(this).val().length > 0){
						if(dataSourceCode == "VIEW_DOUBLE_IMPORT_MATCH"){
							pageParam += "&"+$(this).attr("id")+"="+$(this).val();
						}
				  		if(dataSourceCode == "FIN_BUSINESS_IMPORT"){
							if($(this).attr("id") == 'SEARCH-FILLING_DATE_FROM'){
								pageParam += "&SEARCH-IMPORT_TIME_FROM="+$(this).val();
							}
							if($(this).attr("id") == 'SEARCH-FILLING_DATE_TO'){
								pageParam += "&SEARCH-IMPORT_TIME_TO="+$(this).val();
							}
							if($(this).attr("id") == 'SEARCH-BILL_NO'){
								pageParam += "&"+$(this).attr("id")+"="+$(this).val();
							}
							if($(this).attr("id") == 'SEARCH-CHECK_STATE'){
								pageParam += "&"+$(this).attr("id")+"="+$(this).val();
							}
						}
				  	}
				});
				return encodeURI(pageParam);
			}
			
			//自动匹配
			function autoMatch(){
				var selected = JSON.parse(getSelections('VIEW_DOUBLE_IMPORT_MATCH'));
				if(selected.length == 0){
					leftoTable.showModal('提示', '请至少选择一条多维度报账单数据进行操作！');
					return;
				}
				var checkState = "";//匹配状态
				$.each(selected, function(i){
					if(selected[i]['CHECK_STATE'] == "1"){
						checkState += selected[i]['CHECK_STATE'] + ',';
					}
				});
				if(checkState.length > 0){
					leftoTable.showModal('提示', '当前选中数据中存在已匹配数据，请重新选择数据！');
					return;
				}
				var billNo = "";//报账单号
				$.each(selected, function(i){
					billNo += selected[i]['BILL_NO'] + ",";
				});
				billNo = billNo.substring(0,billNo.length-1);
				var message = transToServer(matchUrl+'&flag=1&billNo='+billNo,"");
				leftoTable.showModal('提示', message);
				queryTable();
			}
			
			//撤销匹配
			function revokeMatch(){
				var selected = JSON.parse(getSelections('VIEW_DOUBLE_IMPORT_MATCH'));
				if(selected.length == 0){
					leftoTable.showModal('提示', '请至少选择一条多维度报账单数据进行操作！');
					return;
				}
				var checkState = "";//匹配状态
				$.each(selected, function(i){
					if(selected[i]['CHECK_STATE'] == "0"){
						checkState += selected[i]['CHECK_STATE'] + ',';
					}
				});
				if(checkState.length > 0){
					leftoTable.showModal('提示', '当前选中数据中存在未匹配数据，请重新选择数据！');
					return;
				}
				var billNo = "";//报账单号
				var matchNo = "";//匹配流水号
				$.each(selected, function(i){
					billNo += selected[i]['BILL_NO'] + ",";
					if(selected[i]['MATCH_NO'] != ""){
						matchNo += "'" + selected[i]['MATCH_NO'] + "',";
					}
				});
				billNo = billNo.substring(0,billNo.length-1);
				if(matchNo.length > 0){
					matchNo = matchNo.substring(0,matchNo.length-1);
				}
				var message = transToServer(matchUrl+'&flag=2&matchNo='+matchNo+'&billNo='+billNo,"");
				leftoTable.showModal('提示', message);
				queryTable();
			}
			
			//手工匹配
			function manualMatch(){
				var doubleSelected = JSON.parse(getSelections('VIEW_DOUBLE_IMPORT_MATCH'));
				if(doubleSelected.length != 1){
					leftoTable.showModal('提示', '请选择一条多维度报账单数据进行操作！');
					return;
				}
				if(doubleSelected[0]['CHECK_STATE'] == '1'){
					leftoTable.showModal('提示', '当前选中数据已匹配，请重新选择数据！');
					return;
				}
				var businessSelected = JSON.parse(getSelections('FIN_BUSINESS_IMPORT'));
				if(businessSelected.length != 1){
					rightoTable.showModal('提示', '请选择一条项目成本明细数据进行操作！');
					return;
				}
				if(businessSelected[0]['CHECK_STATE'] == '1'){
					rightoTable.showModal('提示', '当前选中数据已匹配，请重新选择数据！');
					return;
				}
				
				var doubleBillNo = doubleSelected[0]['BILL_NO'];
				var businessBillNo = businessSelected[0]['BILL_NO'];
				if(doubleBillNo != businessBillNo){
					if(!billNoValidate()){
						return ;
					}
				}
				if(doubleSelected[0]['MONEY'] != businessSelected[0]['PERFORM_OF']){
					if(!moneyValidate()){
						return ;
					}
				}
				
				var message = transToServer(matchUrl+'&flag=3&doubleBillNo='+doubleBillNo+'&businessBillNo='+businessBillNo,"");
				leftoTable.showModal('提示', message);
				queryTable();
			}
			
			var inup = true;
			//布局切换
			function style_switch(){
				if(inup){
					$("#i_table_div").appendTo("#in");
					$("#l_table_div").appendTo("#up");
					//setParntHeigth(1800);
					inup=false;
				}else{
					$("#i_table_div").appendTo("#left");
					$("#l_table_div").appendTo("#rigth");
					//setParntHeigth($(document.body).height());
					inup=true;
				}
				queryTable();
			}
			
        	//更多搜索条件
    		function moreToggle(){
				$("#queryParam").toggle();
				setParntHeigth($(document.body).height());
			}
			
			function findPageParam() {
				pageParam="&token"+token;
				//组装查询参数
				$("[id^=SEARCH-]").each(function() {
					if($(this).val().length > 0){
				  		pageParam += "&"+$(this).attr("id")+"="+$(this).val();
				  		}
				  	});
				return pageParam;
			}
			
			//报账单号验证
			function billNoValidate(){
				if(confirm("当前所选数据报账单号不一致，确定继续匹配吗？")){
					return true;
				}else{
					return false;
				}
			}
			
			//金额验证
			function moneyValidate(){
				if(confirm("当前所选数据金额不一致，确定继续匹配吗？")){
					return true;
				}else{
					return false;
				}
			}
			
			//改变某行的格式 
			function rowStyle(row,index) {
				if(row['CHECK_STATE'] == '2'){
					return { 
						css: {'color': 'red'}
					};
				}
				return {};
			}
			
			//获取当前选中行数据
			function getSelections (dataSourceCode) {
				if(dataSourceCode == 'VIEW_DOUBLE_IMPORT_MATCH'){					
					return JSON.stringify(leftoTable.bootMethod($leftTable, "getSelections"));
				}else{
					return JSON.stringify(rightoTable.bootMethod($rightTable, "getSelections"));
				}
			}
		</script>
		
	</body>
</html>