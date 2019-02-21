buttonJson =[
              {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
              {name:'新增',fun:'tog(this),addTable(this)',buttonToken:'addDoubleImport'},
              {name:'修改',fun:'updateRow(this),updateTable(this)',buttonToken:'updateDoubleImport'},
              {name:'删除',fun:'delRows(this)',buttonToken:'deleteDoubleImport'},
              {name:'导出',fun:'exportPDF(this)',buttonToken:'exportPDF'}
			];

var childDataSourceCode = "DOUBLE_IMPORT_DETAIL";
var validJsonDetail = {};
var detailTableIndex = 1;
var i = 0;
var deleteIds = "";

$(function(){
	$('#BILL_STATUS').parents('.col-md-4').css('display','none');//审批状态
	//validJson["fields"]["BILL_NO"] = {"validators": {notEmpty: {message: " 报账单号 必填不能为空!"},stringLength: {min: "0", max: "50", message: " 报账单号  长度超过50位"},regexp: {regexp: /^[0-9a-zA-Z]*$/, message: '报账单号 输入格式不合法，请输入数字和大小写字母!'}}}
})

//新增
function addTable(t){
	$('#FILLING_DATE').val(getDate());//填制日期
	$('#CREATOR_NAME').val(userName);//报账人
	$('#REPORTING_DEPARTMENT').val(companyName);//报账部门
	setAuditButton();
	buttonDisabledFalse();
	buildChildTable();
	bulidListPage($("#detailTable"),childDataSourceCode,pageParamFormat("PARENT_ID = '' "));
}

//更新
function updateTable(t){
	var selected = JSON.parse(getSelections());
	if (selected.length != 1) {
		oTable.showModal('提示', "请选择一条数据进行操作！");
		return;
	}
	setAuditButton();
	buttonDisabledFalse();
	buildChildTable();
	if(selected[0]['BILL_STATUS'] !=0 && selected[0]['BILL_STATUS'] != 7){
		buttonDisabledTrue();
	}
	bulidListPage($("#detailTable"),childDataSourceCode,pageParamFormat("PARENT_ID = '"+selected[0]['ID']+"' "));
}

//导出pdf
function exportPDF(t){
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('提示', "请选择一条数据进行操作！");
		return;
	}
	window.location.href="/system/buttonBase?cmd=button&buttonToken=exportDoubleImport&dataSourceCode=DOUBLE_IMPORT_DETAIL&token="+token+"&id="+selected[0]['ID'];
}

//重写删除
function delRowsByDataSourceCode(t,_dataSourceCode){
	var selected = JSON.parse(getSelections());
	if(selected.length < 1){
		oTable.showModal('提示', "请至少选择一条数据进行删除！");
		return;
	}
	var json = new Array();
	for(var i=0;i<selected.length;i++){
		var billstatus = selected[i]["BILL_STATUS"];
		if(billstatus == 0 || billstatus == 7){
			json.push(selected[i]);
		}
	}
	if(json.length == 0){
		oTable.showModal('提示', "只能删除  已保存、已退回单据！");
		return;
	}
	var jsonData = JSON.stringify(json);
	if(!validateDel(selected)){
		return ;
	}
	var message = transToServer(findBusUrlByPublicParam(t,'',_dataSourceCode),jsonData,"","","");
	oTable.showModal('提示', message);
	queryTableByDataSourceCode(t,_dataSourceCode);
}

//重写保存
function savaByQuery(t,_dataSourceCode,$div){
	var rowsData = $('#detailTable').bootstrapTable('getData');
	if(rowsData.length == 0){
		oTable.showModal('提示', "报账单明细不能为空！");
		return;
	}
	var message = "";
	var buttonToken = $("#ins_or_up_buttontoken").val();
	if($('#insPage #ID').val() == ""){
		$('#insPage #ID').val(getId());
	}
	if($('#insPage #BILL_STATUS').val() == ""){
		$('#insPage #BILL_STATUS').val("0");
	}
	var childJsonData = new Array();
	var defaultTotalAdValorem = 0;
	$.each(rowsData, function(i) {
		childJsonData.push(JSON.stringify(rowsData[i]));
		var adValorem = rowsData[i]['AD_VALOREM'];
		defaultTotalAdValorem = Math.round((Number(defaultTotalAdValorem) + Number(adValorem.replace(",",""))) * 100) / 100;
	});
	$('#TOTAL_AD_VALOREM').val(defaultTotalAdValorem);
	message = transToServer(findBusUrlByButtonTonken(buttonToken, '', _dataSourceCode), getJson($div), childJsonData, childDataSourceCode, deleteIds);
	oTable.showModal('提示', message);
	if (message.indexOf('成功') != -1) {
		buttonDisabledTrue();
		$('#tj').removeAttr('disabled');
	}
}

