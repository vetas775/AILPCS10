package com.ailpcs.core;
import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;
import org.springframework.util.StringUtils;
//import com.tl.core.exception.FrameworkException;

/**
 * http://cfanz.cn/index.php?c=article&a=read&id=303559
 * Spring提供的数据绑定方法有一个缺陷：一个比较大的项目的话，可能需要有带时间的日期，也有可能是不带日期的日期，这种办法只能兼用一种日期格式数据
 * 我的解决办法
 * 基于上述方法，我查看了spring 这个CustomDateEditor类的源码，其实这个类是提供了日期转换功能。所以我的做法是
 * 重写spring日期转换器(MyCustomDateEditor)，自定义日期转换器, 然后在控制层加上以下语句即可完美应付前端多种时间格式的日期数据
 * 
 * @InitBinder    
 * public void initBinder(WebDataBinder binder) {
 *     binder.registerCustomEditor(Date.class, new MyCustomDateEditor());
 * }
 */
public class MyCustomDateEditor extends PropertyEditorSupport {
    //protected Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * Parse the Date from the given text, using the specified DateFormat.
     */
    @Override
    public void setAsText(String text) throws MyExceptionParameter {
    	if (!StringUtils.hasText(text)) {
    		setValue(null); // Treat empty String as null value.
    	} else {
   			setValue(this.dateAdapter(text));
        }
    }
    /**
     * Format the Date as String, using the specified DateFormat.
     */
    @Override
    public String getAsText() {
        Date value = (Date) getValue();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return (value != null ? dateFormat.format(value) : "");
    }
    /**
     * 字符串转日期适配方法
     * @param dateStr 日期字符串
     * @throws FrameworkException 
     */
    @SuppressWarnings("deprecation")
	public Date dateAdapter(String dateStr) throws MyExceptionParameter {
        Date date=null;
        if(!(null==dateStr || "".equals(dateStr))) {
            //判断是不是日期字符串，如Wed May 28 08:00:00 CST 2014
        	if (dateStr.contains("CST")) {
        		date = new Date(dateStr);
        	} else {
        		dateStr = dateStr.replace("年", "-").replace("月", "-").replace("日", "").replaceAll("/", "-").replaceAll("\\.", "-").trim();
        		String fm = "";
        		if(Pattern.compile("^[0-9]{4}-[0-9]{2}-[0-9]{2}.*").matcher(dateStr).matches()){
        			fm = "yyyy-MM-dd";
        		} else if (Pattern.compile("^[0-9]{4}-[0-9]{1}-[0-9]+.*||^[0-9]{4}-[0-9]+-[0-9]{1}.*").matcher(dateStr).matches()){ 
        			fm = "yyyy-M-d";
        		} else if(Pattern.compile("^[0-9]{2}-[0-9]{2}-[0-9]{2}.*").matcher(dateStr).matches()) {
        			fm = "yy-MM-dd";        			
        		} else if(Pattern.compile("^[0-9]{2}-[0-9]{1}-[0-9]+.*||^[0-9]{2}-[0-9]+-[0-9]{1}.*").matcher(dateStr).matches()){
                    fm = "yy-M-d";
                }
        		
        		//确定时间格式
                if(Pattern.compile(".*[ ][0-9]{2}").matcher(dateStr).matches()){
                    fm+= " HH";
                }else if(Pattern.compile(".*[ ][0-9]{2}:[0-9]{2}").matcher(dateStr).matches()){
                    fm+= " HH:mm";
                }else if(Pattern.compile(".*[ ][0-9]{2}:[0-9]{2}:[0-9]{2}").matcher(dateStr).matches()){
                    fm+= " HH:mm:ss";
                }else if(Pattern.compile(".*[ ][0-9]{2}:[0-9]{2}:[0-9]{2}:[0-9]{0,3}").matcher(dateStr).matches()){
                    fm+= " HH:mm:ss:sss";
                }
                
                if(!"".equals(fm)) {
                	try {
                	    date = new SimpleDateFormat(fm).parse(dateStr);
                	} catch (ParseException e) {
                		//throw new MyExceptionBusiness("101","参数字符串“"+dateStr+"”无法被转换为日期格式！");
                		throw new MyExceptionParameter("[AILPCS]参数字符串“" + dateStr + "”无法被转换为日期格式！");
                		
                	}
                }
        	}
        }
        return date;
    }
}
