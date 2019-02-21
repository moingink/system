
<!-- bootstrapIE8补丁 -->
<%@page import="com.yonyou.util.PropertyFileUtil"%>
<%@page import="com.yonyou.util.theme.ThemePath"%>
<%@page import="com.yonyou.util.RmIdFactory"%>
<%@page import="com.yonyou.business.entity.TokenEntity"%>
<%@page import="com.yonyou.business.entity.TokenUtil"%>
<script src="<%=request.getContextPath()%>/vendor/bootstrap/js/html5.js"></script>
<script src="<%=request.getContextPath()%>/vendor/bootstrap/js/respond.js"></script>

<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
  <script src="http://apps.bdimg.com/libs/html5shiv/3.7/html5shiv.min.js"></script>
  <script src="http://apps.bdimg.com/libs/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->
<link rel="stylesheet" href="<%=request.getContextPath()%>/vendor/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/vendor/bootstrap/css/bootstrap-datetimepicker.min.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/vendor/jquery/bigAutocomplete/bigAutocomplete.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/vendor/bootstrap-table/src/bootstrap-table.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/vendor/bootstrap3-editable/css/bootstrap-editable.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/vendor/bootstrap-select-master/dist/css/bootstrap-select.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/vendor/bootstrap-fileinput/css/fileinput.min.css"  media="all">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/css01.css">

<script src="<%=request.getContextPath()%>/vendor/jquery/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/vendor/jquery/bigAutocomplete/bigAutocomplete.js"></script>
<script src="<%=request.getContextPath()%>/vendor/bootstrap/js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/vendor/bootstrap-table/src/bootstrap-table.js"></script>
<script src="<%=request.getContextPath()%>/vendor/bootstrap-table/src/bootstrap-table.js"></script>
<script src="<%=request.getContextPath()%>/vendor/bootstrap-select-master/dist/js/bootstrap-select.js" defer></script>
<script src="<%=request.getContextPath()%>/vendor/bootstrap-select-master/dist/js/i18n/defaults-zh_CN.min.js" defer></script>
<script src="<%=request.getContextPath()%>/vendor/bootstrap3-editable/js/bootstrap-editable.js"></script>
<script src="<%=request.getContextPath()%>/vendor/bootstrap-table/src/bootstrap-table-editable.js"></script> 
<script src="<%=request.getContextPath()%>/vendor/bootstrap-fileinput/js/fileinput.js"></script>
<script src="<%=request.getContextPath()%>/vendor/bootstrap-fileinput/js/locales/zh.js"></script>
<script src="<%=request.getContextPath()%>/vendor/bootstrap/js/bootstrap-datetimepicker.js" charset="UTF-8"></script>
<script src="<%=request.getContextPath()%>/vendor/bootstrap/js/locales/bootstrap-datetimepicker.zh-CN.js" charset="UTF-8"></script>
<script src="<%=request.getContextPath()%>/vendor/bootstrap/js/bootstrap_load.js" charset="UTF-8"></script>
<script src="<%=request.getContextPath()%>/pages/js/reference.js"></script>
<script type="text/javascript">
var isBuildButtonByFile = false; //是否开启js文件调试模式,true :js文件获取按钮  ； false:数据库获取已授权按钮；
var context = '<%=request.getContextPath()%>';

var dataSourceCode ='<%=request.getParameter("pageCode")%>';
var pageName ='<%=request.getParameter("pageName")%>';
var menuCode ='<%=request.getParameter("menuCode")!=null?request.getParameter("menuCode"):""%>';
var totalcode ='<%=request.getParameter("totalcode")!=null?request.getParameter("totalcode"):""%>';
var url ='<%=request.getParameter("url")%>';
var rolecode='';
var corpcode='';
var token='<%=request.getParameter("token")!=null?request.getParameter("token"):""%>';
<%TokenEntity tokenEntity = TokenUtil.initTokenEntity(request);%>
var corpId = '<%=tokenEntity.COMPANY.getCompany_id()%>';
var userId = '<%=tokenEntity.USER.getId()%>';
var userName = '<%=tokenEntity.USER.getName()%>';

var cumaEmail = '<%=tokenEntity.USER.getEmail()%>';
var cumaPhone = '<%=tokenEntity.USER.getCustom3()%>';

var companyName ='<%= tokenEntity.COMPANY.getCompany_name()%>';
var companyNameID ='<%= tokenEntity.COMPANY.getCompany_id()%>';
var roleId = '<%= tokenEntity.ROLE==null?"":tokenEntity.ROLE.getRoleId()%>';
var isNewModel = false;

