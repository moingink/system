query_buttontoken='query';
buttonJson =[
			{name:'查询',fun:'queryRel(this)',buttonToken:'query'},
			{name:'新增',fun:'button_insert_rel(this)',buttonToken:'but_rel_menu_insert'},
			{name:'修改',fun:'update_button_rel(this)',buttonToken:'but_rel_menu_update'},
			{name:'删除',fun:'button_delete(this)',buttonToken:'but_rel_menu_delete'},
			{name:'绑定',fun:'button_bind_onclick(this)',buttonToken:'but_rel_menu_bind'},
			{name:'解绑',fun:'button_unbind_onclick(this)',buttonToken:'but_rel_menu_unbind'}
		]
             
//关联表
var _dataSourceCode= 'RM_BUTTON_RELATION_MENU';
//按钮表
var dataSourceCode = 'RM_BUTTON';
//按钮表在关联表中的外键ID字段
var ParentPKField='BUTTON_ID';

var ParentPKValue='';

function button_unbind_onclick(t){
	if(selectMenuValue()){ return; };
	var buttonToken = $(t).attr("buttonToken");
	var selected = JSON.parse(getSelections());
	if(selected.length == 0){
		oTable.showModal('提示', "请至少选择一条数据解绑");
		return;
	}
	var message = transToServer(findBusUrlByPublicParam(t,'',_dataSourceCode),getSelections());
	oTable.showModal('提示', message);
	queryTableByDataSourceCode(t,_dataSourceCode);
}

var isButton =false;
function button_bind_onclick(){
	if(selectMenuValue()){ return; };
	isButton =true;
	checkReference(this,'REF(RM_BUTTON_REF,BUTTON_NAME:SEARCH-BUTTON_NAME,0)','','SELECT');
}

function ref_write_json(rejsonArray){
	if(rejsonArray.length == 0){
		oTable.showModal('提示', "请至少选择一条数据进行绑定");
		return;
	}
	if(isButton){
		isButton=false;
		var button_ids = '';
		var buttonToken = "but_rel_menu_bind";
		var menu_id = $("#SEARCH-MENU_ID").val();
		var menu_code = $("#SEARCH-MENU_CODE").val();
		for(var i = 0;i< rejsonArray.length ; i++){
			button_ids += rejsonArray[i]['ID']+',';
		}
		button_ids = button_ids.substring(0,button_ids.length-1);
		var bindUrl = context+'/buttonBase?cmd=button&menu_id='+menu_id+'&button_ids='+button_ids+'&buttonToken='+buttonToken+'&menu_code='+menu_code;
		oTable.showModal('提示', transToServer(bindUrl,JSON.stringify(rejsonArray)));
	}else{
		return true;
	}
}

function ref_end(){
	oTable.queryTable($table, findBusUrlByButtonTonken("query",'',_dataSourceCode));
}

function save(t){
	var message ="";
	var buttonToken =$("#ins_or_up_buttontoken").val();
	if(!notNullver()){
		return ; 
	}
	if($("#JSCONTENT").val()=='' || $("#JSCONTENT").val()==null){
		$("#JSCONTENT").val(" ");
	}
	
	message = transToServer(findBusUrlByButtonTonken(buttonToken,'',_dataSourceCode),getJsonOfJsContent());
	oTable.showModal('提示', message);
	back(t);
	queryTableByDataSourceCode(t,_dataSourceCode);
}

function button_delete(t){
	if(selectMenuValue()){ return; };
	var buttonToken=$(t).attr("buttonToken");
	var selected = JSON.parse(getSelections());
	if(selected.length == 0){
		oTable.showModal('提示', "请至少选择一条数据删除");
		return;
	}
	var message = transToServer(findBusUrlByPublicParam(t,'',_dataSourceCode),getSelections());
	oTable.showModal('提示', message);
	queryTableByDataSourceCode(t,_dataSourceCode);
}

