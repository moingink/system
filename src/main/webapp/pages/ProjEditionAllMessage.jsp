<%@page import="com.yonyou.util.PropertyFileUtil"%>
<%@page import="com.yonyou.util.theme.ThemePath"%>
<%@page import="com.yonyou.business.entity.TokenEntity"%>
<%@page import="com.yonyou.business.entity.TokenUtil"%>
<%@page import="com.yonyou.util.wsystem.service.ORG"%>
<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>项目文件</title>
<jsp:include page="../include/public.jsp"></jsp:include>
<link rel="stylesheet" type="text/css" href="../style/customerManage.css"/>
<script type="text/javascript" src="../vendor/eCharts/echarts.js"></script>
<%
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires", -10); 
	TokenEntity tokenEntity = TokenUtil.initTokenEntity(request);
	String ContextPath = request.getContextPath();
	String path=ContextPath+"/vendor/vehicles";
	String workFlowContext ="/workflow";
%>
<style type="text/css">
p {
	margin: 0 0 0;
}
.content{
	width:100% !important;
	height:auto !important;
}
</style>
</head>
<body>
    <div id="xm" style="display: none ;" align="center">
    <h3 id="y1"><a id="ames" href="#" >项目文件>></a></h3> 
    <div id="lme"></div>
    </div>  
    
	<div id="xmtable" class="contain">
		
		
		<div class="content" style="border:none !important;">
		<!-- 	<div class="content-title">
				<div class="content-name">拜访情况</div>
				<div class="content-more">
					<a href="/system/pages/singleTableModify.jsp?pageCode=VISIT_SITUATION&pageName=拜访情况" target="_blank">>></a>
				</div>
			</div> -->
			<div class="contain-detail" style="padding:0 !important; margin-bottom:10px;">
				<table class="table-bordered table-hover" style="width:90%;text-align:center;margin:0 auto;">
					<thead>
						<tr style="font-size: 14px;text-align:center;background-color: #d9edf7;">
							<td style="width:18%;padding:15px 0;font-weight:bold;">文档名称</td>
							<td style="width:82%;padding:15px 0;font-weight:bold;">文档版本</td>							
						</tr>
					</thead>
					<tbody id="visit" style="text-align:center;">
					</tbody>
				</table>
			</div>
		</div>
		
	</div>
	<br>
	<div id="mes" align="center"></div>
	<div id="message" style="display: none;width: 100%;height: auto" >
	   <iframe scrolling="no" frameborder="0"  width="100%" height="2200"></iframe>
	</div>
</body>
<script type="text/javascript">
$(function(){

	visitSituation();
});


function showtable(){
   $("#xmtable").css("display","inline");
   $("#xm").css("display","none");
}

function showtable1(map,id){
   $("mes").css("display","inline");
   $("#xmtable").css("display","inline");
   $("#xm").css("display","none");
   
   $("#mes").append("<h4>"+map["tyname"]+" "+map["edid"]+"</h4>");
   
   $("#mes").css("display","none");
   $("#message").css("display","none");
   
   
}

//客户
function showmessage(map,id){

    var myDate = new Date();
	//myDate.getFullYear(); //获取完整的年份(4位,1970-????)
    //myDate.getMonth(); //获取当前月份(0-11,0代表1月)
    //myDate.getDate(); //获取当前日(1-31)
	var mons=myDate.getMonth()+1;
	var time_s=myDate.getTime();
	//alert(time_s);


   $("mes").css("display","none");
   $("#lme").empty();
   $("#mes").empty();
   var message=map["src"];
   
   var yid=map["yid"];  //源id  
   var modename=map["modename"];//  模版类型
  
   //alert(map["yid"]+"\n"+map["modename"]);
   
   //return ;  //源id  模版类型
  
   $("div[id='message'] iframe").attr("src",message+"="+id+"&time="+time_s+"&yid="+yid+"&modename="+modename); 
   
   $("#xmtable").css("display","none");
   $("#xm").css("display","block");
   
   $("#lme").append("<h4>"+map["tyname"]+" "+map["edid"]+"</h4>");
    
    
   $("#message").css("display","block");
   
   $("#ames").attr("onclick","showtable1("+JSON.stringify(map)+","+id+")");
   
   
}
//能力
function showmessagepmo(map,id){

    var myDate = new Date();
	//myDate.getFullYear(); //获取完整的年份(4位,1970-????)
    //myDate.getMonth(); //获取当前月份(0-11,0代表1月)
    //myDate.getDate(); //获取当前日(1-31)
	var mons=myDate.getMonth()+1;
	//var time_s=myDate.getFullYear()+mons+myDate.getDate();
    var time_s=myDate.getTime();

   $("mes").css("display","none");
   $("#lme").empty();
   $("#mes").empty();
   var message=map["src"];
  
   var yid=map["yid"];  //源id  
   var modename=map["modename"];//  模版类型
   
  
   $("div[id='message'] iframe").attr("src",message+"="+id+"&type=1"+"&time="+time_s+"&yid="+yid+"&modename="+modename); 
   
   $("#xmtable").css("display","none");
   $("#xm").css("display","block");
   
   $("#lme").append("<h4>"+map["tyname"]+" "+map["edid"]+"</h4>");
    
    
   $("#message").css("display","block");
   
   $("#ames").attr("onclick","showtable1("+JSON.stringify(map)+","+id+")");
   
   
}





