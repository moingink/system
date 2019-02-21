/**
 *system/src/main/webapp/pages/childTableModifySUEVALPLANCTC.jsp 
 */


	
		function insto(json){
			//alert(json.length)
			
			 var testdiv = document.getElementById("tbodyy");
			 var selected="";
			
			//var count = Object.keys(json).length;
			
			for (var i=0 ; i < json.length; i++) {
				selected=selected+"<tr>";
				selected=selected+"<td>"+(i+1)+"</td>";
				selected=selected+"<td>"+ json[i]["BD_SUPPLIER_NAME"] +"</td>";
				selected=selected+"<td>"+ json[i]["SU_EVAL_PLAN_NAME"] +"</td>";
				selected=selected+"<td>"+ json[i]["EVAL_CRIT_DIMENSION_NAME"] +"</td>";
				selected=selected+"<td>"+ json[i]["GRADING_RANGE"] +"</td>";
				//console.info(json[i]["SCORE"]);
				var sco=json[i]["SCORE"];
				//alert(sco);
				if (json[i]["SCORE"]=="*") {
					selected=selected+"<td><input id=\""+json[i]["ID"]+"\" value=\"\" suid=\""+json[i]["SU_EVAL_PLAN_ID"]+"\" grad=\""+json[i]["GRADING_RANGE"]+"\" name= \"score"+(i+1)+"\" type=\"text\" class=\"form-control\" placeholder=\"请输入分数\"></td>";
				}else{
					selected=selected+"<td>"+ json[i]["SCORE"] +"</td>";
				};
				
				//selected=selected+"<td><input type=\"hidden\" class=\"form-control\"></td>";
				selected=selected+"</tr>";
				
			};
			testdiv.innerHTML=selected;
			//testdiv.innerHTML=selected;
  			//console.log(count);
			//alert(json.size())
		}
		
		function getinput(){
			var state=false;
			var json="[";
			var index=(document.getElementsByTagName("tr").	length - 1);
			for (var i = 0; i < index; i++) {
				var name=name='score'+(i+1);
				$("input[name='"+name+"']").each(function() {
					var grad=$("input[name='"+name+"']").attr("grad");//区间
					var id=$("input[name='"+name+"']").attr("id");//区间
					var suid=$("input[name='"+name+"']").attr("suid");//区间
					//alert(va1);
					var va=$("input[name='"+name+"']").val();//输入
					if(isnull(va)){
						var qu=grad.split("-");
						// alert(parseInt(va)>=parseInt(qu[0]));
						// alert(parseInt(va)<=parseInt(qu[1]));
						if (parseInt(va)>=parseInt(qu[0]) && parseInt(va)<=parseInt(qu[1])) {
							//验证完毕前往更改
							//alert("当前输入的值："+va+"&当前input的ID值："+id)
//String a="[{\"id\":\"1\",\"scort\":\"2\"},{\"id\":\"12\",\"scort\":\"23\"},{\"id\":\"123\",\"scort\":\"233\"}]";
							var jsonStr='{\"id\":\"'+id+'\",\"score\":\"'+va+'\",\"suid\":\"'+suid+'\"},';
							json+=(jsonStr);
							state=true;
						}else{
							oTable.showModal('modal', "填写值超过区间");
							state=false;
							return;
						};
					}else{
						oTable.showModal('modal', "请确认分数填写！");
						state=false;
						return;
					}
					//获取对象this
				});
			};

			// $("input[id^='id']").each(function() {
				// alert($("input[id^='id']").val())
				// //获取对象this
			// });
			// var SCORE= $("input:text[name='SCORE']").val();
			json = json.substr(0, json.length - 1);  
			json+=("]");
			//console.log(json);
			if (state) {
				updatainins(json);//操作
				//console.info("updatainins()"+json)
			};
			
	}
	
	function updatainins(json){
		//var jsonData='<%=ParentPKField%>=<%=ParentPKValue%> and <%=ParentPKField1%>=<%=ParentPKValue1%>';
		$.ajax({
		url : "/system/score/updatascore",
		dataType : "json",
		type : "POST",
		data:{"jsonData": json },
		success : function(data) {
		//oTable.showModal('modal', data);
		if (data.state) {
			oTable.showModal('modal', "操作生效");
			setTimeout('window.history.back(-1)','2000');
		} else{
			oTable.showModal('modal', "err:");
		};
			}
		});
		}
	
	function isnull(val) {
		var str = val.replace(/(^\s*)|(\s*$)/g, '');
		//去除空格;
		if (str == '' || str == undefined || str == null) {
			//return true;
			//console.log('空');
			return false;
		} else {
			//return false;
			//console.log('非空');
			return true;
		}
	}
	
	function gotoURL(){
	var url = "/pages/suppliers_Evaluation_SingleTableModify.jsp?pageCode=SU_EVAL_PLAN_CT_EVALUATION&pageName=供应商评价&userId="+userId+"&userName="+userName;
	//alert(url);
	url = context + url;
	//alert(url);
	window.location.href = url;
}
