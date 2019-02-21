buttonJson =[
             {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
             {name:'新增',fun:'tog(this)',buttonToken:'add'},
             {name:'修改',fun:'updateRow(this)',buttonToken:'update'},
             {name:'删除',fun:'delRows(this)',buttonToken:'delete'},
             {name:'周报详情',fun:'view(this)',buttonToken:'checkMeta'},
			];



function view(){
	
	//查看子表相关配置
	var pageCode = 'BID_PROJ_WEEKLY';//子表数据源
	var pageName = '应标周报基本信息';//列表显示名称
	var ParentPKField = 'PEOJ_ID';//主表主键在子表中字段名
	
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('modal', "请选择一条数据进行操作");
		return;
	}
	window.location.href=context+"/pages/childTableModify.jsp?pageCode="+pageCode+"&pageName="+pageName+"&ParentPKField="+ParentPKField+"&ParentPKValue="+JSON.parse(getSelections())[0]["ID"]; 
}