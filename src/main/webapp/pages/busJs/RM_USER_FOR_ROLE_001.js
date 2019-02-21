//角色设置
buttonJson =[
             {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
             {name:'确定',fun:'ensure(this)',buttonToken:'roleSetting'},
             {name:'返回',fun:'backToUserSetting(this)',buttonToken:''}
			];


function ensure(t){
	var Request = new Object(); 
	Request = GetRequest();
	var userId = Request['userId'];
	//alert(userId);
	var json = {"userId":userId};
	
	var selected = JSON.parse(getSelections());
	if(selected.length < 1){
		oTable.showModal('modal',"请选择至少一条数据进行用户设置！");
		return;
	}
	//alert("001");
	selected[selected.length]=json;
	//selected.add(json);
	//alert("002");
	//selected.put("roleId":roleId);
	var jsonData = JSON.stringify(selected);
	//alert(jsonData);
	
	var message = transToServer(findBusUrlByPublicParam(t,'',dataSourceCode),jsonData);
	oTable.showModal('modal', message);
	queryTableByDataSourceCode(t,dataSourceCode);
	
}


function backToUserSetting(t){
	history.go(-1);
}



