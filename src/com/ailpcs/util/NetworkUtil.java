package com.ailpcs.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 网络工具类
 * @author chenzhiheng
 *
 */
public class NetworkUtil {
	
	/**
	 *获取请求主机IP地址
	 * @param request
	 * @return
	 */
	public final static String getIpAddress(HttpServletRequest request){
	 	String ip = request.getHeader("x-forwarded-for"); 
	    
	 	if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getHeader("Proxy-Client-IP"); 
	    } 
	    
	 	if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getHeader("WL-Proxy-Client-IP"); 
	    } 
	    
	 	if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getHeader("HTTP_CLIENT_IP"); 
	    } 
	    
	 	if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getHeader("HTTP_X_FORWARDED_FOR"); 
	    } 
	    
	 	if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getRemoteAddr(); 
	    }
	 	
		return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
	}
}
