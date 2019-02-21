/*-----------------------------------------------------------明细业务处理------------------------------------------------------------*/

//初始化明细页面设置
function bulidMaintainPageSetUp($detailPage, detailPageId){
	if(detailPageId == null || detailPageId == ''){
		detailPageId = $detailPage.attr('id');
	}
	if(detailPageId == 'SINGLE_POSTAGE_DETAIL_PAGE'){
		var singleFlowArray  = new Array('FLOW','FLOW_UNIT','STANDARD_PRICE','APPLICATION_PRICE','DISCOUNT_RATE');
		var singlePhoneticsArray = new Array('PHONETICS','PHONETICS_STANDARD_PRICE','PHONETICS_APPLICATION_PRICE','PHONETICS_DISCOUNT_RATE');
		var singleMessageArray = new Array('MESSAGE','MESSAGE_STANDARD_PRICE','MESSAGE_APPLICATION_PRICE','MESSAGE_DISCOUNT_RATE');
		var singleUseDateArray = new Array('OPEN_USE_DATE');
		
		var singleGroupArray = new Array();
		singleGroupArray.push({'array':singlePhoneticsArray, 'number':'1', 'className':'col-md-1-5 col-xs-1-5 col-sm-1-5'});
		singleGroupArray.push({'array':singleMessageArray, 'number':'1', 'className':'col-md-1-5 col-xs-1-5 col-sm-1-5'});
		singleGroupArray.push({'array':singleUseDateArray, 'number':'4', 'className':'col-md-1-5 col-xs-1-5 col-sm-1-5'});
		createElement($detailPage, singleGroupArray);
		
		var singleArray = singleFlowArray.concat(singlePhoneticsArray).concat(singleMessageArray).concat(singleUseDateArray);
		modifyParentClass($detailPage, singleArray, 'col-md-1-5 col-xs-1-5 col-sm-1-5');
	}else{
		var totalFlowArray  = new Array('FLOW','FLOW_UNIT','STANDARD_PRICE',);
		var totalPhoneticsArray = new Array('PHONETICS','PHONETICS_STANDARD_PRICE');
		var totalMessageArray = new Array('MESSAGE','MESSAGE_STANDARD_PRICE');
		var totalUseDateArray = new Array('OPEN_USE_DATE');
		
		var totalGroupArray = new Array();
		totalGroupArray.push({'array':totalPhoneticsArray, 'number':'1', 'className':'col-md-4 col-xs-4 col-sm-4'});
		totalGroupArray.push({'array':totalMessageArray, 'number':'1', 'className':'col-md-4 col-xs-4 col-sm-4'});
		totalGroupArray.push({'array':totalUseDateArray, 'number':'2', 'className':'col-md-4 col-xs-4 col-sm-4'});
		createElement($detailPage, totalGroupArray);
		
		var totalArray = totalFlowArray.concat(totalPhoneticsArray).concat(totalMessageArray).concat(totalUseDateArray);
		modifyParentClass($detailPage, totalArray, 'col-md-4 col-xs-4 col-sm-4');
	}
}

//该段字段父类class属性及lable字体
function modifyParentClass($detailPage, array, className){
	for(var i=0; i<array.length; i++){
		var filedId = array[i];
		var parentPage = $detailPage.find('#'+filedId).parents('.col-md-4');
		parentPage.attr('class',className);
		parentPage.find('label').css('font-size','12px');
	}
}

//创建占用元素
function createElement($detailPage, groupArray){
	for(var i=0; i<groupArray.length; i++){
		var groupObject = groupArray[i];
		var number = groupObject['number'];
		for(var j=0; j<number; j++){
			var filedId = groupObject['array'][groupObject['array'].length-1];
			var className = groupObject['className'];
			$detailPage.find('#'+filedId).parents('.col-md-4').after('<div class="'+className+'" style="margin-top:10px;display:inline"><div class="form-group has-feedback"><div class="col-md-4 col-xs-4 col-sm-4" style="text-align: right;"></div><div class="col-md-8 col-xs-8 col-sm-8"></div></div></div>');
		}
	}
}