var _dataSourceCode='<%= request.getParameter("_dataSourceCode")%>';
var ParentId ='ID';
var ParentIded ='CON_ID';
var ParentPKField='<%=request.getParameter("ParentPKField")!=null?request.getParameter("ParentPKField"):""%>';
var ParentPKValue='<%=request.getParameter("ParentPKValue")!=null?request.getParameter("ParentPKValue"):""%>';

var _query_param='';
if(ParentPKField!=null&&ParentPKField.length>0){
	_query_param =pageParamFormat(ParentPKField+" = "+ParentPKValue);
}	
var query_buttonToken ='';
$('input[name="ParentPK"]').attr('id',ParentPKField).val(ParentPKValue);
function pageParamFormat(p){
	if(p!=null&p.length>0){
		return "&pageParam="+"'"+p+"'";
	}else{
		return "";
	}
}

var cacheTableJsonArray =null;

var initTableParam='<%=request.getParameter("initTableParam")!=null?request.getParameter("initTableParam").replaceAll(",", "&"):""%>';
if(initTableParam!=null&&initTableParam.length>0){
	initTableParam="&"+initTableParam;
}else{
	initTableParam ="";
}

var isAddAuditBut ='<%=request.getParameter("isAddAuditBut")!=null?request.getParameter("isAddAuditBut"):""%>';
var workflow ='<%=PropertyFileUtil.getValue("WORK_FLOW_HIS")%>';

var appCode=context.replace("/","");

var ParentPKParams='<%=request.getParameter("ParentPKParams")!=null?request.getParameter("ParentPKParams"):""%>';
var isDetail="<%= request.getParameter("isDetail")!=null?(request.getParameter("isDetail").equals("1")?"1":"0"):"0" %>";
var isHide = '<%=request.getParameter("isHide")%>';
var _isBusPage="<%= request.getParameter("_isBusPage")!=null?(request.getParameter("_isBusPage").equals("1")?"1":"0"):"0" %>";
function getId(){
	var rmId = '<%= RmIdFactory.requestId("BUS_BUS_EXAMINE", 1)[0]%>';
	return rmId;
}
</script>

<script src="<%=request.getContextPath()%>/js/public.js"></script>
<script src="<%=request.getContextPath()%>/js/openDetail.js"></script>
<script src="<%=request.getContextPath()%>/js/properties.js"></script>
<script src="<%=request.getContextPath()%>/js/crud.js"></script>
<script src="<%=request.getContextPath()%>/js/bootTable.js"></script>
<script src="<%=request.getContextPath()%>/js/common.js"></script>
<script src="<%=request.getContextPath()%>/js/initPage.js"></script>
<script src="<%=request.getContextPath()%>/js/pubAuditJs.js"></script>
<script src="<%=request.getContextPath()%>/js/singlePubAuditJs.js"></script>
<script src="<%=request.getContextPath()%>/js/buttonJs.js"></script>
<script src="<%=request.getContextPath()%>/js/validation.js"></script>
<script src="<%=request.getContextPath()%>/js/validation.js"></script>
<script src="<%=request.getContextPath()%>/js/bulidPlistPage.js"></script>
<script src="<%=request.getContextPath()%>/js/hide.js"></script>
<script src="<%=request.getContextPath()%>/js/dblclickfunction.js"></script>
<script src="<%=request.getContextPath()%>/js/pubBusFlow.js"></script>
<script src="<%=request.getContextPath()%>/js/fileUpload.js"></script>
<script src="<%=request.getContextPath()%>/js/showDelivery.js"></script>

<script type="text/javascript"  src='../pages/scoremessage/scoremessage.js'></script>

<input type="hidden" id="ins_or_up_buttontoken"/>
<input type="hidden" id="query_buttontoken" value="query"/>
<input type="hidden" id="cache_dataSourceCode"/>
<input type="hidden" id="BILL_STATUS" value='<%=request.getParameter("BILL_STATUS")%>'/>

<link href="<%=request.getContextPath()%>/vendor/bootstrap/css/bootstrap.css" rel="stylesheet" />
<link href="<%=request.getContextPath()%>/vendor/Bootstrap-icon-picker/css/icon-picker.css" rel="stylesheet" /> 
<script src="<%=request.getContextPath()%>/vendor/Bootstrap-icon-picker/js/iconPicker.js"></script>

<script src="<%=request.getContextPath()%>/pages/js/multiselectDemo.js"></script>

<link rel="stylesheet" href="<%=request.getContextPath()%>/pages/vailidator/css/bootstrapValidator.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/pages/vailidator/js/bootstrapValidator.js"></script>

<style type="text/css">
	.tooltip-inner {
	  max-width: 400px;
	  padding: 3px 8px;
	  color: #000;
	  text-align: center;
	  text-decoration: none;
	  background-color: rgb(240,240,242);
	  border-radius: 4px;
	}
</style>
