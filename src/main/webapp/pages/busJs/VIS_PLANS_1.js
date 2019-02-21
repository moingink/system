buttonJson =[
             {name:'查询',fun:'queryTable(this),outInit()',buttonToken:'query'}, 
             {name:'新增计划',fun:'add(this)'},
             {name:'修改计划',fun:'update(this)'},
             {name:'复制计划',fun:'copy(this)'}
			];

var qusurl = '/system/cuma?getTableList&dataSourceCode=VIS_PLANS_1&token='+token;
oTable.initCols($("#table"), colurl + findPageParamByDataSourceCode('VIS_PLANS_1'),qusurl);

//重写列表加载函数
function bulidListPageForQusUrl($t,_dataSourceCode,_listPageParam,_qusurl){
	if(_qusurl!=null){
		_qusurl= _qusurl + findPageParamByDataSourceCode(_dataSourceCode)+_listPageParam;
	}
	oTable.initCols($t, colurl + findPageParamByDataSourceCode(_dataSourceCode),_qusurl);
}

setTimeout(replace(),1000);

function outInit(){
	setTimeout(replace(),1000);
}

//添加变更状态下拉框搜索
function replace(){
	if(!$("#SEARCH-EXECUTION_STATUS").length>0){
   		var html = "<div class='col-md-4' style='margin-top:10px;display:inline'>"+
					   "<div class='form-group'>"+
						   "<div class='col-md-4' style='white-space:nowrap;'>"+
						   		"<label class='control-label' style='text-align: left;line-height: 29px;' for='SEARCH-EXECUTION_STATUS'>执行状态：</label>"+
						   "</div>"+
						   "<div class='col-md-8'>"+
							   "<select class='form-control' id='SEARCH-EXECUTION_STATUS' name='SEARCH-EXECUTION_STATUS'>"+
								   "<option selected='selected' value=''>==请选择==</option>"+
								   "<option value='0'>未执行</option>"+
								   "<option value='1'>已执行</option>"+
							   "</select>"+
						   "</div>"+
					   "</div>"+
				   "</div>";
   		$("#queryParam").append(html);
	}
}

//设置高度
function setParntHeigth(heigth){
	if(parent['setHeigth']){
		parent['setHeigth'](850);
	}
}

/*新增计划*/
function add(t){
	window.location.href=context+"/pages/cusPlanVisit.jsp?field=ID&fieValue=&planId=&currentUserId="+userId+"&currentUserName="+userName+"&token="+token+"&keyWord=add";
}

/*修改计划*/
function update(t){
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('modal', "请选择一条数据进行操作");
		return;
	}
	window.location.href=context+"/pages/cusPlanVisit.jsp?field=ID&fieValue="+selected[0]["ID"]+"&planId="+selected[0]["PLAN_ID"]+"&currentUserId="+userId+"&currentUserName="+userName+"&token="+token+"&keyWord=update";
}

/*复制计划*/
function copy(t){
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('modal', "请选择一条数据进行操作");
		return;
	}
	window.location.href=context+"/pages/cusPlanVisit.jsp?field=ID&fieValue="+selected[0]["ID"]+"&planId="+selected[0]["PLAN_ID"]+"&currentUserId="+userId+"&currentUserName="+userName+"&token="+token+"&keyWord=copy&pageCode=test";
}

//嵌入详情页面
$(document.body).append(
	'<div class="modal fade" id="visPlansModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">'+
		'<div class="modal-dialog" style="width: 90%; min-width:1030px;">'+
			'<div class="modal-content">'+
				'<div class="modal-header">'+
					'<button type="button" class="close" data-dismiss="modal" aria-hidden="true">'+
						'×'+
					'</button>'+
					'<h4 class="modal-title" id="myModalLabel">业务详情</h4>'+
				'</div>'+
				'<div class="modal-body"><iframe id="visPlansReport" name="report" src=""  width="100%" height="310" frameborder="0" style="min-width:1005px;"></iframe></div>'+
				'<div class="modal-footer">'+
					'<button type="button" class="btn btn-inverse" data-dismiss="modal">'+
						'关闭'+
					'</button>'+
				'</div>'+
			'</div>'+
		'</div>'+
	'</div>'
);

