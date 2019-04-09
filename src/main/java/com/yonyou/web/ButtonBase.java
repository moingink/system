package com.yonyou.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yonyou.business.button.util.system.ButForConnectTest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yonyou.business.button.ButtonAbs;
import com.yonyou.business.button.cache.ButForDeleteWithCache;
import com.yonyou.business.button.cache.ButForInsertWithCache;
import com.yonyou.business.button.cache.ButForUpdateWithCache;
import com.yonyou.business.button.demobutton.button.DemoButForCheck;
import com.yonyou.business.button.demobutton.button.DemoButForCheckBack;
import com.yonyou.business.button.demobutton.button.DemoButForImport;
import com.yonyou.business.button.demobutton.button.DemoButForUpdate;
import com.yonyou.business.button.notify.NoticeManageButForDelete;
import com.yonyou.business.button.notify.NoticeManageButForInsert;
import com.yonyou.business.button.notify.NoticeManageButForUpdate;
import com.yonyou.business.button.notify.NotifyButForInsert;
import com.yonyou.business.button.notify.NotifyButForPlan;
import com.yonyou.business.button.notify.NotifyButForUpdate;
import com.yonyou.business.button.rm.party.PartyButForDelete;
import com.yonyou.business.button.rm.party.PartyButForInsertCompany;
import com.yonyou.business.button.rm.party.PartyButForInsertDepartment;
import com.yonyou.business.button.rm.party.PartyButForUpdate;
import com.yonyou.business.button.rm.role.ButForAddRoleTheme;
import com.yonyou.business.button.rm.role.RoleButForDelete;
import com.yonyou.business.button.rm.role.RoleButForGrantToButton;
import com.yonyou.business.button.rm.role.RoleButForInsert;
import com.yonyou.business.button.rm.role.RoleButForModifyAuthorizeToButton;
import com.yonyou.business.button.rm.role.RoleButForQueryToButton;
import com.yonyou.business.button.rm.role.RoleButForRoleMutexDelete;
import com.yonyou.business.button.rm.role.RoleButForRoleMutexSetting;
import com.yonyou.business.button.rm.role.RoleButForUserSetting;
import com.yonyou.business.button.rm.role.RoleButtonAuthNext;
import com.yonyou.business.button.rm.theme.ButForAddPic;
import com.yonyou.business.button.rm.theme.ButForAddTheme;
import com.yonyou.business.button.rm.user.UserButForActivation;
import com.yonyou.business.button.rm.user.UserButForDelete;
import com.yonyou.business.button.rm.user.UserButForInsert;
import com.yonyou.business.button.rm.user.UserButForLock;
import com.yonyou.business.button.rm.user.UserButForPasswordStrategy;
import com.yonyou.business.button.rm.user.UserButForResetPassword;
import com.yonyou.business.button.rm.user.UserButForRoleSetting;
import com.yonyou.business.button.rm.user.UserButForUpdate;
import com.yonyou.business.button.system.button.SystemButtonButForBind;
import com.yonyou.business.button.system.button.SystemButtonButForDelete;
import com.yonyou.business.button.system.button.SystemButtonButForInsert;
import com.yonyou.business.button.system.button.SystemButtonButForInsertButton;
import com.yonyou.business.button.system.button.SystemButtonButForUnbind;
import com.yonyou.business.button.system.button.SystemButtonButForUpdate;
import com.yonyou.business.button.system.button.SystemButtonButForUpdateButton;
import com.yonyou.business.button.taskjob.button.ButForJobDelete;
import com.yonyou.business.button.taskjob.button.ButForJobInsert;
import com.yonyou.business.button.taskjob.button.ButForJobStateUp;
import com.yonyou.business.button.taskjob.button.ButForJobStop;
import com.yonyou.business.button.util.ButForAddCuMenu;
import com.yonyou.business.button.util.ButForAudit;
import com.yonyou.business.button.util.ButForBusFlow;
import com.yonyou.business.button.util.ButForDeleteCuMenu;
import com.yonyou.business.button.util.ButForDeleteForDb;
import com.yonyou.business.button.util.ButForInsert;
import com.yonyou.business.button.util.ButForInsertCompany;
import com.yonyou.business.button.util.ButForInsertDepartment;
import com.yonyou.business.button.util.ButForInsertForRoleDataAuth;
import com.yonyou.business.button.util.ButForMetadataPhyIns;
import com.yonyou.business.button.util.ButForMetadataPhyUpd;
import com.yonyou.business.button.util.ButForPhysicalDelete;
import com.yonyou.business.button.util.ButForPrintPreview;
import com.yonyou.business.button.util.ButForProcedure;
import com.yonyou.business.button.util.ButForResetCuMenu;
import com.yonyou.business.button.util.ButForSelect;
import com.yonyou.business.button.util.ButForUpdate;
import com.yonyou.business.button.util.ButForUpdateStatus;
import com.yonyou.business.button.util.IncomeExpor;
import com.yonyou.business.button.util.system.ButForInsertOrUpdate;
import com.yonyou.business.button.util.system.ButForInsertTestDemo;
import com.yonyou.business.button.util.system.ButForUpdateTestDemo;


