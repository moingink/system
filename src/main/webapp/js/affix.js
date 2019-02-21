var fileuploadmaxSize = 100;//文件上传大小限制
var _isLoadTableData=false;
function findUrl(modifyName) {
	return findUrlParam('base', modifyName, '');
}

function findUrlParam(actionName, modifyName, params) {
	var url = context + "/" + actionName + "?cmd=" + modifyName + params;
	return findUrlByToken(url);
}

function findUrlByToken(url){
	var mark ="?"
	if(url.indexOf("?")!=-1){
		mark ="&";
	}
	return url+mark+"token="+token;
}

String.prototype.format = function(args) {
	if (arguments.length > 0) {
		var result = this;
		if (arguments.length == 1 && typeof (args) == "object") {
			for ( var key in args) {
				var reg = new RegExp("({" + key + "})", "g");
				result = result.replace(reg, args[key]);
			}
		} else {
			for (var i = 0; i < arguments.length; i++) {
				if (arguments[i] == undefined) {
					return "";
				} else {
					var reg = new RegExp("({[" + i + "]})", "g");
					result = result.replace(reg, arguments[i]);
				}
			}
		}
		return result;
	} else {
		return this;
	}
}

function findPageParam() {
	return findPageParamByDataSourceCode(dataSourceCode);
}

function findPageParamByDataSourceCode(_dataSourceCode) {
	var pageParam = "&dataSourceCode=" + _dataSourceCode;
	//组装查询参数
	$("[id^=SEARCH-]").each(function() {
		if($(this).val().length > 0){
	  		pageParam += "&"+$(this).attr("id")+"="+$(this).val();
	  		}
	  	});
	return encodeURI(pageParam);
}





/**
 *由insPage块获取字段id、value，组装为json字符串 
 */
function getJson($div){
	var json = "{";
	$div.find("[id]").each(function() {
	  json += "\"" + $(this).attr("id") + "\":" + "\"" + $(this).val() + "\",";
	});
	json = json.substring(0,json.length-1);
	json += "}";
	json=replaceNSpace(json);
	return json;
}

function replaceNSpace(jsonString){
	if(jsonString!=null&&jsonString.indexOf("\n")!=-1){
		jsonString=jsonString.replace(/\n/g,"\\n");
	}
	return jsonString;
}

function findCheckMessage() {
	var message = "table:" + JSON.stringify(oTable.bootMethod($table, "getSelections"));
	oTable.showModal('modal', message);
}

/*function getSelections () {
	//注意：bootstrap-table内置getSelections方法所返回的json不能直接用于ajax传输——存在JSONNull数据
	return JSON.stringify(oTable.bootMethod($table, "getSelections"));
}
*/
function getSelections() {
	//注意：bootstrap-table内置getSelections方法所返回的json不能直接用于ajax传输——存在JSONNull数据
	if(isDetail=="1"){
		return getData();
	}else{
		return JSON.stringify(oTable.bootMethod($table, "getSelections"));
	}
}

function getData () {
	//注意：bootstrap-table内置getSelections方法所返回的json不能直接用于ajax传输——存在JSONNull数据
	return JSON.stringify(oTable.bootMethod($table, "getData"));
}

function getDataJsonArray() {
	//注意：bootstrap-table内置getSelections方法所返回的json不能直接用于ajax传输——存在JSONNull数据
	return oTable.bootMethod($table, "getData");
}

function getTableJsons(){
	return oTable.bootMethod($table, "getSelections");
}

function getTableJsonsByDiv($t){
	return oTable.bootMethod($t, "getSelections");
}

function  busEvent(t,buttonParams,_dataSourceCode){
	
	var message = transToServer(findBusUrlByPublicParam(t,buttonParams,_dataSourceCode),getSelections());
	oTable.showModal('modal', message);
}

function findBusUrlByPublicParam(t,buttonParams,_dataSourceCode){
	var buttonToken = $(t).attr("buttonToken");
	return findBusUrlByButtonTonken(buttonToken,buttonParams,_dataSourceCode);
}

function findBusUrlByButtonTonken(buttonToken,buttonParams,_dataSourceCode){
	return  findUrlParam('buttonBase','button','&buttonToken='+buttonToken+findPageParamByDataSourceCode(_dataSourceCode)+buttonParams);
}

function loadJs(file) {
	var script=document.createElement("script");  
	script.type="text/javascript";  
	script.src=file;  
	document.getElementsByTagName('head')[0].appendChild(script);  
}


