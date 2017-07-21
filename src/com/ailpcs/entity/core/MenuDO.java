package com.ailpcs.entity.core;

import java.io.Serializable;
import java.util.List;
/**
  * 菜单表DO (sys_menu)
  * MichaelTsui17/06/25, 加入实现序列化接口, 用于深度复制List<Menu>
  */
public class MenuDO  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String menuId;		//菜单ID sys_menu.MENU_ID
	private String menuName;	//菜单名称 sys_menu.MENU_NAME
	private String menuUrl;	    //链接 sys_menu.MENU_URL
	private String parentId;	//上级菜单ID sysmenu.PARENT_ID
	private String menuOrder;	//排序 sys_menu.MENU_ORDER
	private String menuIcon;	//图标 sys_menu.MENU_ICON
	private String menuType;	//类型 sys_menu.MENU_TYPE      1-系统  2-业务
	private Integer menuState;	//菜单状态 sys_menu.MENU_STATE  0-隐藏  1-显示 , 菜单是否显示在主画面的两个条件: 1. hasMenu=True 2.Menu_STtate=1
	//private String menuStyle;	// sys_menu.MENU_STYLE
	//private String activeStatus;	// sys_menu.ACTIVE_STATUS
	
	private String target;
	private MenuDO parentMenu;
	private List<MenuDO> subMenu;
	private boolean hasMenu = false;  //用户是否有此菜单的操作权
	
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId == null ? null : menuId.trim();
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName == null ? null : menuName.trim();
	}
	public String getMenuUrl() {
		return menuUrl;
	}
	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl == null ? null : menuUrl.trim();
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId == null ? null : parentId.trim();
	}
	public String getMenuOrder() {
		return menuOrder;
	}
	public void setMenuOrder(String menuOrder) {
		this.menuOrder = menuOrder == null ? null : menuOrder.trim();
	}
	public String getMenuIcon() {
		return menuIcon;
	}
	public void setMenuIcon(String menuIcon) {
		this.menuIcon = menuIcon == null ? null : menuIcon.trim();
	}
	public String getMenuType() {
		return menuType;
	}
	public void setMenuType(String menuType) {
		this.menuType = menuType == null ? null : menuType.trim();
	}
	public Integer getMenuState() {
		return menuState;
	}
	public void setMenuState(Integer menuState) {
		this.menuState = menuState;
	}
	/*
	public String getMenuStyle() {
		return menuStyle;
	}
	public void setMenuStyle(String menuStyle) {
		this.menuStyle = menuStyle == null ? null : menuStyle.trim();
	}
	public String getactiveStatus() {
		return activeStatus;
	}
	public void setactiveStatus(String activeStatus) {
		this.activeStatus = activeStatus == null ? null : activeStatus.trim();
	}	
	*/
	public MenuDO getParentMenu() {
		return parentMenu;
	}
	public void setParentMenu(MenuDO parentMenu) {
		this.parentMenu = parentMenu;
	}
	public List<MenuDO> getSubMenu() {
		return subMenu;
	}
	public void setSubMenu(List<MenuDO> subMenu) {
		this.subMenu = subMenu;
	}
	public boolean isHasMenu() {
		return hasMenu;
	}
	public void setHasMenu(boolean hasMenu) {
		this.hasMenu = hasMenu;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target == null ? null : target.trim();
	}
	
}
