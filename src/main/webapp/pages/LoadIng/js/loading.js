 
 $(function(){
 	$(document).ready(function(){
      $("body").append("<div id='load_message' style='border:none'><iframe style='width:100%;height:100%;' src='/system/pages/loading.html' frameborder=0 scrolling=no allowtransparency></iframe></div>");	  
 	     
 	  
 	  $('#load_message').window({    
 	         title:"",
 	         width:200,
 	         height:100,
 	         modal:true,
 	         closed:true,
 	         shadow:true,
 	         inline:false,
 	         draggable:false,
 	         resizable:false,
 	         minimizable:false,	      
 	         maximizable:false 
 	         
 	       });
 	   //$('#load_message').window("resize",{top:$(document).scrollTop() + ($(window).height()-250) * 0.5});    
 	   //
 	});
 	
 	
 });
 
 function jz(){
    
    
    $("#load_message").window("open"); 
    
    $("#load_message").parent().css("background","#FFFFFF");
    $('#load_message').window("center","none");
    $("#load_message").parent().css("position","fixed");
    $("#load_message").parent().css("top","100px");
    //$("#load_message").parent().css("left","450px");			
    //$("#load_message").parent().css("top","190px");
    
    $("#load_message").parent().next().css("display","none");//css("display","none");
    

    loadmessage1();
 } 
 
 function loadmessage1(){	
 	setTimeout(loadmessage2,6000);
 }  
 
 function loadmessage2(){
   $("#load_message").window("close"); 
 } 