//重写返回
function back(t){
	var cache_dataSourceCode =$("#cache_dataSourceCode").val();
	var _dataSourceCode=dataSourceCode;
	if(cache_dataSourceCode!=null&&cache_dataSourceCode.length>0){
		_dataSourceCode=cache_dataSourceCode;
	}
	togByDataSourceCode(t,_dataSourceCode);
	$inspage.find('[id]').val("");
	$("#ins_or_up_buttontoken").val("");
	//重置附件组件
	//$('.file').fileinput('reset');
	//不使用组件时清空file域
	clearFile();
	//清空附件清单
	$("[id^=fileList]").children("li").remove();
	window.location.reload();
}

//重写提交
function singlePubRestAuditByTenant(t){
	var productId = "";
	var marketingUnitId = "";
	var selected = JSON.parse("["+getJson($("#insPage"))+"]");
	var tenant_code = findTenantCode(selected[0]);
	if(tenant_code == undefined){
		tenant_code = "";
	}
	var type = 1;
	var audit_column = singleFindAuditColumn();
	if($("#ID").val() == null || $("#ID").val() == ""){
		oTable.showModal('提示', "请保存后，再提交！");
		return;
	}
	if(audit_column.length == 0){
		oTable.showModal('提示', "没有设置审批字段！");
	}
	var billStatus = selected[0][audit_column];
	if(billStatus == undefined || billStatus == '' || billStatus.length == 0){
		oTable.showModal('提示', "审批状态为空，不能提交！");
		return;
	}
	if(billStatus != 0 && billStatus != 7){
		oTable.showModal('提示', "只能提交  已保存、已退回单据！");
		return;
	}
	//判断制单人单位是否属于营销单元
	$.ajax({
		type : "GET",
		async : false,
		url : "/system/finance/getCompanyById",
		dataType : "json",
		data : {
			'companyId' : companyNameID
		},
		success : function(data) {
			var tableData = $('#detailTable').bootstrapTable("getData");
			$.each(tableData, function(k) {
				productId += tableData[k]['PRODUCT_ID'] + ",";
				marketingUnitId += tableData[k]['MARKETING_UNIT_ID'] + ",";
			});
			if(data[0]['OU_TYPE'] == '0'){
				selected[0]['productList'] = productId.substring(0,productId.length-1);
			}else{
				selected[0]['productList'] = marketingUnitId.substring(0,marketingUnitId.length-1);
			}
		},
		error : function(data) {
			alert("获取部门信息失败，请联系管理员！" + "\t" + "错误代码：" + JSON.stringify(data));
		}
	}); 
	var apprType="&audit_type="+type+"&audit_column="+audit_column+"&tenant_code="+tenant_code;
	var message = transToServer(findBusUrlByPublicParam(t,apprType,dataSourceCode),JSON.stringify(selected[0]),"","","");
	if(message.indexOf('成功') != -1){
		$("#"+singleFindAuditColumn()).val("1");
	}
	oTable.showModal('提示', message);
}

