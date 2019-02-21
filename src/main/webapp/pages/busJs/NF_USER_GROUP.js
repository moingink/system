buttonJson =[
             {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
             {name:'新增',fun:'tog(this),initDiv()',buttonToken:'addByNotity'},
             {name:'修改',fun:'updateRow(this),initDiv()',buttonToken:'updateByNotity'},
             {name:'删除',fun:'delRows(this)',buttonToken:'delete'}
			];



function initDiv(){
	$("#GROUP_VALUE").attr("readonly",false);
	$("#GROUP_TYPE").find("option").attr('onclick',"updateRefGroupName(this)");
	$("#GROUP_VALUE").parent().parent().hide();
	var selectVal =$("#GROUP_TYPE").val();
	updateRef(selectVal);
}

function updateRefGroupName(t){
	var selectVal =$(t).val();
	updateRef(selectVal);
}

var select_val ="";

function updateRef(selectVal){
	var refOnclick="refError()";
	if(selectVal=='0'){
		refOnclick ="checkReference(this,'REF(RM_USER,ID:GROUP_VALUE;NAME:GROUP_NAME,1)','GROUP_NAME','INSUP')";
	}else if(selectVal =='1'){
		refOnclick ="checkReference(this,'REF(TM_COMPANY,ID:GROUP_VALUE;NAME:GROUP_NAME,1)','GROUP_NAME','INSUP')";
	}else if(selectVal =='2'){
		refOnclick ="checkReference(this,'REF(RM_ROLE,ID:GROUP_VALUE;NAME:GROUP_NAME,1)','GROUP_NAME','INSUP')";
	}
	$("#GROUP_NAME").next().attr('onclick',refOnclick);
	if(selectVal!=select_val){
		$("#GROUP_NAME").val("");
		$("#GROUP_VALUE").val("");
		select_val=selectVal;
	}
	
}

function refError(){
	alert("请选着类型信息！");
}

