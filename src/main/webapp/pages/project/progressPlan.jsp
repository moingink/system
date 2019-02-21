<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
    String ContextPath = request.getContextPath();
    String path=ContextPath+"/vendor/vehicles";
    %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
		padding:0;
		text-align:center;
		vertical-align: middle;
	}

</style>
</head>

<body style="padding:7px;">
<button onclick="saveProgressPlan()" class="btn btn-success" id="ins_or_up_buttontoken">保存</button>
<input type="button" value="修改" onclick="update();" id="update_button" class="btn btn-warning"/>
<input type="button" class="btn btn-primary" value="添加一行" onclick="add()" id="add_button"/> <!--在添加按钮上添加点击事件 -->
<div style="overflow-y:auto; min-height:500px;">
<form id="form" autocomplete="off" style="margin-top:10px;"> 
<table class="table table-bordered" id="table">
<thead>
	<tr class="info">
	<th style="width:50px;">序号</th>
	<th>里程碑</th>
	<th>开始日期</th>
	<th>计划完成日期</th>
	<th>阶段任务</th>
	<th>交付物</th>
	<th>责任人</th>
	<th style="width:50px;">操作</th>
	</tr>
</thead>
<tbody id="tbody">
	<tr >
		<td style="text-align: center; vertical-align: middle; "><input type="text" class="form-control" style="display: none;" value="" />1</td>
		<td><select class="form-control" style="border:none;" id="UrbanDepNo0" name="UrbanDepNo" ><option value="立项" selected="selected">立项</option><option value="客户需求确认">客户需求确认</option><option value="项目建议书评审完成">项目建议书评审完成</option><option value="采购完成">采购完成</option><option value="开发设计">开发设计</option><option value="实施">实施</option><option value="项目初验">项目初验</option><option value="客户验收上线">客户验收上线</option></select></td>
		<td><div class="input-group date" id="date1"><input type="text" class="form-control" id="data_yz"/><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div></td>
		<td><div class="input-group date" id="date1"><input type="text" class="form-control" id="data_yz"/><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div></td>
		<td><input type="text" class="form-control" style="border:none;"></td>
		<td><input type="text" class="form-control" style="border:none;"></td>
		<td><input type="text" class="form-control" style="border:none;" onclick="checkReference(this,'REF(RM_USER_REF,NAME:MANAGER;ID:MANAGER_ID,1)','MANAGER','INSUP')" name="manager"></td>
		<td><a class="btn btn-danger" href="javascript:;" onclick="del(this);">删除</a></td> <!--在删除按钮上添加点击事件 -->
	</tr>
	<tr>
		<td style="text-align: center; vertical-align: middle; "><input type="text" class="form-control" style="display: none;" value="" />2</td>
		<td><select class="form-control" style="border:none;" id="UrbanDepNo0" name="UrbanDepNo" ><option value="立项" selected="selected">立项</option><option value="客户需求确认" selected="selected">客户需求确认</option><option value="项目建议书评审完成">项目建议书评审完成</option><option value="采购完成">采购完成</option><option value="开发设计">开发设计</option><option value="实施">实施</option><option value="项目初验">项目初验</option><option value="客户验收上线">客户验收上线</option></select></td>
		<td><div class="input-group date" id="date1"><input type="text" class="form-control" id="data_yz"/><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div></td>
		<td><div class="input-group date" id="date1"><input type="text" class="form-control" id="data_yz"/><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div></td>
		<td><input type="text" class="form-control" style="border:none;"></td>
		<td><input type="text" class="form-control" style="border:none;"></td>
		<td><input type="text" class="form-control" style="border:none;" onclick="checkReference(this,'REF(RM_USER_REF,NAME:MANAGER;ID:MANAGER_ID,1)','MANAGER','INSUP')"  name="manager"></td>
		<td><a class="btn btn-danger" href="javascript:;" onclick="del(this);">删除</a></td> <!--在删除按钮上添加点击事件 -->
	</tr>
	<tr>
		<td style="text-align: center; vertical-align: middle; "><input type="text" class="form-control" style="display: none;" value="" />3</td>
		<td><select class="form-control" style="border:none;" id="UrbanDepNo0" name="UrbanDepNo" ><option value="立项" selected="selected">立项</option><option value="客户需求确认">客户需求确认</option><option value="项目建议书评审完成" selected="selected">项目建议书评审完成</option><option value="采购完成">采购完成</option><option value="开发设计">开发设计</option><option value="实施">实施</option><option value="项目初验">项目初验</option><option value="客户验收上线">客户验收上线</option></select></td>
		<td><div class="input-group date" id="date1"><input type="text" class="form-control" id="data_yz"/><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div></td>
		<td><div class="input-group date" id="date1"><input type="text" class="form-control" id="data_yz"/><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div></td>
		<td><input type="text" class="form-control" style="border:none;"></td>
		<td><input type="text" class="form-control" style="border:none;"></td>
		<td><input type="text" class="form-control" style="border:none;" onclick="checkReference(this,'REF(RM_USER_REF,NAME:MANAGER;ID:MANAGER_ID,1)','MANAGER','INSUP')"  name="manager"></td>
		<td><a class="btn btn-danger" href="javascript:;" onclick="del(this);">删除</a></td> <!--在删除按钮上添加点击事件 -->
	</tr>
	<tr>
		<td style="text-align: center; vertical-align: middle; "><input type="text" class="form-control" style="display: none;" value="" />4</td>
		<td><select class="form-control" style="border:none;" id="UrbanDepNo0" name="UrbanDepNo" ><option value="立项" selected="selected">立项</option><option value="客户需求确认">客户需求确认</option><option value="项目建议书评审完成">项目建议书评审完成</option><option value="采购完成" selected="selected">采购完成</option><option value="开发设计">开发设计</option><option value="实施">实施</option><option value="项目初验">项目初验</option><option value="客户验收上线">客户验收上线</option></select></td>
		<td><div class="input-group date" id="date1"><input type="text" class="form-control" id="data_yz"/><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div></td>
		<td><div class="input-group date" id="date1"><input type="text" class="form-control" id="data_yz"/><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div></td>
		<td><input type="text" class="form-control" style="border:none;"></td>
		<td><input type="text" class="form-control" style="border:none;"></td>
		<td><input type="text" class="form-control" style="border:none;" onclick="checkReference(this,'REF(RM_USER_REF,NAME:MANAGER;ID:MANAGER_ID,1)','MANAGER','INSUP')" name="manager"></td>
		<td><a class="btn btn-danger" href="javascript:;" onclick="del(this);">删除</a></td> <!--在删除按钮上添加点击事件 -->
	</tr>
	<tr>
		<td style="text-align: center; vertical-align: middle; "><input type="text" class="form-control" style="display: none;" value="" />5</td>
		<td><select class="form-control" style="border:none;" id="UrbanDepNo0" name="UrbanDepNo" ><option value="立项" selected="selected">立项</option><option value="客户需求确认">客户需求确认</option><option value="项目建议书评审完成">项目建议书评审完成</option><option value="采购完成">采购完成</option><option value="开发设计" selected="selected">开发设计</option><option value="实施">实施</option><option value="项目初验">项目初验</option><option value="客户验收上线">客户验收上线</option></select></td>
		<td><div class="input-group date" id="date1"><input type="text" class="form-control" id="data_yz"/><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div></td>
		<td><div class="input-group date" id="date1"><input type="text" class="form-control" id="data_yz"/><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div></td>
		<td><input type="text" class="form-control" style="border:none;"></td>
		<td><input type="text" class="form-control" style="border:none;"></td>
		<td><input type="text" class="form-control" style="border:none;" onclick="checkReference(this,'REF(RM_USER_REF,NAME:MANAGER;ID:MANAGER_ID,1)','MANAGER','INSUP')" name="manager"></td>
		<td><a class="btn btn-danger" href="javascript:;" onclick="del(this);">删除</a></td> <!--在删除按钮上添加点击事件 -->
	</tr>
	<tr>
		<td style="text-align: center; vertical-align: middle; "><input type="text" class="form-control" style="display: none;" value="" />6</td>
		<td><select class="form-control" style="border:none;" id="UrbanDepNo0" name="UrbanDepNo" ><option value="立项" selected="selected">立项</option><option value="客户需求确认">客户需求确认</option><option value="项目建议书评审完成">项目建议书评审完成</option><option value="采购完成">采购完成</option><option value="开发设计">开发设计</option><option value="实施" selected="selected">实施</option><option value="项目初验">项目初验</option><option value="客户验收上线">客户验收上线</option></select></td>
		<td><div class="input-group date" id="date1"><input type="text" class="form-control" id="data_yz"/><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div></td>
		<td><div class="input-group date" id="date1"><input type="text" class="form-control" id="data_yz"/><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div></td>
		<td><input type="text" class="form-control" style="border:none;"></td>
		<td><input type="text" class="form-control" style="border:none;"></td>
		<td><input type="text" class="form-control" style="border:none;" onclick="checkReference(this,'REF(RM_USER_REF,NAME:MANAGER;ID:MANAGER_ID,1)','MANAGER','INSUP')" name="manager"></td>
		<td><a class="btn btn-danger" href="javascript:;" onclick="del(this);">删除</a></td> <!--在删除按钮上添加点击事件 -->
	</tr>
	<tr>
		<td style="text-align: center; vertical-align: middle; "><input type="text" class="form-control" style="display: none;" value="" />7</td>
		<td><select class="form-control" style="border:none;" id="UrbanDepNo0" name="UrbanDepNo" ><option value="立项" selected="selected">立项</option><option value="客户需求确认">客户需求确认</option><option value="项目建议书评审完成">项目建议书评审完成</option><option value="采购完成">采购完成</option><option value="开发设计">开发设计</option><option value="实施">实施</option><option value="项目初验" selected="selected">项目初验</option><option value="客户验收上线">客户验收上线</option></select></td>
		<td><div class="input-group date" id="date1"><input type="text" class="form-control" id="data_yz"/><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div></td>
		<td><div class="input-group date" id="date1"><input type="text" class="form-control" id="data_yz"/><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div></td>
		<td><input type="text" class="form-control" style="border:none;"></td>
		<td><input type="text" class="form-control" style="border:none;"></td>
		<td><input type="text" class="form-control" style="border:none;" onclick="checkReference(this,'REF(RM_USER_REF,NAME:MANAGER;ID:MANAGER_ID,1)','MANAGER','INSUP')" name="manager"></td>
		<td><a class="btn btn-danger" href="javascript:;" onclick="del(this);">删除</a></td> <!--在删除按钮上添加点击事件 -->
	</tr>
	<tr>
		<td style="text-align: center; vertical-align: middle; "><input type="text" class="form-control" style="display: none;" value="" />8</td>
		<td><select class="form-control" style="border:none;" id="UrbanDepNo0" name="UrbanDepNo" ><option value="立项" selected="selected">立项</option><option value="客户需求确认">客户需求确认</option><option value="项目建议书评审完成">项目建议书评审完成</option><option value="采购完成">采购完成</option><option value="开发设计">开发设计</option><option value="实施">实施</option><option value="项目初验">项目初验</option><option value="客户验收上线" selected="selected">客户验收上线</option></select></td>
		<td><div class="input-group date" id="date1"><input type="text" class="form-control" id="data_yz"/><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div></td>
		<td><div class="input-group date" id="date1"><input type="text" class="form-control" id="data_yz"/><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div></td>
		<td><input type="text" class="form-control" style="border:none;"></td>
		<td><input type="text" class="form-control" style="border:none;"></td>
		<td><input type="text" class="form-control" style="border:none;" onclick="checkReference(this,'REF(RM_USER_REF,NAME:MANAGER;ID:MANAGER_ID,1)','MANAGER','INSUP')" name="manager"></td>
		<td><a class="btn btn-danger" href="javascript:;" onclick="del(this);">删除</a></td> <!--在删除按钮上添加点击事件 -->
	</tr>
