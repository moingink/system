buttonJson =[
             {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
             {name:'新增',fun:'tog(this),setUpAdd(this),setAuditButton()',buttonToken:'addWorkOrderApproval'},
             {name:'修改',fun:'updateRow(this),setUpUpdate(this),setAuditButton()',buttonToken:'update'},
             {name:'删除',fun:'delRows(this)',buttonToken:'delete'}
            ];

$(function(){
	$('#SUBMISSION_PERSON,#SUBMISSION_DEPARTMENT,#SUBMISSION_DATE').attr('readonly','true');//提交人、提交部门、提交时间
	$('#WORK_ORDER_NUMBER,#BILL_STATUS').parents('.col-md-4').css('display','none');//审批状态
})

//新增
function setUpAdd(t){
	buttonDisabledFalse();
	$('#SUBMISSION_PERSON').val(userName);//提交人
	$('#SUBMISSION_DEPARTMENT').val(companyName);//提交部门
	$('#SUBMISSION_DATE').val(getDate());//提交时间
}

//修改
function setUpUpdate(t){
	buttonDisabledFalse();
	var selected = JSON.parse(getSelections());
	if(selected[0]['BILL_STATUS'] != 0 && selected[0]['BILL_STATUS'] != 7){
		buttonDisabledTrue();
	}
	var str = dataHandle($('#BILL_NO').val());
	var _dataSourceCode = "";
	var workOrderType = $('#WORK_ORDER_TYPE').val();
	if(workOrderType == "0"){//暂估收入
		_dataSourceCode = "FIN_ESTIMATED_INCOME";
	}else{//确认收入
		_dataSourceCode = "FIN_CONFIRM_INCOME";
	}
	buildChildTable(_dataSourceCode,"BILL_NO IN (" + str + ")");
}

//重写删除
function delRowsByDataSourceCode(t,_dataSourceCode){
	var selected = JSON.parse(getSelections());
	if(selected.length < 1){
		oTable.showModal('提示', "请至少选择一条数据进行删除");
		return;
	}
	var json = new Array();
	for (var i = 0; i < selected.length; i++) {
		var billStatus = selected[i]["BILL_STATUS"];
		if (billStatus == 0 || billStatus == 7) {
			json.push(selected[i]);
		}
	}
	if (json.length == 0) {
		oTable.showModal('提示', "只能删除已保存、审批退回单据");
		return;
	}
	if(!validateDel(selected)){
		return;
	}
	var message = transToServer(findBusUrlByPublicParam(t,'',_dataSourceCode),JSON.stringify(json));
	oTable.showModal('提示', message);
	queryTableByDataSourceCode(t,_dataSourceCode);
}

//重写保存
function savaByQuery(t,_dataSourceCode,$div){
	var message = "";
	var buttonToken = $("#ins_or_up_buttontoken").val();
	if($('#ID').val() == ""){
		$('#ID').val(getId());
	}
	if($('#BILL_STATUS').val() == ""){
		$('#BILL_STATUS').val("0");
	}
	message = transToServer(findBusUrlByButtonTonken(buttonToken,'',_dataSourceCode),getJson($div));
	oTable.showModal('提示', message);
	if(message.indexOf('成功') != -1){
		buttonDisabledTrue();
		$('#tj').removeAttr('disabled');
	}
}

//重写返回
function back(t){
	var cache_dataSourceCode = $("#cache_dataSourceCode").val();
	var _dataSourceCode = dataSourceCode;
	if(cache_dataSourceCode != null && cache_dataSourceCode.length > 0){
		_dataSourceCode = cache_dataSourceCode;
	}
	togByDataSourceCode(t,_dataSourceCode);
	$inspage.find('[id]').val("");
	$("#ins_or_up_buttontoken").val("");
	//不使用组件时清空file域
	clearFile();
	//清空附件清单
	if ($batchNo!="" && $batchNo!=null) {
		if ($("#showfiles_"+$batchNo.attr("ID"))!="") {
			$("#showfiles_"+$batchNo.attr("ID")).empty();//清除
		}
	}
	window.location.reload();
}

var dbclickHtml = '<div class="modal-body" style="overflow:hidden;" id="detail">'+
						'<div id="detailTableDbclick"></div>'+
				  '</div>';
				  
//重写双击事件
function show_dbclick(selected,title){
	$('#bus_message_dbclick').parent().next().remove();
	if($('#detail').length == 0){
		$('#bus_message_dbclick').parent().after(dbclickHtml);
	}
	$('#bus_message_dbclick').html('');
	$('#myModalLabel_dbclick').html(title);
	
	if(selected.hasOwnProperty('MONTH')){
		initDetailPageByJson($("#bus_message_dbclick"),"FIN_ESTIMATED_INCOME",JSON.stringify(selected));
	}else if(selected.hasOwnProperty('CONFIRM_DATE')){
		initDetailPageByJson($("#bus_message_dbclick"),"FIN_CONFIRM_INCOME",JSON.stringify(selected));
	}else if(selected.hasOwnProperty('ESTIMATED_ID')){
		if(selected['ESTIMATED_ID'] == ""){
			initDetailPageByJson($("#bus_message_dbclick"),"ESTIMATED_AUDIT_CHARGE",JSON.stringify(selected));
		}else{
			initDetailPageByJson($("#bus_message_dbclick"),"CONFIRM_AUDIT_CHARGE",JSON.stringify(selected));
		}
	}else{
		initDetailPageByJson($("#bus_message_dbclick"),dataSourceCode,JSON.stringify(selected));
		var str = dataHandle(selected['BILL_NO']);
		if(selected['WORK_ORDER_TYPE'] == "0"){
			bulidListPage($("#detailTableDbclick"),"FIN_ESTIMATED_INCOME",pageParamFormat("BILL_NO IN (" + str + ")"));
		}else{
			bulidListPage($("#detailTableDbclick"),"FIN_CONFIRM_INCOME",pageParamFormat("BILL_NO IN (" + str + ")"));
		}
	}
	
	$("#bus_message_dbclick").find("[id]").each(function(){
		$(this).attr("disabled",true);
	});
	$("#bus_message_dbclick span").each(function(){
		$(this).attr("onclick","");
	});
	
	$('#ViewModal_dbclick').modal('show');
}

//按钮启用
function buttonDisabledFalse(){
	$('#insPage').prevAll().eq(1).children().eq(0).removeAttr('disabled');
	$('#tj').attr('disabled','true');
}

//按钮禁用
function buttonDisabledTrue(){
	$('#insPage').prevAll().eq(1).children().eq(0).attr('disabled','true');
}

//获取当前时间
function getDate(){
	var myDate = new Date();
	var year = myDate.getFullYear();    
	var month = myDate.getMonth()+1;
	var day = myDate.getDate();
	var hours = myDate.getHours();
	var minutes = myDate.getMinutes();
	var seconds = myDate.getSeconds();
	if (month >= 1 && month <= 9) {
		   month = "0" + month;
	}
	if (day >= 0 && day <= 9) {
		day = "0" + day;
	}
	return year+"-"+month+"-"+day;
}

//获取主键
function getId() {
	var id = "";
	$.ajax({
		type : "GET",
		url : "/system/base?cmd=getIDByDataSourceCode&pageCode=CONTRACT_INFO_UPDATE_APPLY",
		async : false,
		dataType : "json",
		success : function(data) {
			id = data["id"];
		},
		error : function(data) {
			alert("获取主键失败，请联系管理员！" + "\t" + "错误代码：" + JSON.stringify(data));
		}
	});
	return id;
}

//参选开始事件
function ref_start(t,col,page_type){
	if(col == "BILL_NO"){
		var workOrderType = $('#WORK_ORDER_TYPE').val();
		if(workOrderType.length == 0){
			oTable.showModal('提示', "请先选择工单类型");
			return;
		}
	}
	return true;
}

//工单类型
$("#WORK_ORDER_TYPE").change(function(){
	$('#BILL_NO').val('');
	childTableDisplayTrue();
	var workOrderType = $('#WORK_ORDER_TYPE').val();
	if(workOrderType == "0"){//暂估收入
		$('#BILL_NO').next().attr("onclick","checkReference(this,'REF(FIN_ESTIMATED_INCOME_REF,BILL_NO:BILL_NO;ID:SOURCE_ID,0)','BILL_NO','INSUP')");
	}else if(workOrderType == "1"){//确认收入
		$('#BILL_NO').next().attr("onclick","checkReference(this,'REF(FIN_CONFIRM_INCOME_REF,BILL_NO:BILL_NO;ID:SOURCE_ID,0)','BILL_NO','INSUP')");
	}else{
		$('#BILL_NO').next().attr("onclick","checkReference(this,'','BILL_NO','INSUP')");
	}
})

//参选结束事件
function ref_end_code(ref_dataSourceCode,rejsonArray){
	if (ref_dataSourceCode == 'FIN_ESTIMATED_INCOME_REF' || ref_dataSourceCode == 'FIN_CONFIRM_INCOME_REF') {
		var str = dataHandle($('#BILL_NO').val());
		buildChildTable(ref_dataSourceCode,"BILL_NO IN (" + str + ")");
	}
}

//数据处理
function dataHandle(data){
	var str = "";
	var dataArray = data.split(',');
	for (var i = 0; i < dataArray.length; i++) {
		str += "'" + dataArray[i] + "',";
	}
	str = str.substring(0,str.length - 1);
	return str;
}

//构建子表
function buildChildTable(_dataSourceCode,param){
	if(_dataSourceCode.indexOf('_REF') !=  -1){
		_dataSourceCode = _dataSourceCode.substring(0,_dataSourceCode.length - 4);
	}
	if($('#insPage').children('#childTableButton').length == 0){
		buildChildTableDiv();
	}else{
		childTableDisplayFalse();
	}
	bulidListPage($("#childTable"),_dataSourceCode,pageParamFormat(param));
}

function buildChildTableDiv(){
	var html = '<div class="col-md-12 col-xs-12 col-sm-12 classifiedtitle" id="childTableLable">计收单据信息</div>'+
			   '<div class="col-md-12" id="childTableButton" style="margin-top:10px; margin-bottom:10px; padding:0;">'+
			   	   '<button type="button" class="btn btn-primary" style="margin-left:0px;" onclick="checkBillDetail();">查看计收单据明细</button>'+
			   '</div>'+
			   '<table id="childTable" data-row-style="rowStyle" style="padding:0;"></table>';
	$('#insPage').append(html);
}

//子表隐藏
function childTableDisplayTrue(){
	$('#childTableLable,#childTableButton,#childTableDetailLable').css('display','none');
	$('#childTable').bootstrapTable('destroy'); 
	$('#childTableDetail').bootstrapTable('destroy'); 
}

//子表显示
function childTableDisplayFalse(){
	$('#childTableLable,#childTableButton').css('display','inline');
}

//子表明细显示
function childTableDetailDisplayFalse(){
	$('#childTableDetailLable').css('display','inline');
}

//查看计收单据明细
function checkBillDetail(){
	var selected = $('#childTable').bootstrapTable('getSelections');
	if (selected.length != 1) {
		oTable.showModal('提示', "请选择一条数据进行操作！");
		return;
	}
	var _dataSourceCode = "";
	var workOrderType = $('#WORK_ORDER_TYPE').val();
	if(workOrderType == "0"){//暂估收入
		_dataSourceCode = "ESTIMATED_AUDIT_CHARGE";
	}else{//确认收入
		_dataSourceCode = "CONFIRM_AUDIT_CHARGE";
	}
	buildChildTableDetail(_dataSourceCode,"PARENT_ID = '" + selected[0]['BILL_NO'] + "'");
}

//构建子表明细
function buildChildTableDetail(_dataSourceCode,param){
	if($('#insPage').children('#childTableDetailLable').length  == 0){
		buildChildTableDetailDiv();
	}else{
		childTableDetailDisplayFalse();
	}
	bulidListPage($("#childTableDetail"),_dataSourceCode,pageParamFormat(param));
}

function buildChildTableDetailDiv(){
	var html = '<div class="col-md-12 col-xs-12 col-sm-12 classifiedtitle" id="childTableDetailLable">计收单据明细信息</div>'+
			   '<div class="col-md-12" id="childTableButton"></div>'+
			   '<table id="childTableDetail" data-row-style="rowStyle" style="padding:0;"></table>';
	$('#insPage').append(html);
}