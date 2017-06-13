package com.github.liyiorg.mbg.bean;

import java.util.List;

/**
 *
 * @author LiYi 2010-09-26
 *
 */
public class Page<T> {
	// 当前页码
	private int pageNo;

	// 每页展现多少条记录
	private int pageSize;

	// 总记录条数
	private long total;

	// 当前记录条数
	private int current;

	// 总页数
	private int totalPage;

	// 承载数据
	private List<T> list;

	//是否有上一页
	private boolean hasPrev;

	//是否有下一页
	private boolean hasNext;

	//是否为第一页
	private boolean first;

	//是否为最后一页
	private boolean last;

	public Page(int pageNo, int pageSize) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}

	public Page(List<T> list,long totalRecord, int page, int pageSize) {
		this.pageNo = page;
		if (page < 0)
			this.pageNo = 1;
		this.pageSize = pageSize;
		if (pageSize < 0)
			this.pageSize = 1;
		this.current = list.size();
		this.list = list;
		this.total =totalRecord;
		int totalPageNum = (int) this.total / this.pageSize;
		this.totalPage = (this.total % this.pageSize > 0) ? totalPageNum + 1
				: totalPageNum;
		if(pageNo>totalPage){
			pageNo = totalPage;
		}
		if(pageNo>1){
			this.hasPrev = true;
		}
		if(pageNo<this.totalPage){
			this.hasNext = true;
		}
		if(pageNo==1){
			this.first = true;
		}
		if(pageNo == totalPage){
			this.last = true;
		}
	}

	public int getPageNo() {
		return pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public List<T> getList() {
		return list;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public boolean getHasPrev() {
		return hasPrev;
	}

	public boolean getHasNext() {
		return hasNext;
	}

	public boolean getFirst() {
		return first;
	}

	public boolean getLast() {
		return last;
	}


}