//明细页面设置
function setUpDetailPageAll($detailPage){
	var detailPageId = $detailPage.attr('id');
	$detailPage.find('#FLOW_UNIT').find('option[value=""]').remove();
	$detailPage.find("#DISCOUNT_RATE,#PHONETICS_DISCOUNT_RATE,#MESSAGE_DISCOUNT_RATE,#STANDARD_PRICE,#PHONETICS_STANDARD_PRICE,#MESSAGE_STANDARD_PRICE,#STANDARD_PRICE_SUM,#DISCOUNT_RATE_SUM").attr("disabled",true);
	if(detailPageId == 'SINGLE_POSTAGE_DETAIL_PAGE'){
		$detailPage.find('#APPLICATION_PRICE_SUM').attr("disabled",true);
	}
	$detailPage.find("#APPLICATION_PRICE,#PHONETICS,#PHONETICS_APPLICATION_PRICE,#MESSAGE,#MESSAGE_APPLICATION_PRICE,#APPLICATION_PRICE_SUM").attr({
	   "onkeyup": "onlyNumber(this,'"+detailPageId+"')",
	   "onblur": "onlyNumber(this,'"+detailPageId+"'),inputBlur(this)"
	});
	$detailPage.find('#FLOW').removeAttr('onfocus').attr({'onkeyup':'onlyNumber(this,\''+detailPageId+'\')','onblur':'onlyNumber(this,\''+detailPageId+'\')'});
}

//明细页面新增设置
function setUpDetailPageAdd(detailDataSourceCode){
	var $detailPage = $('#'+detailDataSourceCode+'_PAGE');
	$detailPage.find('#FLOW_UNIT').val('MB');  
}

var flowAutocomplete = new Array();//流量联想查询数据

//获取资费标准数据
function getCharges(){
	$.ajax({
		type: "GET",
		url: "/system/business?getCharges",
		dataType: "json",
		async: false,
		success: function(data) {
			flowAutocomplete = data;
		}
	});
}

//明细页面数据设置
function setDataDetailPageAll($detailPage){
	var detailBootstrapValidator = $detailPage.data('bootstrapValidator');
	getCharges();
	//流量联想查询
	$detailPage.find('#FLOW').bigAutocomplete({
		width: 185,
		data: flowAutocomplete,
		callback: function(data){
			var flow = data.title;//流量和单位字符串
			var flowUnit = flow.substr(flow.length-2);//单位
			if(flowUnit === 'MB' || flowUnit === 'GB'){
				$detailPage.find('#FLOW').val(flow.substr(0,flow.length-2));//流量赋值
				$detailPage.find('#FLOW_UNIT').val(flowUnit);//流量单位赋值
				detailBootstrapValidator.updateStatus('FLOW_UNIT', 'NOT_VALIDATED').validateField('FLOW_UNIT'); 
			}
			$detailPage.find('#STANDARD_PRICE').val(data.standardPrice);//流量标准价格赋值
			$detailPage.data('bootstrapValidator');
			linkage($detailPage);
		}
	});
	$detailPage.find('#FLOW_UNIT').change(function() {
		getStandardPrice($detailPage);
		linkage($detailPage);
	});
}

//输入框输入限制
function onlyNumber(obj, detailPageId){
	var $detailPage = $('#'+detailPageId);
	var id = obj.id;
	if(id == "PHONETICS" || id == "MESSAGE"){
		//只能输入整数
		obj.value = obj.value.replace(/[^\d]/g,'');
	}else{
		//先把非数字的都替换掉，除了数字和. 
		obj.value = obj.value.replace(/[^\d\.]/g,'');
		//必须保证第一个为数字而不是.   
		obj.value = obj.value.replace(/^\./g,'');
		//保证只有出现一个.而没有多个.   
		obj.value = obj.value.replace(/\.{2,}/g,'.');
		//保证.只出现一次，而不能出现两次以上   
		obj.value = obj.value.replace('.','$#$').replace(/\./g,'').replace('$#$','.');
		//只能输入两个小数 
		obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3');
	}
	if(id == "FLOW"){//流量
		getStandardPrice($detailPage);
	}else if(id == "PHONETICS"){//语音
		getPhoneticsStandardPrice($detailPage);
	}else if(id == "MESSAGE"){//短信
		getMessageStandardPrice($detailPage);
	}
	linkage($detailPage);
}

