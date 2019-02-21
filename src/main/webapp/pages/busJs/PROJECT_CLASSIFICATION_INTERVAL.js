/**
 * @author Sean
 */
buttonJson =[
              {name:'查询',fun:'queryTable(this)',buttonToken:'query'},
              {name:'新增',fun:'tog(this)',buttonToken:'add'},
              {name:'修改',fun:'updateRow(this)',buttonToken:'update'},
              {name:'删除',fun:'delRows(this)',buttonToken:'delete'}
            ];

$(function(){
	//总分下限
	validJson["fields"]["LOWER_LIMIT"] = toGreaterThan('UPPER_LIMIT','总分下限');
	//总分上限
	validJson["fields"]["UPPER_LIMIT"] = toGreaterThan('LOWER_LIMIT','总分上限');
	//项目类型
	validJson["fields"]["PROJECT_TYPE"]["validators"]["callback"] = {
		message : '此项目类型已经存在',
		callback : function(value, validator, $field) {
			return validProjectType(value);
		}
	}
});

function toGreaterThan(antisense,text) {
	var obj = {
		verbose : false,
		validators : {
			notEmpty : {
				message : text+' 必填不能为空!'
			},
			integer : {
				max: '39',
				message: text+' 该值只能包含数字！'
			},
			callback : {
				message : '总分下限不可大于总分上限',
				callback : function(value, validator, $field) {
					var antisenseVal = $('#'+antisense).val();
					if(antisenseVal == ''){
						return true;
					}
					var lowerLimit = $('#LOWER_LIMIT').val()!=""?$('#LOWER_LIMIT').val():0;//总分下限
					var upperLimit = $('#UPPER_LIMIT').val()!=""?$('#UPPER_LIMIT').val():0;//总分上限
				    if (Number(lowerLimit)<Number(upperLimit)) {
				        validator.updateStatus('UPPER_LIMIT', validator.STATUS_VALID, 'callback');
				        validator.updateStatus('LOWER_LIMIT', validator.STATUS_VALID, 'callback');
				        return true;
				    }
					return false;
				}
			}
		}
	}
	return obj
}

//验证项目类型是否重复
function validProjectType(value){
	var flag = true;
	$.ajax({
		type:'GET',
		url:'/system/business/validProjectType',
		data:{'dataSourceCode':dataSourceCode, 'projectType':value, 'id':$('#ID').val()},
		dataType:'text',
		async:false,
		success:function(data){
			if(data > 0){
				flag = false;
			}
		}
	});
	return flag;
}
