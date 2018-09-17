package com.github.liyiorg.mbg.support.example;

/**
 * 
 * @author LiYi
 *
 */
public interface PaginationAble {

	void setLimitStart(Long limitStart);

	Long getLimitStart();

	void setLimitEnd(Long limitEnd);

	Long getLimitEnd();

	void limit(Long limitStart, Long limitEnd);

	void setOrderByClause(String orderByClause);

	String getOrderByClause();

	String getDatabaseType();

	void setDatabaseType(String databaseType);
}
