<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<%
   String p_ids=request.getParameter("p_ids");
%>

<link rel="stylesheet" type="text/css" href="../easyui/demo/demo.css">
<link rel="stylesheet" type="text/css" href="../easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="../easyui/themes/icon.css">
<script type="text/javascript" src="../easyui/jquery.min.js"></script>
<script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="../easyui/locale/easyui-lang-zh_CN.js"></script>

<%-- <script type="text/javascript">
$.ajax({
			type: "POST",
			url:"/system/base?cmd=find_procj_id&id=<%=p_ids %>",		
			dataType: "json",
			async: false,
			cache: false,
			contentType: false,
			processData: false,
			success: function(data) {
				
			}, error: function(data) {
				var da=JSON.stringify(data);
				alert("查询失败，请联系管理员！" + "\t" + "错误代码：" + da);
			}
			}); 
</script> --%>
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
		    {productid:'关闭',name:'关闭'},
		    {productid:'风险转问题',name:'风险转问题'}
		];
		
		$(function(){
			$('#tt').datagrid({
				title:'风险问题跟踪',
				iconCls:'icon-edit',
			//	width:660,
			//	height:250,
				pagination:true,
				fitColumns:true,
				singleSelect:true,
				idField:'itemid',
				url:'/system/base?cmd=find_procj_id&id=<%=p_ids %>',
				columns:[[
				    {field:'ID',title:'ID',width:180, hidden: true,editor:'text'},
				    {field:'INFORMATION',title:'风险问题描述',width:180,editor:'text'},
				    //{field:'INFLUENCE_LEVEL',title:'影响',width:180,editor:'text'},
				    
				    {field:'INFLUENCE_LEVEL',title:'影响',width:100,
						formatter:function(value){
							for(var i=0; i<productses.length; i++){
								if (productses[i].productid == value) return productses[i].name;
							}
							return value;
						},
						editor:{
							type:'combobox',
							options:{
								valueField:'productid',
								textField:'name',
								data:productses,
								required:true
							}
						}
					},
				    {field:'OCCURENCE_TIME',title:'发生时间',width:180,editor:{type:'datetimebox',options:{required:true}}},				    
					{field:'INTERNAL_SOLUTION',title:'项目内部是否可解决',width:100,
						formatter:function(value){
							for(var i=0; i<products.length; i++){
								if (products[i].productid == value) return products[i].name;
							}
							return value;
						},
						editor:{
							type:'combobox',
							options:{
								valueField:'productid',
								textField:'name',
								data:products,
								required:true
							}
						}
					},
					{field:'SOLUTION_MEASURES',title:'解决措施',width:180,editor:'text'},	
					{field:'DEADLINE',title:'最后期限',width:180,editor:{type:'datetimebox',options:{required:true}}},	
					//{field:'RISK_STATUS',title:'风险状态',width:180,editor:'text'},
					
					{field:'RISK_STATUS',title:'风险状态',width:100,
						formatter:function(value){
							for(var i=0; i<risks.length; i++){
								if (risks[i].productid == value) return risks[i].name;
							}
							return value;
						},
						editor:{
							type:'combobox',
							options:{
								valueField:'productid',
								textField:'name',
								data:risks,
								required:true
							}
						}
					},
					
					
					
				//	{field:'listprice',title:'INFLUENCE_LEVEL',width:80,align:'right',editor:{type:'numberbox',options:{precision:1}}},
				//	{field:'unitcost',title:'Unit Cost',width:80,align:'right',editor:'numberbox'},
				//	{field:'attr1',title:'Attribute',width:180,editor:'text'},
				//	{field:'status',title:'Status',width:50,align:'center',
				//		editor:{
				//			type:'checkbox',
				//			options:{
				//				on: 'P',
				//				off: ''
				//			}
				//		}
				//	},
					{field:'action',title:'操作',width:80,align:'center',
						formatter:function(value,row,index){
							if (row.editing){
								var s = '<a href="#" onclick="saverow(this)">保存</a> ';
								var c = '<a href="#" onclick="cancelrow(this)">取消</a>';
								return s+c;
							} else {
								var e = '<a href="#" onclick="editrow(this)">修改</a> ';
								var d = '<a href="#" onclick="deleterow(this)">删除</a>';
								return e+d;
							}
						}
					}
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
	         // row.ID  row.INFORMATION 
	         // row.INFLUENCE_LEVEL 
	         // row.OCCURENCE_TIME 
	         // row.INTERNAL_SOLUTION 
	         // row.SOLUTION_MEASURES	          
	         // row.DEADLINE RISK_STATUS
	         
	         var p_message_id="<%=p_ids %>";
	         
	         if(row.INFLUENCE_LEVEL=="高"){row.INFLUENCE_LEVEL="1";}
	         if(row.INFLUENCE_LEVEL=="中"){row.INFLUENCE_LEVEL="2";}
	         if(row.INFLUENCE_LEVEL=="低"){row.INFLUENCE_LEVEL="2";}
	         
	         if(row.INTERNAL_SOLUTION=="是"){row.INTERNAL_SOLUTION="1";}else{row.INTERNAL_SOLUTION="0";}
	         
	         if(row.RISK_STATUS=="打开"){row.RISK_STATUS="1";}else if(row.RISK_STATUS=="关闭"){row.RISK_STATUS="2";}else{row.RISK_STATUS="3";}
	         
	         var map=[row.INFORMATION,row.INFLUENCE_LEVEL,row.OCCURENCE_TIME,row.INTERNAL_SOLUTION,row.SOLUTION_MEASURES,row.DEADLINE,row.RISK_STATUS,p_message_id];
            //alert(map);
            $.ajax({
			type: "POST",
			url:"/system/base?cmd=save_procj_id&id="+row.ID+"&map="+map,
			//data:{"INFORMATION":row.INFORMATION,"INFLUENCE_LEVEL":row.INFLUENCE_LEVEL,"OCCURENCE_TIME":row.OCCURENCE_TIME,"INTERNAL_SOLUTION":row.INTERNAL_SOLUTION,"SOLUTION_MEASURES":row.SOLUTION_MEASURES,"DEADLINE":row.DEADLINE,"RISK_STATUS":row.RISK_STATUS},		
			dataType: "json",
			async: false,
			cache: false,
			contentType: false,
			processData: false,
			success: function(data) {
				alert("保存成功！");
				location.reload(true);   
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
<body style="padding:0 10px !important;">
	<!-- <div class="demo-info">
		<div class="demo-tip icon-tip">&nbsp;</div>
		<div>Click the edit button on the right side of row to start editing.</div>
	</div> -->
	
	<div style="margin-bottom:10px">
		<a href="#" class="easyui-linkbutton" onclick="insert()">添加行</a>
	</div>
	
	<table id="tt"></table>



<!-- <table class="easyui-datagrid"   fitColumns="true"  title="项目风险问题跟踪"  style="height: 180PX;" 
			data-options="singleSelect:true,collapsible:true,url:'/system/base?cmd=find_procj_id',method:'post',fitColumns:true,singleSelect:true,pagination:true">
		<thead>
			<tr>
				<th data-options="field:'INFORMATION',width:80,halign:'center'">风险描述</th>
				<th data-options="field:'INFLUENCE_LEVEL',width:50,halign:'center'">影响</th>
				<th data-options="field:'OCCURENCE_TIME',width:80,halign:'center'">发生时间</th>
				<th data-options="field:'INTERNAL_SOLUTION',width:80,halign:'center'">项目内部是否可解决</th>
				<th data-options="field:'SOLUTION_MEASURES',width:80,halign:'center'">解决措施</th>
				<th data-options="field:'DEADLINE',width:80,halign:'center'">最后期限</th>
				<th data-options="field:'RISK_STATUS',width:80,halign:'center'">风险状态</th>
			</tr>
		</thead>
	</table> -->
</body>
</html>