package com.ailpcs.service.impl;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.ailpcs.dao.Dao2;
import com.ailpcs.entity.Invs6001DO;
import com.ailpcs.entity.core.Page;
import com.ailpcs.entity.core.Page2;
import com.ailpcs.entity.core.PageData;
import com.ailpcs.service.Invs6001Service;
import com.ailpcs.util.UuidUtil;
import com.ailpcs.core.Jurisdiction;
import com.ailpcs.core.MyExceptionBusiness;

/** 
 * Invs6001业务层实现类.  文章表2（行情资讯的四个板块-实时快讯、财经日历、实盘策略、黄金讲堂）
 * 创建人：MichaelTsui
 * 创建时间：2017-07-10
 * @version 1.0
 */
@Service("invs6001Service")
public class Invs6001ServiceImpl extends BaseServiceImpl implements Invs6001Service {
	@Resource(name = "dao2")
	private Dao2<Invs6001DO> daoInvs6001;
	@Resource(name = "dao2")
	private Dao2<PageData> daoPD;

	@Override  
	public List<Invs6001DO> listPageInvs6001(Page page) throws Exception {
	    List<Invs6001DO> res = new ArrayList<Invs6001DO>();
	    try {
	        res = daoInvs6001.listFromDb("Invs6001Mapper.listPage", page);
	    } catch (Exception e) {
			throw new MyExceptionBusiness(e);
		}
		return res; 
	}	
	@Override  
	public Map<String, Object> selectPageListInvs6001(Page2 page2) throws Exception {
		Map<String , Object> result = new HashMap<String , Object>();
        List<Invs6001DO> rows = daoInvs6001.listFromDb("Invs6001Mapper.selectPageList", page2);
        Integer total = daoInvs6001.countFromDb("Invs6001Mapper.countListTotal", page2);
        result.put("rows", rows);
		result.put("total", total);
		return result;
	}
 	@Override  
	public List<PageData> listAllInvs6001(PageData pd) throws Exception {
	    List<PageData> res = new ArrayList<PageData>();
	    try {
	        res = daoPD.listFromDb("Invs6001Mapper.listAll", pd);
	    } catch (Exception e) {
			throw new MyExceptionBusiness(e);
		}
		return res;
	}
	@Override 
	public void saveInvs6001(Invs6001DO pdInvs6001) throws Exception {
	    pdInvs6001.setDocId(UuidUtil.get32UUID());
	    pdInvs6001.setInsertUser(Jurisdiction.getUsername());	
	    Date dd = new Date();
	    pdInvs6001.setInsertDate(dd); 
	    pdInvs6001.setUpdateDate(new Date()); 
		try {
	        daoInvs6001.saveDb("Invs6001Mapper.insertSelective", pdInvs6001);
	    } catch (Exception e) {
			throw new MyExceptionBusiness(e);
		}
	}
	@Override 
	public void deleteInvs6001(String docId) throws Exception {
	    try {
	        daoInvs6001.deleteDb2("Invs6001Mapper.deleteByPrimaryKey", docId);
	    } catch (Exception e) {
			throw new MyExceptionBusiness(e);
		}
	}		
	@Override 
	public void editInvs6001(Invs6001DO pdInvs6001) throws Exception {
		if (StringUtils.isBlank(pdInvs6001.getOrtherUrl())) {
			pdInvs6001.setOrtherUrl("{}");
		}
	    pdInvs6001.setUpdateUser(Jurisdiction.getUsername());
	    pdInvs6001.setUpdateDate(new Date()); 
		try {
	        daoInvs6001.updateDb("Invs6001Mapper.updateByPrimaryKeySelective", pdInvs6001);
	    } catch (Exception e) {
			throw new MyExceptionBusiness(e);
		}
	}
	@Override 
	public Invs6001DO findInvs6001ById(String docId) throws Exception {
	    Invs6001DO res = new Invs6001DO();
	    try {
	        res = daoInvs6001.getFromDb("Invs6001Mapper.selectByPrimaryKey", docId);
	    } catch (Exception e) {
			throw new MyExceptionBusiness(e);
		}
	    return res;
	}		
	@Override 
	public void deleteBatchInvs6001(String[] pks) throws Exception {
		try {
	        daoInvs6001.deleteDb2("Invs6001Mapper.deleteBatch", pks);
	    } catch (Exception e) {
			throw new MyExceptionBusiness(e);
		}
	}
}

