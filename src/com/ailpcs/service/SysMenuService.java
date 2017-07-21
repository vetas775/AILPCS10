package com.ailpcs.service;

import java.util.List;
import java.util.Map;

import com.ailpcs.entity.core.MenuDO;
import com.ailpcs.entity.core.PageData;
/**
 * 菜单服务
 */
public interface SysMenuService {
	/**
	 * 列出某个菜单项下全部的子菜单
	 * @param String parentId
	 * @return List<MenuDO>
	 */
	List<MenuDO> listSubMenuByParentId(String parentId);	
	/**
	 * 根据MenuId获取一个菜单信息
	 * @param String menuId
	 * @return PageData
	 */
	PageData getMenuById(String menuId);		
	/**
	 * 保存新增菜单
	 * @param MenuDO menu
	 */
	String saveMenu(MenuDO menu);	
	/**
	 * 删除菜单
	 * @param menuId
	 */
	String deleteMenuById(String menuId);	
	/**
	 * 编辑菜单
	 * @param MenuDO menu
	 */
	void editMenu(MenuDO menu);	
	/**
	 * 标记菜单图标
	 */
	void editMenuIcon(PageData pd);	
	/**
	 * 获取系统总菜单(并设定用户授权)
	 * @param roleRights 授权字串，当它不为空时还要设定User对各菜单项的使用授权
	 */
	List<MenuDO> getAllMenuList(String roleRights, boolean needFillEditUrl);	
	/**
	 * 获取自menuId开始的所有菜单及其子菜单,并填充每个菜单的子菜单列表, 最后得到一个树状菜单列表(递归处理)
	 * @param String menuId, boolean needFillUrl
	 * @return List<MenuDO>
	 */
	List<MenuDO> listMenu(String menuId, boolean needFillEditUrl);	
	/**
	 * 设定菜单列表menuList里面各菜单项的使用授权(MenuDO.hasMenu)
	 * 参数 menuList-菜单列表   roleRights-授权字串, 来自sys_role.rights
	 */	
	List<MenuDO> setMenuRight(List<MenuDO> menuList, String roleRights);	
	/**
	 * 把树状菜单列表转化为json字符串, 用于Z-tree的节点显示
	 * @param menuList -树状菜单列表
	 * @return String  -转化后的Json字符串
	 */
	String menuTreeToJsonString(List<MenuDO> menuList);		
	/**
	 * 当主画面左边的菜单超过4级时，自动拓展出一个新的z-tree菜单, 本方法用于产生这个新z-tree的Json格式节点。
	 */
	Map<String, String> otherlistMenu(String menuId);
}
