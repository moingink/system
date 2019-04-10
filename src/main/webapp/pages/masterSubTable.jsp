<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>主子表</title>
<!-- <script type="text/javascript" src="../vendor/jquery/jquery.min.js"></script>
<script type="text/javascript" src="../vendor/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="../vendor/bootstrap-table/src/bootstrap-table.js"></script>
<link href="../vendor/bootstrap/css/bootstrap.min.css">
<link href="../vendor/bootstrap-table/src/bootstrap-table.css"> -->
</head>
<body style="background-color: rgb(240,240,241)">
	<table style="width:100%;">
		<tr>
			<td>
				<table id="masterTable">
					
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table id="sonTable">
			
				</table>
			</td>
		</tr>
	</table>
	<div class="fixed-table-pagination">
	</div>
</body>
<jsp:include page="../include/public.jsp"></jsp:include>
<script type="text/javascript">
	var parentCode="<%=request.getParameter("parentCode")%>";
	var sonCode="<%=request.getParameter("sonCode")%>";
	var data_str=[];
	$(function(){
		initCol(parentCode);
    	initTab($("#masterTable"),parentCode,true);
    	data_str.splice(0,data_str.length);
    	initCol(sonCode);
    	initTab($("#sonTable"),sonCode,false);
	})
	function initTab($e,code,pagination){
		$e.bootstrapTable({
            url: '/system/base?cmd=init&dataSourceCode='+code/*+"&pageParam=sID=20190330240000020721"*/,         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: pagination,                   //是否显示分页（*）
            sortable: false,                     //是否启用排序
            sortOrder: "asc",                   //排序方式
            //queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 10,                       //每页的记录行数（*）
            pageList: [10, 25, 50, 100],        //可供选择的每页的行数（*）
            search: false,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: false,
            showColumns: false,                  //是否显示所有的列
            showRefresh: false,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            //height:526 ,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "ID",                     //每一行的唯一标识，一般为主键列
            showToggle: false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns:data_str,
            onLoadSuccess: function (data){
            	
		    }
        });
	}
	function initCol(code){
		$.ajax({
    		async: false,
            type: "post",
            url: "/system/base?cmd=queryColumns&dataSourceCode="+code+"&pageParam=ID=2019031896000000029",
            dataType: "text",
            success: function(data){
            	data_str.push(JSON.parse(data));
            }
        });
	}
</script>
</html>