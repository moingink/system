buttonJson =[
              {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
              {name:'录入',fun:'tog(this)',buttonToken:'updateMoreContractInfo'},
              {name:'修改',fun:'updateRow(this)',buttonToken:'update'},
              {name:'删除',fun:'delRows(this)',buttonToken:'deleteMoreContractInfo'},
              {name:'导出',fun:'moreContractInfoExport(this)',buttonToken:'moreContractInfoExport'}
             ];
             
$(function(){
	$('#CON_NAME').attr('readonly','true');//合同名称
	$('#DATE_SIGNING').attr('disabled','true');//具体签约日期
	$('#CONTRACT_PERFORM_TIME').nextAll().css('display','none');
	$('#PARTY_MAIN').attr('readonly','true');//签约主体
	$('#VALUE_ADDED_TAX_PRICE').attr('readonly','true');//签约金额
	$('#CONTRACT_PERFORM_TIME').attr('readonly','true');//合同期限
})

//导出
function moreContractInfoExport(t){
	window.location.href = "/system/buttonBase?cmd=button&buttonToken=moreContractInfoExport&dataSourceCode=MORE_CONTRACT_INFO_EXPORT&token="+token;
}