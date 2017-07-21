package com.ailpcs.core;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.java_websocket.WebSocketImpl;

import com.ailpcs.controller.BaseController;
import com.ailpcs.util.DbUtil;

/**
 * 启动tomcat时运行此类, 在里面进行启动聊天服务、在线管理服务等初始化工作...
 * web.xml配置:
 * <filter>
 *   <filter-name>startFilter</filter-name>
 *   <filter-class>com.ailpcs.sys.MyStartFilter</filter-class>
 * </filter>
 * filter是随你的web应用启动而启动的，只初始化一次，以后就可以拦截相关请求，只有当你的web应用停止或重新部署的时候才销毁。
 * 而servlet只有配置了参数<load-onstartup>1</load-on-startup>时才会在容器启动时加载; 否则在第一次请求的时候被加载和实例化。
 * servlet一旦被加载，一般不会从容器中删除，直至应用服务器关闭或重新启动。但当容器做内存回收动作时，servlet有可能被删除。
 * Amend by MichaelTsui 17/06/20
 */
public class MyStartFilter extends BaseController implements Filter{
	//public FilterConfig config;
	//sp: 系统设置参数, 在Tomcat启动本过滤器时，在静态代码块里从txt配置文件读入全部系统设置参数并保存在sp里， 供后面各处调用
	//当用户在参数设置画面修改了系统参数时，需要调用本类的refreshSysParameter方法来刷新sp.
	public static Map<String, String> sp;
	
	static { 
		sp = SystemParameterReadWrite.catchSysParameter(999); //Tomcat启动时读取全部系统参数到 sp
	}  
	
	//刷新系统参数
	public static void refreshSysParameter(){
		sp = SystemParameterReadWrite.catchSysParameter(999); 
	}
	
	/**
	 * 初始化
	 */
	public void init(FilterConfig fc) throws ServletException {
		//Map<String,String> sMapWS = SystemParameterReadWrite.catchSysParameter(999);	//3 读取WEBSOCKET配置 Const.WEBSOCKET
		//config = fc;
		this.startWebsocketInstantMsg(sp.get("WIMPORT"));  //WIMPORT-聊天服务器端口
		this.startWebsocketOnline(sp.get("OLPORT")); //OLPORT-在线管理服务器端口
		this.reductionDbBackupQuartzState();
	}
	
	/**
	 * 启动即时聊天服务
	 */
	public void startWebsocketInstantMsg(String port){
		WebSocketImpl.DEBUG = false;
		ChatServer s;
		try {
			s = new ChatServer(Integer.parseInt(port));
			s.start();
			//System.out.println( "websocket聊天服务器启动,端口" + s.getPort() );			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 启动在线管理服务
	 */
	public void startWebsocketOnline(String port){
		WebSocketImpl.DEBUG = false;
		OnlineChatServer s;
		try {
			s = new OnlineChatServer(Integer.parseInt(port));
			s.start();
			//System.out.println( "websocket在线管理服务器启动,端口" + s.getPort() );			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * web容器重启时，所有定时备份状态关闭
	 */
	public void reductionDbBackupQuartzState(){
		try {
			DbUtil.executeUpdateFH("update db_timingbackup set STATUS = '2'");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 计时器(已废弃), 用quartz代替它的功能。
	 */
	@Deprecated
	public void timer() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 9); // 控制时
		calendar.set(Calendar.MINUTE, 0); 		// 控制分
		calendar.set(Calendar.SECOND, 0); 		// 控制秒
		Date time = calendar.getTime(); 		// 得出执行任务的时间
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				//PersonService personService = (PersonService)ApplicationContext.getBean("personService");
				//System.out.println("-------设定要指定任务--------");
			}
		}, time, 1000*60*60*24);// 这里设定将延时每天固定执行
	}
	
	public void destroy() {
		// TODO Auto-generated method stub
		//this.config = null;
	}
	
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		// TODO Auto-generated method stub
		//web服务器根据Filter在web.xml文件中的注册顺序，决定先调用哪个Filter，当第一个Filter的doFilter方法被调用时，
		//web服务器会创建一个代表Filter链的FilterChain对象传递给该方法。在doFilter方法中，开发人员如果调用了FilterChain
		//对象的doFilter方法，则web服务器会检查FilterChain对象中是否还有filter，如果有，则调用第2个filter，如果没有，则调用目标资源。
		//String disabletestfilter = config.getInitParameter("disabletestfilter");
		arg2.doFilter(arg0, arg1);  
	}
	
}
