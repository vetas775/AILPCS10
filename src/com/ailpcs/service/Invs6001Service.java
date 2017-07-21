package com.ailpcs.service;

import java.util.List;
import java.util.Map;

import com.ailpcs.entity.Invs6001DO;
import com.ailpcs.entity.core.Page;
import com.ailpcs.entity.core.Page2;
import com.ailpcs.entity.core.PageData;


/** 
 * Invs6001业务层接口.  文章表2（行情资讯的四个板块-实时快讯、财经日历、实盘策略、黄金讲堂）
 * 创建人：MichaelTsui
 * 创建时间：2017-07-10
 * @version 1.0
 */
public interface Invs6001Service {
    /**
	  *1 of 7 invs6001分页列表
	  */
    List<Invs6001DO> listPageInvs6001(Page page) throws Exception;
    Map<String, Object> selectPageListInvs6001(Page2 page2) throws Exception;
    /**
	  *2 of 7 invs6001列表(全部), 用于导出到Excel
 	  */
    List<PageData> listAllInvs6001(PageData pd) throws Exception;
    /**
      *3 of 7 invs6001新增
	  */
    void saveInvs6001(Invs6001DO pdInvs6001) throws Exception;
    /**
	 *4 of 7  invs6001删除
	 */
	void deleteInvs6001(String docId) throws Exception;
	/**
	 *5 of 7  invs6001修改
	 */
	void editInvs6001(Invs6001DO pdInvs6001) throws Exception; 
    /**
	 *6 of 7  通过id获取invs6001数据
	 */
	Invs6001DO findInvs6001ById(String docId) throws Exception;
	/**
	 *7 of 7  invs6001批量删除
	 */
	void deleteBatchInvs6001(String[] pks) throws Exception;
}

