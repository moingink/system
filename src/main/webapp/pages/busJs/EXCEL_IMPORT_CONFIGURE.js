buttonJson =[
	             {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
	             {name:'新增',fun:'tog(this),addSetUp(this)',buttonToken:'add'},
	             {name:'修改',fun:'updateRow(this),updateSetUp(this)',buttonToken:'update'},
	             {name:'删除',fun:'delRows(this)',buttonToken:'delete'}
            ];
            
$(function(){
	//数据源名称、元数据编码、唯一验证条件描述、子表唯一验证条件描述
	$('#DATASOURCE_NAME,#METADATA_CODE,#ONLY_VALIDATE_DESCRIBE,#CHILD_ONLY_VALIDATE_DESCRIBE').attr('readonly','true'); 
	var TipsHtml = "<h4 style = 'color:red; margin-left:20px;'>Tips：</h4>"
					+ "<h5 style = 'color:red;margin-left:30px;'>规则一：如果存在错误数据，所有数据不入库，返回提示信息。</h5>"
					+ "<h5 style = 'color:red;margin-left:30px;'>规则二：如果存在错误数据，错误数据返回提示信息，正确数据入库。</h5>" ;
	$('#insPage').after(TipsHtml);
})

function addSetUp(t){
	fieldsHide();
}

function updateSetUp(t){
	var selected = JSON.parse(getSelections());
	if(selected[0]['INCLUDE_CHILD_TABLE'] == '1'){
		fieldsShow();
	}else{
		fieldsHide();
	}
}

//包含子表
$('#INCLUDE_CHILD_TABLE').change(function() {
	if($('#INCLUDE_CHILD_TABLE').val() == '1'){
		fieldsShow();
	}else{
		fieldsHide();
		fieldsEmpty();
	}
});

function fieldsHide(){
	$('#CHILD_METADATA_CODE,#CHILD_ONLY_VALIDATE,#CHILD_ONLY_VALIDATE_DESCRIBE,#CHILD_BUS_HANDLE_CLASS,#MAPPING_TYPE,#MAIN_MAPPING_FIELD,#CHILD_MAPPING_FIELD').parents('.col-md-4').css("display","none");
}

function fieldsShow(){
	$('#CHILD_METADATA_CODE,#CHILD_ONLY_VALIDATE,#CHILD_ONLY_VALIDATE_DESCRIBE,#CHILD_BUS_HANDLE_CLASS,#MAPPING_TYPE,#MAIN_MAPPING_FIELD,#CHILD_MAPPING_FIELD').parents('.col-md-4').css("display","inline");
}

function fieldsEmpty(){
	$('#CHILD_METADATA_CODE,#CHILD_METADATA_ID,#CHILD_ONLY_VALIDATE,#CHILD_ONLY_VALIDATE_DESCRIBE,#CHILD_BUS_HANDLE_CLASS,#MAPPING_TYPE,#MAIN_MAPPING_FIELD,#CHILD_MAPPING_FIELD').val("");
}

function ref_start(t,col,page_type){
	if(col == "ONLY_VALIDATE"){
		var dataSourceCode = $('#DATASOURCE_CODE').val();
		if(dataSourceCode.length == 0){
			oTable.showModal('提示', "请先选择数据源编码");
			return;
		}
	}
	if(col == "CHILD_ONLY_VALIDATE"){
		var childMetaDataCode = $('#CHILD_METADATA_CODE').val();
		if(childMetaDataCode.length == 0){
			oTable.showModal('提示', "请先选择子表元数据编码");
			return;
		}
	}
	return true;
}

function ref_query_param(dataSourceCode){
	var param = "";
	if(dataSourceCode == "CD_METADATA_DETAIL"){
		param = "&SEARCH-METADATA_ID="+$('#METADATA_ID').val();
	}
	if(dataSourceCode == "CD_METADATA_DETAIL_EXCEL_IMPORT_CONFIGURE"){
		param = "&SEARCH-METADATA_ID="+$('#CHILD_METADATA_ID').val();
	}
	return param;
}

function savaByQuery(t,_dataSourceCode,$div){
	var message = "";
	var buttonToken = $("#ins_or_up_buttontoken").val();
	var json = JSON.parse(getJson($div));
	if(json['INCLUDE_CHILD_TABLE'] == "1"){
		if(json['CHILD_METADATA_CODE'].length == 0){
			oTable.showModal('提示', '请选择子表元数据编码');
			return;
		}
		if(json['MAPPING_TYPE'].length == 0){
			oTable.showModal('提示', '请选择映射类型');
			return;
		}
		if(json['MAIN_MAPPING_FIELD'].length == 0){
			oTable.showModal('提示', '请填写主表映射字段');
			return;
		}
		if(json['CHILD_MAPPING_FIELD'].length == 0){
			oTable.showModal('提示', '请填写子表映射字段');
			return;
		}
	}
	message = transToServer(findBusUrlByButtonTonken(buttonToken,'',_dataSourceCode),JSON.stringify(json));
	oTable.showModal('modal', message);
	back(t);
	queryTableByDataSourceCode(t,_dataSourceCode);
}