function transToServer(url,jsonData,childJsonData,childDataSourceCode,deleteIds){
	var message;
	$.ajax({
    	type: "post",
    	async: false,
		url: url,
		dataType: "json",
		//防止深度序列化
    	traditional: true,
		data:{
			"jsonData":jsonData,
			"childJsonData":childJsonData,
			"childDataSourceCode":childDataSourceCode,
			"deleteIds" : deleteIds
		},
		success: function(data){
			message = data['message'];
		},
		error:function(XMLHttpRequest, textStatus, errorThrown){
			//登录超时
		    if(XMLHttpRequest.getResponseHeader("TIMEOUTURL")!=null){
		    	window.top.location.href = XMLHttpRequest.getResponseHeader("TIMEOUTURL");
		    }
			message ="请求失败";
		}
	});
    return message;
};

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
	
	if(selected.hasOwnProperty('PARENT_ID')){
		initDetailPageByJson($("#bus_message_dbclick"),childDataSourceCode,JSON.stringify(selected));
		getBusinessType("1",selected);
		getProductType("1",selected);
		getProduct("1",selected);
		$('#bus_message_dbclick #BUSINESS_TYPE').val(selected['BUSINESS_TYPE_ID']);
		$('#bus_message_dbclick #PRODUCT_TYPE').val(selected['PRODUCT_TYPE_ID']);
		$('#bus_message_dbclick #PRODUCT').val(selected['PRODUCT_ID']);
		$('#bus_message_dbclick #INVOICE_TYPE').val(selected['INVOICE_TYPE_ID']);
		$('#bus_message_dbclick #TAX_RATE').val(selected['TAX_RATE_ID']);
		$('#bus_message_dbclick #PROJECT_TYPE').val(selected['PROJECT_TYPE_ID']);
	}else{
		initDetailPageByJson($("#bus_message_dbclick"),dataSourceCode,JSON.stringify(selected));
		bulidListPage($("#detailTableDbclick"),childDataSourceCode,pageParamFormat("PARENT_ID = '"+selected['ID']+"' "));
	}
	
	$("#bus_message_dbclick").find("[id]").each(function(){
		$(this).attr("disabled",true);
	});
	$("#bus_message_dbclick span").each(function(){
		$(this).attr("onclick","");
	});
	
	$('#ViewModal_dbclick').modal('show');
}

//按钮禁用
function buttonDisabledTrue(){
	$('#insPage').prevAll().eq(1).children().eq(0).attr('disabled','true');
	$('#insert').attr('disabled','true');
	$('#update').attr('disabled','true');
	$('#delete').attr('disabled','true');
}

//按钮启用
function buttonDisabledFalse(){
	$('#insPage').prevAll().eq(1).children().eq(0).removeAttr('disabled');
	$('#insert').removeAttr('disabled');
	$('#update').removeAttr('disabled');
	$('#delete').removeAttr('disabled');
}

//子表构建
function buildChildTable(){
	if($('#insPage').children('.col-md-12').length == 0){
		initDetailTable();
		initDetailDiv();
		bulidMaintainPage($('#detailDiv'),childDataSourceCode,'');
		validJsonDetail=transToServer(findUrlParam('base','queryValids','&dataSourceCode=DOUBLE_IMPORT_DETAIL'),'','','','');
		$('#detailDiv').bootstrapValidator(validJsonDetail);
		$('#PROJECT_TYPE').parents('.col-sm-4').before("<h5 style='color:red; text-align:center;'>提示：报账时选择客户项目类型请参选商机编号报账；选择能力项目类型请参选项目编号报账</h5>");
		$('#detailDiv #PROJECT_NAME,#TAX_AMOUNT').attr('readonly','true');//项目名称、税额
		$('#detailDiv #BUSINESS_TYPE').attr('onchange','getProductType("0","")');//业务类型
		$('#detailDiv #PRODUCT_TYPE').attr('onchange','getProduct("0","")');//产品类型
		$('#detailDiv #TAX_RATE').attr('onchange','changeTaxRate(this.value)');//税率
		$('#detailDiv #MONEY').attr('onchange','changeMoney(this.value)');//金额
		$('#detailDiv #AD_VALOREM').attr('onchange','changeAdValorem(this.value)');//价税合计
		$('#detailDiv #PROJECT_TYPE').attr('onchange','changeProjectType(this.value)');//报账项目类型
	}
}

