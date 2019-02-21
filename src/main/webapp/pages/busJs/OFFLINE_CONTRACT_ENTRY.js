buttonJson =[
             {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
             {name:'新增',fun:'tog(this),pd()',buttonToken:'add'},
             {name:'修改',fun:'updateRow(this),pd()',buttonToken:'update'},
             {name:'删除',fun:'delRows(this)',buttonToken:'delete'},
             {name:'导出',fun:'outmessage(this)',buttonToken:'outmessage'}
            ];
            
isAuditValidate=true;           

function outmessage(){
	window.location.href="/system/buttonBase?cmd=button&buttonToken=button_export4&dataSourceCode=OFFLINE_CONTRACT_ENTRY_OUT&token="+token;   
}


//履行PERFORM_NAME   PERFORM_DEP


//UNDERTAKE_NAME    UNDERTAKE_DEP    UNDERTAKE_ID
 
//重写参选回调函数
function ref_end_code(ref_dataSourceCode,rejsonArray,ref_column){
	if(ref_column == 'UNDERTAKE_NAME'){//合同承办人
		var undertakeId = $('#UNDERTAKE_ID').val();
		$('#UNDERTAKE_DEP').val(getCompanyInFo(undertakeId)['NAME']);
	}
	
	else  if(ref_column == 'PERFORM_NAME'){//合同承办人
		var performId = $('#PERFORM_ID').val();
		$('#PERFORM_DEP').val(getCompanyInFo(performId)['NAME']);
	}
}

//获取合同附件
function getBusContractAdminAttachment(){
	var conId = $('#CON_ID').val();//合同主键
	if(conId != null && conId != ''){
		initLoadAffixJsp($('#insPage').find('#ID'));
		MultiNodeView('BUS_CONTRACT_ADMIN',conId);
	}
}


function pd(){
	
	 var  his=document.getElementById("HIS_CONTRACT");
          his[1].selected=true;
          
          $("#HIS_CONTRACT").attr("disabled",true);
          
     var  hiss=document.getElementById("IF_OFFLINE");
          hiss[1].selected=true;     
          $("#IF_OFFLINE").attr("disabled",true);
          //$("#IF_OFFLINE").parent().parent().parent().hide();
          
          $("#UNDERTAKE_DEP").attr("disabled",true);
          $('#PERFORM_DEP').attr("disabled",true);
     $("#VALUE_ADDED_TAX_PRICE").blur(function() {
          var je=$("#VALUE_ADDED_TAX_PRICE").val();
          if(je>5000000){
          	$("#IF_FIVE_MILLION").val(1);
          	$("#IF_FIVE_MILLION").attr("disabled",true);
          }
      });
          i
}



function getCompanyInFo(userId){
	var userInFo = querySingleRecord("&dataSourceCode=RM_USER_REF&SEARCH-ID="+userId);
	var companyId = userInFo['ORGANIZATION_ID'];
	var companyInFo = querySingleRecord("&dataSourceCode=TM_COMPANY_REF&SEARCH-ID="+companyId);
	return companyInFo;
}

//重写主表删除
// function delRowsByDataSourceCode(t,_dataSourceCode){
	// var selected = JSON.parse(getSelections());
	// if(selected.length < 1){
		// oTable.showModal('提示', "请至少选择一条数据进行删除！");
		// return;
	// }
	// var json = new Array();
	// for(var i=0;i<selected.length;i++){
		// var billStatus = selected[i]["BILL_STATUS"];
		// if(billStatus == 0 || billStatus == 7){
			// json.push(selected[i]);
		// }
	// }
	// if(json.length == 0){
		// oTable.showModal('提示', "只能删除  已保存、已退回单据！");
		// return;
	// }
	// if(!validateDel(selected)){
		// return ;
	// }
	// var message = transToServer(findBusUrlByPublicParam(t,'',_dataSourceCode),JSON.stringify(json));
	// oTable.showModal('提示', message);
	// queryTableByDataSourceCode(t,_dataSourceCode);
// }