package com.ailpcs.controller;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ailpcs.core.Jurisdiction;
import com.ailpcs.entity.core.Page;
import com.ailpcs.entity.core.PageData;
import com.ailpcs.service.SysInnerMsgService;
import com.ailpcs.util.AppUtil;

/** 
 * 系统顶部面板右上角=>"站内信"接口
 */
@Controller
@RequestMapping(value="/fReeInnerMsg")
public class SysInnerMsgController extends BaseController {	
	@Resource(name="sysInnerMsgService")
	private SysInnerMsgService sysInnerMsgService;
	
	/**
	 * 用户站内信列表
	 */
	@RequestMapping(value="/listUserMsg")
	public ModelAndView listUserMsg(Page page) throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String, Object> resMap = sysInnerMsgService.listUserMsg(page, pd);
		mv.setViewName("system/fhsms/fhsms_list");
		mv.addObject("varList", resMap.get("varList"));
		mv.addObject("pd", resMap.get("pd"));
		mv.addObject("QX",Jurisdiction.getUserAuthList());				//按钮权限
		return mv;
	}
	
	/**
	 * 去查看站内信界面
	 */
	@RequestMapping(value="/goView")
	public ModelAndView goView()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		if("1".equals(pd.getString("TYPE")) && "2".equals(pd.getString("STATUS"))){ //在收信箱里面查看未读的站内信时去数据库改变未读状态为已读
			sysInnerMsgService.edit(pd);
		}
		pd = sysInnerMsgService.findById(pd);	//根据ID读取
		mv.setViewName("system/fhsms/fhsms_view");
		mv.addObject("pd", pd);
		return mv;
	}	
	
	/**
	 * 去发站内信界面
	 */
	@RequestMapping(value="/goAdd")
	public ModelAndView goAdd()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("system/fhsms/fhsms_edit");
		mv.addObject("msg", "save");
		mv.addObject("pd", pd);
		return mv;
	}	
	
	/**
	 * 发送站内信
	 */
	@RequestMapping(value="/sendInnerMessage")
	@ResponseBody
	public Object sendInnerMessage() throws Exception{
		if(!Jurisdiction.buttonJurisdiction("", "BTN0001")) {  //按钮BTN0001权限校验
			return null;
		} 
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> map = new HashMap<String,Object>();
		List<PageData> pdList = sysInnerMsgService.sendInnerMessage(pd); //存入发信
		map.put("list", pdList);
		return AppUtil.returnObject(pd, map);
	}
	
	/**
	 * 删除一封站内信
	 */
	@RequestMapping(value="/deleteOneMsg")
	public void delete(PrintWriter out) throws Exception{
		PageData pd = new PageData();
		pd = this.getPageData();
		sysInnerMsgService.delete(pd);
		out.write("success");
		out.close();
	}	 
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception{
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			sysInnerMsgService.deleteAll(ArrayDATA_IDS);
			pd.put("msg", "ok");
		}else{
			pd.put("msg", "no");
		}
		pdList.add(pd);
		map.put("list", pdList);
		return AppUtil.returnObject(pd, map);
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
