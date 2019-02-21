<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<title>子表模板</title>
</head>
<body>

	<form class="form-horizontal">
		
				<div class="panel panel-primary">
					<div class="panel-heading" id='pageName'></div>
					<div class="panel-body">
						<div id="toolbar">							
							
						</div>
					</div>
				</div>
		
	</form>
	  <iframe name="query_message" src='query_message_all2_m.jsp?ParentPKValue=<%=request.getParameter("ParentPKValue")%>&car_id=<%=request.getParameter("car_id") %>&name=<%=request.getParameter("name") %>&pro=<%=request.getParameter("pro") %>' scrolling="no" frameborder="0"  width="99%" height="700px"></iframe>
	<!-- 主表主键 用于查询 -->
	<input type="hidden" name="ParentPK_Query" />

</body>
<jsp:include page="../../include/public.jsp"></jsp:include>

<script type="text/javascript">
	
	$(function() {
		bulidPage(true,true,true,true);
	});
	
	//主表主键在子表字段名
	var ParentPKField = '<%=request.getParameter("ParentPKField")%>';
	//主表主键值
	var ParentPKValue = '<%=request.getParameter("ParentPKValue")%>';
	var meta_id ='<%=request.getParameter("META_ID")%>';
	$('input[name="ParentPK"]').attr('id',ParentPKField);
	//$('input[name="ParentPK_Query"]').attr('id','SEARCH-'+ParentPKField);
	$('input[name^="ParentPK"]').val(ParentPKValue);

	//重写back方法
	function back(t){
		tog(t);
		//排除主表主键
		$inspage.find('[id]').not('#'+ParentPKField).val("");
		$("#ins_or_up_buttontoken").val("");
	}
	
	var columns =['FIELD_CODE','FIELD_NAME','FIELD_TYPE','NULL_FLAG','KEY_FLAG','SORT','INPUT_TYPE','INPUT_FORMART','INPUT_HTML'];
	
	function ref_write_json(rejsonArray){
		
		if(rejsonArray.length==1){
			var jsonObject =rejsonArray[0];
			var prefix = "#";
			if(globalRef_pageType == 'SELECT'){
				prefix += "SEARCH-";
				}
			for(var i in columns){
				var mark =prefix+columns[i];
				$(mark).val(jsonObject[columns[i]]);
			}
			$("#META_DETAIL_ID").val(jsonObject["ID"]);
		}
	}
	
	function ref_query_param(u){
		return "&SEARCH-METADATA_ID="+'<%=request.getParameter("META_ID")%>';
		
	}
	
	
	
	 //父调用子方法
		 function callChild() {
              query_message.window.update_messages(); 
         }
       
	
	
</script>

</html>