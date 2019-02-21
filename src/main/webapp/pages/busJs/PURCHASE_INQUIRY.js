/**
 * 采购询价
 * @author yansu
 */

buttonJson =[
			{name:'查询',fun:'queryTable(this)',buttonToken:'query'},
			{name:'新增',fun:'tog(this),readonly(),echo()',buttonToken:'add'},
			{name:'修改',fun:'updateRow(this)',buttonToken:'update'},
			{name:'删除',fun:'delRows(this)',buttonToken:'delete'},
			{name:'发送邮件询价',fun:'view2(this)',buttonToken:'checkMeta'},
			];
			

/**
 * readonly
 */
function readonly(){
	$("#CREATE_PERSON").attr("readonly","readonly");
	//$("label[for='DATE_CREATED']").hide();
	$("label[for='DATE_CREATED']").parent().hide();
	//$('#DATE_CREATED').css('display','none');
	$("input[id='DATE_CREATED']").parent().hide();
	//$("input[id='DATE_CREATED']").parent().parent().hide();
}

/**
 *回显当前登录用户名 
 */
function echo(){
	$("#CREATE_PERSON").val(userName);
}

/**
 *保存并发送 
 */
function saveAndSend(t){
	$inspage.data("bootstrapValidator").validate();
    if(!$inspage.data('bootstrapValidator').isValid()){ 
    	setParntHeigth($("body").height());
        return ; 
    }
  var values=getJson($inspage).substr(0, getJson($inspage).length - 1);
	  values= values+",\"STATE\":\"1\"}";
	//  values= JSON.parse(values);
	//console.info(values);
	
	var datasoures="PURCHASE_INQUIRY";
	var message ="";
	var buttonToken =$("#ins_or_up_buttontoken").val();
	message = transToServer(findBusUrlByButtonTonken(buttonToken,'',datasoures),values);
	oTable.showModal('modal', message);
	values="["+ values +"]";
	go_addscore(values);
	back(t);
	queryTableByDataSourceCode(t,datasoures);
}

/**
 * 发送邮件询价
 */
function view2(t){
	var selected = JSON.parse(getSelections());
	if(selected.length == 0){
		oTable.showModal('modal', "请选择一条数据进行操作");
		return;
	}
	var state=false;
	var datasoures="PURCHASE_INQUIRY";
		oTable.showModal('modal', "发送中...请稍等");
		$.ajax({
		url : "/system/purchaseinquiry/sendmail",
        dataType : "json",  
        type : "POST",
        //async: false,
        data:{"jsonData": getSelections() },
        success : function(data) {
        	if(data.state){
        		//alert(data.state)
        		var a=oTable.showModal('modal', "邮件已发送");
        		queryTableByDataSourceCode(t,datasoures);
        	}else{
        		//alert(data.state)
        		oTable.showModal('modal', data.desc);
        	}
        }
    });
}

/**
 *发送 邮件
 * @param {Object} selected
 */
function go_addscore(selected){
	oTable.showModal('modal', "发送中...请稍等");
		$.ajax({
		url : "/system/purchaseinquiry/sendmail",
        dataType : "json",  
        type : "POST",
        //async: false,
        data:{"jsonData": selected },
        success : function(data) {
        	if(data.state){
        		//alert(data.state)
        		oTable.showModal('modal', "邮件已发送");
        	}else{
        		//alert(data.state)
        		oTable.showModal('modal', data.desc);
        	}
        }
    });
}

function gotoURL(){
	var url = "/pages/PURCHASE_INQUIRY_SingleTableModify.jsp?pageCode=PURCHASE_INQUIRY&pageName=采购询价&userId="+userId+"&userName="+userName;
	//alert(url);
	url = context + url;
	//alert(url);
	window.location.href = url;
}
