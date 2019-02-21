<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<title>子表模板</title>
</head>
<%
	String ContextPath =request.getContextPath();
	String token = request.getParameter("token");
%>

<body>

	<form class="form-horizontal">
		<div class="panel panel-primary">
		
		<div class="col-md-12" style="margin-top:10px;margin-bottom:10px;">
				<div class="col-md-4">
					<div class="col-md-4">
						<label style="width:100%;text-align:right;">分析维度：</label>
					</div>
					<div class="col-md-8">
						<select class="Analysis_dimension form-control" id="Analysis_dimension1">
							<option selected="selected" value="1">业务账期</option>
							<option value="2">财务账期</option>
						</select>
					</div>				
				</div>
				<div class="col-md-4" style="margin-left:-30px">
					<div class="col-md-4">
						<label style="width:100%;text-align:right;">账龄分析时点：</label>
				  	</div>
				  	<div class="input-group date form_date" style=" data-date-format="dd MM yyyy" data-link-format="yyyy-mm-dd">
					  	<input type="TEXT" class="form-control" id="ageTime" readonly="readonly">
					  	<span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
					  	<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
					</div>				
				</div>
				
				<div class="col-md-3">			
					<input class="btn btn-info" type="button" id="ageRangeSet" value="账龄区间设置"/>
					<input class="btn btn-info" type="button" id="urge" value="催办"/>
				</div>
				
			</div>
		
		
		
		

			
			<div id="div1">
				<div class="tb" style="overflow-x:auto; clear:both;">
					
					<table border="1" class="table table-hover table-bordered text-nowrap" style="text-align: center;vertical-align: inherit;" >
						<thead id="thead">
							<tr class="info">
								<td rowspan="2">客户</td>
								<td rowspan="2">营销单元</td>
								<td rowspan="2">项目编号</td>
								<td rowspan="2">项目名称</td>
								<td rowspan="2">合同编号</td>
								<td rowspan="2">合同名称</td>
								<td rowspan="2">合同回款期</td>
								<td rowspan="2">客户经理</td>
							</tr>
							<tr id="ageRange">
							</tr>
						</thead>
						<tbody id="tbody">
						</tbody>
					</table>
				</div>
			</div>
			
			<div id="page">
			</div>
		</div>
		<!-- <input type="hidden" id="currentPage" value="1"/>
		<input type="hidden" id="total" value="0"/> -->
	</form>

	
			
</body>
<jsp:include page="../include/public.jsp"></jsp:include>
<script type="text/javascript">

var id = <%=request.getParameter("id")%>;
var array = [];
if(id==null){
	id='201810050001';
}
$(function() {

	loadList('{analysisDimension:"1"}');

	$.ajax({
		type : "POST",
		url :  "/system/reportStatis?cmd=selectAgeRangeSub&id="+id,
		dataType : "json",
		async : false,
		cache : false,
		contentType : false,
		processData : false,
		success : function(data) {
			if(data.status=="success"){
				var cols=0;
				for(var i=0;i<data.body.rowdata.length;i++){
					if(data.body.rowdata[i].END_TIME!=null && i==data.body.rowdata.length-1)
						cols+=2;
					else
						cols+=1;
				}
				$("#thead .info").append('<td colspan="'+cols+'">账龄区间</td>');
				$("#thead .info").append('<td rowspan="2">合计</td>');
				for(var i=0;i<data.body.rowdata.length;i++){
					var htmlstr='';
					if(data.body.rowdata[i].END_TIME!=null){
						if(i==data.body.rowdata.length-1){
							htmlstr+='<td>'+data.body.rowdata[i].START_TIME+'-'+data.body.rowdata[i].END_TIME+'</td>';
							htmlstr+='<td>'+data.body.rowdata[i].END_TIME+'以上</td>';
						}else{
							htmlstr+='<td>'+data.body.rowdata[i].START_TIME+'-'+data.body.rowdata[i].END_TIME+'</td>';
						}
					}else{
						htmlstr+='<td>'+data.body.rowdata[i].START_TIME+'以上</td>';
					}
					
					$("#ageRange").append(htmlstr);
				}
			}else{
				alert("查询失败");
			}
		},
		error:function(){
			alert("查询失败");
		}
	});
	
	$('.form_date').attr("readonly","true").datetimepicker({
   		minView: 'month',         //设置时间选择为年月日 去掉时分秒选择
  		format:'yyyy-mm-dd',
   	    weekStart: 1,
   	    todayBtn:  1,
   	    autoclose: true,
   	    todayHighlight: 1,
   	    startView: 2,
   	    forceParse: 0,
   	    showMeridian: 1,
   	    pickerPosition: "bottom-left",
   	    language: 'zh-CN'              //设置时间控件为中文
   	});
   	
	$("#Analysis_dimension1").change(function(){
		var json={};
		if($("#Analysis_dimension1").val()!=""){
			json.analysisDimension=$("#Analysis_dimension1").val();
		}
		if($("#ageTime").val()!=""){
			json.ageTime=$("#ageTime").val();
		}
		loadList(JSON.stringify(json));
	});
	
	$("#ageTime").change(function(){
		var json={};
		if($("#Analysis_dimension1").val()!=""){
			json.analysisDimension=$("#Analysis_dimension1").val();
		}
		if($("#ageTime").val()!=""){
			json.ageTime=$("#ageTime").val();
		}
		loadList(JSON.stringify(json));
	});
	
	$("#ageRangeSet").click(function(){
		window.location.href="./ageRangSet.jsp?token=<%=token%>";
	});

});

