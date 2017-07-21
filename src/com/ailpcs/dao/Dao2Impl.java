package com.ailpcs.dao;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

/**
 * MichaelTsui17/06/21 改用泛型重构 DAO接口和实现
 */

@Repository("dao2")
public class Dao2Impl<T> implements Dao2<T> {
	@Resource(name = "sqlSessionTemplate")
	private SqlSessionTemplate sqlSessionTemplate;	
	
	@Override
	public T getFromDb(String str, Object t) throws Exception {
		return sqlSessionTemplate.selectOne(str, t);
	}
	
	@Override
	public Integer countFromDb(String str, Object obj) throws Exception {
		return sqlSessionTemplate.selectOne(str, obj);
	}
	
	@Override
	public List<T> listFromDb(String str, Object t) throws Exception {
		return sqlSessionTemplate.selectList(str, t);
	}
	/*	
	@Override
	public Object findForMap(String str, Object obj, String key, String value) throws Exception {
		return sqlSessionTemplate.selectMap(str, obj, key);
	}
    */		
	@Override
	public int saveDb(String str, T t) throws Exception {
		return sqlSessionTemplate.insert(str, t);
	}	
	
	@Override
	public int saveListDb(String str, List<T> objs )throws Exception {
		return sqlSessionTemplate.insert(str, objs);
	}
	
	@Override
	public int updateDb(String str, Object obj) throws Exception {
		return sqlSessionTemplate.update(str, obj);
	}
	
	@Override
	public void updateListDb(String str, List<T> objs ) throws Exception {
		SqlSessionFactory sqlSessionFactory = sqlSessionTemplate.getSqlSessionFactory();
		//批量执行器
		SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH,false);
		try{
			if(null != objs){
				for(int i = 0, size = objs.size(); i < size; i++){
					sqlSession.update(str, objs.get(i));
				}
				sqlSession.flushStatements();
				sqlSession.commit();
				sqlSession.clearCache();
			}
		}finally{
			sqlSession.close();
		}
	}
	
	@Override
	public int deleteDb(String str, T t) throws Exception {
		return sqlSessionTemplate.delete(str, t);
	}
	
	@Override
	public int deleteDb2(String str, Object obj) throws Exception {
		return sqlSessionTemplate.delete(str, obj);
	}
	
	@Override
	public int deleteListDb(String str, List<T> objs )throws Exception {
		return sqlSessionTemplate.delete(str, objs);
	}	
	
}


