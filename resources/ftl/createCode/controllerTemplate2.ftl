package com.${packageName}.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.${packageName}.entity.core.Page2;
import com.${packageName}.entity.core.PageData;
import com.${packageName}.entity.${objectName}DO;
import com.${packageName}.util.ObjectExcelView;
import com.${packageName}.util.DateHelper;
import com.${packageName}.core.MyCustomDateEditor;
import com.${packageName}.core.Jurisdiction;
import com.${packageName}.service.${objectName}Service;

/** 
 * ${objectName}控制层.  ${TITLE}
 * 创建人：MichaelTsui
 * 创建时间：${nowDate?string("yyyy-MM-dd")}
 * @version 1.0
 */
@Controller
@RequestMapping(value="/${objectNameLower}")
public class ${objectName}Controller extends BaseController {
	private static final String menuUrl = "${objectNameLower}/list${objectName}.do"; //菜单地址(权限用)
	@Resource(name="${objectNameLower}Service")
	private ${objectName}Service ${objectNameLower}Service;
	
	@InitBinder    
	public void initBinder(WebDataBinder binder) {
	    binder.registerCustomEditor(Date.class, new MyCustomDateEditor());
	}
		
	/**
	 *列表
	 */
	@RequestMapping(value="/list${objectName}")
	public ModelAndView list() throws Exception {
		logBefore(logger, Jurisdiction.getUsername()+"列表${objectName}");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		PageData pdNew = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");	//关键词检索条件
		if(StringUtils.isNotBlank(keywords)){
			pdNew.put("keywords", keywords.trim());
		}		
		String dateField = pd.getString("dateField");	//查询日期类型
		if (StringUtils.isNotBlank(dateField)) {
			pdNew.put("dateField", dateField.trim());
			String dateFm = pd.getString("dateFm");	//起止日期
		    String dateTo = pd.getString("dateTo");		
		    if(StringUtils.isNotBlank(dateFm)){
			    pdNew.put("dateFm", dateFm + " 00:00:00");
		    }
		    if(StringUtils.isNotBlank(dateTo)){
			    pdNew.put("dateTo", dateTo + " 23:59:59");
		    }
		} else {
			pdNew.put("dateField", "updateDate"); //设置缺省日期查询字段(这里日期项应按实际情况调整)
		}
		//设定前端日期查询字段列表的内容  (这里内容应按实际状况调整)
		//String listDateStr = "[{\"key\": \"insertDate\", \"value\": \"建档日期\"},{\"key\": \"updateDate\", \"value\": \"修改日期\"}]";
		List<Map<String, String>> listDate = new ArrayList<Map<String, String>>();
		Map<String, String> map3 = new HashMap<String, String>();
		map3.put("key", "insertDate");  
		map3.put("value", "建档日期"); 
		listDate.add(map3);
		Map<String, String> map4 = new HashMap<String, String>();
		map4.put("key", "updateDate");  
		map4.put("value", "修改日期"); 
		listDate.add(map4);
		
		mv.setViewName("${packageName}/${objectNameLower}List");
		mv.addObject("pd", pdNew);
		mv.addObject("listDate", listDate);
		mv.addObject("QX",Jurisdiction.getUserAuthList());	
		return mv;
	}
	
	@RequestMapping(value="/lis2${objectName}")
	@ResponseBody
	public Object catchData${objectName}(Page2 page2) throws Exception {
		page2 = this.prepareQueryCondition(page2);
		Map<String , Object> result = ${objectNameLower}Service.selectPageList${objectName}(page2);
		result.put("pageSize",page2.getRows());
		//result.put("QX",Jurisdiction.getUserAuthList());
		//return JSON.toJSON(result);
		return result;
	}
	