function initDetailTable(){
	var tableHtml = '<div class="col-md-12" style="margin-top:10px; margin-bottom:10px; padding:0;">'+
					   '<button type="button" class="btn btn-primary" id="insert" onclick="insertDetail();">新增</button>&nbsp;'+
					   '<button type="button" class="btn btn-warning" id="update" onclick="updateDetail();">修改</button>&nbsp;'+
					   '<button type="button" class="btn btn-danger" id="delete" onclick="deleteDetail();">删除</button>'+
			   		'</div>'+
			   		'<table id="detailTable" data-row-style="rowStyle"></table>';
	$('#insPage').append(tableHtml);
}

function initDetailDiv(){
	var fromHtml = '<div class="panel-body bv-form" id="detailDiv" novalidate="novalidate" style="display:none;"></div>';
	$('#insPage').after(fromHtml);
	var buttonHtml = '<div id="detailButton" style="display:none;">'+
					   '<button type="button" class="btn btn-success" onclick="detailSave(this)">'+
							'<span class="glyphicon glyphicon" aria-hidden="true"></span>保存'+
					   '</button>&nbsp;'+
					   '<button type="button" class="btn btn-inverse" onclick="detailBack(this)">'+
							'<span class="glyphicon glyphicon" aria-hidden="true"></span>返回'+
					   '</button>'+
					'</div>';
	$('#detailDiv').before(buttonHtml);	
}

/*****************************************子表*******************************************************/

//新增	
function insertDetail(){
	clearForm();
	detailTableShow();
	mainTableHide();
	//如果是客户经理角色填写，营销单元默认当前用户的部门名称
	if(roleId == '1000105000000000181' ){
		$('#MARKETING_UNIT').val(companyName);
		$('#MARKETING_UNIT_ID').val(companyNameID);
	}
	getBusinessType("0","");
	getProductType("0","");
	getProduct("0","");
}	

//修改
function updateDetail(){
	var selected = JSON.parse(JSON.stringify($('#detailTable').bootstrapTable('getSelections')));
	if (selected.length != 1) {
		oTable.showModal('提示', "请选择一条数据进行操作！");
		return;
	}
	detailTableShow();
	mainTableHide();
	$('#detailDiv').find("[id]").each(function() {
		$(this).val(selected[0][$(this).attr('id')]);
	});
	getBusinessType("0","");
	$('#BUSINESS_TYPE').val(selected[0]['BUSINESS_TYPE_ID']);
	getProductType("0","");
	$('#PRODUCT_TYPE').val(selected[0]['PRODUCT_TYPE_ID']);
	getProduct("0","");
	$('#PRODUCT').val(selected[0]['PRODUCT_ID']);
	$('#INVOICE_TYPE').val(selected[0]['INVOICE_TYPE_ID']);
	$('#TAX_RATE').val(selected[0]['TAX_RATE_ID']);
	$('#PROJECT_TYPE').val(selected[0]['PROJECT_TYPE_ID']);
	projectTypeRelatedOperations($('#PROJECT_TYPE').val(),'');
}

//删除
function deleteDetail(){
	var selected = JSON.parse(JSON.stringify($('#detailTable').bootstrapTable('getSelections')));
	if (selected.length == 0) {
		oTable.showModal('提示', "请选择数据进行操作！");
		return;
	}
	if(!validateDelete()){
		return ;
	}
	var ids = new Array();
	$.each(selected, function(index,row) {
		ids.push(selected[index]['ID']);
		if(selected[index]['ID'].indexOf('add') == -1 ){
			deleteIds += selected[index]['ID'] + ",";
		}
	});
	$('#detailTable').bootstrapTable('remove', {field: 'ID', values: ids});
	var tableData = $('#detailTable').bootstrapTable("getData");
	$.each(tableData, function(j) {
		tableData[j]['RMRN'] = j+1;
		detailTableIndex = j+1;
	});
	$('#detailTable').bootstrapTable('load', tableData);
	$('.pagination-info').html("共"+detailTableIndex+"条记录");
}

