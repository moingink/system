
function appendDeliveryPage($insert,title,dataSourceCode,bus_id){
	if(_isBusPage==null){
		_isBusPage=0;
	}
	setTimeout(function(){
		var cache_dataSourceCode =$( "#cache_dataSourceCode" ).val();//页面数据源名称
		$insert.append( "<div id=\"_mcs\" class=\"col-md-12 col-xs-12 col-sm-12 classifiedtitle\" style=\"margin-top:-10px;margin-bottom:10px;\">"+title+"</div>" );
		$insert.append( "<div style=\"overflow: auto; padding:0;\" class=\"col-md-12\" id=\"_mainDelivery\"><iframe  id=\"showMainDelivery\" name=\"showMainDelivery\" width=\"100%\" height=\"300px\" frameborder=\"0\" scrolling=\"auto\" src=\"../pages/mainDelivery.jsp?bus_id="+bus_id+ "&userId="+userId+ "&username="+userName+ "&cache_dataSourceCode=" +dataSourceCode+ "&token="+token+ "&_isBusPage="+_isBusPage+"&mes_st=1  \"></iframe></div>");
	},500);
	

}

function removeDeliveryPage(){
	$("#_mcs").remove();
	$("#_mainDelivery").remove();
}