<%@page import="com.yonyou.util.theme.ThemePath"%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<%
	String ContextPath =request.getContextPath();
%>

<html lang="zh-cn">
	<head>
		<meta charset="utf-8">
		<title>历史版本</title>
	</head>
	<body id="body_div">

		<form class="form-horizontal">
			<div class="panel panel-primary">
		
				<div class="panel-body" id="bulidPage">
					<div class="panel panel-primary">
						<div id="tog_titleName" class="panel-heading">
						</div>
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
		//功能元数据编码 
		var pageCode = '<%=request.getParameter("pageCode")%>';
		var roleId = '<%=request.getParameter("roleId")%>';
		$(function() {
			bulidPage(false,true,false,true);//获取按钮页面为true是为了加载数据源对应js
			//默认为新增
			$("#"+ParentPKField).val(ParentPKValue);
			//第一次进入默认激活的页面需要手动请求下最新数据
			getRecord();
			$("#bulidPage").find("[id]").each(function(){
							$(this).attr("disabled",true);
					});
					$("#bulidPage").find("[span]").each(function(){
							$(this).attr("onclick","");
					});
					//置灰附件删除图标
					console.log('-提交后置灰删除附件-');
					console.log($('#insPage'));
					setTimeout(function(){
						$('#insPage').children().find('[id=files_a_del]').remove();
					},1000);
			
			hide(isHide);
		});
		
		function getRecord(){
			var param = "&dataSourceCode="+dataSourceCode+"&SEARCH-"+ParentPKField+"="+ParentPKValue;
			if(typeof(specifyParam) != "undefined"){
				param += specifyParam;
			}
			var record = querySingleRecord(param);
			if(!jQuery.isEmptyObject(record)){
				//已存在记录时为修改
				$("#ins_or_up_buttontoken").val('update');
				$("#tog_titleName").html("内部验收历史版本");
				$inspage.find("[id]").each(function() {			
	  				$(this).val(record[$(this).attr("id")]);
				});
			}
			if(roleId != "null" && roleId !="1000105000000000067"){
				//pmo禁止操作页面 全部置灰
				$('#bc').attr("disabled",true);
				$('#fh').attr("disabled",true);
				$("#insPage").find("[id]").each(function(){
					$(this).attr("disabled",true);
				});
			}
			hide(isHide);
			getFileTypeVal($("#insPage"));
			$("#bs_code").val(dataSourceCode);
		}
		
	</script>
</html>