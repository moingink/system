<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>资费类型分析</title>
<script type="text/javascript" src="../vendor/eCharts/echarts-all.js"></script>
<jsp:include page="../include/public.jsp"></jsp:include>
<link rel="stylesheet" type="text/css" href="../style/customerManage.css"/>
<style type="text/css">
.content .contain-detail{height: 43%;width:50%;float:left;margin-top:10px;}
body {height: 120%;}
</style>
</head>
<body>
	<div class="contain">
		<div class="content" style="width:100%;">
			<div class="content-title">
				<div class="content-name">数量状态分析图</div>
				<div class="content-more">
					<a href="#" id="list">列表>></a>
				</div>
			</div>
			<div class="panel-body">
				
				<div class="col-md-4">
					<div class="form-group">
						<div class="col-md-4">
							<label class="control-label" style="width:100%;padding-top:0; text-align:right;">营销单元：</label>
						</div> 
						<div class="col-md-8">
							<select class="company form-control" id="company1">
								<option selected="selected" value="">全部</option>
							</select>
						</div>
					</div>
				</div>				
				<div class="col-md-3">
					<div class="form-group">
						<div class="col-md-4">
							<label class="control-label" style="width:100%; padding-top:0; text-align:right;">开始日期：</label>
						</div> 
						<div class="col-md-8">
							<input type="text" id="datestart1" class="datetimepicker form-control"  />
						</div>
					</div>
				</div>
				<div class="col-md-3">
					<div class="form-group">
						<div class="col-md-4">
							<label class="control-label" style="width:100%;padding-top:0; text-align:right;">结束日期：</label>
						</div> 
						<div class="col-md-8">							
							<input type="text" id="dateend1"  class="datetimepicker form-control" />
						</div>
					</div>
				</div>
				<div class="col-md-2">
					<button id="query1" class="btn btn-info">查询</button>
				</div>
				
			</div>
			<div class="contain-detail" id="chart1"></div>
			<div class="contain-detail" id="chart2"></div>
			<div class="contain-detail" id="chart3"></div>
			<div class="contain-detail" id="chart4"></div>
			</div>
		</div>
	
