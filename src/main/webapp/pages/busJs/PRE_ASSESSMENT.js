buttonJson =[
             {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
             {name:'新增',fun:'tog(this),tog1(this)',buttonToken:'addPreAssessment'},
             {name:'修改',fun:'updateRow(this),updateRow1(this)',buttonToken:'updatePreAssessment'},
             {name:'删除',fun:'delRows(this)',buttonToken:'deletePreAssessment'}
			];
			
var busId = "";//商机主键 
var BUILD_INVESTMENT_COST_T = "";//建设投资成本-投资 
var BUILD_INVESTMENT_COST_F = "";//建设投资成本-费用
var BUILD_INVESTMENT_COST_ALL = "";//建设投资总成本
		
$(function(){
	buildInvestmentHide();
	$('#BUILD_INVESTMENT').attr('onchange','buildInvestment(this.value)');//是否建设投资事件
	$('#WRITE_PERSON').attr('readonly','true');//填写人
	$('#WRITE_DATE').nextAll().css('display','none');//填写日期
	$('#BUSINESS_NAME').attr('readonly','true');//商机名称
	$('#CUSTOMER_NAME').attr('readonly','true');//客户名称
	$('#MARKETING_UNIT').attr('readonly','true');//营销单元
	$('#CUSTOMER_MANAGER').attr('readonly','true');//客户经理
	$('#BILL_STATUS').parents('.col-md-4').css('display','none');//审批状态
	$("#PRODUCT_NAME").attr({"class":"selectpicker","multiple":"","data-live-search":"true","data-actions-box":"true"});//产品名称多选
	$('.selectpicker').selectpicker({
		noneSelectedText : '==请选择==', 
		deselectAllText : '全不选',
		selectAllText : '全选'
	});
	$("#PRODUCT_NAME").prop('disabled', false);//取消禁用
	$('#PRODUCT_NAME').selectpicker('refresh');//刷新
});

function tog1(){
	initDetailTableList('POSTAGE_DETAIL', null, '资费明细');
	$('#WRITE_PERSON').val(userName);//填写人
	$('#WRITE_DATE').val(getDate());//填写日期
}

function updateRow1(t){
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		return;
	}
	initDetailTableList('POSTAGE_DETAIL', $('#ID').val(), '资费明细');
	//已保存和已退回可以修改，其他状态不能修改，表单置灰
	var billStatus = $('#BILL_STATUS').val();
	if(billStatus != '0' && billStatus != '7'){
		formDisable();
	}
	//是否建设投资 值为是时，显示相关字段
	if($('#BUILD_INVESTMENT').val() == "1"){
		buildInvestmentShow();
	}
	busId = $('#BUS_ID').val();//商机主键
	//产品多选数据回显
	var businessIds = selected[0]["BUSINESS_ID"];//包含业务主键
	var productName = selected[0]["PRODUCT_NAME"].split(',');//产品名称
	if (businessIds.length > 0) {
		var html = "";
		$.ajax({
			type : "GET",
			url : "/system/business/getProductInfo",
			async : false,
			dataType : "json",
			data : {
				"ids" : businessIds
			},
			success : function(data) {
				for (var i = 0; i < data.length; i++) {
					var proName = data[i]["PRO_NAME"];
					var id = data[i]["ID"];
					html += "<option value = '" + proName + "' id = '" + id + "'>" + proName + "</option>";
				}
			},
			error : function(data) {
				alert("获取产品名称失败，请联系管理员！" + "\t" + "错误代码：" + JSON.stringify(data));
			}
		});
		$("#PRODUCT_NAME").html(html);
		$('#PRODUCT_NAME').selectpicker('refresh');	
		$("#PRODUCT_NAME").selectpicker('val', productName);//多选赋值
	}
}

//判断是否建设投资
function buildInvestment(value){
	if(value == '1'){
		buildInvestmentShow();
	}else{
		buildInvestmentHide();
		buildInvestmentClearValue();
	}
	page_heigth = $(document.body).height();
	setParntHeigth(page_heigth);
}

