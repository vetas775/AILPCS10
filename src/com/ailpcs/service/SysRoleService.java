package com.ailpcs.service;

import org.springframework.web.servlet.ModelAndView;

import com.ailpcs.entity.core.PageData;

/**
 * 角色管理服务
 */
public interface SysRoleService {	
	/**
	 * 抓取角色画面的数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	ModelAndView listRole(PageData pd);	
	/**
	 * 保存新增角色
	 */
	String saveNewRole(PageData pd);	
	/**
	 * 保存修改角色
	 */
	String editRole(PageData pd);	
	/**
	 * 删除角色
	 */
	String deleteRole(String roleId, String roleName);
	/**
	 *产生角色roleId的树状菜单列表(带授权)并转化为Json字串返回. 	
	 */
	String listMenuqx(String roleId);	
	/**
	 * 保存角色对菜单的访问权限(拥有这个角色的用户登录系统后是否能看到这个菜单(hasMenu))
	 * @param pd
	 * @throws Exception
	 */
	String saveMenuqx(String roleId, String menuIds);
	/**
	 *产生角色roleId的树状菜单列表(带增/删/改/查授权)并转化为Json字串返回. 	
	 */
	String listB4Button(String roleId, String msg);
	/**
	 * 保存角色对菜单的增删改查权限(增删改查)
	 * @param msg 区分增删改查
	 * @throws Exception
	 */
	String saveB4Button(String roleId, String menuIds, String msg);

}
