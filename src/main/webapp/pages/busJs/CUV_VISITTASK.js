buttonJson =[
             {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
             {name:'新增',fun:'tog(this)',buttonToken:'add'},
             {name:'修改',fun:'updateRow(this)',buttonToken:'update'},
             {name:'删除',fun:'delRows(this)',buttonToken:'delete'},
             {name:'推送移动端',fun:'jts()'}
			];
			
function jts(){
	var selected = JSON.parse(getSelections());
	if(selected.length = 0){
		oTable.showModal('modal', "请选择要推送的任务");
		return;
	}
	oTable.showModal('modal',"推送成功！");
}
