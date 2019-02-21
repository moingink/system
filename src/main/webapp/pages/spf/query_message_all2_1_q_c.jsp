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
	function save_car_message(){
	   
	   var car_id="<%=request.getParameter("cus_id") %>";
	 
	   var cus_name="<%=request.getParameter("name") %>";
	   var pro="<%=request.getParameter("pro") %>";
	   var car_mode=$("#car_message_").val();
	 
	   var cus_id="<%=request.getParameter("ParentPKValue")%>";
	  
	   var car_map=[car_id,cus_name,pro,car_mode,cus_id];
	   
	  alert(car_map);

	   $.ajax({
			type: "POST",
			url:"/system/cuma?cmd=find_car_tain_message&id=<%=request.getParameter("ParentPKValue")%>&is_no=6&page=1&rows=10&car_map="+car_map,		
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
     
 
  function sure_message(){
  
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
	  	  //row[0]["CUS_CAR_MODE"]="11";
	  	  if(row[0]["CUS_CAR_MODE"]=="请填写"){
	       alert("请先添加数据");
	       
	       return ;
	      }
	  	    
	  	    //alert($('#default', window.parent.document).html());
            //在iframe中调用父页面中定义的变量
            //alert(parent.value);
            //在iframe中调用父页面中定义的方法
            parent.car_messages_all(row[0]["CUS_CAR_MODE"],row[0]["ID"]);
	  	   
	  	  
	      }
	 
  
   }
  }
 
	
	
	
	
</script>  

</head>
<body>	
    
       <!--     添加车型:<input id="car_message_"  value=""/> <button id="add_message"  onclick="save_car_message()">添加</button>   
    <hr />  -->
    <button onclick="sure_message()">确定 </button>   <label>Tip：请先选择客户车型，再添加车型服务内容。</label>                 
                         
	 <table id="tt_message1" class="easyui-datagrid" style="width: 100%"
			data-options="singleSelect:false,rownumbers:true,fitColumns:true,pagination:true,collapsible:true,url:'/system/cuma?cmd=find_car_tain_message&id=<%=request.getParameter("ParentPKValue")%>&is_no=5',method:'post'">
		<thead>
			<tr>
				<th data-options="field:'ID',width:80,align:'center'" hidden="true">主键</th>
				<th data-options="field:'CUS_NAME',width:100,align:'center',halign:'center'" hidden="true">客户名称</th>
				<th data-options="field:'PRO_NAME',width:100,align:'center',halign:'center'" hidden="true">品牌</th>
				<th data-options="field:'CUS_CAR_MODE',width:100,align:'center',halign:'center'">客户车型</th>
				
			</tr>
		</thead>
	</table> 
	
</body>
</html>