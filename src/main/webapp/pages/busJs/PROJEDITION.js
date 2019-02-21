/**
 * @author Administrator
 */
 buttonJson =[                
                {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
				//{name:'新增',fun:'tog(this),addmessage()',buttonToken:'add'},
				{name:'修改',fun:'updateRow(this),up()',buttonToken:'update'}
				//{name:'删除',fun:'delRows(this)',buttonToken:'delete'},
				];
				

function up(){
	$("#EDITION_NAME").attr("disabled",true);
}


function save(t){
	$inspage.data("bootstrapValidator").validate();
    if(!$inspage.data('bootstrapValidator').isValid()){ 
    	setParntHeigth($("body").height());
        return ; 
    }
    
    var cache_dataSourceCode =$("#cache_dataSourceCode").val();
	var _dataSourceCode=dataSourceCode;
	if(cache_dataSourceCode!=null&&cache_dataSourceCode.length>0){
		_dataSourceCode=cache_dataSourceCode;
	}
	saveByDataSourceCode(t,"PROJ_EDITION");
	
}

function saveByDataSourceCode(t,_dataSourceCode){
	savaByQuery(t,_dataSourceCode,$inspage);
}

function savaByQuery(t,_dataSourceCode,$div){
	var message ="";
	var buttonToken =$("#ins_or_up_buttontoken").val();
	message = transToServer(findBusUrlByButtonTonken(buttonToken,'',_dataSourceCode),getJson($div));
	oTable.showModal('modal', message);
	back(t);
	queryTableByDataSourceCode(t,"PROJEDITION");
}