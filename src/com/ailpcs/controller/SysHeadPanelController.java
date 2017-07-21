package com.ailpcs.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ailpcs.core.Jurisdiction;
import com.ailpcs.core.MyStartFilter;
import com.ailpcs.dao.Dao2;
import com.ailpcs.entity.core.PageData;
import com.ailpcs.service.SysAppUserService;
import com.ailpcs.service.SysHeadPanelService;
import com.ailpcs.util.AppUtil;
import com.ailpcs.util.SmsUtil;
import com.ailpcs.util.Tools;
import com.ailpcs.util.mail.SimpleMailSender;

/** 
 * 主画面顶部面板(头部)的操作接口, 包括修改头像、邮件、站内信、短信等功能
 */
@Controller
@RequestMapping(value="/headPanel")
public class SysHeadPanelController extends BaseController {	
	@Resource(name="sysAppUserService")
	private SysAppUserService appuserService;
	@Resource(name="sysWebHeadPanelService")
	private SysHeadPanelService sysWebHeadPanelService;
	@Resource(name = "dao2")
	private Dao2<PageData> daoPD;
	
	/**去编辑头像页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/editPhoto")
	public ModelAndView editPhoto() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("system/userphoto/userphoto_edit");
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**获取头部信息
	 * @return
	 */
	@RequestMapping(value="/fReeGetNecessaryList")
	@ResponseBody
	public Object getHeadPanelList(HttpServletRequest request) {
		return sysWebHeadPanelService.getHeadPanelList(request);
	}

	/**获取站内信未读总数
	 * @return
	 */
	@RequestMapping(value="/countInnerMailTotal")
	@ResponseBody
	public Object getInnerMailCount() {
		PageData pd = new PageData();
		Map<String,Object> map = sysWebHeadPanelService.getInnerMailCount();		
		return AppUtil.returnObject(pd, map);
	}
	
	/**去发送邮箱页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/editEmail")
	public ModelAndView editEmail() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("system/head/edit_email");
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**去发送短信页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/goSendSms")
	public ModelAndView goSendSms() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("system/head/send_sms");
		mv.addObject("pd", pd);
		return mv;
	}
	
	
	/**发送短信
	 * @return
	 */
	@RequestMapping(value="/sendSms")
	@ResponseBody
	public Object sendSms(){
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> map = new HashMap<String,Object>();
		String msg = "ok";		//发送状态
		int count = 0;			//统计发送成功条数
		int zcount = 0;			//理论条数
		List<PageData> pdList = new ArrayList<PageData>();
		String PHONEs = pd.getString("PHONE");					//对方邮箱
		String CONTENT = pd.getString("CONTENT");				//内容
		String isAll = pd.getString("isAll");					//是否发送给全体成员 yes or no
		String TYPE = pd.getString("TYPE");						//类型 1：短信接口1   2：短信接口2
		String fmsg = pd.getString("fmsg");						//判断是系统用户还是会员 "appuser"为会员用户
		if("yes".endsWith(isAll)){
			try {
				List<PageData> userList = new ArrayList<PageData>();
				userList = "appuser".equals(fmsg) ? appuserService.listAllUser(pd) : daoPD.listFromDb("UserMapper.listAllUser", pd);
				zcount = userList.size();
				try {
					for(int i=0;i<userList.size();i++){
						if(Tools.checkMobileNumber(userList.get(i).getString("PHONE"))){			//手机号格式不对就跳过
							if("1".equals(TYPE)){
								SmsUtil.sendSms1(userList.get(i).getString("PHONE"), CONTENT);		//调用发短信函数1
							}else{
								SmsUtil.sendSms2(userList.get(i).getString("PHONE"), CONTENT);		//调用发短信函数2
							}
							count++;
						}else{
							continue;
						}
					}
					msg = "ok";
				} catch (Exception e) {
					msg = "error";
				}
			} catch (Exception e) {
				msg = "error";
			}
		}else{
			PHONEs = PHONEs.replaceAll("；", ";");
			PHONEs = PHONEs.replaceAll(" ", "");
			String[] arrTITLE = PHONEs.split(";");
			zcount = arrTITLE.length;
			try {
				for(int i=0;i<arrTITLE.length;i++){
					if(Tools.checkMobileNumber(arrTITLE[i])){			//手机号式不对就跳过
						if("1".equals(TYPE)){
							SmsUtil.sendSms1(arrTITLE[i], CONTENT);		//调用发短信函数1
						}else{
							SmsUtil.sendSms2(arrTITLE[i], CONTENT);		//调用发短信函数2
						}
						count++;
					}else{
						continue;
					}
				}
				msg = "ok";
			} catch (Exception e) {
				msg = "error";
			} 
		}	
		pd.put("msg", msg);
		pd.put("count", count);						//成功数
		pd.put("ecount", zcount-count);				//失败数
		pdList.add(pd);
		map.put("list", pdList);
		return AppUtil.returnObject(pd, map);
	}
	
