<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=9; IE=8; IE=7; IE=EDGE"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>子表模板</title>
<style type="text/css">
#btns{
width: 30px;height: 30px;border-radius: 30%;border: none
}
.labelfont{
	width:100%;
	text-align:right;
}
.col-md-4{
	margin-bottom:10px;
}
.labelfontsmall{
	width:100%;
	text-align:right;
	overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
}
.col-md-4 .colmdnopadd{
	padding-left:18px;
	margin:0;
}
</style>

<script src="../vendor/jquery/jquery.min.js"></script>



<%

    String p_ids=request.getParameter("pid");

	
%>

<script type="text/javascript">
      $(function(){
      
      
         $.ajax({
			type: "POST",
			url:"/system/project?cmd=showp_history&id=<%=p_ids%>",		
			dataType: "json",
			async: false,
			cache: false,
			contentType: false,
			processData: false,
			success: function(data) {
			   
			
			   
			   $("#p_id").val(data[0]["BUSINESS_NUM"]);         //商机编号
		       $("#p_name").val(data[0]["PRO_NAME"]);       //项目名称
		       $("#manager").val(data[0]["PRO_MANAGER"]);      //项目经理
		       $("#BILL_STATUS").val(data[0]["WANT_CONCENT_MESSAGE"]);  //合同状态
		       $("#p_status").val(data[0]["PRO_BOOK_MESSAGE"]);     //项目建议书状态
		       $("#p_time").val(data[0]["PRO_TIME"]);       //立项时间
		       $("#con_status").val(data[0]["PRO_STATUS"]);   //项目状态
		       
		       $("#mtime").val(data[0]["MTIME"]);     //中标
		       $("#opex").val(data[0]["OPEX"]);       //op
		       $("#capex").val(data[0]["CAPEX"]);   //cp
		       
		       if(data[0]["PRO_COM"]==""){$("#proj_message_in").val("0");}
		       else{$("#proj_message_in").val(data[0]["PRO_COM"]);} //项目进度
		       
		       $("#con_in_status").val(data[0]["PRO_IN_STATUS"]);   //项目内部阶段
		       
		       //alert(data[0]["PRO_OUT_STATUS"]);
		       
		       if(data[0]["PRO_OUT_STATUS"]==null){
		           $("#d1").css("display","none");
		           $("#d2").css("display","none");
		           $("#d3").css("display","none");
		           $("#d4").css("display","none");
		           
		           $("#m1").css("display","none");
		           $("#m2").css("display","none");
		           $("#m3").css("display","none");
		           
		           
		           
		       }
		       else{$("#con_out_status").val(data[0]["PRO_OUT_STATUS"]);} //项目外部阶段
		       
		       $("#now_price").val(data[0]["WILL_COST_NOW"]);      //当前可用额度
		       $("#BUILD_INVESTMENT_COST_ALL").val(data[0]["BEF_BUILD_COST"]); //前评估建设成本
		       $("#BUILD_INVESTMENT_COST_T").val(data[0]["BEF_BUILD_COST_COM"]);   //前评估建设成本投资
		       $("#BUILD_INVESTMENT_COST_F").val(data[0]["BEF_BUILD_COST_PRI"]);   //前评估建设成本费用
		       $("#p_price").val(data[0]["PRO_BUILD_COST"]);                   //立项建设成本
		       $("#SET_BUILD_INVESTMENT_COST_T").val(data[0]["PRO_BUILD_COST_COM"]); //立项建设成本投资
		       $("#SET_BUILD_INVESTMENT_COST_F").val(data[0]["PRO_BUILD_COST_PRI"]); //立项建设成本费用
		       $("#pro_talk").val(data[0]["PROJ_TALK_MESSAGE"]); //项目总体进度概述
		       
		       $("input").attr("disabled",true);
		       $("select").attr("disabled",true);
		       $("#pro_talk").attr("disabled",true);
			    
			   $("#mtime").attr("disabled",true);    //中标
		       $("#opex").attr("disabled",true);     //op
		       $("#capex").attr("disabled",true); 
			    
			    
			    //alert(JSON.stringify(data));
			     
			}, error: function(data) {
				var da=JSON.stringify(data);
				alert("版本保存失败");
			}
			});
      
      
       return ;
      
      
      
      });
    	  
