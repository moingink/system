<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
	<meta name="viewport" content="width=device-width, minimum-scale=1, maximum-scale=1">
	<head>
		<meta charset="utf-8">
		<title>单表模板</title>
	</head>
	<body>
		<form class="form-horizontal">
			<div class="panel-body" id="superInsertPage"></div>
			<table id="POSTAGE_DETAIL_DETAIL_TABLE" data-row-style="rowStyle"></table>
		</form>
	</body>
	<jsp:include page="../include/public.jsp"></jsp:include>
	<script type="text/javascript">
		$(function() {
			var historyId = '<%=request.getParameter("historyId")%>';
			var id = '';
			
			//加载资费主表
			bulidMaintainPage($("#superInsertPage"),"PRE_ASSESSMENT_HISTORY","","mod");
			var param = "&dataSourceCode=PRE_ASSESSMENT_HISTORY&SEARCH-HISTORY_ID="+historyId;
			var record = querySingleRecord(param);
			if(!jQuery.isEmptyObject(record)){
				id = record['ID'];
				$("#superInsertPage").find("[id]").each(function() {
					if($(this).attr("id") == 'PRODUCT_NAME'){
						$(this).append("<option value='0' selected='selected'>"+record[$(this).attr("id")]+"</option>");
	  					$(this).attr("title",record[$(this).attr("id")]);
					}else if($(this).attr("id") == 'ENCLOSURE') {
						$(this).val(record[$(this).attr("id")]);
						if(undefined!=record[$(this).attr("id")]){
							getFileTypeValTo($("#superInsertPage"));
						}
					}
					else{
						$(this).val(record[$(this).attr("id")]);
					}
				});
			}
			
			//资费明细表格初始化
	    	bulidListPage($('#POSTAGE_DETAIL_DETAIL_TABLE'), 'POSTAGE_DETAIL', pageParamFormat("PRE_ID = '"+id+"'"));
			notEdit();
			//解除files_a_don display: none;
			$("#superInsertPage").children().find('[id=files_a_don]').css("display","block");
		});
		
		//重写详情函数
		function show_dbclick(selected,title){
			return;
		}
	</script>
</html>