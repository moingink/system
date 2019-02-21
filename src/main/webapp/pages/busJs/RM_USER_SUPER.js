
buttonJson =[
              {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
              //{name:'查询模板',fun:'',buttonToken:''},
              {name:'密码策略',fun:'passwordStrategy(this)',buttonToken:''},
              {name:'角色设置',fun:'roleSetting(this)',buttonToken:''},
              {name:'激活',fun:'activationUser(this)',buttonToken:'activationUser'},
              {name:'锁定',fun:'lockUser(this)',buttonToken:'lockUser'},
              {name:'新增',fun:'tog(this)',buttonToken:'addUser'},
              {name:'删除',fun:'delRows(this)',buttonToken:'deleteUser'},
              {name:'修改',fun:'updateUser(this)',buttonToken:'updateUser'},
              {name:'密码重置',fun:'send_mails(this)',buttonToken:'resetPassword'},
              {name:'查看',fun:'view(this)',buttonToken:''}
			];

//子表  数据源编码
var _dataSourceCode='RM_USER_ONLINE_RECORD';
//父表 主键[在子表的外键字段]
var ParentPKField='USER_ID';
//值
var ParentPKValue='';

function view(t){
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('modal', "请选择一条数据进行操作");
		return;
	}
	ParentPKValue=JSON.parse(getSelections())[0]["ID"];
	var url =context+"/pages/userDetail.jsp?pageCode="+dataSourceCode+"&menuCode=DEMO";
	url=appendChildUrl(url,_dataSourceCode,ParentPKField,ParentPKValue);
	window.location.href=url;
}

function appendChildUrl(url,_dataSourceCode,ParentPKField,ParentPKValue){

	return url+"&_dataSourceCode="+_dataSourceCode
		      +"&ParentPKField="+ParentPKField
		      +"&ParentPKValue="+ParentPKValue;
}

function updateUser(t){
	$inspage.find("[id='LOCK_STATUS']").attr("disabled",true);
	$inspage.find("[id='LOGIN_ID']").attr("disabled",true);
	$inspage.find("[id='ADMIN_TYPE']").attr("disabled",true);
	//$inspage.find("[id='OLD_ORGANIZATION_ID']").value="abc";
	//$("#inspage input[id='OLD_ORGANIZATION_ID']").val("abc");
	updateRow(t);
	
	$inspage.find("[id='OLD_ORGANIZATION_ID']").attr("value",($inspage.find("[id='ORGANIZATION_ID']").val()));
	$inspage.find("[id='OLD_NAME']").attr("value",($inspage.find("[id='OLD_NAME']").val()));
}

function activationUser(t){
	var msg = "请至少选择一条数据进行激活";
	isLockUsersByDataSourceCode(t,dataSourceCode,msg);
}

function lockUser(t){
	var msg = "请至少选择一条数据进行锁定";
	isLockUsersByDataSourceCode(t,dataSourceCode,msg);
}



function send_mails(t) {
	
	var selected = JSON.parse(getSelections());
	if(selected.length < 1){
		oTable.showModal('提示', '请至少选择一条数据进行密码重置');
		return;
	}
	
	
	for(var i=0; i<selected.length; i++){
		var array = new Array();
		array.push(selected[i]);
		var name = selected[i]["NAME"];
		var password = transToServer(findBusUrlByPublicParam(t, '', _dataSourceCode), JSON.stringify(array));
		if(password != null && password != ''){
			password = password.split('：')[1];
		}
		var mails = selected[i]["EMAIL"];
		var map = [name, password, mails];
		$.ajax({
			type : "POST",
			url : "/system/base?cmd=send_mail_password&map=" + map,
			dataType : "json",
			async : false,
			cache : false,
			contentType : false,
			processData : false,
			success : function(data) { 
				alert("发送成功");
			},
			error : function(data) {
				var da = JSON.stringify(data);
				alert("发送失败");
			}
		}); 

	}

}






function roleSetting(t){
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('modal',"请选择一条数据进行用户设置！");
		return;
	}
	var user = selected[0];
	var userId = user.ID;
	var userName = user.NAME;
	
	//alert(userId+"******"+userName)
	var url = "/pages/userSetting.jsp?pageCode=RM_PARTY_ROLE_FOR_ROLE&menuCode=SUPER&pageName=角色设置&userId="+userId+"&userName="+userName;
	url = context + url;
	//alert(url);
	window.location.href = url;
}


function passwordStrategy(){
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('modal',"请选择一条数据进行用户设置！");
		return;
	}
	var user = selected[0];
	var userId = user.ID;
	//var userName = user.NAME;
	//此处需要获取用户已选的密码策略
	var strategyIds;
	var jsonData = '{"userId":"'+userId+'"}';
	$.ajax({  
		url : "/system/user/passwordStrategy",
        dataType : "json",  
        type : "POST",
        //async: false,
        data:{"jsonData":jsonData},
        success : function(data) {
        	if(data.isUsing){
        		strategyIds = data.strategyIds;
        	}else{
        		strategyIds = "";
        	}
        } 
    });
	
	//alert(userId+"******"+userName)
	var url = "/pages/passwordStrategy.jsp?pageCode=RM_PASSWORD_STRATEGY&menuCode=001&pageName=密码策略&userId="+userId+"&strategyIds="+strategyIds;
	url = context + url;
	//alert(url);
	window.location.href = url;
	
}