	/**去发送电子邮件页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/goSendEmail")
	public ModelAndView goSendEmail() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("system/head/send_email");
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**
	 * 发送电子邮件
	 */
	@RequestMapping(value="/sendEmail")
	@ResponseBody
	public Object sendEmail(){
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> map = new HashMap<String,Object>();
		String msg = "ok";		//发送状态
		int count = 0;			//统计发送成功条数
		int zcount = 0;			//理论条数
				
		//String strEMAIL = Tools.readTxtFile(Const.EMAIL);		//读取邮件配置
		List<PageData> pdList = new ArrayList<PageData>();
		String toEMAIL = pd.getString("EMAIL");					//对方邮箱
		String TITLE = pd.getString("TITLE");					//标题
		String CONTENT = pd.getString("CONTENT");				//内容
		String TYPE = pd.getString("TYPE");						//类型
		String isAll = pd.getString("isAll");					//是否发送给全体成员 yes or no
		String fmsg = pd.getString("fmsg");						//判断是系统用户还是会员 "appuser"为会员用户
		
		if("yes".endsWith(isAll)){
			try {
				List<PageData> userList = new ArrayList<PageData>();
				userList = "appuser".equals(fmsg) ? appuserService.listAllUser(pd) : daoPD.listFromDb("UserMapper.listAllUser", pd);
				zcount = userList.size();
				try {
					for(int i = 0; i < userList.size(); i++){
						if(Tools.checkEmail(userList.get(i).getString("EMAIL"))){		//邮箱格式不对就跳过
							SimpleMailSender.sendEmail(MyStartFilter.sp.get("SMTP"), MyStartFilter.sp.get("PORT"), 
									MyStartFilter.sp.get("EMAIL"), MyStartFilter.sp.get("PAW"), userList.get(i).getString("EMAIL"), 
									TITLE, CONTENT, TYPE);//调用发送邮件函数
							count++;
						}else{
							continue;
						}
					}
					msg = "ok";
				} catch (Exception e) {
					msg = "error";
				}
			} catch (Exception e) {
				msg = "error";
			}
		}else{
			toEMAIL = toEMAIL.replaceAll("；", ";");
			toEMAIL = toEMAIL.replaceAll(" ", "");
			String[] arrTITLE = toEMAIL.split(";");
			zcount = arrTITLE.length;
			try {
				for(int i=0;i<arrTITLE.length;i++){
					if(Tools.checkEmail(arrTITLE[i])){		//邮箱格式不对就跳过
						SimpleMailSender.sendEmail(MyStartFilter.sp.get("SMTP"), MyStartFilter.sp.get("PORT"), 
								MyStartFilter.sp.get("EMAIL"), MyStartFilter.sp.get("PAW"), arrTITLE[i], TITLE, 
								CONTENT, TYPE);//调用发送邮件函数
						count++;
					}else{
						continue;
					}
				}
				msg = "ok";
			} catch (Exception e) {
				msg = "error";
			} 
		}	
			
		pd.put("msg", msg);
		pd.put("count", count);						//成功数
		pd.put("ecount", zcount-count);				//失败数
		pdList.add(pd);
		map.put("list", pdList);
		return AppUtil.returnObject(pd, map);
	}
	
	/**
	 * 去系统设置页面
	 */
	@RequestMapping(value="/goSystem")
	public ModelAndView goEditEmail() throws Exception{
		if(!"admin".equals(Jurisdiction.getUsername())) {  //只有admin能操作
			return null;
		}	
		ModelAndView mv = this.getModelAndView();
		PageData pd = sysWebHeadPanelService.catchAllSysParameter();		
		mv.setViewName("system/head/sys_edit");
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**
	 * 保存系统设置1
	 */
	@RequestMapping(value="/saveSysMenu1")
	public ModelAndView saveSys() throws Exception{
		if(!"admin".equals(Jurisdiction.getUsername())) { //非admin用户不能修改
			return null;
		}	
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		sysWebHeadPanelService.saveSysParameterSetting(pd, 1);		
		mv.addObject("msg","OK");
		mv.setViewName("save_result");
		return mv;
	}
	
	/**
	 * 保存系统设置2
	 */
	@RequestMapping(value="/saveSysMenu2")
	public ModelAndView saveSys2() throws Exception{
		if(!"admin".equals(Jurisdiction.getUsername())) { //非admin用户不能修改
			return null;
		}	
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		sysWebHeadPanelService.saveSysParameterSetting(pd, 2);	
		mv.addObject("msg","OK");
		mv.setViewName("save_result");
		return mv;
	}
	
	/**保存系统设置3
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/saveSysMenu3")
	public ModelAndView saveSys3() throws Exception{
		if(!"admin".equals(Jurisdiction.getUsername())) { //非admin用户不能修改
			return null;
		}	
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		sysWebHeadPanelService.saveSysParameterSetting(pd, 3);	
		mv.addObject("msg","OK");
		mv.setViewName("save_result");
		return mv;
	}
	
	/**
	 * 保存系统设置4
	 */
	@RequestMapping(value="/saveSysMenu4")
	public ModelAndView saveSys4() throws Exception {
		if(!"admin".equals(Jurisdiction.getUsername())) {  //只有 admin 一人能修改!
			return null;
		}	
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		sysWebHeadPanelService.saveSysParameterSetting(pd, 4);	
		mv.addObject("msg","OK");
		mv.setViewName("save_result");
		return mv;
	}	
}


