<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<title>子表模板</title>
<style type="text/css">

</style>
</head>
<%
	String token = request.getParameter("token");
%>
<body>
	<div class="panel panel-primary">
		<a class="btn btn-inverse" onclick="jump('201810050001');" style="margin-top:10px;">
			<span class="glyphicon glyphicon-ban-circle" aria-hidden="true"></span>返回
		</a>
		<input id="add" type="button" value="添加账龄区间" class="btn btn-info" style="margin-top:10px;">
		<div id="div1" style="margin-top:10px;">
			<table border="1" class="table table-hover table-bordered" style="text-align: center;  width:700px;" >
				<thead id="thead">
					<tr class="info" style="background-color: rgb(255, 255, 255);">
						<td>账龄区间方案</td>
						<td colspan="2">操作</td>
					</tr>
				</thead>
				<tbody id="tbody">
				</tbody>
			</table>
		</div>
		
		<div id="div2" style="display:none;">
			<input id="save" type="button" value="保存" class="btn btn-success">
			<input id="incLine" type="button" value="增行" class="btn btn-primary">
			<table border="1" class="table table-hover table-bordered" style="text-align: center;  width:700px; margin-top:10px;" >
				<thead id="thead1">
					<tr class="info" style="background-color: rgb(255, 255, 255);">
						<td>起始区间</td>
						<td>结束区间</td>
						<td>操作</td>
					</tr>
				</thead>
				<tbody id="tbody1">
				</tbody>
			</table>
		</div>
		
	</div>
			
</body>
<jsp:include page="../include/public.jsp"></jsp:include>
<script type="text/javascript">
var i = 0;
$(function() {
	$.ajax({
		
		type : "POST",
		url :  "/system/reportStatis?cmd=selectAgeRange",
		dataType : "json",
		async : false,
		cache : false,
		contentType : false,
		processData : false,
		success : function(data) {
			for(var i=0;i<data.body.rowdata.length;i++){
				var htmlstr = '';
				htmlstr+='<tr>';
				var programme = '';
				$.ajax({
		
					type : "POST",
					url :  "/system/reportStatis?cmd=selectAgeRangeSub&id="+data.body.rowdata[i].ID,
					dataType : "json",
					async : false,
					cache : false,
					contentType : false,
					processData : false,
					success : function(data1) {
						for(var i=0;i<data1.body.rowdata.length;i++){
							if(data1.body.rowdata[i].END_TIME!=null){
								programme += ' | '+data1.body.rowdata[i].START_TIME+'-'+data1.body.rowdata[i].END_TIME+'';
							}else{
								programme += ' | '+data1.body.rowdata[i].START_TIME+'以上';
							}
						}
					},
					error : function(){
						alert("查询失败");
					}
				});
				
				htmlstr+='<td>'+programme+'</td>';
				htmlstr+='<td><a href="#" class="btn btn-primary" onclick="jump('+data.body.rowdata[i].ID+');">应用</a></td>';
				if(i==0){
					htmlstr+='<td><a href="#" class="btn btn-primary disabled" onclick="jump1('+data.body.rowdata[i].ID+');">删除</a></td>';
				}else{
					htmlstr+='<td><a href="#" class="btn btn-primary" onclick="jump1('+data.body.rowdata[i].ID+');">删除</a></td>';
				}
				htmlstr+='</tr>';
				
				$("#tbody").append(htmlstr);
			}
		},
		error:function(){
			alert("查询失败");
		}
	});
	
	$("#add").click(function(){
		$("#div2").attr("style","display:block;");
		
	});
	
	$("#incLine").click(function(){
		
		var htmlstr = '';
		htmlstr+='<tr>';
		htmlstr+='<td><div class="col-md-7"><input type="text" name="start_time"  class="form-control"></div><div class="col-md-5"><select class="form-control"><option selected="selected" value="月">月</option><option value="年">年</option></select></div></td>';
		htmlstr+='<td><div class="col-md-7"><input type="text" name="end_time"  class="form-control"></div><div class="col-md-5"><select class="form-control"><option selected="selected" value="月">月</option><option value="年">年</option></select></div></td>';
		htmlstr+='<td><a href="#" class="btn btn-danger"  onclick="del(this);">删除</a></td>';
		htmlstr+='</tr>';
		$("#tbody1").append(htmlstr);
		
		i+=1;
		
	});
	
	
	var json = {};
	$("#save").click(function(){
		var aRang = [];
		for(var j=0;j<i;j++){
			var reg = new RegExp("^[0-9]*$");
			if(!reg.test($("#tbody1").find("tr").eq(j).find("td").eq(0).find("input").val())){
				alert("请检查是否含有非数字字符");
				return;
			}
			if(!reg.test($("#tbody1").find("tr").eq(j).find("td").eq(1).find("input").val())){
				alert("请检查是否含有非数字字符");
				return;
			}
			var dat = {start:$("#tbody1").find("tr").eq(j).find("td").eq(0).find("input").val()+$("#tbody1").find("tr").eq(j).find("td").eq(0).find("select").val(),end:$("#tbody1").find("tr").eq(j).find("td").eq(1).find("input").val()+$("#tbody1").find("tr").eq(j).find("td").eq(1).find("select").val()}
			aRang.push(dat);
		}
		json = JSON.stringify({aRang});
		console.log(json);
		$.ajax({
			type : "POST",
			url :  "/system/reportStatis?cmd=saveAgeRang&loop="+i,
			data: json,
			dataType : "json",
			async : false,
			cache : false,
			contentType : false,
			processData : false,
			success : function(data) {
				if(data.status=="success"){
					alert("保存成功");
					window.location.href="./ageRangSet.jsp?token=<%=token%>";
				}else{
					alert("保存失败,请检查是否含有非数字字符");
				}
				
			},
			error:function(){
				alert("保存失败,请检查是否含有非数字字符");
			}
		});
	
	});
	
	
});

function jump(id){
	window.location.href="./balanceAndAgeAnaly.jsp?token=<%=token%>&id="+id;
}

function jump1(id){
	var msg = "您确定要删除吗？";
	if (confirm(msg)==true){
		$.ajax({
			type : "POST",
			url :  "/system/reportStatis?cmd=delAgeRang&id="+id,
			dataType : "json",
			async : false,
			cache : false,
			contentType : false,
			processData : false,
			success : function(data) {
				if(data.status=="success"){
					alert("删除成功");
					window.location.href="./ageRangSet.jsp?token=<%=token%>";
				}else{
					alert("删除失败");
				}
				
			},
			error:function(){
				alert("删除失败");
			}
		});
	}
}

function del(t){
	i-=1;
	if(i<=4){
		$("#incLine").removeAttr("disabled");
	}
	$(t).parent().parent().remove();  
}

</script>
</html>