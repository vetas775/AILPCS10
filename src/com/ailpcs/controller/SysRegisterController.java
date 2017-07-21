package com.ailpcs.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ailpcs.core.Const;
import com.ailpcs.core.Jurisdiction;
import com.ailpcs.dao.Dao2;
import com.ailpcs.entity.core.PageData;
import com.ailpcs.util.AppUtil;
import com.ailpcs.util.LogUtil;
import com.ailpcs.util.Tools;


/**
  * 页面注册 接口
  * 相关参数协议：
  * 00	请求失败
  * 01	请求成功
  * 02	返回空值
  * 03	请求协议参数不完整    
  * 04  用户名或密码错误
  * 05  FKEY验证失败
 */
@Controller
@RequestMapping(value="/fReeRegisterUser")
public class SysRegisterController extends BaseController {
	private static final String[] REQUIRED_PARAMETER = new String[]{"USERNAME","PASSWORD","NAME","EMAIL","rcode"};
	
    @Resource(name = "dao2")
	private Dao2<PageData> daoPD;
	
	/**系统用户注册接口
	 * @return
	 */
	@RequestMapping(value="/registerNewUser")
	@ResponseBody
	public Object registerSysUser(){
		logBefore(logger, "系统用户注册接口");
		Map<String,Object> map = new HashMap<String,Object>();
		PageData pd = new PageData();
		pd = this.getPageData();
		String result = "00";
		try{
			if(Tools.checkKey("USERNAME", pd.getString("FKEY"))){	//检验请求key值是否合法
				if(AppUtil.checkParam("/fReeRegisterUser/registerNewUser", pd, REQUIRED_PARAMETER)){		//检查参数
					
					Session session = Jurisdiction.getSession();
					String sessionCode = (String)session.getAttribute(Const.SESSION_SECURITY_CODE);		//获取session中的验证码
					String rcode = pd.getString("rcode");
					if(Tools.notEmpty(sessionCode) && sessionCode.equalsIgnoreCase(rcode)){				//判断登录验证码
						pd.put("USER_ID", this.get32UUID());	//ID 主键
						pd.put("ROLE_ID", "3");					//角色ID 3 为注册用户
						pd.put("NUMBER", "");					//编号
						pd.put("PHONE", "");					//手机号
						pd.put("BZ", "注册用户");				//备注
						pd.put("LAST_LOGIN", "");				//最后登录时间
						pd.put("IP", "");						//IP
						pd.put("STATUS", "0");					//状态
						pd.put("SKIN", "default");
						pd.put("RIGHTS", "");		
						pd.put("PASSWORD", new SimpleHash("SHA-1", pd.getString("USERNAME"), pd.getString("PASSWORD")).toString());	//密码加密
						if(null == daoPD.getFromDb("UserMapper.findByUsername", pd)){	//判断用户名是否存在
							daoPD.saveDb("UserMapper.saveUser", pd);	//执行保存
							LogUtil.saveLog2Db(daoPD, pd.getString("USERNAME"), "新注册");
						}else{
							result = "04"; 	//用户名已存在
						}
					}else{
						result = "06"; 		//验证码错误
					}
				}else {
					result = "03";
				}
			}else{
				result = "05";
			}
		}catch (Exception e){
			logger.error(e.toString(), e);
		}finally{
			map.put("result", result);
			logAfter(logger);
		}
		return AppUtil.returnObject(new PageData(), map);
	}
}
	
 