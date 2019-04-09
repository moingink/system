buttonJson = [
    {name: '查询', fun: 'queryTable(this)', buttonToken: 'query'},
    {name: '新增', fun: 'tog(this)', buttonToken: 'add'},
    {name: '修改', fun: 'updateRow(this)', buttonToken: 'update'},
    {name: '删除', fun: 'deleteRowCheck(this)', buttonToken: 'delete'},
    {name: '导入', fun: 'upload(this)', buttonToken: 'upload'},
    {name: '新增目录', fun: 'upload(this)', buttonToken: 'ConnectTest'}
];
//导入初始化 必须 否则页面功能有问题
$(function () {
    var fileInput = new FileInput();
    fileInput.init();
});

//重写参照
function ref_write_json(rejsonArray) {
    //参照 选择json 做单独的处理
    console.log(JSON.stringify(rejsonArray));
    return false; //return true 继续重写页面字段  false 不做页面回写
}

// 点击删除按钮做判断其他函数

function deleteRowCheck() {
    var selected = JSON.parse(getSelections());
    if (selected.length != 1) {
        oTable.showModal('modal', "请选择一条数据进行操作");
        return;
    }
    var billStatus = selected[0]['BILL_STATUS'];
    if (billStatus != 0 && billStatus != 7) {
        oTable.showModal('modal', "不允许删除已提交或审批完单据");
    } else {
        delRows(t);
    }

}

//双击事件  列表模版双击事件跳转其他操作
function dblClickFunction(row, tr) {
    var json = JSON.parse(JSON.stringify(row));
    console.log(json);
}

//业务跳转

function jump(t) {
    window.location.href = context + "/pages/childTableModify_persion.jsp?pageCode=MD_PERSONNEL&token=" + token;

}

function ref_end() {
    console.log("参选回调用");
}

//主字表模板

//导入
function upload() {
    console.log("导入");
    $('#uploadModal').modal('show');
}

//重写保存
function savaByQuery() {
    var message = "";
    var buttonToken = "DirectoryTest";
    var jsonData = getJson($('#insPage'));
    message = transToServer(findBusUrlByButtonTonken(buttonToken, '', _dataSourceCode), jsonData);
    oTable.showModal('modal', message);

    /*  var message ="";
      var buttonToken =$("#ins_or_up_buttontoken").val();
      message = transToServer(findBusUrlByButtonTonken(buttonToken,'',_dataSourceCode),getJson($div));
      oTable.showModal('modal', message);
  */
}

function transToServer(url, jsonData) {
    var message;
    $.ajax({
        async: false,
        type: "post",
        url: url,
        dataType: "json",
        //防止深度序列化
        traditional: true,
        data: {
            "jsonData": jsonData
        },
        success: function (data) {
            message = data['message'];
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            //登录超时
            if (XMLHttpRequest.getResponseHeader("TIMEOUTURL") != null) {
                window.top.location.href = XMLHttpRequest.getResponseHeader("TIMEOUTURL");
            }
            message = "请求失败";
        }
    });
    return message;
};