//根据公式获取流量标准价格
function getStandardPrice($detailPage){
	var flow = $detailPage.find("#FLOW").val();//流量
	if(flow != ""){
		var flowUnit = $detailPage.find("#FLOW_UNIT option:selected").val();//流量单位
		if(flowUnit == "GB"){
			flow = flow*1024;
		}
		$.ajax({
			type: "GET",
			url: "/system/business?getStandardPrice",
			data: {"flow":flow},
			dataType: "text",
			async: false,
			success: function(data) {
				$detailPage.find("#STANDARD_PRICE").val(data);
			}
		});
	}
}

//根据设置标准获取语音标准价格
function getPhoneticsStandardPrice($detailPage){
	var phonetics = $detailPage.find("#PHONETICS").val();
	$.ajax({
		type: "GET",
		url: "/system/business?getPhoneticsStandardPrice",
		data:{"phonetics":phonetics},
		dataType: "text",
		async: false,
		success: function(data) {
			$detailPage.find("#PHONETICS_STANDARD_PRICE").val(data);
		}
	});
}

//根据设置标准获取短信标准价格
function getMessageStandardPrice($detailPage){
	var message = $detailPage.find("#MESSAGE").val();
	$.ajax({
		type: "GET",
		url: "/system/business?getMessageStandardPrice",
		data:{"message":message},
		dataType: "text",
		async: false,
		success: function(data) {
			$detailPage.find("#MESSAGE_STANDARD_PRICE").val(data);
		}
	});
}

//联动
function linkage($detailPage){
	var detailPageId = $detailPage.attr('id');
	
	var standardPrice = $detailPage.find("#STANDARD_PRICE").val();//流量标准价格
	if(standardPrice == ""){
		standardPrice = 0;
	}else{
		standardPrice = Number(standardPrice);
	}
	
	var applicationPrice = $detailPage.find("#APPLICATION_PRICE").val();//流量申请价格
	if(applicationPrice == ""){
		applicationPrice = 0;
	}else{
		applicationPrice = Number(applicationPrice);
	}
	
	var phoneticsStandardPrice = $detailPage.find("#PHONETICS_STANDARD_PRICE").val();//语音标准价格
	if(phoneticsStandardPrice == ""){
		phoneticsStandardPrice = 0;
	}else{
		phoneticsStandardPrice = Number(phoneticsStandardPrice);
	}
	
	var phoneticsApplicationPrice = $detailPage.find("#PHONETICS_APPLICATION_PRICE").val();//语音申请价格
	if(phoneticsApplicationPrice == ""){
		phoneticsApplicationPrice = 0;
	}else{
		phoneticsApplicationPrice = Number(phoneticsApplicationPrice);
	}
	
	var messageStandardPrice = $detailPage.find("#MESSAGE_STANDARD_PRICE").val();//短信标准价格
	if(messageStandardPrice == ""){
		messageStandardPrice = 0;
	}else{
		messageStandardPrice = Number(messageStandardPrice);
	}
	
	var messageApplicationPrice = $detailPage.find("#MESSAGE_APPLICATION_PRICE").val();//短信申请价格
	if(messageApplicationPrice == ""){
		messageApplicationPrice = 0;
	}else{
		messageApplicationPrice = Number(messageApplicationPrice);
	}
	
	var standardPriceSum = standardPrice+phoneticsStandardPrice+messageStandardPrice
	$detailPage.find("#STANDARD_PRICE_SUM").val(standardPriceSum.toFixed(2));
	
	if(detailPageId == 'SINGLE_POSTAGE_DETAIL_PAGE'){//单项
		
		if(standardPrice == 0){
			$detailPage.find("#DISCOUNT_RATE").val(0);
		}else{
			var discountRate = applicationPrice/standardPrice*100;
			$detailPage.find("#DISCOUNT_RATE").val(discountRate.toFixed(1));
		}
		
		if(phoneticsStandardPrice == 0){
			$detailPage.find("#PHONETICS_DISCOUNT_RATE").val(0);
		}else{
			var phoneticsDiscountRate = phoneticsApplicationPrice/phoneticsStandardPrice*100;
			$detailPage.find("#PHONETICS_DISCOUNT_RATE").val(phoneticsDiscountRate.toFixed(1));
		}
		
		if(messageStandardPrice == 0){
			$detailPage.find("#MESSAGE_DISCOUNT_RATE").val(0);
		}else{
			var messageDiscountRate = messageApplicationPrice/messageStandardPrice*100;
			$detailPage.find("#MESSAGE_DISCOUNT_RATE").val(messageDiscountRate.toFixed(1));
		}
		
		var applicationPriceSum = applicationPrice+phoneticsApplicationPrice+messageApplicationPrice
		$detailPage.find("#APPLICATION_PRICE_SUM").val(applicationPriceSum.toFixed(2));
		
	}else if(detailPageId == 'TOTAL_POSTAGE_DETAIL_PAGE'){//总和
		
		var applicationPriceSum = $detailPage.find("#APPLICATION_PRICE_SUM").val();
		if(standardPriceSum == ""){
			$detailPage.find("#DISCOUNT_RATE_SUM").val(0);
		}else{
			var discountRateSum = applicationPriceSum/standardPriceSum*100;
			$detailPage.find("#DISCOUNT_RATE_SUM").val(discountRateSum.toFixed(1));
		}
		
	}
}

