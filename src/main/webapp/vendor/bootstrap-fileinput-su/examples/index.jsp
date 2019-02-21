<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String bs_code = request.getParameter("pageCode");//此业务数据源
	String fieldCode = request.getParameter("fieldCode");
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>文件上传</title>
<!-- css -->
<link rel="stylesheet" href="../../../vendor/bootstrap/css/bootstrap.min.css">
<link href="../../../vendor/bootstrap-fileinput-su/css/fileinput.css" media="all" rel="stylesheet" type="text/css"/><!-- fileinput bootstrap文件上传插件 -->
<link href="../../../vendor/bootstrap-fileinput-su/css/font-awesome.css" media="all" rel="stylesheet" type="text/css"/><!-- 网页字体图标库 -->
<link href="../../../vendor/bootstrap-fileinput-su/themes/explorer-fa/theme.css" media="all" rel="stylesheet" type="text/css"/>
<!-- js -->
<script src="../../../vendor/jquery/jquery.min.js"></script>
<script src="../../../vendor/bootstrap-fileinput-su/js/plugins/sortable.js" type="text/javascript"></script><!-- 拖放插件  -->
<script src="../../../vendor/bootstrap-fileinput-su/js/fileinput.js" type="text/javascript"></script><!-- fileinput所需js  -->
<script src="../../../vendor/bootstrap-fileinput-su/js/locales/zh.js" type="text/javascript"></script><!-- 本地化中文包 -->
<script src="../../../vendor/bootstrap-fileinput-su/themes/explorer-fa/theme.js" type="text/javascript"></script><!-- 插件主题样式explorer-fa -->
<script src="../../../vendor/bootstrap-fileinput-su/themes/fa/theme.js" type="text/javascript"></script><!-- 插件主题样式fa -->
<!-- <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js" type="text/javascript"></script> --> <!--  轻量级提示工具 -->
<script src="../../../vendor/bootstrap/js/bootstrap.min.js"></script>
<style type="text/css" media="screen">
*{margin: 0px;padding: 0px;border: 0px}
body,div,dl,dt,dd,ul,ol,li,h1,h2,h3,h4,h5,h6,pre,code,form,fieldset,legend,input,textarea,p,blockquote,th,td,hr,button,article,aside,details,figcaption,figure,footer,header,hgroup,menu,nav,section {
    margin:0;
    padding:0;
}
html{width: 100%;
	height: 100%
}
</style>
</head>
<body>
	<br/>
<div class="container kv-main">
	<form enctype="multipart/form-data">
		<div class="form-group">
			<label><i class="fa fa-exclamation-circle" aria-hidden="true"></i>&nbsp;仅支持.xls、.xlsx格式 </label>
				<div class="file-loading">
					<!-- multiple 标识可以文件多选 -->
					<input id="myfile" class="file" name="files" type="file" multiple>
				</div>
				<button style="display: none" id="reset" class="btn btn-info" type="reset" hidden="hidden">Refresh Test</button>
		</div>
	</form>
</div>
</body>
<script>
var batchno="";
var paramExtra=JSON.parse('{"bs_code":"<%= bs_code %>","batchno":"'+ batchno +'"}');
//console.info(paramExtra)
    $('#myfile').fileinput({//测试5
		theme: 'fa',
		language: 'zh',
		validateInitialCount:true,
		maxFilesNum : 1,//上传最大的文件数量
		initialCaption: "请选择文件",//文本框初始话value
		//maxFileCount: 1, //表示允许同时上传的最大文件个数
		enctype: 'multipart/form-data',
		uploadUrl: '<%=path%>/import/uploadAffix',
		allowedFileExtensions: ['xls', 'xlsx'],//控制被预览的所有mime类型
		showPreview : true, //是否显示预览
		showCaption: true,//是否显示标题
		dropZoneEnabled: true,//是否显示拖拽区域
		uploadExtraData:{'paramExtra': '{"bs_code":"<%= bs_code %>","batchno":"222"}' },//上传文件时额外传递的参数设置'"'+ paramExtra +'"'
		dropZoneTitle:'可以拖拽文件到这里 &hellip;<br>还支持多选哦！',//拖拽框提示文字
		layoutTemplates :{
            //actionDelete:'', //去除上传预览的缩略图中的删除图标
            actionUpload:'',//去除上传预览缩略图中的上传图片；
            //actionZoom:''   //去除上传预览缩略图中的查看详情预览的缩略图标。
        },
    })
 	//异步上传失败返回结果处理
    $('.file').on('fileerror', function(event, data, msg) {
        console.log("fileerror:异步上传失败{}");
        });
    //异步上传成功返回结果处理
    $(".file").on("fileuploaded", function (event, data, previewId, index) {
    	var result = data.response; //后台返回的json
        console.log("fileuploaded:异步上传成功{}"+result.state);
        if (result.state) {
        	alert("处理成功");
        	if (paramExtra == "") {
        		batchno=result.body.batchno;
        		console.log("批次号改变{}"+batchno);
        		};
        };
        console.log("批次号{}"+batchno);
        });
    //上传中
    $('.file').on('filepreupload', function(event, data, previewId, index) {
    	 var form = data.form, files = data.files, extra = data.extra,
            response = data.response, reader = data.reader;
            console.log('文件正在上传');
    	console.log("filepreupload:上传前{}event{}"+event+"data{}"+data+"previewId{}"+previewId+"index{}"+index );
        });   
    //文件选择后
     $(".file").on('fileselect', function(event, n, l) {
     	//alert('File Selected. Name: ' + l + ', Num: ' + n);
     	//console.log('File Selected. Name: ' + l + ', Num: ' + n);
     });

 //$(document).ready(function () {});
 //清空file域
function clearFiles(){ 
	var file = $(".file").each(function(index) {
	  $(this).after($(this).clone().val(""));
	  $(this).remove();
	}); 
}
</script>
</html>