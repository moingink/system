<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>项目预算</title>
</head>
<body >
<div style="overflow: auto; height: 600px">
<button onclick="saveBudget()" id="ins_or_up_buttontoken">保存</button>
<form id="form">
<table border="1" width="90%" id="table">
<thead>
	<tr>
	<th style="width: 50px">序号</th>
	<th style="width: 115px">费用类别</th>
	<th>费用项目</th>
	<th>单位</th>
	<th>单价(万元)</th>
	<th>数量</th>
	<th>投资估算(万元)</th>
	<th style="width: 50px">操作</th>
	</tr>
</thead>

<tbody id="tbody">
	<tr>
		<td style="width: 50px"><input type="text" style="width:100%;display: none" value="" /></td>
		<td rowspan="50" style="width: 115px">主设备费用</td>
		<td><input type="text" style="width:98%" /></td>
		<td><input type="text" style="width:98%" /></td>
		<td><input type="text" style="width:98%" value="0"/></td>
		<td><input type="text" style="width:98%" value="0"/></td>
		<td><input type="text" style="width:98%" value="0"/></td>
		<td style="width: 50px"><a href="javascript:;" id ="delete" ></a></td> 
	</tr>
</tbody>
</table>
</form>
<table border="0.5" width="90%">
<tr>
<td style="width: 50px;">小计</td>
<td style="width: 50px;" id="cost_a">0</td>
</tr>
</table>
<form id="form1">
<table border="1" width="90%">
<tbody id="tbody1">
 <tr>
 	<td style="width: 50px"><input type="text" style="width:100%;display: none" value="" /></td>
	<td style="width: 115px">软件开发</td>
	<td><input type="text" style="width:98%" /></td>
	<td><input type="text" style="width:98%" /></td>
	<td><input type="text" style="width:98%" value="0"/></td>
	<td><input type="text" style="width:98%" value="0"/></td>
	<td><input type="text" style="width:98%" value="0"/></td>
	<td style="width: 50px;"></td> 
</tr>
<tr>
	<td style="width: 50px"><input type="text" style="width:100%;display: none" value="" /></td>
	<td style="width: 115px">安装工程费</td>
	<td><input type="text" style="width:98%" /></td>
	<td><input type="text" style="width:98%" /></td>
	<td><input type="text" style="width:98%" value="0"/></td>
	<td><input type="text" style="width:98%" value="0"/></td>
	<td><input type="text" style="width:98%" value="0"/></td>
	<td style="width: 50px;"></td> 
</tr>
<tr>
	<td style="width: 50px"><input type="text" style="width:100%;display: none" value="" /></td>
	<td style="width: 115px">工程建设其他费</td>
	<td><input type="text" style="width:98%" /></td>
	<td><input type="text" style="width:98%" /></td>
	<td><input type="text" style="width:98%" value="0"/></td>
	<td><input type="text" style="width:98%" value="0"/></td>
	<td><input type="text" style="width:98%" value="0"/></td>
	<td style="width: 50px;"></td> 
</tr>
<tr>
	<td style="width: 50px"><input type="text" style="width:100%;display: none" value="" /></td>	
	<td style="width: 115px">预备费</td>
	<td><input type="text" style="width:98%" /></td>
	<td><input type="text" style="width:98%" /></td>
	<td><input type="text" style="width:98%" value="0"/></td>
	<td><input type="text" style="width:98%" value="0"/></td>
	<td><input type="text" style="width:98%" value="0"/></td>
	<td style="width: 50px;"></td> 
</tr>
</tbody>
</table></form>
<table border="0.5" width="90%">
<tr>
<td style="width: 50px;">小计2</td>
<td style="width: 50px;" id="cost_b">0</td>
</tr>
</table>
<form id="form2">
<table border="1" width="90%" id="taba">
<tbody id="tbody2">
<tr>
	<td style="width: 50px"><input type="text" style="width:100%;display: none" value="" /></td>
	<td style="width: 115px">贷款利息</td>
	<td><input type="text" style="width:98%" /></td>
	<td><input type="text" style="width:98%" /></td>
	<td><input type="text" style="width:98%" value="0"/></td>
	<td><input type="text" style="width:98%" value="0"/></td>
	<td><input type="text" style="width:98%" value="0"/></td>
	<td style="width: 50px;"></td> 
