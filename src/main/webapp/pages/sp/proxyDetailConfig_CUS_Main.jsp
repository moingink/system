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
					<li>
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
						<iframe id="cumaIf" name="pc" src='../maintainPage_cuma_message.jsp?pageCode=CUMA_ACCOUNT_UPDATE_MESSAGE&pageName=客户信息修改&ParentPKField=ID&ParentPKValue=<%=request.getParameter("ParentPKValue")%>&car_id=<%=request.getParameter("car_id")%>&state=1' scrolling="no" frameborder="0"  width="99%" height="1000"></iframe>
					</div>
					<div class="tab-pane fade" id='util'>
						<iframe id="utilIf" name="pc" src='childTableModifyByProxy_s_m.jsp?pageCode=CAR_NETWORKING_INFO&pageName=排产及车联网标配信息&ParentPKField=CUS_ID&ParentPKValue=<%=request.getParameter("ParentPKValue")%>&car_id=<%=request.getParameter("car_id")%>' scrolling="no" frameborder="0"  width="99%" height="500"></iframe>
					</div>
					<div class="tab-pane fade" id='select'>
						<iframe id="selectIf" src='childTableModifyByProxy_s1_m.jsp?pageCode=NET_PROJECT_INFO&pageName=车联网项目相关信息&ParentPKField=CUS_ID&ParentPKValue=<%=request.getParameter("ParentPKValue")%>&car_id=<%=request.getParameter("car_id")%>' scrolling="no" frameborder="0"  width="99%" height="500"></iframe>
					</div>
					<div class="tab-pane fade" id='list'>
						<iframe id="listIf" src='childTableModifyByProxy_s2_m.jsp?pageCode=CUMA_CAR_MODEL_INFO&pageName=客户车型服务内容信息&ParentPKField=CUS_ID&ParentPKValue=<%=request.getParameter("ParentPKValue")%>&car_id=<%=request.getParameter("car_id")%>&name=<%=request.getParameter("name") %>&pro=<%=request.getParameter("pro") %>' scrolling="no" frameborder="0"  width="99%" height="1000"></iframe>
					</div>
					<div class="tab-pane fade" id='maint'>
						<iframe id="maintIf" src='../maintainPage_cus_m.jsp?pageCode=CUMA_PRO_PLANS&pageName=方案及计划&ParentPKField=CUS_ID&ParentPKValue=<%=request.getParameter("ParentPKValue")%>&state=1' scrolling="no" frameborder="0"  width="99%" height="1500"></iframe>
					</div>
					<div class="tab-pane fade" id='update'>
						<iframe id="updateIf" src='childTableModifyByProxy_s3_m.jsp?pageCode=CUMA_DECIDSION_RERATION&pageName=决策关系&ParentPKField=CUS_ID&ParentPKValue=<%=request.getParameter("ParentPKValue")%>&car_id=<%=request.getParameter("car_id")%>' scrolling="no" frameborder="0"  width="99%" height="500"></iframe>
					</div>
					
					
					

				</div>
			</div>
			
		</form>

	</body>
	<jsp:include page='<%=request.getContextPath()%>/include/public.jsp'></jsp:include>
	<script type="text/javascript">
	function setIframeHeight(id){
		setTimeout(function(){
			var height = $(id).contents().find("body").height();
			if(height<200){
				height = 300;
			}
			$(id).height(height);
			setParntHeigth($(document.body).height());//高度设置
		},200);
	}
	
	
	
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
		 
		 
//提交		 
function findTenantCodeA(){
	isorno="";
    var id="<%=request.getParameter("ParentPKValue")%>";
    var url="/system/base?cmd=ajax_message&id="+id;
    
    var json=<%=request.getParameter("json")%>;
    
    //alert(url);
    var isno="";
    $.ajax({
			type: "POST",
			url: url,
		    //data: {CUS_ID:[id]}, 
			dataType: "json",
			async: false,
			cache: false,
			contentType: false,
			processData: false,
			success: function(data) {				
			//alert(selected["ID"]);
			//alert(JSON.stringify(data));
			if(json["BILL_STATUS"]=="0"){
			if(JSON.stringify(data)=="[[],[],[],[],[]]"){
				alert("此信息未携带一户一案信息！");
				isno="";
			}else{
				isno="EXA_APP_JUDGES";
			}				
            } 
            else if(json["BILL_STATUS"]=="3"){
                if(JSON.stringify(data)=="[[],[],[],[],[]]"){
				alert("此信息未携带一户一案信息！");
				isno="";
			    }else{
				isno="EXA_APP_JUDGE";
			    }	
            }  
			else if(JSON.stringify(data)!="[[],[],[],[],[]]"){
				isno="EXA_APP_JUDGES";
			}
			else{
				isno="";
			}
			}, error: function(data) {
			   //alert("");	
			}
			});
	 isorno=is_or_no(isno);		
	 return isno;
}

function pubRestAudit(t){
    
	var tenant_code =findTenantCodeA();
	//alert("判断编码:"+tenant_code);
	
	if(tenant_code==""){
		
	if(tenant_code==undefined){
		tenant_code="";
	}
	
	pubRestAuditByTenanted(t,tenant_code);
	
	}
	else{
		tenant_code=is_or_no(isorno);
		//alert("一起："+tenant_code);	
		if(tenant_code==undefined){
		tenant_code="";
	    }
	    pubRestAuditByTenanted(t,tenant_code);
	
	
		
	}
}
function pubRestAuditByTenanted(t,tenant_code){
    var dataname="<%=request.getParameter("dataname")%>";
    var message_all=<%=request.getParameter("json")%>;
	//alert(message_all["BILL_STATUS"]);
	var type =1;
	var audit_column ="BILL_STATUS";
    var buttonToken = "audit";
	if(audit_column.length==0){
		oTable.showModal('提示', "没有设置审批字段");
	}
	var billStatus = message_all["BILL_STATUS"];
	
	if(billStatus==undefined||billStatus==''||billStatus.length==0){
		oTable.showModal('提示', "审批状态为空，不能提交！");
		return;
	}
	
	if(isorno==""){
	if(billStatus != 0 && billStatus != 7){
		oTable.showModal('提示', "只能提交  已保存、已退回单据");
		return;
	}
	var apprType="&audit_type="+type+"&audit_column="+audit_column+"&tenant_code="+tenant_code;
    
    //alert(JSON.stringify(message_all));
   
	var message = transToServer(findBusUrlByButtonTonken(buttonToken,apprType,dataname),JSON.stringify(message_all));

	oTable.showModal('提示', message);
	queryTable(t);
	}
    if(isorno=="EXA_APP_JUDGE"){
	
		var apprType="&audit_type="+type+"&audit_column=BILL_STATUSES&tenant_code="+tenant_code;
		var message = transToServer(findBusUrlByButtonTonken(buttonToken,apprType,dataname),JSON.stringify(message_all));
		oTable.showModal('提示', message);
		queryTable(t);
	}
	else if(isorno=="EXA_APP_JUDGES"){
		var apprType="&audit_type="+type+"&audit_column="+audit_column+"&tenant_code="+tenant_code;
		var message = transToServer(findBusUrlByButtonTonken(buttonToken,apprType,dataname),JSON.stringify(message_all));
		oTable.showModal('提示', message);
		queryTable(t);
	}
	
	
	
}
//提交结束
		 
	function is_or_no(code) {	
       return code;
    }	 
		 
  	</script>
</html>