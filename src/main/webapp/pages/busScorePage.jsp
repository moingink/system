<%@page import="com.yonyou.util.RmIdFactory"%>
<%@page import="com.yonyou.util.theme.ThemePath"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<%
	String ContextPath =request.getContextPath();
%>

<html lang="zh-cn">
	<head>
		<meta charset="utf-8">
		<title>页签模板</title>
	</head>
	<body id="body_div">
		<form class="form-horizontal">
			<div class="panel panel-primary">
				<div class="panel-body" id="bulidPage">
					<div class="panel panel-primary">
						<div id="tog_titleName" class="panel-heading">评分</div>
						<div class="panel-body">
							<div class="alert alert-info" id="message" style="display: none;"></div>
							<div>
								<button id="ins_or_up_buttontoken" type="button" class="btn btn-success" onclick="bc(this)">
									<span class="glyphicon glyphicon-saved" aria-hidden="true"></span>保存
								</button>
								<button id="fh" type="button" class="btn btn-inverse" onclick="javascript:history.back(-1);">
									<span class="glyphicon glyphicon-saved" aria-hidden="true"></span>返回
								</button>
							</div>
							<div class="form-group">
								<div class="col-md-12">
									<div id="errors"></div>
								</div>
							</div>
							<!-- 维护页面 -->
							<div class="panel-body" id="insPage"></div>
							<div align="center">
								<div id="scoreDiv" class="fixed-table-container">
									<div class="fixed-table-header" style="margin-right: 0px;">
										<center><h4 id="mc"></h4></center>
										<table class="table table-hover">
											<thead id="scoreHhead"></thead>
											<tbody id="scoreInsPage"></tbody>
										</table>
									</div>
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
	<script type="text/javascript">
		//主表主键在子表字段名
		var ParentPKField = '<%=request.getParameter("ParentPKField")%>';
		//主表主键值
		var ParentPKValue = '<%=request.getParameter("ParentPKValue")%>';
		//主表数据源编码
		var pageCode = '<%=request.getParameter("pageCode")%>';
		//评分数据源编码
		var scorePageCode = '<%=request.getParameter("scorePageCode")%>';
		//入口标识
		var keyWord = '<%=request.getParameter("keyWord")%>';
		
		$(function() {
			bulidPage(false,true,false,true);
			setUp();
			choiceNotEdit("#insPage");
			hideFile($('#insPage'));
			$("#mc").text(keyWord);
		});
		
		function getRecord(){
			var param = "&dataSourceCode="+pageCode+"&SEARCH-ID="+ParentPKValue;
			var record = querySingleRecord(param);
			return record;
		}
		
		function setUp(){
			var record = getRecord();
			if(!jQuery.isEmptyObject(record)){
				$inspage.find("[id]").each(function() {			
	  				$(this).val(record[$(this).attr("id")]);
				});
			}
		}
		
		
		$(function() {
			var data1 = new Object();
			$.ajax({
				type: "GET",
				url: "/system/project?cmd=find_modifyExecute&dataSourceCode="+scorePageCode+"&ParentPKField="+ParentPKField+"&ParentPKValue="+ParentPKValue,
				async: false,
				dataType: "json",
				success: function(data) {
					data1 = data;
				}
			});
			var scoreHheadHtml = "";
			if(keyWord == "客户战略重大项目评分"){
				scoreHheadHtml = '<tr class="info">'+
									'<th style="display: none;"></th>'+
									'<th style="display: none;"></th>'+
									'<th style="text-align: center; vertical-align: middle; " tabindex="0">'+
										'<div class="th-inner ">评分项目</div>'+
										'<div class="fht-cell"></div>'+
									'</th>'+
									'<th style="text-align: center; vertical-align: middle; " tabindex="0">'+
										'<div class="th-inner ">评分选择项</div>'+
										'<div class="fht-cell"></div>'+
									'</th>'+
									'<th style="text-align: center; vertical-align: middle; " tabindex="0">'+
										'<div class="th-inner ">分值</div>'+
										'<div class="fht-cell"></div>'+
									'</th>'+
									'<th style="text-align: center; vertical-align: middle; " tabindex="0">'+
										'<div class="th-inner ">评分说明</div>'+
										'<div class="fht-cell"></div>'+
									'</th>'+
								'</tr>';
				$("#scoreHhead").html(scoreHheadHtml);

				var data2 = new Object();
				$.ajax({
					type: "GET",
					url: "/system/project?cmd=find_modifyExecute&dataSourceCode=PROJECT_CLASSIFICATION_STANDARD&ParentPKField=1&ParentPKValue=1",
					async: false,
					dataType: "json",
					success: function(data) {
						data2 = data;
					}
				});
				
				var index = 0
				for(var i=0;i<data1.length;i++){
					index++;
					var id = data1[i]["ID"];
					var busBusExamineId = ParentPKValue;
					var cusLevelId = data1[i]["CUS_LEVEL_ID"];
					var cusLevel = data1[i]["CUS_LEVEL"];
					var fraction = data1[i]["FRACTION"];
					var scoreExplain = data1[i]["SCORE_EXPLAIN"];
					var scoreChoiceItem = data1[i]["SCORE_CHOICE_ITEM"];
					var html = '<tr>'+
									'<td style="display: none;">'+
										'<input type="TEXT" id="ID" class="form-control" value="'+id+'">'+						
									'</td>'+
									'<td style="display: none;">'+
										'<input type="TEXT" id="BUS_BUS_EXAMINE_ID" class="form-control" value="'+busBusExamineId+'">'+						
									'</td>'+
									'<td style="display: none;">'+
										'<input type="TEXT" id="CUS_LEVEL_ID" class="form-control" value="'+cusLevelId+'">'+
									'</td>'+
									'<td style="text-align: center; vertical-align: middle;">'+
										'<input type="TEXT" id="CUS_LEVEL" class="form-control" value="'+cusLevel+'" disabled="true">'+
									'</td>'+
									'<td style="text-align: center; vertical-align: middle; ">'+
										'<select class="form-control" id="SCORE_CHOICE_ITEM'+index+'" onChange="getFraction(\''+index+'\',this.value)">'+
											'<option value="0">==请选择==</option>';
					var data3 = new Object();
					$.ajax({
						type: "GET",
						url: "/system/project?cmd=find_modifyExecute&dataSourceCode=GRADING_STANDARD&ParentPKField=PCS_ID&ParentPKValue="+cusLevelId,
						async: false,
						dataType: "json",
						success: function(data) {
							data3 = data;
						}
					});
					for(var j=0;j<data3.length;j++){
						html += '<option value="'+data3[j]["ID"]+'">'+data3[j]["STANDARD"]+'</option>';
					}
					html += '</select></td>'+
								'<td style="text-align: center; vertical-align: middle; ">'+
									'<input type="TEXT" id="FRACTION'+index+'" class="form-control" value="'+fraction+'" disabled="true" style="text-align: center;">'+
								'</td>'+
								'<td style="text-align: center; vertical-align: middle; ">'+
									'<input type="TEXT" id="SCORE_EXPLAIN" class="form-control" value="'+scoreExplain+'">'+
								'</td>'+
							'</tr>';
					$("#scoreInsPage").append(html);
					$("#SCORE_CHOICE_ITEM"+index).val(scoreChoiceItem);
					if(cusLevel == "行业影响力" || cusLevel == "示范效应"){
						$("#FRACTION"+index).removeAttr("disabled");
						$("#FRACTION"+index).attr("onkeyup","num(this.id,this.value),sum("+index+")");
					}
				}
				
				for(var i=0;i<data2.length;i++){
					var flag = true;
					for(var j=0;j<data1.length;j++){
						if(data2[i]["ID"] == data1[j]["CUS_LEVEL_ID"]){
							flag = false;
							break;
						}
					}
					if(flag == true){
						index++;
						var busBusExamineId = ParentPKValue;
						var cusLevelId = data2[i]["ID"];
						var cusLevel = data2[i]["GRADING_PROJECT"];
						html = '<tr>'+
										'<td style="display: none;">'+
											'<input type="TEXT" id="ID" class="form-control" value="">'+						
										'</td>'+
										'<td style="display: none;">'+
											'<input type="TEXT" id="BUS_BUS_EXAMINE_ID" class="form-control" value="'+busBusExamineId+'">'+						
										'</td>'+
										'<td style="display: none;">'+
											'<input type="TEXT" id="CUS_LEVEL_ID" class="form-control" value="'+cusLevelId+'">'+
										'</td>'+
										'<td style="text-align: center; vertical-align: middle;">'+
											'<input type="TEXT" id="CUS_LEVEL" class="form-control" value="'+cusLevel+'" disabled="true">'+
										'</td>'+
										'<td style="text-align: center; vertical-align: middle; ">'+
											'<select class="form-control" id="SCORE_CHOICE_ITEM'+index+'" onchange="getFraction(\''+index+'\',this.value)">'+
												'<option value="0">==请选择==</option>';
						var data3 = new Object();
						$.ajax({
							type: "GET",
							url: "/system/project?cmd=find_modifyExecute&dataSourceCode=GRADING_STANDARD&ParentPKField=PCS_ID&ParentPKValue="+cusLevelId,
							async: false,
							dataType: "json",
							success: function(data) {
								data3 = data;
							}
						});
						for(var j=0;j<data3.length;j++){
							html += '<option value="'+data3[j]["ID"]+'">'+data3[j]["STANDARD"]+'</option>';
						}
						html +=	'</select></td><td style="text-align: center; vertical-align: middle; ">'+
										'<input type="TEXT" id="FRACTION'+index+'" class="form-control" disabled="true" style="text-align: center;">'+
									'</td>'+
									'<td style="text-align: center; vertical-align: middle; ">'+
										'<input type="TEXT" id="SCORE_EXPLAIN" class="form-control">'+
									'</td>'+
								'</tr>';
						$("#scoreInsPage").append(html);
						if(cusLevel == "行业影响力" || cusLevel == "示范效应"){
							$("#FRACTION"+index).removeAttr("disabled");
							$("#FRACTION"+index).attr("onkeyup","num(this.id,this.value),sum("+index+")");
						}
					}
				}
				var record = getRecord();
				
				var tfootHtml = '<tr><td style="text-align: center; vertical-align: middle;">'+
									'<input type="TEXT" class="form-control" value="总得分" disabled="true">'+
								'</td>'+
								'<td style="text-align: center; vertical-align: middle;" colspan="3">'+
									'<input type="TEXT" id="totalScore" class="form-control" value="'+record["TOTAL_SCORE"]+'" disabled="true" style="text-align: center;">'+
								'</td></tr>';
				$("#scoreInsPage").append(tfootHtml);
			}
			setParntHeigth($(document.body).height());
			hide(isHide);
		});
		
		function getDividingValue(index,id){
			var data3 = new Object();
			$.ajax({
				type: "GET",
				url: "/system/project?cmd=find_modifyExecute&dataSourceCode=GRADING_STANDARD&ParentPKField=ID&ParentPKValue="+id,
				async: false,
				dataType: "json",
				success: function(data) {
					data3 = data;
				}
			});
			return data3;
		}
		
		//获取分值
		function getFraction(index,id){
			if(id == "0"){
				$("#FRACTION"+index).val(0);
			}else{
				var data4 = getDividingValue(index,id);
				$("#FRACTION"+index).val(data4[0]["DIVIDING_VALUE"]);
			}
			setTimeout("sum(null)",1);
		}
		
		//计算总分
		function sum(index){
			var sum = 0;
			$("input[id^='FRACTION']").each(function(){
				if($(this).val() != "" && $(this).val() != null){
					sum += Number($(this).val());
				}else{
					sum += Number(0);
				}
        	});
        	var result = sum.toString().indexOf(".");
        	if(result != -1) {
		        var length = sum.toString().split(".")[1].length;
		        if(length>=3){
		        	sum = sum.toFixed(2);
		        }
		    }
        	$("#totalScore").val(sum);
        	setTimeout(function(){
                   if(index != null && index != ""){
						var score_choice_item = $("#SCORE_CHOICE_ITEM"+index).val();
						if(score_choice_item != null && score_choice_item != "" && score_choice_item != 0){
							var data4 = getDividingValue(index,score_choice_item);
							var fraction = $("#FRACTION"+index).val();
							if(Number(fraction) > Number(data4[0]["DIVIDING_VALUE"])){
								oTable.showModal('提示', '不可大于评分选择项预计分值');
								return false;
							}
						}else{
							oTable.showModal('提示', '请选择评分选择项');
							$("#FRACTION"+index).val("0");
							return false;
						}
					}
		        	if(Number(sum)>100){
		        		oTable.showModal('提示', '总分不可大于100');
						return false;
		        	} 
            }, 1)
		}
		
		function saveVerification(){
			var flag = true;
			var sum = $("#totalScore").val();
			$("input[id^='FRACTION']").each(function(){
				var cus_level = $(this).parent().prev().prev().find("input:eq(0)").val();
				if(cus_level == "行业影响力" || cus_level == "示范效应"){
					var id = $(this).attr('id');
					var xiabiao = id.indexOf("N");
					var index = id.substring(xiabiao+1,id.length);
					if(index != null && index != ""){
						var score_choice_item = $("#SCORE_CHOICE_ITEM"+index).val();
						if(score_choice_item != null && score_choice_item != "" && score_choice_item != 0){
							var data4 = getDividingValue(index,score_choice_item);
							var fraction = $("#FRACTION"+index).val();
							if(Number(fraction) > Number(data4[0]["DIVIDING_VALUE"])){
								oTable.showModal('提示', cus_level+'列---不可大于评分选择项预计分值');
								flag = false;
								return false;
							}
						}else{
							oTable.showModal('提示', cus_level+'列---请选择评分选择项');
							$("#FRACTION"+index).val("0");
							flag = false;
							return false;
						}
					}
				}
        	});
        	
        	var totalScore = $('#totalScore').val();
        	if(totalScore == '' || totalScore == '0'){
        		oTable.showModal('提示', '总分不可小于或等于0');
				return false;
        	}else if(flag == false){
        		return false;
        	}else{
        		if(Number(sum)>100){
        			oTable.showModal('提示', '总分不可大于100');
					return false;
	        	}
        		return true;
        	}
        	
		}
		
		function getJson(table){
			var json = "[";
			if(keyWord == "客户战略重大项目评分"){
				for(var i = 0;i<table.length-1;i++){
					var ID = $("#scoreInsPage tr:eq("+i+")").children().eq(0).find("input").val();
					json += "{\"ID\":\""+ID+"\",";
					
					var BUS_BUS_EXAMINE_ID = $("#scoreInsPage tr:eq("+i+")").children().eq(1).find("input").val();
					json +="\"BUS_BUS_EXAMINE_ID\":\""+BUS_BUS_EXAMINE_ID+"\",";
					
					var CUS_LEVEL_ID = $("#scoreInsPage tr:eq("+i+")").children().eq(2).find("input").val();
					json +="\"CUS_LEVEL_ID\":\""+CUS_LEVEL_ID+"\",";
					
					var CUS_LEVEL = $("#scoreInsPage tr:eq("+i+")").children().eq(3).find("input").val();
					json +="\"CUS_LEVEL\":\""+CUS_LEVEL+"\",";
					
					var SCORE_CHOICE_ITEM = $("#scoreInsPage tr:eq("+i+")").children().eq(4).find("select").val();
					json +="\"SCORE_CHOICE_ITEM\":\""+SCORE_CHOICE_ITEM+"\",";
					
					var FRACTION = $("#scoreInsPage tr:eq("+i+")").children().eq(5).find("input").val();
					json +="\"FRACTION\":\""+FRACTION+"\",";
					
					var SCORE_EXPLAIN = $("#scoreInsPage tr:eq("+i+")").children().eq(6).find("input").val();
					json +="\"SCORE_EXPLAIN\":\""+SCORE_EXPLAIN+"\"},";
				}
			}
			json = json.substring(0,json.length-1)+"]";
			return json;
		}
		
		//保存
		function bc(t){
			var table = $('#scoreInsPage tr');
			if(keyWord == "客户战略重大项目评分"){
				if(saveVerification() == true){
					var TOTAL_SCORE = $("#totalScore").val();
					var BUS_TYPE = '';
					var record = new Object;
					$.ajax({
						type: "GET",
						url: "/system/project?cmd=find_modifyExecute&dataSourceCode=PROJECT_CLASSIFICATION_INTERVAL&ParentPKField=1&ParentPKValue=1",
						async: false,
						dataType: "json",
						success: function(data) {
							record = data;
						}
					});
					if(!jQuery.isEmptyObject(record)){
						for(var i=0;i<record.length;i++){
							console.log(Number(TOTAL_SCORE)+"--"+Number(record[i]["LOWER_LIMIT"])+"---"+Number(record[i]["UPPER_LIMIT"]));
							if(Number(TOTAL_SCORE) >= Number(record[i]["LOWER_LIMIT"]) && Number(TOTAL_SCORE) <= Number(record[i]["UPPER_LIMIT"])){
								BUS_TYPE = record[i]["PROJECT_TYPE"];
								break;
							}
						}
					}
					var ACTUAL_PROJECT_TYPE = getCodeTypeList('OPPORTUNITY_TYPE', BUS_TYPE);
					if(BUS_TYPE != '' && BUS_TYPE != null){
						if (table.length-1 >= 1) {
							var jsonobj = eval(getJson(table));//转换为JSON对象
							for(var i=0;i<jsonobj.length;i++){
								var buttonToken = 'add';
								if(jsonobj[i]["ID"] !=null && jsonobj[i]["ID"] !=""){
									buttonToken = 'update';
								}
								var jsonString = JSON.stringify(jsonobj[i]);
								transToServer(findBusUrlByButtonTonken(buttonToken,'','BUS_BUS_SCORE'),jsonString);
							}
						}
						var json1 = "{\"ID\":\""+ParentPKValue+"\",\"TOTAL_SCORE\":\""+TOTAL_SCORE+"\",\"BUS_TYPE\":\""+BUS_TYPE+"\",\"ACTUAL_PROJECT_TYPE\":\""+ACTUAL_PROJECT_TYPE+"\"}";
						transToServer(findBusUrlByButtonTonken('update','&OPERATION=BUS_BUS_SCORE','BUS_BUSINESS_MESSAGE'), json1);//更新商机信息字段
						
						var json1 = "{\"ID\":\""+ParentPKValue+"\",\"TOTAL_SCORE\":\""+TOTAL_SCORE+"\",\"ACTUAL_PROJECT_TYPE\":\""+ACTUAL_PROJECT_TYPE+"\"}";
						transToServer(findBusUrlByButtonTonken('update','','BUS_BUS_EXAMINE'), json1);//更新商机申请字段
						oTable.showModal('提示', "保存成功,根据评分给项目定级为:"+ACTUAL_PROJECT_TYPE);
						$("#ins_or_up_buttontoken").attr("disabled",true);
					}else{
						oTable.showModal('提示', "保存失败,该评分没有找到对应的分级区间");
					}
				}
			}
		}
		
		function getCodeTypeList(typeKeyword, key){
			var codeTypeList = new Object();
			$.ajax({
				type:'GET',
				url:'/system/business/getCodeType',
				data:{'typeKeyword':typeKeyword},
				dataType:'json',
				async:false,
				success:function(data){
					codeTypeList = data;
				}
			});
			return codeTypeList[key];
		}
		
		//输入分值不可超过两位小数
		function num(id,val){
			$("#"+id).val(val.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3'));//只能输入两个小数
		}
	</script>
</html>