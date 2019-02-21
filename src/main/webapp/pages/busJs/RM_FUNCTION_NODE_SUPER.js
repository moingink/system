
buttonJson =[
              {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
              //{name:'修改',fun:'updateRow(this)',buttonToken:'update'},
              {name:'查看',fun:'view(this)',buttonToken:''}
			];

//子表  数据源编码
var _dataSourceCode='RM_FUNCTION_NODE';
//父表 主键[在子表的外键字段]
var ParentPKField='ID';
//值
var ParentPKValue='';


function view(t){
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('modal', "请选择一条数据进行操作");
		return;
	}
	ParentPKValue=JSON.parse(getSelections())[0]["ID"];
	
	var url =context+"/pages/menuDetail.jsp?pageCode="+dataSourceCode+"&menuCode=DEMO";
	url=appendChildUrl(url,_dataSourceCode,ParentPKField,ParentPKValue);
	window.location.href=url;
}

function appendChildUrl(url,_dataSourceCode,ParentPKField,ParentPKValue){
	
	return url+"&_dataSourceCode="+_dataSourceCode
		      +"&ParentPKField="+ParentPKField
		      +"&ParentPKValue="+ParentPKValue
		      +"&isReadonly=1";
}
