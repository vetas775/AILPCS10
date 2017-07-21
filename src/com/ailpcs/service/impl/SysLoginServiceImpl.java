package com.ailpcs.service.impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.ailpcs.core.Const;
import com.ailpcs.core.Jurisdiction;
import com.ailpcs.core.MyStartFilter;
import com.ailpcs.dao.Dao2;
import com.ailpcs.entity.core.MenuDO;
import com.ailpcs.entity.core.PageData;
import com.ailpcs.entity.core.RoleDO;
import com.ailpcs.entity.core.UserDO;
import com.ailpcs.service.SysLoginService;
import com.ailpcs.service.SysMenuService;
import com.ailpcs.util.LogUtil;
import com.ailpcs.util.NetworkUtil;
import com.ailpcs.util.Tools;
import com.ailpcs.util.fh.DateUtil;

@Service("sysWebLoginService")
public class SysLoginServiceImpl extends BaseServiceImpl implements SysLoginService {
	@Resource(name = "dao2")
	private Dao2<PageData> daoPD;
	@Resource(name = "dao2")
	private Dao2<UserDO> daoUser;
	@Resource(name = "sysMenuService")
	private SysMenuService sysMenuService;
	//private SysMenuService sysMenuService = (SysMenuService)Const.WEB_APP_CONTEXT.getBean("sysMenuService"); 
	
	@Override
	public PageData getLoginParameter(PageData pd) {
		pd.put("SYSNAME", MyStartFilter.sp.get("SYSNAME")); 	//1.读取系统名称
		pd.put("regFlag", MyStartFilter.sp.get("regFlag"));	//2.读取登录页面配置-是否开启注册 yes-开启
		pd.put("isMusic", MyStartFilter.sp.get("isMusic"));  //2.读取登录页面配置-是否开启背景音乐 yes-开启
		/*Remark by MichaelTsui at 17/06/24
		try {
			List<PageData> listImg = daoPD.listFromDb("LogInImgMapper.listAll", pd);	//登录背景图片
			pd.put("listImg", listImg);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString(), e);
		}
		*/
		return pd;
	}
	
