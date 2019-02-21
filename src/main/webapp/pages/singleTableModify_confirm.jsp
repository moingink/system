<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<title>子表模板</title>

<link rel="stylesheet" type="text/css" href="../easyui/demo/demo.css">
<link rel="stylesheet" type="text/css" href="../easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="../easyui/themes/icon.css">
<script type="text/javascript" src="../easyui/jquery.min.js"></script>
<script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="../easyui/locale/easyui-lang-zh_CN.js"></script>

<link type="text/css" rel="stylesheet" href="jDate/test/jeDate-test.css">
<link type="text/css" rel="stylesheet" href="jDate/skin/jedate.css">
<script type="text/javascript" src="jDate/src/jedate.js"></script>

</head>
<%
	String ContextPath =request.getContextPath();
%>
<body>

	<form class="form-horizontal">
		<div class="panel panel-primary">

			<div class="panel-body" id="bulidTable">
			
				<div id="toolbar" style="margin-bottom: 25px;margin-top: 25px;">
					<button type="button" class="btn btn-success" id="makesure"  >
						<span class="glyphicon glyphicon-saved" aria-hidden="true"></span>
						确认
					</button>
					<button type="button" class="btn btn-inverse"   onclick="$('.close',window.parent.document).click();">
						<span class="glyphicon glyphicon-ban-circle" aria-hidden="true"></span>
						取消
					</button>
					<div id='button_div'></div>
				</div>
				<div class="panel panel-primary" style="padding: 5px;">
					暂估收入：<input type="number" width="70px;" id="zgsr" value="" readonly="readonly" disabled="true"/>
					收入待分配：<input type="number" width="70px;" id="srdfp" value="" readonly="readonly" disabled="true"/>
					<br/>
					调整收入：<input type="number" width="70px;" id="tzsr" value="" style="margin-top: 5px;"readonly="readonly"disabled="true" />
					确认收入：<input type="number" width="70px;" id="qrsr" value="" style="margin-top: 5px;"readonly="readonly"disabled="true" />
					<table border="1" width="80%" class="table table-hover" style="margin-top: 50px;">
						<thead>
							<tr class="info" style="background-color: rgb(255, 255, 255);">
								<th style="width: 100px;">收入实际发生月份</th>
								<th style="width: 100px;">确认收入</th>
								<th  >操作</th>
							</tr>
						</thead>
						<tbody id="tbody">
						</tbody>
					</table>
					<button type="button" id="addTR" style="margin-top: 5px;" >
						添加一行
					</button>
				</div>
			</div>

		</div>
	</form>
			
</body>
<jsp:include page="../include/public.jsp"></jsp:include>

<script type="text/javascript">

