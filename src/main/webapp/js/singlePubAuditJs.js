function setAuditButton(){
	if(!$("#tj").length > 0 && !$("#spjc").length > 0){
		var buttonHtml = '<button id="tj" type="button" class="btn btn-success" onclick="singlePubRestAuditByTenant(this);" buttontoken="audit" disabled="true">'+
							 '<span class="glyphicon glyphicon-check" aria-hidden="true"></span>提交'+
						 '</button>&nbsp;'+
						 '<button id="spjc" type="button" class="btn btn-info" onclick="singlePubVisitHis(this);" buttontoken="audit">'+
							 '<span class="glyphicon glyphicon-film" aria-hidden="true"></span>审批进程'+
						 '</button>';
		$("#insPage").prevAll().eq(1).append(buttonHtml);
	}
}

function singleFindAuditColumn(){
	return 'BILL_STATUS';
}

function singlePubRestAuditByTenant(t){
	var selected = JSON.parse("["+getJson($("#insPage"))+"]");
	var tenant_code = findTenantCode(selected[0]);
	if(tenant_code == undefined){
		tenant_code = "";
	}
	var type = 1;
	var audit_column = singleFindAuditColumn();
	if($("#ID").val() == null || $("#ID").val() == ""){
		oTable.showModal('提示', "请保存后，再提交！");
		return;
	}
	if(audit_column.length == 0){
		oTable.showModal('提示', "没有设置审批字段！");
	}
	var billStatus = selected[0][audit_column];
	if(billStatus == undefined || billStatus == '' || billStatus.length == 0){
		oTable.showModal('提示', "审批状态为空，不能提交！");
		return;
	}
	if(billStatus != 0 && billStatus != 7){
		oTable.showModal('提示', "只能提交  已保存、已退回单据！");
		return;
	}
	var apprType="&audit_type="+type+"&audit_column="+audit_column+"&tenant_code="+tenant_code;
	var message = transToServer(findBusUrlByPublicParam(t,apprType,dataSourceCode),JSON.stringify(selected[0]));
	if(message.indexOf('成功') != -1){
		$("#"+singleFindAuditColumn()).val("1");
	}
	oTable.showModal('提示', message);
}

function singleFindTenantCode(selected){
	return "";
}

function singlePubVisitHis(t){
	var selected = JSON.parse("["+getJson($("#insPage"))+"]");
	var id =selected[0]["ID"];
	var NEED_PROCESS_CODE=appCode+"@"+dataSourceCode+"@";
	var visit_url=workflow.replace("#BUS_ID#",id).replace("#ts#",new Date().getTime()).replace("#NEED_PROCESS_CODE#",NEED_PROCESS_CODE);
	singleShow(visit_url);
}

function singleShow(viewUrl){
	$('#ViewModal999_report').attr("src",viewUrl);
	$('#ViewModal999').modal('show');
}

$(function(){
	$(document.body).append(
			'<!--查看数据源展示效果专用模态框-->'+
			'<div class="modal fade" id="ViewModal999" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">'+
				'<div class="modal-dialog" style="width: 90%;">'+
					'<div class="modal-content">'+
						'<div class="modal-header">'+
							'<button type="button" class="close" data-dismiss="modal" aria-hidden="true">'+
								'×'+
							'</button>'+
							'<h4 class="modal-title" id="myModalLabel">审批进程</h4>'+
						'</div>'+
						'<div class="modal-body"><iframe id="ViewModal999_report" name="report" src=""  width="100%" height="518" frameborder="0"></iframe></div>'+
						'<div class="modal-footer">'+
							'<button type="button" class="btn btn-inverse" data-dismiss="modal">'+
								'关闭'+
							'</button>'+
						'</div>'+
					'</div>'+
				'</div>'+
			'</div>'
	);
	$('#ViewModal').on('hide.bs.modal', function() {
		$(this).removeData('modal');
	});
});