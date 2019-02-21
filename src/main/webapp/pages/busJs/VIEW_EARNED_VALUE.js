buttonJson =[
             {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
             {name:'计算',fun:'calculation(this)',buttonToken:'insertInvoiceOpen'},
             {name:'历史',fun:'history(this)',buttonToken:'insertInvoiceOpen'}
			];


function calculation(t){
	var selected = JSON.parse(getSelections());
	if(selected.length == 0){
		oTable.showModal('modal', "请选择一条数据进行操作");
		return;
	}
	var selectIds ="";
	for(var i=0; i<selected.length;i++){
		if(i==selected.length-1){
			selectIds+="'"+selected[i]["PROJ_CODE"]+"'";
		}else{
			selectIds+="'"+selected[i]["PROJ_CODE"]+"'"+",";
		}
	}
	//调用后台计算挣值 刷新当前页面  传入项目code 自动计算项目的各项指数到 挣值表 保存,并在历史表保存一份
	$.ajax({  
    	url : "/system/earned/calculation?ids="+selectIds+"&token="+token,  
        type : "GET", 
        async: false, 
        success : function(data) {
        	if(data=="success"){
        		alert('操作成功');
        		queryTable(this);
        	}
        }
    });
}

function history(t){
	var selected = JSON.parse(getSelections());
	if(selected.length == 0||selected.length>1){
		oTable.showModal('modal', "请选择一条数据进行操作");
		return;
	}
	window.location.href=context+"/pages/childTableModify_earned_value.jsp?pageCode=EARNED_VALUE_HISTORY&pageName=挣值计算历史表&ParentPKField=EARNED_ID&ParentPKValue="
		+JSON.parse(getSelections())[0]["ID"];
}

function dblClickFunction(row,tr){
	console.log(row);
	console.log(tr);
	window.location.href=context+"/pages/earned_value_zxt.jsp?proj_code="+row.PROJ_CODE;
}
