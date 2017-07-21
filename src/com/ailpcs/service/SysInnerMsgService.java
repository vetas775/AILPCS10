package com.ailpcs.service;

import java.util.List;
import java.util.Map;

import com.ailpcs.entity.core.Page;
import com.ailpcs.entity.core.PageData;
/** 
 * 说明： 站内信接口
 */
public interface SysInnerMsgService{

	/**
	 * 用户站内信列表
	 */
	Map<String, Object> listUserMsg(Page page, PageData pd)throws Exception;
	
	/**
	 * 发送站内信(新增)
	 */
	List<PageData> sendInnerMessage(PageData pd)throws Exception;
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	void delete(PageData pd)throws Exception;
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	void edit(PageData pd)throws Exception;
	/**
	 * 列表(全部)
	 */
	List<PageData> listAll(PageData pd)throws Exception;
	
	/**
	 * 通过id获取数据
	 */
	PageData findById(PageData pd)throws Exception;
	
	/**获取未读总数
	 
	PageData findFhsmsCount(String USERNAME)throws Exception;
	*/
	/**
	 * 批量删除
	 */
	void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	
}

