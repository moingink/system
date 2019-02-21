<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
	<head>
		<meta charset="utf-8">
		<title>数据字典数据</title>
		<link rel="stylesheet" href="../../vendor/bootstrap/css/bootstrap.min.css">
		<link rel="stylesheet" href="../../vendor/bootstrap-table/src/bootstrap-table.css">
		<script src="../../vendor/jquery/jquery.min.js"></script>
		<script src="../../vendor/bootstrap/js/bootstrap.min.js"></script>
		<script src="../../vendor/bootstrap-table/src/bootstrap-table.js"></script>
		<script src="../js/bootTable.js"></script>
	</head>

	<body>

		<form class="form-horizontal">
			
			<div class="panel panel-primary">
				
				<div class="panel-body" id="bulidTable">
					<div class="panel panel-primary">
						<div class="panel-heading">
							数据字典数据列表
						</div>
						
						<div class="panel-body">
							<div class="panel panel-default">
								<div class="panel-heading">
									查询条件
								</div>
								<div class="panel-body" id="queryParam">					
								</div>
							</div>
							
							<div id="toolbar">
								<button type="button" class="btn btn-info" onclick="window.history.back(-1);">
									<span class="glyphicon glyphicon-ban-circle" aria-hidden="true"></span>返回主表
								</button>
								<button type="button" class="btn btn-info" onclick="queryTable()">
									<span class="glyphicon glyphicon-search" aria-hidden="true">查询
								</button>
								<button type="button" class="btn btn-primary" onclick="tog()">
									<span class="glyphicon glyphicon-plus" aria-hidden="true">新增
								</button>
								<button type="button" class="btn btn-warning" onclick="updateRow()">
									<span class="glyphicon glyphicon-pencil" aria-hidden="true">修改
								</button>
								<button type="button" class="btn btn-danger" onclick="delRows()">
									<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>删除
								</button>
								<button type="button" class="btn btn-warning" onclick="findCheckMessage()">
									检查数据
								</button>
							</div>
							
							<table id="table"></table>

						</div>
					</div>
				</div>

				<div class="panel-body" id="bulidPage" style="display: none">
					<div class="panel panel-primary">
						<div class="panel-heading">
							新增
						</div>
						<div class="panel-body">
							
							<div>
								<button type="button" class="btn btn-success" onclick="save()">
									<span class="glyphicon glyphicon-saved" aria-hidden="true"></span>保存
								</button>
								<button type="button" class="btn btn-inverse" onclick="back()">
									<span class="glyphicon glyphicon-ban-circle" aria-hidden="true"></span>返回
								</button>
							</div>
							
							<div class="panel-body" id="insPage">
								<!--主表主键  FOR 新增、修改-->
								<input type='hidden' id='CODE_TYPE_ID'>
							</div>
						
						</div>
					</div>
				</div>
				
			</div>
		</form>
		<!-- 是否为修改页面 -->
		<input type="hidden" id="is_edit"/>
		<!--主表主键 FOR 查询-->
		<input type="hidden" id="SEARCH-CODE_TYPE_ID">

		<script>
			var dataSourceCode = "RM_CODE_DATA";
			var tabName = "RM_CODE_DATA";
			var context = '<%=request.getContextPath()%>';
			var colurl = context+'/codeData?cmd=queryColumns';
			var paramurl = context+"/codeData?cmd=queryParam";
			var qusurl = context+'/codeData?cmd=init';
			var delurl = context+'/codeData?cmd=delRows';
			var insurl = context+'/codeData?cmd=insRow';
			var editurl = context+'/codeData?cmd=editRow';
			var maintainurl = context+'/codeData?cmd=queryMaintainCols';

			var $table = $('#table');
			var oTable = new TableInit();
			var $queryParam = $("#queryParam");
			var $inspage = $("#insPage");

			$(function() {
				$("[id='SEARCH-CODE_TYPE_ID'],#CODE_TYPE_ID").val(getURLParameter("CODE_TYPE_ID"));
				bulidTable();
			});

			function getURLParameter(name) {
				return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search)||[,""])[1].replace(/\+/g, '%20'))||null;
			}

			function bulidTable() {
				oTable.initQueryParam($queryParam, paramurl + findPageParam());
				oTable.initCols($table, colurl + findPageParam(), qusurl + findPageParam());
				oTable.initMaintainCols($inspage, maintainurl + findPageParam());
			}

			function queryTable() {
				oTable.queryTable($table, qusurl + findPageParam());
			}
			
			function delRows(){
				var selected = JSON.parse(getSelections());
				if(selected.length < 1){
					oTable.showModal('modal', "请至少选择一条数据进行删除");
					return;
					}
				var message = oTable.transToServer(tabName,delurl,getSelections());
				oTable.showModal('modal', message);
				queryTable();
				}

			function back(){
				tog();
				//排除主表主键
				$inspage.find('[id]').not("#CODE_TYPE_ID").val("");
				$("#is_edit").val("");
			}
			
			function save(){
				var message ="";
				if($("#is_edit").val() != "true"){
					message = oTable.transToServer(tabName,insurl,getJson());
				}else{
					message = oTable.transToServer(tabName,editurl,getJson());
				}
				oTable.showModal('modal', message);
				back();
				queryTable();
			}
			
			function updateRow(){
				var selected = JSON.parse(getSelections());
				if(selected.length != 1){
					oTable.showModal('modal', "请选择一条数据进行修改");
					return;
				}
				$inspage.find('[id]').each(function() {			
				  	$(this).val(selected[0][$(this).attr("id")]);
				});
				$("#is_edit").val("true");
				tog();
			}
			
			function findPageParam() {
				var pageParam = "&dataSourceCode=" + dataSourceCode;
				//组装查询参数
				$("[id^=SEARCH-]").each(function() {
					if($(this).val().length > 0){
				  		pageParam += "&"+$(this).attr("id")+"="+$(this).val();
				  		}
				  	});
				return pageParam;
			}

			function getSelections () {
				//注意：bootstrap-table内置getSelections方法所返回的json不能直接用于ajax传输——存在JSONNull数据
				return JSON.stringify(oTable.bootMethod($table, "getSelections"));
			}

			 
			/**
			 *由insPage块获取字段id、value，组装为json字符串 
			 */
			function getJson(){
				var json = "{";
				$inspage.find("[id]").each(function() {
				  	json += "\"" + $(this).attr("id") + "\":" + "\"" + $(this).val() + "\",";
				});
				json += "}";
				return json;
			}

			function findCheckMessage() {
				var message = "table:" + JSON.stringify(oTable.bootMethod($table, "getSelections"));
				oTable.showModal('modal', message);
			}
			
			function tog(){
				var $bulidtable = $("#bulidTable");
				var $bulidpage = $("#bulidPage");
				if($bulidtable.is(":hidden")){
					$bulidtable.slideDown();
					$bulidpage.slideUp();
				}else{
					$bulidpage.slideDown();
					$bulidtable.slideUp();
				}
			}
		</script>
		
	</body>
</html>