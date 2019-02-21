buttonJson =[
              {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
              {name:'新增',fun:'tog(this),setUp()',buttonToken:'add'},
              {name:'修改',fun:'updateRow(this)',buttonToken:'update'},           
              {name:'删除',fun:'delRows(this)',buttonToken:'delete'}
             ];

//设置
function setUp(){
	$("#VISIT_CATEGORY").val(1);//拜访类别
	$("#VISIT_CATEGORY").attr("disabled","true");
	$("#CUS_MAN").val(userName);//客户经理
	$("#CUS_MAN").attr("disabled","true");
	$("#MARKETING_UNIT").val(companyName);//营销单元
	$("#MARKETING_UNIT").attr("disabled","true");
	$("#VISITOR_DUTY").attr("disabled","true");
}

//双击
function dblClickFunction(row,tr){
	var json = JSON.parse(JSON.stringify(row));
	window.location.href = context+"/pages/cusVisitRecordPage.jsp?pageCode=test&pageName=非拜访计划&state=1&keyWord=unplanned&keyWords=ck&ParentPKField=ID&ParentPKValue="+json['ID']+
						   "&planId=&cusId=&cusName=&token="+token;
}

//参选页面增加条件
function ref_query_param(u){
	if(u=='CUMA_DECIDSION_RERATION' || u=='BUS_BUSINESS_MESSAGE'){
		var cusId = $("#CUS_ID").val();
		if(cusId != null && cusId != ""){
			return "&SEARCH-CUS_ID="+cusId;
		}else{
			return "&SEARCH-CUS_ID=121314";
		}
	}else{
		return "";
	}
}

//重写打开参选页面函数
function reference_remote(u, mapping, isRadio) {
	write_html();
	url = context+'/pages/busPage/referencePage.jsp?dataSourceCode=' + u + '&isRadio=' + isRadio + '&t=' + Math.random(1000);
	$('#ReferenceDataSourceCode').val(u);
	$('#ReferenceIsRadio').val(isRadio);
	$('#ReferenceMapping').val(mapping);
	$.get(url, '', function(data) {
		$('#ReferenceModal .modal-body').html(data);
		var modal_height=$('#ReferenceModal .modal-body').height();
		if(modal_height>body_height){
			setParntHeigth(modal_height);
		}
	})
	$('#ReferenceModal').modal({
		show : true,
		backdrop : true
	})
	if(u == "CUMA_DECIDSION_RERATION"){
		setTimeout(function(){
			$("#reference_toolbar").append('<button type="button" class="btn btn-primary" data-dismiss="modal" onclick="goReration()">新增拜访人员</button>')
		},1000);
	}else if(u == "RM_USER"){
		setTimeout(function(){
			$("#SEARCH-ORGANIZATION_ID").parent().parent().parent().parent().css("display","none");
		},1000);
	}
}

//新增拜访人员
function goReration(){
	var cusId = $("#CUS_ID").val();
	if(cusId != null && cusId != ""){
		$("#visitPar_report").attr("src",context+"/pages/childTableModifyByProxy_s3.jsp?pageCode=CUMA_DECIDSION_RERATION&pageName=决策关系&keyWord=1&ParentPKField=CUS_ID&ParentPKValue="+cusId+"&token="+token);
		$("#visitParModal").modal('toggle')
	}else{
		oTable.showModal('提示', '不存在客户');
		return;
	}
}

//重写删除函数
function delRowsByDataSourceCode(t,_dataSourceCode){
	var selected = JSON.parse(getSelections());
	if(selected.length < 1){
		oTable.showModal('modal', "请至少选择一条数据进行删除");
		return;
	}
	var json = new Array();
	for(var i=0;i<selected.length;i++){
		var id = selected[i]["ID"];
		var param = "&dataSourceCode=VIS_RECORD_ANNOTATION&SEARCH-PLANORUNPLANNED=unplanned&SEARCH-RECORD_ID="+id;
		var record = querySingleRecord(param);
		if(jQuery.isEmptyObject(record)){
			json.push(selected[i]);
		}
	}
	if(json.length == 0){
		oTable.showModal('提示', "只能删除  未批注的单据！");
		return;
	}
	var jsonString = JSON.stringify(json);
	if(!validateDel(selected)){
		return ;
	}
	var message = transToServer(findBusUrlByPublicParam(t,'',_dataSourceCode),jsonString);
	oTable.showModal('modal', message);
	queryTableByDataSourceCode(t,_dataSourceCode);
}

$(document.body).append(
	'<div class="modal fade" id="visitParModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">'+
		'<div class="modal-dialog" style="width: 90%;height: 90%">'+
			'<div class="modal-content" style="height:100%">'+
				'<div class="modal-header">'+
					'<button type="button" class="close" data-dismiss="modal" aria-hidden="true">'+
						'×'+
					'</button>'+
					'<h4 class="modal-title" id="myModalLabel">拜访人员</h4>'+
				'</div>'+
				'<div class="modal-body"><iframe id="visitPar_report" name="report" src=""  width="100%" height="400" frameborder="0"></iframe></div>'+
				'<div class="modal-footer">'+
					'<button type="button" class="btn btn-inverse" data-dismiss="modal">'+
						'关闭'+
					'</button>'+
				'</div>'+
			'</div>'+
		'</div>'+
	'</div>'
);