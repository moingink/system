buttonJson = [
				
			 ];

$(function(){
	setUp();
});

//页面设置
function setUp() {
	$('#hr').nextAll().css("display","none");
	var id = $('#ID').val();
	if (id != null && id != '' && id != '0') {
		$('#download').css("display", "inline");//下载
		$('#printPreview').css("display", "inline");//打印预览
	}else{
		setUpAdd();
	}
	if (billStatus == '' || billStatus == '0' || billStatus == '7') {
		$('#insert').css("display", "inline");//保存
	}else{
		$('#AUTHORIZATION_MATTERS_RIGHTS,#LETTER_OF_AUTHORIZATION_DATE').attr("disabled",true);
	}
}

//新增页面设置
function setUpAdd() {
	$('#AUTHORIZING_DATE').val(getTime());//日期
	$('#SUBORDINATE_DEPARTMENT').val(useCachetDepartment);//所属部门
	$('#AUTHORIZATION_MATTERS_RIGHTS').val(useCachetCause);//授权事项及权利
	getWordToWord();//授权码
	getAgentPeople();//委托代理人
}

//生成授权码
function getWordToWord() {
	$.ajax({
		type : 'GET',
		url : '/system/syntheticalAudit/getWordToWord',
		async : false,
		dataType : "text",
		success : function(data) {
			$('#WORD_TO_WORD').val(data);
		}
	});
}

//获取委托代理人
function getAgentPeople(){
	var userData = querySingleRecord("&dataSourceCode=RM_USER&SEARCH-ID="+applyPeopleId);
	if(!jQuery.isEmptyObject(userData) && userData != ''){
		var organizationId = userData["ORGANIZATION_ID"];
		var companyData = querySingleRecord("&dataSourceCode=TM_COMPANY&SEARCH-ID="+organizationId);
		var deptLeader = companyData["DEPT_LEADER"];//部门负责人
		$('#AGENT_PEOPLE').val(deptLeader);//委托代理人
	}
}

//重写保存函数
function savaByQuery(t,_dataSourceCode,$div){
	var buttonToken = $("#ins_or_up_buttontoken").val();
	if(typeof(specifyButtonToken) == "function"){
		buttonToken = specifyButtonToken(buttonToken);
	}
	if(buttonToken == 'add'){
		$('#ID').val(getId());
	}
	var message = transToServer(findBusUrlByButtonTonken(buttonToken,'',_dataSourceCode),getJson($('#insPage')));
	oTable.showModal('提示', message);
	if(message.indexOf('成功') != -1){
		$("#insert").attr("disabled", true);
		$('#AUTHORIZATION_ID', window.parent.document).val($('#ID').val());//设置主表外键
	}
}

function getJson($div){
	var json = "{";
	$div.find("[id]").each(function() {
		if(json.indexOf($(this).attr("id")) == -1){
			json += "\"" + $(this).attr("id") + "\":" + "\"" + $(this).val() + "\",";
		}
	});
	json = json.substring(0,json.length-1);
	json += "}";
	json=replaceNSpace(json);
	return json;
}

//预览
function preview(){
	var id = $('#ID').val();
	var dealWithClass = "com.yonyou.util.printPreview.impl.sta.StaAuthorizationPrintPreviewUtil";
	var url = "/system/buttonBase?cmd=button&buttonToken=printPreview&pafName=授权书&dealWithClass="+dealWithClass+"&dataSourceCode="+dataSourceCode+"&id="+id+"&token="+token;
	window.location.href = url;
}

//获取主键
function getId() {
	var id = "";
	$.ajax({
		type : "GET",
		url : "/system/base?cmd=getIDByDataSourceCode&pageCode=CACHET_CERTIFICATE_OF_AUTHORIZATION",
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

//当前时间获取
function getTime() {
	var date = new Date();
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	var day = date.getDate();
	if (month >= 1 && month <= 9) {
		month = "0" + month;
	}
	if (day >= 0 && day <= 9) {
		day = "0" + day;
	}
	return year + "年" + month + "月" + day + "日";
}