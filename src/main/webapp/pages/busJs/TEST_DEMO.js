buttonJson =[
    {name: '查询', fun: 'queryTable(this)', buttonToken: 'query'},
    {name: '新增', fun: 'setUpAdd(this),tog(this)', buttonToken: 'add'},
    {name: '修改', fun: 'updateRow(this)', buttonToken: 'update'},
    {name: '删除', fun: 'deleteRowCheck(this)', buttonToken: 'delete'},
    {name: '导入', fun: 'upload(this)', buttonToken: 'upload'},
    {name: '洽谈业务', fun: 'jump(this)', buttonToken: 'third'}
];
//导入初始化 必须 否则页面功能有问题
$(function(){
	var fileInput=new FileInput();
	fileInput.init();
});
//重写参照
function ref_write_json(rejsonArray){
    //参照 选择json 做单独的处理
	console.log(JSON.stringify(rejsonArray));
	return false; //return true 继续重写页面字段  false 不做页面回写
}

// 点击删除按钮做判断其他函数

function deleteRowCheck(){
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('modal', "请选择一条数据进行操作");
		return;
	}
	var billStatus = selected[0]['BILL_STATUS'];
	if(billStatus!=0 && billStatus!=7){
		oTable.showModal('modal', "不允许删除已提交或审批完单据");
	}else{
		delRows(t);
	}

}

//双击事件  列表模版双击事件跳转其他操作
function dblClickFunction(row,tr){
	var json =JSON.parse(JSON.stringify(row));
	console.log(json);
}

//业务跳转

function jump(t){
	window.location.href=context+"/pages/childTableModify_persion.jsp?pageCode=MD_PERSONNEL&token="+token;

}
function ref_end(){
	console.log("参选回调用");
}

//导入
function upload(){
	console.log("导入");
	$('#uploadModal').modal('show');
}
//主字表模板
function setUpAdd(t){
	initDetailTable('001');
}

function initDetailTable(parentId){
	console.log('主子表');
	initDetailTableList();
	bulidListPage($("#detailTable"),'MD_PERSONNEL',pageParamFormat("PARENT_ID = '"+parentId+"'"));
	initDetailDiv();
	bulidMaintainPage($('#detailDiv'),"MD_PERSONNEL",'');
	//dateBoxHandle();
	validJsonDetail = transToServer(findUrlParam('base','queryValids','&dataSourceCode=MD_PERSONNEL'),'');
	$('#detailDiv').bootstrapValidator(validJsonDetail);
	$('#OPPORTY_CODE').parents('.col-sm-4').before("<h5 style='color:red; text-align:center;'>暂估收入填报要求：一笔暂估收入由多个业务账期组成时，应按业务账期分多条明细录入。</h5>");
	$('#BUSINESS_ACCOUNT_PERIOD').attr('onchange','businessAccountPeriodChange()');//业务账期
}

//初始化子表列表
function initDetailTableList(){
	var tableHtml = '<div class="col-md-12" style="margin-top:10px; margin-bottom:10px; padding:0;">'+
        '<button type="button" class="btn btn-primary" id="insert" onclick="insertDetail();">新增</button>&nbsp;' +
        '<button type="button" class="btn btn-warning" id="update" onclick="updateDetail();">修改</button>&nbsp;' +
        '<button type="button" class="btn btn-danger" id="delete" onclick="deleteDetail();">删除</button>' +
        '</div>' +
        '<table id="detailTable" data-row-style="rowStyle"></table>';
	$('#insPage').append(tableHtml);
}

//初始化子表页面
function initDetailDiv(){
	var fromHtml = '<div class="panel-body bv-form" id="detailDiv" novalidate="novalidate" style="display:none;"></div>';
	$('#insPage').after(fromHtml);
	var buttonHtml = '<div id="detailButton" style="display:none;">'+
        '<button type="button" class="btn btn-success" onclick="detailSave(this)">' +
        '<span class="glyphicon glyphicon" aria-hidden="true"></span>保存' +
        '</button>&nbsp;' +
        '<button type="button" class="btn btn-inverse" onclick="detailBack(this)">' +
        '<span class="glyphicon glyphicon" aria-hidden="true"></span>返回' +
        '</button>' +
        '</div>';
	$('#detailDiv').before(buttonHtml);
}
//删除验证
function validateDelete(){
	if(confirm("确定要删除选中数据吗？")){
		return true;
	}else{
		return false;
	}
}

