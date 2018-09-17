package com.github.liyiorg.mbg.bean;

public class SqlImprovement {

	public static final int MySQL_REPLACE = 1;

	public static final int MySQL_ON_DUPLICATE_KEY_UPDATE = 2;
	
	public static final int PostgreSQL_ON_CONFLICT_DO_UPDATE = 3;
	
	public static final int PostgreSQL_ON_CONFLICT_DO_NOTHING = 4;
	
	public static final int Oracle_MERGE_INTO_USING_DUAL = 5;
}