function update_button_rel(t){
	if(selectMenuValue()){ return; };
	var buttonToken=$(t).attr("buttonToken");
	$("#MENU_CODE").attr("disabled",true);
	$("#MENU_NAME").attr("disabled",true);
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('提示', "请选择一条数据进行修改");
		return;
	}
	$("#insPageBut input[id='ID']").remove();
	var getJsonByIdUrl = context+'/sysButPubBase?cmd=getMapByRelTableId&id='+JSON.parse(getSelections())[0]['ID'] ;
	var message_getJsonById = transToServer(getJsonByIdUrl,getJson($("#bulidPage")));
	if(message_getJsonById.indexOf('该菜单节点下未设置按钮') != -1){
		oTable.showModal('提示', message_getJsonById);
		return;
	}else if(message_getJsonById.indexOf('公共按钮不允许修改') != -1){
		oTable.showModal('提示', message_getJsonById);
		return;
	}else{
		var fieldKey = message_getJsonById.substring(1,message_getJsonById.length-1).split(',');
		var json ="{"; 
			for(var i = 0 ;i<fieldKey.length ; i++){
				if(fieldKey[i].indexOf("=null")<=0){
					json += "\"" +	fieldKey[i].split('=')[0].trim() + "\":" + "\"" + fieldKey[i].split('=')[1].trim() + "\",";
				}
			}
			json = json.substring(0,json.length-1);
			json += "}";
		var updateDate = JSON.parse(json);
		updateDate['JSCONTENT'] = b64Decode(updateDate['JSCONTENT']);
		$("#bulidPage").find("[id]").each(function() {			
			$(this).val(updateDate[$(this).attr("id")]);
		});
		$("#ins_or_up_buttontoken").val(buttonToken);
		$("#cache_dataSourceCode").val(_dataSourceCode);
		togByDataSourceCode(t,_dataSourceCode);
	}
}

function queryRel(t){
	if(selectMenuValue()){ return; };
	queryTableByDataSourceCode(t,_dataSourceCode);
}

function button_insert_rel(t){
	if(selectMenuValue()){ return; };
	$("#BUTTON_NAME").blur(function() {
		$("#REMARK").val($("#MENU_NAME").val()+"--"+$(this).val());
	});
	var menuCode = $("#SEARCH-MENU_CODE").val();
	$("#insPage #MENU_NAME").val($("#SEARCH-MENU_NAME").val());
	$("#insPage #MENU_CODE").val(menuCode);
	$("#insPage #MENU_ID").val($("#SEARCH-MENU_ID").val());
	$("#insPageBut input[type='text'],textarea").each(function(){
		$(this).val("");
	});
	$("#BUTTON_DESC").keyup(function(){  
		$(this).val($(this).val().replace(/[^0-9]/g,''));  
	})
	$("#BUTTON_DESC").attr("maxLength","2");
	$("#BUTTON_CSS").val("btn btn-default"); 
	//$("#SPAN_CSS").val("glyphicon")
	//获取新增按钮编码
	var getCodeUrl = context+'/sysButPubBase?cmd=getButtonCode&menuCode='+menuCode;
	var buttonCode = transToServer(getCodeUrl,null);
	$("#insPageBut #BUTTON_CODE").val(buttonCode).attr("disabled",true);
	
	$("#ins_or_up_buttontoken").val("but_rel_menu_insert");
	$("#cache_dataSourceCode").val("");
	togByDataSourceCode(t,"RM_BUTTON")
}

function selectMenuValue(){
	if($("#SEARCH-MENU_NAME").val()==''){
		oTable.showModal('提示', "请选择菜单节点");
		return true;
	}
	return false;
}

function notNullver(){
	if($("#MENU_NAME").val()==''){ oTable.showModal('提示', "请选择菜单！"); return false; }
	if($("#BUTTON_NAME").val()==''){ oTable.showModal('提示', "请输入按钮名称！"); return false; }
	//if($("#BUTTON_TOKEN").val()==''){ oTable.showModal('提示', "请输入按钮标识！"); return false; }
	if($("#HTML_CLICK_NAME").val()==''){ oTable.showModal('提示', "请输入按钮方法！"); return false; }
	//if($("#BUTTON_ENTITY").val()==''){ oTable.showModal('提示', "请输入按钮对应实体类！"); return false; }
	if($("#ISHIDDEN").val()==''){ oTable.showModal('提示', "请选择是否隐藏！"); return false; }
	return true;
}

function getJsonOfJsContent(){
	var json = "{";
		$("#bulidPage :not(input[id='ID'])").find("[id]").each(function() {
			if($(this).attr("id")=='JSCONTENT'){
				$(this).val(b64Encode($(this).val()));
			}
			json += "\"" + $(this).attr("id") + "\":" + "\"" + $(this).val() + "\",";
		});
		json = json.substring(0,json.length-1);
		json += "}";
	return json ; 	
}

/**
 * 
 * btoa()：字符串或二进制值转为Base64编码
 * atob()：Base64编码转为原来的编码
 * var string = 'Hello World!';
  console.log(btoa(string)) // "SGVsbG8gV29ybGQh"
  console.log(atob('SGVsbG8gV29ybGQh')) // "Hello World!"
  console.log(b64Encode('Hello World! 你好！'))
  console.log(b64Decode('SGVsbG8lMjBXb3JsZCElMjAlRTQlQkQlQTAlRTUlQTUlQkQlRUYlQkMlODE='));
  */

/*btoa()：字符串或二进制值转为Base64编码*/
  function b64Encode(str) { return btoa(encodeURIComponent(str)); }
/*atob()：Base64编码转为原来的编码*/
  function b64Decode(str) { return decodeURIComponent(atob(str)); }