buttonJson =[
             {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
             {name:'新增',fun:'tog(this),addSetUp(this)',buttonToken:'addOpenApply'},
             {name:'修改',fun:'updateRow(this),updateSetUp(this)',buttonToken:'updateOpenApply'},
             {name:'删除',fun:'delRows(this)',buttonToken:'deleteOpenApply'},
             {name:'业务开通通知单预览',fun:'openApplyPrinting(this)',buttonToken:'businessOpeningNotice'},
             {name:'公章用印申请',fun:'cachetUseSealApply(this)',buttonToken:'cachetUseSealApply'}
			];

//初始化
$(function(){
	setUp();
});

//公共设置
function setUp(){
	if(JSON.stringify(validJson) != '{}'){
		delete validJson["fields"]["SIM_TYPE"]["validators"];
	}
	$('#BILL_STATUS').parents('.col-md-4').css('display','none');
	$('#APPLY_CODE,#CUS_NAME,#APPLY_TEL,#PRO_NAME,#RELATION_PRE_ASSESSMENT').attr('readonly','true');
	$('#RELATION_PRE_ASSESSMENT').attr('placeholder','老项目新资费请参选原前评估流程');
}

//添加页面设置
function addSetUp(){
	initDetailTableList('POSTAGE_DETAIL', null, '资费明细');
	$('#APPLY_DATE').val(getDate);//申请日期
	$('#APPLY_PERSON').val(userName);//申请人
	$('#APPLY_PERSON_CODE').val(userId);//申请人主键
	$('#APPLY_DEPT').val(companyName);//申请部门
	$('#APPLY_DEPT_CODE').val(corpId);//申请部门主键
	initMultipleSelect($("#SIM_TYPE"), '');//SIM卡类型   多选框设置
	initMultipleSelect($("#FEE_TYPE"), '');//资费类型   多选框设置
	//申请人电话
	$.ajax({
		async : true,
		type : "GET",
		url : '/system/business/getaApplyTel',
		dataType : "json",
		data : {'userId':userId},
		success : function(data) {
			$('#APPLY_TEL').val(data);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			//登录超时
			if (XMLHttpRequest.getResponseHeader("TIMEOUTURL") != null) {
				window.top.location.href = XMLHttpRequest.getResponseHeader("TIMEOUTURL");
			}
			message = "获取申请人电话数据失败！";
		}
	});
}

//修改页面设置
function updateSetUp(){
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		return;
	}
	initDetailTableList('POSTAGE_DETAIL', $('#ID').val(), '资费明细');
	initMultipleSelect($("#SIM_TYPE"), selected[0]["SIM_TYPE"]);//SIM卡类型   多选框设置
	initMultipleSelect($("#FEE_TYPE"), selected[0]["FEE_TYPE"]);//资费类型   多选框设置
	getPreAssessmentAttachment($('#insPage'));
	collectCardModeChange($('#insPage'));
	var billStatus = $('#BILL_STATUS').val();
	if(billStatus != '0' && billStatus != '7'){
		localHide($("#insPage"));
		$('[data-id=SIM_TYPE],[data-id=FEE_TYPE]').css('display','block').attr('disabled',true);
		$("#btnSave").attr("disabled",true);
		$("#audit").attr("disabled",true);
	}
}

//初始化多选下拉框
function initMultipleSelect(obj,val){
	obj.find('option').each(function() {
		var text = $(this).text();
		if(Object.is(text,"==请选择==")){
	  		$(this).remove();
		}
	});
	obj.attr('class','selectpicker');
	obj.attr('multiple','multiple');
	obj.selectpicker({
		noneSelectedText: '==请选择==',
		actionsBox: true,
        'deselectAllText': '全不选',
        'selectAllText': '全选',
        width: '200px'
    });
    var values = new Array();
    if(!Object.is(val,'')){
    	var vals = val.split(',');
    	for(var i=0;i<vals.length;i++){
    		values.push(vals[i]);
    	}
    }
    obj.selectpicker('val',values);
    obj.selectpicker('refresh');
	$('.dropdown-toggle').dropdown();
}

//获取前评估
function getPreAssessmentAttachment($div){
	var preAssessmentId = $div.find('#RELATION_PRE_ASSESSMENT_ID').val();//合同主键
	if(preAssessmentId != null && preAssessmentId != ''){
		initLoadAffixJsp($div.find('#ID'));
		MultiNodeView('PRE_ASSESSMENT', preAssessmentId);
	}
}

//获取合同附件信息
function MultiNodeView(dataSourceCode, filedId) {
	var jsonParam = "{\"dataSourceCode\":\"" + dataSourceCode + "\",\"_OPPORTUNITY_CODE\":\"" + filedId + "\"}";
	$.ajax({
		url : "/system/getaffixs/getnodeafs",
		dataType : "json",
		type : "POST",
		data : {
			"jsonData" : jsonParam
		},
		success : function(data) {
			if (data != null) {
				showAffixLoadAdd();
				setTimeout("iframeFunctionPreAssessmentAttachment('" + JSON.stringify(data) + "')", 1000);
			} else {
				deleteLoadAffixJsp();
			}
		},
		error : function(e) {
			alert("附件数据加载失败！");
			console.info(e)
		}
	});
}

