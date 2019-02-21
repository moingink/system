buttonJson =[
              {name:'确定',fun:'addM(this)',buttonToken:'addM',buttonType:'super'},
              {name:'查询',fun:'queryTable(this)',buttonToken:'query',buttonType:'super'},
              {name:'重置',fun:'resetAll(this)',buttonToken:'resetM',buttonType:'super'},
              {name:'删除',fun:'delRows(this)',buttonToken:'deleteM'}
			];




function addM(t){
	var selected = JSON.parse(getSelections());
	if(selected.length < 1){
		oTable.showModal('modal', "请选择至少一条数据进行操作");
		return;
	}
	var message = transToServer(findBusUrlByPublicParam(t,'',dataSourceCode),JSON.stringify(selected));
	alert(message);
	queryTableByDataSourceCode(t,dataSourceCode);
}



function resetAll(t){
	var message = transToServer(findBusUrlByPublicParam(t,'',dataSourceCode),"");
	alert(message);
	queryTableByDataSourceCode(t,dataSourceCode);
	//oTable.showModal('提示',message);
}


