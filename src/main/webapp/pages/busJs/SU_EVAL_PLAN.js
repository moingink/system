/**
 * 供应商评价方案
 * @author yansu
 */

buttonJson =[
             {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
             {name:'新增',fun:'tog(this)',buttonToken:'add'},
             {name:'修改',fun:'updateRow(this)',buttonToken:'update'},
             {name:'删除',fun:'delRows(this)',buttonToken:'delete'},
             {name:'方案发布',fun:'view2(this)',buttonToken:'checkMeta'},
            // {name:'test',fun:'view3(this)',buttonToken:'checkMeta'}
			];

function view3(t){
	//var selected = JSON.parse(getSelections());
	
	//alert(JSON.parse(getSelections())[0]["ID"])
	//alert(JSON.parse(getSelections())[0]["EVAL_CRIT_ID"])
	console.info(getSelections());
	
	var jsonData='{"as":"123"}';
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
}

function view2(){
	var selected = JSON.parse(getSelections());
	if (selected.length < 1) {
		oTable.showModal('modal', "请选择一条数据进行操作");
		return;
	}
	//console.log(selected);
	for (var int = 0; int < selected.length ; int++) {
		if (selected[int]["WHETHER_PUBLISH"] == 1) {
			oTable.showModal('modal', "选择的数据中含有<span style='color: red;'>[状态]</span>为<span style='color: red;'>[已发布]</span>的,请取消勾选！");
			return;
		} 
	}
	//alert(selected[0][""]
	
//	for (var int1 = 0; int1 < selected.length ; int1++) {
//		alert(selected[int1]["WHETHER_PUBLISH"])
//	}
	
	//SU_EVAL_PLAN
//	alert(JSON.parse(getSelections())[0]["ID"])//ID
//	alert(JSON.parse(getSelections())[0]["G_DIME"])//评价标准名称
//	alert(JSON.parse(getSelections())[0]["G_DEPT"])//评分部门
//	alert(JSON.parse(getSelections())[0]["SUPPLIER"])//供应商
//	alert(JSON.parse(getSelections())[0]["SCORE_START_DATE"])//评分开始时间	
//	alert(JSON.parse(getSelections())[0]["SCORE_END_DATE"])//评分结束时间
//	alert(JSON.parse(getSelections())[0]["SCORING_CYCLE"])//评分周期
//	alert(JSON.parse(getSelections())[0]["SCHEME_NAME"])//方案名称
//	alert(JSON.parse(getSelections())[0]["SCHEME_NUM"])//方案编号
//	alert(JSON.parse(getSelections())[0]["WHETHER_PUBLISH"])//是否发布
//	alert(JSON.parse(getSelections())[0]["EVAL_CRIT_ID"])//方案ID（评价标准ID）
	
//	var jsonData='{"ID": "'+ JSON.parse(getSelections()) + '"}';
//	//alert(jsonData)
	$.ajax({
		url : "/system/supcycle?inquire",
        dataType : "json",  
        type : "POST",
        //async: false,
        data:{"jsonData": getSelections() },
        success : function(data) {
        	if(data.state){
        		//alert(data.state)
        		oTable.showModal('modal', "操作已生效");
        		setTimeout('gotoURL()',2000); 
        	}else{
        		if(data.desc!=null){
        			oTable.showModal('modal', "操作生效失败,请重新操作"+":"+data.desc);
        		}else{
        			oTable.showModal('modal', "操作生效失败,请重新操作");
        		}
        	}
        }
    });
	
}

function gotoURL(){
	var url = "/pages/singleTableModify.jsp?pageCode=SU_EVAL_PLAN&pageName=供应商评价方案&userId="+userId+"&userName="+userName;
	//alert(url);
	url = context + url;
	//alert(url);
	window.location.href = url;
}