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
	String partner_type_ids = request.getParameter("partner_type_ids");
	String token = request.getParameter("token");
%>
<body>

	<form class="form-horizontal">
		<div class="panelnew">
			<div style="margin-top:10px; margin-bottom:10px;">
				<a class="btn btn-inverse" onclick="window.history.back(-1);">
					<span class="glyphicon glyphicon-ban-circle" aria-hidden="true"></span>返回
				</a>
				<input type="button" value="关联合同项目" onclick="inspe();" class="btn btn-primary">
			</div>
			
			<table class="table table-hover table-bordered" style="text-align: center;  width:700px;" >
				<thead id="thead">
					<tr class="info" style="background-color: rgb(255, 255, 255);">
						<td></td>
						<td>合作伙伴名称</td>
						<td>法定代表人</td>
						<td>注册资金</td>
						<td>公司类型</td>
						<td>审批状态</td>
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
<script type="text/javascript">
	var name_partner='';
	$(function(){
		var ptm = '<%=partner_type_ids%>';
		$.ajax({
				type : "GET",
				url :  "/system/reportStatis?cmd=partner&token=<%=token%>&partner_type_ids="+ptm,
				dataType : "json",
				success : function(data) {
					if(data.status=="success"){
						if(data.body.rowdata.length==0){
							$("#tbody").append('<tr><td colspan="6">暂时无数据</td></tr>');
						}else{
							for(var i=0;i<data.body.rowdata.length;i++){
								var htmlstr = '';
								htmlstr+='<td>'+data.body.rowdata[i].NAME_PARTNER+'</td>';
								htmlstr+='<td>'+data.body.rowdata[i].LEGAL_REPRESENTATIVE+'</td>';
								htmlstr+='<td>'+data.body.rowdata[i].REGISTERED_CAPITAL+'</td>';
								htmlstr+='<td>'+data.body.rowdata[i].COMPANY_TYPE+'</td>';
								htmlstr+='<td>'+data.body.rowdata[i].BILL_STATUS+'</td>';
								//htmlstr+='</tr>';
								$("#tbody").append('<tr><td class="bs-checkbox"><input name="partner" type="checkbox"></td>'+htmlstr+'</tr>');
							}
							
							$("#tbody tr").click(function(){
				                $(this).find(":checkbox").click(function(event){
				                    event.stopPropagation();
				                });
				                if($(this).find(":checkbox").prop("checked")==true){
				                    $(this).find(":checkbox").prop("checked",false);
				                }else{
				                    $(this).find(":checkbox").prop("checked",true);
				                }
				                name_partner=$(this).children('td').eq(1).text();
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
			
			
	});
	
	function inspe(){
		if($("input:checkbox:checked").length!=1){
			alert("请选择一条数据");
			return;
		}
		
		window.location.href="./contractProList.jsp?token=<%=token%>&name_partner="+name_partner;
	}
</script>

</html>