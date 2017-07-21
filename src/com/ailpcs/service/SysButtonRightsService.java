package com.ailpcs.service;

import java.util.List;

import com.ailpcs.entity.core.PageData;

/** 
 * 按钮权限 接口
 */
public interface SysButtonRightsService{
	/**
	 * 新增
	 */
	void save(PageData pd) throws Exception;
	/**
	 * 通过(角色ID和按钮ID)获取数据
	 */
	PageData findById(PageData pd)throws Exception;
	/**
	 * 删除
	 */
	void delete(PageData pd)throws Exception;
	/**
	 * 列表(全部)
	 */
	List<PageData> listAll(PageData pd)throws Exception;
	/**
	 * 列表(全部)左连接按钮表,查出安全权限标识
	 */
	List<PageData> listAllBrAndQxname(PageData pd)throws Exception;
}

