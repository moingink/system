<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>客户回款认领</title>
</head>

<body>
	<div class="panel panel-primary">
		<div class="panel-body">
			<div class="panel panel-primary">
				<div class="panel-body">
					<div>
						<button type="button" class="btn btn-success" onclick="save(this)">
							<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>&nbsp;确定
						</button>
						<button type="button" class="btn btn-warning" onclick="javascript:history.back(-1);">
							<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>&nbsp;取消
						</button>
					</div>
				</div>
				<div id="table_div1" style="display: none;">
					<table id="table1" data-row-style="rowStyle">
						<thead>
							<th data-radio=true></th>
							<th data-field="RMRN" data-halign="center" data-align="center">序</th>
							<th data-field="MONEY_TYPE" data-halign="center" data-align="center">款项类型</th>
							<th data-field="OPPOSITE_UNIT" data-halign="center" data-align="center">对方单位</th>
							<th data-field="ENTER_DATE" data-halign="center" data-align="center">银行入账日期</th>
							<th data-field="SUMMARY" data-halign="center" data-align="center">摘要</th>
							<th data-field="REMARK" data-halign="center" data-align="center">备注</th>
							<th data-field="RECEIVE_AMOUNT" data-halign="center" data-align="center">银行回单金额（元）</th>
							<th data-field="CLAIM_AMOUNT" data-halign="center" data-align="center">本次认领金额（元）</th>
							<th data-field="WAIT_CLAIM" data-halign="center" data-align="center">未认领/预收金额（元）</th>
						</thead>
					</table>
				</div>	
				<div class="panel-body">
					<div>
						<button type="button" class="btn btn-info" onclick="claimA(this)">
							<span class="glyphicon glyphicon" aria-hidden="true"></span>应收款认领
						</button>
						<button type="button" class="btn btn-info" onclick="claimB(this)">
							<span class="glyphicon glyphicon" aria-hidden="true"></span>预收款认领
						</button>
					</div>
				</div>
				<div id="table_div2" style="display: none;">
					<table id="table2" data-row-style="rowStyle">
						<thead>
							<th data-field="RMRN" data-halign="center" data-align="center">序</th>
							<th data-field="CLAIM_AMOUNT" data-halign="center" data-align="center">认领金额（元）</th>
							<th data-field="BILL_NO" data-halign="center" data-align="center">确认收入单号</th>
							<th data-field="CLIENT_NAME" data-halign="center" data-align="center">客户名称</th>
							<th data-field="CONFIRM_INCOME" data-halign="center" data-align="center">确认收入金额（元）</th>
							<th data-field="CLAIM_BALANCE" data-halign="center" data-align="center">确认收入余额（元）</th>
							<th data-field="OPPORTY_CODE" data-halign="center" data-align="center">商机编号</th>
							<th data-field="PROJ_NAME" data-halign="center" data-align="center">项目名称</th>
							<th data-field="CONTRACT_CODE" data-halign="center" data-align="center">合同编号</th>
							<th data-field="CONTRACT_NAME" data-halign="center" data-align="center">合同名称</th>
							<th data-field="BUSINESS_TYPE" data-halign="center" data-align="center">业务类型</th>
							<th data-field="PRODUCT_TYPE" data-halign="center" data-align="center">产品</th>
							<th data-field="CLAIM_STATE" data-halign="center" data-align="center">认领状态</th>
						</thead>
					</table>
				</div>
				<div id="table_div3" style="display: none;">
					<table id="table3" data-row-style="rowStyle">
						<thead>
							<th data-field="RMRN" data-halign="center" data-align="center">序</th>
							<th data-field="CLAIM_AMOUNT" data-halign="center" data-align="center">认领金额（元）</th>
							<th data-field="CLIENT_NAME" data-halign="center" data-align="center">客户名称</th>
							<th data-field="OPPORTUNITY_CODE" data-halign="center" data-align="center">商机编号</th>
							<th data-field="PROJ_NAME" data-halign="center" data-align="center">项目名称</th>
							<th data-field="CON_ID" data-halign="center" data-align="center">合同编号</th>
							<th data-field="CON_NAME" data-halign="center" data-align="center">合同名称</th>
							<th data-field="VALUE_ADDED_TAX_PRICE" data-halign="center" data-align="center">合同金额（元）</th>
							<th data-field="BALANCE" data-halign="center" data-align="center">余额（元）</th>
							<th data-field="CLAIM_STATE" data-halign="center" data-align="center">认领状态</th>
						</thead>
					</table>
				</div>	
			</div>
		</div>
	</div>	
