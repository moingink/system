<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="../vendor/eCharts/echarts.js"></script>
<jsp:include page="../include/public.jsp"></jsp:include>
<title>产品事业部收入分析</title>
<style type="text/css">
.div{
	width: 100%; 
	height: 350px;
}
.div_west{
	width: 49%;
	height: 100%;
	float: left;
	padding: 10px;
	margin-right: 10px;
}
.div_east{
	width: 49%; 
	height: 100%; 
	float: left;
	padding: 10px;
}
.content{
	 width: 100%; 
	 height: 100%;
	 border: 1px solid #000000;
}
label{
	line-height: 30px;
	float: left;
    margin-left: 10px;
}
</style>
</head>
<body>
	<div style="width: 100%;height: 50px;">
		<label>日期：</label>
		<input class="form-control" onchange="change(this.value)" size="10" type="text" readonly="" id="CONFIRM_MONTH" value="" style="width: 100px; margin-top: 10px;">
	</div>
	<div class="div">
		<div class="div_west">
			<div class="content" id='aChart'></div>
		</div>
		<div class="div_east">
			<div class="content" id='bChart'></div>
		</div>
	</div>

	<div class="div">
		<div class="div_west">
			<div class="content" id='cChart'></div>
		</div>
		<div class="div_east">
			<div class="content" id='dChart'></div>
		</div>
	</div>
</body>
<script type="text/javascript">
$(function(){
	window.parent.$("#busPage").attr('height','800px');
	var myDate = new Date();
	var year = myDate.getFullYear();    
	var month = myDate.getMonth()+1;
	if (month >= 1 && month <= 9) {
	    month = "0" + month;
	}
	var value = year+month;
	$('#CONFIRM_MONTH').attr("readonly","true").datetimepicker({
		minView: 'year',
	   	format:'yyyymm',
	    todayBtn:  1,
	    autoclose: 1,
	    startView: 3,
	    forceParse: 0,
	    showMeridian: 1,
	    language: 'zh-CN'
   	});
   	aChartInit(value);
	bChartInit(value);
	cChartInit(value);
	dChartInit(value);
});
function change(value){
   	aChartInit(value);
	bChartInit(value);
	cChartInit(value);
	dChartInit(value);
}
   	
function aChartInit(value){
	var legendData = new Array();
	var seriesData = new Array();
	var aChart = echarts.init(document.getElementById('aChart'));
	aChart.setOption({
	 	title: {
	 		text: '产品事业部确认收入统计',
            x: 'center',
            top: '5%',
            textStyle:{
            	color: '#1190CC',
            	fontSize: '14'
        	}
        },
        legend: {
        	orient: 'horizontal',
        	x: 'center',
        	y: 'bottom',
        	data: []
    	},
    	tooltip: {
        	trigger :'item',
        	confine :true,
            formatter: "{b}: {c}元"
        },
        series: [{
            type: 'pie',
            radius: ['0%', '50%'],
            color: ['#EDCE1C','#00CBC7','#18B3EE','#FFB673','#EC7173','#8996AC','#BDA0DA','#890C4C'],
            data: []
        }]
	 });
	 $.ajax({
		url : '/system/buttonBase?cmd=button&buttonToken=query&dataSourceCode=VIEW_CONFIRM_INCOME_REPORT2&token='+token+'&limit=9999&offset=0&_=1530779481604&CONFIRM_MONTH='+value,
		type : 'post',
		async : true,
		data : {},
		dataType : "json",
		success : function(data) {
			var rows = data.rows;
			if(rows.length < 1){
				legendData.push('国际品牌','华北','华南','华东','华中','东北','西北','西南');
				seriesData.push(
					{value:0,name:'国际品牌'},
					{value:0,name:'华北'},
					{value:0,name:'华南'},
					{value:0,name:'华东'},
					{value:0,name:'华中'},
					{value:0,name:'东北'},
					{value:0,name:'西北'},
					{value:0,name:'西南'}
				);
			}else{
				for(var i=0;i<rows.length;i++){
					legendData.push(rows[i].ORG_DEPT_NAME);
					seriesData.push({value:rows[i].CLAIM_BALANCE,name:rows[i].ORG_DEPT_NAME});
				}
			}
			aChart.setOption({
				legend: {
        			data:legendData
    			},
				series: [{
            		data:seriesData
        		}]
        	});
		},
		error : function() {
			alert("获取数据失败！");
		}
	});
}

function bChartInit(value){
	var bChart = echarts.init(document.getElementById('bChart'));
	bChart.setOption({
	 	title: {
            text: '产品事业部回款统计',
            x:'center',
            top: '5%',
            textStyle:{
            	color:'#1190CC',
            	fontSize:'14'
        	}
        },
        tooltip: {
        	trigger :'item',
        	confine :true,
            formatter:"{b}: {c}元"
        },
        legend: {
        	orient: 'horizontal',
        	x: 'center',
        	y:'bottom',
        	data:['a','b']
    	},
        series: [{
            type:'pie',
            radius: ['0%', '50%'],
            color:['#EDCE1C','#00CBC7','#18B3EE','#FFB673','#EC7173','#8996AC','#BDA0DA','#890C4C'],
            label: {
            	show: false
            },
            data:[
            	{value:1, name:'a'},
                {value:2, name:'b'}
            ]
        }]
	 });
}

