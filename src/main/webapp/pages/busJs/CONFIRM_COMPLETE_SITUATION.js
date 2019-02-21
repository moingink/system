buttonJson =[	
              {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
              {name:'联查明细',fun:'view(this)',buttonToken:'view'}
			];

$(function(){
	
	$("#SEARCH-CONFIRM_DATE_FROM").parent().parent().parent().remove();
	$("#queryParam").append('<div class="col-md-5" style="margin-top: 150px; display: inline"> <div class="form-group"> <div class="col-md-4" style="white-space: nowrap;"> <label class="control-label" style="text-align: left; line-height: 29px;" for="SEARCH-CONFIRM_DATE">计收月份：</label> </div><div class="col-md-8" style="display: inline-flex;"> <input type="TEXT" class="form-control" id="SEARCH-CONFIRM_DATE_FROM" name="SEARCH-CONFIRM_DATE_FROM" value="" readonly="true"> <label class="control-label" style="  margin-bottom:  auto;  margin-top:  auto; "> 至 </label><input readonly="true" type="TEXT" class="form-control" id="SEARCH-CONFIRM_DATE_TO"   value=""> </div> </div> </div>');
	 $("#SEARCH-CONFIRM_DATE_FROM,#SEARCH-CONFIRM_DATE_TO").datetimepicker({
		minView : 'year',
		format : 'yyyy-mm-dd',
		todayBtn : 1,
		autoclose : 1,
		startView : 3,
		forceParse : 0,
		showMeridian : 1,
		language : 'zh-CN',
		beforeShow : function() {
			setTimeout(function() {
				$('div.datetimepicker').css("z-index", 999);
			}, 100);
		}
	});
});

function view(t){
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('modal', "请选择一条数据进行操作");
		return;
	}
	window.location.href=context+"/pages/childTableModify_confirmCompleteSituation.jsp?BILL_NO="+JSON.parse(getSelections())[0]["BILL_NO"]+"&RECEIVED_PAYMENT_ID="+JSON.parse(getSelections())[0]["RECEIVED_PAYMENT_ID"];
}
