buttonJson =[
             {name:'查询',fun:'queryTable(this)',buttonToken:'query'}
             // {name:'新增',fun:'tog(this),pd()',buttonToken:'add'},
             // {name:'修改',fun:'updateRow(this),pd()',buttonToken:'update'},
             // {name:'删除',fun:'delRows(this)',buttonToken:'delete'},
             // {name:'导出',fun:'outmessage(this)',buttonToken:'outmessage'}          
             ];
             

function dblClickFunction(row,tr){
	
	
	show_dbclick(JSON.parse(JSON.stringify(row)),"业务详情");
}

function show_dbclick(selected,title){
	$('#bus_message_dbclick').html('');
	$('#myModalLabel_dbclick').html(title);
	
	
	var mainPageParam =pageParamFormat(ParentId +" ="+selected["ID"]);
    mainPageParam=mainPageParam+"&showType=INSUP";
    
    var mainPageParams =pageParamFormat(ParentIded +" ="+selected["ID"]);
    mainPageParams=mainPageParams+"&showType=INSUP";
    
     var mainPageParamses =pageParamFormat("id = " +selected["ID"]);
    mainPageParamses=mainPageParamses+"&showType=INSUP";
    
    
    
    
	bulidMaintainPage($("#bus_message_dbclick"),dataSourceCode,mainPageParam);
	
	$("#bus_message_dbclick").find("[id]").each(function(){
			$(this).attr("disabled",true);
	});
	$("#bus_message_dbclick span").each(function(){
			$(this).attr("onclick","");
	});
	
	
	bulidMaintainPage($("#bus_message_dbclick"),"BUS_CONTRACT_SUPPLEMENT",mainPageParamses);
	
	$("#bus_message_dbclick").find("[id]").each(function(){
			$(this).attr("disabled",true);
	});
	$("#bus_message_dbclick span").each(function(){
			$(this).attr("onclick","");
	});
	
	
	bulidMaintainPage($("#bus_message_dbclick"),"CONTRACT_HANDOVER_PLACE_FILE",mainPageParams);
	
	$("#bus_message_dbclick").find("[id]").each(function(){
			$(this).attr("disabled",true);
	});
	$("#bus_message_dbclick span").each(function(){
			$(this).attr("onclick","");
	});
	
	$('#ViewModal_dbclick').modal('show');
}