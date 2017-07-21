package com.ailpcs.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ailpcs.entity.core.PageData;

/**
 * 主画面顶部面板(头部)的全部操作功能都在这里实现, 包括修改头像、邮件、站内信、短信、聊天、设置参数等等 
 */
public interface SysHeadPanelService {
	
	/**
	 * 获取头部信息(用户头像、未读站内信数目、webSocket配置等等...)
	 */
	Object getHeadPanelList(HttpServletRequest request);
	/**
	 * 获取站内信未读总数
	 */
	public Map<String,Object> getInnerMailCount();
	/**
	 * 保存系统设置参数. menuIndex: 1-系统设置一 , 2-系统设置二......4-系统设置四
	 */
	void saveSysParameterSetting(PageData pd, int menuIndex);
	/**
	 * 读取全部系统设置参数
	 */
	PageData catchAllSysParameter();
}
