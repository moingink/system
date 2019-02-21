<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
    String ContextPath = request.getContextPath();
    String path=ContextPath+"/vendor/vehicles";
    %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>项目进度计划</title>
</head>

<body>
<button onclick="saveProgressPlan()" id="ins_or_up_buttontoken">保存</button>

<form id="form">
<table border="1" width="90%" id="table">
<thead>
	<tr>
	<td>序号</td>
	<th>里程碑</th>
	<th>开始日期</th>
	<th>计划完成日期</th>
	<th>阶段任务</th>
	<th>交付物</th>
	<th>责任人</th>
	<th>操作</th>
	</tr>
</thead>
<tbody id="tbody">
	<tr>
		<td style="width: 50px"><input type="text" style="width:100%;display: none" value="" />1</td>
		<td><select id="UrbanDepNo0" name="UrbanDepNo" style="width:90%"><option value="1" selected="selected">立项</option><option value="2">客户需求确认</option><option value="3">项目建议书评审完成</option><option value="4">采购完成</option><option value="5">开发设计</option><option value="6">实施</option><option value="7">项目初验</option><option value="6">客户验收上线</option></select></td>
		<td><div class="input-group date" id="date1"><input type="text" class="form-control" /><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div></td>
		<td><div class="input-group date" id="date1"><input type="text" class="form-control" /><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div></td>
		<td><input type="text" style="width:98%"></td>
		<td><input type="text" style="width:98%"></td>
		<td><input type="text" style="width:98%"></td>
		<td style="width: 50px;"><a href="javascript:;" onclick="del(this);">删除</a></td> <!--在删除按钮上添加点击事件 -->
	</tr>
	<tr>
		<td style="width: 50px"><input type="text" style="width:100%;display: none" value="" />2</td>
		<td><select id="UrbanDepNo0" name="UrbanDepNo" style="width:90%"><option value="1">立项</option><option value="2" selected="selected">客户需求确认</option><option value="3">项目建议书评审完成</option><option value="4">采购完成</option><option value="5">开发设计</option><option value="6">实施</option><option value="7">项目初验</option><option value="6">客户验收上线</option></select></td>
		<td><div class="input-group date" id="date1"><input type="text" class="form-control" /><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div></td>
		<td><div class="input-group date" id="date1"><input type="text" class="form-control" /><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div></td>
		<td><input type="text" style="width:98%"></td>
		<td><input type="text" style="width:98%"></td>
		<td><input type="text" style="width:98%"></td>
		<td style="width: 50px;"><a href="javascript:;" onclick="del(this);">删除</a></td> <!--在删除按钮上添加点击事件 -->
	</tr>
	<tr>
		<td style="width: 50px"><input type="text" style="width:100%;display: none" value="" />3</td>
		<td><select id="UrbanDepNo0" name="UrbanDepNo" style="width:90%"><option value="1">立项</option><option value="2">客户需求确认</option><option value="3" selected="selected">项目建议书评审完成</option><option value="4">采购完成</option><option value="5">开发设计</option><option value="6">实施</option><option value="7">项目初验</option><option value="6">客户验收上线</option></select></td>
		<td><div class="input-group date" id="date1"><input type="text" class="form-control" /><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div></td>
		<td><div class="input-group date" id="date1"><input type="text" class="form-control" /><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div></td>
		<td><input type="text" style="width:98%"></td>
		<td><input type="text" style="width:98%"></td>
		<td><input type="text" style="width:98%"></td>
		<td style="width: 50px;"><a href="javascript:;" onclick="del(this);">删除</a></td> <!--在删除按钮上添加点击事件 -->
	</tr>
	<tr>
		<td style="width: 50px"><input type="text" style="width:100%;display: none" value="" />4</td>
		<td><select id="UrbanDepNo0" name="UrbanDepNo" style="width:90%"><option value="1">立项</option><option value="2">客户需求确认</option><option value="3">项目建议书评审完成</option><option value="4" selected="selected">采购完成</option><option value="5">开发设计</option><option value="6">实施</option><option value="7">项目初验</option><option value="6">客户验收上线</option></select></td>
		<td><div class="input-group date" id="date1"><input type="text" class="form-control" /><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div></td>
		<td><div class="input-group date" id="date1"><input type="text" class="form-control" /><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div></td>
		<td><input type="text" style="width:98%"></td>
		<td><input type="text" style="width:98%"></td>
		<td><input type="text" style="width:98%"></td>
		<td style="width: 50px;"><a href="javascript:;" onclick="del(this);">删除</a></td> <!--在删除按钮上添加点击事件 -->
	</tr>
	<tr>
		<td style="width: 50px"><input type="text" style="width:100%;display: none" value="" />5</td>
		<td><select id="UrbanDepNo0" name="UrbanDepNo" style="width:90%"><option value="1">立项</option><option value="2">客户需求确认</option><option value="3">项目建议书评审完成</option><option value="4">采购完成</option><option value="5" selected="selected">开发设计</option><option value="6">实施</option><option value="7">项目初验</option><option value="6">客户验收上线</option></select></td>
		<td><div class="input-group date" id="date1"><input type="text" class="form-control" /><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div></td>
		<td><div class="input-group date" id="date1"><input type="text" class="form-control" /><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div></td>
		<td><input type="text" style="width:98%"></td>
		<td><input type="text" style="width:98%"></td>
		<td><input type="text" style="width:98%"></td>
		<td style="width: 50px;"><a href="javascript:;" onclick="del(this);">删除</a></td> <!--在删除按钮上添加点击事件 -->
	</tr>
	<tr>
		<td style="width: 50px"><input type="text" style="width:100%;display: none" value="" />6</td>
		<td><select id="UrbanDepNo0" name="UrbanDepNo" style="width:90%"><option value="1">立项</option><option value="2">客户需求确认</option><option value="3">项目建议书评审完成</option><option value="4">采购完成</option><option value="5">开发设计</option><option value="6" selected="selected">实施</option><option value="7">项目初验</option><option value="6">客户验收上线</option></select></td>
		<td><div class="input-group date" id="date1"><input type="text" class="form-control" /><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div></td>
		<td><div class="input-group date" id="date1"><input type="text" class="form-control" /><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div></td>
		<td><input type="text" style="width:98%"></td>
		<td><input type="text" style="width:98%"></td>
		<td><input type="text" style="width:98%"></td>
		<td style="width: 50px;"><a href="javascript:;" onclick="del(this);">删除</a></td> <!--在删除按钮上添加点击事件 -->
	</tr>
	<tr>
		<td style="width: 50px"><input type="text" style="width:100%;display: none" value="" />7</td>
		<td><select id="UrbanDepNo0" name="UrbanDepNo" style="width:90%"><option value="1">立项</option><option value="2">客户需求确认</option><option value="3">项目建议书评审完成</option><option value="4">采购完成</option><option value="5">开发设计</option><option value="6">实施</option><option value="7" selected="selected">项目初验</option><option value="6">客户验收上线</option></select></td>
		<td><div class="input-group date" id="date1"><input type="text" class="form-control" /><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div></td>
		<td><div class="input-group date" id="date1"><input type="text" class="form-control" /><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div></td>
		<td><input type="text" style="width:98%"></td>
		<td><input type="text" style="width:98%"></td>
		<td><input type="text" style="width:98%"></td>
		<td style="width: 50px;"><a href="javascript:;" onclick="del(this);">删除</a></td> <!--在删除按钮上添加点击事件 -->
	</tr>
	<tr>
		<td style="width: 50px"><input type="text" style="width:100%;display: none" value="" />8</td>
		<td><select id="UrbanDepNo0" name="UrbanDepNo" style="width:90%"><option value="1">立项</option><option value="2">客户需求确认</option><option value="3">项目建议书评审完成</option><option value="4">采购完成</option><option value="5">开发设计</option><option value="6">实施</option><option value="7">项目初验</option><option value="8" selected="selected">客户验收上线</option></select></td>
		<td><div class="input-group date" id="date1"><input type="text" class="form-control" /><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div></td>
		<td><div class="input-group date" id="date1"><input type="text" class="form-control" /><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div></td>
		<td><input type="text" style="width:98%"></td>
		<td><input type="text" style="width:98%"></td>
		<td><input type="text" style="width:98%"></td>
		<td style="width: 50px;"><a href="javascript:;" onclick="del(this);">删除</a></td> <!--在删除按钮上添加点击事件 -->
	</tr>
