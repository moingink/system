buttonJson =[
			{name:'查询',fun:'queryTable(this)',buttonToken:'query'},
			{name:'导出',fun:'SealQcExport()',buttonToken:'sealQcExport'}
            ];

//导出
function SealQcExport(){
	var param = '';
	var cachetAptitudeName = $('#SEARCH-CACHET_APTITUDE_NAME').val();
	if(cachetAptitudeName != null && cachetAptitudeName != ''){
		param = '&SEARCH-CACHET_APTITUDE_NAME='+cachetAptitudeName;
	}
	window.location.href = "/system/buttonBase?cmd=button&buttonToken=seal_qc_export&dataSourceCode=SEAL_AND_QC_LENDOUT_ROLLCALL"+param;
}

//重写详情函数
function dblClickFunction(row,tr){}