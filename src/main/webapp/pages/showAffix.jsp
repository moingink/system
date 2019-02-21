<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	request.setCharacterEncoding("utf-8");
	String pageCode = request.getParameter("selectAffixValues");
	/*参数格式
	 [{1.附件类型1（1.是本页面；2.非本页面），2.bs_code(数据源编码)，3.batchNo(批次号)，4.affixname(附件显示名称)},
	  {1.附件类型2（1.是本页面；2.非本页面），2._bs_code(数据源编码)，3._bs_code_field(数据源表中存放批次号字段)，4._bs_code_where_field(数据源表中存放批次号字段)，5.affixname(附件显示名称)}...]
	*/
	String path = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<title>附件展示模板</title>
<link rel="stylesheet" href="../vendor/bootstrap/css/bootstrap.css">
<link rel="stylesheet" href="../vendor/bootstrap/css/bootstrap.min.css">
<script src="../vendor/jquery/jquery.min.js"></script>
<script src="../vendor/bootstrap/js/bootstrap.min.js"></script>

<style type="text/css">
*{margin: 0px;padding: 0px;border: 0px}
body,div,dl,dt,dd,ul,ol,li,h1,h2,h3,h4,h5,h6,pre,code,form,fieldset,legend,input,textarea,p,blockquote,th,td,hr,button,article,aside,details,figcaption,figure,footer,header,hgroup,menu,nav,section {
    margin:0;
    padding:0;
}
#caption{
	text-align: left;
}
a{
	cursor:pointer;
}
.table{
	width:98%;
	
	margin: 0 auto;
}
th{
	text-align: center;
}
td{
	//border-right: 1px solid #2196f3;
}
</style>
</head>
<body>
<div class="col-md-12 col-xs-12 col-sm-12 classifiedtitle" style="margin-bottom:10px; clear:both;">业务附件</div>
	<div class="panel" style="padding:5px;">
		
		
			<div class="col-md-12 col-xs-12 col-sm-12 panel panel-primary">
				
					<table class="table table-hover table-bordered">
		<!-- <caption id="caption">111</caption> -->
		
		<br>
		<thead class='thead'>
			<tr>
				<th>附件类型名称</th>
				<th>附件文本名称</th>
			</tr>
		</thead>
		<tbody class='tbody' id="tbody">
			<!-- <tr><td>前评估资料</td><td style="color: red">此商机前置业务未上传过任何附件</td></tr>
			<tr><td>应标文件及中标依据</td><td style="color: red">此商机前置业务未上传过任何附件</td></tr>
			<tr><td>客户合同</td><td style="color: red">此商机前置业务未上传过任何附件</td></tr> -->
			<!-- <tr>
				<td>前评估资料</td>
				<td>
						<div class="col-md-4 listmessage">
							<div class="col-md-9 listhide" data-toggle="tooltip" data-placement="top" title="153735541415645646461456464656454646464460(1).jpg">
								153735541415645646461456464656454646464460(1).jpg
							</div>
							<div class="col-md-3 listbtn">
								<a href="" class="btnlist"  data-toggle="tooltip" data-placement="top" title="下载"><span class="glyphicon glyphicon-download-alt"></span></a>
								<a href="" class="btnlist"  data-toggle="tooltip" data-placement="top" title="删除"><span class="glyphicon glyphicon-trash"></span></a>
							</div>
						</div>
						
						<div class="col-md-4 listmessage">
							<div class="col-md-9 listhide" data-toggle="tooltip" data-placement="top" title="153735541415645646461456464656454646464460(1).jpg">
								153735541415645646461456464656454646464460(1).jpg
							</div>
							<div class="col-md-3 listbtn">
								<a href="" class="btnlist"  data-toggle="tooltip" data-placement="top" title="下载"><span class="glyphicon glyphicon-download-alt"></span></a>
								<a href="" class="btnlist"  data-toggle="tooltip" data-placement="top" title="删除"><span class="glyphicon glyphicon-trash"></span></a>
							</div>
						</div>
						
						
					</div>
					
					
				</td>
			</tr> -->

		</tbody>
	</table>
				
				
				
			</div>
		</div>	
		


<!-- <button type='button' id='but' onclick='spelling()'>123</button> -->
</body>
<script type="text/javascript">

