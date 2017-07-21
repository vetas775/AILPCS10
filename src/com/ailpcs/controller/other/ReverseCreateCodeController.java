package com.ailpcs.controller.other;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ailpcs.controller.BaseController;
import com.ailpcs.core.Jurisdiction;
import com.ailpcs.entity.core.PageData;
import com.ailpcs.util.AppUtil;
import com.ailpcs.util.CamelField;
import com.ailpcs.util.DbUtil;

/** 
 * "系统工具"=>"代码生成器"=>"反向生成"
 */
@Controller
@RequestMapping(value="/recreateCode")
public class ReverseCreateCodeController extends BaseController {
	
	private static final String menuUrl = "recreateCode/list.do"; //菜单地址(权限用)
	
	/**列表
	 * @return
	 */
	@RequestMapping(value="/list")
	public ModelAndView list() throws Exception{
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){} 	//校验权限
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("system/createcode/recreatecode_list");
		mv.addObject("QX",Jurisdiction.getUserAuthList());	//按钮权限
		return mv;
	}
	
	 /**列出所有表
	 * @param
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/listAllTable")
	@ResponseBody
	public Object listAllTable(){
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限
		PageData pd = new PageData();		
		pd = this.getPageData();
		Map<String,Object> map = new HashMap<String,Object>();
		List<PageData> pdList = new ArrayList<PageData>();
		List<String> tblist = new ArrayList<String>();
		try {
			Object[] arrOb = DbUtil.getTables(pd);
			tblist = (List<String>)arrOb[1];
			pd.put("msg", "ok");
		} catch (ClassNotFoundException e) {
			pd.put("msg", "no");
			e.printStackTrace();
		} catch (SQLException e) {
			pd.put("msg", "no");
			e.printStackTrace();
		}
		pdList.add(pd);
		map.put("tblist", tblist);
		map.put("list", pdList);
		return AppUtil.returnObject(pd, map);
	}
	
	/**去代码生成器页面(进入弹窗)
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/goProductCode")
	public ModelAndView goProductCode() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String fieldType = "";
		StringBuffer sb = new StringBuffer("");
		String jdbcTypePk = ""; //Michael17/07/08 记录主键的JDBC数据类型
		String parameterTypePK = "String"; //Michael17/07/08 记录主键的Parameter Type数据类型
		String pkGetter = ""; //Michael17/07/08 记录主键的getter事件名
		
		List<Map<String,String>> columnList = DbUtil.getFieldParameterLsit(DbUtil.getFHCon(pd),pd.getString("table")); //读取字段信息
		for(int i=0;i<columnList.size();i++){
			Map<String,String> fmap = columnList.get(i);
			String fieldName = fmap.get("fieldNanme").toString().toLowerCase(); 
			String camel = CamelField.underlineToCamel(fieldName);  //
			String uCamel = CamelField.captureName(camel); //首字母变大写
			if (i == 0) { pkGetter = "set"+uCamel; } //Michael17/07/08 记录主键的getter事件名
			sb.append(fieldName);					//var[0] 字段名称
			sb.append(",fh,");
			fieldType = fmap.get("fieldType").toString().toLowerCase();					//var[1] 字段类型
			if(fieldType.contains("int")){
				sb.append("Integer");
			}else if(fieldType.contains("NUMBER")){
				if(Integer.parseInt(fmap.get("fieldSccle")) > 0){
					sb.append("Double");
				}else{
					sb.append("Integer");
				}
			}else if(fieldType.contains("double") || fieldType.contains("numeric")){
				sb.append("Double");
			}else if(fieldType.contains("date")){
				sb.append("Date");
			}else{
				sb.append("String");
			}
			sb.append(",fh,");
			sb.append("备注"+(i+1));														//var[2]备注
			sb.append(",fh,");
			sb.append("是");																//var[3]是否前台录入
			sb.append(",fh,");
			//sb.append("无");															//默认值	
			sb.append(camel);                          //var[4]Michael:默认值改做 字段名转为驼峰规则
			sb.append(",fh,");
			sb.append(fmap.get("fieldLength").toString());								//var[5]长度
			sb.append(",fh,");
			//sb.append(fmap.get("fieldSccle").toString());								//var[6]小数点右边的位数
			//sb.append(fmap.get("fieldSccle").toString());								//Michael : var[6]改做 jdbcType
			if(fieldType.contains("bigint")){
				sb.append("BIGINT");
				if (i == 0) { jdbcTypePk = "BIGINT"; parameterTypePK = "Long"; }
			}else if(fieldType.contains("int")){
				sb.append("INTEGER");
				if (i == 0) { jdbcTypePk = "INTEGER";}
			}else if(fieldType.contains("decimal")){
				sb.append("DECIMAL");	
				if (i == 0) { jdbcTypePk = "DECIMAL";}
			}else if(fieldType.contains("datetime")){
				sb.append("TIMESTAMP");
				if (i == 0) { jdbcTypePk = "TIMESTAMP";}
			}else if(fieldType.contains("text")){
				sb.append("LONGVARCHAR");
				if (i == 0) { jdbcTypePk = "LONGVARCHAR";}
			}else if(fieldType.contains("json")){
				sb.append("OTHER");
				if (i == 0) { jdbcTypePk = "OTHER";}	
			}else{
				sb.append("VARCHAR");
				if (i == 0) { jdbcTypePk = "VARCHAR";}
			}
			 
			sb.append(",fh,");
			sb.append(uCamel);   //Michael新增 : var[7]把驼峰规则的属性名首字母变大写
			sb.append("Q416852186");
		}
		pd.put("jdbcTypePk", jdbcTypePk); //Michael17/07/08 记录主键的JDBC数据类型
		pd.put("parameterTypePK", parameterTypePK); //Michael17/07/08 记录主键的Parameter Type数据类型		
		pd.put("pkGetter", pkGetter); //Michael17/07/08 记录主键的getter事件名
		pd.put("FIELDLIST", sb.toString());
		mv.addObject("msg", "edit");
		mv.addObject("pd", pd);
		mv.setViewName("system/createcode/productCode");
		return mv;
	}
	
}
