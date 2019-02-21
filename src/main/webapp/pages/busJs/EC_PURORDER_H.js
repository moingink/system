/**
 * 订单采购子
 */
buttonJson =[	
             	/*
				{name:'查询',fun:'queryTable(this)',buttonToken:'query'},*/
				{name:'新增',fun:'tog(this),readonly()',buttonToken:'add'},
				{name:'修改',fun:'updateRow(this)',buttonToken:'update'},
				{name:'删除',fun:'delRows(this)',buttonToken:'delete'}/*,
				{name:'返回',fun:'window.history.back(-1);',buttonToken:'checkMeta'}*/
               
			];



//计算此条下合同的价税合计存到主表用以审批
function backw(){
	var index = '';
	$('#table thead tr').each(function(i) {
		$(this).children('th').each(function(j) {
			var dataField = $(this).attr('data-field');
			if(dataField == 'LEVIED_TOTAL'){
				index = j;
			}
		});
	});
	var LEVIED_TOTAL_SUM=0.00;
	if(index != null && index != ''){
		var trSum=$('#table tbody tr').each(function(i) { // 遍历 tr
			$(this).children('td').each(function(j) { // 遍历 tr 的各个 td
				if (j==index) {
					LEVIED_TOTAL_SUM=accAdd(LEVIED_TOTAL_SUM,$(this).text());
				}
			});
		});
	}
	LEVIED_TOTAL_SUM=LEVIED_TOTAL_SUM.toFixed(2);
	if (LEVIED_TOTAL_SUM!=0.00) {
		$("#LEVIED_TOTAL", window.parent.document).val(LEVIED_TOTAL_SUM);
			var jsonStr='{\"ID\":\"'+ParentPKValue+'\",\"LEVIED_TOTAL_SUM\":\"'+LEVIED_TOTAL_SUM+'\"}';
			//alert(jsonStr);
			update_AJAX(jsonStr);
			
	}
	//var datasoures="EC_PURORDER_H_MAIN";
	//back(t);
	//setTimeout('window.history.back(-1);',2000);  
	//window.history.back(-1);
	//queryTableByDataSourceCode(t,datasoures);
}


/**
 * 回写到主表
 * @param {Object} selected
 */
function update_AJAX(selected){
		$.ajax({
		url : "/system/ecpurorderh/update",
        dataType : "json",  
        type : "POST",
        //async: false,
        data:{"jsonData": selected },
        success : function(data) {
        	if(data.state){
        		//alert(data.state)
        		console.log('modal', "合价成功");
        	}else{
        		//alert(data.state)
        		console.log('modal', data.desc);
        	}
        }
    });
}

/**
 * js加运算
 * @param arg1
 * @param arg2
 * @returns {Number}
 */
function accAdd(arg1,arg2){ 
	var r1,r2,m;  
	try{
	r1=arg1.toString().split(".")[1].length
	}catch(e){
	r1=0}  try{
	r2=arg2.toString().split(".")[1].length}catch(e){r2=0}  m=Math.pow(10,Math.max(r1,r2))  
	return (arg1*m+arg2*m)/m
	}

/**
 * readonly
 */
function readonly(){
	$("#NAME_GOODS").attr("readonly","readonly");//物资名称
	$("#UNIT_PRICE").attr("readonly","readonly");//含税单价
	$("#RATE_OF").attr("readonly","readonly");//税率
	$("#TAX_PRICE").attr("readonly","readonly");//不含税单价
	$("#PRICE_DISCOUNT").attr("readonly","readonly");//价格折扣
	
	$("#TAX_FREE").attr("readonly","readonly");//不含税金额
	$("#LEVIED_TOTAL").attr("readonly","readonly");//价税合计
	$("#TAX_OF").attr("readonly","readonly");//税额
	
}

$(function(){
    $('input').bind('input propertychange', function() {  
        var NAME_GOODS = $("#NAME_GOODS").val();
        var UNIT_PRICE = $("#UNIT_PRICE").val();//含税单价
        var RATE_OF = $("#RATE_OF").val();
        var TAX_PRICE = $("#TAX_PRICE").val();//不含税单价（元）
        var NUMBER_OF = $("#NUMBER_OF").val();//数量
        var result = 0;
          if( NAME_GOODS!=null||NAME_GOODS!="" && UNIT_PRICE!=null||UNIT_PRICE!="" && RATE_OF!=null||RATE_OF!="" && TAX_PRICE!=null||TAX_PRICE!="" ){
        	//  alert(NAME_GOODS+")))))")
        	  //不含税单价*数量=不含税金额
        	  resultTAX_FREE = parseInt(TAX_PRICE)*(parseInt(NUMBER_OF));
        	  $("#TAX_FREE").val(resultTAX_FREE.toFixed(2));
        	  //价税合计 = 含税单价 *数量
        	  resultLEVIED_TOTAL = parseInt(UNIT_PRICE)*(parseInt(NUMBER_OF));
        	  $("#LEVIED_TOTAL").val(resultLEVIED_TOTAL.toFixed(2));
        	  //税额 = 价税合计 -  不含税金额
        	  resultTAX_OF = parseInt(resultLEVIED_TOTAL) - (resultTAX_FREE);
        	  $("#TAX_OF").val(resultTAX_OF.toFixed(2));
          }else{
        	 // $("#COST_SAVING_AMOUNT").val(result)
          }
    });
    $table.on('load-success.bs.table',function(data){
    	backw();
     });
});
/*
buttonJson =[	
				{name:'查询',fun:'queryTable(this)',buttonToken:'query'},
				{name:'新增',fun:'tog(this)',buttonToken:'add'},
				{name:'修改',fun:'updateRow(this)',buttonToken:'update'},
				{name:'删除',fun:'delRows(this)',buttonToken:'delete'},
                {name:'提交',fun:'approval(this,1)',buttonToken:'audit'}
			];





function approval(t,type){
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('modal', "请选择一条数据进行操作");
		return;
	}
	var billStatus = selected[0]['BILL_STATUS'];
	if(billStatus != ''&&billStatus != 0 && billStatus != 7){
		oTable.showModal('modal', "只能提交  已保存、已退回的单据");
		return;
	}
	var apprType="&audit_type="+type;
	var message = transToServer(findBusUrlByPublicParam(t,apprType,dataSourceCode),JSON.stringify(selected[0]));
	oTable.showModal('modal', message);
	queryTable(t);
}
*/