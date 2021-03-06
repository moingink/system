<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
  <meta http-equiv="X-UA-Compatible" content="IE=9; IE=8; IE=7; IE=EDGE"/>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <title>Teamwork</title>
  
   <%
   String p_ids=request.getParameter("p_ids");
   %>
  
  <link rel=stylesheet href="../ganttmessage/platform.css" type="text/css">
  <link rel=stylesheet href="../ganttmessage/libs/jquery/dateField/jquery.dateField.css" type="text/css">

  <link rel=stylesheet href="../ganttmessage/gantt.css" type="text/css">
  <link rel=stylesheet href="../ganttmessage/ganttPrint.css" type="text/css" media="print">

  <script src="../ganttmessage/libs/jquery.min.js"></script>
  <script src="../ganttmessage/libs/jquery-ui.min.js"></script>

  <script src="../ganttmessage/libs/jquery/jquery.livequery.1.1.1.min.js"></script>
  <script src="../ganttmessage/libs/jquery/jquery.timers.js"></script>

  <script src="../ganttmessage/libs/utilities.js"></script>
  <script src="../ganttmessage/libs/forms.js"></script>
  <script src="../ganttmessage/libs/date.js"></script>
  <script src="../ganttmessage/libs/dialogs.js"></script>
  <script src="../ganttmessage/libs/layout.js"></script>
  <script src="../ganttmessage/libs/i18nJs.js"></script>
  <script src="../ganttmessage/libs/jquery/dateField/jquery.dateField.js"></script>
  <script src="../ganttmessage/libs/jquery/JST/jquery.JST.js"></script>

  <script type="text/javascript" src="../ganttmessage/libs/jquery/svg/jquery.svg.min.js"></script>
  <script type="text/javascript" src="../ganttmessage/libs/jquery/svg/jquery.svgdom.1.8.js"></script>


  <script src="../ganttmessage/ganttUtilities.js"></script>
   <!-- 添加字段js  -->
  <script src="../ganttmessage/ganttTask.js"></script>
  
   <!-- 里程碑图片路径修改  -->
  <script src="../ganttmessage/ganttDrawerSVG.js"></script>
  <script src="../ganttmessage/ganttZoom.js"></script>
  
   <!-- 任务添加时字段加入  -->
  <script src="../ganttmessage/ganttGridEditor.js"></script>
   <!-- 任务添加时字段加入  -->
  <script src="../ganttmessage/ganttMaster.js"></script>  


  <!--<script src="libs/profiling.js"></script>-->
  <!--<script type="text/javascript" src="ganttTestSuite.js"></script>-->
  
<script type="text/javascript">
 $(document).ready(function(){
  p_message="1";
  proj_source_id="<%=p_ids %>";
  p_proj_source_id="<%=p_ids %>";
  //alert("1:"+p_message);
});
</script>
  
  
</head>
<body style="background-color: #fff;">


<!-- <div id="ndo" style="position:absolute;right:5px;top:5px;width:378px;padding:5px;background-color: #FFF5E6; border:1px solid #F9A22F; font-size:12px" class="noprint">
  This Gantt editor is free thanks to <a href="http://twproject.com" target="_blank">Twproject</a> where it can be used on a complete and flexible project management solution.<br> Get your projects done! Give <a href="http://twproject.com" target="_blank">Twproject a try now</a>.
</div> -->
<div id="workSpace" style="padding:0px; overflow-y:auto; overflow-x:hidden;border:1px solid #e5e5e5;position:relative;margin:0 5px"></div>

<style>
  .resEdit {
    padding: 15px;
  }

  .resLine {
    width: 95%;
    padding: 3px;
    margin: 5px;
    border: 1px solid #d0d0d0;
  }

  body {
    overflow: hidden;
  }

  .ganttButtonBar h1{
    color: #000000;
    font-weight: bold;
    font-size: 28px;
    margin-left: 10px;
  }

</style>

<form id="gimmeBack" style="display:none;" action="../gimmeBack.jsp" method="post" target="_blank"><input type="hidden" name="prj" id="gimBaPrj"></form>

<script type="text/javascript">

var ge;
$(function() {
  var canWrite=true; //this is the default for test purposes

  // here starts gantt initialization
  ge = new GanttMaster();
  ge.set100OnClose=true;

  ge.shrinkParent=true;

  ge.init($("#workSpace"));
  loadI18n(); //overwrite with localized ones

  //in order to force compute the best-fitting zoom level
  delete ge.gantt.zoom;

  var project=loadFromLocalStorage();

  if (!project.canWrite)
    $(".ganttButtonBar button.requireWrite").attr("disabled","true");

  ge.loadProject(project);
  ge.checkpoint(); //empty the undo stack
});