</script>
</head>
<body>
<!--***************************************************************************************  -->
	<%-- <form class="form-horizontal">
		<div class="panel panel-primary" >

			<div class="panel-body" id="bulidTable" >
				<div class="panel panel-primary">
					<div class="panel-heading" id='pageName'>子表模板</div>

					<div class="panel-body">
					 <div class="panel panel-default">
							<div class="panel-heading">查询条件</div>
							<div class="panel-body" id="queryParam"></div>
						</div> 
                    
						<div id="toolbar">
						    <!-- onclick="window.history.back(-1);" -->
							<button type="button" class="btn btn-info" >
								<span class="glyphicon glyphicon-ban-circle" aria-hidden="true"></span>
								<a href="/system/pages/singleTableModify.jsp?pageCode=CUMA_ACCOUNT&pageName=客户基本资料&isAddAuditBut=1&token=d2ViLDM2MDAsZ3ZPRmFaZ0lSVWlBWVdsTDVOMlM2VlAwdDFSRVRWb0hoODA4dDBadHpJY2JzVDRwaWcxT00zSEpZZ3hIMEp4Y1Zaa0xHUTVqOVZDbjhTVk1reGhqYVE9PQ&totalCode=100137102102&asd=<%=asd%>&dsa=<%=dsa %>">返回主表</a>
							</button> 
							<div id='button_div'></div>
						</div> 
						<!-- <table id="table"></table> -->
						<br/> --%>
						
						<!-- <div id='button_div'></div> -->
		
		<form class="form-horizontal">
			<div class="panel panel-primary">
		
				<div class="panel-body">
					<div class="panel panel-primary">
						<div  class="panel-heading">项目周报</div>
						
						

				
						
					<div class="panel-body">
						
						<div class="col-md-12 col-xs-12 col-sm-12 classifiedtitle" style="margin-bottom:10px;">项目基本信息</div>
															
						<div class="col-md-4 col-xs-4 col-sm-4">
							<div class="form-group">
								<div class="col-md-4 col-xs-4 col-sm-4 colmdnopadd"><label class="labelfont">商机编号：</label></div> 
								<div class="col-md-8 col-xs-8 col-sm-8"><input class="form-control" id="p_id" value="" /></div>
							</div>
						</div>
						<div class="col-md-4 col-xs-4 col-sm-4">
							<div class="form-group">
								<div class="col-md-4 col-xs-4 col-sm-4 colmdnopadd"><label class="labelfont">项目名称：</label></div> 
								<div class="col-md-8 col-xs-8 col-sm-8"><input class="form-control" id="p_name" value="" /></div>
							</div>
						</div>
						<div class="col-md-4 col-xs-4 col-sm-4">
							<div class="form-group">
								<div class="col-md-4 col-xs-4 col-sm-4 colmdnopadd"><label class="labelfont">项目经理：</label></div> 
								<div class="col-md-8 col-xs-8 col-sm-8"><input class="form-control" id="manager" value=""  /></div>
							</div>
						</div>
						<div class="col-md-4 col-xs-4 col-sm-4">
							<div class="form-group">
								<div class="col-md-4 col-xs-4 col-sm-4 colmdnopadd"><label class="labelfont">合同状态：</label></div> 
								<div class="col-md-8 col-xs-8 col-sm-8"><input class="form-control" id="BILL_STATUS" name="BILL_STATUS" type="text"  value=""  /></div>
							</div>
						</div>
						<div class="col-md-4 col-xs-4 col-sm-4">
							<div class="form-group">
								<div class="col-md-4 col-xs-4 col-sm-4 colmdnopadd"><label class="labelfontsmall" data-toggle="tooltip" title="" data-original-title="项目建议书状态">项目建议书状态：</label></div> 
								<div class="col-md-8"><input class="form-control" id="p_status" type="text"  value=""  /></div>
							</div>
						</div>
						<div class="col-md-4 col-xs-4 col-sm-4">
							<div class="form-group">
								<div class="col-md-4 col-xs-4 col-sm-4 colmdnopadd"><label class="labelfont">立项时间：</label></div> 
								<div class="col-md-8 col-xs-8 col-sm-8"><input class="form-control" id="p_time" value="" /></div>
							</div>
						</div>
						
						    <div id="m1" class="col-md-4">
								<div class="form-group" >
									<div class="col-md-4 colmdnopadd"><label class="labelfont">中标时间：</label></div> 
									<div class="col-md-8">
										<input id="mtime" class="form-control"  />
									</div>
								</div>
							</div>
						
						
						<div class="col-md-12 col-xs-12 col-sm-12 classifiedtitle" style="margin-bottom:10px;">项目状态信息</div>
						<div class="col-md-4 col-xs-4 col-sm-4">
							<div class="form-group">
								<div class="col-md-4 col-xs-4 col-sm-4 colmdnopadd"><label class="labelfont">项目状态：</label></div> 
								<div class="col-md-8 col-xs-8 col-sm-8" style="position:relative">
									<select class="form-control" id="con_status" value="2">
										<option value="0">请选择</option>
										<option value="1">正常</option>
										<option value="2">预警</option>
										<option value="3">延期</option>
										<option value="4">挂起</option>
                                        <option value="5">完成</option>						        
						            </select>	
						            <img id="image_btn" alt="" src="" hidden="true;" align="middle" style="position: absolute;right: -10px;top: 2px;">					            
							     </div>
							</div>
						</div>
												<div class="col-md-4 col-xs-4 col-sm-4">
							<div class="form-group">
								<div class="col-md-4 col-xs-4 col-sm-4 colmdnopadd"><label class="labelfontsmall" data-toggle="tooltip" title="" data-original-title="项目内部阶段">项目内部阶段：</label></div> 
								<div class="col-md-8 col-xs-8 col-sm-8">
									<select class="form-control" id="con_in_status">
										<option value="0">请选择</option>
										<option value="1">立项</option>
										<option value="2">实施（采购完成）</option>
										<option value="3">实施（采购进行中）</option>
										<option value="4">实施（无采购）</option>
										<option value="5">初验</option>
										<option value="6">终验</option>
						            </select>
							    </div>
							</div>
						</div>	
						<div id="d1" class="col-md-4 col-xs-4 col-sm-4">
							<div class="form-group">
								<div class="col-md-4 col-xs-4 col-sm-4 colmdnopadd"><label class="labelfontsmall" data-toggle="tooltip" title="" data-original-title="项目外部阶段">项目外部阶段：</label></div> 
								<div class="col-md-8 col-xs-8 col-sm-8">
									<select class="form-control" id="con_out_status">
											<option value="0">请选择</option>
											<option value="1">立项</option>
											<option value="2">实施</option>
											<option value="3">试运行</option>
											<option value="4">上线</option>
											<option value="5">验收</option>
							        </select>
							    </div>
							</div>
						</div>
						
						<div class="col-md-4 col-xs-4 col-sm-4">
							<div class="form-group">
								<div class="col-md-4 col-xs-4 col-sm-4 colmdnopadd"><label class="labelfont">项目进度：</label></div> 
								<div class="col-md-8 col-xs-8 col-sm-8"><input class="form-control" id="proj_message_in" value=""/></div>
							</div>
						</div>				
						
						<div class="col-md-12 col-xs-12 col-sm-12">
							<div class="form-group">
								<div class="col-md-2 col-xs-2 col-sm-2 colmdnopadding" style="padding:0; height:35px; margin-left:-50px;"><label class="labelfontsmall" data-toggle="tooltip" title="" data-original-title="项目总体进度概述">项目总体进度概述：</label></div> 
								<div class="col-md-10 col-xs-10 col-sm-10">
							<textarea class="form-control" id="pro_talk" rows="3"></textarea>
								</div>
							</div>
						</div>
						

						<div class="col-md-12 col-xs-12 col-sm-12 classifiedtitle" style="margin-bottom:10px;">项目预算信息</div>																		
						<div class="col-md-4 col-xs-4 col-sm-4">
							<div class="form-group">
								<div class="col-md-4 col-xs-4 col-sm-4 colmdnopadd"><label class="labelfontsmall" data-toggle="tooltip" title="" data-original-title="预算当前可用额度(万元)">预算当前可用额度(万元)：</label></div> 
								<div class="col-md-8 col-xs-8 col-sm-8" style="position:relative">
									<input class="form-control" id="now_price" value=""/>
								 	<img id="images" alt="" src="../easyui/15.png"  align="middle" hidden="true;" height="25" style="position: absolute;right: -7px;top: 2px;">
								</div>
							</div>
						</div>

						<div id="d2" class="col-md-4 col-xs-4 col-sm-4">
							<div class="form-group">
								<div class="col-md-4 col-xs-4 col-sm-4 colmdnopadd"><label class="labelfontsmall" data-toggle="tooltip" title="" data-original-title="前评估建设成本(万元)">前评估建设成本(万元)：</label></div> 
								<div class="col-md-8 col-xs-8 col-sm-8"><input class="form-control" id="BUILD_INVESTMENT_COST_ALL" value=""/></div>
							</div>
						</div>
						<div id="d3" class="col-md-4 col-xs-4 col-sm-4">
							<div class="form-group">
								<div class="col-md-4 col-xs-4 col-sm-4 colmdnopadd"><label class="labelfontsmall" data-toggle="tooltip" title="" data-original-title="前评估建设成本-投资(万元)">前评估建设成本-投资(万元)：</label></div> 
								<div class="col-md-8 col-xs-8 col-sm-8"><input class="form-control" id="BUILD_INVESTMENT_COST_T" value=""/></div>
							</div>
						</div>
						<div id="d4" class="col-md-4 col-xs-4 col-sm-4">
							<div class="form-group">
								<div class="col-md-4 col-xs-4 col-sm-4 colmdnopadd"><label class="labelfontsmall" data-toggle="tooltip" title="" data-original-title="前评估建设成本-费用(万元)">前评估建设成本-费用(万元)：</label></div> 
								<div class="col-md-8 col-xs-8 col-sm-8"><input class="form-control" id="BUILD_INVESTMENT_COST_F" value=""/></div>
							</div>
						</div>
						<div class="col-md-4 col-xs-4 col-sm-4">
							<div class="form-group">
								<div class="col-md-4 col-xs-4 col-sm-4 colmdnopadd"><label class="labelfontsmall" data-toggle="tooltip" title="" data-original-title="立项建设成本(万元)">立项建设成本(万元)：</label></div> 
								<div class="col-md-8 col-xs-8 col-sm-8"><input class="form-control" id="p_price" value=""/></div>
							</div>
						</div>
						<div class="col-md-4 col-xs-4 col-sm-4">
							<div class="form-group">
								<div class="col-md-4 col-xs-4 col-sm-4 colmdnopadd"><label class="labelfontsmall" data-toggle="tooltip" title="" data-original-title="立项建设成本-投资(万元)">立项建设成本-投资(万元)：</label></div> 
								<div class="col-md-8 col-xs-8 col-sm-8"><input class="form-control" id="SET_BUILD_INVESTMENT_COST_T" value="" /></div>
							</div>
						</div>
						<div class="col-md-4 col-xs-4 col-sm-4">
							<div class="form-group">
								<div class="col-md-4 col-xs-4 col-sm-4 colmdnopadd"><label class="labelfontsmall" data-toggle="tooltip" title="" data-original-title="立项建设成本(万元)">立项建设成本-费用(万元)：</label></div> 
								<div class="col-md-8 col-xs-8 col-sm-8"><input class="form-control" id="SET_BUILD_INVESTMENT_COST_F" value=""/></div>
							</div>
						</div>
						
						
						 <div id="m2" class="col-md-4">
								<div class="form-group" >
									<div class="col-md-4 colmdnopadd"><label class="labelfontsmall" data-toggle="tooltip" title="" data-original-title="OPEX实施累计(万元)">OPEX实施累计(万元)：</label></div> 
									<div class="col-md-8">
										<input id="opex" class="form-control" value="" />
									</div>
								</div>
							</div>
							
							
								<div id="m3" class="col-md-4">
								<div class="form-group" >
									<div class="col-md-4 colmdnopadd"><label class="labelfontsmall" data-toggle="tooltip" title="" data-original-title="CAPEX实施累计(万元)">CAPEX实施累计(万元)：</label></div> 
									<div class="col-md-8">
										<input id="capex" class="form-control" value=""  />
									</div>
								</div>
							</div>




					