function buildInvestmentChange($div){
	var value = $('#BUILD_INVESTMENT').val();
	if(value != '1'){
		var input_Ids = new Array('BUILD_INVESTMENT_COST_ALL','BUILD_INVESTMENT_COST_T','BUILD_INVESTMENT_COST_F','PURCHASING_COST','OPERATING_COSTS');
		var textarea_Ids = new Array('BUILD_INVESTMENT_DEMAND','PURCHASING_DEMAND','OPERATION_DEMAND');
		for(var i=0;i<input_Ids.length;i++){
			$div.find('#'+input_Ids[i]).parent().parent().parent().css('display','none');
		}
		for(var i=0;i<textarea_Ids.length;i++){
			$div.find('#'+textarea_Ids[i]).parent().parent().css('display','none');
		}
	}
}

//是否建设投资相关字段隐藏
function buildInvestmentHide(){
	var input_Ids = new Array('BUILD_INVESTMENT_COST_ALL','BUILD_INVESTMENT_COST_T','BUILD_INVESTMENT_COST_F','PURCHASING_COST','OPERATING_COSTS');
	var textarea_Ids = new Array('BUILD_INVESTMENT_DEMAND','PURCHASING_DEMAND','OPERATION_DEMAND');
	for(var i=0;i<input_Ids.length;i++){
		$('#'+input_Ids[i]).parent().parent().parent().css('display','none');
	}
	for(var i=0;i<textarea_Ids.length;i++){
		$('#'+textarea_Ids[i]).parent().parent().css('display','none');
	}
}

//是否建设投资相关字段显示
function buildInvestmentShow(){
	var input_Ids = new Array('BUILD_INVESTMENT_COST_ALL','BUILD_INVESTMENT_COST_T','BUILD_INVESTMENT_COST_F','PURCHASING_COST','OPERATING_COSTS');
	var textarea_Ids = new Array('BUILD_INVESTMENT_DEMAND','PURCHASING_DEMAND','OPERATION_DEMAND');
	for(var i=0;i<input_Ids.length;i++){
		$('#'+input_Ids[i]).parent().parent().parent().css('display','inline');
	}
	for(var i=0;i<textarea_Ids.length;i++){
		$('#'+textarea_Ids[i]).parent().parent().css('display','inline');
	}
	$('#BUILD_INVESTMENT_COST_ALL').attr('readonly','true');
}

//是否建设投资相关字段值清空
function buildInvestmentClearValue(){
	var ids = new Array('BUILD_INVESTMENT_COST_ALL','BUILD_INVESTMENT_COST_T','BUILD_INVESTMENT_COST_F','BUILD_INVESTMENT_DEMAND','PURCHASING_COST','PURCHASING_DEMAND','OPERATING_COSTS','OPERATION_DEMAND');
	for(var i=0;i<ids.length;i++){
		$('#'+ids[i]).val("");
	}
}

//表单禁用
function formDisable(){
	setReadonlyByDiv($("#insPage input"));
	setReadonlyByDiv($("#insPage select"));
	setReadonlyByDiv($("#insPage textarea"));
	$('#BUSINESS_NUMBER').next().css('display','none');
	$('#BUSINESS_NUMBER').attr('size','30');
	$('#PROJECT_TYPE').attr('disabled','true');
	$('#BUILD_INVESTMENT').attr('disabled','true');
	$('#anchor_ENCLOSURE').attr('disabled','true');
	$('#anchor_ENCLOSURE').next().css('display','none');
	$('#WRITE_DATE').nextAll().css('display','none');
	$('#btnSave').attr('disabled','true');
	$('#detailTableInsert').attr('disabled','true');
	$('#detailTableUpdate').attr('disabled','true');
	$('#detailTableDelete').attr('disabled','true');
	$("#PRODUCT_NAME").prop('disabled', true);
	$('#PRODUCT_NAME').selectpicker('refresh');
	getFileTypeValTo($('#insPage'));
}

//建设投资成本-投资 
$("#BUILD_INVESTMENT_COST_T").change(function(){
	BUILD_INVESTMENT_COST_T = $(this).val();
	BUILD_INVESTMENT_COST_ALL = Number(BUILD_INVESTMENT_COST_T)+Number(BUILD_INVESTMENT_COST_F);
	$('#BUILD_INVESTMENT_COST_ALL').val(BUILD_INVESTMENT_COST_ALL);
})

