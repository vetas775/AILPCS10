package com.ailpcs.service.impl;

import com.ailpcs.service.BaseService;
import org.apache.log4j.Logger;

public class BaseServiceImpl implements BaseService {
	
	protected Logger logger = Logger.getLogger(this.getClass());
	
	protected void logBefore(Logger logger, String interfaceName){
		logger.info("");
		logger.info("start");
		logger.info(interfaceName);
	}
	
	protected void logAfter(Logger logger){
		logger.info("end");
		logger.info("");
	}	
}