/*-----------------------------------------------------------明细生成------------------------------------------------------------*/

var index = 0;
var SINGLE_POSTAGE_DETAIL_JSON_DETAIL = {};
var TOTAL_POSTAGE_DETAIL_JSON_DETAIL = {};
var detailDeleteData = new Array();

//初始化子表列表
function initDetailTableList(detailDataSourceCode, parentId, lableName){
	var detailTableHtml = '<div id="'+detailDataSourceCode+'_TABLE_BUTTON" class="col-md-12" style="margin-top:10px; margin-bottom:10px; padding:0;">'+
							 '<button id="detailTableInsert" type="button" class="btn btn-primary" onclick="openModel(\''+detailDataSourceCode+'\');">新增</button>'+
							 '<button id="detailTableUpdate" type="button" class="btn btn-warning" onclick="detailTableUpdateClick(\''+detailDataSourceCode+'\');">修改</button>'+
				   			 '<button id="detailTableDelete" type="button" class="btn btn-danger" onclick="detailTableDeleteClick(\''+detailDataSourceCode+'\');">删除</button>'+
						  '</div>'+
						  '<table id="'+detailDataSourceCode+'_TABLE" data-row-style="rowStyle"></table>';
	$('#insPage').append(detailTableHtml);
	setDetailLable($('#'+detailDataSourceCode+'_TABLE_BUTTON'), lableName);
	initDetailTable($('#'+detailDataSourceCode+'_TABLE'), detailDataSourceCode, parentId);
}

//初始化子表
function initDetailTable($detailTable, detailDataSourceCode, parentId){
	bulidListPage($detailTable, detailDataSourceCode, pageParamFormat("PRE_ID = '"+parentId+"'"));
	
	var detailPageArray = new Array('TOTAL_POSTAGE_DETAIL','SINGLE_POSTAGE_DETAIL');
	for(var i=0; i<detailPageArray.length; i++){
		detailDataSourceCode = detailPageArray[i];
		initDetailDiv(null, detailDataSourceCode);
		var $detailPage = $('#'+detailDataSourceCode+'_PAGE');
		bulidMaintainPage($detailPage, detailDataSourceCode, '', 'add');
		bulidMaintainPageSetUp($detailPage);
		setValidJsonDetail(detailDataSourceCode);
	}
}

