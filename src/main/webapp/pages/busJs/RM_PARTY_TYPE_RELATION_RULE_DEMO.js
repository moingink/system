buttonJson =[
              {name:'查询',fun:'queryDemo(this)',buttonToken:'query',buttonType:'child'},
              {name:'新增',fun:'togByDataSourceCode(this,\''+_dataSourceCode+'\')',buttonToken:'add',buttonType:'child'},
              {name:'修改',fun:'updataRowByDataSourceCode(this,\''+_dataSourceCode+'\')',buttonToken:'update',buttonType:'child'},
              {name:'删除',fun:'delRowsByDataSourceCode(this,\''+_dataSourceCode+'\')',buttonToken:'delete',buttonType:'child'}
			];


function queryDemo(t){
	queryTableByParam(t,_dataSourceCode,_query_param);
}


function updataRowSuper(t){
	savaByQueryForSuper(t,dataSourceCode,$("#superInsertPage"));
}