buttonJson =[
             {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
             {name:'新增',fun:'tog(this),addSetUp()',buttonToken:'add'},
             {name:'修改',fun:'updateRow(this),updateSetUp()',buttonToken:'update'},
             {name:'删除',fun:'delRows(this)',buttonToken:'delete'}
            ];

//初始化   
$(function(){
	$("#CUT_OFF_TIME").parent().parent().attr("class","form-group");
	$("#CUT_OFF_TIME").parent().css("display","none");
	var optionHtml = "";
	for(var i=1;i<32;i++){
		var value = i;
		if(i<10){
			value = "0"+i;
		}
		optionHtml += "<option value="+value+">"+value+"</option>";
	}
	var html = '<div class="form-inline" style="width:500px;"><select id="CUT_OFF_TIME_DAY" class="form-control" style="width:100px;">'+optionHtml+'</select><span>日</span><input id="CUT_OFF_TIME_TIME" type="TIME" class="form-control" style="width:130px;"></div>';
	$("#CUT_OFF_TIME").parent().parent().append(html);
});

//新增设置
function addSetUp(){
	$("#OPERATION_PEOPLE").val(userName);//操作人
	$("#OPERATION_PEOPLE_ID").val(userId);//操作人ID
	$("#OPERATION_DATE").val(getTime());//操作时间
	$("#CREATOR_ID").val(userId);//创建人ID
}

//新增设置
function updateSetUp(){
	$("#OPERATION_PEOPLE").val(userName);//操作人
	$("#OPERATION_PEOPLE_ID").val(userId);//操作人ID
	$("#OPERATION_DATE").val(getTime());//操作时间
	var cutOffTime = $("#CUT_OFF_TIME").val();//截至时间
	var cutOffTimes = cutOffTime.split(" ");
	$("#CUT_OFF_TIME_DAY").val(cutOffTimes[0]);
	$("#CUT_OFF_TIME_TIME").val(cutOffTimes[1]);
}

//重写保存函数
function save(t){
	var cutOffTimeDay = $("#CUT_OFF_TIME_DAY").val();//截至时间--日
	var cutOffTimeTime = $("#CUT_OFF_TIME_TIME").val();//截至时间--时分
	if(cutOffTimeDay != null && cutOffTimeDay != "" && cutOffTimeTime != null && cutOffTimeTime != ""){
		$("#CUT_OFF_TIME").val(cutOffTimeDay+" "+cutOffTimeTime);
	}else{
		oTable.showModal('modal', "截至时间不可为空");
		return ; 
	}
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
	saveByDataSourceCode(t,_dataSourceCode);
}

//重写设置高度函数
function setParntHeigth(heigth){
	if(heigth<600){
		heigth = 600;
	}
	if(parent['setHeigth']){
		parent['setHeigth'](heigth);
	}
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
	var hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
	var minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
	var seconds = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
    return year+"-"+month+"-"+day+" "+hours+":"+minutes+":"+seconds;
}
