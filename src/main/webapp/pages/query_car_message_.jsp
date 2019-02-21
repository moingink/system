<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>   
<%@page import="com.yonyou.business.RmDictReferenceUtil"%> 
<%@page import="java.util.concurrent.ConcurrentHashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Map.Entry"%>     
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

   <%
      ConcurrentHashMap<String, String> selectMap;
      ConcurrentHashMap<String, String> selectMap1;
      ConcurrentHashMap<String, String> selectMap2;
      ConcurrentHashMap<String, String> selectMap3;
      selectMap = RmDictReferenceUtil.getDictReference("CAR_MODE_SERVICE");
      selectMap1 = RmDictReferenceUtil.getDictReference("YES_OR_NO_TWO");
      selectMap2 = RmDictReferenceUtil.getDictReference("SERVICE_MODE");
      selectMap3 = RmDictReferenceUtil.getDictReference("SERVICETYPE");
      
   %>


<link rel="stylesheet" type="text/css" href="../easyui/demo/demo.css">
<link rel="stylesheet" type="text/css" href="../easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="../easyui/themes/icon.css">
<link rel="stylesheet" href="../vendor/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="../vendor/bootstrap-table/src/bootstrap-table.css">
<script type="text/javascript" src="../easyui/jquery.min.js"></script>
<script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="../easyui/locale/easyui-lang-zh_CN.js"></script>
<script src="../vendor/bootstrap/js/bootstrap.min.js"></script>
<script src="../vendor/bootstrap-table/src/bootstrap-table.js"></script>
<script src="../js/bootTable.js"></script>

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
 var filenames="";
 
  $(function(){
    $(document).ready(function(){
       var c1=$("#c1");
       var c2=$("#c2");
       var c4=$("#c4");
       var c5=$("#c5");
        <%
         for(Map.Entry<String,String> entry:selectMap.entrySet()){
        	   String select_message=entry.getKey();
        %>
          c1.append("<option value='<%=select_message%>'"+">"+'<%=select_message%>'+"</option>");
        <%
        };
        %>
        
         <%
         for(Map.Entry<String,String> entry:selectMap1.entrySet()){
        	   String select_message1=entry.getKey();
        %>
          c2.append("<option value='<%=select_message1%>'"+">"+'<%=select_message1%>'+"</option>");
        <%
        };
        %>
        <%
         for(Map.Entry<String,String> entry:selectMap2.entrySet()){
        	   String select_message2=entry.getKey();
        %>
          c4.append("<option value='<%=select_message2%>'"+">"+'<%=select_message2%>'+"</option>");
        <%
        };
        %>
        <%
         for(Map.Entry<String,String> entry:selectMap3.entrySet()){
        	   String select_message3=entry.getKey();
        %>
          c5.append("<option value='<%=select_message3%>'"+">"+'<%=select_message3%>'+"</option>");
        <%
        }
        %>
        
               $("#c2").change(function(){
            // y3 y4 y5 y6 y7 y8 
            //$("#xmtable").css("display","none");
            //$("#xm").css("display","block");
            
            
            
           var mes=$("#c2").val(); 
           if(mes=="否"){
              //$("#y3").hide();
              //$("#y4").hide();
              //$("#y5").hide();
              //$("#y6").hide();
              //$("#y7").hide();
              //$("#y8").hide();
              $("#y3").css("display","none");
              $("#y4").css("display","none");
              $("#y5").css("display","none");
              $("#y6").css("display","none");
              $("#y7").css("display","none");
              $("#y8").css("display","none");
           }
           else{
             //$("#y3").show();
             //$("#y4").show();
             //$("#y5").show();
             //$("#y6").show();
             //$("#y7").show();
             //$("#y8").show();
             $("#y3").css("display","block");
             $("#y4").css("display","block");
             $("#y5").css("display","block");
             $("#y6").css("display","block");
             $("#y7").css("display","block");
             $("#y8").css("display","block");
             
           }
          
        
        });

    });
  
  }); 
</script>

