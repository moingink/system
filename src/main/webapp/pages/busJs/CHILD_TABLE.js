var index = 0;

//初始化子表列表
function initDetailTableList(detailDataSourceCode, childFiled, parentId, lableName){
	var detailTableButtonHtml = initDetailTableButtonHtml(detailDataSourceCode);
	$('#insPage').append(detailTableButtonHtml);
	var detailTableHtml = '<table id="'+detailDataSourceCode+'_TABLE" data-row-style="rowStyle"></table>';
	$('#insPage').append(detailTableHtml);
	setDetailLable($('#'+detailDataSourceCode+'_TABLE_BUTTON'), lableName);
	initDetailTable($('#'+detailDataSourceCode+'_TABLE'), detailDataSourceCode, childFiled, parentId);
}

//初始化子表
function initDetailTable($detailTable, detailDataSourceCode, childFiled, parentId){
	bulidListPage($detailTable, detailDataSourceCode, pageParamFormat(childFiled+" = '"+parentId+"'"));
	initDetailDiv($detailTable, detailDataSourceCode);
	bulidMaintainPage($('#'+detailDataSourceCode+'_PAGE'), detailDataSourceCode, '');
	setValidJsonDetail(detailDataSourceCode);
}

//初始化子表页面
function initDetailDiv($detailTable, detailDataSourceCode){
	var fromHtml = '<div class="panel-body bv-form" id="'+detailDataSourceCode+'_PAGE" novalidate="novalidate" style="display:none;"></div>';
	$('#insPage').after(fromHtml);
	var buttonHtml = '<div id="'+detailDataSourceCode+'_PAGE_BUTTON" style="display:none;">'+
						'<button type="button" class="btn btn-success" onclick="detailPageSave(\''+detailDataSourceCode+'\')">'+
							'<span class="glyphicon glyphicon" aria-hidden="true"></span>保存'+
						'</button>'+
						'<button type="button" class="btn btn-inverse" onclick="detailPageBack(\''+detailDataSourceCode+'\')">'+
							'<span class="glyphicon glyphicon" aria-hidden="true"></span>返回'+
						'</button>'+
					 '</div>';
	$('#'+detailDataSourceCode+'_PAGE').before(buttonHtml);
}

//设置明细标签
function setDetailLable($detailTable, labelName){
	var lableHtml = '<div class="col-md-12 col-xs-12 col-sm-12 classifiedtitle">'+labelName+'</div>';
	$detailTable.before(lableHtml);
}

//子表新增
function detailTableInsertClick(detailDataSourceCode){
	clearForm(detailDataSourceCode);
	detailTableShow(detailDataSourceCode);
	mainTableHide();
	setUpDetailPage(detailDataSourceCode);
}

//子表修改
function detailTableUpdateClick(detailDataSourceCode){
	var $detailPage = $('#'+detailDataSourceCode+'_PAGE');
	var $detailTable = $('#'+detailDataSourceCode+'_TABLE');
	var selected = JSON.parse(JSON.stringify($detailTable.bootstrapTable('getSelections')));
	if (selected.length != 1) {
		oTable.showModal('提示', "请选择一条数据进行操作！");
		return;
	}
	detailTableShow(detailDataSourceCode);
	mainTableHide();
	setUpDetailPage(detailDataSourceCode);
	$detailPage.find("[id]").each(function() {
		$(this).val(selected[0][$(this).attr('id')]);
	});
}

