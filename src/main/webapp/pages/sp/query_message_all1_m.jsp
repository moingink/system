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

<script type="text/javascript">
 var save_update="";

 function remove_hidden(){
 
     $("#save2").attr("hidden",true);
	 $("#save1").removeAttr("hidden");
	 $("#message").removeAttr("hidden");
	 
	       $("#year_").val("");
	 
	       $("#car_").val("");
	  
	       $("#pc").val("");
	 
	       $("#cl").val("");
	
	       $("#yc_").val("");	 
	}
	
	function save_car_message(){
	   
	   var car_id="<%=request.getParameter("car_id") %>";
	 
	   var year=$("#year_").val();
	 
	   var car_=$("#car_").val();
	  
	   var pc=$("#pc").val();
	 
	   var cl=$("#cl").val();
	
	   var yc_=$("#yc_").val();
	 
	   var cus_id="<%=request.getParameter("ParentPKValue")%>";
	   var car_map=[car_id,year,car_,pc,cl,yc_,cus_id];
	   
	  // alert(car_map);

	   $.ajax({
			type: "POST",
			url:"/system/cuma?cmd=find_car_message&id=<%=request.getParameter("ParentPKValue")%>&is_no=4&page=1&rows=10&car_map="+car_map,		
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
		     $("#tt_message1").datagrid("loadData",data);
		    
		     
		     
			});
			}, error: function(data) {
				var da=JSON.stringify(data);
				//alert("查询失败，请联系管理员！" + "\t" + "错误代码：" + da);
			    alert("保存失败");
			}
			}); 
		
	}	
     
 

 function update_messages(){
	  	var row = $('#tt_message1').datagrid('getSelections');
	  
	   
	  	
	  	if(row==""){
	  	 alert("请选中一条数据");
	  	   
	  	   return ;
	  	}else{
	  	  if(row.length>1){
	  	   alert("请选中一条数据");
	  	   
	  	   return ;
	  	  }
	  	  else{
	  	  
	  	  if(row[0]["PROJECT"]=="请填写"){
	       alert("请先添加数据");
	       
	       return ;
	      }
	  	    
	  	   
	  	   $("#save1").attr("hidden",true);
	  	   $("#save2").removeAttr("hidden");
	  	  
	       $("#message").removeAttr("hidden");	
	      
	       $("#year_").val(row[0]["PROJECT"]);
	 
	       $("#car_").val(row[0]["PROTYPES"]);
	  
	       $("#pc").val(row[0]["PRODUCJ"]);
	 
	       $("#cl").val(row[0]["SUP_NAME"]);
	
	       $("#yc_").val(row[0]["CON_MESSAGE"]);
	       
	        var car_id=row[0]["ID"];
	 
	        var year=$("#year_").val();
	 
	        var car_=$("#car_").val();
	  
	        var pc=$("#pc").val();
	 
	        var cl=$("#cl").val();
	
	        var yc_=$("#yc_").val();
	 
	        var cus_id="<%=request.getParameter("ParentPKValue")%>";
	        
	        save_update=[car_id,year,car_,pc,cl,yc_,cus_id];
	      }
	 
	 
	  
	
      }
	  	
	}
	
	
	 function save_car_message1(){
	   
	        var year=$("#year_").val();
	 
	        var car_=$("#car_").val();
	  
	        var pc=$("#pc").val();
	 
	        var cl=$("#cl").val();
	
	        var yc_=$("#yc_").val();
	 
        save_update[1]=year;
        save_update[2]=car_;
        save_update[3]=pc;
        save_update[4]=cl;
        save_update[5]=yc_;
        
        $.ajax({
			type: "POST",
			url:"/system/cuma?cmd=find_car_message&id=<%=request.getParameter("ParentPKValue")%>&is_no=4&page=1&rows=10&car_map="+save_update,		
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
		     $("#tt_message1").datagrid("loadData",data);
		    
		    
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
	  	var row = $('#tt_message1').datagrid('getSelections');
	
	  	  if(row==""){
	  	    alert("请选中一条数据");
	  	    return ;
	  	  }
	  	   else{
	  	   
	  	   if(row[0]["PROJECT"]=="请填写"){
	       alert("请先添加数据");
	       
	       return ;
	      }
	  	   
	  	   var id=Array();
	  	   
	  	   var car_id="<%=request.getParameter("ParentPKValue")%>"
	  	   
	  	   for(var i=0;i<row.length;i++){
	  	      id[i]=row[i]["ID"];
	  	   }
	  	   
	  
	  	    $.ajax({
			type: "POST",
			url:"/system/cuma?cmd=delete_box_message&id="+id+"&cus_id="+car_id+"&is_no=1",		
			dataType: "json",
			async: false,
			cache: false,
			contentType: false,
			processData: false,
			success: function(data) {	
			
			$(function(){			
			alert("删除成功");
		    $("#tt_message1").datagrid("loadData",data);
		    
		    
		   
			});
			}, error: function(data) {
				var da=JSON.stringify(data);
				//alert("查询失败，请联系管理员！" + "\t" + "错误代码：" + da);
			    alert("删除失败");
			}
			}); 
	  	
	  	    
	  	    	 }
	}
</script>  

</head>
<body>	
    
   
                        
                         <div id="message" hidden="true">
                         <table>
                         <tr><td hidden="true"><input id="car_id" value=""></td>     <td>项目:<input id=year_ value=""></input>
                       <!--   <select id="year_">
                                                                                             <option selected="selected" value="">请选择</option>
                                                                                             <option  value="2018">2018</option>   
                                                                                             <option  value="2019">2019</option>   
                                                                                             <option  value="2020">2020</option>                        
                                                                                             </select> -->
                         </td><td>业务类型:<input id="car_" /></td><td>产品:<input id="pc" /></td></tr>
                         <tr><td>供应商名称:<input id="cl" /></td><td>合同期信息:<input id="yc_"/></td><td><button id="save1" onclick="save_car_message();">完成</button> <button id="save2" hidden="true" onclick="save_car_message1();">完成</button></td></tr>
                         </table>                       
                         </div>

	 <table id="tt_message1" class="easyui-datagrid" style="width: 100%"
			data-options="singleSelect:false,rownumbers:true,fitColumns:true,pagination:true,collapsible:true,url:'/system/cuma?cmd=find_car_message&id=<%=request.getParameter("ParentPKValue")%>&is_no=3',method:'post'">
		<thead>
			<tr>
				<th data-options="field:'ID',width:80,align:'center'" hidden="true">主键</th>
				<th data-options="field:'PROJECT',width:100,align:'center',halign:'center'">项目</th>
				<th data-options="field:'PROTYPES',width:100,align:'center',halign:'center'">业务类型</th>
				<th data-options="field:'PRODUCJ',width:100,align:'center',halign:'center'">产品</th>
				<th data-options="field:'SUP_NAME',width:150,align:'center',halign:'center'">供应商名称</th>
				<th data-options="field:'CON_MESSAGE',width:200,align:'center',halign:'center'">合同期信息</th>
			</tr>
		</thead>
	</table> 
	
</body>
</html>