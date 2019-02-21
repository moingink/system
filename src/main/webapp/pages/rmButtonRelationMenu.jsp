<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
	<head>
		<meta charset="utf-8">
		<title>菜单按钮关联管理</title>
	</head>
	<body>
	
	
	<form class="form-horizontal">
		<div class="panel panel-primary">
				
				<div class="panel-body" id="bulidTable">
					<div class="panel panel-primary">
						<div class="panel-heading" id ='pageName'>
							菜单按钮关联管理
						</div>
						
						<div id="bill_date_and_status"></div>
								
						<!-- 查询页面-->
						<div class="panel-body" id="queryParam" style="display: none;">	</div>
						<div class="input-box-toggle" onclick="moreToggle()">
							<span class="caret"></span>更多搜索条件
						</div>
							
								
						<div id='button_child_div_001' > </div>
						<!-- 列表页面 -->
						<table id="table"></table>
					</div>

				</div>

				<div class="panel-body" id="bulidPage" style="display: none">
					<div class="panel panel-primary">
						<div class="panel-heading">
							新增
						</div>
						<div class="panel-body">
							
							<div>
								<button type="button" class="btn btn-success" onclick="save(this)">
									<span class="glyphicon glyphicon-saved" aria-hidden="true"></span>保存
								</button>
								<button type="button" class="btn btn-inverse" onclick="back(this)">
									<span class="glyphicon glyphicon-ban-circle" aria-hidden="true"></span>返回
								</button>
							</div>
							<!-- 维护页面 -->
							<h5>菜单信息：</h5>
							<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
							<div class="panel-body" id="insPage">
								<!--主表主键  用于 新增、修改-->
								<input type='hidden' name="ParentPK">
							</div>
							<h5>按钮基本信息：</h5>
							<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
							<div class="panel-body" id="insPageBut"></div>
						
						</div>
					</div>
				</div>
				<input type="hidden" id="isNewStyle" value="1" />
			</div>		
	</form>
		<!-- 缓存参数 -->
		<!-- 是否为修改页面 -->
		</body>
	<jsp:include page="../include/public.jsp"></jsp:include>
	<jsp:include page="./buttonjs.jsp"></jsp:include>
	<script type="text/javascript">
	  $(function() {
	 		var _dataSourceCode= 'RM_BUTTON_RELATION_MENU';
			//按钮表
			var dataSourceCode = 'RM_BUTTON';
			//按钮表在关联表中的外键ID字段
			var ParentPKField='BUTTON_ID';
		    var mainPageParam =pageParamFormat("");
		    //mainPageParam=mainPageParam+"&showType=text";
			bulidSelect($("#queryParam"),_dataSourceCode,mainPageParam);
			bulidMaintainPage($("#insPage"),_dataSourceCode,'');
			bulidMaintainPage($("#insPageBut"),dataSourceCode,'');
			//bulidButton($("#button_div"),_dataSourceCode,'');
			//bulidButton($("#button_child_div"),_dataSourceCode,'');
			bulidButton($("#button_child_div_001"),_dataSourceCode,'');
			bulidListPage($("#table"),_dataSourceCode,_query_param);
			
			//$("#SPAN_CSS").iconPicker().attr("readonly",true); 
			
			$("#queryParam").append('<input type="hidden" id="SEARCH-MENU_ID" value="">');
			
			$("#insPage")
			.append('<input type="hidden" id="MENU_ID" value="">')
			.append('<input type="hidden" id="BUTTON_ID" value="">')
			.append('<input type="hidden" id="UPDATE_AUTHORITY" value="">');
				//$("#insPage input[id='MENU_NAME']").attr("disabled",true); 
				//$("#insPage input[id='MENU_CODE']").attr("disabled",true); 
			
			if($("#ins_or_up_buttontoken").val()=='but_rel_menu_insert'){
				$("#bulidPage input[id='ID']").remove();
			}else if($("#ins_or_up_buttontoken").val()=='but_rel_menu_update'){
				$("#insPageBut input[id='ID']").remove();
			}
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