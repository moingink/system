buttonJson =[
             {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
             {name:'新增',fun:'tog(this)',buttonToken:'add'},
             {name:'修改',fun:'updateRow(this)',buttonToken:'update'},
             {name:'删除',fun:'delRows(this)',buttonToken:'delete'},
			 {name:'导入',fun:'upload(this)',buttonToken:'upload'},
			 {name:'导出excel',fun:'exportEcPurorder(this)',buttonToken:'exportEcPurorder'}
            ];
            
$(function(){
    setUp(); 	
});

//页面设置
function setUp(){
	$('#PROJECT_NAME,#NAME_CONTRACT,#PURCHASING_MANAGERS,#THE_CONTACT,#THE_PHONE,#EMAIL_OF').attr("readonly",true);
	$('#LEVIED_TOTAL,#COST_SAVING_AMOUNT').attr("disabled",true);
	var id = $('#ID').val();
	if(id == '' || id == null){
		setUpAdd();
	}
}

//新增设置
function setUpAdd(){
	$('#PURCHASING_MANAGERS').val(userName);//采购经理
}

//参选页面增加条件
function ref_query_param(u){
	if(u=='CONTRACT_INFO_ESTIMATED_INCOME_REF'){
		var itemNo = $("#ITEM_NO").val();
		if(itemNo != null && itemNo != ""){
			return "&SEARCH-PRO_ID="+itemNo;
		}else{
			return "&SEARCH-PRO_ID=@@@";
		}
	}else{
		return "";
	}
}

//中标金额（含税）监听
$('#WINNING_TAX_INCLUDED').keyup(function(){
	$('#LEVIED_TOTAL').val($(this).val());//价税合计
	costSavingAmount();
});

//预算金额（万元）监听
$('#BUDGET_AMOUNT').keyup(function(){
	costSavingAmount();
});

//成本节约金额改变
function costSavingAmount(){
	var budgetAmount = $('#BUDGET_AMOUNT').val();//预算金额
	var winningTaxIncluded = $('#WINNING_TAX_INCLUDED').val();//中标金额（含税）
	budgetAmount = budgetAmount!=''?budgetAmount:'0';
	winningTaxIncluded = winningTaxIncluded!=''?winningTaxIncluded:'0';
	$('#COST_SAVING_AMOUNT').val(Number(budgetAmount.replace(new RegExp(',','gm'),''))-Number(winningTaxIncluded.replace(new RegExp(',','gm'),'')));
}


//导入
function upload(){
    var pageCode = 'EC_PURORDER_H2';
    var pageFileName="项目制采购导入模板.xls";//下载导入模板的文件名

    var l = (screen.width - 500) / 2; 
    var t = (screen.height - 300) / 2; 
    var s = 'width=' + 500 + ', height=' + 300 + ', top=' + t + ', left=' + l; 
    s += ', toolbar=no, scrollbars=no, menubar=no, location=no, resizable=no'; 
    open(context+"/pages/excelImport.jsp?pageCode="+pageCode+"&pageFileName=" + pageFileName,"导入", s); 
}

//导出excel
function exportEcPurorder(){
	var procurement_name=$("#SEARCH-PROCUREMENT_NAME").val();
	var purchase_number=$("#SEARCH-PURCHASE_NUMBER").val();
	var url="/system/buttonBase?cmd=button&buttonToken=exportEcPurorder&dataSourceCode=EC_PURORDER_H2";
	if(procurement_name!=null && procurement_name!=''){
		url+="&procurement_name="+procurement_name;
	}
	if(purchase_number!=null && purchase_number!=''){
		url+="&purchase_number="+purchase_number;
	}
	window.location.href=url;
	
}
