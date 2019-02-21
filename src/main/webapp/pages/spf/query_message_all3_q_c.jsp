<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<link rel="stylesheet" type="text/css" href="../../easyui/demo/demo.css">
<link rel="stylesheet" type="text/css" href="../../easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="../../easyui/themes/icon.css">
<script type="text/javascript" src="../../easyui/jquery.min.js"></script>
<script type="text/javascript" src="../../easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="../../easyui/locale/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" href="../../vendor/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="../../vendor/bootstrap-table/src/bootstrap-table.css">
<script src="../../vendor/bootstrap/js/bootstrap.min.js"></script>
<script src="../../vendor/bootstrap-table/src/bootstrap-table.js"></script>
<script src="../../js/bootTable.js"></script>
<style type="text/css">
	.tooltip-inner {
	  max-width: 400px;
	  padding: 3px 8px;
	  color: #000;
	  text-align: center;
	  text-decoration: none;
	  background-color: rgb(240,240,242);
	  border-radius: 4px;
	}
</style>

<script type="text/javascript">

$(function(){
    $("[data-toggle='tooltip']").tooltip();
     
    });

 var save_update="";
 
 var filenames="";

 function remove_hidden(){
 
     $("#save2").attr("hidden",true);
	 $("#save1").removeAttr("hidden");
	 $("#message").removeAttr("hidden");
	       $("#j_1").val("");	 
	       $("#j_2").val("");	  
	       $("#j_3").val("");	 
	       $("#j_4").val("");	
	       $("#j_5").val("");	
	       $("#j_6").val("");
	       $("#j_7").val("");
	       $("#j_8").val(""); 
	       
	        fileId="";
	}
	
	function save_car_message(){
	
	    var car_id="";
	   $.ajax({
			type: "POST",
			url: "/system/base?cmd=getIDByDataSourceCode&pageCode=CUMA_DECIDSION_RERATION",
			dataType: "json",
			async: false,
			cache: false,
			contentType: false,
			processData: false,
			success: function(data) {
	         $(function(){
	         car_id=data["id"]; 	         
	         });
		  
			}, 
			error: function(data) {
		      var da=JSON.stringify(data);
		      alert("查询失败，请联系管理员！" + "\t" + "错误代码：" + da);
			}
			});	   
	   	   
	  // var car_id="<%=request.getParameter("car_id") %>";	 
	   var j1=$("#j_1").val();	 
	   var j2=$("#j_2").val();	  
	   var j3=$("#j_3").val();	 
	   var j4=$("#j_4").val();	
	   var j5=$("#j_5").val();
	   var j6=$("#j_6").val();
	   var j7=$("#j_7").val();
	   var j8=$("#j_8").val();
	       j8=filenames;
	   
	   var j9=fileId;    
	   var cus_id="<%=request.getParameter("ParentPKValue")%>";
	   
	    if(j8==""){j9="0";}
	   
	   var car_map=[car_id,j1,j2,j3,j4,j5,j6,j7,j8,cus_id];
	    
	   
	    
	    if(j1=="" || j2=="" || j3=="" || j4=="" || j5=="" || j6=="" || j7=="" ){
	       
	     
	          alert("不能为空");
	          return ;
	    
	  } 
	 
      else{
	   $.ajax({
			type: "POST",
			url:"/system/cuma?cmd=find_car_tain_message&id=<%=request.getParameter("ParentPKValue")%>&is_no=10&page=1&rows=10&car_map="+car_map,		
			dataType: "json",
			async: false,
			cache: false,
			contentType: false,
			processData: false,
			success: function(data) {	
			
			$(function(){			
			alert("保存成功");
		    $("#message").attr("hidden",true);

		    //location.reload(true);   
		     $("#tt_message").datagrid("loadData",data);
		    
		    
		     
			});
			}, error: function(data) {
				var da=JSON.stringify(data);
				//alert("查询失败，请联系管理员！" + "\t" + "错误代码：" + da);
			    alert("保存失败");
			}
			}); 
			
		}
		
	}	
     
 

 function update_messages(){
	  	var row = $('#tt_message').datagrid('getSelections');
	  
	  	
	  	if(row==""){
	  	 alert("请选中一条数据");
	  	   
	  	   return ;
	  	}else{
	  	  if(row.length>1){
	  	   alert("请选中一条数据");
	  	   
	  	   return ;
	  	  }
	  	  else{
	  	  
	  	  if(row[0]["YEAR"]=="请填写"){
	       alert("请先添加数据");
	       
	       return ;
	      }
	  	    
	  	   
	  	   $("#save1").attr("hidden",true);
	  	   $("#save2").removeAttr("hidden");	  	  
	       $("#message").removeAttr("hidden");	
	      
	       $("#j_1").val(row[0]["CONTACTS_NAME"]);	 
	       $("#j_2").val(row[0]["CONTACTS_POST"]);	  
	       $("#j_3").val(row[0]["INCLUDED_CAUSE"]);	 
	       $("#j_4").val(row[0]["FUN_ROLE"]);	
	       $("#j_5").val(row[0]["ROLE_APPEAL"]);	
	       $("#j_6").val(row[0]["CONTACTS_INFORMATION"]);
	       $("#j_7").val(row[0]["REMARKS_MESSAGE"]);
	       //$("#j_8").val(row[0]["UPLOADING_ATT"]); 
	       
	       var car_id=row[0]["ID"];	 
	       	       	      	       	            
	       var j1=$("#j_1").val();	 
	       var j2=$("#j_2").val();	  
	       var j3=$("#j_3").val();	 
	       var j4=$("#j_4").val();	
	       var j5=$("#j_5").val();
	       var j6=$("#j_6").val();
	       var j7=$("#j_7").val();
	       
	       var j8=row[0]["UPLOADING_ATT"];
	       var j9=row[0]["UPLOADING_ATT_ID"];	 
	       var cus_id="<%=request.getParameter("ParentPKValue")%>";
	        
	       save_update=[car_id,j1,j2,j3,j4,j5,j6,j7,j8,cus_id,c9];
	      }
	 
	 
	  
	
      }
	  	
	}
	
	
	 function save_car_message1(){
	   
	    var j1=$("#j_1").val();	 
	    var j2=$("#j_2").val();	  
	    var j3=$("#j_3").val();	 
	    var j4=$("#j_4").val();	
	    var j5=$("#j_5").val();
	    var j6=$("#j_6").val();
	    var j7=$("#j_7").val();
	    var j8=$("#j_8").val();
	        j8=filenames; 
	    var j9=fileId;
	        
        save_update[1]=j1;
        save_update[2]=j2;
        save_update[3]=j3;
        save_update[4]=j4;
        save_update[5]=j5;
        save_update[6]=j6;
        save_update[7]=j7;
        
        //save_update[8]=j8;
        //save_update[10]=j9;
        
        if(j8==""){}else{save_update[8]=j8;}
	    if(j9==""){}else{save_update[10]=j9;}
        
        $.ajax({
			type: "POST",
			url:"/system/cuma?cmd=find_car_tain_message&id=<%=request.getParameter("ParentPKValue")%>&is_no=10&page=1&rows=10&car_map="+save_update,		
			dataType: "json",
			async: false,
			cache: false,
			contentType: false,
			processData: false,
			success: function(data) {	
			
			$(function(){			
			alert("修改成功");
		    $("#message").attr("hidden",true);

		    //location.reload(true);   
		     $("#tt_message").datagrid("loadData",data);
		    
		     save_update="";
			});
			}, error: function(data) {
				var da=JSON.stringify(data);
				//alert("查询失败，请联系管理员！" + "\t" + "错误代码：" + da);
			    alert("修改失败");
			}
			}); 
    }
	
	function delete_messages(){
	  	var row = $('#tt_message').datagrid('getSelections');
	
	  	  if(row==""){
	  	    alert("请选中一条数据");
	  	    return ;
	  	  }
	  	   else{
	  	   var id=Array();
	  	   
	  	   var car_id="<%=request.getParameter("ParentPKValue")%>"
	  	   
	  	   for(var i=0;i<row.length;i++){
	  	      id[i]=row[i]["ID"];
	  	   }
	  	   
	  
	  	    $.ajax({
			type: "POST",
			url:"/system/cuma?cmd=delete_box_tain_message&id="+id+"&cus_id="+car_id+"&is_no=2",		
			dataType: "json",
			async: false,
			cache: false,
			contentType: false,
			processData: false,
			success: function(data) {	
			
			$(function(){			
			alert("删除成功");
		    $("#tt_message").datagrid("loadData",data);
		    
		    
		    
			});
			}, error: function(data) {
				var da=JSON.stringify(data);
				//alert("查询失败，请联系管理员！" + "\t" + "错误代码：" + da);
			    alert("删除失败");
			}
			}); 
	  	
	  	    
	  	    	 }
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
  

</script>  

</head>
<body>	
    
   <!--  <button id="add_message" class="btn btn-primary"  onclick="remove_hidden()" style="margin-bottom:10px">添加</button>
    <button id="update_message" class="btn btn-warning"  onclick="update_messages()" style="margin-bottom:10px">修改</button> 
    <button id="delete_mess" class="btn btn-danger" onclick="delete_messages()" style="margin-bottom:10px">删除</button>  -->
        <br/>
                        
    <div id="message" hidden="true">
      <table>
        <tr>
        	<td hidden="true"><input id="car_id" value=""></td>     
        	<td class="col-md-3"  style="margin-bottom:10px">
        		<div class="form-group">
                   <div class="col-md-5" style="text-align: right;"><label> 联系人:</label></div>
                   <div class="col-md-7">
                   	<input id="j_1" value="" class="form-control" data-toggle="tooltip" data-original-title=" 联系人">
                   </div>
            	</div>
        	</td>
        	<td class="col-md-3"  style="margin-bottom:10px">
        		<div class="form-group">
                   <div class="col-md-5" style="text-align: right;"><label> 联系人职务:</label></div>
                   <div class="col-md-7">
                   	<input id="j_2" class="form-control" data-toggle="tooltip" data-original-title=" 联系人职务" />
                   	</div>
            </div>
        	</td>
        	<td class="col-md-3"  style="margin-bottom:10px">
        		<div class="form-group">
                   <div class="col-md-5" style="text-align: right;"><label> 列入原因:</label></div>
                   <div class="col-md-7">
                   	<input id="j_3" class="form-control" data-toggle="tooltip" data-original-title=" 列入原因" />
                   </div>
            </div>
        	</td>
        	<td class="col-md-3"  style="margin-bottom:10px">
        		<div class="form-group">
                   <div class="col-md-5" style="text-align: right;"><label> 职能角色:</label></div>
                   <div class="col-md-7">
                   <select id="j_4" class="form-control" data-toggle="tooltip" data-original-title=" 职能角色">
                        <option selected="selected" value="">请选择</option>
                        <option  value="最高决策者">最高决策者</option>   
                        <option  value="技术把关者">技术把关者</option>   
                        <option  value="关键应用者">关键应用者</option>   
                        <option  value="辅助决策者">辅助决策者</option>  
                        <option  value="接洽引入者">接洽引入者</option>  
                        <option  value="内线接应者">内线接应者</option>                       
                       </select>
                   </div>
            	</div>
        	</td>
           </tr>
           <tr>
           	<td class="col-md-3"  style="margin-bottom:10px">
           		<div class="form-group">
                   <div class="col-md-5" style="text-align: right;"><label> 角色诉求:</label></div>
                   <div class="col-md-7">
                   	<input id="j_5" class="form-control" data-toggle="tooltip" data-original-title=" 角色诉求" />
                   </div>
            	</div>
           	</td>
           	<td class="col-md-3"  style="margin-bottom:10px">
           		<div class="form-group">
                   <div class="col-md-5" style="text-align: right;"><label> 联系方式:</label></div>
                   <div class="col-md-7">
                   	<input id="j_6" class="form-control" data-toggle="tooltip" data-original-title=" 联系方式"/>
                   </div>
            	</div>
           	</td>
           	<td class="col-md-3"  style="margin-bottom:10px">
           		<div class="form-group">
                   <div class="col-md-5" style="text-align: right;"><label> 备注信息:</label></div>
                   <div class="col-md-7">
                   	<input id="j_7" placeholder="请描述联系人xxxx信息" class="form-control" data-toggle="tooltip" data-original-title=" 备注信息" />
                   </div>
            	</div>
           	</td>
            <td class="col-md-3"  style="margin-bottom:10px">
            	<div class="form-group">
                   <div class="col-md-5" style="text-align: right;"><label> 附件:</label></div>
                   <div class="col-md-7">
                   	<form action="#" enctype="multipart/form-data">                   <!--  javascript:uploadAffix("ENCLOSURE"); -->
						 <input id="j_8" type="file" name="files" id="anchor_ENCLOSURE" onChange="showInfo(this);"><a href='javascript:uploadAffix("ENCLOSURE");'  class="btn green">提交</a>
						 <input type="hidden" id="ENCLOSURE" name="ENCLOSURE" value="" class="fileHidden" data-bv-field="ENCLOSURE">
						 <i class="form-control-feedback" data-bv-icon-for="ENCLOSURE" style="display: none;"></i>
						 <input type="hidden" id="bs_code" name="bs_code" value="" class="fileHidden">
					</form> 
                   </div>
            	</div>
            </td>
            <td class="col-md-3" style="margin-bottom:10px">
	            <button id="save1" class="btnsuc" onclick="save_car_message();">完成</button> 
	            <button id="save2" class="btnsuc" hidden="true" onclick="save_car_message1();">完成</button>
            </td>
           </tr>
          </table>                       
       </div>

	<table id="tt_message" class="easyui-datagrid" style="width: 100%"
			data-options="pageList:[10,20,50],singleSelect:false,rownumbers:true,fitColumns:true,pagination:true,collapsible:true,url:'/system/cuma?cmd=find_car_tain_message&id=<%=request.getParameter("ParentPKValue")%>&is_no=9',method:'post'">
		<thead>
			<tr>
				<th data-options="field:'ID',width:80,align:'center'" hidden="true">主键</th>
				<th data-options="field:'CONTACTS_NAME',width:100,align:'center',halign:'center'">联系人</th>
				<th data-options="field:'CONTACTS_POST',width:100,align:'center',halign:'center'">联系人职务</th>
				<th data-options="field:'INCLUDED_CAUSE',width:100,align:'center',halign:'center'">列入原因</th>
				<th data-options="field:'FUN_ROLE',width:150,align:'center',halign:'center'">职能角色</th>
				<th data-options="field:'ROLE_APPEAL',width:200,align:'center',halign:'center'">角色诉求</th>
				<th data-options="field:'CONTACTS_INFORMATION',width:200,align:'center',halign:'center'">联系方式</th>
				<th data-options="field:'REMARKS_MESSAGE',width:200,align:'center',halign:'center'">备注信息</th>
				<th data-options="field:'UPLOADING_ATT',width:200,align:'center',halign:'center'" formatter="downmessage">附件</th>
			    <th data-options="field:'UPLOADING_ATT_ID',width:200,align:'center',halign:'center'" hidden="true">附件ID</th>
			</tr>
		</thead>
	</table>
</body>
<script type="text/javascript">
var fileId="";
//不使用组件上传附件
function uploadAffix(tid){
    var code="CUMA_DECIDSION_RERATION"; 
    var token="<%=request.getParameter("token")%>";
	console.log(tid);
	var $a = $("#"+tid);
	var bs_code=$("#bs_code").val();
	console.log($a.val());
	//var formData = new FormData($("#"+tid));
	var url="/document/base?cmd=uploadAffixtain&did="+tid+"&batchNo="+$a.val()+"&bscode="+code+"&token="+token;
	
	
	
	//return ;
	$.ajax({
		type: "POST",
		url:url,
		data: new FormData($a.parent()[0]),
		dataType: "json",
		async: false,  
		cache: false,  
		contentType: false,  
		processData: false,
		success: function (data) {
		    $(function(){
		     //alert(111);
		    alert("上传成功");
		    //alert(JSON.stringify(data));
		    fileId = data["fileid"];
		    
			//$a.val(data['batchno']);
			//var fileId = data['fileid'];
			//$("#fileList_"+tid).append("<li class='list-group-item' id='"
			//	+fileId+"'>"+ data['filename'] + "<a href='/document/base?cmd=downloadAffix&fid="
			//	+fileId+"' class='btn green'>下载</a><a href='javascript:deleteAffix(\""
			//	+fileId+"\")' class='btn red'>删除</a></li>");
		    //
		    //
		    
		    });
		   
		},
		error: function(data) {
			alert("上传附件失败，请联系管理员"+data);
		}
	});
	clearFile();
}

function downmessage(value,rowData,rowindex){
    if(value=="请填写"){
     
    }
    else{
    return "<a href='/document/base?cmd=downloadAffix&fid="+rowData["UPLOADING_ATT_ID"]+"'>"+value+"</a>";  
   }
}
</script>
</html>