//建设投资成本-费用 
$("#BUILD_INVESTMENT_COST_F").change(function(){
	BUILD_INVESTMENT_COST_F = $(this).val();
	BUILD_INVESTMENT_COST_ALL = Number(BUILD_INVESTMENT_COST_T)+Number(BUILD_INVESTMENT_COST_F);
	$('#BUILD_INVESTMENT_COST_ALL').val(BUILD_INVESTMENT_COST_ALL);
})

function refHideParntHeight(body_height){
	
}

//保存
function savaByQuery(t,_dataSourceCode,$div){
	var BILL_STATUS = $('#BILL_STATUS').val();
	if(BILL_STATUS == ""){
		$('#BILL_STATUS').val("0");
	}
	if(BILL_STATUS != "" && BILL_STATUS != 0 && BILL_STATUS != 7){
		oTable.showModal('提示', "只能保存  已保存、已退回单据！");
		return;
	}
	var buttonToken = $("#ins_or_up_buttontoken").val();
	var id = $('#ID').val();
	if(id == ""){
		$('#ID').val(getId());
		buttonToken = "addPreAssessment";
	}else{
		buttonToken = "updatePreAssessment";
	}
	productNameOnChangge();
	var postageDetailData = $('#POSTAGE_DETAIL_TABLE').bootstrapTable('getData');
	var message = transToServer(findBusUrlByButtonTonken(buttonToken, '', _dataSourceCode), getJson($div), JSON.stringify(postageDetailData), JSON.stringify(detailDeleteData));
	oTable.showModal('modal', message);
	if(message == "保存成功" || message == "修改成功"){
		$('#tj').removeAttr('disabled');//提交按钮接触隐藏
	    //前评估参选商机，商机只能被参选一次，商机参选修改时需更新状态
	    if(busId != $('#BUS_ID').val()){
	    	$.ajax({
		    	async: false,
		    	type: "post",
				url: '/system/business/updateBusinessPreAssessment',
				dataType: "json",
				data:{
					"busId":busId
				},
				success: function(data){
					
				},
				error : function(data) {
					alert("更改前评估参选商机标识失败，请联系管理员！" + "\t" + "错误代码：" + JSON.stringify(data));
				}
			});
	    }
		//更改商机阶段
		var businessNumber = $('#BUSINESS_NUMBER').val();//商机编号
		$.ajax({
	    	async: false,
	    	type: "post",
			url: '/system/business/updateBusinessStage',
			dataType: "json",
			data:{
				"businessNumber":businessNumber
			},
			success: function(data){
				
			},
			error : function(data) {
				alert("更改商机阶段失败，请联系管理员！" + "\t" + "错误代码：" + JSON.stringify(data));
			}
		});
		saveInit();
	}
}

//保存后初始化
function saveInit(){
	detailDeleteData = new Array();
	bulidListPage($('#POSTAGE_DETAIL_TABLE'), 'POSTAGE_DETAIL', pageParamFormat("PRE_ID = '"+$('#ID').val()+"'"));
	$('#btnSave').removeAttr('disabled');
	$('#detailTableInsert').removeAttr('disabled');
	$('#detailTableUpdate').removeAttr('disabled');
	$('#detailTableDelete').removeAttr('disabled');
}

//返回
function back(t){
	var cache_dataSourceCode =$("#cache_dataSourceCode").val();
	var _dataSourceCode=dataSourceCode;
	if(cache_dataSourceCode!=null&&cache_dataSourceCode.length>0){
		_dataSourceCode=cache_dataSourceCode;
	}
	togByDataSourceCode(t,_dataSourceCode);
	$inspage.find('[id]').val("");
	$("#ins_or_up_buttontoken").val("");
	//不使用组件时清空file域
	clearFile();
	//清空附件清单
	$("[id^=fileList]").children("li").remove();
	window.location.reload();
}

