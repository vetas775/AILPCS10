package com.ailpcs.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ailpcs.core.Jurisdiction;
import com.ailpcs.dao.Dao2;
import com.ailpcs.entity.core.PageData;
import com.ailpcs.entity.core.RoleDO;
import com.ailpcs.service.SysButtonRightsService;
import com.ailpcs.service.SysButtonService;
import com.ailpcs.util.AppUtil;
import com.ailpcs.util.LogUtil;
/** 
 * "系统管理"=>"权限管理"=>""按钮权限"接口 
 */
@Controller
@RequestMapping(value="/buttonrights")
public class SysButtonRightsController extends BaseController {
	private static final String menuUrl = "buttonrights/list.do"; //菜单地址(权限用)
	@Resource(name="buttonrightsService")
	private SysButtonRightsService buttonrightsService;
	@Resource(name="sysButtonService")
	private SysButtonService sysButtonService;
	@Resource(name = "dao2")
	private Dao2<PageData> daoPD;
	@Resource(name = "dao2")
	private Dao2<RoleDO> daoRole;
	
	/**列表
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public ModelAndView list() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String type = pd.getString("type");
		type = StringUtils.isBlank(type) ? "0" :type;
		if(StringUtils.isBlank(pd.getString("roleId"))){
			pd.put("roleId", "1");										//默认列出第一组角色(初始设计系统用户和会员组不能删除)
		}
		PageData fpd = new PageData();
		fpd.put("roleId", "0");
		List<RoleDO> roleList = daoRole.listFromDb("RoleMapper.listSubRolesByParentId", fpd);	//列出组(页面横向排列的一级组)
		List<RoleDO> roleList_z =  daoRole.listFromDb("RoleMapper.listSubRolesByParentId", pd);	//列出此组下级角色
		List<PageData> buttonlist = sysButtonService.listAll(pd);			//列出所有按钮NAME,	QX_NAME,BZ,FHBUTTON_ID
		List<PageData> roleFhbuttonlist = buttonrightsService.listAll(pd);	//列出所有角色按钮关联数据 RB_ID,ROLE_ID,BUTTON_ID
		pd = daoPD.getFromDb("RoleMapper.findObjectById", pd);				//取得点击的角色组(横排的)
		mv.addObject("pd", pd);
		mv.addObject("roleList", roleList);
		mv.addObject("roleList_z", roleList_z);
		mv.addObject("buttonlist", buttonlist);
		mv.addObject("roleFhbuttonlist", roleFhbuttonlist);
		mv.addObject("QX",Jurisdiction.getUserAuthList());	//按钮权限
		if("2".equals(type)){
			mv.setViewName("system/buttonrights/buttonrights_list_r");
		}else{
			mv.setViewName("system/buttonrights/buttonrights_list");
		}		
		return mv;
	}
	
	/**
	 * 点击按钮处理关联表
	 */
	@RequestMapping(value="/upRb")
	@ResponseBody
	public Object updateRolebuttonrightd() throws Exception{
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) { //校验权限
			return null;
		} 
		Map<String, Object> map = new HashMap<String, Object>();
		PageData pd = new PageData();
		pd = this.getPageData();
		String errInfo = "success";
		if(null != buttonrightsService.findById(pd)){	//判断关联表是否有数据 是:删除/否:新增  ROLE_ID, BUTTON_ID
			buttonrightsService.delete(pd);		//删除
			LogUtil.saveLog2Db(daoPD, Jurisdiction.getUsername(), "删除按钮权限"+pd);
		}else{
			pd.put("RB_ID", this.get32UUID());	//主键
			buttonrightsService.save(pd);		//新增  RB_ID,ROLE_ID,BUTTON_ID
			LogUtil.saveLog2Db(daoPD, Jurisdiction.getUsername(), "新增按钮权限"+pd);
		}
		map.put("result", errInfo);
		return AppUtil.returnObject(new PageData(), map);
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
