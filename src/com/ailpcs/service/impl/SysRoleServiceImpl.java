package com.ailpcs.service.impl;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.ailpcs.core.Jurisdiction;
import com.ailpcs.dao.Dao2;
import com.ailpcs.entity.core.MenuDO;
import com.ailpcs.entity.core.PageData;
import com.ailpcs.entity.core.RoleDO;
import com.ailpcs.service.SysMenuService;
import com.ailpcs.service.SysRoleService;
import com.ailpcs.util.LogUtil;
import com.ailpcs.util.RightsHelper;
import com.ailpcs.util.Tools;
import com.ailpcs.util.UuidUtil;

@Service("sysRoleService")
public class SysRoleServiceImpl extends BaseServiceImpl implements SysRoleService{
	@Resource(name = "dao2")
	private Dao2<PageData> daoPD;	
	@Resource(name = "dao2")
	private Dao2<RoleDO> daoRole;
	@Resource(name = "sysMenuService")
	private SysMenuService sysMenuService;
	
	@Override
	public ModelAndView listRole(PageData pd) {
		ModelAndView mv = new ModelAndView();
		try{
			PageData sPD1 = new PageData();
			sPD1.put("roleId", "0");
			//列出三个一级角色(页面横向排列的一级组, 他们的parentId都是0)
			List<RoleDO> roleList = (List<RoleDO>) daoRole.listFromDb("RoleMapper.listSubRolesByParentId", sPD1);
			//列出此组下级角色				
			List<RoleDO> roleList_z = (List<RoleDO>) daoRole.listFromDb("RoleMapper.listSubRolesByParentId", pd);
			//取得被选中的一级角色(横排的)
			pd = daoPD.getFromDb("RoleMapper.findNameById", pd);
			mv.addObject("pd", pd);
			mv.addObject("roleList", roleList);
			mv.addObject("roleList_z", roleList_z);
			Map<String,String> sQX = Jurisdiction.getUserAuthList();
			mv.addObject("QX", sQX);	//按钮权限
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}	
	@Override
	public String saveNewRole(PageData pd) {		
		String sRes = "success"; 
		try{
			String sParentId = pd.getString("parentId");		//父类角色id
			pd.put("roleId", sParentId);			
			if("0".equals(sParentId)){  //一级角色默认菜单权限为""
				pd.put("roleRights", "");							
			}else{                      //其他角色默认菜单权限同父角色的菜单权限
				String rights = daoPD.getFromDb("RoleMapper.findObjectById", pd).getString("roleRights");
				pd.put("roleRights", (null == rights) ? "" : rights);	//组菜单权限
			}
			pd.put("roleId", UuidUtil.get32UUID());				//主键
			pd.put("addQx", "0");	//初始新增权限为否
			pd.put("delQx", "0");	//删除权限
			pd.put("editQx", "0");	//修改权限
			pd.put("chaQx", "0");	//查看权限
			daoPD.saveDb("RoleMapper.insertRole", pd);	
			LogUtil.saveLog2Db(daoPD, Jurisdiction.getUsername(), "新增角色:" + pd.getString("roleName")
					+ "(ID=" + pd.getString("roleId") + ")");
		} catch(Exception e){
			logger.error(e.toString(), e);
			sRes = "failed";
		}
		return sRes;
	}	
	@Override
	public String editRole(PageData pd) {
		String sRes = "success";
		try{
			daoPD.updateDb("RoleMapper.editRoleName", pd);	
			LogUtil.saveLog2Db(daoPD, Jurisdiction.getUsername(), "修改角色:" + pd.getString("roleName")
					+ "(ID=" + pd.getString("roleId") + ")");
		} catch(Exception e){
			logger.error(e.toString(), e);
			sRes = "failed";
		}
		return sRes;
	}	
	@Override
	public String deleteRole(String roleId, String roleName) {
		String sRes = "success";
		try{
			PageData pd = new PageData();
			pd.put("roleId", roleId);
			List<RoleDO> roleList_z = (List<RoleDO>) daoRole.listFromDb("RoleMapper.listSubRolesByParentId", pd); //列出此角色的所有下级
			if(roleList_z.size() > 0){
				sRes = "false";	//下级有数据时，删除失败
			}else{
				int sUserCount = daoPD.countFromDb("UserMapper.countUserByRoldId", roleId);		     //此角色下的用户总数
				int sAppUserCount = daoPD.countFromDb("AppuserMapper.countAppUserByRoldId", roleId); //此角色下的会员总数		

				if(sUserCount > 0 || sAppUserCount > 0){ //此角色已被使用就不能删除
					sRes = "false2";
				} else {
					daoRole.deleteDb2("RoleMapper.deleteRoleById", roleId); //执行删除
				    LogUtil.saveLog2Db(daoPD, Jurisdiction.getUsername(), "删除角色" + roleName + "(ID=" + roleId + ")");
				    sRes = "success";
				}
			}
		} catch(Exception e){
			logger.error(e.toString(), e);
		}		
		return sRes;
	}
	@Override
	public String listMenuqx(String roleId) {
		String json = "";
		try{
			RoleDO role = daoRole.getFromDb("RoleMapper.getRoleById", roleId);	//根据角色ID获取角色对象
			String roleRights = role.getRoleRights();		//取出本角色菜单使用权限
			List<MenuDO> menuList = sysMenuService.getAllMenuList(roleRights, false); //取出授角色使用权限的总菜单	
			json = sysMenuService.menuTreeToJsonString(menuList);	//把带了角色使用授权的菜单表转换为json字符串	, 用于前端z-tree菜单显示	
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return json;
	}	
	@Override
	public String saveMenuqx(String roleId, String menuIds) {		
		String sRes = "";
		PageData pd = new PageData();
		try{
			RoleDO role = new RoleDO();
			role.setRoleId(roleId);
			if(StringUtils.isNotBlank(menuIds)) {
				BigInteger rights = RightsHelper.sumRights(Tools.str2StrArray(menuIds)); //用菜单ID做权处理
				role.setRoleRights(rights.toString());				
				pd.put("roleRights",rights.toString());
			}else{
				role.setRoleRights("");				
				pd.put("roleRights","");
			}
			daoRole.updateDb("RoleMapper.updateRoleRights", role);
			
			pd.put("roleId", roleId);
			if(!"1".equals(roleId)){	//当修改admin权限时,不修改其它角色权限
				daoRole.updateDb("RoleMapper.setAllRights", pd);  //更新此角色所有子角色的菜单权限
			}
			sRes = "success";
			LogUtil.saveLog2Db(daoPD, Jurisdiction.getUsername(), "修改角色(ID=" + roleId + ")菜单使用权限.");
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return sRes;
	}	
	@Override
	public String listB4Button(String roleId, String msg) {
		String json = "";
		try{
			RoleDO role = daoRole.getFromDb("RoleMapper.getRoleById", roleId);	//根据角色ID获取角色对象
			String roleRights = "";  
			if("add_qx".equals(msg)){   //取出本角色菜单增/删/改/查权限
				roleRights = role.getAddQx();	//取角色里的新增权限
			}else if("del_qx".equals(msg)){
				roleRights = role.getDelQx();	//取角色里的删除权限
			}else if("edit_qx".equals(msg)){
				roleRights = role.getEditQx();	//取角色里的修改权限
			}else if("cha_qx".equals(msg)){
				roleRights = role.getChaQx();	//取角色里的查看权限
			}
			List<MenuDO> menuList = sysMenuService.getAllMenuList(roleRights, false); //取出授角色msg权的总菜单	
			json = sysMenuService.menuTreeToJsonString(menuList);  //把带了角色增/删/改/查授权的菜单表转换为json字符串, 用于前端z-tree菜单显示				
		} catch(Exception e){
			logger.error(e.toString(), e);
		}		
		return json;
	}		
	@Override
	public String saveB4Button(String roleId, String menuIds, String msg) {
		String sRes = "";
		PageData pd = new PageData();
		try{
			if(StringUtils.isNotBlank(menuIds)) {
				BigInteger rights = RightsHelper.sumRights(Tools.str2StrArray(menuIds)); //用菜单ID做权处理
				pd.put("value",rights.toString());
			}else{
				pd.put("value","");
			}
			pd.put("roleId", roleId);
			daoRole.updateDb("RoleMapper." + msg, pd);  
			sRes = "success";
		    LogUtil.saveLog2Db(daoPD, Jurisdiction.getUsername(), "修改角色" + "(ID=" + roleId + ")菜单"+msg+"权限.");
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return sRes;
	}

}