</tbody>
</table>
</form>
<input type="button" value="添加一行" onclick="add()" id="add_button"/> <!--在添加按钮上添加点击事件 -->
  
</body>
</html>
<script src="<%=request.getContextPath()%>/vendor/jquery/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/js/public.js"></script>
<script src="<%=request.getContextPath()%>/js/common.js"></script>
<script src="../../js/hide.js"></script>
<script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.min.js"></script>  
<link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet" /> 
<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script> 
<script src="https://cdn.bootcss.com/moment.js/2.18.1/moment-with-locales.min.js"></script>  
<link href="https://cdn.bootcss.com/bootstrap-datetimepicker/4.17.47/css/bootstrap-datetimepicker.min.css" rel="stylesheet" />
<script src="https://cdn.bootcss.com/bootstrap-datetimepicker/4.17.47/js/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript"> 
var context='';
var token='';
var buttonToken ="addForProJect";
//主表主键值
var ParentPKValue = '<%=request.getParameter("ParentPKValue")%>';
var pageCode = '<%=request.getParameter("pageCode")%>';
var keyWord = '<%=request.getParameter("keyWord")%>';//入口判断
var modifyId = '<%=request.getParameter("modifyId")%>';//变更执行申请ID
var modifyFieldName = '<%=request.getParameter("modifyFieldName")%>';//变更执行申请字段名
var isHide = '<%=request.getParameter("isHide")%>';//是否隐藏及置灰
var version_history = '<%=request.getParameter("version_history") %>';
var length = 0;
var bill_statue="";
window.onload = function(){
	context='<%=request.getContextPath()%>';
	token='<%=request.getParameter("token")!=null?request.getParameter("token"):""%>';
	var tr=document.getElementsByTagName("tr");
	for(var i= 0;i<tr.length;i++){
		bgcChange(tr[i]);
	}
	// 鼠标移动改变背景,可以通过给每行绑定鼠标移上事件和鼠标移除事件来改变所在行背景色。
	var tab=document.getElementById("tbody");
	//初始化界面
	var fill = new Object();
	if(keyWord == 1){//iframe
		$.ajax({
		    		async: false,
		            type: "get",
		            url: context+"/project?cmd=find_selectTab",
		            data: {"modifyId":modifyId,"modifyFieldName":modifyFieldName},
		            dataType: "json",
		            success: function(data){
		            	console.log(data);
		            	fill = data;
		            }
		        });
		 $("#ins_or_up_buttontoken").css("display","none");
	}
	if($.isEmptyObject(fill) == true){
		$.ajax({  
               url : "/system/projectHistory/progress_plan?proj_released_id="+ParentPKValue+"&version_history="+version_history,  
               dataType : "json",  
                           type : "GET",  
                           async: false, 
                           success : function(data) {
                           	console.log(data);
                           	fill = data;
                           }
        });
        //获取预算主单单据状态
        $.ajax({  
              url : "/system/projectHistory/proposalStatue?proj_released_id="+ParentPKValue+"&version_history="+version_history,  
              dataType : "json",  
                          type : "GET",  
                          async: false, 
                          success : function(data) {
	                      	if(data.length>0){
			            		bill_statue = data[0].BILL_STATUS;
			            		//bill_statue="3"
			            	}
                          }
        });
        
	}
    		//如果有数据则回显数据
    		if($.isEmptyObject(fill) == false){
    			buttonToken ="deleteForProJect";
    			$("#tbody").html("");
    		}
    		length = fill.length;
    		for(var i=0;i<fill.length;i++){
    			//循环表格数据动态添加行列信息
    			var tr=document.createElement("tr");
    			var t0=document.createElement("td");
				var t1=document.createElement("td");
				var t2=document.createElement("td");
				var t3=document.createElement("td");
				var t4=document.createElement("td");
				var t5=document.createElement("td");
				var t6=document.createElement("td");
				t0.innerHTML = '<input type="text"  style="width:100%;display:none" value="'+fill[i].ID+'" />'+(Number(i)+1);
				var id= "select"+i;
				var selectvar = '<select id="'+id+'" name="UrbanDepNo" style="width:90%">'+
				'<option value="1">立项</option>'+
				'<option value="2">客户需求确认</option>'+
				'<option value="3">项目建议书评审完成</option>'+
				'<option value="4">采购完成</option>'+
				'<option value="5">开发设计</option>'+
				'<option value="6">实施</option>'+
				'<option value="7">项目初验</option>'+
				'<option value="8">客户验收上线</option></select>';
				var wsbstatue = fill[i].MILESTONE;
				t1.innerHTML = selectvar;
				//t2.innerHTML='<input type="text" style="width:98%" value="'+fill[i].START_DATE+'"/>';
				t2.innerHTML='<div class="input-group date" id="date1"><input type="text" class="form-control" value="'+fill[i].START_DATE+'" /><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div>';
				t3.innerHTML='<div class="input-group date" id="date1"><input type="text" class="form-control" value="'+fill[i].EXPECTED_END_DATE+'" /><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div>';
				t4.innerHTML='<input type="text" style="width:98%" value="'+fill[i].PHASE_TASK+'" />';
				t5.innerHTML='<input type="text" style="width:98%" value="'+fill[i].DELIVERABLES+'"/>';
				t6.innerHTML='<input type="text" style="width:98%" value="'+fill[i].PRINCIPAL_NAME+'"/>';
				var del=document.createElement("td");
				del.style="width: 50px";
				if(bill_statue!=""){
				 	if(bill_statue !="0" && bill_statue!= "7"){
						del.innerHTML="";
					}else{
						del.innerHTML="<a href='javascript:;' onclick='del(this,\""+fill[i].ID+"\")' >删除</a>";
					}
			 	}
				tab.appendChild(tr);
				tr.appendChild(t0);
				tr.appendChild(t1);
				tr.appendChild(t2);
				tr.appendChild(t3);
				tr.appendChild(t4);
				tr.appendChild(t5);
				tr.appendChild(t6);
				tr.appendChild(del);
				$("#"+id+" option[value='"+wsbstatue+"']").attr("selected","selected");
				$('.date').datetimepicker({  
				    format: 'YYYY-MM-DD',  
				    locale: moment.locale('zh-cn')  
				}); 
	         }
	         if(keyWord == 1){//iframe
		     	$('a').removeAttr('onclick');
		     	$('a').removeAttr('href');
			 }
			 if(keyWord != 1){
			 	if(bill_statue!=""){
				 	if(bill_statue !="0" && bill_statue!= "7"){
						$("#ins_or_up_buttontoken").attr("disabled", true);
						$("#add_button").attr("disabled", true);
						$(":input").each(function(){
							$(this).attr("disabled",true);
						});
						$(":select").each(function(){
							$(this).attr("disabled",true);
						});
					}
				 }
			 }
			 hide(isHide);//隐藏及置灰
} 
function bgcChange(obj){
	obj.onmouseover=function(){
		obj.style.backgroundColor="#f2f2f2";
	}
	obj.onmouseout=function(){
		obj.style.backgroundColor="#fff";
	}
}

