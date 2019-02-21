buttonJson =[
             {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
             {name:'新增',fun:'tog(this),initDiv(0)',buttonToken:'addByNoticeManage'},
             {name:'修改',fun:'updateRow(this),initDiv(1)',buttonToken:'updateByNoticeManage'},
             {name:'删除',fun:'delRows(this)',buttonToken:'deleteByNoticeManage'},
             {name:'查看公告',fun:'view()',buttonToken:''}
			];





function view(){
	
	//查看子表相关配置
	var pageCode = 'NF_NOTICE';//子表数据源
	var pageName = '公告详细列表';//列表显示名称
	var ParentPKField = 'NOTICE_CODE';//主表主键在子表中字段名
	
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('modal', "请选择一条数据进行操作");
		return;
	}
	window.location.href=context+"/pages/childTableModify.jsp?pageCode="+pageCode+"&pageName="+pageName+"&ParentPKField="+ParentPKField+"&ParentPKValue="+JSON.parse(getSelections())[0]["NOTICE_CODE"];
}


function initDiv(isUp){
	
	if(isUp==1){
		$("#PLAN_CODE").parent().parent().parent().hide();
	}
	$("#NOTICE_CODE").parent().parent().hide();
	
}