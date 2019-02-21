
function show_dbclick(selected,title){
	$('#bus_message_dbclick').html('');
	$('#myModalLabel_dbclick').html(title);
	
	var mainPageParam =pageParamFormat(ParentId +" ="+selected["ID"]);
    mainPageParam=mainPageParam+"&showType=INSUP";
	bulidMaintainPage($("#bus_message_dbclick"),dataSourceCode,mainPageParam);
	getAuditProcess(selected);
	
	$("#bus_message_dbclick").find("[id]").each(function(){
		$(this).attr("disabled",true);
	});
	$("#bus_message_dbclick span").each(function(){
		$(this).attr("onclick","");
	});
	$('#ViewModal_dbclick').modal('show');
}

//获取审批进程
function getAuditProcess(selected){
	var id = selected['ID'];
	var auditRecord = querySingleRecord("&dataSourceCode=WF_NEED_HANDLE_COMMIT&SEARCH-BUS_ID="+id);
	if(!jQuery.isEmptyObject(auditRecord)){
		$('#ViewModal_dbclick').find('.modal-body').find('#WF_NEED_HANDLE_DETAIL').remove();
		var NEED_PROCESS_CODE = appCode+"@"+dataSourceCode+"@";
		var visit_url = workflow.replace("#BUS_ID#",id).replace("#ts#",new Date().getTime()).replace("#NEED_PROCESS_CODE#",NEED_PROCESS_CODE);
		$('#ViewModal_dbclick').find('.modal-body').append('<iframe id="WF_NEED_HANDLE_DETAIL" width="100%" height="500" src="'+visit_url+'" frameborder="0" style="margin-top:15px;"></iframe>');
	}
}

function hide_dbclick(t){
	$('#ViewModal_dbclick').modal('hide');
	queryTable(t);
}

function dblClickFunction(row,tr){
	//pageName
	show_dbclick(JSON.parse(JSON.stringify(row)),"业务详情");
}

$(function(){
	$(document.body).append(
		'<!--查看数据源展示效果专用模态框-->'+
		'<div class="modal fade" id="ViewModal_dbclick" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">'+
			'<div class="modal-dialog" style="width: 90%;">'+
				'<div class="modal-content">'+
					'<div class="modal-header">'+
						'<button type="button" class="close" data-dismiss="modal" aria-hidden="true">'+
							'×'+
						'</button>'+
						'<h4 class="modal-title" id="myModalLabel_dbclick">审批页面</h4>'+
					'</div>'+
					'<div class="modal-body" style="overflow:hidden;">'+
						'<div id="bus_message_dbclick"></div>'+
					'</div>'+
					'<div class="modal-footer">'+
						'<button type="button" class="btn btn-inverse" data-dismiss="modal">'+
							'关闭'+
						'</button>'+
					'</div>'+
				'</div>'+
			'</div>'+
		'</div>'
	);
	$('#ViewModal_dbclick').on('hide.bs.modal', function() {
		$(this).removeData('modal');
	});
});
