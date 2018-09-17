package com.github.liyiorg.mbg.support.example;

/**
 * 
 * @author LiYi
 *
 */
public class CItem {

	private static final String D = ".";

	private int type;

	private int jdbcType;

	private boolean delimited;

	private String columnName;

	private String alias;

	private String beginningDelimiter;

	private String endingDelimiter;

	/**
	 * 
	 * @param type
	 *            type [1,2,3]
	 * @param jdbcType
	 *            jdbcType
	 * @param delimited
	 *            delimited
	 * @param columnName
	 *            columnName
	 * @param alias
	 *            alias
	 * @param beginningDelimiter
	 *            beginningDelimiter [",`, others]
	 * @param endingDelimiter
	 *            endingDelimiter [",`, others]
	 */
	public CItem(int type, int jdbcType, boolean delimited, String columnName, String alias, String beginningDelimiter,
			String endingDelimiter) {
		this.type = type;
		this.jdbcType = jdbcType;
		this.delimited = delimited;
		this.columnName = columnName;
		this.alias = alias;
		this.beginningDelimiter = beginningDelimiter;
		this.endingDelimiter = endingDelimiter;
	}

	public int getType() {
		return type;
	}

	public int getJdbcType() {
		return jdbcType;
	}

	public boolean isDelimited() {
		return delimited;
	}

	public String delimitedName() {
		if (delimited) {
			return beginningDelimiter + columnName + endingDelimiter;
		}
		return columnName;
	}

	public String delimitedAliasName() {
		if (alias == null) {
			return delimitedName();
		}
		return alias + D + delimitedName();
	}
}
