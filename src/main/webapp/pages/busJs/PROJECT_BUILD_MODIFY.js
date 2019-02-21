buttonJson =[
             {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
             {name:'返回',fun:'window.history.go(-1);'}
            ];

var disabledIframe = true;//编辑状态
var executeManagerPageState = false;//项目经理执行页面详情状态

//初始化
$(function(){
	setUp();
});

//公共设置
function setUp(){
	setData();
	$("#MODIFY_FEASIBILITY_ANALYSIS").css('height','90px').attr("placeholder","1） 项目成本\n2） 项目进度\n3） 项目质量要求及实施难易程度\n4） 变更前后经济性评估的变化");//变更影响分析默认值
	$("#COUNTERMEASURES").css('height','90px').attr("placeholder","1）  新增投资/成本建议\n2）  项目进度调整建议\n3）  采购方式建议\n4）  其它应对建议");//应对建议默认值
	$("#MODIFY_NUMBER,#ENTRY_CODE,#ENTRY_NAME").attr("readonly","true");//变更编号
	//将文本框隐藏，设置复选框
	$("#MODIFY_CONTENT_CLASSIFY").parent().attr('id','MODIFY_CONTENT_CLASSIFY_DIV');
	var modifyContentClassifyHtml = "";
	modifyContentClassifyHtml += "<input type='CHECKBOX' id='CHECKBOX6' name='CHECKBOX' value='5' onclick='checkBoxOnClick();'>项目组织架构变更"+
								 "<input type='CHECKBOX' id='CHECKBOX2' name='CHECKBOX' value='1' onclick='checkBoxOnClick();'>项目预算变更"+
								 "<input type='CHECKBOX' id='CHECKBOX3' name='CHECKBOX' value='2' onclick='checkBoxOnClick();'>项目进度阶段计划变更"+
								 "<input type='CHECKBOX' id='CHECKBOX4' name='CHECKBOX' value='3' onclick='checkBoxOnClick();'>项目范围(业务)变更"+
								 "<input type='CHECKBOX' id='CHECKBOX5' name='CHECKBOX' value='4' onclick='checkBoxOnClick();'>项目问题风险变更";
	if(keyWord == 1){//能力项目建设
		modifyContentClassifyHtml += "<input type='CHECKBOX' id='CHECKBOX1' name='CHECKBOX' VALUE='0' onclick='checkBoxOnClick();'>能力项目详细设计变更";
	}
	modifyContentClassifyHtml += "<input type='hidden' class='form-control' id='MODIFY_CONTENT_CLASSIFY' name='MODIFY_CONTENT_CLASSIFY' value='"+modify_content_classify+"' data-bv-field='MODIFY_CONTENT_CLASSIFY'>"+
					   			 "<i class='form-control-feedback' data-bv-icon-for='MODIFY_CONTENT_CLASSIFY' style='display: none;'></i>";
	$("#MODIFY_CONTENT_CLASSIFY_DIV").html(modifyContentClassifyHtml).css({'width':'920px','height':'76px','line-height':'76px'});
	$("#MODIFY_CONTENT_CLASSIFY_DIV").prev().css({'height':'76px','line-height':'90px'});
	$('#CHECKBOX2,#CHECKBOX3,#CHECKBOX4,#CHECKBOX5,#CHECKBOX1').css('margin-left','10px');
	//设置选中
	setCheckbox();
	//获取附件
	getFileTypeVal($('#insPage'));
	//初始化卡片Tab
	selectTab(modify_content_classify);
	//新增数据设置
	setDataAdd();
}

//数据设置
function setData(){
	modifyId = $("#ID").val();//把单据ID放在全局
	modify_content_classify = $("#MODIFY_CONTENT_CLASSIFY").val();
}

//新增数据设置
function setDataAdd(){
	if(modifyId == null || modifyId == ''){
		$("#APPLICANT").val(currentUserName);//申请人名称
		$("#APPLICATION_TIME").val(getTime());//申请时间
		$("#CREATOR_ID").val(currentUserId);//录入人主键
		$("#CREATOR_NAME").val(currentUserName);//录入人名称
		$("#CREATOR_DATE").val(getTime());//录入时间
		$("#MODIFY_STATE").val(1);//变更状态
		$("#MODIFY_PLACE_STATE").val(0);//变更所处阶段
		$("#BILL_STATUS").val(0);//审批状态
		$("#PROJECT_MANAGER_ID").val(getManagerId());//项目经理主键
		$("#PROJECT_MANAGER").val(manager);//项目经理
		$("#ENTRY_NAME").val(name);//项目名称
		$("#ENTRY_CODE").val(code);//商机/项目编号
	}
}

//获取项目经理主键
function getManagerId(){
	var managerId = '';
	if(keyWord == 0){
		var releasedData = querySingleRecord("&dataSourceCode=PROJ_RELEASED&SEARCH-ID="+ParentPKValue);
		managerId = releasedData["MANAGER_ID"];
	}else if(keyWord == 1){
		 $.ajax({
             type: 'GET',
             url: '/system/project/getProjectManagerId',
             data: {'requirementId':ParentPKValue},
             dataType: "text",
             async: false,
             success: function(data){
                managerId = data;
             }
         });
	}
	return managerId;
}

//设置复选框选中
function setCheckbox(){
	var checkbox = document.getElementsByName('CHECKBOX');
	for (var i = 0; i < checkbox.length; i++){
		if (modify_content_classify.indexOf(checkbox[i].value) != -1){
			checkbox[i].checked = true;
		} 
	}
}

//点击复选框
function checkBoxOnClick(){
	var checkBoxVal = "";
	$.each($('input:checkbox'),function(){
        if(this.checked){
        	checkBoxVal += $(this).val()+",";
        }
    });
    modify_content_classify = checkBoxVal.substring(0,checkBoxVal.length-1);//赋给全局变量
    $("#MODIFY_CONTENT_CLASSIFY").val(modify_content_classify);//设置到文本框中
    selectTab(modify_content_classify);//变更卡片Tab
}

//设置变更内容样式
function selectTab(clickValue){
	//将每个Tab的Url以逗号拼接起来
	var src = "newSingleTableModify22.jsp?token="+token+"&keyWord=1&isHide="+isHide+"&pageCode=CAPACITY_DESIGN&pageName=能力建设详细设计&isAddAuditBut=1&ParentPKField=PROJ_SOURCE_ID&ParentPKValue="+ParentPKValue+"&modifyId="+modifyId+"&modifyFieldName=DESIGN_JSON&state=1#能力项目详细设计变更,"+
			  "budgetBuildModify.jsp?token="+token+"&keyWord="+keyWord+"&isHide="+isHide+"&pageCode=PROJ_BUDGET&pageName=预算&ParentPKValue="+ParentPKValue+"&modifyId="+modifyId+"#项目预算变更,"+
			  "project/progressPlan.jsp?token="+token+"&keyWord=1&isHide="+isHide+"&pageCode=PROJ_PROGRESS_PHASE&pageName=进度计划&ParentPKValue="+ParentPKValue+"&modifyId="+modifyId+"&modifyFieldName=PROGRESS_JSON&state=1#项目进度阶段计划变更,"+
			  "maintainPage_project.jsp?token="+token+"&keyWord=1&isHide="+isHide+"&pageCode=PROJ_PROPOSAL_GK&pageName=项目基本信息&menuCode=0&ParentPKField=PROJ_SOURCE_ID&ParentPKValue="+ParentPKValue+"&modifyId="+modifyId+"&modifyFieldName=RANGE_JSON&state=1#项目范围(业务)变更,"+
			  "project/riskTracking.jsp?token="+token+"&keyWord=1&isHide="+isHide+"&pageCode=PROJ_RISK_MANAGEMENT&menuCode=0&pageName=项目风险问题管控&ParentPKField=PROJ_SOURCE_ID&ParentPKValue="+ParentPKValue+"&modifyId="+modifyId+"&modifyFieldName=RISK_JSON&state=1#项目问题风险变更,"+
			  "project/projectOrganization.jsp?token="+token+"&keyWord=1&isHide="+isHide+"&pageCode=PROJ_ORGANIZATION&ParentPKValue="+ParentPKValue+"&modifyId="+modifyId+"&modifyFieldName=ORGANIZATION_STRUCTURE_JSON&state=1#项目组织架构变更,";
	$("#SPECIFIC_MODIFY_CONTENT").parent().parent().parent().attr('id','SPECIFIC_MODIFY_CONTENT_DIV');//设置变更内容div   ID
	//遍历现在已有的Tab
	var existentClickValues = [];
	$("#myTab").find('li').each(function(){
		var iframeId = transformation('',$(this).text()).split(":")[2];
		existentClickValues.push(iframeId);
	});
	//遍历已选择的Tab
	var clickValues = clickValue.split(",");
	var htmlState = "";//状态(add,delete)
	var arr = [];//Tab数组
	//根据大小对比判断是取消选中还是新增选中
	if(existentClickValues.length > clickValues.length || clickValues[0] ==""){
		for (var i = 0; i < existentClickValues.length; i++) {
			if(clickValues.indexOf(existentClickValues[i]) == -1 && existentClickValues[i] !=""){
				arr.push(existentClickValues[i]);
			}
	    }
	    htmlState = "delete";
	}else{
		for (var i = 0; i < clickValues.length; i++) {
			if(existentClickValues.indexOf(clickValues[i]) == -1 && clickValues[i] !=""){
				arr.push(clickValues[i]);
			}
	    }
	    htmlState = "add";
	}
	var myTabHtml = "";//卡片html
	var myIframeHtml = "";//iframehtml
	//为新增时循环拼接html
	if(htmlState == "add"){
		for(var i=0;i<arr.length;i++){
			myTabHtml += "<li id='li"+arr[i]+"'>";
			myTabHtml += "<a href='#"+arr[i]+"' data-toggle='tab' onclick=setIframeHeight('#myIframe"+arr[i]+"')>"+src.split(',')[parseInt(arr[i])].split('#')[1]+"</a></li>";
		}
		for(var i=0;i<arr.length;i++){
			myIframeHtml += "<div class='tab-pane fade' id='"+arr[i]+"'>";
			myIframeHtml += "<iframe id='myIframe"+arr[i]+"' name='myIframe"+arr[i]+"' src='"+src.split(',')[parseInt(arr[i])].split('#')[0]+"' scrolling='auto' frameborder='0' width='99%' height='500px'></iframe></div>";
		}
	}
	//条件满足根据状态判断新增或删除对html操作  else 初始化div
	if(htmlState !=null && htmlState !="" && existentClickValues.length>0 && existentClickValues[0] !=""){
		if(htmlState == "add"){
			$('#myTab').append(myTabHtml);
			$('#myIframe').append(myIframeHtml);
		}else if(htmlState == "delete"){
			for(var i=0;i<arr.length;i++){
				document.getElementById("myIframe").removeChild(document.getElementById(arr[i]));
				document.getElementById("myTab").removeChild(document.getElementById("li"+arr[i]));
			}
		}
	}else{
		var specificModifyContentHtml = "<form><div class='col-md-12'><ul id='myTab' class='nav nav-tabs'>";
		specificModifyContentHtml += myTabHtml+"</ul></div></br><div class='col-md-12'><div class='tab-content' id='myIframe'>";
		specificModifyContentHtml += myIframeHtml+"</div></div>";
		specificModifyContentHtml += "</form>";
		if(!$("#SPECIFIC_MODIFY_CONTENT_DIV_DIV").length>0){
			$("#SPECIFIC_MODIFY_CONTENT_DIV").before('<div class="col-md-12 classifiedtitle" id = "SPECIFIC_MODIFY_CONTENT_DIV_DIV">变更内容</div>');
		}
		$("#SPECIFIC_MODIFY_CONTENT_DIV").html(specificModifyContentHtml).css({'border-left':'1px solid rgb(211, 238, 249)','border-right':'1px solid rgb(211, 238, 249)','border-bottom':'1px solid rgb(211, 238, 249)','border-radius':'0px 0px 5px 5px'});
	}
	setParntHeigth($(document.body).height());//高度设置
	hideAndShow();//设置界面文本及按钮的显示和隐藏
}

//设置Iframe高度
function setIframeHeight(id){
	if(disabledIframe == false){
		$(id).contents().find('[type=button]').hide();
		$(id).contents().find('select,[type=TEXT]').attr("disabled",true);
		$(id).contents().find('#SUBMISSION_DATE').nextAll().css("display","none");
		displayAttachment($(id).contents());
	}
	setTimeout(function(){
		var height = $(id).contents().find("body").height();
		if(height<200){
			height = 300;
		}
		$(id).height(height);
		setParntHeigth($(document.body).height());//高度设置
	},200);
}

function setParntHeigthCopy(){
	setParntHeigth($(document.body).height());
}

//变更内容  卡片json存在放在相应的字段中
function prepareSave(val) {
	var iframeIndex = val.split(',');
	if(iframeIndex.length > 0 && iframeIndex[0] != ""){
		if(iframeIndex.indexOf('1') != -1){
			if(!isUploadEnclosure()){
				oTable.showModal('提示', "请上传预算相关附件");
				return false;
			}
		}
		if(!saveVerification()){
			return false;
		}
		//清空字段
		$("#DESIGN_JSON,#BUDGET_JSON,#PROGRESS_JSON,#RANGE_JSON,#RISK_JSON,#ORGANIZATION_STRUCTURE_JSON,#OTHER_JSON,#PRE_ASSESSMENT_JSON").val("");
		//循环iframe
		for(var i=0;i<iframeIndex.length;i++){
			var iframeId = "myIframe"+iframeIndex[i];
			//校验字段
			var verification = document.getElementById(iframeId).contentWindow.iframeVerification(keyWord);
			if(verification.split(":")[0] == "false"){
				var iframeName = transformation(iframeIndex[i],'').split(":")[3];
				oTable.showModal('提示', iframeName+":"+verification.split(":")[1]);
				return false;
			}
			//取iframe中jsonValue()函数json
			if(iframeId == 'myIframe1'){//预算
				var budgetJson = JSON.stringify(document.getElementById(iframeId).contentWindow.budgetJsonValue()).replace(/\"/g,"'");
				$('#BUDGET_JSON').val(budgetJson);
				var preAssessmentJson = document.getElementById(iframeId).contentWindow.preAssessmentValue().replace(/\"/g,"'");
				$('#PRE_ASSESSMENT_JSON').val(preAssessmentJson);
			}else{
				var json = JSON.stringify(document.getElementById(iframeId).contentWindow.jsonValue()).replace(/\"/g,"'");
				var jqId = transformation(iframeIndex[i],'').split(":")[0];
				$(jqId).val(json);
			}
		}
		return true;
	}else{
		oTable.showModal('提示', '变更内容分类不可为空');
		return false;
	}
}

//保存
function savaByQuery(t,_dataSourceCode,$div){
	if(modifyId == null || modifyId == ""){
		$("#ins_or_up_buttontoken").val("add");
		$('#MODIFY_NUMBER').val(getCode());
		modifyId = getId();
		$('#ID').val(modifyId);
	}
	var buttonToken = $("#ins_or_up_buttontoken").val();
	$('#BILL_STATUS').val('0');
	var message = transToServer(findBusUrlByButtonTonken(buttonToken,'',_dataSourceCode),getJson($('#insPage')));
	oTable.showModal('提示', message);
	if(message.indexOf('成功') != -1){
		$("#ins_or_up_buttontoken,#zx").attr("disabled", true);
		$('#tj').attr("disabled",false);
		disabledAll();
		disabledIframe = false;
	}
}

//保存验证
function saveVerification(){
	var billStatus = $('#BILL_STATUS').val();
	if(billStatus == '7'){
		if(confirm("是否确认重新提交当前变更流程")){
			return true;
		}else{
			return false;
		}
	}
	return true;
}

//重写提交函数
function singlePubRestAuditByTenant(t){
	var selected = JSON.parse("["+getJson($("#insPage"))+"]");
	var tenant_code = findTenantCode(selected[0]);
	if(tenant_code == undefined){
		tenant_code = "";
	}
	if(keyWord == '1'){//能力建设项目
		tenant_code = 2000;
	}
	var type = 1;
	var audit_column = singleFindAuditColumn();
	if($("#ID").val() == null || $("#ID").val() == ""){
		oTable.showModal('提示', "请保存后，再提交！");
		return;
	}
	if(audit_column.length == 0){
		oTable.showModal('提示', "没有设置审批字段！");
	}
	var billStatus = selected[0][audit_column];
	if(billStatus == undefined || billStatus == '' || billStatus.length == 0){
		oTable.showModal('提示', "审批状态为空，不能提交！");
		return;
	}
	if(billStatus != 0 && billStatus != 7){
		oTable.showModal('提示', "只能提交  已保存、已退回单据！");
		return;
	}
	var json = selected[0];
	
	var apprType="&audit_type="+type+"&audit_column="+audit_column+"&tenant_code="+tenant_code;
	var message = transToServer(findBusUrlByPublicParam(t,apprType,dataSourceCode),JSON.stringify(json));
	if(message.indexOf('成功') != -1){
		$('#ins_or_up_buttontoken').attr("disabled",true);
		$("#"+singleFindAuditColumn()).val("1");
		var json = "{\"ID\":\""+modifyId+"\",\"MODIFY_PLACE_STATE\":\"1\"}";
		transToServer(findBusUrlByButtonTonken('update','','PROJECT_BUILD_MODIFY'),json);//更新字段
	}
	oTable.showModal('提示', message);
}

//重写审批进程
function singlePubVisitHis(t){
	var selected = JSON.parse("["+getJson($("#insPage"))+"]");
	var id =selected[0]["ID"];
	var tenant_code = "";
	if(keyWord == '1'){//能力建设项目
		tenant_code = 2000;
	}
	var NEED_PROCESS_CODE=appCode+"@"+dataSourceCode+"@"+tenant_code;
	var visit_url=workflow.replace("#BUS_ID#",id).replace("#ts#",new Date().getTime()).replace("#NEED_PROCESS_CODE#",NEED_PROCESS_CODE);
	singleShow(visit_url);
}

//获取主键
function getId() {
	var id = "";
	$.ajax({
		type : "GET",
		url : "/system/base?cmd=getIDByDataSourceCode&pageCode=PROJECT_BUILD_MODIFY",
		async : false,
		dataType : "json",
		success : function(data) {
			id = data["id"];
		},
		error : function(data) {
			alert("获取主键失败，请联系管理员！" + "\t" + "错误代码：" + JSON.stringify(data));
		}
	});
	return id;
}

//获取变更编号
function getCode(){
	var code = '';
	$.ajax({
        type: "get",
        url: context+"/project?getSerialCode",
        data: {"modifyNumber":$("#ENTRY_CODE").val()},
        async: false,
        success: function(data){
        	code = data;
        }
	});
	return code;
}

//双击事件
function dblClickFunction(row,tr){
	var json =JSON.parse(JSON.stringify(row));
	window.location.href=context+"/pages/projectBuildModify.jsp?isHide=true&token="+token+"&userId="+userId+"&userName="+userName+"&pageCode=PROJECT_BUILD_MODIFY&pageName=建设项目变更&ParentPKField=ID&ParentPKValue="+json["ID"];
}

//执行点击
function zxClick(){
	var url = context+"/pages/projectModifyExecute.jsp?isHide="+isHide+"&token="+token+"&pageCode=PROJECT_MODIFY_EXECUTE&pageName=建设项目变更执行&ParentPKField=BUILD_MODIFY_ID&ParentPKValue="+modifyId+"&executeManagerPageState="+executeManagerPageState;
	window.location.href = url;
}

//转换数据
function transformation(id,name){
	var jqID = "";//jquery  ID
	var dataSource = "";// 数据源名称
	var iframeID = "";//iframe  ID
	var iframeName = "";//iframe NAME
	if(id.indexOf("0") != -1 || name.indexOf("能力项目详细设计变更") != -1){//能力项目详细设计变更
		jqID = "#DESIGN_JSON";
		dataSource = '';
		iframeID = 0;
		iframeName = "能力项目详细设计变更";
	}else if(id.indexOf("1") != -1 || name.indexOf("项目预算变更") != -1){//项目预算变更
		jqID = "#BUDGET_JSON";
		dataSource = 'PROJ_BUDGET';
		iframeID = 1;
		iframeName = "项目预算变更";
	}else if(id.indexOf("2") != -1 || name.indexOf("项目进度阶段计划变更") != -1){//项目进度阶段计划变更
		jqID = "#PROGRESS_JSON";
		dataSource = '';
		iframeID = 2;
		iframeName = "项目进度阶段计划变更";
	}else if(id.indexOf("3") != -1 || name.indexOf("项目范围(业务)变更") != -1){//项目范围(业务)变更
		jqID = "#RANGE_JSON";
		dataSource = '';
		iframeID = 3;
		iframeName = "项目范围(业务)变更";
	}else if(id.indexOf("4") != -1 || name.indexOf("项目问题风险变更") != -1){//项目问题风险变更
		jqID = "#RISK_JSON";
		dataSource = '';
		iframeID = 4;
		iframeName = "项目问题风险变更";
	}else if(id.indexOf("5") != -1 || name.indexOf("项目组织架构变更") != -1){//项目组织架构变更
		jqID = "#ORGANIZATION_STRUCTURE_JSON";
		dataSource = 'PROJ_ORGANIZATION';
		iframeID = 5;
		iframeName = "项目组织架构变更";
	}else if(id.indexOf("6") != -1 || name.indexOf("其他计划") != -1){//其他计划
		jqID = "#OTHER_JSON";
		dataSource = '';
		iframeID = 6;
		iframeName = "其他计划";
	}
	return jqID+":"+dataSource+":"+iframeID+":"+iframeName;
}

//设置界面文本及按钮的显示和隐藏
function hideAndShow(){
	var billStatus = $('#BILL_STATUS').val();
	if(billStatus == '3'){
		isShowZX();
		$('#ins_or_up_buttontoken,#tj').css("display","none");
		disabledAll();
		disabledIframe = false;
	}else if(billStatus == '7'){
		isShowZX();
	}else if(billStatus != '' && billStatus != '0'){
		$('#ins_or_up_buttontoken').attr("disabled",true);
		disabledAll();
		disabledIframe = false;
	}
	//项目变更详情
	if(ParentPKField == 'ID'){//审批页面或者点击详情
		$('#tog_titleName').text('项目变更详情');
		$('#zx').text('变更执行查看');
		$('#ins_or_up_buttontoken,#tj').css("display","none");
		disabledIframe = false;
		disabledAll();
	}
	//历史详情
	if(isHistory){
		$('#ins_or_up_buttontoken,#tj,#xpjc,#zx,#fh').css("display","none");
	}
}

//是否显示变更执行
function isShowZX(){
	var executeState = $('#EXECUTE_STATE').val();//执行状态
	var projectManagerId = $('#PROJECT_MANAGER_ID').val();//项目经理主键
	var modifyExecutorId = $('#MODIFY_EXECUTOR_ID').val();//变更执行主键
	
	if(ParentPKField == 'ID'){
		
		if(userId == projectManagerId){
			executeManagerPageState = true;
		}
		$('#zx').css("display","inline");
		
	}else if(executeState == ''){
		
		if(userId == projectManagerId){
			$('#zx').css("display","inline");
		}
		
	}else if(executeState == '0'){//保存
		
		if(userId == projectManagerId || userId == modifyExecutorId){
			$('#zx').css("display","inline");
		}
		
	}else if(executeState == '1'){//提交
		
		if(userId == projectManagerId){
			$('#zx').css("display","inline");
		}
		
	}else if(executeState == '2'){//驳回
		
		if(userId == modifyExecutorId){
			$('#zx').css("display","inline");
		}
		
	}
}

//设置本界面  disabled
function disabledAll(){
	$("#insPage").find("[id]").each(function(){
		$(this).attr("disabled","disabled");
	});
	$("#insPage").find(".form_date").datetimepicker('remove');
	getFileTypeValTo($('#insPage'));//隐藏上传附件、不可删除
}

//附件不可编辑
function displayAttachment(div){
	div.find('[id=kuang]').each(function(index) {
		$(this).css("display","none");
	});
	div.find('[id=files_a_del]').each(function(index) {
		$(this).css("display","none");
	});
}

//是否上传附件
function isUploadEnclosure(){
	var showfilesRnclosure = $('#showfiles_ENCLOSURE').html();
	if(showfilesRnclosure.indexOf('此条数据未上传附件') == -1 && showfilesRnclosure != null && showfilesRnclosure != ''){
		return true;
	}
	return false;
}

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
    return year+"-"+month+"-"+day;
}
