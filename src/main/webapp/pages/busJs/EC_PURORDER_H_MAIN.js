/**
 * 订单采购主表
 */

buttonJson =[	
				{name:'查询',fun:'queryTable(this)',buttonToken:'query'},
				{name:'新增',fun:'tog(this),changeReadonly(),setAuditButton(),addTable(this)',buttonToken:'add'},
				{name:'修改',fun:'updateRow(this),editBut(this),changeReadonly(),setAuditButton(),updateTable(this)',buttonToken:'update'},
				{name:'删除',fun:'delRows(this)',buttonToken:'delete'},/*
                {name:'合同明细',fun:'view(this)',buttonToken:'audit'},*/
//				{name:'提交',fun:'approval(this,1)',buttonToken:'audit'},
				{name:'导入',fun:'upload(this)',buttonToken:'upload'},
				{name:'上传归档文件',fun:'uploadArchive(this)',buttonToken:'upload'}
			];

function changeReadonly(){
	$("[name='REVIEW_STATUS']").parent().parent().parent().css("display","none");
	$("[name='BILL_STATUS']").parent().parent().parent().css("display","none");
	
}
function editBut(obj){
	var selected = JSON.parse(getSelections());
	if(selected[0].BILL_STATUS != '0'&&selected[0].BILL_STATUS != '7'){
		$("#tog_titleName").parent().find(".glyphicon-saved").parent().attr("disabled","true");
	}
}

var saved=null;
function back(t){

	window.location.reload();
	if(model=="add"&&saved!="true"){
		$.ajax({
			type:"get",
			url:"/system/purchase/delByParentId?id="+id,
			success:function(){
			},
			error:function(){
			}
		});	
		
	}
}

function save(t) {
	$inspage.data("bootstrapValidator").validate();
	if (!$inspage.data('bootstrapValidator').isValid()) {
		return;
	}

	var cache_dataSourceCode = $("#cache_dataSourceCode").val();
	var _dataSourceCode = dataSourceCode;
	if (cache_dataSourceCode != null && cache_dataSourceCode.length > 0) {
		_dataSourceCode = cache_dataSourceCode;

	}
	saveByDataSourceCode(t, _dataSourceCode);

}

function saveByDataSourceCode(t, _dataSourceCode) {
	savaByQuery(t, _dataSourceCode, $inspage);
}

function savaByQuery(t, _dataSourceCode, $div) {
	
	var purorderDemandReply = $('#PURORDER_DEMAND_REPLY option:selected').val();
	if(purorderDemandReply == '1'){
		if(!isUploadEnclosure()){
			oTable.showModal('提示', '请上传采购需求批复相关附件');
			return;
		}
	}
	
	// var buttonToken=$(t).attr("buttonToken");
	var buttonToken = "";
	if (model=="add") {
		buttonToken = "add";
		//$("#ID").val(getId());
	} else {
		buttonToken = "update";
	}
	var message = transToServer(findBusUrlByButtonTonken(buttonToken, '',
			_dataSourceCode), getJson($("#insPage")));
	saved="true";
	oTable.showModal('modal', message);
//	queryTableByDataSourceCode(t, "EC_PURORDER_H_MAIN");
//	queryTable(t);
	$("[name='BILL_STATUS']").val("0");
	
	if (message == "保存成功" || message == "修改成功") {
		formDisable();
		$("#tj").removeAttr('disabled');
	}
	
}

//判断是否上传附件
function isUploadEnclosure(){
	var showfilesAttachment = $('#showfiles_ATTACHMENT').html();
	if(showfilesAttachment.indexOf('此条数据未上传附件') == -1 && showfilesAttachment != null && showfilesAttachment != ''){
		return true;
	}
	return false;
}

function formDisable() {

	setReadonlyByDiv($("#insPage input"));
	setReadonlyByDiv($("#insPage select"));
	$('#bulidPage').find('.panel-body').find('div').eq(1).find('button').eq(0)
			.attr('disabled', 'true');
}

//拖拽上传
function uploadAffixes(t){
    var pageCode = 'EC_PURORDER_H';
    
    var l = (screen.width - 600) / 2; 
    var t = (screen.height - 450) / 2; 
    var s = 'width=' + 600 + ', height=' + 450 + ', top=' + t + ', left=' + l; 
    s += ', toolbar=no, scrollbars=no, menubar=no, location=no, resizable=no'; 
    open(context+"/pages/fileupload.jsp?pageCode="+pageCode,"导入", s); 
}

