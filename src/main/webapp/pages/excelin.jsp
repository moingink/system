<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="../vendor/jquery/jquery.min.js"></script>
<title>Excel导入</title>
<%
  String path = request.getContextPath();
  String dataSourceCode = request.getParameter("dataSourceCode");
  String token = request.getParameter("token");
%>
</head>

<style>
.btn-primary {
    color: #fff;
    background-color: #337ab7;
    border-color: #2e6da4;
}
.btn {
    display: inline-block;
    padding: 6px 12px;
    margin-bottom: 0;
    margin-left: 7px;
    font-size: 14px;
    font-weight: normal;
    line-height: 1.42857143;
    text-align: center;
    white-space: nowrap;
    vertical-align: middle;
    cursor: pointer;
    background-image: none;
    border: 1px solid transparent;
    border-radius: 4px;
}
</style>
<body>
<form name="files" action="<%=path %>/base?cmd=uploadAffixs1&dataSourceCode=<%=dataSourceCode %>&token=<%=token %>" method="post" enctype="multipart/form-data" >
<input id="file" type="file" name="files"/>
<input type="submit" value="导入" class="btn btn-primary"/>
</form>
</body>
<script type="text/javascript">
$('form').submit(function(){
	if($('#file').val() == ""){
		alert("请选择文件！");
		return false;
	}else{
		return true;
	}
})
</script>
</html>