var oTable = new TableInit();

var $table = $('#table');
var $queryParam = $("#queryParam");
var $inspage = $("#insPage");

//新模板用两变量分别存储后台返回的新增/修改页面html
var tempInsHtml = "";
var tempUpdHtml = "";

var buttonJson =null;
//var selectPageParam='';
//var listPageParam='';
//var maintainPageParam='';

var validJson ={} ;
var _isBulidListPage=false;
var _isBulidPage=false;
function bulidAlonePage() {
	bulidPage(true,true,true,true);
}

//function initPageParam(_selectPageParam,_listPageParam,_maintainPageParam){
//	selectPageParam=pageParamFormat(_selectPageParam);
//	listPageParam=pageParamFormat(_listPageParam);
//	maintainPageParam=pageParamFormat(_maintainPageParam);
//}


function bulidPage(isBulidSelect,isBulidButton,isBulidListPage,isBulidMaintainPage){
	//showLoading("请稍等！");
	_isBulidListPage=isBulidListPage;
	_isBulidPage=true;
	loadStart();
	if(dataSourceCode!=null&&dataSourceCode!='null'){
		if(pageName!=null&&pageName!='null'){
			$("#pageName").html(pageName);
		}
		if(isBulidSelect){
			bulidSelect($queryParam,dataSourceCode,'');
		}
		if(isBulidButton){
			bulidButton($("#button_div"),dataSourceCode);
		}
		if(isBulidListPage){
			var param =_query_param;
			if(_query_param==null||_query_param.length==0){
				param=initTableParam;
			}
			bulidListPage($table,dataSourceCode,param);
		}
		if(isBulidMaintainPage){
			bulidMaintainPage($inspage,dataSourceCode,'','add');
			validJson=transToServer(findUrlParam('base','queryValids','&dataSourceCode='+dataSourceCode),'');
		}
		//console.info("validJson=== "+JSON.stringify(validJson));
		$inspage.bootstrapValidator(validJson);
		$('.form_date').datetimepicker({
    		minView: 'month',         //设置时间选择为年月日 去掉时分秒选择
   			format:'yyyy-mm-dd',
    	    weekStart: 1,
    	    todayBtn:  1,
    	    autoclose: 1,
    	    todayHighlight: 1,
    	    startView: 2,
    	    forceParse: 0,
    	    showMeridian: 1,
    	    language: 'zh-CN'              //设置时间控件为中文
    	});
		
		$inspage.find('.form_date').find("input").each(function(){
			var column =$(this).attr("id");
			console.info("column===  "+column);
			$(this).change(function() {
				$("#insPage").data('bootstrapValidator').updateStatus(column, 
			             'NOT_VALIDATED',null).validateField(column);
			});
		});
		

    	//附件插件初始化
//    	 $('.file').fileinput({
//	         language: 'zh',
//	         hideThumbnailContent: true,
//	         uploadUrl:"/cdp/", 
//	         // uploadExtraData://上传时额外传入的参数
//	         // deleteUrl:
//	         // deleteExtraData:
//	         // allowedFileExtensions:['jpg','gif'],//允许的文件后缀
//	          showPreview: false,//是否显示预览区域
//	         preferIconicPreview: true,
//	         showCancel: false,
//	         showClose: false,
//	         maxFileSize: 102400,//KB
//	         maxFileCount: 100,
//	         dropZoneEnabled:false, //是否显示拖拽区域
//	         // dropZoneTitleClass//拖拽区域类属性
//	         // elCaptionText://标题栏提示信息
//	     });
    	//$("[data-toggle='tooltip']").tooltip();
    	
    	$("[data-toggle='tooltip']").tooltip({html : true });
    	//查看详情
    	_open_detail();
	}else{
		alert("dataSource is null");
	}
	if(_isLoadTableData){
		loadEnd();
	}
	_isBulidListPage=false;
	_isBulidPage=false;
}
//构建按钮
function bulidButton($b,_dataSourceCode){
	bulidButtonHtml($b,_dataSourceCode);
}

