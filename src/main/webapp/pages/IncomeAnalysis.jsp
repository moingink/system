<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<title>子表模板</title>
<style type="text/css">
</style>
</head>
<%
	String ContextPath =request.getContextPath();
%>
<body>
			<div class="col-md-12" style="margin-top:10px;margin-bottom:10px;">
				<div class="col-md-3">
					<div class="col-md-4">
						<label style="width:100%;text-align:right;">营销单元</label>
					</div>
					<div class="col-md-8">
						<select class="company form-control" id="company1">
							<option selected="selected" value="">全部</option>
						</select>
					</div>				
				</div>
				<div class="col-md-3">
					<div class="col-md-4">
						<label style="width:100%;text-align:right;">年度</label>						
					</div>
					<div class="col-md-8">
						<select id="year1" class="form-control"></select>
					</div>				
				</div>
				<button class="btn btn-info" onclick="derive()">Excel导出</button>
			
			</div>
		

			
			
			
			<table border="1"class="table table-hover table-bordered" style="text-align: center; width:3000px;   vertical-align: inherit;margin-top: 10px;" >
				<thead>
					<tr class="info" style="background-color: rgb(255, 255, 255);">
						<th rowspan="2" style="text-align: center;" >项目编码</th>
						<th rowspan="2"  style="text-align: center;" >项目名称</th>
						<th rowspan="2"  style="text-align: center;" >合同编码</th>
						<th rowspan="2"  style="text-align: center;" >合同名称</th>
						<th rowspan="2"  style="text-align: center;" >计入以前年度</th>
						<th colspan="2" style="text-align: center;" >1月份</th>
						<th colspan="2" style="text-align: center;" >2月份</th>
						<th colspan="2" style="text-align: center;" >3月份</th>
						<th colspan="2" style="text-align: center;" >4月份</th>
						<th colspan="2" style="text-align: center;" >5月份</th>
						<th colspan="2" style="text-align: center;" >6月份</th>
						<th colspan="2" style="text-align: center;" >7月份</th>
						<th colspan="2" style="text-align: center;" >8月份</th>
						<th colspan="2" style="text-align: center;" >9月份</th>
						<th colspan="2" style="text-align: center;" >10月份</th>
						<th colspan="2" style="text-align: center;" >11月份</th>
						<th colspan="2" style="text-align: center;" >12月份</th>
						<th rowspan="2" style="text-align: center;" >实际收入合计</th>
						<th rowspan="2" style="text-align: center;" >帐显收入合计</th>
					</tr>
					<tr class="info" style="background-color: rgb(255, 255, 255);">
						<th style="text-align: center;" >实际收入</th>
						<th style="text-align: center;" >帐显收入</th>
						<th style="text-align: center;" >实际收入</th>
						<th style="text-align: center;" >帐显收入</th>
						<th style="text-align: center;" >实际收入</th>
						<th style="text-align: center;" >帐显收入</th>
						<th style="text-align: center;" >实际收入</th>
						<th style="text-align: center;" >帐显收入</th>
						<th style="text-align: center;" >实际收入</th>
						<th style="text-align: center;" >帐显收入</th>
						<th style="text-align: center;" >实际收入</th>
						<th style="text-align: center;" >帐显收入</th>
						<th style="text-align: center;" >实际收入</th>
						<th style="text-align: center;" >帐显收入</th>
						<th style="text-align: center;" >实际收入</th>
						<th style="text-align: center;" >帐显收入</th>
						<th style="text-align: center;" >实际收入</th>
						<th style="text-align: center;" >帐显收入</th>
						<th style="text-align: center;" >实际收入</th>
						<th style="text-align: center;" >帐显收入</th>
						<th style="text-align: center;" >实际收入</th>
						<th style="text-align: center;" >帐显收入</th>
						<th style="text-align: center;" >实际收入</th>
						<th style="text-align: center;" >帐显收入</th>
					</tr>
				</thead>
				<tbody id="tbody">
				</tbody>
			</table>

	
			
</body>
<jsp:include page="../include/public.jsp"></jsp:include>

