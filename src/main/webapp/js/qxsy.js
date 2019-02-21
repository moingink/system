//同步信息一户一案
function symessage(id){
	var o1=["CAR_NETWORKING_INFO","CUMA_COM_CAR_MESSAGE","CUMA_PRO_PLANS","CUMA_ING_PLAN","CUMA_DECIDSION_RERATION","CUMA_CUS_CAR_MODE","CUMA_MNO_MESSAGE","CUMA_SERVICE_CONTENT","CUMA_REAL_NAME_SYSTEM","CUMA_SHOPSERVICE","CUMA_MAPSERVICE","CUMA_MUSSERVICE","CUMA_WIFI"];
	var o2=["CUMA_CUS_CAR_MODE","CUMA_MNO_MESSAGE","CUMA_SERVICE_CONTENT","CUMA_REAL_NAME_SYSTEM","CUMA_SHOPSERVICE","CUMA_MAPSERVICE","CUMA_MUSSERVICE","CUMA_WIFI"];
	var json={
		  "name1": [
		    "CAR_NETWORKING_INFO",
		    "CUMA_COM_CAR_MESSAGE",
		    "CUMA_PRO_PLANS",
		    "CUMA_ING_PLAN",
		    "CUMA_DECIDSION_RERATION"
		  ],
		  "name2": [
		    "CUMA_CUS_CAR_MODE",
		    "CUMA_MNO_MESSAGE",
		    "CUMA_SERVICE_CONTENT",
		    "CUMA_REAL_NAME_SYSTEM",
		    "CUMA_SHOPSERVICE",
		    "CUMA_MAPSERVICE",
		    "CUMA_MUSSERVICE",
		    "CUMA_WIFI"
		  ]
		};
	
		$.ajax({
			type: "POST",
			url: "/system/cuma?cmd=qxsy&map1="+o1+"&id="+id,
			dataType: "json",
			success: function(data) {			
			
			}, 
			error: function(data) {
		      var da=JSON.stringify(data);
		    
			}
		});
	
	
}
