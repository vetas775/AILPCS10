package com.ailpcs.entity.core;

import java.io.Serializable;

/**
 * 系统用户(含角色)sys_user/sys_role 
 * MichaelTsui16/06/23
 */
public class UserDO implements Serializable{
	private static final long serialVersionUID = 1L;	
	private String userId;		    //用户id  sys_user.USER_ID
	private String userName;	    //用户名       sys_user.USERNAME
	private String userPassword;    //密码           sys_user.PASSWORD
	private String userRealName;	//真实姓名    sys_user.NAME
	private String userRights;	    //权限           sys_user.RIGHTS
	private String userRoleId;		//角色id   sys_user.ROLE_ID 
	private String userLastLogin;	//最后登录时间   sys_user.LAST_LOGIN
	private String userLastIp;		//用户登录ip地址  sys_user.IP
	private String userStatus;		//状态  sys_user.STATUS
	private String userRemark;		//备注  sys_user.BZ
	private String userSkin;		//当前皮肤 sys_user.SKIN
	private String userEmail;		//邮箱  sys_user.EMAIL
	private String userSerialNo;	//用户编号  sys_user.NUMBER
	private String userPhone;	    //用户手机 sys_user.PHONE
	
	private Page page;			    //分页对象
	private RoleDO role;			//该User的角色对象
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }
    public String getUserPassword() {
        return userPassword;
    }
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword == null ? null : userPassword.trim();
    }
    public String getUserRealName() {
        return userRealName;
    }
    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName == null ? null : userRealName.trim();
    }
    public String getUserRights() {
        return userRights;
    }
    public void setUserRights(String userRights) {
        this.userRights = userRights == null ? null : userRights.trim();
    }
    public String getUserRoleId() {
        return userRoleId;
    }
    public void setUserRoleId(String userRoleId) {
        this.userRoleId = userRoleId == null ? null : userRoleId.trim();
    }
    public String getUserLastLogin() {
        return userLastLogin;
    }
    public void setUserLastLogin(String userLastLogin) {
        this.userLastLogin = userLastLogin == null ? null : userLastLogin.trim();
    }
    public String getUserLastIp() {
        return userLastIp;
    }
    public void setUserLastIp(String userLastIp) {
        this.userLastIp = userLastIp == null ? null : userLastIp.trim();
    }
    public String getUserStatus() {
        return userStatus;
    }
    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus == null ? null : userStatus.trim();
    }
    public String getUserRemark() {
        return userRemark;
    }
    public void setUserRemark(String userRemark) {
        this.userRemark = userRemark == null ? null : userRemark.trim();
    }
    public String getUserSkin() {
        return userSkin;
    }
    public void setUserSkin(String userSkin) {
        this.userSkin = userSkin == null ? null : userSkin.trim();
    }
    public String getUserEmail() {
        return userEmail;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail == null ? null : userEmail.trim();
    }
    public String getUserSerialNo() {
        return userSerialNo;
    }
    public void setUserSerialNo(String userSerialNo) {
        this.userSerialNo = userSerialNo == null ? null : userSerialNo.trim();
    }
    public String getUserPhone() {
        return userPhone;
    }
    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone == null ? null : userPhone.trim();
    }
	public Page getPage() {
		if(page==null)
			page = new Page();
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	public RoleDO getRole() {
		return role;
	}
	public void setRole(RoleDO role) {
		this.role = role;
	}
	
}