</body>

<jsp:include page="../include/public.jsp"></jsp:include>

<script type="text/javascript">
	var makeSign = 0;
	$(function(){
		setParntHeigth(1200);
		$('#table_div1').css('display','block');
		var url = '/finance/getReturnMoney?id=<%=request.getParameter("id")%>';
		initTable($("#table1"),url,'head','200');
		$('#table_div1').children().find('.fixed-table-container').css("min-height","200px");
	});
	
	function initTable($Table,url,type,height){
		$Table.bootstrapTable('destroy');//销毁表格 
		$Table.bootstrapTable({
			url : context+url,
			method : 'get',
			dataType: "json",
			uniqueId:'ID', //表明每行唯 一的标识符
            cache:false, //默认缓存ajax请求，设为false则禁用缓存
            striped: false, // 是否显示斑马条
            pagination: true, //是否启用分页
            pageSize: 10, //设置每页初始显示条数
            pageList: [10, 25, 50, 100], //设置选择每页显示条数
            height: height,//高度
            clickToSelect: true, //是否启用点击选中行
            formatLoadingMessage: function () {
			    return "请稍等，正在加载中...";  
			}, 
            formatNoMatches: function () {
				return '无符合条件的记录';  
			},
			onLoadSuccess: function(data) {
				
        	},
        	onPostBody: function(data){
	        	if(data.length != 0){
	        		if(type == 'claimA'){
	        			var selected = JSON.parse(getSelections($('#table1')));
	        			var index = selected[0]['RMRN']-1;
						var tableData = $('#table1').bootstrapTable('getData');
						var tr = $Table.find('tbody').find('tr');
						var claimAmountTotal = 0; //本次认领金额
						var claimAmount = 0; //修改前认领金额
						var waitClaim = 0; //未认领金额
						for(var i=0;i<tr.length;i++){
							$(tr[i]).find('td').eq(1).html('<a href="#" id="claimAmountA'+i+'"></a>');
							$('#claimAmountA'+i).editable({
								type: 'text', //编辑框的类型,支持text|textarea|select|date|checklist等
					    		title: '认领金额', //编辑框的标题
					    		disabled: false, //是否禁用编辑
					    		emptytext: "填写认领金额", //空值的默认文本
					            mode: "popup", //编辑框的模式：支持popup和inline两种模式，默认是popup
					            validate: function (value) {
					             	//非负浮点数
					            	var regPos = /^\d+(\.\d+)?$/;
					            	
					            	//余额
					            	var balance = $(this).parent().nextAll().eq(3).text();
					            	//如果认领金额修改，那么余额应先加上修改前的认领金额
							    	balance = Number(balance)+Number(claimAmount);
							    	
							    	//未认领金额
							    	waitClaim = tableData[index]['WAIT_CLAIM'];
							    	//如果修改前的认领金额与修改后的认领金额不一致，那么未认领金额先加上修改前的认领金额
							    	if(Number(claimAmount) != Number(value)){
							    		waitClaim = Number(waitClaim)+Number(claimAmount);
							    	}
							    	
					                if (!regPos.test(value)) {
					                    return '认领金额必须是数字！';
					                }
					                if (Number(value) > Number(balance)) {
					                    return '认领金额不能大于余额！';
					                }
					                if (Number(value) > Number(waitClaim)) {
					                    return '认领金额不能大于未认领/预收金额！';
					                }
					            },
					            display: function(value) {
							    	$(this).html(value);
							    	
							    	//回款表应收认领金额
							    	var amountReceivable = tableData[index]['AMOUNT_RECEIVABLE'];
							    	//回款表预收认领金额
							    	var amountAdvance = tableData[index]['AMOUNT_ADVANCE'];
							    	//应收款认领子表数据
							    	var data = $Table.bootstrapTable('getData');
							    	
							    	var id = $(this).attr('id');
							    	id = id.substring(id.length-1,id.length);
							    	
							    	//如果修改前的认领金额与修改后的认领金额不一致，那么本次认领金额先减去修改前的认领金额,再加上修改后的认领金额
							    	//主表应收认领金额同理
							    	if(Number(claimAmount) != Number(value)){
							    		claimAmountTotal = Number(claimAmountTotal)-Number(claimAmount);
							    		amountReceivable = Number(amountReceivable)-Number(claimAmount);
							    	}
							    	claimAmountTotal = Number(claimAmountTotal)+Number(value);
							    	amountReceivable = Number(amountReceivable)+Number(value);
							    	
							    	//未认领金额
							    	waitClaim = tableData[index]['WAIT_CLAIM'];
							    	if(selected[0]['MONEY_TYPE'] == "预收"){
							    		waitClaim = tableData[index]['AMOUNT_ADVANCE'];
									}
							    	//如果修改前的认领金额与修改后的认领金额不一致，那么未认领金额先加上修改前的认领金额
							    	if(Number(claimAmount) != Number(value)){
							    		waitClaim = Number(waitClaim)+Number(claimAmount);
							    		if(selected[0]['MONEY_TYPE'] == "预收"){
							    			//主表预收认领金额同理
							    			amountAdvance = Number(amountAdvance)+Number(claimAmount);
										}
							    	}	
								    //未认领金额=未认领金额-当前认领金额
								    waitClaim = Number(waitClaim)-Number(value);
								    if(selected[0]['MONEY_TYPE'] == "预收"){
							    		//主表预收认领金额同理
							    		amountAdvance = Number(amountAdvance)-Number(value);
									}
							    	//未认领金额赋值
							    	tableData[index]['WAIT_CLAIM']=Number(waitClaim).toFixed(2);
									
									//本次认领金额赋值
							    	tableData[index]['CLAIM_AMOUNT']=Number(claimAmountTotal).toFixed(2);
							    	
									//回款表应收认领金额赋值
									tableData[index]['AMOUNT_RECEIVABLE']=Number(amountReceivable).toFixed(2);
									//回款表预收认领金额赋值
									tableData[index]['AMOUNT_ADVANCE']=Number(amountAdvance).toFixed(2);
									
									$('#table1').bootstrapTable('load', tableData[index]);
									
							    	//余额
							    	var balance = $(this).parent().nextAll().eq(3).text();
							    	//如果修改前的认领金额与修改后的认领金额不一致，那么余额先加上修改前的认领金额,再减去修改后的认领金额
							    	if(claimAmount != value){
							    		balance = Number(balance)+Number(claimAmount);
							    	}
							    	//余额=余额-认领金额
							    	balance = Number(balance)-Number(value);
							    	//余额赋值
							    	$(this).parent().nextAll().eq(3).text(Number(balance).toFixed(2));
							    	data[id]['CLAIM_BALANCE']=Number(balance).toFixed(2);
							    	
							    	//确认收入金额
					            	var confirmIncome = $(this).parent().nextAll().eq(2).text();
							    	//确认收入认领状态
							    	if(balance == "0"){
							    		$(this).parent().nextAll().eq(10).text('已认领');
							    		data[id]['CLAIM_STATE']='1';
							    	}else if(balance != "0" && Number(balance) != Number(confirmIncome)){
							    		$(this).parent().nextAll().eq(10).text('部分认领');
							    		data[id]['CLAIM_STATE']='2';
							    	}else{
							    		$(this).parent().nextAll().eq(10).text('未认领');
							    		data[id]['CLAIM_STATE']='0';
							    	}
							    	
							    	//客户回款主键
							    	if(value != "" && value != 0){
							    		data[id]['RECEIVED_PAYMENT_ID']=tableData[index]['ID'];
							    	}else{
							    		data[id]['RECEIVED_PAYMENT_ID']="";
							    	}
							    	//认领金额
							    	data[id]['CLAIM_AMOUNT']=Number(value).toFixed(2);
							    	//认领详情认领状态
							    	data[id]['CLAIM_TYPE']="0";
							    	if(selected[0]['MONEY_TYPE'] == "预收"){
							    		data[id]['CLAIM_TYPE']="2";
									}
							    }
							});
							//文本赋值前事件
							$('#claimAmountA'+i).on('shown', function(event,editable) {
								//获取输入框值	
								claimAmount = $(this).editable('getValue')[$(this).attr('id')];
							});
						}
					}
					if(type == 'claimB'){
						var selected = JSON.parse(getSelections($('#table1')));
	        			var index = selected[0]['RMRN']-1;
	        			var tableData = $('#table1').bootstrapTable('getData');
						var tr = $Table.find('tbody').find('tr');
						var claimAmountTotal = 0; //本次认领金额
						var claimAmount = 0; //修改前认领金额
						var waitClaim = 0; //未认领金额
						for(var i=0;i<tr.length;i++){
							$(tr[i]).find('td').eq(1).html('<a href="#" id="claimAmountB'+i+'"></a>');
							$('#claimAmountB'+i).editable({
								type: 'text', //编辑框的类型,支持text|textarea|select|date|checklist等
					    		title: '认领金额', //编辑框的标题
					    		disabled: false, //是否禁用编辑
					    		emptytext: "填写认领金额", //空值的默认文本
					            mode: "popup", //编辑框的模式：支持popup和inline两种模式，默认是popup
					            validate: function (value) { //字段验证
					            	//非负浮点数
					            	var regPos = /^\d+(\.\d+)?$/;
					            	
					            	//余额
					            	var balance = $(this).parent().nextAll().eq(8).text();
					            	//如果认领金额需修改，那么余额应先加上修改前的认领金额
							    	balance = Number(balance)+Number(claimAmount);
							    	
							    	//未认领金额
							    	waitClaim = tableData[index]['WAIT_CLAIM'];
							    	//如果修改前的认领金额与修改后的认领金额不一致，那么未认领金额先加上修改前的认领金额
							    	if(Number(claimAmount) != Number(value)){
							    		waitClaim = Number(waitClaim)+Number(claimAmount);
							    	}
							    	
					                if (!regPos.test(value)) {
					                    return '认领金额必须是数字！';
					                }
					                if (Number(value) > Number(balance)) {
					                    return '认领金额不能大于余额！';
					                }
					                if (Number(value) > Number(waitClaim)) {
					                    return '认领金额不能大于未认领/预收金额！';
					                }
					            },
							    display: function(value) {
							    	$(this).html(value);
							    	
							    	//主表预收认领金额
							    	var amountAdvance = tableData[index]['AMOUNT_ADVANCE'];
							    	//预收款认领子表数据
							    	var data = $Table.bootstrapTable('getData');
							    	
							    	var id = $(this).attr('id');
							    	id = id.substring(id.length-1,id.length);
							    	
							    	//如果修改前的认领金额与修改后的认领金额不一致，那么合同金额先减去修改前的认领金额,再加上修改后的认领金额
							    	//主表预收认领金额同理
							    	if(Number(claimAmount) != Number(value)){
							    		claimAmountTotal = Number(claimAmountTotal)-Number(claimAmount);
							    		amountAdvance = Number(amountAdvance)-Number(claimAmount);
							    	}
							    	claimAmountTotal = Number(claimAmountTotal)+Number(value);
							    	amountAdvance = Number(amountAdvance)+Number(value);
							    	//未认领金额
							    	waitClaim = tableData[index]['WAIT_CLAIM'];
							    	//如果修改前的认领金额与修改后的认领金额不一致，那么未认领金额先加上修改前的认领金额
							    	if(Number(claimAmount) != Number(value)){
							    		waitClaim = Number(waitClaim)+Number(claimAmount);
							    	}	
							    	//未认领金额=未认领金额-当前认领金额
								    waitClaim = Number(waitClaim)-Number(value);
							    	//未认领金额赋值
							    	tableData[index]['WAIT_CLAIM']=Number(waitClaim).toFixed(2);
							    	
							    	//本次认领金额赋值
							    	tableData[index]['CLAIM_AMOUNT']=Number(claimAmountTotal).toFixed(2);
							    	
								    //回款表预收认领金额赋值
									tableData[index]['AMOUNT_ADVANCE']=Number(amountAdvance).toFixed(2);
									
									$('#table1').bootstrapTable('load', tableData[index]);
							    	
							    	//余额
							    	var balance = $(this).parent().nextAll().eq(8).text();
							    	//如果修改前的认领金额与修改后的认领金额不一致，那么余额先加上修改前的认领金额,再减去修改后的认领金额
							    	if(claimAmount != value){
							    		balance = Number(balance)+Number(claimAmount);
							    	}
							    	//余额-认领金额=余额
							    	balance = Number(balance)-Number(value);
							    	//余额赋值
							    	$(this).parent().nextAll().eq(8).text(Number(balance).toFixed(2));
							    	data[id]['BALANCE']=Number(balance).toFixed(2);
							    	
							    	//合同金额
					            	var confirmIncome = $(this).parent().nextAll().eq(7).text();
							    	//合同认领状态
							    	if(balance == "0"){
							    		$(this).parent().nextAll().eq(9).text('已认领');
							    		data[id]['CLAIM_STATE']='1';
							    	}else if(balance != "0" && Number(balance) != Number(confirmIncome)){
							    		$(this).parent().nextAll().eq(9).text('部分认领');
							    		data[id]['CLAIM_STATE']='2';
							    	}else{
							    		$(this).parent().nextAll().eq(9).text('未认领');
							    		data[id]['CLAIM_STATE']='0';
							    	}
							    	
							    	//回款主键
							    	if(value != "" && value != 0){
							    		data[id]['RECEIVED_PAYMENT_ID']=tableData[0]['ID'];
							    	}else{
							    		data[id]['RECEIVED_PAYMENT_ID']="";
							    	}
							    	//认领金额
							    	data[id]['CLAIM_AMOUNT']=Number(value).toFixed(2);
							    	//认领详情认领状态
							    	data[id]['CLAIM_TYPE']="1";
							    }
							});
							//文本赋值前事件
							$('#claimAmountB'+i).on('shown', function(event,editable) {
								//获取输入框值	
								claimAmount = $(this).editable('getValue')[$(this).attr('id')];
							});
						}
					}
				}
        	}
        }); 
	}
	
	//应收款认领
	function claimA(t){
		var url;
		var selected = JSON.parse(getSelections($('#table1')));
		if(selected.length != 1){
			oTable.showModal('提示', "请选择一条数据进行操作！");
			return;
		}
		if(selected[0]['WAIT_CLAIM'] == 0){
			oTable.showModal('提示', "未认领金额为0，不能进行应收款认领！");
			return;
		}
		makeSign++;
		if(makeSign > 1){
			oTable.showModal('提示', "请先保存当前数据！");
			return;
		}
		$('#table_div3').css('display','none');
		$('#table_div2').css('display','block');
		url = '/finance/getConfirmIncome?clientName='+selected[0].OPPOSITE_UNIT;
		if(selected[0]['MONEY_TYPE'] == "预收"){
			url = '/finance/getConfirmIncome?clientName='+selected[0].OPPOSITE_UNIT+'&conId='+selected[0].CONTRACT_NO;
		}
		initTable($('#table2'),url,'claimA','500');
	}
	
	//预收款认领
	function claimB(t){
		var selected = JSON.parse(getSelections($('#table1')));
		if(selected.length != 1){
			oTable.showModal('提示', "请选择一条数据进行操作！");
			return;
		}
		if(selected[0]['MONEY_TYPE'] == "预收"){
			oTable.showModal('提示', "请选择应收进行预收款认领！");
			return;
		}
		if(selected[0]['WAIT_CLAIM'] == 0){
			oTable.showModal('提示', "未认领金额为0，不能进行预收款认领！");
			return;
		}
		makeSign++;
		if(makeSign > 1){
			oTable.showModal('提示', "请先保存当前数据！");
			return;
		}
		$('#table_div2').css('display','none');
		$('#table_div3').css('display','block');
		var url = '/finance/getContractInfo?clientName='+selected[0].OPPOSITE_UNIT;
		initTable($('#table3'),url,'claimB','500');
	}
	
	//返回被选择的行
	function getSelections ($Table) {
		//注意：bootstrap-table内置getSelections方法所返回的json不能直接用于ajax传输——存在JSONNull数据
		return JSON.stringify($Table.bootstrapTable('getSelections'));
	}
	
	//保存
	function save(t){
		var selected = JSON.parse(getSelections($('#table1')));
		if(selected.length != 1){
			oTable.showModal('提示', "请选择一条数据进行操作！");
			return;
		}
		var tableDataOne = $('#table1').bootstrapTable('getData');
		var tableDataTwo = $('#table2').bootstrapTable('getData');
		var tableDataThree = $('#table3').bootstrapTable('getData');
		var index = selected[0]['RMRN']-1;
		if(selected[0]['MONEY_TYPE'] == "应收"){
			var childJsonOne = new Array();
			var dataOne = {
				'id': tableDataOne[index].ID,
				'receiveAmount': Number(tableDataOne[index].RECEIVE_AMOUNT),
				'amountReceivable': Number(tableDataOne[index].AMOUNT_RECEIVABLE),
				'amountAdvance': Number(tableDataOne[index].AMOUNT_ADVANCE),
				'waitClaim': Number(tableDataOne[0].WAIT_CLAIM)
			};
			//需要序列化对象，转成字符串类型
			childJsonOne.push(JSON.stringify(dataOne));
			
			var dataTwo;
			var childJsonTwo = new Array();
			$.each(tableDataTwo, function (i){
				dataTwo = {
				   	'id': tableDataTwo[i].ID,
				    'claimState': Number(tableDataTwo[i].CLAIM_STATE),
				    'balance': Number(tableDataTwo[i].CLAIM_BALANCE),
				    'receivedPaymentId': tableDataTwo[i].RECEIVED_PAYMENT_ID,
				    'userName': userName,
				    'companyName': companyName,
				    'billNo': tableDataTwo[i].BILL_NO,
				    'claimAmount': tableDataTwo[i].CLAIM_AMOUNT,
				    'contractCode': tableDataTwo[i].CONTRACT_CODE,
				    'claimType': tableDataTwo[i].CLAIM_TYPE
				};
			    //需要序列化对象，转成字符串类型
			 	childJsonTwo.push(JSON.stringify(dataTwo));
			});
			    
			var dataThree;
			var childJsonThree = new Array();
			$.each(tableDataThree, function (i){
				dataThree = {
				    'id': tableDataThree[i].ID,
				    'claimState': Number(tableDataThree[i].CLAIM_STATE),
				    'balance': Number(tableDataThree[i].BALANCE),
				    'receivedPaymentId': tableDataThree[i].RECEIVED_PAYMENT_ID,
				    'userName': userName,
				    'companyName': companyName,
				    'conId': tableDataThree[i].CON_ID,
				    'claimAmount': tableDataThree[i].CLAIM_AMOUNT,
				    'claimType': tableDataThree[i].CLAIM_TYPE
				};
			    //需要序列化对象，转成字符串类型
			    childJsonThree.push(JSON.stringify(dataThree));
			});
			
			var claimTableOneSize = new Array();
			var claimTableTwoSize = new Array();
			$.each(childJsonTwo,function(i){
				var claimTableOne = JSON.parse(childJsonTwo[i]);
				if(claimTableOne.hasOwnProperty('claimAmount')){
					if(claimTableOne.claimAmount != 0 && claimTableOne.claimAmount != ""){
						claimTableOneSize.push(claimTableOne.claimAmount);
					}
				}
			}); 
			$.each(childJsonThree,function(i){
				var claimTableTwo = JSON.parse(childJsonThree[i]);
				if(claimTableTwo.hasOwnProperty('claimAmount')){
					if(claimTableTwo.claimAmount != 0 && claimTableTwo.claimAmount != ""){
						claimTableTwoSize.push(claimTableTwo.claimAmount);
					}
				}
			});  
			if(claimTableOneSize.length == 0 &&  claimTableTwoSize.length == 0){
				oTable.showModal('提示', "请先认领金额！");
				return;
			} 
			
			$.ajax({
			    async: true,
			    type: "post",
				url: '/system/finance/claim',
				dataType: "json",
				//防止深度序列化
	    		traditional: true,
				data:{
					"childJsonOne":childJsonOne,
					"childJsonTwo":childJsonTwo,
					"childJsonThree":childJsonThree,
					"token":token
				},
				success: function(data){
					if(data = "success"){
						oTable.showModal('提示', '认领成功！');
						makeSign = 0;
						initTable($("#table1"),'/finance/getReturnMoney?id=<%=request.getParameter("id")%>','head','200');
						$('#table_div1').children().find('.fixed-table-container').css("min-height","200px");
						$("#table2").bootstrapTable('destroy');
						$("#table3").bootstrapTable('destroy');
						$('#table_div2').css('display','none');
						$('#table_div3').css('display','none');
					}else{
						oTable.showModal('提示', '认领失败！');
					}
				},
				error:function(XMLHttpRequest, textStatus, errorThrown){
					//登录超时
					if(XMLHttpRequest.getResponseHeader("TIMEOUTURL")!=null){
					    window.top.location.href = XMLHttpRequest.getResponseHeader("TIMEOUTURL");
					}
				}
			});
		}
		
		if(selected[0]['MONEY_TYPE'] == "预收"){
			var childJsonOne = new Array();
			var dataOne = {
				'id': tableDataOne[index].ID,
				'receiveAmount': Number(tableDataOne[index].RECEIVE_AMOUNT),
				'amountReceivable': Number(tableDataOne[index].AMOUNT_RECEIVABLE),
				'amountAdvance': Number(tableDataOne[index].AMOUNT_ADVANCE),
				'waitClaim': Number(tableDataOne[0].WAIT_CLAIM)
			};
			//需要序列化对象，转成字符串类型
			childJsonOne.push(JSON.stringify(dataOne));
			    
			var dataTwo;
			var childJsonTwo = new Array();
			$.each(tableDataTwo, function (i){
				dataTwo = {
				    'id': tableDataTwo[i].ID,
				    'claimState': Number(tableDataTwo[i].CLAIM_STATE),
				    'balance': Number(tableDataTwo[i].CLAIM_BALANCE),
				    'receivedPaymentId': tableDataTwo[i].RECEIVED_PAYMENT_ID,
				    'userName': userName,
				    'companyName': companyName,
				    'billNo': tableDataTwo[i].BILL_NO,
				    'claimAmount': tableDataTwo[i].CLAIM_AMOUNT,
				    'contractCode': tableDataTwo[i].CONTRACT_CODE,
				    'claimType': tableDataTwo[i].CLAIM_TYPE,
				    'detailId': tableDataOne[index].DETAIL_ID
				};
			    //需要序列化对象，转成字符串类型
			    childJsonTwo.push(JSON.stringify(dataTwo));
			});
			
			var claimTableOneSize = new Array();
			$.each(childJsonTwo,function(i){
				var claimTableOne = JSON.parse(childJsonTwo[i]);
				if(claimTableOne.hasOwnProperty('claimAmount')){
					if(claimTableOne.claimAmount != 0 && claimTableOne.claimAmount != ""){
						claimTableOneSize.push(claimTableOne.claimAmount);
					}
				}
			}); 
			if(claimTableOneSize.length == 0){
				oTable.showModal('提示', "请先认领金额！");
				return;
			}
			    
			$.ajax({
			    async: true,
			    type: "post",
				url: '/system/finance/claim',
				dataType: "json",
				//防止深度序列化
	    		traditional: true,
				data:{
					"childJsonOne":childJsonOne,
					"childJsonTwo":childJsonTwo,
					"token":token
				},
				success: function(data){
					if(data = "success"){
						oTable.showModal('提示', '认领成功！');
						makeSign = 0;
						initTable($("#table1"),'/finance/getReturnMoney?id=<%=request.getParameter("id")%>','head','200');
						$('#table_div1').children().find('.fixed-table-container').css("min-height","200px");
						$("#table2").bootstrapTable('destroy');
						$("#table3").bootstrapTable('destroy');
						$('#table_div2').css('display','none');
						$('#table_div3').css('display','none');
					}else{
						oTable.showModal('提示', '认领失败！');
					}
				},
				error:function(XMLHttpRequest, textStatus, errorThrown){
					//登录超时
					if(XMLHttpRequest.getResponseHeader("TIMEOUTURL")!=null){
					   	window.top.location.href = XMLHttpRequest.getResponseHeader("TIMEOUTURL");
					}
				}
			});
		}
	}
</script>

</html>