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
		//是否需要加载后台数据
		var state = '<%=request.getParameter("state")%>';
		var keyWord = '<%=request.getParameter("keyWord")%>';//入口判断
		var modifyId = '<%=request.getParameter("modifyId")%>';//变更执行申请ID
		var modifyFieldName = '<%=request.getParameter("modifyFieldName")%>';//变更执行申请字段名
		var pageCode = '<%=request.getParameter("pageCode")%>';//数据源名称
		//获取建议书主单单据状态
		var bill_statues="";
		$(function() {
			bulidPage(false,true,false,true);//获取按钮页面为true是为了加载数据源对应js
			//默认为新增
			$("#ins_or_up_buttontoken").val('add');
			$("#"+ParentPKField).val(ParentPKValue);
			//第一次进入默认激活的页面需要手动请求下最新数据
			if(state == 1){
				getRecord();
			}
		});
		
		function getRecord(){
			var record = new Object();
			if(keyWord == 1){//iframe
				$("#bc").css("display","none");
				$.ajax({
				    		async: false,
				            type: "get",
				            url: context+"/project?cmd=find_selectTab",
				            data: {"modifyId":modifyId,"modifyFieldName":modifyFieldName},
				            dataType: "json",
				            success: function(data){
				            	record = data;
				            }
				        });
			}
			 $.ajax({  
		              url : "/system/project/proposalStatue?proj_source_id="+ParentPKValue,  
		              dataType : "json",  
		                          type : "GET",  
		                          async: false, 
		                          success : function(data) {
			                      	if(data.length > 0){
					            		bill_statues = data[0].BILL_STATUS;
					            		//bill_statues = "3";
					            	}
		                          }
		        });
			if(jQuery.isEmptyObject(record)){
				var param = "&dataSourceCode="+dataSourceCode+"&SEARCH-"+ParentPKField+"="+ParentPKValue;
				if(typeof(specifyParam) != "undefined"){
					param += specifyParam;
				}
				record = querySingleRecord(param);
			}
			if(!jQuery.isEmptyObject(record)){
				//已存在记录时为修改
				$("#ins_or_up_buttontoken").val('update');
				$("#tog_titleName").html("修改");
				$inspage.find("[id]").each(function() {			
	  				$(this).val(record[$(this).attr("id")]);
				});
				if(bill_statues !="" ){
					if(bill_statues!="7" && bill_statues !="0"){
						$("#bc").attr("disabled", true);
					}
				}
			}
			if(pageCode == "PROJ_PROPOSAL_GK"){
				$(".col-md-2").css("display","none");
				$(".col-md-10").css("display","none");
				$(".col-md-4").css("display","none");
				$(".col-md-8").css("display","none");
				$("#fileList_AFFIX").css("display","none");
				$("#CONTENT_OVERVIEW").parent().css("display","inline");
				$("#CONTENT_OVERVIEW").parent().prev().css("display","inline");
			}
			if(keyWord == 1){//iframe
	     	   $('.green').removeAttr('href');//提交href
	     	   $('#anchor_DETAILED_SCHEME').attr("disabled","true");;//选择文件按钮
		   }
		}
		
		//重写方法，保存后不清空输入框而是设为只读，保存按钮置灰
		function savaByQuery(t,_dataSourceCode,$div){
			var message ="";
			var buttonToken =$("#ins_or_up_buttontoken").val();
			if(typeof(specifyButtonToken) == "function"){
				buttonToken = specifyButtonToken(buttonToken);
			}
			message = transToServer(findBusUrlByButtonTonken(buttonToken,'',_dataSourceCode),getJson($div));
			oTable.showModal('modal', message);
			setReadonlyByDiv($("#insPage input[type='text']"));
			$("#bc").attr("disabled", true);
		}
		
		//json
		function jsonValue(){
			var json = "{";
			$("#insPage").find("[id]").each(function() {
				if ($(this).attr("id") == 'JSCONTENT') {
					$(this).val(b64Encode($(this).val()));
				}
				json += '\'' + $(this).attr("id") + '\':' + '\'' + $(this).val() + '\',';
			});
			json = json.substring(0, json.length - 1);
			json += "}";
			return json;
		}
	</script>
</html>