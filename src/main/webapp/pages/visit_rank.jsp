<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<title>子表模板</title>
<link type="text/css" rel="stylesheet" href="jDate/test/jeDate-test.css">
<link type="text/css" rel="stylesheet" href="jDate/skin/jedate.css">
<link rel="stylesheet" href="../vendor/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="../vendor/bootstrap-table/src/bootstrap-table.css">
<script type="text/javascript" src="../easyui/jquery.min.js"></script>
<script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="../easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="jDate/src/jedate.js"></script>
<script src="../vendor/bootstrap/js/bootstrap.min.js"></script>
<script src="../vendor/bootstrap-table/src/bootstrap-table.js"></script>
<script src="js/bootTable.js"></script>
<style type="text/css">
	.tb{
		margin-top:10px;
		margin-left:30px;
		/* width:500px; */
		float:left;
	}
	#bg{
		margin-top:25px;
		/* width:500px; */
		float:right;
	}
	
</style>
</head>
<%
	String ContextPath =request.getContextPath();
	String token = request.getParameter("token");
%>

<body>

	<form class="form-horizontal">
		<div class="panel panel-primary" style="height:auto;zoom:1;overflow:hidden;">
			<div class="col-md-3" style="margin-top:10px;">
				<div class="form-group">
                   <div class="col-md-4" style="text-align: right;"><label class="control-label"
				style="text-align: left; line-height: 29px;  margin-left: 10px;"  >分析维度：</label></div>
                   <div class="col-md-8">
                   	<select class="Analysis_dimension form-control" id="Analysis_dimension1" >
						<option selected="selected" value="1">部门</option>
						<option value="2">客户经理</option>
						<option value="3">客户</option>
					</select>
                   </div>
            	</div>
			</div>
			<div class="col-md-3" style="margin-top:10px;">
				<div class="form-group">
                   <div class="col-md-4" style="text-align: right;"><label class="control-label"
				style="text-align: left; line-height: 29px;  margin-left: 10px;   "  >开始日期：</label>
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
			
			
			<div id="div1">
				<div class="tb" style="width:46%;">
					
					<table border="1"class="table table-hover" style="text-align: center;    vertical-align: inherit;margin-top: 10px;" >
						<thead id="thead">
						</thead>
						<tbody id="tbody">
						</tbody>
					</table>
				</div>
				<!-- 柱状图 -->
				<div id="bg" style="width: 46%;height:400px;" type="hidden"></div>
			</div>
			
			<div id="page" style="width:100%;height:auto;zoom:1;overflow:hidden; position:relative;">
			</div>
		</div>
		<input type="hidden" id="currentPage" value="1"/>
		<input type="hidden" id="total" value="0"/> 
	</form>

	
			
</body>
<jsp:include page="../include/public.jsp"></jsp:include>
<%-- <script src="<%=request.getContextPath()%>/js/public.js"></script>
<script src="<%=request.getContextPath()%>/js/common.js"></script> --%>
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
		}
		if($("#dateend1").val()!=""){
			json.dateend=$("#dateend1").val();
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
			json.token=<%=token%>;
		}
		loadList(JSON.stringify(json));
	});
	
	
});

