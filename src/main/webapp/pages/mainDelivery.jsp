<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<title>子表模板</title>
<style>
.btn-group{
	padding:0 !important;
}
</style>
</head>
<body>
<div class="form-horizontal" style="padding:5px;">
	<div class="panel panel-primary" style="padding:10px 0;">
	
		<div class="col-md-12 col-xs-12 col-sm-12" style="margin-top:10px;">
			
				<div class="form-group has-feedback">
					<div class="col-md-2 col-xs-2 col-sm-2" style="text-align:left;">主题：</div>
					<div class="col-md-9 col-xs-9 col-sm-9">
						<input type="TEXT" class="form-control" id="zt">
					</div>
				</div>				
			
		</div>
		
		<div class="col-md-12 col-xs-12 col-sm-12" style="margin-top:10px;">
			
				<div class="form-group has-feedback">

					<div class="col-md-2 col-xs-2 col-sm-2" style="text-align:left;">主送：</div>
					<div class="col-md-9 col-xs-9 col-sm-9">
						<select class="form-control selectpicker input-xlarge dropdown"  style="outline: none;"
		                  data-live-search="true"  id="md"  multiple data-dropup-auto="false">
						</select>
					</div>
				</div>
					
		</div>
		
		<div class="col-md-12 col-xs-12 col-sm-12" style="margin-top:10px;">
			
				<div class="form-group has-feedback">

					<div class="col-md-2 col-xs-2 col-sm-2" style="text-align:left;">抄送：</div>
					<div class="col-md-9 col-xs-9 col-sm-9">
						<select class="form-control selectpicker input-xlarge dropdown"  style="outline: none;"
		                  data-live-search="true"  id="cd"  multiple data-dropup-auto="false">
							
						</select>
					</div>
				</div>
				
		</div>
		
		
<div class="col-md-12 col-xs-12 col-sm-12" style="margin-top:10px;">
	<div class="form-group has-feedback">
		<div class="col-md-6 col-xs-6 col-sm-6" style="text-align: left;" >
			<label class="control-label" style="line-height: 29px; text-align:left;" data-toggle="tooltip" title="" for="ATTACHMENT" data-original-title="附件">附件:</label>
		</div>
		<div class="col-md-6 col-xs-6 col-sm-6" id="new_file">
			<form action="#" enctype="multipart/form-data">
				<div style="float: left;line-height: 28px;">
					<input type="file" name="files" id="anchor_ATTACHMENT" onChange="showInfo(this);">
				</div>
				<div>
					<input type="hidden" id="ENCLOSURE" name="ENCLOSURE" value="" class="fileHidden" data-bv-field="ENCLOSURE">
					<i class="form-control-feedback" data-bv-icon-for="ENCLOSURE" style="display: none;"></i>
					<button id="sub" type="button" class="btn btn-primary" onclick='uploadAffix("ENCLOSURE");'>
					<span class="glyphicon glyphicon-cloud-upload"></span>&nbsp;&nbsp;提交
					</button>
				</div>
			</form>
				 	 	
		</div>
		<div style="border-bottom: 1px dashed #c8c6c6; margin-top:15px;margin-bottom:10px;" class="col-md-12 col-xs-12 col-sm-12" id="showfiles_ATTACHMENT"></div>
	</div>
</div>

</br>
	<div class="text-center" style="clear:both;">
		<button type="button" class="btn btn-info"  onclick="save();" id="send">
			<span aria-hidden="true">确认并发送</span>
		</button>

	</div>
	<div class="text-center" style="clear:both;"></div>
