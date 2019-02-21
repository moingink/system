/**
 * 供应商评价标准
 * @author yansu
 */
buttonJson =[
             {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
             {name:'新增',fun:'tog(this)',buttonToken:'add'},
             {name:'修改',fun:'updateRow(this)',buttonToken:'update'},
             {name:'删除',fun:'delRows(this)',buttonToken:'delete'},
             //{name:'供应商评价维度配置',fun:'view(this)',buttonToken:'checkMeta'},
             {name:'配置评价等级配置',fun:'view1(this)',buttonToken:'checkMeta'},
             {name:'供应商评价维度',fun:'view2(this)',buttonToken:'checkMeta'}
			];

	/**
	 * dimension Configuration
	 */
	function view(t){
		
		//查看子表相关配置
		var pageCode = 'EVAL_CRIT_DIMENSION';//子表数据源
		var pageName = '供应商评价标准维度';//列表显示名称
	//	var ParentPKField = 'PEOJ_ID';//主表主键在子表中字段名
		
		var selected = JSON.parse(getSelections());
//		if(selected.length != 1){
//			oTable.showModal('modal', "请选择一条数据进行操作");
//			return;
//		}
		window.location.href=context+"/pages/childTableModify.jsp?pageCode="+pageCode+"&pageName="+pageName /*+"&ParentPKField="+ParentPKField+"&ParentPKValue="+JSON.parse(getSelections())[0]["ID"]*/; 
	}
	
	/**
	 * Grade Configuration
	 */
	function view1(t){
		
		//查看子表相关配置
		var pageCode = 'EVAL_CRIT_GRADE';//子表数据源
		var pageName = '供应商评价标准等级';//列表显示名称
	//	var ParentPKField = 'PEOJ_ID';//主表主键在子表中字段名
		
//		var selected = JSON.parse(getSelections());
//		if(selected.length != 1){
//			oTable.showModal('modal', "请选择一条数据进行操作");
//			return;
//		}
		window.location.href=context+"/pages/childTableModify.jsp?pageCode="+pageCode+"&pageName="+pageName /*+"&ParentPKField="+ParentPKField+"&ParentPKValue="+JSON.parse(getSelections())[0]["ID"]*/; 
	}
	
	/**
	 * 
	 */
	function view2(t){
		
		//查看子表相关配置
		var pageCode = 'EVAL_CRIT_DIMENSION';//子表数据源
		var pageName = '供应商评价标准添加维度';//列表显示名称
		var ParentPKField = 'EVAL_CRIT_ID';//主表主键在子表中字段名
		
		var selected = JSON.parse(getSelections());
		if(selected.length != 1){
			oTable.showModal('modal', "请选择一条数据进行操作");
			return;
		}
		var EVAL_CRITID=(JSON.parse(getSelections())[0]["ID"]);
		window.location.href=context+"/pages/adddimension.jsp?pageCode="+pageCode+"&pageName="+pageName +"&ParentPKField="+ParentPKField+"&ParentPKValue="+EVAL_CRITID; 
	}