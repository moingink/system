buttonJson =[
              // {name:'新增公司',fun:'addCompany(this)',buttonToken:'addCompany',buttonType:'super'},
              //{name:'新增部门',fun:'addDepartment(this)',buttonToken:'addDepartment',buttonType:'super'},
              //{name:'参照组织',fun:'tog(this)',buttonToken:'add',buttonType:'super'},
              {name:'参照公司',fun:'queryTable(this)',buttonToken:'query',buttonType:'super'},
              {name:'参照部门',fun:'queryTable2(this)',buttonToken:'query',buttonType:'super'},
              {name:'修改',fun:'newUpdate(this)',buttonToken:'updateParty',buttonType:'super'},
              {name:'保存super',fun:'updataRowSuper(this)',buttonToken:'updateParty',buttonType:'super'},
              {name:'删除',fun:'deleteParty(this)',buttonToken:'deleteParty',buttonType:'super'}
             // {name:'返回',fun:'doBack(this)',buttonToken:'',buttonType:'super'}
			];
var winObj;
var flag=false;
function queryTable(t){
	
    var pageWidth = window.innerWidth ; 
	var pageHeight = window.innerHeight; 
	
	if (typeof pageWidth != "number"){ 
	    if (document.compatMode == "CSS1Compat"){ 
	        pageWidth = document.documentElement.clientWidth; 
	        pageHeight = document.documentElement.clientHeight; 
	    } else { 
	        pageWidth = document.body.clientWidth; 
	        pageHeight = document.body.clientHeight; 
	    } 
	} 
	
	var newWidth = (Number(pageWidth)/3) < 600 ? 600 : (Number(pageWidth)/3); 
	var newHeight = (Number(pageHeight)/2) < 400 ? 400 : (Number(pageHeight)/2); 
	var newLeft = pageWidth - newWidth - 50; 
	var newTop = pageHeight - newHeight - 5 ; 
	
	//alert("===========");
	//alert("PARENT_PARTY_ID:"+ParentPKValue);
	var s1=ParentPKValue;
	//alert("PARENT_PARTY_CODE:"+$("#PARENT_PARTY_CODE").val());
	var s2= $("#PARENT_PARTY_CODE").val();
	winObj = window.open('/system/pages/singleTableModify.jsp?pageCode=TM_COMPANY_REF&pageName=公司&Token=query&PARENT_PARTY_ID='+s1+'&PARENT_PARTY_CODE='+s2,'_blank','width='+newWidth+',height='+newHeight+',top='+newTop+'px,left='+newLeft+'px,toolbar=no, menubar=no, scrollbars=yes, resizable=no,location=no');
	flag=true;
}	

function queryTable2(t){

var pageWidth = window.innerWidth ; 
var pageHeight = window.innerHeight; 

if (typeof pageWidth != "number"){ 
   if (document.compatMode == "CSS1Compat"){ 
       pageWidth = document.documentElement.clientWidth; 
       pageHeight = document.documentElement.clientHeight; 
   } else { 
       pageWidth = document.body.clientWidth; 
       pageHeight = document.body.clientHeight; 
   } 
} 

var newWidth = (Number(pageWidth)/3) < 600 ? 600 : (Number(pageWidth)/3); 
var newHeight = (Number(pageHeight)/2) < 400 ? 400 : (Number(pageHeight)/2); 
var newLeft = pageWidth - newWidth - 50; 
var newTop = pageHeight - newHeight - 5 ; 

//alert("===========");
//alert("PARENT_PARTY_ID:"+ParentPKValue);
var s1=ParentPKValue;
//alert("PARENT_PARTY_CODE:"+$("#PARENT_PARTY_CODE").val());
var s2= $("#PARENT_PARTY_CODE").val();
winObj = window.open('/system/pages/singleTableModify.jsp?pageCode=TM_DEPARTMENT_REF&pageName=部门&Token=query&PARENT_PARTY_ID='+s1+'&PARENT_PARTY_CODE='+s2,'_blank','width='+newWidth+',height='+newHeight+',top='+newTop+'px,left='+newLeft+'px,toolbar=no, menubar=no, scrollbars=yes, resizable=no,location=no');
flag=true;
}	

var loop = setInterval(function() {  
	   
    if(flag  && winObj.closed) {  
      // alert("add1");
   	  parent.refreshParent();
    	//alert("add2");
        flag = false ; 
    }  
}, 10000);
loop();

//function doBack(t){
//	history.go(-1);
//	window.location.href="singleTableModify.jsp?pageCode=RM_PARTY_MANAGE&menuCode=001&pageName=组织管理";
//}

function queryDemo(t){
	queryTableByParam(t,_dataSourceCode,_query_param);
}

function updataRowSuper(t){
	//此处需要新增公司编码的校验方法
	
	savaByQueryForSuper(t,dataSourceCode,$("#superInsertPage"));
	var message ="";
	var buttonToken =$(t).attr("buttonToken");
	message = transToServer(findBusUrlByButtonTonken(buttonToken,'',dataSourceCode),getJson($("#superInsertPage")));
	//oTable.showModal('modal', message);
	
	if(message=="修改成功"){
		//oTable.showModal('modal', message);
		var newUrl = window.location.href;
		newUrl = changeURLPar(newUrl,"isReadonly","1")
		window.location.href = newUrl;
		alert(message);
	}else{
		oTable.showModal('modal', message);	
	}
}

function newUpdate(t){
	var newUrl = window.location.href;
	newUrl = changeURLPar(newUrl,"isReadonly","0")
	//alert("newUrl:"+newUrl);
	window.location.href = newUrl;
}

function deleteParty(t){
	var id = $("#ID").val();
	var Request = new Object(); 
	Request = GetRequest();
	var ParentCode = Request['ParentPartyCode'];
	//alert(ParentCode);
	var jsonData = '{"ID":"'+id+'","PARENT_PARTY_CODE":"'+ParentCode+'"}';
	//var jsonData ='[{"ID":"'+id+'"},{"PARENT_PARTY_CODE":"'+ParentCode+'"}]';
	var message = transToServer(findBusUrlByPublicParam(t,'',dataSourceCode),jsonData);
	//oTable.showModal('modal', message);
	if(message=="删除成功"){
		var url = "/pages/singleTableModify.jsp?pageCode=RM_PARTY_MANAGE&menuCode=001&pageName=组织管理";
		url = context + url;
		alert(message);
		window.location.href = url;
		parent.refreshParent();
	}else{
		oTable.showModal('modal', message);
	}
}

/*function addDepartment(t){
	tog(t);
}

function addCompany(t){	
	var comDataSourceCode="TM_COMPANY";
	//团体编码向新增页传递
	var parentCode = $("#PARENT_PARTY_CODE").val();
	var url =context+"/pages/partyAdd.jsp?pageCode="+comDataSourceCode+"&menuCode=DEMO"+"&ParentPartyCode="+parentCode;
	url=appendChildUrl(url,comDataSourceCode,ParentPKField,ParentPKValue);
	window.location.href=url;
}*/

function appendChildUrl(url,_dataSourceCode,ParentPKField,ParentPKValue){

	return url+"&_dataSourceCode="+_dataSourceCode
		      +"&ParentPKField="+ParentPKField
		      +"&ParentPKValue="+ParentPKValue;
}