</tbody>
</table>
</form>
</div>
</body>
</html>
<script src="<%=request.getContextPath()%>/pages/js/reference.js"></script>
<script src="<%=request.getContextPath()%>/js/public.js"></script>
<script src="<%=request.getContextPath()%>/js/common.js"></script>
<%-- <script src="<%=request.getContextPath()%>/vendor/jquery/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/vendor/bootstrap/js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/vendor/bootstrap/js/moment-with-locales.min.js"></script>
<link href="<%=request.getContextPath()%>/vendor/bootstrap/css/bootstrap-datetimepicker.min.css" rel="stylesheet" /> 
<script src="<%=request.getContextPath()%>/vendor/bootstrap/js/bootstrap-datetimepicker.min.js"></script>  --%>
<!-- 更换本地引用暂时有些样式问题,后期调整 -->
<script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.min.js"></script>  
<link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet" /> 
<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script> 
<script src="https://cdn.bootcss.com/moment.js/2.18.1/moment-with-locales.min.js"></script>  
<link href="https://cdn.bootcss.com/bootstrap-datetimepicker/4.17.47/css/bootstrap-datetimepicker.min.css" rel="stylesheet" />
<script src="https://cdn.bootcss.com/bootstrap-datetimepicker/4.17.47/js/bootstrap-datetimepicker.min.js"></script>
<script src="../../js/hide.js"></script>
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
		            	//console.log(data);
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
                           	//console.log(data);
                           	fill = data;
                           }
        });
        //获取预算主单单据状态
        $.ajax({  
              url : "/system/project/proposalStatue?proj_source_id="+ParentPKValue,  
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
    		//length = fill.length;
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
				var selectvar = '<select id="'+id+'" name="UrbanDepNo" class="form-control" style="border:none;">'+
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
				t4.innerHTML='<input type="text" class="form-control" style="border:none;" value="'+fill[i].PHASE_TASK+'" />';
				t5.innerHTML='<input type="text" class="form-control" style="border:none;" value="'+fill[i].DELIVERABLES+'"/>';
				t6.innerHTML='<input type="text" class="form-control" style="border:none;" value="'+fill[i].PRINCIPAL_NAME+'"  onclick="checkReference(this,'+"'REF(RM_USER_REF,NAME:MANAGER;ID:MANAGER_ID,1)','MANAGER','INSUP'"+ ')" name="manager" />';
				var del=document.createElement("td");
				del.style="width: 50px";
				if(bill_statue!=""){
				 	if(bill_statue !="0" && bill_statue!= "7"){
						del.innerHTML="";
					}else{
						del.innerHTML="<a class='btn btn-danger' href='javascript:;' onclick='del(this,\""+fill[i].ID+"\")' >删除</a>";
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
				}).on('hide',function(e) {  
	                $('#enterpriseInfoForm').data('bootstrapValidator')  
	                    .updateStatus('data_yz', 'NOT_VALIDATED',null)  
	                    .validateField('data_yz');  
	            });
	         }
	        
	         if(keyWord == 1){//iframe
		     	$('a').css("display","none");
			 }
	//****默认第一次 按钮只能点击修改,页面不能编辑,点击修改后才可以编辑。再次保存置灰所有****//
	$(":input").each(function(){
		$(this).attr("disabled",true);
	});
	$("select").each(function(){
		$(this).attr("disabled",true);
	});
	$("#update_button").attr("disabled", false);
	//******************************************//
			 if(keyWord != 1){
			 	if(bill_statue!=""){
				 	if(bill_statue !="0" && bill_statue!= "7"){
						$("#ins_or_up_buttontoken").attr("disabled", true);
						$("#add_button").attr("disabled", true);
						$("#update_button").attr("disabled", true);
						$(":input").each(function(){
							$(this).attr("disabled",true);
						});
						$("select").each(function(){
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
var selectvar = '<select class="form-control" style="border:none;" id="" name="UrbanDepNo" style="width:90%">'+
	'<option value="立项">立项</option>'+
	'<option value="客户需求确认">客户需求确认</option>'+
	'<option value="项目建议书评审完成">项目建议书评审完成</option>'+
	'<option value="采购完成">采购完成</option>'+
	'<option value="开发设计">开发设计</option>'+
	'<option value="实施">实施</option>'+
	'<option value="项目初验">项目初验</option>'+
	'<option value="客户验收上线">客户验收上线</option></select>';
function add(){
	//获取table当前总行数
	
	length = $("#tbody").find("tr").length +1; //length +1;
	var tr=document.createElement("tr");
	var t0=document.createElement("td");
	var t1=document.createElement("td");
	var t2=document.createElement("td");
	var t3=document.createElement("td");
	var t4=document.createElement("td");
	var t5=document.createElement("td");
	var t6=document.createElement("td");
	t0.innerHTML='<input type="text" class="form-control" style="border:none;" value="" />';
	t1.innerHTML=selectvar;
	t2.innerHTML='<div class="input-group date" id="date1"><input type="text" class="form-control" /><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div>';
	t3.innerHTML='<div class="input-group date" id="date1"><input type="text" class="form-control" /><span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span></div>';
	t4.innerHTML='<input type="text" class="form-control" style="border:none;" />';
	t5.innerHTML='<input type="text" class="form-control" style="border:none;" />';
	t6.innerHTML='<input type="text" class="form-control" style="border:none;" onclick="checkReference(this,'+"'REF(RM_USER_REF,NAME:MANAGER;ID:MANAGER_ID,1)','MANAGER','INSUP'"+ ')" name="manager"/>';
	var del=document.createElement("td");
	del.innerHTML="<a class='btn btn-danger' href='javascript:;' onclick='del(this)' >删除</a>";
	var tab=document.getElementById("tbody");
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
	                //console.log(saveDate);
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
	saveDate= [];
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
  	data = [];
  	return saveDate;
}

function iframeVerification(){
	var message = "";
	var flag = true;
	//校验开始日期和结束日期必填
	saveDate = jsonValue();
	for(var s=0;s<saveDate.length;s++){
		if(saveDate[s].START_DATE==''||saveDate[s].EXPECTED_END_DATE==''){
			saveDate=[];
			message = "开始日期和计划完成日期不能为空";
			flag = false;
			break;
		}
	}
	return flag+":"+message;
}

function saveProgressPlan() {
	//教研开始日期和结束日期必填
	    
	saveDate = jsonValue();
	//console.log(saveDate);
	for(var s=0;s<saveDate.length;s++){
		//console.log(saveDate[s].START_DATE=='');
		if(saveDate[s].START_DATE==''||saveDate[s].EXPECTED_END_DATE==''){
			alert('开始日期和计划完成日期不能为空');
			saveDate=[];
			return ;
		}
		//开始日期不能大于结束日期
		//日期先后判断 
  		var d1 = new Date(saveDate[s].START_DATE.replace(/\-/g, "\/"));
		var d2 = new Date(saveDate[s].EXPECTED_END_DATE.replace(/\-/g, "\/"));
		if(d1 > d2 ){
			alert("开始日期不能大于计划完成日期");
			saveDate=[];
			return ;
		}
		//校验是否必填
		if(saveDate[s].PHASE_TASK==''||saveDate[s].PHASE_TASK=='null'){
			alert('请填写阶段任务,每个阶段任务不能为空');
			//oTable.showModal('提示', "阶段任务不能为空");
			saveDate=[];
			return ;
		}
	}
  
  	if(buttonToken=="deleteForProJect"){
		var dataMessage =saveDate;
	  	//alert(dataMessage);
	  	dataMessage=JSON.stringify(dataMessage);
	  	//alert(dataMessage)
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
  	if(message=="保存成功"){
	  	alert('保存成功');
	  	$(":input").each(function(){
			$(this).attr("disabled",true);
		});
  		$("#ins_or_up_buttontoken").attr("disabled", true);
  		$("#add_button").attr("disabled", true);
  		$("#update_button").attr("disabled", false);
  		location.reload();
  	}else{
  		alert('保存失败');
  	}
}
$('.date').datetimepicker({  
				    format: 'YYYY-MM-DD',  
				    locale: moment.locale('zh-cn')
				}); 
	function update(){
		//点亮保存和添加行按钮
		$(":input").each(function(){
			$(this).attr("disabled",false);
		});
		$("#ins_or_up_buttontoken").attr("disabled", false);
		$("#add_button").attr("disabled", false);
		$("#update_button").attr("disabled", true);
	}
</script> 

	
	
