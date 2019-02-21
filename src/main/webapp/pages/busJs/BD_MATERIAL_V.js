/**
 * 物資管理
 * @author yansu
 */

buttonJson =[                
                {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
				{name:'新增',fun:'tog(this),setUpAdd(this)',buttonToken:'add'},
				{name:'修改',fun:'updateRow(this)',buttonToken:'update'},
				{name:'删除',fun:'delRows(this)',buttonToken:'delete'},
				{name:'导入',fun:'upload(this)',buttonToken:'upload'},
			];	

$(function(){
	$("#CREATOR").attr("disabled","disabled");
	$("#CREATIONTIME").attr("disabled","disabled");
	$("#UNIT_PRICE").attr("disabled","disabled");
	
	var rate_of = 0;
	
	$("#TAX_PRICE").blur(function(){
		$("#RATE_OF").blur(function(){
			if($("#RATE_OF").val()==1){
				rate_of=0.03;
			}
			if($("#RATE_OF").val()==2){
				rate_of=0.06;
			}
			if($("#RATE_OF").val()==3){
				rate_of=0.16;
			}
			$('#UNIT_PRICE').val(($("#TAX_PRICE").val().replace(/,/g,"")*(1+rate_of)).toFixed(6));//含税单价
		});
	});
	$("#RATE_OF").blur(function(){
		if($("#RATE_OF").val()==1){
			rate_of=0.03;
		}
		if($("#RATE_OF").val()==2){
			rate_of=0.06;
		}
		if($("#RATE_OF").val()==3){
			rate_of=0.16;
		}
		$("#TAX_PRICE").blur(function(){
			if($("#TAX_PRICE").val()!=null && $("#TAX_PRICE").val()!="" && $("#RATE_OF").val()!=null && $("#RATE_OF").val()!=""){
				$('#UNIT_PRICE').val(($("#TAX_PRICE").val().replace(/,/g,"")*(1+rate_of)).toFixed(6));//含税单价
			}
		});
	});
	
	
});

function upload(){
    var pageCode = 'BD_MATERIAL_V';
    var pageFileName="物资管理导入模板.xls";//下载导入模板的文件名

    var l = (screen.width - 500) / 2; 
    var t = (screen.height - 300) / 2; 
    var s = 'width=' + 500 + ', height=' + 300 + ', top=' + t + ', left=' + l; 
    s += ', toolbar=no, scrollbars=no, menubar=no, location=no, resizable=no'; 
    open(context+"/pages/excelImport.jsp?pageCode="+pageCode+"&pageFileName=" + pageFileName,"导入", s); 
}

//新增页面设置
function setUpAdd(t){
	$('#CREATOR').val(userName);//创建人
	$('#CREATIONTIME').val(getDate());//创建时间

}

//获取当前时间
function getDate(){
	var myDate = new Date();
	var year = myDate.getFullYear();    
	var month = myDate.getMonth()+1;
	var day = myDate.getDate();
	var hours = myDate.getHours();
	var minutes = myDate.getMinutes();
	var seconds = myDate.getSeconds();
	if (month >= 1 && month <= 9) {
		   month = "0" + month;
	}
	if (day >= 0 && day <= 9) {
		day = "0" + day;
	}
	return year+"-"+month+"-"+day;
}
