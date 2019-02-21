<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<title>子表模板</title>
<style type="text/css">
	#bg{
		margin-top:15px;
	}
	.form-horizontal{
		padding:7px;
	}
	#bt{
		margin-top:15px;
	}
	.panelnew{
		border: 1px solid #337ab7;
   		border-radius: 4px;
	}
</style>
</head>
<%
	String ContextPath =request.getContextPath();
	String token = request.getParameter("token");
%>
<body>

	<form class="form-horizontal">
		<div class="panelnew">
			<div style="width:49%;float:left;">
				<input type="button" value="联查明细" onclick="inspe();" class="btn btn-primary" style="margin-top:10px;margin-bottom:10px;">
				<table class="table table-hover table-bordered" style="text-align: center; " >
					<thead id="thead">
						<tr class="info" style="background-color: rgb(255, 255, 255);">
							<td></td>
							<td>合作伙伴类别</td>
							<td>数量</td>
						</tr>
					</thead>
					<tbody id="tbody">
					</tbody>
				</table>
			</div>
			<div id="bt" style="width: 49%;height:300px;float:right"></div>
			<div id="page">
			</div>
		</div>
	</form>

	
			
</body>
<jsp:include page="../include/public.jsp"></jsp:include>
<script type="text/javascript" src="../vendor/eCharts/echarts.js"></script>
<script type="text/javascript">
	var countData=null;
	var partner_type_ids='';
	$(function() {
		$("#tbody").html("");
		$.ajax({
			type : "POST",
			url :  "/system/reportStatis?cmd=partnerType&token=<%=token%>",
			dataType : "json",
			async: false,
			success : function(data) {
			countData=data;
				if(data.status=="success"){
					if(data.body.rowdata.length==0){
						$("#tbody").append('<tr><td colspan="3">暂时无数据</td></tr>');
						$("#bt").hide();
					}else{
						for(var i=0;i<data.body.rowdata.length;i++){
							$("#tbody").append('<tr><td class="bs-checkbox"><input name="partner" type="checkbox"></td><td><input type="hidden" value="'+data.body.rowdata[i].PARTNER_TYPE_IDS+'">  '+data.body.rowdata[i].PARTNER_TYPE_MESSAGE+'</td><td>'+data.body.rowdata[i].COUNT+'</td></tr>');
						}
						$("#bt").show();
						var myChart = echarts.init(document.getElementById('bt'));
						myChart.setOption(bt());
					}
				}else{
					alert( "查询失败");
				}
			},
			error : function(data) {
				alert( "查询失败");
			}
		});
		
		$("#tbody tr").click(function(){

                $(this).find(":checkbox").click(function(event){
                    event.stopPropagation();
                });
                if($(this).find(":checkbox").prop("checked")==true){
                    $(this).find(":checkbox").prop("checked",false);
                }else{
                    $(this).find(":checkbox").prop("checked",true);
                }
               
                partner_type_ids=$(this).children('td').eq(1).find("input").val();
               
        });

	});
	
	function inspe(){
		if($("input:checkbox:checked").length!=1){
			alert("请选择一条数据");
			return;
		}
		window.location.href="./partnerList.jsp?token=<%=token%>&partner_type_ids="+partner_type_ids;
	}
	
	//饼图
	function bt(){
		var zdata = [];
      	for(var i=0;i<countData.body.rowdata.length;i++){
			zdata.push({"value":countData.body.rowdata[i].COUNT,"name":countData.body.rowdata[i].PARTNER_TYPE_MESSAGE});       
		}
   		 
		var option1 = {
		    title : {
		        text: '合作伙伴数量统计',
		        x:'center'
		    },
		    tooltip : {
		        trigger: 'item',
		        formatter: "{a} <br/>{b} : {c} ({d}%)"
		    },
		    legend: {
		        orient : 'vertical',
		        x : 'left'
		    },
		    toolbox: {
		        show : true,
		        feature : {
		            mark : {show: true},
		            dataView : {show: true, readOnly: false},
		            magicType : {
		                show: true, 
		                type: ['pie', 'funnel'],
		                option: {
		                    funnel: {
		                        x: '25%',
		                        width: '50%',
		                        funnelAlign: 'left',
		                    }
		                }
		            },
		            restore : {show: true},
		            saveAsImage : {show: true}
		        }
		    },
		    calculable : true,
		    series : [
		        {
		            name:'占比',
		            type:'pie',
		            radius : '45%',
		            center: ['50%', '70%'],
		            data:zdata
		        }
		    ]
		};
		return option1;
	}

</script>

</html>