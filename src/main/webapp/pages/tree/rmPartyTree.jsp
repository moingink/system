<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>机构延迟加载树实例</title>
	<link rel="stylesheet" href="themes/default/style.min.css" />
	
	<script src="../../vendor/jquery/jquery-1.11.2.min.js"></script>

	<script src="jstree.min.js"></script>
	
</head>
<body>
	<%@ page language="java" contentType="text/html; charset=UTF-8"  
    pageEncoding="UTF-8"%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">  
<html>  
<head>  
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">  
<title>JSTree</title>  
<link rel="stylesheet" href="js/themes/default/style.min.css" />  
<script src="../../vendor/jquery/jquery.min.js"></script>  
<script src="./jstree.min.js"></script>  
<link rel="stylesheet" href="../../vendor/bootstrap/css/bootstrap.min.css">
</head>  
<body>  
<p>标签式的导航菜单</p>
<ul id="myTab" class="nav nav-tabs">
  <li onclick='showTabs("http://www.baidu.com")' class="active"><a href="#" data-toggle = "tab" >Home</a></li>
  <li onclick='showTabs("http://www.sohu.com")'><a href="#" data-toggle = "tab">SVN</a></li>
  <li onclick='showTabs("http://www.baidu.com")'><a href="#" data-toggle = "tab">iOS</a></li>
  <li onclick='showTabs("http://www.baidu.com")'><a href="#" data-toggle = "tab">VB.Net</a></li>
</ul>
				<div class="tab-content">
	<div class="modal-body">       
		<iframe id="myframe"  height="500" src="../partyMain.jsp" frameBorder="0" width="99.6%"></iframe>  
	</div> 
	</div>		
    <div id="jstree_div"></div>  
    <script type="text/javascript">
	function showTabs(url)
	{
		//alert(url);
		$('myframe').attr("src",url);
	}
        
    </script>  
</body> 
</body>
</html>