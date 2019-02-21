buttonJson =[	
              {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
             {name:'新增',fun:'addrow(this)',buttonToken:'add'},
             {name:'修改',fun:'updaterow(this)',buttonToken:'update'},
             {name:'删除',fun:'delrows(this)',buttonToken:'delete'}
			];

var auth=false;

//初始化
$(function(){
	//置灰
	$("#insPage").find("#CONTRACT_NAME,#CONTRACT_SIGNATORY,#CONTRACT_EFFECT_DATE,#BUSINESS_TYPE,#CONTRACT_TAX,#PAYBACK_PERIOD,#CONTRACT_PERIOD,#PRODUCT_TYPE").attr("readonly","true");
	$('#PRODUCT_TYPE').next().css('display','none');//产品
	$('#PRODUCT_TYPE').attr('size','30');
	//标明为暂估数据
	$("#insPage").append("<input hidden='hidden' type='text' id='DETAIL_TYPE' value='0'>");

	$.ajax({
		type : "POST",
		url :  context+"/confirmIncome?cmd=queryLoginUserOperateAuth&token="+token,
		data : "{}",
		dataType : "json",
		async : false,
		cache : false,
		contentType : false,
		processData : false,
		success : function(data) {
			if(data.status=="success"){
				auth=data.body.auth;
			}
		},
		error : function(data) {
		}
		
	});
	
});

//点击保存
function addrow(obj){
	tog(obj);
}

//点击修改
function updaterow(obj){
	updateRow(obj);
}

//点击删除
function delrows(obj){
	delRows(obj);
}