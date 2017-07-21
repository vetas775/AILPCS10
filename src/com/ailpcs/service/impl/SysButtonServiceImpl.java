package com.ailpcs.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ailpcs.dao.Dao2;
import com.ailpcs.entity.core.Page;
import com.ailpcs.entity.core.PageData;
import com.ailpcs.service.SysButtonService;
/** 
 * 说明： 按钮管理 
 */
@Service("sysButtonService")
public class SysButtonServiceImpl implements SysButtonService{
	@Resource(name = "dao2")
	private Dao2<PageData> daoPD;
	
	@Override
	public void save(PageData pd)throws Exception{
		//dao.save("FhbuttonMapper.save", pd);
		daoPD.saveDb("FhbuttonMapper.save", pd);
	}
	@Override
	public void delete(PageData pd)throws Exception{
		//dao.delete("FhbuttonMapper.delete", pd);
		daoPD.deleteDb("FhbuttonMapper.delete", pd);
	}
	@Override
	public void edit(PageData pd)throws Exception{
		//dao.update("FhbuttonMapper.edit", pd);
		daoPD.updateDb("FhbuttonMapper.edit", pd);
	}
	@Override
	//@SuppressWarnings("unchecked")
	public List<PageData> listPage(Page page)throws Exception{
		//return (List<PageData>)dao.findForList("FhbuttonMapper.datalistPage", page);
		return daoPD.listFromDb("FhbuttonMapper.listPage", page);
	}
	@Override
	//@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		//return (List<PageData>)dao.findForList("FhbuttonMapper.listAll", pd);
		return daoPD.listFromDb("FhbuttonMapper.listAll", pd);
	}
	@Override
	public PageData findById(PageData pd)throws Exception{
		//return (PageData)dao.findForObject("FhbuttonMapper.findById", pd);
		return daoPD.getFromDb("FhbuttonMapper.findById", pd);
	}
	@Override
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		//dao.delete("FhbuttonMapper.deleteAll", ArrayDATA_IDS);
		daoPD.deleteDb2("FhbuttonMapper.deleteAll", ArrayDATA_IDS);
	}
	
}

