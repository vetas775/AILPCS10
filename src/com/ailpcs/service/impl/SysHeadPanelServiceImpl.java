package com.ailpcs.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.session.Session;
import org.springframework.stereotype.Service;

import com.ailpcs.core.Const;
import com.ailpcs.core.Jurisdiction;
import com.ailpcs.core.MyStartFilter;
import com.ailpcs.core.SystemParameterReadWrite;
import com.ailpcs.dao.Dao2;
import com.ailpcs.entity.core.PageData;
import com.ailpcs.entity.core.UserDO;
import com.ailpcs.service.SysHeadPanelService;
import com.ailpcs.util.AppUtil;

@Service("sysWebHeadPanelService")
public class SysHeadPanelServiceImpl extends BaseServiceImpl implements SysHeadPanelService {
	@Resource(name = "dao2")
	private Dao2<PageData> daoPD;	
	
	@Override
	public Object getHeadPanelList(HttpServletRequest request) {
		PageData pd = new PageData(request);
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			List<PageData> resList = new ArrayList<PageData>();
			Session session = Jurisdiction.getSession();
			UserDO user = (UserDO)session.getAttribute(Const.SESSION_USER);
			PageData pds = new PageData();
			pds.put("NAME", user.getUserRealName()); //真实姓名, 用于画面右上角显示
			pds.put("USERNAME", user.getUserName());
			pds.put("USER_ID", user.getUserId());
			resList.add(pds);
			map.put("resList", resList);

			PageData pdPhoto = (PageData)daoPD.getFromDb("UserPhotoMapper.findById", pds); //userphotoService.findById(pds);
			map.put("userPhoto", (null == pdPhoto) ? "static/ace/avatars/user.jpg" : pdPhoto.getString("PHOTO2"));//用户头像
			map.put("innerMailCount", daoPD.countFromDb("FhsmsMapper.findFhsmsCount", Jurisdiction.getUsername()).toString());//站内信未读总数
			
			map.put("wimadress", MyStartFilter.sp.get("WIMIP") + ":" + MyStartFilter.sp.get("WIMPORT"));	//即时聊天服务器IP和端口
			map.put("oladress", MyStartFilter.sp.get("OLIP") + ":" + MyStartFilter.sp.get("OLPORT"));		//在线管理和站内信服务器IP和端口
			map.put("FHsmsSound", MyStartFilter.sp.get("FHsmsSound"));			            	//站内信提示音效配置			
		} catch (Exception e) {
			logger.error(e.toString(), e);
		} finally {
			
		}
		return AppUtil.returnObject(pd, map);
	}
	
	@Override
	public Map<String,Object> getInnerMailCount() {
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			map.put("innerMailCount", daoPD.countFromDb("FhsmsMapper.findFhsmsCount", Jurisdiction.getUsername()).toString()); //站内信未读总数
		} catch (Exception e) {
			logger.error(e.toString(), e);
		} finally {
			logAfter(logger);
		}
		return map;
	}
	
	@Override
	public void saveSysParameterSetting(PageData pd, int menuIndex) {
		SystemParameterReadWrite.saveSysParameter(pd, menuIndex);
	}
	
	@Override
	public PageData catchAllSysParameter() {
		PageData pd = new PageData();		
		pd.putAll(MyStartFilter.sp);	//读取全部系统参数	
		return pd;
	}
	
}
