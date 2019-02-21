buttonJson =[
             
            ];
           
var lendOutArray = ['LEND_OUT_DATE','SEND_BACK_DATE','LEND_OUT_DEPARTMENT','LEND_OUT_PEOPLE','CAUSE','HANDLING_MATTERS'];//外借字段
var manySealArray = ['IS_FIRST'];//多次盖章字段
var yesFirstArray = ['SEE_AUTHORIZATION_BOOK'];//是首次字段
var noFirstArray = ['RELATION_FIRST_PROCESS'];//否首次字段
var confirmArray = ['SEAL_CONFIRM_PEOPLE','SEAL_CONFIRM_DATE','SEAL_CONFIRM_EXPLAIN','SEAL_CONFIRM_PEOPLE_DIV'];//确认信息
var auditArray = ['LAST_AUDIT_PEOPLE','AUDIT_ADOPT_DATE','LAST_AUDIT_OPINION'];//审核信息

var dataHideArray = new Array();//隐藏数组
var dataShowArray = new Array();//显示数组

//初始化
$(function(){
	setUp();
});

//公共设置
function setUp(){
	$('#tog_titleName').text('公章用印申请详情');//设置标题
	notEdit();//设置不可编辑
	if(!isWorkFlow){
		var buttonHtml = '<button id="spjc" type="button" class="btn btn-info" onclick="singlePubVisitHis(this);" buttontoken="audit">'+
							'<span class="glyphicon glyphicon-film" aria-hidden="true"></span>审批进程'+
					 	 '</button>';
		$('#insPage').prev().prev().html(buttonHtml);
	}
	$('#RELATION_FIRST_PROCESS').removeAttr('disabled');
	$('#SEE_AUTHORIZATION_BOOK').parent().parent().after('<button id="SEE_AUTHORIZATION_BOOK" type="button" class="btn btn-danger" onclick="openAuthorizationBook()">授权书</button>').parent().css('text-align','center').attr('class','col-md-12 col-xs-12 col-sm-12');//查看授权书---添加按钮
	$('#SEE_AUTHORIZATION_BOOK').parent().parent().remove();//查看授权书---删除input
	$("#SEE_AUTHORIZATION_BOOK").css("display","inline");//查看授权书
	$('#RELATION_FIRST_PROCESS').css({'text-decoration':'underline','color':'blue'}).attr({'data-toggle':'tooltip','data-original-title':'点击查看授权书'});//关联首次申请流程
	$("[data-toggle='tooltip']").tooltip();
	$('#CAUSE,#SEAL_CONFIRM_EXPLAIN,#LAST_AUDIT_OPINION').parent().parent().parent().attr('class','col-md-12 col-xs-12 col-sm-12');//事由、确认说明、最后审批意见
	$('#BILL_STATUS').parent().parent().parent().css("display","none");//审批状态---隐藏
	changeSealType();
	changgeIsFirst();
	changeIsLendOut();
	getFileTypeVal($("#insPage"));
	getFileTypeValTo($("#insPage"));
	if(isWorkFlow){
		hideWorkflowFile();
	}
}

//盖章类型改变
function changeSealType(){
	initArray();
	var sealType = $('#SEAL_TYPE option:selected').val();//盖章类型
	if(sealType == '0'){//单次盖章
		dataHideArray = manySealArray.concat(yesFirstArray).concat(noFirstArray);
	}
	setCssDisplay(dataHideArray,dataShowArray);
}

//是否首次
function changgeIsFirst(){
	initArray();
	var isFirst = $("#IS_FIRST option:selected").val();//是否首次
	if(isFirst == '1'){//是
		dataHideArray = noFirstArray;
	}else if(isFirst == '0'){//否
		dataHideArray = yesFirstArray;
	}
	setCssDisplay(dataHideArray,dataShowArray);
}

//是否外借改变
function changeIsLendOut(){
	initArray();
	var isLendOut = $("#IS_LEND_OUT option:selected").val();//是否外借
	if(isLendOut == '1'){//是
		dataHideArray = confirmArray;
	}else if(isLendOut == '0'){//否
		dataHideArray = lendOutArray.concat(auditArray);
	}
	setCssDisplay(dataHideArray,dataShowArray);
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
			$("#"+dataHideArray[i]).parent().parent().parent().parent().css("display","none");
		}else if(dataHideArray[i].indexOf('SEE_AUTHORIZATION_BOOK') != -1){//查看授权书
			$("#"+dataHideArray[i]).parent().css("display","none");
		}else if(dataHideArray[i].indexOf('DIV') != -1){//标签
			$("#"+dataHideArray[i]).css("display","none");
		}else{//文本
			$("#"+dataHideArray[i]).parent().parent().parent().css("display","none");
		}
	}
	for(var j=0;j<dataShowArray.length;j++){
		if(dataShowArray[j].indexOf('DATE') != -1 || dataShowArray[j].indexOf('RELATION_FIRST_PROCESS') != -1){//时间、参选
			$("#"+dataShowArray[j]).parent().parent().parent().parent().css("display","inline");
		}else if(dataShowArray[j].indexOf('SEE_AUTHORIZATION_BOOK') != -1){//查看授权书
			$("#"+dataShowArray[j]).parent().css("display","inline");
		}else if(dataShowArray[j].indexOf('DIV') != -1){//标签
			$("#"+dataShowArray[j]).css("display","inline");
		}else{//文本
			$("#"+dataShowArray[j]).parent().parent().parent().css("display","inline");
		}
	}
	setTimeout('setHeight()',200);
}

//点击授权书
function openAuthorizationBook(){
	$('#myModalLabelA').html('授权书');
	var authorizationId = $('#AUTHORIZATION_ID').val();
	var dealWithClass = "com.yonyou.util.printPreview.impl.sta.StaAuthorizationPrintPreviewUtil";
	var url = "/system/buttonBase?cmd=button&buttonToken=printPreview&pafName=授权书&dealWithClass="+dealWithClass+"&dataSourceCode=CACHET_CERTIFICATE_OF_AUTHORIZATION&id="+authorizationId+"&token="+token;
	$('#myReport').attr("src", url);
	$('#myReport').attr('height','500px');
	$('#myModal').modal('toggle');
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

//隐藏确认信息和外借信息
function hideWorkflowFile(){
	$("#LAST_AUDIT_PEOPLE,#AUDIT_ADOPT_DATE,#LAST_AUDIT_OPINION,#SEAL_CONFIRM_PEOPLE,#SEAL_CONFIRM_DATE,#SEAL_CONFIRM_EXPLAIN").parent().parent().parent().css("display","none");
	$('#SEAL_CONFIRM_PEOPLE_DIV').css("display","none");
}

//设置高度
function setHeight(){
	page_heigth = $(document.body).height();
	setParntHeigth(page_heigth);
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

//重写审批进程
function singlePubVisitHis(t){
	var selected = JSON.parse("["+getJson($("#insPage"))+"]");
	var id =selected[0]["ID"];
	var NEED_PROCESS_CODE=appCode+"@CACHET_USE_SEAL_APPLY@";
	var visit_url=workflow.replace("#BUS_ID#",id).replace("#ts#",new Date().getTime()).replace("#NEED_PROCESS_CODE#",NEED_PROCESS_CODE);
	singleShow(visit_url);
}
