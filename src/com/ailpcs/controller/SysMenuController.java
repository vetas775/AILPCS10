package com.ailpcs.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ailpcs.core.Jurisdiction;
import com.ailpcs.dao.Dao2;
import com.ailpcs.entity.core.MenuDO;
import com.ailpcs.entity.core.PageData;
import com.ailpcs.service.SysMenuService;
import com.ailpcs.util.AppUtil;
/** 
 * "系统管理"=>"菜单管理"接口
 */
@Controller
@RequestMapping(value="/menu")
public class SysMenuController extends BaseController {
	private static final String menuUrl = "menu.do"; //菜单地址(权限用)
	@Resource(name="sysMenuService")
	private SysMenuService menuService;
	@Resource(name = "dao2")
	private Dao2<PageData> daoPD;	
	/**
	 * 显示菜单列表
	 * @param model
	 * @return
	 */
	@RequestMapping
	public ModelAndView list() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			String menuId = StringUtils.isBlank(pd.get("menuId").toString()) ? "0" : pd.get("menuId").toString();
			List<MenuDO> menuList = menuService.listSubMenuByParentId(menuId);
			mv.addObject("pd", menuService.getMenuById(menuId));	//传入父菜单所有信息
			mv.addObject("menuId", menuId);
			mv.addObject("MSG", null == pd.get("MSG") ? "'list'" : pd.get("MSG").toString()); //MSG='change' 则为编辑或删除后跳转过来的
			mv.addObject("menuList", menuList);
			mv.addObject("QX",Jurisdiction.getUserAuthList());	//按钮权限
			mv.setViewName("system/menu/menu_list");
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**
	 * 请求新增菜单页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/toAdd")
	public ModelAndView toAdd() throws Exception{
		ModelAndView mv = this.getModelAndView();
		try{
			PageData pd = new PageData();
			pd = this.getPageData();
			String menuId = StringUtils.isBlank(pd.get("menuId").toString()) ? "0" : pd.get("menuId").toString(); //接收传过来的上级菜单ID,如果上级为顶级就取值“0”
			pd.put("menuId",menuId);
			mv.addObject("pds", menuService.getMenuById(menuId));	//传入父菜单所有信息
			mv.addObject("menuId", menuId);					//传入菜单ID，作为子菜单的父菜单ID用
			mv.addObject("MSG", "addMenu");							//执行状态 add 为添加
			mv.setViewName("system/menu/menu_edit");
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}	
	
	/**
	 * 保存菜单信息
	 * @param menu
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/addMenu")
	public ModelAndView add(MenuDO menu) throws Exception{
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();	
		String sStr = menuService.saveMenu(menu); //保存新增的菜单
		if (!"".equals(sStr) ) {
			mv.addObject("msg",sStr);			
		}		
		//mv.setViewName("redirect:?MSG='change'&menuId="+menu.getParentId()); //保存成功跳转到列表页面
		String menuId = menu.getParentId();
		List<MenuDO> menuList = menuService.listSubMenuByParentId(menuId);
		mv.addObject("pd", menuService.getMenuById(menuId));	//传入父菜单所有信息
		mv.addObject("menuId", menuId);
		mv.addObject("MSG", "'change'"); //MSG=change 则为编辑或删除后跳转过来的
		mv.addObject("menuList", menuList);
		mv.addObject("QX",Jurisdiction.getUserAuthList());	//按钮权限
		mv.setViewName("system/menu/menu_list");
		return mv;
	}
	
	/**
	 * 删除菜单
	 * @param menuId
	 * @param out
	 */
	@RequestMapping(value="/delete")
	@ResponseBody
	public Object delete(@RequestParam("menuId")String menuId) throws Exception{
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")) {  //校验权限
			return null;
		} 
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = menuService.deleteMenuById(menuId);
		map.put("result", errInfo);
		return AppUtil.returnObject(new PageData(), map);
	}
	