</div>
</div>
</body>
<jsp:include page="../include/public.jsp"></jsp:include>
<script type="text/javascript">
var bus_id='<%=request.getParameter("bus_id")%>';
var username='<%=request.getParameter("username")%>';
var token ='<%=request.getParameter("token")%>';
//判断抄送是新增还是修改
var mes_st=<%=request.getParameter("mes_st")%>;
var data_code='<%=request.getParameter("cache_dataSourceCode")%>';
var _isBusPage='<%=request.getParameter("_isBusPage")%>';
var theme='';
var main_name='';
var copy_name='';
var enclosure='';
var batch_no='';
var data_code='<%=request.getParameter("cache_dataSourceCode")%>';
$(function(){
	//debugger;
	//$("#send").attr("disabled",true);
	if(1==_isBusPage){
		$("#cd").parent().parent().parent().remove();
		$("#md").parent().parent().parent().remove();
		$("#zt").parent().parent().parent().remove();
	}
	
	$.ajax({
		type : "POST",
		url :  "/system/mainDelivery?cmd=selectUser",
		dataType : "json",
		async : false,
		cache : false,
		contentType : false,
		processData : false,
		success : function(data) {
			if(data.status=="success"){
				for(var i=0;i<data.body.rowdata.length;i++){
					var htmlstr='<option value="'+data.body.rowdata[i].ID+'">'+data.body.rowdata[i].NAME+'</option>';
					$("#md").append(htmlstr);
				}
			}else{
				alert( "查询失败");
			}
		},
		error : function(data) {
			alert( "查询失败");
		}
		
	});
	
	$.ajax({
		type : "POST",
		url :  "/system/mainDelivery?cmd=selectDept",
		dataType : "json",
		async : false,
		cache : false,
		contentType : false,
		processData : false,
		success : function(data) {
			if(data.status=="success"){
				for(var i=0;i<data.body.rowdata.length;i++){
					var htmlstr='<option value="'+data.body.rowdata[i].ID+'">'+data.body.rowdata[i].NAME+'</option>';
					$("#cd").append(htmlstr);
				}
			}else{
				alert( "查询失败");
			}
		},
		error : function(data) {
			alert( "查询失败");
		}
		
	});

	//debugger;
	if(true){
		$.ajax({
			type : "POST",
			url :  "/system/mainDelivery?cmd=selectMcd&bus_id="+bus_id+"&data_code="+data_code,
			dataType : "json",
			async : false,
			cache : false,
			contentType : false,
			processData : false,
			success : function(data) {
				for(var i=0;i<data.body.rowdata.length;i++){
					theme=data.body.rowdata[i].THEME;
					main_name=data.body.rowdata[i].MAIN_NAME; 
					copy_name=data.body.rowdata[i].COPY_NAME; 
					enclosure=data.body.rowdata[i].ENCLOSURE;
					batch_no=data.body.rowdata[i].ENCLOSURE_CODE;
				}
				if(data.body.rowdata.length>0){
					$("#new_file").hide();
					$("#send").parent().remove();
					$("#cd").attr("disabled","disabled");
					$("#md").attr("disabled","disabled");
					$("#zt").attr("disabled","disabled");
					showFile(batch_no);
				}
			},
			error : function(data) {
				alert( "查询失败");
			}
			
		});
		$("#zt").val(theme);
		if(main_name!=''){
			main_name=main_name.substring(1,main_name.length-1).split(",");
		}
		if(copy_name!=''){
			copy_name=copy_name.substring(1,copy_name.length-1).split(",");
		}
		
		var select = document.getElementById("md");  
	
	    for (var i = 0; i < select.options.length; i++){
	    	for(var j=0;j<main_name.length;j++){	    	
		        if (select.options[i].text == main_name[j]){  
		            select.options[i].selected = true;  
		            break;  
		        }  
	        }
	    }
	    
	    var select1 = document.getElementById("cd");  
	
	    for (var i = 0; i < select1.options.length; i++){
	    	for(var j=0;j<copy_name.length;j++){	    	
		        if (select1.options[i].text == copy_name[j]){  
		            select1.options[i].selected = true;  
		            break;  
		        }  
	        }
	    } 
	    
		
	}
	
		
});

var fileId="";
var filenames="";
var batchNo='';
//不使用组件上传附件
function uploadAffix(tid){
	//debugger;
    var code="BUS_MAIN_DELIVERY"; 
    var token="<%=request.getParameter("token")%>";
	console.log(tid);
	var $a = $("#"+tid);
	var bs_code=$("#bs_code").val();
	var url="/document/base?cmd=uploadAffixtain&did="+tid+"&batchNo="+batchNo+"&bscode="+code+"&token="+token;
	
	
	$.ajax({
		type: "POST",
		url:url,
		data: new FormData($a.parent().parent()[0]),
		dataType: "json",
		async: false,  
		cache: false,  
		contentType: false,  
		processData: false,
		success: function (data) {
		    $(function(){
			    alert("上传成功");
			    fileId = data["fileid"];
			    if(batchNo==''){
			    	batchNo=data["batchno"];
			    }
			    
			    addFile(fileId);
		    });
		   
		},
		error: function(data) {
			alert("上传附件失败，请联系管理员"+data);
		}
	});
	clearFile();
}

