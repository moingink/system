var bool=false;
function insertRow(row){
	$.each($("#tbody_flow_b tr"),function(i,obj){
		if($(this).html()==$(row).parent().parent().parent().html()){
			bool=true;
			$(this).after("<tr data-id=''>"+
							"<td>"+(i+2)+"</td>"+
							"<td><input type='text' value='' style='border:0px;background-color:transparent' class='Enable' required='required'></td>"+
							"<td><input type='text' value='' style='border:0px;background-color:transparent' class='Enable' required='required'></td>"+
							"<td><input type='number' value='' style='border:0px;background-color:transparent' class='Enable' required='required'></td>"+
							"<td da_id=''><input type='text' value='' style='border:0px;background-color:transparent' class='Enable' required='required'></td>"+
							"<td><input type='text' value='' style='border:0px;background-color:transparent' class='Enable' required='required'></td>"+
							"<td>"+
								 "<a href='#'><span class='glyphicon glyphicon-plus' onclick='insertRow(this);'></span></a>"+
								 "<a href='#' style='margin:0px 6px'><span class='glyphicon glyphicon-trash' onclick='delRow(this);'></span></a>"+
								 "<a href='#' style='margin:0px 0px 0px 6px'><span class='glyphicon glyphicon-menu-up' onclick='preRow(this);'></span></a>"+
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
	$.each($("#tbody_flow_b tr"),function(i,obj){
		if($(this).html()==$(row).parent().parent().parent().html()){
			bool=true;
			return true;
		}
		if(bool){
			$(this).children("td:eq(0)").html(parseInt($(this).children("td:eq(0)").html())-1);
		}
	});
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