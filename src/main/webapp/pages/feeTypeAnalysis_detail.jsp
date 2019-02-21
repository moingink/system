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
	String apply_dept = request.getParameter("apply_dept");
	String date_start = request.getParameter("date_start");
	String date_end = request.getParameter("date_end");
	String flow_min = request.getParameter("flow_min");
	String flow_max = request.getParameter("flow_max");
	String flow_unit = request.getParameter("flow_unit");
	String title = request.getParameter("title");
%>
<body>

	<form class="form-horizontal" style="padding:7px;">
		<div style="border: 1px solid #337ab7;border-radius: 4px;">
<!-- 		<div class="col-md-4" style="margin:10px 0">
			<div class="form-group">
                   <div class="col-md-4" style="text-align: right;">
                   		<label class="control-label" style="padding-top:0;">营销单元：</label>
                   </div>
	               <div class="col-md-8">
	                   	<select class="company form-control" id="company1">
	                   		<option selected="selected" value="">全部</option>
						</select>
	               </div>
            </div>
		</div>
		<div class="col-md-3" style="margin:10px 0">
			<div class="form-group">
                   <div class="col-md-4" style="text-align: right;">
                   		<label class="control-label" style="padding-top:0;">开始日期：</label>
                   </div>
	               <div class="col-md-8">
	               	<input type="text" id="datestart1"   class="datetimepicker form-control"  />
	               </div>
            </div>
		</div>
		<div class="col-md-3" style="margin:10px 0">
			<div class="form-group">
                   <div class="col-md-4" style="text-align: right;">
                   		<label class="control-label" style="padding-top:0;">结束日期：</label>
                   	</div>
	               <div class="col-md-8">
	               	<input type="text" id="dateend1"  class="datetimepicker form-control" />
	               </div>
            </div>
		</div>
		<div class="col-md-2" style="margin:10px 0">
			<a href="#"><span id="query2" class="btn btn-info">查询</span></a>
		</div> -->	
			
			
			<button style="margin: 20px;" type="button" onclick="exportExcelp()" class="btn btn-info">Excel导出</button>			
			<table class="table table-hover table-bordered" style="text-align: center;    vertical-align: inherit;margin-top: 10px;" >
				<thead>
					<tr class="info" style="background-color: rgb(255, 255, 255);">
						<th>营销单元</th>
						<th>项目编号</th>
						<th>项目名称</th>
						<th>流量</th>
						<th>金额（元）</th>
					</tr>
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

<script type="text/javascript">
	var export_url="/system/buttonBase?cmd=button&buttonToken=exportFeeTypeAnalysis";
	var url = "";
	$(function() {
	//debugger;
		
		if(<%=apply_dept%>!=null){
			url+="&apply_dept=<%=apply_dept%>";
		}
		if(<%=date_start%>!=null){
			url+="&date_start=<%=date_start%>";
		}
		if(<%=date_end%>!=null){
			url+="&date_end=<%=date_end%>";
		}
		if(<%=flow_min%>!=null){
			url+="&flow_min=<%=flow_min%>&flow_max=<%=flow_max%>&flow_unit=<%=flow_unit%>";
		}
		export_url+=url;
		loadList("/system/feeTypeAnalysis?cmd=feeTypeAnalysisDetail"+url,'{}');
		/*$.ajax({
			type : "POST",
			url :  "/system/businessAnalysis?cmd=companyInfo",
			data : '{}',
			dataType : "json",
			success : function(data) {
				if(data.status=="success"){
					for(var i=0;i<data.body.rowdata.length;i++){
						$(".company").append('<option value="'+data.body.rowdata[i].ID+'">'+data.body.rowdata[i].NAME+'</option>');
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
			if($("#company1").val()!=""){
				json.company=$("#company1").val();
			}
			if($("#datestart1").val()!=""){
				json.datestart=$("#datestart1").val();
			}
			if($("#dateend1").val()!=""){
				json.dateend=$("#dateend1").val();
			}
			loadList(JSON.stringify(json));
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
			loadList(JSON.stringify(json));
		});*/
		
	});
	
		function loadList(url,json){
			$("#tbody").html("");
			$.ajax({
				type : "POST",
				url :  url,
				data : json,
				dataType : "json",
				async : false,
				cache : false,
				contentType : false,
				processData : false,
				success : function(data) {
					if(data.status=="success"){
						if(data.body.rowdata.length==0){
							$("#tbody").append('<tr><td colspan="5">暂时无数据</td></tr>');
						}else{
							for(var i=0;i<data.body.rowdata.length;i++){
								var htmlstr='';
								htmlstr+='<tr>';
								htmlstr+=('<td>'+data.body.rowdata[i].APPLY_DEPT+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].PROJ_CODE+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].PROJ_NAME+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].FLOW+'</td>');
								htmlstr+=('<td>'+data.body.rowdata[i].APPLICATION_PRICE+'</td>');
								htmlstr+='</tr>';
								$("#tbody").append(htmlstr);
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
		
		function exportExcelp(){
			//debugger;
			export_url+="&title=<%=title%>";
			/*$.ajax({
				type : "GET",
				url :  export_url,
				async : false
			});*/
			window.location.href=export_url;
		}

</script>

</html>