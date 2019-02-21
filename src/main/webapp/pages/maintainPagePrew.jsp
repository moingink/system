<%@page import="com.yonyou.util.RmIdFactory"%>
<%@page import="com.yonyou.util.theme.ThemePath"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<%
String ContextPath =request.getContextPath();
String PROJECT_COST=request.getParameter("PROJECT_COST");
String SET_BUILD_INVESTMENT_COST_T=request.getParameter("SET_BUILD_INVESTMENT_COST_T");
String SET_BUILD_INVESTMENT_COST_F=request.getParameter("SET_BUILD_INVESTMENT_COST_F");
String BUILD_INVESTMENT_COST_ALL=request.getParameter("BUILD_INVESTMENT_COST_ALL");
String BUILD_INVESTMENT_COST_T=request.getParameter("BUILD_INVESTMENT_COST_T");
String BUILD_INVESTMENT_COST_F=request.getParameter("BUILD_INVESTMENT_COST_F");

%>

<html lang="zh-cn">
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"> 
		<title>立项下达</title>
	</head>
	<body id="body_div">
	<div id="tip" style="display: none; height:30px;line-height:30px;"><p style="color: red;text-align: right">Tip:如项目金额超过300万元，请上传项目可研方案和第三方评审意见！</p></div>
		<form class="form-horizontal">
			<div class="panel panel-primary">
		
				<div class="panel-body" id="bulidPage">
					<div class="panel panel-primary">
						<div id="tog_titleName" class="panel-heading">
							新增 
						</div>
						<div class="panel-body">
							<div class="alert alert-info" id="message" style="display: none"></div>
							<div>
								
							</div>
							<div class="form-group">
								<div class="col-md-12">
									<div id="errors"></div>
								</div>
							</div>
							<!-- 维护页面 -->
							<div class="panel-body" id="insPage">
								<div id="distpicker4">
								<table id="product_table" data-row-style="rowStyle" class="table table-hover">
							<!-- <span class="input-group-addon" style="cursor:pointer" onclick="checkReference(this,'REF(BUSS_TYPE,ID:BUS_MAIN_ID;TYPE_NAME:IN_BUSINESS,0)','IN_BUSINESS','INSUP')"><span class="glyphicon glyphicon-search"></span></span> -->
	<thead>
		<tr class="info">
				<th style="text-align: center; vertical-align: middle; " data-field="PARENT_PARTY_CODE" tabindex="0">
					<div class="th-inner ">业务类型</div>
					<div class="fht-cell" style="width: 120px;"></div>
				</th>
				<th style="text-align: center; vertical-align: middle; " data-field="PARENT_PARTY_NAME" tabindex="0">
					<div class="th-inner ">产品类型</div>
					<div class="fht-cell" style="width: 104px;"></div>
				</th>
				<th style="text-align: center; vertical-align: middle; " data-field="PARENT_PARTY_NAME" tabindex="0">
					<div class="th-inner ">产品</div>
					<div class="fht-cell" style="width: 104px;"></div>
				</th>
				 <th style="text-align: center; vertical-align: middle; " data-field="PARENT_PARTY_NAME" tabindex="0">
					<div class="th-inner ">责任部门</div>
					<div class="fht-cell" style="width: 104px;"></div>
				</th> 
				<th style="text-align: center; vertical-align: middle; " data-field="PARENT_PARTY_NAME" tabindex="0">
					<div class="th-inner ">操作</div>
					<div class="fht-cell" style="width: 104px;"></div>
				</th>
			</tr>
	</thead>
	
	<tbody id="users">
		<tr id="distpicker">
			<td style="text-align: center; vertical-align: middle; "><select class="form-control" id="province"></select></td>
			<td style="text-align: center; vertical-align: middle; "><select class="form-control" id="city"></select></td>
			<td style="text-align: center; vertical-align: middle; "><select class="form-control" id="district" class="district"></select></td>
			<td style="text-align: center; vertical-align: middle; "></td>
			<td style="text-align: center; vertical-align: middle; display: none"></td>
			<td style="text-align: center; vertical-align: middle; "><input type="button"  onclick="del(this)" value="删行" id="deleteCert" class="btn btn-danger"><input type="button" onclick="addLine()" value="增行" id="insertCert" class="btn btn-primary"></td>
		</tr>
	</tbody>
	</table>
	</div>
	
							</div>
						</div>
					</div>
				</div>
		
		<input type="hidden" id="isNewStyle" value="1" />
		</form>
		
	</body>

	<jsp:include page="../include/public.jsp"></jsp:include>
	<jsp:include page="buttonjs.jsp"></jsp:include>
	<link rel="stylesheet" type="text/css" href="<%=ThemePath.findPath(request, ThemePath.SUB_SYSTEM_CSS)%>">
	<!-- <script src="http://libs.baidu.com/jquery/1.10.2/jquery.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script> -->
	<script src="../js/distpicker.data.js"></script>
	<script src="../js/distpicker.js"></script>
	<script src="../js/main.js"></script>
	<script type="text/javascript">
			var PROJECT_COST='<%=PROJECT_COST%>';
			var SET_BUILD_INVESTMENT_COST_T='<%=SET_BUILD_INVESTMENT_COST_T%>';
			var SET_BUILD_INVESTMENT_COST_F='<%=SET_BUILD_INVESTMENT_COST_F%>';
			var BUILD_INVESTMENT_COST_T='<%=BUILD_INVESTMENT_COST_T%>';
   			var BUILD_INVESTMENT_COST_F='<%=BUILD_INVESTMENT_COST_F%>';
   			var BUILD_INVESTMENT_COST_ALL='<%=BUILD_INVESTMENT_COST_ALL%>';
			var selectBusinessType='<select class="form-control" id="busiType" name="costtype" style="width:90%"><option value="0">请选择</option><option value="1">主设备费用</option><option value="2">软件开发</option><option value="3">安装工程费</option><option value="4">工程建设其他费</option><option value="5">预备费</option><option value="6">贷款利息</option></select>';
			var selectProductName="";
		//主表主键在子表字段名
		var ParentPKField = '<%=request.getParameter("ParentPKField")%>';
		//主表主键值
		var ParentPKValue = '<%=request.getParameter("ParentPKValue")%>';
		var proj_source_id = '<%=request.getParameter("proj_source_id")%>';
		var pageCode = '<%=request.getParameter("pageCode")%>';
		//是否需要加载后台数据
		var state = '<%=request.getParameter("state")%>';
		var projCode = '';
		var projName='';
		var cost ='';
		var bill_statues ="";
		var button ="add";
		var uname = '<%=request.getParameter("userName")%>';
		var comname = '<%=request.getParameter("companyName")%>';
		var pre_amount =0;
		var pre_rate = 0;
		$.ajax({  
              url : "/system/project/projectinfo?proj_source_id="+ParentPKValue,  
              dataType : "json",
                          type : "GET", 
                          async: false, 
                          success : function(data) {
                          	console.log(data);
                          	if(data.length>0){
	                          	projCode = data[0].OPPORTUNITY_CODE;
	                          	projName = data[0].PROJ_NAME;
	                          	//前评估收入  和收益率
	                          	pre_amount = data[0].TOTAL_INCOME;
	                          	pre_rate = data[0].RATE_OF_RETURN;
                          	}
                          }
            });
		$(function() {
			bulidPage(false,true,false,true);//获取按钮页面为true是为了加载数据源对应js
			//默认为新增
			$("#ins_or_up_buttontoken").val('add');
			$("#"+ParentPKField).val(ParentPKValue);
			//第一次进入默认激活的页面需要手动请求下最新数据
			if(state == 1){
				getRecord();
			}
			
			if(pageCode=="PROJ_REQUIREMENT"){
				//设置项目类型 和 需求编号到项目下达表中
				$("#DEPTNAME").val(comname);
				$("#CREATOR_NAME").val(uname);
				$("#DEPTNAME").attr("disabled", true);
				$("#CREATOR_NAME").attr("disabled", true);
			}else if(pageCode=="PROJ_PROPOSAL_GK"){
				ajaxPre();
				$('#bs_code').val('PROJ_PROPOSAL_GK');
				//$('#users').append('<tr><td style="text-align: center; vertical-align: middle; "><select class="form-control" id="busiType" name="costtype" style="width:90%"><option  selected="selected">请选择</option><option value="1">主设备费用</option><option value="2">软件开发</option><option value="3">安装工程费</option><option value="4">工程建设其他费</option><option value="5">预备费</option><option value="6">贷款利息</option></select></td><td style="text-align: center; vertical-align: middle; "><select class="form-control" id="busiType" name="costtype" style="width:90%"><option  selected="selected">请选择</option><option value="1">主设备费用</option><option value="2">软件开发</option><option value="3">安装工程费</option><option value="4">工程建设其他费</option><option value="5">预备费</option><option value="6">贷款利息</option></select></td><td style="text-align: center; vertical-align: middle; "><select class="form-control" id="productName" name="costtype" style="width:90%"><option selected="selected">请选择</option><option value="1">主设备费用</option><option value="2">软件开发</option><option value="3">安装工程费</option><option value="4">工程建设其他费</option><option value="5">预备费</option><option value="6">贷款利息</option></select></td><td style="text-align: center; vertical-align: middle; ">111</td><td style="text-align: center; vertical-align: middle; "><input type="button"  onclick="del(this)" value="删行"><input type="button" onclick="addLine()" value="增行"></td></tr>');
			}else{
				$("#OPPORTUNITY_CODE").attr("disabled", true);
				$("#PROJECT_COST").attr("disabled", true);
			}
			if(pageCode=="PROJ_PROPOSAL_GK"){
				$("#OPPORTUNITY_CODE").attr("disabled",true);
				$("#PROJ_NAME").attr("disabled",true);
				//回显项目编码和项目名称
				$("#OPPORTUNITY_CODE").val(projCode);
				$("#PROJ_NAME").val(projName);
				//计算前评估利润 
				//alert(BUILD_INVESTMENT_COST_ALL);
				var  pre_rate_amount = parseFloat(pre_amount).toFixed(3)- parseFloat($('#BUILD_INVESTMENT_COST_ALL').val()).toFixed(3);
				$('#INCOME_ESTIMATE').val(parseFloat(pre_amount).toFixed(3));
				$('#PROFIT_ESTIMATE').val(parseFloat(pre_rate_amount).toFixed(3)); //前评估利润额
				$('#RETURN_RATE_ESTIMATE').val(pre_rate) //前评估收益率
				
				$("#INCOME_ESTIMATE").attr("disabled",true);
				$("#PROFIT_ESTIMATE").attr("disabled",true);
				$("#RETURN_RATE_ESTIMATE").attr("disabled",true);
				$("#BUILD_INVESTMENT_COST_ALL").attr("disabled",true);
				$("#BUILD_INVESTMENT_COST_F").attr("disabled",true);
				$("#BUILD_INVESTMENT_COST_T").attr("disabled",true);
				$("#PROJECT_COST").attr("disabled",true);
				$("#SET_BUILD_INVESTMENT_COST_T").attr("disabled",true);
				$("#SET_BUILD_INVESTMENT_COST_F").attr("disabled",true);
				initMoney($("#BUILD_INVESTMENT_COST_ALL"));
				initMoney($("#BUILD_INVESTMENT_COST_T"));
				initMoney($("#BUILD_INVESTMENT_COST_F"));
				initMoney($("#SET_BUILD_INVESTMENT_COST_F"));
				initMoney($("#SET_BUILD_INVESTMENT_COST_T"));
				initMoney($("#PROJECT_COST"));
				$("#SET_BUILD_INVESTMENT_COST_T").blur(function(){
					var a = $("#SET_BUILD_INVESTMENT_COST_F").val();
					var b = $("#SET_BUILD_INVESTMENT_COST_T").val();
					var aa = parseFloat(a).toFixed(3);
					var bb = parseFloat(b).toFixed(3);
					$("#SET_BUILD_INVESTMENT_COST_F").val(aa);
					$("#SET_BUILD_INVESTMENT_COST_T").val(bb);
					var sum = parseFloat(aa)+parseFloat(bb);
					var yse = sum.toFixed(3);
					$("#PROJECT_COST").val(yse);
				});
			}			
			setParntHeigth($(document.body).height());//设置高度
			getFileTypeVal($("#insPage"));
		});
		//var bill_statue=0;
		
		var i_count =0;
		function getRecord(){
			var param = "&dataSourceCode="+dataSourceCode+"&SEARCH-"+ParentPKField+"="+ParentPKValue;
			if(typeof(specifyParam) != "undefined"){
				param += specifyParam;
			}
			var record = querySingleRecord(param);
			$('#back').removeAttr("disabled");
			//设置回显字段 项目编码和名称
			$("#OPPORTUNITY_CODE").val(projCode);
			$("#PROJ_NAME").val(projName);
			
			$("#PROJECT_COST").val(BUILD_INVESTMENT_COST_ALL);
			$("#SET_BUILD_INVESTMENT_COST_T").val(BUILD_INVESTMENT_COST_T);
			$("#SET_BUILD_INVESTMENT_COST_F").val(BUILD_INVESTMENT_COST_F);
			$("#BUILD_INVESTMENT_COST_ALL").val(BUILD_INVESTMENT_COST_ALL);
			$("#BUILD_INVESTMENT_COST_F").val(BUILD_INVESTMENT_COST_F);
			$("#BUILD_INVESTMENT_COST_T").val(BUILD_INVESTMENT_COST_T);
			$("#COST_ESTIMATE").val(cost);
			if(pageCode=="PROJ_RELEASED_NLXM1"){
				//设置项目类型 和 需求编号到项目下达表中
				$("#PROJ_TYPE").val('1');
				$("#BID_BUSS_NO").val(ParentPKValue);
				$("#CREATOR_ID").val(proj_source_id);
				$("#PROJ_CODE").attr("disabled", true);
				$("#PROJECT_COST").val(Number(SET_BUILD_INVESTMENT_COST_F)+Number(SET_BUILD_INVESTMENT_COST_T));
				$("#SET_BUILD_INVESTMENT_COST_F").val(SET_BUILD_INVESTMENT_COST_F);
				$("#SET_BUILD_INVESTMENT_COST_T").val(SET_BUILD_INVESTMENT_COST_T);
				initMoney($("#SET_BUILD_INVESTMENT_COST_F"));
				initMoney($("#SET_BUILD_INVESTMENT_COST_T"));
				initMoney($("#PROJECT_COST"));
			}
			if(!jQuery.isEmptyObject(record)){
				//已存在记录时为修改
				button ="";
				$("#ins_or_up_buttontoken").val('update');
				$("#tog_titleName").html("修改");
				$inspage.find("[id]").each(function() {			
	  				$(this).val(record[$(this).attr("id")]);
				});
				bill_statues = 3;
			}
			initMoney($("#SET_BUILD_INVESTMENT_COST_F"));
			initMoney($("#SET_BUILD_INVESTMENT_COST_T"));
			initMoney($("#PROJECT_COST"));
			if(pageCode=="PROJ_PROPOSAL_NL"){
			    $("#tip").show();
				$("#PROJ_CODE").attr("disabled",true);
				$("#PROJ_NAME").attr("disabled",false);
				$("#project_cost").attr("disabled",true);
				$("#SET_BUILD_INVESTMENT_COST_T").attr("disabled",false);
				$("#SET_BUILD_INVESTMENT_COST_F").attr("disabled",false);
				initMoney($("#SET_BUILD_INVESTMENT_COST_F"));
				initMoney($("#SET_BUILD_INVESTMENT_COST_T"));
				initMoney($("#PROJECT_COST"));
				$('#bs_code').val('PROJ_PROPOSAL_NL');

			$("#SET_BUILD_INVESTMENT_COST_T").blur(function(){
					var a = $("#SET_BUILD_INVESTMENT_COST_F").val();
					var b = $("#SET_BUILD_INVESTMENT_COST_T").val();
					var aa = parseFloat(a).toFixed(3);
					var bb = parseFloat(b).toFixed(3);
					$("#SET_BUILD_INVESTMENT_COST_F").val(aa);
					$("#SET_BUILD_INVESTMENT_COST_T").val(bb);
					var sum = parseFloat(aa)+parseFloat(bb);
					var yse = sum.toFixed(3);
					$("#PROJECT_COST").val(yse);
			});
			
			}
			$('#deleteCert').attr("value","删行");;
			$('#insertCert').attr("value","增行");;
			//未审批状态置灰部分 && bill_statues =="0"
			//异步请求产品相关信息
			$.ajax({  
              url : "/system/project/getProductList?id="+ParentPKValue,
              dataType : "json",  
                          type : "GET", 
                          async: false, 
                          success : function(data) {
                          	//回显产品列表信息
                          	//
                          	if(data.length>0){
                          		$('#users').html('');
	                          	for(var i=0;i<data.length;i++){
	                          		var id = "distpicker-"+i_count;
	                          		var pid = "province"+i_count;
	                          		var cid = "city"+i_count;
									var did = "district-"+i_count;
									var text ='<tr id="'+id+'"><td style="text-align: center; vertical-align: middle; "><select class="form-control" id="'+pid+'"></select></td><td style="text-align: center; vertical-align: middle; "><select class="form-control" id="'+cid+'"></select></td><td style="text-align: center; vertical-align: middle; "><select class="form-control" id="'+did+'" class="district"></select></td>'+'<td style="text-align: center; vertical-align: middle; ">'+data[i].DUTY_DEPT_NAME+'</td><td style="text-align: center; vertical-align: middle; display: none">'+data[i].DUTY_DEPT_ID+'</td><td style="text-align: center; vertical-align: middle; "><input type="button" onclick="del(this)" id="deleteCert" value="删行" class="btn btn-danger"/><input type="button" onclick="addLine()" id="insertCert" value="增行" class="btn btn-primary"/></td></tr>';
									$('#users').append(text);
									$("#"+id).distpicker({
										province: data[i].BUSINESS_TYPE_NAME,
									 	city: data[i].PRODUCT_TYPE_NAME,
									  	district: data[i].PRODUCT_NAME
									});
									i_count++;
									//$("#"+pid+" option[value='"+data[i].BUSINESS_TYPE_ID+"']").attr("selected","selected");
									//$("#"+cid+" option[value='"+data[i].PRODUCT_TYPE_ID+"']").attr("selected","selected");
									//$("#"+did+" option[value='"+data[i].PRODUCT_ID+"']").attr("selected","selected");
	                          	}
                          	}
                          }
           	});
           	if($('#ins_or_up_buttontoken').attr("disabled")=="disabled"){
           			$('#deleteCert').attr("disabled",true);
					$('#insertCert').attr("disabled",true);
           	}
			if(bill_statues !="" ){
				if(bill_statues!="7" && bill_statues !="0"){
					console.log("bill_statue="+bill_statues);
					console.log('不可编辑');
					$('#deleteCert').attr("disabled",true);
					$('#insertCert').attr("disabled",true);
					$("#bulidPage").find("[id]").each(function(){
							$(this).attr("disabled",true);
					});
					$("#bulidPage").find("[span]").each(function(){
							$(this).attr("onclick","");
					});
					//置灰附件删除图标
					console.log('-提交后置灰删除附件-');
					setTimeout(function(){
						$('#insPage').children().find('[id=files_a_del]').remove();
					},500);
				}
			}
			hide(isHide);//隐藏及置灰			
			$("#back").removeAttr("disabled");
		}
		
		//重写方法，保存后不清空输入框而是设为只读，保存按钮置灰
		function savaByQuery(t,_dataSourceCode,$div){
			console.log($div);
			var message ="";
			console.log('--------------');
            				$("#users").find("[id^=deleteCert]").each(function(){
								$(this).attr("disabled",true);
							});
							$("#users").find("[id^=insertCert]").each(function(){
								$(this).attr("disabled",true);
							});
			var buttonToken=$("#ins_or_up_buttontoken").val();
			
			
			if(pageCode=="PROJ_RELEASED_NLXM1"){
				if(button =="add"){
					$("#ID").val('<%= RmIdFactory.requestId("PROJ_RELEASED", 1)[0]%>');
					buttonToken ="insertReleasedNL";
				}
				message = transToServer(findBusUrlByButtonTonken(buttonToken,'&table=PROJ_RELEASED',_dataSourceCode),getJson($div));
			}else{
				if(button=="add"){
					$("#ID").val('<%= RmIdFactory.requestId("PROJ_RELEASED", 1)[0]%>');
				}
				message = transToServer(findBusUrlByButtonTonken(buttonToken,'',_dataSourceCode),getJson($div));
			}
			jsonValue();
			for(var i=0;i<saveDate.length;i++){
				saveDate[i].PROJ_SOURCE_ID = ParentPKValue;
			}
			var productMessage = "";
			$.ajax({  
              url : "/system/project/saveProduct",  
                        dataType : "json", 
                          type : "POST",
                          data: {json:JSON.stringify(saveDate),id:ParentPKValue},
                          async: false, 
                          success : function(data) {
                          	saveDate=[];
                          	productMessage = data.success;
                          }
           });
			
			if(pageCode=="PROJ_RELEASED_NLXM1"){
				if(message=="保存成功"){
					location.reload();
				}
			}
			if(message=="保存成功"&&productMessage==true||message=="修改成功"&&productMessage==true){
				alert(message);
			}else if(message=="保存成功"&&productMessage==""||message=="修改成功"&&productMessage==""){
				alert("产品保存失败");
			}else{
				alert("产品保存失败");
			}
				setReadonlyByDiv($("#insPage input[type='text']"));
				$("#ins_or_up_buttontoken").attr("disabled", true);
			
		}
	var saveDate= [];
	function jsonValue(){
		saveDate= [];
	 	$("#users tr").each(function (e) {
	 		var j = {};
			//e代表索引  从0开始   eq(0)就是第一行
		  	var busTypeId = $(this).find("td").eq(0).find("select").val();      //这样获取 也是每行的第一列
		  	var proTypeId = $(this).find("td").eq(1).find("select").val();
		  	var pid = $(this).find("td").eq(2).find("select").val();
		  	var busTypeName =  $(this).find("td").eq(0).find("option:selected").text();
		  	var proTypeName =  $(this).find("td").eq(1).find("option:selected").text();
		  	var pName =  $(this).find("td").eq(2).find("option:selected").text();
		  	var deptName = $(this).find("td").eq(3).text();
		  	var deptId = $(this).find("td").eq(4).text();
		  	j.DUTY_DEPT_NAME = deptName;
		  	j.DUTY_DEPT_ID = deptId
		  	j.BUSINESS_TYPE_ID = busTypeId;
		  	j.PRODUCT_TYPE_ID = proTypeId;
		  	j.PRODUCT_ID = pid;
		  	j.BUSINESS_TYPE_NAME = busTypeName;
		  	j.PRODUCT_TYPE_NAME = proTypeName;
		  	j.PRODUCT_NAME = pName;
		  	saveDate.push(j);
   		});
	 	console.log(saveDate);
	  	return saveDate;
	}
		function ajaxPre(){
			var OPPORTUNITY_CODE =$("#OPPORTUNITY_CODE").val();
			if(OPPORTUNITY_CODE!=''){
				var url =context+'/busAjax?cmd=findPreByProj&OPPORTUNITY_CODE='+OPPORTUNITY_CODE;
				$.ajax({
			    		async: false,
			    		type: "post",
						url: url,
						dataType: "json",
						data:{"jsonData":''},
						success: function(data){
							for(var key in data){
								$("#"+key).val(data[key]);
							}
				},
				error:function(XMLHttpRequest, textStatus, errorThrown){
					//登录超时
			    	if(XMLHttpRequest.getResponseHeader("TIMEOUTURL")!=null){
			    		window.top.location.href = XMLHttpRequest.getResponseHeader("TIMEOUTURL");
			    	}
					message ="请求失败";
					}
				});
			
			}
		}
		
		function initMoney($e){
			if($e.val()=='null'||$e.val()==''||$e.val()=='0'){
				$e.val("0.000");
			}else{
				$e.val(parseFloat($e.val()).toFixed(3));
			}
		}
		//计算函数  统计立项建设总成本 
		
	$("body").on("keyup", "", function (){
		var a = $('#SET_BUILD_INVESTMENT_COST_T').val();
		var b = $('#SET_BUILD_INVESTMENT_COST_F').val();
		$('#PROJECT_COST').val(Number(a)+Number(b));
	});
		function addLine(){
			var id = "distpicker-"+i_count;
			var did = "district-"+i_count;
			var text ='<tr id="'+id+'"><td style="text-align: center; vertical-align: middle; "><select class="form-control" id="province"></select></td><td style="text-align: center; vertical-align: middle; "><select class="form-control" id="city"></select></td><td style="text-align: center; vertical-align: middle; "><select class="form-control" id="'+did+'" class="district"></select></td>'+'<td style="text-align: center; vertical-align: middle; "></td><td style="text-align: center; vertical-align: middle; display: none"></td><td style="text-align: center; vertical-align: middle; "><input type="button" onclick="del(this)" id="deleteCert" value="删行" class="btn btn-danger"/><input type="button" onclick="addLine()" id="insertCert" value="增行" class="btn btn-primary"/></td></tr>';
			$('#users').append(text);
			$("#"+id).distpicker({
			    autoSelect: false
			});
			$("#"+id).distpicker('reset', true);
			i_count++;
		}
		//删行
		function del(obj){
			//获取当前行数  如果为1 则不走删除行数据
			var len = $("#users").find("tr").length;
			if(len>1){
				var tr=obj.parentNode.parentNode;
				tr.parentNode.removeChild(tr);
			}
		}
		//select 事件  回显部门名  隐藏code
		$("body").on("change", "[id^=district]", function (){
			//currSelecttTr = $(this).parent().find("select").index($(this)[0]);
			var selid = $(this).attr("id");
			var id = $('#'+selid+' option:selected').attr("data-code");
			//$(this).children("td:eq(3)").html("aaaaaa");
			var x = $(this).parent().parent().find("td");
			//获取所属部门名称 code 
			$.ajax({  
              url : "/system/project/getProductDept?pid="+id,  
              dataType : "json",
                          type : "GET", 
                          async: false, 
                          success : function(data) {
                          	console.log(data);
                          	x.eq(3).html(data.NAME);
                          	x.eq(4).html(data.CODE);
                          }
           });
		});
	</script>
</html>