function getQueryString(name) {
  var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
  var r = window.location.search.substr(1).match(reg);
  if (r != null) {
    return unescape(r[2]);
  }
  return null;
}


function visitSituation(){
	$.ajax({
		url : '/system/project?cmd=all_show_history&pdata=<%=request.getParameter("pdata")%>&id=<%=request.getParameter("ParentPKValue")%>&yid=<%=request.getParameter("yid")%>&modename=<%=request.getParameter("modename")%>',
		type : 'post',	
		dataType : "json",
		success : function(data) {


			if(data["a"]=="0"){
			 $('#visit').append("<tr><td style='text-align:center;padding:15px 0; padding-left:10px;'>无记录</td><td style='text-align:center;padding:15px 0; padding-left:10px; word-break:break-all; word-wrap:break-all;'>无记录</td></tr>");
			}
			else{
			
			
			for(var i=0;i<data["number"].length;i++){
			   var s=i;
			   
			   var bname=data["number"][s]["EDITION_NAME"];
			   
			   var nlmessage=getQueryString("locationCode");
			   
			   var nl="<%=request.getParameter("locationCode")%>"
			   
			   
			  
			   if(nlmessage=="100147102"){
			    if(data["number"][i]["EDITION_NAME"]=="前评估信息"||data["number"][i]["EDITION_NAME"]=="客户验收"){
			    
			    }
			   
			    else{
			      if(data["number"][i]["EDITION_NAME"]=="内部验收"){
			          data["number"][i]["EDITION_NAME"]="项目验收";
			      }
			      
			    $('#visit').append("<tr><td>"+data["number"][i]["EDITION_NAME"]+
			      "</td><td id=v"+i+" style='text-align:center;padding:15px 0; padding-left:10px;word-break:break-all; word-wrap:break-all;'></td></tr>");
	
			   
			    }
			    for(var j=0;j<data["number"][i]["EMS"];j++){ 
			     
			     var jv=j;
			   
			   if(data["endmessage"][bname][j][bname][0]=="#"){
			  
			    $("#v"+s).append("<td style='float:left;margin-right: 20px;'><a href="+"'"+data["endmessage"][bname][j][bname][0]+"="+data["endmessage"][bname][j][bname][1]+"'"+">"+data["endmessage"][bname][j][bname][2]+"</a></td>");
			   }
			   else{
			      var src={src:data["endmessage"][bname][j][bname][0],tyname:bname,edid:data["endmessage"][bname][j][bname][2],yid:data["endmessage"][bname][j][bname][3],modename:data["endmessage"][bname][j][bname][4]};
			   // var src={src:data["endmessage"][bname][j][bname][0],tyname:bname,edid:data["endmessage"][bname][j][bname][2]};
			    

			    //$("#v"+s).append("<td><a onclick="+"'"+"showmessagepmo("+JSON.stringify(src)+","+data["endmessage"][bname][j][bname][1]+")"+"'"+"  href="+"'"+"#"+"'"+">"+data["endmessage"][bname][j][bname][2]+"</a></td>");

			    $("#v"+s).append("<td style='float:left;margin-right: 20px;'><a onclick="+"'"+"showmessagepmo("+JSON.stringify(src)+",\""+data["endmessage"][bname][j][bname][1]+"\")"+"'"+"  href="+"'"+"#"+"'"+">"+data["endmessage"][bname][j][bname][2]+"</a></td>");

			   }
			   
			   
			   //$("#v"+s).append("<td><a href="+"'"+data["endmessage"][bname][j][bname][0]+"="+data["endmessage"][bname][j][bname][1]+"'"+">"+data["endmessage"][bname][j][bname][2]+"</a></td>"); 
			   //alert(data["message"][s]["EDITION"]);
			   
			   }
			   
			   }
			   
			   
			   
			    else{
			    $('#visit').append("<tr><td>"+data["number"][i]["EDITION_NAME"]+
			      "</td><td id=v"+i+" style='text-align:center;padding:15px 0; padding-left:10px; word-break:break-all; word-wrap:break-all;'></td></tr>");
			   
			   
			    for(var j=0;j<data["number"][i]["EMS"];j++){ 
			     
			     var jv=j;
			   
			   if(data["endmessage"][bname][j][bname][0]=="#"){
			  
			    $("#v"+s).append("<td style='float:left;margin-right: 20px;'><a href="+"'"+data["endmessage"][bname][j][bname][0]+"="+data["endmessage"][bname][j][bname][1]+"'"+">"+data["endmessage"][bname][j][bname][2]+"</a>");
			   }
			   else{
			    var src={src:data["endmessage"][bname][j][bname][0],tyname:bname,edid:data["endmessage"][bname][j][bname][2],yid:data["endmessage"][bname][j][bname][3],modename:data["endmessage"][bname][j][bname][4]};
			    
			    $("#v"+s).append("<td style='float:left;margin-right: 20px;'><a onclick="+"'"+"showmessage("+JSON.stringify(src)+",\""+data["endmessage"][bname][j][bname][1]+"\")"+"'"+"  href="+"'"+"#"+"'"+">"+data["endmessage"][bname][j][bname][2]+"</a></td>");
			   }
			   }
			   
			   }
			    
			   
			   
			  }
			}
		
		},
		
		error : function(data) {
			alert("项目文件详情获取数据失败！");
		}
	});
}



</script>
</html>