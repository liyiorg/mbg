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
public interface MbgUpdateBLOBsMapper<Model, Example, PrimaryKey> extends MbgUpdateMapper<Model, Example, PrimaryKey> {

	int updateByExampleWithBLOBs(@Param("record") Model record, @Param("example") Example example);

	int updateByPrimaryKeyWithBLOBs(Model record);

}