//点击子表新增
function insertDetail(){
	if($('#insPage #OPPORTY_CODE').val() == ""){
		oTable.showModal('提示', "商机编号不能为空！");
		return;
	}
	clearForm();
	detailTableShow();
	mainTableHide();
	setUpDetailTable();
}

//子表修改
function updateDetail(){
	var selected = JSON.parse(JSON.stringify($('#detailTable').bootstrapTable('getSelections')));
	if (selected.length != 1) {
		oTable.showModal('提示', "请选择一条数据进行操作！");
		return;
	}
	detailTableShow();
	mainTableHide();
	setUpDetailTable();
	$('#detailDiv').find("[id]").each(function() {
		$(this).val(selected[0][$(this).attr('id')]);
	});
	$('#detailDiv #AUDIT_TAX').val(selected[0]['AUDIT_TAX_ID']);
	$('#detailDiv #SETTLE_METHOD').val(selected[0]['SETTLE_METHOD_ID']);
	$('#detailDiv #SETTILE_RATIO').val(selected[0]['SETTILE_RATIO_ID']);
}

//子表删除
function deleteDetail(){
	var selected = JSON.parse(JSON.stringify($('#detailTable').bootstrapTable('getSelections')));
	if (selected.length == 0) {
		oTable.showModal('提示', "请选择数据进行操作！");
		return;
	}
	if(!validateDelete()){
		return ;
	}
	var ids = new Array();
	$.each(selected, function(index,row) {
		ids.push(selected[index]['ID']);
		if(selected[index]['ID'].indexOf('add') == -1 ){
			deleteIds += selected[index]['ID'] + ",";
		}
	});
	$('#detailTable').bootstrapTable('remove', {field: 'ID', values: ids});
	var tableData = JSON.parse(JSON.stringify($('#detailTable').bootstrapTable("getData")));
	$.each(tableData, function(j) {
		tableData[j]['RMRN'] = j+1;
		detailTableIndex = j+1;
	});
	$('#detailTable').bootstrapTable('load', {'total':tableData.length,'rows':tableData});
	$('.pagination-info').html("共"+detailTableIndex+"条记录");
}

//子表返回
function detailBack(t){
	bulidValid($('#detailDiv'),validJsonDetail);
	divDisplay();
}

//子表保存
function detailSave(){
	$('#detailDiv').data("bootstrapValidator").validate();
    if (!$('#detailDiv').data('bootstrapValidator').isValid()) {
        setParntHeigth($("body").height());
        return;
    }
    divDisplay();
    bulidValid($('#detailDiv'), validJsonDetail);
	var json = JSON.parse(getJson($('#detailDiv')));
    if (json['ID'] == "") {
		var tableData = $('#detailTable').bootstrapTable("getData");
		if(tableData != null && tableData.length >= 0){
			detailTableIndex = tableData.length + 1;
		}
		json["RMRN"] = detailTableIndex;
		json["ID"] = "add"+i++;
		json = jsonHandle(json);
		$('#detailTable').bootstrapTable('append', json);
		$('.pagination-info').html("共"+detailTableIndex+"条记录");
	}else{
		json = jsonHandle(json);
        $('#detailTable').bootstrapTable('updateByUniqueId', {id: json['ID'], row: json});
	}
}

//清空表单数据
function clearForm(){
	$('#detailDiv').find("[id]").each(function() {
		$(this).val("");
	});
}
//子表div显示
function detailTableShow(){
	$('#detailDiv').css('display','block');
    $('#detailButton').css('display', 'block');
}
//主表div隐藏
function mainTableHide(){
	$('#insPage').css('display','none');
	$('#insPage').prevAll().eq(1).css('display','none');
}

function divDisplay(){
	$('#insPage').css('display','block');
	$('#insPage').prevAll().eq(1).css('display','block');
	$('#detailDiv').css('display','none');
	$('#detailButton').css('display','none');
}

