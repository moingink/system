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


</head>
	<body id="body_div">
		<form class="form-horizontal">
			<div class="panel panel-primary">
				<div class="panel-body" id="bulidPage">
					<div class="panel panel-primary">
						<div id="tog_titleName" class="panel-heading">
							修改
						</div>
						<div class="panel-body">
							<div class="alert alert-info" id="message" style="display: none"></div>
							<div>
								<button id="ins_or_up_buttontoken" type="button" class="btn btn-default" onclick="isAudit(this)">
									<span class="glyphicon glyphicon-saved" aria-hidden="true"></span>保存
								</button>
							</div>
							<div class="form-group">
								<div class="col-md-12">
									<div id="errors"></div>
								</div>
							</div>
							<!-- 维护页面 -->
							<div class="panel-body" id="insPage"></div>
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
		var ParentPKField = 'ID';
		//主表主键值
		var ParentPKValue = '<%=request.getParameter("id")%>';
		
		$(function() {
			dataSourceCode="EC_PURORDER_H_MAIN";
			bulidPage(false,true,false,true);//获取按钮页面为true是为了加载数据源对应js
			getRecord();
			loadChild();
			getFileTypeVal($("#insPage"));
			$("#bs_code").val(dataSourceCode);
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
				$("#tog_titleName").html("修改");
				$inspage.find("[id]").each(function() {			
	  				$(this).val(record[$(this).attr("id")]);
				});
			}
		}
		function loadChild(){
			
		
		    	var tableHtml = '<div class="col-md-12" style="margin-top:10px; margin-bottom:10px; padding:0; id="detailTable" ">'+
		    	'<iframe id="iframe4child" src="" style="width: 100%; border: 0;height: 600px;"></iframe></div>' ;
		    	$('#insPage').after(tableHtml);
		    	var pageCode = 'EC_PURORDER_H';// 子表数据源
		    	var pageName = '订单添加合同';// 列表显示名称
		    	var ParentPKField = 'EC_PURORDER_H_MAIN_ID';// 主表主键在子表中字段名
		
		    	var iframeUrl = context + "/pages/childTableModify_EC_PURORDER_H_MAIN.jsp?pageCode="
		    			+ pageCode + "&pageName=" + pageName + "&ParentPKField="
		    			+ ParentPKField + "&ParentPKValue=" + ParentPKValue  ;
		    	$("#iframe4child").attr("src",iframeUrl);
		}
		function save1(t){
			$inspage.data("bootstrapValidator").validate();
		    if(!$inspage.data('bootstrapValidator').isValid()){ 
		        return ; 
		    }
		    
		    var cache_dataSourceCode =$("#cache_dataSourceCode").val();
			var _dataSourceCode=dataSourceCode;
			if(cache_dataSourceCode!=null&&cache_dataSourceCode.length>0){
				_dataSourceCode=cache_dataSourceCode;
				
			}
			saveByDataSourceCode(t,_dataSourceCode);
		}
		
		//验证提交是否成功
		function isAudit(t){
			var flag = parent.setJsonData(getJson($("#insPage")));
			if(flag == '0'){
				save1(t);
			}else{
				oTable.showModal('提示', '保存失败');
			}
		}
		 
	</script>
</html>