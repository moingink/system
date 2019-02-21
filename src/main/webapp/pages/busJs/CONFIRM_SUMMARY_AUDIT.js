buttonJson =[	
              {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
              {name:'审核',fun:'if(isNullTable())audit()',buttonToken:'audit'},
              {name:'弃审',fun:'if(isNullTable())dAudit()',buttonToken:'dAudit'},
              {name:'导出Excel',fun:'if(isNullTable())reportExport()',buttonToken:'reportExport'},
              {name:'收入环比',fun:'incomeCompare()',buttonToken:'incomeCompare'}
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
	    	setData(data);
	    	setUp();
	    	page_heigth=$(document.body).height();
	    	if(page_heigth<600){
	    		page_heigth = 750;
	    	}
	    	setParntHeigth(page_heigth);
	    	bulidCacheTableJsonArray();
	    },
	    onLoadError: function(status,response){
	    	//登录超时
	    	if(response.getResponseHeader("TIMEOUTURL")!=null){
	    		window.top.location.href = response.getResponseHeader("TIMEOUTURL");
	    	}
	    	return "数据加载失败";
	    },
	    onDblClickRow :function (row,tr){
	    	dblClickFunction(row,tr);
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

$(function(){
	$("#TERM").val(getTime());//账期
});

//条件拼接
function param(){
	var term = $("#TERM").val();//账期
	var clientName = $("#CLIENT_NAME").val();//客户名称
	var incomeOfDept = $("#INCOME_OF_DEPT").val();//部门
	var businessType = $("#BUSINESS_TYPE").val();//业务类型
	var productType = $("#PRODUCT_TYPE").val();//产品
	var param = "&TERM="+term+"&CLIENT_NAME="+clientName+"&INCOME_OF_DEPT="+incomeOfDept+"&BUSINESS_TYPE="+businessType+"&PRODUCT_TYPE="+productType;
	return param;
}

var qusurl = '/system/finance?getConfirmSummaryAuditList'+param();
oTable.initCols($("#table"), colurl + findPageParamByDataSourceCode('CONFIRM_SUMMARY_AUDIT'),qusurl);

//表设置
function setUp(){
	$(".bs-checkbox").each(function(){//设置复选框不显示
		$(this).css("display","none");
	});
	$("#table tbody tr:last td:eq(1)").text('合计');//将序号改名为合计
}

function setData(data){
	if(data.total > 0){
		$("#MONEY_TOTAL").val(data.rows[data.total-1]["ACCOUNTS_INCOME"]);
		$("#AUDIT_PEOPLE").val(data.rows[data.total-1]["AUDIT_PEOPLE"]);
		$("#AUDIT_DATE").val(data.rows[data.total-1]["AUDIT_DATE"]);
	}
}

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
	oTable.queryTable($table, '/system/finance?getConfirmSummaryAuditList'+param());
}

//嵌入收入环比modal
$(document.body).append(
	'<div class="modal fade" id="incomeCompareModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">'+
		'<div class="modal-dialog" style="width: 90%;height: 90%">'+
			'<div class="modal-content">'+
				'<div class="modal-header">'+
					'<button type="button" class="close" data-dismiss="modal" aria-hidden="true">'+
						'×'+
					'</button>'+
					'<h4 class="modal-title" id="incomeCompareModalLabel"></h4>'+
				'</div>'+
				'<div class="modal-body">'+
					'<div id="queryParam" >'+
						'<div class="col-md-4" style="margin-top:10px;display:inline">'+
							'<div class="form-group">'+
								'<div class="col-md-4">'+
									'<label for="BUSINESS_TYPE1">业务类型：</label>'+
								'</div>'+
								'<div class="col-md-8">'+
									'<input type="TEXT" class="form-control" id="BUSINESS_TYPE1" autocomplete="off" value="">'+
								'</div>'+
							'</div>'+
						'</div>'+
						'<div class="col-md-4" style="margin-top:10px;display:inline">'+
							'<div class="form-group">'+
								'<div class="col-md-4">'+
									'<label for="PRODUCT_TYPE1">产品：</label>'+
								'</div>'+
								'<div class="col-md-8">'+
									'<input type="TEXT" class="form-control" id="PRODUCT_TYPE1" autocomplete="off" value="">'+
								'</div>'+
							'</div>'+
						'</div>'+
						'<div class="col-md-4" style="margin-top:10px;display:inline">'+
							'<a href="#"><span id="query2" class="btn btn-primary">查询</span></a>'+
						'</div>'+
					'</div>'+
					'<iframe id="incomeCompareReport" name="report" src="" width="100%" height="518" frameborder="0"></iframe>'+
				'</div>'+
				'<div class="modal-footer">'+
					'<button type="button" class="btn btn-inverse" data-dismiss="modal">'+
						'关闭'+
					'</button>'+
				'</div>'+
			'</div>'+
		'</div>'+
	'</div>'
);

//嵌入调整收入明细modal
$(document.body).append(
	'<div class="modal fade" id="confirmSummaryAuditModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">'+
		'<div class="modal-dialog" style="width: 700px;height: 90%">'+
			'<div class="modal-content">'+
				'<div class="modal-header">'+
					'<button type="button" class="close" data-dismiss="modal" aria-hidden="true">'+
						'×'+
					'</button>'+
					'<h4 class="modal-title" id="myModalLabel">调整收入明细</h4>'+
				'</div>'+
				'<div class="modal-body" id="tzsrmx"></div>'+
				'<div class="modal-footer">'+
					'<button type="button" class="btn btn-inverse" data-dismiss="modal">'+
						'关闭'+
					'</button>'+
				'</div>'+
			'</div>'+
		'</div>'+
	'</div>'
);

//当前时间获取
function getTime(){
	var date = new Date();
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	var day = date.getDate();
	if (month >= 1 && month <= 9) {
    	month = "0" + month;
    }
    if (day >= 0 && day <= 9) {
        day = "0" + day;
    }
    return year+"-"+month;
}

/**业务处理**/

//账期发生改变
$("#TERM").change(function() {
	var qusurl = '/system/finance?getConfirmSummaryAuditList'+param();
	oTable.initCols($("#table"), colurl + findPageParamByDataSourceCode('CONFIRM_SUMMARY_AUDIT'),qusurl);
});

//判断是否审核过
function isAudit(term){
	var flag = true;
	$.ajaxSettings.async = false;
	$.get("/system/finance?getConfirmSummaryAuditCountByTerm&term="+term,function(data){
		if(parseInt(data)>0){
			flag = false;
		}
	});
	$.ajaxSettings.async = false;
	return flag;
}

//判断Tbale是否存在数据
function isNullTable(){
	var flag = true;
	var tableLength = $("#table tbody tr").length;
	if(tableLength <= 1){
		flag = false;
		oTable.showModal('提示', "暂无数据");
	}
	return flag;
}

//点击审核
function audit(){
	var term = $("#TERM").val();//账期
	if(isAudit(term)){
		if(confirm("确定要审核"+term+"月的计收嘛？")){
			$.ajax({
				type: "post",
				url: "/system/finance?audit",
				data: {"term":term,"auditPeople":userName},
	    		async: false,
	    		success: function(data){
	    			if(parseInt(data) == 1){
	    				oTable.showModal('提示', "审核成功");
	    				queryTableByParam('','','');
	    			}else{
	    				oTable.showModal('提示', "审核失败");
	    			}
	    		}
			});
		}else{
			return false;
		}
	}else{
		oTable.showModal('提示', "已审过");
	}
}

//点击弃核
function dAudit(){
	var term = $("#TERM").val();//账期
	if(isAudit(term) == false){
		if(confirm("确定要弃核"+term+"月的计收嘛？")){
			$.ajax({
				type: "post",
				url: "/system/finance?dAudit",
				data: {"term":term},
	    		async: false,
	    		success: function(data){
	    			if(parseInt(data) == 1){
	    				oTable.showModal('提示', "弃核成功");
	    				queryTableByParam('','','');
	    			}else{
	    				oTable.showModal('提示', "弃核失败");
	    			}
	    		}
			});
		}else{
			return false;
		}
	}else{
		oTable.showModal('提示', "请先审核");
	}
}

//点击导出Excel
function reportExport(){
	var term = $("#TERM").val();//账期
	window.location.href="/system/finance?reportExport&TERM="+term;
}
	
//点击收入还比
function incomeCompare(){
	$("#BUSINESS_TYPE1").val("");
	$("#PRODUCT_TYPE1").val("");
	$("#incomeCompareModalLabel").text($("#TERM").val()+"收入环比");
	$("#incomeCompareReport").attr("src","/system/pages/incomeCompare.jsp?pageCode=INCOME_COMPARE&pageName=收入环比"+param()+"&BUSINESS_TYPE1=&PRODUCT_TYPE1=");
	$("#incomeCompareModal").modal('toggle')
}

//双击事件
function dblClickFunction(row,tr){
	var json = JSON.parse(JSON.stringify(row));
	var detail = json["DETAIL"];
	if(detail != null && detail != "" && detail != undefined){
		$.ajax({
			type: "GET",
			url: "/system/finance/getDetail",
			data: {"id":detail},
			async: false,
	        dataType: "text",
	        success: function(data){
	        	$("#tzsrmx").html(data);
	        }
	    });
		$('#confirmSummaryAuditModal').modal('toggle');
		setTimeout(function(){
			$("iframe[id='confirmSummaryAuditReport']").each(function(){
				$(this).contents().find("input").attr("disabled","disabled");
				$(this).contents().find("button").hide();
				$(this).contents().find("a").hide();
			});
		},500);
	}else{
		oTable.showModal('提示', "暂无调整收入明细");
	}
}

$("#query2").click(function(){
	var term = $("#TERM").val();
	var business_type = $("#BUSINESS_TYPE1").val();
	var product_type = $("#PRODUCT_TYPE1").val();
	$("#incomeCompareReport").attr("src","/system/pages/incomeCompare.jsp?pageCode=INCOME_COMPARE&pageName=收入环比&TERM="+term+"&BUSINESS_TYPE1="+business_type+"&PRODUCT_TYPE1="+product_type);
});
