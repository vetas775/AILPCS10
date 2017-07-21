package com.ailpcs.controller;

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

import com.ailpcs.entity.core.Page;
import com.ailpcs.entity.core.PageData;
import com.ailpcs.entity.Invs6000DO;
import com.ailpcs.util.ObjectExcelView;
import com.ailpcs.core.MyCustomDateEditor;
import com.ailpcs.core.Jurisdiction;
import com.ailpcs.service.Invs6000Service;

/** 
 * Invs6000控制层.  文章表1（公告，热门活动，常见问题等）
 * 创建人：MichaelTsui
 * 创建时间：2017-07-10
 * @version 1.0
 */
@Controller
@RequestMapping(value="/invs6000")
public class Invs6000Controller extends BaseController {
	private static final String menuUrl = "invs6000/listInvs6000.do"; //菜单地址(权限用)
	@Resource(name="invs6000Service")
	private Invs6000Service invs6000Service;
	
	@InitBinder    
	public void initBinder(WebDataBinder binder) {
	    binder.registerCustomEditor(Date.class, new MyCustomDateEditor());
	}
		
	/**
	 *列表
	 */
	@RequestMapping(value="/listInvs6000")
	public ModelAndView list(Page page) throws Exception {
		logBefore(logger, Jurisdiction.getUsername()+"列表Invs6000");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		PageData pdNew = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");	//关键词检索条件
		if(StringUtils.isNotBlank(keywords)){
			pdNew.put("keywords", keywords.trim());
		}
		String orderBy = pd.getString("orderBy");	//排序方式
		if (StringUtils.isNotBlank(orderBy)) {
			pdNew.put("orderBy", orderBy.trim());
		}
		String dateStyle = pd.getString("dateStyle");	//查询日期类型
		if (StringUtils.isNotBlank(dateStyle)) {
			pdNew.put("dateStyle", dateStyle.trim());
			String dateFm = pd.getString("dateFm");	//起止日期
		    String dateTo = pd.getString("dateTo");		
		    if(StringUtils.isNotBlank(dateFm)){
			    pdNew.put("dateFm", dateFm + " 00:00:00");
		    }
		    if(StringUtils.isNotBlank(dateTo)){
			    pdNew.put("dateTo", dateTo + " 23:59:59");
		    }
		}
		page.setPd(pdNew);
		
		//设定排序字段列表 (这里内容可按实际状况调整)
		//String listSortStr = "[{\"key\": \"insertDate\", \"value\": \"建档日期\"},{\"key\": \"updateDate\", \"value\": \"修改日期\"}]";
		List<Map<String, String>> listSort = new ArrayList<Map<String, String>>();
		Map<String, String> map1 = new HashMap<String, String>();
		map1.put("key", "insertDate");  
		map1.put("value", "建档日期"); 
		listSort.add(map1);
		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("key", "updateDate");  
		map2.put("value", "修改日期"); 
		listSort.add(map2);
		//设定日期查询字段列表 (这里内容可按实际状况调整)
		List<Map<String, String>> listDate = new ArrayList<Map<String, String>>();
		Map<String, String> map3 = new HashMap<String, String>();
		map3.put("key", "insertDate");  
		map3.put("value", "建档日期"); 
		listDate.add(map3);
		Map<String, String> map4 = new HashMap<String, String>();
		map4.put("key", "updateDate");  
		map4.put("value", "修改日期"); 
		listDate.add(map4);
		
		List<Invs6000DO> varList = invs6000Service.listPageInvs6000(page);	//列出分页列表
		mv.setViewName("ailpcs/invs6000List");
		mv.addObject("varList", varList);
		mv.addObject("pd", pdNew);
		mv.addObject("listSort", listSort);
		mv.addObject("listDate", listDate);
		mv.addObject("QX",Jurisdiction.getUserAuthList());	
		return mv;
	}
	
