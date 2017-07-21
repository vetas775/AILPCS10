package com.ailpcs.entity.core;

import java.io.Serializable;

/**
 * 角色表DO (sys_role) 
 */
public class RoleDO implements Serializable{
	private static final long serialVersionUID = 1L;	
	private String roleId;      //sys_role.ROLE_ID 
	private String roleName;    //sys_role.ROLE_NAME 
	private String roleRights;  //sys_role.RIGHTS
	private String parentId;    //sys_role.PARENT_ID
	private String addQx;       //sys_role.ADD_QX
	private String delQx;       //sys_role.DEL_QX
	private String editQx;      //sys_role.EDIT_QX
	private String chaQx;       //sys_role.CHA_QX
	
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId == null ? null : roleId.trim();
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName == null ? null : roleName.trim();
	}
	public String getRoleRights() {
		return roleRights;
	}
	public void setRoleRights(String roleRights) {
		this.roleRights = roleRights == null ? null : roleRights.trim();
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId == null ? null : parentId.trim();
	}
	public String getAddQx() {
		return addQx;
	}
	public void setAddQx(String addQx) {
		this.addQx = addQx == null ? null : addQx.trim();
	}
	public String getDelQx() {
		return delQx;
	}
	public void setDelQx(String delQx) {
		this.delQx = delQx == null ? null : delQx.trim();
	}
	public String getEditQx() {
		return editQx;
	}
	public void setEditQx(String editQx) {
		this.editQx = editQx == null ? null : editQx.trim();
	}
	public String getChaQx() {
		return chaQx;
	}
	public void setChaQx(String chaQx) {
		this.chaQx = chaQx == null ? null : chaQx.trim();
	}	
}
