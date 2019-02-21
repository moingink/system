/**
 * @author Sean
 */
buttonJson =[
              {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
              {name:'新增',fun:'tog(this)',buttonToken:'add'},
              {name:'修改',fun:'updateRow(this)',buttonToken:'update'},
              {name:'删除',fun:'delRows(this)',buttonToken:'delete'},
              {name:'评分标准',fun:'addStandard(this)',buttonToken:'addStandard'}
            ];

function addStandard(t){
	//查看子表相关配置
	var pageCode = 'GRADING_STANDARD';//子表数据源
	var pageName = '评分标准';//列表显示名称
	var ParentPKField = 'PCS_ID';//主表主键在子表中字段名
	
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('modal', "请选择一条数据进行操作");
		return;
	}
	window.location.href=context+"/pages/childTableModify.jsp?token="+token+"&pageCode="+pageCode+"&pageName="+pageName+"&ParentPKField="+ParentPKField+"&ParentPKValue="+selected[0]["ID"]+"&ParentPKParams="+selected[0]["GRADING_PROJECT"]+","+selected[0]["TOTAL_SCORE"];
																																							
}