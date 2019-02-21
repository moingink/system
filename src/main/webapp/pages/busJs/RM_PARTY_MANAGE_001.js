

buttonJson =[
             {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
             //{name:'新增',fun:'tog(this)',buttonToken:'add'},
             {name:'查看',fun:'view(this)',buttonToken:''}
			];




function view(t){
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('modal', "请选择一条数据进行操作");
		return;
	}
	var selectedId = selected[0]["ID"];
	var partyType = selected[0]["CHILD_PARTY_TYPE_ID"];
	var partyId = selected[0]["CHILD_PARTY_ID"];
	var partyCode = selected[0]["CHILD_PARTY_CODE"];
	//根据团体类型跳转到不同的信息页
	if(partyType=="1000200800000000001"){
		//window.location.href=context+"/pages/addDetail.jsp?pageCode=TM_COMPANY&menuCode=DEMO&_dataSourceCode=TM_COMPANY&ParentPKField=PARENT_PARTY_ID&ParentPKValue="+partyId+"&isReadonly=1";
		window.location.href=context+"/pages/partyDetail.jsp?pageCode=TM_COMPANY&menuCode=DEMO&_dataSourceCode=TM_COMPANY&ParentPKField=PARENT_PARTY_ID&ParentPKValue="+partyId+"&ParentPartyCode="+partyCode+"&isReadonly=1";
	}else if(partyType=="1000200800000000002"){
		//window.location.href=context+"/pages/addDetail.jsp?pageCode=TM_DEPARTMENT&menuCode=DEMO&_dataSourceCode=TM_DEPARTMENT&ParentPKField=PARENT_PARTY_ID&ParentPKValue="+partyId+"&isReadonly=1";
		window.location.href=context+"/pages/partyDetail.jsp?pageCode=TM_DEPARTMENT&menuCode=DEMO&_dataSourceCode=TM_DEPARTMENT&ParentPKField=PARENT_PARTY_ID&ParentPKValue="+partyId+"&ParentPartyCode="+partyCode+"&isReadonly=1";
	}else if(partyType=="1000200800000000004"){
		alert("暂不支持此功能！");
	}else{
		alert("暂不支持此功能！");
	}
	
	//window.location.href=context+"/pages/busPage/metaDataDetailPage.jsp?METADATA_ID="+JSON.parse(getSelections())[0]["ID"]; 
}