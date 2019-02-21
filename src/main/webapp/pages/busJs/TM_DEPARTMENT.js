buttonJson =[
             {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
             {name:'新增',fun:'tog(this)',buttonToken:'addDept'},
             {name:'修改',fun:'updateRow(this)',buttonToken:'update'},
             {name:'删除',fun:'delRows(this)',buttonToken:'delete'},
			];


function savaByQuery(t,dataSourceCode,$inspage){
	if(!verlaidte()){
		return false; 
	};
	var message ="";
	var buttonToken =$("#ins_or_up_buttontoken").val();
	message = transToServer(findBusUrlByButtonTonken(buttonToken,'',dataSourceCode),getJson($inspage));
	oTable.showModal('modal', message);
	back(t);
	queryTableByDataSourceCode(t,_dataSourceCode);
}


function verlaidte(){
	//此处新增必填项校验
	//var com = "TM_COMPANY";
	//var dep = "TM_DEPARTMENT";
	//alert(dataSourceCode);
  // if(dataSourceCode==dep){
		if($("#insPage input[id='OU']").val()==""){
			alert("部门 编码不能为空！");
			return false;
		}
		if($("#insPage input[id='NAME']").val()==""){
			alert("部门名称不能为空！");
			return false;
		}
		if($("#insPage input[id='DISPLAYNAME']").val()==""){
			alert("部门简称不能为空！");
			return false;
		}
		if($("#insPage input[id='TM_CODE']").val()==""){
			alert("部门编码不能为空！");
			return false;
		}
	//}
	return true;
	 
}	

