buttonJson = [{
	name : '查询',
	fun : 'queryTable(this)',
	buttonToken : 'role_query_to_button'
}, {
	name : '按钮授权',
	fun : 'grantButton(this)',
	buttonToken : 'role_grant_to_button'
}, {
	name : '修改本人',
	fun : 'modifyAuthority(this)',
	buttonToken : 'modify_myself_authority'
}, {
	name : '修改本机构',
	fun : 'modifyAuthority(this)',
	buttonToken : 'modify_company_authority'
}, {
	name : '修改所有',
	fun : 'modifyAuthority(this)',
	buttonToken : 'modify_all_authority'
}, {
	name : '清空',
	fun : 'clearSelect()',
	buttonToken : ''
}]; 

//关联表
var _dataSourceCode= 'RM_AUTHORIZE_RESOURCE_BUTTON';

//获取父页面roleAuthorize.jsp的元素
var menuName = $('#function_node_id_names', window.parent.document).val();
var menuCode = $('#function_node_ids', window.parent.document).val();
var roleId = $('#role_id', window.parent.document).val();

$(function(){
	$("#SEARCH-MENU_CODE").val(menuCode).attr("disabled",true);
	$("#SEARCH-MENU_NAME").val(menuName).attr("disabled",true);
})

function queryTableByParam(t,_dataSourceCode,_param){
	if(selectMenuVer()){ return ; }
	var queryButtonToken=$("#role_query_to_button").val();
	var buttonToken='';
	if(queryButtonToken!=null&&queryButtonToken.length>0){
		buttonToken=$("#role_query_to_button").val();
	}else{
		if(query_buttonToken!=null&&query_buttonToken.length>0){
			buttonToken = query_buttonToken;
		}else{
			buttonToken = $(t).attr("buttonToken");
		}
		$("#query_buttontoken").val(buttonToken);
	}
	oTable.queryTable($table, findBusUrlByButtonTonken(buttonToken,_query_param+'&roleId='+roleId+'&menuName='+menuName+'&menuCode='+menuCode,_dataSourceCode));
}

//默认列表已授权的按钮为选中状态
function stateFormatter(value, row, index) {
	var button_code=row["是否授权"];
	if(button_code=='是'){
		 return {
             checked :true
             //,disabled: true
         }
	}
    return value;
}

function selectMenuVer(){
	if($("#SEARCH-MENU_NAME").val()=='' || $("#SEARCH-MENU_CODE").val()==''){
		oTable.showModal('温馨提示', "请双击将要授权的菜单节点！");
		return true;
	}
	return false;
}

function clearSelect(){
	$("#queryParam input,textarea").each(function(index) {
		$(this).val("");
	});
	$('#function_node_id_names', window.parent.document).val('');
	$('#function_node_ids', window.parent.document).val('');
	window.location.href = "roleButtonRelation.jsp?pageCode=RM_BUTTON_RELATION_MENU_ROLE&pageName=角色按钮授权管理";
}

function grantButton(t){
	if(selectMenuVer()){ return ; }
	var url = findBusUrlByPublicParam(t,'&roleId='+roleId+'&menuCode='+menuCode+'&menuName='+menuName,dataSourceCode);
	var message = transToServer(url, getSelections());
	oTable.queryTable($table, findBusUrlByButtonTonken("role_query_to_button",_query_param,dataSourceCode));
	oTable.showModal('操作提示', message);
}

function modifyAuthority(t){
	var selected = JSON.parse(getSelections());
	if(selected.length == 0 ){
		oTable.showModal('操作提示', "请至少选中一条数据进行修改权限操作！");
		return ; 
	}
	var message = transToServer(findBusUrlByPublicParam(t,'&roleId='+roleId,''),getSelections());
	oTable.queryTable($table, findBusUrlByButtonTonken("role_query_to_button",_query_param,dataSourceCode));
	oTable.showModal('操作提示', message);
}

function nextAuth2(t){
	if(selectMenuVer()){ return ; }
	var level = $('#page_level').val();
	if(level == '' || level == null){
		oTable.showModal('操作提示', "请输入页面级别!!!");
		return ;
	}
	var url = findBusUrlByPublicParam(t,'&roleId='+roleId+'&menuCode='+menuCode+'&menuName='+menuName+'&level='+level,dataSourceCode);
	var message = transToServer(url, getSelections());
	oTable.queryTable($table, findBusUrlByButtonTonken("nextAuth2",_query_param,dataSourceCode));
	oTable.showModal('操作提示', message);
}