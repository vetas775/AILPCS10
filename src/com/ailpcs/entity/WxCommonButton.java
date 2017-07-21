package com.ailpcs.entity;

/** 微信菜单: 二级菜单
 * Amend by MichaelTsui17/06/20
 */
public class WxCommonButton extends WxButton{
	
	private String type;		//菜单类型
	private String key;			//key值
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
}
