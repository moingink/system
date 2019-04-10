buttonJson =[
             {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
             {name:'新增',fun:'tog(this)',buttonToken:'insertWithCache'},
             {name:'修改',fun:'updateRow(this)',buttonToken:'updateWithCache'},
             {name:'删除',fun:'delRows(this)',buttonToken:'deleteWithCache'},
 			];

//双击事件  列表模版双击事件跳转其他操作
function dblClickFunction(row,tr){
	var json =JSON.parse(JSON.stringify(row));
	console.log(json.ID);
	//获取主表主键传递
	//window.location.href=context+"/pages/test_demo.jsp?ParentPKValue="+json.ID+"&token="+token;
	
}

