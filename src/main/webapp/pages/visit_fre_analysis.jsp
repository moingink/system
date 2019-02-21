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
			<div class="col-md-3" style="margin-top:10px;">
				<label class="control-label"
					style="text-align: left; line-height: 29px;   "  >分析维度：</label>
				<select class="Analysis_dimension" id="Analysis_dimension1" style="width:150px;height:30px;margin-bottom:6px;">
					<option selected="selected" value="1">部门</option>
					<option value="2">客户经理</option>
					<option value="3">客户</option>
				</select>
			</div>
			<div class="col-md-3" style="margin-top:10px;">
				<div class="form-group">
                   <div class="col-md-4" style="text-align: right;"><label class="control-label"
				style="text-align: left; line-height: 29px; "  >开始日期：</label>
				  </div>
				  <div class="input-group date form_date" style=" data-date-format="dd MM yyyy" data-link-format="yyyy-mm-dd">
				  	<input type="TEXT" class="form-control" id="datestart1" readonly="readonly">
				  	<span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
				  	<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
				  </div>
            	</div>
			</div>
			<div class="col-md-3" style="margin-top:10px;">
				<div class="form-group">
                   <div class="col-md-4" style="text-align: right;"><label class="control-label"
				style="text-align: left; line-height: 29px;  margin-left: 10px;   "  >结束日期：</label>
				  </div>
				  <div class="input-group date form_date" style=" data-date-format="dd MM yyyy" data-link-format="yyyy-mm-dd">
				  	<input type="TEXT" class="form-control" id="dateend1" readonly="readonly">
				  	<span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
				  	<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
				  </div>
            	</div>
			</div>
			<div class="col-md-3" style="margin-top:10px;">
				<a href="#" ><span id="query2" class="btn btn-primary" >查询</span></a>
			</div>
			
			<table border="1"class="table table-hover" style="text-align: center;    vertical-align: inherit;margin-top: 10px;" >
				<thead id="thead">
				</thead>
				<tbody id="tbody">
				</tbody>
			</table>
	
			<div id="page">
			</div>
		</div>
		<input type="hidden" id="currentPage" value="1"/>
		<input type="hidden" id="total" value="0"/>
	</form>

	
			
</body>
<jsp:include page="../include/public.jsp"></jsp:include>
<script type="text/javascript" src="../vendor/eCharts/echarts.js"></script>
<script type="text/javascript">

$(function() {
	loadList('{analysisDimension:"1"}');
	
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
   	
   	$("#query2").click(function(){
		var json={};
		if($("#Analysis_dimension1").val()!=""){
			json.analysisDimension=$("#Analysis_dimension1").val();
		}
		if($("#datestart1").val()!=""){
			json.datestart=$("#datestart1").val();
		}else{
			alert("请选择开始时间");
			return;
		}
		if($("#dateend1").val()!=""){
			json.dateend=$("#dateend1").val();
		}else{
			alert("请选择结束时间");
			return;
		}
		loadList(JSON.stringify(json));
	});
	$("#Analysis_dimension1").change(function(){
		var json={};
		if($("#Analysis_dimension1").val()!=""){
			json.analysisDimension=$("#Analysis_dimension1").val();
		}
		if($("#datestart1").val()!=""){
			json.datestart=$("#datestart1").val();
		}
		if($("#dateend1").val()!=""){
			json.dateend=$("#dateend1").val();
		}
		loadList(JSON.stringify(json));
	});
	
});

