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
  
  $("div[id='mno'] iframe").attr("src","../maintainPage_main.jsp?pageCode=CUMA_MNO_MESSAGE&pageName=MNO????????????&ParentPKField=CUS_ID&ParentPKValue=<%=request.getParameter("ParentPKValue")%>&state=2&carmessageid="+id+"");
  $("div[id='service'] iframe").attr("src","../query_car_message_m.jsp?pageCode=CUMA_SERVICE_CONTENT&pageName=??????????????????&ParentPKField=CUS_ID&ParentPKValue=<%=request.getParameter("ParentPKValue")%>&token=<%=request.getParameter("token") %>&carmessageid="+id+"");
  $("div[id='namesys'] iframe").attr("src","../maintainPage_main.jsp?pageCode=CUMA_REAL_NAME_SYSTEM&pageName=???????????????&ParentPKField=CUS_ID&ParentPKValue=<%=request.getParameter("ParentPKValue")%>&state=2&carmessageid="+id+"");
  $("div[id='shop'] iframe").attr("src","../maintainPage_main.jsp?pageCode=CUMA_SHOPSERVICE&pageName=????????????&ParentPKField=CUS_ID&ParentPKValue=<%=request.getParameter("ParentPKValue")%>&state=2&carmessageid="+id+"");
  $("div[id='map'] iframe").attr("src","../maintainPage_main.jsp?pageCode=CUMA_MAPSERVICE&pageName=????????????&ParentPKField=CUS_ID&ParentPKValue=<%=request.getParameter("ParentPKValue")%>&state=2&carmessageid="+id+"");
  $("div[id='music'] iframe").attr("src","../maintainPage_main.jsp?pageCode=CUMA_MUSSERVICE&pageName=????????????&ParentPKField=CUS_ID&ParentPKValue=<%=request.getParameter("ParentPKValue")%>&state=2&carmessageid="+id+"");
  $("div[id='wifi'] iframe").attr("src","../maintainPage_main.jsp?pageCode=CUMA_WIFI&pageName=wifi??????&ParentPKField=CUS_ID&ParentPKValue=<%=request.getParameter("ParentPKValue")%>&state=2&carmessageid="+id+"");  
  $("#car_modes").val(car);
  
  $("#l1").trigger("click");
  
   jz(); 
 }
</script>  

</head>
<body>	
	<div class="col-md-4 col-xs-4 col-sm-4" style="margin-bottom:10px">
		<div class="form-group">
            <div class="col-md-3 col-xs-3 col-sm-3"><label>??????:</label></div>
            <div class="col-md-9 col-xs-9 col-sm-9">
            	<input id="car_modes" value="" class="form-control">
            </div>
        </div>
	</div>

	<form>
		<div class="col-md-12 col-xs-12 col-sm-12">
			<ul id="myTab" class="nav nav-tabs">
				<!-- <li>
					<a class="btn btn-inverse" onclick="window.history.back(-1);">
						<span class="glyphicon glyphicon-ban-circle" aria-hidden="true"></span>??????
					</a>
				</li> -->
				    <li  class="active">
						<a id="l1" href="#car" data-toggle="tab">????????????</a>
					</li>
					<li >
						<a id="l2" href="#mno" data-toggle="tab">MNO????????????</a>
					</li>
					<li >
						<a id="l3" href="#service" data-toggle="tab">????????????</a>
					</li>
					<li>
						<a id="l4" href="#namesys" data-toggle="tab">???????????????</a>
					</li>
					<li >
						<a id="l5" href="#shop" data-toggle="tab">????????????</a>
					</li>
					<li >
						<a id="l6" href="#map" data-toggle="tab">????????????</a>
					</li>
					<li >
						<a id="l7" href="#music" data-toggle="tab">????????????</a>
					</li>
					<li >
						<a id="l8" href="#wifi" data-toggle="tab">wifi??????</a>
					</li>
			</ul>
		</div>
		</br>
		<div class="col-md-12 col-xs-12 col-sm-12">

			<div class="tab-content">
			        
			
			        <div class="tab-pane fade in active" id='car'>
						<iframe src='query_message_all2_1_m.jsp?pageCode=CUMA_CUS_CAR_MODE&pageName=????????????&ParentPKField=ID&ParentPKValue=<%=request.getParameter("ParentPKValue")%>&cus_id=<%=request.getParameter("car_id") %>&name=<%=request.getParameter("name") %>&pro=<%=request.getParameter("pro") %>' scrolling="no" frameborder="0"  width="99%" height="1000"></iframe>
					</div>
					
					
					<div class="tab-pane fade in active" id='mno' >
						<iframe  scrolling="no" frameborder="0"  width="99%" height="1000"></iframe>
					</div>
					
					<div class="tab-pane fade in active" id='service'>
						<iframe  scrolling="no" frameborder="0"  width="99%" height="1000"></iframe>
					</div>
					<div class="tab-pane fade " id='namesys'>
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
		
		
		<!--  <div id="load_message" class="easyui-window" title="" 
            data-options="modal:true,closed:true,minimizable:false,maximizable:false">   
               <iframe  style="width:100%;height:100%;" src="../loading.html"  frameborder=0 scrolling=no allowtransparency></iframe>
         </div>   -->

	</form>
	
	    
</body>
</html>