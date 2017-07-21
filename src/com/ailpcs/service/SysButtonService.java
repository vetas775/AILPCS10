package com.ailpcs.service;

import java.util.List;

import com.ailpcs.entity.core.Page;
import com.ailpcs.entity.core.PageData;

/** 
 * 说明：按钮管理 接口 
 */
public interface SysButtonService{
	/**
	 * 新增
	 */
	void save(PageData pd)throws Exception;
	/**
	 * 删除
	 */
	void delete(PageData pd)throws Exception;
	/**
	 * 修改
	 */
	void edit(PageData pd)throws Exception;
	/**
	 * 列表
	 */
	List<PageData> listPage(Page page)throws Exception;
	/**
	 * 列表(全部)
	 */
	List<PageData> listAll(PageData pd)throws Exception;
	/**
	 * 通过id获取数据
	 */
	PageData findById(PageData pd)throws Exception;
	/**
	 * 批量删除
	 */
	void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	
}

