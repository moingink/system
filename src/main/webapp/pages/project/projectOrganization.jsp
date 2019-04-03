<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script src="<%=request.getContextPath()%>/pages/js/reference.js"></script>
<link href="<%=request.getContextPath()%>/vendor/bootstrap/css/bootstrap.css" rel="stylesheet" />
<link href="<%=request.getContextPath()%>/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet" /> 
<link href="<%=request.getContextPath()%>/vendor/bootstrap-table/src/bootstrap-table.css" rel="stylesheet" />  
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>项目组织架构</title>
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
<button class="btn btn-success" onclick="saveProjectOra()" id="ins_or_up_buttontoken">保存</button>
<input class="btn btn-warning" type="button" value="修改" onclick="update();" id="update_button" />
<input class="btn btn-primary" type="button" value="添加一行" onclick="add();" id="add_button" /> 
<div style="overflow-y:auto; margin-top:10px;">
<form id="form" autocomplete="off">

<table id="table" class="table table-bordered">
<thead>
	<tr class="info">
		<th style="width:50px;">序号</th>
		<th style="width:150px;">职能组</th>
		<th style="width:120px;">人员类型</th>
		<th>姓名</th>
		<th>角色</th>
		<th>邮箱</th>
		<th>电话</th>	
		<th style="width:80px;">操作</th>
	</tr>
		
	
</thead>

<tbody id="tbody">
	
	
</tbody>
</table>

</form>
</div>
</body>
</html>

<script src="<%=request.getContextPath()%>/js/public.js"></script>
<script src="<%=request.getContextPath()%>/js/common.js"></script>
<%-- <script src="<%=request.getContextPath()%>/vendor/jquery/jquery.min.js"></script>
 --%>
<script src="../../js/hide.js"></script>
<script src="<%=request.getContextPath()%>/vendor/jquery/jquery.min.js"></script>

<script src="<%=request.getContextPath()%>/vendor/bootstrap/js/bootstrap.min.js"></script> 
<script src="<%=request.getContextPath()%>/vendor/bootstrap/js/moment-with-locales.min.js"></script>   
<script src="<%=request.getContextPath()%>/vendor/bootstrap/js/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript"> 
//首先加载数据 填充如果无数据则设置按钮buttonToken 否则是buttonToken=update
var bill_statue="";
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

