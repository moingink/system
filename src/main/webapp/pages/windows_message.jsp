<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<!-- <link rel="stylesheet" type="text/css" href="../easyui/demo/demo.css">
<link rel="stylesheet" type="text/css" href="../easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="../easyui/themes/icon.css">
<link rel="stylesheet" href="../vendor/bootstrap/css/bootstrap.min.css">
<script type="text/javascript" src="../easyui/jquery.min.js"></script>
<script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="../easyui/locale/easyui-lang-zh_CN.js"></script> -->

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


<style>
.window-mask{
	width:100% !important;
	height:100% !important;
}
</style>

<script type="text/javascript">
 
 function sure_message(){
  $('#tt_pro').datagrid({singleSelect:false});
  var row=$("#tt_pro").datagrid("getSelections"); 
  if(row.length>0){
      
     var message=Array();
     
     for(var i=0;i<row.length;i++){
           var a=row[i]["PRO_NAME"];
           message[i]=a;
     }
   
     var sl=row.length;
     var carmes="";
     
     carmes=message.toString();
     
     for(var h=0;h<sl;h++){
        carmes=carmes.replace(","," ");
     }
     
     
     
     //alert(carmes);
          
     //return ;
     opener.sure_message1(carmes);
     
     $('#wins').window('close'); 
     window.close();
   }
   else{
   alert("请选择一条数据");
   return ;
   
   }
}
 


</script>


</head>
<body>



       <!--产品  -->  
      <div id="wins" class="easyui-window" title="产品"  
        data-options="fit:true, iconCls:'icon-save',modal:true,closed:false,minimizable:false,maximizable:false">   
      <!--<button onclick="sure_message()">确定</button> -->
   <table id="tt_pro" class="easyui-datagrid" toolbar="#tbs"  style="width: 100%"
			data-options="fit:true,singleSelect:false,rownumbers:true,fitColumns:true,pagination:true,collapsible:true,url:'/system/cuma?cmd=bus_types&is_no=2&map=<%=request.getParameter("map") %>',method:'post'">
		<thead>
			<tr>
				<th data-options="field:'ID',width:10,halign:'center'" hidden="true">主键</th>
				<th data-options="field:'PRO_TYPE',width:20,align:'center',halign:'center'">业务类型</th>
				<th data-options="field:'PRO_NAME',width:20,align:'center',halign:'center'">产品</th>
				<!-- <th data-options="field:'CAR_CLASSIFY',width:100,align:'center',halign:'center'">车系</th>
				<th data-options="field:'CAR_NUM_MORE',width:100,align:'center',halign:'center'">排产量(万辆)</th>
				<th data-options="field:'CAR_NUM',width:150,align:'center',halign:'center'">车联网配质量(万辆)</th>
				<th data-options="field:'WILL_YEAR_PRICE',width:200,align:'center',halign:'center'">预测年收入(万元)</th> -->
			</tr>
		</thead>
	</table>
    </div>  
        <div id="tbs" style="padding:3px">
    	<a href="#" class="btn btn-info" plain="true" onclick="sure_message()">确定</a>
        </div>  
      
		
		
	







<!-- 
<input type="text" id="tt" value="" onclick="asd()"/>


<div id="win" class="easyui-window" title="ssss" style="width:600px;"   
        data-options="iconCls:'icon-save',modal:true,closed:true,minimizable:false,maximizable:false">   
   
   <button onclick="sure_message()">确定</button>
   <table id="tt_message" class="easyui-datagrid" toolbar="#tb"
			data-options="singleSelect:false,rownumbers:true,fitColumns:true,pagination:true,collapsible:true,url:'/system/cuma?cmd=find_car_message&id=201807260000000005&is_no=0',method:'post'">
		<thead>
			<tr>
				<th data-options="field:'ID',width:80,halign:'center'" hidden="true">主键</th>
				<th data-options="field:'YEAR',width:100,align:'center',halign:'center'">年份</th>
				<th data-options="field:'CAR_CLASSIFY',width:100,align:'center',halign:'center'">车系</th>
				<th data-options="field:'CAR_NUM_MORE',width:100,align:'center',halign:'center'">排产量(万辆)</th>
				<th data-options="field:'CAR_NUM',width:150,align:'center',halign:'center'">车联网配质量(万辆)</th>
				<th data-options="field:'WILL_YEAR_PRICE',width:200,align:'center',halign:'center'">预测年收入(万元)</th>
			</tr>
		</thead>
	</table>
</div>  

        <div id="tb" style="padding:3px">
    	<a href="#" class="easyui-linkbutton" plain="true" onclick="sure_message()">确定</a>
        </div> -->
</body>
</html>