function loadList(json){
	$("#tbody").html("");
	$("#page").html("");
	$.ajax({
		//json.token="<%=token%>";
		type : "POST",
		url :  "/system/visit?cmd=visitRank&token=<%=token%>",
		data : json,
		dataType : "json",
		async : false,
		cache : false,
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
							str += '<th>拜访次数</th>';
							str += '<th>排名</th>';
							str += '</tr>';
							$("#thead").append(str);
							
						}
						 if($("#Analysis_dimension1").val()=="2"){
					   		var str = '';
					   		$("#thead").html(str);
					   		str += '<tr>';
							str += '<th>客户经理</th>';
							str += '<th>拜访次数</th>';
							str += '<th>排名</th>';
							str += '</tr>';
							$("#thead").append(str);
							
						}
						 if($("#Analysis_dimension1").val()=="3"){
					   		var str = '';
					   		$("#thead").html(str);
					   		str += '<tr>';
							str += '<th>客户</th>';
							str += '<th>拜访次数</th>';
							str += '<th>排名</th>';
							str += '</tr>';
							$("#thead").append(str);
							
						}
					$("#tbody").append('<tr><td colspan="6">暂时无数据</td></tr>');
					var myChart = echarts.init(document.getElementById('bg'));
					myChart.setOption(bg("",data,1));
				}else{
					   if($("#Analysis_dimension1").val()=="1"){
					   		var str = '';
					   		$("#thead").html(str);
					   		str += '<tr>';
							str += '<th>部门</th>';
							str += '<th>拜访次数</th>';
							str += '<th>排名</th>';
							str += '</tr>';
							$("#thead").append(str);
							for(var i=0;i<data.body.rowdata.length;i++){
								var htmlstr='';
								htmlstr+='<tr>';
								htmlstr+=('<td>'+data.body.rowdata[i].VISIT_BUMEN+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].COUNT+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].RANK+'</td>');
								htmlstr+='</tr>';
								$("#tbody").append(htmlstr);
							}
							var myChart = echarts.init(document.getElementById('bg'));
							myChart.setOption(bg("部门拜访排名",data,1));
						}
						if($("#Analysis_dimension1").val()=="2"){
							var str = '';
							$("#thead").html(str);
					   		str += '<tr>';
							str += '<th>客户经理</th>';
							str += '<th>拜访次数</th>';
							str += '<th>排名</th>';
							str += '</tr>';
							$("#thead").append(str);
							for(var i=0;i<data.body.rowdata.length;i++){
								var htmlstr='';
								htmlstr+='<tr>';
								htmlstr+=('<td>'+data.body.rowdata[i].CUS_MAN+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].COUNT+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].RANK+'</td>');
								htmlstr+='</tr>';
								$("#tbody").append(htmlstr);
							}
							var myChart = echarts.init(document.getElementById('bg'));
							myChart.setOption(bg("客户经理拜访排名",data,2));
						}
						if($("#Analysis_dimension1").val()=="3"){
							var str = '';
							$("#thead").html(str);
					   		str += '<tr>';
							str += '<th>客户</th>';
							str += '<th>拜访次数</th>';
							str += '<th>排名</th>';
							str += '</tr>';
							$("#thead").append(str);
							for(var i=0;i<data.body.rowdata.length;i++){
								var htmlstr='';
								htmlstr+='<tr>';
								htmlstr+=('<td>'+data.body.rowdata[i].CUSTOMER_NAME+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].COUNT+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].RANK+'</td>');
								htmlstr+='</tr>';
								$("#tbody").append(htmlstr);
							}
							var myChart = echarts.init(document.getElementById('bg'));
							myChart.setOption(bg("客户拜访排名",data,3));
						}
					
/*
					if(parseInt(data.body.total/10)+(data.body.total%10>0?1:0)>=1){
						var htmlstr='<ul class="pagination" style=" float:left;margin-left: 20%; "><li> <a href="#" id="previous" >上一页</a> </li>';
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
						if($("#Analysis_dimension1").val()!=""){
							json.analysisDimension=$("#Analysis_dimension1").val();
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
						if($("#Analysis_dimension1").val()!=""){
							json.analysisDimension=$("#Analysis_dimension1").val();
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
						if($("#Analysis_dimension1").val()!=""){
							json.analysisDimension=$("#Analysis_dimension1").val();
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
				
			}else{
				alert( "查询失败");
			}
		},
		error : function(data) {
			alert( "查询失败");
		}
	});
}

//柱状图
function bg(title,data,ad){
  var xdata = [];
  var ydata = [];
  if(ad==1){
	  for(var i=0;i<data.body.rowdata.length;i++){
	   	  xdata.push(data.body.rowdata[i].VISIT_BUMEN);
	   	  ydata.push(data.body.rowdata[i].COUNT);
	  }
  }
  if(ad==2){
  	  for(var i=0;i<data.body.rowdata.length;i++){
	   	  xdata.push(data.body.rowdata[i].CUS_MAN);
	   	  ydata.push(data.body.rowdata[i].COUNT);
	  }
  }
  if(ad==3){
  	  for(var i=0;i<data.body.rowdata.length;i++){
	   	  xdata.push(data.body.rowdata[i].CUSTOMER_NAME);
	   	  ydata.push(data.body.rowdata[i].COUNT);
	  }
  }
  console.log(xdata);
  console.log(ydata);
  var option = {
       title: {
           text: title
       },
       tooltip: {},
       legend: {
          data:['拜访次数']
       },
       xAxis: {
           data: xdata,
           axisLabel:{
		     interval:0,//横轴信息全部显示
		     rotate:30,//-30度角倾斜显示
		   }
           
       },
       yAxis: {},
       series: [{
           name: '销量',
           type: 'bar',
           data: ydata
       }]
   };
   return option;
}


</script>

</html>