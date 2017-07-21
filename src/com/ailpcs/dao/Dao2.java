package com.ailpcs.dao;

import java.util.List;

/**
 * MichaelTsui17/06/21 改用泛型重构 DAO接口和实现
 */
public interface Dao2<T> {
	/**
	 * Select 单笔记录
	 */
	T getFromDb(String str, Object t) throws Exception;
	
	/**
	 * Select 统计资料笔数
	 */
	Integer countFromDb(String str, Object t) throws Exception;
	
	/**
	 * Select 多笔记录
	 */
	List<T> listFromDb(String str, Object t) throws Exception;
	
	/**
	 * 查找对象封装成Map
	public T findForMap(String sql, T t, String key , String value) throws Exception;
	*/
	
	/**
	 * Insert 单笔记录
	 */
	int saveDb(String str, T t) throws Exception;
	
	/**
	 * Insert 多笔记录（批量保存）
	 */
	int saveListDb(String str, List<T> objs )throws Exception;
	
	/**
	 * update 单笔记录
	 */
	int updateDb(String str, Object obj) throws Exception;
	
	/**
	 * update 多笔记录
	 */
	void updateListDb(String str, List<T> objs ) throws Exception;
	
	/**
	 * delete 单笔记录
	 */
	int deleteDb(String str, T t) throws Exception;
	int deleteDb2(String str, Object obj) throws Exception;
	
	/**
	 * delete 多笔记录
	 */
	int deleteListDb(String str, List<T> objs )throws Exception;	
}
