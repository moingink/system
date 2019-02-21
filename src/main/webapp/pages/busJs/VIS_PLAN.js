buttonJson =[
             {name:'查询',fun:'queryTable(this)',buttonToken:'query'}, 
             {name:'新增计划',fun:'add(this)',buttonToken:''},
             {name:'修改计划',fun:'update(this)',buttonToken:''},
             {name:'复制计划',fun:'',buttonToken:''}
			];

	
/*新增计划*/
function add(){
	window.location.href=context+"/pages/cusPlanVisit.jsp?field=ID&fieValue=&planId=201808020000000005";
}

/*修改计划*/
function update(){
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('modal', "请选择一条数据进行操作");
		return;
	}
	window.location.href=context+"/pages/cusPlanVisit.jsp?field=ID&fieValue="+selected["ID"]+"&planId="+selected["plan_id"];
}
/*
buttonJson =[
             {name:'查询',fun:'queryTable(this)',buttonToken:'query'}, 
            {name:'新增',fun:'tog(this),add_ss()',buttonToken:'add'},
            {name:'修改',fun:'updateRow(this),update_ends()',buttonToken:'update'},
            {name:'删除',fun:'delRows(this)',buttonToken:'delete'}
			];*/

var sel="";

function addPaln(){
	
	//查看子表相关配置
	var pageCode = 'ATT_PERSON';//子表数据源
	var pageName = '随从人员';//列表显示名称
	var ParentPKField = 'FREQ_ID';//主表主键在子表中字段名
	
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('modal', "请选择一条数据进行操作");
		return;
	}
	window.location.href=context+"/pages/childTableModify.jsp?pageCode="+pageCode+"&pageName="+pageName+"&ParentPKField="+ParentPKField+"&ParentPKValue="+JSON.parse(getSelections())[0]["ID"];
}

function add_ss(){
	$("#PLAN_STATE").empty(); 
	$("#PLAN_STATE").attr("disabled", true);
 	var select_mess=$("#PLAN_STATE");
	select_mess.append("<option value='2'>未执行</option>");	
	
	$("#VIS_LAST_NUM").attr("disabled", true);
	$("#VIS_FREQ").attr("disabled", true);
	$("#CUS_LEVEL").attr("disabled", true);
	
}
var i="0";
var num_data=["","","","","","","",""];
function add_update(){
    var data = $("#ATT_PERSON_NAME").val();	
    alert(data);
	$("#ATT_PERSON_NAME").change(function() {
		data=$("#ATT_PERSON_NAME").val();
		alert(i);
	if(i=="0"){
		num_data[0]=data;
		alert(num_data[0]);
		i="1";
	}
    else if(i=="1"){
    	alert(num_data[0]);
    	num_data[1]=num_data[0]+","+data;
    	alert(num_data[1]);
    	$("#ATT_PERSON_NAME").val(num_data[1]);
    }
		});
}


function add_s(id){
	var name=$("#CUS_NAME").val();
 	id=$("#CUS_ID").val();	
 	//var url="/system/buttonBase?cmd=button&buttonToken=query&dataSourceCode=CUMA_ACCOUNT&ID="+id+"&CUSTOMER_PERSON="+name+"&token="+token;
 	var url="/system/base?cmd=find_cus_id_message&id="+id;
 	//alert(id+" "+name);
 	//alert(url);
	$.ajax({
			type: "POST",
			url: url,
			// data: {ID:[pid],IN_BUSINESS:[name]}, 
			dataType: "json",
			async: false,
			cache: false,
			contentType: false,
			processData: false,
			success: function(data) {
				if(data["CUS_LEVEL"]=="0"){
				 return ;
				}
				else{
				 sel=data[0]["CUSTOMERLEVEL"];
				 $("#CUS_LEVEL").val(sel);
				 getlevel(sel);
				}
				// var rows=data["rows"];
				// //alert(JSON.stringify(rows));			
				// for(var i =0 ;i<rows.length;i++){
					// if(id==rows[i]["ID"]){
					// //alert(id+" "+rows[i]["ID"]);
					// sel=rows[i]["CUSTOMERLEVEL"];
				    // alert(sel);	
// 				    				
				    // $("#CUS_LEVEL").val(sel);
// 				   
					// getlevel(sel);							
				 // }
				// }
			}, error: function(data) {
				var da=JSON.stringify(data);
				alert("查询失败，请联系管理员！" + "\t" + "错误代码：" + da);
			}
			});
	
}

function getQueryString(name) {
  var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
  var r = window.location.search.substr(1).match(reg);
  if (r != null) {
    return unescape(r[2]);
  }
  return null;
}



function update_ends(){
	$("#VIS_LAST_NUM").attr("disabled", true);
    $("#VIS_FREQ").attr("disabled", true);
    $("#PLAN_STATE").attr("disabled", true);
}

function ref_end(){
	
	var id="0";
	add_s(id);
	

    //add_update();
   
}

function getlevel(name){
	var url="/system/base?cmd=find_Messageses&sel="+name;
	$.ajax({
			type: "POST",
			url: url,
			// data: {ID:[pid],IN_BUSINESS:[name]}, 
			dataType: "json",
			async: false,
			cache: false,
			contentType: false,
			processData: false,
			success: function(data) {
              var is_sum=data[0];
              if(is_sum=="0"){
              	alert("此客户级别未配置！");
              }
              else{
                var da=data["CUS_LEVEL"];
                //alert(da);
                $("#VIS_LAST_NUM").val(da);
                $("#VIS_FREQ").val(da); 	
              }
			}, error: function(data) {
				var da=JSON.stringify(data);
				alert("查询失败，请联系管理员！" + "\t" + "错误代码：" + da);
			}
			});
}



