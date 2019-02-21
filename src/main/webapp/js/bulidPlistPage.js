
var PlistPage = function() {
	
	var plist = new Object();
	plist.style="";
	plist.$tr=null;
	plist.data=new Object();
	plist.markArray =new Object();
	plist.load_table=function(type,$tr,json){
		
		//$tdTemp.nextAll().remove(); $tr.html()
		var mark ='';
		if(plist.markArray[type]!=null){
			mark =plist.markArray[type];
		}else{
			mark=$tr.html();
			plist.markArray[type]=mark;
		}
		plist.data[type]=json;
		plist.$tr=$tr;
		var tdArray =plist.findTdArray();
		$tr.show();
		$tr.html("");
		var html="";
		for(var i=0;i<json.length;i++){
			var appendHtml=mark;
			appendHtml=appendHtml.replace("#INDEX#",i);
			for(var j=0;j<tdArray.length;j++){
				var tdVo =tdArray[j];
				if(tdVo.column!=""){
					appendHtml=appendHtml.replace("#"+tdVo.column+"#",json[i][tdVo.column]);
				}
			}
			html+=appendHtml;
		}
		$tr.html(html);
	};


	
	
	
	plist.findProperTyArray=function(){
		var $tr =plist.$tr;
		var tr_length=$tr.children().length;
		var propertyArray =[tr_length];
		
		var i=0;
		$tr.children().each(function(){
		    var value=$(this).attr("property");
		    propertyArray[i]=value;
		    i++;
		});
		
		return propertyArray;
	};
	
	
	plist.findTdArray=function(){
		var $tr =plist.$tr;
		var tr_length=$tr.children().children().length;
		var propertyArray =[tr_length];
		
		var i=0;
		$tr.children().children().each(function(){
		    propertyArray[i]=plist.bulidTdVo($(this));
		    i++;
		});
		
		return propertyArray;
	};
	
	plist.bulidTdVo=function($td){
		
		var tdVo =new Object();
		
		tdVo.column=$td.attr("property");
		tdVo.tdonclick=$td.attr("tdonclick");
		tdVo.$t =$td;
		return tdVo;
		
	};
	
	plist.findTdStyle=function($th){
		if(plist.style==""){
			plist.$tr =$th;
			plist.style=$th.attr("style");
		}
		return plist.style;
	};
	
	plist.findData=function($a){
		var $tr =$a.parent().parent();
		var sup_code =$tr.attr("sup_code");
		var index =$tr.attr("index");
		var jsonData =plist.data[sup_code];
		if(jsonData.length>index){
			return jsonData[index];
		}else{
			return null;
		}
		
	};
	
	plist.findAllJsonData=function(code){
		return plist.data[code];
	};
	
	plist.findJson =function(index,code){
		var jsonData =plist.data[code];
		if(jsonData.length>index){
			return jsonDate[index];
		}else{
			return null;
		}
	};
	

	return plist;
	
};

var plist=new PlistPage();

function bulidPlist(type,$div,params){
	var url =visit_context+'/buttonBase?cmd=button&buttonToken='+type+'&token='+token+params;
	var json =transToServerByPlist(url,'');
	plist.load_table(type,$div, json);
}


function plistOnClick(type,index,title,t){
	var json =plist.data[type][index];
	showForList(type,json,title);
}

function showForList(type,selected,title){
	$('#ViewModal .modal-body').html('');
	//selected["VISIT_BUS_URL"]=escape(selected["VISIT_BUS_URL"]);
	var jsonData = JSON.stringify(selected);
	var $width =$("#ViewModal_width");
	var $title =$("#myModalLabel");
	if(bulidShowPage(type,selected,jsonData,$('#myModalLabel'),$width)){
		$('#ViewModal').modal('show');
	}
	
	
}

function  bulidShowPage(type,json,jsonData,$title,$width){
	var viewUrl= work_flow_context +'/pages/auditPage.jsp?&ts=' + Math.random(1000);
	$.get(viewUrl, {"jsonData":jsonData}, function(data) {
		$('#ViewModal .modal-body').html(data);
		return true;
	});
}


function hide(t){
	$('#ViewModal').modal('hide');
	initNeedHandle();
	initHeedPending();
	//查询
	//queryTable(t);
}



function transToServerByPlist(url, jsonData) {
	var message;
	$.ajax({
		async : false,
		type : "post",
		url : url,
		dataType : "json",
		data : {
			"jsonData" : jsonData
		},
		success : function(data) {
			message = data['message'];
		},
		error:function(XMLHttpRequest, textStatus, errorThrown){
				//登录超时
		    	if(XMLHttpRequest.getResponseHeader("TIMEOUTURL")!=null){
		    		window.top.location.href = XMLHttpRequest.getResponseHeader("TIMEOUTURL");
		    	}
				message ="请求失败";
				alert(message);
				message=[];
			}
	});
	return message;
};



$(function(){
	$(document.body).append(
			'<!--查看数据源展示效果专用模态框-->'+
			'<div class="modal fade" id="ViewModal" tabindex="-1"  style="overflow: auto" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">'+
				'<div class="modal-dialog" id="ViewModal_width" style="width: 90%;height: 90%">'+
					'<div class="modal-content">'+
						'<div class="modal-header">'+
							'<button type="button" class="close" data-dismiss="modal" aria-hidden="true">'+
								'×'+
							'</button>'+
							'<h4 class="modal-title" id="myModalLabel">审批页面</h4>'+
						'</div>'+
						'<div id="ViewModal_body" class="modal-body"></div>'+
						'<div class="modal-footer">'+
							'<button type="button" class="btn btn-inverse" data-dismiss="modal">'+
								'关闭'+
							'</button>'+
						'</div>'+
					'</div>'+
				'</div>'+
			'</div>'
	);
	$('#ViewModal').on('hide.bs.modal', function() {
		if(typeof(initNeedHandle) == "function"){
			initNeedHandle();
		}
		$(this).removeData('modal');
		$("#ViewModal_body").html("");
	});
});