function loadList(json){
	$("#tbody").html("");
	$.ajax({
		type : "POST",
		url :  "/system/reportStatis?cmd=balanceAndAgeAnaly&token=<%=token%>&id="+id,
		data : json,
		dataType : "json",
		async : false,
		cache : false,
		contentType : false,
		processData : false,
		success : function(data) {
			if(data.status=="success"){
				if(data.body.rowdata.length==0){
					$("#tbody").append('<tr><td colspan="100">暂时无数据</td></tr>');
				}else{
					for(var i=0;i<data.body.rowdata.length;i++){
						var jsonDate = data.body.rowdata[i];
						var count = 0;
						for (var key in jsonDate) {
						    if(key.indexOf("CB") != -1){
						    	count+=1;
						    }
						}
						array.push(count);
					}
					for(var i=0;i<data.body.rowdata.length;i++){
					//debugger;
						var htmlstr='';
						htmlstr+='<tr>';
						htmlstr+=('<td>'+data.body.rowdata[i].CLIENT_NAME+'</td>');
						htmlstr+=('<td>'+data.body.rowdata[i].DEPT+'</td>');
						htmlstr+=('<td>'+data.body.rowdata[i].PROJ_CODE+'</td>');
						htmlstr+=('<td>'+data.body.rowdata[i].PROJ_NAME+'</td>');
						htmlstr+=('<td>'+data.body.rowdata[i].CONTRACT_CODE+'</td>');
						htmlstr+=('<td>'+data.body.rowdata[i].CONTRACT_NAME+'</td>');
						htmlstr+=('<td>'+data.body.rowdata[i].PAYBACK_PERIOD+'</td>');
						htmlstr+=('<td>'+data.body.rowdata[i].CLIENT_MANAGER+'</td>');
						var total = 0;
						for(var j=0;j<array[i];j++){
							var cb = 'CB'+j;
							htmlstr+=('<td>'+data.body.rowdata[i][''+cb+'']+'</td>');
							total+=parseFloat(data.body.rowdata[i][''+cb+'']);
						}
						htmlstr+=('<td>'+total+'</td>');
						htmlstr+='</tr>';
						$("#tbody").append(htmlstr);
					}
				
				}
					
/*
					if(parseInt(data.body.total/10)+(data.body.total%10>0?1:0)>1){
						var htmlstr='<ul class="pagination" style=" float:  right;margin-right: 20px; "><li> <a href="#" id="previous" >上一页</a> </li>';
						for(var i=1;i<=parseInt(data.body.total/10)+(data.body.total%10>0?1:0);i++){
							if($("#currentPage").val()==i){
								htmlstr+='<li> <a href="#" class="pagenum" style="background-color: #eee;" >'+i+'</a> </li>';
							}else{
								htmlstr+='<li> <a href="#" class="pagenum">'+i+'</a> </li>';
							}
						}
						htmlstr+='<li> <a href="#" id="next">下一页</a> </li>';
						$("#page").html(htmlstr);
					}*/
					$("#total").val(data.body.total);
					
					
					$(".pagenum").click(function(){
						var json={};
						if($("#company1").val()!=""){
							json.company=$("#company1").val();
						}
						if($("#datestart1").val()!=""){
							json.datestart=$("#datestart1").val();
						}
						if($("#dateend1").val()!=""){
							json.dateend=$("#dateend1").val();
						}
						$("#currentPage").val($(this).text());
						json.pagestart=($(this).text()-1)*10;
						loadList(JSON.stringify(json));
					});
					
					$("#previous").click(function(){
						if($("#currentPage").val()<=1)
							return;
						var json={};
						if($("#company1").val()!=""){
							json.company=$("#company1").val();
						}
						if($("#datestart1").val()!=""){
							json.datestart=$("#datestart1").val();
						}
						if($("#dateend1").val()!=""){
							json.dateend=$("#dateend1").val();
						}
						$("#currentPage").val($("#currentPage").val()-1);
						json.pagestart=($("#currentPage").val()-1)*10;
						loadList(JSON.stringify(json));
					});
					
					$("#next").click(function(){
						if($("#currentPage").val()>=parseInt($("#total").val()/10)+($("#total").val()%10>0?1:0))
							return;
						var json={};
						if($("#company1").val()!=""){
							json.company=$("#company1").val();
						}
						if($("#datestart1").val()!=""){
							json.datestart=$("#datestart1").val();
						}
						if($("#dateend1").val()!=""){
							json.dateend=$("#dateend1").val();
						}
						$("#currentPage").val(parseInt($("#currentPage").val())+1);
						json.pagestart=($("#currentPage").val()-1)*10;
						loadList(JSON.stringify(json));
					});
				
			}else{
				alert( "查询失败");
			}
		},
		error : function(data) {
			alert( "查询失败");
		}
	});
}
</script>
</html>