// 编写一个函数，供添加按钮调用，动态在表格的最后一行添加子节点；
var selectvar = '<select id="" name="UrbanDepNo" style="width:90%">'+
	'<option value="1">立项</option>'+
	'<option value="2">客户需求确认</option>'+
	'<option value="3">项目建议书评审完成</option>'+
	'<option value="4">采购完成</option>'+
	'<option value="5">开发设计</option>'+
	'<option value="6">实施</option>'+
	'<option value="7">项目初验</option>'+
	'<option value="8">客户验收上线</option></select>';
function add(){
	length = length +1;
	var tr=document.createElement("tr");
	var t0=document.createElement("td");
	var t1=document.createElement("td");
	var t2=document.createElement("td");
	var t3=document.createElement("td");
	var t4=document.createElement("td");
	var t5=document.createElement("td");
	var t6=document.createElement("td");
	t0.innerHTML='<input type="text" style="width:100%;display: none" value="" />'+length;
	t1.innerHTML=selectvar;
	t2.innerHTML='<div class="input-group date" id="date1"><input type="text" class="form-control" /><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div>';
	t3.innerHTML='<div class="input-group date" id="date1"><input type="text" class="form-control" /><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div>';
	t4.innerHTML='<input type="text" style="width:98%" />';
	t5.innerHTML='<input type="text" style="width:98%" />';
	t6.innerHTML='<input type="text" style="width:98%" />';
	var del=document.createElement("td");
	del.innerHTML="<a href='javascript:;' onclick='del(this)' >删除</a>";
	var tab=document.getElementById("table");
	tab.appendChild(tr);
	tr.appendChild(t0);
	tr.appendChild(t1);
	tr.appendChild(t2);
	tr.appendChild(t3);
	tr.appendChild(t4);
	tr.appendChild(t5);
	tr.appendChild(t6);
	tr.appendChild(del);
	var tr = document.getElementsByTagName("tr");
	for(var i= 0;i<tr.length;i++){
		bgcChange(tr[i]);
	}
	$('.date').datetimepicker({  
				    format: 'YYYY-MM-DD',  
				    locale: moment.locale('zh-cn')  
				}); 
}


