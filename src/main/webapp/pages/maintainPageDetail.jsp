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
	</head>
	<body id="body_div">

		<form class="form-horizontal">
			<div class="panel panel-primary">
		
				<div class="panel-body" id="bulidPage">
					<div class="panel panel-primary">
						<div class="panel-body">
							<div class="alert alert-info" id="message" style="display: none"></div>
							<div class="form-group">
								<div class="col-md-12">
									<div id="errors"></div>
								</div>
							</div>
							<!-- 维护页面 -->
							<div class="panel-body" id="insPage">
								
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
		var pageCode = '<%=request.getParameter("pageCode")%>';
		//是否需要加载后台数据
		var state = '<%=request.getParameter("state")%>';
		var projCode = '';
		var projName='';
		var cost ='';
		var uname = '<%=request.getParameter("userName")%>';
		var comname = '<%=request.getParameter("companyName")%>';
		$.ajax({  
              url : "/system/project/projectinfo?proj_source_id="+ParentPKValue,  
              dataType : "json",
                          type : "GET", 
                          async: false, 
                          success : function(data) {
                          	console.log(data);
                          	if(data.length>0){
	                          	projCode = data[0].OPPORTUNITY_CODE;
	                          	projName = data[0].PROJ_NAME;
	                          	cost = data[0].COST;
                          	}
                          }
            });
		$(function() {
			bulidPage(false,true,false,true);//获取按钮页面为true是为了加载数据源对应js
			//默认为新增
			$("#ins_or_up_buttontoken").val('add');
			$("#"+ParentPKField).val(ParentPKValue);
			//第一次进入默认激活的页面需要手动请求下最新数据
			if(state == 1){
				getRecord();
			}
			if(pageCode=="PROJ_REQUIREMENT"){
				//设置项目类型 和 需求编号到项目下达表中
				$("#DEPTNAME").val(comname);
				$("#CREATOR_NAME").val(uname);
				$("#DEPTNAME").attr("disabled", true);
				$("#CREATOR_NAME").attr("disabled", true);
			}
		});
		//var bill_statue=0;
		function getRecord(){
			var param = "&dataSourceCode="+dataSourceCode+"&SEARCH-"+ParentPKField+"="+ParentPKValue;
			if(typeof(specifyParam) != "undefined"){
				param += specifyParam;
			}
			var record = querySingleRecord(param);
			console.log(record);
			//设置回显字段
			//
			$("#OPPORTUNITY_CODE").val(projCode);
			//
			$("#PROJ_NAME").val(projName);
			$("#COST_ESTIMATE").val(cost);
			if(pageCode=="PROJ_RELEASED_NLXM1"){
				//设置项目类型 和 需求编号到项目下达表中
				$("#PROJ_TYPE").val('1');
				$("#BID_BUSS_NO").val(ParentPKValue);
			}
			
			if(!jQuery.isEmptyObject(record)){
				//已存在记录时为修改
				$("#ins_or_up_buttontoken").val('update');
				$("#tog_titleName").html("修改");
				$inspage.find("[id]").each(function() {			
	  				$(this).val(record[$(this).attr("id")]);
				});
				bill_statues = $("#BILL_STATUS").val();
				//bill_statues="3"
			}
			if(bill_statues !="" ){
				if(bill_statues!="7" && bill_statues !="0"){
					console.log('不可编辑')
					$("#bulidPage").find("[id]").each(function(){
							$(this).attr("disabled",true);
					});
					$("#bulidPage").find("[span]").each(function(){
							$(this).attr("onclick","");
					});
				}
			}
		}
		
		//重写方法，保存后不清空输入框而是设为只读，保存按钮置灰
		function savaByQuery(t,_dataSourceCode,$div){
			console.log($div);
			var message ="";
			var buttonToken =$("#ins_or_up_buttontoken").val();
			//if(typeof(specifyButtonToken) == "function"){
				//buttonToken = specifyButtonToken(buttonToken);
			//}
			message = transToServer(findBusUrlByButtonTonken(buttonToken,'',_dataSourceCode),getJson($div));
			oTable.showModal('modal', message);
			
			setReadonlyByDiv($("#insPage input[type='text']"));
			$("#ins_or_up_buttontoken").attr("disabled", true);
			if(pageCode=="PROJ_RELEASED_NLXM1"){
				if(message=="保存成功"||message=="修改成功"){
				//回写需求表数据 项目建议书名称 和项目建议书状态
					var name = $("#PROJ_NAME").val();
					var code = $("#PROJ_CODE").val();
					var namager = $("#MANAGER").val();
					$.ajax({  
		              url : "/system/project/updatereQuirement?bind_buss_no="+ParentPKValue+"&proj_name="+name+"&proj_code="+code+"&proj_manager="+namager,  
		              dataType : "json",
		                          type : "GET", 
		                          async: false, 
		                          success : function(data) {
		                          	console.log(data);
		                          	if(data.length0){
			                          	projCode = data[0].OPPORTUNITY_CODE;
			                          	projName = data[0].PROJ_NAME;
			                          	cost = data[0].COST;
		                          	}
		                          }
            		});
				}
			}
		}
		
	</script>
</html>