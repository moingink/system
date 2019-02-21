
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
