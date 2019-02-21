<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
	<head>
		<meta charset="utf-8">
		<title>单表模板</title>
	</head>
	<body>
	
	
	<form class="form-horizontal">
		<div class="panel panel-primary">
				
				<div class="panel-body" id="bulidTable">
					<div class="panel panel-primary">
						<div class="panel-heading" id ='pageName'>
							单表模板
						</div>
						
						<div class="panel-body">
							<div class="panel panel-default">
								<div class="panel-heading">
									信息
								</div>
								<!-- 维护页面-->
								<div class="panel-body" id="bus_message_dbclick">			
								</div>
							</div>
							
							<div id="toolbar">
								<!-- 按钮页面 -->
								<button type="button" class="btn btn-info" onclick="window.history.back(-1);">
										<span class="glyphicon glyphicon-ban-circle" aria-hidden="true"></span>返回
								</button>
							</div>
						</div>
					</div>
				</div>
			</div>		
	</form>
		<!-- 缓存参数 -->
		<!-- 是否为修改页面 -->
		</body>
	<jsp:include page="../include/public.jsp"></jsp:include>
	<script type="text/javascript">
	  $(function() {
			    var mainPageParam =pageParamFormat(ParentId +" ="+ParentPKValue);
			    mainPageParam=mainPageParam+"&showType=text";
				bulidMaintainPage($("#bus_message_dbclick"),dataSourceCode,mainPageParam);
				//bulidMaintainPage($("#insPage"),_dataSourceCode,'');
				//bulidButton($("#button_div"),_dataSourceCode,'');
				//bulidButton($("#button_child_div"),_dataSourceCode,'');
				//bulidButton($("#button_child_div_001"),_dataSourceCode,'');
				//bulidListPage($("#table"),_dataSourceCode,_query_param);
				//setReadonlyByDiv($("#superInsertPage input[type='text']"));
				/*$("#superInsertPage").find("[id]").each(function(){
						$(this).attr("disabled",true);
					});*/
				
	  });
		
	//重写back方法
	function back(t){
		tog(t);
		//排除主表主键
		$inspage.find('[id]').not('#'+ParentPKField).val("");
		$("#ins_or_up_buttontoken").val("");
	}
	</script>
</html>