function bulidSelect($q,_dataSourceCode,_selectPageParam){
	oTable.initQueryParam($q, paramurl + findPageParamByDataSourceCode(_dataSourceCode)+_selectPageParam);
}

function bulidListPage($t,_dataSourceCode,_listPageParam){
	//oTable.initCols($t, colurl + findPageParamByDataSourceCode(_dataSourceCode), qusurl + findPageParamByDataSourceCode(_dataSourceCode)+_listPageParam);
	bulidListPageForQusUrl($t,_dataSourceCode,_listPageParam,qusurl);
}

function bulidListPageForQusUrl($t,_dataSourceCode,_listPageParam,_qusurl){
	//oTable.initCols($t, colurl + findPageParamByDataSourceCode(_dataSourceCode), qusurl + findPageParamByDataSourceCode(_dataSourceCode)+_listPageParam);
	if(_qusurl!=null){
		_qusurl= _qusurl + findPageParamByDataSourceCode(_dataSourceCode)+_listPageParam;
	}
	//console.info($t+"2==="+colurl+"3===" + findPageParamByDataSourceCode(_dataSourceCode)+"4==="+_qusurl);
	oTable.initCols($t, colurl + findPageParamByDataSourceCode(_dataSourceCode),_qusurl);
}


function bulidListPageForQusUrlAndColUrl($t,_dataSourceCode,_listPageParam,_qusurl,_colurl){
	//oTable.initCols($t, colurl + findPageParamByDataSourceCode(_dataSourceCode), _qusurl + findPageParamByDataSourceCode(_dataSourceCode)+_listPageParam);
	oTable.initCols($t, _colurl + findPageParamByDataSourceCode(_dataSourceCode),  _qusurl + findPageParamByDataSourceCode(_dataSourceCode)+_listPageParam);
}

function bulidMaintainPage($i,_dataSourceCode,_maintainPageParam,type){
	if(!_isBulidPage){
		loadStart();
	}
	if(_maintainPageParam!=null&&_maintainPageParam.length>0){
		tempInsHtml = oTable.initMaintainCols($i, findDatanurl + findPageParamByDataSourceCode(_dataSourceCode)+_maintainPageParam);
	}else{
		tempInsHtml = oTable.initMaintainCols($i, maintainurl + findPageParamByDataSourceCode(_dataSourceCode));
	}
	if(isNewModel != true){
		$i.append(tempInsHtml);	
	}else{
		//新模板获取独立修改页面（新增与修改分开）
		tempUpdHtml = oTable.initMaintainCols($i, indupdateurl + findPageParamByDataSourceCode(_dataSourceCode));
	}
	modifyAffixFileInput();
	setLable($i, _dataSourceCode);
	if (type=='add') {//新增
		changeAffixShow($i,"add");
	} else {//修改
		changeAffixShow($i,"mod");
	}
	if(!_isBulidListPage){
		var time =1000;
		if(!_isBulidPage){
			time=0;
		}
		loadEndByTime(time);
	}
}

//设置标签
function setLable($i, dataSourceCode){
	var lableArray = new Array();
	$.ajax({
		type: "GET",
		url: '/system/base/getLable?dataSourceCode='+dataSourceCode,
		async: false,
		dataType: "json",
		success: function(data) {
			lableArray = data;
		}
	});
	for(var i=0; i<lableArray.length; i++){
		var inputType = lableArray[i]["LABEL_AFTER_FIELD_INPUT_TYPE"];//字段类型
		var fieldCode = lableArray[i]["LABEL_AFTER_FIELD_CODE"];//字段编码
		var labelStyleHtml = lableArray[i]["LABEL_STYLE_HTML"];//标签html
		if(inputType == '0' || inputType == '1' || inputType == '2' || inputType == '6'){//文本框、下拉框、文本域、隐藏
			$i.find('#'+fieldCode).parent().parent().parent().before(labelStyleHtml);
		}else if(inputType == '3' || inputType == '4'){//参选、日期
			$i.find('#'+fieldCode).parent().parent().parent().parent().before(labelStyleHtml);
		}
	}
}

