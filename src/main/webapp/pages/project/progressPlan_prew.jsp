<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
    String ContextPath = request.getContextPath();
    String path=ContextPath+"/vendor/vehicles";
    %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script src="<%=request.getContextPath()%>/js/reference.js"></script>
<link href="<%=request.getContextPath()%>/vendor/bootstrap/css/bootstrap.css" rel="stylesheet" />
<link href="<%=request.getContextPath()%>/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet" /> 
<link href="<%=request.getContextPath()%>/vendor/bootstrap-table/src/bootstrap-table.css" rel="stylesheet" />  

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>项目进度计划</title>
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
	<div class="col-md-12 previewtitle">项目进度计划</div>

<div>
<form id="form">
<table id="table" class="table table-bordered">
<thead>
	<tr class="info">
	<th style="width:60px;">序号</th>
	<th style="min-width:160px;">里程碑</th>
	<th>开始日期</th>
	<th>计划完成日期</th>
	<th>阶段任务</th>
	<th>交付物</th>
	<th>责任人</th>
	</tr>
</thead>
<tbody id="tbody">
	
</tbody>
</table>
</form>
</div>
</div>
</div>

</body>
<script src="<%=request.getContextPath()%>/js/public.js"></script>
<script src="<%=request.getContextPath()%>/js/common.js">
</html>
<%-- <script src="<%=request.getContextPath()%>/vendor/jquery/jquery.min.js"></script>
</script> --%>
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

$("body").delegate("[name='manager']",'click',function(){
   removeId();
   $(this).attr("id","MANAGER");
})

function removeId(){
	$("input[name='manager']").each(function(){
		$(this).attr("id","");
	});
}
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
               url : "/system/project/progress_plan?proj_source_id="+ParentPKValue,  
               dataType : "json",  
                           type : "GET",  
                           async: false, 
                           success : function(data) {
                           	console.log(data);
                           	fill = data;
                           }
        });
        //获取预算主单单据状态
       bill_statue="3";
        
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
				var selectvar = '<select id="'+id+'" name="UrbanDepNo" class="form-control">'+
				'<option value="立项">立项</option>'+
				'<option value="客户需求确认">客户需求确认</option>'+
				'<option value="项目建议书评审完成">项目建议书评审完成</option>'+
				'<option value="采购完成">采购完成</option>'+
				'<option value="开发设计">开发设计</option>'+
				'<option value="实施">实施</option>'+
				'<option value="项目初验">项目初验</option>'+
				'<option value="客户验收上线">客户验收上线</option></select>';
				var wsbstatue = fill[i].MILESTONE;
				t1.innerHTML = selectvar;
				//t2.innerHTML='<input type="text" style="width:98%" value="'+fill[i].START_DATE+'"/>';
				t2.innerHTML='<div class="input-group date" id="date1"><input type="text" class="form-control" value="'+fill[i].START_DATE+'" id="data_yz"/><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div>';
				t3.innerHTML='<div class="input-group date" id="date1"><input type="text" class="form-control" value="'+fill[i].EXPECTED_END_DATE+'" id="data_yz"/><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div>';
				t4.innerHTML='<input type="text" class="form-control" value="'+fill[i].PHASE_TASK+'" />';
				t5.innerHTML='<input type="text" class="form-control" value="'+fill[i].DELIVERABLES+'"/>';
				t6.innerHTML='<input type="text" class="form-control" value="'+fill[i].PRINCIPAL_NAME+'"  onclick="checkReference(this,'+"'REF(RM_USER_REF,NAME:MANAGER;ID:MANAGER_ID,1)','MANAGER','INSUP'"+ ')" />';
				var del=document.createElement("td");
				del.style="width: 50px";
				if(bill_statue!=""){
				 	if(bill_statue !="0" && bill_statue!= "7"){
						del.innerHTML="";
					}else{
						del.innerHTML="<a href='javascript:;' class='btn btn-danger' onclick='del(this,\""+fill[i].ID+"\")' ></a>";
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
				
				$("#"+id+" option[value='"+wsbstatue+"']").attr("selected","selected");
				$('.date').datetimepicker({  
				    format: 'YYYY-MM-DD',  
				    locale: moment.locale('zh-cn')  
				}).on('hide',function(e) {  
	                $('#enterpriseInfoForm').data('bootstrapValidator')  
	                    .updateStatus('data_yz', 'NOT_VALIDATED',null)  
	                    .validateField('data_yz');  
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
'<option value="立项">立项</option>'+
'<option value="客户需求确认">客户需求确认</option>'+
'<option value="项目建议书评审完成">项目建议书评审完成</option>'+
'<option value="采购完成">采购完成</option>'+
'<option value="开发设计">开发设计</option>'+
'<option value="实施">实施</option>'+
'<option value="项目初验">项目初验</option>'+
'<option value="客户验收上线">客户验收上线</option></select>';
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
	t6.innerHTML='<input type="text" style="width:98%" onclick="checkReference(this,'+"'REF(RM_USER_REF,NAME:MANAGER;ID:MANAGER_ID,1)','MANAGER','INSUP'"+ ')" name="manager"/>';
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
				}).on('hide',function(e) {  
	                $('#enterpriseInfoForm').data('bootstrapValidator')  
	                    .updateStatus('data_yz', 'NOT_VALIDATED',null)  
	                    .validateField('data_yz');  
	            });
}

$('.date').datetimepicker({  
				    format: 'YYYY-MM-DD',  
				    locale: moment.locale('zh-cn')
				}); 
</script> 

	
	