//子表删除
function detailTableDeleteClick(detailDataSourceCode){
	var $detailPage = $('#'+detailDataSourceCode+'_PAGE');
	var $detailTable = $('#'+detailDataSourceCode+'_TABLE');
	var selected = JSON.parse(JSON.stringify($detailTable.bootstrapTable('getSelections')));
	if (selected.length == 0) {
		oTable.showModal('提示', "请选择数据进行操作！");
		return;
	}
	if(!validateDelete()){
		return;
	}
	var ids = new Array();
	$.each(selected, function(index,row) {
		ids.push(selected[index]['ID']);
		if(selected[index]['ID'].indexOf('add') == -1 ){
			setDeleteData(detailDataSourceCode, selected[index]['ID']);
		}
	});
	$detailTable.bootstrapTable('remove', {field: 'ID', values: ids});
	var tableData = JSON.parse(JSON.stringify($detailTable.bootstrapTable("getData")));
	var detailTableIndex = 0;
	$.each(tableData, function(j) {
		tableData[j]['RMRN'] = j+1;
		detailTableIndex = j+1;
	});
	$detailTable.bootstrapTable('load', {'total':tableData.length, 'rows':tableData});
	$detailTable.parent().next().next().find('.pagination-info').html("共"+detailTableIndex+"条记录");
}

//删除验证
function validateDelete(){
	if(confirm("确定要删除选中数据吗？删除后请点击保存操作才可生效")){
		return true;
	}else{
		return false;
	}
}

//清空表单数据
function clearForm(detailDataSourceCode){
	$('#'+detailDataSourceCode+'_PAGE').find("[id]").each(function() {
		$(this).val("");
	});
}

//子表div显示
function detailTableShow(detailDataSourceCode){
	$('#'+detailDataSourceCode+'_PAGE').css('display','block');
	$('#'+detailDataSourceCode+'_PAGE_BUTTON').css('display','block');	
}

//主表div隐藏
function mainTableHide(){
	$('#insPage').css('display','none');
	$('#insPage').prevAll().eq(1).css('display','none');
}

//子表页面设置
function setUpDetailPage(detailDataSourceCode){
	var $detailPage = $('#'+detailDataSourceCode+'_PAGE');
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
	$detailPage.find('.form_date').find("input").each(function(){
		var column = $(this).attr("id");
		$(this).change(function() {
			$detailPage.data('bootstrapValidator').updateStatus(column, 'NOT_VALIDATED', null).validateField(column);
		});
	});
	setUpDetailPageAll($detailPage);
	setDataDetailPageAll($detailPage);
}

//子表保存
function detailPageSave(detailDataSourceCode){
	var $detailPage = $('#'+detailDataSourceCode+'_PAGE');
	var $detailTable = $('#'+detailDataSourceCode+'_TABLE');
	$detailPage.data("bootstrapValidator").validate();
    if(!$detailPage.data('bootstrapValidator').isValid()){ 
    	setParntHeigth($("body").height());
        return; 
    }
    if(!saveVerification(detailDataSourceCode)){
    	return;	
    }
    divDisplay(detailDataSourceCode);
    bulidValid($detailPage, isValidJsonDetail(detailDataSourceCode));
    var detailTableIndex = 0;
	var json = JSON.parse(getJson($detailPage));
	if(json['ID'] == ""){	
		var tableData = $detailTable.bootstrapTable("getData");
		if(tableData != null && tableData.length >= 0){
			detailTableIndex = tableData.length + 1;
		}
		json["RMRN"] = detailTableIndex;
		json["ID"] = "add"+index++;
		json = jsonHandle(json);
		$detailTable.bootstrapTable('append', json);
		$detailTable.parent().next().next().find('.pagination-info').html("共"+detailTableIndex+"条记录");
	}else{
		json = jsonHandle(json);
		$detailTable.bootstrapTable('updateByUniqueId', {id: json['ID'], row: json}); 
	}
}

//子表返回
function detailPageBack(detailDataSourceCode){
	bulidValid($('#'+detailDataSourceCode+'_PAGE'), isValidJsonDetail(detailDataSourceCode));
	divDisplay(detailDataSourceCode);
}

//隐藏明细信息，显示主表信息
function divDisplay(detailDataSourceCode){
	$('#insPage').css('display','block');
	$('#insPage').prevAll().eq(1).css('display','block');
	$('#'+detailDataSourceCode+'_PAGE').css('display','none');
	$('#'+detailDataSourceCode+'_PAGE_BUTTON').css('display','none');
}
