

var i=5;

function _open_detail(){
	if(isDetail=="1"){
		$show=$("button[name='"+_findTriggerButtonName()+"']");
		if($show!=null&&$show.length>0&&_isLoadTableData){
			$("#bulidTable").show();
			$show.trigger("click");
			loadEndByTime(50);
	    	setTimeout(function(){
	    		page_heigth=$(document.body).height();
	    		setParntHeigth(page_heigth);
	    	},500)
		}else{
			if(i>0){
				setTimeout("_open_detail()",500);
				i--;
			}
		}
	}
}

function _findTriggerButtonName(){
	return "";
}


function loadEnd(){
	if(isDetail=="0"){
		loadEndByTime(0);
	}
}