function transToServer(url, jsonData, childJsonData, detailDeleteData){
	var message;
	$.ajax({
    	async: false,
    	type: "post",
		url: url,
		dataType: "json",
		//防止深度序列化
    	traditional: true,
		data:{
			"jsonData":jsonData,
			"childJsonData":childJsonData,
			"detailDeleteData":detailDeleteData
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

//删除
function delRowsByDataSourceCode(t,_dataSourceCode){
	var selected = JSON.parse(getSelections());
	if(selected.length < 1){
		oTable.showModal('modal', "请至少选择一条数据进行删除");
		return;
	}
	var delJson = new Array();
	for(var i=0;i<selected.length;i++){
		var billstatus = selected[i]["BILL_STATUS"];
		if(billstatus == 0 || billstatus == 7){
			delJson.push(selected[i]);
		}
	}
	if(delJson.length == 0){
		oTable.showModal('提示', "只能删除  已保存、已退回单据！");
		return;
	}
	var delJsonString = JSON.stringify(delJson);
	if(!validateDel(selected)){
		return ;
	}
	var message = transToServer(findBusUrlByPublicParam(t, '', _dataSourceCode),delJsonString);
	oTable.showModal('提示', message);
	queryTableByDataSourceCode(t,_dataSourceCode);
}

//参选
function ref_write_json(rejsonArray){
	return false;
}

//参选
function ref_end_code(dataSourceCode,rejsonArray) {
	var mappings = $("#ReferenceMapping").val();//字段映射
	var errorMessage = '';
	var mappingArray = mappings.split(";");
	for (var i = 0; i < mappingArray.length; i++) {
		var colMessage = mappingArray[i].split(":");
		var writeMessage = '';
		if (!isWriteNull(rejsonArray[0][colMessage[0]])) {
			for (var index in rejsonArray) {
				if (rejsonArray[index][colMessage[0]].length > 0) {
					writeMessage = writeMessage + rejsonArray[index][colMessage[0]] + ",";
				}
			}
			if (writeMessage.length > 0) {
				writeMessage = writeMessage.substr(0, writeMessage.length - 1);
			}
		} else {
			errorMessage = errorMessage + "参选页面，没有[" + colMessage[0] + "]字段的信息</br>";
		}
		if (globalRef_pageType == 'SELECT') {
			colMessage[1] = "SEARCH-" + colMessage[1];
		}
		$write = $("div> input[id=\'" + colMessage[1] + "\']");
		if (writeMessage.length > 0) {
			write($write, writeMessage, colMessage[1],dataSourceCode);
		}
	}
	if (errorMessage.length > 0) {
		errorMessage = "<font color='red' >" + errorMessage + "</font>";
		reference_oTable.showModal("回填情况", errorMessage);
	}
}

//参选
function write($write, writeMessage, colName, dataSourceCode) {
	if ($write.length > 0) {
		$write.val(writeMessage);
		var businesIds = $("#BUSINESS_ID").val();//包含业务主键
		if (dataSourceCode == "BUSS_TYPE") {//参选包含业务
			var html = "";
			$.ajax({
				type : "GET",
				url : "/system/business/getProductInfo",
				async : false,
				dataType : "json",
				data : {
					"ids" : businesIds
				},
				success : function(data) {
					for (var i = 0; i < data.length; i++) {
						var proName = data[i]["PRO_NAME"];
						var id = data[i]["ID"];
						html += "<option value = '" + proName + "' id = '" + id + "'>" + proName + "</option>";
					}
				},
				error : function(data) {
					alert("获取产品名称失败，请联系管理员！" + "\t" + "错误代码：" + JSON.stringify(data));
				}
			});
			$("#PRODUCT_NAME").html(html);
			$("#PRODUCT_NAME").selectpicker('refresh');
			$('.dropdown-toggle').dropdown();
		}
	} else {
		$("#" + colName).val(writeMessage);
		var businessIds = $("#BUSINESS_ID").val();//包含业务主键
		if (dataSourceCode == "BUS_BUSINESS_MESSAGE_QPG_REF") {//参选商机信息
			var array = writeMessage.split(",");
			var html = "";
			$.ajax({
				type : "GET",
				url : "/system/business/getProductInfo",
				async : false,
				dataType : "json",
				data : {
					"ids" : businessIds
				},
				success : function(data) {
					for (var i = 0; i < data.length; i++) {
						var proName = data[i]["PRO_NAME"];
						var id = data[i]["ID"];
						html += "<option value = '" + proName + "' id = '" + id + "'>" + proName + "</option>";
					}
				},
				error : function(data) {
					alert("获取产品名称失败，请联系管理员！" + "\t" + "错误代码：" + JSON.stringify(data));
				}
			});
			$("#PRODUCT_NAME").html(html);
			$("#PRODUCT_NAME").selectpicker('refresh');
			$("#PRODUCT_NAME").selectpicker('val', array);//多选赋值
			productNameOnChangge();
			$('.dropdown-toggle').dropdown();
		}
	}
}

//产品名称监听
$('#PRODUCT_NAME').change(function(){
	productNameOnChangge();
});

function productNameOnChangge(){
	var productIds = $("#PRODUCT_NAME option:selected").map(function() {
		return $(this).attr('id');
	}).get().join(",")
	$('#PRODUCT_ID').val(productIds);
}

//重写提交函数
function singlePubRestAuditByTenant(t){
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
	selected[0]['productList'] = $('#PRODUCT_ID').val();
	var apprType="&audit_type="+type+"&audit_column="+audit_column+"&tenant_code="+tenant_code;
	var message = transToServer(findBusUrlByPublicParam(t,apprType,dataSourceCode),JSON.stringify(selected[0]));
	oTable.showModal('提示', message);
	if(message.indexOf('成功') != -1){
		$("#"+singleFindAuditColumn()).val("1");
		formDisable();
	}
}

//重写双击函数
function show_dbclick(selected,title){
	$('#bus_message_dbclick').html('');
	$('#myModalLabel_dbclick').html(title);
	
	var detailDataSourceCode = dataSourceCode;
	if(selected.hasOwnProperty('SINGLE_OR_TOTAL')){
    	if(selected['ID'].indexOf('add') == -1){
    		var singleOrTotal = selected['SINGLE_OR_TOTAL'];
			if(singleOrTotal == 'single'){
		    	detailDataSourceCode = 'SINGLE_POSTAGE_DETAIL';
		    }else if(singleOrTotal == 'total'){
		    	detailDataSourceCode = 'TOTAL_POSTAGE_DETAIL';
		    }
    	}else{
    		return false;
    	}
    }
	
	var mainPageParam =pageParamFormat(ParentId +" ="+selected["ID"]);
    mainPageParam=mainPageParam+"&showType=INSUP";
	bulidMaintainPage($("#bus_message_dbclick"), detailDataSourceCode, mainPageParam);
	buildInvestmentChange($('#bus_message_dbclick'));
	
	if(!selected.hasOwnProperty('SINGLE_OR_TOTAL')){
		var detailHtml = '<div class="col-md-12 col-xs-12 col-sm-12 classifiedtitle">资费明细</div>'+
    					 '<table id="POSTAGE_DETAIL_DETAIL_TABLE"></table>';
    	$('#bus_message_dbclick').append(detailHtml);
    	bulidListPage($('#POSTAGE_DETAIL_DETAIL_TABLE'), 'POSTAGE_DETAIL', pageParamFormat("PRE_ID = '"+selected['ID']+"'"));
	}else{
		var singleOrTotal = selected['SINGLE_OR_TOTAL'];
		if(singleOrTotal == 'single'){
			bulidMaintainPageSetUp($("#bus_message_dbclick"), 'SINGLE_POSTAGE_DETAIL_PAGE');
	    }else if(singleOrTotal == 'total'){
	    	bulidMaintainPageSetUp($("#bus_message_dbclick"), 'TOTAL_POSTAGE_DETAIL_PAGE');
	    }
	}
	
	getAuditProcess(selected);
	$("#bus_message_dbclick").find("[id]").each(function(){
		if($(this).attr("id") == "PRODUCT_NAME"){
			$(this).empty();
		    $(this).append("<option value='0'>"+selected["PRODUCT_NAME"]+"</option>");
		}
		$(this).attr("disabled",true);
	});
	$("#bus_message_dbclick span").each(function(){
			$(this).attr("onclick","");
	});
	$('#ViewModal_dbclick').modal('show');
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

//嵌入公用modal
$(document.body).append(
	'<div class="modal fade" id="publicModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">'+
		'<div class="modal-dialog" style="width: 90%;height: 90%">'+
			'<div class="modal-content">'+
				'<div class="modal-header">'+
					'<button type="button" class="close" data-dismiss="modal" aria-hidden="true">'+
						'×'+
					'</button>'+
					'<h4 class="modal-title" id="publicModalLabel"></h4>'+
				'</div>'+
				'<div class="modal-body" id="publicReport"></div>'+
				'<div class="modal-footer" id="publicModalFooter">'+
					'<button type="button" class="btn btn-inverse" data-dismiss="modal">'+
						'关闭'+
					'</button>'+
				'</div>'+
			'</div>'+
		'</div>'+
	'</div>'
);