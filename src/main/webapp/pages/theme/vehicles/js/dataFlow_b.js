var bool=false;
function insertRow(row){
	$.each($("#tbody_flow_b tr"),function(i,obj){
		if($(this).html()==$(row).parent().parent().parent().html()){
			bool=true;
			$(this).after("<tr data-id=''>"+
							"<td>"+(i+2)+"</td>"+
							"<td><input type='text' value='' style='border:0px;background-color:transparent' class='Enable' required='required' onclick='focusInput(this)'></td>"+
							"<td><input type='text' value='' style='border:0px;background-color:transparent' class='Enable' required='required' onclick='focusInput(this)'></td>"+
							"<td><input type='number' value='' style='border:0px;background-color:transparent' class='Enable' required='required' onclick='focusInput(this)'></td>"+
							"<td da_id=''><input type='text' value='' style='border:0px;background-color:transparent' class='Enable' required='required' onclick='focusInput(this)'></td>"+
							"<td><input type='text' value='' style='border:0px;background-color:transparent' class='Enable' required='required' onclick='focusInput(this)'></td>"+
							"<td>"+
								 "<a href='#'><span class='glyphicon glyphicon-plus' onclick='insertRow(this);'></span></a>"+
								 "<a href='#' style='margin:0px 6px'><span class='glyphicon glyphicon-trash' onclick='delRow(this);'></span></a>"+
								 "<a href='#' style='margin:0px 6px 0px 6px'><span class='glyphicon glyphicon-menu-up' onclick='preRow(this);'></span></a>"+
								 "<a href='#'><span class='glyphicon glyphicon-menu-down' onclick='nextRow(this);'></span></a>"+
							"</td>"+
						"</tr>");
			return true;
		}
		if(bool){
			$(this).children("td:eq(0)").html(parseInt($(this).children("td:eq(0)").html())+1);
		}
	});
	bool=false;
}
function delRow(row){
	var j=0;
	$.each($("#tbody_flow_b tr"),function(i,obj){
		j++;
		if($(this).html()==$(row).parent().parent().parent().html()){
			bool=true;
			return true;
		}
		if(bool){
			$(this).children("td:eq(0)").html(parseInt($(this).children("td:eq(0)").html())-1);
		}
	});
	if(j<2){
		alert("不能删除剩余行！");
		return;
	}
	$(row).parent().parent().parent().remove();
	bool=false;
	var detail_id=$(row).parent().parent().parent().attr("data-id");
	if(detail_id!=null||detail_id!=""){
		var jsonData=[{
			"ID":detail_id
		}];
		$.ajax({
			async : false,
			type : "get",
			url : "/system/base?cmd=delRows&tabName=BS_DATA_FLOW_B",
			data:{"jsonData":JSON.stringify(jsonData)},
			dataType : "json",
			success : function(data) {
				alert(data.message);
			}
		});
	}
	
}

function update(temp){
	if(flow_id==""||flow_id==null){
		alert("跳转主数据ID丢失！");
		return;
	}
	var jsonData={
		"ID":flow_id,
		"FLOW_CODE":$("#flow_code").val(),
		"FLOW_NAME":$("#flow_name").val(),
		"TASK_TYPE":$("#task_type").val(),
		"TASK_CODE":$("#task_code").val(),
		"SY_REGEIST_CODE":$("#sy_regeist_code").val(),
		"SY_REGEIST_NAME":$("#sy_regeist_name").val(),
		"DR":0
	};
	var childJsonData=[];
	$("#tbody_flow_b tr").each(function(){
		if($(this).attr("data-id")==null||$(this).attr("data-id")==""){
			var childJsonDataObject={
				"ID":"add",
				"FLOW_CODE":$(this).children("td:eq(1)").find("input").val(),
				"FLOW_NAME":$(this).children("td:eq(2)").find("input").val(),
				"FLOW_TYPE":$(this).children("td:eq(3)").find("input").val(),
				"FLOW_SORT":$(this).children("td:eq(0)").html(),
				"DA_ID":$(this).children("td:eq(4)").attr("da_id"),
				"DA_CODE":$(this).children("td:eq(4)").find("input").val(),
				"DA_NAME":$(this).children("td:eq(5)").find("input").val(),
				"DR":"0"
			};
			childJsonData.push(JSON.stringify(childJsonDataObject));
		}else{
			var childJsonDataObject={
				"ID":$(this).attr("data-id"),
				"FLOW_CODE":$(this).children("td:eq(1)").find("input").val(),
				"FLOW_NAME":$(this).children("td:eq(2)").find("input").val(),
				"FLOW_TYPE":$(this).children("td:eq(3)").find("input").val(),
				"FLOW_SORT":$(this).children("td:eq(0)").html(),
				"DA_ID":$(this).children("td:eq(4)").attr("da_id"),
				"DA_CODE":$(this).children("td:eq(4)").find("input").val(),
				"DA_NAME":$(this).children("td:eq(5)").find("input").val(),
				"DR":"0"
			};
			childJsonData.push(JSON.stringify(childJsonDataObject));
		}
	});
	var src="/system/buttonBase?cmd=button&buttonToken=updateTestDemo_operation&dataSourceCode=BS_DATA_FLOW&childDataSourceCode=BS_DATA_FLOW_B&parentFile=P_ID";
	$.ajax({
		async : false,
		type : "post",
		url : src,
		traditional: true,
		data:{"jsonData":JSON.stringify(jsonData),"childJsonData":childJsonData},
		dataType : "json",
		success : function(data) {
			alert(data.message);
		}
	});
}