//增加业务附件div
function initLoadAffixJsp(obj){
	obj.after("<div style=\overflow: auto;\" class=\"col-md-12\" id=\"showaffixjspdiv\"></div>");
}

function showAffixLoadAdd(){
	var showAffixJspIframe_element = $("#showAffixJspIframe");
	if(!showAffixJspIframe_element.length >0){ 
	    setParntHeigth($("#insPage").height()+500);
	}
	$('#showaffixjspdiv').empty();
	$("#showaffixjspdiv").html("<iframe id=\"showAffixJspIframe\" name=\"showAffixJspIframe\" width=100% height=240px frameborder=\"0\" scrolling=\"auto\" src=\"showAffix.jsp\"></iframe>");
}

//采卡方式监听
$("#COLLECT_CARD_MODE").change(function(){
	collectCardModeChange($('#insPage'));
});

//采卡方式改变
function collectCardModeChange($div){
	var collectCardMode = $div.find('#COLLECT_CARD_MODE').val();
	//如果是客户自采和智网代采，SIM卡类型置灰不用编辑
	if(Object.is(collectCardMode,"客户自采") || Object.is(collectCardMode,"智网代采")){
		$div.find("#SIM_TYPE").prev().prev().attr("disabled",true);
		$div.find("#SIM_TYPE").parents('.col-md-4').css("display","none");
		$div.find("#SIM_TYPE").selectpicker('deselectAll');
	}else{
		$div.find("#SIM_TYPE").prev().prev().removeAttr('disabled');
		$div.find("#SIM_TYPE").parents('.col-md-4').css("display","inline");
	}
}

//获取资费申请编号
function getApplyCode(){
	var code = '';
	$.ajax({
		url : '/system/business/getApplyCode',
		type : "GET",
		async : false,
		dataType : "text",
		success : function(data) {
			code = data;
		}
	});
	return code
}

//重写保存方法
function savaByQuery(t,_dataSourceCode,$div){
	if(Object.is($("#PRO_TYPE option:selected").text(),"车厂指导后装")){
		if(!$("#fileList_DEPOT_CONTACT_LETTER").find('li').length>0){
			oTable.showModal('提示', "车厂联系函不能为空！");
			return;
		}
	}
	var postageDetailData = $('#POSTAGE_DETAIL_TABLE').bootstrapTable('getData');
	if(postageDetailData.length == 0){
		oTable.showModal('提示', "资费明细不能为空！");
		return;
	}
	var id = $('#ID').val();
	var BILL_STATUS = $('#BILL_STATUS').val();
	if(BILL_STATUS == ""){
		$('#BILL_STATUS').val("0");
	}
	if(id == ""){
		$('#ID').val(getId());
		var applyCode = getApplyCode();
		$("#APPLY_CODE").val(applyCode);
	}
	var buttonToken = $("#ins_or_up_buttontoken").val();
	var message = transToServer(findBusUrlByButtonTonken(buttonToken,'',_dataSourceCode) ,getJson($("#insPage")), JSON.stringify(postageDetailData), JSON.stringify(detailDeleteData));
	oTable.showModal('提示', message);
	if(message.indexOf('成功') != -1){
		choiceNotEdit("#insPage");
		$("#FEE_TYPE,#SIM_TYPE").prev().prev().css("display","inline").attr("disabled",true);
		$("#btnSave").attr("disabled",true);
		$("#audit").removeAttr('disabled');//提交按钮接触隐藏
	}
}

//ajax保存主子表
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

//返回函数
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

//点击打印预览
function openApplyPrinting(t){
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('提示', "请选择一条数据");
		return;
	}
	var billStatus = selected[0]["BILL_STATUS"];
	if(billStatus == "3"){
		window.location.href = "/system/pages/openApplyPrinting.jsp?id="+selected[0]["ID"];
	}else{
		oTable.showModal('提示', "只可打印审批完成单据");
	}
}

//点击推送公章用印申请
function cachetUseSealApply(t){
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('提示', "请选择一条数据进行推送");
		return;
	}
	var billStatus = selected[0]['BILL_STATUS'];
	if(billStatus != '3'){
		oTable.showModal('提示', "只能推送审批完成的数据");
		return;
	}
	parent.window.location = nginxPortalUrl+'/pages/t/vehicles/menuPage.jsp?totalcode=100150&totalname=公章用印申请&locationCode=100150102101&token='+token;
}

