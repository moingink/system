buttonJson =[
             {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
             {name:'新增',fun:'tog(this),setUpAdd()',buttonToken:'add'},
             {name:'修改',fun:'updateRow(this),setUpUpdate()',buttonToken:'update'},
             {name:'删除',fun:'delRows(this)',buttonToken:'delete'},
             {name:'预览',fun:'useSealApplyPreview(this)',buttonToken:'useSealApplyPreview'}
            ];

var lendOutArray = ['LEND_OUT_DATE','SEND_BACK_DATE','LEND_OUT_DEPARTMENT','LEND_OUT_PEOPLE','CAUSE','HANDLING_MATTERS'];//外借字段
var manySealArray = ['IS_FIRST'];//多次盖章字段
var yesFirstArray = ['SEE_AUTHORIZATION_BOOK'];//是首次字段
var noFirstArray = ['RELATION_FIRST_PROCESS'];//否首次字段


var dataHideArray = new Array();//隐藏数组
var dataShowArray = new Array();//显示数组

//初始化
$(function(){
	setUp();
});

//公共设置
function setUp(){
	delete validJson["fields"]["SEE_AUTHORIZATION_BOOK"];//删除查看授权书的验证
	$('#BILL_STATUS').parent().parent().parent().css("display","none");//审批状态---隐藏
	$('#CACHET_TYPE,#APPLY_PEOPLE,#USE_CACHET_DEPARTMENT').attr("readonly",true);//印别、申请人---不可编辑
	$('#APPLY_DATE').attr("disabled",true).nextAll().css("display","none");//申请日期---不可编辑
	$('#LEND_OUT_PEOPLE,#LEND_OUT_DEPARTMENT').attr("readonly",true);//借用人、借用部门
	$('#SEAL_TYPE').attr('onchange','changeSealType()');//盖章类型---添加改变事件
	$('#IS_FIRST').attr('onchange','changgeIsFirst()');//是否首次---添加改变事件
	$('#IS_LEND_OUT').attr('onchange','changeIsLendOut()');//是否外借---添加改变事件
	$('#SEE_AUTHORIZATION_BOOK').parent().parent().after('<button id="SEE_AUTHORIZATION_BOOK" type="button" class="btn btn-danger" onclick="clickAuthorizationBook()">授权书</button>').parent().css('text-align','center').attr('class','col-md-12 col-xs-12 col-sm-12');//查看授权书---添加按钮
	$('#SEE_AUTHORIZATION_BOOK').parent().parent().remove();//查看授权书---删除input
	$('#RELATION_FIRST_PROCESS').css({'text-decoration':'underline','color':'blue'}).attr({'data-toggle':'tooltip','data-original-title':'点击查看授权书'});//关联首次申请流程
	$("[data-toggle='tooltip']").tooltip();
	$('#CAUSE').parent().parent().parent().attr('class','col-md-12 col-xs-12 col-sm-12');//事由
	isCkNull();
}

//隐藏查看中的字段
function isCkNull(){
	initArray();
	dataHideArray.push('CACHET_APPLY_PROCESS_NUMBER');//公章申请流程号---隐藏
	setCssDisplay(dataHideArray,dataShowArray);
}

//不可编辑
function disabled(){
	
}

//添加界面设置
function setUpAdd(){
	$('#APPLY_PEOPLE').val(userName);//申请人
	$('#APPLY_PEOPLE_ID').val(userId);//申请人id
	$('#APPLY_DATE').val(getTime());//申请日期
	$('#USE_CACHET_DEPARTMENT').val(companyName);//用印部门
	$('#USE_CACHET_TIME').val(getTime());//用印时间
	$('#CACHET_TYPE').val('公章');//印别
	initArray();
	dataHideArray = lendOutArray.concat(manySealArray).concat(yesFirstArray).concat(noFirstArray);
	setCssDisplay(dataHideArray,dataShowArray);
}

//修改界面设置
function setUpUpdate(){
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		return;
	}
	disabled();
	changeSealType();
	changgeIsFirst();
	changeIsLendOut();
	setTimeout('setHeight()',300);
}

