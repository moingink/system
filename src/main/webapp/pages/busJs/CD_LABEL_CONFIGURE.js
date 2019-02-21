buttonJson =[                
                {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
				{name:'新增',fun:'tog(this)',buttonToken:'add'},
				{name:'删除',fun:'delRows(this)',buttonToken:'labelConfigureDelete'},
				{name:'标签配置明细',fun:'detail(this)',buttonToken:'detail'},
			];	

//初始化
$(function(){
	setUp();
})

//界面设置
function setUp(){
	$('#DATASOURCE_CODE,#METADATA_CODE').attr("readonly",true);//数据源编码、 元数据编码 不可编辑
}

//标签配置明细
function detail(){
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('modal', "请选择一条数据进行操作");
		return;
	}
	window.location.href=context+"/pages/childTableModify.jsp?pageCode=CD_LABEL_CONFIGURE_DETAIL&pageName=标签配置明细&ParentPKField=PARENT_ID&ParentPKValue="+selected[0]["ID"]+"&token="+token;
}
