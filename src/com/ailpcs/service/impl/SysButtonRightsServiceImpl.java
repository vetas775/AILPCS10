package com.ailpcs.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ailpcs.dao.Dao2;
import com.ailpcs.entity.core.PageData;
import com.ailpcs.service.SysButtonRightsService;

/** 
 * 说明： 按钮权限
 */
@Service("buttonrightsService")
public class SysButtonRightsServiceImpl implements SysButtonRightsService{
	@Resource(name = "dao2")
	private Dao2<PageData> daoPD;
	
	@Override
	public void save(PageData pd)throws Exception {
		daoPD.saveDb("ButtonrightsMapper.save", pd);
	}
	@Override
	public PageData findById(PageData pd) throws Exception {
		return (PageData)daoPD.getFromDb("ButtonrightsMapper.findById", pd);
	}
	@Override
	public void delete(PageData pd)throws Exception{
		daoPD.deleteDb("ButtonrightsMapper.delete", pd);
	}
	@Override
	public List<PageData> listAll(PageData pd)throws Exception{
		return daoPD.listFromDb("ButtonrightsMapper.listAll", pd);
	}
	@Override
	public List<PageData> listAllBrAndQxname(PageData pd)throws Exception{
		return daoPD.listFromDb("ButtonrightsMapper.listAllBrAndQxname", pd);
	}
}

