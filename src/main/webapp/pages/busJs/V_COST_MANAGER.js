buttonJson =[                
                {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
				{name:'补录',fun:'updateRow(this)',buttonToken:'updateCostManager'}
			];	
			
$(function(){
	$('#insPage').find('#BUSINESS_TYPE,#PRODUCT_TYPE,#PRODUCT').attr("readonly","true");
});