$(function() {
	$("#zgsr").val("<%= request.getParameter("ESTIMATED_INCOME") %>");
	$("#qrsr").val("<%= request.getParameter("CONFIRM_INCOME") %>");
	if("<%= request.getParameter("CONFIRM_INCOME") %>"==""){
		$("#srdfp").val("<%= request.getParameter("CONFIRM_INCOME") %>" );
	}
	$("#tzsr").val("<%= request.getParameter("ADJUST_INCOME") %>");
		
	var flag=0;
   	$("#addTR").click(function(){
		if(parseFloat($("#qrsr").val())-parseFloat(getTotalCharge())<=0){
			alert("已没有待分配的收入!");
			return;
		}
   		var htmlStr='<tr>'+
							'<td><input type="text" name="happenDate" class="happenDate" id="happenDate'+flag+'" onclick="" readonly="readonly" /></td>'+
							'<td><input type="number" name="charge" class="charge" /></td>'+
							'<td><a onclick="$(this).parent().parent().remove();">删除</a></td>'+
						'</tr>';
   		$("#tbody").append(htmlStr);
		$(".happenDate").attr("readonly","true");
   	    jeDate("#happenDate"+flag,{
	        format: "YYYYMM"
	    });
   		flag++;
	   	$(".happenDate").change(function(){
		   	if(!checkDateInput($(this).val())){
		   		alert("月份重复");
		   		$(this).val('');
		   	}
	   	});
	   	$(".charge").on("input",function(){
			if(parseFloat($("#qrsr").val())-parseFloat(getTotalCharge())<0){
				$(this).val("");
				$("#srdfp").val((parseFloat($("#qrsr").val())-parseFloat(getTotalCharge())).toFixed(2));
			}else{
				$("#srdfp").val((parseFloat($("#qrsr").val())-parseFloat(getTotalCharge())).toFixed(2));
			}
		});
	   	$(".charge").on("change",function(){
			$(this).val(parseFloat($(this).val()).toFixed(2));
		});
   	});
   	$("#makesure").click(function(){
		for(var i=0;i<$(".charge").length;i++){
			if($($(".charge")[i]).val()==""){
				alert("确认收入不能为空");
				return;
			}
		}
		for(var i=0;i<$(".happenDate").length;i++){
			if($($(".happenDate")[i]).val()==""){
				alert("发生月份不能为空");
				return;
			}
		}
		var totalCharge=0;
		for(var i=0;i<$(".charge").length;i++){
			totalCharge+=parseFloat($($(".charge")[i]).val());
		}
  		if(totalCharge!=$("#qrsr").val()){
	   		alert("确认收入总金额与确认收入金额不相等");
			return;
	   	}
	   	var happenDateStr="";
		for(var i=0;i<$(".happenDate").length;i++){
			happenDateStr+=$($(".happenDate")[i]).val();
			happenDateStr+=":";
			happenDateStr+=$($(".charge")[i]).val();
			if(i!=$(".happenDate").length-1){
				happenDateStr+=";";
			}
		}
		$('#CONFIRM_MONTH',window.parent.document).val(happenDateStr);
		$('.close',window.parent.document).click();
   	});
	   	
function checkDateInput(dateStr){
	var times=0;
	for(var i=0;i<$(".happenDate").length;i++){
		var obj=($(".happenDate")[i]);
		if($(obj).val()==dateStr)times++;
	}
	return times==1;
}
function getTotalCharge(){
	var totalCharge=0;
	for(var i=0;i<$(".charge").length;i++){
		if($($(".charge")[i]).val()!=""){
			totalCharge+=parseFloat($($(".charge")[i]).val());
		}
	}
	return totalCharge;
}
		
initHappenDate();
function initHappenDate(){
	if("<%= request.getParameter("CONFIRM_INCOME") %>"=="")return;
	var strList="<%= request.getParameter("CONFIRM_MONTH") %>".split(";");
	for(var i=0;i<strList.length;i++){
   		var htmlStr='<tr>'+
							'<td><input type="text" name="happenDate" class="happenDate" id="happenDate'+flag+'" onclick="" readonly="readonly" value="'+strList[i].split(":")[0]+'"/></td>'+
							'<td><input type="number" name="charge" class="charge"  value="'+strList[i].split(":")[1]+'"/></td>'+
							'<td><a onclick="$(this).parent().parent().remove();">删除</a></td>'+
						'</tr>';
   		$("#tbody").append(htmlStr);
   		$(".happenDate").attr("readonly","true");
   	    jeDate("#happenDate"+flag,{
	        format: "YYYYMM"
	    });
	    flag++;
	   	$(".happenDate").change(function(){
		   	if(!checkDateInput($(this).val())){
		   		alert("月份重复");
		   		$(this).val('');
		   	}
	   	});
	   	$(".charge").on("input",function(){
			if(parseFloat($("#qrsr").val())-parseFloat(getTotalCharge())<0){
				$(this).val("");
				$("#srdfp").val((parseFloat($("#qrsr").val())-parseFloat(getTotalCharge())).toFixed(2));
			}else{
				$("#srdfp").val((parseFloat($("#qrsr").val())-parseFloat(getTotalCharge())).toFixed(2));
			}
		});
		
	   	$(".charge").on("change",function(){
			$(this).val(parseFloat($(this).val()).toFixed(2));
		});
	}
	$("#srdfp").val((parseFloat($("#qrsr").val())-parseFloat(getTotalCharge())).toFixed(2));
}
		
});

</script>
<script type="text/javascript" src="jDate/test/demo.js"></script>   
</html>