function getDemoProject(){
  //console.debug("getDemoProject")
  //查询
  var tasks="";
  
  $.ajax({
			type: "POST",
			url:"/system/project?cmd=find_wbs_message&id=<%=p_ids %>",		
			dataType: "json",
			async: false,
			cache: false,
			contentType: false,
			processData: false,
			success: function(data) {
			 tasks=data["tasks"];
			 
			 
			 alert(JSON.stringify(tasks));
			}, error: function(data) {
				var da=JSON.stringify(data);
				alert("查询失败，请联系管理员！" + "\t" + "错误代码：" + da);
			}
			}); 
  
  //alert(JSON.stringify(tasks));
  ret= {tasks, "selectedRow": 1, "deletedTaskIds": [],
      "resources": [
      {"id": "tmp_1", "name": "Resource 1"},
      {"id": "tmp_2", "name": "Resource 2"},
      {"id": "tmp_3", "name": "Resource 3"},
      {"id": "tmp_4", "name": "Resource 4"}
    ],
      "roles":       [
      {"id": "tmp_1", "name": "Project Manager"},
      {"id": "tmp_2", "name": "Worker"},
      {"id": "tmp_3", "name": "Stakeholder"},
      {"id": "tmp_4", "name": "Customer"}
    ], "canWrite": true, "canDelete":true, "canWriteOnParent": false, canAdd:true}
  //"canWrite": true,
  
  
// ret= {"tasks":    [
      // {"id": -1, "name": "Gantt editor", "progress": 0, "progressByWorklog": false, "relevance": 0, "type": "", "typeId": "", "description": "", "code": "", "level": 0, "status": "STATUS_ACTIVE", "depends": "", "canWrite": true, "start": 1396994400000, "duration": 20, "end": 1399586399999, "startIsMilestone": false, "endIsMilestone": false, "collapsed": false, "assigs": [], "hasChild": true},
      // {"id": -2, "name": "coding", "progress": 0, "progressByWorklog": false, "relevance": 0, "type": "", "typeId": "", "description": "", "code": "", "level": 1, "status": "STATUS_ACTIVE", "depends": "", "canWrite": true, "start": 1396994400000, "duration": 10, "end": 1398203999999, "startIsMilestone": false, "endIsMilestone": false, "collapsed": false, "assigs": [], "hasChild": true},
      // {"id": -3, "name": "gantt part", "progress": 0, "progressByWorklog": false, "relevance": 0, "type": "", "typeId": "", "description": "", "code": "", "level": 2, "status": "STATUS_ACTIVE", "depends": "", "canWrite": true, "start": 1396994400000, "duration": 2, "end": 1397167199999, "startIsMilestone": false, "endIsMilestone": false, "collapsed": false, "assigs": [], "hasChild": false},
      // {"id": -4, "name": "editor part", "progress": 0, "progressByWorklog": false, "relevance": 0, "type": "", "typeId": "", "description": "", "code": "", "level": 2, "status": "STATUS_SUSPENDED", "depends": "3", "canWrite": true, "start": 1397167200000, "duration": 4, "end": 1397685599999, "startIsMilestone": false, "endIsMilestone": false, "collapsed": false, "assigs": [], "hasChild": false},
      // {"id": -5, "name": "testing", "progress": 0, "progressByWorklog": false, "relevance": 0, "type": "", "typeId": "", "description": "", "code": "", "level": 1, "status": "STATUS_SUSPENDED", "depends": "2:5", "canWrite": true, "start": 1398981600000, "duration": 5, "end": 1399586399999, "startIsMilestone": false, "endIsMilestone": false, "collapsed": false, "assigs": [], "hasChild": true},
      // {"id": -6, "name": "test on safari", "progress": 0, "progressByWorklog": false, "relevance": 0, "type": "", "typeId": "", "description": "", "code": "", "level": 2, "status": "STATUS_SUSPENDED", "depends": "", "canWrite": true, "start": 1398981600000, "duration": 2, "end": 1399327199999, "startIsMilestone": false, "endIsMilestone": false, "collapsed": false, "assigs": [], "hasChild": false},
      // {"id": -7, "name": "test on ie", "progress": 0, "progressByWorklog": false, "relevance": 0, "type": "", "typeId": "", "description": "", "code": "", "level": 2, "status": "STATUS_SUSPENDED", "depends": "6", "canWrite": true, "start": 1399327200000, "duration": 3, "end": 1399586399999, "startIsMilestone": false, "endIsMilestone": false, "collapsed": false, "assigs": [], "hasChild": false},
      // {"id": -8, "name": "test on chrome", "progress": 0, "progressByWorklog": false, "relevance": 0, "type": "", "typeId": "", "description": "", "code": "", "level": 2, "status": "STATUS_SUSPENDED", "depends": "6", "canWrite": true, "start": 1399327200000, "duration": 2, "end": 1399499999999, "startIsMilestone": false, "endIsMilestone": false, "collapsed": false, "assigs": [], "hasChild": false}
    // ], "selectedRow": 2, "deletedTaskIds": [],
      // "resources": [
      // {"id": "tmp_1", "name": "Resource 1"},
      // {"id": "tmp_2", "name": "Resource 2"},
      // {"id": "tmp_3", "name": "Resource 3"},
      // {"id": "tmp_4", "name": "Resource 4"}
    // ],
      // "roles":       [
      // {"id": "tmp_1", "name": "Project Manager"},
      // {"id": "tmp_2", "name": "Worker"},
      // {"id": "tmp_3", "name": "Stakeholder"},
      // {"id": "tmp_4", "name": "Customer"}
    // ], "canWrite":    true, "canDelete":true, "canWriteOnParent": true, canAdd:true}


    //actualize data
    var offset=new Date().getTime()-ret.tasks[0].start;
    for (var i=0;i<ret.tasks.length;i++) {
      ret.tasks[i].start = ret.tasks[i].start + offset;
    }
  return ret;
}



function loadGanttFromServer(taskId, callback) {

  //this is a simulation: load data from the local storage if you have already played with the demo or a textarea with starting demo data
  var ret=loadFromLocalStorage();

  //this is the real implementation
  /*
  //var taskId = $("#taskSelector").val();
  var prof = new Profiler("loadServerSide");
  prof.reset();

  $.getJSON("ganttAjaxController.jsp", {CM:"LOADPROJECT",taskId:taskId}, function(response) {
    //console.debug(response);
    if (response.ok) {
      prof.stop();

      ge.loadProject(response.project);
      ge.checkpoint(); //empty the undo stack

      if (typeof(callback)=="function") {
        callback(response);
      }
    } else {
      jsonErrorHandling(response);
    }
  });
  */

  return ret;
}


