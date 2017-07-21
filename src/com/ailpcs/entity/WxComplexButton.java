package com.ailpcs.entity;

/** 微信菜单: 一级菜单
 * Amend by MichaelTsui 17/06/20
 */
public class WxComplexButton {
	
	private String name;			//菜单名称
	private WxButton[] sub_button;	//子级菜单
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public WxButton[] getSub_button() {
		return sub_button;
	}
	public void setSub_button(WxButton[] sub_button) {
		this.sub_button = sub_button;
	}
}