function modifyAffixFileInput(){
	/*修改原有扁平化file input标签的自适应宽度 start*/
	var $filef = $(".file").parent().parent().parent().parent().parent();
	$filef.addClass("col-md-12 col-xs-12 col-sm-12");
	$filef.removeClass("col-md-4 col-xs-4 col-sm-4");
	//$filef.css('background-color','#e3e2e2dd');
	var $file_Div1 = $(".file").parent().parent().parent();//框
	$file_Div1.addClass("col-md-6 col-xs-6 col-sm-6");
	$file_Div1.attr("id","kuang");
	$file_Div1.removeClass("col-md-8 col-xs-8 col-sm-8");
	var $file_Div2 = $file_Div1.prev();//框名
	$file_Div2.addClass("col-md-6 col-xs-6 col-sm-6");
	$file_Div2.attr("id","kuangname");
	$file_Div2.removeClass("col-md-4 col-xs-4 col-sm-4");
	$file_Div2.css('text-align','left');
	//$file_Div2.children("label").css('margin-left','50px');
	/*修改原有扁平化file input标签的自适应宽度 end*/
}
//前台改变附件展示
function changeAffixShow($bus_message_dbclick_DIV,v){
	//存在附件字段时页面追加“已上传文件列表”
	if (v=="add") {
		//alert("add")
		var constart=true;
		$bus_message_dbclick_DIV.find(".file").each(function() {
			var _id = $(this).parent().siblings(".fileHidden").attr("ID");
			//以下测试附件提交后展示的情况
			$(this).parent().parent().parent().parent().append("<div style=\"border-bottom: 1px dashed #c8c6c6; padding: 5px;\" class=\"col-md-12 col-xs-12 col-sm-12\" id=\"showfiles_"+_id+"\">" +
//				"<div class=\"col-md-4 listmessage\">" +
//				"<div class=\"col-md-9 listhide\" data-toggle=\"tooltip\" data-placement=\"top\" title=\""+"文件名"+"\">测试展示</div>" +
//				"<div class=\"col-md-3 listbtn\">" +
//				"<a href=\"\" class=\"btnlist\"  data-toggle=\"tooltip\" data-placement=\"top\" title=\"下载\"><span class=\"glyphicon glyphicon-download-alt\"></span></a>" +
//				"<a href=\"\" class=\"btnlist\"  data-toggle=\"tooltip\" data-placement=\"top\" title=\"删除\"><span class=\"glyphicon glyphicon-trash\"></span></a>" +
//				"</div>"+
//				"</div>"+
					//"<li class=\"listmessage\" id=\"\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"文件名\">1537351111111111111111111111111111111111111111111111111111111115410(1).jpg <a href=\"\" class=\"btnlist\"  data-toggle=\"tooltip\" data-placement=\"top\" title=\"下载\"><span class=\"glyphicon glyphicon-download-alt\"></span></a><a href=\"\" class=\"btnlist\"  data-toggle=\"tooltip\" data-placement=\"top\" title=\"删除\"><span class=\"glyphicon glyphicon-trash\"></span></a></li>"+
					//"<li class=\"listmessage\" id=\"\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"文件名\">1537355111111111111111111111111111111111111111111111111111111111111111111410(1).jpg <a href=\"\" class=\"btnlist\"  data-toggle=\"tooltip\" data-placement=\"top\" title=\"下载\"><span class=\"glyphicon glyphicon-download-alt\"></span></a><a href=\"\" class=\"btnlist\"  data-toggle=\"tooltip\" data-placement=\"top\" title=\"删除\"><span class=\"glyphicon glyphicon-trash\"></span></a></li>"+
					"</div>");
			//存在附件字段时页面追加“附件”标签
			if (constart) {
	    		$(this).parent().parent().parent().parent().parent().before("<div class=\"col-md-12 col-xs-12 col-sm-12 classifiedtitle\">附件</div>");
	    		constart=false;
			}
		});
//		var $file_Div1 = $(".file").parent().parent().parent();//框
//		var $file_Div2 = $file_Div1.prev();//框名
//		$file_Div1.css("display","block");//display: none
//		$file_Div2.css("display","block");
	}
	if(v=="mod") {
		//alert("mod")
		var constart=true;
		$bus_message_dbclick_DIV.find(".file").each(function() {
			var _id = $(this).parent().siblings(".fileHidden").attr("ID");
			$(this).parent().parent().parent().parent().append("<div style=\"border-bottom: 1px dashed #c8c6c6; padding: 5px;\" class=\"col-md-12\" id=\"showfiles_"+_id+"\">" +
					//"此条数据未上传附件"+		
			"</div>");
			//存在附件字段时页面追加“附件”标签
			if (constart) {
	    		$(this).parent().parent().parent().parent().parent().before("<div class=\"col-md-12 col-xs-12 col-sm-12 classifiedtitle\">附件</div>");
	    		constart=false;
			}
		});
		//隐藏上传框与框描述
		//$bus_message_dbclick_DIV.find("#kuang").css("display","none");
		//$bus_message_dbclick_DIV.find("#kuangname").css("display","none");
		$("div[id='kuang']").each(function (){//原有列表如有多附件字段则只会屏蔽第一个 因为城府id 现修改20181112_ys
			$(this).css("display","none");
		});
		$("div[id='kuangname']").each(function (){
			$(this).css("display","block");//框名显示
		});
		//页面追加附件
		getFileTypeVal($bus_message_dbclick_DIV);
		//隐藏删除按钮
		$bus_message_dbclick_DIV.children().find('[id=files_a_del]').remove();
	}

}

