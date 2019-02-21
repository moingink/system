		var oTable = new TableInit("");
		
		
		var checkurl = '/iuap-quickstart/boot.action?cmd=autoCheck';
		var handurl = "/iuap-quickstart/boot.action?cmd=handCheck"
		var handUnurl = "/iuap-quickstart/boot.action?cmd=handUnCheck"
			
		
		function handCheck(node_code,check_l, check_r,$button) {
				var mess = loadStart($button);
				$.ajax({
					async : false,
					type : "post",
					url : handurl,
					dataType : "text",
					data : {
						"nodeCode" : node_code,
						"check_l" : check_l,
						"check_r" : check_r
					},
					success : function(data) {
						loadEnd($button, mess);
						if (data == 1) {
							oTable.showModal('稽核结果', "成功！");
						} else {
							oTable.showModal('稽核结果', data);
						}
						queryTable();

					},
					error : function(XMLHttpRequest, textStatus, errorThrown) {
						loadEnd($button, mess);
						oTable.showModal('稽核结果', "稽核失败！");
					}
				});
			}

			function handUnCheck(node_code,check_l, check_r,$button) {
				
				var mess = loadStart($button);
				$.ajax({
					async : false,
					type : "post",
					url : handUnurl,
					dataType : "text",
					data : {
						"nodeCode" : node_code,
						"check_l" : check_l,
						"check_r" : check_r
					},
					success : function(data) {
						loadEnd($button, mess);
						if (data == 1) {
							oTable.showModal('撤销稽核', "成功！");
						} else {
							oTable.showModal('撤销稽核', data);
						}
						//queryTable();

					},
					error : function(XMLHttpRequest, textStatus, errorThrown) {
						loadEnd($button, mess);
						oTable.showModal('撤销稽核', "撤销稽核失败！");
					}
				});
			}


			function autoCheck(node_code,$button) {
				
				var mess = loadStart($button);
				$.ajax({
					async : false,
					type : "post",
					url : checkurl,
					dataType : "text",
					data : {
						"nodeCode" : node_code
					},
					success : function(data) {
						loadEnd($button, mess);
						if (data == 1) {
							oTable.showModal('稽核结果', "成功！");
							queryTable();
						} else {
							oTable.showModal('稽核结果', data);
						}

					},
					error : function(XMLHttpRequest, textStatus, errorThrown) {
						loadEnd($button, mess);
						oTable.showModal('稽核结果', "稽核失败！");
					}
				});
			};

			function loadStart($button) {
				$button.attr("disabled", true);
				var old_name = $button.html();
				$button.html("loading...");
				return old_name;
			}

			function loadEnd($button, name) {
				$button.attr("disabled", false);
				$button.html(name);
			}