buttonJson =[
              {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
              {name:'查看',fun:'view()',buttonToken:''}
            ];
            


function view(){
	
	//查看子表相关配置 
	var pageCode = 'NF_NOTIFY_LOG_DETAIL';//子表数据源
	var pageName = '通知日志明细';//列表显示名称
	var ParentPKField = 'LOG_ID';//主表主键在子表中字段名
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('modal', "请选择一条数据进行操作");
		return;
	}
	window.location.href=context+"/pages/childTableModify.jsp?pageCode="+pageCode+"&pageName="+pageName+"&ParentPKField="+ParentPKField+"&ParentPKValue="+JSON.parse(getSelections())[0]["ID"]+"&token="+token;
}
