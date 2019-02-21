buttonJson =[
             {name:'查询',fun:'queryTable(this)',buttonToken:'query'}, 
			  {name:'评分',fun:'pifen(this)',buttonToken:'pifen'},
			];
						
	function pifen(){
		
		var selected = JSON.parse(getSelections());
	    if(selected.length != 1){
		oTable.showModal('modal', "请至少选择一条数据进行评级!");
		return;
		}
		
		
		var json = JSON.parse(getSelections())[0];
        $.ajax({
			type: "POST",
			url: "/system/cuma?cmd=find_scoure_message&id="+json["CUMA_ACCOUNT_ID"],
			dataType: "json",
			async: false,
			cache: false,
			contentType: false,
			processData: false,
			success: function(data) {
			 
			if(data["sum_price"]==null){data["sum_price"]="0.0";}
		    var json_message=data["acc"][0];
		  
		    //alert(json_message["BILL_STATUS"]);
		    var js_=JSON.stringify(json);
		    var url_message=encodeURI(js_);
		    var json_all_message=json_message["BILL_STATUS_SCORE"];
		    if(json_all_message==null){json_all_message=0;}
		    
		    //alert(data["sum_price"]);
		    
		    //return ;
		    
		    if(json_all_message=="1" || json_all_message=="3" || json_all_message=="2" || json_all_message=="4" || json_all_message=="7"){
		     	window.location.href=context+"/pages/score_up_qt.jsp?pifen="+url_message+"&sum="+data["sum_price"]+"&json="+json_all_message+"&token="+token+"&ParentPKValue="+json["CUMA_ACCOUNT_ID"];
		     }
		    else{

		    window.location.href=context+"/pages/score.jsp?pifen="+url_message+"&sum="+data["sum_price"]+"&json="+json_all_message+"&token="+token;
			}
			}, 
			error: function(data) {
		      var da=JSON.stringify(data);
		      alert("查询失败，请联系管理员！" + "\t" + "错误代码：" + da);
			}
			});
	
	
	}	
	

    



