<%@page import="java.net.URLDecoder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page language="java" import="net.sf.json.JSONObject" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!--  <link href="http://libs.baidu.com/bootstrap/3.0.3/css/bootstrap.min.css" rel="stylesheet">
<script src="http://libs.baidu.com/jquery/2.0.0/jquery.min.js"></script> -->
<link rel="stylesheet" href="../vendor/bootstrap/css/bootstrap.min.css">
<link href="../vendor/bootstrap-fileinput-su/css/font-awesome.css" media="all" rel="stylesheet" type="text/css"/><!-- 网页字体图标库 -->
<script src="../vendor/jquery/jquery.min.js"></script>
<script src="../vendor/bootstrap/js/bootstrap.min.js"></script>
<script src="../vendor/jquery-shCircleLoader/jquery.shCircleLoader-min.js"></script><!-- 加载 -->
<title>归档</title>

<%
	String data = (String) request.getParameter("respData");
	String state=null;
	String msg=null;
	JSONObject jsonInfo=null;
	String batchno=null;
	String fileid=null;
	String filename=null;
	if (data != null && !"".equals(data)) {
		//System.out.println("importarchive.jsp27行解码前{}"+data);
		data=URLDecoder.decode(data, "utf-8");
		//System.out.println("importarchive.jsp29行解码后{}"+data);
		JSONObject jsda = JSONObject.fromObject(data);
		state = (String) jsda.get("state");
		msg = (String) jsda.get("msg");
		if("success".equals(state)){
			jsonInfo = JSONObject.fromObject(jsda.get("body"));
			batchno = (String) jsonInfo.get("batchno");
			fileid = (String) jsonInfo.get("fileid");
			filename = (String) jsonInfo.get("filename");
		}
	}
	request.setCharacterEncoding("utf-8");
	String pageCode = request.getParameter("pageCode");
	String id = request.getParameter("id");
	String bs_code = request.getParameter("bs_code");
	String _batchno = request.getParameter("batchno");
	
	String path = request.getContextPath();
	//<!-- 采购导入 -->
	String nbsp="&nbsp;&nbsp;&nbsp;&nbsp;";
	String nbsp2="&nbsp;&nbsp;";

%>


	<script language = "javascript" >
var fileuploadmaxSize = 100;//文件上传大小限制

	$(document).ready(function(){
  //代码
	  if(<%= data %> !=null && <%= data %> !=""){
			  $('#main').hide();
			  $('#results').show();
			//$("#tabler").html("<%=data%>"); 

	  }else{
	  	// $('#results').hide();
	  }
	})

	function toVaild() {
		var val = document.getElementById("inputfile").value;
		if (val != "") {
			var size = $("#inputfile")[0].files[0].size;
		    //var filesize = (size / 1024).toFixed(2);//kb
			var filesize = (size / (1024*1024) ).toFixed(2);//kb
			var filesizeMB = (size / (1024*1024) ).toFixed(2)+'MB';//kb
			//console.info((size/(1024*1024)).toFixed(2)+'MB');//mb
			//alert("校验成功，之后进行提交");
			if (filesize<=fileuploadmaxSize) {
				showLoad();
				return true;
			}else{
				alert("上传附件太大!请传输100MB及以下大小的文件");
				return false;
			}
			
		} else {
			alert("未选择文件，不进行提交");
			return false;
		}
	}

	function checkFileExt(filename) {
		var flag = false;
		//状态
		var arr = ["xls", "xlsx"];
		//取出上传文件的扩展名
		var index = filename.lastIndexOf(".");
		var ext = filename.substr(index + 1);
		//循环比较
		for (var i = 0; i < arr.length; i++) {
			if (ext == arr[i]) {
				flag = true;
				//一旦找到合适的，立即退出循环
				break;
			}
		}
		return flag;
	}

	function showLoad(){
		$('#shclDefault').shCircleLoader();
		$('#backshc').css("display","block");
	}
	function hideLoad(){
		$('#backshc').css("display","none");
	}
	function submit_before(){
		$("#excle_in_").val("");
		var paramExtra='{"bs_code":"<%= bs_code %>","batchno":"<%= _batchno %>","id":"<%= id %>" ,"pageCode":"<%= pageCode %>"}';
		//console.info(paramExtra);
		$("#excle_in_").val(paramExtra);
		return toVaild();
	}
	</script>
<style type="text/css">
#main {
	width: 90%;
	margin: 0px auto;
}

#results {
	display: none;
	margin-top: 6px;
}

#tabler {
	width: 90%;
	margin: 0px auto;
}

.list-group-item {
	text-align: center;
}

#but {
	margin: 0px auto;
}

#excle_in {
	/* visibility: hidden; */
	display: none;
}

#excle_in_ {
	/* visibility: hidden; */
	display: none;
}

#inputfile {
	border-radius: 2px;
	border: 1px #1c83dc solid;
}
h3{
	width: 30%;
    margin: 0px auto;
    margin-bottom: 5px;
}
#backmain{
	width: 100%;
	height: 100%;
	z-index: 2;
}
#backshc{
	display:none;
    position: fixed;
    top: 0%;
    left: 0%;
	width: 100%;
	height: 100%;
	z-index: 6;
	background-color:rgba(0, 0, 0, 0.35);
}
#backshc p{
	text-align: center;
	margin-top:2%;
	font-size: large;
}
#shclDefault {
	width: 100px;
	height: 100px;
   	margin: 15% auto 0% auto;
	z-index: 8;
}
</style>
</head>
<body>
<div id="backmain">

	<div id="main">
		<br>
		<form name="files"
			action="<%=path%>/import/archive"
			method="post" 
			enctype="multipart/form-data"
			onsubmit="return submit_before()">
			<div class="form-group">
				<label for="inputfile"><i class="fa fa-exclamation-circle" aria-hidden="true"></i>&nbsp;请选择文件</label>
				<input type="file" id="inputfile" name="files">
			</div>
			<hr>
			<div type="button" class="btn btn-primary"
				onclick="$('#excle_in').click();">
				<span class="glyphicon glyphicon-import" aria-hidden="true"></span><%= nbsp2 %>归档
			</div>
			<input id="excle_in" class="btn btn-primary" type="submit" value="导入" />
			<input id="excle_in_" class="btn btn-primary" name="paramExtra" value="" />
		</form>

	</div>
	<div id="results">
		<div id="tabler">
				<div id="but">
			<button type="button" class="btn btn-primary" 
				onclick="window.history.back(-1);">
				<span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span><%= nbsp2 %>返回
			</button>
			<button type="button" class="btn btn-info"
				onclick="window.close();opener.location.reload();">
				<span class="glyphicon glyphicon-ban-circle" aria-hidden="true"></span><%= nbsp2 %>关闭
			</button>
		</div>
			<h3 align="center"><strong>信息</strong></h3>
			<ul class="list-group">
			   <li class="list-group-item"><strong>归档结果:</strong><%= nbsp %><span><%= msg %></span></li>
			   <%
			   if(jsonInfo!=null && !"".equals(jsonInfo)){
				   %>
				    <li class="list-group-item"><strong>归档文件名:</strong><%= nbsp %><span><%= filename %></span></li>
				<%  }
			   %>
			</ul>
		</div>
	</div>
</div>

	<div id="backshc">
		<div id="shclDefault"></div> 
		<p>加载中</p>
	</div>
	
	
</body>
</html>