window.onload = function(){
	context='<%=request.getContextPath()%>';
	token='<%=request.getParameter("token")!=null?request.getParameter("token"):""%>';
	var tr=document.getElementsByTagName("tr");
	for(var i= 0;i<tr.length;i++){
		bgcChange(tr[i]);
	}
	// 鼠标移动改变背景,可以通过给每行绑定鼠标移上事件和鼠标移除事件来改变所在行背景色。
	var tab=document.getElementById("tbody");
	//获取后台数据 填table
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
            url : "/system/project/getOrag?proj_source_id="+ParentPKValue,  
            dataType : "json",  
            type : "GET",  
            async: false,
            success : function(data) {
            		fill = data;
            }
          });
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
   		}
  		for(var i=0;i<fill.length;i++){
	  		//循环表格数据动态添加行列信息
	  		var tr=document.createElement("tr");
			var t1=document.createElement("td");
			var t2=document.createElement("td");
			var t3=document.createElement("td");
			var t4=document.createElement("td");
			var t5=document.createElement("td");
			var t6=document.createElement("td");
			var t7=document.createElement("td");
			var t8=document.createElement("td");
			var t9=document.createElement("td");
			var t10=document.createElement("td");
			var t11=document.createElement("td");
			t1.innerHTML='<input type="text"  style="width:100%;display:none" value="'+fill[i].ID+'" />'+(Number(i)+1);
			var id  = "type"+i;
			var zngroup = fill[i].FUNCTIONAL_GROUP;
	        t2.innerHTML='<select id="'+id+'" name="UrbanDepNo" class="form-control" style="border:none;"><option value="1">项目管理委员会</option><option value="2">项目核心管理团队</option><option value="3">资源环境准备组</option><option value="4">系统设计组</option><option value="5">应用开发部署组</option><option value="6">系统集成组</option><option value="7">质量保证团队</option></select>';
			t3.innerHTML='<select id="UrbanDepNo0" name="UrbanDepNo" class="form-control UrbanDepNo" style="border:none;"><option value="1" '+(fill[i].PERSON_TYPE=="1"?"selected":"" )+'>内部人员</option><option value="2" '+(fill[i].PERSON_TYPE=="2"?"selected":"" )+'>外部人员</option></select>';
			t4.innerHTML='<input type="text" class="form-control" style="border:none;" value="'+fill[i].NAME+'" onclick="checkReference(this,\'REF(RM_USER_REF,NAME:USER_NAME;EMAIL:USER_EMAIL;ID:USER_ID,1)\',\'NAME\',\'INSUP\');" />';
			var ida = "role"+i;
			var role_value = fill[i].ROLE;
			t5.innerHTML='<select id="'+ida+'" name="UrbanDepNo" class="form-control" style="border:none;"><option value="1">领导</option><option value="2">总、副总</option><option value="3">分管领导</option><option value="4">销售总监</option><option value="5">项目总监</option><option value="6">产品总监</option><option value="7">项目经理</option><option value="8">客户经理</option><option value="9">产品方案经理</option><option value="10">运营经理</option><option value="11">维护经理</option><option value="12">现场协调</option><option value="13">软件架构师</option><option value="14">系统架构师</option><option value="15">开发经理</option><option value="16">前台工程师</option><option value="17">后台工程师</option><option value="17">质量保障经理</option><option value="19">测试工程师</option></select>';
			t6.innerHTML='<input type="text" class="form-control" style="border:none;" value="'+fill[i].EMAIL+'"/>';
			t7.innerHTML='<input type="text" class="form-control" style="border:none;" value="'+fill[i].TEL+'"/><input type="hidden"    value="'+fill[i].USER_ID+'"/>';
			var del=document.createElement("td");
			if(bill_statue!=""){
			 	if(bill_statue !="0" && bill_statue!= "7"){
			 		del.innerHTML="<a href='javascript:;' ></a>";
				}else{
					del.innerHTML="<a class='btn btn-danger' href='javascript:;' onclick='del(this,\""+fill[i].ID+"\")' >删除</a>";
				}
		 	}else{
				del.innerHTML="<a class='btn btn-danger' href='javascript:;' onclick='del(this,\""+fill[i].ID+"\")' >删除</a>";
		 	}
			tab.appendChild(tr);
			tr.appendChild(t1);
			tr.appendChild(t2);
			tr.appendChild(t3);
			tr.appendChild(t4);
			tr.appendChild(t5);
			tr.appendChild(t6);
			tr.appendChild(t7);
			
			tr.appendChild(del);
			$("#"+id+" option[value='"+zngroup+"']").attr("selected","selected");
			$("#"+ida+" option[value='"+role_value+"']").attr("selected","selected");
			
        }
		
		  $("tr").click(function(){
				$("tr").removeClass("active");
				$(this).addClass("active");
	 			if($(this).find("td:nth-child(3)>select").val()=='1'){
	 				$("tr>td:nth-child(4)>input").removeAttr("id");
	 				$(this).find("td:nth-child(4)>input").attr("id","USER_NAME");
	 				$("tr>td:nth-child(7)>input:nth-child(2)").removeAttr("id");
	 				$(this).find("td:nth-child(7)>input:nth-child(2)").attr("id","USER_ID");
	 				$("tr>td:nth-child(6)>input").removeAttr("id");
	 				$(this).find("td:nth-child(6)>input").attr("id","USER_EMAIL");
	 				$(this).find("td:nth-child(4)>input").attr("readonly","true");
		 			//checkReference(this,'REF(RM_USER_REF,NAME:USER_NAME;EMAIL:USER_EMAIL;ID:USER_ID,1)','NAME','INSUP');
	 			}else{
	 				$(this).find("td:nth-child(4)>input").removeAttr("readonly");
	 			}
			});
		 $(".UrbanDepNo").change(function(){
		 	$("#USER_NAME").val("");
		 	$("#USER_EMAIL").val("");
		 	$("#USER_ID").val("");
		 	if($(this).val()=='1'){
			 	$("table").find(".active").find("td:nth-child(4)>input").attr("readonly","true");
			 	$("table").find(".active").find("td:nth-child(4)>input").attr("onclick","checkReference(this,'REF(RM_USER_REF,NAME:USER_NAME;EMAIL:USER_EMAIL;ID:USER_ID,1)','NAME','INSUP');");
		 	}else{
			 	$("table").find(".active").find("td:nth-child(4)>input").removeAttr("readonly");
			 	$("table").find(".active").find("td:nth-child(4)>input").removeAttr("onclick");
		 	}
		 });
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
} 
function update(){
	$(":input").each(function(){
		$(this).attr("disabled",false);
	});
	//点亮保存和添加行按钮
	$("#ins_or_up_buttontoken").attr("disabled", false);
	$("#add_button").attr("disabled", false);
	$("#update_button").attr("disabled", true);
}
function ref_end1(){
	var rejsonArray =reference_oTable.bootMethod($reference_table,"getSelections");
	if(rejsonArray.length>0)
	$("#USER_EMAIL",window.document).val(rejsonArray[0].EMAIL);
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

var selectvar = '<select id="UrbanDepNo0" name="UrbanDepNo" style="width:100%">'+
 '<option value="1">立项</option>'+
 '<option value="2">客户需求确认</option>'+
 '<option value="3">项目建议书评审完成</option>'+
 '<option value="4">采购完成</option>'+
 '<option value="5">开发设计</option>'+
 '<option value="6">实施</option>'+
 '<option value="7">项目初验</option>'+
 '<option value="6">客户验收上线</option></select>';
	function add(){
		var tr=document.createElement("tr");
		var t1=document.createElement("td");
		var t2=document.createElement("td");
		var t3=document.createElement("td");
		var t4=document.createElement("td");
		var t5=document.createElement("td");
		var t6=document.createElement("td");
		var t7=document.createElement("td");
		var t8=document.createElement("td");
		var t9=document.createElement("td");
		var t10=document.createElement("td");
		var ta=document.createElement("td");
		t1.innerHTML='<input type="text" style="display:none "/>';
		t2.innerHTML='<select class="form-control" id="grop_type" name="grop_type" style="width:100%;border:none;"><option value="1">项目管理委员会</option><option value="2">项目核心管理团队</option><option value="3">资源环境准备组</option><option value="4">系统设计组</option><option value="5">应用开发部署组</option><option value="6">系统集成组</option><option value="7">质量保证团队</option></select>';
		t3.innerHTML='<select class="form-control UrbanDepNo" id="persion_type" name="persion_type" style="width:100%;border:none;"><option value="1">内部人员</option><option value="2">外部人员</option></select>';
		t4.innerHTML='<input type="text" style="width:100%;border:none;"  onclick="checkReference(this,\'REF(RM_USER_REF,NAME:USER_NAME;EMAIL:USER_EMAIL;ID:USER_ID,1)\',\'NAME\',\'INSUP\');"/>';
		t5.innerHTML='<select class="form-control" id="role_type" name="role_type" style="width:100%;border:none;"><option value="1">领导</option><option value="2">总、副总</option><option value="3">分管领导</option><option value="4">销售总监</option><option value="5">项目总监</option><option value="6">产品总监</option><option value="7">项目经理</option><option value="8">客户经理</option><option value="9">产品方案经理</option><option value="10">运营经理</option><option value="11">维护经理</option><option value="12">现场协调</option><option value="13">软件架构师</option><option value="14">系统架构师</option><option value="15">开发经理</option><option value="16">前台工程师</option><option value="17">后台工程师</option><option value="17">质量保障经理</option><option value="19">测试工程师</option></select>';
		t6.innerHTML='<input type="text" class="form-control" style="width:100%;border:none;" />';
		t7.innerHTML='<input type="text" class="form-control" style="width:100%;border:none;" /><input type="hidden"    value=""/>';
		//t8.innerHTML='<input type="text"/>';
		//t8.style= "display:none";
		t9.innerHTML='<select id="s3" name="UrbanDepNo" style="width:100%"><option value="1">领导</option><option value="2">总、副总</option><option value="3">分管领导</option><option value="4">销售总监</option><option value="5">项目总监</option><option value="6">产品总监</option><option value="7">项目经理</option><option value="8">客户经理</option><option value="9">产品方案经理</option><option value="10">运营经理</option><option value="11">维护经理</option><option value="12">现场协调</option><option value="13">软件架构师</option><option value="14">系统架构师</option><option value="15">开发经理</option><option value="16">前台工程师</option><option value="17">后台工程师</option><option value="17">质量保障经理</option><option value="19">测试工程师</option></select>';
		t10.innerHTML='<input type="text" class="form-control" style="width:100%" />';
		ta.innerHTML='<input type="text" class="form-control" style="width:100%" />';
		
		var del=document.createElement("td");
		del.innerHTML="<a href='javascript:;' onclick='del(this)' class='btn btn-danger' style='display:block;margin:0 auto;'>删除</a>";
		var tab=document.getElementById("tbody");
		tab.appendChild(tr);
		tr.appendChild(t1);
		tr.appendChild(t2);
		tr.appendChild(t3);
		tr.appendChild(t4);
		tr.appendChild(t5);
		tr.appendChild(t6);
		tr.appendChild(t7);
		//tr.appendChild(t8);
		/*tr.appendChild(t9);
		tr.appendChild(t10);
		tr.appendChild(ta);*/
		tr.appendChild(del);
		var tr = document.getElementsByTagName("tr");
		for(var i= 0;i<tr.length;i++){
			bgcChange(tr[i]);
		}
		
		  $("tr").click(function(){
				$("tr").removeClass("active");
				$(this).addClass("active");
	 			if($(this).find("td:nth-child(3)>select").val()=='1'){
	 				$("tr>td:nth-child(4)>input").removeAttr("id");
	 				$(this).find("td:nth-child(4)>input").attr("id","USER_NAME");
	 				$("tr>td:nth-child(7)>input:nth-child(2)").removeAttr("id");
	 				$(this).find("td:nth-child(7)>input:nth-child(2)").attr("id","USER_ID");
	 				$("tr>td:nth-child(6)>input").removeAttr("id");
	 				$(this).find("td:nth-child(6)>input").attr("id","USER_EMAIL");
	 				$(this).find("td:nth-child(4)>input").attr("readonly","true");
		 			//checkReference(this,'REF(RM_USER_REF,NAME:USER_NAME;EMAIL:USER_EMAIL;ID:USER_ID,1)','NAME','INSUP');
	 			}else{
	 				$(this).find("td:nth-child(4)>input").removeAttr("readonly");
	 			}
			});
		 $(".UrbanDepNo").change(function(){
		 	$("#USER_NAME").val("");
		 	$("#USER_EMAIL").val("");
		 	$("#USER_ID").val("");
		 	if($(this).val()=='1'){
			 	$("table").find(".active").find("td:nth-child(4)>input").attr("readonly","true");
			 	$("table").find(".active").find("td:nth-child(4)>input").attr("onclick","checkReference(this,'REF(RM_USER_REF,NAME:USER_NAME;EMAIL:USER_EMAIL;ID:USER_ID,1)','NAME','INSUP');");
		 	}else{
			 	$("table").find(".active").find("td:nth-child(4)>input").removeAttr("readonly");
			 	$("table").find(".active").find("td:nth-child(4)>input").removeAttr("onclick");
		 	}
		 });
		
}
// 创建删除函数
function del(obj,id){
	//确认
                var j = {};
                var saveDate= [];
	  if(typeof(id)==""||typeof(id)=="undefined"){
    	var tr=obj.parentNode.parentNode;
		tr.parentNode.removeChild(tr);
    }else{
	    if(window.confirm('确定删除？')){
	                //调用删除方法
	                j.ID = id;
	                saveDate.push(j);
	                //console.log(saveDate);
	                var datamess = JSON.stringify(saveDate);
			        var message = transToServer(findBusUrlByButtonTonken(buttonToken,'&table='+pageCode,''),datamess);
			    	if(message=="删除成功"){
				    	var tr=obj.parentNode.parentNode;
						tr.parentNode.removeChild(tr);
			    	}
	                 return true;
	              }else{
	                 //alert("取消");
	                 return false;
	             }
    }
}
function jsonValue(){
	var saveDate= [];
	var data = [];
	var j = {};
 	var  arr= document.getElementById('form').getElementsByTagName("*");
 	var l = arr.length;
	for(var i=0;i<l;i++){
	    if(arr[i].tagName && (arr[i].tagName=="INPUT" || arr[i].tagName=="SELECT")){
	    	data.push(arr[i].value);
	    }
 	}
 	//console.log(data);
 	for(var k=0; k*8 < data.length;k++){
 		var j = {};
 		j.PROJ_SOURCE_ID = ParentPKValue;
 		if(buttonToken=="deleteForProJect"){
		  		j.ID = data[k*8];
 		}
  		j.FUNCTIONAL_GROUP = data[k*8+1];
	  	j.PERSON_TYPE = data[k*8+2];
	  	j.NAME = data[k*8+3];
	  	j.ROLE = data[k*8+4];
	  	j.EMAIL = data[k*8+5];
	  	j.TEL = data[k*8+6];
	  	
	  	j.USER_ID = data[k*8+7];
	  	j.DR='0';
	  	j.PROJ_TYPE = '0';
  		saveDate.push(j);
  	}
  	//console.log(saveDate);
  	return saveDate;
}
function save(){
	//如果是新增 获取行所有数据保存  如果是修改  记录修改行id和列数据json 批量调用后台
	if(btnToken=="add"){
		// 获取所有行
	}
}

function iframeVerification(){
	var message = "";
	var flag = true;
	var saveDate = jsonValue();
	for(var i=0;i<saveDate.length;i++){
		if(checkEmail(saveDate[i]["EMAIL"])==false){
			message = "请填写正确邮箱";
	  		flag = false;
	  		break;
	  	}
	  	if(checkPhone(saveDate[i]["TEL"])==false){
	  		message = "请填写正确手机号";
	  		flag = false;
	  		break;
	  	}
	}
	return flag+":"+message;
}

//获取指定form中的所有的<input>对象    
function saveProjectOra() {    

	var saveDate = jsonValue();
	console.log(saveDate);
	for(var i=0;i<saveDate.length;i++){
		if(checkEmail(saveDate[i]["EMAIL"])==false){
	  		alert('请填写正确邮箱');
	  		saveDate = [];
	  		return false;
	  	}
	  	if(checkPhone(saveDate[i]["TEL"])==false){
	  		alert('请填写正确手机号');
	  		saveDate = [];
	  		return false;
	  	}
	}
	
	if(saveDate == false){
		return ;
	}
	
//   	var message ="";
//   	//判断是否先删除在插入
//   	if(buttonToken=="deleteForProJect"){
// 	  	var dataMessage =saveDate;
// 	  	dataMessage=JSON.stringify(dataMessage);
// 	  	message = transToServer(findBusUrlByButtonTonken(buttonToken,'&table='+pageCode,''),dataMessage);
//   	}
//   	buttonToken = "addForProJect";
//   	if(buttonToken == "addForProJect"){
//   		var dataMessage =saveDate;
//   		dataMessage=JSON.stringify(dataMessage);
//   		message = transToServer(findBusUrlByButtonTonken(buttonToken,'&table='+pageCode+"&button="+buttonToken,''),dataMessage);
//   		//console.log(message);
//   	};
//   	saveDate=[];
//   	$("#ins_or_up_buttontoken").attr("disabled", true);
//   	if(message=="保存成功"){
// 	  	alert(message);
// 	  	updateState ="update";
// 	  	location.reload();
// 		$("#ins_or_up_buttontoken").attr("disabled", true);
// 		$("#add_button").attr("disabled", true);
//   	}else{
// 		alert(message);  	
//   	}
  	ajax异步调用保存组织职能信息
}   


function checkPhone(value){ 
    if(!(/^1[34578]\d{9}$/.test(value))){  
        return false; 
    } 
}
function checkEmail(str){
	var reg = new RegExp("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$"); //正则表达式
	if(str === ""){ //输入不能为空
		return false;
	}else if(!reg.test(str)){ //正则验证不通过，格式不对
		return false;
	}else{
		return true;
	}
}
</script> 