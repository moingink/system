buttonJson =[
              {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
			  {name:'新增',fun:'tog(this),readonly()',buttonToken:'add'},
			  {name:'修改',fun:'updateRow(this),readonly()',buttonToken:'update'},
			  {name:'删除',fun:'delRows(this)',buttonToken:'delete'}
            ];

//重写保存函数
function savaByQuery(t,_dataSourceCode,$div){
	var taxTatettTest = $('#TAX_TATETT option:selected').text();
	$('#TAX_TATETT_FORMAT').val(Number(taxTatettTest.substring(0,taxTatettTest.length-1))/100);
	var buttonToken = $("#ins_or_up_buttontoken").val();
	var message = transToServer(findBusUrlByButtonTonken(buttonToken,'',_dataSourceCode),getJson($('#insPage')));
	oTable.showModal('modal', message);
	back(t);
	queryTableByDataSourceCode(t,_dataSourceCode);
}

