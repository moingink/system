<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<link href="<%=request.getContextPath()%>/vendor/bootstrap/css/bootstrap.css" rel="stylesheet" />
<link href="<%=request.getContextPath()%>/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet" /> 
<link href="<%=request.getContextPath()%>/vendor/bootstrap-table/src/bootstrap-table.css" rel="stylesheet" />  

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>wbs信息</title>
<style>

	.info th{
		text-align:center;
		vertical-align: middle;
	}
	#tbody td{
		text-align:center;
		vertical-align: middle;
	}
.previewtitle {
    color: #31708f;
    padding: 10px 15px;
    font-weight: bold;
	font-size:16px;
}
</style>
</head>
<script src="../../pages/js/reference.js"></script>
<body>
<div class="panel-body">
	<div class="panel panel-primary">
	<div class="col-md-12 previewtitle">wbs信息</div>



<form id="form">
<table id="table" class="table table-bordered">
	<thead>
		<tr class="info">
		<th style="width:60px;">序号</th>
		<th style="min-width:120px;">WBS</th>
		<th style="width:155px;">开始日期</th>
		<th style="width:155px;">计划完成日期</th>
		<th style="width:155px;">实际完成日期</th>
		<th>任务描述</th>
		<th style="width:100px;">责任人</th>
		<th style="width:80px;">工时(/天)</th>
		<th style="width:100px;">状态</th>
		
		</tr>
	</thead>
	<tbody id="tbody">
	</tbody>
</table>
</form>
</div>
</div>

</body>
</html>
<script src="<%=request.getContextPath()%>/vendor/jquery/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/js/public.js"></script>
<script src="<%=request.getContextPath()%>/js/common.js"></script>
<script src="../../js/hide.js"></script>
<script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.min.js"></script>  

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
var selectvar = '<select id="wsbstatue" class="form-control" name="UrbanDepNo" style="width:100%">'+
'<option value="立项">立项</option>'+
'<option value="客户需求确认">客户需求确认</option>'+
'<option value="项目建议书评审完成">项目建议书评审完成</option>'+
'<option value="采购完成">采购完成</option>'+
'<option value="开发设计">开发设计</option>'+
'<option value="实施">实施</option>'+
'<option value="项目初验">项目初验</option>'+
'<option value="客户验收上线">客户验收上线</option></select>';

var bill_statue="";
var currSelecttTr=""; //定义选择行 添加子任务  为空时提示未选择行 
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
      	bill_statue="3";
		$.ajax({  
                url : "/system/project/wbs?proj_source_id="+ParentPKValue,  
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
											var selectvar = '<select id="'+id+'" name="UrbanDepNo" class="form-control" disabled="disabled">'+
											'<option value="立项">立项</option>'+
											'<option value="客户需求确认">客户需求确认</option>'+
											'<option value="项目建议书评审完成">项目建议书评审完成</option>'+
											'<option value="采购完成">采购完成</option>'+
											'<option value="开发设计">开发设计</option>'+
											'<option value="实施">实施</option>'+
											'<option value="项目初验">项目初验</option>'+
											'<option value="客户验收上线">客户验收上线</option></select>';
											t1.innerHTML = selectvar;
											//t2.innerHTML='<input type="text" class="form-control" value="'+data[i].WBS_START_DATE+'" readonly="readonly"/>';
											//t3.innerHTML='<input type="text" class="form-control" value="'+data[i].WBS_EXPECTED_END_DATE+'" readonly="readonly"/>';
											t2.innerHTML='<div class="input-group date" id="date1"><input type="text" class="form-control" value="'+data[i].WBS_START_DATE+'" readonly="readonly"/><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div>';
											t3.innerHTML='<div class="input-group date" id="date1"><input type="text" class="form-control" value="'+data[i].WBS_EXPECTED_END_DATE+'" readonly="readonly"/><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div>';
											//t4.innerHTML='<input type="text" style="width:108%" class="form-control" value="'+data[i].WBS_COMPLETION_TIME+'" />';
											t4.innerHTML='<div class="input-group date" id="date1"><input type="text" class="form-control" value="'+data[i].WBS_COMPLETION_TIME+'" /><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div>';
											t5.innerHTML='<input type="text" class="form-control" value="'+data[i].TASK_REMARK+'"/>';
											t6.innerHTML='<input type="text" class="form-control" value="'+data[i].WBS_THOSE_RESPONSIBLE+'"/>';
											t7.innerHTML='<input type="text" class="form-control" value="'+data[i].WBS_WORKING_HOURS+'"/>';
											var ids= statue+i
											t8.innerHTML='<select id="'+ids+'" name="UrbanDepNo" class="form-control"><option value=""></option><option value="1">正常</option><option value="2">预警</option><option value="3">延期</option></select>';
											t9.innerHTML='<input type="text" class="form-control" style="width:0%;display:none" value="'+data[i].PROJ_PHASE_ID+'"/>';
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
											
											
											$("#"+ids+" option[value='"+statue+"']").attr("selected","selected");
											$("#"+id +" option[value='"+wsbstatue+"']").attr("selected","selected");
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
                url : "/system/project/progress_plan?proj_source_id="+ParentPKValue,  
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
										var selectvar = '<select id="'+id+'" name="UrbanDepNo" class="form-control" style="width:100%" disabled="disabled">'+
										'<option value="立项">立项</option>'+
										'<option value="客户需求确认">客户需求确认</option>'+
										'<option value="项目建议书评审完成">项目建议书评审完成</option>'+
										'<option value="采购完成">采购完成</option>'+
										'<option value="开发设计">开发设计</option>'+
										'<option value="实施">实施</option>'+
										'<option value="项目初验">项目初验</option>'+
										'<option value="客户验收上线">客户验收上线</option></select>';
										t1.innerHTML = selectvar;
										//t2.innerHTML='<input type="text" style="width:108%" value="'+data[i].START_DATE+'" readonly="readonly"/>';
										//t3.innerHTML='<input type="text" style="width:108%" value="'+data[i].EXPECTED_END_DATE+'" readonly="readonly"/>';
										//t4.innerHTML='<input type="text" style="width:108%" value="" />';
										t2.innerHTML='<div class="input-group date" id="date1"><input type="text" class="form-control" value="'+data[i].START_DATE+'" readonly="readonly"/><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div>';
										t3.innerHTML='<div class="input-group date" id="date1"><input type="text" class="form-control" value="'+data[i].EXPECTED_END_DATE+'" readonly="readonly"/><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div>';
										t4.innerHTML='<div class="input-group date" id="date1"><input type="text" class="form-control" value="" /><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div>';
										t5.innerHTML='<input type="text" style="width:100%" class="form-control" value="'+data[i].PHASE_TASK+'"/>';
										t6.innerHTML='<input type="text" style="width:100%" class="form-control" value="'+data[i].PRINCIPAL_NAME+'"/>';
										t7.innerHTML='<input type="text" style="width:100%" class="form-control" value="'+data[i].WBS_WORKING_HOURS+'"/>';
										t8.innerHTML='<select id="statue" class="form-control" name="UrbanDepNo" style="width:100%"><option value=""></option><option value="1">正常</option><option value="2">预警</option><option value="3">延期</option></select>';
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
										
										
										$("#"+id+" option[value='"+wsbstatue+"']").attr("selected","selected");
                            		}
                            }
                       });
	
}
$("body").on("click", "#tbody tr", function (){
	currSelecttTr = $(this).parent().find("tr").index($(this)[0])+1;
});

var saveDate= [];
var data = [];
  	
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