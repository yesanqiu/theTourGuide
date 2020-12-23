package com.ysq.theTourGuide.base.page;

import java.io.Serializable;

public class Paginator implements Serializable {

	private static final long serialVersionUID = -1727737904978979805L;

	private int pageSize;

	private int pageNum;

	private int totalItems;

	public Paginator(int pageNum, int pageSize, int totalItems) {
		super();
		this.pageSize = pageSize;
		this.totalItems = totalItems;
		this.pageNum = computePageNo(pageNum);
	}

	private static int computeLastPageNumber(int totalItems, int pageSize) {
		if (pageSize <= 0)
			return 1;
		int result = (int) (totalItems % pageSize == 0 ? totalItems / pageSize : totalItems / pageSize + 1);
		if (result <= 1)
			result = 1;
		return result;
	}

	private static int computePageNumber(int page, int pageSize, int totalItems) {
		if (page <= 1) {
			return 1;
		}
		if (Integer.MAX_VALUE == page || page > computeLastPageNumber(totalItems, pageSize)) {
			return computeLastPageNumber(totalItems, pageSize);
		}
		return page;
	}

	/**
	 * The total number of items made.
	 *
	 * @return total
	 */
	public int getTotalItems() {
		return totalItems;
	}

	/**
	 * Whether it is home page (the first page), the first page number is 1
	 *
	 * @return Boolean
	 */
	public boolean isFirstPage() {
		return pageNum <= 1;
	}

	/**
	 * Whether it is the last page
	 *
	 * @return Boolean
	 */
	public boolean isLastPage() {
		return pageNum >= getTotalPages();
	}

	public int getPrePage() {
		if (isHasPrePage()) {
			return pageNum - 1;
		} else {
			return pageNum;
		}
	}

	public int getNextPage() {
		if (isHasNextPage()) {
			return pageNum + 1;
		} else {
			return pageNum;
		}
	}

	/**
	 * The specified page numbers are forbidden to judge, that is beyond the scope of the specified number or equal to the current page.
	 *
	 * @param page
	 * @return boolean
	 */
	public boolean isDisabledPage(int pageNum) {
		return ((pageNum < 1) || (pageNum > getTotalPages()) || (pageNum == this.pageNum));
	}

	/**
	 * If there is a page
	 *
	 * @return boolean
	 */
	public boolean isHasPrePage() {
		return (pageNum - 1 >= 1);
	}

	/**
	 * If there is a next page
	 *
	 * @return boolean
	 */
	public boolean isHasNextPage() {
		return (pageNum + 1 <= getTotalPages());
	}

	/**
	 * The start line, can be used for Oracle page using the (1-based).
	 **/
	public int getStartRow() {
		if (getPageSize() <= 0 || totalItems <= 0)
			return 0;
		return pageNum > 0 ? (pageNum - 1) * getPageSize() + 1 : 0;
	}

	/**
	 * End of line, can be used for Oracle page using the (1-based).
	 **/
	public int getEndRow() {
		return pageNum > 0 ? Math.min(pageSize * pageNum, getTotalItems()) : 0;
	}

	/**
	 * Offset, counting from the beginning of 0, can be used for MySQL page using the (0-based)
	 **/
	public int getOffset() {
		return pageNum > 0 ? (pageNum - 1) * getPageSize() : 0;
	}

	/**
	 * Limit, can be used for MySQL page using the (0-based)
	 **/
	public int getLimit() {
		if (pageNum > 0) {
			return Math.min(pageSize * pageNum, getTotalItems()) - (pageSize * (pageNum - 1));
		} else {
			return 0;
		}
	}

	/**
	 * To get the total number of pages
	 *
	 * @return
	 */
	public int getTotalPages() {
		if (totalItems <= 0) {
			return 0;
		}
		if (pageSize <= 0) {
			return 0;
		}

		int count = totalItems / pageSize;
		if (totalItems % pageSize > 0) {
			count++;
		}
		return count;
	}

	protected int computePageNo(int page) {
		return computePageNumber(page, pageSize, totalItems);
	}

	public String toString() {
		return "page:" + pageNum + " pageSize:" + pageSize + " totalItems:" + totalItems;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

}
