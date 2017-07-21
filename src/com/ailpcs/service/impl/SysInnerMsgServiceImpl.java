package com.ailpcs.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.ailpcs.core.Jurisdiction;
import com.ailpcs.dao.Dao2;
import com.ailpcs.entity.core.Page;
import com.ailpcs.entity.core.PageData;
import com.ailpcs.service.SysInnerMsgService;
import com.ailpcs.util.UuidUtil;
import com.ailpcs.util.fh.DateUtil;
/** 
 * 说明： 站内信
 */
@Service("sysInnerMsgService")
public class SysInnerMsgServiceImpl implements SysInnerMsgService{
	@Resource(name = "dao2")
	private Dao2<PageData> daoPD;
	
	@Override
	public Map<String, Object> listUserMsg(Page page, PageData pd) throws Exception{
		Map<String, Object> resMap = new HashMap<String, Object>();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(StringUtils.isNotBlank(keywords)) {
			pd.put("keywords", keywords.trim());
		}
		String lastLoginStart = pd.getString("lastLoginStart");	//开始时间
		String lastLoginEnd = pd.getString("lastLoginEnd");		//结束时间
		if(StringUtils.isNotBlank(lastLoginStart)) {
			pd.put("lastLoginStart", lastLoginStart+" 00:00:00");
		}
		if(StringUtils.isNotBlank(lastLoginEnd)) {
			pd.put("lastLoginEnd", lastLoginEnd+" 00:00:00");
		}
		if(StringUtils.isBlank(pd.getString("TYPE")) || !"2".equals(pd.getString("TYPE"))){		//1：收信箱 2：发信箱
			pd.put("TO_USERNAME", Jurisdiction.getUsername()); 	//看收信
		} else {
			pd.put("FROM_USERNAME", Jurisdiction.getUsername()); 	//看发信
		}
		
		page.setPd(pd);			

		List<PageData>	varList = daoPD.listFromDb("FhsmsMapper.datalistPage", page); //sysInnerMsgService.listUserMsg(page);		//列出Fhsms列表
		resMap.put("pd", pd);
		resMap.put("varList", varList);
		//return (List<PageData>)dao.findForList("FhsmsMapper.datalistPage", page);
		return resMap;
	}
	
	@Override
	public List<PageData> sendInnerMessage(PageData pd)throws Exception{
		List<PageData> pdList = new ArrayList<PageData>();
		String msg = "ok";		//发送状态
		int count = 0;			//统计发送成功条数
		int zcount = 0;			//理论条数
		String userNameList = pd.getString("USERNAME");		//收件人列表
		if(StringUtils.isNotBlank(userNameList)) {
			userNameList = userNameList.replaceAll("；", ";");
			userNameList = userNameList.replaceAll(" ", "");
			String[] arrUserName = userNameList.split(";");
			zcount = arrUserName.length;
			try {
				pd.put("STATUS", "2");										//状态 2-未读  1-已读
				for(int i=0;i<arrUserName.length;i++){
					pd.put("SANME_ID", UuidUtil.get32UUID());				//共同ID
					pd.put("SEND_TIME", DateUtil.getTime());				//发送时间
					pd.put("FHSMS_ID", UuidUtil.get32UUID());				//主键1
					pd.put("TYPE", "0");									//类型2：发信
					pd.put("FROM_USERNAME", Jurisdiction.getUsername());	//发信人
					pd.put("TO_USERNAME", arrUserName[i]);					//收信人
					daoPD.saveDb("FhsmsMapper.save", pd);
					count++;
				}
				msg = "ok";
			} catch (Exception e) {
				msg = "error";
			}
		}else{
			msg = "error";
		}
		pd.put("msg", msg);
		pd.put("count", count);						//成功数
		pd.put("ecount", zcount-count);				//失败数
		pdList.add(pd);
		return pdList;
	}
	@Override
	public void delete(PageData pd)throws Exception{
		daoPD.deleteDb("FhsmsMapper.delete", pd);
	}
	@Override
	public void edit(PageData pd)throws Exception{
		daoPD.updateDb("FhsmsMapper.edit", pd);
	}	
	@Override
	public List<PageData> listAll(PageData pd)throws Exception{
		return daoPD.listFromDb("FhsmsMapper.listAll", pd); 
	}
	@Override
	public PageData findById(PageData pd)throws Exception{
		return daoPD.getFromDb("FhsmsMapper.findById", pd);
	}
	/*
	@Override
	public PageData findFhsmsCount(String USERNAME)throws Exception{
		return (PageData)dao.findForObject("FhsmsMapper.findFhsmsCount", USERNAME);
	}
	*/
	@Override
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		daoPD.deleteDb2("FhsmsMapper.deleteAll", ArrayDATA_IDS);
	}
	
}

