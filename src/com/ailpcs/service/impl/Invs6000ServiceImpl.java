package com.ailpcs.service.impl;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ailpcs.dao.Dao2;
import com.ailpcs.entity.Invs6000DO;
import com.ailpcs.entity.core.Page;
import com.ailpcs.entity.core.PageData;
import com.ailpcs.service.Invs6000Service;
import com.ailpcs.util.UuidUtil;
import com.ailpcs.core.Jurisdiction;
import com.ailpcs.core.MyExceptionBusiness;

/** 
 * Invs6000业务层实现类.  文章表1（公告，热门活动，常见问题等）
 * 创建人：MichaelTsui
 * 创建时间：2017-07-10
 * @version 1.0
 */
@Service("invs6000Service")
public class Invs6000ServiceImpl extends BaseServiceImpl implements Invs6000Service {
	@Resource(name = "dao2")
	private Dao2<Invs6000DO> daoInvs6000;
	@Resource(name = "dao2")
	private Dao2<PageData> daoPD;

	@Override  
	public List<Invs6000DO> listPageInvs6000(Page page) throws Exception {
	    List<Invs6000DO> res = new ArrayList<Invs6000DO>();
	    try {
	        res = daoInvs6000.listFromDb("Invs6000Mapper.listPage", page);
	    } catch (Exception e) {
			throw new MyExceptionBusiness(e);
		}
		return res; 
	}		
 	@Override  
	public List<PageData> listAllInvs6000(PageData pd) throws Exception {
	    List<PageData> res = new ArrayList<PageData>();
	    try {
	        res = daoPD.listFromDb("Invs6000Mapper.listAll", pd);
	    } catch (Exception e) {
			throw new MyExceptionBusiness(e);
		}
		return res;
	}
	@Override 
	public void saveInvs6000(Invs6000DO pdInvs6000) throws Exception {
	    pdInvs6000.setDocId(UuidUtil.get32UUID());
	    pdInvs6000.setInsertUser(Jurisdiction.getUsername());	
	    Date dd = new Date();
	    pdInvs6000.setInsertDate(dd); 
	    pdInvs6000.setUpdateDate(new Date()); 
		try {
	        daoInvs6000.saveDb("Invs6000Mapper.insertSelective", pdInvs6000);
	    } catch (Exception e) {
			throw new MyExceptionBusiness(e);
		}
	}
	@Override 
	public void deleteInvs6000(String docId) throws Exception {
	    try {
	        daoInvs6000.deleteDb2("Invs6000Mapper.deleteByPrimaryKey", docId);
	    } catch (Exception e) {
			throw new MyExceptionBusiness(e);
		}
	}		
	@Override 
	public void editInvs6000(Invs6000DO pdInvs6000) throws Exception {
	    pdInvs6000.setUpdateUser(Jurisdiction.getUsername());
	    pdInvs6000.setUpdateDate(new Date()); 
		try {
	        daoInvs6000.updateDb("Invs6000Mapper.updateByPrimaryKeySelective", pdInvs6000);
	    } catch (Exception e) {
			throw new MyExceptionBusiness(e);
		}
	}
	@Override 
	public Invs6000DO findInvs6000ById(String docId) throws Exception {
	    Invs6000DO res = new Invs6000DO();
	    try {
	        res = daoInvs6000.getFromDb("Invs6000Mapper.selectByPrimaryKey", docId);
	    } catch (Exception e) {
			throw new MyExceptionBusiness(e);
		}
	    return res;
	}		
	@Override 
	public void deleteBatchInvs6000(String[] pks) throws Exception {
		try {
	        daoInvs6000.deleteDb2("Invs6000Mapper.deleteBatch", pks);
	    } catch (Exception e) {
			throw new MyExceptionBusiness(e);
		}
	}
}

