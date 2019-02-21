
buttonJson =[	
              {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
              {name:'新增',fun:'tog(this)',buttonToken:'add'},
              {name:'退回',fun:'approval(this,7)',buttonToken:'audit'}
			];





function approval(t,type){
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('modal', "请选择一条数据进行操作");
		return;
	}
	var billStatus = selected[0]['BILL_STATUS'];
	if(billStatus != 1){
		oTable.showModal('modal', "单据状态不是已提交");
		return;
	}
	var audit_message=prompt("请填审批意见"); /*在页面上弹出提示对话框，*/
	var apprType="&audit_message="+audit_message+"&audit_type="+type;
	var message = transToServer(findBusUrlByPublicParam(t,apprType,dataSourceCode),JSON.stringify(selected[0]));
	oTable.showModal('modal', message);
	queryTable(t);
}
