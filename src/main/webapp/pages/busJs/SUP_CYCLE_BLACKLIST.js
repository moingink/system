/**
 * 供应商黑名单
 * @author yansu
 */
buttonJson =[
             {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
             {name:'新增黑名单供应商',fun:'tog(this)',buttonToken:'add'},
             // {name:'修改',fun:'updateRow(this)',buttonToken:'update'},
             // {name:'删除',fun:'delRows(this)',buttonToken:'delete'},         
               {name:'取消黑名单供应商',fun:'updatasb(this)',buttonToken:'checkMeta'},
			];

function updatasb(t) {
	var selected = JSON.parse(getSelections());
	if (selected.length != 1) {
		oTable.showModal('modal', "请选择一条数据进行操作");
		return;
	}
	//console.info(selected);
	$.ajax({
		url : "/system/updatesuppliers/updatasb",//updatasb
		dataType : "json",
		type : "POST",
		data : {
			"jsonData" : getSelections()
		},
		success : function(data) {
			if (data.state) {
				oTable.showModal('modal', "操作已生效");
			} else {
				oTable.showModal('modal', "操作生效失败,请重新操作");
			}
		}
	});
	//setTimeout('gotoURL()', 2000);
	queryTableByDataSourceCode(t,"SUP_CYCLE_BLACKLIST");
}


function save(t){
	$inspage.data("bootstrapValidator").validate();
    if(!$inspage.data('bootstrapValidator').isValid()){ 
    	setParntHeigth($("body").height());
        return ; 
    }
  
  var values=getJson($inspage).substr(0, getJson($inspage).length - 1);
	  values= values+","+"\"ACCESSION_PEOPLE\""+":"+"\""+ userName +"\"}";//ACCESSION_PEOPLE	添加人
	//  values= JSON.parse(values);
	//console.info(values);
	
	
	// var message ="";
	// var buttonToken =$("#ins_or_up_buttontoken").val();
	// message = transToServer(findBusUrlByButtonTonken(buttonToken,'',"SUP_CYCLE_BLACKLIST"),values);
	// oTable.showModal('modal', message);
	// back(t);
	// queryTableByDataSourceCode(t,"SUP_CYCLE_BLACKLIST");
	
	//alert(token)
		$.ajax({  
		url : "/system/updatesuppliers/insertsb?token="+token,
        dataType : "json",  
        type : "POST",
        //async: false,
        data:{"jsonData":values},
        success : function(data) {
        	if(data.state){
        		
        		oTable.showModal('modal', "操作已生效");
        		
        	}else{
        		//alert(data.state)
        		oTable.showModal('modal', "操作生效失败,请重新操作");
        	}
        } 
    });
		
		//setTimeout('gotoURL()',2000); 
		back(t);
	 queryTableByDataSourceCode(t,"SUP_CYCLE_BLACKLIST");
}
function gotoURL(){
	var url = "/pages/singleTableModify.jsp?pageCode=SUP_CYCLE_BLACKLIST&pageName=供应商黑名单&userId="+userId+"&userName="+userName;
	//alert(url);
	url = context + url;
	//alert(url);
	window.location.href = url;
}