package com.ailpcs.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ailpcs.dao.Dao2;
import com.ailpcs.entity.core.PageData;
import com.ailpcs.service.SysUserService;

@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService{
	@Resource(name = "dao2")
	private Dao2<PageData> daoPD;		

	
	
}
