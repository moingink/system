
buttonJson =[	
				{name:'查询',fun:'queryTable(this)',buttonToken:'query'},
				{name:'导出Excel',fun:'conAccountExport()'},
			];

//点击导出Excel
function conAccountExport(){
	window.location.href="/system/buttonBase?cmd=button&buttonToken=conacc_button_export&dataSourceCode=VIEW_CONTRACT_ACCOUNT";
}
	