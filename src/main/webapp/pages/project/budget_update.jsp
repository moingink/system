<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<link href="<%=request.getContextPath()%>/vendor/bootstrap/css/bootstrap.css" rel="stylesheet" />
<link href="<%=request.getContextPath()%>/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet" /> 
<link href="<%=request.getContextPath()%>/vendor/bootstrap-table/src/bootstrap-table.css" rel="stylesheet" /> 
<%@ page import="com.yonyou.business.RmDictReferenceUtil"%>
<%@ page import="java.util.*" %>
<%@ page import="java.util.concurrent.ConcurrentHashMap"%>
<%@ page import="java.util.Map.Entry" %>
<%@ page import="com.yonyou.util.BussnissException" %>
<%
	
			ConcurrentHashMap<String,String> message = RmDictReferenceUtil.getDictReference("PROCUREMENT_METHOD");
			System.out.println(message);
			System.out.println(message.keySet());
			StringBuffer html =new StringBuffer();
			
			html.append("<select id='cgfs' name='cgfs' class='form-control' style='border:none;'>").append("<option  selected = 'selected' value=''>==请选择==</option>");
			
			//String input_formart =message.get("2");
			ConcurrentHashMap<String, String> selectMap=null;
			try {
				//selectMap = RmDictReferenceUtil.getDictReference("2");
				if(message!=null&&message.size()>0){
					for (Entry<String, String> entry : message.entrySet()) {
						String tempKey = entry.getKey();
						String tempValue = message.get(tempKey);
						html.append("<option value='" + tempKey + "' ");
						//if(tempKey.equalsIgnoreCase(value)){
							//html.append(" selected = 'selected' ");
						//}
						html.append(">" + tempValue + "</option>");
					}
					System.out.println(html.toString());
				}
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}

	%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>项目预算</title>
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
<div style="overflow: auto;">
<button onclick="saveBudget()" id="ins_or_up_buttontoken" class="btn btn-success">保存</button>
<input type="button" value="修改" onclick="update();" id="update_button" class="btn btn-warning" />
<input type="button" class="btn btn-primary" value="添加一行" onclick="addLine();"  id="add_button"/> <!--在添加按钮上添加点击事件 -->
<form id="form" style="margin-top:10px;">

<table id="table" class="table table-bordered">
<thead>
	<tr class="info">		
		<th style="width:50px;">序号</th>
		<th style="width:150px;">成本大类</th>
		<th style="width:150px;">成本类别</th>
		<th>费用项目</th>
		<th>单位</th>
		<th>单价(万元)</th>
		<th>数量</th>
		<th>金额(万元)</th>
		<th>自建/采购方式</th>
		<th style="width:60px;">操作</th>
	</tr>			
</thead>


<tbody id="tbody">

</tbody>
</table>
</form>

<table class="table table-bordered" style="width:300px;">
<tr>
	<td style="width:200px;">技术外包服务费</td>
	<td id="cost_class_a">10000</td>
</tr>
<tr>
	<td style="width:200px;">自建软件</td>
	<td id="cost_class_b">0</td>
</tr>
<tr>
	<td style="width:200px;">第三方软硬件</td>
	<td id="cost_class_c">0</td>
</tr>
<tr>
	<td style="width:200px;">其他</td>
	<td id="cost_class_d">0</td>
</tr>
<tr>
	<td style="width:200px;">总计</td>
	<td id="cost_c">0</td>
</tr>
</table>

</div>
</body>
</html>
<script src="<%=request.getContextPath()%>/vendor/jquery/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/js/public.js"></script>
<script src="<%=request.getContextPath()%>/js/common.js"></script>
<script src="../../js/hide.js"></script>
<script type="text/javascript">
var id = "";

