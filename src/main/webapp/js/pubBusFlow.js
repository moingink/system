

function sendBusFlow(node,task_id,jsonData){
	
	var params ="&NODE_CODE="+node+"&TASK_ID="+task_id;
	
	var returnMessage =busFlowToServer(findBusUrlByButtonTonken('busFlow',params,dataSourceCode),JSON.stringify(jsonData));
	
	oTable.showModal('提示', returnMessage['message']);
}



function busFlowToServer(url,jsonData){
		var message;
		$.ajax({
    		async: false,
    		type: "post",
			url: url,
			dataType: "json",
			data:{"jsonData":jsonData},
			success: function(data){
				message =data;
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){
				//登录超时
		    	if(XMLHttpRequest.getResponseHeader("TIMEOUTURL")!=null){
		    		window.top.location.href = XMLHttpRequest.getResponseHeader("TIMEOUTURL");
		    	}
				message ="请求失败";
			}
		});
    	return message;
    };