	/**
	 * 去新增页面
	 */
	@RequestMapping(value="/goAdd${objectName}")
	public ModelAndView goAdd${objectName}() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("${packageName}/${objectNameLower}Detail");
		mv.addObject("msg", "save${objectName}");
		mv.addObject("pd", pd);
		return mv;
	}	
	
	/**
	 * 新增保存
	 */
	@RequestMapping(value="/save${objectName}")
	public ModelAndView save${objectName}(${objectName}DO pd${objectName}) throws Exception {
		logBefore(logger, Jurisdiction.getUsername()+"新增${objectName}");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")) {  //校验权限
		    return null;
		} 
		${objectNameLower}Service.save${objectName}(pd${objectName});
		ModelAndView mv = this.getModelAndView();
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(value="/delete${objectName}")
	public void delete${objectName}(PrintWriter out, @RequestParam("${camelPk}")String ${camelPk}) throws Exception {
		logBefore(logger, Jurisdiction.getUsername()+"删除${objectName}");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")) { //校验权限
		    return;
		} 
		<#if parameterTypePK == 'Long'>
		${objectNameLower}Service.delete${objectName}(Long.valueOf(${camelPk}));
		<#else>
		${objectNameLower}Service.delete${objectName}(${camelPk});
		</#if>
		out.write("success");
		out.close();
	}
	/**
	 * 去修改页面
	 */
	@RequestMapping(value="/goEdit${objectName}")
	public ModelAndView goEdit${objectName}(@RequestParam("${camelPk}")String ${camelPk}) throws Exception {
		ModelAndView mv = this.getModelAndView();
		${objectName}DO ${objectNameLower} = new ${objectName}DO();
<#if parameterTypePK == 'Long'>
		${objectNameLower} = ${objectNameLower}Service.find${objectName}ById(Long.valueOf(${camelPk}));	//根据ID读取
<#else>
		${objectNameLower} = ${objectNameLower}Service.find${objectName}ById(${camelPk});	//根据ID读取
</#if>
		mv.setViewName("${packageName}/${objectNameLower}Detail");
		mv.addObject("msg", "edit${objectName}");
		mv.addObject("pd", ${objectNameLower});
		return mv;
	}		
	/**
	 * 修改
	 */
	@RequestMapping(value="/edit${objectName}")
	public ModelAndView edit${objectName}(${objectName}DO pd${objectName}) throws Exception {
		logBefore(logger, Jurisdiction.getUsername()+"修改${objectName}");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {  //校验权限
		    return null;
		} 
		${objectNameLower}Service.edit${objectName}(pd${objectName});
		ModelAndView mv = this.getModelAndView();
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	/**
	 * 批量删除
	 */
	@RequestMapping(value="/deleteBatch${objectName}")
	@ResponseBody
	public Object deleteBatch${objectName}() throws Exception {
		logBefore(logger, Jurisdiction.getUsername()+"批量删除${objectName}");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")) { //校验权限
		    return null;
		} 
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String pks = pd.getString("pks${objectName}");
		if(StringUtils.isNotBlank(pks)) {
			String arrayPks[] = pks.split(",");
			${objectNameLower}Service.deleteBatch${objectName}(arrayPks);
			pd.put("msg", "ok");
		}else{
			pd.put("msg", "no");
		}
		pdList.add(pd);
		map.put("list", pdList);
		//return AppUtil.returnObject(pd, map);
		return map;
	}	
    /**
	 * 导出到excel
	 */
	@RequestMapping(value="/excel${objectName}")
	public ModelAndView exportExcel() throws Exception {
		logBefore(logger, Jurisdiction.getUsername()+"导出${objectName}到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")) {
		    return null;
		}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
<#list fieldList as var>
		titles.add("${var[2]}");	//${var_index+1}
</#list>
		dataMap.put("titles", titles);
		List<PageData> varOList = ${objectNameLower}Service.listAll${objectName}(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
<#list fieldList as var>
		<#if var[1] == 'Integer'>
			vpd.put("var${var_index+1}", varOList.get(i).get("${var[0]}").toString());	//${var_index+1}
		<#elseif var[1] == 'Double'>
			vpd.put("var${var_index+1}", varOList.get(i).get("${var[0]}").toString());	//${var_index+1}
		<#elseif var[6] == 'TIMESTAMP'>
		    vpd.put("var${var_index+1}", DateHelper.format((Date)(varOList.get(i).get("${var[0]}"))));
		<#else>
			vpd.put("var${var_index+1}", varOList.get(i).getString("${var[0]}"));	    //${var_index+1}
		</#if>
</#list>
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
}