@RestController
@RequestMapping(value = "/buttonBase")
public class ButtonBase extends ButtonController {

	
	private Map<String,ButtonAbs> buttonMap =new HashMap<String,ButtonAbs>();
	{
		/*************************公共的*****************/
		buttonMap.put("add", 	new ButForInsert());//新增
		buttonMap.put("doem", 	new ButForInsert());//新增
		buttonMap.put("update", new ButForUpdate());//修改 
		buttonMap.put("query", 	new ButForSelect());//查询
		buttonMap.put("delete", new ButForDeleteForDb());//删除 //应demo需要 暂换成物理删除  ；ButForDelete 为逻辑删除  --moing_Ink
		buttonMap.put("physicalDelete", new ButForPhysicalDelete());//物理删除
		
		
		/*************************demo*****************/
		buttonMap.put("button_check", new DemoButForCheck());//复核
		buttonMap.put("button_checkback", new DemoButForCheckBack());//撤销复核
		buttonMap.put("button_import", new DemoButForImport());//Excel导入
		buttonMap.put("updateByDemo", new DemoButForUpdate());//修改
		
		/*************************带缓存的数据的增删改*****************/
		buttonMap.put("insertWithCache", new ButForInsertWithCache());//新增
		buttonMap.put("updateWithCache", new ButForUpdateWithCache());//修改
		buttonMap.put("deleteWithCache", new ButForDeleteWithCache());//删除
		
		/*************************组织管理的增删改*****************/
		buttonMap.put("addCompany", new PartyButForInsertCompany());//新增
		buttonMap.put("addDepartment", new PartyButForInsertDepartment());//新增
		buttonMap.put("deleteParty", new PartyButForDelete());//delete
		buttonMap.put("updateParty", new PartyButForUpdate());//update
		
		/*************************按钮管理*****************/
		buttonMap.put("but_rel_menu_bind", 	 new SystemButtonButForBind());//菜单按钮关联绑定
		buttonMap.put("but_rel_menu_unbind", new SystemButtonButForUnbind());//菜单按钮关联解绑
		buttonMap.put("but_rel_menu_insert", new SystemButtonButForInsert());//菜单按钮关联新增
		buttonMap.put("but_rel_menu_update", new SystemButtonButForUpdate());//菜单按钮关联修改
		buttonMap.put("but_rel_menu_delete", new SystemButtonButForDelete());//菜单按钮关联删除
		buttonMap.put("button_button_insert", new SystemButtonButForInsertButton());//按钮新增
		buttonMap.put("button_button_update", new SystemButtonButForUpdateButton());//按钮修改
		

		/*************************角色管理的增删改*****************/
		buttonMap.put("addRole", new RoleButForInsert());//新增
		buttonMap.put("deleteRole", new RoleButForDelete());//删除角色
		buttonMap.put("userSetting", new RoleButForUserSetting());//用户设置
		//buttonMap.put("deletePartyRole", new PartyRoleButForDelete());//删除角色关系
		buttonMap.put("roleMutexSetting", new RoleButForRoleMutexSetting());//用户设置
		buttonMap.put("roleMutexDelete", new RoleButForRoleMutexDelete());//互斥角色删除
		buttonMap.put("role_grant_to_button", new RoleButForGrantToButton());//角色按钮授权
		buttonMap.put("role_query_to_button", new RoleButForQueryToButton());//角色按钮授权查询
		buttonMap.put("modify_myself_authority", new RoleButForModifyAuthorizeToButton());//修改本人
		buttonMap.put("modify_company_authority", new RoleButForModifyAuthorizeToButton());//修改本机构
		buttonMap.put("modify_all_authority", new RoleButForModifyAuthorizeToButton());//修改所有
		
		buttonMap.put("nextAuth2", new RoleButtonAuthNext());//二级页面授权按钮
		
		/*************************用户管理的增删改*****************/
		buttonMap.put("addUser", new UserButForInsert());//新增
		buttonMap.put("updateUser", new UserButForUpdate());//修改
		buttonMap.put("deleteUser", new UserButForDelete());//删除
		buttonMap.put("activationUser", new UserButForActivation());//激活
		buttonMap.put("lockUser", new UserButForLock());//锁定
		buttonMap.put("resetPassword", new UserButForResetPassword());//锁定
		buttonMap.put("roleSetting", new UserButForRoleSetting());//角色设置
		buttonMap.put("passwordStrategy", new UserButForPasswordStrategy());//角色设置
		/*************************利用存储过程转化并插入子表*****************/
		buttonMap.put("procedure", new ButForProcedure());
		
		/*************************物理新增、修改元数据字段*****************/
		buttonMap.put("metadataPhyIns", new ButForMetadataPhyIns());
		buttonMap.put("metadataPhyUpd", new ButForMetadataPhyUpd());
		
		/*************************图片新增，修改（轮播图）*****************************/
		buttonMap.put("addPic", new ButForAddPic());//新增
		
		/*************************主题新增，修改（轮播图）*****************************/
		buttonMap.put("addThe", new ButForAddTheme());//新增
		
		/*************************角色主题新增（轮播图）*****************************/
		buttonMap.put("addRoleTheme", new ButForAddRoleTheme());//新增
		
		/*************************自定义菜单*****************************/
		buttonMap.put("addM", new ButForAddCuMenu());//新增
		buttonMap.put("resetM", new ButForResetCuMenu());//重置
		buttonMap.put("deleteM", new ButForDeleteCuMenu());//删除
		
		/*************************任务按钮*****************************/
		buttonMap.put("jobInsert", new ButForJobInsert()); //任务新增
		buttonMap.put("jobDelete", new ButForJobDelete()); //任务修改
		buttonMap.put("jobStateUp", new ButForJobStateUp()); //任务启动
		buttonMap.put("jobStop", new ButForJobStop()); //任务关闭
		
		/******************************公司 管理*****************************************/
		buttonMap.put("addCom", new ButForInsertCompany());
		/******************************部门 管理*****************************************/
		buttonMap.put("addDept", new ButForInsertDepartment());
		
		/******************************通知*******************************************/
		buttonMap.put("send", new NotifyButForPlan());
		buttonMap.put("addByNotity", 	new NotifyButForInsert());//新增
		buttonMap.put("updateByNotity", new NotifyButForUpdate());//修改
		
		buttonMap.put("addByNoticeManage", 	new NoticeManageButForInsert());//新增
		buttonMap.put("updateByNoticeManage", new NoticeManageButForUpdate());//修改
		buttonMap.put("deleteByNoticeManage", new NoticeManageButForDelete());//修改
		
		/******************************审批*****************************************/
		buttonMap.put("audit", new ButForAudit());
		/******************************修改表字段状态*****************************************/
		buttonMap.put("updateStatus", new ButForUpdateStatus());
		/******************************角色数据权限新增*****************************************/
		buttonMap.put("addForRoleDataAuth", new ButForInsertForRoleDataAuth());
		
		/******************************Excel导出*****************************************/
		buttonMap.put("zw_income_export", new IncomeExpor());
		
		/******************************业务流请求接口*****************************************/
		buttonMap.put("busFlow", new ButForBusFlow());
		
		
		
		/******************************新增主子表*****************************************/
		buttonMap.put("addTestDemo", new ButForInsertTestDemo());
		/******************************修改主子表*****************************************/

		buttonMap.put("updateTestDemo", new ButForUpdateTestDemo());
		
		//一主多子 子表保存
		buttonMap.put("saveChildDemo", new ButForInsertOrUpdate());
		
		//
        //  测阿萨v发v
        //方式通过深入体会b
		//测试冲突类解决办法
		//测试冲突类解决办法
		//测试文件冲突解决办法 #  阿斯顿
		/******************************打印预览*****************************************/
		buttonMap.put("printPreview", new ButForPrintPreview());

        /******************************wzl  开始*****************************************/

        buttonMap.put("ConnectTest", new ButForConnectTest());
        buttonMap.put("DirectoryTest", new ButForDirectoryTest());

        /******************************wzl  结束*****************************************/
	}
	
	@Override
	protected Map<String, ButtonAbs> findButtonMap() {
		// TODO 自动生成的方法存根
		return buttonMap;
	}
	
	
	@Override
	public void init(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO 自动生成的方法存根
		super.init(request, response);
	}

	
}
