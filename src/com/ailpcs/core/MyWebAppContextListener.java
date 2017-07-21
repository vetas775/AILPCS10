package com.ailpcs.core;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.support.WebApplicationContextUtils;
/*  MichaelTsui17/06/20 Amend (web.xml配置)
 *  在 Servlet API 中有一个 ServletContextListener 接口，它能够监听 ServletContext 对象的生命周期，实际上就是监听 Web 应用的生命周期。
 *  当Servlet 容器启动或终止Web 应用时，会触发ServletContextEvent 事件，该事件由ServletContextListener 来处理。在 ServletContextListener 
 *  接口中定义了处理ServletContextEvent 事件的两个方法contextDestroyed和contextInitialized。
 *  contextInitialized - 在 Servlet Container 加载Web 应用程序时（例如启动 Container 之后），会呼叫contextInitialized() 
 *  contextDestroyed - 当容器移除Web 应用程序时，会呼叫contextDestroyed () 方法.
 *    
 *  通过 Tomcat 控制台的打印结果的先后顺序，会发现当 Web 应用启动时，Servlet 容器先调用contextInitialized() 方法，再调用lifeInit 的init() 方法；
 *  当Web 应用终止时，Servlet 容器先调用lifeInit 的destroy() 方法，再调用contextDestroyed() 方法。
 *  由此可见，在Web 应用的生命周期中，ServletContext 对象最早被创建，最晚被销毁。
 *  web.xml配置：
 *  <listener>
 *   <listener-class>com.ailpcs.sys.MyWebAppContextListener</listener-class> <!--我们利用它来获取WebApplicationContext -->
 *  </listener>  
 */
public class MyWebAppContextListener implements ServletContextListener {

	/* 当Servlet 容器终止Web 应用时会调用该方法。在调用该方法之前，容器会先销毁所有的Servlet 和Filter 过滤器。 */
	public void contextDestroyed(ServletContextEvent event) {
		// TODO Auto-generated method stub
	}

	/*
	 *  当Servlet 容器启动Web 应用时会调用该方法。在调用完该方法之后，容器再对Filter初始化， 
     *  并且对那些在Web 应用启动时就需要被初始化的Servlet 进行初始化。 
	 */
	public void contextInitialized(ServletContextEvent event) {
		// TODO Auto-generated method stub
		//通过ServletContext对象获取ApplicationContext对象，然后在通过它获取需要的类实例。
		Const.WEB_APP_CONTEXT = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
		System.out.println("========获取Spring WebApplicationContext");
	}

}
