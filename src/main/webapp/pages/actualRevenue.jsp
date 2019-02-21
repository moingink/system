<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<title>子表模板</title>
<style>
	#bg{
		margin:15px;
	}
	.info th{
		text-align:center;
		vertical-align: middle;
	}
	#tbody td{
		
		text-align:center;
		vertical-align: middle;
	}
</style>


</head>
<%
	String ContextPath =request.getContextPath();
	String token = request.getParameter("token");
%>
<body>

	<form class="form-horizontal">
		<div style="border:1px solid #337ab7; width:auto;">
			
			<div class="col-md-12" style="margin-top:10px;margin-bottom:10px;">
				<div class="col-md-3">
					<div class="col-md-4">
						<label style="width:100%;text-align:right;">客户</label>
					</div>
					<div class="col-md-8">
						<select class="customer form-control" id="customer1">
							<option selected="selected"value="">全部</option>
						</select>
					</div>				
				</div>
				<div class="col-md-3">
					<div class="col-md-4">
						<label style="width:100%;text-align:right;">项目</label>						
					</div>
					<div class="col-md-8">
						<select class="pro_name form-control" id="pro_name1">
							<option selected="selected"value="">全部</option>
						</select>
					</div>				
				</div>
				<!-- <a href="#" class="btn btn-info"><span id="query2">查询</span></a> -->
			
			
			</div>
						
			<div style="width:100%;" class="table-responsive">
			
			<table border="1"class="table table-hover table-bordered" style="text-align: center; white-space:nowrap" >
				<thead>
					<tr class="info" style="background-color: rgb(255, 255, 255);">
						<th>客户名称</th>
						<th>商机编号</th>
						<th>项目编号</th>
						<th>项目名称</th>
						<th>计收填报月份</th>
						<th>部门</th>
						<th>客户经理</th>
						<th>合同编号</th>
						<th>合同名称</th>
						<th>合同签约方</th>
						<th>业务类型</th>
						<th>产品类别</th>
						<th>实际发生月份</th>
						<th>本月确认收入</th>
						<th>本月新增连接数</th>
						<th>本月累计连接数</th>
					</tr>
				</thead>
				<tbody id="tbody">
				</tbody>
			</table>
			
			
			</div>
			<div id="page">
			</div>
			<div id="bg" style="width: 600px;height:400px;"></div>
			
		</div>
		<input type="hidden" id="currentPage" value="1"/>
		<input type="hidden" id="total" value="0"/>
	</form>

	
			
</body>
<jsp:include page="../include/public.jsp"></jsp:include>
<script type="text/javascript" src="../vendor/eCharts/echarts.js"></script>
<script type="text/javascript">

	$(function() {
		loadList('{}');
		$.ajax({
			type : "POST",
			url :  "/system/actualAnalysis?cmd=conditionInfo&token=<%=token%>",
			data : '{}',
			dataType : "json",
			success : function(data) {
				if(data.status=="success"){
					for(var i=0;i<data.body.rowdata.length;i++){
						$(".customer").append('<option value="'+data.body.rowdata[i].CUS_NAME+'">'+data.body.rowdata[i].CUS_NAME+'</option>');
					}
					for(var i=0;i<data.body.rowdata1.length;i++){
						$(".pro_name").append('<option value="'+data.body.rowdata1[i].PRO_NAME+'">'+data.body.rowdata1[i].PRO_NAME+'</option>');
					}
					
				}else{
					alert( "查询失败");
				}
			},
			error : function(data) {
				alert( "查询失败");
			}
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
	   	
	   	
		$("#query2").click(function(){
			var json={};
			if($("#customer1").val()!=""){
				json.customer=$("#customer1").val();
			}
			if($("#pro_name1").val()!=""){
				json.pro_name=$("#pro_name1").val();
			}
			loadList(JSON.stringify(json));
		});
	
		$("#customer1").change(function(){
			var json={};
			if($("#customer1").val()!=""){
				json.customer=$("#customer1").val();
			}
			if($("#pro_name1").val()!=""){
				json.pro_name=$("#pro_name1").val();
			}
			loadList(JSON.stringify(json));
		});
		
		$("#pro_name1").change(function(){
			var json={};
			if($("#customer1").val()!=""){
				json.customer=$("#customer1").val();
			}
			if($("#pro_name1").val()!=""){
				json.pro_name=$("#pro_name1").val();
			}
			loadList(JSON.stringify(json));
		});
		
	});
	
		function loadList(json){
			$("#tbody").html("");
			$("#page").html("");
			$.ajax({
				type : "POST",
				url :  "/system/actualRevenue?cmd=feeTypeAnalysisDetail&token=<%=token%>",
				data : json,
				dataType : "json",
				async : false,
				cache : false,
				contentType : false,
				processData : false,
				success : function(data) {
					if(data.status=="success"){
						if(data.body.rowdata.length==0){
							$("#tbody").append('<tr><td colspan="16">暂时无数据</td></tr>');
							$("#bg").hide();
						}else{
							for(var i=0;i<data.body.rowdata.length;i++){
								var htmlstr='';
								htmlstr+='<tr>';
								htmlstr+=('<td>'+data.body.rowdata[i].CUS_NAME+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].BUS_ID+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].PRO_ID+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].PRO_NAME+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].COLL_FILL_TIME+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].DEP+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].CUS_MANAGER+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].CON_ID+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].CON_NAME+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].CON_SIGN_OF+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].SERVICE_TYPE+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].PRO_TYPE+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].ACTUAL_HAPPEN_MON+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].SURE_PRICE+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].MON_NER_NUM+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].SUM_CONNECT+'</td>');
								htmlstr+='</tr>';
								$("#tbody").append(htmlstr);
							}
							
							var myChart = echarts.init(document.getElementById('bg'));
							if($("#customer1").val()!=""){
								$.ajax({
									type : "POST",
									url :  "/system/actualRevenue?cmd=feeTypeAnalysisDetailGb&token=<%=token%>",
									data : json,
									dataType : "json",
									async : false,
									cache : false,
									contentType : false,
									processData : false,
									success : function(data) {
										if(data.status=="success"){
											myChart.setOption(bg("客户收入趋势图",data));
											$("#bg").show();
										}else{
											alert("查询失败");
										}
										
									},
									error : function(data) {
										alert( "查询失败");
									}
								});
							}else{
								$.ajax({
									type : "POST",
									url :  "/system/actualRevenue?cmd=feeTypeAnalysisDetailGb&token=<%=token%>",
									data : json,
									dataType : "json",
									async : false,
									cache : false,
									contentType : false,
									processData : false,
									success : function(data) {
										if(data.status=="success"){
											myChart.setOption(bg("汇总收入趋势图",data));
											$("#bg").show();
										}else{
											alert("查询失败");
										}
									},
									error : function(data) {
										alert( "查询失败");
									}
								});
							}
							
							
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
							});
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
		
		
		//折线图
		function bg(title,data){
			var xdata = [];
  			var ydata = [];
  			for(var i=0;i<data.body.rowdata1.length;i++){
   	  			xdata.push(data.body.rowdata1[i].ACTUAL_HAPPEN_MON);
   	  			ydata.push(data.body.rowdata1[i].SURE_PRICE);
  			}
		    console.log(xdata);
		    console.log(ydata);
			var option = {
				title: {
					x:'center',
		           text: title
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
			                formatter: '{value} '
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