function preRow(row){
	if(typeof($(row).parent().parent().parent().prev().html())== "undefined"){
		alert("该行已为最上层！");
		return false;
	}
	var current=$(row).parent().parent().parent();
	var prev=$(row).parent().parent().parent().prev();
	$(row).parent().parent().parent().prev().find("td:eq(0)").html(parseInt($(row).parent().parent().parent().prev().find("td:eq(0)").html())+1);
	$(row).parent().parent().parent().find("td:eq(0)").html(parseInt($(row).parent().parent().parent().find("td:eq(0)").html())-1);
	$(prev).before(current);
}
function nextRow(row){
	if(typeof($(row).parent().parent().parent().next().html())== "undefined"){
		alert("该行已为最底层！");
		return false;
	}
	var current=$(row).parent().parent().parent();
	var next=$(row).parent().parent().parent().next();
	$(row).parent().parent().parent().next().find("td:eq(0)").html(parseInt($(row).parent().parent().parent().next().find("td:eq(0)").html())-1);
	$(row).parent().parent().parent().find("td:eq(0)").html(parseInt($(row).parent().parent().parent().find("td:eq(0)").html())+1);
	$(next).after(current);
}
function enable(){
	$(".Enable").attr("readonly",false);
}
function initTab($e, code) {
		$e.bootstrapTable({
			url : "/system/reference?token=&cmd=init&dataSourceCode=" + code+"&isRadio=0", //请求后台的URL（*）
			method : 'get', //请求方式（*）
			toolbar : '#toolbar', //工具按钮用哪个容器
			striped : true, //是否显示行间隔色
			cache : false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination : true, //是否显示分页（*）
			sortable : false, //是否启用排序
			sortOrder : "asc", //排序方式
			//queryParams: oTableInit.queryParams,//传递参数（*）
			sidePagination : "server", //分页方式：client客户端分页，server服务端分页（*）
			pageNumber : 1, //初始化加载第一页，默认第一页
			pageSize : 10, //每页的记录行数（*）
			pageList : [ 10, 25, 50, 100 ], //可供选择的每页的行数（*）
			search : false, //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
			strictSearch : false,
			showColumns : false, //是否显示所有的列
			showRefresh : false, //是否显示刷新按钮
			minimumCountColumns : 2, //最少允许的列数
			clickToSelect : true, //是否启用点击选中行
			//height:526 ,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			uniqueId : "ID", //每一行的唯一标识，一般为主键列
			showToggle : false, //是否显示详细视图和列表视图的切换按钮
			cardView : false, //是否显示详细视图
			detailView : false, //是否显示父子表
			columns : data_str
		});
	}
	function initCol(code) {
		$.ajax({
			async : false,
			type : "post",
			url : "/system/reference?token=&cmd=queryColumns&dataSourceCode=" + code+"&isRadio=0",
			dataType : "text",
			success : function(data) {
				data_str.push(JSON.parse(data));
				for (var i = 0; i < data_str.length; i++) {
					data_str[i][i].formatter = "";
				}
			}
		});
	} 
function focusInput(row){
	current_row=$(row).parent().parent();
	$("#ReferenceModal_detail").modal("show");
	initCol("REF_BS_FTP_CONFIG")
	initTab($("#flow_b_table"),"REF_BS_FTP_CONFIG");
}
