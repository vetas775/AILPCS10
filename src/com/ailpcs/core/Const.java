package com.ailpcs.core;


import java.util.List;

import org.springframework.context.ApplicationContext;

import com.ailpcs.entity.core.MenuDO;
/**
 * 系统常量定义
 */
public class Const {
	public static final String SESSION_SECURITY_CODE = "sessionSecCode";	//验证码
	public static final String SESSION_USER = "sessionUser";				//session用的用户
	public static final String SESSION_ROLE_RIGHTS = "sessionRoleRights";
	public static final String sSESSION_ROLE_RIGHTS = "sessionRoleRights";
	public static final String SESSION_menuList = "menuList";				//当前菜单
	public static final String SESSION_allmenuList = "allmenuList";			//全部菜单
	public static final String SESSION_QX = "QX";
	////public static final String SESSION_userpds = "userpds";			
	////public static final String SESSION_USERROL = "USERROL";					//用户对象
	public static final String SESSION_USERNAME = "USERNAME";				//用户名
	public static final String DEPARTMENT_IDS = "DEPARTMENT_IDS";			//当前用户拥有的最高部门权限集合
	public static final String DEPARTMENT_ID = "DEPARTMENT_ID";				//当前用户拥有的最高部门权限
	public static final String TRUE = "T";
	public static final String FALSE = "F";
	public static final String LOGIN = "/fReeLogInJumpToLogin.do";		//登录地址
	
	public static final String SYSNAME = "admin/config/SYSNAME.txt";		//系统名称路径
	public static final String LOGINEDIT = "admin/config/LOGIN.txt";		//登录页面配置
	public static final String PAGE	= "admin/config/PAGE.txt";				//分页条数配置路径
	public static final String EMAIL = "admin/config/EMAIL.txt";			//邮箱服务器配置路径
	public static final String SMS1 = "admin/config/SMS1.txt";				//短信账户配置路径1
	public static final String SMS2 = "admin/config/SMS2.txt";				//短信账户配置路径2
	public static final String FWATERM = "admin/config/FWATERM.txt";		//文字水印配置路径
	public static final String IWATERM = "admin/config/IWATERM.txt";		//图片水印配置路径
	public static final String WEIXIN	= "admin/config/WEIXIN.txt";		//微信配置路径
	public static final String WEBSOCKET = "admin/config/WEBSOCKET.txt";	//WEBSOCKET配置路径
	
	
	public static final String FILEPATHIMG = "uploadFiles/uploadImgs/";		//图片上传路径
	public static final String FILEPATHFILE = "uploadFiles/file/";			//文件上传路径
	public static final String FILEPATHFILEOA = "uploadFiles/uploadFile/";	//文件上传路径(oa管理)
	public static final String FILEPATHTWODIMENSIONCODE = "uploadFiles/twoDimensionCode/"; //二维码存放路径
	
	//(不对匹配该值的访问路径拦截做菜单操作权限、按钮操作权限检查（正则）)
	public static final String NO_INTERCEPTOR_PATH = ".*/((app)|(weixin)|(static)|(plugins)|(fRee)|(websocket)|(uploadImgs)).*";	
	//只需再次确认用户已登录(Shiro那边已经检查过一次), 无需检查具体的菜单、按钮操作权限的访问
	public static final String JUST_NEED_USER_LOGIN_PATH = ".*/((uJnUL)|(XxXPpD)).*";	
	
	public static ApplicationContext WEB_APP_CONTEXT = null; //该值会在web容器启动时由 MyWebAppContextListener(web.xml里配置)初始化
	
	//系统总菜单 - 当前系统完整菜单的树状列表(有了此表,菜单数据从数据库一次读入,永久可用...)
	public static List<MenuDO> allMenuList; 
}
