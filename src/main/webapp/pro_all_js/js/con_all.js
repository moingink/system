function message_all(id){
	 	$.ajax({
			type: "POST",
			url:"/system/base?cmd=find_buss_id&id="+id,
			//data: {ID:[pid],IN_BUSINESS:[name]}, 
			dataType: "json",
			async: false,
			cache: false,
			contentType: false,
			processData: false,
			success: function(data) {
				return data;
			}, error: function(data) {
				var da=JSON.stringify(data);
				alert("查询失败，请联系管理员！" + "\t" + "错误代码：" + da);
			}
			});
	 
	
}