<script type="text/javascript">

	$(function() {
	
		$.ajax({
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
		loadList('{}');
		
	   	
		$("#year1").change(function(){
			var json={};
			if($("#company1").val()!=""){
				json.company=$("#company1").val();
			}
			if($("#year1").val()!=""){
				json.year=$("#year1").val();
			}
			loadList(JSON.stringify(json));
		});
	
		$("#company1").change(function(){
			var json={};
			if($("#company1").val()!=""){
				json.company=$("#company1").val();
			}
			if($("#year1").val()!=""){
				json.year=$("#year1").val();
			}
			loadList(JSON.stringify(json));
		});
		
	});
	
		function loadList(json){
			$("#tbody").html("");
			$.ajax({
				type : "POST",
				url :  "/system/confirmIncome?cmd=incomeAnalysis",
				data : json,
				dataType : "json",
				async : false,
				cache : false,
				contentType : false,
				processData : false,
				success : function(data) {
					if(data.status=="success"){
						if(data.body.length==0){
							$("#tbody").append('<tr><td colspan="1000">暂时无数据</td></tr>');
						}else{
							for(var i=0;i<data.body.length;i++){
								var htmlstr='';
								htmlstr+='<tr>';
								htmlstr+=('<td>'+data.body[i].PROJ_CODE+'</td>');
								htmlstr+=('<td>'+data.body[i].PROJ_NAME+'</td>');
								htmlstr+=('<td>'+data.body[i].CONTRACT_CODE+'</td>');
								htmlstr+=('<td>'+data.body[i].CONTRACT_NAME+'</td>');
								htmlstr+=('<td>'+data.body[i].PREVIOUS_YEAR_CONFIRM+'</td>');
								htmlstr+=('<td>'+data.body[i].MONTH01_CONFIRM+'</td>');
								htmlstr+=('<td>'+data.body[i].MONTH01_ACCOUNT+'</td>');
								htmlstr+=('<td>'+data.body[i].MONTH02_CONFIRM+'</td>');
								htmlstr+=('<td>'+data.body[i].MONTH02_ACCOUNT+'</td>');
								htmlstr+=('<td>'+data.body[i].MONTH03_CONFIRM+'</td>');
								htmlstr+=('<td>'+data.body[i].MONTH03_ACCOUNT+'</td>');
								htmlstr+=('<td>'+data.body[i].MONTH04_CONFIRM+'</td>');
								htmlstr+=('<td>'+data.body[i].MONTH04_ACCOUNT+'</td>');
								htmlstr+=('<td>'+data.body[i].MONTH05_CONFIRM+'</td>');
								htmlstr+=('<td>'+data.body[i].MONTH05_ACCOUNT+'</td>');
								htmlstr+=('<td>'+data.body[i].MONTH06_CONFIRM+'</td>');
								htmlstr+=('<td>'+data.body[i].MONTH06_ACCOUNT+'</td>');
								htmlstr+=('<td>'+data.body[i].MONTH07_CONFIRM+'</td>');
								htmlstr+=('<td>'+data.body[i].MONTH07_ACCOUNT+'</td>');
								htmlstr+=('<td>'+data.body[i].MONTH08_CONFIRM+'</td>');
								htmlstr+=('<td>'+data.body[i].MONTH08_ACCOUNT+'</td>');
								htmlstr+=('<td>'+data.body[i].MONTH09_CONFIRM+'</td>');
								htmlstr+=('<td>'+data.body[i].MONTH09_ACCOUNT+'</td>');
								htmlstr+=('<td>'+data.body[i].MONTH10_CONFIRM+'</td>');
								htmlstr+=('<td>'+data.body[i].MONTH10_ACCOUNT+'</td>');
								htmlstr+=('<td>'+data.body[i].MONTH11_CONFIRM+'</td>');
								htmlstr+=('<td>'+data.body[i].MONTH11_ACCOUNT+'</td>');
								htmlstr+=('<td>'+data.body[i].MONTH12_CONFIRM+'</td>');
								htmlstr+=('<td>'+data.body[i].MONTH12_ACCOUNT+'</td>');
								htmlstr+=('<td>'+data.body[i].MONTH_TOTAL_CONFIRM+'</td>');
								htmlstr+=('<td>'+data.body[i].MONTH_TOTAL_ACCOUNT+'</td>');
								htmlstr+='</tr>';
								$("#tbody").append(htmlstr);
							}
						}
						
						var htmlstr1='';
						for(var i=parseInt(data.year)-5;i<parseInt(data.year)+5;i++){
							htmlstr1+='<option ';
							if(i==parseInt(data.year))
								htmlstr1+=' selected="selected" ';
							htmlstr1+=' value="'+i+'">'+i+'</option>';
						}
						$("#year1").html(htmlstr1);
					}else{
						alert( "查询失败");
					}
				},
				error : function(data) {
					alert( "查询失败");
				}
			});
		}

	function derive(){
		var json={};
		if($("#company1").val()!=""){
			json.company=$("#company1").val();
		}
		if($("#year1").val()!=""){
			json.year=$("#year1").val();
		}
		var encodeParam=encodeURIComponent(JSON.stringify(json));
		window.location.href="/system/confirmIncome?cmd=incomeAnalysisExport&json="+encodeParam; 
	}
</script>

</html>