function loadScript(url, callback) {
	  var script = document.createElement("script");
	  script.type = "text/javascript";
	  if(typeof(callback) != "undefined"){
		//页面模板默认按钮暂时加载
		callback();
	    if (script.readyState) {
	      script.onreadystatechange = function () {
	        if (script.readyState == "loaded" || script.readyState == "complete") {
	          script.onreadystatechange = null;
	          callback();
	        }
	      };
	    } else {
	      script.onload = function () {
	        callback();
	      };
	    }
	  }
	  script.src = url;
	  document.body.appendChild(script);
	}

function loadJsIsFLEF(filespec){
	$.load( filespec , ''  , function(responseText, textStatus, XMLHttpRequest){
	    if(textStatus !== "success"){
	        alert("文件下载失败");//console.log("文件下载失败");
	    }
	} );
}

function loadBusJsByDataSource(dataSourceCode){
	
	loadJs(findBusJsUrl(dataSourceCode));
}

function findBusJsUrl(dataSourceCode){
	var fileName=dataSourceCode;
	if(menuCode!=''&&menuCode.length>0){
		fileName=dataSourceCode+'_'+menuCode;
	}
	
	var file =context+"/pages/busJs/"+fileName+".js";
	return file;
}

function appendChildUrl(url,_dataSourceCode,ParentPKField,ParentPKValue){

	return url+"&_dataSourceCode="+_dataSourceCode
		      +"&ParentPKField="+ParentPKField
		      +"&ParentPKValue="+ParentPKValue;
}

function clickFunction(row,tr){
	
}

function bulidCacheTableJsonArray(){
	
}



function isToggle(){
	
	$("div[ishidden=true]").toggle();
}

//不使用组件上传附件
function uploadAffix(tid){
//	var fileId=1122;
//	$("#showfiles_"+tid).append("<div class=\"col-md-4 listmessage\" id='"+ fileId+ "'>" +
//			"<div class=\"col-md-9 listhide\" data-toggle=\"tooltip\" data-placement=\"top\" title=\""+"文件名"+"\">"+"文件名"+"</div>" +
//			"<div class=\"col-md-3 listbtn\">" +
//			"<a href='/document/base?cmd=downloadAffix&fid="+ fileId+ "' class=\"btnlist\"  data-toggle=\"tooltip\" data-placement=\"top\" title=\"下载\"><span class=\"glyphicon glyphicon-download-alt\"></span></a>" +
//			"<a href='javascript:deleteAffix(\""+ fileId+ "\")' class=\"btnlist\"  data-toggle=\"tooltip\" data-placement=\"top\" title=\"删除\"><span class=\"glyphicon glyphicon-trash\"></span></a>" +
//			"</div>"+
//			"</div>")
//	$("[data-toggle='tooltip']").tooltip();
//	return;
	

	var $a = $("#"+tid);
	var bs_code=$("#bs_code").val();
	var val = document.getElementById("anchor_"+tid).value;
	if (val != "") {
		var inputid="#anchor_"+tid;
		var $aind=$(inputid);
		var fsize = $aind[0].files[0].size;//文件的大小，单位为字节
		var filesizekb = (fsize / 1024).toFixed(2);//kb
		var filesize = (fsize / (1024*1024) ).toFixed(2);//mb
		if (filesize<=fileuploadmaxSize) {
			$.ajax({
				type: "POST",
				url: "/document/base?cmd=uploadAffix&fieldCode="+tid+"&batchNo="+$a.val()+"&bs_code="+bs_code+"&isbusiness=1",
				data: new FormData($a.parent()[0]),
				dataType: "json",
				async: false,  //默认是true：异步，false：同步。
				cache: false,  
				contentType: false,  
				processData: false,
				success: function (data) {
					if (data['status']!=null&&data['status']!=undefined&&data['status']=="fail") {
						console.info(window.location.href)
						alert("业务字段获取失败！请联系系统管理员。");
						return;
					}
					$("#fileerrorinfo").remove();//清空【此条数据未上传附件】
					$a.val(data['batchno']);
					var fileId = data['fileid'];
					$("#showfiles_"+tid).append("<div class=\"col-md-4 col-xs-4 col-sm-4 listmessage\" id='"+ fileId+ "'>" +
							"<div class=\"col-md-9 col-xs-9 col-sm-9 listhide\" data-toggle=\"tooltip\" data-placement=\"top\" title=\""+ data['filename'] +"\">"+ data['filename'] +"</div>" +
							"<div class=\"col-md-3 col-xs-3 col-sm-3 listbtn\">" +
							"<a href='/document/base?cmd=downloadAffix&fid="+ fileId+ "' class=\"btnlist\"  data-toggle=\"tooltip\" data-placement=\"top\" title=\"下载\"><span class=\"glyphicon glyphicon-download-alt\"></span></a>" +
							"<a href='javascript:deleteAffix(\""+ fileId+ "\")' class=\"btnlist\"  data-toggle=\"tooltip\" data-placement=\"top\" title=\"删除\"><span class=\"glyphicon glyphicon-trash\"></span></a>" +
							"</div>"+
							"</div>");
					$("[data-toggle='tooltip']").tooltip();

				},
				error: function(data) {
					alert("上传附件失败，请联系管理员"+data);
				},
				failure:function (data) {  

			            alert('Failed');  

			    }
			});
		} else {
			alert("上传附件太大!请传输100MB及以下大小的文件");
		}
	} else {
		alert("未选择文件，不进行提交");
	}
	clearFile();
}


