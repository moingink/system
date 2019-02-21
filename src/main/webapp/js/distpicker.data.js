/*!
 * Distpicker v1.0.4
 * https://github.com/fengyuanchen/distpicker
 *
 * Copyright (c) 2014-2016 Fengyuan Chen
 * Released under the MIT license
 *
 * Date: 2016-06-01T15:05:52.606Z
 */

(function (factory) {
  if (typeof define === 'function' && define.amd) {
    // AMD. Register as anonymous module.
    define('ChineseDistricts', [], factory);
  } else {
    // Browser globals.
    factory();
  }
})(function () {

  var ChineseDistricts = {
  };
	 $.ajax({  
              url : "/system/project/getBusinessType?proj_source_id="+ParentPKValue,  
              dataType : "json",
                          type : "GET", 
                          async: false, 
                          success : function(data) {
                          	//console.log(data);
                          	ChineseDistricts= data;
                          }
            });
  if (typeof window !== 'undefined') {
    window.ChineseDistricts = ChineseDistricts;
  }

  return ChineseDistricts;

});
