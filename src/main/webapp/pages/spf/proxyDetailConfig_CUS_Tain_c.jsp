<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
	<head>
		<meta charset="utf-8">
		<title>页面代理明细</title>
		<link rel="stylesheet" href="../../vendor/bootstrap/css/bootstrap.min.css">
		<link rel="stylesheet" href="../../vendor/bootstrap-table/src/bootstrap-table.css">
		<script src="../../vendor/jquery/jquery.min.js"></script>
		<script src="../../vendor/bootstrap/js/bootstrap.min.js"></script>
		<script src="../../vendor/bootstrap-table/src/bootstrap-table.js"></script>
		<script src="../js/bootTable.js"></script>
		
	</head>

	<body>
	    <br/>
		<form>
		<br/>
			<div class="col-md-12">
				<ul id="myTab" class="nav nav-tabs">
				    <li class="active">
						<a href="#cuma" data-toggle="tab">客户信息</a>
					</li>
					<li >
						<a href="#util" data-toggle="tab">排产及车联网标配信息</a>
					</li>
					<li >
						<a href="#select" data-toggle="tab">车联网项目相关信息</a>
					</li>
					<li>
						<a href="#list" data-toggle="tab">客户车型服务内容信息</a>
					</li>
					<li >
						<a href="#maint" data-toggle="tab">方案及计划</a>
					</li>
					<li >
						<a href="#update" data-toggle="tab">决策关系</a>
					</li>
				</ul>
			</div>
		</br>
			<div class="col-md-12">

				<div class="tab-content">

				
					<div class="tab-pane fade in active" id='cuma'>
						<iframe name="pc" src='../maintainPage_cuma_message.jsp?pageCode=CUMA_ACCOUNT_TAIN&pageName=客户信息基本资料维护&ParentPKField=ID&ParentPKValue=<%=request.getParameter("ParentPKValue")%>&car_id=<%=request.getParameter("car_id")%>&state=1' scrolling="no" frameborder="0"  width="99%" height="1000"></iframe>
					</div>
					<div class="tab-pane fade " id='util'>
						<iframe name="pc" src='childTableModifyByProxy_s_q_c.jsp?pageCode=CAR_NETWORKING_INFO_TAIN&pageName=排产及车联网标配信息&ParentPKField=CUS_ID&ParentPKValue=<%=request.getParameter("ParentPKValue")%>' scrolling="no" frameborder="0"  width="99%" height="1000"></iframe>
					</div>
					<div class="tab-pane fade " id='select'>
						<iframe src='childTableModifyByProxy_s1_q_c.jsp?pageCode=NET_PROJECT_INFO_TAIN&pageName=车联网项目相关信息&ParentPKField=CUS_ID&ParentPKValue=<%=request.getParameter("ParentPKValue")%>' scrolling="no" frameborder="0"  width="99%" height="1000"></iframe>
					</div>
					<div class="tab-pane fade " id='list'>
						<iframe src='childTableModifyByProxy_s2_q_c.jsp?pageCode=CUMA_CAR_MODEL_INFO_TAIN&pageName=客户车型服务内容信息&ParentPKField=CUS_ID&ParentPKValue=<%=request.getParameter("ParentPKValue")%>&car_id=<%=request.getParameter("car_id")%>&name=<%=request.getParameter("name") %>&pro=<%=request.getParameter("pro") %>' scrolling="no" frameborder="0"  width="99%" height="1000"></iframe>
					</div>
					<div class="tab-pane fade " id='maint'>
						<iframe src='../maintainPage_cus_sp.jsp?pageCode=CUMA_PRO_PLANS_TAIN&pageName=方案及计划&ParentPKField=CUS_ID&ParentPKValue=<%=request.getParameter("ParentPKValue")%>&state=1' scrolling="no" frameborder="0"  width="99%" height="1200"></iframe>
					</div>
					<div class="tab-pane fade " id='update'>
						<iframe src='childTableModifyByProxy_s3_q_c.jsp?pageCode=CUMA_DECIDSION_RERATION_TAIN&pageName=决策关系&ParentPKField=CUS_ID&ParentPKValue=<%=request.getParameter("ParentPKValue")%>&car_id=<%=request.getParameter("car_id")%>' scrolling="no" frameborder="0"  width="99%" height="1000"></iframe>
					</div>
					
					
					

				</div>
			</div>
			
		</form>

	</body>
	<script type="text/javascript">
		var ParentPKValue = '<%=request.getParameter("ParentPKValue")%>';
		 
		 //父调用子方法
		 function callChild() {
              pc.window.remove_hidden(); 
         }
		 function setHeigth(height){
		 }
		 
		 $(function(){
		 	setParntHeigth(1000);
		 	//$("body").height(1500);
		 	//$(".tab-content").height(1500);
		 });
  	</script>
</html>