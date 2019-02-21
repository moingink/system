buttonJson =[
             {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
             {name:'新增',fun:'tog(this)',buttonToken:'add'},
             {name:'修改',fun:'updateRow(this)',buttonToken:'update'},
             {name:'删除',fun:'delRows(this)',buttonToken:'delete'},
             {name:'字段对应关系',fun:'view(this)',buttonToken:''},
             {name:'执行',fun:'execute(this)',buttonToken:'readDBFile'}
			];


function execute(t){
	var selected = JSON.parse(getSelections());
	if(selected.length == 0){
		oTable.showModal('modal', "请至少选择一条数据进行操作");
		return;
	}
	var message = transToServer(findBusUrlByPublicParam(t,'',dataSourceCode),JSON.stringify(selected[0]));
	oTable.showModal('modal', message);
	queryTableByDataSourceCode(t,dataSourceCode);
}

/**
 *查看子表
 */
function view() {
	
	var selected = JSON.parse(getSelections());
	if (selected.length != 1) {
		oTable.showModal('modal', "请选择一条数据进行操作");
		return;
	}
	window.location.href = context+"/pages/childTableModify.jsp?pageCode=DBFILE_FIELD_MAPPING&pageName=文件字段对应关系&ParentPKField=DBFILE_CONFIG_CODE&ParentPKValue="+JSON.parse(getSelections())[0]["CONFIG_CODE"];

}