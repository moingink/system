/**
 * @author Sean
 */
buttonJson =[
              {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
              {name:'新增',fun:'tog(this),tog1(this)',buttonToken:'add'},
              {name:'修改',fun:'updateRow(this),updateRow1(this)',buttonToken:'update'},
              {name:'删除',fun:'delRows(this)',buttonToken:'delete'}
            ];


$(function(){
	setValidJson();
});

//设置验证
function setValidJson(){
	//标准验证
	validJson["fields"]["STANDARD"] = {
		"validators" : {
			notEmpty : {
				message : '标准 必填不能为空!'
			},
			stringLength : {
				min : 0,
				max : 50,
				message : '标准  长度超过50位'
			},
			remote: {
				type:'GET',
				url:'/system/business/isStandardRepeat',
				data:{
					id:function() {
						return ParentPKValue;
				  	},
				  	standard:function() {
						return $("#STANDARD").val();
				  	}
				},
				dataType:"json",
				dataFilter:function(data,type){
					return data;
				},
				message: '标准已存在',
			}
		}
	}
	//分值验证
	validJson["fields"]["DIVIDING_VALUE"] = {
		"validators" : {
			notEmpty : {
				message : '分值 必填不能为空!'
			},
			regexp : {
				regexp : /^(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)+$/,
				message : '分值 格式不合法，请输入数字类型且最多为2位小数!'
			}
		}
	}
}

var str = ParentPKParams.split(",");
function tog1(t){
	$('#GRADING_PROJECT').val(str[0]);
	$('#GRADING_PROJECT').attr("readonly","readonly");
	$('#DIVIDING_VALUE').attr("onchange","change(this.value)");
}

function updateRow1(){
	$('#DIVIDING_VALUE').attr("onchange","change(this.value)");
}

function change(value){
	var regPos = /^[1-9]\d*$/; //正整数
    if(regPos.test(value)){
        if(parseInt(str[1])<value){
        	//更新元素状态
        	$inspage.data('bootstrapValidator').updateStatus("DIVIDING_VALUE","INVALID",false);
        	alert("分值不能大于"+str[1]+"!");
        }
    }
}

function exam(t){
	
	var pageCode = 'test';//子表数据源
	var pageName = '测试';//列表显示名称
	
	var selected = JSON.parse(getSelections());
	if(selected.length != 1){
		oTable.showModal('modal', "请选择一条数据进行操作");
		return;
	}
	
	var url = context+"/pages/test.jsp?token="+token+"&pageCode="+pageCode+"&pageName="+pageName;
	
	window.location.href = url;
}