function saveGanttOnServer() {

  //this is a simulation: save data to the local storage or to the textarea
  saveInLocalStorage();

  /*
  var prj = ge.saveProject();

  delete prj.resources;
  delete prj.roles;

  var prof = new Profiler("saveServerSide");
  prof.reset();

  if (ge.deletedTaskIds.length>0) {
    if (!confirm("TASK_THAT_WILL_BE_REMOVED\n"+ge.deletedTaskIds.length)) {
      return;
    }
  }

  $.ajax("ganttAjaxController.jsp", {
    dataType:"json",
    data: {CM:"SVPROJECT",prj:JSON.stringify(prj)},
    type:"POST",

    success: function(response) {
      if (response.ok) {
        prof.stop();
        if (response.project) {
          ge.loadProject(response.project); //must reload as "tmp_" ids are now the good ones
        } else {
          ge.reset();
        }
      } else {
        var errMsg="Errors saving project\n";
        if (response.message) {
          errMsg=errMsg+response.message+"\n";
        }

        if (response.errorMessages.length) {
          errMsg += response.errorMessages.join("\n");
        }

        alert(errMsg);
      }
    }

  });
  */
}

function newProject(){
  clearGantt();
}


function clearGantt() {
  ge.reset();
}

//-------------------------------------------  Get project file as JSON (used for migrate project from gantt to Teamwork) ------------------------------------------------------
function getFile() {
  $("#gimBaPrj").val(JSON.stringify(ge.saveProject()));
  $("#gimmeBack").submit();
  $("#gimBaPrj").val("");

  /*  var uriContent = "data:text/html;charset=utf-8," + encodeURIComponent(JSON.stringify(prj));
   neww=window.open(uriContent,"dl");*/
}


function loadFromLocalStorage() {
  var ret;
  if (localStorage) {
    if (localStorage.getObject("teamworkGantDemo")) {
      ret = localStorage.getObject("teamworkGantDemo");
    }
  }

  //if not found create a new example task
  if (!ret || !ret.tasks || ret.tasks.length == 0){
    ret=getDemoProject();
  }
  return ret;
}

//allsave全局保存
function saveInLocalStorage() {
  var prj = ge.saveProject();
  //alert(JSON.stringify(prj["tasks"]));
  for(var i=0;i<prj["tasks"].length;i++){
  
     delete prj["tasks"][i]["progressByWorklog"];
     delete prj["tasks"][i]["LEVEL"];
     delete prj["tasks"][i]["depends"];
     delete prj["tasks"][i]["startIsMilestone"];
     delete prj["tasks"][i]["endIsMilestone"];
     delete prj["tasks"][i]["canAdd"];
     delete prj["tasks"][i]["canDelete"];
     delete prj["tasks"][i]["canAddIssue"];
     delete prj["tasks"][i]["relevance"];
     delete prj["tasks"][i]["type"];
     delete prj["tasks"][i]["typeId"];
     delete prj["tasks"][i]["hasChild"];
     delete prj["tasks"][i]["RMRN"];
     delete prj["tasks"][i]["assigs"];
     delete prj["tasks"][i]["status"];
     delete prj["tasks"][i]["collapsed"];
     delete prj["tasks"][i]["canWrite"];
     //开始时间  WBS_START_DATE    计划完成日期   WBS_EXPECTED_END_DATE   实际完成日期    WBS_COMPLETION_TIME
     
     prj["tasks"][i]["WBS_EXPECTED_END_DATE"]=prj["tasks"][i]["end"];
     prj["tasks"][i]["WBS_START_DATE"]=prj["tasks"][i]["start"];
     prj["tasks"][i]["WBS_COMPLETION_TIME"]=prj["tasks"][i]["appln_time"];
     
     prj["tasks"][i]["WBS_NAME"]=prj["tasks"][i]["code"];                   //任务名字
     prj["tasks"][i]["TASK_REMARK"]=prj["tasks"][i]["name"];                //任务描述
     prj["tasks"][i]["WBS_THOSE_RESPONSIBLE"]=prj["tasks"][i]["main_name"]; //责任人
     prj["tasks"][i]["WBS_BILL_STATE"]=prj["tasks"][i]["wbs_status"];       //状态
     prj["tasks"][i]["WBS_WORKING_HOURS"]=prj["tasks"][i]["duration"];      //工时
     
     //主键id 父级id 建议书id 
     //prj["tasks"][i]["description"]; 
     //prj["tasks"][i]["id"]=prj["tasks"][i]["wbs_ids"];
     delete prj["tasks"][i]["id"];
     delete prj["tasks"][i]["wbs_ids"];
     
     //prj["tasks"][i]["PROJ_PHASE_ID"]=prj["tasks"][i]["proj_phase_id"];
     
     //delete prj["tasks"][i]["proj_phase_id"];
     
     prj["tasks"][i]["PROJ_SOURCE_ID"]="<%=p_ids%>";
    
     delete prj["tasks"][i]["proj_id"];
     
     delete prj["tasks"][i]["end"];
     delete prj["tasks"][i]["start"];
     delete prj["tasks"][i]["appln_time"];
     
     delete prj["tasks"][i]["code"];
     delete prj["tasks"][i]["name"];
     delete prj["tasks"][i]["main_name"];
     delete prj["tasks"][i]["wbs_status"];
     delete prj["tasks"][i]["duration"];
     delete prj["tasks"][i]["PROJ_TYPE"];
     delete prj["tasks"][i]["progress"];
     delete prj["tasks"][i]["description"];
     
  }
  

  alert(JSON.stringify(prj["tasks"]));
  if (localStorage) {
    localStorage.setObject("teamworkGantDemo", prj);
  }  
  $.ajax({
			type: "POST",
			url:"/system/project?cmd=add_all_message&id=<%=p_ids%>",		
			dataType: "json",
			data:{message:JSON.stringify(prj)},
			success: function(data) {
			   alert("保存成功");
			   
			   //location.reload(true);   
			}, error: function(data) {
				var da=JSON.stringify(data);
				
			}
			}); 
  
  
}