//保存
function detailSave(){
	$('#detailDiv').data("bootstrapValidator").validate();
    if(!$('#detailDiv').data('bootstrapValidator').isValid()){ 
    	setParntHeigth($("body").height());
        return ; 
    }
    divDisplay();
    bulidValid($('#detailDiv'),validJsonDetail);
	var json = JSON.parse(getJson($('#detailDiv')));
	if(json['ID'] == ""){
		var tableData = $('#detailTable').bootstrapTable("getData");
		if(tableData != null && tableData.length >= 0){
			detailTableIndex = tableData.length + 1;
		}
		json["RMRN"] = detailTableIndex;
		json["ID"] = "add"+i++;
		json = jsonHandle(json);
		$('#detailTable').bootstrapTable('append', json);
		$('.pagination-info').html("共"+detailTableIndex+"条记录");
	}else{
		json = jsonHandle(json);
		$('#detailTable').bootstrapTable('updateByUniqueId', {id: json['ID'], row: json}); 
	}
}

//返回
function detailBack(t){
	bulidValid($('#detailDiv'),validJsonDetail);
	divDisplay();
}

//清空表单数据
function clearForm(){
	$('#detailDiv').find("[id]").each(function() {
		$(this).val("");
	});
}

//子表div显示
function detailTableShow(){
	$('#detailDiv').css('display','block');
	$('#detailButton').css('display','block');	
}

//主表div隐藏
function mainTableHide(){
	$('#insPage').css('display','none');
	$('#insPage').prevAll().eq(1).css('display','none');
}

function divDisplay(){
	$('#insPage').css('display','block');
	$('#insPage').prevAll().eq(1).css('display','block');
	$('#detailDiv').css('display','none');
	$('#detailButton').css('display','none');
}

function refHideParntHeight(body_height){
	
}

function findValidatorDiv(code) {
	if (code == 'PROJ_RELEASED_NLXM_DOUBLE_IMPORT_REF') {
		return $('#detailDiv');
	}else if(code == 'TM_COMPANY_REF'){
		return $('#detailDiv');
	}else if(code == 'BUSINESS_MESSAGE_FIN_REF'){
		return $('#detailDiv');
	}else {
		return $("#insPage");
	}
}

//获取业务类型下拉框数据
function getBusinessType(type,selected) {
	var html = "<option value = '' selected='selected'>==请选择==</option>";
	$.ajax({
		type : "GET",
		async : false,
		url : "/system/finance/getBusinessType",
		dataType : "json",
		data : {},
		success : function(data) {
			for (var i = 0; i < data.length; i++) {
				html += "<option value = '" + data[i]["ID"] + "'>" + data[i]["TYPE_NAME"] + "</option>";
			}
		},
		error : function(data) {
			alert("获取业务类型失败，请联系管理员！" + "\t" + "错误代码：" + JSON.stringify(data));
		}
	});
	if(type == "0"){
		$("#BUSINESS_TYPE").html(html);
	}else{
		$("#bus_message_dbclick #BUSINESS_TYPE").html(html);
	}
}

//获取产品类型下拉框数据
function getProductType(type,selected){
	var businessTypeId = "";
	if(type == "0"){
		businessTypeId = $('#BUSINESS_TYPE').val();
	}else{
		businessTypeId = selected['BUSINESS_TYPE_ID'];
	}
	var html = "<option value = '' selected='selected'>==请选择==</option>";
	if(businessTypeId != ""){
		$.ajax({
			type : "GET",
			async : false,
			url : "/system/finance/getProductType",
			dataType : "json",
			data : {
				'businessTypeId':businessTypeId
			},
			success : function(data) {
				if(data.length != 0){
					for (var i = 0; i < data.length; i++) {
						html += "<option value = '" + data[i]["ID"] + "'>" + data[i]["PRO_TYPE_NAME"] + "</option>";
					}
				}
			},
			error : function(data) {
				alert("获取产品类型失败，请联系管理员！" + "\t" + "错误代码：" + JSON.stringify(data));
			}
		});
	}
	if(type == "0"){
		$("#PRODUCT_TYPE").html(html);
	}else{
		$("#bus_message_dbclick #PRODUCT_TYPE").html(html);
	}
}

