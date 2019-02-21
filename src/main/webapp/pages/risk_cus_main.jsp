<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

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
				toolbar:'#tbs',
				url:'/system/cuma?cmd=find_car_message&id=<%=request.getParameter("ParentPKValue")%>&is_no=7',
				columns:[[
				    {field:'ID',title:'ID',width:180, hidden: true,editor:'text'},
				    {field:'MAIN_TASK',title:'主要任务',width:180,editor:'text'},
				    {field:'PERSON_LIABLE',title:'责任人',width:180,editor:'text'},
				    {field:'COMPLETE_TIME',title:'完成时间',width:180,editor:{type:'datetimebox',options:{required:true}}},				    
				    {field:'DELIVERIES',title:'交付物',width:180,editor:'text'},
				    {field:'RESOURCE_REQ',title:'所需资源',width:180,editor:'text'},
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
		    var row = $('#tt').datagrid('getSelected');
            if (row){
             if(row.MAIN_TASK==null){
             $('#tt').datagrid('deleteRow', getRowIndex(target));            
             return ;
            }
            else if(row.MAIN_TASK=="请填写"){
               alert("请选添加或修改一条数据");             
             return ;
            }
            else{           
			var map=[row.ID];
			var cus_id="<%=request.getParameter("ParentPKValue")%>";
			$.ajax({
			type: "POST",
			url:"/system/cuma?cmd=delete_box_message&id="+map+"&cus_id="+cus_id+"&is_no=4",
			//data:{"INFORMATION":row.INFORMATION,"INFLUENCE_LEVEL":row.INFLUENCE_LEVEL,"OCCURENCE_TIME":row.OCCURENCE_TIME,"INTERNAL_SOLUTION":row.INTERNAL_SOLUTION,"SOLUTION_MEASURES":row.SOLUTION_MEASURES,"DEADLINE":row.DEADLINE,"RISK_STATUS":row.RISK_STATUS},		
			dataType: "json",
			async: false,
			cache: false,
			contentType: false,
			processData: false,
			success: function(data) {
			alert("删除成功！");
		    $('#tt').datagrid('deleteRow', getRowIndex(target));
			//location.reload(true);   			
			}, error: function(data) {				
			}
			}); 
			}		
			}
			else{
			   alert("请选中一条数据");
              
               return ;
			}
		}
		//------
		function saverow(target){
			var row = $('#tt').datagrid('getSelected');
            if (row){
            $('#tt').datagrid('endEdit', getRowIndex(target));
	       
	         if(row.ID==""){row.ID="0";}
	         
	         var cus_id="<%=request.getParameter("ParentPKValue")%>";
	         
	         var map=[row.ID,row.MAIN_TASK,row.PERSON_LIABLE,row.COMPLETE_TIME,row.DELIVERIES,row.RESOURCE_REQ,cus_id];
	         var map1=[row.ID,row.MAIN_TASK,row.PERSON_LIABLE,"",row.DELIVERIES,row.RESOURCE_REQ];
           
           //^\d{4}-\d{1,2}-\d{1,2}
            costmessage(row.COMPLETE_TIME,map1);
            
            //alert(map);
           
            //return ;
           
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
		
		
function isPoneAvailables($poneInput,map) {
    var myreg1=/^\d{4}-\d{1,2}-\d{1,2}/;
    
    if (!myreg1.test($poneInput)) {  
       return false;
    } else {
        return true;
    }
}

function costmessage(message,map) {
	if(!isPoneAvailables(message,map)){
		alert("输入格式错误,请输入正确的时间");
		return;
	}
  } 
</script>
</head>
<body>
	<!-- <div class="demo-info">
		<div class="demo-tip icon-tip">&nbsp;</div>
		<div>Click the edit button on the right side of row to start editing.</div>
	</div> -->
	
	<div id="tbs" style="margin:10px 0">
		<a href="#" class="btn btn-primary easyui-linkbutton" onclick="insert()" >添加行</a>
	</div>
	
	<table id="tt"></table>
</body>
</html>