//盖章类型改变
function changeSealType(){
	initArray();
	var sealType = $('#SEAL_TYPE option:selected').val();//盖章类型
	if(sealType == '1'){//多次盖章
		dataShowArray = manySealArray;
	}else if(sealType == '0'){//单次盖章
		dataHideArray = manySealArray.concat(yesFirstArray).concat(noFirstArray);
		$('#RELATION_FIRST_PROCESS').val('');//关联首次申请流程ID---设置空值
	}
	setCssDisplay(dataHideArray,dataShowArray);
}

//是否首次
function changgeIsFirst(){
	initArray();
	var isFirst = $("#IS_FIRST option:selected").val();//是否首次
	if(isFirst == '1'){//是
		dataShowArray = yesFirstArray;
		dataHideArray = noFirstArray;
		$('#RELATION_FIRST_PROCESS').val('');//关联首次申请流程ID---设置空值
	}else if(isFirst == '0'){//否
		dataShowArray = noFirstArray;
		dataHideArray = yesFirstArray;
	}
	setCssDisplay(dataHideArray,dataShowArray);
}

//是否外借改变
function changeIsLendOut(){
	initArray();
	var isLendOut = $("#IS_LEND_OUT option:selected").val();//是否外借
	if(isLendOut == '1'){//是
		dataShowArray = lendOutArray;
		if($('#CACHET_APTITUDE_NAME').val() == '' ||
		   $('#LEND_OUT_PEOPLE').val() == '' ||
		   $('#CAUSE').val() == '' ||
		   $('#LEND_OUT_DATE').val() == '' ||
		   $('#SEND_BACK_DATE').val() == ''){
				$('#CACHET_APTITUDE_NAME').val($('#CACHET_TYPE').val());//印章、资质证照名称
				$('#LEND_OUT_PEOPLE').val($('#APPLY_PEOPLE').val());//借用人
				$('#LEND_OUT_DEPARTMENT').val($('#USE_CACHET_DEPARTMENT').val());//借用部门
				$('#CAUSE').val($('#USE_CACHET_CAUSE').val());//事由
				$('#LEND_OUT_DATE').val(getTime());//借出日期
				$('#SEND_BACK_DATE').val(getTime());//归还日期
		}
	}else if(isLendOut == '0'){//否
		dataHideArray = lendOutArray;
	}
	setCssDisplay(dataHideArray,dataShowArray);
}

//点击查看
function ck(id){
	$('#myModalLabelA').html('公章用印申请详情');
	$('#myReport').attr("src",context+"/pages/publicPageReport.jsp?pageCode=CACHET_USE_SEAL_APPLY_DETAIL&pageName=公章用印申请详情&ParentPKField=ID&ParentPKValue="+id+"&token="+token+"&state=1");
	$('#myReport').attr('height','500px');
	$('#myModal').modal('toggle');
}

//点击确认
function qr(id){
	$('#myModalLabelA').html('公章用印申请确认');
	$('#myReport').attr("src",context+"/pages/publicPageReport.jsp?pageCode=CACHET_USE_SEAL_APPLY_CONFIRM&pageName=公章用印申请确认&ParentPKField=ID&ParentPKValue="+id+"&token="+token+"&state=1");
	$('#myReport').attr('height','300px');
	$('#myModal').modal('toggle');
}

//初始化数组
function initArray(){
	dataHideArray = new Array();
	dataShowArray = new Array();
}

//设置字段隐藏
function setCssDisplay(dataHideArray,dataShowArray){
	for(var i=0;i<dataHideArray.length;i++){
		if(dataHideArray[i].indexOf('DATE') != -1 || dataHideArray[i].indexOf('RELATION_FIRST_PROCESS') != -1){//时间、参选
			$("#"+dataHideArray[i]).val('').parent().parent().parent().parent().css("display","none");
		}else if(dataHideArray[i].indexOf('SEE_AUTHORIZATION_BOOK') != -1){//查看授权书
			$("#"+dataHideArray[i]).parent().css("display","none");
		}else{//文本
			$("#"+dataHideArray[i]).val('').parent().parent().parent().css("display","none");
		}
		bulidValid();
	}
	for(var j=0;j<dataShowArray.length;j++){
		if(dataShowArray[j].indexOf('DATE') != -1 || dataShowArray[j].indexOf('RELATION_FIRST_PROCESS') != -1){//时间、参选
			$("#"+dataShowArray[j]).parent().parent().parent().parent().css("display","inline");
		}else if(dataShowArray[j].indexOf('SEE_AUTHORIZATION_BOOK') != -1){//查看授权书
			$("#"+dataShowArray[j]).parent().css("display","inline");
		}else{//文本
			$("#"+dataShowArray[j]).parent().parent().parent().css("display","inline");
		}
		if(dataShowArray[j] != 'SEE_AUTHORIZATION_BOOK' == -1){//查看授权书
			$inspage.data('bootstrapValidator').validateField(dataShowArray[j]);
		}
	}
	setTimeout('setHeight()',200);
}

