package com.ailpcs.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.ailpcs.entity.core.PageData;

/**
 * Web登录服务-处理网页端用户登录、权限验证
 * @author MichaelTsui 17/06/21
 */
public interface SysLoginService {
	
	/**
	 * 读取并返回登录页面的配置参数
	 * 包括系统名称、是否开放注册、是否播放背景音乐、背景登录图片列表等
	 * (用于在跳转登录页面等处，获取并返回相关的系统登录设置参数)
	 * /fReeLogInJumpToLogin
	 * MichaelTsui 17/06/21
	 */
	PageData getLoginParameter(PageData pd);
	
	/**
	 * 处理用户登录
	 */
	Map<String,String> userLogin(HttpServletRequest request);
	
	/**
	 * 用户登录成功后, 设定权限、菜单操作等信息到session, 最后跳入系统主画面system/index/main.jsp
	 * 另外在主画面点击左上角“后台首页”按钮也会触发此操作, 刷新主画面.	 
	 * /fReeLayoutMain/{changeMenu} 
	 */
	ModelAndView enterOrRefreshBackground(String ceMenu);
	
	/**
	 * 抓取用户统计数据用于刷新首页
	 */
	PageData catchDefaultPage() throws Exception;
	
	/**
	 * 退出登录
	 */
	PageData userLogout(PageData pd) throws Exception;
}
