<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<title>子表模板</title>
<style type="text/css">
	.panel-primary{
		height:1000px;
		
	}
	#bg{
		width:46%;
		height:400px;
		float:left;
	}
	#bg1{

		width:46%;
		height:400px;
		float:right;
	}
</style>
</head>
<%
	String ContextPath =request.getContextPath();
%>

<body>

	<form class="form-horizontal">
		<div class="panel panel-primary" style="padding-top:50px;">
		
		<div class="col-md-12">
			<a class="btn btn-inverse" onclick="window.history.back(-1);" style="float:left;">
				<span class="glyphicon glyphicon-ban-circle" aria-hidden="true"></span>返回
			</a>
				<div class="col-md-4">
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
				<div class="col-md-4">
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
				<a href="#" class="btn btn-info" style="margin-left:50px;"><span id="query2">查询</span></a>
			
			</div>
		
					
			
			<!-- 折线图 -->
			<div style="height:500px;margin-top:60px;">
				<div id="bg" type="hidden"></div>
				<div id="bg1" type="hidden"></div>
			</div>
			
		</div>
		
	</form>

	
			
</body>
<jsp:include page="../include/public.jsp"></jsp:include>
<script type="text/javascript" src="../vendor/eCharts/echarts.js"></script>
<script type="text/javascript">
var proj_code = '<%=request.getParameter("proj_code")%>';
$(function() {
	loadList('{}');
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
	$.ajax({
		type : "POST",
		url :  "/system/reportStatis?cmd=earnedValAnalysis&proj_code="+proj_code,
		data : json,
		dataType : "json",
		async : false,
		cache : false,
		contentType : false,
		processData : false,
		success : function(data) {
			if(data.status=="success"){
				var myChart = echarts.init(document.getElementById('bg'));
				myChart.setOption(bg("CPI趋势图",data));
				
				var myChart = echarts.init(document.getElementById('bg1'));
				myChart.setOption(bg("SPI趋势图",data));
				
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
function bg(title,data){
  var xdata = [];
  var ydata = [];
  if(title=='CPI趋势图'){
	  for(var i=0;i<data.body.rowdata.length;i++){
	   	  xdata.push(data.body.rowdata[i].CALCULATION_DATE);
	   	  ydata.push(data.body.rowdata[i].CPI);
	  }
  }
  if(title=='SPI趋势图'){
  	  for(var i=0;i<data.body.rowdata.length;i++){
	   	  xdata.push(data.body.rowdata[i].CALCULATION_DATE);
	   	  ydata.push(data.body.rowdata[i].SPI);
	  }
  }
  console.log(xdata);
  console.log(ydata);
  option = {
    title : {
        text: title,
        x:'center'
    },
    tooltip : {
        trigger: 'axis'
    },
    toolbox: {
        show : true,
        feature : {
            mark : {show: true},
            dataView : {show: true, readOnly: false},
            restore : {show: true},
            saveAsImage : {show: true}
        }
    },
    calculable : true,
    xAxis : [
        {
            type : 'category',
            boundaryGap : false,
            data : xdata
        }
    ],
    yAxis : [
        {
            type : 'value',
            axisLabel : {
                formatter: '{value} %'
            }
        }
    ],
    series : [
        {
            type:'line',
            data:ydata,
            markPoint : {
                data : [
                    {type : 'max', name: '最大值'},
                    {type : 'min', name: '最小值'}
                ]
            },
            markLine : {
                data : [
                    {type : 'average', name: '平均值'}
                ]
            }
        }
    ]
};
   return option;
}




</script>

</html>