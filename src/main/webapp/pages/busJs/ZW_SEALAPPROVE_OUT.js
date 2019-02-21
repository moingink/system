buttonJson =[	
              {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
              {name:'新增',fun:'tog(this),tog1(this)',buttonToken:'add'},
              {name:'修改',fun:'updateRow(this)',buttonToken:'update'},
              {name:'删除',fun:'delRows(this)',buttonToken:'delete'},
              
			];
			
var isButton =false;
var NEED_PROCESS_CODE;

$(function() {
	$("#fileList_ENCLOSURE").after('<table class="table table-hover" style="width: 100%" id="need_handle_message_audit"></table>');
})

function tog1(t){
	$('#AGENT').val(userName);
	$('#AGENT').attr('readonly','true');
}

function ref_write_json(rejsonArray){
	if(rejsonArray.length == 0){
		oTable.showModal('modal', "请至少选择一条数据进行操作");
		return;
	}
	var BUS_ID = rejsonArray[0].ID;
	if(rejsonArray[0].TYPE == "商机"){
		NEED_PROCESS_CODE = 'system@BUS_BUS_EXAMINE@';
	}else if(rejsonArray[0].TYPE == "前评估"){
		NEED_PROCESS_CODE = 'system@PRE_ASSESSMENT@';
	}else if(rejsonArray[0].TYPE == "项目建议书"){
		NEED_PROCESS_CODE = 'system@PROJ_PROPOSAL@';
	}
	if(isButton){
		isButton=false;
		var tableNameValues = '';
		var buttonToken = "procedure";
		var dataSourceCode = "USER_TABLES"
		for(var i = 0;i< rejsonArray.length ; i++){
			tableNameValues += rejsonArray[i]['TABLE_NAME']+',';
		}
		tableNameValues = tableNameValues.substring(0,tableNameValues.length-1);
		var bindUrl = context+'/buttonBase?cmd=button&tableNameValues='+tableNameValues+'&buttonToken='+buttonToken+'&dataSourceCode='+dataSourceCode;
		oTable.showModal('modal', transToServer(bindUrl,JSON.stringify(rejsonArray)));
	}else{
		bulidListForNeedHandle(BUS_ID,NEED_PROCESS_CODE);
		return true;
	}
}

function bulidListForNeedHandle(BUS_ID,NEED_PROCESS_CODE) {
	var paramsAudit = '&buttonToken=notityNeedHandle&BUS_ID=' + BUS_ID + '&NEED_PROCESS_CODE=' + NEED_PROCESS_CODE ;
	var json = transToServer(findUrlParamByWorkFlow('buttonBase', 'button', paramsAudit), '');
	var html = '';
	for (var i = 0; i < json.length; i++) {
		var tr = "<tr>" + "<td width='13%' >" + json[i]["NEED_USER_NAME"] + "</td>" + "<td width='52%' >" + json[i]["AUDIT_MESSAGE"] + "</td>" + "<td width='35%' >" + json[i]["NEED_END_TIME"] + "</td>" + "</tr>";
		html = html + tr;
	}
	var thead = "<tr><td width='13%'>任务执行人</td><td width='52%' >审批意见</td><td width='35%' >结束时间</td></tr>";
	$("#need_handle_message_audit").html(thead+html);
};

function findUrlParamByWorkFlow(actionName, modifyName, params) {
	var url = "/workflow/" + actionName + "?cmd=" + modifyName + params;
	return url;
};