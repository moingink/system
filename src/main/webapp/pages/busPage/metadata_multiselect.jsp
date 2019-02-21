<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<title>选择框</title>
<script src="../../vendor/jquery/jquery.min.js"></script>

<script src="../multiselect-master/multiselect.js"></script>
<script src="../multiselect-master/prettify.min.js"></script>
<link href="../multiselect-master/prettify.css" rel="stylesheet" />
<link href="../multiselect-master/style.css" rel="stylesheet" />

<script src="<%=request.getContextPath()%>/pages/js/reference.js"></script>
<script src="<%=request.getContextPath()%>/js/common.js"></script>
<script src="<%=request.getContextPath()%>/vendor/bootstrap/js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/vendor/bootstrap-table/src/bootstrap-table.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/vendor/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/vendor/bootstrap-table/src/bootstrap-table.css">

</head>
<body>

	<form class="form-horizontal">
		<div class="panel panel-primary">
			<div class="panel-body" id="bulidTable">
				<div class="panel-body" id="pageField">
					<div class="col-md-12">
						<div class="col-md-6">
							<div class="col-md-4" style="white-space:nowrap;">
								<label class="control-label" style="text-align: left" for="CHOSE_DATASOURCE_CODE">数据源：</label>
							</div>
							<div class="col-md-8">
								<div id="formart_multiselect" class="input-group">
									<input class="form-control" id="CHOSE_DATASOURCE_CODE" name="CHOSE_DATASOURCE_CODE" value="" type="TEXT" placeholder="请选择数据源" >
									<input class="form-control" id="DATASOURCE_CODE" name="DATASOURCE_CODE" value="" type="hidden">
									<span class="input-group-addon" style="cursor:pointer" onclick="chooseDataSource(this)" id="CHOOSE_DATASOURCE">
										<span class="glyphicon glyphicon-search"></span>
									</span> 
								</div>
							</div>
						</div>
						<div class="col-md-6">
							<div class="col-md-4" style="white-space:nowrap;">
								<label class="control-label" style="text-align: left" for="CHOOSE_OPTION">是否多选：</label>
							</div>
							<div class="col-md-8">
								<select class="form-control" id="CHOOSE_OPTION" name="CHOOSE_OPTION">
									<option selected="selected" value="">==请选择==</option>
									<option value="1">单选</option>
									<option value="0">多选</option>
								</select>
							</div>
						</div>
					</div>
					<div class="col-md-12">
						<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" /></div>
					<div class="col-md-12">参选字段</div>
						<div class="row" id="MultiSelectReferDiv">
						<div class="col-xs-5">
							<select name="from" id="multiselectRefer" class="form-control" size="10" multiple="multiple"></select>
						</div>
						<div class="col-xs-2">
							<button type="button" id="multiselectRefer_rightAll" class="btn btn-block"><i class="glyphicon glyphicon-forward"></i></button>
							<button type="button" id="multiselectRefer_rightSelected" class="btn btn-block"><i class="glyphicon glyphicon-chevron-right"></i></button>
							<button type="button" id="multiselectRefer_leftSelected" class="btn btn-block"><i class="glyphicon glyphicon-chevron-left"></i></button>
							<button type="button" id="multiselectRefer_leftAll" class="btn btn-block"><i class="glyphicon glyphicon-backward"></i></button>
							<button type="button" id="multiselect_upSelected" class="btn btn-block"><i class="glyphicon glyphicon-chevron-up"></i></button>
							<button type="button" id="multiselect_downSelected" class="btn btn-block"><i class="glyphicon glyphicon-chevron-down"></i></button>
						</div>
						<div class="col-xs-5">
							<select name="to" id="multiselectRefer_to" class="form-control" size="10" multiple="multiple"></select>
						</div>
						<input type='hidden' value='' id='multiselectReferHidden'>
					</div>
					<div class="col-md-12" style="margin-top: 10px;">回写字段</div>
					<div class="row" id="MultiSelectDiv">
						<div class="col-xs-5">
							<select name="from" id="multiselect" class="form-control" size="10" multiple="multiple"></select>
						</div>
						<div class="col-xs-2">
							<button type="button" id="multiselect_rightAll" class="btn btn-block"><i class="glyphicon glyphicon-forward"></i></button>
							<button type="button" id="multiselect_rightSelected" class="btn btn-block"><i class="glyphicon glyphicon-chevron-right"></i></button>
							<button type="button" id="multiselect_leftSelected" class="btn btn-block"><i class="glyphicon glyphicon-chevron-left"></i></button>
							<button type="button" id="multiselect_leftAll" class="btn btn-block"><i class="glyphicon glyphicon-backward"></i></button>
						</div>
						<div class="col-xs-5">
							<select name="to" id="multiselect_to" class="form-control" size="10" multiple="multiple"></select>
						</div>
					</div>
				</div>
			</div>
		</div>
	</form>