<%-- 						<table style="width: 1500PX;" cellspacing="2" cellpadding="1"
							bordercolor="#000000" border="0">
							<tbody>
								<tr>
									<!-- <td rowspan="2"
										style="background-color: #337FE5; vertical-align: middle; text-align: left; width: 15%; height: 10%;"
										align="center">
										<h2>项目基本信息</h2>
									</td> -->
									<td
										style="vertical-align: middle; text-align: left; width: 25%; height: 10%;"
										align="center"><label>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp商机编号：</label> <input id="p_id" value="<%=p_id %>" /></td>
									<td
										style="vertical-align: middle; text-align: left; width: 25%; height: 10%;"
										align="center"><label>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp项目名称：</label>
										<input id="p_name" value="<%=p_name %>" /></td>
									<td
										style="vertical-align: middle; text-align: left; width: 25%; height: 10%;"
										align="center"><label>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp项目经理：</label> <input id="manager" value="<%=manager %>" /></td>
									<td
										style="vertical-align: middle; text-align: left; width: 25%; height: 10%;"
										align="center"></td>	
								</tr>
								
							    <tr>
							            <td
										style="vertical-align: middle; text-align: left; width: 25%; height: 14px;"
										align="center">
										</td>	
							    </tr>
							    
								<tr>
									<td
										style="vertical-align: middle; text-align: left; width: 25%; height: 10%;"
										align="center"><label>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp合同状态：</label> <input id="BILL_STATUS" name="BILL_STATUS" type="text"  value=""></input> </td>
									<td
										style="vertical-align: middle; text-align: left; width: 20%; height: 10%;"
										align="center"><label>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp项目建议书状态：</label><input id="p_status"
										value=""  style="width: 100px"/></td>
									<td
										style="vertical-align: middle; text-align: left; width: 25%; height: 10%;"
										align="center"><label>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp立项时间：</label> <input id="p_time" value="<%=p_time %>" /></td>
									<td
										style="vertical-align: middle; text-align: left; width: 25%; height: 10%;"
										align="center"></td>
								</tr>
								 <tr>
							            <td
										style="vertical-align: middle; text-align: left; width: 25%; height: 10%;"
										align="center"></td>	
							    </tr>
								
								<tr>
									<!-- <td
										style="background-color: #337FE5; text-align: left; vertical-align: middle; width: 15%; height: 10%;">
										<h2>项目状态信息</h2>
									</td> -->
									<td
										style="vertical-align: middle; text-align: left; width: 25%; height: 10%;"
										align="center"><label>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp项目状态：</label>&nbsp<select id="con_status" value="2" style="width:120px;text-align: center;">
											<option value="0">请选择</option>
											<option value="1">正常</option>
											<option value="2">预警</option>
											<option value="3">延期</option>
											<option value="4">挂起</option>
                                            <option value="5">完成</option>
							                </select>
							               <!--  <input id="btns" type="button" disabled="value"
										style="background-color:green; width: 8%" /> -->
										<img id="image_btn" alt="" src="" hidden="true;" align="middle">
										</td>
									<td
										style="vertical-align: middle; text-align: left; width: 25%; height: 10%;"
										align="center">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<label>项目进度：</label> <input id="proj_message_in" value="" /></td>
									<td
										style="vertical-align: middle; text-align: left; width: 100px; height: 10%;"
										align="center"><label>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp项目内部阶段：</label> <select id="con_in_status" style="width: 150px;text-align: center;">
											<option value="0">请选择</option>
											<option value="1">立项</option>
											<option value="2">实施（采购完成）</option>
											<option value="3">实施（采购进行中）</option>
											<option value="4">实施（无采购）</option>
											<option value="5">初验</option>
											<option value="6">终验</option>
							                </select>
									        <br/>
										    <label>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp项目外部阶段：</label> <select id="con_out_status" style="width: 150px;text-align: center;">
											<option value="0">请选择</option>
											<option value="1">立项</option>
											<option value="2">实施</option>
											<option value="3">试运行</option>
											<option value="4">上线</option>
											<option value="5">验收</option>
							                </select></td>
									<!-- <td
										style="vertical-align: middle; text-align: center; width: 20%; height: 10%;"
										align="center"><label>项目外部阶段：</label> <select id="con_out_status" style="width: 150px;text-align: center;">
											<option value="立项">立项</option>
											<option value="实施">实施</option>
											<option value="上线">上线</option>
											<option value="验收">验收</option>
							                </select></td> -->
								</tr>
								 <tr>
							            <td
										style="vertical-align: middle; text-align: left; width: 25%; height: 10%;"
										align="center"></td>	
							    </tr>
								<tr>
									<td
										style="vertical-align: middle; text-align: left; width: 100px; height: 10%;"
										align="center"><label>&nbsp预算当前可用额度(万元)：</label><input id="now_price" value="" style="width: 100px;"/>
							           <!--  <input id="images"  type="image"  disabled="value" src="../easyui/14.jpg" style="border:none; height: 100% " hidden="true;"/> -->
								          <img id="images" alt="" src="../easyui/15.png"  align="middle" hidden="true;" height="25" >
								    </td>
								</tr>
								<tr>
									<!-- <td
										style="background-color: #337FE5; text-align: left; vertical-align: middle; width: 15%; height: 10%;">
										<h2>项目预算信息</h2>
									</td> -->
									<td
										style="vertical-align: middle; text-align: left; width: 25%; height: 10%;"
										align="center"><label>&nbsp前评估建设成本(万元)：</label> <input id="BUILD_INVESTMENT_COST_ALL" value="" /></td>
									<td
										style="vertical-align: middle; text-align: left; width: 25%; height: 10%;"
										align="center"><label>&nbsp前评估建设成本-投资(万元)：</label> <input id="BUILD_INVESTMENT_COST_T" value="" /></td>
									<td
										style="vertical-align: middle; text-align: left; width: 25%; height: 10%;"
										align="center"><label>&nbsp前评估建设成本-费用(万元)：</label> <input id="BUILD_INVESTMENT_COST_F" value="" /></td>
									
								</tr>
								
								<tr>
									<td
										style="vertical-align: middle; text-align: left; width: 25%; height: 10%;"
										align="center"><label>&nbsp立项建设成本(万元)：</label> <input id="p_price"
										value="<%=p_price %>" /></td>
										
									<td
										style="vertical-align: middle; text-align: left; width: 25%; height: 10%;"
										align="center"><label>&nbsp立项建设成本-投资(万元)：</label> <input id="SET_BUILD_INVESTMENT_COST_T" value="" /></td>
									
									<td
										style="vertical-align: middle; text-align: left; width: 25%; height: 10%;"
										align="center"><label>&nbsp立项建设成本-费用(万元)：</label> <input id="SET_BUILD_INVESTMENT_COST_F" value="" /></td>
									
									<%=p_price %>
								</tr>
								
								
								
							</tbody>
						</table> --%>
					

						

						
					
						<!-- <table style="width: 100%;" cellspacing="2" cellpadding="1"
							bordercolor="#000000" border="0">
							<tbody>
								
							</tbody>
						</table>
						<br /> -->
						<!-- <table style="width: 100%;" cellspacing="2" cellpadding="1"
							bordercolor="#000000" border="0">
							<tbody>
								
							</tbody>
						</table>
						<br />		 -->
						
						
						<div class="col-md-12 col-xs-12 col-sm-12 classifiedtitle" style="margin-bottom:10px;">WBS分解详情</div>
						<iframe src='ganttmessage/gantts_proj.jsp?p_ids=<%=p_ids %>&time=<%=request.getParameter("time") %>' scrolling="no" frameborder="0"  width="100%" height="400px" style="margin-top:10px"></iframe>
						
						<!-- <table style="width: 100%;" cellspacing="0" cellpadding="2"
							bordercolor="#000000" border="1" align="center">
							<tbody>
								<tr>
									<td align="center">WBS<br />
									</td>
									<td align="center">开始日期<br />
									</td>
									<td align="center">计划完成日期<br />
									</td>
									<td align="center">实际完成日期<br />
									</td>
									<td align="center">任务描述<br />
									</td>
									<td align="center">责任人<br />
									</td>
									<td align="center">工时<br />
									</td>
									<td align="center">状态<br />
									</td>
									<td align="center">时间进度<br />
									</td>
								</tr>
							</tbody>
						</table> -->
						
						<!-- <table style="width: 100%;" cellspacing="0" cellpadding="2"
							bordercolor="#000000" border="1" align="center">
							<tbody>
								<tr>
									<td align="center">风险描述<br />
									</td>
									<td align="center">影响程度<br />
									</td>
									<td align="center">发生时间<br />
									</td>
									<td align="center">项目内部是否可解决<br />
									</td>
									<td align="center">解决措施<br />
									</td>
									<td align="center">最后期限<br />
									</td>
									<td align="center">风险状态<br />
									</td>
								</tr>
							</tbody>
						</table> -->
						<div class="col-md-12 col-xs-12 col-sm-12 classifiedtitle" style="margin-bottom:10px;">风险问题跟踪</div>
                        <iframe src='riskP_Pmo.jsp?p_ids=<%=p_ids %>' scrolling="no" frameborder="0"  width="100%" height="400px"></iframe>
                        
                        
                        </div>
                        
                    </div>
				</div>
			</div>
		</form>	
						<!-- ****************************************************************************************************** -->						
					
					<!-- </div>	
				</div>
			</div>
            
             
             
			<div class="panel-body" id="bulidPage" style="display: none">
				<div class="panel panel-primary">
					<div class="panel-heading">新增</div>
					<div class="panel-body">

						<div>
							<button type="button" class="btn btn-default"
								onclick="save(this)">
								<span class="glyphicon glyphicon-saved" aria-hidden="true"></span>保存
							</button>
							<button type="button" class="btn btn-default"
								onclick="back(this)">
								<span class="glyphicon glyphicon-ban-circle" aria-hidden="true"></span>返回
							</button>
						</div>

						<div class="panel-body" id="insPage">
							主表主键  用于 新增、修改
							<input type='hidden' name="ParentPK">
							<input type='hidden' id="META_DETAIL_ID" name="META_DETAIL_ID">
						</div>

					</div>
				</div>
			</div>

		</div>
	</form>

	主表主键 用于查询
	<input type="hidden" name="ParentPK_Query" /> -->

