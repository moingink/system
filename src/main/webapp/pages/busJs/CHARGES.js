buttonJson =[
             {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
             {name:'新增',fun:'tog(this)',buttonToken:'add'},
             {name:'修改',fun:'updateRow(this)',buttonToken:'update'},
             {name:'删除',fun:'delRows(this)',buttonToken:'delete'},
             {name:'语音/短信',fun:'voiceOrMessage(this)',buttonToken:'voiceShortMessage'}
            ];

//初始化
$(function(){
	validJson["fields"]["TRAFFIC"]["validators"]["callback"] = {
		message : '流量 输入值必须大于0',
		callback : function(value, validator, $field) {
			return isGreaterThan(value);
		}
	}
});

//验证是否大于0
function isGreaterThan(val){
	if(0 < Number(val)){
		return true;
	}
	return false;
}

//重写保存函数
function saveByDataSourceCode(t,_dataSourceCode){
	//存储流量以兆为单位
	var traffic_w = 0;
	var trafficUnit = $("#TRAFFIC_UNIT option:selected").val();
	if(trafficUnit === "GB"){
		var traffic = $("#TRAFFIC").val();
		traffic_w = Number(traffic)*1024;
	}else if(trafficUnit === "MB"){
		traffic_w = $("#TRAFFIC").val();
	}
	$("#TRAFFIC_W").val(traffic_w);
	savaByQuery(t,_dataSourceCode,$inspage);
}

//点击语音/短信
function voiceOrMessage(){
	var json = new Array();
	$.ajax({
		type: "GET",
		url: "/system/base?cmd=init",
		data: {"dataSourceCode":"VOICE_OR_MESSAGE"},
		dataType: "json",
		async: false,
		success: function(data) {
			json = data.rows;
			console.log(json);
		}
	});
	var id = "";
	if(json.length != 0){
		id = json[0]["ID"];
	}
	$("#voiceOrMessageReport").attr("src",context+"/pages/publicPage.jsp?pageCode=VOICE_OR_MESSAGE&pageName=语音/短信&ParentPKField=ID&ParentPKValue="+id+"&token="+token+"&state=1&bill_statues=3");
	$("#voiceOrMessageModal").modal('toggle');
}

//嵌入语音/短信Modal
$(document.body).append(
	'<div class="modal fade" id="voiceOrMessageModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">'+
		'<div class="modal-dialog" style="width: 90%;height: 90%">'+
			'<div class="modal-content">'+
				'<div class="modal-header">'+
					'<button type="button" class="close" data-dismiss="modal" aria-hidden="true">'+
						'×'+
					'</button>'+
					'<h4 class="modal-title" id="myModalLabel">语音/短信</h4>'+
				'</div>'+
				'<div class="modal-body"><iframe id="voiceOrMessageReport" name="report" src="" width="100%" height="200" frameborder="0"></iframe></div>'+
				'<div class="modal-footer">'+
					'<button type="button" class="btn btn-inverse" data-dismiss="modal">'+
						'关闭'+
					'</button>'+
				'</div>'+
			'</div>'+
		'</div>'+
	'</div>'
);