</body>

<script type="text/javascript">
var jsonMessage = $("#multiselectReferHidden", window.parent.document).val();
var inputFormart = $("#INPUT_FORMART", window.parent.document).val();
var context='<%=request.getContextPath()%>';

$(function() {
	$('#multiselect').multiselect({
		keepRenderingSort : false,
		sort : false,
		afterMoveToLeft : function($left, $right, options) {
			$(options).removeAttr("style");
		},
		afterMoveToRight : function($left, $right, options) {
			$(options).attr("selected",false);
		}
	});
	$('#multiselectRefer').multiselect({
		keepRenderingSort : false,
		sort : false,
		afterMoveToRight : function($left, $right, options) {
			$(options).attr("selected",false);
			initSort();
		},
	});
	
	//初始化参选字段内容
	$("#multiselect").html(jsonMessage);
	//上下移动排序
	$("#multiselect_downSelected,#multiselect_upSelected").click(function() {
		var $opt = $("#multiselectRefer_to option:selected:first");
		if (!$opt.length) return;
		if (this.id == "multiselect_upSelected") {
			$opt.prev().before($opt);
			synWriteSort($opt.index());
		} else {
			$opt.next().after($opt);
			synWriteSort($opt.index());
		}
	});
	//按Alt加上下鍵也可以移动
	$("#multiselectRefer_to").keydown(function(evt) {
		if (!evt.altKey) return;
		var k = evt.which;
		if (k == 38) {
			$("#multiselect_upSelected").click();
			return false;
		} else if (k == 40) {
			$("#multiselect_downSelected").click();
			return false;
		}
	});
	
	//参选option默认值
	if("" != inputFormart){
		var dataSourceCode = inputFormart.substr(inputFormart.indexOf('(')+1,inputFormart.indexOf(',')-4).trim();
		var isCheckbox = inputFormart.substr(inputFormart.lastIndexOf(',')+1).match(/(\d)/)[1];
		$("#CHOOSE_OPTION").val(isCheckbox);
		$("#CHOSE_DATASOURCE_CODE").val(dataSourceCode);
		$("#multiselectRefer").html(getRefOption(dataSourceCode));
		initRefOption(splitRefContent(inputFormart  , 0 ) ,"multiselectRefer");
		initRefOption(splitRefContent(inputFormart  , 1 ) ,"multiselect");
	}
	
	//点击参选被选中字段时，回写字段相应下标同时被选中
	initSort();
	
});	

function initSort(){
	$("#multiselectRefer_to option").click(function(){ 
		synWriteSort($(this).index());
	});
}

function synWriteSort(index){
	$("#multiselect_to option").css("background-color","#FFFFFF");
	$("#multiselect_to option:eq("+index+")").css("background-color","#3399FF");
}

/**当参选不为空时，初始化参选框值*/
function initRefOption(objarr, refOrWrite){
	$("#"+refOrWrite+" option").each(function() {
		if(objarr.indexOf($(this).val()) != -1 ){
			$(this).appendTo($("#"+refOrWrite+"_to"));
		}
	});
}

function splitRefContent(obj , type){ //type:0参照字段    1页面回写字段
	var refField = obj.split(',')[1].split(';');
	var a = "" ; 
	for(var i = 0 ; i < refField.length ; i++){
		a += refField[i].split(':')[type] + ',';
	}
	return subStringField(a).split(',') ; 
}

/**截取字符串末含有“,”的字符串*/
function subStringField(object) {
	if (object.length > 0) {
		return object.substr(0, object.length - 1);
	} else {
		return "";
	}
}

function chooseDataSource (t) {
  checkReference(t,'REF(CD_DATASOURCE,DATASOURCE_CODE:DATASOURCE_CODE;DATASOURCE_CODE:CHOSE_DATASOURCE_CODE,1)','','');
}
function ref_end(){
	$("#multiselectRefer_to").find("option").remove(); 
	var dataSourceCode = $("#DATASOURCE_CODE").val();
    $("#multiselectRefer").html(getRefOption(dataSourceCode));
}

function getRefOption(sourceCode){
	var getReturnFieldUrl = context+'/multiselect?cmd=getReturnField&dataSourceCode='+sourceCode;
    var message = transToServer(getReturnFieldUrl,null);
    var optionHtml = "" ; 
    for(var i = 0 ; i < message.length ; i ++){
    	if(""==message[i]['FIELD_NAME'] || null == message[i]['FIELD_NAME']){
    		optionHtml += "<option value="+message[i]['FIELD_CODE']+">"+message[i]['FIELD_CODE']+"</option>" ; 
    	}else{
    		optionHtml += "<option value="+message[i]['FIELD_CODE']+">"+message[i]['FIELD_CODE']+"→"+message[i]['FIELD_NAME']+"</option>" ; 
    	}
    }
    return optionHtml ; 
}

    </script>
</html>