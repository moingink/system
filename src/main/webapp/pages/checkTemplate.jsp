<%@page import="com.yonyou.business.entity.TokenUtil"%>
<%@page import="com.yonyou.business.entity.TokenEntity"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
	<head>
		<meta charset="utf-8">
		<title>发票勾对</title>
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
		
		<script src="../js/bootTables.js"></script>
		<link rel="stylesheet" type="text/css" href="../css/css01.css">
		<jsp:include page="buttonjs.jsp"></jsp:include>
		<script src="../js/bootTables.js"></script>
	</head>

	<body>

		<form class="form-horizontal"
			<div class="panel panel-primary">
				
				<div class="panel-body" id="bulidTable">
					<div class="panel panel-primary">
						<div class="panel-heading">
							发票勾对
						</div>
						
						<div class="panel-body">
							<div class="panel panel-default">
								<div class="panel-heading">
									查询条件
								</div>
								<div class="panel-body" id="queryParam" style="display: none;"> </div>
								<div class="input-box-toggle" onclick="moreToggle('queryParam')">
									<span class="caret"></span>更多搜索条件
								</div>
							</div>
							<div class="panel" id="operate">
								<button type="button" class="btn btn-primary" onclick="queryTable()">
									<span class="glyphicon" aria-hidden="true">查询
								</button>
								<button type="button" class="btn btn-primary" onclick="check(true)">
									<span class="glyphicon" aria-hidden="true">勾对
								</button>
								<button type="button" class="btn btn-primary" onclick="check(false)">
									<span class="glyphicon" aria-hidden="true">撤销勾对
								</button>
								<button type="button" class="btn btn-primary" onclick="style_switch()">
									<span class="glyphicon" aria-hidden="true">布局切换
								</button>
							</div>
								
							<div id="in">
								<div class="panel panel-default" id='i_table_div'>
									<div class="panel-heading">
										金税发票数据
									</div>
									<table id="l_table" data-row-style="rowStyle_L"></table>
								</div>
							</div>
							<div id="up">
								<div class="panel panel-default" id='l_table_div'>
									<div class="panel-heading">
										开票申请数据
									</div>
									<table id="r_table" data-row-style="rowStyle_R"  ></table>
								</div>
							</div>
							
							<div id="style_inup">			
								<div class="col-md-6" id="left"></div>
								<div class="col-md-6" id="rigth"></div>	
							</div>
						
						</div>
					</div>
				</div>

				
				
			</div>
		</form>
		<!-- 是否为修改页面 -->
		<input type="hidden" id="is_edit"/>
		<% 
			TokenEntity tokenEntity =TokenUtil.initTokenEntity(request);
		%>
		<script>
			var inup = false;
			function style_switch(){
				if(inup){
					$("#i_table_div").appendTo("#in");
					$("#l_table_div").appendTo("#up");
					inup=false;
				}else{
					$("#i_table_div").appendTo("#left");
					$("#l_table_div").appendTo("#rigth");
					inup=true;
				}
				queryTable();
			}
			
			var token='<%=request.getParameter("token")!=null?request.getParameter("token"):""%>';
			var context = '<%=request.getContextPath()%>';
			
			var $l_table = $('#l_table');
			var $r_table = $('#r_table');
			var l_oTable = new TablesInit($l_table);
			var r_oTable = new TablesInit($r_table);
			var $queryParam = $("#queryParam");
			var $inspage = $("#insPage");
			
			var itemStatus = 0;
			var limit = 0;

			var paramurl = context+"/base?cmd=queryParam";
			var colurl = context+'/base?cmd=queryColumns';
			var qusurl = context+'/base?cmd=init';
			var checkurl = context+'/base?cmd=check';
			
			var lDataSourceCode = '&dataSourceCode=FIN_TICKET_IMPORT';//金税发票
			var rDataSourceCode = '&dataSourceCode=FIN_INVOICE_OPEN';//开票申请

			$(function() {
				bulidTable();
			});

			function bulidTable() {
				//公共查询条件，在左表数据源中配置
				l_oTable.initQueryParam($queryParam,paramurl + lDataSourceCode);
				l_oTable.initCols(colurl + lDataSourceCode, qusurl + lDataSourceCode);
				r_oTable.initCols(colurl + rDataSourceCode, qusurl + rDataSourceCode);
			}

			function queryTable() {
				l_oTable.queryTable(qusurl + lDataSourceCode);
				r_oTable.queryTable(qusurl + rDataSourceCode);
			}
			
			function check(flag){
				var lData = JSON.parse(getSelections('L'));
				var rData = JSON.parse(getSelections('R'));
				console.log(lData);
				console.log(lData.length);
				console.log(rData);
				console.log(rData.length);
				if(lData.length == 0 || rData.length == 0){
					l_oTable.showModal('modal', "两边数据均至少选择一条数据进行勾对");
					return;
				}
				for(var i=0;i<lData.length;i++){
					var checkState = lData[i]['CHECK_STATE'];
					if(checkState != 0){
						l_oTable.showModal('modal', "金税发票数据中序号为"+ lData[i]['RMRN']+"的数据已进行过勾对");
						return;
					}
				}
				for(var i=0;i<rData.length;i++){
					var checkState = rData[i]['CHECK_STATE'];
					if(checkState != 0){
						r_oTable.showModal('modal', "开票申请数据中序号为"+ rData[i]['RMRN']+"的数据已进行过勾对");
						return;
					}
				}
				
				r_oTable.showModal('modal', message["message"]);
				queryTable();
			}
			
			function findPageParam(flag) {
				pageParam="&token"+token;
				//组装查询参数
				$("[id^=SEARCH-]").each(function() {
					if($(this).val().length > 0){
				  		pageParam += "&"+$(this).attr("id")+"="+$(this).val();
				  		}
				  	});
				return pageParam;
			}

			function getSelections (LorR) {
				if(LorR == 'L'){					
					return JSON.stringify(l_oTable.bootMethod($l_table, "getSelections"));
				}else{
					return JSON.stringify(r_oTable.bootMethod($r_table, "getSelections"));
				}
			}
        	
    		function moreToggle(divId){
				$("#"+divId).toggle();
				setParntHeigth($(document.body).height());
			}
		</script>
		
	</body>
</html>