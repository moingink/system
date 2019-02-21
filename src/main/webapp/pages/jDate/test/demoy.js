/**
 * jeDate 演示
 */
    // var enLang = {                            
        // name  : "en",
        // month : ["01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"],
        // weeks : [ "SUN","MON","TUR","WED","THU","FRI","SAT" ],
        // times : ["Hour","Minute","Second"],
        // timetxt: ["Time","Start Time","End Time"],
        // backtxt:"Back",
        // clear : "Clear",
        // today : "Now",
        // yes   : "Confirm",
        // close : "Close"
    // }
    // //常规选择
    // jeDate("#year_",{
        // //festival:true,
        // //multiPane:false,
        // isClear:false,                               //是否显示清空按钮
        // isToday:false,                               //是否显示今天或本月按钮
        // isYes:false, 
        // onClose:false,
        // format: "YYYY年",
        // donefun: function(obj){
			// //$("#test01").val(jeDate.getLunar(obj.date[0]).cY);
			// //aa(jeDate.getLunar(obj.date[0]).cY);
			// //initDate:true;
			// $("#year_").val(jeDate.getLunar(obj.date[0]).cY);
		// }
//         
    // });
$(function(){
	var ys=5;
	var time=new Date();
	var nowtime=time.getFullYear();
	var year=nowtime-ys;
	var y=$("#year_");
	var nowi="";
	for (var i=0;i<(ys*2+1);i++){
		if((year+i)==nowtime){
		  y.append("<option selected value='"+(year+i)+"'>"+(year+i)+"</option>");
		}
		else{
	    y.append("<option value='"+(year+i)+"'>"+(year+i)+"</option>");
	}
	}
	//$("#year_").val(nowtime);	
});    
    
    
    
    
    
    
    
    
    

    
    