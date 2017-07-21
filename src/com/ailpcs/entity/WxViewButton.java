package com.ailpcs.entity;


/** 微信菜单: view类型的菜单对象
 * Amend by MichaelTsui 17/06/20
 */
public class WxViewButton extends WxButton{
	private String type;		//菜单类型
	private String url;			//view路径值
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
