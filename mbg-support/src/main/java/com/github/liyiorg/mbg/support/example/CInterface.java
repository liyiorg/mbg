package com.github.liyiorg.mbg.support.example;

/**
 * 
 * @author LiYi
 *
 */
public interface CInterface {

	int getType();

	int getJdbcType();

	boolean isDelimited();

	String name();
	
	String delimitedName();

	String delimitedAliasName();

}