//删除附件
function deleteAffix(fid){
	var _fid = fid;
		if(confirm('确定删除此文件吗?')){
			var message = transToServer('/document/base?cmd=deleteAffix&fid='+_fid,null);
			console.log(message);
			if(message.indexOf('成功') > 0){
				$("#"+_fid).remove();
			}
			oTable.showModal('modal',message);
		};
}
//清空file域
function clearFile(){ 
	var file = $(".file").each(function(index) {
	  $(this).after($(this).clone().val(""));
	  $(this).remove();
	}); 
}

//根据给定条件查询单条记录
function querySingleRecord(param){
    var record = "";
    $.ajax({
    	async: false,
    	type: "post",
		url: findUrlParam('base',"querySingleRecord",param),
		dataType: "json",
		success: function(data){
			record = data;
		}
	});
    return record;
  }
  
//js浮点数加法运算-防止精度bug
function accAdd(arg1,arg2){
	var r1,r2,m;
    try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}
    try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}
    m=Math.pow(10,Math.max(r1,r2));
    return (arg1*m+arg2*m)/m;
    }
//js浮点数减法运算-防止精度bug
function accSub(arg1,arg2){
	var r1,r2,m,n;
	try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}
	try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}
	m=Math.pow(10,Math.max(r1,r2));
	//动态控制精度长度
	n=(r1>=r2)?r1:r2;
	return ((arg1*m-arg2*m)/m).toFixed(n);
	}

/**
 * 乘法运算，避免数据相乘小数点后产生多位数和计算精度损失。
 *
 * @param num1被乘数 | num2乘数
 */
function numMulti(num1, num2) {
 var baseNum = 0;
 try {
  baseNum += num1.toString().split(".")[1].length;
 } catch (e) {
 }
 try {
  baseNum += num2.toString().split(".")[1].length;
 } catch (e) {
 }
 return Number(num1.toString().replace(".", "")) * Number(num2.toString().replace(".", "")) / Math.pow(10, baseNum);
};


function isWriteNull(variable){
	if (variable != null && variable != undefined &&  variable != 'undefined' ) {
		return false;
	}else{
		return true;
	}
	
}

function inputFocus(obj) {
	var tiphtml = '<div class="panel panel-primary" id="inputTips" style="position:absolute; width:220px; height: auto; min-height:50px; z-index:999; display: none; ">' +				
				'<div class="panel-body" id="inputValue" style="word-break:break-all;">' +
				'</div>' +
				'</div>'
	$(obj).parent().append(tiphtml);
	$("#inputTips").slideDown(200);
	$(obj).attr("oninput", "changeValue()");
	var x = document.activeElement.value;
	$("#inputValue").html(x);
};
function inputBlur(obj){
	$("#inputTips").slideUp(200);
	$(obj).removeAttr("oninput");	
	$("#inputTips").remove();	
}
function changeValue() {	
	//alert(this.value);
	var inputVal = document.activeElement.value;
	$("#inputValue").html(inputVal);		
}

var _isLoadStart=false;
function loadStart(){
	if(!_isLoadStart){
		$.bootstrapLoading.start({ loadingTips: "正在加载数据，请稍候..." });
		_isLoadStart=true;
	}
}

function loadStartTitle(title){
	if(!_isLoadStart){
		$.bootstrapLoading.start({ loadingTips: title });
		_isLoadStart=true;
	}
}
function loadEnd(){
	$.bootstrapLoading.end();
	_isLoadStart=false;
}

function loadEndByTime(time){
	if(time==null){
		time=0;
	}
	setTimeout(function(){
		$.bootstrapLoading.end();
	},time);
	_isLoadStart=false;
}