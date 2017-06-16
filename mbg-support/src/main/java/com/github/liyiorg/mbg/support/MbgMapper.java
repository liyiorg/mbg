package com.github.liyiorg.mbg.support;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface MbgMapper<Model, Example, PrimaryKey> {

	long countByExample(Example example);

	int deleteByExample(Example example);

	int deleteByPrimaryKey(PrimaryKey id);

	int insert(Model record);

	int insertSelective(Model record);

	List<Model> selectByExample(Example example);

	Model selectByPrimaryKey(PrimaryKey id);

	int updateByExampleSelective(@Param("record") Model record,@Param("example") Example example);

	int updateByExample(@Param("record") Model record,@Param("example") Example example);

	int updateByPrimaryKeySelective(Model record);

	int updateByPrimaryKey(Model record);
	
}