	/**
	 * 请求编辑菜单页面
	 * @param id <====参数名必须为id (原因在: menuTreeToJsonString, Ztree)
	 * @return
	 */
	@RequestMapping(value="/toEdit")
	public ModelAndView toEdit(@RequestParam("id")String menuId) throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("menuId",menuId);				//接收过来的要修改的ID
		pd = menuService.getMenuById(menuId);	//读取此ID的菜单数据
		mv.addObject("pd", pd);				//放入视图容器
		String sParentId = pd.get("parentId").toString();		//用作读取父菜单信息
		mv.addObject("pds", menuService.getMenuById(sParentId));			//传入父菜单所有信息
		mv.addObject("menuId", pd.get("parentId").toString());	//传入父菜单ID，作为子菜单的父菜单ID用
		mv.addObject("MSG", "editMenu");
		mv.addObject("QX",Jurisdiction.getUserAuthList());	//按钮权限
		mv.setViewName("system/menu/menu_edit");
		return mv;
	}
	
	/**
	 * 保存编辑
	 * @param 
	 * @return
	 */
	@RequestMapping(value="/editMenu")
	public ModelAndView edit(MenuDO menu) throws Exception{
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		menuService.editMenu(menu);
		//mv.setViewName("redirect:?MSG='change'&menuId="+menu.getParentId()); //保存成功跳转到列表页面
		String menuId = menu.getParentId();
		List<MenuDO> menuList = menuService.listSubMenuByParentId(menuId);
		mv.addObject("pd", menuService.getMenuById(menuId));	//传入父菜单所有信息
		mv.addObject("menuId", menuId);
		mv.addObject("MSG", "'change'"); //MSG=change 则为编辑或删除后跳转过来的
		mv.addObject("menuList", menuList);
		mv.addObject("QX",Jurisdiction.getUserAuthList());	//按钮权限
		mv.setViewName("system/menu/menu_list");
		return mv;
	}
	
	/**
	 * 请求编辑菜单图标页面
	 * @param 
	 * @return
	 */
	@RequestMapping(value="/toEditicon")
	public ModelAndView toEditicon(@RequestParam("menuId")String menuId) throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			pd.put("menuId",menuId);
			mv.addObject("pd", pd);
			mv.setViewName("system/menu/menu_icon");
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**
	 * 保存菜单图标 
	 * @param 
	 * @return
	 */
	@RequestMapping(value="/editicon")
	public ModelAndView editicon() throws Exception{
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			menuService.editMenuIcon(pd);
			mv.addObject("msg","success");
		} catch(Exception e){
			logger.error(e.toString(), e);
			mv.addObject("msg","failed");
		}
		mv.setViewName("save_result");
		return mv;
	}
	
	/**
	 * 显示菜单列表ztree(菜单管理)
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/listAllMenu")
	public ModelAndView listAllMenu(@RequestParam(value="menuId",required=false)String menuId) throws Exception{
		ModelAndView mv = this.getModelAndView();
		List<MenuDO> menuList = menuService.getAllMenuList("", true); //取出无授权但url已修改的总菜单, 用于z-tree显示		
		String json = menuService.menuTreeToJsonString(menuList);
		mv.addObject("zTreeNodes", json);
		mv.addObject("menuId", menuId);
		mv.setViewName("system/menu/menu_ztree");
		return mv;
	}
	
	/**
	 * 显示扩展菜单列表ztree(拓展左侧四级菜单)
	 * @param model
	 * @return zTreeNodes-用于z-tree显示的jason格式菜单节点
	 */
	@RequestMapping(value="/otherlistMenu")
	public ModelAndView otherlistMenu(@RequestParam("menuId")String menuId) throws Exception{
		ModelAndView mv = this.getModelAndView();
		Map<String,String> sMap = menuService.otherlistMenu(menuId);
		mv.addObject("zTreeNodes", sMap.get("zTreeNodes"));
		mv.addObject("menuUrl", sMap.get("menuUrl"));		//本ID菜单链接
		mv.setViewName("system/menu/menu_ztree_other");		
		return mv;
	}	
}