function upload(){
    var pageCode = 'EC_PURORDER_H';
    var pageFileName="订单采购导入测试.xls";//下载导入模板的文件名

    var l = (screen.width - 500) / 2; 
    var t = (screen.height - 300) / 2; 
    var s = 'width=' + 500 + ', height=' + 300 + ', top=' + t + ', left=' + l; 
    s += ', toolbar=no, scrollbars=no, menubar=no, location=no, resizable=no'; 
    open(context+"/pages/excelImport.jsp?pageCode="+pageCode+"&pageFileName=" + pageFileName,"导入", s); 
}
//归档
function uploadArchive(t){
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('modal', "请选择一条数据进行操作");
		return;
	}
	var ID=selected[0]["ID"];
	var bs_code=selected[0]["ATTACHMENT"];
	//
    var pageCode = 'EC_PURORDER_H';

    var l = (screen.width - 500) / 2; 
    var t = (screen.height - 300) / 2; 
    var s = 'width=' + 500 + ', height=' + 300 + ', top=' + t + ', left=' + l; 
    s += ', toolbar=no, scrollbars=no, menubar=no, location=no, resizable=no'; 
    open(context+"/pages/importarchive.jsp?pageCode="+pageCode+"&id="+ID+"&bs_code="+dataSourceCode+"&batchno="+bs_code,"上传归档文件", s); 
}


/**
 * 去字表EC_PURORDER_H
 */
function view(t) {

	// 查看子表相关配置
	var pageCode = 'EC_PURORDER_H';// 子表数据源
	var pageName = '订单明细';// 列表显示名称
	var ParentPKField = 'EC_PURORDER_H_MAIN_ID';// 主表主键在子表中字段名

	var selected = JSON.parse(getSelections());
	if (selected.length != 1) {
		oTable.showModal('modal', "请选择一条数据进行操作");
		return;
	}
	//alert(selected[0]["ID"])
	window.location.href = context + "/pages/childTableModify_EC_PURORDER_H.jsp?pageCode="
			+ pageCode + "&pageName=" + pageName + "&ParentPKField="
			+ ParentPKField + "&ParentPKValue=" + selected[0]["ID"] ;
}


function approval(t,type){
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('modal', "请选择一条数据进行操作");
		return;
	}
	var billStatus = selected[0]['BILL_STATUS'];
	if(billStatus != ""){
		if (billStatus != 0 && billStatus != 7) {
			oTable.showModal('modal', "只能提交【审核状态】为 【 已保存】、【已退回】的单据");
			return;
		}
	}
	var vdataSourceCode="EC_PURORDER_H_MAIN";
	var apprType="&audit_type="+type+"&audit_column=BILL_STATUS";
	var message = transToServer(findBusUrlByPublicParam(t,apprType,vdataSourceCode),JSON.stringify(selected[0]));
	oTable.showModal('modal', message);
	queryTable(t);
}
$(function(){
	$.ajax({
		type:"get",
		url:"/system/purchase/getBillNo",
		success:function(data){
			if(data!=null){
				$("#BILLNO").val(data);
			}else{
				alert("系统出错，请联系管理员");
			}
		},
		error:function(){
			alert("系统出错，请联系管理员");
		}
	});
	$("#PROCUREMENT_TYPE").val("订单采购");
	$("#PROCUREMENT_TYPE").attr("disabled","true");
	$("#PURCHASE_ORDER_DATE").val(getNowFormatDate());

	if(token!=null&&token.length>0){
		
		$.ajax({
			type:"get",
			url:"/system/purchase/getLoginUserDept?token="+token,
			success:function(data){
				if(data!=null){
					$("#DEPARTMENT_DEMAND").val(data);
				}else{
					alert("系统出错，请联系管理员");
				}
			},
			error:function(){
				alert("系统出错，请联系管理员");
			}
		});
		$.ajax({
			type:"get",
			url:"/system/purchase/getLoginUserName?token="+token,
			success:function(data){
				if(data!=null){
					$("#FILL_PERSON").val(data);
				}else{
					alert("系统出错，请联系管理员");
				}
			},
			error:function(){
				alert("系统出错，请联系管理员");
			}
		});
	}
	$("#LEVIED_TOTAL").val(0);
	$("#LEVIED_TOTAL").attr("disabled","true");
	
    $table.on('dbl-click-row.bs.table', function (e, row, ele,field) {

    	var tableHtml = '<div class="col-md-12" style="margin-top:10px; margin-bottom:10px; padding:0; id="detailTable" ">'+
    	'<iframe id="iframe4child" src="" style="width: 100%; border: 0;height: 600px;"></iframe></div>' ;
    	$('#bus_message_dbclick').append(tableHtml);
    	var pageCode = 'EC_PURORDER_H';// 子表数据源
    	var pageName = '订单明细';// 列表显示名称
    	var ParentPKField = 'EC_PURORDER_H_MAIN_ID';// 主表主键在子表中字段名

    	var iframeUrl = context + "/pages/childTableModify_EC_PURORDER_H_MAIN.jsp?token="+token+"&pageCode="
    			+ pageCode + "&pageName=" + pageName + "&ParentPKField="
    			+ ParentPKField + "&ParentPKValue=" + row["ID"]  ;
    	$("#iframe4child").attr("src",iframeUrl);
    	$('#iframe4child').load(function(){
    		$('#iframe4child').contents().find('#toolbar').css('display',"none");
    	});
    });
});
function getNowFormatDate() {
    var date = new Date();
    var seperator1 = "-";
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = year + seperator1 + month + seperator1 + strDate;
    return currentdate;
}