</tr>
</tbody>
</table></form>
<table border="0.5" width="90%">
<tr>
<td style="width: 50px;">总计</td>
<td style="width: 50px;" id="cost_c">0</td>
</tr>
</table>

<input type="button" value="添加一行" onclick="add()"  id="add_button"/> <!--在添加按钮上添加点击事件 -->
</div>
</body>
</html>
<script src="<%=request.getContextPath()%>/vendor/jquery/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/js/public.js"></script>
<script src="<%=request.getContextPath()%>/js/common.js"></script>
<script src="../../js/hide.js"></script>
<script type="text/javascript"> 
function statisticalPrice(obj){
	//所有行的俩列相乘 放入到总预算里
	var price = $("#taba tr").children("td:eq(4)").find("input").val();
	var count = $("#taba tr").children("td:eq(5)").find("input").val();
	console.log(price*count);
}
var statues = "";
var bill_statue="";
//主表主键值
var ParentPKValue = '<%=request.getParameter("ParentPKValue")%>';
var pageCode = '<%=request.getParameter("pageCode")%>';
var keyWord = '<%=request.getParameter("keyWord")%>';//入口判断
var modifyId = '<%=request.getParameter("modifyId")%>';//变更执行申请ID
var modifyFieldName = '<%=request.getParameter("modifyFieldName")%>';//变更执行申请字段名'
var isHide = '<%=request.getParameter("isHide")%>';//是否隐藏及置灰
var version_history='<%=request.getParameter("version_history") %>';//历史版本号
var context='';
var buttonToken ="";
var token='';
var count =0;
var count1=0;
window.onload = function(){
	context='<%=request.getContextPath()%>';
	token='<%=request.getParameter("token")!=null?request.getParameter("token"):""%>';
	var tr=document.getElementsByTagName("tr");
	for(var i= 0;i<tr.length;i++){
		bgcChange(tr[i]);
	}
	// 鼠标移动改变背景,可以通过给每行绑定鼠标移上事件和鼠标移除事件来改变所在行背景色。
	var tab=document.getElementById("tbody");
	var tab1=document.getElementById("tbody1");
	var tab2=document.getElementById("tbody2");
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
	//debugger;
	if($.isEmptyObject(fill) == true){
		$.ajax({  
              url : "/system/projectHistory/budget?proj_released_id="+ParentPKValue+"&version_history="+version_history, 
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
	}
   		for(var i=0;i<fill.length;i++){
   			//循环表格数据动态添加行列信息
   			statues = "update";
   			var t0=document.createElement("td");
   			var t1=document.createElement("td");
			var t2=document.createElement("td");
			var t3=document.createElement("td");
			var t4=document.createElement("td");
			var t5=document.createElement("td");
			var t6=document.createElement("td");
			var t7=document.createElement("td");
			t0.innerHTML = '<input type="text"  style="width:100%;display:none" value="'+fill[i].ID+'" />'+(Number(i)+1);
			t0.style="width: 50px";
			if(i<1){
				t1.style="width: 115px";
				t1.rowSpan = "50";
			}
			t2.innerHTML='<input type="text" style="width:98%" value="'+fill[i].COST_PROJECT+'" />';
			t3.innerHTML='<input type="text" style="width:98%" value="'+fill[i].UNIT+'" />';
			t4.innerHTML='<input type="text" style="width:98%" value="'+fill[i].AMOUNT+'" />';
			t5.innerHTML='<input type="text" style="width:98%" value="'+fill[i].BUDGET_COUNT+'" />';
			
			var del=document.createElement("td");
			var tr="";
			if(fill[i].COST_TYPE=="主设备费用"){
				if(count1==0){
					$("#tbody").html("");
	   				count1=1;
	   			}
	   			
	   			t6.innerHTML='<input type="text" name="cost_a" style="width:98%" value="'+fill[i].INVESTMENT_ESTIMATION+'" onmousemove="total(\'cost_a\')"/>';
				tr=document.createElement("tr");
				tab.appendChild(tr);
				if(bill_statue!=""){
				 	if(bill_statue !="0" && bill_statue!= "7"){
				 		del.innerHTML="";
					}else{
						del.innerHTML="<a href='javascript:;' onclick='del(this,\""+fill[i].ID+"\")' >删除</a>";
					}
			 	}
				tr.appendChild(t0);
		   		if(i<1){
		   			t1.innerHTML = fill[i].COST_TYPE;
					tr.appendChild(t1);
		   		}
				tr.appendChild(t2);
				tr.appendChild(t3);
				tr.appendChild(t4);
				tr.appendChild(t5);
				tr.appendChild(t6);
				tr.appendChild(del);
	   		}else if(fill[i].COST_TYPE !="贷款利息"){
	   			if(count==0){
	   				$("#tbody1").html("");
	   				count=1;
	   			}
	   			t1.innerHTML = fill[i].COST_TYPE;
	   			t1.style="width: 115px";
	   			t6.innerHTML='<input type="text" name="cost_b" style="width:98%" value="'+fill[i].INVESTMENT_ESTIMATION+'" onchange="total(\'cost_b\')"/>';
	   			tr=document.createElement("tr");
	   			tab1.appendChild(tr);
	   			del.innerHTML="";
	   			del.style="width: 50px";
	   			tr.appendChild(t0);
				tr.appendChild(t1);
				tr.appendChild(t2);
				tr.appendChild(t3);
				tr.appendChild(t4);
				tr.appendChild(t5);
				tr.appendChild(t6);
				tr.appendChild(del);
	   		}else{
	   			$("#tbody2").html("");
	   			t1.innerHTML = fill[i].COST_TYPE;
	   			t1.style="width: 115px";
	   			t6.innerHTML='<input type="text" name="cost_c" style="width:98%" value="'+fill[i].INVESTMENT_ESTIMATION+'" onchange="total(\'cost_c\')"/>';
	   			tr=document.createElement("tr");
	   			tab2.appendChild(tr);
	   			del.innerHTML="";
	   			del.style="width: 50px";
	   			tr.appendChild(t0);
	   			tr.appendChild(t1);
				tr.appendChild(t2);
				tr.appendChild(t3);
				tr.appendChild(t4);
				tr.appendChild(t5);
				tr.appendChild(t6);
				tr.appendChild(del);
	   		}
	     }
	     total('cost_a');total('cost_b');total('cost_c')
	     if(keyWord == 1){//iframe
	     	$('a').removeAttr('onclick');
	     		$('a').removeAttr('onclick');
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
					$('a').removeAttr('onclick');
					$('a').removeAttr('onclick');
				}
			 }
		 }
		hide(isHide);//隐藏及置灰
} 
function bgcChange(obj)
{
obj.onmouseover=function(){
obj.style.backgroundColor="#f2f2f2";
}
obj.onmouseout=function(){
obj.style.backgroundColor="#fff";
}
}

