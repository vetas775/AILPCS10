package com.ailpcs.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ailpcs.entity.core.UserDO;
/*
* 这是一个我们自定义的Spring MVC拦截器, 里面重写了preHandle方法来对本系统的所有访问进行拦截后进行“业务操作权限”检查。
* 本类进行业务操作权限检查 :
* 1. 对Const.NO_INTERCEPTOR_PATH里设定为无需检查业务操作权限的请求直接放行, 否则进行第2步检查
* 2. 对设定为需要检查的请求进行User登录状况验证, 未登录的直接跳转到登录画面, 已登录的进行第3步检查
* 3. 从session里找出该User的操作权限列表, 检查他对本次请求的访问是否有权操作, 是则放行进入controller, 否则跳转到登录画面.
*    对于权限列表里找不到的访问请求, 也会被视作有授权而放行。
*    
* 需在ApplicationContext-mvc.xml里配置:
* <!-- 这是一个我们自定义的Spring MVC拦截器, 里面重写了preHandle方法来对本系统的所有访问进行拦截后进行业务权限检查  -->  
*  	<mvc:interceptors>
*		<mvc:interceptor>
*			<mvc:mapping path="/××/**"/>
*			<bean class="com.ailpcs.sys.MyLoginHandlerInterceptor"/>
*		</mvc:interceptor>
*	</mvc:interceptors>
*    
* Amend by MichaelTsui 17/06/20
*/
public class MyLoginHandlerInterceptor extends HandlerInterceptorAdapter{
	private Logger logger = Logger.getLogger(this.getClass());
	/* preHandle方法是进行处理器拦截用的，顾名思义，该方法将在Controller处理之前进行调用，
	 * SpringMVC中的Interceptor拦截器是链式的，可以同时存在多个Interceptor，
	 * 然后SpringMVC会根据声明的前后顺序一个接一个的执行，
	 * 而且所有的Interceptor中的preHandle方法都会在Controller方法调用之前调用。
	 * SpringMVC的这种Interceptor链式结构也是可以进行中断的，
	 * 这种中断方式是令preHandle的返回值为false，当preHandle的返回值为false的时候整个请求就结束了。
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// TODO Auto-generated method stub
		String path = request.getServletPath();
		
		if(path.matches(Const.NO_INTERCEPTOR_PATH)){ //无需做菜单访问权限校验的非菜单操作
			System.out.println("MyLoginHandlerInterceptor.preHandle===Free=============================path="+path);
			return true;
		}else{
			System.out.println("MyLoginHandlerInterceptor.preHandle====================================path="+path);
			UserDO user = (UserDO)Jurisdiction.getSession().getAttribute(Const.SESSION_USER);
			if(user != null) {
				path = path.substring(1, path.length()); //path example: /catchSessionSecCode.do
				boolean b = Jurisdiction.hasJurisdiction(path); //菜单访问权限校验
				if(!b){
					logger.info("MyLoginHandlerInterceptor=>"+user.getUserName()+"无权访问"+path+", 系统跳转登录页面！");
					response.sendRedirect(request.getContextPath() + Const.LOGIN);
				}
				return b;
			} else {
				//登陆过滤
				logger.info("MyLoginHandlerInterceptor=>User为Null访问'"+path+"',跳转登录页面");
				response.sendRedirect(request.getContextPath() + Const.LOGIN);
				return false;		
			}
		}
	}
	
}
