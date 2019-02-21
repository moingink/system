<%@page import="com.yonyou.util.theme.ThemePath"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<%
	String ContextPath =request.getContextPath();
%>

<html lang="zh-cn">
	<head>
		<meta charset="utf-8">
		<title>页签模板</title>
		<link href="dist/dialog.css" rel="stylesheet">
	</head>
	<script src="dist/mDialogMin.js"></script>
	<style type="text/css">
	.zt {
		color: #F00;
	}
	.pdr{
		padding-right: 0!important;
	}
	</style>
	<body id="body_div">
		<form class="form-horizontal">
			<div class="panel-body" id="bulidPage">
				<div class="panel panel-primary">
					<div class="panel-body">
						<div class="alert alert-info" id="message" style="display: none"></div>
						<div>
							<button id="insert" type="button" class="btn btn-success" onclick="save(this)" style="display: none;">
								<span class="glyphicon glyphicon-saved" aria-hidden="true"></span>保存
							</button>
							<button id="download" type="button" class="btn btn-info" onclick="preview()" style="display: none;">
								预览
							</button>
						</div>
						<div class="form-group">
							<div class="col-md-12">
								<div id="errors"></div>
							</div>
						</div>
						<!-- 维护页面 -->
						<div class="panel-body" id="insPage">
							<h2 align="center" style="color: red;">授权书</h2>
							<div align="right">
								<input id="WORD_TO_WORD" type="TEXT" style="width:250px; border: 0px; outline:none; cursor: pointer;" readonly="true">
							</div>
							<hr />
							<div class="col-md-12">
								<div class="form-group has-feedback">
									<div class="col-md-3 col-xs-3 col-sm-3" style="white-space: nowrap; text-align: right; margin-left: -5.5%">
										<label class="control-label" style="line-height: 29px;">授权单位：</label>
									</div>
									<div class="col-md-9 col-xs-9 col-sm-9" style="margin-left: -6px; margin-top: 10px">
										<input id="AUTHORIZED_UNIT" value="联通智网科技有限公司" type="TEXT" style="border: 0px; outline:none; cursor: pointer;" readonly="true">
									</div>
								</div>
							</div>
							<div class="col-md-12">
								<div class="form-group has-feedback">
									<div class="col-md-3 col-xs-3 col-sm-3" style="white-space: nowrap; text-align: right; margin-left: -5.5%">
										<label class="control-label" style="line-height: 29px;">法定代表人/负责人：</label>
									</div>
									<div class="col-md-9 col-xs-9 col-sm-9" style="margin-left: -6px; margin-top: 10px">
										<input id="LEGAL_REPRESENTATIVE" value="辛克铎" type="TEXT" style="border: 0px; outline:none; cursor: pointer;" readonly="true">
									</div>
								</div>
							</div>
							<div class="col-md-12">
								<div class="form-group has-feedback">
									<div class="col-md-3 col-xs-3 col-sm-3" style="white-space: nowrap; text-align: right; margin-left: -5.5%">
										<label class="control-label" style="line-height: 29px;">委托代理人：</label>
									</div>
									<div class="col-md-9 col-xs-9 col-sm-9" style="margin-left: -6px; margin-top: 10px">
										<input id="AGENT_PEOPLE" type="TEXT" style="border: 0px; outline:none; cursor: pointer;" readonly="true">
									</div>
								</div>
							</div>
							<div class="col-md-12">
								<div class="form-group has-feedback">
									<div class="col-md-3 col-xs-3 col-sm-3" style="white-space: nowrap; text-align: right; margin-left: -5.5%">
										<label class="control-label" style="line-height: 29px;">所属部门：</label>
									</div>
									<div class="col-md-3 col-xs-3 col-sm-3" style="margin-left: -6px; margin-top: 10px">
										<input id="SUBORDINATE_DEPARTMENT" value="" type="TEXT" style="border: 0px; outline:none; cursor: pointer;" readonly="true">
									</div>
									<div class="col-md-3 col-xs-3 col-sm-3" style="white-space: nowrap; text-align: right; margin-left: -5.5%">
										<label class="control-label" style="line-height: 29px;">职务：</label>
									</div>
									<div class="col-md-3 col-xs-3 col-sm-3" style="margin-left: -6px; margin-top: 10px">
										<input id="DUTIES" value="普通员工" type="TEXT" style="border: 0px; outline:none; cursor: pointer;" readonly="true">
									</div>
								</div>
							</div>
							<div class="col-md-12">
								<div class="form-group has-feedback">
									<div class="col-md-3 col-xs-3 col-sm-3" style="white-space: nowrap; text-align: right; margin-left: -5.5%">
										<label class="control-label" style="line-height: 29px;">授权事项及权利：</label>
									</div>
									<div class="col-md-9 col-xs-9 col-sm-9" style="margin-left: -6px; margin-top: 10px">
										<span>授权以上委托代理人审签以下事项，</span>
										<textarea class=" form-control" id="AUTHORIZATION_MATTERS_RIGHTS" name="AUTHORIZATION_MATTERS_RIGHTS" rows="3"
												  data-toggle="tooltip" data-bv-field="AUTHORIZATION_MATTERS_RIGHTS" data-original-title="授权事项及权利"></textarea>
										<i class="form-control-feedback" data-bv-icon-for="AUTHORIZATION_MATTERS_RIGHTS" style="display: none;"></i>
									</div>
								</div>
							</div>
							<div class="col-md-12">
								<div class="form-group has-feedback">
									<div class="col-md-3 col-xs-3 col-sm-3" style="white-space: nowrap; text-align: right; margin-left: -5.5%">
										<label class="control-label" style="line-height: 29px;">本授权书有效日期：</label>
									</div>
									<div class="col-md-9 col-xs-9 col-sm-9" style="margin-left: -6px; margin-top: 10px">
										<div class="input-group date form_date" style="width: 99%" data-date-format="dd MM yyyy" data-link-field="LETTER_OF_AUTHORIZATION_DATE" data-link-format="yyyy-mm-dd">
											<input type="TEXT" class="form-control" id="LETTER_OF_AUTHORIZATION_DATE" name="LETTER_OF_AUTHORIZATION_DATE" data-bv-field="LETTER_OF_AUTHORIZATION_DATE">
											<span class="input-group-addon">
												<span class="glyphicon glyphicon-remove"></span>
											</span>
											<span class="input-group-addon">
												<span class="glyphicon glyphicon-calendar"></span>
											</span>
										</div>
										<i class="form-control-feedback" data-bv-icon-for="LETTER_OF_AUTHORIZATION_DATE" style="display: none; top: 0px; z-index: 100;"></i>
									</div>
								</div>
							</div>
							<div align="right" style="margin-top: 15px;">
								<span>授权人：</span>
								<input id="AUTHORIZING_PERSON" value="辛克铎" type="TEXT" style="width: 130px; border: 0px; outline:none; cursor: pointer;" readonly="true">
							</div>
							<div align="right">
								<span>日期：</span>
								<input id="AUTHORIZING_DATE" value="" type="TEXT" style="border: 0px; outline:none; cursor: pointer;" readonly="true">
							</div>
							<hr id="hr"/>
						</div>
					</div>
				</div>
			</div>
			<input type="hidden" id="isNewStyle" value="1" />
		</form>
	</body>
	
	<jsp:include page="../include/public.jsp"></jsp:include>
	<jsp:include page="buttonjs.jsp"></jsp:include>
	<link rel="stylesheet" type="text/css" href="<%=ThemePath.findPath(request, ThemePath.SUB_SYSTEM_CSS)%>">
	
	<script type="text/javascript">
		//主表主键在子表字段名
		var ParentPKField = '<%=request.getParameter("ParentPKField")%>';
		//主表主键值
		var ParentPKValue = '<%=request.getParameter("ParentPKValue")%>';
		//申请人id
		var applyPeopleId = '<%=request.getParameter("APPLY_PEOPLE_ID")%>';
		//用印部门
		var useCachetDepartment = '<%=request.getParameter("USE_CACHET_DEPARTMENT")%>';
		//审批状态
		var billStatus = '<%=request.getParameter("BILL_STATUS")%>';
		//用印事由
		var useCachetCause = '<%=request.getParameter("USE_CACHET_CAUSE")%>';
		
		//初始化
		$(function(){
			bulidPage(false,true,false,true);//获取按钮页面为true是为了加载数据源对应js
			$("#ins_or_up_buttontoken").val('add');
			$("#"+ParentPKField).val(ParentPKValue);
			getRecord();
		});
		
		//回显数据
		function getRecord(){
			if(ParentPKValue == null || ParentPKValue == ''){
				ParentPKValue = '001';
			}
			var param = "&dataSourceCode="+dataSourceCode+"&SEARCH-"+ParentPKField+"="+ParentPKValue;
			if(typeof(specifyParam) != "undefined"){
				param += specifyParam;
			}
			var record = querySingleRecord(param);
			if(!jQuery.isEmptyObject(record)){
				//已存在记录时为修改
				$("#ins_or_up_buttontoken").val('update');
				$inspage.find("[id]").each(function() {			
	  				$(this).val(record[$(this).attr("id")]);
				});
			}
		}
	</script>
</html>