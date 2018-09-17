package com.github.liyiorg.mbg.support.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * 
 * @author LiYi
 *
 * @param <PrimaryKey> PrimaryKey
 * @param <Model> Model
 * @param <ModelWithBLOBs> ModelWithBLOBs
 * @param <Example> Example
 */
public interface MbgWriteBLOBsMapper<PrimaryKey, Model, ModelWithBLOBs, Example> extends MbgWriteMapper<PrimaryKey, Model, ModelWithBLOBs, Example> {

	int updateByExampleWithBLOBs(@Param("record") ModelWithBLOBs record, @Param("example") Example example);

	int updateByPrimaryKeyWithBLOBs(ModelWithBLOBs record);

}
