package com.ailpcs.core;
/**  
 * 我们自定义的系统业务异常  
 * 创建人：MichaelTsui
 * 创建时间：2017-07-09
 */ 
public class MyExceptionBusiness extends RuntimeException {
	/** serialVersionUID */    
    private static final long serialVersionUID = 2332608236621015980L;    
    
    private String code;    
    
    public MyExceptionBusiness() {    
        super();    
    }    
    
    public MyExceptionBusiness(String message) {    
        super(message);    
    }    
    
    public MyExceptionBusiness(String code, String message) {    
        super(message);    
        this.code = code;    
    }    
    
    public MyExceptionBusiness(Throwable cause) {    
        super(cause);    
    }    
    
    public MyExceptionBusiness(String message, Throwable cause) {    
        super(message, cause);    
    }    
    
    public MyExceptionBusiness(String code, String message, Throwable cause) {    
        super(message, cause);    
        this.code = code;    
    }    
    
    public String getCode() {    
        return code;    
    }    
    
    public void setCode(String code) {    
        this.code = code;    
    }    
}
