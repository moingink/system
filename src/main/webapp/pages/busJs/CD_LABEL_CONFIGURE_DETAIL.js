buttonJson =[                
                {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
				{name:'新增',fun:'tog(this),setData()',buttonToken:'add'},
				{name:'修改',fun:'updateRow(this),setData()',buttonToken:'update'}, 
				{name:'删除',fun:'delRows(this)',buttonToken:'delete'}
			];	

var metadataId = "";//元数据id

//初始化
$(function(){
	setUp();
	labelStyleHtml();
})

//界面设置
function setUp(){
	validJson["fields"]["LABEL_CONTENT_COPY"] = validJson["fields"]["LABEL_CONTENT"];
	$inspage.bootstrapValidator("addField","LABEL_CONTENT_COPY");
	var html = '<div class="input-group">'+
					'<span class="input-group-addon">&lt;div id=&quot;</span>'+
					'<input id="LABEL_ID_COPY" type="text" class="form-control" placeholder="标签id" readonly="true">'+
					'<span class="input-group-addon">&quot; class=&quot;</span>'+
					'<input id="LABEL_CLASS_COPY" type="text" class="form-control" placeholder="请输入标签class" readonly="true" style="min-width:300px">'+
					'<span class="input-group-addon">&quot;&gt;</span>'+
					'<input id="LABEL_CONTENT_COPY" name="LABEL_CONTENT_COPY" type="text" data-bv-field="LABEL_CONTENT_COPY" class="form-control">'+
					'<span class="input-group-addon">&lt;/div&gt;</span>'+
			   '</div>';
	$('#LABEL_STYLE_HTML').css("display","none").parent().append(html);
	$('#LABEL_AFTER_FIELD_CODE').attr("readonly",true);//标签后字段编码  不可编辑
}

//数据设置
function setData(){
	var parentData = querySingleRecord("&dataSourceCode=CD_LABEL_CONFIGURE&SEARCH-ID="+ParentPKValue);
	metadataId = parentData["METADATA_ID"];
	var id = $('#ID').val();
	if(id == null || id == ''){//新增
		$('#LABEL_CLASS').val('col-md-12 col-xs-12 col-sm-12 classifiedtitlenew');//标签class
		$('#LABEL_STYLE').val('默认样式');//标签样式
	}
	$('#LABEL_ID_COPY').val($('#LABEL_ID').val());//展示的  标签id
	$('#LABEL_CLASS_COPY').val($('#LABEL_CLASS').val());//展示的 标签class
	$('#LABEL_CONTENT_COPY').val($('#LABEL_CONTENT').val());//展示的  标签内容
}

//标签样式 监听
$('#LABEL_STYLE').change(function() {
	labelStyleHtml();
});

//标签样式 监听
$('#LABEL_CONTENT').keyup(function() {
	$('#LABEL_CONTENT_COPY').val($(this).val());
});

//标签样式HTML 设置
function labelStyleHtml(){
	var labelStyleVal = $('#LABEL_STYLE').val();
	if(labelStyleVal == '自定义样式'){
		$('#LABEL_ID_COPY,#LABEL_CLASS_COPY').removeAttr('readonly');
	}else if(labelStyleVal == '默认样式'){
		$('#LABEL_ID_COPY,#LABEL_CLASS_COPY').attr("readonly",true);
		$('#LABEL_ID_COPY').val('');
		$('#LABEL_CLASS_COPY').val('col-md-12 col-xs-12 col-sm-12 classifiedtitlenew');
	}
}

//参选页面增加条件
function ref_query_param(u){
	if(u == 'CD_METADATA_DETAIL'){
		if(metadataId != null && metadataId != ""){
			return "&SEARCH-METADATA_ID="+metadataId;
		}else{
			return "&SEARCH-METADATA_ID=1234567890";
		}
	}else{
		return "";
	}
}

//重写保存函数
function savaByQuery(t,_dataSourceCode,$div){
	$('#LABEL_ID').val($('#LABEL_ID_COPY').val());//标签id
	$('#LABEL_CLASS').val($('#LABEL_CLASS_COPY').val());//标签class
	$('#LABEL_CONTENT').val($('#LABEL_CONTENT_COPY').val());//标签内容
	$('#LABEL_STYLE_HTML').val("<div id='"+$('#LABEL_ID').val()+"' class='"+$('#LABEL_CLASS').val()+"'>"+$('#LABEL_CONTENT').val()+"</div>");//标签样式HTML
	
	var buttonToken = $("#ins_or_up_buttontoken").val();
	var message = transToServer(findBusUrlByButtonTonken(buttonToken,'',_dataSourceCode),getJson($div));
	oTable.showModal('modal', message);
	if(message.indexOf('成功') != -1){
		back(t);
		queryTableByDataSourceCode(t,_dataSourceCode);
	}
}
