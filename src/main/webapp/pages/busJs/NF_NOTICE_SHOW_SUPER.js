buttonJson =[
              {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
              {name:'新增',fun:'tog(this),add(this)',buttonToken:'add'},   
              {name:'修改',fun:'updateRow(this)',buttonToken:'update'},           
              {name:'删除',fun:'delRows(this)',buttonToken:'delete'}
             ];

$(function(){
	$("#insPage").find("#NOTICE_CODE,#USRE_ID").attr("readonly","true");// 公告编号、发布人
})

//新增
function add(t){
	$("#NOTICE_CODE").val(getCode());//公告编号
	$("#USRE_ID").val(userName);//发布人
}

//获取公告编号
function getCode(){
	var noticeCode = "";
	$.ajax({
	    url: "/system/business/getNoticeCode",
	    type: "GET",
	    async: false,
	    success: function(results) {
	    	noticeCode = results;
	    },
	    error:function(res){
	    }
	});
	return noticeCode;
}