buttonJson =[
			{name:'查询',fun:'queryTable(this)',buttonToken:'query'},
			{name:'导出',fun:'sealUseExport()',buttonToken:'sealUseExport'}
            ];

//导出
function sealUseExport(){
	var param = '';
	var cachetApplyProcessNumber = $('#SEARCH-CACHET_APPLY_PROCESS_NUMBER').val();
	if(cachetApplyProcessNumber != null && cachetApplyProcessNumber != ''){
		param = '&SEARCH-CACHET_APPLY_PROCESS_NUMBER='+cachetApplyProcessNumber;
	}
	window.location.href = "/system/buttonBase?cmd=button&buttonToken=seal_use_export&dataSourceCode=SEAL_USE_REGISTER"+param;
}

//重写详情函数
function dblClickFunction(row,tr){}