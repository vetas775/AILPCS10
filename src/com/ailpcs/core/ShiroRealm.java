package com.ailpcs.core;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;


/** Shiro 自定义realm。 
 *  登录请求调用currentUser.login之后，shiro会将token传递给自定义realm,此时realm会先调用doGetAuthenticationInfo
 *  (AuthenticationToken authcToken )登录验证的方法，验证通过后会接着调用 doGetAuthorizationInfo(PrincipalCollection principals)
 *  获取角色和权限的方法（授权），最后返回视图。  
 *  当其他请求进入shiro时，shiro会调用doGetAuthorizationInfo(PrincipalCollection principals)去获取授权信息，若是没有权限或角色，会跳转到未
 *  授权页面，若有权限或角色，shiro会放行，ok，此时进入真正的请求方法……
 *  Amend by MichaelTsui 17/06/19
 */
public class ShiroRealm extends AuthorizingRealm {

	/*
	 *登录验证, 验证成功后返回一个AuthenticationInfo实现
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		 String username = (String)token.getPrincipal();  				//得到用户名 
	     String password = new String((char[])token.getCredentials()); 	//得到密码
	     if(null != username && null != password) { 
	    	 return new SimpleAuthenticationInfo(username, password, getName());
	     } 
	     return null;  //验证失败, 抛出对应信息的异常AuthenticationException
	}
	
	/*
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用,负责在应用程序中决定用户的访问控制的方法(non-Javadoc)
	 * @see org.apache.shiro.realm.AuthorizingRealm#doGetAuthorizationInfo(org.apache.shiro.subject.PrincipalCollection)
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pc) {
		System.out.println("========2");
		return null;
	}

}
