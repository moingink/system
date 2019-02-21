buttonJson =[
              {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
              {name:'新增',fun:'tog(this),add_es()',buttonToken:'add'},
              {name:'修改',fun:'updateRow(this)',buttonToken:'update'},           
              {name:'删除',fun:'delRows(this)',buttonToken:'delete'},
              {name:'新增计划',fun:'add(this)',buttonToken:''},
             {name:'修改计划',fun:'update(this)',buttonToken:''}
             ];
             
             
/*新增计划*/
function add(){
	window.location.href=context+"/pages/cusPlanVisit.jsp?field=ID&fieValue=&planId=";
}

/*修改计划*/
function update(){
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('modal', "请选择一条数据进行操作");
		return;
	}
	window.location.href=context+"/pages/cusPlanVisit.jsp?field=ID&fieValue="+selected[0]["ID"]+"&planId="+selected[0]["PLAN_ID"];
}
             
             
             
  
var sel="";
var id_s="";
var select_ids="";

function add_es(){
	$("#VISIT_CATEGORY").change(function() {
	var  select_names=$("#VISIT_CATEGORY").val();	
	     select_ids=select_names;
	//alert(select_names);
	if(select_names=="0"){
		$("#VISIT_PLAN").removeAttr("disabled");	
	}
	else{
		$("#VISIT_PLAN").attr("disabled", true);
		$("#VISIT_PLAN").val("");
		$("#CUSTOMER_NAME").val("");	
		
	}
	
	});
}

function add_s(){
    if(select_ids=="0"){
       var name=$("#VISIT_PLAN").val();
       var id=$("#PLAN_ID").val();
 	   id_s=id;
    }
    else{
    	$("#VISIT_PLAN").val("");
    	
    	return ;
    }
	// if(name=="0"){
	 // $("#VISIT_PLAN").removeAttr("disabled");	
	// }else(
	 // $("#VISIT_PLAN").attr("disabled", true);	
	// )

	//var name=$("#VISIT_PLAN").val();
	var url="/system/base?cmd=find_visit_id_message&id="+id;
	$.ajax({
			type: "POST",
			url: url,
			//data: {ID:[pid],IN_BUSINESS:[name]}, 
			dataType: "json",
			async: false,
			cache: false,
			contentType: false,
			processData: false,
			success: function(data) {
				     //alert(JSON.stringify(data));
				     sel= data[0]["VIS_LAST_NUM"];
				  	 $("#CUSTOMER_NAME").val(data[0]["CUS_NAME"]);	
				     if(sel=="0"){
				    	return ;
				     }
				     else{
				     sel--;
				     }
			   // var rows=data["rows"];
				// // alert(JSON.stringify(rows));			
				// for(var i =0 ;i<rows.length;i++){
					// if(id==rows[i]["ID"]){
					// //alert(id+" "+rows[i]["ID"]);
					// sel=rows[i]["VIS_LAST_NUM"];	
				    // //alert(sel);
				    // if(sel=="0"){
				    	// alert("此计划已无拜访次数！");
				    	// return ;
				    // }	
				    // sel--;
				    // getlevel(sel,id);							
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

function ref_end(){
	 //var url="/system/buttonBase?cmd=button&buttonToken=query&dataSourceCode=VIS_PLAN&token="+token;
	 add_s();
	
}


function getlevel(name,id){
	var url="/system/base?cmd=_num&sel="+name+"&id="+id;
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
				//alert("aaaaaaaaaaaaaaaaa");
                //var da=data["CUS_LEVEL"];
                 
              
         
			}, error: function(data) {
				var da=JSON.stringify(data);
				alert("查询失败，请联系管理员！sss" + "\t" + "错误代码：" + da);
			}
			});
}

function save(t){
	$inspage.data( "bootstrapValidator").validate();
    if(!$inspage.data('bootstrapValidator').isValid()){ 
        return ; 
    }
    
     if(sel=="0"){
     	alert("此计划已无拜访次数！");
     	return  ; 
     }
     else{
     	 getlevel(sel,id_s);
     var cache_dataSourceCode =$("#cache_dataSourceCode").val();
	          var _dataSourceCode=dataSourceCode;
	          if(cache_dataSourceCode!=null&&cache_dataSourceCode.length>0){
		      _dataSourceCode=cache_dataSourceCode;
	 }
	          saveByDataSourceCode(t,_dataSourceCode);
    
    }

}