function setReadonlyByDiv($div){
	$div.each(function(){
		$(this).attr("readonly","readonly");
	});
}


function setUnReadonlyByDiv($div){
	$div.each(function(){
		$(this).attr("readonly","");
	});
}

function GetRequest() { 
	var url = location.search; //获取url中"?"符后的字串 
	var theRequest = new Object(); 
	if (url.indexOf("?") != -1) { 
	var str = url.substr(1); 
	strs = str.split("&"); 
	for(var i = 0; i < strs.length; i ++) { 
	theRequest[strs[i].split("=")[0]]=unescape(strs[i].split("=")[1]); 
	} 
	} 
	return theRequest; 
	}

function changeURLPar(destiny, par, par_value) 
	{ 
	var pattern = par+'=([^&]*)'; 
	var replaceText = par+'='+par_value; 
	if (destiny.match(pattern)) 
	{ 
	var tmp = '/\\'+par+'=[^&]*/'; 
	tmp = destiny.replace(eval(tmp), replaceText); 
	return (tmp); 
	} 
	else 
	{ 
	if (destiny.match('[\?]')) 
	{ 
	return destiny+'&'+ replaceText; 
	} 
	else 
	{ 
	return destiny+'?'+replaceText; 
	} 
	} 
	return destiny+'\n'+par+'\n'+par_value; 
} 

/**
 * 取得页面隐藏标签存值的value值--批次号
 * @returns
 */
function getFileHiddenBatChNo(){
	var result = new Array();
	var value="";
	var id;
	var addixname="";
	$inspage.find(".file").each(function() {
		var _value = $(this).parent().siblings(".fileHidden").attr("value");
		var _id = $(this).parent().siblings(".fileHidden").attr("id");
			id=_id;
			value=_value;
	});
	affixname=$("label[for='"+id+"']").text();
	result[0]=value;
	result[1]=affixname;
	return result;
}