/**
 * 页面附件集中展示
 * @param bs_code 主页面数据源编码 -- 根据找到此编码的元数据编码即表名
 * @param batchNo 批次号同业务同一批次一个批次号
 * @param _bs_code 关联附件数据源编码 -- 根据找到此编码的元数据编码即表名
 */
function showAffix(bs_code,batchNo,_bs_code,_bs_codeField,_bs_codeValue){
	if (batchNo!="") {//正常业务
		selectAffixByBatchNo();
	} else {//关联业务
		
	}
}

function selectAffixByBatchNo(bs_code,batchNo){
	// 正常业务
	
}

function MouseOver(t){
	var v1 = $(t).attr("fileId");
	var v2 = $(t).text();
	var htmlString="<div align=center >"+
	"<a href=\"/document/base?cmd=downloadAffix&fid="+v1+"\">&nbsp;下载&nbsp;</a>"+
	"</div>";
	$(t).popover({
    	trigger:'manual',//manual 触发方式  
        placement : 'top',    
       // title:'<div align=center>操作</div>',  
        html: true,   
        content : htmlString,  //这里可以直接写字符串，也可以 是一个函数，该函数返回一个字符串；  
        animation: false  
     }).on({
	mouseover : function(){	},
	mouseout : function(){
		var _this=t;
		setTimeout(function () { 
    	if (!$(".popover:hover").length) {  
    		$(_this).popover("hide");
    	}
		});
	}
});
var _this=t;
$(t).popover("show");
$(t).siblings(".popover").on("mouseleave", function () {
	setTimeout(function () {
		$(_this).popover('hide');
		})
});
}		

function addData(v){
	alert("调用addData()传来的数据{}"+v);
	var tbodyVal=$(".tbody").html();
	$(".tbody").html("<h1>添加数据</h1>");
}