function getFileName(obj){ 
    var pos = obj.value.lastIndexOf("\\")*1;
    pos=obj.value.substring(pos+1);
    return pos; 
}
function showInfo(obj){
    var filename =getFileName(obj);
    filenames=filename;
}

function addFile(fileId){
	$.ajax({
		type : "POST",
		url :  "/system/mainDelivery?cmd=selectDoc&fileId='"+fileId+"'",
		dataType : "json",
		async : false,
		cache : false,
		contentType : false,
		processData : false,
		success : function(data) {
			if(data.status=="success"){
				if(data.body.rowdata.length>0){
					appendFile(data.body.rowdata[0].ID,data.body.rowdata[0].FILE_NAME,"add");
				}
			}else{
				alert( "新增附件失败");
			}
		},
		error : function(data) {
			alert( "新增附件失败");
		}
		
	});
}

function showFile(batch_no){
	$.ajax({
		type : "POST",
		url :  "/system/mainDelivery?cmd=selectDoc&batch_no='"+batch_no+"'",
		dataType : "json",
		async : false,
		cache : false,
		contentType : false,
		processData : false,
		success : function(data) {
			if(data.status=="success"){
				for(var i=0;i<data.body.rowdata.length;i++){
					appendFile(data.body.rowdata[i].ID,data.body.rowdata[i].FILE_NAME,"show");
				}
			}else{
				alert( "显示附件失败");
			}
		},
		error : function(data) {
			alert( "显示附件失败");
		}
		
	});
}

function appendFile(fId,fName,st){
	var del="";
	if(st=='add'){
		del="<a href='javascript:deleteAffix(\""+ fId+ "\")' class=\"btnlist\"  data-toggle=\"tooltip\" data-placement=\"top\" title=\"删除\"><span class=\"glyphicon glyphicon-trash\"></span></a>";
	}
	$("#showfiles_ATTACHMENT").append("<div class=\"col-md-4 listmessage\" id='"+ fId+ "'>" +
		"<div class=\"col-md-9 listhide\" data-toggle=\"tooltip\" data-placement=\"top\" title=\""+"文件名"+"\">"+fName+"</div>" +
		"<div class=\"col-md-3 listbtn\">" +
		"<a href='/document/base?cmd=downloadAffix&fid="+ fId+ "' class=\"btnlist\"  data-toggle=\"tooltip\" data-placement=\"top\" title=\"下载\"><span class=\"glyphicon glyphicon-download-alt\"></span></a>" +
			del+
		"</div>"+
		"</div>")
}

function save(){
	var json={};
	json.THEME=$("#zt").val();
	json.MAIN_ID='('+$("#md").val()+')';
	var Main_Texts = [];
	$("#md :selected").each(function() {
    	Main_Texts.push($(this).text());                
    });
	json.MAIN_NAME='('+Main_Texts+')'; 
	json.COPY_ID='('+$("#cd").val()+')';
	var Copy_Texts = [];
	$("#cd :selected").each(function() {
    	Copy_Texts.push($(this).text());                
    });
	json.COPY_NAME='('+Copy_Texts+')'; 
	json.ENCLOSURE_CODE=batchNo;
	json.BUS_ID=bus_id;
	json.DATA_CODE=data_code;
	json.SEND_ID='<%=request.getParameter("userId")%>';
	json.SEND_PERSON=username;
	
	//默认走新增
	if(false){
		$.ajax({
			type : "POST",
			url :  "/system/mainDelivery?cmd=updMain",
			data : JSON.stringify(json),
			dataType : "json",
			async : false,
			cache : false,
			contentType : false,
			processData : false,
			success : function(data) {
				if(data.status=="success"){
					alert("修改成功");
					$("#stat").attr("disabled",true);
					$("#send").attr("disabled",false);
				}else{
					alert("修改失败");
				}
			},
			error : function(data) {
				alert("修改失败");
			}
		
		});
	}else{
		$.ajax({
			type : "POST",
			url :  "/system/mainDelivery?cmd=insertMain&token="+token,
			data : JSON.stringify(json),
			dataType : "json",
			async : false,
			cache : false,
			contentType : false,
			processData : false,
			success : function(data) {
				if(data.status=="success"){
					alert("通知成功");
					location.reload();
				}else{
					alert("保存失败");
				}
			},
			error : function(data) {
				alert("保存失败");
			}
		
		});
	}
}

</script>
</html>