<%@page import="com.yonyou.util.PropertyFileUtil"%>
<%@page import="com.yonyou.util.theme.ThemePath"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<title>多页签模板</title>
</head>
<body>
<jsp:include page="../include/public.jsp"></jsp:include>
<jsp:include page="buttonjs.jsp"></jsp:include>
<%
	String PROJECT_COST=request.getParameter("PROJECT_COST");
	String SET_BUILD_INVESTMENT_COST_T=request.getParameter("SET_BUILD_INVESTMENT_COST_T");
	String SET_BUILD_INVESTMENT_COST_F=request.getParameter("SET_BUILD_INVESTMENT_COST_F");
	String BUILD_INVESTMENT_COST_ALL=request.getParameter("BUILD_INVESTMENT_COST_ALL");
	String BUILD_INVESTMENT_COST_T=request.getParameter("BUILD_INVESTMENT_COST_T");
	String BUILD_INVESTMENT_COST_F=request.getParameter("BUILD_INVESTMENT_COST_F");
	System.out.print("第二BUILD_INVESTMENT_COST_ALL+++++++++++"+BUILD_INVESTMENT_COST_ALL);
	
	
%>

	<form>
		<div class="col-md-12">
			<ul id="myTab" class="nav nav-tabs" style="width:1080px;height:42px;">
				<li>
					<btn id="show" class="btn btn-inverse" onclick="window.history.back(-1);" style="margin-top:4px;margin-right:5px;">
						<span class="glyphicon glyphicon-ban-circle" aria-hidden="true"></span>返回
					</btn>
				</li>
				<li class="active"><a id="show" href="#1" data-toggle="tab">项目概况</a></li>
				<li><a id="show" href="#5" data-toggle="tab">项目组织架构</a></li>
				<li><a id="show" href="#4" data-toggle="tab" onclick="refreshFrameByBudget()">编制预算</a></li>
				<li><a id="show" href="#3" data-toggle="tab">项目进度阶段划分</a></li>
				<li><a id="show" href="#6" data-toggle="tab" onclick="refreshFrame()">WBS</a></li>
				<li><a id="show" href="#8" data-toggle="tab">项目风险问题管控</a></li>
				<li><a id="show" href="#9" data-toggle="tab">其他计划</a></li>
				<li>
					<btn id="tj" class="btn btn-success" onclick="audit(this);" buttontoken="audit" style="margin-top:4px;">
						<span class="glyphicon glyphicon-check" aria-hidden="true"></span>提交
					</btn> 
				</li>
				<li>
					<btn class="btn btn-primary" onclick="preview_all(this);" buttontoken="audit" style="margin-top:4px;">
						<span class="glyphicon glyphicon-check" aria-hidden="true"></span>预览
					</btn>
				</li>
				<li>
					<btn id="spjc" class="btn btn-info" onclick="pubVisitHis(this);" style="margin-top:4px;">
						<span class="glyphicon glyphicon-check" aria-hidden="true"></span>审批进程
					</btn>
				</li>
			</ul>
		</div>
		</br>
		<div class="col-md-12">
			<div class="tab-content">

				<div class="tab-pane fade in active" id='1'>
					 <iframe src="maintainPage_project.jsp?isHide=<%=request.getParameter("isHide")%>&pageCode=PROJ_PROPOSAL_GK&pageName=项目基本信息&ParentPKField=PROJ_SOURCE_ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>&state=1&PROJECT_COST=<%=PROJECT_COST%>&SET_BUILD_INVESTMENT_COST_T=<%=SET_BUILD_INVESTMENT_COST_T %>&SET_BUILD_INVESTMENT_COST_F=<%=SET_BUILD_INVESTMENT_COST_F %>&BUILD_INVESTMENT_COST_ALL=<%=BUILD_INVESTMENT_COST_ALL%>&BUILD_INVESTMENT_COST_F=<%=BUILD_INVESTMENT_COST_F%>&BUILD_INVESTMENT_COST_T=<%=BUILD_INVESTMENT_COST_T%>&token=<%=request.getParameter("token") %>&state=1" scrolling="no" frameborder="0" width="99%" height="1380" id="PROJ_PROPOSAL_NL" style="min-width:1050px;"></iframe>
				</div>
				<div class="tab-pane fade" id='3'>
				<iframe src="project/progressPlan.jsp?isHide=<%=request.getParameter("isHide")%>&pageCode=PROJ_PROGRESS_PHASE&pageName=进度计划&ParentPKField=PROJ_SOURCE_ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>&state=1" scrolling="no" frameborder="0" width="99%" height="1000"></iframe>
					
				</div>
				<div class="tab-pane fade " id='4'>
					<iframe id="budget" src="project/budget.jsp?isHide=<%=request.getParameter("isHide")%>&pageCode=PROJ_BUDGET&pageName=项目预算&ParentPKField=PROJ_SOURCE_ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>&state=1" scrolling="no" frameborder="0" width="99%" height="1000"></iframe>
				</div>
				<div class="tab-pane fade " id='5'>
					<iframe src="project/projectOrganization.jsp?isHide=<%=request.getParameter("isHide")%>&pageCode=PROJ_ORGANIZATION&pageName=组织架构&ParentPKField=PROJ_SOURCE_ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>&state=1"  scrolling="no" frameborder="0" width="99%" height="1000"></iframe>
				</div>
				<div class="tab-pane fade " id='6'>
					<iframe id= "wbs" src="ganttmessage/wbs_gantts.jsp?isHide=<%=request.getParameter("isHide")%>&pageCode=PROJ_WBSINFO&pageName=wbs&ParentPKField=PROJ_SOURCE_ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>&state=1" scrolling="no" frameborder="0" width="99%" height="1000"></iframe>
				</div>
				<div class="tab-pane fade" id='8'>
					<iframe src="project/riskTracking.jsp?isHide=<%=request.getParameter("isHide")%>&pageCode=PROJ_RISK_MANAGEMENT&pageName=项目风险问题管控&ParentPKField=PROJ_SOURCE_ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>&state=1" scrolling="no" frameborder="0" width="99%" height="1000"></iframe>
				</div>
				<div class="tab-pane fade" id='9'>
					<iframe src="otherPlans.jsp?isHide=<%=request.getParameter("isHide")%>&pageCode=PROJ_OTHERPLAN&pageName=客户建设项目其他计划&ParentPKField=PROJ_SOURCE_ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>&state=1" scrolling="no" frameborder="0" width="99%" height="1000" style="min-width:1000px;"></iframe>
				</div>
			</div>
		</div>

	</form>