	/**
	 * 处理用户登录
	 * MichaelTsui 17/06/21
	 */
	@Override
	public Map<String,String> userLogin(HttpServletRequest request) {
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "";
		String userName = request.getParameter("key1");  //登录过来的用户名
		String userPwd = request.getParameter("key2");   //登录过来的密码
		String validCode = request.getParameter("key3"); //登录过来的校验码
		//String KEYDATA[] = pd.getString("KEYDATA").replaceAll("qq43645735645fh", "").replaceAll("QQ9576787686fh", "").split(",fh,");
		//if(null != KEYDATA && KEYDATA.length == 3){
		if (StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(userPwd)) {
			Session session = Jurisdiction.getSession();
			String sessionCode = (String)session.getAttribute(Const.SESSION_SECURITY_CODE);		//获取session中的验证码
			if(StringUtils.isBlank(validCode)){ //判断效验码
				errInfo = "nullcode"; 			//效验码为空
			}else{	
				String strUserLogInfo = "";
				if(Tools.notEmpty(sessionCode) && sessionCode.equalsIgnoreCase(validCode)){		//判断登录验证码
					//加密（md5+盐），返回一个32位的字符串小写----MD5加密方式
					//String salt = "("+USERNAME+")";
					//String md5Pwd = new Md5Hash(PASSWORD,salt).toString();
					String passwdSalt = new SimpleHash("SHA-1", userName, userPwd).toString();	//密码加密
					UserDO user = new UserDO();
					user.setUserName(userName);
					user.setUserPassword(passwdSalt);
					try {
						//SQL查询成功找到资料: user不为null且里面装载了正确的查询结果
						//SQL查询成功找不到资料: user为null
						//SQL查询出错: user不为null, 里面做条件设入的Name和Password仍有值, 其余字段为null. 并且这时候会触发error log记录, 同时前端登录失败保持在登录画面 
						user = daoUser.getFromDb("UserMapper.getUserInfo", user); //根据用户名和密码去读取用户信息
					} catch (Exception e) {
						logger.error(e.toString(), e);
					}
					if(user != null){ //账号密码验证通过...
						//更新最后登录时间和IP						
						UserDO sUser = new UserDO();
						sUser.setUserId(user.getUserId());
						sUser.setUserLastLogin(DateUtil.getTime().toString());
						String ip = NetworkUtil.getIpAddress(request);						
						sUser.setUserLastIp(ip);
						try {
							daoUser.updateDb("UserMapper.updateLastLogin", sUser);  
						} catch (Exception e) {
							logger.error(e.toString(), e);
						} 						
						
						this.removeSession(userName);//清缓存		
						session.setAttribute(Const.SESSION_USER, user);	//把当前登录的User(含Role)信息放session中, 系统后面很多地方都要取用此信息
						RoleDO role = user.getRole();													//获取用户角色
						String roleRights = (role != null) ? role.getRoleRights() : "";				    //角色权限(菜单权限)
						session.setAttribute(userName + Const.SESSION_ROLE_RIGHTS, roleRights); 	//将角色权限单独存session
						strUserLogInfo = "角色=" + role.getRoleName() + " 权限="+role.getRoleRights();						
						session.setAttribute(Const.SESSION_USERNAME, userName);						//用户名单独存session
						
						//shiro加入身份验证
						Subject subject = SecurityUtils.getSubject(); 
					    UsernamePasswordToken token = new UsernamePasswordToken(userName, userPwd); 
					    try { 
					        subject.login(token); 
					    } catch (AuthenticationException e) { 
					    	errInfo = "身份验证失败！";
					    }
					}else{
						errInfo = "usererror"; 				//用户名或密码有误
						logBefore(logger, userName+"登录系统密码或用户名错误");
						LogUtil.saveLog2Db(daoPD, userName, "登录系统密码或用户名错误");
					}
				}else{
					errInfo = "codeerror";				 	//验证码输入有误
				}
				if(Tools.isEmpty(errInfo)){
					errInfo = "success";					//验证成功
					logBefore(logger, userName+"登录系统: " + strUserLogInfo); 
					LogUtil.saveLog2Db(daoPD, userName, "登录系统: " + strUserLogInfo);
				}				
			}
		}else{
			errInfo = "error";	//缺少参数
		}
		map.put("result", errInfo);
		return map;		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public ModelAndView enterOrRefreshBackground(String ceMenu) {
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		//pd = this.getPageData();
		try{
			Session session = Jurisdiction.getSession();
			UserDO user = (UserDO)session.getAttribute(Const.SESSION_USER);	//读取session中的用户信息(含角色信息), 登录成功时存入
			if (user != null) {
				String userName = (String)session.getAttribute(Const.SESSION_USERNAME);						//用户名
				String roleRights = (String)session.getAttribute(userName + Const.SESSION_ROLE_RIGHTS); 	//角色权限
				//菜单进session
				//List<Menu> allmenuList = new ArrayList<Menu>();
				//allmenuList = this.getAttributeMenu(session, userName, roleRights);			
				List<MenuDO> userMenuList = new ArrayList<MenuDO>();
				if(null == session.getAttribute(userName + Const.SESSION_allmenuList)) {
					userMenuList = sysMenuService.getAllMenuList(roleRights, false); //取出授角色使用权限的总菜单		
					session.setAttribute(userName + Const.SESSION_allmenuList, userMenuList);//菜单权限放入session中
				}else{
					userMenuList = (List<MenuDO>)session.getAttribute(userName + Const.SESSION_allmenuList); //从session中取出可操作菜单
				}	
				
				List<MenuDO> menuList = new ArrayList<MenuDO>();
				menuList = this.changeMenuF(userMenuList, session, userName, ceMenu);	//切换菜单 ceMenu=index
				
				if(null == session.getAttribute(userName + Const.SESSION_QX)){
					session.setAttribute(userName + Const.SESSION_QX, this.getUQX(userName)); //用户的按钮权限Map<String,String>格式放进session中
				}

				mv.setViewName("system/index/main");
				mv.addObject("user", user);
				mv.addObject("menuList", menuList);  //<====在主画面的左边显示的主菜单
			}else {
				mv.setViewName("system/index/login");//session失效后跳转登录页面
			}
		} catch(Exception e){
			mv.setViewName("system/index/login");
			logger.error(e.getMessage(), e);
		}
		pd.put("SYSNAME", MyStartFilter.sp.get("SYSNAME")); 	//1. 读取系统名称
		mv.addObject("pd",pd);
		
		return mv;
	}
	
	/**
	 * 清除session缓存
	 */
	private void removeSession(String USERNAME){
		Session session = Jurisdiction.getSession();	
		session.removeAttribute(Const.SESSION_USER);
		session.removeAttribute(USERNAME + Const.SESSION_ROLE_RIGHTS);
		session.removeAttribute(USERNAME + Const.SESSION_allmenuList); //全部菜单
		session.removeAttribute(USERNAME + Const.SESSION_menuList);
		session.removeAttribute(USERNAME + Const.SESSION_QX);
		////session.removeAttribute(Const.SESSION_userpds);
		session.removeAttribute(Const.SESSION_USERNAME);
		///session.removeAttribute(Const.SESSION_USERROL);
		session.removeAttribute("changeMenu");
		session.removeAttribute("DEPARTMENT_IDS");
		session.removeAttribute("DEPARTMENT_ID");
		session.removeAttribute(Const.SESSION_SECURITY_CODE);	//清除登录验证码的session
	}	
	
	/**
	 * 切换菜单: sys_menu.menu_type  1-系统菜单  2-业务菜单, 系统刚登录完成后显示业务菜单
	 */
	@SuppressWarnings("unchecked")
	private List<MenuDO> changeMenuF(List<MenuDO> allmenuList, Session session, String USERNAME, String changeMenu){
		List<MenuDO> menuList = new ArrayList<MenuDO>();
		if(null == session.getAttribute(USERNAME + Const.SESSION_menuList) || ("yes".equals(changeMenu))){
			List<MenuDO> menuList1 = new ArrayList<MenuDO>();
			List<MenuDO> menuList2 = new ArrayList<MenuDO>();
			for(int i = 0;i < allmenuList.size(); i++) { //拆分菜单-系统菜单和业务菜单
				MenuDO menu = allmenuList.get(i);
				if("1".equals(menu.getMenuType())){ //Menu_Type = 1-系统菜单 2-业务菜单, 第一次默认先进入的是2-业务菜单
					menuList1.add(menu);
				}else{
					menuList2.add(menu);
				}
			}
			session.removeAttribute(USERNAME + Const.SESSION_menuList);
			if("2".equals(session.getAttribute("changeMenu"))){
				session.setAttribute(USERNAME + Const.SESSION_menuList, menuList1);
				session.removeAttribute("changeMenu");
				session.setAttribute("changeMenu", "1");
				menuList = menuList1;
			}else{
				session.setAttribute(USERNAME + Const.SESSION_menuList, menuList2);
				session.removeAttribute("changeMenu");
				session.setAttribute("changeMenu", "2");
				menuList = menuList2;
			}
		} else {
			menuList = (List<MenuDO>)session.getAttribute(USERNAME + Const.SESSION_menuList);
		}
		return menuList;
	}
	
	/**
	 * 获取某用户的按钮操作权限
	 * @param userName
	 * @return
	 */
	private Map<String, String> getUQX(String userName){
		PageData pd = new PageData();
		Map<String, String> map = new HashMap<String, String>();
		Session session = Jurisdiction.getSession();
		UserDO user = (UserDO)session.getAttribute(Const.SESSION_USER);
		try {
			//pd.put(Const.SESSION_USERNAME, userName);
			pd.put("ROLE_ID", user.getUserRoleId());//获取角色ID
			//select ROLE_ID,ROLE_NAME,RIGHTS,PARENT_ID,ADD_QX,DEL_QX,EDIT_QX,CHA_QX from SYS_ROLE where ROLE_ID = #{ROLE_ID}
			//pd = daoPD.getFromDb("RoleMapper.findObjectById", pd); //获取角色信息															
			//map.put("adds", pd.getString("ADD_QX"));	//增
			//map.put("dels", pd.getString("DEL_QX"));	//删
			//map.put("edits", pd.getString("EDIT_QX"));//改
			//map.put("chas", pd.getString("CHA_QX"));	//查
			RoleDO role = user.getRole();	//获取此用户的role	
			map.put("adds", role.getAddQx());	//增 - sys_role.ADD_QX: 这个role可以对哪些菜单做Insert的动作, 例: 590295247407712034782
			map.put("dels", role.getDelQx());	//删 - sys_role.DEL_QX: 这个role可以对哪些菜单做Delete的动作
			map.put("edits", role.getEditQx());	//改 - sys_role.EDIT_QX: 这个role可以对哪些菜单做Update的动作
			map.put("chas", role.getChaQx());	//查 - sys_role.CHA_QX: 这个role可以对哪些菜单做Select的动作
			
			//列出这个role对SYS_FHBUTTON里7个按钮的操作权限, 如果是admin则默认权限全给。
			List<PageData> buttonQXnamelist = new ArrayList<PageData>();
			if("admin".equals(userName)){
				//select NAME,QX_NAME,BZ,FHBUTTON_ID from SYS_FHBUTTON
				buttonQXnamelist = (List<PageData>)daoPD.listFromDb("FhbuttonMapper.listAll", pd);	//admin用户拥有所有按钮权限 
			}else{
				//select QX_NAME from SYS_ROLE_FHBUTTON a left join SYS_FHBUTTON b on a.BUTTON_ID = b.FHBUTTON_ID where a.ROLE_ID = #{ROLE_ID}  
				buttonQXnamelist = (List<PageData>)daoPD.listFromDb("ButtonrightsMapper.listAllBrAndQxname", pd); //此角色拥有的按钮权限标识列表
			}
			for(int i=0;i<buttonQXnamelist.size();i++){
				map.put(buttonQXnamelist.get(i).getString("QX_NAME"), "1");		//按钮权限
			}
		} catch (Exception e) {
			logger.error(e.toString(), e);
		} finally {
			logger.info(user.getUserName()+"权限:"+map.toString());
		}
		return map;
	}
	
	/*把用户的组织机构权限放到session里面
	 * @param session
	 * @param USERNAME
	 * @return
	 * @throws Exception 
	 
	private void setAttributeToAllDEPARTMENT_ID(Session session, String USERNAME) throws Exception{
		String DEPARTMENT_IDS = "0",DEPARTMENT_ID = "0";
		if(!"admin".equals(USERNAME)){
			PageData pd = (PageData)daoPD.getFromDb("DatajurMapper.getDEPARTMENT_IDS", USERNAME); //datajurService.getDEPARTMENT_IDS(USERNAME);
			DEPARTMENT_IDS = (null == pd) ? "无权" : pd.getString("DEPARTMENT_IDS");
			DEPARTMENT_ID = (null == pd) ?"无权" : pd.getString("DEPARTMENT_ID");
		}
		session.setAttribute(Const.DEPARTMENT_IDS, DEPARTMENT_IDS);	//把用户的组织机构权限集合放到session里面
		session.setAttribute(Const.DEPARTMENT_ID, DEPARTMENT_ID);	//把用户的最高组织机构权限放到session里面
	}
	*/
	
	@Override
	public PageData catchDefaultPage() throws Exception {
		PageData pd = new PageData();
		pd.put("userCount",  ((daoPD.countFromDb("UserMapper.getUserCount", Jurisdiction.getUsername())).intValue() - 1)+"");			//系统用户数
		pd.put("appUserCount",  daoPD.countFromDb("AppuserMapper.getAppUserCount", Jurisdiction.getUsername()).toString()); //会员数
		return pd;
	}
	
	@Override
	public PageData userLogout(PageData pd) throws Exception{		
		String userName = Jurisdiction.getUsername();	//当前登录的用户名
		logBefore(logger, userName+"退出系统");
		this.removeSession(userName);//清缓存
		//shiro销毁登录
		Subject subject = SecurityUtils.getSubject(); 
		subject.logout();
		pd = getLoginParameter(pd);		
		return pd;		
	}
	
	

}