function spelling(){
	var tbodyVal=$(".tbody").html();
	//替换
	$(".tbody").html( tbodyVal + '<tr><td>jsp初始化加载</td><td><a onmouseover="MouseOver(this)" downpath="D:/1/2/3/123.txt" >loaning......</a></td></tr>' );
	//追加
	//$(".tbody").append('<tr><td>jsp初始化加载</td><td><a href="javascript:void(0)">loaning......</a></td></tr>');
}
function qwet(data){
		//console.info(data)
	var jsonobj= JSON.parse(data);
	var html="";
	var nullDesc="<td style=\"color: red\">此商机前置业务未上传过任何附件</td>";
	//1
	   	var pre_assessment_business_number_JSON=jsonobj.pre_assessment_business_number;
   		if (pre_assessment_business_number_JSON.length>0) {
   			var ht=rest(pre_assessment_business_number_JSON);
   			var _html_td="<td>"+pre_assessment_business_number_JSON[0].filename+"</td>";
   			var _ht="<td>"+ ht +"</td>";
   			html="<tr>"+ _html_td + _ht +"</tr>";
			}else{
				var _html_td="<td>前评估资料</td>";
				html="<tr>"+ _html_td + nullDesc +"</tr>";
			}
	//2		
		var bus_win_bid_result_business_id_JSON=jsonobj.bus_win_bid_result_business_id;
   		if (bus_win_bid_result_business_id_JSON.length>0) {
   			var ht=rest(bus_win_bid_result_business_id_JSON);
   			var _html_td="<td>"+bus_win_bid_result_business_id_JSON[0].filename+"</td>";
   			var _ht="<td>"+ ht +"</td>";
   			html+="<tr>"+ _html_td + _ht +"</tr>";
			}else{
				var _html_td="<td>应标文件及中标依据</td>";
				html+="<tr>"+ _html_td + nullDesc +"</tr>";
			}
	//3
		var bus_contract_admin_business_id_JSON=jsonobj.bus_contract_admin_business_id;
   		if (bus_contract_admin_business_id_JSON.length>0) {
   			var ht=rest(bus_contract_admin_business_id_JSON);
   			var _html_td="<td>"+bus_contract_admin_business_id_JSON[0].filename+"</td>";
   			var _ht="<td>"+ ht +"</td>";
   			html+="<tr>"+ _html_td + _ht +"</tr>";
			}else{
				var _html_td="<td>客户合同</td>";
				html+="<tr>"+ _html_td + nullDesc +"</tr>";
			}
			
			
	
        	// for (var i=0; i < jsonobj.length; i++) {
        		// var _html_a="<td> ";
        		// var _html_td;
        		// var _html_tr;
        		// var affixname=jsonobj[i].filename;
        		// _html_td="<td>"+affixname+"</td>";
//         		
        			// _html_a+="<div class=\"col-md-4 listmessage\"><div class=\"col-md-9 listhide\" data-toggle=\"tooltip\" data-placement=\"top\" title=\""+jsonobj[i].FILE_NAME+"\">"+jsonobj[i].FILE_NAME+"</div>"+
        			// "<div class=\"col-md-3 listbtn\"><a href=\"/document/base?cmd=downloadAffix&fid="+ jsonobj[i].ID + "\" class=\"btnlist\"  data-toggle=\"tooltip\" data-placement=\"top\" title=\"下载\"><span class=\"glyphicon glyphicon-download-alt\"></span></a></div></div>";
//         		
        		// _html_a+=" </td>";
//         		
        		// _html_tr="<tr>"+
		        		// _html_td+
		        		// _html_a+
        			// "</tr>";
        		// if (html=="") {
        			// html=_html_tr;
        			// }else{
        				// html+=_html_tr;
        			// };
        	// };
        	//console.info(html);
		var tbodyVal=$(".tbody").html("");
		var tbodyVal=$(".tbody").html(html);
		
		$("[data-toggle='tooltip']").tooltip();
}
/*
*1006 1轮 能力项目立项，附件为展示功能，非手工上传；展示附件内容为需求规划和项目建议书上传的附件，与客户项目立项一样
*/
function qwett(data){
		//console.info(data)
	var jsonobj= JSON.parse(data);
	var html="";
	var nullDesc="<td style=\"color: red\">此商机前置业务未上传过任何附件</td>";
	//1
	   	var pre_assessment_business_number_JSON=jsonobj.req_affix;
   		if (pre_assessment_business_number_JSON.length>0) {
   			var ht=rest(pre_assessment_business_number_JSON);
   			var _html_td="<td>"+pre_assessment_business_number_JSON[0].filename+"</td>";
   			var _ht="<td>"+ ht +"</td>";
   			html="<tr>"+ _html_td + _ht +"</tr>";
			}else{
				var _html_td="<td>需求附件</td>";
				html="<tr>"+ _html_td + nullDesc +"</tr>";
			}
	//2		
		var bus_win_bid_result_business_id_JSON=jsonobj.pro_affix;
   		if (bus_win_bid_result_business_id_JSON.length>0) {
   			var ht=rest(bus_win_bid_result_business_id_JSON);
   			var _html_td="<td>"+bus_win_bid_result_business_id_JSON[0].filename+"</td>";
   			var _ht="<td>"+ ht +"</td>";
   			html+="<tr>"+ _html_td + _ht +"</tr>";
			}else{
				var _html_td="<td>建议书附件</td>";
				html+="<tr>"+ _html_td + nullDesc +"</tr>";
			}

		var tbodyVal=$(".tbody").html("");
		var tbodyVal=$(".tbody").html(html);
		
		$("[data-toggle='tooltip']").tooltip();
}

function busContractAdminAttachment(data){

	var jsonobj = JSON.parse(data);
	var html = "";
	var nullDesc = "<td style=\"color: red\">此合同前置业务未上传过任何附件</td>";
	
	//合同扫描件
   	var ele_scanning = jsonobj.bus_contract_admin_ele_scanning;
 	if (ele_scanning.length>0) {
		var ht = rest(ele_scanning);
		var _html_td = "<td>"+ele_scanning[0].filename+"</td>";
		var _ht = "<td>"+ ht +"</td>";
		html = "<tr>"+ _html_td + _ht +"</tr>";
	}else{
		var _html_td = "<td>合同扫描件</td>";
		html = "<tr>"+ _html_td + nullDesc +"</tr>";
	}
	
	//合同审批单
	var uploading_att = jsonobj.bus_contract_admin_uploading_att;
	if (uploading_att.length>0) {
		var ht = rest(uploading_att);
		var _html_td = "<td>"+uploading_att[0].filename+"</td>";
		var _ht = "<td>"+ ht +"</td>";
		html += "<tr>"+ _html_td + _ht +"</tr>";
	}else{
		var _html_td="<td>合同审批单</td>";
		html += "<tr>"+ _html_td + nullDesc +"</tr>";
	}
	
	//合同授权书
	var other_file = jsonobj.bus_contract_admin_other_file;
	if (other_file.length>0) {
		var ht = rest(other_file);
		var _html_td = "<td>"+other_file[0].filename+"</td>";
		var _ht = "<td>"+ ht +"</td>";
		html += "<tr>"+ _html_td + _ht +"</tr>";
	}else{
		var _html_td="<td>合同授权书</td>";
		html += "<tr>"+ _html_td + nullDesc +"</tr>";
	}
	
	var tbodyVal=$(".tbody").html("");
	var tbodyVal=$(".tbody").html(html);
	$("[data-toggle='tooltip']").tooltip();
}

