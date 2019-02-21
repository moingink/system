<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<title>子表模板</title>
<style type="text/css">

	.form-horizontal{
		padding:7px;
	}
	.panelnew{
		border: 1px solid #337ab7;
   		border-radius: 4px;
	}

</style>
</head>
<%
	String other_party_main = request.getParameter("name_partner");
	String token = request.getParameter("token");
%>
<body>

	<form class="form-horizontal">
		<div class="panelnew">
			<div style="margin-top:10px; margin-bottom:10px;">
				<a class="btn btn-inverse" onclick="window.history.back(-1);">
					<span class="glyphicon glyphicon-ban-circle" aria-hidden="true"></span>返回
				</a>
			</div>
			<table class="table table-hover table-bordered" style="text-align: center;  width:700px;" >
				<thead id="thead">
					<tr class="info" style="background-color: rgb(255, 255, 255);">
						<td>合同编号</td>
						<td>合同名称</td>
						<td>项目编号</td>
						<td>项目名称</td>
						<td>业务类型</td>
						<td>产品</td>
					</tr>
				</thead>
				<tbody id="tbody">
				</tbody>
			</table>
			<div id="page">
			</div>
		</div>
	</form>

	
			
</body>
<jsp:include page="../include/public.jsp"></jsp:include>
<script type="text/javascript" src="../vendor/eCharts/echarts.js"></script>
<script type="text/javascript">

	$(function() {
		var other_party_main='<%=other_party_main%>';
		$.ajax({
			type : "POST",
			url :  "/system/reportStatis?cmd=contractPro&token=<%=token%>&other_party_main="+other_party_main,
			dataType : "json",
			success : function(data) {
				if(data.status=="success"){
					if(data.body.rowdata.length==0){
						$("#tbody").append('<tr><td colspan="6">暂时无数据</td></tr>');
					}else{
						for(var i=0;i<data.body.rowdata.length;i++){
							var htmlstr = '';
							htmlstr+='<tr>';
							htmlstr+='<td>'+data.body.rowdata[i].CON_ID+'</td>';
							htmlstr+='<td>'+data.body.rowdata[i].CON_NAME+'</td>';
							htmlstr+='<td>'+data.body.rowdata[i].PRO_ID+'</td>';
							htmlstr+='<td>'+data.body.rowdata[i].PRO_NAME+'</td>';
							htmlstr+='<td>'+data.body.rowdata[i].BUS_TYPE+'</td>';
							htmlstr+='<td>'+data.body.rowdata[i].PRODUCT+'</td>';
							htmlstr+='</tr>';
							$("#tbody").append(htmlstr);
						}
					}
				}else{
					alert( "查询失败");
				}
			},
			error : function(data) {
				alert( "查询失败");
			}
		});

	});
</script>

</html>