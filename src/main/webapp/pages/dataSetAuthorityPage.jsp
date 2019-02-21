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
		<title>数据授权</title>
	<style type="text/css">
		.classifiedtitle{
			color: #31708f;
    		background-color: #d9edf7;
    		border-color: #bce8f1;
   			padding: 10px 15px;
    		border-radius: 3px;
    		font-weight: bold;
    		margin-top: 10px;
		}
	</style>
	</head>
	<body id="body_div">

		<form class="form-horizontal">
			<div class="panel panel-primary">
		
				<div class="panel-body" id="bulidPage"  >
					<div class="panel panel-primary">
						<div id="tog_titleName" class="panel-heading">
							设置 
						</div>
						<div class="panel-body" style="border-color: blue; border: 1px solid blue; border-radius: 4px;">
							<div class="alert alert-info" id="message" style="display: none"></div>
							<div>
								<button id="ins_or_up_buttontoken" type="button" class="btn btn-success" onclick="save(this)">
									<span class="glyphicon glyphicon-saved" aria-hidden="true"></span>保存
								</button>
								<button id="back" type="button" class="btn btn-inverse" onclick="javascript:history.back(-1);">
									<span class="glyphicon glyphicon-saved" aria-hidden="true"></span>返回
								</button>
								<button id="fh" type="button" class="btn btn-inverse" onclick="back(this)"  style="visibility: hidden">
									<span class="glyphicon glyphicon-ban-circle" aria-hidden="true"></span>返回
								</button>
							</div>
							<div class="form-group">
								<div class="col-md-12">
									<div id="errors"></div>
								</div>
							</div>
							<!-- 维护页面 -->
