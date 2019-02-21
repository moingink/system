<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<link href="<%=request.getContextPath()%>/vendor/bootstrap/css/bootstrap.css" rel="stylesheet" />
<link href="<%=request.getContextPath()%>/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet" /> 
<link href="<%=request.getContextPath()%>/vendor/bootstrap-table/src/bootstrap-table.css" rel="stylesheet" />  

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>风险问题跟踪</title>
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
<body>
<div class="panel-body">
	<div class="panel panel-primary">
	<div class="col-md-12 previewtitle">风险问题跟踪</div>


<form id="form">
	<table id="table" class="table table-bordered">
	<thead>
		<tr class="info">
			<th style="width:60px;">序号</th>
			<th>风险描述</th>
			<th style="width:80px;">影响程度</th>
			<th>发生时间</th>
			<th style="width:150px;">项目内部是否可解决</th>
			<th>解决措施</th>
			<th>最后期限</th>
			<th>风险状态</th>
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
//主表主键值
var ParentPKValue = '<%=request.getParameter("ParentPKValue")%>';
var pageCode = '<%=request.getParameter("pageCode")%>';
var keyWord = '<%=request.getParameter("keyWord")%>';//入口判断
var modifyId = '<%=request.getParameter("modifyId")%>';//变更执行申请ID
var modifyFieldName = '<%=request.getParameter("modifyFieldName")%>';//变更执行申请字段名
var isHide = '<%=request.getParameter("isHide")%>';//是否隐藏及置灰
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
	var fill = new Object();
	if(keyWord == 1){//iframe
		$.ajax({
		    		async: false,
		            type: "get",
		            url: context+"/project?cmd=find_selectTab",
		            data: {"modifyId":modifyId,"modifyFieldName":modifyFieldName},
		            dataType: "json",
		            success: function(data){
		            	fill = data;
		            }
		        });
		 $("#ins_or_up_buttontoken").css("display","none");
	}
	if($.isEmptyObject(fill) == true){
		$.ajax({  
                url : "/system/project/risk?proj_source_id="+ParentPKValue,  
                dataType : "json",  
                            type : "GET",  
                            async: false, 
                            success : function(data) {
                            	fill = data;
                            }
                       });
        //获取预算主单单据状态
        bill_statue="3";
	}
       	//如果有数据则回显数据
       	//console.log(data);
       if($.isEmptyObject(fill) == false){
           buttonToken ="deleteForProJect";
           $("#tbody").html("");
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
				var t7=document.createElement("td");
				t7.style="width: 80px";
				t0.innerHTML='<input type="text" style="width:100%;display: none" value="'+fill[i].ID+'" />'+(Number(i)+1);
				t1.innerHTML ='<input type="text" class="form-control" value="'+fill[i].INFORMATION+'"/>';
				var id= "select"+i;
				var fxstatue = fill[i].INFLUENCE_LEVEL;
				t2.innerHTML='<select id="'+id+'" name="UrbanDepNo" class="form-control"><option value="1">高</option><option value="2">中</option><option value="3">低</option></select>';
				//t3.innerHTML='<input type="text" style="width:98%" value="'+fill[i].OCCURENCE_TIME+'"/>';
				t3.innerHTML='<div class="input-group date" id="date1"><input type="text" class="form-control" value="'+fill[i].OCCURENCE_TIME+'" id ="last_date"/><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div>';
				var ida = "statue"+i;
				t4.innerHTML='<select id="'+ida+'" name="UrbanDepNo" class="form-control"><option value="1">是</option><option value="2">否</option></select>';
				var statue = fill[i].INTERNAL_SOLUTION;
				
				t5.innerHTML='<input type="text" class="form-control" value="'+fill[i].SOLUTION_MEASURES+'"/>';
				//t6.innerHTML='<input type="text" style="width:98%" value="'+fill[i].DEADLINE+'"/>';
				t6.innerHTML='<div class="input-group date" id="date1"><input type="text" class="form-control" value="'+fill[i].DEADLINE+'" id ="last_date"/><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div>';
				//t6.innerHTML= '<div class="col-md-8"><div class="input-group date form_date" style="width:99%" data-date="" data-date-format="dd MM yyyy" data-link-field="RELEASED_DATE" data-link-format="yyyy-mm-dd"> <input class="form-control" size="10" type="text" readonly="" id="RELEASED_DATE" value=""> <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span> <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span> </div></div>'
				var idb = "zt"+i;
				t7.innerHTML='<select id="'+idb+'" name="riskStatue" class="form-control"><option value="0"></option><option value="1">开启</option><option value="2">关闭</option><option value="3">风险转问题</option></select>';
				var tat = fill[i].RISK_STATUS;
				//$("#select option[value='3']").attr("selected","selected");
				tab.appendChild(tr);
				tr.appendChild(t0);
				tr.appendChild(t1);
				tr.appendChild(t2);
				tr.appendChild(t3);
				tr.appendChild(t4);
				tr.appendChild(t5);
				tr.appendChild(t6);
				tr.appendChild(t7);
				$("#"+idb+" option[value='"+tat+"']").attr("selected","selected");
				$("#"+ida+" option[value='"+statue+"']").attr("selected","selected");
				$("#"+id+" option[value='"+fxstatue+"']").attr("selected","selected");
				$('.date').datetimepicker({  
				    format: 'YYYY-MM-DD',  
				    locale: moment.locale('zh-cn')  
				}).on('hide',function(e) {  
	                $('#enterpriseInfoForm').data('bootstrapValidator')  
	                    .updateStatus('last_date', 'NOT_VALIDATED',null)  
	                    .validateField('last_date');  
	            });
             }
          }else{
          }
          if(keyWord == 1){//iframe
	     	$('a').removeAttr('onclick');
	     	$('a').removeAttr('href');
		 }
		 //置灰
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
	t0.innerHTML='<input type="text" style="width:100%;display: none" value="" />'+length;
	t1.innerHTML='<input type="text" style="width:98%" />';
	t2.innerHTML='<select id="s" name="UrbanDepNo" style="width:90%"><option value="1">高</option><option value="2">中</option><option value="3">低</option></select>';
	t3.innerHTML='<div class="input-group date" id="date1"><input type="text" class="form-control" value="" id ="last_date"/><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div>';
	t4.innerHTML='<select id="UrbanDepNo0" name="UrbanDepNo" style="width:90%"><option value="1">是</option><option value="2">否</option></select>';
	t5.innerHTML='<input type="text" style="width:98%" />';
	t6.innerHTML='<div class="input-group date" id="date1"><input type="text" class="form-control" value="" id ="last_date" /><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div>';
	t7.innerHTML='<select id="" name="riskStatue" style="width:90%"><option value="0"></option><option value="1">开启</option><option value="2">关闭</option><option value="3">风险转问题</option></select>';
	var tab=document.getElementById("tbody");
	tab.appendChild(tr);
	tr.appendChild(t0);
	tr.appendChild(t1);
	tr.appendChild(t2);
	tr.appendChild(t3);
	tr.appendChild(t4);
	tr.appendChild(t5);
	tr.appendChild(t6);
	tr.appendChild(t7);
	var tr = document.getElementsByTagName("tr");
	for(var i= 0;i<tr.length;i++){
		bgcChange(tr[i]);
	}
	$('.date').datetimepicker({  
		format: 'YYYY-MM-DD',  
		locale: moment.locale('zh-cn')  
	}).on('hide',function(e) {  
                $('#enterpriseInfoForm').data('bootstrapValidator')  
                    .updateStatus('last_date', 'NOT_VALIDATED',null)  
                    .validateField('last_date');  
            });  
}


