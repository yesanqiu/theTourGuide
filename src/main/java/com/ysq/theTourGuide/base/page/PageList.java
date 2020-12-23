package com.ysq.theTourGuide.base.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PageList<E> implements Serializable {

	private static final long serialVersionUID = -629727493103397326L;

	private List<E> data = new ArrayList<E>();

	private int pageSize;

	private int pageNum;

	private int totalItems;

	private int totalPages;

	public PageList() {
	}

	public PageList(Collection<? extends E> c) {
		data.addAll(c);
	}

	public PageList(int pageNum, int pageSize, int totalItems) {
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		this.totalItems = totalItems;
		countTotalPages();
	}

	public PageList(Collection<? extends E> c, int pageNum, int pageSize, int totalItems) {
		data.addAll(c);
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		this.totalItems = totalItems;
		countTotalPages();
	}

	public PageList(Paginator p) {
		this.pageNum = p.getPageNum();
		this.pageSize = p.getPageSize();
		this.totalItems = p.getTotalItems();
		countTotalPages();
	}

	public PageList(Collection<? extends E> c, Paginator p) {
		data.addAll(c);
		this.pageNum = p.getPageNum();
		this.pageSize = p.getPageSize();
		this.totalItems = p.getTotalItems();
		countTotalPages();
	}

	public void add(E e) {
		data.add(e);
	}

	public void addAll(Collection<? extends E> c) {
		data.addAll(c);
	}

	private void countTotalPages() {
		if (totalItems == 0 || pageSize == 0) {
			totalPages = 1;
		} else if (totalItems % pageSize == 0) {
			totalPages = totalItems / pageSize;
		} else {
			totalPages = totalItems / pageSize + 1;
		}
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(int totalItems) {
		this.totalItems = totalItems;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public List<E> getData() {
		return data;
	}

	public void setData(List<E> data) {
		this.data = data;
	}
}
