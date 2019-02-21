<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
		var products = [
		    //{productid:'FI-SW-01',name:'是'},
		     {productid:'是',name:'是'},
		     {productid:'否',name:'否'}
		];
		
		var productses = [
		    {productid:'高',name:'高'},
		    {productid:'中',name:'中'},
		    {productid:'低',name:'低'}
		];
		
		var risks = [
		    //{productid:'FI-SW-01',name:'关闭'},
		    //{productid:'K9-DL-01',name:'打开'}
		    {productid:'打开',name:'打开'},
		    {productid:'关闭',name:'关闭'}
		];
		
		$(function(){
			$('#tt').datagrid({
				title:'',
				iconCls:'icon-edit',
				width:"100%",
			//	height:250,
				pagination:true,
				fitColumns:true,
				singleSelect:true,
				idField:'itemid',
				url:'/system/cuma?cmd=find_car_message&id=<%=request.getParameter("ParentPKValue")%>&is_no=7',
				columns:[[
				    {field:'ID',title:'ID',width:180, hidden: true,editor:'text'},
				    {field:'MAIN_TASK',title:'主要任务',width:180,editor:'text'},
				    {field:'PERSON_LIABLE',title:'责任人',width:180,editor:'text'},
				    {field:'COMPLETE_TIME',title:'完成时间',width:180,editor:{type:'datetimebox',options:{required:true}}},				    
				    {field:'DELIVERIES',title:'交付物',width:180,editor:'text'},
				    {field:'RESOURCE_REQ',title:'所需资源',width:180,editor:'text'}
				
				]],
				
				onBeforeEdit:function(index,row){
					row.editing = true;
					updateActions(index);
				},
				onAfterEdit:function(index,row){
					row.editing = false;
					updateActions(index);
				},
				onCancelEdit:function(index,row){
					row.editing = false;
					updateActions(index);
				}
			});
		});
		function updateActions(index){
			$('#tt').datagrid('updateRow',{
				index: index,
				row:{}
			});
		}
		function getRowIndex(target){
			var tr = $(target).closest('tr.datagrid-row');
			return parseInt(tr.attr('datagrid-row-index'));
		}
		function editrow(target){
			$('#tt').datagrid('beginEdit', getRowIndex(target));
		}
		function deleterow(target){
			$.messager.confirm('Confirm','Are you sure?',function(r){
		    var row = $('#tt').datagrid('getSelected');
            
            
            if (row){
            
			
			$.ajax({
			type: "POST",
			url:"/system/base?cmd=save_procj_id&id="+row.ID+"&del=0",
			//data:{"INFORMATION":row.INFORMATION,"INFLUENCE_LEVEL":row.INFLUENCE_LEVEL,"OCCURENCE_TIME":row.OCCURENCE_TIME,"INTERNAL_SOLUTION":row.INTERNAL_SOLUTION,"SOLUTION_MEASURES":row.SOLUTION_MEASURES,"DEADLINE":row.DEADLINE,"RISK_STATUS":row.RISK_STATUS},		
			dataType: "json",
			async: false,
			cache: false,
			contentType: false,
			processData: false,
			success: function(data) {
			alert("删除成功！");
			location.reload(true);   
			
			}, error: function(data) {				
			}
			}); 
					
			}		
				
				if (r){
					$('#tt').datagrid('deleteRow', getRowIndex(target));
				}
				
			});
		}
		function saverow(target){
			var row = $('#tt').datagrid('getSelected');
            if (row){
            $('#tt').datagrid('endEdit', getRowIndex(target));
	       
	         if(row.ID==""){row.ID="0";}
	         
	         var cus_id="<%=request.getParameter("ParentPKValue")%>";
	         
	         var map=[row.ID,row.MAIN_TASK,row.PERSON_LIABLE,row.COMPLETE_TIME,row.DELIVERIES,row.RESOURCE_REQ,cus_id];
           
            alert(map);
           
            $.ajax({
			type: "POST",
			url:"/system/cuma?cmd=find_car_message&id=<%=request.getParameter("ParentPKValue")%>&is_no=8&page=1&rows=10&car_map="+map,
			dataType: "json",
			async: false,
			cache: false,
			contentType: false,
			processData: false,
			success: function(data) {
				alert("保存成功！");
				//location.reload(true);   
			}, error: function(data) {
				
			}
			}); 
            
            }else{
             alert("请选中行数据进行操作!");
             $('#tt').datagrid('cancelEdit', getRowIndex(target));
             return ;
            }
            
		}
		function cancelrow(target){
			$('#tt').datagrid('cancelEdit', getRowIndex(target));
		}
		function insert(){
			var row = $('#tt').datagrid('getSelected');
			if (row){
				var index = $('#tt').datagrid('getRowIndex', row);
			} else {
				index = 0;
			}
			$('#tt').datagrid('insertRow', {
				index: index,
				row:{
					status:'P'
				}
			});
			$('#tt').datagrid('selectRow',index);
			$('#tt').datagrid('beginEdit',index);
		}
	</script>
</head>
<body>
	<!-- <div class="demo-info">
		<div class="demo-tip icon-tip">&nbsp;</div>
		<div>Click the edit button on the right side of row to start editing.</div>
	</div> -->
	
	
	
	<table id="tt"></table>
</body>
</html>