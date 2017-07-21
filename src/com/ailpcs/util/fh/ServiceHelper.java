package com.ailpcs.util.fh;

import com.ailpcs.core.Const;
import com.ailpcs.service.SysUserService;
import com.ailpcs.service.impl.SysMenuServiceImpl;
import com.ailpcs.service.impl.SysRoleServiceImpl;


/**
 * @author Administrator
 * 获取Spring容器中的service bean
 */
public final class ServiceHelper {
	
	public static Object getService(String serviceName){
		//WebApplicationContextUtils.
		return Const.WEB_APP_CONTEXT.getBean(serviceName);
	}
	
	public static SysUserService getUserService(){
		return (SysUserService) getService("sysUserService");
	}
	
	public static SysRoleServiceImpl getRoleService(){
		return (SysRoleServiceImpl) getService("roleService");
	}
	
	public static SysMenuServiceImpl getMenuService(){
		return (SysMenuServiceImpl) getService("menuService");
	}
}