</body>
<script type="text/javascript">
	var myChart1 = echarts.init(document.getElementById("chart1"));
	var myChart2 = echarts.init(document.getElementById("chart2"));
	var myChart3 = echarts.init(document.getElementById("chart3"));
	var myChart4 = echarts.init(document.getElementById("chart4"));
	
	var url1 = "/system/feeTypeAnalysis?cmd=feeTypeAnalysis&flow_min=1&flow_max=1023&flow_unit=MB";
	var url2 = "/system/feeTypeAnalysis?cmd=feeTypeAnalysis&flow_min=1&flow_max=50&flow_unit=GB";
	var url3 = "/system/feeTypeAnalysis?cmd=feeTypeAnalysis&flow_min=51&flow_max=100&flow_unit=GB";
	var url4 = "/system/feeTypeAnalysis?cmd=feeTypeAnalysis&flow_min=101&flow_max=200&flow_unit=GB";
	var title1 = '1M-1G资费分析';
	var title2 = '1G-50G资费分析';
	var title3 = '50G-100G资费分析';
	var title4 = '100G-200G资费分析';
	$(function(){
		var option1=loadline(url1,'{}','chart1',title1);
		var option2=loadline(url2,'{}','chart2',title2);
		var option3=loadline(url3,'{}','chart3',title3);
		var option4=loadline(url4,'{}','chart4',title4);
		echartsSetOption(option1,option2,option3,option4);
		$.ajax({
			type : "POST",
			url :  "/system/businessAnalysis?cmd=companyInfo",
			data : '{}',
			dataType : "json",
			success : function(data) {
				if(data.status=="success"){
					for(var i=0;i<data.body.rowdata.length;i++){
						$(".company").append('<option value="'+data.body.rowdata[i].NAME+'">'+data.body.rowdata[i].NAME+'</option>');
					}
				}else{
					alert( "查询失败");
				}
			},
			error : function(data) {
				alert( "查询失败");
			}
		});
		
		$("#list").click(function(){
			$("#myModalLabel").html("详情");
			show('../pages/feeTypeAnalysis_detail.jsp?title=');
		});
		
		$('.datetimepicker').attr("readonly","true").datetimepicker({
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
	   	
	   	
		$("#query1").click(function(){
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
			echartsClear();
			option1=loadline(url1,JSON.stringify(json),'chart1',title1);
			option2=loadline(url2,JSON.stringify(json),'chart2',title2);
			option3=loadline(url3,JSON.stringify(json),'chart3',title3);
			option4=loadline(url4,JSON.stringify(json),'chart4',title4);
			echartsSetOption(option1,option2,option3,option4);
		});
	
		$("#company1").change(function(){
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
			echartsClear();
			option1=loadline(url1,JSON.stringify(json),'chart1',title1);
			option2=loadline(url2,JSON.stringify(json),'chart2',title2);
			option3=loadline(url3,JSON.stringify(json),'chart3',title3);
			option4=loadline(url4,JSON.stringify(json),'chart4',title4);
			echartsSetOption(option1,option2,option3,option4);
		});
		
		
		
	});
	
	//散点图
	function loadline(url,jsonparam,flow_unit,title){
		var option;
		
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
						option={
						    title : {
						        text : title,
						        subtext : jsondata.body.subtitle
						    },
						    tooltip : {
				                trigger: 'item',
				                formatter : function (params) {
				                	//debugger;
				                	var ft="";
				               		if (params.value.length > 1) {
				               			if(flow_unit=='chart1'){
				               				ft="M";
				               			}else{
				               				ft="G";
				               			}
					                    return params.seriesName + '<br/>'
					                    	 + '项目编号: '+ params.value[2] +' <br/>' 
					                    	 + '项目名称: '+ params.value[3] +' <br/>' 
					                         + '流量 : '+ params.value[0]+''+ft+' <br/>'  
					                         + '金额 : '+ params.value[1]+'元 '; 
					                }else{
					                	if(flow_unit=='chart1'){
				               				ft="M";
				               			}else{
				               				ft="G";
				               			}
					                	return params.seriesName + ' :<br/>'
					                		   + '项目编号: '+ params.data.proj_code +' <br/>' 
					                    	   + '项目名称: '+ params.data.proj_name +' <br/>' 
							                   + '流量 : '+params.data.flow+''+ft+'<br/>'
							                   + params.name + ' : '
							                   + params.value + '元 ';
							                   
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
			
						   /* toolbox: {
						        show : true,
						        feature : {
						            dataView : {show: true, readOnly: false},
						            restore : {show: true},
						            saveAsImage : {show: true}
						        }
						    },*/
						    legend : {
						        data :  jsondata.body.rowname 
						    },
						    xAxis : [
						        {
						            type : 'value',
						            scale:true,
						            axisLabel : {
						                formatter:function(value){
						                	if(flow_unit=='chart1'){
						                		return value+'M' ;
						                	}else{
						                		return value+'G' ;
						                	}
						                } 
						            }
						        }
						    ],
						    yAxis : [
						        {
						            type : 'value',
						            scale:true,
						            axisLabel : {
						                formatter: '{value}元 '
						            }
						        }
						    ],
						    animation: false,
						    series :  jsondata.body.rowdata
						};
						if(jsondata.body.rowdata==0){
							//"toolbox":{"show":true,"feature":{"dataView":{"show":true,"readOnly":false},"restore":{"show":true},"saveAsImage":{"show":true}}},
							option={"title":{"text":"","subtext":""},"tooltip":{"trigger":"item","axisPointer":{"show":true}},"legend":{"data":[" "]},"xAxis":[{"type":"value"}],"yAxis":[{"type":"value"}],"animation":false,"series":[{"name":" ","type":"scatter","symbolSize":5,"data":[[]],"tooltip":{"trigger":"item","axisPointer":{"show":true}}}]};
							option.title={"text":title,"subtext":""};
						}
						
						
			           // myChart.hideLoading();	//loading hidden
			            
				}else{
					alert(jsondata.message);
				}
			
			},
			error : function(jsondata) {
				alert( "查询失败");
			}
			
		});		
		
		return option;
	}
	
	function echartsClear(){
		myChart1.clear();
		myChart2.clear();
		myChart3.clear();
		myChart4.clear();
	}
	
	function echartsSetOption(option1,option2,option3,option4){
		myChart1.setOption(option1);
		myChart2.setOption(option2);
		myChart3.setOption(option3);
		myChart4.setOption(option4);
	}
	
	var url = '../pages/feeTypeAnalysis_detail.jsp?show=details';
	if($("#company1").val()!=""){
		url+="&apply_dept="+$("#company1").val();
	}
	if($("#datestart1").val()!=""){
		url+="&date_start="+$("#datestart1").val();
	}
	if($("#dateend1").val()!=""){
		url+="&date_end="+$("#dateend1").val();
	}
	
	
	$(document).on('click', '#chart1', function(){
		$("#myModalLabel").html(title1);
		show(url+'&flow_min=1&flow_max=1023&flow_unit=MB&title=1M-1G');
	});
	$(document).on('click', '#chart2', function(){
		$("#myModalLabel").html(title2);
		show(url+'&flow_min=1&flow_max=50&flow_unit=GB&title=1G-50G');
	});
	$(document).on('click', '#chart3', function(){
		$("#myModalLabel").html(title3);
		show(url+'&flow_min=51&flow_max=100&flow_unit=GB&title=50G-100G');
	});
	$(document).on('click', '#chart4', function(){
		$("#myModalLabel").html(title4);
		show(url+'&flow_min=101&flow_max=200&flow_unit=GB&title=100G-200G');
	});
	
</script>
</html>