//获取产品下拉框数据
function getProduct(type,selected){
	var productTypeId = "";
	if(type == "0"){
		productTypeId = $('#PRODUCT_TYPE').val();
	}else{
		productTypeId = selected['PRODUCT_TYPE_ID'];;
	}
	var html = "<option value = '' selected='selected'>==请选择==</option>";
	if(productTypeId != ""){
		$.ajax({
			type : "GET",
			async : false,
			url : "/system/finance/getProduct",
			dataType : "json",
			data : {
				'productTypeId':productTypeId
			},
			success : function(data) {
				if(data.length != 0){
					for (var i = 0; i < data.length; i++) {
						html += "<option value = '" + data[i]["ID"] + "'>" + data[i]["PRO_NAME"] + "</option>";
					}
				}	
			},
			error : function(data) {
				alert("获取产品失败，请联系管理员！" + "\t" + "错误代码：" + JSON.stringify(data));
			}
		});
	}
	if(type == "0"){
		$("#PRODUCT").html(html);
	}else{
		$("#bus_message_dbclick #PRODUCT").html(html);
	}
}

//业务类型、产品类型、产品json处理
function jsonHandle(json){
	var BUSINESS_TYPE = $('#BUSINESS_TYPE').val();//业务类型
	var PRODUCT_TYPE = $('#PRODUCT_TYPE').val();//产品类型
	var PRODUCT = $('#PRODUCT').val();//产品
	var INVOICE_TYPE = $('#INVOICE_TYPE').val();//发票类型
	var TAX_RATE = $('#TAX_RATE').val();//税率
	var PROJECT_TYPE = $('#PROJECT_TYPE').val();//报账项目类型
	if(BUSINESS_TYPE == ""){
		json['BUSINESS_TYPE'] = "";
	}else{
		json['BUSINESS_TYPE'] = $('#BUSINESS_TYPE > option:selected').text();
	}
	if(PRODUCT_TYPE == ""){
		json['PRODUCT_TYPE'] = "";
	}else{
		json['PRODUCT_TYPE'] = $('#PRODUCT_TYPE > option:selected').text();
	}
	if(PRODUCT == ""){
		json['PRODUCT'] = "";
	}else{
		json['PRODUCT'] = $('#PRODUCT > option:selected').text();
	}
	if(INVOICE_TYPE == ""){
		json['INVOICE_TYPE'] = "";
	}else{
		json['INVOICE_TYPE'] = $('#INVOICE_TYPE > option:selected').text();
	}
	if(TAX_RATE == ""){
		json['TAX_RATE'] = "";
	}else{
		json['TAX_RATE'] = $('#TAX_RATE > option:selected').text();
	}
	if(PROJECT_TYPE == ""){
		json['PROJECT_TYPE'] = "";
	}else{
		json['PROJECT_TYPE'] = $('#PROJECT_TYPE > option:selected').text();
	}
	json['BUSINESS_TYPE_ID'] = BUSINESS_TYPE;
	json['PRODUCT_TYPE_ID'] = PRODUCT_TYPE;
	json['PRODUCT_ID'] = PRODUCT;
	json['INVOICE_TYPE_ID'] = INVOICE_TYPE;
	json['TAX_RATE_ID'] = TAX_RATE;
	json['PROJECT_TYPE_ID'] = PROJECT_TYPE;
	return json;
}

//税率
function changeTaxRate(value){
	$('#TAX_AMOUNT').val("");//税额
	$('#MONEY').val("");//金额
	$('#AD_VALOREM').val("");//价税合计
}

//金额
function changeMoney(value){
	var taxRate = $('#TAX_RATE > option:selected').text();//税率
	if(taxRate != "==请选择=="){
		if(value != ""){
			value = value.replace(",","");
			taxRate = taxRate.substring(0,taxRate.length-1);
			var taxAmount = Math.round(Number(taxRate) / 100 * Number(value) * 100) / 100;
			var adValorem = Math.round(Number(value) * (1+Number(taxRate) / 100) * 100) / 100;
			$('#TAX_AMOUNT').val(taxAmount);//税额
			$('#AD_VALOREM').val(adValorem);//价税合计
		}else{
			$('#TAX_AMOUNT').val("");
			$('#AD_VALOREM').val("");
		}
		$("#detailDiv").data('bootstrapValidator').updateStatus('TAX_AMOUNT','NOT_VALIDATED').validateField('TAX_AMOUNT');
		$("#detailDiv").data('bootstrapValidator').updateStatus('AD_VALOREM','NOT_VALIDATED').validateField('AD_VALOREM');
	}
}

