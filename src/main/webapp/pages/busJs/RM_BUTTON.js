buttonJson =[
              {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
              {name:'新增',fun:'button_insert(this)',buttonToken:'button_button_insert'},
              {name:'修改',fun:'updataRowByQuery(this)',buttonToken:'button_button_update'},
              {name:'删除',fun:'delete_button_click(this)',buttonToken:'delete'},
              {name:'查看',fun:'view(this)',buttonToken:''}
			];
//初始化图标   {name:'检查数据',fun:'findCheckMessage(this)',buttonToken:''},   {name:'预览',fun:'viewStyle(this)',buttonToken:''},
//$("#SPAN_CSS").iconPicker(); 

function view(t){
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('提示', "请选择一条数据进行操作");
		return;
	}
	ParentPKValue=JSON.parse(getSelections())[0]["ID"];
	window.location.href=context+"/pages/dataDetail.jsp?pageCode="+dataSourceCode+"&ParentPKValue="+ParentPKValue;
}

//删除
function delete_button_click(t){
	var selected = JSON.parse(getSelections());
	if(selected.length < 1){
		oTable.showModal('提示', "请至少选择一条数据进行删除");
		return;
	}
	var getJsonByIdUrl = context+'/sysButPubBase?cmd=verButtonDelete&id='+JSON.parse(getSelections())[0]['ID'] ;
	var verButtonDelete = transToServer(getJsonByIdUrl,null);
	if(verButtonDelete!="0"){
		oTable.showModal('删除失败', verButtonDelete);
		return;
	}
	var message = transToServer(findBusUrlByPublicParam(t,'',dataSourceCode),getSelections());
	oTable.showModal('提示', message);
	queryTableByDataSourceCode(t,_dataSourceCode);
	
}

function updataRowByQuery(t){
	var buttonToken=$(t).attr("buttonToken");
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('提示', "请选择一条数据进行修改");
		return;
	}
	var jsContent = selected[0]['JSCONTENT'];
	var unZipUrl = context+'/sysButPubBase?cmd=unZipJsContent' ;//解压缩
	selected[0]['JSCONTENT'] = b64Decode(transToServer(unZipUrl,getSelections()));
	$inspage.find("[id]").each(function() {			
	  	$(this).val(selected[0][$(this).attr("id")]);
	});
	$("#insPage #BUTTON_CODE").attr("disabled",true);
	$("#ins_or_up_buttontoken").val(buttonToken);
	$("#cache_dataSourceCode").val(dataSourceCode);
	togByDataSourceCode(t,dataSourceCode);
}

function findCheckMessage(t){
	//var message = "table:" + JSON.stringify(oTable.bootMethod($table, "getSelections"));
	
	var transUrl = context+'/button?cmd=displayButtonByRole&menuCode=100102100&roleId=1000105000000000009';
	var message = transToServer(transUrl,null);
	for(var i = 0 ; i < message.length ; i++){
		alert(message[i]['ID']);
	}
	var jsonMessage = JSON.stringify(message);
	oTable.showModal('提示', jsonMessage);
}

function button_insert(t){
	$("#BUTTON_DESC").keyup(function(){  
		$(this).val($(this).val().replace(/[^0-9]/g,''));  
	})
	$("#BUTTON_CODE").attr("disabled" , true);		//默认公共的菜单编码为空
	$("#BUTTON_DESC").attr("maxLength","2");
	$("#insPage #BUTTON_CODE").attr("disabled",true);
	$("#BUTTON_CSS").val("btn btn-default");
	//$("#SPAN_CSS").val("glyphicon");
	tog(t);
}

function viewStyle(t){
	var selected = JSON.parse(getSelections());
	var buttonHtml = '' ;
	for(var i = 0 ;i<selected.length;i++){
		buttonHtml+='<button type="button" '+
		'  class="'+selected[i]['BUTTON_CSS']+
		'  class="btn btn-default" style="margin-left: 5px"'+
		'  buttontoken="'+selected[i]['BUTTON_TOKEN']+
		//'" onclick="'+selected[i]['HTML_CLICK_NAME'].toLowerCase()+'(this)'+
		'" isHidden="'+selected[i]['ISHIDDEN']+
		'" position="'+selected[i]['HTML_POSITION']+
		//'" id="'+selected[i]['HTML_ID'].toUpperCase()+
		'" isCheckbox="'+selected[i]['ISCHECKBOX']+
		'" ><span class="'+selected[i]['SPAN_CSS']+
		'" aria-hidden="true"></span>'+selected[i]['BUTTON_NAME']+
		'</button>';
	}
	oTable.showModal('提示', buttonHtml);
}

function notNullver(){
	if($("#BUTTON_NAME").val()==''){ oTable.showModal('提示', "请输入按钮名称！"); return false; }
	//if($("#BUTTON_TOKEN").val()==''){ oTable.showModal('提示', "请输入按钮标识！"); return false; }
	if($("#HTML_CLICK_NAME").val()==''){ oTable.showModal('提示', "请输入按钮方法！"); return false; }
	//if($("#BUTTON_ENTITY").val()==''){ oTable.showModal('提示', "请输入按钮对应实体类！"); return false; }
	//if($("#ISCHECKBOX").val()==''){ oTable.showModal('提示', "请选择是否多选！"); return false; }
	//if($("#ISHIDDEN").val()==''){ oTable.showModal('提示', "请选择是否隐藏！"); return false; }
	//if($("#JSCONTENT").val()==''){ oTable.showModal('提示', "请输入js脚本内容！"); return false; }
	return true;
}


function save(t){
	if(!notNullver()){
		return ; 
	}
	if($("#JSCONTENT").val()=='' || $("#JSCONTENT").val()==null){
		$("#JSCONTENT").val(" ");
	}
	var cache_dataSourceCode =$("#cache_dataSourceCode").val();
	var _dataSourceCode=dataSourceCode;
	if(cache_dataSourceCode!=null&&cache_dataSourceCode.length>0){
		_dataSourceCode=cache_dataSourceCode;
	}
	var message ="";
	var buttonToken =$("#ins_or_up_buttontoken").val();
	message = transToServer(findBusUrlByButtonTonken(buttonToken,'',_dataSourceCode),getJsonOfJsContent());
	oTable.showModal('提示', message);
	back(t);
	queryTableByDataSourceCode(t,_dataSourceCode);
	
}

function getJsonOfJsContent() {
	var json = "{";
	$inspage.find("[id]").each(function() {
		if ($(this).attr("id") == 'JSCONTENT') {
			$(this).val(b64Encode($(this).val()));
		}
		json += "\"" + $(this).attr("id") + "\":" + "\"" + $(this).val() + "\",";
	});
	json = json.substring(0, json.length - 1);
	json += "}";
	return json;
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

//btoa()：字符串或二进制值转为Base64编码
  function b64Encode(str) { return btoa(encodeURIComponent(str)); }
//atob()：Base64编码转为原来的编码
  function b64Decode(str) { return decodeURIComponent(atob(str)); }


