/**
 * 供应商评价方案
 * @author yansu
 */

buttonJson =[
             {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
             {name:'开始评分',fun:'view3(this)',buttonToken:'checkMeta'},
             {name:'评分详情',fun:'ratingdetails(this)',buttonToken:'checkMeta'},
			];

function ratingdetails(t){
	//查看子表相关配置
	var pageCode = 'SU_EVAL_PLAN_CT_C';//子表数据源
	var pageName = '评价详情';//列表显示名称
	var ParentPKField = 'EVALUATION_DEPARTMENT_ID';//主表主键在子表中字段名
	var ParentPKField1 = 'SU_EVAL_PLAN_ID';//主表主键在子表中字段名
	
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('modal', "请选择一条数据进行操作");
		return;
	}
	if(selected[0]["STATE"]==0){
		oTable.showModal('modal', "该方案还未评分");
		return;
	}
	
	window.location.href=context+"/pages/childTableModifySUEVALPLANCTC_RATINGDETAILS.jsp?pageCode="+pageCode+"&pageName="+pageName+"&ParentPKField="+ParentPKField+"&ParentPKValue="+JSON.parse(getSelections())[0]["EVALUATION_DEPARTMENT_ID"] +"&ParentPKField1="+ParentPKField1+"&ParentPKValue1="+JSON.parse(getSelections())[0]["SU_EVAL_PLAN_ID"]; 

}

function view3(t){
	
	//查看子表相关配置
	var pageCode = 'SU_EVAL_PLAN_CT_C';//子表数据源
	var pageName = '评价';//列表显示名称
	var ParentPKField = 'EVALUATION_DEPARTMENT_ID';//主表主键在子表中字段名
	var ParentPKField1 = 'SU_EVAL_PLAN_ID';//主表主键在子表中字段名
	
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('modal', "请选择一条数据进行操作");
		return;
	}
	if(selected[0]["STATE"]==1){
		oTable.showModal('modal', "该方案已评过分");
		return;
	}
	//alert(selected[0]["EVALUATION_DEPARTMENT_ID"])
	//alert(selected[0]["SU_EVAL_PLAN_ID"])
	//alert(context+"/pages/suppliers_Evaluation_SingleTableModify.jsp?pageCode="+pageCode+"&pageName="+pageName+"&ParentPKField="+ParentPKField+"&ParentPKValue="+JSON.parse(getSelections())[0]["ID"])
	window.location.href=context+"/pages/childTableModifySUEVALPLANCTC.jsp?pageCode="+pageCode+"&pageName="+pageName+"&ParentPKField="+ParentPKField+"&ParentPKValue="+JSON.parse(getSelections())[0]["EVALUATION_DEPARTMENT_ID"] +"&ParentPKField1="+ParentPKField1+"&ParentPKValue1="+JSON.parse(getSelections())[0]["SU_EVAL_PLAN_ID"]; 

}



function gotoURL(){
	var url = "/pages/singleTableModify.jsp?pageCode=SU_EVAL_PLAN&pageName=供应商评价方案&userId="+userId+"&userName="+userName;
	//alert(url);
	url = context + url;
	//alert(url);
	window.location.href = url;
}