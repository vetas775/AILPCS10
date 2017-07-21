package com.ailpcs.core;
/**
 * 我们自定义的参数异常
 * 创建人：MichaelTsui
 * 创建时间：2017-07-09
 */

public class MyExceptionParameter extends RuntimeException {
	/** serialVersionUID */    
    private static final long serialVersionUID = 6417641452178955756L;    
    
    public MyExceptionParameter() {    
        super();    
    }    
    
    public MyExceptionParameter(String message) {    
        super(message);    
    }    
    
    public MyExceptionParameter(Throwable cause) {    
        super(cause);    
    }    
    
    public MyExceptionParameter(String message, Throwable cause) {    
        super(message, cause);    
    }    
}
