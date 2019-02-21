
buttonJson =[
              {name:'查询',fun:'queryRole(this)',buttonToken:'query'},
              {name:'添加角色',fun:'addRole(this)',buttonToken:''},
              {name:'删除',fun:'delRows(this)',buttonToken:'physicalDelete'},
              {name:'返回',fun:'doBack(this)',buttonToken:''}
			];

function doBack(t){
	history.go(-1);
	//window.location.href="success.html";
}

//打开模式窗口 
function addRole(t){  
	  //设置模式窗口的一些状态值  
	  //var windowStatus = "dialogWidth:460px;dialogHeight:380px;center:1;status:0;";  
	  //在模式窗口中打开的页面  
	  //var url = "test.html";  
	/* var url = "/pages/singleTableModify.jsp?pageCode=RM_ROLE_FOR_USER&menuCode=001&pageName=用户列表";
	  url = context + url;*/
	  //将模式窗口返回的值临时保存  
	  //var temp = showModalDialog(url,"",windowStatus);  
	  //将刚才保存的值赋给文本输入框returnValue
	  //alert("");
	  //document.all.returnValue.value = temp;
	var Request = new Object(); 
	Request = GetRequest();
	var userId = Request['userId'];
	//alert(userId);
	window.location.href=context+"/pages/singleTableModify.jsp?pageCode=RM_USER_FOR_ROLE&menuCode=001&pageName=角色列表&userId="+userId;
	}  

function queryRole(t) {
	var Request = new Object(); 
	Request = GetRequest();
	var userId = Request['userId'];
	//添加参数查询条件
	var param = " PR.OWNER_PARTY_ID = '"+userId+"' ";
	var pageParam = "&pageParam="+param;
	queryTableByParam(t,dataSourceCode,pageParam);
}


function queryTableByParam(t,_dataSourceCode,_query_param){
	var queryButtonToken=$("#query_buttontoken").val();
	var buttonToken='';
	if(queryButtonToken!=null&&queryButtonToken.length>0){
		buttonToken=$("#query_buttontoken").val();
	}else{
		if(query_buttonToken!=null&&query_buttonToken.length>0){
			buttonToken = query_buttonToken;
		}else{
			buttonToken = $(t).attr("buttonToken");
		}
		
		$("#query_buttontoken").val(buttonToken);
	}
	oTable.queryTable($table, findBusUrlByButtonTonken(buttonToken,_query_param,_dataSourceCode));
}

function delRows(t){
	delRowsByDataSourceCode(t,dataSourceCode);
}

function delRowsByDataSourceCode(t,_dataSourceCode){
	var selected = JSON.parse(getSelections());
	if(selected.length < 1){
		oTable.showModal('modal', "请至少选择一条数据进行删除");
		return;
		}
	var message = transToServer(findBusUrlByPublicParam(t,'',_dataSourceCode),getSelections());
	oTable.showModal('modal', message);
	//queryTableByDataSourceCode(t,_dataSourceCode);
	queryRole(t);
}
