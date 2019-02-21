buttonJson =[
              {name:'查询',fun:'queryDemo(this)',buttonToken:'query',buttonType:'child'},
              {name:'新增',fun:'togByDataSourceCode(this,\''+_dataSourceCode+'\')',buttonToken:'add',buttonType:'child'},
              {name:'修改',fun:'updataRowByDataSourceCode(this,\''+_dataSourceCode+'\')',buttonToken:'update',buttonType:'child'},
              {name:'删除',fun:'delRowsByDataSourceCode(this,\''+_dataSourceCode+'\')',buttonToken:'delete',buttonType:'child'}
              //{name:'查看',fun:'view(this)',buttonToken:'',buttonType:'child'}
			];


function queryDemo(t){
	queryTableByParam(t,_dataSourceCode,_query_param);
}


function updataRowSuper(t){
	savaByQueryForSuper(t,dataSourceCode,$("#superInsertPage"));
}

function view(t){
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('modal', "请选择一条数据进行操作");
		return;
	}
	ParentPKValue=JSON.parse(getSelections())[0]["ID"];
	//alert("ParentPKValue:"+ParentPKValue);
	//var selfDataSourceCode='RM_PARTY';
	var url =context+"/pages/dataDetail.jsp?pageCode=RM_PARTY&menuCode=DEMO&_dataSourceCode=&ParentPKField=&ParentPkValue="+ParentPkValue;
	//url=appendChildUrl(url,_dataSourceCode,ParentPKField,ParentPKValue);
	//url="http://127.0.0.1:8080/system/pages/dataDetail.jsp?pageCode=RM_PARTY&menuCode=DEMO&_dataSourceCode=&ParentPKField=&ParentPKValue=2000201100000074528";
	//alert("url:"+url);
	window.location.href=url;
}


function appendChildUrl(url,_dataSourceCode,ParentPKField,ParentPKValue){

	return url+"&_dataSourceCode="+_dataSourceCode
		      +"&ParentPKField="+ParentPKField
		      +"&ParentPKValue="+ParentPKValue;
}