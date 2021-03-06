/**
 * jeDate 演示
 */
    var enLang = {                            
        name  : "en",
        month : ["01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"],
        weeks : [ "SUN","MON","TUR","WED","THU","FRI","SAT" ],
        times : ["Hour","Minute","Second"],
        timetxt: ["Time","Start Time","End Time"],
        backtxt:"Back",
        clear : "Clear",
        today : "Now",
        yes   : "Confirm",
        close : "Close"
    }
    //常规选择
   

    jeDate("#j_1",{
        format: "YYYY-MM"
    });
    
  
    

    //一次绑定多个选择
    var jel = document.querySelectorAll(".moredate");
    for(var j=0;j<jel.length;j++){
        var mat = jel[j].getAttribute("placeholder");
        jeDate(jel[j],{
            format: mat
        });
    }

    //一次绑定多个选择DIV类型
    var divel = document.querySelectorAll(".divmore");
    for(var j=0;j<divel.length;j++){
        var divmat = divel[j].getAttribute("placeholder");
        jeDate(divel[j],{
            format: divmat
        });
    }

    //左边多类选择
    jeDate("#short",{
        format:"YYYY-MM-DD",
        shortcut:[
            {name:"一周",val:{DD:7}},
            {name:"一个月",val:{DD:30}},
            {name:"二个月",val:{MM:2}},
            {name:"三个月",val:{MM:3}},
            {name:"一年",val:{DD:365}}
        ],
        donefun:function (obj) {
            //alert(jeDate.getLunar(obj.date[0]).cW);
        }
    });
    jeDate("#shortboth",{
        format:"YYYY-MM-DD",
        isinitVal: true,
        range:" TO ",
        multiPane:false,
        shortcut:[
            {name:"一周",val:{DD:7}},
            {name:"一个月",val:{DD:30}},
            {name:"二个月",val:{MM:2}},
            {name:"三个月",val:{MM:3}},
            {name:"一年",val:{DD:365}}
        ],
        donefun:function (obj) {
            //var bs = {yy:123,dd:789}
            console.log(jeDate.extend({yy:123,dd:789},{yy:"you",aa:456}))
            //alert(jeDate.getLunar(obj.date[0]).cW);
        }
    });

    //YYYYMMDD格式
    jeDate("#dateymd",{
        format: "YYYYMMDD"
    });
    jeDate("#dateymdboth",{
        format: "YYYYMMDD",
        multiPane:false,
        range:" 至 "
    });
    
    
   

    