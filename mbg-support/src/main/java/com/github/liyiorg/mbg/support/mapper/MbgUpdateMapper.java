package com.github.liyiorg.mbg.support.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * 
 * @author LiYi
 *
 * @param <Model> Model
 * @param <Example> Example
 * @param <PrimaryKey> PrimaryKey
 */
public interface MbgUpdateMapper<Model, Example, PrimaryKey> extends MbgMapper<Model, Example, PrimaryKey> {

	int deleteByExample(Example example);

	int deleteByPrimaryKey(PrimaryKey id);

	int insert(Model record);

	int insertSelective(Model record);

	int updateByExampleSelective(@Param("record") Model record, @Param("example") Example example);

	int updateByExample(@Param("record") Model record, @Param("example") Example example);

	int updateByPrimaryKeySelective(Model record);

	int updateByPrimaryKey(Model record);

}