//点击授权书
function clickAuthorizationBook(){
	var authorizationId = $('#AUTHORIZATION_ID').val();//授权书id
	var applyPeopleId = $('#APPLY_PEOPLE_ID').val();//申请人id
	var useCachetDepartment = $('#USE_CACHET_DEPARTMENT').val();//用户部门
	var billStatus = $('#BILL_STATUS').val();//审批状态
	var useCachetCause = $('#USE_CACHET_CAUSE').val();//用印事由
	openAuthorizationBook(authorizationId, applyPeopleId, useCachetDepartment, billStatus, useCachetCause);
}

//打开授权书
function openAuthorizationBook(authorizationId,applyPeopleId,useCachetDepartment,billStatus,useCachetCause){
	$('#myModalLabelA').html('授权书');
	var url = context + "/pages/cachetCertificateOfAuthorization.jsp?pageCode=CACHET_CERTIFICATE_OF_AUTHORIZATION&pageName=公章授权书&ParentPKField=ID&ParentPKValue="+authorizationId+
			 "&APPLY_PEOPLE_ID="+applyPeopleId+"&USE_CACHET_DEPARTMENT="+useCachetDepartment+"&BILL_STATUS="+billStatus+"&USE_CACHET_CAUSE="+useCachetCause+"&token="+token;
	$('#myReport').attr("src", url);
	$('#myReport').attr('height','500px');
	$('#myModal').modal('toggle');
}

//关闭Modal刷新列表
function closeModalRefreshList(message){
	$('#myModal').modal('hide');
	oTable.showModal('提示', message);
	queryTableByDataSourceCode('','CACHET_USE_SEAL_APPLY');
}

//点击关联首次申请流程
$('#RELATION_FIRST_PROCESS').click(function(){
	var val = $(this).val();
	if(val != null && val != ''){
		var data = querySingleRecord("&dataSourceCode="+dataSourceCode+"&SEARCH-CACHET_APPLY_PROCESS_NUMBER="+val);
		if(!jQuery.isEmptyObject(data) && data != ''){
			$('#myModalLabelA').html('授权书');
			var authorizationId = data["AUTHORIZATION_ID"];
			var dealWithClass = "com.yonyou.util.printPreview.impl.sta.StaAuthorizationPrintPreviewUtil";
			var url = "/system/buttonBase?cmd=button&buttonToken=printPreview&pafName=授权书&dealWithClass="+dealWithClass+"&dataSourceCode=CACHET_CERTIFICATE_OF_AUTHORIZATION&id="+authorizationId+"&token="+token;
			$('#myReport').attr("src", url);
			$('#myReport').attr('height','500px');
			$('#myModal').modal('toggle');
		}
	}
});

//重写保存函数
function save(t){
	$inspage.data("bootstrapValidator").validate();
    if(!$inspage.data('bootstrapValidator').isValid()){ 
    	setParntHeigth($("body").height());
        return ; 
    }
    var isFirst = $('#IS_FIRST option:selected').val();//是否首次
    if(isFirst == '1'){//是
    	var authorizationId = $('#AUTHORIZATION_ID').val();//授权书id
    	if(authorizationId == '' || authorizationId == null){
    		oTable.showModal('提示', '请生成授权书');
    		return;
    	}
    }
    var cache_dataSourceCode = $("#cache_dataSourceCode").val();
	var _dataSourceCode=dataSourceCode;
	if(cache_dataSourceCode!=null&&cache_dataSourceCode.length>0){
		_dataSourceCode=cache_dataSourceCode;
	}
	saveByDataSourceCode(t,_dataSourceCode);
}