</body>
<jsp:include page="../include/public.jsp"></jsp:include>

<script type="text/javascript">
	
	$(function() {
		//bulidPage(true,true,true,true);
		setParntHeigth(2000);
		$("[data-toggle='tooltip']").tooltip();
	});
	
<%-- 	//主表主键在子表字段名
	var ParentPKField = '<%=request.getParameter("ParentPKField")%>';
	//主表主键值
	var ParentPKValue = '<%=request.getParameter("ParentPKValue")%>';
	var meta_id ='<%=request.getParameter("META_ID")%>';
	$('input[name="ParentPK"]').attr('id',ParentPKField);
	//$('input[name="ParentPK_Query"]').attr('id','SEARCH-'+ParentPKField);
	$('input[name^="ParentPK"]').val(ParentPKValue);

	//重写back方法
	function back(t){
		tog(t);
		//排除主表主键
		$inspage.find('[id]').not('#'+ParentPKField).val("");
		$("#ins_or_up_buttontoken").val("");
	}
	
	var columns =['FIELD_CODE','FIELD_NAME','FIELD_TYPE','NULL_FLAG','KEY_FLAG','SORT','INPUT_TYPE','INPUT_FORMART','INPUT_HTML'];
	
	function ref_write_json(rejsonArray){
		
		if(rejsonArray.length==1){
			var jsonObject =rejsonArray[0];
			var prefix = "#";
			if(globalRef_pageType == 'SELECT'){
				prefix += "SEARCH-";
				}
			for(var i in columns){
				var mark =prefix+columns[i];
				$(mark).val(jsonObject[columns[i]]);
			}
			$("#META_DETAIL_ID").val(jsonObject["ID"]);
		}
	}
	
	function ref_query_param(u){
		return "&SEARCH-METADATA_ID="+'<%=request.getParameter("META_ID")%>';
		
	} --%>
	
	function setParntHeigth(heigth){
	  if(parent['setHeigth']){
		  parent['setHeigth'](heigth);
	  }
 	}
</script>
</html>