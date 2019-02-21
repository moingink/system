<%@page import="com.yonyou.util.PropertyFileUtil"%>
<%@page import="com.yonyou.util.theme.ThemePath"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
	<head>
		<meta charset="utf-8">
		<title>多页签模板</title>
	</head>
	<body>
		<jsp:include page="../include/public.jsp"></jsp:include>
		<jsp:include page="buttonjs.jsp"></jsp:include>
		<form>
			<div class="col-md-12">
				<ul id="myTab" class="nav nav-tabs">
					<li><a href="#projBudgetIframeDiv" data-toggle="tab" onclick="setIframeHeight('#projBudgetIframe');">编制预算</a></li>
					<li><a href="#projProposalGkIframeDiv" data-toggle="tab" onclick="setIframeHeight('#projProposalGkIframe');">预算成本</a></li>
				</ul>
			</div>
			</br>
			<div class="col-md-12">
				<div class="tab-content">
					<div class="tab-pane fade" id='projBudgetIframeDiv'>
						<iframe id='projBudgetIframe' scrolling="auto" frameborder="0" width="99%" height='500px'></iframe>
					</div>
					<div class="tab-pane fade" id='projProposalGkIframeDiv'>
						<iframe id='projProposalGkIframe' scrolling="auto" frameborder="0" width="99%" height='500px'></iframe>
					</div>
				</div>
			</div>
		</form>
	</body>
	<script type="text/javascript">
		$(function(){
			var ParentPKValue = '<%=request.getParameter("ParentPKValue")%>';
			var keyWord = '<%=request.getParameter("keyWord")!=null?request.getParameter("keyWord"):""%>';
			var modifyId = '<%=request.getParameter("modifyId")%>';
			var projBudgetIframeUrl = "project/budget.jsp?keyWord=1&pageCode=PROJ_BUDGET&pageName=预算&ParentPKField=PROJ_SOURCE_ID&ParentPKValue="+ParentPKValue+"&modifyId="+modifyId+"&modifyFieldName=BUDGET_JSON&state=1";
			var projProposalGkDataSourceCode = 'PROJ_PROPOSAL_GK_BUILD_MODIFY';
			if(keyWord == '1'){//能力项目变更
				projProposalGkDataSourceCode = 'PROJ_PROPOSAL_GK_ABILITY_BUILD_MODIFY';
			}
			var projProposalGkIframeUrl = "maintainPage_project.jsp?keyWord=1&pageCode="+projProposalGkDataSourceCode+"&pageName=前评估信息&menuCode=0&ParentPKField=PROJ_SOURCE_ID&ParentPKValue="+ParentPKValue+"&modifyId="+modifyId+"&modifyFieldName=PRE_ASSESSMENT_JSON&state=1";
			$('#projBudgetIframe').attr('src',projBudgetIframeUrl);
			$('#projProposalGkIframe').attr('src',projProposalGkIframeUrl);
		});
		
		//设置Iframe高度
		function setIframeHeight(id){
			setDisabled();
			setTimeout(function(){
				var height = $(id).contents().find("body").height();
				if(height<200){
					height = 300;
				}
				$(id).height(height);
				$(window.parent.document).find("#myIframe1").height(height+80);
				window.parent.setParntHeigthCopy();
			},200);
		}
		
		//验证
		function iframeVerification(keyWord){
			var costC = $('#projBudgetIframe').contents().find("#cost_c").text();//预算总计
			var projectCost = $('#projProposalGkIframe').contents().find("#PROJECT_COST").val();//立项建设总成本	
			if(Number(costC) != Number(projectCost)){
				var message = '编制预算总金额 '+costC+'万元与项目概况中项目立项建设总成本'+projectCost+'万元，录入金额不一致，请将金额调整为一致后再保存数据';
				return false+":"+message;
			}
			
			if(keyWord == '1'){//能力项目变更
				return document.getElementById('projProposalGkIframe').contentWindow.iframeAbilityProjProposalGkVerification();
			}
			
			return document.getElementById('projProposalGkIframe').contentWindow.iframeProjProposalGkVerification();
		}
		
		//预算json
		function budgetJsonValue(){
			return document.getElementById('projBudgetIframe').contentWindow.jsonValue();
		}
		
		//前评估json
		function preAssessmentValue(){
			return document.getElementById('projProposalGkIframe').contentWindow.preAssessmentJsonValue();
		}
		
		//不可编辑
		function setDisabled(){
			if(!parent.disabledIframe){
				$('#projBudgetIframe').contents().find('[type=button]').hide();
				$('#projProposalGkIframe').contents().find("[id]").each(function() {
					$(this).attr("disabled",true);
				});
			}
		}
	</script>
</html>