//初始化子表页面
function initDetailDiv($detailTable, detailDataSourceCode){
	var fromHtml = '<div class="panel-body bv-form" id="'+detailDataSourceCode+'_PAGE" novalidate="novalidate" style="display:none;"></div>';
	$('#insPage').after(fromHtml);
	var buttonHtml = '<div id="'+detailDataSourceCode+'_PAGE_BUTTON" style="display:none;">'+
					   '<button type="button" class="btn btn-success" onclick="detailPageSave(\''+detailDataSourceCode+'\')">'+
							'<span class="glyphicon glyphicon" aria-hidden="true"></span>保存'+
					   '</button>'+
					   '<button type="button" class="btn btn-inverse" onclick="detailPageBack(\''+detailDataSourceCode+'\')">'+
							'<span class="glyphicon glyphicon" aria-hidden="true"></span>返回'+
					   '</button>'+
					'</div>';
	$('#'+detailDataSourceCode+'_PAGE').before(buttonHtml);
}

//设置验证信息
function setValidJsonDetail(detailDataSourceCode){
	if(detailDataSourceCode == 'SINGLE_POSTAGE_DETAIL'){//单项资费明细
		SINGLE_POSTAGE_DETAIL_JSON_DETAIL = transToServer(findUrlParam('base', 'queryValids', '&dataSourceCode='+detailDataSourceCode), '');
		$('#'+detailDataSourceCode+'_PAGE').bootstrapValidator(SINGLE_POSTAGE_DETAIL_JSON_DETAIL);
	}else if(detailDataSourceCode == 'TOTAL_POSTAGE_DETAIL'){//总和资费明细
		TOTAL_POSTAGE_DETAIL_JSON_DETAIL = transToServer(findUrlParam('base', 'queryValids', '&dataSourceCode='+detailDataSourceCode), '');
		$('#'+detailDataSourceCode+'_PAGE').bootstrapValidator(TOTAL_POSTAGE_DETAIL_JSON_DETAIL);
	}
}

//验证信息切换
function isValidJsonDetail(detailDataSourceCode){
	if(detailDataSourceCode == 'SINGLE_POSTAGE_DETAIL'){//单项资费明细
		return SINGLE_POSTAGE_DETAIL_JSON_DETAIL;
	}else if(detailDataSourceCode == 'TOTAL_POSTAGE_DETAIL'){//总和资费明细
		return TOTAL_POSTAGE_DETAIL_JSON_DETAIL;
	}
	return null;
}

//设置明细标签
function setDetailLable($detailTable, labelName){
	var lableHtml = '<div class="col-md-12 col-xs-12 col-sm-12 classifiedtitle">'+labelName+'</div>';
	$detailTable.before(lableHtml);
}

//打开弹窗
function openModel(detailDataSourceCode){
	var insertHtml = '<div style="height: 35px;text-align: center;line-height: 35px;">'+
						 '<button class="btn btn-info" onclick="detailTableInsertClick(\'SINGLE_POSTAGE_DETAIL\');">按单项申请</button>&nbsp;&nbsp;'+
						 '<button class="btn btn-info" onclick="detailTableInsertClick(\'TOTAL_POSTAGE_DETAIL\');">按总价申请</button>'+
					 '</div>';
	$("#publicReport").html(insertHtml);//内容
	$("#publicModalLabel").html('选择资费申请类型');//标题
	$("#publicModal").modal('show');//展示模态框
}

//子表新增
function detailTableInsertClick(detailDataSourceCode){
	clearForm(detailDataSourceCode);
	detailTableShow(detailDataSourceCode);
	mainTableHide();
	setUpDetailPageAdd(detailDataSourceCode);
	setUpDetailPage(detailDataSourceCode);
	hideModel($('#publicModal'));
}

//子表修改
function detailTableUpdateClick(detailDataSourceCode){
	var $detailTable = $('#POSTAGE_DETAIL_TABLE');
	var selected = JSON.parse(JSON.stringify($detailTable.bootstrapTable('getSelections')));
	if (selected.length != 1) {
		oTable.showModal('提示', "请选择一条数据进行操作！");
		return;
	}
	if(selected[0]['SINGLE_OR_TOTAL'] == 'single'){
		detailDataSourceCode = 'SINGLE_POSTAGE_DETAIL';
	}else{
		detailDataSourceCode = 'TOTAL_POSTAGE_DETAIL';
	}
	detailTableShow(detailDataSourceCode);
	mainTableHide();
	setUpDetailPage(detailDataSourceCode);
	var $detailPage = $('#'+detailDataSourceCode+'_PAGE');
	$detailPage.find("[id]").each(function() {
		$(this).val(selected[0][$(this).attr('id')]);
	});
}

