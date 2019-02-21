<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<title>客户验收历史查看</title>
</head>

<body>

	<form>
		<div class="col-md-12">
			<ul id="myTab" class="nav nav-tabs">
				
				<li class="active"><a id="show" href="#cyjw" data-toggle="tab">上线</a></li>
				<li><a id="show" href="#3" data-toggle="tab">验收 </a></li>
			</ul>
		</div>
		</br>
		<div class="col-md-12">

			<div class="tab-content">

				<div class="tab-pane fade in active" id='cyjw'>
					<iframe src=""  id="5"  scrolling="no" frameborder="0" width="99%" height="1000"></iframe>
				</div>
				<div class="tab-pane fade" id='3'>
					<iframe src=""  id="6" scrolling="no" frameborder="0" width="99%" height="1000"></iframe>
				</div>
			</div>
		</div>

	</form>

</body>
<jsp:include page="../include/public.jsp"></jsp:include>

<script type="text/javascript">
	hide(isHide);
	$('#5').attr("src",'client_detail_history.jsp?isHide=<%=request.getParameter("isHide")%>&pageCode=CLIENT_ACCEPTANCE_HISTORY&pageName=客户侧上线&state=1&ParentPKField=ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>&roleId='+roleId);
	$('#6').attr("src",'client_detail_history.jsp?isHide=<%=request.getParameter("isHide")%>&pageCode=CUSTOMER_SIDE_HISTORY&pageName=客户侧验收&ParentPKField=ID&ParentPKValue=<%=request.getParameter("ParentPKValue") %>&roleId='+roleId);
	
	//使用维护页面模板的每次激活页签时向服务器请求最新数据更新此页签
	$('a[data-toggle="tab"]').on('show.bs.tab', function (e) {
		console.log(e.target.hash);
		var getRecord = $(e.target.hash+" iframe:first-child")[0].contentWindow.getRecord;
		if(typeof(getRecord) == "function"){
			getRecord();
		}
	})
</script>

</html>