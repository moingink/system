<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<link rel="stylesheet" type="text/css" href="../../easyui/demo/demo.css">
<link rel="stylesheet" type="text/css" href="../../easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="../../easyui/themes/icon.css">
<script type="text/javascript" src="../../easyui/jquery.min.js"></script>
<script type="text/javascript" src="../../easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="../../easyui/locale/easyui-lang-zh_CN.js"></script>

        <link rel="stylesheet" href="../../vendor/bootstrap/css/bootstrap.min.css">
		<link rel="stylesheet" href="../../vendor/bootstrap-table/src/bootstrap-table.css">
		
		<script src="../../vendor/bootstrap/js/bootstrap.min.js"></script>
		<script src="../../vendor/bootstrap-table/src/bootstrap-table.js"></script>
		<script src="../js/bootTable.js"></script>
		<script src="../LoadIng/js/loading.js"></script> 

<script type="text/javascript">

 $(function(){
   $(document).ready(function(){
    $("#car_modes").attr("disabled","disabled");
    $("#l2").removeAttr("href");
    $("#l3").removeAttr("href");
    $("#l4").removeAttr("href");
    $("#l5").removeAttr("href");
    $("#l6").removeAttr("href");
    $("#l7").removeAttr("href");
    $("#l8").removeAttr("href");
   
   });
 
 });

  function car_messages_all(car,id){
  $("#l2").attr("href","#mno");
  $("#l3").attr("href","#service");
  $("#l4").attr("href","#namesys");
  $("#l5").attr("href","#shop");
  $("#l6").attr("href","#map");
  $("#l7").attr("href","#music");
  $("#l8").attr("href","#wifi");
  
  $("div[id='mno'] iframe").attr("src","../maintainPage_main.jsp?pageCode=CUMA_MNO_MESSAGE_TAIN&pageName=MNO基本信息&ParentPKField=CUS_ID&ParentPKValue=<%=request.getParameter("ParentPKValue")%>&state=2&carmessageid="+id+"");
  $("div[id='service'] iframe").attr("src","../query_car_message_c.jsp?pageCode=CUMA_SERVICE_CONTENT&pageName=服务内容信息&ParentPKField=CUS_ID&ParentPKValue=<%=request.getParameter("ParentPKValue")%>&token=<%=request.getParameter("token") %>&carmessageid="+id+"");
  $("div[id='namesys'] iframe").attr("src","../maintainPage_main.jsp?pageCode=CUMA_REAL_NAME_SYSTEM_TAIN&pageName=实名制系统&ParentPKField=CUS_ID&ParentPKValue=<%=request.getParameter("ParentPKValue")%>&state=2&carmessageid="+id+"");
  $("div[id='shop'] iframe").attr("src","../maintainPage_main.jsp?pageCode=CUMA_SHOPSERVICE_TAIN&pageName=在线商店&ParentPKField=CUS_ID&ParentPKValue=<%=request.getParameter("ParentPKValue")%>&state=2&carmessageid="+id+"");
  $("div[id='map'] iframe").attr("src","../maintainPage_main.jsp?pageCode=CUMA_MAPSERVICE_TAIN&pageName=地图导航&ParentPKField=CUS_ID&ParentPKValue=<%=request.getParameter("ParentPKValue")%>&state=2&carmessageid="+id+"");
  $("div[id='music'] iframe").attr("src","../maintainPage_main.jsp?pageCode=CUMA_MUSSERVICE_TAIN&pageName=在线音乐&ParentPKField=CUS_ID&ParentPKValue=<%=request.getParameter("ParentPKValue")%>&state=2&carmessageid="+id+"");
  $("div[id='wifi'] iframe").attr("src","../maintainPage_main.jsp?pageCode=CUMA_WIFI_TAIN&pageName=wifi服务&ParentPKField=CUS_ID&ParentPKValue=<%=request.getParameter("ParentPKValue")%>&state=2&carmessageid="+id+"");
 
  
  
  $("#car_modes").val(car);
  
  $("#l1").trigger("click");
  jz();
   
 }

</script>  

</head>
<body>	
	车型:<input id="car_modes" value=""/>
	<form>
		<div class="col-md-12">
			<ul id="myTab" class="nav nav-tabs">
				<!-- <li>
					<a class="btn btn-inverse" onclick="window.history.back(-1);">
						<span class="glyphicon glyphicon-ban-circle" aria-hidden="true"></span>返回
					</a>
				</li> -->
				    <li class="active">
						<a id="l1" href="#car" data-toggle="tab">客户车型</a>
					</li>
					<li >
						<a id="l2" href="#mno" data-toggle="tab">MNO基本信息</a>
					</li>
					<li >
						<a id="l3" href="#service" data-toggle="tab">服务内容</a>
					</li>
					<li>
						<a id="l4" href="#namesys" data-toggle="tab">实名制系统</a>
					</li>
					<li >
						<a id="l5" href="#shop" data-toggle="tab">在线商店</a>
					</li>
					<li >
						<a id="l6" href="#map" data-toggle="tab">地图导航</a>
					</li>
					<li >
						<a id="l7" href="#music" data-toggle="tab">在线音乐</a>
					</li>
					<li >
						<a id="l8" href="#wifi" data-toggle="tab">wifi服务</a>
					</li>
			</ul>
		</div>
		</br>
		<div class="col-md-12">

			<div class="tab-content">
			        
			
			        <div class="tab-pane fade in active" id='car'>
						<iframe src='query_message_all2_1_q_c.jsp?pageCode=CUMA_CUS_CAR_MODE&pageName=客户车型&ParentPKField=ID&ParentPKValue=<%=request.getParameter("ParentPKValue")%>&cus_id=<%=request.getParameter("car_id") %>&name=<%=request.getParameter("name") %>&pro=<%=request.getParameter("pro") %>' scrolling="no" frameborder="0"  width="99%" height="1000"></iframe>
					</div>
					
					<div class="tab-pane fade in active" id='mno'>
						<iframe  scrolling="no" frameborder="0"  width="99%" height="1000"></iframe>
					</div>
					
					<div class="tab-pane fade in active" id='service'>
						<iframe  scrolling="no" frameborder="0"  width="99%" height="1000"></iframe>
					</div>
					<div class="tab-pane fade in active" id='namesys'>
						<iframe  scrolling="no" frameborder="0"  width="99%" height="1000"></iframe>
					</div>
					<div class="tab-pane fade in active" id='shop'>
						<iframe  scrolling="no" frameborder="0"  width="99%" height="1000"></iframe>
					</div>
					<div class="tab-pane fade in active" id='map'>
						<iframe  scrolling="no" frameborder="0"  width="99%" height="1000"></iframe>
					</div>
					<div class="tab-pane fade in active" id='music'>
						<iframe  scrolling="no" frameborder="0"  width="99%" height="1000"></iframe>
					</div>
					<div class="tab-pane fade in active" id='wifi'>
						<iframe  scrolling="no" frameborder="0"  width="99%" height="1000"></iframe>
					</div>
			</div>
		</div>

	</form>
	
</body>
</html>