<div class="panel-body" id="insPage">
	<div class="jq22-container">	
	  <div class="container">
      <div class="row" style="height: 160px; position: relative; z-index:99;">
        <h2></h2>
        <div class="col-md-6" style="position:absolute;left:0;">
          <h2>角色用户列表</h2>
          <div id="treeview-selectable" class="" ></div>
        </div>
        <div class="col-md-6" style="position:absolute;right:0;margin-top:60px">
          <div class="col-md-9">
          	<div class="form-group">
          		<div class="col-md-4"><h4>用户搜索</h4></div>
          		<div class="col-md-8">
          			<label for="input-select-node" class="sr-only">用户搜索:</label>
           			 <input type="input" class="form-control" id="input-select-node" placeholder="模糊查询..." value="" />
          		</div>
          </div>
         </div>  
         <div class="col-md-3">
              <button type="button" class="btn btn-success select-node" id="btn-select-node">查找</button>
         </div>     
        </div>
      </div>
     </div>
	</div>
	<div class="col-md-12 classifiedtitle">分配权限</div>
	
	<div class="col-md-12" class="col-md-12" style="border-top:1px solid #EEEEEE; margin-top:20px;">
		<div class="col-md-6" style="margin-bottom:20px;margin-top:20px">
			<div class="form-group">
				<div class="col-md-2" style="margin-top:7px">
					<input type="checkbox" id="selectpersion" onclick="checkRoleAuth('b')"/>用户:
				</div>
				<div class="col-md-4">
					<div class="input-group">
				<span>分配用户</span>
					<span class="btn btn-info" style="cursor:pointer;" onclick="checkReference(this,'REF(RM_USER_REF,NAME:MANAGER;ID:MANAGER_ID,0)','MANAGER','INSUP')">
						<span class="glyphicon glyphicon-search"></span>
					</span>
				</div>
				</div>
			</div>		
		</div>
		<div class="col-md-6" style="margin-bottom:55px;margin-top:20px">
			
		</div>
	
	</div>
	
	<div class="col-md-12">
		<div class="col-md-6">
			<div class="fixed-table-container" style="min-height: 200px;">
		<!-- 	<div class="fixed-table-header" style="margin-right: 0px;">
				<table class="table table-hover" style="width: 792px;">
					<thead></thead>
				</table>
			</div> -->
			<div class="fixed-table-body11" style="">
				<div class="fixed-table-loading" style="top: 41px; display: none;">请稍等，正在加载中...</div>
			<table id="table" data-row-style="rowStyle" class="table table-hover">
			 <thead>
				<tr class="info">
						<th style="text-align: center; vertical-align: middle; " data-field="PARENT_PARTY_CODE" tabindex="0">
							<div class="th-inner ">登录名</div>
							<div class="fht-cell" style="width: 120px;"></div>
						</th>
						<th style="text-align: center; vertical-align: middle; " data-field="PARENT_PARTY_NAME" tabindex="0">
							<div class="th-inner ">用户名</div>
							<div class="fht-cell" style="width: 104px;"></div>
						</th>
						<th style="text-align: center; vertical-align: middle; " data-field="PARENT_PARTY_NAME" tabindex="0">
							<div class="th-inner ">操作</div>
							<div class="fht-cell" style="width: 104px;"></div>
						</th>
				</tr>
			 </thead>
			 <tbody id="users"></tbody>
			</table>
			
			</div>
			<div class="fixed-table-footer" style="display: none;"></div>
			
			</div>
		</div>
		<div class="col-md-6">
			<div>说明：</div>
			<p>1、用户权限指当前所选角色或用户能够对其他哪些用户创建的业务或单据拥有查询和操作权限；</p>
			<p>2、勾选“<input type="checkbox" disabled="disabled"/>用户”，当前所选角色或用户拥有自己创建的业务或单据的查询和操作权限；</p>
			<p>3、点击“<span class="glyphicon glyphicon-search btn btn-info" disabled="disabled"></span>”可分配用户，当前所选角色或用户拥有已分配用户创建的业务或单据的查询和操作权限；</p>
		</div>
	</div>
	
	


	<div class="col-md-12" style="border-top:1px solid #EEEEEE; margin-top:20px;">
		<div class="col-md-6">
			<div style="padding-top:10px;padding-bottom:10px;">
				<input type="checkbox" id="selectcompany" name="selectcompany" onclick="checkRoleAuth('b')"/>部门:
			</div>			
			<!-- 公司checkTree-->
			<div style="">
				<div id="company" style="border-color: blue; border: 1px solid blue; border-radius: 4px; overflow-y:auto;  height:200px;"></div>			 
			</div>
		
		</div>
		<div class="col-md-6" style="margin-top:40px;">
			<div>说明：</div>
			<p>1、部门权限指当前所选角色或用户能够对其他哪些部门下所有用户创建的业务或单据拥有查询和操作权限；</p>
			<p>2、勾选“<input type="checkbox" disabled="disabled"/>部门”，当前所选角色或用户拥有自己所属部门下所有用户创建的业务或单据的查询和操作权限；</p>
			<p>3、部门列表中可勾选分配部门，当前所选角色或用户拥有已分配的部门下所有用户创建的业务或单据的查询和操作权限；</p>
		</div>
		
	</div>
	
	<div class="col-md-12" style="border-top:1px solid #EEEEEE; margin-top:20px;">
		<div class="col-md-6" style="margin-top:10px;">
			<input type="checkbox" id="selectgolbal"  onclick="checkRoleAuth('a')"/>全局:
		</div>
		<div class="col-md-6" style="margin-top:10px;">
			<div>说明：</div>
			<p>1、全局权限指当前所选角色或用户能够对所有部门下所有用户创建的业务或单据拥有查询和操作权限；</p>
			<p>2、勾选“全局”，当前所选角色或用户拥有所有部门下所有用户创建的业务或单据的查询和操作权限；</p>
		</div>
	</div>
	<div class="col-md-12">
		<div class="col-md-8">
			<input type="hidden" class="form-control" id="MANAGER_ID" name="MANAGER_ID" value="" data-bv-field="MANAGER_ID"><i class="form-control-feedback" data-bv-icon-for="MANAGER_ID" style="display: none;"></i>
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
	<!-- <link href="http://www.jq22.com/jquery/bootstrap-3.3.4.css" rel="stylesheet"> -->
	<style type="text/css">
		.jq22-header{margin-bottom: 15px;font-family: "Segoe UI", "Lucida Grande", Helvetica, Arial, "Microsoft YaHei", FreeSans, Arimo, "Droid Sans", "wenquanyi micro hei", "Hiragino Sans GB", "Hiragino Sans GB W3", "FontAwesome", sans-serif;}
		.jq22-icon{color: #fff;}
	</style>
	<script src="../js/bootstrap-treeview.js"></script>
	<script type="text/javascript">
		//主表主键在子表字段名
		var ParentPKField = '<%=request.getParameter("ParentPKField")%>';
		//主表主键值
		var ParentPKValue = '<%=request.getParameter("ParentPKValue")%>';
		var pageCode = '<%=request.getParameter("pageCode")%>';
		//是否需要加载后台数据
		var state = '<%=request.getParameter("state")%>';
		var projCode = '';
		var projName='';
		var roleId = '<%=request.getParameter("roleId")%>';
		var selectRoleId="";
		var selectUserId="";
		var roleName = '<%=request.getParameter("roleName")%>';
		var cost ='';
		var bill_statues ="";
		var button ="add";
		var uname = '<%=request.getParameter("userName")%>';
		var comname = '<%=request.getParameter("companyName")%>';
		var defaultData=[];
		var isSetRoleDataAuth=false;
		var tx = "";
		//获取所有公司
		//获取角色所关联的个人或公司数据权限回显
		var but_auth='<input type="checkbox" checked="checked" name="persion" valeu="search"/>查询<input type="checkbox" name="persion" checked="checked" value="update"/>修改<input type="checkbox" name="persion" valeu="add"  checked="checked" />新增<input type="checkbox" name="persion" checked="checked" valeu="delete"/>删除';
		loadCompany();
		function loadCompany(){
			$.ajax({  
	              url : "/system/project/companyList?roleid=",  
	              dataType : "json",
	                          type : "GET", 
	                          async: false, 
	                          success : function(data) {
	                          	var html =""; 
	                          	if(data.length>0){
	                          		for(var i=0;i<data.length;i++){
		                          		html+='<div><input type="checkbox" value="'+data[i].ID+'" id= "'+data[i].ID+'"  name="company"/>'+data[i].NAME+'</div>'
	                          		}
	                          		$('#company').html(html);
	                          	}
	                          }
	         });
		}
            //获取角色用户列表
            $.ajax({  
              url : "/system/project/roleUserList?roleId="+roleId+"&roleName="+roleName,  
              dataType : "json",
                          type : "GET", 
                          async: false, 
                          success : function(data) {
                          	if(data.length>0){
                          		defaultData = data;
                          	}
                          }
            });
             $.ajax({  
              url : "/system/project/roleDataAuth?roleId="+roleId,  
              dataType : "json",
                          type : "GET", 
                          async: false, 
                          success : function(data) {
                          	if(data==1){
                          		//可以直接设置用户数据权限保存
                          		isSetRoleDataAuth = true;
                          	}
                          }
            });
		$(function() {
			//bulidPage(false,true,false,true);//获取按钮页面为true是为了加载数据源对应js
			//默认为新增
		});
		function checkRoleAuth(t){
			if(t=="a"){
				$(":checkbox[id='selectcompany']").removeProp("checked");
				$(":checkbox[id='selectpersion']").removeProp("checked");
			}else{
				$(":checkbox[id='selectgolbal']").removeProp("checked");
			}
		
			//判断选择的是否
			if(isSetRoleDataAuth==false){
				if(nodeId!=0){
					alert('首次必须先设置角色权限');
				}
			}
		}
		var json=[];
		var lsjson=[];
		function ref_write_json(rejsonArray){
			//优先储存之前 去重  显示
			//alert(JSON.stringify(rejsonArray));
			var tr ='<tr data-index="1">';
			var td = '<td style="text-align: center; vertical-align: middle; ">';	
			var body = "";
			for(var i =0;i<rejsonArray.length;i++){
				var boo = 0;
				for(var j=0;j<json.length;j++){
					if(json[j].ID==rejsonArray[i].ID){
						boo=1;
					}
				}
				if(boo==0){
					body+='<tr data-index="1" name="user" value="'+rejsonArray[i].ID+'">'+'<td style="display: none">'+rejsonArray[i].ID+'</td>'+td+rejsonArray[i].LOGIN_ID+"</td>"+td+rejsonArray[i].NAME+"</td>"+td+'<input type="button" onclick="del(this,\''+rejsonArray[i].ID+'\')"  value="删除"></td>'+'</tr>';
				 	var length = lsjson.length;
					lsjson[length]=rejsonArray[i];
				}
			}
			json = lsjson;
			if(tx=="replace"){
				$('#users').html(body);
				json =[];
				tx="";
			}else{
				$('#users').append(body);
			}
			return false;
		}
		function del(obj,id){
			var tr=obj.parentNode.parentNode;
			tr.parentNode.removeChild(tr);
			//移除临时json 数据
			for(var i =0;i<json.length;i++){
				if(json[i].ID==id){
					json[i]="";
				}
			}
		}
		//var bill_statue=0;
		function getRecord(){
			var param = "&dataSourceCode="+dataSourceCode+"&SEARCH-"+ParentPKField+"="+ParentPKValue;
			if(typeof(specifyParam) != "undefined"){
				param += specifyParam;
			}
			var record = querySingleRecord(param);
			$('#back').removeAttr("disabled");
			//设置回显字段
			//
			$("#OPPORTUNITY_CODE").val(projCode);
			//
			$("#PROJ_NAME").val(projName);
			$("#COST_ESTIMATE").val(cost);
			if(pageCode=="PROJ_RELEASED_NLXM1"){
				//设置项目类型 和 需求编号到项目下达表中
				$("#PROJ_TYPE").val('1');
				$("#BID_BUSS_NO").val(ParentPKValue);
				$("#PROJ_CODE").attr("disabled", true);
			}else if(pageCode=="PROJ_PROPOSAL_NL"){
				$("#PROJ_TYPE").val('1');
			}else{
				$("#PROJ_TYPE").val('0');
			}
			if(!jQuery.isEmptyObject(record)){
				//已存在记录时为修改
				button ="";
				$("#ins_or_up_buttontoken").val('update');
				$("#tog_titleName").html("修改");
				$inspage.find("[id]").each(function() {			
	  				$(this).val(record[$(this).attr("id")]);
				});
				bill_statues = $("#BILL_STATUS").val();
			}
			if(bill_statues !="" ){
				if(bill_statues!="7" && bill_statues !="0"){
					console.log("bill_statue="+bill_statues);
					console.log('不可编辑')
					$("#bulidPage").find("[id]").each(function(){
							$(this).attr("disabled",true);
					});
					$("#bulidPage").find("[span]").each(function(){
							$(this).attr("onclick","");
					});
				}
			}
			hide(isHide);//隐藏及置灰
			$("#back").removeAttr("disabled");
		}
		
		//重写方法，保存后不清空输入框而是设为只读，保存按钮置灰
		function savaByQuery(t,_dataSourceCode,$div){
			console.log($div);
			var message ="";
			var buttonToken=$("#ins_or_up_buttontoken").val();
			if(pageCode=="PROJ_RELEASED_NLXM1"){
				if(button =="add"){
					$("#ID").val('<%= RmIdFactory.requestId("PROJ_RELEASED", 1)[0]%>');
					buttonToken ="insertReleasedNL";
				}
				message = transToServer(findBusUrlByButtonTonken(buttonToken,'&table=PROJ_RELEASED',_dataSourceCode),getJson($div));
			}else{
				message = transToServer(findBusUrlByButtonTonken(buttonToken,'',_dataSourceCode),getJson($div));
			}
			
			oTable.showModal('modal', message);
			if(pageCode=="PROJ_RELEASED_NLXM1"){
				if(message=="保存成功"){
					location.reload();
				}
			}
			setReadonlyByDiv($("#insPage input[type='text']"));
			$("#ins_or_up_buttontoken").attr("disabled", true);
		}
		//保存获取选择的角色或者用户  获取个人 table数据和 勾选公司json
		//以哪种方式进行保存
		var persion = [];
		var company = [];
		function save(){
			company = [];
			persion = [];
			if(isSetRoleDataAuth==false){
				if(nodeId!=0){
					alert('保存失败,请先选择"-角色-"设置权限');
					return false;
				}
			}
			//首先 获取个人json数据
			var tableInfo = "";
 			var tableObj = document.getElementById("users");
 			for (var i = 0; i < tableObj.rows.length; i++) { //遍历Table的所有Row
 				//tableInfo = tableObj.rows[i].cells[0].innerText;
 				var json = {};
	 			//for (var j = 0; j < tableObj.rows[i].cells.length; j++) { 
	 				//遍历Row中的每一列
				   json.ID = tableObj.rows[i].cells[0].innerText; //获取Table中单元格的内容
				   json.LOGIN_ID = tableObj.rows[i].cells[1].innerText;
				   json.NAME = tableObj.rows[i].cells[2].innerText;
	  			//}
	  			persion.push(json);
 			}
 			console.log("userList="+JSON.stringify(persion));
 		 	//获取公司所选择的json数据
 		 	var adIds = "";
	       $('#company').find(':checkbox').each(function(){
			  
			  if ($(this).is(":checked")) {
			    //操作
			    var json = {};
 				json.id = $(this).val();
 				company.push(json);
			  }
			});
			console.log("companylist="+JSON.stringify(company))
			//获取角色id和用户id 如果无用户id则只保存角色权限表,否则同时保存字表 用户子表权限
			if(selectRoleId==""&&selectUserId==""){
				alert("必须选择角色或用户");
				return false;
			}
			//保存后台  获取个人json   公司json  是否全局    是否个人  是否公司   角色id 角色id 用户id 返回后台保存
			var roleAuth = {};
			roleAuth.RM_ROLE_ID = selectRoleId;
			roleAuth.RM_USER_ID = selectUserId;
			//获取 个人 公司 全局 选择状态 ,默认不选中  选中全局取消个人和公司 ,否则单配个人和公司数据 
			console.log(persion+"=="+company);
			if($('#selectpersion').is(':checked')) {
				roleAuth.PERSONAL = "0";
			}else{
				roleAuth.PERSONAL = "1";
			}
			if(persion.length!=0){
				roleAuth.PERSONAL_DATA = JSON.stringify(persion);
			}else{
				roleAuth.PERSONAL_DATA = "";
			}
			
			if($('#selectcompany').is(':checked')) {
				roleAuth.COMPANY = "0";
			}else{
				roleAuth.COMPANY = "1";
			}
			if(company.length!=0){
				roleAuth.COMPANY_DATA = JSON.stringify(company);
			}else{
				roleAuth.COMPANY_DATA = "";
			}
			if($('#selectgolbal').is(':checked')) {
				//区分去角色设置还是
				if(selectUserId!=""){
					roleAuth.USER_GOLBAL = "0";
					roleAuth.GOLBAL = "1";
				}else{
					roleAuth.GOLBAL = "0";
				}
			}else{
				if(selectUserId!=""){
					roleAuth.USER_GOLBAL = "1";
				}else{
					roleAuth.GOLBAL = "1";
				}
			}
			roleAuth.DR="0";
			//调用个人保存方法 
			console.log(roleAuth);
			var dataMessage =JSON.stringify(roleAuth);
			var buttonToken = "addForRoleDataAuth";
			message = transToServer(findBusUrlByButtonTonken(buttonToken,'&table='+pageCode+"&button="+buttonToken,''),dataMessage);
			
			if(message=="保存成功"){
				isSetRoleDataAuth=true;
				persion =[];
			}
			alert(message);
		}
		//查询用户角色或者用户权限
		function getDataAuth(roleId,userId){
			//根据用户id 或者角色id 获取权限json数据返回到页面显示,没有则置空选项
			loadCompany();
			 tx="replace";
			 //初始化 三个select默认为不选择
			 $('#selectgolbal').attr("checked", false);
			 $('#selectpersion').attr("checked", false);
			 $('#selectcompany').attr("checked", false);
 			 $.ajax({
              url : "/system/project/dataAuth?roleId="+roleId+"&userId="+userId,  
              dataType : "json",
                          type : "GET", 
                          async: false, 
                          success : function(data) {
                          	//根据查询返回的公共数据 回显 是否够用
                          	console.log(roleId+"=="+userId);
                          	if(roleId != ""&& userId!= ""){
                          		//$('#selectgolbal').attr("checked", false);
                          		//取用户数据权限json回显  包含是否全局 或者个人或公司
                          		var user_personal =  null;
                          		if(data.USER_PERSONAL=="0"){
                          			$('#selectpersion').prop("checked", true);
                          		}
                          		if(data.USER_COMPANY=="0"){
                          			$('#selectcompany').prop("checked", true);
                          		}
                          		if(data.USER_PERSONAL_DATA==""){
                          		}else{
                          			user_personal =  JSON.parse(data.USER_PERSONAL_DATA);
                          		}
                          		var user_company = null;
                          		if(data.USER_COMPANY_DATA!=""){
                          			user_company = JSON.parse(data.USER_COMPANY_DATA);
                          		}
                          		//var user_company = JSON.parse(data.USER_COMPANY_DATA);
                          		//动态设置人员回显
                          		if(data.USER_GOLBAL=="0"){
                          			$('#selectgolbal').prop("checked", true);
                          		}
                          		if(user_personal!=null&&user_personal!=""){
	                          		ref_write_json(user_personal);
	                          		//$('#selectpersion').prop("checked", true);
                          		}else{
                          			user_personal =[];
                          			ref_write_json(user_personal);
                          		}
                          		//动态设置公司check  获取回显公司id集合 循环设置勾选
                          		if(user_company == null||user_company==""){
                          			user_company =[];
                          		}else{
                          			//$('#selectcompany').prop("checked", true);
                          		}
                          		//清空所有
								for(var i=0; i<user_company.length;i++){
									$('#'+user_company[i].id).prop("checked", true);
								}                          		
                          	}else{
                          		//取角色数据权限json回显 包含是否全局 或者个人或公司
                          		//先判断是否全局角色。 1全局只设置check勾选。2非全局设置 判断company 和 personal 是否有设置  有数据则调用回显方法
                          		if(data!= null){
	                          		setcheckbox(data);
	                          		var role_personal = null;
	                          		var role_company = null;
	                          		if(data.PERSONAL_DATA!=""){
	                          			role_personal =  JSON.parse(data.PERSONAL_DATA);
	                          		}
	                          		if(data.COMPANY_DATA!=""){
	                          			role_company = JSON.parse(data.COMPANY_DATA);
	                          		}
	                          		//var role_personal =  JSON.parse(data.PERSONAL_DATA);
	                          		//var role_company = JSON.parse(data.COMPANY_DATA);
	                          		//动态设置人员回显
	                          		if(role_personal!=null){
		                          		ref_write_json(role_personal);
	                          		}else{
	                          			role_personal =[];
	                          			ref_write_json(role_personal);
	                          		}
	                          		//动态设置公司check  获取回显公司id集合 循环设置勾选
	                          		if(role_company == null){
	                          			role_company =[];
	                          		}
	                          		for(var i=0; i<role_company.length;i++){
									$('#'+role_company[i].id).prop("checked", true);
								}   
                          		}
                          	}
                          }
            });
		}
		function setcheckbox(data){
								if(data.GOLBAL =="0"){
                          			//设置 全局选择checkbox   置空个人和公司所有数据
                          			$('#selectgolbal').prop("checked", true);
                          			$('#users').html("");
                          		}else{
	                          		if(data.PERSONAL=="0"){
		                          		$('#selectpersion').prop("checked", true);
		                          	}
		                          	if(data.COMPANY=="0"){
		                          		$('#selectcompany').prop("checked", true);
		                          	}
                          		}
		}
	</script>
	<script type="text/javascript">
	  var nodeId="";
	  //初始化数据 获取角色所分配用户加载出来设置差异化权限
	  $(function() {
        var $searchableTree = $('#treeview-searchable').treeview({
          data: defaultData,
      });

      var search = function(e) {
          var pattern = $('#input-search').val();
          var options = {
            ignoreCase: $('#chk-ignore-case').is(':checked'),
            exactMatch: $('#chk-exact-match').is(':checked'),
            revealResults: $('#chk-reveal-results').is(':checked')
          };
          var results = $searchableTree.treeview('search', [ pattern, options ]);

          var output = '<p>' + results.length + ' matches found</p>';
          $.each(results, function (index, result) {
            output += '<p>- ' + result.text + '</p>';
          });
          $('#search-output').html(output);
      }

        $('#btn-search').on('click', search);
        $('#input-search').on('keyup', search);

        $('#btn-clear-search').on('click', function (e) {
          $searchableTree.treeview('clearSearch');
          $('#input-search').val('');
          $('#search-output').html('');
        });

		
        var initSelectableTree = function() {
          return $('#treeview-selectable').treeview({
            data: defaultData,
            multiSelect: $('#chk-select-multi').is(':checked'),
            onNodeSelected: function(event, node) {
              if(roleName==node.text){
              	selectRoleId = node.href.replace("#","");
              	selectUserId ="";
              }else{
              	selectUserId = node.href.replace("#","");
              	selectRoleId = roleId;
              }
              persion = [];
			  company = [];
              nodeId = node.nodeId;
              //alert(selectRoleId+"==="+selectUserId);
              getDataAuth(selectRoleId,selectUserId);
              $('#selectable-output').prepend('<p>' +node.href+ node.text + ' was selected</p>');
            },
            onNodeUnselected: function (event, node) {
              if(roleName==node.text){
              	selectRoleId = "";
              }else{
              	selectUserId = "";
              	selectRoleId ="";
              }
              nodeId = node.nodeId;
              $('#selectable-output').prepend('<p>' + node.text + ' was unselected</p>');
            }
          });
        };
        var $selectableTree = initSelectableTree();

        var findSelectableNodes = function() {
          return $selectableTree.treeview('search', [ $('#input-select-node').val(), { ignoreCase: false, exactMatch: false } ]);
        };
        var selectableNodes = findSelectableNodes();

        $('#chk-select-multi:checkbox').on('change', function () {
          console.log('multi-select change');
          $selectableTree = initSelectableTree();
          selectableNodes = findSelectableNodes();          
        });

        // Select/unselect/toggle nodes
        $('#input-select-node').on('keyup', function (e) {
          selectableNodes = findSelectableNodes();
          $('.select-node').prop('disabled', !(selectableNodes.length >= 1));
        });

        $('#btn-select-node.select-node').on('click', function (e) {
          $selectableTree.treeview('selectNode', [ selectableNodes, { silent: $('#chk-select-silent').is(':checked') }]);
        });

        $('#btn-unselect-node.select-node').on('click', function (e) {
          $selectableTree.treeview('unselectNode', [ selectableNodes, { silent: $('#chk-select-silent').is(':checked') }]);
        });

        $('#btn-toggle-selected.select-node').on('click', function (e) {
          $selectableTree.treeview('toggleNodeSelected', [ selectableNodes, { silent: $('#chk-select-silent').is(':checked') }]);
        });



        var $expandibleTree = $('#treeview-expandible').treeview({
          data: defaultData,
          onNodeCollapsed: function(event, node) {
            $('#expandible-output').prepend('<p>' + node.text + ' was collapsed</p>');
          },
          onNodeExpanded: function (event, node) {
            $('#expandible-output').prepend('<p>' + node.text + ' was expanded</p>');
          }
        });

        var findExpandibleNodess = function() {
          return $expandibleTree.treeview('search', [ $('#input-expand-node').val(), { ignoreCase: false, exactMatch: false } ]);
        };
        var expandibleNodes = findExpandibleNodess();

        // Expand/collapse/toggle nodes
        $('#input-expand-node').on('keyup', function (e) {
          expandibleNodes = findExpandibleNodess();
          $('.expand-node').prop('disabled', !(expandibleNodes.length >= 1));
        });

        $('#btn-expand-node.expand-node').on('click', function (e) {
          var levels = $('#select-expand-node-levels').val();
          $expandibleTree.treeview('expandNode', [ expandibleNodes, { levels: levels, silent: $('#chk-expand-silent').is(':checked') }]);
        });

        $('#btn-collapse-node.expand-node').on('click', function (e) {
          $expandibleTree.treeview('collapseNode', [ expandibleNodes, { silent: $('#chk-expand-silent').is(':checked') }]);
        });

        $('#btn-toggle-expanded.expand-node').on('click', function (e) {
          $expandibleTree.treeview('toggleNodeExpanded', [ expandibleNodes, { silent: $('#chk-expand-silent').is(':checked') }]);
        });

        // Expand/collapse all
        $('#btn-expand-all').on('click', function (e) {
          var levels = $('#select-expand-all-levels').val();
          $expandibleTree.treeview('expandAll', { levels: levels, silent: $('#chk-expand-silent').is(':checked') });
        });

        $('#btn-collapse-all').on('click', function (e) {
          $expandibleTree.treeview('collapseAll', { silent: $('#chk-expand-silent').is(':checked') });
        });



        var $checkableTree = $('#treeview-checkable').treeview({
          data: defaultData,
          showIcon: false,
          showCheckbox: true,
          onNodeChecked: function(event, node) {
            $('#checkable-output').prepend('<p>' + node.text + ' was checked</p>');
          },
          onNodeUnchecked: function (event, node) {
            $('#checkable-output').prepend('<p>' + node.text + ' was unchecked</p>');
          }
        });

        var findCheckableNodess = function() {
          return $checkableTree.treeview('search', [ $('#input-check-node').val(), { ignoreCase: false, exactMatch: false } ]);
        };
        var checkableNodes = findCheckableNodess();

        // Check/uncheck/toggle nodes
        $('#input-check-node').on('keyup', function (e) {
          checkableNodes = findCheckableNodess();
          $('.check-node').prop('disabled', !(checkableNodes.length >= 1));
        });

        $('#btn-check-node.check-node').on('click', function (e) {
          $checkableTree.treeview('checkNode', [ checkableNodes, { silent: $('#chk-check-silent').is(':checked') }]);
        });

        $('#btn-uncheck-node.check-node').on('click', function (e) {
          $checkableTree.treeview('uncheckNode', [ checkableNodes, { silent: $('#chk-check-silent').is(':checked') }]);
        });

        $('#btn-toggle-checked.check-node').on('click', function (e) {
          $checkableTree.treeview('toggleNodeChecked', [ checkableNodes, { silent: $('#chk-check-silent').is(':checked') }]);
        });

        // Check/uncheck all
        $('#btn-check-all').on('click', function (e) {
          $checkableTree.treeview('checkAll', { silent: $('#chk-check-silent').is(':checked') });
        });

        $('#btn-uncheck-all').on('click', function (e) {
          $checkableTree.treeview('uncheckAll', { silent: $('#chk-check-silent').is(':checked') });
        });



        var $disabledTree = $('#treeview-disabled').treeview({
          data: defaultData,
          onNodeDisabled: function(event, node) {
            $('#disabled-output').prepend('<p>' + node.text + ' was disabled</p>');
          },
          onNodeEnabled: function (event, node) {
            $('#disabled-output').prepend('<p>' + node.text + ' was enabled</p>');
          },
          onNodeCollapsed: function(event, node) {
            $('#disabled-output').prepend('<p>' + node.text + ' was collapsed</p>');
          },
          onNodeUnchecked: function (event, node) {
            $('#disabled-output').prepend('<p>' + node.text + ' was unchecked</p>');
          },
          onNodeUnselected: function (event, node) {
            $('#disabled-output').prepend('<p>' + node.text + ' was unselected</p>');
          }
        });

        var findDisabledNodes = function() {
          return $disabledTree.treeview('search', [ $('#input-disable-node').val(), { ignoreCase: false, exactMatch: false } ]);
        };
        var disabledNodes = findDisabledNodes();

        // Expand/collapse/toggle nodes
        $('#input-disable-node').on('keyup', function (e) {
          disabledNodes = findDisabledNodes();
          $('.disable-node').prop('disabled', !(disabledNodes.length >= 1));
        });

        $('#btn-disable-node.disable-node').on('click', function (e) {
          $disabledTree.treeview('disableNode', [ disabledNodes, { silent: $('#chk-disable-silent').is(':checked') }]);
        });

        $('#btn-enable-node.disable-node').on('click', function (e) {
          $disabledTree.treeview('enableNode', [ disabledNodes, { silent: $('#chk-disable-silent').is(':checked') }]);
        });

        $('#btn-toggle-disabled.disable-node').on('click', function (e) {
          $disabledTree.treeview('toggleNodeDisabled', [ disabledNodes, { silent: $('#chk-disable-silent').is(':checked') }]);
        });

        // Expand/collapse all
        $('#btn-disable-all').on('click', function (e) {
          $disabledTree.treeview('disableAll', { silent: $('#chk-disable-silent').is(':checked') });
        });

        $('#btn-enable-all').on('click', function (e) {
          $disabledTree.treeview('enableAll', { silent: $('#chk-disable-silent').is(':checked') });
        });
  		});
	  	</script>
</html>