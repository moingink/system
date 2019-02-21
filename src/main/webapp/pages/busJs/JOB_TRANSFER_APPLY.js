buttonJson =[
             {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
             {name:'新增',fun:'tog(this),setUpAdd(this),setAuditButton()',buttonToken:'add'},
             {name:'修改',fun:'updateRow(this),setUpUpdate(this),setAuditButton()',buttonToken:'update'},
             {name:'删除',fun:'delRows(this)',buttonToken:'delete'}
            ];
            
$(function(){
	$('#CONTRACT_NUMBER,#PRIMARY_UNDERTAKER,#PRIMARY_EXECUTOR,#APPLICANT,#APPLY_DEPARTMENT,#APPLY_DATE').attr('readonly','true');//合同编号、原承办人、原履行人、申请人、申请部门、申请日期
	$('#BILL_STATUS').parents('.col-md-4').css('display','none');//审批状态
})

//新增
function setUpAdd(t){
	buttonDisabledFalse();
	$('#APPLICANT').val(userName);//申请人
	$('#APPLY_DEPARTMENT').val(companyName);//申请部门
	$('#APPLY_DATE').val(getDate());//申请日期
}

//修改
function setUpUpdate(t){
	buttonDisabledFalse();
	var selected = JSON.parse(getSelections());
	if(selected[0]['BILL_STATUS'] != 0 && selected[0]['BILL_STATUS'] != 7){
		buttonDisabledTrue();
	}
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
	queryTableByDataSourceCode(t,_dataSourceCode);
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