buttonJson =[
             {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
             {name:'建表',fun:'tog(this)',buttonToken:'insertWithCache'},
             {name:'表结构设计',fun:'quilkCreate(this)',buttonToken:'quilkCreate'},
             //{name:'元数据字段',fun:'view(this)',buttonToken:'checkMeta'},
             //{name:'存储过程',fun:'tableProc(this)',buttonToken:'procedure'},
             {name:'提交',fun:'creaeTable(this)',buttonToken:'createTable'},
             
             {name:'导入',fun:'upload(this)',buttonToken:'upload'},
             {name:'导出',fun:'upload(this)',buttonToken:'upload'},
            
             {name:'复制',fun:'copy(this)',buttonToken:'copy'},
             {name:'修改',fun:'updateRow(this)',buttonToken:'updateWithCache'},
             {name:'删除',fun:'delRows(this)',buttonToken:'deleteWithCache'}
			];
var current_row="";
var data_str = [];
var data_detail = [];
//导入初始化 必须 否则页面功能有问题
$(function(){
	var fileInput=new FileInput();
	fileInput.init();
});
function quilkCreate(t){
	//
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('modal', "请选择一条数据进行操作");
		return;
	}
	var id=(JSON.parse(getSelections())[0]["ID"]);
	console.log(context);
	$("#ViewModal_dbclick").modal("hide");
	flow_id = "";
	flow_id = id;
	$("#flow_code").attr("data-id", flow_id);
	$("#flow_code").val($(this).find("td:eq(2)").text());
	$("#flow_name").val($(this).find("td:eq(3)").text());
	$("#task_type").val($(this).find("td:eq(4)").text());
	$("#sy_regeist_code").val(
			$(this).find("td:eq(7)").text());
	$("#sy_regeist_name").val(
			$(this).find("td:eq(8)").text());

	$("#bulidPage_detail").slideDown(1100);
	$("#bulidTable").slideUp();
	data_str.splice(0, data_str.length);
	data_detail.splice(0, data_detail.length);
	flow_detail("CD_METADATA_DETAIL", id);
	$("#thead_flow_b").html("");
	$("#thead_flow_b").html(
			"<tr style='background-color:#d9edf7;'>"
					+ "<td>序号</td>" + "<td>字段编码</td>"
					+ "<td>字段名称</td>" + "<td>字段类型</td>"
					+ "<td>字段长度</td>" + "<td>默认值</td>" + "<td>唯一约束</td>" + "<td>主键</td>"
					+ "<td>操作</td>" + "</tr>");
	var str = "";
	if(data_detail[0].rows.length<1){
		return;	
	}
	setTimeout(
			function() {
				for(var i=0;i<data_detail[0].rows.length-1;i++){
					for(var j=0;j<data_detail[0].rows.length-1-i;j++){
						var temp;
						if(data_detail[0].rows[j].FLOW_SORT>data_detail[0].rows[j+1].FLOW_SORT){
							temp=data_detail[0].rows[j];
							data_detail[0].rows[j]=data_detail[0].rows[j+1];
							data_detail[0].rows[j+1]=temp
						}
					}
				}
				for (var i = 0; i <data_detail[0].rows.length; i++) {
					str += "<tr data-id='"+data_detail[0].rows[i].ID+"'>"
							+ "<td>"
							+ (i + 1)
							+ "</td>"
							+ "<td><input type='text' value='"+data_detail[0].rows[i].FIELD_CODE+"' style='border:0px;background-color:transparent' class='Enable'  required='required' ></td>"
							+ "<td><input type='text' value='"+data_detail[0].rows[i].FIELD_NAME+"' style='border:0px;background-color:transparent' class='Enable'  required='required' ></td>"
							+ "<td><select><option value='02'>varchar</option><option>char</option><option>int</option><option>bigint</option></select></td>"
							+ "<td da_id='"+data_detail[0].rows[i].DA_ID+"'><input type='text' value='"+data_detail[0].rows[i].FIELD_LENGTH+"' style='border:0px;background-color:transparent' class='Enable'  required='required' ></td>"
							+ "<td><input type='text' value='"+data_detail[0].rows[i].DEF_VALUE+"' style='border:0px;background-color:transparent' class='Enable'  required='required' ></td>"
							//+ "<td><input type='text' value='"+data_detail[0].rows[i].UNIQUE_CONSTRAINT+"' style='border:0px;background-color:transparent' class='Enable'  required='required' ></td>"
							+ "<td><input type='checkbox' value='"+data_detail[0].rows[i].UNIQUE_CONSTRAINT+"' style='border:0px;background-color:transparent' class='Enable'  required='required' ></td>"
							+ "<td><input type='checkbox' value='"+data_detail[0].rows[i].KEY_FLAG+"' style='border:0px;background-color:transparent' class='Enable'  required='required' ></td>"
							+ "<td>"
							+ "<a href='#'><span class='glyphicon glyphicon-plus' onclick='insertRow(this);'></span></a>"
							+ "<a href='#' style='margin:0px 6px'><span class='glyphicon glyphicon-trash' onclick='delRow(this);'></span></a>"
							+ "<a href='#' style='margin:0px 6px 0px 6px'><span class='glyphicon glyphicon-menu-up' onclick='preRow(this);'></span></a>"
							+ "<a href='#'><span class='glyphicon glyphicon-menu-down' onclick='nextRow(this);'></span></a>"
							+ "</td>" + "</tr>";
				}
				$("#tbody_flow_b").html(str);
				
			}, 1000);
	//window.location.href=context+"/pages/metadata_edit_row.jsp?ParentPKValue="+id+"&token="+token+"&pageCode=CD_METADATA_DETAIL";
}
function flow_detail(code, id) {
	$.ajax({
		async : false,
		type : "get",
		url : "/system/base?cmd=init&dataSourceCode=" + code
				+ "&pageParam=@METADATA_ID=" + id + "@",
		dataType : "json",
		success : function(data) {
			console.log(JSON.stringify(data));
			if(data.rows.length==0){
				$("#tbody_flow_b").html("<tr data-id=''>"+
						"<td>"+1+"</td>"+
						"<td><input type='text' value='' style='border:0px;background-color:transparent' class='Enable' required='required' ></td>"+
						"<td><input type='text' value='' style='border:0px;background-color:transparent' class='Enable' required='required' ></td>"+
						"<td><select><option value='02'>varchar</option><option>char</option><option>int</option><option>bigint</option></select></td>"+
						"<td da_id=''><input type='text' value='' style='border:0px;background-color:transparent' class='Enable' required='required' ></td>"+
						"<td><input type='text' value='' style='border:0px;background-color:transparent' class='Enable' required='required' ></td>"+
						"<td><input type='checkbox' value='' style='border:0px;background-color:transparent' class='Enable' required='required' ></td>"+
						"<td><input type='checkbox' value='' style='border:0px;background-color:transparent' class='Enable' required='required' ></td>"+
						"<td>"+
							 "<a href='#'><span class='glyphicon glyphicon-plus' onclick='insertRow(this);'></span></a>"+
							 "<a href='#' style='margin:0px 6px'><span class='glyphicon glyphicon-trash' onclick='delRow(this);'></span></a>"+
							 "<a href='#' style='margin:0px 6px 0px 6px'><span class='glyphicon glyphicon-menu-up' onclick='preRow(this);'></span></a>"+
							 "<a href='#'><span class='glyphicon glyphicon-menu-down' onclick='nextRow(this);'></span></a>"+
						"</td>"+
					"</tr>");
			}
			data_detail.push(data);
		}
	});
}