<div class="modal fade" id="modalX" tabindex="-1" role="dialog" aria-labelledby="modalTitle" aria-hidden="true">
	<div class="modal-dialog" style="width: 90%; min-width:1030px;" >
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h4 class="modal-title" id="modalTitle"></h4>
			</div>
			<div id="myModalX" class="modal-body"></div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" id="qd_modal">确定</button>
			</div>
		</div>
	</div>
</div>
</body>
<script type="text/javascript">
	
	$("#PROJECT_COST").attr("disabled",true);
	$("#OPPORTUNITY_CODE").attr("disabled",true);
	$("#PROJ_NAME").attr("disabled",true);
	$("#BUILD_INVESTMENT_COST_F").attr("disabled",true);
	$("#BUILD_INVESTMENT_COST_T").attr("disabled",true);
	$("#BUILD_INVESTMENT_COST_ALL").attr("disabled",true);
	
	
	$(function (){
		
		 setParntHeigth(1500);
		
	});

	hide(isHide);//隐藏及置灰
	token = '<%=request.getParameter("token")!=null?request.getParameter("token"):""%>';
	workflow = '<%=PropertyFileUtil.getValue("WORK_FLOW_HIS")%>';
	console.log("token:"+token);
	console.log("isHide:"+isHide);
	//使用维护页面模板的每次激活页签时向服务器请求最新数据更新此页签
	$('a[data-toggle="tab"]').on('show.bs.tab', function (e) {
		console.log(e.target.hash);
		var getRecord = $(e.target.hash+" iframe:first-child")[0].contentWindow.getRecord;
		if(typeof(getRecord) == "function"){
			getRecord();
		}
	})
	var bill_statue = 0;
	
		//获取建议书状态
        $.ajax({  
              url : "/system/project/proposalStatue?proj_source_id=<%=request.getParameter("ParentPKValue") %>",  
              dataType : "json",  
                          type : "GET",  
                          async: false, 
                          success : function(data) {
                        	  	if(data!=""){
                        	  		bill_statue = data[0].BILL_STATUS;
                        	  	}
			            		if(bill_statue!="0"&&bill_statue!="7"){
			            			//置灰提交按钮
			            			$("#tj").attr("disabled", "disabled");
			            		}
			            		//$("#OPPORTUNITY_NAME").val(data[0].BILL_STATUS);
                          }
        });
	function checkWbs(){
		var foo= true;
		$.ajax({  
               url : "/system/project/progress_plan?proj_source_id=<%=request.getParameter("ParentPKValue") %>",  
               dataType : "json",  
                           type : "GET",  
                           async: false, 
                           success : function(data) {
                           	if(data.length==0){
                           		foo =false;
                           	}
                           }
        });
		return foo;
	}
	var proposal = "";
	function checkAmount(){
		var foo= true;
		//获取子frame 预算总额度 
		var budget_amount = $("#budget").contents().find("#cost_c").text();
		//获取预算主单单据状态
        $.ajax({  
              url : "/system/project/proposalStatue?proj_source_id="+ParentPKValue,  
              dataType : "json",  
                          type : "GET",  
                          async: false, 
                          success : function(data) {
	                      	if(data.length>0){
				            	project_cost = data[0].PROJECT_COST;
				            	proposal = "written";
			            	}else{
			            		//建议书没有编写 提示用户
			            		proposal= "not_written";
			            		project_cost=0;
			            	}
	                      	if(budget_amount!=project_cost){
	                      		foo =false;
	                      	}
                          }
       	 	});
		return foo;
	}
	function audit(t){
		
		//判断预算额度是否和建设额度相等  不相等提示 无法提交
		if(!checkAmount()){
			//判断建议书是否填写 
			if(proposal=="not_written"){
				//提示未填写建议书
				oTable.showModal('提示', "请填写项目概况信息! ");
				return false;
			}
			//提示填写wbs和进度计划
			oTable.showModal('提示', "编织预算总金额与立项建设成本不相等,请修改预算金额");
			return false;
		}
		
		//提交前判断 预算编制额度是否相等  wbs 是否填写
		if(!checkWbs()){
			//提示填写wbs和进度计划
			oTable.showModal('提示', "提交前请填写 项目进度阶段划分和wbs");
			return false;
		}
		
		var type =1;
		var audit_column = 'BILL_STATUS';
		var tenant_code = '';
		dataSourceCode = 'PROJ_PROPOSAL';
		if(audit_column.length == 0){
			oTable.showModal('提示', "没有设置审批字段");
		}
		if(bill_statue!="0"&&bill_statue!="7"){
			alert('已提交,无需重复提交');
			return ;
		}
		var json = "{";
		$("#1 iframe:first-child").contents().find("#insPage").find("[id]").each(function() {
		  json += "\"" + $(this).attr("id") + "\":" + "\"" + $(this).val() + "\",";
		});
		
		var producIdList = "";
		var productList = document.getElementById("PROJ_PROPOSAL_NL").contentWindow.jsonValue();
		for(var i=0;i<productList.length;i++){
			producIdList += productList[i]["PRODUCT_ID"]+",";
		}
		json += "\"productList\":\""+producIdList.substring(0,producIdList.length-1)+"\"";
		
		json += "}";
		
		//if($("#"+audit_column).val() != 0 && $("#"+audit_column).val() != 3){
			//oTable.showModal('提示', "只能提交  已保存、已退回单据");
			//return;
		//}
		var apprType="&audit_type="+type+"&audit_column="+audit_column+"&tenant_code="+tenant_code;
		var message = transToServer(findBusUrlByPublicParam(t,apprType,dataSourceCode),json);
		//var message = "请求成功！";
		oTable.showModal('提示', message);
		if(message=="提交成功，请等待审批!"){
			//按钮置灰
			$('#tj').attr("disabled","disabled");
			setTimeout( function(){
				location.reload();
			},1500);	
		}
	}
	function refreshFrame(){
   		document.getElementById('wbs').contentWindow.location.reload(true);
	}
	function refreshFrameByBudget(){
   		document.getElementById('budget').contentWindow.location.reload(true);
	}
	
	
	function pubVisitHis(t){
		var json = "{";
		$("#1 iframe:first-child").contents().find("#insPage").find("[id]").each(function() {
		  json += "\"" + $(this).attr("id") + "\":" + "\"" + $(this).val() + "\",";
		});
		json = json.substring(0,json.length-1);
		json += "}";
		var jsonObj = JSON.parse(json);
		var id = jsonObj["ID"];
		var NEED_PROCESS_CODE="system@PROJ_PROPOSAL@";
		var visit_url=workflow.replace("#BUS_ID#",id).replace("#ts#",new Date().getTime()).replace("#NEED_PROCESS_CODE#",NEED_PROCESS_CODE);
		show(visit_url);
	}
	
	function show(viewUrl){
		$('#ViewModal999_report').attr("src",viewUrl);
		$('#ViewModal999').modal('show');
	}
	
	
	$(function(){
		
		$(document.body).append(
				'<!--查看数据源展示效果专用模态框-->'+
				'<div class="modal fade" id="ViewModal999" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">'+
					'<div class="modal-dialog" style="width: 90%;">'+
						'<div class="modal-content">'+
							'<div class="modal-header">'+
								'<button type="button" class="close" data-dismiss="modal" aria-hidden="true">'+
									'×'+
								'</button>'+
								'<h4 class="modal-title" id="myModalLabel">审批进程</h4>'+
							'</div>'+
							'<div class="modal-body"><iframe id="ViewModal999_report" name="report" src=""  width="100%" height="518" frameborder="0"></iframe></div>'+
							'<div class="modal-footer">'+
								'<button type="button" class="btn btn-inverse" data-dismiss="modal">'+
									'关闭'+
								'</button>'+
							'</div>'+
						'</div>'+
					'</div>'+
				'</div>'
		);
		$('#ViewModal').on('hide.bs.modal', function() {
			$(this).removeData('modal');
		});
	});
        
    function preview_all(t){
		//oTable.showModal('建议书预览', '<iframe src="maintainPage_prew.jsp?pageCode=PROJ_PROPOSAL_NL&menuCode=0&pageName=项目基本信息&ParentPKField=PROJ_SOURCE_ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>&state=1&token=<%=request.getParameter("token") %>" scrolling="no" frameborder="0" width="1200px" height="1000"></iframe>');
		//$('#modaltitle').attr("style","width: 1200px");
		$('#modalTitle').html("建议书预览");
		//$('#myModalX').html('<iframe src="project/projectOrganization_prew.jsp?pageCode=PROJ_ORGANIZATION&menuCode=0&pageName=组织架构&ParentPKField=PROJ_SOURCE_ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>&state=1"  scrolling="no" frameborder="0" width="99%" height="100%"></iframe><iframe src="project/budget_prew.jsp?pageCode=PROJ_BUDGET&menuCode=0&pageName=项目预算&ParentPKField=PROJ_SOURCE_ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>&state=1" scrolling="no" frameborder="0" width="99%" height="100%"></iframe><iframe src="project/progressPlan_prew.jsp?pageCode=PROJ_PROGRESS_PHASE&menuCode=0&pageName=进度计划&ParentPKField=PROJ_SOURCE_ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>&state=1" scrolling="no" frameborder="0" width="99%" height="180%"></iframe><iframe id="wbs" src="project/wbs_prew.jsp?pageCode=PROJ_WBSINFO&menuCode=0&pageName=wbs&ParentPKField=PROJ_SOURCE_ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>&state=1" scrolling="no" frameborder="0" width="99%" height="150%"></iframe><iframe  src="project/riskTracking_prew.jsp?pageCode=PROJ_RISK_MANAGEMENT&menuCode=0&pageName=项目风险问题管控&ParentPKField=PROJ_SOURCE_ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>&state=1" scrolling="no" frameborder="0" width="99%" height="100%"></iframe><iframe src="otherPlans_prew.jsp?pageCode=PROJ_OTHERPLAN&menuCode=0&pageName=客户建设项目其他计划&ParentPKField=PROJ_SOURCE_ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>&state=1&ts=new Date().getTime()" scrolling="no" frameborder="0" width="99%" height="700px"></iframe>');
		$('#myModalX').html('<iframe src="maintainPage_prew.jsp?isHide=<%=request.getParameter("isHide")%>&pageCode=PROJ_PROPOSAL_GK&menuCode=0&pageName=项目基本信息&ParentPKField=PROJ_SOURCE_ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>&state=1&PROJECT_COST=<%=PROJECT_COST%>&SET_BUILD_INVESTMENT_COST_T=<%=SET_BUILD_INVESTMENT_COST_T %>&SET_BUILD_INVESTMENT_COST_F=<%=SET_BUILD_INVESTMENT_COST_F %>&BUILD_INVESTMENT_COST_ALL=<%=BUILD_INVESTMENT_COST_ALL%>&BUILD_INVESTMENT_COST_F=<%=BUILD_INVESTMENT_COST_F%>&BUILD_INVESTMENT_COST_T=<%=BUILD_INVESTMENT_COST_T%>&token=<%=request.getParameter("token") %>" scrolling="no" frameborder="0" width="99%" style="min-width:1005px; min-height:1550px;"></iframe><iframe src="project/projectOrganization_prew.jsp?isHide=<%=request.getParameter("isHide")%>&pageCode=PROJ_ORGANIZATION&menuCode=0&pageName=组织架构&ParentPKField=PROJ_SOURCE_ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>&state=1"  scrolling="no" frameborder="0" width="99%" style="min-width:1005px; min-height:600px;"></iframe><iframe src="project/budget_prew.jsp?isHide=<%=request.getParameter("isHide")%>&pageCode=PROJ_BUDGET&menuCode=0&pageName=项目预算&ParentPKField=PROJ_SOURCE_ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>&state=1" scrolling="no" frameborder="0" width="99%" style="min-width:1005px;min-height:600px;"></iframe><iframe src="project/progressPlan_prew.jsp?isHide=<%=request.getParameter("isHide")%>&pageCode=PROJ_PROGRESS_PHASE&menuCode=0&pageName=进度计划&ParentPKField=PROJ_SOURCE_ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>&state=1" scrolling="no" frameborder="0" width="99%" height="400px" style="min-width:1005px; min-height:540px;"></iframe><iframe id= "wbs" src="project/wbs_prew.jsp?isHide=<%=request.getParameter("isHide")%>&pageCode=PROJ_WBSINFO&menuCode=0&pageName=wbs&ParentPKField=PROJ_SOURCE_ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>&state=1" scrolling="no" frameborder="0" width="99%" style="min-width:1005px; min-height:540px;"></iframe><iframe src="project/riskTracking_prew.jsp?isHide=<%=request.getParameter("isHide")%>&pageCode=PROJ_RISK_MANAGEMENT&menuCode=0&pageName=项目风险问题管控&ParentPKField=PROJ_SOURCE_ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>&state=1" scrolling="no" frameborder="0" width="99%" style="min-width:1005px; min-height:605px;"></iframe><iframe src="otherPlans_prew.jsp?isHide=<%=request.getParameter("isHide")%>&pageCode=PROJ_OTHERPLAN&menuCode=0&pageName=客户建设项目其他计划&ParentPKField=PROJ_SOURCE_ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>&state=1" scrolling="no" frameborder="0" width="99%" style="min-width:1005px; min-height:540px;"></iframe>');
		
		$('#modalX').modal('show');
		
	}
        
</script>

</html>