<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
	<head>
		<meta charset="utf-8">
		<title>组织管理</title>
	</head>
	<body>
	
	
	<form class="form-horizontal">
		<div class="panel panel-primary">
				
				<div class="panel-body" id="bulidTable">
					<div class="panel panel-primary">
						<div class="panel-heading" id ='pageName'>
							组织管理
						</div>
						
						<div class="panel-body">
							<div class="panel panel-default">
								<div class="panel-heading" id="companyTitle">
									组织详细信息
								</div>
								<div class="panel-heading" id="departmentTitle">
									部门详细信息
								</div>
								<!-- 维护页面-->
								<div class="panel-body" id="superInsertPage">
								</div>
							</div>
							
							<div id="toolbar">
								<!-- 按钮页面 -->
								<!-- <button type="button" class="btn btn-info" onclick="window.history.back(-1);">
										<span class="glyphicon glyphicon-ban-circle" aria-hidden="true"></span>返回
								</button> -->
								<div id='button_div' buttonType='super'></div>
								<div id='button_child_div' buttonType='child'></div>
								<div id='button_child_div_001' >
								</div>
							</div>
							<!-- 列表页面 -->
							<table id="table"></table>

						</div>
					</div>
				</div>

				<div class="panel-body" id="bulidPage" style="display: none">
					<div class="panel panel-primary">
						<div class="panel-heading" id="companyAdd">
							新增公司
						</div>
						<div class="panel-heading" id="departmentAdd">
							新增部门
						</div>
						<div class="panel-body">
							
							<div>
								<button type="button" class="btn btn-success" onclick="saveParty(this)">
									<span class="glyphicon glyphicon-saved" aria-hidden="true"></span>保存
								</button>
								<button type="button" class="btn btn-inverse" onclick="back(this)">
									<span class="glyphicon glyphicon-ban-circle" aria-hidden="true"></span>返回
								</button>
							</div>
							<!-- 维护页面 -->
							<div class="panel-body" id="insPage">
								<!--主表主键  用于 新增、修改-->
								<input type='hidden' name="ParentPK">
								<input type='hidden' name="ParentCode">
							</div>
						
						</div>
					</div>
				</div>
				
			</div>		
	</form>
		<!-- 缓存参数 -->
		<!-- 是否为修改页面 -->
		</body>
	<jsp:include page="../include/public.jsp"></jsp:include>
	<script type="text/javascript">
	  $(function() {
	  
		if(dataSourceCode=='TM_COMPANY'){
			document.getElementById("departmentTitle").style.display="none";
			document.getElementById("departmentAdd").style.display="none";
		}else if(dataSourceCode=='TM_DEPARTMENT'){
			document.getElementById("companyTitle").style.display="none";
			document.getElementById("companyAdd").style.display="none";
		}
		//alert(ParentPKValue);
	  	var mainPageParam =pageParamFormat(ParentId +" ="+ParentPKValue);
	  	
	  	var Request = new Object(); 
		Request = GetRequest();
		var isReadonly = Request['isReadonly'];
	  	/*if(isReadonly=='1'){
		   	mainPageParam=mainPageParam+"&showType=text";
		}*/
		bulidMaintainPage($("#superInsertPage"),dataSourceCode,mainPageParam);
		bulidMaintainPage($("#insPage"),_dataSourceCode,'');
		bulidButton($("#button_div"),_dataSourceCode,'');
		bulidButton($("#button_child_div"),_dataSourceCode,'');
		//bulidButton($("#button_child_div_001"),_dataSourceCode,'');
		//bulidListPage($("#table"),_dataSourceCode,_query_param);
		//setReadonlyByDiv($("#superInsertPage input[type='text']"));
		
	  	//获取树节点的child_party_code,作为新增节点的parent_party_code;同时，删除时可以用到
	  	var ParentCode = '<%=request.getParameter("ParentPartyCode")!=null?request.getParameter("ParentPartyCode"):""%>';
	  	//alert("ParentCode:"+ParentCode);
	  	
	  	//request.setAttribute("ParentCode",ParentCode);
	  	//alert("****"+request.getParameter(ParentCode));
	  	
		$('input[name="ParentCode"]').attr('id','PARENT_PARTY_CODE').val(ParentCode);
		//alert("****"+$("#PARENT_PARTY_CODE").val());
		//alert("isReadonly:"+isReadonly);
		if(isReadonly=='1'){
			//setReadonlyByDiv($("#superInsertPage input[type='text']"));
			//$inspage.find("[id='ADMIN_TYPE']").attr("disabled",true);
			$("#superInsertPage").find("[id]").each(function(){
				$(this).attr("disabled",true);
			});
		}
	  });
		
	//重写back方法
	function back(t){
		tog(t);
		//排除主表主键
		$inspage.find('[id]').not('#'+ParentPKField).val("");
		$("#ins_or_up_buttontoken").val("");
	}
	
	function saveParty(t){
		//此处新增必填项校验
		if(validate()){
			save(t);
			
		}else{
			return null;
		}
	}
	
	function validate(){
	//此处新增必填项校验
		var com = "TM_COMPANY";
		var dep = "TM_DEPARTMENT";
		//alert(dataSourceCode);
		if(dataSourceCode==com){
			//alert("***"+dataSourceCode);
			//alert("----"+$("#insPage input[id='OU']").val());
			if($("#insPage input[id='OU']").val()==""){
				alert("公司 编码不能为空！");
				return false;
			}
			if($("#insPage input[id='NAME']").val()==""){
				alert("公司名称不能为空！");
				return false;
			}
			if($("#insPage input[id='DISPLAYNAME']").val()==""){
				alert("公司简称不能为空！");
				return false;
			}
			if($("#insPage input[id='TM_CODE']").val()==""){
				alert("资金编码不能为空！");
				return false;
			}
			
		}else if(dataSourceCode==dep){
			if($("#insPage input[id='OU']").val()==""){
				alert("部门 编码不能为空！");
				return false;
			}
			if($("#insPage input[id='NAME']").val()==""){
				alert("部门名称不能为空！");
				return false;
			}
			if($("#insPage input[id='DISPLAYNAME']").val()==""){
				alert("部门简称不能为空！");
				return false;
			}
			if($("#insPage input[id='TM_CODE']").val()==""){
				alert("资金编码不能为空！");
				return false;
			}
		}
		return true;
	}
	</script>
</html>