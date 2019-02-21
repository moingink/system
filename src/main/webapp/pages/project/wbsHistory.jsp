<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<button onclick="saveWbs()" id="ins_or_up_buttontoken">保存</button>
<form id="form">
<table border="1" width="100%" id="table">
	<thead>
		<tr>
		<th>序号</th>
		<th>WBS</th>
		<th>开始日期</th>
		<th>计划完成日期</th>
		<th>实际完成日期</th>
		<th>任务描述</th>
		<th>责任人</th>
		<th>工时(/天)</th>
		<th style="width: 60px;">状态</th>
		<th style="width: 50px;">操作</th>
		</tr>
	</thead>
	<tbody id="tbody">
		<!-- <tr>
			<td><select id="UrbanDepNo0" name="UrbanDepNo" style="width:100%"><option value="1">立项</option><option value="2">客户需求确认</option><option value="3">项目建议书评审完成</option><option value="4">采购完成</option><option value="5">开发设计</option><option value="6">实施</option><option value="7">项目初验</option><option value="6">客户验收上线</option></select></td>
			<td><div class="col-md-8"><div class="input-group date form_date" style="width:1010%" data-date="" data-date-format="dd MM yyyy" data-link-field="RELEASED_DATE" data-link-format="yyyy-mm-dd"> <input class="form-control" size="10" type="text" readonly="" id="RELEASED_DATE" value=""> <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span> <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span> </div></div></td>
			<td><input type="text" style="width:108%"></td>
			<td><input type="text" style="width:108%"></td>
			<td><input type="text" style="width:108%"></td>
			<td><input type="text" style="width:108%"></td>
			<td><input type="text" style="width:108%"></td>
			<td><select id="UrbanDepNo0" name="UrbanDepNo" style="width:100%"><option value="0"></option><option value="1">正常</option><option value="2">预警</option><option value="3">延期</option></select></td>
			<td><a href="javascript:;" onclick="del(this);">删除</a></td> 在删除按钮上添加点击事件
		</tr> -->
	</tbody>
</table>
</form>
<input type="button" value="添加一行" onclick="add()" id = "add_button" /> <!--在添加按钮上添加点击事件 -->
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
var length = 0;
//主表主键值
var ParentPKValue = '<%=request.getParameter("ParentPKValue")%>';
var pageCode = '<%=request.getParameter("pageCode")%>';
var isHide = '<%=request.getParameter("isHide")%>';//是否隐藏及置灰
var version_history='<%=request.getParameter("version_history") %>';//历史版本号

var selectvar = '<select id="wsbstatue" name="UrbanDepNo" style="width:100%">'+
'<option value="1">立项</option>'+
'<option value="2">客户需求确认</option>'+
'<option value="3">项目建议书评审完成</option>'+
'<option value="4">采购完成</option>'+
'<option value="5">开发设计</option>'+
'<option value="6">实施</option>'+
'<option value="7">项目初验</option>'+
'<option value="8">客户验收上线</option></select>';