function preAssessmentAttachment(data){

	var jsonobj = JSON.parse(data);
	var html = "";
	var nullDesc = "<td style=\"color: red\">此前评估附件未上传过任何附件</td>";
	
	//前评估附件
   	var enclosure = jsonobj.pre_assessment_enclosure;
 	if (enclosure.length>0) {
		var ht = rest(enclosure);
		var _html_td = "<td>"+enclosure[0].filename+"</td>";
		var _ht = "<td>"+ ht +"</td>";
		html = "<tr>"+ _html_td + _ht +"</tr>";
	}else{
		var _html_td = "<td>前评估附件</td>";
		html = "<tr>"+ _html_td + nullDesc +"</tr>";
	}
	
	var tbodyVal=$(".tbody").html("");
	var tbodyVal=$(".tbody").html(html);
	$("[data-toggle='tooltip']").tooltip();
}

function rest(jsonobj){
	var html="";
	
        	for (var i=0; i < jsonobj.length; i++) {
        		var _html_a=" ";
        		var _html_td;
        		var _html_tr;
        		var affixname=jsonobj[i].filename;
        		//_html_td="<td>"+affixname+"</td>";
        		_html_td="";
        			_html_a+="<div class=\"col-md-4 col-xs-4 col-sm-4 listmessage\"><div class=\"col-md-9 col-xs-9 col-sm-9 listhide\" data-toggle=\"tooltip\" data-placement=\"top\" title=\""+jsonobj[i].FILE_NAME+"\">"+jsonobj[i].FILE_NAME+"</div>"+
        			"<div class=\"col-md-3 col-xs-3 col-sm-3 listbtn\"><a href=\"/document/base?cmd=downloadAffix&fid="+ jsonobj[i].ID + "\" class=\"btnlist\"  data-toggle=\"tooltip\" data-placement=\"top\" title=\"下载\"><span class=\"glyphicon glyphicon-download-alt\"></span></a></div></div>";
        		
        		_html_a+="";
        		
        		_html_tr=""+
		        		_html_td+
		        		_html_a+
        			"";
        		if (html=="") {
        			html=_html_tr;
        			}else{
        				html+=_html_tr;
        			};
        	};	
        	//html="<tr>"+ html +"</tr>";
        	
        	return html;
}

/**
 * 添加数据
 */
function addDataLi(JSON){
		$.ajax({
		url : "/system/getaffixs/getafs",
        dataType : "json",  
        type : "POST",
        data:{"jsonData": JSON },
        success : function(data) {
        	console.info(data)
        	var html="";
        	for (var i=0; i < data.length; i++) {
        		var _html_a="<td> ";
        		var _html_td;
        		var _html_tr;
        		var affixname=(data[i]["affixname"]);
        		_html_td="<td>"+affixname+"</td>";
        		var affixfiledata=(data[i]["affixfiledata"]);
        		for (var ii=0; ii < affixfiledata.length; ii++) {
        			//_html_a+="<a onmouseover=\"MouseOver(this)\" fileId=\""+affixfiledata[ii].ID+"\" >"+affixfiledata[ii].FILE_NAME+"</a>";
        			_html_a+="<div class=\"col-md-4 listmessage\"><div class=\"col-md-9 listhide\" data-toggle=\"tooltip\" data-placement=\"top\" title=\""+affixfiledata[ii].FILE_NAME+"\">"+affixfiledata[ii].FILE_NAME+"</div>"+
        			"<div class=\"col-md-3 listbtn\"><a href=\"/document/base?cmd=downloadAffix&fid="+ affixfiledata[ii].ID + "\" class=\"btnlist\"  data-toggle=\"tooltip\" data-placement=\"top\" title=\"下载\"><span class=\"glyphicon glyphicon-download-alt\"></span></a></div></div>";
        		};
        		_html_a+=" </td>";
        		
        		_html_tr="<tr>"+
		        		_html_td+
		        		_html_a+
        			"</tr>";
        		if (html=="") {
        			html=_html_tr;
        			}else{
        				html+=_html_tr;
        			};
        	};
        	//console.info(html);
		var tbodyVal=$(".tbody").html("");
		var tbodyVal=$(".tbody").html(html);
        },
        error: function (e) {
            alert("附件数据加载失败！");
            comsole.info(e)
        }
    });
   //parent.initShowAffixParamEventually="";
   //alert(parent.initShowAffixParamEventually);
}
/**
 * 添加多节点数据
 */
