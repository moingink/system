var FileInput = function () {
	var oFile = new Object();
	//初始化fileinput控件
	oFile.init = function() {
	    $('#fileUpload').fileinput({
			uploadUrl: context + '/base?cmd=uploadFile',
	    	language: 'zh',
			enctype: 'multipart/form-data',
			uploadExtraData: {
				dataSourceCode: dataSourceCode,
				token: token
			},
			showCancel:false,
			showClose:false,
			showUploadedThumbs:false,
			maxFileCount:0,
			autoReplace: true,
			allowedFileExtensions : ['xls','xlsx'],
			previewFileIcon: "<i class=\"glyphicon glyphicon-file\"></i>",
			minFileSize: 0,
			maxFileSize: 0,
			fileActionSettings:{
				showRemove:false, //是否在缩略图中显示删除按钮
		    	showUpload :false,//是否在缩略图中显示上传按钮
		    	showZoom:false//是否在缩略图中显示缩放按钮
			},
			msgPlaceholder: "请选择文件",
			dropZoneTitle:'将文件拖放到此处'//拖放区域中显示的标题
	    });
	    
	    //上传完成后触发此事件
		$('#fileUpload').on('fileuploaded', function(event, data, previewId, index) {
		    $('#uploadModalClose').css('display','block');
		   	var errorMessage = data.response.errorMessage;
		    if(errorMessage != ""){
		    	$('#errorMessage').css('display','block');
		    	$('#errorMessage').html(errorMessage);
		    }
		});
	
		//上传完成前触发此事件
		$('#fileUpload').on('filepreupload', function(event, data, previewId, index) {
    		$('#uploadModalClose').css('display','none');
		});

		//当按下文件输入删除按钮或预览窗口关闭图标以清除文件预览时，将触发此事件
		$('#fileUpload').on('fileclear', function() {
		     tipsInfoSetUp();
		});
	
		//在预览中加载文件后触发此事件
		$('#fileUpload').on('fileloaded', function(file, previewId, index, reader) {
		     tipsInfoSetUp();
		});
	};
	return oFile;
};

function tipsInfoSetUp(){
	$('#errorMessage').css('display','none');
	$('#errorMessage').html("");
};

$(function(){
	$(document.body).append(
		'<!--文件上传模态框-->'+
		'<div class="modal fade" id="uploadModal" tabindex="-1" style="overflow: auto" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static">'+
			'<div class="modal-dialog" id="uploadModalWidth" style="width: 50%;">'+
				'<div class="modal-content">'+
					'<div class="modal-header">'+
						'<button type="button" class="close" id="uploadModalClose" data-dismiss="modal" aria-hidden="true" style="display: block;">'+
							'×'+
						'</button>'+
						'<h4 class="modal-title" id="uploadModalLabel"></h4>'+
					'</div>'+
					'<div class="modal-body" id="uploadModalBody">'+
						/*
						'<div style="border: 1px solid #ddd; border-radius: 5px; padding: 8px; width: 100%; margin-bottom: 5px;">'+
													'<a href="#">下载导入模板</a>'+
												'</div>'+*/
						'<div id="errorMessage" style="display: none; border: 1px solid #ddd; border-radius: 5px; padding: 8px; width: 100%; margin-bottom: 5px;"></div>'+
						'<input id="fileUpload" type="file" name="file"/>'+
					'</div>'+
				'</div>'+
			'</div>'+
		'</div>'
	);
	
	$('#uploadModalLabel').html(pageName);
	
	$('#uploadModal').on('hide.bs.modal', function() {
		$(this).removeData('modal');
		$('#fileUpload').fileinput('clear');
		tipsInfoSetUp();
		queryTableByDataSourceCode("",dataSourceCode);
	});
});	