function loadList(json){
	$("#tbody").html("");
	$("#page").html("");
	var field = '<th>计划拜访次数</th>'+
				'<th>计划完成次数</th>'+
				'<th>计划完成率</th>'+
				'<th>临时拜访次数</th>'+
				'<th>拜访总次数</th>'+
				'<th>拜访完成总次数</th>'+
				'<th>拜访未完成总次数</th>'+
				'<th>拜访完成率</th>'+
				'<th>日均拜访次数</th>';
	$.ajax({
		type : "POST",
		url :  "/system/visit?cmd=visitCountAnaly&token=<%=token%>",
		data : json,
		dataType : "json",
		contentType : false,
		processData : false,
		success : function(data) {
			if(data.status=="success"){
				if(data.body.rowdata.length==0){
					if($("#Analysis_dimension1").val()=="1"){
						var str = '';
				   		$("#thead").html(str);
				   		str += '<tr>';
						str += '<th>部门</th>';
						str += field;
						str += '</tr>';
						$("#thead").append(str);
					}
					if($("#Analysis_dimension1").val()=="2"){
						var str = '';
				   		$("#thead").html(str);
				   		str += '<tr>';
						str += '<th>客户经理</th>';
						str += field;
						str += '</tr>';
						$("#thead").append(str);
					}
					if($("#Analysis_dimension1").val()=="3"){
						var str = '';
				   		$("#thead").html(str);
				   		str += '<tr>';
						str += '<th>客户</th>';
						str += field;
						str += '</tr>';
						$("#thead").append(str);
					}
					$("#tbody").append('<tr><td colspan="10">暂时无数据</td></tr>');
				}else{
				
					
					for (var key in data.body.rowdata[0]) {
					   if(key=="VISIT_BUMEN"){
					   		var str = '';
					   		$("#thead").html(str);
					   		str += '<tr class="info" style="background-color: rgb(255, 255, 255);">';
							str += '<th>部门</th>';
							str += field;
							str += '</tr>';
							$("#thead").append(str);
							for(var i=0;i<data.body.rowdata.length;i++){
								var htmlstr='';
								htmlstr+='<tr>';
								htmlstr+=('<td>'+data.body.rowdata[i].VISIT_BUMEN+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].PLAN_VIS_COUNT+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].PLAN_COMPLETE_COUNT+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].PLAN_COMP_RATE+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].TEMP_VIS_COUNT+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].VIS_COUNT_TOTAL+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].VIS_COMP_TOTAL+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].VIS_LAST_NUM+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].VIS_COMP_RATE+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].AVE_DAILY_VIS+'</td>');
								htmlstr+='</tr>';
								$("#tbody").append(htmlstr);
							}
						}
						if(key=="CUS_MAN"){
							var str = '';
					   		$("#thead").html(str);
					   		str += '<tr class="info" style="background-color: rgb(255, 255, 255);">';
							str += '<th>客户经理</th>';
							str += field;
							str += '</tr>';
							$("#thead").append(str);
							for(var i=0;i<data.body.rowdata.length;i++){
								var htmlstr='';
								htmlstr+='<tr>';
								htmlstr+=('<td>'+data.body.rowdata[i].CUS_MAN+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].PLAN_VIS_COUNT+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].PLAN_COMPLETE_COUNT+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].PLAN_COMP_RATE+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].TEMP_VIS_COUNT+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].VIS_COUNT_TOTAL+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].VIS_COMP_TOTAL+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].VIS_LAST_NUM+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].VIS_COMP_RATE+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].AVE_DAILY_VIS+'</td>');
								htmlstr+='</tr>';
								$("#tbody").append(htmlstr);
							}
						}
						if(key=="CUS_NAME"){
							var str = '';
					   		$("#thead").html(str);
					   		str += '<tr class="info" style="background-color: rgb(255, 255, 255);">';
							str += '<th>客户</th>';
							str += field;
							str += '</tr>';
							$("#thead").append(str);
							for(var i=0;i<data.body.rowdata.length;i++){
								var htmlstr='';
								htmlstr+='<tr>';
								htmlstr+=('<td>'+data.body.rowdata[i].CUS_NAME+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].PLAN_VIS_COUNT+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].PLAN_COMPLETE_COUNT+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].PLAN_COMP_RATE+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].TEMP_VIS_COUNT+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].VIS_COUNT_TOTAL+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].VIS_COMP_TOTAL+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].VIS_LAST_NUM+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].VIS_COMP_RATE+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].AVE_DAILY_VIS+'</td>');
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
					}
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
					});*/
				}
				
			}
			
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