function addNodeDataLi(JSON){
	var jsonobj=jQuery.parseJSON(JSON);//
	var filename=(jsonobj["filename"]);
	
		$.ajax({
		url : "/system/getaffixs/getnodeafs",
        dataType : "json",  
        type : "POST",
        data:{"jsonData": JSON },
        success : function(data) {
        	var html="";
        	for (var i=0; i < data.length; i++) {
        		var _html_a="<td> ";
        		var _html_td;
        		var _html_tr;
        		var affixname=filename;
        		_html_td="<td>"+affixname+"</td>";
        			//_html_a+="<a onmouseover=\"MouseOver(this)\" fileId=\""+affixfiledata[ii].ID+"\" >"+affixfiledata[ii].FILE_NAME+"</a>";
        			_html_a+="<div class=\"col-md-4 listmessage\"><div class=\"col-md-9 listhide\" data-toggle=\"tooltip\" data-placement=\"top\" title=\""+data[i].FILE_NAME+"\">"+data[i].FILE_NAME+"</div>"+
        			"<div class=\"col-md-3 listbtn\"><a href=\"/document/base?cmd=downloadAffix&fid="+ data[i].ID + "\" class=\"btnlist\"  data-toggle=\"tooltip\" data-placement=\"top\" title=\"下载\"><span class=\"glyphicon glyphicon-download-alt\"></span></a></div></div>";
        		_html_a+=" </td>";
        		
        		_html_tr="<tr>"+
		        		_html_td+
		        		_html_a+
        			"</tr>";
        		if (html=="") {
        			html=_html_tr;
        			}else{
        				html+=_html_tr;
        			};
        	};
        	//console.info(html);
		var tbodyVal=$(".tbody").html("");
		var tbodyVal=$(".tbody").html(html);
		$("[data-toggle='tooltip']").tooltip();
        },
        error: function (e) {
            alert("附件数据加载失败！");
            comsole.info(e)
        }
    });
   //parent.initShowAffixParamEventually="";
   //alert(parent.initShowAffixParamEventually);
}

$(window).load(function(){
	
	var parent_InitShowAffixParam=parent.initShowAffixParamEventually;
	if (parent_InitShowAffixParam!=undefined && parent_InitShowAffixParam!="") {
		//console.info("子页面获取的initShowAffixParam{}"+parent_InitShowAffixParam);
		var parent_InitShowAffixParam_JsonString=JSON.parse(parent_InitShowAffixParam);
		//parent_InitShowAffixParam_Json=JSON.parse(parent_InitShowAffixParam_JsonString);
		console.info(parent_InitShowAffixParam_JsonString);
		addDataLi(parent_InitShowAffixParam_JsonString);
		$("[data-toggle='tooltip']").tooltip();
	};
	
	// var parent_MultiNodeViewShowAffixParam=parent.MultiNodeViewShowAffixParamEventually;
	// if (parent_MultiNodeViewShowAffixParam!=undefined && parent_MultiNodeViewShowAffixParam!="") {
		// //var parent_MultiNodeViewShowAffixParam_JsonString=JSON.parse(parent_MultiNodeViewShowAffixParam);
		// addNodeDataLi(parent_MultiNodeViewShowAffixParam);
	// };
	
	});

</script>
</html>