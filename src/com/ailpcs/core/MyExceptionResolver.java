package com.ailpcs.core;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

//我们还需要一个辅助的类StringPrintWriter，因为ex.printStackTrace参数只有个PrintWriter类型的，java自带的StringWriter 
//不可用,所以我们需要自己实现一个装饰器的StringPrintWriter。 
//import com.ailpcs.utils.StringPrintWriter;  


/** 自定义实现Spring的HandlerExceptionResolver处理类, 需在ApplicationContext-mvc.xml里配置。
 *  MichaelTsui17/06/20 Amend
 *     "今天遇到一个事情，让我想用到HandlerExceptionResolver这个东东来处理异常: 今天准备把自助系统进入上线状态， 
 * 所以把log的级别从DEBUG调到INFO，结果没有catch Runtime异常在log记录，后来跟踪了一下原来Spring把异常处理里的log 
 * 直接使用的是debug，而不是error，所以log级别设置为INFO导致异常没有记录，看了一下spring的源代码,可以看到可以插入自
 * 己的HandlerExceptionResover来搞定这个问题"，
 *     通过自定义实现HandlerExceptionResolver类,我们可以在它里面的resolveException方法任意处理异常和log。也可以把错误信息
 * 个性化后传到view层显示。
 * 
 *     这么好用的东西, 记得要先在ApplicationContext-mvc.xml里配置它才生效的:
 *     <!-- 自定义实现的Spring HandlerExceptionResolver, 用于处理异常和log -->
 *     <bean id="exceptionResolver" class="com.ailpcs.sys.MyExceptionResolver"></bean>
 *      
 *     我们在这里实现了三个处理： 1.记入log，2. 控台显示  3.将异常的完整信息放在错误页面的一个隐藏的区域，方便查找出现错误的原因。 
 * 相关文档: http://fuliang.iteye.com/blog/947191
 */
public class MyExceptionResolver implements HandlerExceptionResolver{
	
	private static Logger logger = Logger.getLogger(MyExceptionResolver.class);
	
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		// TODO Auto-generated method stub
		//1of3  记入日志   
		if(ex instanceof MyExceptionBusiness) {  
			logger.error("AILPCS-MyExceptionBusiness: ", ex);
        }else if(ex instanceof MyExceptionParameter) {  
        	logger.error("AILPCS-MyExceptionParameter: ", ex);
        } else {  
        	logger.error("AILPCS-Catch-Other-Exception: ", ex); 
        } 
		
		/*
		//2of3  控台打印输出
		StringPrintWriter strintPrintWriter = new StringPrintWriter();
		System.out.println("==============异常开始=============");
		//ex.printStackTrace();
		ex.printStackTrace(strintPrintWriter);
		System.out.println("==============异常结束=============");		
		*/
		
		//3of3 同时把错误丢到view里传到前台供前台显示. 
		////这是一个在jsp里显示example:
		////<div style="display:none;">  
	    ////<c:out value="${exception}"></c:out>  
	    ////</div> 
		Map<String, Object> model = new HashMap<String, Object>();  
        model.put("exception", ex);  
        // 根据不同错误转向不同页面  
        if(ex instanceof MyExceptionBusiness) {  
            return new ModelAndView("error-business", model);  
        }else if(ex instanceof MyExceptionParameter) {  
            return new ModelAndView("error-parameter", model);  
        } else {  
            return new ModelAndView("error", model);  
        } 
	}

}