//子表删除
function detailTableDeleteClick(detailDataSourceCode){
	var $detailPage = $('#'+detailDataSourceCode+'_PAGE');
	var $detailTable = $('#POSTAGE_DETAIL_TABLE');
	var selected = JSON.parse(JSON.stringify($detailTable.bootstrapTable('getSelections')));
	if (selected.length == 0) {
		oTable.showModal('提示', "请选择数据进行操作！");
		return;
	}
	if(!validateDelete()){
		return;
	}
	var ids = new Array();
	$.each(selected, function(index,row) {
		ids.push(selected[index]['ID']);
		if(selected[index]['ID'].indexOf('add') == -1){
			setDeleteData(detailDataSourceCode, selected[index]['ID']);
		}
	});
	$detailTable.bootstrapTable('remove', {field: 'ID', values: ids});
	var tableData = JSON.parse(JSON.stringify($detailTable.bootstrapTable("getData")));
	var detailTableIndex = 0;
	$.each(tableData, function(j) {
		tableData[j]['RMRN'] = j+1;
		detailTableIndex = j+1;
	});
	$detailTable.bootstrapTable('load', {'total':tableData.length, 'rows':tableData});
	$detailTable.parent().next().next().find('.pagination-info').html("共"+detailTableIndex+"条记录");
}

//删除验证
function validateDelete(){
	if(confirm("确定要删除选中数据吗？删除后请点击保存操作才可生效")){
		return true;
	}else{
		return false;
	}
}

//删除id添加到集合
function setDeleteData(detailDataSourceCode, deleteId){
	detailDeleteData.push(deleteId);
}

//清空表单数据
function clearForm(detailDataSourceCode){
	$('#'+detailDataSourceCode+'_PAGE').find("[id]").each(function() {
		$(this).val("");
	});
}

//子表div显示
function detailTableShow(detailDataSourceCode){
	$('#'+detailDataSourceCode+'_PAGE').css('display','block');
	$('#'+detailDataSourceCode+'_PAGE_BUTTON').css('display','block');	
}

//主表div隐藏
function mainTableHide(){
	$('#insPage').css('display','none');
	$('#insPage').prevAll().eq(1).css('display','none');
}

//子表页面设置
function setUpDetailPage(detailDataSourceCode){
	var $detailPage = $('#'+detailDataSourceCode+'_PAGE');
	$('.form_date').datetimepicker({
		minView: 'month',         //设置时间选择为年月日 去掉时分秒选择
		format:'yyyy-mm-dd',
	    weekStart: 1,
	    todayBtn:  1,
	    autoclose: 1,
	    todayHighlight: 1,
	    startView: 2,
	    forceParse: 0,
	    showMeridian: 1,
	    language: 'zh-CN'              //设置时间控件为中文
	});
	$detailPage.find('.form_date').find("input").each(function(){
		var column =$(this).attr("id");
		$(this).change(function() {
			$detailPage.data('bootstrapValidator').updateStatus(column, 'NOT_VALIDATED', null).validateField(column);
		});
	});
	setUpDetailPageAll($detailPage);
	setDataDetailPageAll($detailPage);
}

//关闭模态框
function hideModel($id){
	$($id).modal('hide');//关闭模态框
}

