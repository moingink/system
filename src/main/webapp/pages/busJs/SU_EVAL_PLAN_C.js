/**
 * 供应商评价结果
 * @author yansu
 */

buttonJson =[
{name:'查询',fun:'queryTable(this)',buttonToken:'query'},
//{name:'新增',fun:'tog(this)',buttonToken:'add'},
//{name:'修改',fun:'updateRow(this)',buttonToken:'update'},
//{name:'删除',fun:'delRows(this)',buttonToken:'delete'},
{name:'计算等级',fun:'calculation(this)',buttonToken:'checkMeta'},
{name:'查看',fun:'selectcalcu(this)',buttonToken:'checkMeta1'}
			];


function selectcalcu(t){
		//查看子表相关配置
		var pageCode = 'SU_EVAL_PLAN_C_COPY';//子表数据源
		var pageName = '详细';//列表显示名称
		var _BD_SUPPLIER_ID = 'BD_SUPPLIER_ID';//主表主键在子表中字段名 供应商id
		var _EVALUATION_DEPARTMENT_ID = 'EVALUATION_DEPARTMENT_ID';//部门id
		var _SU_EVAL_PLAN_ID = 'SU_EVAL_PLAN_ID';//方案id
		
		var selected = JSON.parse(getSelections());
		//console.info(selected)
		if(selected.length != 1){
			oTable.showModal('modal', "请选择一条数据进行操作");
			return;
		}
		//console.info(JSON.parse(getSelections())[0])
		var BD_SUPPLIER_ID=(JSON.parse(getSelections())[0]["BD_SUPPLIER_ID"]);
		var EVALUATION_DEPARTMENT_ID=(JSON.parse(getSelections())[0]["EVALUATION_DEPARTMENT_ID"]);
		var SU_EVAL_PLAN_ID=(JSON.parse(getSelections())[0]["SU_EVAL_PLAN_ID"]);

		var tuurl= context + "/pages/singleTableModify_SU_EVAL_PLAN_C_C.jsp?pageCode="
			+ pageCode+"&pageName=" + pageName + "&ParentPKField="
			+ _BD_SUPPLIER_ID + "&ParentPKValue=" + BD_SUPPLIER_ID
			//+ "&ParentPKField1="
			//+ _EVALUATION_DEPARTMENT_ID + "&ParentPKValue1=" + EVALUATION_DEPARTMENT_ID
			+ "&ParentPKField2="
			+ _SU_EVAL_PLAN_ID + "&ParentPKValue2=" + SU_EVAL_PLAN_ID
			; 
		//console.info(tuurl)
		 window.location.href = tuurl;
		 
}

function calculation(t){
	//alert("登陆人公司ID"+corpId)
	var selected = JSON.parse(getSelections());
	//if(selected.length != 1){
	//	oTable.showModal('modal', "请选择一条数据进行操作");
	//	return;
	//}	
//	if(parseInt(selected[0]["ASSESSMENT_SCORE_COMPLETE_SUM"])!=parseInt(selected[0]["ASSESSMENT_SCORE_SUM"])){
//		oTable.showModal('modal', "此方案进度未完成，不可计算");
//		return;
//	}
	console.log(selected);
	//alert(selected[0]["SU_EVAL_PLAN_ID"])
	
	updatainins(JSON.stringify(selected));
	//alert(parseInt(selected[0]["ASSESSMENT_SCORE_COMPLETE_SUM"])==parseInt(selected[0]["ASSESSMENT_SCORE_SUM"]))

	queryTableByDataSourceCode(t,"SU_EVAL_PLAN_C");//刷新数据
}

function updatainins(j) {
	// console.info(j)
	// var jsonData='<%=ParentPKField%>=<%=ParentPKValue%> and
	// <%=ParentPKField1%>=<%=ParentPKValue1%>';
	$.ajax({
		url : "/system/calculation/calculationts",
		dataType : "json",
		type : "POST",
		data : {
			"jsonData" : j
		},
		success : function(data) {
			var amsg="请求成功！";
			amsg=decodeURI(data.msg);
			oTable.showModal('modal', amsg);
			//alert(amsg)
			//queryTableByDataSourceCode(t,"SU_EVAL_PLAN_C");
		}
	});

}

function gotoURL(){
	var url = "/pages/singleTableModify.jsp?pageCode=SU_EVAL_PLAN_C&pageName=供应商评价结果&userId="+userId+"&userName="+userName;
	//alert(url);
	url = context + url;
	//alert(url);
	window.location.href = url;
}