//重写保存函数
function savaByQuery(t,_dataSourceCode,$div){
	var buttonToken = $("#ins_or_up_buttontoken").val();
	if(buttonToken == 'add'){
		$.ajax({
			url : context+"/syntheticalAudit/getCachetApplyNumber",
			type : 'GET',
			dataType : "text",
			async:false,
			success: function(data){
				$('#CACHET_APPLY_PROCESS_NUMBER').val(data);
			}
		});
	}
	var message = transToServer(findBusUrlByButtonTonken(buttonToken,'',_dataSourceCode),getJson($('#insPage')));
	oTable.showModal('提示', message);
	back(t);
	queryTableByDataSourceCode(t,_dataSourceCode);
}

//重写修改函数
function updataRowByDataSourceCode(t,_dataSourceCode){
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('modal', "请选择一条数据进行修改");
		return;
	}
	var billStatus = selected[0]['BILL_STATUS'];
	if(billStatus != '0' && billStatus != '7'){
		oTable.showModal('提示', "只能修改  已保存、已退回单据！");
		return;
	}
	updataRowByQuery(t,_dataSourceCode,$inspage);
}

//重写删除方法
function delRowsByDataSourceCode(t,_dataSourceCode){
	var selected = JSON.parse(getSelections());
	if(selected.length < 1){
		oTable.showModal('modal', "请至少选择一条数据进行删除");
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
	var json = JSON.stringify(json);
	if(!validateDel(selected)){
		return ;
	}
	var message = transToServer(findBusUrlByPublicParam(t,'',_dataSourceCode),json);
	oTable.showModal('提示', message);
	queryTableByDataSourceCode(t,_dataSourceCode);
}

//预览
function useSealApplyPreview(){
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('modal', "请选择一条数据进行预览");
		return;
	}
	$('#myModalLabelA').html('公章用印申请预览');
	var dealWithClass = "com.yonyou.util.printPreview.impl.sta.StaUseSealApplyPrintPreviewUtil";
	var url = "/system/buttonBase?cmd=button&buttonToken=printPreview&pafName=公章用印申请&dealWithClass="+dealWithClass+"&dataSourceCode=CACHET_USE_SEAL_APPLY&id="+selected[0]["ID"]+"&token="+token;
	$('#myReport').attr("src", url);
	$('#myReport').attr('height','500px');
	$('#myModal').modal('toggle');
}

//参选页面增加条件
function ref_query_param(u){
	if(u=='CACHET_USE_SEAL_APPLY_SC_REF'){
		var applyPeople = $('#APPLY_PEOPLE').val();//申请人
		if(applyPeople != null && applyPeople != ""){
			return "&SEARCH-APPLY_PEOPLE="+applyPeople;
		}else{
			return "&SEARCH-CUS_ID=001";
		}
	}else{
		return "";
	}
}

//重写列表加载完成函数
function bulidCacheTableJsonArray(){
	if(roleId != '1000105000000000225'){//印章管理员
		removeQueren();
	}
}

var querenLength = 15;

function removeQueren(){
	var length = $('input[id=queren]').length;
	if(length > 0){
		$('input[id=queren]').remove();
	}else{
		if(querenLength > 0){
			setTimeout('removeQueren()',500);
		}else{
			alert('页面加载失败');
		}
	}
}

//重写详情函数
function dblClickFunction(row,tr){}

//设置高度
function setHeight(){
	page_heigth = $(document.body).height();
	setParntHeigth(page_heigth);
}

//当前时间获取
function getTime(){
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
    return year+"-"+month+"-"+day;
}

//嵌入modal
$(document.body).append(
	'<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">'+
		'<div class="modal-dialog" style="width: 90%;">'+
			'<div class="modal-content">'+
				'<div class="panel-heading" style="color: #fff; background-color: #337ab7; border-radius: 6px 6px 0px 0px;">'+
					'<button type="button" class="close" data-dismiss="modal" aria-hidden="true">'+
						'×'+
					'</button>'+
					'<span id="myModalLabelA"></span>'+
				'</div>'+
				'<div class="modal-body"><iframe id="myReport" name="report" scrolling="auto" width="100%" frameborder="0"></iframe></div>'+
			'</div>'+
		'</div>'+
	'</div>'
);
