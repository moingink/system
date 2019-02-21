buttonJson =[
              {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
              {name:'新增',fun:'tog(this),addmessage()',buttonToken:'add'},
              {name:'修改',fun:'updateRow(this),addmessage()',buttonToken:'update'},           
              {name:'删除',fun:'delRows(this)',buttonToken:'delete'}
             ];
             


function addmessage(){
	var myDate = new Date();
	//myDate.getFullYear(); //获取完整的年份(4位,1970-????)
    //myDate.getMonth(); //获取当前月份(0-11,0代表1月)
    //myDate.getDate(); //获取当前日(1-31)
	var mons=myDate.getMonth()+1;
	var time_s=myDate.getFullYear()+"-"+mons+"-"+myDate.getDate();
	
	$("#CREAT_NAME").val(userName);
	$("#UPDATE_NAME").val(userName);
	
	$("#CREAT_NAME").attr("disabled",true);
    $("#UPDATE_NAME").attr("disabled",true);
    
    $("#CREAT_TIME").val(time_s);
    $("#UPDATE_TIME").val(time_s);
 
	
	
}
