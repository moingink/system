<%@page import="com.yonyou.util.theme.ThemePath"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<%
	String ContextPath =request.getContextPath();
%>

<html lang="zh-cn">
	<head>
		<meta charset="utf-8">
		<title>网盘</title>
		<script type="text/javascript" src="<%=ContextPath %>/vendor/jquery/jquery.min.js"></script>
		<script type="text/javascript" src="<%=ContextPath %>/vendor/bootstrap/css/3.3.7/bootstrap.min.js"></script>
		<link rel="stylesheet" type="text/css" href="<%=ContextPath %>/vendor/bootstrap/css/3.3.7/bootstrap.min.css">
		<link href="./css/skydrive.css">
	</head>
	<body >
		<div class="head" style="background-color: #fff;width:100%;border:2px;border-radius: 5px;margin:10px auto">
			<button type="button" class="btn btn-primary"><span class="glyphicon glyphicon-plus" style="font-size:20px"></span>新建文件夹</button>
			<button type="button" class="btn btn-primary" style="margin:0px 10px"><span class="glyphicon glyphicon-eur" style="font-size:20px"></span>上传</button>
			<button type="button" class="btn btn-primary"><span class="glyphicon glyphicon-pencil" style="font-size:20px"></span>下载</button>
		</div>
		<table class="table table-hover">
			<thead>
				<tr>
					<th>文件名称</th>
					<th>文件路径</th>
					<th>文件类型</th>
					<th>文件大小</th>
					<th>备注</th>
				</tr>
			</thead>
			<tbody>
				
			</tbody>
		</table>
	</body>
	<script type="text/javascript">
		$(function(){
			
		})
	</script>
</html>