package com.ailpcs.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;






//import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ailpcs.core.Jurisdiction;
import com.ailpcs.dao.Dao2;
import com.ailpcs.entity.core.PageData;
import com.ailpcs.service.SysLoginService;
import com.ailpcs.util.LogUtil;

/**
 * 登录接口-系统总入口, 包含了对权限的验证、处理
 * Amend by MichaelTsui 17/06/21
 */
@Controller
public class SysLoginController extends BaseController {	
	@Resource(name = "dao2")
	private Dao2<PageData> daoPD;
	@Resource(name="sysWebLoginService")
	private SysLoginService SysWebLoginService;
	
	/**跳转登录页面接口, 这是整个系统第一个访问的页面，在index.jsp里使用jsp跳转: <jsp:forward page="/login_toLogin" />
	 */
	@RequestMapping(value="/fReeLogInJumpToLogin")
	public ModelAndView toLogin()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = SysWebLoginService.getLoginParameter(pd);		
		mv.setViewName("system/index/login");
		mv.addObject("pd",pd);
		return mv;
	}
	
	/**用户登录
	 */
	@RequestMapping(value="/fReeLogInUserLogin", produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object login(HttpServletRequest request) throws Exception {
		Map<String,String> map = SysWebLoginService.userLogin(request);		
		return map;
	}	
	
	/**1. 登录成功后访问系统首页:  /fReeLayoutMain/index
	 * 2. 在主画面点击左上角“后台首页”按钮回到登录后的系统首页 : /fReeLayoutMain/index
	 * 3. 点击左上角"后台首页"按钮上方四个小按钮最左边的切换菜单: /fReeLayoutMain/yes
	 */
	@RequestMapping(value="/fReeLayoutMain/{changeMenu}")
	public ModelAndView enterOrRefreshBackground(@PathVariable("changeMenu") String changeMenu) {
		ModelAndView mv = SysWebLoginService.enterOrRefreshBackground(changeMenu); 		
		return mv;
	}
	
	/**
	 * 进入tab标签
	 */
	@RequestMapping(value="/fReeTabOperation")
	public String tab(){
		return "system/index/tab";
	}
	
	/**
	 * 进入系统后的默认页面(首页图表) call from index/tab.jsp
	 */	
	@RequestMapping(value="/fReeShowTheFirstScreen")
	public ModelAndView defaultPagetest() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = SysWebLoginService.catchDefaultPage(); //new PageData();
		mv.addObject("pd",pd);
		mv.setViewName("system/index/default");
		return mv;
	}
	
	/**
	 * 用户注销
	 *  1. head.jsp里用户点击按钮退出登录  (<a href="staticLogInLogout.do"><i class="ace-icon fa fa-power-off"></i>退出登录</a>)
	 *  2. head.js里其他同名用户登录把当前的用户踢出登录, 此时的调用会带一个参数msg=2(window.location.href=locat+"/staticLogInLogout.do?tipsMessage="+msg;)	 
	 */
	@RequestMapping(value="/fReeLogInLogout")
	public ModelAndView logout() throws Exception {	
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("system/index/login");
		String userName = Jurisdiction.getUsername();	//当前登录的用户名
		PageData pd = this.getPageData(); //当此用户是被其他同名用户异地登录而被系统踢出时, 传进来的请求会带一个参数tipsMessage, 里面描述了被踢的原因, 此信息需传到login.jsp里显示...
		pd = SysWebLoginService.userLogout(pd);
		LogUtil.saveLog2Db(daoPD, userName, "退出");
		mv.addObject("pd",pd);
		return mv;		
	}
}
