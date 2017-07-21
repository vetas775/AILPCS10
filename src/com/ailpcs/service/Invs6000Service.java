package com.ailpcs.service;

import java.util.List;
import com.ailpcs.entity.Invs6000DO;
import com.ailpcs.entity.core.Page;
import com.ailpcs.entity.core.PageData;

/** 
 * Invs6000业务层接口.  文章表1（公告，热门活动，常见问题等）
 * 创建人：MichaelTsui
 * 创建时间：2017-07-10
 * @version 1.0
 */
public interface Invs6000Service {
    /**
	  *1 of 7 invs6000分页列表
	  */
    List<Invs6000DO> listPageInvs6000(Page page) throws Exception;
    /**
	  *2 of 7 invs6000列表(全部), 用于导出到Excel
 	  */
    List<PageData> listAllInvs6000(PageData pd) throws Exception;
    /**
      *3 of 7 invs6000新增
	  */
    void saveInvs6000(Invs6000DO pdInvs6000) throws Exception;
    /**
	 *4 of 7  invs6000删除
	 */
	void deleteInvs6000(String docId) throws Exception;
	/**
	 *5 of 7  invs6000修改
	 */
	void editInvs6000(Invs6000DO pdInvs6000) throws Exception; 
    /**
	 *6 of 7  通过id获取invs6000数据
	 */
	Invs6000DO findInvs6000ById(String docId) throws Exception;
	/**
	 *7 of 7  invs6000批量删除
	 */
	void deleteBatchInvs6000(String[] pks) throws Exception;
}

