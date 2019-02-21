buttonJson =[
              {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
              {name:'新增',fun:'tog(this),setUp(),setUpAdd()',buttonToken:'add'},
              {name:'修改',fun:'updateRow(this),setUp()',buttonToken:'update'},           
              {name:'删除',fun:'delRows(this)',buttonToken:'delete'}
             ];

//公共设置
function setUp(){
	$('#FREP_CYCLE').val('5').attr("disabled",true);
	$("#CREATER").attr("disabled",true);
	$("#CREATE_DATE").attr("disabled",true);
	$("#CREATE_DATE").nextAll().css("display","none");
}

//新增设置
function setUpAdd(){
	$("#CREATER").val(userName);//创建者
	$("#CREATE_DATE").val(getTime());//创建日期
}

//重写保存函数
function savaByQuery(t,_dataSourceCode,$div){
	if($('#INITIATE_MODE option:selected').val() == '1'){//启动
		if(!isCusLevelCreate($('#ID').val(),$('#CUS_LEVEL option:selected').val())){
			oTable.showModal('提示', '此级别数据已存在');
			return;
		}
	}
	var buttonToken = $("#ins_or_up_buttontoken").val();
	if(buttonToken == 'add'){
		$('#RULE_CODE').val(getApplyCode());
	}
	var message = transToServer(findBusUrlByButtonTonken(buttonToken,'',_dataSourceCode),getJson($('#insPage')));
	oTable.showModal('提示', message);
	back(t);
	queryTableByDataSourceCode(t,_dataSourceCode);
}

//获取拜访规则编号
function getApplyCode(){
	var code = '';
	$.ajax({
		url : '/system/cuma/getVisitRulesCode',
		type : "GET",
		async : false,
		dataType : "text",
		success : function(data) {
			code = data;
		}
	});
	return code
}

//判断同一个客户分级是否创建了拜访规则
function isCusLevelCreate(id,cusLevel){
	var flag = '';
	$.ajax({
		url : '/system/cuma/isCusLevelCreate',
		type : "GET",
		data : {'id':id,'cusLevel':cusLevel},
		async : false,
		dataType : "json",
		success : function(data) {
			flag = data;
		}
	});
	return flag;
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