// 编写一个函数，供添加按钮调用，动态在表格的最后一行添加子节点；


function add(){
	var tr=document.createElement("tr");
	var t0=document.createElement("td");
	var t1=document.createElement("td");
	var t2=document.createElement("td");
	var t3=document.createElement("td");
	var t4=document.createElement("td");
	var t5=document.createElement("td");
	var t6=document.createElement("td");
	t0.innerHTML='<input type="text" style="width:100%;display: none" value="" />';
	//t1.innerHTML='主设备费用';
	t2.innerHTML='<input type="text" style="width:98%" />';
	t3.innerHTML='<input type="text" style="width:98%" />';
	t4.innerHTML='<input type="text" style="width:98%" />';
	t5.innerHTML='<input type="text" style="width:98%" />';
	t6.innerHTML='<input type="text" style="width:98%" />';
	var del=document.createElement("td");
	del.innerHTML="<a href='javascript:;' onclick='del(this)' >删除</a>";
	var tab = document.getElementById("tbody");
	tab.appendChild(tr);
	tr.appendChild(t0);
	//tr.appendChild(t1);
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
	                j.ID = id;
	                saveDate.push(j);
	                console.log(saveDate);
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
//获取指定form中的所有的<input>对象    
	var data = [];
	var saveDate= [];
	
	
function jsonValue(){
	var j = {};
 	var  arr= document.getElementById('form').getElementsByTagName("*");
 	var l = arr.length;
	for(var i=0;i<l;i++){
	    if(arr[i].tagName && (arr[i].tagName=="INPUT" || arr[i].tagName=="SELECT")){
	    	data.push(arr[i].value);
	    }
 	}
 	//组装主设备费用 json
 	for(var k=0; k*6 < data.length;k++){
 		var j = {};
 		j.ID=data[k*6];
 		j.PROJ_SOURCE_ID = ParentPKValue;
 		j.COST_TYPE = '主设备费用';
  		j.COST_PROJECT = data[k*6+1];
	  	j.UNIT = data[k*6+2];
	  	j.AMOUNT = data[k*6+3];
	  	j.COUNT = data[k*6+4];
	  	j.INVESTMENT_ESTIMATION = data[k*6+5];
	  	j.DR='0';
	  	j.PROJ_TYPE = '0';
  		saveDate.push(j);
  	}
  	data = [];
  	//获取其他费用集合
  	var  arr1 = document.getElementById('form1').getElementsByTagName("*");
  	l = arr1.length;
  	for(var i=0;i<l;i++){
	    if(arr1[i].tagName && (arr1[i].tagName=="INPUT" || arr1[i].tagName=="SELECT")){
	    	data.push(arr1[i].value);
	    }
 	}
 	var typeStr = '';
 	for(var s=0; s*6 < data.length;s++){
 		var j = {};
 		j.PROJ_SOURCE_ID = ParentPKValue;
 		if(s==0){
 			typeStr='软件开发';
 		}else if(s==1){
 			typeStr='安装工程费';
 		}
 		else if(s==2){
 			typeStr='工程建设其他费';
 		}
 		else if(s==3){
 			typeStr='预备费';
 		}
 		j.ID=data[s*6];
 		j.COST_TYPE = typeStr;
  		j.COST_PROJECT = data[s*6+1];
	  	j.UNIT = data[s*6+2];
	  	j.AMOUNT = data[s*6+3];
	  	j.COUNT = data[s*6+4];
	  	j.INVESTMENT_ESTIMATION = data[s*6+5];
	  	j.DR='0';
	  	j.PROJ_TYPE = '0';
  		saveDate.push(j);
  	}
  	//获取贷款利息费用
  	data = [];
  	var  arr2 = document.getElementById('form2').getElementsByTagName("*");
  	l = arr2.length;
  	for(var i=0;i<l;i++){
	    if(arr2[i].tagName && (arr2[i].tagName=="INPUT" || arr2[i].tagName=="SELECT")){
	    	data.push(arr2[i].value);
	    }
 	}
 	for(var count=0; count*6 < data.length;count++){
 		var j = {};
 		j.ID=data[count*6];
 		j.PROJ_SOURCE_ID = ParentPKValue;
 		j.COST_TYPE = '贷款利息';
  		j.COST_PROJECT = data[count*6+1];
	  	j.UNIT = data[count*6+2];
	  	j.AMOUNT = data[count*6+3];
	  	j.COUNT = data[count*6+4];
	  	j.INVESTMENT_ESTIMATION = data[count*6+5];
	  	j.DR='0';
	  	j.PROJ_TYPE = '0';
  		saveDate.push(j);
  	}
  	return saveDate;
}
function saveBudget() {
	saveDate = jsonValue();
	console.log(saveDate);
  	console.log(saveDate);
  	data = [];
  	//判断是否先删除在插入
  	if(buttonToken=="deleteForProJect"){
		var dataMessage =saveDate;
	  	//alert(dataMessage);
	  	dataMessage=JSON.stringify(dataMessage);
	  	//alert(dataMessage)
	  	message = transToServer(findBusUrlByButtonTonken(buttonToken,'&table='+pageCode,''),dataMessage);
	  	console.log(message);
  	}
  	//集中保存
  	buttonToken = "addForProJect";
  	
  	if(buttonToken == "addForProJect"){
  		var dataMessage =saveDate;
  		dataMessage=JSON.stringify(dataMessage);
  		message = transToServer(findBusUrlByButtonTonken(buttonToken,'&table='+pageCode+"&button="+buttonToken,''),dataMessage);
  		//console.log(message);
  	};
  	saveDate=[];
  	if(message=="保存成功"){
  		alert(message);
  		location.reload();
  	}else{
  		alert('保存失败');
  	}
  	$("#ins_or_up_buttontoken").attr("disabled", true);
}   
var acost=0,bcost=0,ccost=0;
function total(id){
	//计算合计
	//获取自主设备费用 合计 
	var sum=0;
	var param = $("input[name='"+id+"']");
	console.log("input个数="+param.length);
	for(var i=0;i<param.length;i++){
		sum = Number(sum)+ Number(param[i].value);
	}
	if(id=="cost_a"){
		acost = sum; 
		$('#'+id).text(sum);
		$('#cost_c').text(acost+bcost+ccost);
	}else if(id=="cost_b"){
		bcost = sum;
		$('#'+id).text(sum);
		$('#cost_c').text(acost+bcost+ccost);
	}else if(id=="cost_c"){
		ccost = sum;
		$('#'+id).text(acost+bcost+ccost);
	}
	//console.log(sum);
}
$("body").on("click", "#tbody tr", function (){
	  var td = $(this).find("td");// 找到td元素
	  var trSeq = $(this).parent().find("tr").index($(this)[0]);
	  if(trSeq!=0){
		  var price = $(this).children("td:eq(3)").find("input").val();// 指定需要获取元素的下标即可
	      var count =$(this).children("td:eq(4)").find("input").val();// 指定需要获取元素的下标即可
	      console.log(price*count);
	      $(this).children("td:eq(5)").html('<input type="text" style="width:98%" name="cost_a" value="'+price*count+'" onmousemove="total(\'cost_a\')" />');
	      
	  	  //$(this).children("td:eq(5)").val(price*count);
	  }else{
	      var price = $(this).children("td:eq(4)").find("input").val();// 指定需要获取元素的下标即可
	      var count =$(this).children("td:eq(5)").find("input").val();// 指定需要获取元素的下标即可
	      console.log(price*count);
	      $(this).children("td:eq(6)").html('<input type="text" style="width:98%" name="cost_a" value="'+price*count+'" onmousemove="total(\'cost_a\')" />');
	      //获取所有行的最后一个字段合计
	  }
});

$("body").on("click", "#tbody1 tr", function (){
	  var td = $(this).find("td");// 找到td元素
      var price = $(this).children("td:eq(4)").find("input").val();// 指定需要获取元素的下标即可
      var count =$(this).children("td:eq(5)").find("input").val();// 指定需要获取元素的下标即可
      console.log(price*count);
      $(this).children("td:eq(6)").html('<input type="text" style="width:98%" value="'+price*count+'" name="cost_b" onmousemove="total(\'cost_b\')"  />');
});
$("body").on("click", "#tbody2 tr", function (){
	  var td = $(this).find("td");// 找到td元素
      var price = $(this).children("td:eq(4)").find("input").val();// 指定需要获取元素的下标即可
      var count =$(this).children("td:eq(5)").find("input").val();// 指定需要获取元素的下标即可
      console.log(price*count);
      $(this).children("td:eq(6)").html('<input type="text" style="width:98%" value="'+price*count+'" name="cost_c"  onmousemove="total(\'cost_c\')"  />');
});

</script> 