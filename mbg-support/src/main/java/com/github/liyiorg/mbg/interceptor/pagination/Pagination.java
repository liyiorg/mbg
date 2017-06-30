package com.github.liyiorg.mbg.interceptor.pagination;

import java.util.ArrayList;
import java.util.List;

import com.github.liyiorg.mbg.bean.Page;

/**
 * MyBatis 分页拦截工具类
 * @author LiYi
 *
 */
public class Pagination {

	private static ThreadLocal<Limit> threadLocal = new ThreadLocal<Limit>();

	public static void pagination(int pageNo, int pageSize) {
		Limit limit = new Limit();
		limit.setPageNo(pageNo);
		limit.setPageSize(pageSize);
		set(limit);
	}

	/**
	 * 清空分页执行状态
	 */
	public static void clear() {
		threadLocal.set(null);
	}
	
	protected static Limit get() {
		return threadLocal.get();
	}
	
	protected static void set(Limit limit) {
		threadLocal.set(limit);
	}

	/**
	 * 获取总记录数，并清空分页执行状态
	 * @return long
	 */
	public static long total() {
		Limit limit = threadLocal.get();
		long total = 0;
		if (limit != null) {
			total = limit.getTotal();
		}
		clear();
		return total;
	}

	/**
	 * 获取Page 对象，并清空分页执行状态
	 * @param list list
	 * @return Page
	 */
	public static <T> Page<T> totalPage(List<T> list) {
		Limit limit = threadLocal.get();
		long total = 0;
		if (limit != null) {
			total = limit.getTotal();
		}
		clear();
		return new Page<T>(list == null ? new ArrayList<T>() : list, total, limit.getPageNo(), limit.getPageSize());
	}

	protected static class Limit {
		// 当前页码
		private int pageNo;

		// 每页展现多少条记录
		private int pageSize;

		// 总记录条数
		private long total;

		public int getPageNo() {
			return pageNo;
		}

		public void setPageNo(int pageNo) {
			this.pageNo = pageNo;
		}

		public int getPageSize() {
			return pageSize;
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

	}
}
