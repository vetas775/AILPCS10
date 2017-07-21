package com.ailpcs.controller.app;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ailpcs.controller.BaseController;
import com.ailpcs.entity.core.PageData;
import com.ailpcs.service.SysAppUserService;
import com.ailpcs.util.AppUtil;
import com.ailpcs.util.MD5;

/**
  * 会员-接口类 
  * 相关参数协议：
  * 00	请求失败
  * 01	请求成功
  * 02	返回空值
  * 03	请求协议参数不完整    
  * 04  用户名或密码错误
  * 05  Token验证失败
 */

@Controller
@RequestMapping(value="/appuser")
public class AppuserController extends BaseController {
	private static final String[] REQUIRED_PARAMETER = new String[]{"userId","sIgn","timestAmp"}; //请求本接口时必须具备的三个参数
	
	@Resource(name="sysAppUserService")
	private SysAppUserService appuserService;
	
	/**根据用户名获取会员信息
	 * @return 相关参数协议：result: 00	请求失败 ; 01	请求成功; 02	返回空值; 03	请求协议参数不完整  ; 04  用户名或密码错误; 05  Token验证失败
	 */
	@RequestMapping(value="/getAppuserByUerId")
	@ResponseBody
	public Object getAppuserByUsernmae(HttpServletRequest request){
		Map<String,Object> map = new HashMap<String, Object>();
		PageData pd = new PageData();
		pd = this.getPageData();
		String result = "00"; 
		//检查请求参数是否完整:
		try {
		    if(!AppUtil.checkParam("/appuser/getAppuserByUsernmae", pd, REQUIRED_PARAMETER)) {
			    map.put("result", "03"); //03	请求协议参数不完整    
			    return map;
		    }
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}	
		//sign = $.md5(svrUrl + toekn + timestamp)
		String userId = request.getParameter("userId"); 
		String userToken = "userToken"; //<====这里需从redis里取出userId的真实Token代入.
		//ex. http://localhost:8080/AILPCS10/appuser/getAppuserByUerId.do?userId=1
		System.out.println(request.getRequestURL() + "?userId=" + userId);	
		String urlSign = MD5.md5(request.getRequestURL() + "?userId=" + userId
				+ userToken + request.getParameter("timestAmp"));
		String sIgn = request.getParameter("sIgn");
		//检查Token
	    if (!urlSign.equals(sIgn)) {
	        map.put("result", "05"); //05  Token验证失败
		    return map;
		}
		
			
		
		try{
			pd = appuserService.findByUsername(pd);
			map.put("pd", pd);
			result = (null == pd) ?  "02" :  "01";
		} catch (Exception e) {
			logger.error(e.toString(), e);
		} finally{
			map.put("result", result);
		}
		//return AppUtil.returnObject(new PageData(), map);
		return map;
	}
	

	
}
	
 