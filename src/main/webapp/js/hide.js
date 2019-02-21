
function hide(isHide){
	if(isHide == "true"){//设置隐藏
		notEdit();
		$("#fh").css("display","block");//返回
		$("#back").css("display","block");//返回
	}
}

//不可点击
function notEdit(){
	$("button").each(function(){//input设置不可编辑
		$(this).css("display","none");
	});
	$("input").each(function(){//input设置不可编辑
		$(this).attr("disabled",true);
	});
	$("a").each(function(){//隐藏a标签、移除onclick
		var id = $(this).attr("id");
		if(id != "show"){
			$(this).removeAttr('onclick');
			$(this).css("display","none");
		}
	});
	$(".green").each(function(){//隐藏提交按钮
		$(this).css("display","none");
	});
	$("select").each(function(){//select设置不可编辑
		$(this).attr("disabled",true);
	});
	$("textarea").each(function(){//textarea设置不可编辑
		$(this).attr("disabled",true);
	});
	$(".input-group-addon").each(function(){//隐藏时间插件后面的 × 还有日历
		//$(this).css("display","none");
		$(this).removeAttr('onclick');
	});
	$('.form_date').datetimepicker('remove');
	hideFile($('#insPage'));
}

//不可点击
function choiceNotEdit(str){
	$(str+" button").each(function(){//input设置不可编辑
		$(this).css("display","none");
	});
	$(str+" input").each(function(){//input设置不可编辑
		$(this).attr("disabled",true);
	});
	$(str+" a").each(function(){//隐藏a标签、移除onclick
		var id = $(this).attr("id");
		if(id != "show"){
			$(this).removeAttr('onclick');
			$(this).css("display","none");
		}
	});
	$(str+" .green").each(function(){//隐藏提交按钮
		$(this).css("display","none");
	});
	$(str+" select").each(function(){//select设置不可编辑
		$(this).attr("disabled",true);
	});
	$(str+" textarea").each(function(){//textarea设置不可编辑
		$(this).attr("disabled",true);
	});
	$(str+" .input-group-addon").each(function(){
		$(this).removeAttr('onclick');
	});
	$(str+" .form_date").datetimepicker('remove');
}

function localHide($div){
	$div.find("button").each(function(){
		$(this).css("display","none");
	});
	$div.find("input").each(function(){
		$(this).attr("disabled",true);
	});
	$div.find("a").each(function(){
		var id = $(this).attr("id");
		if(id != "show"){
			$(this).removeAttr('onclick');
			$(this).css("display","none");
		}
	});
	$div.find(".green").each(function(){
		$(this).css("display","none");
	});
	$div.find("select").each(function(){
		$(this).attr("disabled",true);
	});
	$div.find("textarea").each(function(){
		$(this).attr("disabled",true);
	});
	$div.find(".input-group-addon").each(function(){
		$(this).removeAttr('onclick');
	});
	$div.find(".form_date").datetimepicker('remove');
	hideFile($div);
}

//局部小面积
function localSmallAreaHide($div){
	$div.find("button").each(function(){
		var onclick = $(this).attr('onclick');
		if(onclick != 'back(this)'){
			$(this).attr("disabled", true);
		}
	});
	$div.find("input").each(function(){
		$(this).attr("disabled", true);
	});
	$div.find("a").each(function(){
		var id = $(this).attr("id");
		if(id != "show"){
			$(this).removeAttr('onclick');
			$(this).css("display","none");
		}
	});
	$div.find("select").each(function(){
		$(this).attr("disabled", true);
	});
	$div.find("textarea").each(function(){
		$(this).attr("disabled", true);
	});
	$div.find(".form_date").datetimepicker('remove');
	hideFile($div);
}
