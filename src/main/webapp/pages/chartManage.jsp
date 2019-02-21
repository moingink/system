<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>饼状图管理首页</title>
<link rel="stylesheet" type="text/css" href="../style/customerManage.css"/>
<script type="text/javascript" src="../vendor/eCharts/echarts-all.js"></script>
<jsp:include page="../include/public.jsp"></jsp:include>
<style type="text/css">
.content .content-title {height: 10%;}
.content .contain-detail{height: 70%;}
.contain .content{height: 100%;}
</style>
</head>
<body>
	<div class="contain">
		<div class="content" style="margin-right: 20px;">
			<div class="content-title">
				<div class="content-name">联通智网部门人数分布图</div>
				<div class="content-more">
					<a href="#">>></a>
				</div>
			</div>
			<div class="contain-detail" id="chart1">
			</div>
		</div>
		<div class="content" style="">
			<div class="content-title">
				<div class="content-name">联通智网部门人数分布图</div>
				<div class="content-more">
					<a href="#">>></a>
				</div>
			</div>
			<div class="contain-detail" id="chart2"></div>
		</div>
	</div>
</body>
<script type="text/javascript">
		$(function(){
			var jumpFun= function (params) {
				window.location.href="http://www.baidu.com?name="+params.name+"&value="+params.value;
			};
			loadline("/system/businessAnalysis?cmd=linechart",'{}',"chart1",jumpFun);
			//loadpie("/system/businessAnalysis?cmd=piechart",'{}',"chart2",jumpFun);
			//loadpie("/system/businessAnalysis?cmd=piechart",'{}',"chart3",jumpFun);
			//loadpie("/system/businessAnalysis?cmd=piechart",'{}',"chart4",jumpFun);
			
		});
		//饼状图
		function loadpie(url,jsonparam,div,jumpFun){
			$.ajax({
				type : "POST",
				url :  url,
				data : jsonparam,
				dataType : "json",
				async : false,
				cache : false,
				contentType : false,
				processData : false,
				success : function(jsondata) {
					if(jsondata.status=="success"){
						
						 var myChart = echarts.init(document.getElementById(div));
						 myChart.showLoading({
						    text: '正在努力的读取数据中...'   //loading
						});
							myChart.setOption({
							    title : {
							        text: jsondata.body.title,
							        subtext: jsondata.body.subtitle,
							        x:'center'
							    },
							    tooltip : {
							        trigger: 'item',
							        formatter: "{a} <br/>{b} : {c} ({d}%)"
							    },
							    legend: {
							        orient : 'vertical',
							        x : 'left',
							        data: jsondata.body.rowname
						            
							    },
							    toolbox: {
							        show : true,
							        feature : {
							            dataView : {show: true, readOnly: false},
							            restore : {show: true},
							            saveAsImage : {show: true}
							        }
							    },
							    calculable : true,
							    series : [
							        {
							            name:jsondata.body.dataname,
							            type:'pie',
							            radius : '55%',
							            center: ['50%', '60%'],
							            data:jsondata.body.rowdata
							        }
							    ],
				                mousedown:function(){
				                    console.log(Xindex);
					            }
							});
				            myChart.hideLoading();	//loading hidden
				            myChart.on('click', jumpFun);
					}else{
						alert(jsondata.message);
					}
				
				},
				error : function(jsondata) {
					alert( "查询失败");
				}
				
			});		
		}
		
		//折线图
		function loadline1(url,jsonparam,div,jumpFun){
			$.ajax({
				type : "POST",
				url :  url,
				data : jsonparam,
				dataType : "json",
				async : false,
				cache : false,
				contentType : false,
				processData : false,
				success : function(jsondata) {
					if(jsondata.status=="success"){
						
						 var myChart = echarts.init(document.getElementById(div));
						 myChart.showLoading({
						    text: '正在努力的读取数据中...'   //loading
						});
							myChart.setOption({ 
							    tooltip :  {
								        trigger: 'axis',
								        showDelay : 0,
								        formatter : function (params) {
								            if (params.value.length > 1) {
								                return params.seriesName + ' :<br/>'
								                   + params.value[0] + 'cm ' 
								                   + params.value[1] + 'kg ';
								            }
								            else {
								                return params.seriesName + ' :<br/>'
								                   + params.name + ' : '
								                   + params.value + 'kg ';
								            }
								        }, 
							        axisPointer:{
							            show: true,
							            type : 'cross',
							            lineStyle: {
							                type : 'dashed',
							                width : 1
							            }
							        }
							    },   
							    legend: {
							          data: [  '女性']
							    },
							    dataRange: {
							        min: 0,
							        max: 100,
							        orient: 'horizontal',
							        y: 30,
							        x: 'center',
							        //text:['高','低'],           // 文本，默认为数值文本
							        color:['lightgreen','orange'],
							        splitNumber: 5
							    },
							    toolbox: {
							        show : true,
							        feature : {
							            dataView : {show: true, readOnly: false},
							            restore : {show: true},
							            saveAsImage : {show: true}
							        }
							    },
							    calculable : true,
							    xAxis : [
							        {
							            type : 'value',
							            scale:true,
							            axisLabel : {
							                formatter: '{value} cm'
							            }
							        }
							    ],
							    yAxis : [
							        {
							            type : 'value',
							            scale:true,
							            axisLabel : {
							                formatter: '{value} kg'
							            }
							        }
							    ],
							    series : [
							       {
							            name:'女性',
							            type:'scatter',
							            data: [[161.2, 51.6], [167.5, 59.0], [159.5, 49.2], [157.0, 63.0], [155.8, 53.6],
							                [176.5, 71.8], [164.4, 55.5], [160.7, 48.6], [174.0, 66.4], [163.8, 67.3]
							            ]
							        }
							    ],
				                mousedown:function(){
				                    console.log(Xindex);
					            }
							});
				            myChart.hideLoading();	//loading hidden
				            myChart.on('click', jumpFun);
					}else{
						alert(jsondata.message);
					}
				
				},
				error : function(jsondata) {
					alert( "查询失败");
				}
				
			});		
		}
		
		//折线图
		function loadline(url,jsonparam,div,jumpFun){
			$.ajax({
				type : "POST",
				url :  url,
				data : jsonparam,
				dataType : "json",
				async : false,
				cache : false,
				contentType : false,
				processData : false,
				success : function(jsondata) {
					if(jsondata.status=="success"){
						
						 var myChart = echarts.init(document.getElementById(div));
						 myChart.showLoading({
						    text: '正在努力的读取数据中...'   //loading
						});
							myChart.setOption( {   title : {
							        text: '',
							        subtext: ''
							    },
							    tooltip : {
							        trigger: 'axis',
							        showDelay : 0,
							        formatter : function (params) {
							            if (params.value.length > 1) {
							                return params.seriesName + ' :<br/>'
							                   + params.value[2]   ;
							            }
							            else {
							                return params.seriesName + ' :<br/>'
							                   + params.name + ' : '
							                   + params.value + 'kg ';
							            }
							        },  
							        axisPointer:{
							            show: true,
							            type : 'cross',
							            lineStyle: {
							                type : 'dashed',
							                width : 1
							            }
							        }
							    },
							    legend: {
							        orient : 'horizontal',
						        	x: 'center',
						        	y:'bottom',
							        data: jsondata.body.rowname
							    },
							    toolbox: {
							        show : true,
							        feature : {
							            dataZoom : {show: true},
							            dataView : {show: true, readOnly: false},
							            restore : {show: true},
							            saveAsImage : {show: true}
							        }
							    },
							    xAxis : [
							        {
							            type : 'value',
							            scale:true,
							            axisLabel : {
							                formatter: '{value} cm'
							            }
							        }
							    ],
							    yAxis : [
							        {
							            type : 'value',
							            scale:true,
							            axisLabel : {
							                formatter: '{value} kg'
							            }
							        }
							    ],
							    series : jsondata.body.rowdata
							} );
				            myChart.hideLoading();	//loading hidden
				            myChart.on('click', jumpFun);
					}else{
						alert(jsondata.message);
					}
				
				},
				error : function(jsondata) {
					alert( "查询失败");
				}
				
			});		
		}
</script>
</html>