	/**
	 * 去新增页面
	 */
	@RequestMapping(value="/goAddInvs6000")
	public ModelAndView goAddInvs6000() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("ailpcs/invs6000Detail");
		mv.addObject("msg", "saveInvs6000");
		mv.addObject("pd", pd);
		return mv;
	}	
	
	/**
	 * 新增保存
	 */
	@RequestMapping(value="/saveInvs6000")
	public ModelAndView saveInvs6000(Invs6000DO pdInvs6000) throws Exception {
		logBefore(logger, Jurisdiction.getUsername()+"新增Invs6000");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")) {  //校验权限
		    return null;
		} 
		invs6000Service.saveInvs6000(pdInvs6000);
		ModelAndView mv = this.getModelAndView();
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(value="/deleteInvs6000")
	public void deleteInvs6000(PrintWriter out, @RequestParam("docId")String docId) throws Exception {
		logBefore(logger, Jurisdiction.getUsername()+"删除Invs6000");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")) { //校验权限
		    return;
		} 
		invs6000Service.deleteInvs6000(docId);
		out.write("success");
		out.close();
	}
	/**
	 * 去修改页面
	 */
	@RequestMapping(value="/goEditInvs6000")
	public ModelAndView goEditInvs6000(@RequestParam("docId")String docId) throws Exception {
		ModelAndView mv = this.getModelAndView();
		Invs6000DO invs6000 = new Invs6000DO();
		invs6000 = invs6000Service.findInvs6000ById(docId);	//根据ID读取
		mv.setViewName("ailpcs/invs6000Detail");
		mv.addObject("msg", "editInvs6000");
		mv.addObject("pd", invs6000);
		return mv;
	}		
	/**
	 * 修改
	 */
	@RequestMapping(value="/editInvs6000")
	public ModelAndView editInvs6000(Invs6000DO pdInvs6000) throws Exception {
		logBefore(logger, Jurisdiction.getUsername()+"修改Invs6000");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {  //校验权限
		    return null;
		} 
		invs6000Service.editInvs6000(pdInvs6000);
		ModelAndView mv = this.getModelAndView();
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	/**
	 * 批量删除
	 */
	@RequestMapping(value="/deleteBatchInvs6000")
	@ResponseBody
	public Object deleteBatchInvs6000() throws Exception {
		logBefore(logger, Jurisdiction.getUsername()+"批量删除Invs6000");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")) { //校验权限
		    return null;
		} 
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String pks = pd.getString("pksInvs6000");
		if(StringUtils.isNotBlank(pks)) {
			String arrayPks[] = pks.split(",");
			invs6000Service.deleteBatchInvs6000(arrayPks);
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
	@RequestMapping(value="/excelInvs6000")
	public ModelAndView exportExcel() throws Exception {
		logBefore(logger, Jurisdiction.getUsername()+"导出Invs6000到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")) {
		    return null;
		}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("ID");	//1
		titles.add("分类");	//2
		titles.add("状态");	//3
		titles.add("标记");	//4
		titles.add("等级");	//5
		titles.add("标题");	//6
		titles.add("标题拼音");	//7
		titles.add("来源");	//8
		titles.add("来源拼音");	//9
		titles.add("作者");	//10
		titles.add("作者拼音");	//11
		titles.add("采编");	//12
		titles.add("采编拼音");	//13
		titles.add("关键词");	//14
		titles.add("关键词拼音");	//15
		titles.add("关键词2");	//16
		titles.add("关键词2拼音");	//17
		titles.add("发布日期");	//18
		titles.add("有效期从");	//19
		titles.add("有效期至");	//20
		titles.add("摘要");	//21
		titles.add("正文");	//22
		titles.add("文档URL");	//23
		titles.add("图片UrL");	//24
		titles.add("图片URL2");	//25
		titles.add("JsonURL");	//26
		titles.add("赞");	//27
		titles.add("踩");	//28
		titles.add("建档人");	//29
		titles.add("建档日期");	//30
		titles.add("修改人");	//31
		titles.add("修改日期");	//32
		dataMap.put("titles", titles);
		List<PageData> varOList = invs6000Service.listAllInvs6000(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("doc_id"));	    //1
			vpd.put("var2", varOList.get(i).getString("doc_type"));	    //2
			vpd.put("var3", varOList.get(i).getString("doc_status"));	    //3
			vpd.put("var4", varOList.get(i).getString("doc_flag"));	    //4
			vpd.put("var5", varOList.get(i).getString("doc_class"));	    //5
			vpd.put("var6", varOList.get(i).getString("doc_title"));	    //6
			vpd.put("var7", varOList.get(i).getString("title_pinyin"));	    //7
			vpd.put("var8", varOList.get(i).getString("doc_source"));	    //8
			vpd.put("var9", varOList.get(i).getString("source_pinyin"));	    //9
			vpd.put("var10", varOList.get(i).getString("doc_author"));	    //10
			vpd.put("var11", varOList.get(i).getString("author_pinyin"));	    //11
			vpd.put("var12", varOList.get(i).getString("doc_incharge"));	    //12
			vpd.put("var13", varOList.get(i).getString("incharge_pinyin"));	    //13
			vpd.put("var14", varOList.get(i).getString("index_key"));	    //14
			vpd.put("var15", varOList.get(i).getString("key_pinyin"));	    //15
			vpd.put("var16", varOList.get(i).getString("index_key2"));	    //16
			vpd.put("var17", varOList.get(i).getString("key2_pinyin"));	    //17
			vpd.put("var18", varOList.get(i).getString("doc_date"));	    //18
			vpd.put("var19", varOList.get(i).getString("effective_date_fm"));	    //19
			vpd.put("var20", varOList.get(i).getString("effective_date_to"));	    //20
			vpd.put("var21", varOList.get(i).getString("doc_description"));	    //21
			vpd.put("var22", varOList.get(i).getString("doc_content"));	    //22
			vpd.put("var23", varOList.get(i).getString("doc_url"));	    //23
			vpd.put("var24", varOList.get(i).getString("pic_url"));	    //24
			vpd.put("var25", varOList.get(i).getString("pic_url2"));	    //25
			vpd.put("var26", varOList.get(i).getString("orther_url"));	    //26
			vpd.put("var27", varOList.get(i).get("reader_like").toString());	//27
			vpd.put("var28", varOList.get(i).get("reader_deny").toString());	//28
			vpd.put("var29", varOList.get(i).getString("insert_user"));	    //29
			vpd.put("var30", varOList.get(i).getString("insert_date"));	    //30
			vpd.put("var31", varOList.get(i).getString("update_user"));	    //31
			vpd.put("var32", varOList.get(i).getString("update_date"));	    //32
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
}
