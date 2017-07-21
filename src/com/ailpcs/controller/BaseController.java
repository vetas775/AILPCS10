package com.ailpcs.controller;


import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ailpcs.entity.core.Page;
import com.ailpcs.entity.core.Page2;
import com.ailpcs.entity.core.PageData;
import com.ailpcs.util.DateHelper;
import com.ailpcs.util.UuidUtil;

public class BaseController {
	protected Logger logger = Logger.getLogger(this.getClass());

	/** 
	 * 调用PageData带参数的构造方法new 一个PageData对象, 
	 * 里面封装了request参数
	 * @return return new PageData(this.getRequest());
	 */
	public PageData getPageData(){
		return new PageData(this.getRequest());
	}
	
	/**得到ModelAndView
	 * @return
	 */
	public ModelAndView getModelAndView(){
		return new ModelAndView();
	}
	
	/**得到request对象
	 * @return
	 */
	public HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		return request;
	}

	/**得到32位的uuid
	 * @return
	 */
	public String get32UUID(){
		return UuidUtil.get32UUID();
	}
	
	/**得到分页列表的信息
	 * @return
	 */
	public Page getPage(){
		return new Page();
	}
	
	public static void logBefore(Logger logger, String interfaceName){
		logger.info("");
		logger.info("start");
		logger.info(interfaceName);
	}
	
	public static void logAfter(Logger logger){
		logger.info("end");
		logger.info("");
	}
	
	/**
	 * 列表查询条件预处理: keywords、dateField、dateFm、dateTo、dateRange
	 */
	public Page2 prepareQueryCondition(Page2 page2){
		PageData pd = new PageData(this.getRequest());
		PageData pdNew = new PageData(); 
		String keywords = pd.getString("keywords");	//关键词检索条件
		if(StringUtils.isNotBlank(keywords)){
			pdNew.put("keywords", keywords.trim());
		}
		String dateField = pd.getString("dateField");	//查询日期类型
		if (StringUtils.isNotBlank(dateField)) {
			pdNew.put("dateField", dateField.trim());		    		
			String dateFm = pd.getString("dateFm");	//起止日期
		    if(StringUtils.isNotBlank(dateFm)){
			    pdNew.put("dateFm", dateFm + " 00:00:00");
		    }
		    String dateTo = pd.getString("dateTo");	
		    if(StringUtils.isNotBlank(dateTo)){
			    pdNew.put("dateTo", dateTo + " 23:59:59");
		    }
		    String dateRange = (String)pd.get("dateRange");	
		    if(StringUtils.isNotBlank(dateRange)){
				Date day = new Date();
				if ("day" == dateRange) {
					pdNew.put("dateFm", DateHelper.format(day,"yyyy-MM-dd"));
					//page2.setStartTime(DateHelper.format(day,"yyyy-MM-dd"));				
				} else if ("week" == dateRange) {
					pdNew.put("dateFm", DateHelper.format(DateHelper.getAfterMonth(-7, day),"yyyy-MM-dd"));
					pdNew.put("dateTo", DateHelper.format(day,"yyyy-MM-dd"));
					//page2.setEndTime(DateHelper.format(day,"yyyy-MM-dd"));
					//page2.setStartTime(DateHelper.format(DateHelper.getAfterMonth(-7, day),"yyyy-MM-dd"));			
				} else if ("month" == dateRange) {
					pdNew.put("dateFm",DateHelper.format(DateHelper.getNMonth(-1, day),"yyyy-MM-dd"));
					pdNew.put("dateTo", DateHelper.format(day,"yyyy-MM-dd"));
					//page2.setEndTime(DateHelper.format(day,"yyyy-MM-dd"));
					//page2.setStartTime(DateHelper.format(DateHelper.getNMonth(-1, day),"yyyy-MM-dd"));			
				}		
			}
		}
		page2.setPd(pdNew);	
		return page2;
	}
	
}
