buttonJson =[	
              /*{name:'查询',fun:'queryTable(this)',buttonToken:'query'},*/
			];

var oTableInit = new Object();
// 初始化Table
oTable.queryTable = function($e, url) {
	$e.bootstrapTable('destroy'); 
	$e.bootstrapTable({
		url : url, // 请求后台的URL（*）
		method : 'get', // 请求方式（*）
		dataType: "json",
		toolbar: "#toolbar",
		striped : false, // 是否显示行间隔色
		cache : false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
		sortable : false, // 是否启用排序
		queryParams : oTableInit.queryParams,// 传递参数（*）
		sidePagination : "server", // 分页方式：client客户端分页，server服务端分页（*）
		columns : oTableInit.columns,
		formatLoadingMessage: function () {  
		     return "请稍等，正在加载中...";  
		},  
		formatNoMatches: function () {  //没有匹配的结果  
			if(url==null){
				 return '请【查询】数据！'; 
			}
		    return '无符合条件的记录';  
		},
	    onLoadSuccess: function (data){
	    	page_heigth=$(document.body).height();
	    	setParntHeigth(page_heigth);
	    	bulidCacheTableJsonArray();
	    	setUp();
	    },
	    onLoadError: function(status,response){
	    	//登录超时
	    	if(response.getResponseHeader("TIMEOUTURL")!=null){
	    		window.top.location.href = response.getResponseHeader("TIMEOUTURL");
	    	}
	    	return "数据加载失败";
	    }
	});
};

//初始化列布局后调用queryTable展现数据
oTable.initCols = function($e,colurl,qusurl){
	var data_str="";
	$.ajax({
		async: false,
        type: "post",
        url: colurl,
        dataType: "text",
        success: function(data){
        	data_str=data;
        }
    });
    oTableInit.columns =JSON.parse(data_str);
    setTimeout(function () {
    	oTable.queryTable($e,qusurl);
    }, 1000);
	isDestroy=true;
};

//表设置
function setUp(){
	$(".bs-checkbox").each(function(){//设置复选框不显示
		$(this).css("display","none");
	});
	
	$("#table tbody tr:last td:eq(1)").text('合计');
}

//var BUSINESS_TYPE1 = <%=request.getParameter("BUSINESS_TYPE1")%>;

var qusurl = '/system/finance?getIncomeCompareTableList&TERM='+term+'&BUSINESS_TYPE1='+BUSINESS_TYPE1+'&PRODUCT_TYPE1='+PRODUCT_TYPE1;
oTable.initCols($("#table"), colurl + findPageParamByDataSourceCode('INCOME_COMPARE'),qusurl);

//重写查询函数
function queryTableByParam(t,_dataSourceCode,_query_param){
	var queryButtonToken = $("#query_buttontoken").val();
	var buttonToken='';
	if(queryButtonToken!=null&&queryButtonToken.length>0){
		buttonToken=$("#query_buttontoken").val();
	}else{
		if(query_buttonToken!=null&&query_buttonToken.length>0){
			buttonToken = query_buttonToken;
		}else{
			buttonToken = $(t).attr("buttonToken");
		}
		$("#query_buttontoken").val(buttonToken);
	}
	oTable.queryTable($table, '/system/finance?getIncomeCompareTableList&TERM='+term);
}