///**
// * 供应商评价
// * @author yansu
// */
//
//buttonJson =[
//  /*           
//             {name:'新增',fun:'tog(this)',buttonToken:'add'},
//             {name:'修改',fun:'updateRow(this)',buttonToken:'update'},
//             {name:'删除',fun:'delRows(this)',buttonToken:'delete'},*/
//  {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
//             {name:'评价',fun:'view2(this)',buttonToken:'checkMeta'}
//			];
//
//
//function view2(){
//	var selected = JSON.parse(getSelections());
//	
//	//alert(JSON.parse(getSelections())[0]["ID"])
//	//alert(JSON.parse(getSelections())[0]["EVAL_CRIT_ID"])
//	//var jsonData = '{"bd_supplierId":"' + JSON.parse(getSelections())[0]["ID"] + '"}';
//	var jsonData='{"as":"123"}';
//	$.ajax({  
//		url : "/system/supcycle/inquire",
//        dataType : "json",  
//        type : "POST",
//        //async: false,
//        data:{"jsonData":jsonData},
//        success : function(data) {
//        	if(data.state){
//        		//alert(data.state)
//        		oTable.showModal('modal', "已生效");
//        	}else{
//        		//alert(data.state)
//        		oTable.showModal('modal', "生效失败,请重新操作");
//        	}
//        } 
//    });
//	
//}