//子表保存
function detailPageSave(detailDataSourceCode){
	var $detailPage = $('#'+detailDataSourceCode+'_PAGE');
	var $detailTable = $('#POSTAGE_DETAIL_TABLE');
	$detailPage.data("bootstrapValidator").validate();
    if(!$detailPage.data('bootstrapValidator').isValid()){ 
    	setParntHeigth($("body").height());
        return; 
    }
    divDisplay(detailDataSourceCode);
    bulidValid($detailPage, isValidJsonDetail(detailDataSourceCode));
    var detailTableIndex = 0;
	var json = JSON.parse(getJson($detailPage));
	if(json['ID'] == ""){	
		var tableData = $detailTable.bootstrapTable("getData");
		if(tableData != null && tableData.length >= 0){
			detailTableIndex = tableData.length + 1;
		}
		json["RMRN"] = detailTableIndex;
		json["ID"] = "add"+index++;
		json = jsonHandle(json, detailDataSourceCode);
		$detailTable.bootstrapTable('append', json);
		$detailTable.parent().next().next().find('.pagination-info').html("共"+detailTableIndex+"条记录");
	}else{
		json = jsonHandle(json, detailDataSourceCode);
		$detailTable.bootstrapTable('updateByUniqueId', {id: json['ID'], row: json}); 
	}
}

//子表返回
function detailPageBack(detailDataSourceCode){
	bulidValid($('#'+detailDataSourceCode+'_PAGE'), isValidJsonDetail(detailDataSourceCode));
	divDisplay(detailDataSourceCode);
}

//隐藏明细信息，显示主表信息
function divDisplay(detailDataSourceCode){
	$('#insPage').css('display','block');
	$('#insPage').prevAll().eq(1).css('display','block');
	$('#'+detailDataSourceCode+'_PAGE').css('display','none');
	$('#'+detailDataSourceCode+'_PAGE_BUTTON').css('display','none');
}

//json处理
function jsonHandle(jsonObj, detailDataSourceCode){
	var postageDetail = '';
	if(detailDataSourceCode === "SINGLE_POSTAGE_DETAIL"){//单项
		postageDetail = "<div style='text-align: left;'>"+
							"流量："+jsonObj["FLOW"]+jsonObj["FLOW_UNIT"]+"，流量标准价格："+jsonObj["STANDARD_PRICE"]+"，流量申请价格："+jsonObj["APPLICATION_PRICE"]+"，流量折扣率："+jsonObj["DISCOUNT_RATE"]+"<br/>"+
				   			"语音："+jsonObj["PHONETICS"]+"，语音标准价格："+jsonObj["PHONETICS_STANDARD_PRICE"]+"，语音申请价格："+jsonObj["PHONETICS_APPLICATION_PRICE"]+"，语音折扣率："+jsonObj["PHONETICS_DISCOUNT_RATE"]+"<br/>"+
				   			"短信："+jsonObj["MESSAGE"]+"，短信标准价格："+jsonObj["MESSAGE_STANDARD_PRICE"]+"，短信申请价格："+jsonObj["MESSAGE_APPLICATION_PRICE"]+"，短信折扣率："+jsonObj["MESSAGE_DISCOUNT_RATE"]+"<br/>"+
				   			"标准价格总计："+jsonObj["STANDARD_PRICE_SUM"]+"，申请价格总计："+jsonObj["APPLICATION_PRICE_SUM"]+
			   			"</div>";
		jsonObj["POSTAGE_DETAIL"] = postageDetail;
		jsonObj["SINGLE_OR_TOTAL"] = "single";
	}else if(detailDataSourceCode === "TOTAL_POSTAGE_DETAIL"){//总和
		postageDetail = "<div style='text-align: left;'>"+
							"流量："+jsonObj["FLOW"]+jsonObj["FLOW_UNIT"]+"，流量标准价格："+jsonObj["STANDARD_PRICE"]+"<br/>"+
				   			"语音："+jsonObj["PHONETICS"]+"，语音标准价格："+jsonObj["PHONETICS_STANDARD_PRICE"]+"<br/>"+
				   			"短信："+jsonObj["MESSAGE"]+"，短信标准价格："+jsonObj["MESSAGE_STANDARD_PRICE"]+"<br/>"+
				   			"标准价格总计："+jsonObj["STANDARD_PRICE_SUM"]+"，申请价格总计："+jsonObj["APPLICATION_PRICE_SUM"]+"，折扣率："+jsonObj["DISCOUNT_RATE_SUM"]+
			   			"</div>";
		jsonObj["POSTAGE_DETAIL"] = postageDetail;
		jsonObj["SINGLE_OR_TOTAL"] = "total";
	}
	return jsonObj;
}