function creaeTable(t){
	
	//获取选中行数据 调用后台 创建表和字段信息 
	var id=(JSON.parse(getSelections())[0]["ID"]);
	var table = (JSON.parse(getSelections())[0]["DATA_CODE"]);
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('modal', "请选择一条数据进行操作");
		return;
	}
	$.ajax({
		async: false,
        type: "POST",
        url: context+"/metaData/createTable",
        data: {"id":id,"table":table},
        dataType: "json",
        success: function(data){
        	//返回前提提示是够创建成功
        	console.log(data);
        	oTable.showModal('modal', data); return;
        }
    });
}
function copy(t){
	//辅助元数据功能
	//获取选中行数据 调用后台
	var id=(JSON.parse(getSelections())[0]["ID"]);
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('modal', "请选择一条数据进行操作");
		return;
	}
	$.ajax({
		async: false,
        type: "POST",
        url: context+"/metaData/copy",
        data: {"id":id},
        dataType: "json",
        success: function(data){
        	oTable.showModal('modal', data); return;
        }
    });
}
function view(){
	
	//查看子表相关配置
	var pageCode = 'CD_METADATA_DETAIL';//子表数据源
	var pageName = '元数据字段列表';//列表显示名称
	var ParentPKField = 'METADATA_ID';//主表主键在子表中字段名
	
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('modal', "请选择一条数据进行操作");
		return;
	}
	window.location.href=context+"/pages/childTableModify.jsp?pageCode="+pageCode+"&pageName="+pageName+"&ParentPKField="+ParentPKField+"&ParentPKValue="+JSON.parse(getSelections())[0]["ID"];
}
var isButton =false;
function tableProc(t){
	isButton =true;
	//checkReference('REF(RM_BUTTON,BUTTON_NAME:SEARCH-BUTTON_NAME,0)');
	checkReference(this,'REF(USER_TABLES,TABLE_NAME:SEARCH-TABLE_NAME_NAME,0)','','SELECT');
}

function ref_write_json(rejsonArray){
	if(rejsonArray.length == 0){
		oTable.showModal('modal', "请至少选择一条数据进行操作");
		return;
	}
	if(isButton){
		isButton=false;
		var tableNameValues = '';
		var buttonToken = "procedure";
		var dataSourceCode = "USER_TABLES"
		for(var i = 0;i< rejsonArray.length ; i++){
			tableNameValues += rejsonArray[i]['TABLE_NAME']+',';
		}
		tableNameValues = tableNameValues.substring(0,tableNameValues.length-1);
		var bindUrl = context+'/buttonBase?cmd=button&tableNameValues='+tableNameValues+'&buttonToken='+buttonToken+'&dataSourceCode='+dataSourceCode;
		oTable.showModal('modal', transToServer(bindUrl,JSON.stringify(rejsonArray)));
	}else{
		return true;
	}
}

//导入
function upload(){
	console.log("导入");
	$('#uploadModal').modal('show');
}