function initDetailTable(){
	var tableHtml = '<div class="col-md-12" style="margin-top:10px; margin-bottom:10px; padding:0; id="detailTable" ">'+
	'<iframe id="iframe4child" src="" style="width: 100%; border: 0;height: 600px;"></iframe></div>' ;
	$('#insPage').append(tableHtml);
}
//按钮启用
function buttonDisabledFalse(){
	$('#insPage').prevAll().eq(1).children().eq(0).removeAttr('disabled');
	$('#insert').removeAttr('disabled');
	$('#update').removeAttr('disabled');
	$('#delete').removeAttr('disabled');
}
var model=null;
var id=null;
//主表新增
function addTable(t){
	model="add";
	initDetailTable();

	var pageCode = 'EC_PURORDER_H';// 子表数据源
	var pageName = '订单明细';// 列表显示名称
	var ParentPKField = 'EC_PURORDER_H_MAIN_ID';// 主表主键在子表中字段名
	$.ajax({
		type:"get",
		url:"/system/purchase/getBillNo",
		success:function(data){
			if(data!=null){
				data=data+"";
				id=data;
				$("#ID").val(data);
				var iframeUrl = context + "/pages/childTableModify_EC_PURORDER_H_MAIN.jsp?token="+token+"&pageCode="
				+ pageCode + "&pageName=" + pageName + "&ParentPKField="
				+ ParentPKField + "&ParentPKValue="+id  ;
				$("#iframe4child").attr("src",iframeUrl);
			}else{
				alert("系统出错，请联系管理员");
			}
		},
		error:function(){
			alert("系统出错，请联系管理员");
		}
	});

}
//主表更新
function updateTable(t){
	model="update";
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('提示', "请选择一条数据进行修改！");
		return;
	}
	initDetailTable();
	if(selected[0].BILL_STATUS != '0'&&selected[0].BILL_STATUS != '7'){
		//去掉附件删除按钮
		$('#insPage').children().find('[id=files_a_del]').remove();
		$('#sub').attr("disabled","disabled");
		$('.file').attr("disabled","disabled");
	}
	var pageCode = 'EC_PURORDER_H';// 子表数据源
	var pageName = '订单明细';// 列表显示名称
	var ParentPKField = 'EC_PURORDER_H_MAIN_ID';// 主表主键在子表中字段名

	var iframeUrl = context + "/pages/childTableModify_EC_PURORDER_H_MAIN.jsp?token="+token+"&pageCode="
			+ pageCode + "&pageName=" + pageName + "&ParentPKField="
			+ ParentPKField + "&ParentPKValue=" + selected[0]["ID"]  ;
	$("#iframe4child").attr("src",iframeUrl);


	$('#iframe4child').load(function(){
		var selected = JSON.parse(getSelections());
		if(selected[0].BILL_STATUS != '0'&&selected[0].BILL_STATUS != '7'){
			$('#iframe4child').contents().find('#toolbar').css('display',"none");
		}
	});
}
function delRows(t){
	var _dataSourceCode="EC_PURORDER_H_MAIN";
	var selected = JSON.parse(getSelections());
	if(selected.length < 1){
		oTable.showModal('modal', "请至少选择一条数据进行删除");
		return;
		}
	if(!validateDel(selected)){
		return ;
	}
	var message = transToServer(findBusUrlByPublicParam(t,'',_dataSourceCode),getSelections());
	var json=[];
	for(var i=0;i<selected.length;i++){
		json.push({EC_PURORDER_H_MAIN_ID:selected[i]["ID"]});
	}
	json=JSON.stringify(json);

	$.ajax({
		type:"get",
		url:"/system/purchase/delByParams?dataSourceCode=EC_PURORDER_H&paramKey=EC_PURORDER_H_MAIN_ID&paramValues="+json,
		success:function(){
		},
		error:function(){
		}
	});	
	
/*	$.ajax({
		type:"post",
		url:"/system/buttonBase?cmd=button&buttonToken=delete&dataSourceCode=EC_PURORDER_H&token="+token,
		dataType:"json",
		data:"jsonData="+json,
		success:function(){
		},
		error:function(){
		}
	});*/

	oTable.showModal('modal', message);
	queryTableByDataSourceCode(t,_dataSourceCode);
}

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
	var apprType="&audit_type="+type+"&audit_column="+audit_column+"&tenant_code="+tenant_code;
	var leviedTotal = selected[0]['LEVIED_TOTAL'];
	selected[0]['LEVIED_TOTAL'] = Math.ceil(leviedTotal);
	var message = transToServer(findBusUrlByPublicParam(t,apprType,dataSourceCode),JSON.stringify(selected[0]));
	if(message.indexOf('成功') != -1){
		$("#"+singleFindAuditColumn()).val("1");
	}
	oTable.showModal('提示', message);
}
