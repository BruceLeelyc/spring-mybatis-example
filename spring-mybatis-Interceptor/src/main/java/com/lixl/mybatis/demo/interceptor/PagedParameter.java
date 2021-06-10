package com.lixl.mybatis.demo.interceptor;

public class PagedParameter extends BaseBean implements DomainBean {

	private static final long serialVersionUID = 1L;

	/**
	 * 页号
	 */
	private Integer p;

	/**
	 * 单页大小(rows)
	 */
	private Integer size;

	/**
	 * 起始行号(start)
	 */
	private Integer offset;

	//---- 页号 ---
	public Integer getP() {
		return p;
	}

	public void setP(Integer p) {
		this.p = p;
	}

	//---- 页号别名 ---
	public Integer getPageIndex() {
		return getP();
	}

	public void setPageIndex(Integer pageIndex) {
		setP(pageIndex);
	}

	//---- 单页大小 ---
	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	//---- 单页大小别名 ---
	public Integer getPageSize() {
		return getSize();
	}

	public void setPageSize(Integer pageSize) {
		setSize(pageSize);
	}

	public Integer getRows() {
		return getSize();
	}

	public void setRows(Integer rows) {
		setSize(rows);
	}

	//------ offset -----
	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	//------ offset别名 -----
	public Integer getStart() {
		return getOffset();
	}

	public void setStart(Integer start) {
		setOffset(start);
	}
}