//-------------------------------------------  Open a black popup for managing resources. This is only an axample of implementation (usually resources come from server) ------------------------------------------------------
function editResources(){

  //make resource editor
  var resourceEditor = $.JST.createFromTemplate({}, "RESOURCE_EDITOR");
  var resTbl=resourceEditor.find("#resourcesTable");

  for (var i=0;i<ge.resources.length;i++){
    var res=ge.resources[i];
    resTbl.append($.JST.createFromTemplate(res, "RESOURCE_ROW"))
  }


  //bind add resource
  resourceEditor.find("#addResource").click(function(){
    resTbl.append($.JST.createFromTemplate({id:"new",name:"resource"}, "RESOURCE_ROW"))
  });

  //bind save event
  resourceEditor.find("#resSaveButton").click(function(){
    var newRes=[];
    //find for deleted res
    for (var i=0;i<ge.resources.length;i++){
      var res=ge.resources[i];
      var row = resourceEditor.find("[resId="+res.id+"]");
      if (row.length>0){
        //if still there save it
        var name = row.find("input[name]").val();
        if (name && name!="")
          res.name=name;
        newRes.push(res);
      } else {
        //remove assignments
        for (var j=0;j<ge.tasks.length;j++){
          var task=ge.tasks[j];
          var newAss=[];
          for (var k=0;k<task.assigs.length;k++){
            var ass=task.assigs[k];
            if (ass.resourceId!=res.id)
              newAss.push(ass);
          }
          task.assigs=newAss;
        }
      }
    }

    //loop on new rows
    var cnt=0
    resourceEditor.find("[resId=new]").each(function(){
      cnt++;
      var row = $(this);
      var name = row.find("input[name]").val();
      if (name && name!="")
        newRes.push (new Resource("tmp_"+new Date().getTime()+"_"+cnt,name));
    });

    ge.resources=newRes;

    closeBlackPopup();
    ge.redraw();
  });


  var ndo = createModalPopup(400, 500).append(resourceEditor);
}

function initializeHistoryManagement(){

  //si chiede al server se c'è della hisory per la root
  $.getJSON(contextPath+"/applications/teamwork/task/taskAjaxController.jsp", {CM: "GETGANTTHISTPOINTS", OBJID:10236}, function (response) {

    //se c'è
    if (response.ok == true && response.historyPoints && response.historyPoints.length>0) {

      //si crea il bottone sulla bottoniera
      var histBtn = $("<button>").addClass("button textual icon lreq30 lreqLabel").attr("title", "SHOW_HISTORY").append("<span class=\"teamworkIcon\">&#x60;</span>");

      //al click
      histBtn .click(function () {
        var el = $(this);
        var ganttButtons = $(".ganttButtonBar .buttons");

        //è gi�  in modalit�  history?
        if (!ge.element.is(".historyOn")) {
          ge.element.addClass("historyOn");
          ganttButtons.find(".requireCanWrite").hide();

          //si carica la history server side
          if (false) return;
          showSavingMessage();
          $.getJSON(contextPath + "/applications/teamwork/task/taskAjaxController.jsp", {CM: "GETGANTTHISTPOINTS", OBJID: ge.tasks[0].id}, function (response) {
            jsonResponseHandling(response);
            hideSavingMessage();
            if (response.ok == true) {
              var dh = response.historyPoints;
              //ge.historyPoints=response.historyPoints;
              if (dh && dh.length > 0) {
                //si crea il div per lo slider
                var sliderDiv = $("<div>").prop("id", "slider").addClass("lreq30 lreqHide").css({"display":"inline-block","width":"500px"});
                ganttButtons.append(sliderDiv);

                var minVal = 0;
                var maxVal = dh.length-1 ;

                $("#slider").show().mbSlider({
                  rangeColor : '#2f97c6',
                  minVal     : minVal,
                  maxVal     : maxVal,
                  startAt    : maxVal,
                  showVal    : false,
                  grid       :1,
                  formatValue: function (val) {
                    return new Date(dh[val]).format();
                  },
                  onSlideLoad: function (obj) {
                    this.onStop(obj);

                  },
                  onStart    : function (obj) {},
                  onStop     : function (obj) {
                    var val = $(obj).mbgetVal();
                    showSavingMessage();
                    $.getJSON(contextPath + "/applications/teamwork/task/taskAjaxController.jsp", {CM: "GETGANTTHISTORYAT", OBJID: ge.tasks[0].id, millis:dh[val]}, function (response) {
                      jsonResponseHandling(response);
                      hideSavingMessage();
                      if (response.ok ) {
                        ge.baselines=response.baselines;
                        ge.showBaselines=true;
                        ge.baselineMillis=dh[val];
                        ge.redraw();
                      }
                    })

                  },
                  onSlide    : function (obj) {
                    clearTimeout(obj.renderHistory);
                    var self = this;
                    obj.renderHistory = setTimeout(function(){
                      self.onStop(obj);
                    }, 200)

                  }
                });
              }
            }
          });


          // quando si spenge
        } else {
          //si cancella lo slider
          $("#slider").remove();
          ge.element.removeClass("historyOn");
          if (ge.permissions.canWrite)
            ganttButtons.find(".requireCanWrite").show();

          ge.showBaselines=false;
          ge.baselineMillis=undefined;
          ge.redraw();
        }

      });
      $("#saveGanttButton").before(histBtn);
    }
  })
}

function showBaselineInfo (event,element){
  //alert(element.attr("data-label"));
  $(element).showBalloon(event, $(element).attr("data-label"));
  ge.splitter.secondBox.one("scroll",function(){
    $(element).hideBalloon();
  })
}