<script type="text/javascript">

 var save_update="";

 function remove_hidden(){
 
     $("#save2").attr("hidden",true);
	 $("#save1").removeAttr("hidden");
	 $("#message").removeAttr("hidden");
	 
	   var c1=$("#c1").val("");
	   var c2=$("#c2").val("");
	   var c3=$("#c3").val("");
	   var c4=$("#c4").val(""); 
	   var c5=$("#c5").val("");
	   var c6=$("#c6").val("");
	   var c7=$("#c7").val("");
	   var c8=$("#c8").val("");  
	     fileId=""; 	 	 
	}
	
	
	function save_car_message(){
	  var car_id="";
	   $.ajax({
			type: "POST",
			url: "/system/base?cmd=getIDByDataSourceCode&pageCode=CUMA_ACCOUNT_TAIN",
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

	   var c1=$("#c1").val();
	   var c2=$("#c2").val();
	   var c3=$("#c3").val();
	   var c4=$("#c4").val(); 
	   var c5=$("#c5").val();
	   var c6=$("#c6").val();
	   var c7=$("#c7").val();
	   var c8=$("#c8").val();  
	   var carmessageid="<%=request.getParameter("carmessageid")%>";
       var c9=fileId;	  
	   c8=filenames;
	   var cus_id="<%=request.getParameter("ParentPKValue")%>";
	   var car_map=[car_id,c1,c2,c3,c4,c5,c6,c7,c8,cus_id,c9,carmessageid];
	    
	   if(c8==""){c9="0";}
	   
	   
	   if(c2=="否"){
	       if(c1=="" || c2=="" ){
	        
          
	              alert("不能为空");
	              return ;
             
	   }
	   else{	   
	   $.ajax({
			type: "POST",
			url:"/system/cuma?cmd=find_car_tain_message&id=<%=request.getParameter("ParentPKValue")%>&is_no=16&page=1&rows=10&car_map="+car_map,		
			dataType: "json",
			async: false,
			cache: false,
			contentType: false,
			processData: false,
			success: function(data) {	
			
			 $(function(){			
			 alert("保存成功");
		     $("#message").attr("hidden",true);

		     $("#tt_message").datagrid("loadData",data);
		    
		     $("#pc_num").val(data["all"][0]["ALL_NUMBER"]);
			 $("#cl_num").val(data["all"][0]["ALL_CAR_NUM"]);
			 $("#yc_num").val(data["all"][0]["ALL_WILL_YEAR_PRICE"]);
		     
			});
			}, error: function(data) {
				var da=JSON.stringify(data);
				//alert("查询失败，请联系管理员！" + "\t" + "错误代码：" + da);
			    alert("保存失败");
			}
			}); 
		
	}	
	   
	   }
	   else {
	    if(c1=="" || c2=="" || c3=="" || c4=="" || c5=="" || c6=="" || c7==""){
	        
          
	              alert("不能为空");
	              return ;
             
	   }
	   else{	   
	   $.ajax({
			type: "POST",
			url:"/system/cuma?cmd=find_car_tain_message&id=<%=request.getParameter("ParentPKValue")%>&is_no=16&page=1&rows=10&car_map="+car_map,		
			dataType: "json",
			async: false,
			cache: false,
			contentType: false,
			processData: false,
			success: function(data) {	
			
			 $(function(){			
			 alert("保存成功");
		     $("#message").attr("hidden",true);

		     $("#tt_message").datagrid("loadData",data);
		    
		     $("#pc_num").val(data["all"][0]["ALL_NUMBER"]);
			 $("#cl_num").val(data["all"][0]["ALL_CAR_NUM"]);
			 $("#yc_num").val(data["all"][0]["ALL_WILL_YEAR_PRICE"]);
		     
			});
			}, error: function(data) {
				var da=JSON.stringify(data);
				//alert("查询失败，请联系管理员！" + "\t" + "错误代码：" + da);
			    alert("保存失败");
			}
			}); 
		
	}	
	   
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
	  	  
	  	  if(row[0]["CAR_MODE_SERVICE"]=="请填写"){
	       alert("请先添加数据");
	       
	       return ;
	      }
	  	    
	  	   
	  	   $("#save1").attr("hidden",true);
	  	   $("#save2").removeAttr("hidden");
	  	  
	       $("#message").removeAttr("hidden");	
	      
	       $("#c1").val(row[0]["CAR_MODE_SERVICE"]);
	       $("#c2").val(row[0]["YES_OR_NO"]);
	       $("#c3").val(row[0]["PROVIDER"]);
	       $("#c4").val(row[0]["SERVICE_MODE"]);
	       $("#c5").val(row[0]["SERVICETYPE"]);
	       $("#c6").val(row[0]["CAR_MESSAGE"]);
	       $("#c7").val(row[0]["CONTENT_PATH"]);
	       $("#c8").val(row[0]["UPLODE"]);
	       var car_id=row[0]["ID"];
	       var c1=$("#c1").val();
		   var c2=$("#c2").val();
		   var c3=$("#c3").val();
		   var c4=$("#c4").val(); 
		   var c5=$("#c5").val();
		   var c6=$("#c6").val();
		   var c7=$("#c7").val();
		   var c8=row[0]["UPLODE"];
	       var c9=row[0]["UPLOAD_PICTURE_ID"];
	       var cus_id="<%=request.getParameter("ParentPKValue")%>";
	       var carmessageid="<%=request.getParameter("carmessageid")%>";
	       save_update=[car_id,c1,c2,c3,c4,c5,c6,c7,c8,cus_id,c9,carmessageid];
	      }
      }
	  	
	}
	
	
	 function save_car_message1(){
	   
	       var c1=$("#c1").val();
		   var c2=$("#c2").val();
		   var c3=$("#c3").val();
		   var c4=$("#c4").val(); 
		   var c5=$("#c5").val();
		   var c6=$("#c6").val();
		   var c7=$("#c7").val();
		   var c8=$("#c8").val(); 
	        
	        c8=filenames;
	        var c9=fileId;
	        
	        save_update[1]=c1;
	        save_update[2]=c2;
	        save_update[3]=c3;
	        save_update[4]=c4;
	        save_update[5]=c5;
	        save_update[6]=c6;
	        save_update[7]=c7;
	        
	        if(c8==""){}else{save_update[8]=c8;}
	        if(c9==""){}else{save_update[10]=c9;}
	        //save_update[8]=c8;
	        //save_update[10]=c9;
        
        $.ajax({
			type: "POST",
			url:"/system/cuma?cmd=find_car_tain_message&id=<%=request.getParameter("ParentPKValue")%>&is_no=16&page=1&rows=10&car_map="+save_update,		
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
		    
		     $("#pc_num").val(data["all"][0]["ALL_NUMBER"]);
			 $("#cl_num").val(data["all"][0]["ALL_CAR_NUM"]);
			 $("#yc_num").val(data["all"][0]["ALL_WILL_YEAR_PRICE"]);
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
	  	   
	  	   if(row[0]["CAR_MODE_SERVICE"]=="请填写"){
	       alert("请先添加数据");
	       
	       return ;
	      }
	  	   
	  	   if(confirm("确定要删除选中数据吗？")){  
	  	   
	  	   var id=Array();
	  	   
	  	   var car_id="<%=request.getParameter("ParentPKValue")%>"
	  	   
	  	   for(var i=0;i<row.length;i++){
	  	      id[i]=row[i]["ID"];
	  	   }
	  	   
	  
	  	    $.ajax({
			type: "POST",
			url:"/system/cuma?cmd=delete_box_tain_message&id="+id+"&cus_id="+car_id+"&is_no=5",		
			dataType: "json",
			async: false,
			cache: false,
			contentType: false,
			processData: false,
			success: function(data) {	
			
			$(function(){			
			alert("删除成功");
		    $("#tt_message").datagrid("loadData",data);
		    
		    
		     $("#pc_num").val(data["all"][0]["ALL_NUMBER"]);
			 $("#cl_num").val(data["all"][0]["ALL_CAR_NUM"]);
			 $("#yc_num").val(data["all"][0]["ALL_WILL_YEAR_PRICE"]);
			});
			}, error: function(data) {
				var da=JSON.stringify(data);
				//alert("查询失败，请联系管理员！" + "\t" + "错误代码：" + da);
			    alert("删除失败");
			}
			}); 
	  	
	  	    
	  	    	 }else{
	  	    	 
	  	    	   return ;
	  	    	 
	  	    	 }
	         }
	}


function getFileName(obj){
  
    var pos = obj.value.lastIndexOf("\\")*1;
    pos=obj.value.substring(pos+1);
    return pos; 
}

function showInfo(obj)
{
    var filename =getFileName(obj);
    filenames=filename;
}

function open_message2(){
  $('#winn').window('open'); 
 }


function sure_message2(){
  
   $('#tt_n').datagrid({singleSelect:false});

   var row=$("#tt_n").datagrid("getSelections");
   
   if(row.length>0){
   
   
     if(row.length=="1"){   
         $("#c3").val(row[0]["ALL_COMPETITORS"]);
         $('#winn').window('close'); 
     }
     else{
       alert("请选择一条数据");
       return ;
     }
   }
   else{
   alert("请选择一条数据");
   return ;
   
   }
  
 }


</script>  

</head>
<body>	
    
    <button id="add_message" class="btn btn-primary"  onclick="remove_hidden()" style="margin-bottom:10px">添加</button> 
    <button id="update_message" class="btn btn-warning"  onclick="update_messages()" style="margin-bottom:10px">修改</button>  
    <button id="delete_mess" class="btn btn-danger" onclick="delete_messages()" style="margin-bottom:10px">删除</button> 
        <br/>
                        
         <div id="message" hidden="true">
           <table style="width:100%;">
             <tr>
               <td hidden="true"><input id="car_id" value=""></td>
               <td class="col-md-3"  style="margin-bottom:10px">
           	<div class="form-group">
                   <div class="col-md-5" style="text-align: right;"><label class="control-labelnew" data-toggle="tooltip" data-original-title=" 服务内容分类:"> <font color="red" size="3">*&nbsp;</font>服务内容分类:</label></div>
                   <div class="col-md-7">
                   	<select id="c1" class="form-control" data-toggle="tooltip" data-original-title=" 服务内容分类"></select>
                   </div>
            </div>
          </td>
               <td class="col-md-3" style="margin-bottom:10px">
               	<div class="form-group">
                	<div class="col-md-5" style="text-align: right;"><label><font color="red" size="3">*&nbsp;</font> 是否提供:</label></div>
                	<div class="col-md-7">
                		<select id="c2" class="form-control" data-toggle="tooltip" data-original-title="是否提供"></select>
                	</div>
             	</div>
              </td>
               <td class="col-md-3" style="margin-bottom:10px">
               	<div id="y3" class="form-group">
                	<div class="col-md-5" style="text-align: right;"><label><font color="red" size="3">*&nbsp;</font> 提供商:</label></div>
                	<div class="col-md-7">
                   		<input id="c3" onclick="open_message2()" class="form-control" data-toggle="tooltip" data-original-title="提供商" />
                	</div>
             	</div>
              </td>
               <td class="col-md-3">
           	<div  id="y4" class="form-group">
                   <div class="col-md-5" style="text-align: right;"><label class="control-labelnew" data-toggle="tooltip" data-original-title=" 服务接入方式:"> <font color="red" size="3">*&nbsp;</font> 服务接入方式:</label></div>
                   <div class="col-md-7">
                   	<select id="c4" class="form-control" data-toggle="tooltip" data-original-title=" 服务接入方式"></select>
                   </div>
            </div>
          </td>
              </tr>
              <tr>
               <td class="col-md-3" style="margin-bottom:10px">
               	<div id="y5" class="form-group">
                <div class="col-md-5" style="text-align: right;"><label><font color="red" size="3">*&nbsp;</font> 服务类别:</label></div>
                <div class="col-md-7">
                	<select id="c5" class="form-control" data-toggle="tooltip" data-original-title="服务类别"></select>
                </div>
             </div>
              </td>
               <td class="col-md-3" style="margin-bottom:10px">
               	<div id="y6" class="form-group">
                	<div class="col-md-5" style="text-align: right;"><label><font color="red" size="3">*&nbsp;</font> 服务内容:</label></div>
                	<div class="col-md-7">
                		<textarea id="c6"  rows="1" cols="20" class="form-control" data-toggle="tooltip" data-original-title="服务内容"></textarea>
                	</div>
             	</div>
              </td>
               <td class="col-md-3" style="margin-bottom:10px">
               	<div id="y7" class="form-group">
                <div class="col-md-5" style="text-align: right;"><label><font color="red" size="3">*&nbsp;</font>操作路径:</label></div>
                <div class="col-md-7">
                	<textarea id="c7"  rows="1" cols="20" class="form-control" data-toggle="tooltip" data-original-title="操作路径"></textarea>
                </div>
             </div>
               </td>
               <td class="col-md-3" style="margin-bottom:10px">
               	<div id="y8" class="form-group">
                	<div class="col-md-5" style="text-align: right;"><label> 附件:</label></div>
                	<div class="col-md-7">
                		<form action="#" enctype="multipart/form-data">                   <!--  javascript:uploadAffix("ENCLOSURE"); -->
						 <input id="c8" type="file" name="files" id="anchor_ENCLOSURE" onChange="showInfo(this);"><a href='javascript:uploadAffix("ENCLOSURE");'  class="btn green">提交</a>
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
			data-options="pageList:[10,20,50],singleSelect:false,rownumbers:true,fitColumns:true,pagination:true,collapsible:true,url:'/system/cuma?cmd=find_car_tain_message&id=<%=request.getParameter("ParentPKValue")%>&carmessageid=<%=request.getParameter("carmessageid") %>&is_no=15',method:'post'">
		<thead>
			<tr>
				<th data-options="field:'ID',width:80,align:'center'" hidden="true">主键</th>
				<th data-options="field:'CAR_MODE_SERVICE',width:100,align:'center',halign:'center'">服务内容分类</th>
				<th data-options="field:'YES_OR_NO',width:100,align:'center',halign:'center'">是否提供</th>
				<th data-options="field:'PROVIDER',width:100,align:'center',halign:'center'">提供商</th>
				<th data-options="field:'SERVICE_MODE',width:150,align:'center',halign:'center'">服务接入方式</th>
				<th data-options="field:'SERVICETYPE',width:200,align:'center',halign:'center'">服务类别</th>
				<th data-options="field:'CAR_MESSAGE',width:100,align:'center',halign:'center'">服务内容</th>
				<th data-options="field:'CONTENT_PATH',width:150,align:'center',halign:'center'">操作路径</th>
				<th data-options="field:'UPLODE',width:200,align:'center',halign:'center'"  formatter="downmessage">附件</th>
			    <th data-options="field:'UPLOAD_PICTURE_ID',width:200,align:'center',halign:'center'" hidden="true">附件ID</th>
			</tr>
		</thead>
	</table>
	
	<!--供应商  -->
       
       <div id="winn" class="easyui-window" title="供应商" style="width:800px;height: 300px"   
        data-options="iconCls:'icon-save',modal:true,closed:true,minimizable:false,maximizable:false">   
   
  <!--  <button onclick="sure_message()">确定</button> -->
   <table id="tt_n" class="easyui-datagrid" toolbar="#tbn"
			data-options="fit:true,pageList:[10,20,50],singleSelect:false,rownumbers:true,fitColumns:true,pagination:true,collapsible:true,url:'/system/cuma?cmd=bus_types&is_no=3',method:'post'">
		<thead>
			<tr>
				<th data-options="field:'ID',width:80,halign:'center'" hidden="true">主键</th>
				<th data-options="field:'ALL_COMPETITORS',width:100,align:'center',halign:'center'">供应商</th>
				<!-- <th data-options="field:'CAR_CLASSIFY',width:100,align:'center',halign:'center'">车系</th>
				<th data-options="field:'CAR_NUM_MORE',width:100,align:'center',halign:'center'">排产量(万辆)</th>
				<th data-options="field:'CAR_NUM',width:150,align:'center',halign:'center'">车联网配质量(万辆)</th>
				<th data-options="field:'WILL_YEAR_PRICE',width:200,align:'center',halign:'center'">预测年收入(万元)</th> -->
			</tr>
		</thead>
	</table>
    </div>  

        <div id="tbn" style="padding:3px">
    	<a href="#" class="easyui-linkbutton" plain="true" onclick="sure_message2()">确定</a>
        </div> 
	
</body>
<script type="text/javascript">

	$('#winn').window({
	//width:500,
	top:100,
	left: 80
});
var fileId="";
//不使用组件上传附件
function uploadAffix(tid){
     if(filenames==""||filenames==null){
      return ;
    }


    var code="CUMA_SERVICE_CONTENT"; 
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
		    alert("上传成功");		
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
    return "<a href='/document/base?cmd=downloadAffix&fid="+rowData["UPLOAD_PICTURE_ID"]+"'>"+value+"</a>";  
   }
}
</script>
</html>