var bill_statue="";
window.onload = function(){
	//location.reload();
	context='<%=request.getContextPath()%>';
	token='<%=request.getParameter("token")!=null?request.getParameter("token"):""%>';
	
	var tr=document.getElementsByTagName("tr");
	for(var i= 0;i<tr.length;i++){
		bgcChange(tr[i]);
	}
	// 鼠标移动改变背景,可以通过给每行绑定鼠标移上事件和鼠标移除事件来改变所在行背景色。
	
	//先获取wbs数据表 如果无数据 加载进度计划表数据
		var tab=document.getElementById("tbody");
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
		$.ajax({  
                url : "/system/projectHistory/wbs?proj_released_id="+ParentPKValue+"&version_history="+version_history,  
                dataType : "json",  
                            type : "GET",  
                            async: true, 
                            success : function(data) {
                            	console.log(data);
                            	//如果有数据则回显数据
                            		if(data.length>0){
                            			buttonToken ="deleteForProJect";
                            			$("#tbody").html("");
                            			length = data.length;
	                            		for(var i=0;i<data.length;i++){
	                            			//循环表格数据动态添加行列信息
	                            			var tr=document.createElement("tr");
	                            			var t0=document.createElement("td");
											var t1=document.createElement("td");
											var t2=document.createElement("td");
											var t3=document.createElement("td");
											var t4=document.createElement("td");
											var t5=document.createElement("td");
											var t6=document.createElement("td");
											var t7=document.createElement("td");
											var t8=document.createElement("td");
											var t9=document.createElement("td");
											t0.innerHTML='<input type="text" style="width:100%;display: none" value="'+data[i].ID+'" />'+(Number(i)+1);
											var id= "select"+i;
											var selectvar = '<select id="'+id+'" name="UrbanDepNo" style="width:100%" disabled="disabled">'+
												'<option value="1">立项</option>'+
												'<option value="2">客户需求确认</option>'+
												'<option value="3">项目建议书评审完成</option>'+
												'<option value="4">采购完成</option>'+
												'<option value="5">开发设计</option>'+
												'<option value="6">实施</option>'+
												'<option value="7">项目初验</option>'+
												'<option value="8">客户验收上线</option></select>';
											t1.innerHTML = selectvar;
											//t2.innerHTML='<input type="text" style="width:108%" value="'+data[i].WBS_START_DATE+'" readonly="readonly"/>';
											//t3.innerHTML='<input type="text" style="width:108%" value="'+data[i].WBS_EXPECTED_END_DATE+'" readonly="readonly"/>';
											t2.innerHTML='<div class="input-group date" id="date1"><input type="text" class="form-control" value="'+data[i].WBS_START_DATE+'" readonly="readonly"/><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div>';
											t3.innerHTML='<div class="input-group date" id="date1"><input type="text" class="form-control" value="'+data[i].WBS_EXPECTED_END_DATE+'" readonly="readonly"/><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div>';
											//t4.innerHTML='<input type="text" style="width:108%" value="'+data[i].WBS_COMPLETION_TIME+'" />';
											t4.innerHTML='<div class="input-group date" id="date1"><input type="text" class="form-control" value="'+data[i].WBS_COMPLETION_TIME+'" /><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div>';
											t5.innerHTML='<input type="text" style="width:108%" value="'+data[i].TASK_REMARK+'"/>';
											t6.innerHTML='<input type="text" style="width:108%" value="'+data[i].WBS_THOSE_RESPONSIBLE+'"/>';
											t7.innerHTML='<input type="text" style="width:108%" value="'+data[i].WBS_WORKING_HOURS+'"/>';
											t8.innerHTML='<select id="statue" name="UrbanDepNo" style="width:100%"><option value=""></option><option value="1">正常</option><option value="2">预警</option><option value="3">延期</option></select>';
											t9.innerHTML='<input type="text" style="width:0%;display:none" value="'+data[i].PROJ_PHASE_ID+'"/>';
											var statue = data[i].WBS_BILL_STATE;
											var wsbstatue = data[i].WBS_NAME;
											var del=document.createElement("td");
											del.innerHTML="";
											tab.appendChild(tr);
											tr.appendChild(t0);
											tr.appendChild(t1);
											tr.appendChild(t2);
											tr.appendChild(t3);
											tr.appendChild(t4);
											tr.appendChild(t5);
											tr.appendChild(t6);
											tr.appendChild(t7);
											tr.appendChild(t8);
											tr.appendChild(t9);
											tr.appendChild(del);
											$("#statue option[value='"+statue+"']").attr("selected","selected");
											$("#"+id+" option[value='"+wsbstatue+"']").attr("selected","selected");
											$('.date').datetimepicker({  
											    format: 'YYYY-MM-DD',  
											    locale: moment.locale('zh-cn')  
											}); 
	                            		}
	                            		
                            		}else{
                            			loadProcessPlanData();
                            		}
                            		//置灰
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
                       });
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
function  loadProcessPlanData(){
	var tab=document.getElementById("tbody");
	$.ajax({  
                url : "/system/projectHistory/progress_plan?proj_released_id="+ParentPKValue+"&version_history="+version_history,    
                dataType : "json",  
                            type : "GET",  
                            async: true, 
                            success : function(data) {
                            	//如果有数据则回显数据
                            		if(data.length>0){
                            			//buttonToken ="deleteForProJect";
                            			$("#tbody").html("");
                            			length=data.length;
                            		}
                            		for(var i=0;i<data.length;i++){
                            			//循环表格数据动态添加行列信息
                            			var tr=document.createElement("tr");
                            			var t0=document.createElement("td");
										var t1=document.createElement("td");
										var t2=document.createElement("td");
										var t3=document.createElement("td");
										var t4=document.createElement("td");
										var t5=document.createElement("td");
										var t6=document.createElement("td");
										var t7=document.createElement("td");
										var t8=document.createElement("td");
										var t9=document.createElement("td");
										t0.innerHTML='<input type="text" style="width:100%;display: none" value="" />'+(Number(i)+1);
										var id= "select"+i;
										var selectvar = '<select id="'+id+'" name="UrbanDepNo" style="width:100%" disabled="disabled">'+
												'<option value="1">立项</option>'+
												'<option value="2">客户需求确认</option>'+
												'<option value="3">项目建议书评审完成</option>'+
												'<option value="4">采购完成</option>'+
												'<option value="5">开发设计</option>'+
												'<option value="6">实施</option>'+
												'<option value="7">项目初验</option>'+
												'<option value="8">客户验收上线</option></select>';
										t1.innerHTML = selectvar;
										//t2.innerHTML='<input type="text" style="width:108%" value="'+data[i].START_DATE+'" readonly="readonly"/>';
										//t3.innerHTML='<input type="text" style="width:108%" value="'+data[i].EXPECTED_END_DATE+'" readonly="readonly"/>';
										//t4.innerHTML='<input type="text" style="width:108%" value="" />';
										t2.innerHTML='<div class="input-group date" id="date1"><input type="text" class="form-control" value="'+data[i].START_DATE+'" readonly="readonly"/><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div>';
										t3.innerHTML='<div class="input-group date" id="date1"><input type="text" class="form-control" value="'+data[i].EXPECTED_END_DATE+'" readonly="readonly"/><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div>';
										t4.innerHTML='<div class="input-group date" id="date1"><input type="text" class="form-control" value="" /><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div>';
										t5.innerHTML='<input type="text" style="width:108%" value="'+data[i].PHASE_TASK+'"/>';
										t6.innerHTML='<input type="text" style="width:108%" value="'+data[i].PRINCIPAL_NAME+'"/>';
										t7.innerHTML='<input type="text" style="width:108%" value="'+data[i].WBS_WORKING_HOURS+'"/>';
										t8.innerHTML='<select id="statue" name="UrbanDepNo" style="width:100%"><option value=""></option><option value="1">正常</option><option value="2">预警</option><option value="3">延期</option></select>';
										t9.innerHTML='<input type="text" style="width:0%;display:none" value="'+data[i].ID+'"/>';
										var del=document.createElement("td");
										var wsbstatue = data[i].MILESTONE;
										del.innerHTML="";
										tab.appendChild(tr);
										tr.appendChild(t0);
										tr.appendChild(t1);
										tr.appendChild(t2);
										tr.appendChild(t3);
										tr.appendChild(t4);
										tr.appendChild(t5);
										tr.appendChild(t6);
										tr.appendChild(t7);
										tr.appendChild(t8);
										tr.appendChild(t9);
										tr.appendChild(del);
										$("#"+id+" option[value='"+wsbstatue+"']").attr("selected","selected");
                            		}
                            }
                       });
	
}
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
	var t7=document.createElement("td");
	var t8=document.createElement("td");
	t0.innerHTML=length;
	t1.innerHTML=selectvar;
	t2.innerHTML='<div class="input-group date" id="date1"><input type="text" class="form-control" /><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div>';
	t3.innerHTML='<div class="input-group date" id="date1"><input type="text" class="form-control" /><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div>';
	t4.innerHTML='<div class="input-group date" id="date1"><input type="text" class="form-control" /><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div>';
	t5.innerHTML='<input type="text" style="width:108%" />';
	t6.innerHTML='<input type="text" style="width:108%" />';
	t7.innerHTML='<input type="text" style="width:108%" />';
	t8.innerHTML='<select id="UrbanDepNo0" name="UrbanDepNo" style="width:100%"><option value="0"></option><option value="1">正常</option><option value="2">预警</option><option value="3">延期</option></select>';
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
	tr.appendChild(t7);
	tr.appendChild(t8);
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
function del(obj)
{
var tr=obj.parentNode.parentNode;
tr.parentNode.removeChild(tr);
}
var saveDate= [];
var data = [];
function saveWbs() {    
	var j = {};
 	var  arr= document.getElementById('form').getElementsByTagName("*");
 	var l = arr.length;
	for(var i=0;i<l;i++){
	    if(arr[i].tagName && (arr[i].tagName=="INPUT" || arr[i].tagName=="SELECT")){
	    	data.push(arr[i].value);
	    }
 	}
	for(var k=0; k*10 < data.length;k++){
 		var j = {};
 		j.ID=data[k*10];
  		j.WBS_NAME = data[k*10+1];
	  	j.WBS_START_DATE = data[k*10+2];
	  	j.WBS_EXPECTED_END_DATE = data[k*10+3];
	  	if(data[k*10+4]!=""){
		  	j.WBS_COMPLETION_TIME = data[k*10+4];
	  	}
	  	j.TASK_REMARK = data[k*10+5];
	  	j.WBS_THOSE_RESPONSIBLE = data[k*10+6];
	  	j.WBS_WORKING_HOURS = data[k*10+7];
	  	j.WBS_BILL_STATE = data[k*10+8];
	  	//j.PROJ_PHASE_ID = data[k*10+9];
 		j.PROJ_SOURCE_ID = ParentPKValue;
	  	j.DR='0';
	  	j.PROJ_TYPE = '0';
  		saveDate.push(j);
  	} 	
  	console.log(saveDate);
  	
  	//判断是否先删除在插入
  	if(buttonToken=="deleteForProJect"){
		var dataMessage =saveDate;
	  	//alert(dataMessage);
	  	dataMessage=JSON.stringify(dataMessage);
	  	//alert(dataMessage)
	  	message = transToServer(findBusUrlByButtonTonken(buttonToken,'&table='+pageCode,''),dataMessage);
	  	//console.log("删除消息："+message);
  	}
  	//buttonToken = "addForProJect";
  	//if(buttonToken == "addForProJect"){
  		//var dataMessage =saveDate;
  		//dataMessage=JSON.stringify(dataMessage);
  		//message = transToServer(findBusUrlByButtonTonken(buttonToken,'&table='+pageCode+"&button="+buttonToken,''),dataMessage);
  		//console.log(message);
  	//};
  	saveDate=[];
  	alert('保存成功');
  	$("#ins_or_up_buttontoken").attr("disabled", true);
}   
//getWorkDays();
function getWorkDays(){
var startDate = new Date("2018-07-09");  
//结束日期
var endDate = new Date("2018-07-18");  
	   var diffDays = (endDate - startDate)/(1000*60*60*24) + 1;//获取日期之间相差的天数
	   var remainDay = diffDays % 7;//得到日期之间的余数（0-6之间）
	   var weeks = Math.floor(diffDays / 7);//获取日期之间有多少周
	   var weekends = 2 * weeks;//计算每周*2 得到取整的的周六日天数
	   var weekDay = startDate.getDay();//获取开始日期为星期几（0，1，2，3，4，5，6）0对应星期日
	   for(var i = 0;i < remainDay;i++){//循环处理余下的天数有多少个周六或者周日（最多出现一个周六或者一个周日）
				if(((weekDay + i)==6)||((weekDay + i)==0)||((weekDay + i)==7)){
					weekends = weekends + 1;
				}
			}
	  // alert((diffDays-weekends));//工作日=相差天数减去周六日天数
}

</script> 