// 创建删除函数
function del(obj,id){
	var j = {};
    var saveDate= [];
	if(typeof(id)==""||typeof(id)=="undefined"){
    	var tr=obj.parentNode.parentNode;
		tr.parentNode.removeChild(tr);
    }else{
	    if(window.confirm('确定删除？')){
	             	//alert("确定");
	                //调用删除方法
	                buttonToken="deleteForProJectA";
	                j.ID = id;
	                saveDate.push(j);
	                console.log(saveDate);
	                var datamess = JSON.stringify(saveDate);
			        var message = transToServer(findBusUrlByButtonTonken(buttonToken,'&table='+pageCode,''),datamess);
			    	if(message=="删除成功"){
				    	var tr=obj.parentNode.parentNode;
						tr.parentNode.removeChild(tr);
						//刷新页面
						location.reload();
			    	}
	                 return true;
	              }else{
	                 //alert("取消");
	                 return false;
	             }
    }
}

var saveDate= [];
var data = [];

function jsonValue(){
	var j = {};
 	var  arr= document.getElementById('form').getElementsByTagName("*");
 	var l = arr.length;
	for(var i=0;i<l;i++){
	    if(arr[i].tagName && (arr[i].tagName=="INPUT" || arr[i].tagName=="SELECT")){
	    	data.push(arr[i].value);
	    }
 	}
	for(var k=0; k*7 < data.length;k++){
 		var j = {};
 		j.ID=data[k*7];
  		j.MILESTONE = data[k*7+1];
	  	j.START_DATE = data[k*7+2];
	  	j.EXPECTED_END_DATE = data[k*7+3];
	  	j.PHASE_TASK = data[k*7+4];
	  	j.DELIVERABLES = data[k*7+5];
	  	j.PRINCIPAL_NAME = data[k*7+6];
 		j.PROJ_SOURCE_ID = ParentPKValue;
	  	j.DR='0';
	  	j.PROJ_TYPE = '0';
  		saveDate.push(j);
  	} 
  	return saveDate;
}
function saveProgressPlan() {    
	saveDate = jsonValue();
  	console.log(saveDate);
  	if(buttonToken=="deleteForProJect"){
		var dataMessage =saveDate;
	  	//alert(dataMessage);
	  	dataMessage=JSON.stringify(dataMessage);
	  	//alert(dataMessage)
	  	message = transToServer(findBusUrlByButtonTonken(buttonToken,'&table='+pageCode,''),dataMessage);
	  	console.log(message);
  	}
  	
  	buttonToken = "addForProJect";
  	if(buttonToken == "addForProJect"){
  		var dataMessage =saveDate;
  		dataMessage=JSON.stringify(dataMessage);
  		message = transToServer(findBusUrlByButtonTonken(buttonToken,'&table='+pageCode+"&button="+buttonToken,''),dataMessage);
  		//console.log(message);
  	};
  	saveDate=[];
  	if(message=="保存成功"){
	  	alert('保存成功');
	  	location.reload();
  		//$("#ins_or_up_buttontoken").attr("disabled", true);
  	}else{
  		alert('保存失败');
  	}
}  
$('.date').datetimepicker({  
				    format: 'YYYY-MM-DD',  
				    locale: moment.locale('zh-cn')  
				}); 
</script> 

	
	
