package com.ailpcs.core;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;

import com.ailpcs.entity.core.MenuDO;
import com.ailpcs.util.RightsHelper;

/**
 * 权限处理
 */
public class Jurisdiction {
	private static Logger logger = Logger.getLogger(Jurisdiction.class);
	/**
	 * 校验User对菜单requestUrl有无访问权限, 并(在确认有访问权限后)设置User对此菜单的增删改查操作权限
	 * ("add","del","edit","cha")到Const.SESSION_QX.
	 * 如果没匹配菜单到就不处理(可能是接口链接或其它链接)
	 * @param requestUrl  菜单路径
	 */
	@SuppressWarnings("unchecked")
	public static boolean hasJurisdiction(String requestUrl){
		//判断是否拥有当前点击菜单的访问权限（内部过滤,防止通过url进入跳过菜单权限）
		/**
		 * 根据点击的菜单的xxx.do去和菜单中的URL去匹配，当匹配到了此菜单，判断是否有此菜单的访问权限，没有权限的话跳转到404页面,
		 * 确定对此菜单有访问权限后, 初始化菜单增删改查操作权限
		 * 如果没匹配菜单到就不处理(可能是接口链接或其它链接)
		 */
		String USERNAME = getUsername();	//获取当前登录者loginname
		Session session = getSession();
		List<MenuDO> menuList = (List<MenuDO>)session.getAttribute(USERNAME + Const.SESSION_allmenuList); //获取菜单列表
		boolean sCheckOK = readMenu(menuList, requestUrl, session, USERNAME);		
		return sCheckOK;
	}
	
	/**
	 * 校验菜单访问权限并(在确认有访问权限后)初始化User对此菜单的增删改查操作权限(递归处理)
	 * @param menuList:传入的总菜单(设置菜单时，.do前面的不要重复)
	 * @param menuUrl:访问地址
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean readMenu(List<MenuDO> menuList,String requestUrl,Session session,String USERNAME){
		for(int i=0;i<menuList.size();i++){
			String sStr1 = menuList.get(i).getMenuUrl();
			String sStr2 = requestUrl;
			if(StringUtils.isNotBlank(sStr1) && StringUtils.isNotBlank(sStr2) && 
					menuList.get(i).getMenuUrl().split(".do")[0].equals(requestUrl.split(".do")[0])){ //访问地址与菜单地址循环匹配，如何匹配到就进一步验证，如果没匹配到就不处理(可能是接口链接或其它链接)
				if(!menuList.get(i).isHasMenu()){	//判断有无此菜单权限
					logger.info("MyLoginHandlerInterceptor=>" + USERNAME + "访问" + requestUrl + ", 匹配到无权限的菜单项'"
				        + menuList.get(i).getMenuName() + "', 系统将跳转登录页面！");
					return false;
				}else{											
					//接着继续判断对这个菜单的增删改查按钮的操作权限， 并刷新到session里
					Map<String, String> map = (Map<String, String>)session.getAttribute(USERNAME + Const.SESSION_QX);//按钮权限(增删改查)
					map.remove("add");
					map.remove("del");
					map.remove("edit");
					map.remove("cha");
					String MENU_ID =  menuList.get(i).getMenuId();
					Boolean isAdmin = "admin".equals(USERNAME);
					map.put("add", (RightsHelper.testRights(map.get("adds"), MENU_ID)) || isAdmin?"1":"0");
					map.put("del", RightsHelper.testRights(map.get("dels"), MENU_ID) || isAdmin?"1":"0");
					map.put("edit", RightsHelper.testRights(map.get("edits"), MENU_ID) || isAdmin?"1":"0");
					map.put("cha", RightsHelper.testRights(map.get("chas"), MENU_ID) || isAdmin?"1":"0");
					session.removeAttribute(USERNAME + Const.SESSION_QX);
					session.setAttribute(USERNAME + Const.SESSION_QX, map);	//重新分配按钮权限
					return true;
				}
			}else{
				if(!readMenu(menuList.get(i).getSubMenu(), requestUrl, session, USERNAME)){//继续排查其子菜单
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 1. 首先检查传入的操作参数type是菜单增删改查权限还是功能按钮权限. 
	 * 判断依据:"add","del","edit","cha"是菜单增删改查权限, 否则为功能按钮权限 
	 * 2. 功能按钮权限检查Type是否匹配已授权的按钮功能(Datajur,userBinding,FromExcel,BTN0001,toExcel...)
	 * 3. 菜单增删改查权限先找菜单是否存在, 再核对User对该菜单对应增删改查是否有授权。 菜单不存在视为有授权。
	 */
	@SuppressWarnings("unchecked")
	public static boolean buttonJurisdiction(String menuUrl, String type){
		//菜单增删改查权限和功能按钮权限列表Const.SESSION_QX 格式样例:
		//{Datajur=1, userBinding=1, dels=0, edits=590295247407712034782, chas=2199023255554, FromExcel=1, BTN0001=1, toExcel=1, adds=}
		String USERNAME = getUsername();	//获取当前登录者loginname
		Session session = getSession();
		//菜单增删改查权限
		if (!"".equals(menuUrl) && ("add".equals(type) || "del".equals(type) || "edit".equals(type) || "cha".equals(type))) {
			//判断是否拥有当前点击菜单的权限（内部过滤,防止通过url进入跳过前端菜单权限检查）
			/**
			 * 根据点击的菜单的xxx.do去菜单中的URL去匹配，当匹配到了此菜单，判断是否有此菜单的权限，没有的话跳转到404页面
			 * 根据按钮权限，授权按钮(当前点的菜单和角色中各按钮的权限匹对)
			 */
			List<MenuDO> menuList = (List<MenuDO>)session.getAttribute(USERNAME + Const.SESSION_allmenuList); //获取菜单列表
		    return readMenuButton(menuList, menuUrl, session, USERNAME, type);
		} else {  //功能按钮权限
		    Map<String, String> map = (Map<String, String>)session.getAttribute(USERNAME + Const.SESSION_QX); //获取菜单增删改查权限和功能按钮权限列表
		    return map.containsKey(type); 
		}
	}
	