</script>





<div id="gantEditorTemplates" style="display:none;">
<div class="__template__" type="GANTBUTTONS"><!--
  <div class="ganttButtonBar noprint">
    <div class="buttons">
      

      <button onclick="$('#workSpace').trigger('undo.gantt');return false;" class="button textual icon requireCanWrite" title="撤销"><span class="teamworkIcon">&#39;</span></button>
      <button onclick="$('#workSpace').trigger('redo.gantt');return false;" class="button textual icon requireCanWrite" title="重做"><span class="teamworkIcon">&middot;</span></button>
      <span class="ganttButtonSeparator requireCanWrite requireCanAdd"></span>
      <button onclick="$('#workSpace').trigger('addAboveCurrentTask.gantt');return false;" class="button textual icon requireCanWrite requireCanAdd" title="上方插入行"><span class="teamworkIcon">l</span></button>
      <button onclick="$('#workSpace').trigger('addBelowCurrentTask.gantt');return false;" class="button textual icon requireCanWrite requireCanAdd" title="下方插入行"><span class="teamworkIcon">X</span></button>
      <span class="ganttButtonSeparator requireCanWrite requireCanInOutdent"></span>
      <button onclick="$('#workSpace').trigger('outdentCurrentTask.gantt');return false;" class="button textual icon requireCanWrite requireCanInOutdent" title="升级"><span class="teamworkIcon">.</span></button>
      <button onclick="$('#workSpace').trigger('indentCurrentTask.gantt');return false;" class="button textual icon requireCanWrite requireCanInOutdent" title="降级"><span class="teamworkIcon">:</span></button>
      <span class="ganttButtonSeparator requireCanWrite requireCanMoveUpDown"></span>
      <button onclick="$('#workSpace').trigger('moveUpCurrentTask.gantt');return false;" class="button textual icon requireCanWrite requireCanMoveUpDown" title="上移"><span class="teamworkIcon">k</span></button>
      <button onclick="$('#workSpace').trigger('moveDownCurrentTask.gantt');return false;" class="button textual icon requireCanWrite requireCanMoveUpDown" title="下移"><span class="teamworkIcon">j</span></button>
      <span class="ganttButtonSeparator requireCanWrite requireCanDelete"></span>
      <button onclick="$('#workSpace').trigger('deleteFocused.gantt');return false;" class="button textual icon delete requireCanWrite" title="删除"><span class="teamworkIcon">&cent;</span></button>
      <span class="ganttButtonSeparator"></span>
      <button onclick="$('#workSpace').trigger('expandAll.gantt');return false;" class="button textual icon " title="展开"><span class="teamworkIcon">6</span></button>
      <button onclick="$('#workSpace').trigger('collapseAll.gantt'); return false;" class="button textual icon " title="折叠"><span class="teamworkIcon">5</span></button>

    <span class="ganttButtonSeparator"></span>
      <button onclick="$('#workSpace').trigger('zoomMinus.gantt'); return false;" class="button textual icon " title="缩小"><span class="teamworkIcon">)</span></button>
      <button onclick="$('#workSpace').trigger('zoomPlus.gantt');return false;" class="button textual icon " title="放大"><span class="teamworkIcon">(</span></button>
    
    
    <span class="ganttButtonSeparator"></span>
      <button onclick="ge.gantt.showCriticalPath=!ge.gantt.showCriticalPath; ge.redraw();return false;" class="button textual icon requireCanSeeCriticalPath" title="关键路径"><span class="teamworkIcon">&pound;</span></button>
    <span class="ganttButtonSeparator requireCanSeeCriticalPath"></span>
      <button onclick="ge.splitter.resize(.1);return false;" class="button textual icon" ><span class="teamworkIcon">F</span></button>
      <button onclick="ge.splitter.resize(50);return false;" class="button textual icon" ><span class="teamworkIcon">O</span></button>
      <button onclick="ge.splitter.resize(100);return false;" class="button textual icon"><span class="teamworkIcon">R</span></button>
      
    <button class="button login" title="login/enroll" onclick="loginEnroll($(this));" style="display:none;">login/enroll</button>
    <button class="button opt collab" title="Start with Twproject" onclick="collaborate($(this));" style="display:none;"><em>collaborate</em></button>
    
     &nbsp; &nbsp; &nbsp; &nbsp;
    <button onclick="saveGanttOnServer();" class="button first big requireWrite" title="Save">保存</button>
    
    </div></div>
  --></div>

<div class="__template__" type="TASKSEDITHEAD"><!--
  <table class="gdfTable" cellspacing="0" cellpadding="0">
    <thead>
    <tr style="height:40px">
    	
      <th class="gdfColHeader" style="width:35px; border-right: none"></th>  
      <th class="gdfColHeader" style="width:25px;"></th>  
      <th class="gdfColHeader  gdfResizable"  style="width:100px;">WBS</th>  
      <th class="gdfColHeader  gdfResizable"  style="width:300px;">任务描述</th>	  
      <th class="gdfColHeader" align="center" style="width:17px;" title="Start date is a milestone."><span class="teamworkIcon" style="font-size: 8px;">^</span></th>	  
      <th class="gdfColHeader  gdfResizable"  style="width:80px;">开始日期</th>	  
      <th class="gdfColHeader" align="center" style="width:17px;" title="End date is a milestone."><span class="teamworkIcon" style="font-size: 8px;">^</span></th>
	  <th class="gdfColHeader  gdfResizable"  style="width:100px;">计划完成日期</th>    
	  <th class="gdfColHeader  gdfResizable"  style="width:100px;">实际完成日期</th>  
      <th class="gdfColHeader  gdfResizable"  style="width:50px;">工时(天)</th>
	  
	  <th class="gdfColHeader  gdfResizable"  style="width:20px;">任务进度%</th>
      <th class="gdfColHeader  gdfResizable"  style="width:80px;">责任人</th>
      <th class="gdfColHeader  gdfResizable"  style="width:100px;">状态</th> 
	  <th class="gdfColHeader  gdfResizable"  style="width:100px;display:none;">ID</th> 
      <th class="gdfColHeader  gdfResizable"  style="width:100px;display:none;">主表ID</th> 
      <th class="gdfColHeader  gdfResizable"  style="width:100px;display:none;">建议书ID</th>
      
    </tr>
    </thead>
  </table>
  --></div>

<div class="__template__" type="TASKROW"><!--
    
 <tr id="tid_(#=obj.id#)" taskId="(#=obj.id#)" class="taskEditRow (#=obj.isParent()?'isParent':''#) (#=obj.collapsed?'collapsed':''#)" level="(#=level#)">
	<th class="gdfCell edit" align="right" style="cursor:pointer;"><span class="taskRowIndex">(#=obj.getRow()+1#)</span> <span class="teamworkIcon" style="font-size:12px;" >e</span></th>
    <td class="gdfCell noClip" align="center" style="display:none;><div class="taskStatus cvcColorSquare" status="(#=obj.status#)"></div></td>
	<td class="gdfCell" align="center"></td>
    <td class="gdfCell align="center""><input type="text" name="code" value="(#=obj.code?obj.code:''#)" placeholder="WBS"></td>
	
	
    <td class="gdfCell indentCell" style="padding-left:(#=obj.level*10+18#)px;">
      <div class="exp-controller" align="center"></div>
      <input type="text" name="name" value="(#=obj.name#)" placeholder="请填写">
    </td>
	
    <td class="gdfCell" align="center"><input type="checkbox" name="startIsMilestone"></td>
    <td class="gdfCell"><input type="text" name="start"  value="" class="date"></td>
	
    <td class="gdfCell" align="center"><input type="checkbox" name="endIsMilestone"></td>
    <td class="gdfCell"><input type="text" name="end" value="" class="date"></td>
	<td class="gdfCell"><input type="text" id="APPLN"  name="appln_time" value="" class="date"></td>
    <td class="gdfCell"><input type="text" name="duration" autocomplete="off" value="(#=obj.duration#)"></td>
	
    <td class="gdfCell"><input type="text" name="progress" class="validated" entrytype="PERCENTILE" autocomplete="off" value="(#=obj.progress?obj.progress:''#)" (#=obj.progressByWorklog?"readOnly":""#)></td>
	
	<td class="gdfCell"><input id="main_name"  name="main_name"  type="text"   value="" class=""></td>
    <td class="gdfCell"><input id="status_all" name="wbs_status" type="text"   value="" class=""></td>
    <td class="gdfCell"><input  style="display:none;" name="wbs_ids" type="text"   value="" class=""></td>
	<td class="gdfCell"><input  style="display:none;" name="proj_id" type="text"   value="" class=""></td>
	<td class="gdfCell"><input  style="display:none;" name="proj_phase_id" type="text"   value="" class=""></td>
  </tr>
    
  --></div>

<div class="__template__" type="TASKEMPTYROW"><!--
  <tr class="taskEditRow emptyRow" >
    <th class="gdfCell" align="right"></th>
    <td class="gdfCell noClip" align="center"></td>
    <td class="gdfCell"></td>
    <td class="gdfCell"></td>
    <td class="gdfCell"></td>
    <td class="gdfCell"></td>
    <td class="gdfCell"></td>
    <td class="gdfCell"></td>
    <td class="gdfCell"></td>
    <td class="gdfCell"></td>
    <td class="gdfCell requireCanSeeDep"></td>
    <td class="gdfCell"></td>
	<td class="gdfCell"></td>
    <td class="gdfCell"></td>
  </tr>
  --></div>

<div class="__template__" type="TASKBAR"><!--
  <div class="taskBox taskBoxDiv" taskId="(#=obj.id#)" >
    <div class="layout (#=obj.hasExternalDep?'extDep':''#)">
      <div class="taskStatus" status="(#=obj.status#)"></div>
      <div class="taskProgress" style="width:(#=obj.progress>100?100:obj.progress#)%; background-color:(#=obj.progress>100?'red':'rgb(153,255,51);'#);"></div>
      <div class="milestone (#=obj.startIsMilestone?'active':''#)" ></div>

      <div class="taskLabel"></div>
      <div class="milestone end (#=obj.endIsMilestone?'active':''#)" ></div>
    </div>
  </div>
  --></div>


<div class="__template__" type="CHANGE_STATUS"><!--
    <div class="taskStatusBox">
    <div class="taskStatus cvcColorSquare" status="STATUS_ACTIVE" title="Active"></div>
    <div class="taskStatus cvcColorSquare" status="STATUS_DONE" title="Completed"></div>
    <div class="taskStatus cvcColorSquare" status="STATUS_FAILED" title="Failed"></div>
    <div class="taskStatus cvcColorSquare" status="STATUS_SUSPENDED" title="Suspended"></div>
    <div class="taskStatus cvcColorSquare" status="STATUS_WAITING" title="Waiting" style="display: none;"></div>
    <div class="taskStatus cvcColorSquare" status="STATUS_UNDEFINED" title="Undefined"></div>
    </div>
  --></div>




<div class="__template__" type="TASK_EDITOR"><!--
  <div class="ganttTaskEditor">
    <h2 class="taskData">编辑</h2>
    <table  cellspacing="1" cellpadding="5" width="100%" class="taskData table" border="0">
          <tr>
          <td style="display:none;"><input  style="display:none;"  id="wbs_ids" name="wbs_ids" type="text" autocomplete='off'  value="" class="formElements" oldvalue="1"></td>  
          
          <td width="200" style="height: 80px"  valign="top">
          
          <label for="code">WBS</label><br>
          <input type="text" name="" id="" value="" size=15 class="formElements" autocomplete='off' maxlength=255 style='width:100%;display:none;' oldvalue="1">
          <select id="code" name="code" code="(#=obj.code#)">
                <option value="立项" code="立项">立项</option>
                <option value="客户需求确认" code="客户需求确认">客户需求确认</option>
                <option value="项目建议书评审完成" code="项目建议书评审完成">项目建议书评审完成</option>
                <option value="采购完成" code="采购完成">采购完成</option>
                <option value="开发设计" code="开发设计">开发设计</option>
                <option value="实施" code="实施">实施</option>
                <option value="项目初验" code="项目初验">项目初验</option>
                <option value="客户验收上线" code="客户验收上线">客户验收上线</option>
           
          </select>
        
        
        
        
          </td>
          <td colspan="3" valign="top"><label for="name" class="required">任务描述</label><br><input type="text" name="name" id="name"class="formElements" autocomplete='off' maxlength=255 style='width:100%' value="" required="true" oldvalue="1"></td>
          </tr>


     <tr class="dateRow">
        <td nowrap="">
          <div style="position:relative">
            <label for="start">开始日期</label>&nbsp;&nbsp;&nbsp;&nbsp;
            
            <br><input type="text" name="start" id="start" size="8" class="formElements dateField validated date" autocomplete="off" maxlength="255" value="" oldvalue="1" entrytype="DATE">
            <span title="calendar" id="starts_inputDate" class="teamworkIcon openCalendar" onclick="$(this).dateField({inputField:$(this).prevAll(':input:first'),isSearchField:false});">m</span>          </div>
        </td>
        
        
        
        
        <td nowrap="">
          <label for="end">计划日期</label>&nbsp;&nbsp;&nbsp;&nbsp;
          
          <br><input type="text" name="end" id="end" size="8" class="formElements dateField validated date" autocomplete="off" maxlength="255" value="" oldvalue="1" entrytype="DATE" onclick="open_message_time2()">
          <span title="calendar" id="ends_inputDate" class="teamworkIcon openCalendar" onclick="$(this).dateField({inputField:$(this).prevAll(':input:first'),isSearchField:false});">m</span>
        </td>
        
        <td nowrap="">
          <label for="appln_time">完成日期</label>&nbsp;&nbsp;&nbsp;&nbsp;
         
          <br><input type="text" name="appln_time" id="appln_time" size="8" class="formElements dateField validated date" autocomplete="off" maxlength="255" value="" oldvalue="1" entrytype="DATE" onclick="open_message_time1()"  >
          <span title="calendar" id="ends_inputDate" class="teamworkIcon openCalendar" onclick="$(this).dateField({inputField:$(this).prevAll(':input:first'),isSearchField:false});">m</span>
        </td>
        
        
        
        <td nowrap="" >
          <label for="duration" class=" ">工时（天）</label><br>
          <input type="text" name="duration" id="duration" size="4" class="formElements validated durationdays" title="Duration is in working days." autocomplete="off" maxlength="255" value="" oldvalue="1" entrytype="DURATIONDAYS">&nbsp;
        </td>
        
        <td valign="top" nowrap style="display:none;">
          <label>%</label><br>
          <input type="text" name="progress" id="progress" size="7" class="formElements validated percentile" autocomplete="off" maxlength="255" value="" oldvalue="1" entrytype="PERCENTILE">
        </td>
      </tr>

      <tr>
        <td  colspan="2" style="display:none;">
          <label for="status" class=" ">status</label><br>
          <select id="status" name="status" class="taskStatus" status="(#=obj.status#)"  onchange="$(this).attr('STATUS',$(this).val());">
            <option value="STATUS_ACTIVE" class="taskStatus" status="STATUS_ACTIVE" >active</option>
            <option value="STATUS_WAITING" class="taskStatus" status="STATUS_WAITING" >suspended</option>
            <option value="STATUS_SUSPENDED" class="taskStatus" status="STATUS_SUSPENDED" >suspended</option>
            <option value="STATUS_DONE" class="taskStatus" status="STATUS_DONE" >completed</option>
            <option value="STATUS_FAILED" class="taskStatus" status="STATUS_FAILED" >failed</option>
            <option value="STATUS_UNDEFINED" class="taskStatus" status="STATUS_UNDEFINED" >undefined</option>
          </select>
        </td>
      </tr>

        
          <tr>
            <td>
              <label for="main_name">责任人</label><br>
               <input type="text" name="main_name" id="main_name" value="" size=15 class="formElements" autocomplete='off'  style='width:100px' oldvalue="1">
            </td>
            
             <td>
              <label for="wbs_status">状态</label><br>
              <select id="wbs_status" name="wbs_status" wbs_status="(#=obj.wbs_status#)" >
                <option value="正常" wbs_status="正常">正常</option>
                <option value="预警" wbs_status="预警">预警</option>
                <option value="延期" wbs_status="延期">延期</option>
           
              </select>
            </td>
          </tr>
        </table>

  
 

  <div style="text-align: right; padding-top: 20px">
    <span id="saveButton" class="button first" onClick="$(this).trigger('saveFullEditor.gantt')">完成</span>
  </div>

  </div>
  --></div>



<div class="__template__" type="ASSIGNMENT_ROW"><!--
  <tr taskId="(#=obj.task.id#)" assId="(#=obj.assig.id#)" class="assigEditRow" >
    <td ><select name="resourceId"  class="formElements" (#=obj.assig.id.indexOf("tmp_")==0?"":"disabled"#) ></select></td>
    <td ><select type="select" name="roleId"  class="formElements"></select></td>
    <td ><input type="text" name="effort" value="(#=getMillisInHoursMinutes(obj.assig.effort)#)" size="5" class="formElements"></td>
    <td align="center"><span class="teamworkIcon delAssig del" style="cursor: pointer">d</span></td>
  </tr>
  --></div>



<div class="__template__" type="RESOURCE_EDITOR"><!--
  <div class="resourceEditor" style="padding: 5px;">

    <h2>Project team</h2>
    <table  cellspacing="1" cellpadding="0" width="100%" id="resourcesTable">
      <tr>
        <th style="width:100px;">name</th>
        <th style="width:30px;" id="addResource"><span class="teamworkIcon" style="cursor: pointer">+</span></th>
      </tr>
    </table>

    <div style="text-align: right; padding-top: 20px"><button id="resSaveButton" class="button big">Save</button></div>
  </div>
  --></div>



<div class="__template__" type="RESOURCE_ROW"><!--
  <tr resId="(#=obj.id#)" class="resRow" >
    <td ><input type="text" name="name" value="(#=obj.name#)" style="width:100%;" class="formElements"></td>
    <td align="center"><span class="teamworkIcon delRes del" style="cursor: pointer">d</span></td>
  </tr>
  --></div>


</div>
<script type="text/javascript">
  $.JST.loadDecorator("RESOURCE_ROW", function(resTr, res){
    resTr.find(".delRes").click(function(){$(this).closest("tr").remove()});
  });

  $.JST.loadDecorator("ASSIGNMENT_ROW", function(assigTr, taskAssig){
    var resEl = assigTr.find("[name=resourceId]");
    var opt = $("<option>");
    resEl.append(opt);
    for(var i=0; i< taskAssig.task.master.resources.length;i++){
      var res = taskAssig.task.master.resources[i];
      opt = $("<option>");
      opt.val(res.id).html(res.name);
      if(taskAssig.assig.resourceId == res.id)
        opt.attr("selected", "true");
      resEl.append(opt);
    }
    var roleEl = assigTr.find("[name=roleId]");
    for(var i=0; i< taskAssig.task.master.roles.length;i++){
      var role = taskAssig.task.master.roles[i];
      var optr = $("<option>");
      optr.val(role.id).html(role.name);
      if(taskAssig.assig.roleId == role.id)
        optr.attr("selected", "true");
      roleEl.append(optr);
    }

    if(taskAssig.task.master.permissions.canWrite && taskAssig.task.canWrite){
      assigTr.find(".delAssig").click(function(){
        var tr = $(this).closest("[assId]").fadeOut(200, function(){$(this).remove()});
      });
    }

  });


  function loadI18n(){
    GanttMaster.messages = {
      "CANNOT_WRITE":"No permission to change the following task:",
      "CHANGE_OUT_OF_SCOPE":"Project update not possible as you lack rights for updating a parent project.",
      "START_IS_MILESTONE":"Start date is a milestone.",
      "END_IS_MILESTONE":"End date is a milestone.",
      "TASK_HAS_CONSTRAINTS":"Task has constraints.",
      "GANTT_ERROR_DEPENDS_ON_OPEN_TASK":"Error: there is a dependency on an open task.",
      "GANTT_ERROR_DESCENDANT_OF_CLOSED_TASK":"Error: due to a descendant of a closed task.",
      "TASK_HAS_EXTERNAL_DEPS":"This task has external dependencies.",
      "GANNT_ERROR_LOADING_DATA_TASK_REMOVED":"GANNT_ERROR_LOADING_DATA_TASK_REMOVED",
      "CIRCULAR_REFERENCE":"Circular reference.",
      "CANNOT_DEPENDS_ON_ANCESTORS":"Cannot depend on ancestors.",
      "INVALID_DATE_FORMAT":"The data inserted are invalid for the field format.",
      "GANTT_ERROR_LOADING_DATA_TASK_REMOVED":"An error has occurred while loading the data. A task has been trashed.",
      "CANNOT_CLOSE_TASK_IF_OPEN_ISSUE":"Cannot close a task with open issues",
      "TASK_MOVE_INCONSISTENT_LEVEL":"You cannot exchange tasks of different depth.",
      "CANNOT_MOVE_TASK":"CANNOT_MOVE_TASK",
      "PLEASE_SAVE_PROJECT":"PLEASE_SAVE_PROJECT",
      //"GANTT_SEMESTER":"Semester",
      "GANTT_SEMESTER":"半年",
      "GANTT_SEMESTER_SHORT":"s.",
      //"GANTT_QUARTER":"Quarter",
      "GANTT_QUARTER":"季度",
      "GANTT_QUARTER_SHORT":"q.",
      "GANTT_WEEK":"Week",
      "GANTT_WEEK_SHORT":"w."
    };
  }



  function createNewResource(el) {
    var row = el.closest("tr[taskid]");
    var name = row.find("[name=resourceId_txt]").val();
    var url = contextPath + "/applications/teamwork/resource/resourceNew.jsp?CM=ADD&name=" + encodeURI(name);

    openBlackPopup(url, 700, 320, function (response) {
      //fillare lo smart combo
      if (response && response.resId && response.resName) {
        //fillare lo smart combo e chiudere l'editor
        row.find("[name=resourceId]").val(response.resId);
        row.find("[name=resourceId_txt]").val(response.resName).focus().blur();
      }

    });
  }
  
  $(function(){
    $(" input ").attr("disabled","disabled");
    //$("#workSpace").children("#TWGanttArea").children().children().children().children("table").next("table").children("thead").next("tr").children("th").children("span").attr("click","");
      $("#workSpace").children().next("div").children().children().children().children().next("table").children().next("tr").children().children().attr("onclick","");
  });
 
  
  
</script>






</body>
</html>