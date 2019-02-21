

var auditButJson =[{name:'提交',fun:"pubRestAudit(this)",buttonToken:'audit'},
                   /*{name:'撤销',fun:"pubRestRegix(this,'')",buttonToken:'audit'},*/
                   {name:'审批进程',fun:'pubVisitHis(this)',buttonToken:'audit'}];

function findAuditColumn(){
	return 'BILL_STATUS';
}

function pubRestAudit(t){
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('提示', "请选择一条数据进行操作");
		return;
	}
	var tenant_code =findTenantCode(selected[0]);
	if(tenant_code==undefined){
		tenant_code="";
	}
	pubRestAuditByTenant(t,tenant_code);
}

function pubRestAuditByTenant(t,tenant_code){
	var selected = JSON.parse(getSelections());
	var type =1;
	if(selected.length != 1){
		oTable.showModal('提示', "请选择一条数据进行操作");
		return;
	}
	var audit_column =findAuditColumn();
	if(audit_column.length==0){
		oTable.showModal('提示', "没有设置审批字段");
	}
	var billStatus = selected[0][audit_column];
	if(billStatus==undefined||billStatus==''||billStatus.length==0){
		oTable.showModal('提示', "审批状态为空，不能提交！");
		return;
	}
	
	if(billStatus != 0 && billStatus != 7){
		oTable.showModal('提示', "只能提交  已保存、已退回单据");
		return;
	}
	var apprType="&audit_type="+type+"&audit_column="+audit_column+"&tenant_code="+tenant_code;
	var message = transToServer(findBusUrlByPublicParam(t,apprType,dataSourceCode),JSON.stringify(selected[0]));
	oTable.showModal('提示', message);
	queryTable(t);
}

function findTenantCode(selected){
	return "";
}


function pubVisitHis(t){
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('提示', "请选择一条数据进行操作");
		return;
	}
	var id =selected[0]["ID"];
	var NEED_PROCESS_CODE=appCode+"@"+dataSourceCode+"@";
	var visit_url=workflow.replace("#BUS_ID#",id).replace("#ts#",new Date().getTime()).replace("#NEED_PROCESS_CODE#",NEED_PROCESS_CODE);
	show(visit_url);
	
}

function show(viewUrl){
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



function validateDelByAudit(selected){
	
	var audit_column =findAuditColumn();
	if(audit_column.length==0){
		oTable.showModal('提示', "没有设置审批字段");
		return false;
	}
	var billStatus = selected[0][audit_column];
	
	if(billStatus != 0 && billStatus != 7){
		oTable.showModal('提示', "只能删除 单据状态：已保存、已退回的单据！");
		return false;
	}else{
		if(confirm("删除是不可恢复的，您确认要删除吗？")){
			return true;
		}else{
			return false;
		}
	}
	return true;
	
	
}

function validateUpdateByAudit(selected){
	var audit_column =findAuditColumn();
	if(audit_column.length==0){
		oTable.showModal('提示', "没有设置审批字段");
		return false;
	}
	var billStatus = selected[0][audit_column];
	
	if(billStatus != 0 && billStatus != 7){
		oTable.showModal('提示', "只能修改单据状态：已保存、已退回的单据！");
		return false;
	}
	return true;
}



