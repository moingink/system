buttonJson =[];

var flowAutocomplete = new Array();//流量联想查询数据

//初始化
$(function(){
	setUp();
	getCharges();
});

//页面设置
function setUp(){
	$("#SINGLE_OR_TOTAL").val("total");
	$("#STANDARD_PRICE,#MESSAGE_STANDARD_PRICE,#PHONETICS_STANDARD_PRICE,#STANDARD_PRICE_SUM,#DISCOUNT_RATE_SUM").attr("disabled",true);
	$("#PHONETICS,#MESSAGE,#APPLICATION_PRICE_SUM").attr({
	   "onkeyup": "onlyNumber(this)",
	   "onblur": "onlyNumber(this),inputBlur(this)"
	});
}

//获取资费标准数据
function getCharges(){
	$.ajax({
		type: "GET",
		url: "/system/business?getCharges",
		dataType: "json",
		async: false,
		success: function(data) {
			flowAutocomplete = data;
		}
	});
}

//流量联想查询
$("#FLOW").bigAutocomplete({
	width: 185,
	data: flowAutocomplete,
	callback: function(data){
		var title = data.title;//流量和单位字符串
		var titleNext = title.substr(title.length-2);//单位
		if(titleNext === "MB" || titleNext === "GB"){
			$("#FLOW").val(title.substr(0,title.length-2));//流量赋值
			$("#FLOW_UNIT").val(titleNext);//流量单位赋值
		}
		$("#STANDARD_PRICE").val(data.standardPrice);//流量标准价格赋值
		linkage();
	}
});

//流量单位监听
$("#FLOW_UNIT").change(function() {
	getStandardPrice();
	linkage();
});

//输入框输入限制
function onlyNumber(obj){
	var id = obj.id;
	if(id == "PHONETICS" || id == "MESSAGE"){
		//只能输入整数
		obj.value = obj.value.replace(/[^\d]/g,'');
	}else{
		//先把非数字的都替换掉，除了数字和. 
		obj.value = obj.value.replace(/[^\d\.]/g,'');
		//必须保证第一个为数字而不是.   
		obj.value = obj.value.replace(/^\./g,'');
		//保证只有出现一个.而没有多个.   
		obj.value = obj.value.replace(/\.{2,}/g,'.');
		//保证.只出现一次，而不能出现两次以上   
		obj.value = obj.value.replace('.','$#$').replace(/\./g,'').replace('$#$','.');
		//只能输入两个小数 
		obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3');
	}
	if(id == "FLOW"){//流量
		getStandardPrice();
	}else if(id == "PHONETICS"){//语音
		getPhoneticsStandardPrice();
	}else if(id == "MESSAGE"){//短信
		getMessageStandardPrice();
	}
	linkage();
}

//根据公式获取流量标准价格
function getStandardPrice(){
	var flow = $("#FLOW").val();//流量
	if(flow != ""){
		var flowUnit = $("#FLOW_UNIT option:selected").val();//流量单位
		if(flowUnit == "GB"){
			flow = flow*1024;
		}
		$.ajax({
			type: "GET",
			url: "/system/business?getStandardPrice",
			data: {"flow":flow},
			dataType: "text",
			async: false,
			success: function(data) {
				$("#STANDARD_PRICE").val(data);
			}
		});
	}
}

//根据设置标准获取语音标准价格
function getPhoneticsStandardPrice(){
	var phonetics = $("#PHONETICS").val();
	$.ajax({
		type: "GET",
		url: "/system/business?getPhoneticsStandardPrice",
		data:{"phonetics":phonetics},
		dataType: "text",
		async: false,
		success: function(data) {
			$("#PHONETICS_STANDARD_PRICE").val(data);
		}
	});
}

//根据设置标准获取短信标准价格
function getMessageStandardPrice(){
	var message = $("#MESSAGE").val();
	$.ajax({
		type: "GET",
		url: "/system/business?getMessageStandardPrice",
		data:{"message":message},
		dataType: "text",
		async: false,
		success: function(data) {
			$("#MESSAGE_STANDARD_PRICE").val(data);
		}
	});
}

//联动
function linkage(){
	var standardPrice = $("#STANDARD_PRICE").val();//流量标准价格
	if(standardPrice == ""){
		standardPrice = 0;
	}else{
		standardPrice = Number(standardPrice);
	}
	
	var phoneticsStandardPrice = $("#PHONETICS_STANDARD_PRICE").val();//语音标准价格
	if(phoneticsStandardPrice == ""){
		phoneticsStandardPrice = 0;
	}else{
		phoneticsStandardPrice = Number(phoneticsStandardPrice);
	}
	
	var messageStandardPrice = $("#MESSAGE_STANDARD_PRICE").val();//短信标准价格
	if(messageStandardPrice == ""){
		messageStandardPrice = 0;
	}else{
		messageStandardPrice = Number(messageStandardPrice);
	}

	
	var standardPriceSum = standardPrice+phoneticsStandardPrice+messageStandardPrice
	$("#STANDARD_PRICE_SUM").val(standardPriceSum.toFixed(2));
	
	var applicationPriceSum = $("#APPLICATION_PRICE_SUM").val();
	if(standardPriceSum == ""){
		$("#DISCOUNT_RATE_SUM").val(0);
	}else{
		var discountRateSum = applicationPriceSum/standardPriceSum*100;
		$("#DISCOUNT_RATE_SUM").val(discountRateSum.toFixed(1));
	}
}
