buttonJson =[
              {name:'确定',fun:'addCompany(this)',buttonToken:'addCompany',buttonType:'super'},
              {name:'查询',fun:'queryTable(this)',buttonToken:'query',buttonType:'super'}
			];





function addCompany(t){
	var selected = JSON.parse(getSelections());
	if(selected.length < 1){
		oTable.showModal('modal', "请至少选择一条数据进行添加");
		return;
		}
	var parentPartyId=document.getElementById("PARENT_PARTY_ID").value;
	var parentPartyCode=document.getElementById("PARENT_PARTY_CODE").value;
	//alert("*************:"+parentPartyId);
	//alert("*************:"+parentPartyCode);
	//alert(dataSourceCode);
	var message = transToServer(findBusUrlByPublicParam(t,'&PARENT_PARTY_ID='+parentPartyId+"&PARENT_PARTY_CODE="+parentPartyCode,dataSourceCode),getSelections());
	alert(message);
	queryTableByDataSourceCode(t,dataSourceCode);

}

