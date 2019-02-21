<%@page import="com.yonyou.util.theme.ThemePath"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<%
	String ContextPath =request.getContextPath();
%>

<html lang="zh-cn">
	<head>
		<meta charset="utf-8">
		<title>单表模板</title>
	</head>
	<style>
		label{
			width:100%;
			text-align:right;
		}
	</style>
	<body style="background-color: rgb(240,240,241)">
		<form >
			<div  id="bulidTable">
				<input type="hidden" value='<%=request.getParameter("PARENT_PARTY_ID")%>'  id="PARENT_PARTY_ID" />
				<input type="hidden" value='<%=request.getParameter("PARENT_PARTY_CODE")%>'  id="PARENT_PARTY_CODE" />
				<div class="panel panel-primary">
					<div class="panel-heading" id ='pageName'>
						单表模板
					</div>
					<div id="bill_date_and_status"></div>
					<div class="panel-body" id="queryParam" style="display: none;">
						<div class="col-md-4" style="margin-top:10px;display:inline">
							<div class="form-group">
								<div class="col-md-4">
									<label for="CLIENT_NAME">客户名称：</label>
								</div>
								<div class="col-md-8">
									<input type="TEXT" class="form-control" id="CLIENT_NAME">
								</div>
							</div>
						</div>
						<div class="col-md-4" style="margin-top:10px;display:inline">
							<div class="form-group">
								<div class="col-md-4">
									<label for="INCOME_OF_DEPT">部门：</label>
								</div>
								<div class="col-md-8">
									<input type="TEXT" class="form-control" id="INCOME_OF_DEPT">
								</div>
							</div>
						</div>
						<div class="col-md-4" style="margin-top:10px;display:inline">
							<div class="form-group">
								<div class="col-md-4">
									<label for="BUSINESS_TYPE">业务类型：</label>
								</div>
								<div class="col-md-8">
									<input type="TEXT" class="form-control" id="BUSINESS_TYPE">
								</div>
							</div>
						</div>
						<div class="col-md-4" style="margin-top:10px;display:inline">
							<div class="form-group">
								<div class="col-md-4">
									<label for="PRODUCT_TYPE">产品：</label>
								</div>
								<div class="col-md-8">
									<input type="TEXT" class="form-control" id="PRODUCT_TYPE">
								</div>
							</div>
						</div>
						<div class="col-md-12"><br></div>
						<div class="col-md-8"></div>
						<div class="col-md-4" align="right" style="display: inline;"><div id="selectButtonPage"></div></div>
					</div>
					<div class="input-box-toggle" onclick="moreToggle()">
						<span class="caret"></span>更多搜索条件
					</div>
					<!-- 列表页面 -->
					<div id='button_div' class="button-menu"></div>
					<div class="panel-body">
						<div class="col-md-4" style="margin-top:10px;display:inline">
							<div class="form-group has-feedback">
								<div class="col-md-4">
									<label for="TERM">账期：</label>
								</div>
								<div class="col-md-8">
									<div class="input-group date form_date" style="width:99%" data-date="" data-link-field="TERM" data-link-format="yyyy-mm">
										<input type="TEXT" class="form-control" id="TERM" readonly="readonly" data-bv-field="TERM">
										<span class="input-group-addon">
											<span class="glyphicon glyphicon-calendar"></span>
										</span>
									</div>
								</div>
							</div>
						</div>
						<div class="col-md-4" style="margin-top:10px;display:inline">
							<div class="form-group has-feedback">
								<div class="col-md-4">
									<label  for="MONEY_TOTAL">金额总计(万元)：</label>
								</div>
								<div class="col-md-8">
									<input type="TEXT" class="form-control" id="MONEY_TOTAL" readonly="readonly">
								</div>
							</div>
						</div>
					</div>
					<table id="table" data-row-style="rowStyle"></table>
					<div class="panel-body">
						<div class="col-md-4" style="margin-top:10px;display:inline">
							<div class="form-group has-feedback">
								<div class="col-md-4">
									<label for="AUDIT_PEOPLE">审核人：</label>
								</div>
								<div class="col-md-8">
									<input type="TEXT" class="form-control" id="AUDIT_PEOPLE" readonly="readonly">
								</div>
							</div>
						</div>
						<div class="col-md-4" style="margin-top:10px;display:inline">
							<div class="form-group has-feedback">
								<div class="col-md-4">
									<label for="AUDIT_DATE">审核日期：</label>
								</div>
								<div class="col-md-8">
									<input type="TEXT" class="form-control" id="AUDIT_DATE" readonly="readonly">
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<input type="hidden" id="isNewStyle" value="1" />
		</form>
	</body>
	<jsp:include page="../include/public.jsp"></jsp:include>
	<jsp:include page="buttonjs.jsp"></jsp:include>
	<link rel="stylesheet" type="text/css" href="<%= ThemePath.findPath(request, ThemePath.SUB_SYSTEM_CSS)%>">
	<script type="text/javascript">
		var totalCode = '<%=request.getParameter("totalCode")%>';
		
		$(function() {
			bulidPage(true,true,true,true);
			$("#queryParam .control-label:last").css("display","none");
			$('.form_date').datetimepicker('remove');//去掉时间插件
			$('.form_date').datetimepicker({//月视图
	    		format: 'yyyy-mm',
		        autoclose: true,
		        todayBtn: true,
		        startView: 'year',
		        minView: 'year',
		        maxView: 'decade',
		        language: 'zh-CN',
		        pickerPosition:'bottom-right'
	    	});
		});
		
	</script>
</html>