function statisticalPrice(obj){
	//所有行的俩列相乘 放入到总预算里
	var price = $("#taba tr").children("td:eq(4)").find("input").val();
	var count = $("#taba tr").children("td:eq(5)").find("input").val();
	//console.log(price*count);
}
var statues = "";
var currSelecttTr=0;
var bill_statue="0";
//主表主键值
var ParentPKValue = '<%=request.getParameter("ParentPKValue")%>';
var pageCode = '<%=request.getParameter("pageCode")%>';
var keyWord = '<%=request.getParameter("keyWord")%>';//入口判断
var modifyId = '<%=request.getParameter("modifyId")%>';//变更执行申请ID
var modifyFieldName = '<%=request.getParameter("modifyFieldName")%>';//变更执行申请字段名'
var isHide = '<%=request.getParameter("isHide")%>';//是否隐藏及置灰
var context='';
var buttonToken ="";
var token='';
var count =0;
var count1=0;
var project_cost;
var selection ='<select id="cost_type" name="costtype" class="form-control" style="border:none;"><option value="0"></option><option value="1">主设备费用</option><option value="2">软件开发</option><option value="3">安装工程费</option><option value="4">工程建设其他费</option><option value="5">预备费</option><option value="6">贷款利息</option></select>';

var selectionType ='<select id="cost_class" name="cost_class" class="form-control" style="border:none;" onchange="statistical_Amount()"><option value="0"></option><option value="1">技术外包服务费</option><option value="2">自建软件</option><option value="3">第三方软硬件</option><option value="4">其他</option></select>';
window.onload = function(){
	context='<%=request.getContextPath()%>';
	token='<%=request.getParameter("token")!=null?request.getParameter("token"):""%>';
	var tr=document.getElementsByTagName("tr");
	for(var i= 0;i<tr.length;i++){
		bgcChange(tr[i]);
	}
	
	 $.ajax({  
         url : "/system/project/proposalStatue?proj_source_id="+ParentPKValue,  
         dataType : "json",  
                     type : "GET",  
                     async: false, 
                     success : function(data) {
                     	if(data.length>0){
			            	project_cost = data[0].PROJECT_COST;
		            	}else{
		            		project_cost=0;
		            	}
                     }
  	 	});
	// 鼠标移动改变背景,可以通过给每行绑定鼠标移上事件和鼠标移除事件来改变所在行背景色。
	var tab=document.getElementById("tbody");
	var tab1=document.getElementById("tbody1");
	var tab2=document.getElementById("tbody2");
	var fill = new Object();
	if($.isEmptyObject(fill) == true){
		$.ajax({  
              url : "/system/project/budget?proj_source_id="+ParentPKValue,  
              dataType : "json",  
                          type : "GET",  
                          async: false, 
                          success : function(data) {
                          	fill = data;
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
	   			var tr=document.createElement("tr");
	   			var t0=document.createElement("td");
	   			var ta=document.createElement("td");
	   			var t1=document.createElement("td");
				var t2=document.createElement("td");
				var t3=document.createElement("td");
				var t4=document.createElement("td");
				var t5=document.createElement("td");
				var t6=document.createElement("td");
				var t7=document.createElement("td");
				var t8=document.createElement("td");
				t0.innerHTML = '<input type="text" class="form-control" style="width:100%;display:none;" value="'+fill[i].ID+'" />'+(Number(i)+1);
				t0.style="width: 50px";
				//t1.innerHTML='<input type="text" style="width:98%" value="'+fill[i].COST_TYPE+'" />';
				ta.innerHTML=selectionType;
				t1.innerHTML=selection;
				
				t2.innerHTML='<input type="text" class="form-control" style="width:100%;border:none;" value="'+fill[i].COST_PROJECT+'" />';
				t3.innerHTML='<input type="text" class="form-control" style="width:100%;border:none;" value="'+fill[i].UNIT+'" />';
				t4.innerHTML='<input type="text" class="form-control" style="width:100%;border:none;" value="'+fill[i].AMOUNT+'"  />';
				t5.innerHTML='<input type="text" class="form-control" style="width:100%;border:none;" value="'+fill[i].BUDGET_COUNT+'"  />';
		   		t6.innerHTML='<input type="text" name="cost_a" class="form-control" style="width:100%;border:none;" value="'+fill[i].INVESTMENT_ESTIMATION+'" />';
		   		t7.innerHTML="<%=html.toString()%>";
	   		
				var del=document.createElement("td");
				if(bill_statue!=""){
					if(bill_statue !="0" && bill_statue!= "7"){
					 	del.innerHTML="";
					}else{
						del.innerHTML="<a href='javascript:;' class='btn btn-danger' onclick='del(this,\""+fill[i].ID+"\")' >删除</a>";
					}
				 }
				tab.appendChild(tr);
				tr.appendChild(t0);
				tr.appendChild(ta);
				tr.appendChild(t1);
				tr.appendChild(t2);
				tr.appendChild(t3);
				tr.appendChild(t4);
				tr.appendChild(t5);
				tr.appendChild(t6);
				tr.appendChild(t7);
				tr.appendChild(del);
				var id = "cgfs"+i;
				var ida = "cost_type"+i;
				var tid = "cost_class"+i;
				$("#cgfs").attr("id",id);
				$("#cost_type").attr("id",ida);
				$("#cost_class").attr("id",tid);
				$("#"+id+" option[value='"+fill[i].PROCUREMENT_METHOD+"']").attr("selected","selected");
				$("#"+ida+" option[value='"+fill[i].COST_TYPE+"']").attr("selected","selected");
				$("#"+tid+" option[value='"+fill[i].COST_CLASS+"']").attr("selected","selected");
	   		}
	     
	     total('cost_a');total('cost_b');total('cost_c');
	     statistical_Amount();
	    
	//****默认第一次 按钮只能点击修改,页面不能编辑,点击修改后才可以编辑。再次保存置灰所有****//
	$(":input").each(function(){
		$(this).attr("disabled",true);
	});
	$("select").each(function(){
		$(this).attr("disabled",true);
	});
	$("#update_button").attr("disabled", false);
	//******************************************//
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
function isRealNum(val){
    // isNaN()函数 把空串 空格 以及NUll 按照0来处理 所以先去除
    if(val === "" || val ==null){
        return false;
    }
    if(!isNaN(val)){
        return true;
    }else{
        return false;
    }
} 
function isStartingWhitZero(val){
	//var patrn = /^([1-9]\d*|0)(\.\d*[1-9])?$/;
	var v = val.substring(0,1);
	
	if(v=="0"){
		return false;
	}else{
		return true;
	}
}
 
     
// 编写一个函数，供添加按钮调用，动态在表格的最后一行添加子节点；
function addLine(){
	//如果tbody 有数据行则 加入设备费用  否则就不加入
	var rowCount = $('#tbody tr').length;
	var tab = document.getElementById("tbody");
	var newTr = tab.insertRow(currSelecttTr);
	var newTd0 = newTr.insertCell();
	newTd0.innerHTML='<input type="text" class="form-control"style="width:100%;display: none;border:none;" value="" />';
	newTd0.style="width: 50px";
	var newTdA = newTr.insertCell();
	newTdA.innerHTML=selectionType;
	var newTd1 = newTr.insertCell();
	newTd1.innerHTML=selection;
	
	var newTd2 = newTr.insertCell();
	newTd2.innerHTML='<input type="text" class="form-control" style="width:100%;border:none;" />';
	var newTd3 = newTr.insertCell();
	newTd3.innerHTML='<input type="text" class="form-control" style="width:100%;border:none;" />';
	var newTd4 = newTr.insertCell();
	newTd4.innerHTML='<input type="text" class="form-control" style="width:100%;border:none;" />';
	var newTd5 = newTr.insertCell();
	newTd5.innerHTML='<input type="text" class="form-control" style="width:100%;border:none;" />';
	var newTd6 = newTr.insertCell();
	newTd6.innerHTML='<input type="text" class="form-control" style="width:100%;border:none;" readonly="" />';
	var newTd7 = newTr.insertCell();
	newTd7.innerHTML="<%=html.toString()%>";
	var newTd8 = newTr.insertCell();
	newTd8.innerHTML="<a href='javascript:;' class='btn btn-danger' onclick='del(this)' >删除</a>";
}



$("body").on("click", "#tbody tr", function (){
    id="tbody"
	currSelecttTr = $(this).parent().find("tr").index($(this)[0])+1;
});
$("body").on("click", "#tbody1 tr", function (){
	id="tbody1"
	currSelecttTr = $(this).parent().find("tr").index($(this)[0])+1;
});
$("body").on("click", "#tbody2 tr", function (){
	id="tbody2"
	currSelecttTr = $(this).parent().find("tr").index($(this)[0])+1;
});

// 创建删除函数
function del(obj,id){
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
			    	//total('cost_a');
			    	location.reload();
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
 	var  arr= document.getElementById('tbody').getElementsByTagName("*");
 	var l = arr.length;
	for(var i=0;i<l;i++){
	    if(arr[i].tagName && (arr[i].tagName=="INPUT" || arr[i].tagName=="SELECT")){
	    	data.push(arr[i].value);
	    }
 	}
 	//组装主设备费用 json
 	for(var k=0; k*9 < data.length;k++){
 		var j = {};
 		j.ID=data[k*9];
 		j.PROJ_SOURCE_ID = ParentPKValue;
 		j.COST_CLASS = data[k*9+1];
 		j.COST_TYPE = data[k*9+2];;
  		j.COST_PROJECT = data[k*9+3];
	  	j.UNIT = data[k*9+4];
	  	j.AMOUNT = data[k*9+5];
	  	j.BUDGET_COUNT = data[k*9+6];
	  	j.INVESTMENT_ESTIMATION = data[k*9+7];
	  	j.PROCUREMENT_METHOD = data[k*9+8];
	  	j.DR='0';
	  	j.PROJ_TYPE = '0';
  		saveDate.push(j);
  	}
  	data = [];
  	return saveDate;
}
function saveBudget() {
	
	//判断预算金额与理想建设成本是否一致,不一致提示 不能保存。
	var budget_cost =$('#cost_c').text();
	console.log(project_cost+"="+budget_cost);
	if(project_cost != budget_cost){
		alert('编制预算总金额 '+budget_cost+'万元与项目概况中项目立项建设总成本'+project_cost+'万元，录入金额不一致，请将金额调整为一致后再保存数据');
		return;
	}
	saveDate = jsonValue();
  	data = [];
  	//判断是否先删除在插入
  	if(buttonToken=="deleteForProJect"){
		var dataMessage =saveDate;
	  	//alert(dataMessage);
	  	dataMessage=JSON.stringify(dataMessage);
	  	//alert(dataMessage)
	  	message = transToServer(findBusUrlByButtonTonken(buttonToken,'&table='+pageCode,''),dataMessage);
	  	//console.log(message);
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
  		buttonToken ="deleteForProJect";
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
var acost=0,bcost=0,ccost=0;
function total(id){
	//计算合计
	//获取自主设备费用 合计 
	var sum=0;
	var param = $("input[name='"+id+"']");
	//console.log("input个数="+param.length);
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

$("body").on("keyup", "#tbody tr", function (){
	  var td = $(this).find("td");// 找到td元素
	  var trSeq = $(this).parent().find("tr").index($(this)[0]);
	      var price = $(this).children("td:eq(5)").find("input").val();// 指定需要获取元素的下标即可
	      var count =$(this).children("td:eq(6)").find("input").val();// 指定需要获取元素的下标即可
	      if(isRealNum(price)==false){
	      	$(this).children("td:eq(5)").html('<input type="text" class="form-control" style="width:100%;border:none;"  value="" />');
	      	$(this).children("td:eq(7)").html('<input type="text" class="form-control" style="width:100%;border:none;" readonly=""  name="cost_a" value="" />');
	      	total('cost_a');
	      }else{
	      		if(price.indexOf(".") != -1 && price.toString().split(".")[1].length>3){
	      		price = Math.floor(parseFloat(price) * 1000) / 1000;
	      		$(this).children("td:eq(5)").html('<input type="text" class="form-control" style="width:100%;border:none;"  value="'+price+'" />');
	      	}
	      }
	      if(isRealNum(count)==false){
	      	$(this).children("td:eq(6)").html('<input type="text" class="form-control" style="width:100%;border:none;"  value="" />');
	     	$(this).children("td:eq(7)").html('<input type="text" class="form-control" style="width:100%;border:none;" readonly=""  name="cost_a" value="" />');
	     	total('cost_a');
	      }else{
	      	if(count.indexOf(".") != -1 && count.toString().split(".")[1].length>2){
	      		count = Math.floor(parseFloat(count) * 100) / 100;
	      		$(this).children("td:eq(6)").html('<input type="text" class="form-control" style="width:100%;border:none;"  value="'+count+'" />');
	      	}
	      }
	      if(isRealNum(price)==true&&isRealNum(count)==true){
	      	var m = Math.floor(parseFloat(price*count) * 100) / 100;;
		  	$(this).children("td:eq(7)").html('<input type="text" class="form-control" style="width:100%;border:none;" readonly=""  name="cost_a" value="'+m+'" />');
	      	//计算大类合计方法 每次输入完成后统计各大类的金额合计
	      	statistical_Amount();
	      }
	      //console.log("总price="+price*count);
	      //获取所有行的最后一个字段合计
	      total('cost_a');
	  
});

function statistical_Amount(){
    var trList = $("#tbody").children("tr");
    var cost_class_a="";
    var cost_class_b="";
    var cost_class_c="";
    var cost_class_d="";
    for (var i=0;i<trList.length;i++) {
        var tdArr = trList.eq(i).find("td");
        var cost_class = tdArr.eq(1).find('select').val();//成本大类
        var total_amount = tdArr.eq(7).find('input').val();//收入金额
        if(cost_class==1){
        	cost_class_a= Number(total_amount)+Number(cost_class_a);
        }else if(cost_class==2){
        	cost_class_b = Number(total_amount)+Number(cost_class_b);
        }else if(cost_class==3){
        	cost_class_c = Number(total_amount)+Number(cost_class_c);
        }else{
        	cost_class_d = Number(total_amount)+Number(cost_class_d);
        }
    }
	$('#cost_class_a').text(cost_class_a);
	$('#cost_class_b').text(cost_class_b);
	$('#cost_class_c').text(cost_class_c);
	$('#cost_class_d').text(cost_class_d);   

}

$("body").on("keyup", "#tbody1 tr", function (){
	  var td = $(this).find("td");// 找到td元素
      var price = $(this).children("td:eq(4)").find("input").val();// 指定需要获取元素的下标即可
      
      var count =$(this).children("td:eq(5)").find("input").val();// 指定需要获取元素的下标即可
      //console.log(price*count);
      $(this).children("td:eq(6)").html('<input type="text" class="form-control" style="width:100%;border:none;" value="'+price*count+'" name="cost_b" />');
 	  total('cost_b');
});
$("body").on("keyup", "#tbody2 tr", function (){
	  var td = $(this).find("td");// 找到td元素
      var price = $(this).children("td:eq(4)").find("input").val();// 指定需要获取元素的下标即可
      var count =$(this).children("td:eq(5)").find("input").val();// 指定需要获取元素的下标即可
      //console.log(price*count);
      $(this).children("td:eq(6)").html('<input type="text" class="form-control" style="width:100%;border:none;" value="'+price*count+'" name="cost_c"    />');
	  total('cost_c');
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