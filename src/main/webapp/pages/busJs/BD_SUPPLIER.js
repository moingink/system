/**
 * 供应商目录
 * @author yansu
 */

buttonJson =[                
                {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
				{name:'新增',fun:'tog(this),readonly()',buttonToken:'add'},
				{name:'修改',fun:'updateRow(this),readonly()',buttonToken:'update'},
				{name:'删除',fun:'delRows(this)',buttonToken:'delete'},
				{name:'导入',fun:'upload(this)',buttonToken:'upload'},
			];	
$(function(){
	validJson["fields"]["CONTACT_PHONENUM"] = {"validators": {notEmpty: {message: '联系电话  必填不能为空'},stringLength: {min: 11,max: 11,message: '请输入11位手机号码'},regexp: {regexp: /^1[3|5|8]{1}[0-9]{9}$/,message: '请输入正确的手机号码'}}}
});


/**
 * 新增时隐藏是否黑名单字段
 */
function readonly(){
	//SUPSTATE_BLACKLIST 是否黑名单
	//$("label[for='SUPSTATE_BLACKLIST']").hide();
	//$("label[for='SUPSTATE_BLACKLIST']").hide();
	//$(".col-md-4:last").css('display','none');
//	$(".col-md-4:last").hide();
	$('#SUPSTATE_BLACKLIST').parent().parent().parent().css('display','none');
	$('#SUPSTATE_BLACKLIST').val("0");//供应商状态默认值0 否
}

function upload(){
    var pageCode = 'BD_SUPPLIER';
    var pageFileName="供应商目录导入模板.xls";//下载导入模板的文件名

    var l = (screen.width - 500) / 2; 
    var t = (screen.height - 300) / 2; 
    var s = 'width=' + 500 + ', height=' + 300 + ', top=' + t + ', left=' + l; 
    s += ', toolbar=no, scrollbars=no, menubar=no, location=no, resizable=no'; 
    open(context+"/pages/excelImport.jsp?pageCode="+pageCode+"&pageFileName=" + pageFileName+"&token="+token,"导入", s); 
    }

function save(t){
	$inspage.data("bootstrapValidator").validate();
    if(!$inspage.data('bootstrapValidator').isValid()){ 
    	setParntHeigth($("body").height());
        return ; 
    }

   
    var cache_dataSourceCode =$("#cache_dataSourceCode").val();
	var _dataSourceCode=dataSourceCode;
	if(cache_dataSourceCode!=null&&cache_dataSourceCode.length>0){
		_dataSourceCode=cache_dataSourceCode;
	}
	saveByDataSourceCode(t,_dataSourceCode);
	
}

   


/**
 * 输入框检查
 */
//$(function(){
//    $('input').bind('input propertychange', function() {  
//        var input1 = $("#CONTACT_PHONENUM").val();
//        var result = 0;
//          if( input1!=null||input1!="" ){
//        	  alert(input1)
//        	 // result = parseInt(input1)-parseInt(input2);
//        	 // $("#COST_SAVING_AMOUNT").val(result);
//          }else{
//        	//  $("#COST_SAVING_AMOUNT").val(result)
//          }
//    });  
//});