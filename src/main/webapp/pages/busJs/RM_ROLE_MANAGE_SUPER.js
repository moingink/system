
buttonJson =[
              {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
              {name:'设置互斥角色',fun:'roleMutexSetting(this)',buttonToken:''},
              {name:'数据授权',fun:'data_auth_set(this)',buttonToken:'dataAuth'},
              {name:'用户设置',fun:'userSetting(this)',buttonToken:''},
              {name:'授权',fun:'role_authorize(this)',buttonToken:''},
              {name:'新增',fun:'addRole(this)',buttonToken:'addRole'},
              {name:'删除',fun:'role_delRows(this)',buttonToken:'deleteRole'},
              {name:'修改',fun:'role_updateRow(this)',buttonToken:'update'},
              {name:'查看',fun:'role_manager_view(this)',buttonToken:''}
			];

//子表  数据源编码
var _dataSourceCode='RM_ROLE';
//父表 主键[在子表的外键字段]
var ParentPKField='';
//值
var ParentPKValue='';

function data_auth_set(t){
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('modal', "请选择一条数据进行操作");
		return;
	}
	ParentPKValue=JSON.parse(getSelections())[0]["ID"];
	var  roleName = JSON.parse(getSelections())[0]["NAME"];
	var url =context+"/pages/dataSetAuthorityPage.jsp?&menuCode=data_auth&roleId="+ParentPKValue+"&roleName="+roleName+"&pageCode=RM_ROLE_AUTH";
	//url=appendChildUrl_of_role(url,_dataSourceCode,ParentPKField,ParentPKValue);
	window.location.href=url;
}


function role_manager_view(t){
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('modal', "请选择一条数据进行操作");
		return;
	}
	ParentPKValue=JSON.parse(getSelections())[0]["ID"];
	var url =context+"/pages/dataDetail.jsp?pageCode="+dataSourceCode+"&menuCode=DEMO"+"&token="+token;
	url=appendChildUrl_of_role(url,_dataSourceCode,ParentPKField,ParentPKValue);
	window.location.href=url;
}

function appendChildUrl_of_role(url,_dataSourceCode,ParentPKField,ParentPKValue){

	return url+"&_dataSourceCode="+_dataSourceCode
		      +"&ParentPKField="+ParentPKField
		      +"&ParentPKValue="+ParentPKValue;
}

function addRole(t){
	$inspage.find("[id='ROLE_CODE']").attr("disabled",false);
	$inspage.find("[id='IS_SYSTEM_LEVEL']").attr("disabled",false);
	tog(t);
}

function role_updateRow(t){
	$inspage.find("[id='ROLE_CODE']").attr("disabled",true);
	$inspage.find("[id='IS_SYSTEM_LEVEL']").attr("disabled",true);
	updataRowByQuery(t,dataSourceCode,$inspage);
}

function role_delRows(t){
	delRowsByDataSourceCode(t,dataSourceCode);
}
//暂时设置为只能选择一条记录进行删除
function delRowsByDataSourceCode(t,_dataSourceCode){
	//alert(getSelections());
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('modal', "请选择一条数据进行删除");
		return;
		}
	var message = transToServer(findBusUrlByPublicParam(t,'',_dataSourceCode),getSelections());
	oTable.showModal('modal', message);
	queryTableByDataSourceCode(t,_dataSourceCode);
}

function role_authorize(t){
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('modal',"请选择一条数据进行授权！");
		return;
	}
	var role = selected[0];
	var roleId = role.ID;
	var roleName = role.NAME;
	var url = "/pages/roleAuthorize.jsp?roleId="+roleId+"&roleName="+roleName+"&token="+token;
	url = context + url;
	window.location.href = url;
}

function userSetting(t){
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('modal',"请选择一条数据进行用户设置！");
		return;
	}
	var role = selected[0];
	var roleId = role.ID;
	var roleName = role.NAME;
	var url = "/pages/userSetting.jsp?pageCode=RM_PARTY_ROLE_FOR_USER&menuCode=SUPER&pageName=用户设置&roleId="+roleId+"&roleName="+roleName;
	url = context + url;
	window.location.href = url;
}

function roleMutexSetting(t){
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('modal',"请选择一条数据进行互斥角色设置！");
		return;
	}
	var role = selected[0];
	var roleId = role.ID;
	var roleName = role.NAME;
	var url = "/pages/roleMutexSetting.jsp?pageCode=RM_ROLE_MUTEX&menuCode=SUPER&pageName=互斥角色设置&roleId="+roleId+"&roleName="+roleName;
	url = context + url;
	window.location.href = url;
}

