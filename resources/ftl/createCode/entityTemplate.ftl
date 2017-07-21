package com.${packageName}.entity;

import java.io.Serializable;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

/** 
 * ${objectName}实体类.  ${TITLE}
 * 创建人：MichaelTsui
 * 创建时间：${nowDate?string("yyyy-MM-dd")}
 * @version 1.0
 */
public class ${objectName}DO implements Serializable { 
    private static final long serialVersionUID = 1L;
<#list fieldList as var>
  <#if var[6] == 'BIGINT'>
	private Long ${var[4]};  //${var[2]}
  <#elseif var[6] == 'INTEGER'>
	private Integer ${var[4]};  //${var[2]}
  <#elseif var[6] == 'DECIMAL'>
	private BigDecimal ${var[4]};  //${var[2]}
  <#elseif var[6] == 'TIMESTAMP'>
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date ${var[4]};  //${var[2]}	
	<#if var[4] != 'insertDate' && var[4] != 'updateDate'>
	private String ${var[4]}ClearFlag;  //虚拟字段，用于update时清除日期栏位的操作, 详见mapper里的updateByPrimaryKeySelective, 可视状况启用或删除 
	</#if>	
  <#else>
	private String ${var[4]};  //${var[2]}
  </#if>
</#list>    
    
    public static long getSerialversionuid() {
		return serialVersionUID;
	}
<#list fieldList as var>
  <#if var[6] == 'BIGINT'>
	public Long get${var[7]}() {
		return ${var[4]};
	}
	public void set${var[7]}(Long ${var[4]}) {
		this.${var[4]} = ${var[4]};
	}
  <#elseif var[6] == 'INTEGER'>
	public Integer get${var[7]}() {
		return ${var[4]};
	}
	public void set${var[7]}(Integer ${var[4]}) {
		this.${var[4]} = ${var[4]};
	}
  <#elseif var[6] == 'DECIMAL'>
	public BigDecimal get${var[7]}() {
		return ${var[4]};
	}
	public void set${var[7]}(BigDecimal ${var[4]}) {
		this.${var[4]} = ${var[4]};
	}
  <#elseif var[6] == 'TIMESTAMP'>
	public Date get${var[7]}() {
		return ${var[4]};
	}
	public void set${var[7]}(Date ${var[4]}) {
		this.${var[4]} = ${var[4]};
	}
	<#if var[4] != 'insertDate' && var[4] != 'updateDate'>
	public String get${var[7]}ClearFlag() {
		return ${var[4]}ClearFlag;
	}
	public void set${var[7]}ClearFlag(String ${var[4]}ClearFlag) {
		this.${var[4]}ClearFlag = ${var[4]}ClearFlag == null ? null : ${var[4]}ClearFlag.trim(); 
	}
	</#if>
  <#else>
	public String get${var[7]}() {
		return ${var[4]};
	}
	public void set${var[7]}(String ${var[4]}) {
		this.${var[4]} = ${var[4]} == null ? null : ${var[4]}.trim(); 
	}
  </#if>
</#list> 
    @Override
	public String toString() {
		return "${objectName}DO ["
<#list fieldList as var>		
		  + ", ${var[4]}=" + ${var[4]}
</#list>				
		  + "]";
	}
}