	/**校验按钮权限(递归处理)
	 * @param menuList:传入的总菜单(设置菜单时，.do前面的不要重复)
	 * @param menuUrl:访问地址
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean readMenuButton(List<MenuDO> menuList,String menuUrl,Session session,String USERNAME, String type){
		for(int i=0;i<menuList.size();i++){
			if(menuList.get(i).getMenuUrl().split(".do")[0].equals(menuUrl.split(".do")[0])){ //访问地址与菜单地址循环匹配，如何匹配到就进一步验证，如果没匹配到就不处理(可能是接口链接或其它链接)
				if(!menuList.get(i).isHasMenu()){				//判断有无此菜单权限
					return false;
				}else{											
					//判断用户的role对menuUrl(role.do)的type操作(add)是否有权限
					Map<String, String> map = (Map<String, String>)session.getAttribute(USERNAME + Const.SESSION_QX);//按钮权限(增删改查)
					String MENU_ID =  menuList.get(i).getMenuId();
					Boolean isAdmin = "admin".equals(USERNAME);
					if("add".equals(type)){
						return ((RightsHelper.testRights(map.get("adds"), MENU_ID)) || isAdmin);
					}else if("del".equals(type)){
						return ((RightsHelper.testRights(map.get("dels"), MENU_ID)) || isAdmin);
					}else if("edit".equals(type)){
						return ((RightsHelper.testRights(map.get("edits"), MENU_ID)) || isAdmin);
					}else if("cha".equals(type)){
						return ((RightsHelper.testRights(map.get("chas"), MENU_ID)) || isAdmin);
					}
				}
			}else{
				if(!readMenuButton(menuList.get(i).getSubMenu(),menuUrl,session,USERNAME,type)){//继续排查其子菜单
					return false;
				};
			}
		}
		return true;
	}
	
	/**
	 * 获取当前登录的用户名
	 */
	public static String getUsername(){
		return getSession().getAttribute(Const.SESSION_USERNAME).toString();
	}
	
	/**获取用户的最高组织机构权限集合
	 * @return
	 */
	public static String getDEPARTMENT_IDS(){
		return getSession().getAttribute(Const.DEPARTMENT_IDS).toString();
	}
	
	/**获取用户的最高组织机构权限
	 * @return
	 */
	public static String getDEPARTMENT_ID(){
		return getSession().getAttribute(Const.DEPARTMENT_ID).toString();
	}
	
	/**
	 * 返回当前User的菜单增删改查权限和功能按钮权限列表Const.SESSION_QX, 格式样例如下:
	 * {Datajur=1, userBinding=1, dels=0, edits=590295247407712034782, chas=2199023255554, FromExcel=1, BTN0001=1, toExcel=1, adds=}
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> getUserAuthList(){
		return (Map<String, String>)getSession().getAttribute(getUsername() + Const.SESSION_QX);
	}
	
	/**shiro管理的session
	 * @return
	 */
	public static Session getSession(){
		//Subject currentUser = SecurityUtils.getSubject();  
		return SecurityUtils.getSubject().getSession();
	}
}
