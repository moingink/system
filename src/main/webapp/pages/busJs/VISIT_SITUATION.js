buttonJson =[
              {name:'查询',fun:'queryTable(this)',buttonToken:'query'}
			];
			
function show_dbclick(selected,title){
	$('#bus_message_dbclick').html('');
	$('#myModalLabel_dbclick').html(title);
	var mainPageParam =pageParamFormat(ParentId +" ="+selected["ID"]);
    mainPageParam=mainPageParam+"&showType=INSUP";
	bulidMaintainPage($("#bus_message_dbclick"),dataSourceCode,mainPageParam);
	$('#bus_message_dbclick').children().eq(1).attr('class','col-md-6');
	$('#bus_message_dbclick').children().eq(1).css('display','inline');
	$('#bus_message_dbclick').children().eq(1).css('margin-top','10px');
	$('#bus_message_dbclick').children().eq(1).find('input').attr('type','text');
	$('#bus_message_dbclick').children().eq(2).attr('class','col-md-6');
	$('#bus_message_dbclick').children().eq(2).css('display','inline');
	$('#bus_message_dbclick').children().eq(2).css('margin-top','10px');
	$('#bus_message_dbclick').children().eq(2).find('input').attr('type','text');
	$("#bus_message_dbclick").find("[id]").each(function(){
			$(this).attr("disabled",true);
	});
	$("#bus_message_dbclick span").each(function(){
			$(this).attr("onclick","");
	});
	$('#ViewModal_dbclick').modal('show');
}