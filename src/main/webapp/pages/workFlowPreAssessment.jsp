<%@page import="com.yonyou.util.theme.ThemePath"%>
<%@page import="com.yonyou.util.RmIdFactory"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
	String ContextPath =request.getContextPath();
%>
<html lang="zh-cn">
<head>
	<meta charset="utf-8">
	<title>单表模板</title>
	<link rel="stylesheet" type="text/css" href="../easyui/themes/default/easyui.css">
</head>
<body id="body_div">
	<form class="form-horizontal">
		<div class="panel panel-primary">
			<div class="panel-body" id="bulidPage">
				<div class="panel panel-primary">
					<div id="tog_titleName" class="panel-heading">
						修改
					</div>
					<div class="panel-body">
						<div class="alert alert-info" id="message" style="display: none"></div>
						<div>
							<button id="btnSave" type="button" class="btn btn-success" onclick="isAudit()">
								<span class="glyphicon glyphicon-saved" aria-hidden="true"></span>保存
							</button>
							<input id="ins_or_up_buttontoken" type="hidden" value="updatePreAssessment"/>
						</div>
						<div class="form-group">
							<div class="col-md-12">
								<div id="errors"></div>
							</div>
						</div>
						<!-- 维护页面 -->
						<div class="panel-body" id="insPage"></div>
					</div>
				</div>
			</div>
		<input type="hidden" id="isNewStyle" value="1" />
	</form>
</body>

<jsp:include page="../include/public.jsp"></jsp:include>
<jsp:include page="buttonjs.jsp"></jsp:include>
<link rel="stylesheet" type="text/css" href="<%=ThemePath.findPath(request, ThemePath.SUB_SYSTEM_CSS)%>">

<script type="text/javascript">
	//主表主键在子表字段名
	var ParentPKField = '<%=request.getParameter("ParentPKField")%>';
	//主表主键值
	var ParentPKValue = '<%=request.getParameter("ParentPKValue")%>';
	
	$(function() {
		bulidPage(false,true,false,true);//获取按钮页面为true是为了加载数据源对应js
		setTimeout('getRecord()',100);
	});
	
	function getRecord(){
		var param = "&dataSourceCode="+dataSourceCode+"&SEARCH-"+ParentPKField+"="+ParentPKValue;
		if(typeof(specifyParam) != "undefined"){
			param += specifyParam;
		}
		var record = querySingleRecord(param);
		if(!jQuery.isEmptyObject(record)){
			$inspage.find("[id]").each(function() {	
  				$(this).val(record[$(this).attr("id")]);
			});
			initDetailTable();
			//是否建设投资 值为是时，显示相关字段
			if($('#BUILD_INVESTMENT').val() == "1"){
				buildInvestmentShow();
			}
			busId = $('#BUS_ID').val();//商机主键
			//产品多选数据回显
			var businessIds = record["BUSINESS_ID"];//包含业务主键
			var productName = record["PRODUCT_NAME"].split(',');//产品名称
			if (businessIds.length > 0) {
				var html = "";
				$.ajax({
					type : "GET",
					url : "/system/business/getProductInfo",
					async : false,
					dataType : "json",
					data : {
						"ids" : businessIds
					},
					success : function(data) {
						for (var i = 0; i < data.length; i++) {
							var proName = data[i]["PRO_NAME"];
							var id = data[i]["ID"];
							html += "<option value = '" + proName + "' id = '" + id + "'>" + proName + "</option>";
						}
					},
					error : function(data) {
						alert("获取产品名称失败，请联系管理员！" + "\t" + "错误代码：" + JSON.stringify(data));
					}
				});
				$("#PRODUCT_NAME").html(html);
				$('#PRODUCT_NAME').selectpicker('refresh');	
				$("#PRODUCT_NAME").selectpicker('val', productName);//多选赋值
			}
		}
	}
	
	function save(){
		$inspage.data("bootstrapValidator").validate();
	    if(!$inspage.data('bootstrapValidator').isValid()){ 
	    	setParntHeigth($("body").height());
	        return ; 
	    }
	    var cache_dataSourceCode =$("#cache_dataSourceCode").val();
		var _dataSourceCode=dataSourceCode;
		if(cache_dataSourceCode!=null&&cache_dataSourceCode.length>0){
			_dataSourceCode=cache_dataSourceCode;
		}
		savaByQuery1(t,_dataSourceCode,$inspage);
	}
	
	//保存
	function savaByQuery1(t,_dataSourceCode,$div){
		var id = $('#ID').val();
		var BILL_STATUS = $('#BILL_STATUS').val();
		if(BILL_STATUS == ""){
			$('#BILL_STATUS').val("0");
		}
		if(id == ""){
			$('#ID').val(getId());
		}
		var buttonToken =$("#ins_or_up_buttontoken").val();
		var message = transToServer(findBusUrlByButtonTonken(buttonToken,'',_dataSourceCode),getJson($div),JSON.stringify(json));
		oTable.showModal('modal', message);
		if(message == "保存成功" || message == "修改成功"){
			formDisable();
			$('#tj').removeAttr('disabled');//提交按钮接触隐藏
			//删除子表
			if(deleteIds != ""){
				deleteIds = deleteIds.substring(0,deleteIds.length-1);
				$.ajax({
					async : true,
					type : "GET",
					url : '/system/business/delSurplusData',
					dataType : "json",
					data : {
						"deleteIds" : deleteIds
					},
					success : function(data) {
			
					},
					error : function(data) {
						alert("删除资费明细数据失败，请联系管理员！" + "\t" + "错误代码：" + JSON.stringify(data));
					}
				}); 
			}
		    //前评估参选商机，商机只能被参选一次，商机参选修改时需更新状态
		    if(busId != $('#BUS_ID').val()){
		    	$.ajax({
			    	async: true,
			    	type: "post",
					url: '/system/business/updateBusinessPreAssessment',
					dataType: "json",
					data:{
						"busId":busId
					},
					success: function(data){
						
					},
					error : function(data) {
						alert("更改前评估参选商机标识失败，请联系管理员！" + "\t" + "错误代码：" + JSON.stringify(data));
					}
				});
		    }
			//更改商机阶段
			var businessNumber = $('#BUSINESS_NUMBER').val();//商机编号
			$.ajax({
		    	async: true,
		    	type: "post",
				url: '/system/business/updateBusinessStage',
				dataType: "json",
				data:{
					"businessNumber":businessNumber
				},
				success: function(data){
					
				},
				error : function(data) {
					alert("更改商机阶段失败，请联系管理员！" + "\t" + "错误代码：" + JSON.stringify(data));
				}
			});
		}
	}
	
	function getId(){
		var rmId = '<%= RmIdFactory.requestId("PRE_ASSESSMENT", 1)[0]%>';
		return rmId;
	}
	
	//验证提交是否成功
	function isAudit(){
		var selected = JSON.parse("["+getJson($("#insPage"))+"]");
		selected[0]['productList'] = $('#PRODUCT_ID').val();
		var flag = parent.setJsonData(JSON.stringify(selected[0]));
		if(flag == '0'){
			save();
		}else{
			oTable.showModal('提示', '保存失败');
		}
	}
	
</script>
</html>