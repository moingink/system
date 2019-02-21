<%@page import="com.yonyou.util.theme.ThemePath"%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<%
	String ContextPath =request.getContextPath();
%>

<html lang="zh-cn">
	<head>
		<meta charset="utf-8">
		<title>历史版本</title>
	</head>
	<body id="body_div">

		<form class="form-horizontal">
			<div class="panel panel-primary">
		
				<div class="panel-body" id="bulidPage">
					<div class="panel panel-primary">
						<div id="tog_titleName" class="panel-heading">
						</div>
						<div class="panel-body">
							<div class="alert alert-info" id="message" style="display: none"></div>
							<div class="form-group">
								<div class="col-md-12">
									<div id="errors"></div>
								</div>
							</div>
							<!-- 维护页面 -->
							<div class="panel-body" id="insPage">
							
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
		//是否需要加载后台数据
		var state = '<%=request.getParameter("state")%>';
		//功能元数据编码 
		var pageCode = '<%=request.getParameter("pageCode")%>';
		var roleId = '<%=request.getParameter("roleId")%>';
		var type = '<%=request.getParameter("type")%>';
		if(type=="1"){
			pageCode="PROJ_RELEASED_NLXM_HISTORY";
		}else{
			location.href="released_history_kh.jsp?pageCode=PROJ_RELEASED_HISTORY&ParentPKValue="+ParentPKValue;
			pageCode="PROJ_RELEASED_HISTORY";
		}
		
		var req_affix = ""; //需求附件
        var pro_affix = "";//建议书附件
         $.ajax({  
              url : "/system/project/getManagerInfoByHis?proj_source_id="+ParentPKValue,  
              dataType : "json",
                          type : "GET", 
                          async: false, 
                          success : function(data) {
                          	console.log(data);
                          	if(data.length>0){
	                          //获取项目经理和责任人 需求附件和建议书附件
	                          manager = data[0].PROJ_MANAGER;
	                          manager_id = data[0].PROJ_MANAGER_ID;
	                          duty_id = data[0].DUTY_ID;
	                          duty_name = data[0].DUTY_NAME;
	                          req_affix = data[0].REQUIREMENT_AFFIX;
	                          pro_affix = data[0].AFFIX;
	                          console.log(req_affix+"="+pro_affix);
                          	}
                          }
         });  
          
		$(function() {
			bulidPage(false,true,false,true);//获取按钮页面为true是为了加载数据源对应js
			//默认为新增
			$("#"+ParentPKField).val(ParentPKValue);
			//第一次进入默认激活的页面需要手动请求下最新数据
			getRecord();
			$("#bulidPage").find("[id]").each(function(){
							$(this).attr("disabled",true);
					});
					$("#bulidPage").find("[span]").each(function(){
							$(this).attr("onclick","");
					});
					//置灰附件删除图标
					console.log('-提交后置灰删除附件-');
					console.log($('#insPage'));
					setTimeout(function(){
						$('#insPage').children().find('[id=files_a_del]').remove();
					},1000);
			
			hide(isHide);
		});
		
		function getRecord(){
			var param = "&dataSourceCode="+pageCode+"&SEARCH-ID="+ParentPKValue;
			if(typeof(specifyParam) != "undefined"){
				param += specifyParam;
			}
			var record = querySingleRecord(param);
			if(!jQuery.isEmptyObject(record)){
				//已存在记录时为修改
				$("#ins_or_up_buttontoken").val('update');
				$("#tog_titleName").html("立项下达通知书历史版本");
				$inspage.find("[id]").each(function() {			
	  				$(this).val(record[$(this).attr("id")]);
				});
			}
			
			hide(isHide);
			getFileTypeVal($("#insPage"));
			//$("#bs_code").val(dataSourceCode);
			var jsonst='[{"affixtname":"req_affix","affixname":"需求附件","affixvalue":"'+req_affix+'"},{"affixtname":"pro_affix","affixname":"建议书附件","affixvalue":"'+pro_affix+'"}]';
			//console.info(JSON.parse(jsonst))
			loadAffixList(jsonst);
		}
		
		function loadAffixList(jsonobj){
			console.info(dataSourceCode);
			// for (var i=0; i < jsonobj.length; i++) {
				// console.info(jsonobj[i])
			// };
			MultiNodeView('PROJ_RELEASED_NLXM1',jsonobj);
			{//隐藏附件上传及标题
				$("#kuangname").parent().parent().remove();
				$(".classifiedtitle").remove();
			}
		}

			/**
			* 多功能节点的附件查看 加  替换
			* @param {Object} globalRef_col
			* @param {Object} ID
			*/
			function MultiNodeView(c,o) {
				var jsonParam="{\"dataSourceCode\":\""+c+"\",\"_OPPORTUNITY_CODE\":"+o+"}";
				$.ajax({
				url : "/system/getaffixs/getnodeafs",
				dataType : "json",  
				type : "POST",
				data:{"jsonData": jsonParam },
				success : function(data) {
					console.info("getnodeafs{}");
					console.info(data);
			//  	if (data.pre_assessment_business_number.length==0 && data.bus_win_bid_result_business_id.length==0 && data.bus_contract_admin_business_id.length==0) {
			//  		console.info("所选上商机编号下无任何附件");//此商机前置业务未上传过任何附件
			//  		deleteLoadAffixJsp();
			//  		return;
			//		}
			  	//data=null;
			  	if(data!=null){
			  		initLoadAffixJsp();
			  		showAffixLoadAdd();
			  		var jsda=JSON.stringify(data);
			  		setTimeout("iframeFunctionTriggert('"+jsda+"')",100);
			  	}else{
			  		deleteLoadAffixJsp();
			  	}
			  },
			  error: function (e) {
			      alert("附件数据加载失败！");
			      console.info(e)
			  }
				});
			}
		/*闫肃20181025新增end*/
	</script>
</html>