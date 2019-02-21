
buttonJson =[	
				{name:'查询',fun:'queryTable(this)',buttonToken:'query'},
				{name:'导出Excel',fun:'conAccountExport()'},
			];

//点击导出Excel
function conAccountExport(){
	window.location.href="/system/buttonBase?cmd=button&buttonToken=zw_income_export&dataSourceCode=VIEW_REVE_MAN_ANALY";
}
	