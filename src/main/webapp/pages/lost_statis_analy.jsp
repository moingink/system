<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<title>子表模板</title>
</head>
<%
	String token = request.getParameter("token");
%>
<body>
	
	<div class="panel panel-primary" style="height:auto;zoom:1;overflow:hidden;">
		<div class="col-md-4" style="margin-top:10px;">
			<div class="form-group">
                  <div class="col-md-4" style="text-align: right;"><label class="control-label"
			style="text-align: left; line-height: 29px;  margin-left: 10px;"  >营销单元：</label></div>
                  <div class="col-md-8">
                  	<select class="Analysis_dimension form-control" id="mark_unit" >
					<option selected="selected" value="">全部</option>
				</select>
                  </div>
           	</div>
		</div>
		<div class="col-md-4" style="margin-top:10px;">
			<div class="form-group">
                  <div class="col-md-4" style="text-align: right;"><label class="control-label"
			style="text-align: left; line-height: 29px;  margin-left: 10px;"  >业务类型：</label></div>
                  <div class="col-md-8">
                  	<select class="Analysis_dimension form-control" id="contain_task" >
					<option selected="selected" value="">全部</option>
				</select>
                  </div>
           	</div>
		</div>
		<div class="col-md-4" style="margin-top:10px;">
			<a href="#" ><span id="query" class="btn btn-primary" >查询</span></a>
		</div>
	<div id="funnel" style="width: 100%;height:500px;margin-top:200px;"></div>
	</div>
</body>
<jsp:include page="../include/public.jsp"></jsp:include>
<script type="text/javascript" src="../vendor/eCharts/echarts.js"></script>
<script type="text/javascript">
	$(function() {
		$.ajax({
			type : "POST",
			url :  "/system/reportStatis?cmd=selectMarkUnit",
			dataType : "json",
			async : false,
			cache : false,
			contentType : false,
			processData : false,
			success : function(data) {
				if(data.status=="success"){
					for(var i=0;i<data.body.rowdata.length;i++){
						$("#mark_unit").append('<option value="'+data.body.rowdata[i].ID+'">'+data.body.rowdata[i].NAME+'</option>');
					}
				}else{
					alert( "查询营销单元失败");
				}
			},
			error : function(data) {
				alert( "查询营销单元失败");
			}
		});
		
		$.ajax({
			type : "POST",
			url :  "/system/reportStatis?cmd=selectBusType",
			dataType : "json",
			async : false,
			cache : false,
			contentType : false,
			processData : false,
			success : function(data) {
				if(data.status=="success"){
					for(var i=0;i<data.body.rowdata.length;i++){
						$("#contain_task").append('<option value="'+data.body.rowdata[i].TYPE_NAME+'">'+data.body.rowdata[i].TYPE_NAME+'</option>');
					}
				}else{
					alert( "查询业务类型失败");
				}
			},
			error : function(data) {
				alert( "查询业务类型失败");
			}
		});
	
		fun_echarts('{}');
		$("#query").click(function(){
			var json={};
			if($("#mark_unit").val()!=""){
				json.mark_unit=$("#mark_unit").val();
			}
			if($("#contain_task").val()!=""){
				json.contain_task=$("#contain_task").val();
			}
			fun_echarts(JSON.stringify(json));
		});
			
			
	});
	
	function fun_echarts(json){
		console.log(json);
		$.ajax({
			type : "POST",
			url :  "/system/reportStatis?cmd=selectLost&token=<%=token%>",
			data : json,
			dataType : "json",
			async : false,
			cache : false,
			contentType : false,
			processData : false,
			success : function(data) {
				if(data.status=="success"){
					var myChart = echarts.init(document.getElementById('funnel'));
					myChart.setOption(funnel(data));
				}else{
					alert( "查询失败");
				}
			},
			error : function(data) {
				alert( "查询失败");
			}
		});
	}
	
	
	
	function funnel(data){
		var xdata = [];
		var ydata = [];
		var sum = 0;
		for(var i=0;i<data.body.rowdata.length;i++){
			sum+=data.body.rowdata[i].FRACTION;
		}
		if(data.body.rowdata.length==0){
			xdata = [{text : '价格',max:5},{text : '技术',max:5},{text : '客情关系',max:5},{text : '策略方案',max:5},{text : '讲标',max:5}];
			for(var i=0;i<5;i++){
				ydata.push(0);
			}
		}else{
			for(var i=0;i<data.body.rowdata.length;i++){
				xdata.push({text : data.body.rowdata[i].GRADING_PROJECT,max:5});
				ydata.push(data.body.rowdata[i].FRACTION);
			}
		}
	option = {
		    title: {
		        text: '丢标分析',
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
		
		    radar: [
		        {
		            indicator: xdata,
		            center: ['50%','50%'],
		            radius: 105,	            
		            name: {
		                formatter:'【{value}】',
		                textStyle: {
		                    color:'#000000'
		                }
		            },
		           splitArea : {
		                            show : true,   
		                            areaStyle : {
		                                color: ["#2a4a93"]  // 图表背景网格的颜色
		                            }
		                        },
				            splitLine :{
		                            show : true,
		                            lineStyle : {
		                                width : 1,
		                                color: ["#9dd3fa"] // 图表背景网格线的颜色
		                            },
		
		                        },
		        
		        },
		       
		    ],
		    series: [
		        {
		            type: 'radar',
		             tooltip: {
		                trigger: 'item'
		            },
		            itemStyle: {
		            				   normal: {
					                        color: 'rgba(74,180,231,1)',
					                        lineStyle: {
					                            color: 'rgba(255,225,55,1)',
					                        },
					                        areaStyle: {type: 'default'},
					                        label: {
						                        fontSize:16,
						                        fontWeight: 'bolder',
						                        
						                    }
					                    },
		            },
		            data: [
		                {
		                    value: ydata,
							name:"丢标打分"
		                }
		            ]
		        },
		     
		    ]
		};

		
		return option;
	}

</script>

</html>