var isAuditValidate = false;
var $batchNo="";//页面缓存附件信息
var bcsdcode="";//页面缓存bs_code 业务编码

function queryTable(t) {
	var param =_query_param;
	if(_query_param==null||_query_param.length==0){
		param=initTableParam;
	}

	queryTableByParam(t,dataSourceCode,param);
}

function queryTableByDataSourceCode(t,v_dataSourceCode){
	var param =_query_param;
	if(_query_param==null||_query_param.length==0){
		param=initTableParam;
	}
	queryTableByParam(t,v_dataSourceCode,param);
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

function detail(t){
	detailDataByDataSourceCode(t,dataSourceCode);
}

function detailDataByDataSourceCode(t,dataSourceCode){
	var selected = JSON.parse(getSelections());
	
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
	if(!validateDel(selected)){
		return ;
	}
	var message = transToServer(findBusUrlByPublicParam(t,'',_dataSourceCode),getSelections());
	oTable.showModal('modal', message);
	queryTableByDataSourceCode(t,_dataSourceCode);
}

function back(t){
	var cache_dataSourceCode =$("#cache_dataSourceCode").val();
	var _dataSourceCode=dataSourceCode;
	if(cache_dataSourceCode!=null&&cache_dataSourceCode.length>0){
		_dataSourceCode=cache_dataSourceCode;
	}
	togByDataSourceCode(t,_dataSourceCode);
	$inspage.find('[id]').val("");
	$("#ins_or_up_buttontoken").val("");
	//重置附件组件
	//$('.file').fileinput('reset');
	//不使用组件时清空file域
	clearFile();
	//清空附件清单
	//$("[id^=fileList]").children("li").remove();
	if ($batchNo!="" && $batchNo!=null) {
		if ($("#showfiles_"+$batchNo.attr("ID"))!="") {
			$("#showfiles_"+$batchNo.attr("ID")).empty();//清除
		}
	}
	
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

function saveByDataSourceCode(t,_dataSourceCode){
	savaByQuery(t,_dataSourceCode,$inspage);
}

function savaByQuery(t,_dataSourceCode,$div){
	var message ="";
	var buttonToken =$("#ins_or_up_buttontoken").val();
	message = transToServer(findBusUrlByButtonTonken(buttonToken,'',_dataSourceCode),getJson($div));
	oTable.showModal('modal', message);
	back(t);
	queryTableByDataSourceCode(t,_dataSourceCode);
}

function savaByQueryForSuper(t,_dataSourceCode,$div){
	var message ="";
	var buttonToken =$(t).attr("buttonToken");
	message = transToServer(findBusUrlByButtonTonken(buttonToken,'',_dataSourceCode),getJson($div));
	oTable.showModal('modal', message);
}

function updateRow(t){
	updataRowByDataSourceCode(t,dataSourceCode);
	
	if (""!=bcsdcode) {//点击修改时如有附件元素则将页面缓存的bcsdcodeSET到bs_code属性中
		$('input[id=bs_code]').each(function() {
			// console.info(this)
			//alert("1")
			$(this).val(bcsdcode);
		});
		//$("#bs_code").val(bcsdcode);
	}
}

/*附件集体展示iframe Start*/

/**
 * 加载附件集体展示iframe
 * 	//点击修改后操作html
 *	//showAffixLoad();
 */
function showAffixLoad(){
	var fileHiddenBatChNoValue= getFileHiddenBatChNo();//本页面隐藏的fileinptut的批次号的值
	var fileHiddenBatChNoValue_batchno=fileHiddenBatChNoValue[0];
	var fileHiddenBatChNoValue_affixname=fileHiddenBatChNoValue[1];
	fileHiddenBatChNoValue_affixname=fileHiddenBatChNoValue_affixname.replace("：","");
	var cache_dataSourceCode =$("#cache_dataSourceCode").val();//页面数据源名称
	
	if(fileHiddenBatChNoValue_batchno!="" || initShowAffixParam!="{}"){
		//console.info("此页面的附件参数{}"+initShowAffixParam)
		if(fileHiddenBatChNoValue_batchno!=""){
			//开始拼接本页面的附件数据 --json格式
			var ThispageAffixValus="{\"affixtype\":\"附件类型1\",\"bs_code\":\""+cache_dataSourceCode+"\",\"batchNo\":\""+fileHiddenBatChNoValue_batchno+"\",\"affixname\":\""+fileHiddenBatChNoValue_affixname+"\"}";
			//本页面参数加其他参数
			var eventuallyParam="["+
				ThispageAffixValus +","+
				initShowAffixParam +
			"]";//最终
		}else{
			//本页面参数加其他参数
			var eventuallyParam="["+
				initShowAffixParam +
			"]";//最终
		}
		
		eventuallyParam=JSON.stringify(eventuallyParam);
		initShowAffixParamEventually=eventuallyParam;
		//alert(initShowAffixParamEventually)
		//console.info(eventuallyParam)//stringType
		//console.log(JSON.parse(eventuallyParam));//jsonType
		showAffixLoadAdd();
		//setTimeout(iframeFunctionTrigger(eventuallyParam),"5000");
	}else{
		console.info("此页面无配置附件参数{}");
		deleteLoadAffixJsp();
	}
}

/**
 * 取得页面隐藏标签存值的value值--批次号
 * @returns
 */
function getFileHiddenBatChNo(){
	var result = new Array();
	$inspage.find(".file").each(function() {
		var _value = $(this).parent().siblings(".fileHidden").attr("value");
		var _id = $(this).parent().siblings(".fileHidden").attr("id");
		var _isnewbchn = $(this).parent().siblings(".fileHidden").attr("isnewbchn");//是不是新加的批次号
		if (_isnewbchn==0) {
			affixname=$("label[for='"+_id+"']").text();
			result[0]=_value;
			result[1]=_affixname;
			//alert("是新加的批次号：证明原来没有附件，不用构建iframe"+result)
			return result;
		}else if (_isnewbchn==1) {
			result[0]="";
			result[1]="";
			//alert("是新加的批次号：证明原来没有附件，不用构建iframe")
			return result;
		}
	});
}
/**
 * 加载附件集中展示jsp外面的div
 */
function initLoadAffixJsp(){
	 $("#insPage").append("<div style=\"overflow: auto; padding:0;\" class=\"col-md-12\" id=\"showaffixjspdiv\">"+
			 "</div>");
}
/**
 * 去除附件集中展示jsp外面的div
 */
function deleteLoadAffixJsp(){
	 $("#showaffixjspdiv").empty();
}
/**
 * 在element添加附件集中展示jsp的iframe
 */
function showAffixLoadAdd(){
	var showAffixJspIframe_element = $("#showAffixJspIframe");
	if(!showAffixJspIframe_element.length >0){ 
	    setParntHeigth($("#insPage").height()+500);
	}
	$('#showaffixjspdiv').empty();
	$("#showaffixjspdiv").html("<iframe id=\"showAffixJspIframe\" name=\"showAffixJspIframe\" width=100% height=400px frameborder=\"0\" scrolling=\"auto\" src=\"showAffix.jsp\"></iframe>");
	
}

/**
 * 触发iframe中函数
 * @param v jsonString
 */
var while_size=15;


function iframeFunctionTrigger(v){
	console.info("执行iframeFunctionTrigger{}")
	var childWindow = $("#showAffixJspIframe")[0].contentWindow; //表示获取了嵌入在iframe中的子页面的window对象。  []将JQuery对象转成DOM对象，用DOM对象的contentWindow获取子页面window对象。
	if(childWindow.qwet==undefined){
		if(while_size>0){
			setTimeout("iframeFunctionTrigger('"+v+"')",500);
			while_size--;
		}else{
			alert("响应失败！")
		}
	}else{
		childWindow.qwet(v);
	}
}


/**
 * 触发iframe中函数
 * @param v jsonString
 */
function iframeFunctionTriggert(v){
	console.info("执行iframeFunctionTrigger{}")
	var childWindow = $("#showAffixJspIframe")[0].contentWindow; //表示获取了嵌入在iframe中的子页面的window对象。  []将JQuery对象转成DOM对象，用DOM对象的contentWindow获取子页面window对象。
	if(childWindow.qwett==undefined){
		if(while_size>0){
			setTimeout("iframeFunctionTriggert('"+v+"')",500);
			while_size--;
		}else{
			alert("响应失败！")
		}
	}else{
		childWindow.qwett(v);
	}
}

/**
 * 组装合同信息里面的附件信息
 * @param v jsonString
 */
function iframeFunctionBusContractAdminAttachment(v){
	var childWindow = $("#showAffixJspIframe")[0].contentWindow; //表示获取了嵌入在iframe中的子页面的window对象。  []将JQuery对象转成DOM对象，用DOM对象的contentWindow获取子页面window对象。
	if(childWindow.busContractAdminAttachment==undefined){
		if(while_size>0){
			setTimeout("iframeFunctionBusContractAdminAttachment('"+v+"')",500);
			while_size--;
		}else{
			alert("响应失败！")
		}
	}else{
		childWindow.busContractAdminAttachment(v);
	}
	
}

/**
 * 组装前评估信息里面的附件信息
 * @param v jsonString
 */
function iframeFunctionPreAssessmentAttachment(v){
	var childWindow = $("#showAffixJspIframe")[0].contentWindow; //表示获取了嵌入在iframe中的子页面的window对象。  []将JQuery对象转成DOM对象，用DOM对象的contentWindow获取子页面window对象。
	if(childWindow.preAssessmentAttachment==undefined){
		if(while_size>0){
			setTimeout("iframeFunctionPreAssessmentAttachment('"+v+"')",500);
			while_size--;
		}else{
			alert("响应失败！")
		}
	}else{
		childWindow.preAssessmentAttachment(v);
	}
}



/**
 * 刷新iframe--强制
 * @param t
 */
function iframeRefresh(t){
	var reshSrc="showAffix.jsp";
	  var iframe1=document.getElementById("showAffixJspIframe"); 
	　　 iframe1.src = reshSrc;
}
/*附件集体展示iframe End*/

function updataRowByDataSourceCode(t,_dataSourceCode){
	updataRowByQuery(t,_dataSourceCode,$inspage);
}

function updataRowByQuery(t,_dataSourceCode,$updateDiv){
	var buttonToken=$(t).attr("buttonToken");
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('modal', "请选择一条数据进行修改");
		return;
	}
	if(isNewModel == true){
		$updateDiv.html(tempUpdHtml);
		if(typeof handle_common === "function"){
			handle_common();
		}
		if(typeof handle_update === "function"){
			handle_update();
		}
	}
	if(!validateUpdate(selected)){
		return ;
	}
	$updateDiv.find("[id]").each(function() {			
	  	$(this).val(selected[0][$(this).attr("id")]);//清空
	});
	
//	//存在文件字段时需要由后台获取附件清单
	getFileTypeVal($updateDiv);

	$("#ins_or_up_buttontoken").val(buttonToken);
	$("#cache_dataSourceCode").val(_dataSourceCode);
	togByDataSourceCode(t,_dataSourceCode);
}

/**
 * 查找层内是否有.file类型的input拿到docID追加内容
 * @param $Div
 */
function getFileTypeVal($Div){
	//存在文件字段时需要由后台获取附件清单
	$Div.find(".file").each(function() {
		$batchNo = $(this).parent().siblings(".fileHidden");
		var batchNo = $batchNo.val();
		
		if( batchNo != null && batchNo.length > 0){
			var findId ="#showfiles_"+$batchNo.attr('ID');
			$showFiles =$Div.children().find(findId);
			var tempHtml = transToServer('/document/base?cmd=getAffixList&batchNo='+batchNo,"");
			if(tempHtml != null ){
				if (tempHtml=="") {
					tempHtml="<div id=\"fileerrorinfo\"><p style=\"color:red\">此条数据未上传附件</p></div>";
				}
				$showFiles.empty();
				$showFiles.append(tempHtml);
			}else{
				alert("请求附件清单失败！请联系管理员");
				return;
			}
		}else{
			$("#showfiles_"+$batchNo.attr("ID")).empty();
		}
	});
}

/**
 * 查找层内是否有.file类型的input拿到docID追加内容--适应docID后追加且不需要显示删除按钮
 * @param $Div
 */
function getFileTypeValTo($Div){
	//存在文件字段时需要由后台获取附件清单
	$Div.find(".file").each(function() {
		$batchNo = $(this).parent().siblings(".fileHidden");
		var batchNo = $batchNo.val();
		
		if( batchNo != null && batchNo.length > 0){
			var findId ="#showfiles_"+$batchNo.attr('ID');
			$showFiles =$Div.children().find(findId);
			var tempHtml = transToServer('/document/base?cmd=getAffixList&batchNo='+batchNo,"");
			if(tempHtml != null ){
				if (tempHtml=="") {
					tempHtml="<div id=\"fileerrorinfo\"><p style=\"color:red\">此条数据未上传附件</p></div>";
				}
				$showFiles.empty();
				$showFiles.append(tempHtml);
				//
				//隐藏上传框与框描述
				$Div.find("#kuang").css("display","none");
				$Div.find("#kuangname").css("display","none");
				//隐藏删除按钮
				$Div.children().find('[id=files_a_del]').remove();
				//
			}else{
				alert("请求附件清单失败！请联系管理员");
				return;
			}
		}else{
			$("#showfiles_"+$batchNo.attr("ID")).empty();
		}
	});
}

function hideFile($Div){
	//存在文件字段时需要由后台获取附件清单
	$Div.find(".file").each(function() {
		$batchNo = $(this).parent().siblings(".fileHidden");
		var batchNo = $batchNo.val();
		if( batchNo != null && batchNo.length > 0){
			var findId ="#showfiles_"+$batchNo.attr('ID');
			$showFiles =$Div.children().find(findId);
			var tempHtml = transToServer('/document/base?cmd=getAffixList&batchNo='+batchNo,"");
			if(tempHtml != null ){
				if (tempHtml=="") {
					tempHtml="<div id=\"fileerrorinfo\"><p style=\"color:red\">此条数据未上传附件</p></div>";
				}
				$showFiles.empty();
				$showFiles.append(tempHtml);
				//隐藏上传框与框描述
				$(this).parent().parent().parent('[id="kuang"]').css("display","none");
				//隐藏删除按钮
				$Div.children().find('[id=files_a_del]').remove();
			}else{
				alert("请求附件清单失败！请联系管理员");
				return;
			}
		}else{
			$("#showfiles_"+$batchNo.attr("ID")).empty();
		}
	});
}

function tog(t){
	if ($batchNo!="") {
		$("#showfiles_"+$batchNo.attr("ID")).empty();//清除
	}
//	if ($("#bs_code").val()=="") {//新增时保存bs_code
//		$("#bs_code").val(bcsdcode);
//	}
//	bcsdcode=$("#bs_code").val();
		
	if(isNewModel == true){
		$inspage.html(tempInsHtml);
		if(typeof handle_common === "function"){
			handle_common();
		}
		if(typeof handle_insert === "function"){
			handle_insert();
		}
	}
	togByDataSourceCode(t,dataSourceCode);
	
	
}

var html_height=0;
function togByDataSourceCode(t,_dataSourceCode){
	bulidValid($inspage,validJson);
	var name =$(t).html();
	if(name!=null&&name.length>0){
		$("#tog_titleName").html(name);
	}
	var buttonToken=$(t).attr("buttonToken");
	$("#ins_or_up_buttontoken").val(buttonToken);
	$("#cache_dataSourceCode").val(_dataSourceCode);
	var $bulidtable = $("#bulidTable");
	var $bulidpage = $("#bulidPage");
	if($bulidtable.is(":hidden")){
		$bulidtable.slideDown();
		$bulidpage.slideUp();
		setParntHeigth(html_height);
	}else{
		$bulidpage.slideDown();
		$bulidtable.slideUp();
		html_height=$(document.body).height();
		setParntHeigth($("#insPage").height()+170);
	}
	
}

function bulidValid($div,_validJson){
	if($div==null){
		$div=$inspage;
	}
	if(_validJson==null){
		_validJson=validJson;
	}
	//加入null值判断 如果获取不到对象 不做处理
	if($div.data('bootstrapValidator') != null){
		$div.data('bootstrapValidator').destroy();
		$div.data('bootstrapValidator', null);
	}
	$div.bootstrapValidator(_validJson);
}

function validateDel(selected){
	if("1"==isAddAuditBut||isAuditValidate){
		if(typeof validateDelByAudit === "function"){
			return validateDelByAudit(selected);
		}
	}
	if(confirm("确定要删除选中数据吗？")){
		return true;
	}else{
		return false;
	}
	
}

function validateUpdate(selected){
	if("1"==isAddAuditBut){
		if(typeof validateUpdateByAudit === "function"){
			return validateUpdateByAudit(selected);
		}
	}
	return true;
}

//页面加载完毕后缓存一些值
window.onload = function(){
	if ($("#bs_code").val()!="" && $("#bs_code").val()!=undefined) {
		bcsdcode=$("#bs_code").val();
		//console.info("本页面有附件元素且bs_code value={}"+bcsdcode)
	}
}