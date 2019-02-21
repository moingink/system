buttonJson =[
             {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
             {name:'新增',fun:'init(),tog(this)',buttonToken:'addByNotity'},
             {name:'修改',fun:'hideDiv(),updateRow(this)',buttonToken:'updateByNotity'},
             {name:'删除',fun:'delRows(this)',buttonToken:'delete'}
            
			];



function init(){
	hideDiv();
	$("#"+parent.ParentId).val(parent.ParentValue);
}


function hideDiv(){
	$("#"+parent.ParentId).parent().parent().hide();
}