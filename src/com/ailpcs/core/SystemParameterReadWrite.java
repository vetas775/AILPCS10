package com.ailpcs.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ailpcs.entity.core.PageData;
import com.ailpcs.util.Tools;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
/**
 * 系统设置参数的读写类,
 * @author TSUI98 
 */
public class SystemParameterReadWrite {
	static Logger log = Logger.getLogger(SystemParameterReadWrite.class);
	/**
	 * 读取系统设置参数
	 * 入参menuIndex为0时, 读取并返回全部系统参数, 否则返回指定参数:
	 * 1-系统名称   2-登录页面配置  3.WEBSOCKET配置  4.默认每页资料笔数
	 * 5-邮件服务器设置 6-短信1配置  7-短信2配置  8-读取文字水印配置
	 * 9-读取图片水印配置 10-读取微信配置	
	 */

	public static Map<String, String> catchSysParameter(int menuIdx) {
		
		Map<String, JSONObject> sJOMap = new HashMap<String, JSONObject>();
		switch(menuIdx) {
		case 1: //系统名称
			sJOMap.put(Const.SYSNAME, JSON.parseObject(Tools.readTxtFile(Const.SYSNAME))); //读取系统名称
			break;
		case 2: //登录页面配置
			sJOMap.put(Const.SYSNAME, JSON.parseObject(Tools.readTxtFile(Const.LOGINEDIT))); //读取登录页面配置
			break;
		case 3: //WEBSOCKET配置
			sJOMap.put(Const.WEBSOCKET, JSON.parseObject(Tools.readTxtFile(Const.WEBSOCKET)));//读取WEBSOCKET配置
			break;
		case 4:
			sJOMap.put(Const.PAGE, JSON.parseObject(Tools.readTxtFile(Const.PAGE))); //读取每页条数
			break;
		case 5:
			sJOMap.put(Const.EMAIL, JSON.parseObject(Tools.readTxtFile(Const.EMAIL))); //读取邮件配置
			break;
		case 6:
			sJOMap.put(Const.SMS1, JSON.parseObject(Tools.readTxtFile(Const.SMS1)));  //读取短信1配置
			break;
		case 7:
			sJOMap.put(Const.SMS2, JSON.parseObject(Tools.readTxtFile(Const.SMS2)));  //读取短信2配置
			break;
		case 8:
			sJOMap.put(Const.FWATERM, JSON.parseObject(Tools.readTxtFile(Const.FWATERM)));  //读取文字水印配置
			break;	
		case 9:
			sJOMap.put(Const.IWATERM, JSON.parseObject(Tools.readTxtFile(Const.IWATERM)));  //读取图片水印配置
			break;	
		case 10:
			sJOMap.put(Const.WEIXIN, JSON.parseObject(Tools.readTxtFile(Const.WEIXIN)));  //读取微信配置	
			break;	
		default:
			sJOMap.put(Const.SYSNAME, JSON.parseObject(Tools.readTxtFile(Const.SYSNAME))); //读取系统名称
			sJOMap.put(Const.LOGINEDIT, JSON.parseObject(Tools.readTxtFile(Const.LOGINEDIT))); //读取登录页面配置
			sJOMap.put(Const.WEBSOCKET, JSON.parseObject(Tools.readTxtFile(Const.WEBSOCKET)));//读取WEBSOCKET配置
			sJOMap.put(Const.PAGE, JSON.parseObject(Tools.readTxtFile(Const.PAGE))); //读取每页条数
			sJOMap.put(Const.EMAIL, JSON.parseObject(Tools.readTxtFile(Const.EMAIL))); //读取邮件配置
			sJOMap.put(Const.SMS1, JSON.parseObject(Tools.readTxtFile(Const.SMS1)));  //读取短信1配置
			sJOMap.put(Const.SMS2, JSON.parseObject(Tools.readTxtFile(Const.SMS2)));  //读取短信2配置
			sJOMap.put(Const.FWATERM, JSON.parseObject(Tools.readTxtFile(Const.FWATERM)));  //读取文字水印配置
			sJOMap.put(Const.IWATERM, JSON.parseObject(Tools.readTxtFile(Const.IWATERM))); //读取图片水印配置
			sJOMap.put(Const.WEIXIN, JSON.parseObject(Tools.readTxtFile(Const.WEIXIN)));  //读取微信配置			
		}
		Map<String, String> sResMap = new HashMap<String,String>();
		//遍历获取全部Json object
		Iterator<String> ite = sJOMap.keySet().iterator();
		while (ite.hasNext()) {
			//从各个jsonObject里遍历出系统设置参数，然后添加到Map对象
 		    JSONObject sJsonObj = sJOMap.get(ite.next());
		    for (Map.Entry<String, Object> entry : sJsonObj.entrySet()) {
		    	String sKey = entry.getKey();
		    	String sValue = (String)entry.getValue();
		    	sResMap.put(sKey, sValue);
		    	log.info("读取系统参数+("+menuIdx+")+++++++++++++++++++++++++" + sKey + " = " + sValue);
		    }		    
		}
		return sResMap;		
	}
	/**
	 * 分类保存系统设置参数. menuIndex: 1-系统设置一 , 2-系统设置二...
	 */
	public static void saveSysParameter(PageData pd, int menuIndex) {
		Map<String, String> mapStr = new HashMap<String, String>();
		String str;
		switch (menuIndex) {
		case 4:
			mapStr.put("regFlag", pd.getString("regFlag"));
			mapStr.put("isMusic", pd.getString("isMusic"));
			str = JSON.toJSONString(mapStr);
			Tools.writeFile(Const.LOGINEDIT, str);	//登录页面配置	
			break;
		case 3:
			str = "{\"Token\":\"" + pd.getString("Token") + "\"}";  
			Tools.writeFile(Const.WEIXIN, str);	//写入微信配置			
			//websocket配置
			//Tools.writeFile(Const.WEBSOCKET,pd.getString("WIMIP")+",fh,"+pd.getString("WIMPORT")+",fh,"+pd.getString("OLIP")+",fh,"+pd.getString("OLPORT")+",fh,"+pd.getString("FHsmsSound"));
			mapStr.put("WIMIP", pd.getString("WIMIP"));  //聊天服务器IP
			mapStr.put("WIMPORT", pd.getString("WIMPORT"));  //聊天服务器端口
			mapStr.put("OLIP", pd.getString("OLIP"));  //在线管理服务器IP
			mapStr.put("OLPORT", pd.getString("OLPORT"));  //在线管理服务器端口
			mapStr.put("FHsmsSound", pd.getString("FHsmsSound")); //站内信提示音效配置
			str = JSON.toJSONString(mapStr); 
			Tools.writeFile(Const.WEBSOCKET, str);
			break;
		case 2:
			//文字水印配置
			//Tools.writeFile(Const.FWATERM,pd.getString("isCheck1")+",fh,"+pd.getString("fcontent")+",fh,"+pd.getString("fontSize")+",fh,"+pd.getString("fontX")+",fh,"+pd.getString("fontY"));	
			mapStr.put("isCheck1", pd.getString("isCheck1"));
			mapStr.put("fcontent", pd.getString("fcontent"));
			mapStr.put("fontSize", pd.getString("fontSize"));
			mapStr.put("fontX", pd.getString("fontX"));
			mapStr.put("fontY", pd.getString("fontY"));
			str = JSON.toJSONString(mapStr); 
			Tools.writeFile(Const.FWATERM, str);
			//图片水印配置
			//Tools.writeFile(Const.IWATERM,pd.getString("isCheck2")+",fh,"+pd.getString("imgUrl")+",fh,"+pd.getString("imgX")+",fh,"+pd.getString("imgY"));	
			mapStr.clear();
			mapStr.put("isCheck2", pd.getString("isCheck2"));
			mapStr.put("imgUrl", pd.getString("imgUrl"));
			mapStr.put("imgX", pd.getString("imgX"));
			mapStr.put("imgY", pd.getString("imgY"));
			str = JSON.toJSONString(mapStr); 
			Tools.writeFile(Const.IWATERM, str);				
			break;
		default: //系统设置一
			//写入系统名称
			str = "{\"SYSNAME\":\"" + pd.getString("SYSNAME") + "\"}";  
			Tools.writeFile(Const.SYSNAME, str);	
			//写入每页条数
			//Tools.writeFile(Const.PAGE,pd.getString("COUNTPAGE"));	
			str = "{\"COUNTPAGE\":\"" + pd.getString("COUNTPAGE") + "\"}";  
			Tools.writeFile(Const.PAGE, str);	
			//写入邮件服务器配置
			//Tools.writeFile(Const.EMAIL,pd.getString("SMTP")+",fh,"+pd.getString("PORT")+",fh,"+pd.getString("EMAIL")+",fh,"+pd.getString("PAW"));
			mapStr.put("SMTP", pd.getString("SMTP"));
			mapStr.put("PORT", pd.getString("PORT"));
			mapStr.put("EMAIL", pd.getString("EMAIL"));
			mapStr.put("PAW", pd.getString("PAW"));
			str = JSON.toJSONString(mapStr); 
			Tools.writeFile(Const.EMAIL, str);
			//写入短信1配置
			//Tools.writeFile(Const.SMS1,pd.getString("SMSU1")+",fh,"+pd.getString("SMSPAW1"));	
			mapStr.clear();
			mapStr.put("SMSU1", pd.getString("SMSU1"));
			mapStr.put("SMSPAW1", pd.getString("SMSPAW1"));
			str = JSON.toJSONString(mapStr); 
			Tools.writeFile(Const.SMS1, str);
			//写入短信2配置
			//Tools.writeFile(Const.SMS2,pd.getString("SMSU2")+",fh,"+pd.getString("SMSPAW2"));	
			mapStr.clear();
			mapStr.put("SMSU2", pd.getString("SMSU2"));
			mapStr.put("SMSPAW2", pd.getString("SMSPAW2"));
			str = JSON.toJSONString(mapStr); 
			Tools.writeFile(Const.SMS2, str);
		}
		MyStartFilter.sp.clear();
		MyStartFilter.sp.putAll(catchSysParameter(999)); //<===Important!!! 刷新系统参数到sp
	}
	
	public static void main(String[] args) {
		Map<String,String> sMapWS = catchSysParameter(3);	//3 读取WEBSOCKET配置 Const.WEBSOCKET
		System.out.println(sMapWS);
	}
}
