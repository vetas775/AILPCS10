package com.ailpcs.entity.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
/** 
 * Request请求参数封装Map, Request为空时直接返回一个空的HashMap对象 
 * Create by: FH 14/09/20 
 * Amend by: MichaelTsui 17/06/17 
 */
public class PageData extends HashMap<Object, Object> implements Map<Object, Object>{
	
	private static final long serialVersionUID = 1L;
	private Map<Object, Object> map = null;
	private HttpServletRequest request;

	public PageData(HttpServletRequest request){
		//封装request参数
		Map<Object, Object> sResultMap = new HashMap<Object, Object>();		
		for(Map.Entry<String, String[]> sEntry : request.getParameterMap().entrySet()) {
			String sKey = sEntry.getKey();
	    	String[] valueObj = (String[])sEntry.getValue();
	    	if (null == valueObj) {
	    		sResultMap.put(sKey, null); 
	    	} else if (valueObj.length == 1){
	    		sResultMap.put(sKey, valueObj[0].toString()); 
	    	} else {
	    		String sValue = ""; 
			    for(int i = 0; i < valueObj.length; i++) { 
				    sValue = valueObj[i] + ",";	
			    }
			    sValue = sValue.substring(0, sValue.length()-1);  
			    sResultMap.put(sKey, sValue); 
	    	}     	
		}
		/*
		Iterator<Entry<String, String[]>> sIte = request.getParameterMap().entrySet().iterator(); 
		while (sIte.hasNext()) {
			Map.Entry<String, String[]> sEntry = sIte.next(); 
			String sKey = sEntry.getKey(); 
			Object valueObj = sEntry.getValue(); 
			String sValue = ""; 
			if(null != valueObj){ 
				if(valueObj instanceof String[]){ 
				    String[] values = (String[])valueObj;
				    for(int i = 0; i < values.length; i++) { 
					    sValue = values[i] + ",";	
				    }
				    sValue = sValue.substring(0, sValue.length()-1); 
			    }else{
				    sValue = valueObj.toString(); 
			    }
			}
			sResultMap.put(sKey, sValue); 
		}
		*/
		this.map = sResultMap;
		this.request = request;
	}
	
	public PageData() {
		map = new HashMap<Object, Object>();
	}
	
	@Override
	public Object get(Object key) {
		Object obj = null;
		if(map.get(key) instanceof Object[]) {
			Object[] arr = (Object[])map.get(key);
			obj = (request == null) ? arr : (request.getParameter((String)key) == null ? arr:arr[0]);
		} else {
			obj = map.get(key);
		}
		return obj;
	}
	
	public String getString(Object key) {
		return (String)get(key);
	}
	
//	@SuppressWarnings("unchecked")
	@Override
	public Object put(Object key, Object value) {
		return map.put(key, value);
	}
	
	@Override
	public Object remove(Object key) {
		return map.remove(key);
	}

	public void clear() {
		map.clear();
	}

	public boolean containsKey(Object key) {
		// TODO Auto-generated method stub
		return map.containsKey(key);
	}

	public boolean containsValue(Object value) {
		// TODO Auto-generated method stub
		return map.containsValue(value);
	}

	public Set<Map.Entry<Object, Object>> entrySet() {
		// TODO Auto-generated method stub
		return map.entrySet();
	}

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return map.isEmpty();
	}

	public Set<Object> keySet() {
		// TODO Auto-generated method stub
		return map.keySet();
	}

//	@SuppressWarnings("unchecked")
	public void putAll(Map<?,?> t) {  //把Map t合并到map，如果有相同的key那么Map t里的会覆盖map里的
		// TODO Auto-generated method stub
		map.putAll(t);
	}

	public int size() {
		// TODO Auto-generated method stub
		return map.size();
	}

	public Collection<Object> values() {
		// TODO Auto-generated method stub
		return map.values();
	}
	
}
