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
				
				<div class="panel-body" id="bulidTable">
					
					<div class="panel panel-primary">
						<div class="panel-heading" id ='pageName'>
						
							单表模板
						</div>
						<div id="bill_date_and_status"></div>
						
						<div class="panel-body" id="queryParam" style="display: none;"></div>
						
						<div class="input-box-toggle" onclick="moreToggle()">
							<span class="caret"></span>更多搜索条件
						</div>
							<!-- 列表页面 -->
						<div id='button_div' class="button-menu"> </div>
						<table id="table" data-row-style="rowStyle"></table>

					
					</div>
				</div>
			
				<div class="panel-body" id="bulidPage" style="display: none">
					<div class="panel panel-primary">
						<div id="tog_titleName" class="panel-heading">
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
								<button type="button" class="btn btn-default" onclick="sendCode(this)">
									<span class="这里是啥" aria-hidden="true"></span>发送数据
								</button>
								
							</div>
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
	<jsp:include page="./buttonjs.jsp"></jsp:include>
	<script type="text/javascript">
		$(function() {
				bulidPage(true,true,true,true);
		});
		
		
		function sendCode(){
		<!-- 商机编号ID -->
		String s_id=request.getParameter("id");
		<!-- 商机编号 -->
		String 商机编号=request.getParameter("商机编号");
		<!-- 商机名称 -->
	    String 商机名称=request.getParameter("商机名称");
	    <!-- 项目名称 -->
	    String 项目名称=request.getParameter("项目名称");
	    <!-- 项目经理-->
	 	String 项目经理=request.getParameter("项目经理");
	 	<!-- 立项日期 -->
	 	String 立项日期=request.getParameter("立项日期")";
	 	<!-- 建设总资本 -->
	 	String 建设总资本=request.getParameter("建设总资本");
	 	<!-- 建设投资 -->
	 	String 建设投资=request.getParameter("建设投资");
	 	<!-- 建设成本 -->
	 	String 建设成本=request.getParameter("建设成本");
	 	<!-- token值 -->
	 	String tokenString=request.getParameter("token");
	 	
	 	 
	 	 $.ajax({
			type: "POST",
			url:"我不知道传哪里,		
			dataType: "json",
			async: false,
			cache: false,
			contentType: false,
			processData: false,
			success: function(data) {	
			
			$(function(){			
			alert("发送成功");
			});
			}, error: function(data) {
				var da=JSON.stringify(data);
				alert(da);
			    alert("保存失败");
			}
			}); 
		
	 	};
	</script>
</html>