//双击
function dblClickFunction(row,tr){
	var json = JSON.parse(JSON.stringify(row));
	$("#visPlansReport").attr("src","/system/pages/cusPlanVisitDetail.jsp?pageCode=VIS_PLANS_1&ParentPKField=ID&ParentPKValue="+json["ID"]+"&token="+token);
	$("#visPlansModal").modal('toggle');
}


//修改剩余拜访次数
function updateVisFreq(cusId,planId){
	//剩余频次
	var visFreq = surplusVisFreq(cusId,planId);
	//更新频次
	$.ajax({
		type: "POST",
		url: '/system/cuma?updateVisPlans&vis_freq='+visFreq+'&plan_id='+planId+'&cus_id='+cusId,
		async: false
	});
}

//剩余频次
function surplusVisFreq(cusId,planId){
	var surplusVisFreq = '';
	$.ajax({
		type: "GET",
		url: '/system/cuma?surplusVisFreq&plan_id='+planId+'&cus_id='+cusId,
		async: false,
		dataType: "text",
		success: function(data) {
			console.log("剩余次数"+data);
			surplusVisFreq = data;
		}
	});
	return surplusVisFreq;
}

var param = "&dataSourceCode="+dataSourceCode+"&SEARCH-ID=";

//行内新增
function xz(id){
	var record = querySingleRecord(param+id);
	var planId = record["PLAN_ID"];
	var cusId = record["CUS_ID"];
	var surplusVisFreqs = surplusVisFreq(cusId,planId);
	if(surplusVisFreqs > 0){
		var src = context+"/pages/cusVisitRecordPage.jsp?pageCode=VIS_RECORD&pageName=拜访记录&state=1&keyWord=plan&keyWords=xz&ParentPKField=PLANS_ID&ParentPKValue="+id+"&planId="+record['PLAN_ID']+
							   "&planName="+encodeURIComponent(record['PLAN_NAME'])+"&cusId="+record['CUS_ID']+"&cusName="+record['CUS_NAME']+"&theParty="+record['ATT_PERSON_NAME']+"&thePartyId="+record['ATT_PERSON_ID']+"&visitType="+record['VIS_TYPE']+
							   "&palnContent="+encodeURIComponent(record['PALN_CONTENT'])+"&palnTime="+record['PALN_TIME']+"&date="+getDate()+"&token="+token;
		window.location.href = src;
	}else{
		oTable.showModal('提示', '没有拜访次数了');
	}
}

//行内修改
function xg(id){
	var record = querySingleRecord(param+id);
	window.location.href = context+"/pages/cusVisitRecordPage.jsp?pageCode=VIS_RECORD&pageName=拜访记录&state=1&keyWord=plan&keyWords=xg&ParentPKField=PLANS_ID&ParentPKValue="+id+"&planId="+record['PLAN_ID']+
					      "&planName="+record["PLAN_NAME"]+"&cusId="+record['CUS_ID']+"&cusName="+record['CUS_NAME']+"&date="+getDate()+"&token="+token;
}

//行内查看
function ck(id){
	var record = querySingleRecord(param+id);
	window.location.href = context+"/pages/cusVisitRecordPage.jsp?pageCode=VIS_RECORD&pageName=拜访记录&state=1&keyWord=plan&keyWords=ck&ParentPKField=PLANS_ID&ParentPKValue="+id+"&planId="+record['PLAN_ID']+
						   "&planName="+record['PLAN_NAME']+"&cusId="+record['CUS_ID']+"&cusName="+record['CUS_NAME']+"&date="+getDate()+"&token="+token;
}

//当前时间获取
function getDate(){
	var myDate = new Date();
	var year = myDate.getFullYear();    
	var month = myDate.getMonth()+1;
	var day = myDate.getDate();
	var hours = myDate.getHours();
	var minutes = myDate.getMinutes();
	var seconds = myDate.getSeconds();
	if (month >= 1 && month <= 9) {
		   month = "0" + month;
	}
	if (day >= 0 && day <= 9) {
		day = "0" + day;
	}
	return year+"-"+month+"-"+day+" "+hours+":"+minutes+":"+seconds;
}
