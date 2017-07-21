package com.ailpcs.util;

import java.util.Date;

import org.apache.log4j.Logger;

import com.ailpcs.dao.Dao2;
import com.ailpcs.entity.core.PageData;

/**
 * 保存Log信息到数据库表 sys_fhlogm,
 * 由于整个系统很多地方需要用到此功能, 因此改以util形式实现
 * @author TSUI98
 *
 */
public class LogUtil {
	private static Logger logger = Logger.getLogger(LogUtil.class);
	//保存到数据库的操作日志表SYS_FHLOG
	public static void saveLog2Db(Dao2<PageData> dao, String userName, String content ) {
		PageData pd = new PageData();
		pd.put("USERNAME", userName);					//用户名
		pd.put("CONTENT", content);						//事件
		pd.put("FHLOG_ID", UuidUtil.get32UUID());		//主键
		pd.put("CZTIME", Tools.date2Str(new Date()));	//操作时间
		try {
		    dao.saveDb("FHlogMapper.save", pd);
		} catch (Exception e) {		
			logger.error(e.toString(), e);
		}
	}

}
