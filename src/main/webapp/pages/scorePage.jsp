<%@page import="com.yonyou.util.RmIdFactory"%>
<%@page import="com.yonyou.util.theme.ThemePath"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<%
	String ContextPath = request.getContextPath();
%>

<html lang="zh-cn">
	<head>
		<meta charset="utf-8">
		<title>丢标打分</title>
	</head>
	<body id="body_div">
		<form class="form-horizontal">
			<div class="panel panel-primary">
				<div class="panel-body" id="bulidPage">
					<div class="panel panel-primary">
						<div id="tog_titleName" class="panel-heading">中标结果</div>
						<div class="panel-body">
							<div class="alert alert-info" id="message" style="display: none;"></div>
							<div>
								<button id="ins_or_up_buttontoken" type="button" class="btn btn-success" onclick="bc(this)">
									<span class="glyphicon glyphicon-saved" aria-hidden="true"></span>保存
								</button>
								<button id="fh" type="button" class="btn btn-inverse" onclick="javascript:history.back(-1);">
									<span class="glyphicon glyphicon-saved" aria-hidden="true"></span>返回
								</button>
							</div>
							<div class="form-group">
								<div class="col-md-12">
									<div id="errors"></div>
								</div>
							</div>
							<!-- 维护页面 -->
							<div class="panel-body" id="insPage"></div>
							<div id="centerTable">
								<div id="scoreDiv" class="fixed-table-container" style="float:left;width:50%;">
									<div class="fixed-table-header" style="margin-right: 0px;">
										<center>
											<h4 id="mc">丢标打分</h4>
										</center>
										<table class="table table-hover">
											<thead id="scoreHead"></thead>
											<tbody id="scoreBody"></tbody>
										</table>
									</div>
								</div>
								<div id="funnel" style="width:50%;height:400px;float:right;"></div>
							</div>
						</div>
					</div>
				</div>
			<input type="hidden" id="isNewStyle" value="1" />
		</form>
	</body>
	<jsp:include page="../include/public.jsp"></jsp:include>
	<script type="text/javascript" src="../vendor/eCharts/echarts.js"></script>
	<jsp:include page="buttonjs.jsp"></jsp:include>
	<link rel="stylesheet" type="text/css" href="<%=ThemePath.findPath(request, ThemePath.SUB_SYSTEM_CSS)%>">
	<script type="text/javascript">
		//主表主键在子表字段名
		var ParentPKField = '<%=request.getParameter("ParentPKField")%>';
		//主表主键值
		var ParentPKValue = '<%=request.getParameter("ParentPKValue")%>';
		//主表数据源编码
		var pageCode = '<%=request.getParameter("pageCode")%>';
		//评分数据源编码
		var scorePageCode = '<%=request.getParameter("scorePageCode")%>';
		//入口标识
		var keyWord = '<%=request.getParameter("keyWord")%>';
		var isShow = '<%=request.getParameter("isShow")%>';
		
		var gradingProjectArray = [
									{'name':'价格','explain':'价格'},
									{'name':'技术','explain':'技术'},
									{'name':'客情关系','explain':'客情关系'},
									{'name':'策略方案','explain':'策略方案'},
									{'name':'讲标','explain':'讲标'}
							  	  ];
		
		$(function() {
			bulidPage(false,true,false,true);
			initTable();
			setUp();
			choiceNotEdit("#insPage");
			hideFile($('#insPage'));
		});
		
		function getRecord(){
			var param = "&dataSourceCode="+pageCode+"&SEARCH-ID="+ParentPKValue;
			var record = querySingleRecord(param);
			return record;
		}
		
		function setUp(){
			var record = getRecord();
			if(!jQuery.isEmptyObject(record)){
				$inspage.find("[id]").each(function() {			
	  				$(this).val(record[$(this).attr("id")]);
				});
			}
			resultChange($('#insPage'));
		}
		
		function resultChange($div){
			var result = $div.find('#RESULT option:selected').val();
			if(result == '0'){
				$div.find('#REASON_ANALYSIS').parents('.col-md-12').css("display","block");
			}else{
				$div.find('#REASON_ANALYSIS').val('').parents('.col-md-12').css("display","none");
			}
		}
		
		//获取表格数据
		function getTableData(){
			var tableData = new Object();
			$.ajax({
				type: "GET",
				url: "/system/project?cmd=find_modifyExecute&dataSourceCode="+scorePageCode+"&ParentPKField="+ParentPKField+"&ParentPKValue="+ParentPKValue,
				async: false,
				dataType: "json",
				success: function(data) {
					tableData = data;
				}
			});
			return tableData;
		}
		
		//初始化表格
		function initTable(){
			initTableHead();
			initTableBody();
			setDataTableBody();
			initEcharts();
			setUpTable();
		}
		
		function initTableHead(){
			var tableHeadHtml = '<tr class="info">'+
									'<th style="display: none;"></th>'+
									'<th style="display: none;"></th>'+
									'<th style="text-align: center; vertical-align: middle; width:100px;" tabindex="0">'+
										'<div class="th-inner ">评分项目</div>'+
										'<div class="fht-cell"></div>'+
									'</th>'+
									'<th style="text-align: center; vertical-align: middle; width:100px;" tabindex="0">'+
										'<div class="th-inner ">分值</div>'+
										'<div class="fht-cell"></div>'+
									'</th>'+
								'</tr>';
			$("#scoreHead").html(tableHeadHtml);
		}
		
		function initTableBody(){
			for(var i=0;i<5;i++){
				var tableBodyHtml = '<tr>'+
										'<td style="display: none;">'+
											'<input type="TEXT" id="ID'+i+'" class="form-control">'+						
										'</td>'+
										'<td style="display: none;">'+
											'<input type="TEXT" id="PARENT_ID" class="form-control" value="'+ParentPKValue+'">'+						
										'</td>'+
										'<td>'+
											'<input type="TEXT" id="GRADING_PROJECT'+i+'" class="form-control" title="'+gradingProjectArray[i]['explain']+'" value="'+gradingProjectArray[i]['name']+'" readonly=true>'+
										'</td>'+
										'<td style="text-align: center; vertical-align: middle;">'+
											'<select class="form-control" id="FRACTION'+i+'">'+
												'<option value="0">0</option>'+
												'<option value="1">1</option>'+
												'<option value="2">2</option>'+
												'<option value="3">3</option>'+
												'<option value="4">4</option>'+
												'<option value="5">5</option>'+
											'</select>'+
										'</td>'+
									'</tr>';
				$("#scoreBody").append(tableBodyHtml);
			}
		}
		
		function setDataTableBody(){
			var tableData = getTableData();
			if(!jQuery.isEmptyObject(tableData)){
				for(var j=0; j<tableData.length; j++){
					$('#ID'+j).val(tableData[j]['ID']);
					$('#GRADING_PROJECT'+j).val(tableData[j]['GRADING_PROJECT']);
					$('#FRACTION'+j).val(tableData[j]['FRACTION']);
				}
			}
		}
		
		function initEcharts(){
			var myChart = echarts.init(document.getElementById('funnel'));
			myChart.setOption(funnel());
			for(var i=0; i<5; i++){
				$('#FRACTION'+i).change(function(){
					var myChart = echarts.init(document.getElementById('funnel'));
					myChart.setOption(funnel());
				});
			}
		}
		
		function setUpTable(){
			$("#scoreDiv").css("width","400");
			if(isShow == 'true'){
				$('#centerTable').css('display','none');
				$('#ins_or_up_buttontoken').css('display','none');
			}
			if(isHide == 'true'){
				$('#scoreBody').find('[id]').each(function(){
					$(this).attr('disabled',true);
				});
				$('#ins_or_up_buttontoken').css('display','none');
			}
			setParntHeigth($(document.body).height());
		}
		
		function getJson(table){
			var json = "[";
			for(var i=0; i<table.length; i++){
				var ID = $("#scoreBody tr:eq("+i+")").children().eq(0).find("input").val();
				json += "{\"ID\":\""+ID+"\",";
				
				var PARENT_ID = $("#scoreBody tr:eq("+i+")").children().eq(1).find("input").val();
				json +="\"PARENT_ID\":\""+PARENT_ID+"\",";
				
				var GRADING_PROJECT = $("#scoreBody tr:eq("+i+")").children().eq(2).find("input").val();
				json +="\"GRADING_PROJECT\":\""+GRADING_PROJECT+"\",";
				
				var FRACTION = $("#scoreBody tr:eq("+i+")").children().eq(3).find("select").val();
				json +="\"FRACTION\":\""+FRACTION+"\"},";
			}
			json = json.substring(0,json.length-1)+"]";
			return json;
		}
		
		//保存
		function bc(t){
			var jsonobj = JSON.parse(getJson($('#scoreBody tr')));
			for(var i=0; i<jsonobj.length; i++){
				var buttonToken = 'add';
				if(jsonobj[i]["ID"] !=null && jsonobj[i]["ID"] !=""){
					buttonToken = 'update';
				}
				var jsonString = JSON.stringify(jsonobj[i]);
				transToServer(findBusUrlByButtonTonken(buttonToken,'&OPERATION=PINGFEN&BUSINESS_ID='+$("#BUSINESS_ID").val(),scorePageCode),jsonString);
			}
			oTable.showModal('提示', "保存成功");
			$('#ins_or_up_buttontoken').attr("disabled",true);
			localSmallAreaHide($('#scoreBody'));
		}
		
		function funnel(){
			var xdata = [
							{text:'价格',max:5},
							{text:'技术',max:5},
							{text:'客情关系',max:5},
							{text:'策略方案',max:5},
							{text:'讲标',max:5}
						];
			var ydata = [];
			for(var i=0;i<5;i++){
				ydata.push($("#FRACTION"+i).val());
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
				            radius: 130,
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
									name:'丢标打分'
				                }
				            ]
				        },
				    ]
				};
		return option;
	}
		
	</script>
</html>