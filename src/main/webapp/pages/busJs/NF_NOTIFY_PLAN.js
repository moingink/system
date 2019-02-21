buttonJson =[
             {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
             {name:'新增',fun:'tog(this),initDiv()',buttonToken:'addByNotity'},
             {name:'修改',fun:'updateRow(this),initDiv()',buttonToken:'updateByNotity'},
             {name:'删除',fun:'delRows(this)',buttonToken:'delete'},
             {name:'发送',fun:'send(this)',buttonToken:'send'},
             {name:'查看',fun:'view()',buttonToken:'send'}
			];



function send(t){
	var selected = JSON.parse(getSelections());
	if(selected.length > 1){
		oTable.showModal('modal',"请选择一条！");
		return;
	}
	var jsonData = JSON.stringify(selected);
	var message = transToServer(findBusUrlByPublicParam(t,'',dataSourceCode),jsonData);
	oTable.showModal('modal', message);
	
}

function initDiv(){
	
	$("#ONE_LEVEL").find("option").attr("onclick","updateCodeByOne(this)");
	$("#TWO_LEVEL").find("option").attr("onclick","updateCodeByTwo(this)");
}

var oneSelectVal ='';
function updateCodeByOne(t){
	
	var val =$(t).val();
	if(val!=oneSelectVal){
		$("#PLAN_CODE").val(val);
		oneSelectVal=val;
	}
	
}


var twoSelectVal ='';
function updateCodeByTwo(t){
	var twoVal =$(t).val();
	if(twoVal!=twoSelectVal){
		var oneVal =$("#ONE_LEVEL").val();
		var val =twoVal;
		if(oneVal!=""){
			val =oneVal+"-"+twoVal;
		}
		$("#PLAN_CODE").val(val);
		oneSelectVal=twoVal;
	}
}


function view(){
	
	//查看子表相关配置
	var pageCode = 'NF_NOTIFY_USER_GROUP,NF_NOTIFY_PLAN_SEND_TEMP';//子表数据源
	var pageName = '通知用户,通知模板消息配置';//列表显示名称
	var ParentPKField = 'PLAN_ID';//主表主键在子表中字段名
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('modal', "请选择一条数据进行操作");
		return;
	}
	window.location.href=context+"/pages/childTableModifyForIframe.jsp?pageCode="+pageCode+"&pageName="+pageName+"&ParentPKField="+ParentPKField+"&ParentPKValue="+JSON.parse(getSelections())[0]["ID"]+"&token="+token;
}