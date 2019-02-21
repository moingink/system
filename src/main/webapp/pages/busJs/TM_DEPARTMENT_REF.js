buttonJson =[
              {name:'确定',fun:'addDepartment(this)',buttonToken:'addDepartment',buttonType:'super'}, 
              {name:'查询',fun:'queryTable(this)',buttonToken:'query',buttonType:'super'}
			];





function addDepartment(t){
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

/*function appendDiv(){
	
	<input type="text" value='<%=request.getParameter("PARENT_PARTY_ID")%>'  id="PARENT_PARTY_ID" />
	<input type="text" value='<%=request.getParameter("PARENT_PARTY_CODE")%>'  id="PARENT_PARTY_CODE" />
	
	$("#bulidTable").append("<input type='text' value='"+'<%=request.getParameter("PARENT_PARTY_ID")%>'+"' id='PARENT_PARTY_ID'/>"+
			                "<input type='text' value='"+'<%=request.getParameter("PARENT_PARTY_CODE")%>'+"' id='PARENT_PARTY_CODE'/>" );
	
}


$(function() {
	appendDiv();
});
*/