function cChartInit(value){
	var xAxisData = new Array();
	var seriesData = new Array();
	var cChart = echarts.init(document.getElementById('cChart'));
	cChart.setOption({
	 	title: {
            text: '产品事业部确认收入统计',
            x:'center',
            textStyle:{
            	color:'#1190CC',
            	fontSize:'14'
        	},
        	top :'5%'
        },
        tooltip : {
        	trigger: 'axis',
        	axisPointer : {            
            	type : 'shadow'        
        	}
    	},
    	legend: {
        	orient: 'horizontal',
        	x: 'center',
        	y:'bottom',
        	data: ['确认收入']
    	},
    	grid: {
        	left: '3%',
        	right: '3%',
        	bottom: '10%',
        	containLabel: true
    	},
    	xAxis:  {
        	type: 'category',
        	data: []
    	},
    	yAxis: {
        	type: 'value'
    	},
    	series: [
        	{
            	name: '确认收入',
            	type: 'bar',
            	stack: '数量',
            	label: {
                	normal: {
                   		show: true,
                    	position: 'inside'
                	}
            	},
            	itemStyle:{
                	barBorderRadius :5
            	},
            	color:['#00CBC7'],
            	data: []
        	}
		]
	});
	$.ajax({
		url : '/system/buttonBase?cmd=button&buttonToken=query&dataSourceCode=VIEW_CONFIRM_INCOME_REPORT2&token='+token+'&limit=9999&offset=0&_=1530779481604&CONFIRM_MONTH='+value,
		type : 'post',
		async : true,
		data : {},
		dataType : "json",
		success : function(data) {
			var rows = data.rows;
			if(rows.length < 1){
				xAxisData.push('国际品牌','华北','华南','华东','华中','东北','西北','西南');
				seriesData.push('0','0','0','0','0','0','0','0');
			}else{
				for(var i=0;i<rows.length;i++){
					xAxisData.push(rows[i].ORG_DEPT_NAME);
					seriesData.push(rows[i].CLAIM_BALANCE);
				}
			}
			cChart.setOption({
				xAxis: {
        			data:xAxisData
    			},
				series: [{
            		data:seriesData
        		}]
        	});
		},
		error : function() {
			alert("获取数据失败！");
		}
	});
}

function dChartInit(value){
	var dChart = echarts.init(document.getElementById('dChart'));
	dChart.setOption({
	 	title: {
            text: '产品事业部回款统计',
            x:'center',
            textStyle:{
            	color:'#1190CC',
            	fontSize:'12'
        	}
        },
        tooltip : {
        	trigger: 'axis',
        	axisPointer : {            
            	type : 'shadow'        
        	}
    	},
    	legend: {
        	orient: 'horizontal',
        	x: 'center',
        	y:'bottom',
        	data: ['a','b','c','d']
    	},
    	grid: {
        	left: '3%',
        	right: '3%',
        	bottom: '10%',
        	containLabel: true
    	},
    	xAxis:  {
        	type: 'category',
        	data: ['国际品牌','华北','华中','华南','华东','东北','西南']
    	},
    	yAxis: {
        	type: 'value'
    	},
    	series: [
        	{
            	name: 'a',
            	type: 'bar',
            	stack: '数量',
            	label: {
                	normal: {
                   		show: true,
                    	position: 'inside'
                	}
            	},
            	itemStyle:{
                	barBorderRadius :5
            	},
            	color:['#00CBC7'],
            	data: [1,2,2,2,2,2,2]
        	},
        	{
            	name: 'b',
            	type: 'bar',
            	stack: '数量',
            	label: {
                	normal: {
                    	show: true,
                   		position: 'inside'
                	}
            	},
            	itemStyle:{
                	barBorderRadius :5
            	},
            	color:['#BDA0DA'],
            	data: [1,2,2,2,2,2,2]
        	},
        	{
            	name: 'c',
            	type: 'bar',
            	stack: '数量',
            	label: {
                	normal: {
                    	show: true,
                    	position: 'inside'
                	}
            	},
            	itemStyle:{
                	barBorderRadius :5
            	},
            	color:['#18B3EE'],
            	data: [1,2,2,2,2,2,2]
        	},
        	{
            	name: 'd',
            	type: 'bar',
            	stack: '数量',
            	label: {
                	normal: {
                    	show: true,
                    	position: 'inside'
                	}
            	},
            	itemStyle:{
                	barBorderRadius :5
            	},
            	color:['#FFB673'],
            	data: [1,2,2,2,2,2,2]
        	}
		]
	});
}
</script>
</html>