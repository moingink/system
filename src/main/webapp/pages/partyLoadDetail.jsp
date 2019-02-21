<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
	<head>
		<meta charset="utf-8">
		<title>父页面</title>
	</head>
	<body>

		<form class="form-horizontal">
			<div class="panel panel-primary">
				
				<div class="panel-body" id="bulidTable">
					<div class="panel panel-primary">
						<div class="panel-heading" id ='pageName'>
							用户管理
						</div>
						
							<div id="bill_date_and_status"></div>
							<!-- 查询条件页面 -->
							<div class="panel-body" id="queryParam" style="display: none;"></div>
							<div class="input-box-toggle" onclick="moreToggle()">
								<span class="caret"></span>更多搜索条件
							</div>
							
							<!-- 按钮页面 -->
							<div id='button_div'></div>
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
							<div class="panel-body" id="insPage">
							</div>
						
						</div>
					</div>
				</div>
				<input type="hidden" id="isNewStyle" value="1" />
			</div>
		</form>
		<!-- 是否为修改页面 -->
		
	</body>
	<jsp:include page="../include/public.jsp"></jsp:include>
	<jsp:include page="./buttonjs.jsp"></jsp:include>
	<script type="text/javascript">
		$(function() {
			bulidPage(true,true,true,true);
		});
		
	</script>
</html>