buttonJson =[
             {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
             {name:'新增',fun:'tog(this)',buttonToken:'insertWithCache'},
             {name:'修改',fun:'updateRow(this)',buttonToken:'updateWithCache'},
             {name:'删除',fun:'delRows(this)',buttonToken:'deleteWithCache'}
			];


$(document).ready(function(){
	$("#FTP_NAME").prop("disabled",true);
	$("#WORK_DIRECTORY_NAME").prop("disabled",true);
	$("#SY_REGEIST_NAME").prop("disabled",true);
	$("#SY_FLAG").prop("disabled",true);
	$("#FLOW_NAME").prop("disabled",true);
	
	$("#SEARCH_PATH").prop("disabled",true);
	$("#FILE_NAME").prop("disabled",true);
	
	$("#RESPONSE_FILE").prop("disabled",true);
	$("#DESCRIPTION_FILE").prop("disabled",true);
	$("#FILE_TYPE").prop("disabled",true);
	$("#REVERSE").prop("disabled",true);
	$("#REVERSE_DAY").prop("disabled",true);
	$("#ANALYTICAL_RULE").prop("disabled",true);
	$("#SEARCH_PATH1").prop("disabled",true);
	$("#SEARCH_PATH2").prop("disabled",true);
	$("#FILE_NAME1").prop("disabled",true);
	$("#FILE_NAME2").prop("disabled",true);
	$("#FILE_NAME3").prop("disabled",true);
	$("#FILE_NAME4").prop("disabled",true);
	$("#FILE_NAME5").prop("disabled",true);
	$("#FILE_NAME6").prop("disabled",true);
	$("#FILE_NAME7").prop("disabled",true);
	$("#FILE_NAME8").prop("disabled",true);
	var FILE_NAME1 = document.getElementById('FILE_NAME1');
	var p = FILE_NAME1.parentNode;
	var o = p.parentNode;
	o.style.visibility="hidden";
	
	var FILE_NAME1 = document.getElementById('FILE_NAME3');
	var p = FILE_NAME1.parentNode;
	var o = p.parentNode;
	var getFirstChild = o.firstChild;
	getFirstChild.style.visibility="hidden";
	
	var FILE_NAME1 = document.getElementById('FILE_NAME4');
	var p = FILE_NAME1.parentNode;
	var o = p.parentNode;
	//var getFirstChild = o.firstChild;
	o.style.visibility="hidden";
	
	var FILE_NAME1 = document.getElementById('FILE_NAME5');
	var p = FILE_NAME1.parentNode;
	var o = p.parentNode;
	var getFirstChild = o.firstChild;
	getFirstChild.style.visibility="hidden";
	
	var FILE_NAME1 = document.getElementById('FILE_NAME6');
	var p = FILE_NAME1.parentNode;
	var o = p.parentNode;
	var getFirstChild = o.firstChild;
	getFirstChild.style.visibility="hidden";
	
	var FILE_NAME1 = document.getElementById('FILE_NAME7');
	var p = FILE_NAME1.parentNode;
	var o = p.parentNode;
	//var getFirstChild = o.firstChild;
	o.style.visibility="hidden";
	
	var FILE_NAME1 = document.getElementById('FILE_NAME8');
	var p = FILE_NAME1.parentNode;
	var o = p.parentNode;
	var getFirstChild = o.firstChild;
	getFirstChild.style.visibility="hidden";
	
	
	var FILE_NAME2 = document.getElementById('FILE_NAME2');
	FILE_NAME2.style.setProperty('width','100px');
	var FILE_NAME3 = document.getElementById('FILE_NAME3');
	FILE_NAME3.style.setProperty('margin-left','10px');
	FILE_NAME3.style.setProperty('width','100px');
	var FILE_NAME4 = document.getElementById('FILE_NAME4');
	var FILE_NAME5 = document.getElementById('FILE_NAME5');
	FILE_NAME5.style.setProperty('margin-left','140px');
	FILE_NAME5.style.setProperty('margin-top','-45px');
	FILE_NAME5.style.setProperty('width','100px');
	var FILE_NAME6 = document.getElementById('FILE_NAME6');
	FILE_NAME6.style.setProperty('margin-left','150px');
	FILE_NAME6.style.setProperty('margin-top','-45px');
	FILE_NAME6.style.setProperty('width','100px');
	var FILE_NAME8 = document.getElementById('FILE_NAME8');
	FILE_NAME8.style.setProperty('margin-left','280px');
	FILE_NAME8.style.setProperty('margin-top','-90px');
	FILE_NAME8.style.setProperty('width','100px');
	
	
	//var FILE_NAME1 = document.getElementById('FILE_NAME1');
	//FILE_NAME1.style.setProperty('margin-left','-200px');
	
	
	

});

	$("#STANDARD_TYPE").change(function(){
		var type = $("#STANDARD_TYPE").val();
		if(type == '1'){
			$("#SEARCH_PATH").prop("disabled",false);
			$("#FILE_NAME").prop("disabled",false);
			$("#RESPONSE_FILE").prop("disabled",true);
			$("#DESCRIPTION_FILE").prop("disabled",true);
			$("#FILE_TYPE").prop("disabled",true);
			$("#REVERSE").prop("disabled",true);
			$("#REVERSE_DAY").prop("disabled",true);
			$("#ANALYTICAL_RULE").prop("disabled",true);
			$("#SEARCH_PATH1").prop("disabled",true);
			$("#SEARCH_PATH2").prop("disabled",true);
			$("#FILE_NAME1").prop("disabled",true);
			$("#FILE_NAME2").prop("disabled",true);
			$("#FILE_NAME3").prop("disabled",true);
			$("#FILE_NAME4").prop("disabled",true);
			$("#FILE_NAME5").prop("disabled",true);
			$("#FILE_NAME6").prop("disabled",true);
			$("#FILE_NAME7").prop("disabled",true);
			$("#FILE_NAME8").prop("disabled",true);
			var RESPONSE_FILE = document.getElementById('RESPONSE_FILE');
			RESPONSE_FILE.outerHTML = RESPONSE_FILE.outerHTML;
			var DESCRIPTION_FILE = document.getElementById('DESCRIPTION_FILE');
			DESCRIPTION_FILE.outerHTML = DESCRIPTION_FILE.outerHTML;
			var FILE_TYPE = document.getElementById('FILE_TYPE');
			FILE_TYPE.outerHTML = FILE_TYPE.outerHTML;
			var REVERSE = document.getElementById('REVERSE');
			REVERSE.outerHTML = REVERSE.outerHTML;
			var REVERSE_DAY = document.getElementById('REVERSE_DAY');
			REVERSE_DAY.outerHTML = REVERSE_DAY.outerHTML;
			var ANALYTICAL_RULE = document.getElementById('ANALYTICAL_RULE');
			ANALYTICAL_RULE.outerHTML = ANALYTICAL_RULE.outerHTML;
			var SEARCH_PATH1 = document.getElementById('SEARCH_PATH1');
			SEARCH_PATH1.outerHTML = SEARCH_PATH1.outerHTML;
			var SEARCH_PATH2 = document.getElementById('SEARCH_PATH2');
			SEARCH_PATH2.outerHTML = SEARCH_PATH2.outerHTML;
			var FILE_NAME1 = document.getElementById('FILE_NAME1');
			FILE_NAME1.outerHTML = FILE_NAME1.outerHTML;
			var FILE_NAME2 = document.getElementById('FILE_NAME2');
			FILE_NAME2.outerHTML = FILE_NAME2.outerHTML;
			var FILE_NAME3 = document.getElementById('FILE_NAME3');
			FILE_NAME3.outerHTML = FILE_NAME3.outerHTML;
			var FILE_NAME4 = document.getElementById('FILE_NAME4');
			FILE_NAME4.outerHTML = FILE_NAME4.outerHTML;
			var FILE_NAME5 = document.getElementById('FILE_NAME5');
			FILE_NAME5.outerHTML = FILE_NAME5.outerHTML;
			var FILE_NAME6 = document.getElementById('FILE_NAME6');
			FILE_NAME6.outerHTML = FILE_NAME6.outerHTML;
			var FILE_NAME7 = document.getElementById('FILE_NAME7');
			FILE_NAME7.outerHTML = FILE_NAME7.outerHTML;
			var FILE_NAME8 = document.getElementById('FILE_NAME8');
			FILE_NAME8.outerHTML = FILE_NAME8.outerHTML;
		}else if(type == '2'){
			
			
			$("#SEARCH_PATH").prop("disabled",true);
			$("#FILE_NAME").prop("disabled",true);
			var SEARCH_PATH = document.getElementById('SEARCH_PATH');
			SEARCH_PATH.outerHTML = SEARCH_PATH.outerHTML;
			var FILE_NAME = document.getElementById('FILE_NAME');
			FILE_NAME.outerHTML = FILE_NAME.outerHTML;
			$("#RESPONSE_FILE").prop("disabled",false);
			$("#DESCRIPTION_FILE").prop("disabled",false);
			$("#FILE_TYPE").prop("disabled",false);
			$("#REVERSE").prop("disabled",false);
			$("#REVERSE_DAY").prop("disabled",false);
			$("#ANALYTICAL_RULE").prop("disabled",false);
			$("#SEARCH_PATH1").prop("disabled",false);
			$("#SEARCH_PATH2").prop("disabled",false);
			$("#FILE_NAME1").prop("disabled",false);
			$("#FILE_NAME2").prop("disabled",false);
			$("#FILE_NAME3").prop("disabled",false);
			$("#FILE_NAME4").prop("disabled",false);
			$("#FILE_NAME5").prop("disabled",false);
			$("#FILE_NAME6").prop("disabled",false);
			$("#FILE_NAME7").prop("disabled",false);
			$("#FILE_NAME8").prop("disabled",false);
		}else{
			var SEARCH_PATH = document.getElementById('SEARCH_PATH');
			SEARCH_PATH.outerHTML = SEARCH_PATH.outerHTML;
			var FILE_NAME = document.getElementById('FILE_NAME');
			FILE_NAME.outerHTML = FILE_NAME.outerHTML;
			var RESPONSE_FILE = document.getElementById('RESPONSE_FILE');
			RESPONSE_FILE.outerHTML = RESPONSE_FILE.outerHTML;
			var DESCRIPTION_FILE = document.getElementById('DESCRIPTION_FILE');
			DESCRIPTION_FILE.outerHTML = DESCRIPTION_FILE.outerHTML;
			var FILE_TYPE = document.getElementById('FILE_TYPE');
			FILE_TYPE.outerHTML = FILE_TYPE.outerHTML;
			var REVERSE = document.getElementById('REVERSE');
			REVERSE.outerHTML = REVERSE.outerHTML;
			var REVERSE_DAY = document.getElementById('REVERSE_DAY');
			REVERSE_DAY.outerHTML = REVERSE_DAY.outerHTML;
			var ANALYTICAL_RULE = document.getElementById('ANALYTICAL_RULE');
			ANALYTICAL_RULE.outerHTML = ANALYTICAL_RULE.outerHTML;
			var SEARCH_PATH1 = document.getElementById('SEARCH_PATH1');
			SEARCH_PATH1.outerHTML = SEARCH_PATH1.outerHTML;
			var SEARCH_PATH2 = document.getElementById('SEARCH_PATH2');
			SEARCH_PATH2.outerHTML = SEARCH_PATH2.outerHTML;
			var FILE_NAME1 = document.getElementById('FILE_NAME1');
			FILE_NAME1.outerHTML = FILE_NAME1.outerHTML;
			var FILE_NAME2 = document.getElementById('FILE_NAME2');
			FILE_NAME2.outerHTML = FILE_NAME2.outerHTML;
			var FILE_NAME3 = document.getElementById('FILE_NAME3');
			FILE_NAME3.outerHTML = FILE_NAME3.outerHTML;
			var FILE_NAME4 = document.getElementById('FILE_NAME4');
			FILE_NAME4.outerHTML = FILE_NAME4.outerHTML;
			var FILE_NAME5 = document.getElementById('FILE_NAME5');
			FILE_NAME5.outerHTML = FILE_NAME5.outerHTML;
			var FILE_NAME6 = document.getElementById('FILE_NAME6');
			FILE_NAME6.outerHTML = FILE_NAME6.outerHTML;
			var FILE_NAME7 = document.getElementById('FILE_NAME7');
			FILE_NAME7.outerHTML = FILE_NAME7.outerHTML;
			var FILE_NAME8 = document.getElementById('FILE_NAME8');
			FILE_NAME8.outerHTML = FILE_NAME8.outerHTML;
			$("#SEARCH_PATH").prop("disabled",true);
			$("#FILE_NAME").prop("disabled",true);
			$("#RESPONSE_FILE").prop("disabled",true);
			$("#DESCRIPTION_FILE").prop("disabled",true);
			$("#FILE_TYPE").prop("disabled",true);
			$("#REVERSE").prop("disabled",true);
			$("#REVERSE_DAY").prop("disabled",true);
			$("#ANALYTICAL_RULE").prop("disabled",true);
			$("#SEARCH_PATH1").prop("disabled",true);
			$("#SEARCH_PATH2").prop("disabled",true);
			$("#FILE_NAME1").prop("disabled",true);
			$("#FILE_NAME2").prop("disabled",true);
			$("#FILE_NAME3").prop("disabled",true);
			$("#FILE_NAME4").prop("disabled",true);
			$("#FILE_NAME5").prop("disabled",true);
			$("#FILE_NAME6").prop("disabled",true);
			$("#FILE_NAME7").prop("disabled",true);
			$("#FILE_NAME8").prop("disabled",true);
		}
	});




