// 创建删除函数
function del(obj){
	var tr=obj.parentNode.parentNode;
	tr.parentNode.removeChild(tr);
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
	for(var k=0; k*8< data.length;k++){
 		var j = {};
 		j.ID=data[k*8];
  		j.INFORMATION = data[k*8+1];
	  	j.INFLUENCE_LEVEL = data[k*8+2];
	  	j.OCCURENCE_TIME = data[k*8+3];
	  	j.INTERNAL_SOLUTION = data[k*8+4];
	  	j.SOLUTION_MEASURES = data[k*8+5];
	  	j.DEADLINE = data[k*8+6];
	  	j.RISK_STATUS = data[k*8+7];
 		j.PROJ_SOURCE_ID = ParentPKValue;
	  	j.DR='0';
	  	j.PROJ_TYPE = '0';
  		saveDate.push(j);
  	}
  	
  	data = [];
  	return saveDate;
}


function saveRisk() {
	saveDate = jsonValue();
	
	for(var s=0;s<saveDate.length;s++){
		console.log(saveDate[s].START_DATE=='');
		if(saveDate[s].OCCURENCE_TIME==''||saveDate[s].DEADLINE==''){
			alert('发生时间和最后期限不能为空');
			saveDate=[];
			return ;
		}
	}
		//判断是否先删除在插入
  	if(buttonToken=="deleteForProJect"){
		var dataMessage =saveDate;
	  	dataMessage=JSON.stringify(dataMessage);
	  	message = transToServer(findBusUrlByButtonTonken(buttonToken,'&table='+pageCode,''),dataMessage);
	  	//console.log(message);
  	}
  	buttonToken = "addForProJect";
  		if(buttonToken == "addForProJect"){
  		var dataMessage =saveDate;
  		dataMessage=JSON.stringify(dataMessage);
  		message = transToServer(findBusUrlByButtonTonken(buttonToken,'&table='+pageCode+"&button="+buttonToken,''),dataMessage);
  		//console.log(message);
  	};
  	saveDate=[];
  	alert('保存成功');
  	$("#ins_or_up_buttontoken").attr("disabled", true);
}   
</script> 