//重写提交函数
function singlePubRestAuditByTenant(t){
	var json = $('#POSTAGE_DETAIL_TABLE').bootstrapTable('getData');
	var selected = new Array();
	if(json.length>0){
		var minData = 0;
		for(var i=0;i<json.length;i++){
			var singleOrTotal = json[i]["SINGLE_OR_TOTAL"];
			var ontData = 0;
			if(singleOrTotal == "single"){
				var discountRate = json[i]["DISCOUNT_RATE"];//流量折扣率
				var phoneticsDiscountRate = json[i]["PHONETICS_DISCOUNT_RATE"];//语音折扣率
				var messageDiscountRate = json[i]["MESSAGE_DISCOUNT_RATE"];//短信折扣率
				var twoMin = 0;
				if(Number(discountRate)>Number(phoneticsDiscountRate)){
					twoMin = phoneticsDiscountRate;				
				}else{
					twoMin = discountRate;
				}
				if(Number(messageDiscountRate)<Number(twoMin)){
					twoMin = messageDiscountRate;
				}
				ontData = twoMin;
			}else if(singleOrTotal == "total"){
				var ontData = json[i]["DISCOUNT_RATE_SUM"];
			}
			
			if(minData == 0){
				minData = ontData;
			}else{
				if(Number(ontData)<Number(minData)){
					minData = ontData;
				}
			}
			
		}
		var jsona = getJson($("#insPage"));
		jsona = jsona.substring(0,jsona.length-1);
		var str = "\"" + "MIN_DISCOUNT_RATE" + "\":" +"\"" + minData + "\"";
		jsona = jsona + "," + str + "}";
		selected = JSON.parse("["+jsona+"]");
	}else{
		selected = JSON.parse("["+getJson($("#insPage"))+"]");
	}
	var type =1;
	var audit_column = singleFindAuditColumn();
	if(audit_column.length==0){
		oTable.showModal('提示', "没有设置审批字段！");
	}
	var billStatus = selected[0][audit_column];
	if(billStatus==undefined||billStatus==''||billStatus.length==0){
		oTable.showModal('提示', "审批状态为空，不能提交！");
		return;
	}
	if(billStatus != 0 && billStatus != 7){
		oTable.showModal('提示', "只能提交  已保存、已退回单据！");
		return;
	}
	var tenant_code = findTenantCode(selected[0]);
	if(tenant_code==undefined){
		tenant_code="";
	}
	var apprType="&audit_type="+type+"&audit_column="+audit_column+"&tenant_code="+tenant_code;
	var message = transToServer(findBusUrlByPublicParam(t,apprType,dataSourceCode), JSON.stringify(selected[0]));
	if(message.indexOf('成功') != -1){
		$("#"+singleFindAuditColumn()).val("1");
	}
	oTable.showModal('提示', message);
}

//重写删除函数
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
	var message = transToServer(findBusUrlByPublicParam(t,'',_dataSourceCode),delJsonString);
	oTable.showModal('modal', message);
	queryTableByDataSourceCode(t,_dataSourceCode);
}

//重写详情函数
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
	
	var mainPageParam = pageParamFormat(ParentId +" ='"+selected["ID"]+"'");
    mainPageParam = mainPageParam+"&showType=INSUP";
	bulidMaintainPage($("#bus_message_dbclick"), detailDataSourceCode, mainPageParam);
	collectCardModeChange($("#bus_message_dbclick"));
	getPreAssessmentAttachment($('#bus_message_dbclick'));
	
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
		var id = $(this).attr('id');
		if(id == 'SIM_TYPE' || id == 'FEE_TYPE'){
			$(this).html('<option selected="selected">'+selected[id]+'</option>');
		}
		$(this).attr("disabled",true);
	});
	$("#bus_message_dbclick span").each(function(){
		$(this).attr("onclick","");
	});
	$('#ViewModal_dbclick').modal('show');
}

//重写参选回调函数
function ref_end_code(ref_dataSourceCode, rejsonArray, ref_column){
	if(ref_column == 'BUSS_OPPO_CODE'){
		var bussOppoCode = $('#BUSS_OPPO_CODE').val();
		var projectData = querySingleRecord("&dataSourceCode=PROJ_RELEASED_REF1&SEARCH-OPPORTUNITY_CODE="+bussOppoCode);
		if(!jQuery.isEmptyObject(projectData)){
			$('#PROJ_RELEASED_ID').val(projectData['ID']);
			$('#PRO_NAME').val(projectData['PROJ_NAME']);
		}
		var preAssessmentData = querySingleRecord("&dataSourceCode=PRE_ASSESSMENT_REF&SEARCH-BUSINESS_NUMBER="+bussOppoCode);
		if(!jQuery.isEmptyObject(preAssessmentData)){
			$('#RELATION_PRE_ASSESSMENT_ID').val(preAssessmentData['ID']);
			$('#RELATION_PRE_ASSESSMENT').val(preAssessmentData['BUSINESS_NAME']);
		}
		getPreAssessmentAttachment($('#insPage'));
	}
};

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