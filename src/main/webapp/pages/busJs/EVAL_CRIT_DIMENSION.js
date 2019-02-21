/**
 * 供应商评价标准子表 添加维度
 * @author yansu
 */

function return_validation(){
	//alert("return_validation")
	window.history.back(-1);
}

function validation(){
	
}
buttonJson = [{
	name : '查询',
	fun : 'queryTable(this)',
	buttonToken : 'query'
}, {
	name : '新增',
	fun : 'tog(this)',
	buttonToken : 'add'
}, {
	name : '修改',
	fun : 'updateRow(this)',
	buttonToken : 'update'
}, {
	name : '删除',
	fun : 'delRows(this)',
	buttonToken : 'delete'
}]; 
var summax=0;
var summin=0;
$('#btntb').click(function() {
	var trSum=$('#table tbody tr').each(function(i) { // 遍历 tr
		var Scores_max;
		var Scores_min;
		var Weight;
		
		$(this).children('td').each(function(j) { // 遍历 tr 的各个 td
			//alert("第" + (i + 1) + "行，第" + (j + 1) + "个td的值：" + $(this).text());
			if (j==4) {
				//alert($(this).text())
				Scores_min=$(this).text()
			}else if(j==5)  {
				//alert($(this).text())
				Scores_max=$(this).text()
			}else if(j==6)  {
				//alert($(this).text())
				Weight=$(this).text()
			}
		});
		summax+=(Weight/100)*Scores_max;
		summin+=(Weight/100)*Scores_min;
	//	alert("第" + (i + 1) + "行{}"+"上限值 "+ summax+"下限值" +summin );
		//alert($(this).text())
	});
//	alert(trSum.length)
//	alert("上限值 "+ summax+"下限值" +summin );
	if (summax!=100) {
		oTable.showModal('modal', "合计上限值不符合规则！");
		summax=0;
		summin=0;
		return;
	}else if (summin<0) {
		oTable.showModal('modal', "合计下限值不符合规则！");
		summax=0;
		summin=0;
		return;
	}
	return_validation();
});

