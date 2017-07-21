package com.ailpcs.controller;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ailpcs.core.Jurisdiction;
import com.ailpcs.dao.Dao2;
import com.ailpcs.entity.core.PageData;
import com.ailpcs.service.SysRoleService;
import com.ailpcs.util.AppUtil;
import com.ailpcs.util.LogUtil;
/**
 * 菜单: 角色管理的操作接口
 * @author TSUI98
 *
 */
@Controller
@RequestMapping(value="/role")
public class SysRoleController extends BaseController {	
	private static final String menuUrl = "role.do"; //菜单地址(权限用)
	@Resource(name="sysRoleService")
	private SysRoleService roleService;
	@Resource(name = "dao2")
	private Dao2<PageData> daoPD;
	
	/** 进入权限首页
	 * @param 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping
	public ModelAndView list() throws Exception{
		PageData pd = new PageData();
		pd = this.getPageData();
		if(StringUtils.isBlank(pd.getString("roleId"))){
			pd.put("roleId", "1");	//默认Focus在第一组角色上									
		}
		ModelAndView mv = roleService.listRole(pd);
		mv.setViewName("system/role/role_list");		
		return mv;
	}
	
	/**
	 * 去新增页面, 新增组或新增角色
	 * @param  parentId=0 新增组,  parentId>0新增角色
	 * @return VIEW: system/role/role_edit
	 *         MODEL: {parentId=xxx}
	 */
	@RequestMapping(value="/toAdd")
	public ModelAndView toAdd() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		mv.addObject("msg", "addNewRole");  
		mv.setViewName("system/role/role_edit");
		mv.addObject("pd", pd);		
		return mv;
	}
	
	/**
	 * 保存新增角色
	 * @Parameter {roleId=xx, roleName=xx, parentId=xx}
	 * @return VIEW: save_result
	 *         MODEL: {msg=xxx}
	 */
	@RequestMapping(value="/addNewRole",method=RequestMethod.POST)
	public ModelAndView saveNewRole() throws Exception{
		PageData pd = this.getPageData();
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")) {  //校验对"role.do"是否有"add"权限
			LogUtil.saveLog2Db(daoPD, Jurisdiction.getUsername(), "新增角色:" + pd.getString("roleName") + "失败: 权限检查失败");
			logger.info(Jurisdiction.getUsername()+"新增角色:" + pd.getString("roleName") + "失败: 权限检查失败");
			return null;  //跳转到404页面
		} 
		String sRes = roleService.saveNewRole(pd);
		ModelAndView mv = new ModelAndView();
		mv.addObject("msg", sRes);
		mv.setViewName("save_result");
		return mv;
	}
	
	/**
	 * 请求编辑Role名
	 * @param  {roleId=xxx, roleName=xxx}
	 * @return VIEW: system/role/role_edit
	 *         MODEL: msg="editRole", pd={roleId=xxx, roleName=xxx}
	 */
	@RequestMapping(value="/toEditRole")
	public ModelAndView toEdit() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		mv.addObject("msg", "editRole");
		mv.addObject("pd", pd);
		mv.setViewName("system/role/role_edit");		
		return mv;
	}
	
	/**
	 * 保存修改Role名
	 */
	@RequestMapping(value="/editRole")
	public ModelAndView edit() throws Exception{
		PageData pd = this.getPageData();
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
			LogUtil.saveLog2Db(daoPD, Jurisdiction.getUsername(), "修改角色:" + pd.getString("roleName") + "名称失败: 权限检查失败");
			logger.info(Jurisdiction.getUsername()+"修改角色名称:" + pd.getString("roleName") + "失败: 权限检查失败");
			return null;  //跳转到404页面
		} //校验权限
		ModelAndView mv = this.getModelAndView();
		String sRes = roleService.editRole(pd);
		mv.addObject("msg", sRes);
		mv.setViewName("save_result");
		return mv;
	}
	
	/**删除角色
	 * @param roleId
	 * @return {result=xxx}
	 */
	@RequestMapping(value="/deleteRole")
	@ResponseBody
	public Object deleteRole(@RequestParam("roleId") String roleId, @RequestParam("roleName") String roleName) throws Exception{
		PageData pd = this.getPageData();
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")) {  //校验权限
			LogUtil.saveLog2Db(daoPD, Jurisdiction.getUsername(), "删除角色:" + pd.getString("roleName") + "失败: 权限检查失败");
			logger.info(Jurisdiction.getUsername()+"删除角色:" + pd.getString("roleName") + "失败: 权限检查失败");
			return null;  //跳转到404页面
		}
		Map<String, Object> map = new HashMap<String, Object>();
		String errInfo = roleService.deleteRole(roleId, roleName); 		
		map.put("result", errInfo);
		return AppUtil.returnObject(new PageData(), map);
	}
	
	/**
	 * 显示菜单列表ztree(角色菜单授权)
	 * @param roleId
	 * @return VIEW: system/role/menuqx
	 *         MODEL: roleId="xxx", zTreeNodes={xxx}
	 */
	@RequestMapping(value="/menuqx")
	public ModelAndView listAllMenu(String roleId) throws Exception{
		ModelAndView mv = this.getModelAndView();
		String json = roleService.listMenuqx(roleId);
		mv.addObject("zTreeNodes", json);
		mv.addObject("roleId", roleId);
		mv.setViewName("system/role/menuqx");		
		return mv;
	}
	
	/**
	 * 保存角色菜单权限
	 * @param roleId 角色ID
	 * @param menuIds 菜单ID集合
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/saveMenuqx")
	public void saveMenuqx(@RequestParam("roleId") String roleId, @RequestParam("menuIds") String menuIds, PrintWriter out) throws Exception{
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){ //校验权限
		} 
		String sStr = roleService.saveMenuqx(roleId, menuIds);
		out.write(sStr);
		out.close();		
	}

	/**显示角色对菜单增删改查授权列表 ztree (角色对菜单增删改查授权)
	 * @param ROLE_ID： 角色ID
	 * @param msg： 区分增删改查
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/b4Button")
	public ModelAndView b4Button(@RequestParam("roleId") String roleId, @RequestParam("msg") String msg) throws Exception{
		ModelAndView mv = this.getModelAndView();
		String json = roleService.listB4Button(roleId, msg);
		mv.addObject("zTreeNodes",json);
		mv.addObject("roleId",roleId);
		mv.addObject("msg", msg);		
		mv.setViewName("system/role/b4Button");
		return mv;
	}	
	
	/**
	 * 保存角色对菜单增删改查权限
	 */
	@RequestMapping(value="/saveB4Button")
	public void saveB4Button(@RequestParam("roleId") String roleId, @RequestParam("menuIds") String menuIds, 
			@RequestParam("msg") String msg, PrintWriter out) throws Exception{
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){  //校验权限
		} 
		String sStr = roleService.saveB4Button(roleId, menuIds, msg);
		out.write(sStr);
		out.close();			
	}
	
}