//价税合计
function changeAdValorem(value){
	var taxRate = $('#TAX_RATE > option:selected').text();//税率
	if(taxRate != "==请选择=="){
		if(value != ""){
			value = value.replace(",","");
			taxRate = taxRate.substring(0,taxRate.length-1);
			var money = Math.round(Number(value) / (1+Number(taxRate) / 100) * 100) / 100;
			var taxAmount = Math.round(Number(taxRate) / 100 * Number(money) * 100) / 100;
			$('#TAX_AMOUNT').val(taxAmount);//税额
			$('#MONEY').val(money);//金额
		}else{
			$('#TAX_AMOUNT').val("");
			$('#MONEY').val("");
		}
		$("#detailDiv").data('bootstrapValidator').updateStatus('TAX_AMOUNT','NOT_VALIDATED').validateField('TAX_AMOUNT');
		$("#detailDiv").data('bootstrapValidator').updateStatus('MONEY','NOT_VALIDATED').validateField('MONEY');
	}
}

//报账项目类型
function changeProjectType(value){
	projectTypeRelatedOperations(value,'change');
}

//报账项目类型相关操作
function projectTypeRelatedOperations(value,type){
	if(value == '0'){//客户项目
		$('#BUSINESS_ID').next().attr("onclick","checkReference(this,'REF(BUSINESS_MESSAGE_FIN_REF,BUSINESS_ID:BUSINESS_ID;PROJ_NAME:PROJECT_NAME;PROJ_CODE:PROJECT_CODE,1)','BUSINESS_ID','INSUP')");
		$('#PROJECT_CODE').next().attr("onclick","");
	}else if(value == '1'){//能力项目
		$('#PROJECT_CODE').next().attr("onclick","checkReference(this,'REF(PROJ_RELEASED_NLXM_DOUBLE_IMPORT_REF,PROJ_CODE:PROJECT_CODE;PROJ_NAME:PROJECT_NAME,1)','PROJECT_CODE','INSUP')");
		$('#BUSINESS_ID').next().attr("onclick","");
	}else{
		$('#BUSINESS_ID').next().attr("onclick","checkReference(this,'REF(BUSINESS_MESSAGE_FIN_REF,BUSINESS_ID:BUSINESS_ID;PROJ_NAME:PROJECT_NAME;PROJ_CODE:PROJECT_CODE,1)','BUSINESS_ID','INSUP')");
		$('#PROJECT_CODE').next().attr("onclick","checkReference(this,'REF(PROJ_RELEASED_NLXM_DOUBLE_IMPORT_REF,PROJ_CODE:PROJECT_CODE;PROJ_NAME:PROJECT_NAME,1)','PROJECT_CODE','INSUP')");
	}
	if(type == 'change'){
		$('#BUSINESS_ID,#PROJECT_CODE,#PROJECT_NAME').val('');//商机编号、项目编号、项目名称
	}
}

//获取主键
function getId() {
	var id = "";
	$.ajax({
		type : "GET",
		async : false,
		url : "/system/base?cmd=getIDByDataSourceCode&pageCode=DOUBLE_IMPORT",
		dataType : "json",
		data : {},
		success : function(data) {
			id = data["id"];
		},
		error : function(data) {
			alert("获取主键失败，请联系管理员！" + "\t" + "错误代码：" + JSON.stringify(data));
		}
	}); 
	return id;
}

//删除验证
function validateDelete(){
	if(confirm("确定要删除选中数据吗？")){
		return true;
	}else{
		return false;
	}
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

//重写组装查询参数方法，避免主表带查询参数查询完成影响子表构建
function findPageParamByDataSourceCode(_dataSourceCode) {
	var pageParam = "&dataSourceCode=" + _dataSourceCode;
	if(_dataSourceCode != "DOUBLE_IMPORT_DETAIL"){
		//组装查询参数
		$("[id^=SEARCH-]").each(function() {
			if($(this).val().length > 0){
		  		pageParam += "&"+$(this).attr("id")+"="+$(this).val();
		  	}
		});
	}
	return encodeURI(pageParam);
}