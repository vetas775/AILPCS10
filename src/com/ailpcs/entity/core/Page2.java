package com.ailpcs.entity.core;

import java.io.Serializable;

import com.ailpcs.core.MyStartFilter;

/**
 * 分页对象2
 */
public class Page2 implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer rows; //每页条数
	private Integer page = 1;// 当前页码，与datagrid请求的参数名一致，便于查询时赋值，默认第一页
	private String  sort;		//排序关键字
	private String  order;		//asc正序，desc倒序
	private Integer start;// 从第几条开始，记录的下标从0开始计算
	private PageData pd = new PageData(); 
	
	public Page2(){
		try {
		    this.rows = Integer.parseInt(MyStartFilter.sp.get("COUNTPAGE"));  //读取默认每页资料笔数 Const.PAGE
		} catch (Exception e) {
			this.rows = 15;
		}
	}

	public Integer getRows() {
		return rows;
	}
	public void setRows(Integer rows) {
		this.rows = rows;
	}
	public Integer getPage() {
		return page;
	}	
	public void setPage(Integer page) {
		this.page = page;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public Integer getStart() {
		start= (page-1)*rows;
		return start;
	}
	public void setStart(Integer start) {
		this.start = start;
	}	
	public PageData getPd() {
		return pd;
	}
	public void setPd(PageData pd) {
		this.pd = pd;
	}
	
}
