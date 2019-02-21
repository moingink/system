buttonJson =[
            
            ];

//初始化
$(function(){
	setUp();
});

//公共设置
function setUp(){
	$('#tog_titleName').text('公章用印申请确认');//设置标题
	$('#insPage').prev().prev().html('<button id="ins_or_up_buttontoken" type="button" class="btn btn-success" onclick="confirm(this)" value="update">'+
										'<span class="glyphicon glyphicon-saved" aria-hidden="true"></span>确认'+
									 '</button>');//设置按钮
	$('#BILL_STATUS').parent().parent().parent().css("display","none");//审批状态---隐藏
	$('#SEAL_CONFIRM_PEOPLE').val(userName).attr("readonly",true);//盖章确认人
	$('#SEAL_CONFIRM_DATE').val(getTime()).attr("disabled",true).nextAll().css("display","none");//盖章确认日期
	$('#CACHET_APPLY_PROCESS_NUMBER').attr("readonly",true);//公章申请流程号---不可编辑
	$('#SEAL_CONFIRM_EXPLAIN').parent().parent().parent().attr('class','col-md-12 col-xs-12 col-sm-12');//确认说明
	var html = '<option selected="selected" value="">==请选择==</option>'+
			   '<option value="2">流程关闭</option>';
	var billStatus = $('#BILL_STATUS').val();
	if(billStatus == '7'){
		html += '<option value="1">章已归还</option>';
	}else{
		html += '<option value="0">盖章完成</option>';
	}
	$('#PROCESS_STATE').html(html);//流程状态---设置选项
}

//点击确认
function confirm(){
	$inspage.data("bootstrapValidator").validate();
    if(!$inspage.data('bootstrapValidator').isValid()){
    	setParntHeigth($("body").height());
        return ; 
    }
    var cache_dataSourceCode =$("#cache_dataSourceCode").val();
	var _dataSourceCode = dataSourceCode;
	if(cache_dataSourceCode!=null&&cache_dataSourceCode.length>0){
		_dataSourceCode=cache_dataSourceCode;
	}
	var buttonToken =$("#ins_or_up_buttontoken").val();
	var message = transToServer(findBusUrlByButtonTonken(buttonToken,'',_dataSourceCode),getJson($('#insPage')));
	if(message.indexOf('成功') != -1){
		$('#ins_or_up_buttontoken').attr("disabled",true);
		window.parent.closeModalRefreshList('确认成功');//关闭、刷新
	}else{
		oTable.showModal('modal', message);
	}
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