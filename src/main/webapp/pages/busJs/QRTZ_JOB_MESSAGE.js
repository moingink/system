

buttonJson =[
             {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
             {name:'新增',fun:'tog(this)',buttonToken:'jobInsert'},
             {name:'修改',fun:'updateRow(this)',buttonToken:'update'},
             {name:'删除',fun:'delRows(this)',buttonToken:'jobDelete'},
             {name:'启动',fun:'jobStartUp(this)',buttonToken:'jobStateUp'},
             {name:'暂停',fun:'jobStop(this)',buttonToken:'jobStop'}
			];



function jobStartUp(t){
	var tableJsons =getTableJsons();
	var checkJson=[{CheckColumn:'STATE',CheckValue:'0',ErrorMessage:'已启动的任务,不能再次启动！',IsEqual:true}
				  ];
	
	busPublcFunction(tableJsons,isCheckSelect(tableJsons),checkBusStart(tableJsons,checkJson),t,'','确认启动这些任务吗？');
}

function jobStop(t){
	var tableJsons =getTableJsons();
	var checkJson=[{CheckColumn:'STATE',CheckValue:'1',ErrorMessage:'已停用的任务，不能再次停用！',IsEqual:true}
				  ];
	busPublcFunction(tableJsons,isCheckSelect(tableJsons),checkBusStart(tableJsons,checkJson),t,'','确认停用这些任务吗？');
} 




